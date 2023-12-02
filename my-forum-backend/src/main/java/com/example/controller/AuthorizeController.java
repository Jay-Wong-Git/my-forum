package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.request.ConfirmResetVO;
import com.example.entity.vo.request.EmailRegisterVO;
import com.example.entity.vo.request.EmailResetVO;
import com.example.service.AccountService;
import com.example.utils.ControllerUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Jay Wong
 * @date 2023/10/31
 */
@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthorizeController {
    @Resource
    private AccountService accountService;
    @Resource
    private ControllerUtils controllerUtils;

    @GetMapping("/ask-code")
    public RestBean<Void> askVerifyCode(@RequestParam @Email String email,
                                        @RequestParam @Pattern(regexp = "register|reset|modify") String type,
                                        HttpServletRequest request) {
        return controllerUtils.messageHandler(() ->
                accountService.applyEmailVerifyCode(type, email, request.getRemoteAddr()));
    }

    @PostMapping("/register")
    public RestBean<Void> register(@RequestBody @Valid EmailRegisterVO vo) {
        return controllerUtils.messageHandler(vo, accountService::registerEmailAccount);
    }

    @PostMapping("/reset-confirm")
    public RestBean<Void> resetConfirm(@RequestBody @Valid ConfirmResetVO vo) {
        return controllerUtils.messageHandler(vo, accountService::resetConfirm);
    }

    @PostMapping("/reset-password")
    public RestBean<Void> restPassword(@RequestBody @Valid EmailResetVO vo) {
        return controllerUtils.messageHandler(vo, accountService::resetEmailAccountPassword);
    }
}
