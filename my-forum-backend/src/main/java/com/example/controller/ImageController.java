package com.example.controller;

import com.example.entity.RestBean;
import com.example.service.ImageService;
import com.example.utils.Const;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Jay Wong
 * @date 2023/11/3
 */
@Slf4j
@RestController
@RequestMapping("/api/image")
public class ImageController {
    @Resource
    private ImageService imageService;

    @PostMapping("/avatar")
    public RestBean<String> uploadAvatar(@RequestParam("file") MultipartFile file,
                                         @RequestAttribute(Const.ATTR_USER_ID) int id,
                                         HttpServletResponse response) throws IOException {
        if (file.getSize() > 1024 * 300) {
            return RestBean.failure(400, "The uploaded avatar must not exceed 300KB.");
        }
        log.info("Uploading avatar...");
        String imageUrl = imageService.uploadAvatar(file, id);
        if (imageUrl == null) {
            log.info("Avatar upload failed.");
            response.setStatus(400);
            return RestBean.failure(400, "Avatar upload failed.");
        } else {
            log.info("Avatar upload successful.");
            return RestBean.success("Avatar upload successful.", imageUrl);
        }
    }

    @PostMapping("/cache")
    public RestBean<String> uploadImage(@RequestParam("file") MultipartFile file,
                                        @RequestAttribute(Const.ATTR_USER_ID) int id,
                                        HttpServletResponse response) throws IOException {
        if (file.getSize() > 1024 * 1024 * 5) {
            return RestBean.failure(400, "The uploaded image must not exceed 5MB.");
        }
        log.info("Uploading image...");
        String imageUrl = imageService.uploadImage(file, id);
        if (imageUrl == null) {
            log.info("Image upload failed.");
            response.setStatus(400);
            return RestBean.failure(400, "Image upload failed.");
        } else {
            log.info("Image upload successful.");
            return RestBean.success("Image upload successful.", imageUrl);
        }
    }
}
