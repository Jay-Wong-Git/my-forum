package com.example.config;

import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jay Wong
 * @date 2023/11/3
 */
@Slf4j
@Configuration
public class MinioConfiguration {
    @Value("${minio.endpoint}")
    private String endpoint;
    @Value("${minio.username}")
    private String username;
    @Value("${minio.password}")
    private String password;

    @Bean
    public MinioClient minioClient() {
        log.info("Init minio client...");
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(username, password)
                .build();
    }
}
