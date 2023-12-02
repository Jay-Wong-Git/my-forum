package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author Jay Wong
 * @date 2023/11/6
 */
@Data
@TableName("db_topic_comment")
public class TopicComment {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer uid;
    private Integer tid;
    private String content;
    private Date time;
    private Integer quote;
}
