package com.smart.common.redis.exception;

/**
 * 限流异常
 * 当请求超过限流阈值时抛出
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public class RateLimitExceededException extends RedisException {

    /**
     * 构造函数
     * 
     * @param message 错误消息
     */
    public RateLimitExceededException(String message) {
        super("RATE_LIMIT_EXCEEDED", message);
    }

    /**
     * 构造函数
     * 
     * @param message 错误消息
     * @param cause 原因异常
     */
    public RateLimitExceededException(String message, Throwable cause) {
        super("RATE_LIMIT_EXCEEDED", message, cause);
    }

    /**
     * 构造函数
     * 
     * @param key 限流key
     * @param limit 限制数量
     * @param current 当前数量
     */
    public RateLimitExceededException(String key, int limit, int current) {
        super("RATE_LIMIT_EXCEEDED", 
            String.format("Rate limit exceeded: key=%s, limit=%d, current=%d", key, limit, current));
    }
}
