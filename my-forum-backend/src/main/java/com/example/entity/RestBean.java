package com.example.entity;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;

/**
 * @author Jay Wong
 * @date 2023/10/30
 */
public record RestBean<T>(int code, String message, T data) {
    public static <T> RestBean<T> success(T data) {
        return new RestBean<>(200, "Request successful.", data);
    }

    public static <T> RestBean<T> success() {
        return success(null);
    }

    public static <T> RestBean<T> success(String message, T data) {
        return new RestBean<>(200, message, data);
    }

    public static <T> RestBean<T> success(String message) {
        return success(message, null);
    }

    public static <T> RestBean<T> failure(int code, String message) {
        return new RestBean<>(code, message, null);
    }

    // convert to JSON string
    public String toJSONString() {
        return JSONObject.toJSONString(this, JSONWriter.Feature.WriteNulls);
    }
}
