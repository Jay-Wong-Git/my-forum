package com.example.entity.vo.request;

import com.alibaba.fastjson2.JSONObject;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author Jay Wong
 * @date 2023/11/4
 */
@Data
public class TopicCreateVO {
    @Min(1)
    private Integer type;
    @Length(min = 1, max = 30)
    private String title;
    private JSONObject content;
}
