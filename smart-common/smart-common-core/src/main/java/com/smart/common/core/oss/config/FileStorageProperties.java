package com.smart.common.core.oss.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件存储配置属性
 * 定义文件存储相关的配置属性
 * 
 * @author AI Assistant
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@Component
@ConfigurationProperties(prefix = "file.storage")
public class FileStorageProperties {
    
    /**
     * 存储类型（minio、oss、local）
     */
    private String type = "minio";
    
    /**
     * MinIO配置
     */
    private MinioProperties minio = new MinioProperties();
    
    /**
     * OSS配置
     */
    private OssProperties oss = new OssProperties();
    
    /**
     * 本地存储配置
     */
    private LocalProperties local = new LocalProperties();
    
    /**
     * MinIO配置属性
     */
    @Data
    public static class MinioProperties {
        /**
         * MinIO访问域名
         */
        private String endpoint;
        
        /**
         * MinIO访问密钥
         */
        private String accessKey;
        
        /**
         * MinIO密钥
         */
        private String secretKey;
        
        /**
         * MinIO存储桶名称
         */
        private String bucket;
    }
    
    /**
     * OSS配置属性
     */
    @Data
    public static class OssProperties {
        /**
         * OSS访问域名
         */
        private String endpoint;
        
        /**
         * OSS访问密钥ID
         */
        private String accessKeyId;
        
        /**
         * OSS访问密钥Secret
         */
        private String accessKeySecret;
        
        /**
         * OSS存储桶名称
         */
        private String bucket;
    }
    
    /**
     * 本地存储配置属性
     */
    @Data
    public static class LocalProperties {
        /**
         * 本地存储路径
         */
        private String uploadPath = "/uploads";
        
        /**
         * 文件访问URL前缀
         */
        private String accessUrl = "http://localhost:9123/files";
    }
}
