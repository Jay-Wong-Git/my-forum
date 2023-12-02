package com.example.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Jay Wong
 * @date 2023/11/5
 */
@Data
public class TopicDetailVO {
    private Integer id;
    private String title;
    private String content;
    private Integer type;
    private Date time;
    private User user;
    private Interact interact;
    private Long comments;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Interact {
        private Boolean like;
        private Boolean collect;
    }

    @Data
    public static class User {
        private Integer id;
        private String username;
        private String avatar;
        private String desc;
        private Integer gender;
        private String qq;
        private String wx;
        private String phone;
        private String email;
    }
}
