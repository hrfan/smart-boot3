package com.smart.common.file.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * MinIO配置属性
 * 
 * @author AI Assistant
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@Component
@ConfigurationProperties(prefix = "file.storage.minio")
public class MinioInfo {
    
    /**
     * MinIO服务端点
     */
    private String endpoint;
    
    /**
     * 访问密钥
     */
    private String accessKey;
    
    /**
     * 秘密密钥
     */
    private String secretKey;
    
    /**
     * 存储桶名称
     */
    private String bucket;
}
