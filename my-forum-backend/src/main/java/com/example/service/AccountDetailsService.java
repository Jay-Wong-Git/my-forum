package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.AccountDetails;
import com.example.entity.vo.request.DetailsSaveVO;

/**
 * @author Jay Wong
 * @date 2023/10/31
 */
public interface AccountDetailsService extends IService<AccountDetails> {
    AccountDetails findAccountDetailsById(int id);
    boolean saveAccountDetails(int id, DetailsSaveVO vo);


}
