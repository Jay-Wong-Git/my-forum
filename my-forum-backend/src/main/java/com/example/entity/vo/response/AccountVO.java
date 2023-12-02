package com.example.entity.vo.response;

import lombok.Data;

import java.util.Date;

/**
 * @author Jay Wong
 * @date 2023/11/2
 */
@Data
public class AccountVO {
    private Integer id;
    private String username;
    private String email;
    private String role;
    private String avatar;
    private Date registerTime;
}
