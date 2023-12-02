package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.AccountPrivacy;
import com.example.entity.vo.request.PrivacySaveVO;

/**
 * @author Jay Wong
 * @date 2023/10/31
 */
public interface AccountPrivacyService extends IService<AccountPrivacy> {
    void savePrivacy(int id, PrivacySaveVO vo);

    AccountPrivacy getPrivacy(int id);
}
