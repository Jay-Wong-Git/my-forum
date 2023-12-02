package com.example.entity.vo.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Jay Wong
 * @date 2023/11/4
 */
@Data
public class TopicPreviewVO {
    private Integer id;
    private Integer type;
    private String title;
    private String text;
    private List<String> images;
    private Date time;
    private Integer uid;
    private String username;
    private String avatar;
    private Integer like;
    private Integer collect;
}
