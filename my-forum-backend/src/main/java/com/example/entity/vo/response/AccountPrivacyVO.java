package com.example.entity.vo.response;

import lombok.Data;

/**
 * @author Jay Wong
 * @date 2023/11/3
 */
@Data
public class AccountPrivacyVO {
    private Boolean phone = true;
    private Boolean wx = true;
    private Boolean qq = true;
    private Boolean email = true;
    private Boolean gender = true;
}
