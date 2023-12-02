package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.ImageStore;
import com.example.mapper.ImageStoreMapper;
import com.example.service.AccountService;
import com.example.service.ImageService;
import com.example.utils.Const;
import com.example.utils.FlowUtils;
import io.minio.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author Jay Wong
 * @date 2023/11/3
 */
@Slf4j
@Service
public class ImageServiceImpl extends ServiceImpl<ImageStoreMapper, ImageStore> implements ImageService {
    @Resource
    private MinioClient minioClient;
    @Resource
    private AccountService accountService;
    @Resource
    private FlowUtils flowUtils;
    private final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

    @Override
    public String uploadAvatar(MultipartFile file, int id) throws IOException {
        String imageName = UUID.randomUUID().toString().replace("-", "");
        imageName = "/avatar/" + imageName;
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket("study")
                .stream(file.getInputStream(), file.getSize(), -1)
                .object(imageName)
                .build();
        try {
            minioClient.putObject(args);
            String avatar = accountService.getById(id).getAvatar();
            this.deleteOldAvatar(avatar);
            boolean update = accountService.update().eq("id", id).set("avatar", imageName).update();
            if (update) {
                return imageName;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("upload avatar error" + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public String uploadImage(MultipartFile file, int id) throws IOException {
        String key = Const.FORUM_IMAGE_COUNTER + id;
        if (!flowUtils.limitPeriodCounterCheck(key, 20, 3600)) {
            return null;
        }
        String imageName = UUID.randomUUID().toString().replace("-", "");
        Date date = new Date();
        imageName = "/cache/" + format.format(date) + "/" + imageName;
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket("study")
                .stream(file.getInputStream(), file.getSize(), -1)
                .object(imageName)
                .build();
        try {
            minioClient.putObject(args);
            if (this.save(new ImageStore(id, imageName, date))) {
                return imageName;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("图片上传出现问题: " + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void fetchImageFromMinio(OutputStream stream, String imageName) {
        GetObjectArgs args = GetObjectArgs.builder()
                .bucket("study")
                .object(imageName)
                .build();
        try {
            GetObjectResponse response = minioClient.getObject(args);
            IOUtils.copy(response, stream);
        } catch (Exception e) {
            log.error("fetch image error" + e.getMessage(), e);
        }
    }

    private void deleteOldAvatar(String avatar) throws Exception {
        if (avatar == null || avatar.isEmpty()) {
            return;
        }
        RemoveObjectArgs remove = RemoveObjectArgs.builder()
                .bucket("study")
                .object(avatar)
                .build();
        minioClient.removeObject(remove);
    }
}
