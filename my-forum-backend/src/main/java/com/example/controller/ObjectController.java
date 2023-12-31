package com.example.controller;

import com.example.entity.RestBean;
import com.example.service.ImageService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author Jay Wong
 * @date 2023/11/3
 */
@Slf4j
@RestController
public class ObjectController {
    @Resource
    private ImageService imageService;

    @GetMapping("/images/**")
    public void imageFetch(HttpServletRequest request,
                           HttpServletResponse response) throws IOException {
        response.setHeader("Content-Type", "image/jpg");
        this.fetchImage(request, response);
    }

    private void fetchImage(HttpServletRequest request,
                            HttpServletResponse response) throws IOException {
        String imageName = request.getServletPath().substring(7);
        ServletOutputStream stream = response.getOutputStream();
        if (imageName.length() <= 13) {
            response.setStatus(404);
            stream.println(RestBean.failure(404, "Not found").toString());
        } else {
            try {
                imageService.fetchImageFromMinio(stream, imageName);
                response.setHeader("Cache-Control", "max-age=2592000");
            } catch (Exception e) {
                log.error("从Minio获取图片出现异常: " + e.getMessage(), e);
            }
        }
    }
}
