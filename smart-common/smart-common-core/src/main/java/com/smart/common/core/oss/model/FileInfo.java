package com.smart.common.core.oss.model;

import lombok.Data;

import java.util.Date;

/**
 * 文件信息模型
 * 封装文件的详细信息
 * 
 * @author AI Assistant
 * @version 1.0
 * @since 2024-01-01
 */
@Data
public class FileInfo {
    
    /**
     * 文件名
     */
    private String fileName;
    
    /**
     * 原始文件名
     */
    private String originalFileName;
    
    /**
     * 文件内容类型
     */
    private String contentType;
    
    /**
     * 文件大小（字节）
     */
    private long fileSize;
    
    /**
     * 文件访问URL
     */
    private String fileUrl;
    
    /**
     * 存储桶名称
     */
    private String bucketName;
    
    /**
     * 上传时间
     */
    private Date uploadTime;
    
    /**
     * 上传人
     */
    private String uploadBy;
    
    /**
     * 上传人姓名
     */
    private String uploadByName;
    
    /**
     * 文件MD5值
     */
    private String md5;
    
    /**
     * 文件扩展名
     */
    private String extension;
    
    /**
     * 存储类型（minio、oss、local）
     */
    private String storageType;
    
    /**
     * 文件状态（active、deleted）
     */
    private String status;
}
