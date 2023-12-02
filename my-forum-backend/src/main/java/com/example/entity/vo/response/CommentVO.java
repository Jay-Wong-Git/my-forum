package com.example.entity.vo.response;

import lombok.Data;

import java.util.Date;

/**
 * @author Jay Wong
 * @date 2023/11/6
 */
@Data
public class CommentVO {
    private Integer id;
    private String content;
    private Date time;
    private String quote;
    private User user;

    @Data
    public static class User {
        private Integer id;
        private String username;
        private String avatar;
        private Integer gender;
        private String qq;
        private String wx;
        private String phone;
        private String email;
    }
}
