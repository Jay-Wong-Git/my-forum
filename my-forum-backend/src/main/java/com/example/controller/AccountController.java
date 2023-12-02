package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.Account;
import com.example.entity.dto.AccountDetails;
import com.example.entity.vo.request.ChangePasswordVO;
import com.example.entity.vo.request.DetailsSaveVO;
import com.example.entity.vo.request.ModifyEmailVO;
import com.example.entity.vo.request.PrivacySaveVO;
import com.example.entity.vo.response.AccountDetailsVO;
import com.example.entity.vo.response.AccountPrivacyVO;
import com.example.entity.vo.response.AccountVO;
import com.example.service.AccountDetailsService;
import com.example.service.AccountPrivacyService;
import com.example.service.AccountService;
import com.example.utils.Const;
import com.example.utils.ControllerUtils;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author Jay Wong
 * @date 2023/11/2
 */
@RestController
@RequestMapping("/api/user")
public class AccountController {
    @Resource
    private AccountService accountService;
    @Resource
    private AccountDetailsService accountDetailsService;
    @Resource
    private AccountPrivacyService accountPrivacyService;
    @Resource
    private ControllerUtils controllerUtils;

    @GetMapping("/info")
    public RestBean<AccountVO> info(@RequestAttribute(Const.ATTR_USER_ID) int id) {
        
        Account account = accountService.getById(id);
        if (account == null) {
            return RestBean.failure(401, "User does not exist.");
        } else {
            AccountVO accountVO = account.asViewObject(AccountVO.class);
            return RestBean.success(accountVO);
        }
    }

    @GetMapping("/details")
    public RestBean<AccountDetailsVO> details(@RequestAttribute(Const.ATTR_USER_ID) int id) {
        AccountDetails details = Optional
                .ofNullable(accountDetailsService.findAccountDetailsById(id))
                .orElseGet(AccountDetails::new);
        return RestBean.success(details.asViewObject(AccountDetailsVO.class));
    }

    @PostMapping("/save-details")
    public RestBean<Void> saveDetails(@RequestAttribute(Const.ATTR_USER_ID) int id,
                                      @RequestBody @Valid DetailsSaveVO vo) {
        boolean success = accountDetailsService.saveAccountDetails(id, vo);
        return success ? RestBean.success() : RestBean.failure(400, "此用户名已被其他用户使用，请重新更换！");
    }

    @PostMapping("/modify-email")
    public RestBean<Void> modifyEmail(@RequestAttribute(Const.ATTR_USER_ID) int id,
                                      @RequestBody @Valid ModifyEmailVO vo) {
        return controllerUtils.messageHandler(() -> accountService.modifyEmail(id, vo));
    }

    @PostMapping("/change-password")
    public RestBean<Void> changePassword(@RequestAttribute(Const.ATTR_USER_ID) int id,
                                         @RequestBody @Valid ChangePasswordVO vo) {
        return controllerUtils.messageHandler(() -> accountService.changePassword(id, vo));
    }

    @PostMapping("save-privacy")
    public RestBean<Void> savePrivacy(@RequestAttribute(Const.ATTR_USER_ID) int id,
                                      @RequestBody @Valid PrivacySaveVO vo) {
        accountPrivacyService.savePrivacy(id, vo);
        return RestBean.success("Privacy status modified successfully.");
    }

    @GetMapping("privacy")
    public RestBean<AccountPrivacyVO> getPrivacy(@RequestAttribute(Const.ATTR_USER_ID) int id) {
        return RestBean.success(accountPrivacyService.getPrivacy(id).asViewObject(AccountPrivacyVO.class));
    }
}
