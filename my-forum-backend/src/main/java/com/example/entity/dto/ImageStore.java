package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Jay Wong
 * @date 2023/11/4
 */
@Data
@TableName("db_image_store")
@AllArgsConstructor
@NoArgsConstructor
public class ImageStore {
    private Integer uid;
    private String name;
    private Date time;
}
