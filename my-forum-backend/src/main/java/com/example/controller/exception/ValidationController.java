package com.example.controller.exception;

import com.example.entity.RestBean;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Jay Wong
 * @date 2023/11/1
 */
@Slf4j
@RestControllerAdvice
public class ValidationController {
    @ExceptionHandler({ValidationException.class, MethodArgumentNotValidException.class})
    public RestBean<Void> validationException(Exception exception) {
        log.warn("Resolve [{}: {}]", exception.getClass().getName(), exception.getMessage());
        return RestBean.failure(400, "Invalid request parameter.");
    }
}
