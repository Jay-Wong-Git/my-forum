package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.ImageStore;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Jay Wong
 * @date 2023/11/3
 */
public interface ImageService extends IService<ImageStore> {
    String uploadAvatar(MultipartFile file, int id) throws IOException;
    String uploadImage(MultipartFile file, int id) throws IOException;
    void fetchImageFromMinio(OutputStream stream, String imageName);
}
