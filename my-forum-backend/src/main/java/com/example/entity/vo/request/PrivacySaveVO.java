package com.example.entity.vo.request;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * @author Jay Wong
 * @date 2023/11/3
 */
@Data
public class PrivacySaveVO {
    @Pattern(regexp = "phone|wx|qq|email|gender")
    private String type;
    private boolean status;
}
