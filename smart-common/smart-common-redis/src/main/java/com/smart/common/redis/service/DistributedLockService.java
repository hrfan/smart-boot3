package com.smart.common.redis.service;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁服务接口
 * 提供基于Redis的分布式锁功能
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public interface DistributedLockService {

    /**
     * 获取分布式锁
     * 
     * @param lockKey 锁的key
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return 锁对象
     */
    DistributedLock acquireLock(String lockKey, long timeout, TimeUnit unit);

    /**
     * 尝试获取锁
     * 
     * @param lockKey 锁的key
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return 是否获取成功
     */
    boolean tryLock(String lockKey, long timeout, TimeUnit unit);

    /**
     * 释放锁
     * 
     * @param lockKey 锁的key
     * @param lockValue 锁的值
     */
    void releaseLock(String lockKey, String lockValue);

    /**
     * 检查锁是否存在
     * 
     * @param lockKey 锁的key
     * @return 是否存在
     */
    boolean isLocked(String lockKey);

    /**
     * 获取锁的剩余过期时间
     * 
     * @param lockKey 锁的key
     * @return 剩余时间（毫秒）
     */
    long getLockTtl(String lockKey);
}
