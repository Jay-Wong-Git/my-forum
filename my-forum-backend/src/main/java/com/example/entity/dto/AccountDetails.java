package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jay Wong
 * @date 2023/11/3
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("db_account_details")
public class AccountDetails implements BaseData {
    private Integer id;
    private Integer gender;
    private String phone;
    private String qq;
    private String wx;
    @TableField("`desc`")
    private String desc;
}
