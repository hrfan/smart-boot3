package com.smart.framework.redis.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁注解
 * 用于在方法上添加分布式锁功能
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {

    /**
     * 锁的key
     * 支持SpEL表达式，如：#userId、#user.id等
     * 
     * @return 锁的key
     */
    String key();

    /**
     * 锁的超时时间
     * 
     * @return 超时时间
     */
    long timeout() default 30;

    /**
     * 时间单位
     * 
     * @return 时间单位
     */
    TimeUnit unit() default TimeUnit.SECONDS;

    /**
     * 获取锁失败时的处理方式
     * 
     * @return 失败处理策略
     */
    LockFailStrategy failStrategy() default LockFailStrategy.THROW_EXCEPTION;

    /**
     * 锁失败处理策略枚举
     */
    enum LockFailStrategy {
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
