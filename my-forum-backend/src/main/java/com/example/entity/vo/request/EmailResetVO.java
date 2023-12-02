package com.example.entity.vo.request;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author Jay Wong
 * @date 2023/11/1
 */
@Data
public class EmailResetVO {
    @Email
    private String email;
    @Length(min = 6, max = 6)
    private String code;
    @Length(min = 6, max = 20)
    private String password;
}
