package com.smart.framework.redis.exception;

/**
 * 缓存异常
 * 当缓存操作失败时抛出
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public class CacheException extends RedisException {

    /**
     * 构造函数
     * 
     * @param message 错误消息
     */
    public CacheException(String message) {
        super("CACHE_ERROR", message);
    }

    /**
     * 构造函数
     * 
     * @param message 错误消息
     * @param cause 原因异常
     */
    public CacheException(String message, Throwable cause) {
        super("CACHE_ERROR", message, cause);
    }

    /**
     * 构造函数
     * 
     * @param operation 操作类型
     * @param key 缓存key
     * @param cause 原因异常
     */
    public CacheException(String operation, String key, Throwable cause) {
        super("CACHE_ERROR", 
            String.format("Cache %s operation failed: key=%s", operation, key), cause);
    }
}
