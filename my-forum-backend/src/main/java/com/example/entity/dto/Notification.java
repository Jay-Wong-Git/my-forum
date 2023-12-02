package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseData;
import lombok.Data;

/**
 * @author Jay Wong
 * @date 2023/11/6
 */
@Data
@TableName("db_notification")
public class Notification implements BaseData {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer uid;
    private String title;
    private String content;
    private String type;
    private String url;
    private String time;
}
