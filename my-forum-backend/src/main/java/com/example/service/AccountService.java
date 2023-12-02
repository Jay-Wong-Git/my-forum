package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Account;
import com.example.entity.vo.request.*;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author Jay Wong
 * @date 2023/10/31
 */
public interface AccountService extends IService<Account>, UserDetailsService {
    Account findAccountByUsernameOrEmail(String text);
    String applyEmailVerifyCode(String type, String email, String ip);
    String registerEmailAccount(EmailRegisterVO vo);
    String resetConfirm(ConfirmResetVO vo);
    String resetEmailAccountPassword(EmailResetVO vo);
    String modifyEmail(int id, ModifyEmailVO vo);
    String changePassword(int id, ChangePasswordVO vo);
}
