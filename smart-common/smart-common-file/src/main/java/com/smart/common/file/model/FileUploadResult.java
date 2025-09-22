package com.smart.common.file.model;

import lombok.Data;

import java.util.Date;

/**
 * 文件上传结果模型
 * 封装文件上传操作的结果信息
 * 
 * @author AI Assistant
 * @version 1.0
 * @since 2024-01-01
 */
@Data
public class FileUploadResult {
    
    /**
     * 操作是否成功
     */
    private boolean success;
    
    /**
     * 结果消息
     */
    private String message;
    

    /**
     * 文件名
     */
    private String fileName;
    
    /**
     * 原始文件名
     */
    private String originalFileName;
    
    /**
     * 文件大小（字节）
     */
    private long fileSize;
    
    /**
     * 文件内容类型
     */
    private String contentType;
    
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
     * 错误码
     */
    private String errorCode;


    /**
     * 文件存储真实路径
     */
    private String filePath;


    /**
     * 创建成功结果
     * @return 成功结果
     */
    public static FileUploadResult success() {
        FileUploadResult result = new FileUploadResult();
        result.setSuccess(true);
        result.setUploadTime(new Date());
        return result;
    }
    
    /**
     * 创建成功结果
     * @param fileUrl 文件访问URL
     * @param fileName 文件名
     * @param fileSize 文件大小
     * @param contentType 内容类型
     * @return 成功结果
     */
    public static FileUploadResult success( String fileName, long fileSize, String contentType) {
        FileUploadResult result = new FileUploadResult();
        result.setSuccess(true);
        result.setFileName(fileName);
        result.setFileSize(fileSize);
        result.setContentType(contentType);
        result.setUploadTime(new Date());
        return result;
    }
    
    /**
     * 创建失败结果
     * @param message 错误消息
     * @return 失败结果
     */
    public static FileUploadResult failure(String message) {
        FileUploadResult result = new FileUploadResult();
        result.setSuccess(false);
        result.setMessage(message);
        result.setUploadTime(new Date());
        return result;
    }
    
    /**
     * 创建失败结果
     * @param errorCode 错误码
     * @param message 错误消息
     * @return 失败结果
     */
    public static FileUploadResult failure(String errorCode, String message) {
        FileUploadResult result = new FileUploadResult();
        result.setSuccess(false);
        result.setErrorCode(errorCode);
        result.setMessage(message);
        result.setUploadTime(new Date());
        return result;
    }
}
