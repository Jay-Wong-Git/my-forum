package com.example.entity.vo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author Jay Wong
 * @date 2023/11/1
 */
@Data
public class EmailRegisterVO {
    @Email
    private String email;
    @Length(min = 2, max = 20)
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]+$")
    private String username;
    @Length(min = 6, max = 6)
    private String code;
    @Length(min = 6, max = 20)
    private String password;
}
