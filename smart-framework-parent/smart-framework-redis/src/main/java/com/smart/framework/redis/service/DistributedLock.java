package com.smart.framework.redis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 简化版分布式锁实体类
 * 提供基本的分布式锁功能，支持获取、释放、检查状态、续期等操作
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public class DistributedLock {

    private static final Logger log = LoggerFactory.getLogger(DistributedLock.class);

    /**
     * 锁的key
     */
    private final String lockKey;

    /**
     * 锁的值（UUID，用于标识锁的拥有者）
     */
    private final String lockValue;

    /**
     * 锁的过期时间（毫秒）
     */
    private final long expireTime;

    /**
     * Redis模板
     */
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * String Redis模板（用于Lua脚本执行）
     */
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 锁续期脚本
     */
    private static final String RENEW_SCRIPT =
            "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                    "return redis.call('expire', KEYS[1], ARGV[2]) " +
                    "else return 0 end";

    /**
     * 释放锁脚本
     */
    private static final String UNLOCK_SCRIPT =
            "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                    "return redis.call('del', KEYS[1]) " +
                    "else return 0 end";

    /**
     * 构造函数
     *
     * @param lockKey           锁的key
     * @param lockValue         锁的值
     * @param expireTime        过期时间
     * @param redisTemplate     Redis模板
     * @param stringRedisTemplate String Redis模板
     */
    public DistributedLock(String lockKey, String lockValue, long expireTime,
                           RedisTemplate<String, Object> redisTemplate,
                           StringRedisTemplate stringRedisTemplate) {
        this.lockKey = lockKey;
        this.lockValue = lockValue;
        this.expireTime = expireTime;
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 获取锁的key
     *
     * @return 锁的key
     */
    public String getLockKey() {
        return lockKey;
    }

    /**
     * 获取锁的值
     *
     * @return 锁的值
     */
    public String getLockValue() {
        return lockValue;
    }

    /**
     * 获取锁的过期时间
     *
     * @return 过期时间（毫秒）
     */
    public long getExpireTime() {
        return expireTime;
    }

    /**
     * 续期锁
     *
     * @param timeout 续期时间
     * @param unit    时间单位
     * @return 是否续期成功
     */
    public boolean renewLock(long timeout, TimeUnit unit) {
        try {
            DefaultRedisScript<Long> script = new DefaultRedisScript<>();
            script.setScriptText(RENEW_SCRIPT);
            script.setResultType(Long.class);

            Long result = stringRedisTemplate.execute(script,
                    Collections.singletonList(lockKey),
                    lockValue,
                    String.valueOf(unit.toSeconds(timeout)));

            boolean success = result != null && result == 1L;
            if (success) {
                log.debug("锁续期成功: key={}, timeout={}, unit={}", lockKey, timeout, unit);
            } else {
                log.warn("锁续期失败: key={}, timeout={}, unit={}", lockKey, timeout, unit);
            }
            return success;
        } catch (Exception e) {
            log.error("锁续期异常: key={}, timeout={}, unit={}", lockKey, timeout, unit, e);
            return false;
        }
    }

    /**
     * 释放锁
     *
     * @return 是否释放成功
     */
    public boolean unlock() {
        try {
            DefaultRedisScript<Long> script = new DefaultRedisScript<>();
            script.setScriptText(UNLOCK_SCRIPT);
            script.setResultType(Long.class);

            Long result = stringRedisTemplate.execute(script,
                    Collections.singletonList(lockKey),
                    lockValue);

            boolean success = result != null && result == 1L;
            if (success) {
                log.debug("锁释放成功: key={}", lockKey);
            } else {
                log.warn("锁释放失败: key={}", lockKey);
            }
            return success;
        } catch (Exception e) {
            log.error("锁释放异常: key={}", lockKey, e);
            return false;
        }
    }

    /**
     * 检查锁是否仍然有效
     *
     * @return 是否有效
     */
    public boolean isValid() {
        try {
            // 如果锁值为null，说明锁无效
            if (lockValue == null) {
                return false;
            }
            
            // 检查Redis中的锁值
            Object value = redisTemplate.opsForValue().get(lockKey);
            boolean valid = lockValue.equals(value);

            if (!valid) {
                log.debug("锁值不匹配: key={}, expected={}, actual={}", lockKey, lockValue, value);
            }

            return valid;
        } catch (Exception e) {
            log.error("锁有效性检查异常: key={}", lockKey, e);
            return false;
        }
    }

    /**
     * 检查锁是否过期
     *
     * @return 是否过期
     */
    public boolean isExpired() {
        return getRemainingTtl() <= 0;
    }

    /**
     * 获取锁的剩余过期时间
     *
     * @return 剩余时间（毫秒）
     */
    public long getRemainingTtl() {
        try {
            Long ttl = redisTemplate.getExpire(lockKey, TimeUnit.MILLISECONDS);
            return ttl != null ? ttl : -1;
        } catch (Exception e) {
            log.error("获取锁TTL失败: key={}", lockKey, e);
            return -1;
        }
    }

    /**
     * 生成锁值
     * 使用UUID确保唯一性
     *
     * @return 锁值
     */
    public static String generateLockValue() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return "DistributedLock{" +
                "lockKey='" + lockKey + '\'' +
                ", lockValue='" + lockValue + '\'' +
                ", expireTime=" + expireTime +
                '}';
    }
}