package com.example.entity.vo.request;

import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * @author Jay Wong
 * @date 2023/11/6
 */
@Data
public class AddCommentVO {
    @Min(1)
    private Integer tid;
    private String content;
    @Min(-1)
    private Integer quote;
}
