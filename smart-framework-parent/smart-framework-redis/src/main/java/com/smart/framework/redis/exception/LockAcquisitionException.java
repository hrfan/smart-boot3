package com.smart.framework.redis.exception;

/**
 * 锁获取异常
 * 当无法获取分布式锁时抛出
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public class LockAcquisitionException extends RedisException {

    /**
     * 构造函数
     * 
     * @param message 错误消息
     */
    public LockAcquisitionException(String message) {
        super("LOCK_ACQUISITION_ERROR", message);
    }

    /**
     * 构造函数
     * 
     * @param message 错误消息
     * @param cause 原因异常
     */
    public LockAcquisitionException(String message, Throwable cause) {
        super("LOCK_ACQUISITION_ERROR", message, cause);
    }

    /**
     * 构造函数
     * 
     * @param lockKey 锁的key
     * @param timeout 超时时间
     */
    public LockAcquisitionException(String lockKey, long timeout) {
        super("LOCK_ACQUISITION_ERROR", 
            String.format("Failed to acquire lock: key=%s, timeout=%d", lockKey, timeout));
    }
}
