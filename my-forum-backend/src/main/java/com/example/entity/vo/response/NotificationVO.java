package com.example.entity.vo.response;

import lombok.Data;

import java.util.Date;

/**
 * @author Jay Wong
 * @date 2023/11/6
 */
@Data
public class NotificationVO {
    private Integer id;
    private String title;
    private String content;
    private String type;
    private String url;
    private Date time;
}
