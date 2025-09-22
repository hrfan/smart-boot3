package com.smart.common.file.exception;

/**
 * 文件存储异常
 * 文件存储操作相关的自定义异常
 * 
 * @author AI Assistant
 * @version 1.0
 * @since 2024-01-01
 */
public class FileStorageException extends RuntimeException {
    
    /**
     * 错误码
     */
    private String errorCode;
    
    /**
     * 构造函数
     * @param message 错误消息
     */
    public FileStorageException(String message) {
        super(message);
    }
    
    /**
     * 构造函数
     * @param errorCode 错误码
     * @param message 错误消息
     */
    public FileStorageException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    /**
     * 构造函数
     * @param message 错误消息
     * @param cause 原因异常
     */
    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * 构造函数
     * @param errorCode 错误码
     * @param message 错误消息
     * @param cause 原因异常
     */
    public FileStorageException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    /**
     * 获取错误码
     * @return 错误码
     */
    public String getErrorCode() {
        return errorCode;
    }
    
    /**
     * 设置错误码
     * @param errorCode 错误码
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
