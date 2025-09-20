package com.smart.common.redis.annotation;

import com.smart.common.redis.service.RateLimitService;

import java.lang.annotation.*;

/**
 * 限流注解
 * 用于在方法上添加限流功能
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {

    /**
     * 限流key
     * 支持SpEL表达式，如：#userId、#user.id等
     * 
     * @return 限流key
     */
    String key();

    /**
     * 限流算法
     * 
     * @return 限流算法
     */
    RateLimitService.RateLimitAlgorithm algorithm() default RateLimitService.RateLimitAlgorithm.TOKEN_BUCKET;

    /**
     * 限制数量
     * 
     * @return 限制数量
     */
    int limit() default 100;

    /**
     * 时间窗口（秒）
     * 
     * @return 时间窗口
     */
    int windowSize() default 60;

    /**
     * 令牌桶容量（仅用于TOKEN_BUCKET算法）
     * 
     * @return 令牌桶容量
     */
    int capacity() default 100;

    /**
     * 令牌桶填充速率（仅用于TOKEN_BUCKET算法）
     * 
     * @return 填充速率
     */
    int refillRate() default 10;

    /**
     * 请求令牌数（仅用于TOKEN_BUCKET算法）
     * 
     * @return 请求令牌数
     */
    int tokens() default 1;

    /**
     * 限流失败时的处理方式
     * 
     * @return 失败处理策略
     */
    RateLimitFailStrategy failStrategy() default RateLimitFailStrategy.THROW_EXCEPTION;

    /**
     * 限流失败处理策略枚举
     */
    enum RateLimitFailStrategy {
        /**
         * 抛出异常
         */
        THROW_EXCEPTION,
        
        /**
         * 返回null
         */
        RETURN_NULL,
        
        /**
         * 返回false
         */
        RETURN_FALSE,
        
        /**
         * 等待重试
         */
        WAIT_RETRY
    }
}
