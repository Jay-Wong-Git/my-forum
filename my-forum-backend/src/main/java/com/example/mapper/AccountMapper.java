package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.Account;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Jay Wong
 * @date 2023/10/31
 */
@Mapper
public interface AccountMapper extends BaseMapper<Account> {
}
