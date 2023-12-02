package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.AccountPrivacy;
import com.example.entity.vo.request.PrivacySaveVO;
import com.example.mapper.AccountPrivacyMapper;
import com.example.service.AccountPrivacyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Jay Wong
 * @date 2023/10/31
 */
@Service
public class AccountPrivacyServiceImpl extends ServiceImpl<AccountPrivacyMapper, AccountPrivacy> implements AccountPrivacyService {

    @Override
    @Transactional
    public void savePrivacy(int id, PrivacySaveVO vo) {
        AccountPrivacy accountPrivacy = Optional.ofNullable(this.getById(id)).orElse(new AccountPrivacy(id));
        boolean status = vo.isStatus();
        switch (vo.getType()) {
            case "phone" -> accountPrivacy.setPhone(status);
            case "wx" -> accountPrivacy.setWx(status);
            case "qq" -> accountPrivacy.setQq(status);
            case "email" -> accountPrivacy.setEmail(status);
            case "gender" -> accountPrivacy.setGender(status);
        }
        this.saveOrUpdate(accountPrivacy);
    }

    @Override
    public AccountPrivacy getPrivacy(int id) {
        return Optional.ofNullable(this.getById(id)).orElse(new AccountPrivacy(id));
    }
}
