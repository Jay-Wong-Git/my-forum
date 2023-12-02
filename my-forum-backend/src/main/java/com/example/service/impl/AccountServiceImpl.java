package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Account;
import com.example.entity.dto.AccountDetails;
import com.example.entity.dto.AccountPrivacy;
import com.example.entity.vo.request.*;
import com.example.mapper.AccountDetailsMapper;
import com.example.mapper.AccountMapper;
import com.example.mapper.AccountPrivacyMapper;
import com.example.service.AccountService;
import com.example.utils.Const;
import com.example.utils.FlowUtils;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author Jay Wong
 * @date 2023/10/31
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {
    @Resource
    private AmqpTemplate amqpTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private FlowUtils flowUtils;
    @Resource
    private AccountPrivacyMapper accountPrivacyMapper;
    @Resource
    private AccountDetailsMapper accountDetailsMapper;

    @Resource
    private BCryptPasswordEncoder encoder;
    private final Random random = new Random();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = findAccountByUsernameOrEmail(username);
        if (account == null) {
            throw new UsernameNotFoundException("Invalid username or password2.");
        }
        return User
                .withUsername(account.getUsername())
                .password(account.getPassword())
                .roles(account.getRole())
                .build();
    }

    @Override
    public Account findAccountByUsernameOrEmail(String text) {
        LambdaQueryWrapper<Account> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Account::getUsername, text).or().eq(Account::getEmail, text);
        return this.getOne(wrapper);
    }

    @Override
    public String applyEmailVerifyCode(String type, String email, String ip) {
        synchronized (ip.intern()) {
            if (!this.verifyLimit(ip)) {
                return "Request too frequently, try again later.";
            }
            String code = String.valueOf(100000 + random.nextInt(899999));
            Map<String, Object> data = Map.of("type", type, "email", email, "code", code);
            amqpTemplate.convertAndSend("mail", data);
            saveEmailVerifyCode(email, code);
            return null;
        }
    }

    @Override
    public String registerEmailAccount(EmailRegisterVO vo) {
        String email = vo.getEmail();
        String username = vo.getUsername();
        String code = getEmailVerifyCode(email);
        if (code == null) {
            return "Please request the validation code first.";
        }
        if (!code.equals(vo.getCode())) {
            return "Validation code is wrong.";
        }
        if (isEmailRegistered(email)) {
            return "The email has been registered.";
        }
        if (isUsernameTaken(username)) {
            return "The username has been taken";
        }
        Account account = new Account();
        account.setEmail(email);
        account.setUsername(username);
        account.setRole(Const.ROLE_DEFAULT);
        account.setPassword(encoder.encode(vo.getPassword()));
        account.setRegisterTime(new Date());
        if (!this.save(account)) {
            return "System internal error.";
        } else {
            deleteEmailVerifyCode(email);
            accountPrivacyMapper.insert(new AccountPrivacy(account.getId()));
            AccountDetails accountDetails = new AccountDetails();
            accountDetails.setId(account.getId());
            accountDetailsMapper.insert(accountDetails);
            return null;
        }
    }

    @Override
    public String resetConfirm(ConfirmResetVO vo) {
        String email = vo.getEmail();
        String code = getEmailVerifyCode(email);
        if (code == null) {
            return "Please request the validation code first.";
        }
        if (!code.equals(vo.getCode())) {
            return "Validation code is wrong.";
        }
        return null;
    }

    @Override
    public String resetEmailAccountPassword(EmailResetVO vo) {
        String email = vo.getEmail();
        String code = vo.getCode();
        String password = vo.getPassword();
        String verify = this.resetConfirm(new ConfirmResetVO(email, code));
        if (verify != null) {
            return verify;
        }
        boolean update = this.update().eq("email", email).set("password", encoder.encode(password)).update();
        if (!update) {
            return "System internal error";
        } else {
            deleteEmailVerifyCode(email);
            return null;
        }
    }

    @Override
    public String modifyEmail(int id, ModifyEmailVO vo) {
        String email = vo.getEmail();
        String code = getEmailVerifyCode(email);
        if (code == null) {
            return "Please request the validation code first.";
        }
        if (!code.equals(vo.getCode())) {
            return "Validation code is wrong.";
        }
        deleteEmailVerifyCode(email);
        Account account = this.findAccountByUsernameOrEmail(email);
        if (account != null && account.getId() != id) {
            return "The email has been bound with other account.";
        }
        this.update().set("email", email).eq("id", id).update();
        return null;
    }

    @Override
    public String changePassword(int id, ChangePasswordVO vo) {
        Account account = this.getById(id);
        String newPassword = encoder.encode(vo.getNewPassword());
        if (!encoder.matches(vo.getPassword(), account.getPassword())) {
            return "Current password is invalid.";
        }
        boolean isSucceed = this.update().set("password", newPassword).eq("id", id).update();
        return isSucceed ? null : "System internal error.";
    }

    // delete email verify code from redis
    private void deleteEmailVerifyCode(String email) {
        String key = Const.VERIFY_EMAIL_DATA_PREFIX + email;
        stringRedisTemplate.delete(key);
    }

    // get email verify code from redis
    private String getEmailVerifyCode(String email) {
        String key = Const.VERIFY_EMAIL_DATA_PREFIX + email;
        return stringRedisTemplate.opsForValue().get(key);
    }

    // save email verify code to redis
    private void saveEmailVerifyCode(String email, String code) {
        String key = Const.VERIFY_EMAIL_DATA_PREFIX + email;
        stringRedisTemplate.opsForValue().set(key, code, 3, TimeUnit.MINUTES);
    }

    // check whether specified email is registered
    private boolean isEmailRegistered(String email) {
        LambdaQueryWrapper<Account> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Account::getEmail, email);
        return this.count(wrapper) > 0;
    }

    // check whether specified username is taken
    private boolean isUsernameTaken(String username) {
        LambdaQueryWrapper<Account> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Account::getUsername, username);
        return this.count(wrapper) > 0;
    }

    private boolean verifyLimit(String ip) {
        return flowUtils.limitOnceCheck(Const.VERIFY_EMAIL_LIMIT_PREFIX + ip, 60);
    }
}
