package com.example.utils;

import com.example.entity.RestBean;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Jay Wong
 * @date 2023/11/3
 */
@Component
public class ControllerUtils {
    public <T> RestBean<Void> messageHandler(T vo, Function<T, String> function) {
        return messageHandler(() -> function.apply(vo));
    }

    public RestBean<Void> messageHandler(Supplier<String> supplier) {
        String message = supplier.get();
        return message == null ? RestBean.success("Process successful.") : RestBean.failure(400, message);
    }
}
