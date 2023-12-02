package com.example.entity.vo.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * @author Jay Wong
 * @date 2023/11/1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmResetVO {
    @Email
    private String email;
    @Length(min = 6, max = 6)
    private String code;
}
