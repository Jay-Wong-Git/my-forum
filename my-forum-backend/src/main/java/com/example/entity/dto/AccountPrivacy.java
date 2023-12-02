package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseData;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jay Wong
 * @date 2023/11/3
 */
@Data
@TableName("db_account_privacy")
public class AccountPrivacy implements BaseData {
    private final Integer id;
    private Boolean phone = false;
    private Boolean wx = false;
    private Boolean qq = false;
    private Boolean email = false;
    private Boolean gender = false;

    public String[] hiddenFields() {
        List<String> strings = new LinkedList<>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                if (field.getType().equals(Boolean.class) && !(Boolean) field.get(this)) {
                    strings.add(field.getName());
                }
            } catch (Exception ignored) {
            }
        }
        return strings.toArray(String[]::new);
    }
}
