package com.smart.common.redis.exception;

/**
 * Redis基础异常类
 * Redis模块所有异常的基类
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public class RedisException extends RuntimeException {

    /**
     * 错误代码
     */
    private final String errorCode;

    /**
     * 构造函数
     * 
     * @param message 错误消息
     */
    public RedisException(String message) {
        super(message);
        this.errorCode = "REDIS_ERROR";
    }

    /**
     * 构造函数
     * 
     * @param message 错误消息
     * @param cause 原因异常
     */
    public RedisException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "REDIS_ERROR";
    }

    /**
     * 构造函数
     * 
     * @param errorCode 错误代码
     * @param message 错误消息
     */
    public RedisException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * 构造函数
     * 
     * @param errorCode 错误代码
     * @param message 错误消息
     * @param cause 原因异常
     */
    public RedisException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * 获取错误代码
     * 
     * @return 错误代码
     */
    public String getErrorCode() {
        return errorCode;
    }
}
