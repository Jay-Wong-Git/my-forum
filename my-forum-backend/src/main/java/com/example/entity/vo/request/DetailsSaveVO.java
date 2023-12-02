package com.example.entity.vo.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author Jay Wong
 * @date 2023/11/3
 */
@Data
public class DetailsSaveVO {
    @Pattern(regexp = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$")
    @Length(min = 1, max = 10)
    private String username;
    @Min(0)
    @Max(1)
    private Integer gender;
    @Length(max = 11)
    private String phone;
    @Length(max = 13)
    private String qq;
    @Length(max = 20)
    private String wx;
    @Length(max = 200)
    private String desc;
}
