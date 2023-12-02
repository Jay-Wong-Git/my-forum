package com.example.entity.vo.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author Jay Wong
 * @date 2023/11/3
 */
@Data
public class ChangePasswordVO {
    @Length(min = 6, max = 20)
    private String password;
    @Length(min = 6, max = 20)
    private String newPassword;
}
