package com.example.entity.vo.response;

import lombok.Data;

import java.util.Date;

/**
 * @author Jay Wong
 * @date 2023/10/31
 */
@Data
public class AuthorizeVO {
    private String username;
    private String role;
    private String token;
    private Date expire;
}
