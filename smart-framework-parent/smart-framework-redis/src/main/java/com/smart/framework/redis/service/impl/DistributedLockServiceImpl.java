package com.smart.framework.redis.service.impl;

import com.smart.framework.redis.service.DistributedLock;
import com.smart.framework.redis.service.DistributedLockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * 简化版分布式锁服务实现类
 * 提供基于Redis的分布式锁功能实现，使用Lua脚本保证原子性
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Service
public class DistributedLockServiceImpl implements DistributedLockService {

    private static final Logger log = LoggerFactory.getLogger(DistributedLockServiceImpl.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取锁脚本
     */
    private static final String LOCK_SCRIPT =
            "if redis.call('set', KEYS[1], ARGV[1], 'NX', 'EX', ARGV[2]) then " +
                    "return 1 " +
                    "else " +
                    "return 0 " +
                    "end";

    /**
     * 尝试获取锁脚本
     */
    private static final String TRY_LOCK_SCRIPT =
            "if redis.call('set', KEYS[1], ARGV[1], 'NX', 'EX', ARGV[2]) then " +
                    "return 1 " +
                    "else " +
                    "return 0 " +
                    "end";

    /**
     * 释放锁脚本
     */
    private static final String UNLOCK_SCRIPT =
            "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                    "return redis.call('del', KEYS[1]) " +
                    "else " +
                    "return 0 " +
                    "end";

    /**
     * 检查锁脚本
     */
    private static final String CHECK_LOCK_SCRIPT =
            "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                    "return 1 " +
                    "else " +
                    "return 0 " +
                    "end";

    /**
     * 获取锁的TTL脚本
     */
    private static final String GET_TTL_SCRIPT =
            "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                    "return redis.call('ttl', KEYS[1]) " +
                    "else " +
                    "return -1 " +
                    "end";

    /**
     * 获取分布式锁
     *
     * @param lockKey 锁的key
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return 锁对象
     */
    @Override
    public DistributedLock acquireLock(String lockKey, long timeout, TimeUnit unit) {
        String lockValue = DistributedLock.generateLockValue();
        long expireSeconds = unit.toSeconds(timeout);

        try {
            DefaultRedisScript<Long> script = new DefaultRedisScript<>();
            script.setScriptText(LOCK_SCRIPT);
            script.setResultType(Long.class);

            Long result = stringRedisTemplate.execute(script,
                    Collections.singletonList(lockKey),
                    lockValue,
                    String.valueOf(expireSeconds));

            boolean success = result != null && result == 1L;

            if (success) {
                log.debug("锁获取成功: key={}, value={}, timeout={}, unit={}",
                        lockKey, lockValue, timeout, unit);
                return new DistributedLock(lockKey, lockValue, System.currentTimeMillis() + unit.toMillis(timeout),
                        redisTemplate, stringRedisTemplate);
            } else {
                log.warn("锁获取失败: key={}, timeout={}, unit={}",
                        lockKey, timeout, unit);
                // 返回无效的锁对象
                return new DistributedLock(lockKey, null, -1, redisTemplate, stringRedisTemplate);
            }
        } catch (Exception e) {
            log.error("获取锁时发生错误: key={}, timeout={}, unit={}",
                    lockKey, timeout, unit, e);
            // 异常情况下返回无效的锁对象
            return new DistributedLock(lockKey, null, -1, redisTemplate, stringRedisTemplate);
        }
    }

    /**
     * 尝试获取锁
     *
     * @param lockKey 锁的key
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return 是否获取成功
     */
    @Override
    public boolean tryLock(String lockKey, long timeout, TimeUnit unit) {
        String lockValue = DistributedLock.generateLockValue();
        long expireSeconds = unit.toSeconds(timeout);

        try {
            DefaultRedisScript<Long> script = new DefaultRedisScript<>();
            script.setScriptText(TRY_LOCK_SCRIPT);
            script.setResultType(Long.class);

            Long result = stringRedisTemplate.execute(script,
                    Collections.singletonList(lockKey),
                    lockValue,
                    String.valueOf(expireSeconds));

            boolean success = result != null && result == 1L;

            if (success) {
                log.debug("尝试获取锁成功: key={}, timeout={}, unit={}",
                        lockKey, timeout, unit);
            } else {
                log.warn("尝试获取锁失败: key={}, timeout={}, unit={}",
                        lockKey, timeout, unit);
            }

            return success;
        } catch (Exception e) {
            log.error("尝试获取锁时发生错误: key={}, timeout={}, unit={}",
                    lockKey, timeout, unit, e);
            return false;
        }
    }

    /**
     * 释放锁
     *
     * @param lockKey 锁的key
     * @param lockValue 锁的值
     */
    @Override
    public void releaseLock(String lockKey, String lockValue) {
        try {
            DefaultRedisScript<Long> script = new DefaultRedisScript<>();
            script.setScriptText(UNLOCK_SCRIPT);
            script.setResultType(Long.class);

            Long result = stringRedisTemplate.execute(script,
                    Collections.singletonList(lockKey),
                    lockValue);

            boolean success = result != null && result == 1L;

            if (success) {
                log.debug("锁释放成功: key={}, value={}",
                        lockKey, lockValue);
            } else {
                log.warn("锁释放失败: key={}, value={}", lockKey, lockValue);
            }
        } catch (Exception e) {
            log.error("释放锁时发生错误: key={}, value={}", lockKey, lockValue, e);
        }
    }

    /**
     * 检查锁是否存在
     *
     * @param lockKey 锁的key
     * @return 是否存在
     */
    @Override
    public boolean isLocked(String lockKey) {
        try {
            Boolean exists = redisTemplate.hasKey(lockKey);
            boolean locked = exists != null && exists;
            log.debug("锁存在性检查: key={}, exists={}", lockKey, locked);
            return locked;
        } catch (Exception e) {
            log.error("检查锁存在性时发生错误: key={}", lockKey, e);
            return false;
        }
    }

    /**
     * 获取锁的剩余过期时间
     *
     * @param lockKey 锁的key
     * @return 剩余时间（毫秒）
     */
    @Override
    public long getLockTtl(String lockKey) {
        try {
            Long ttl = redisTemplate.getExpire(lockKey, TimeUnit.MILLISECONDS);
            long result = ttl != null ? ttl : -1;
            log.debug("锁TTL: key={}, ttl={}", lockKey, result);
            return result;
        } catch (Exception e) {
            log.error("获取锁TTL时发生错误: key={}", lockKey, e);
            return -1;
        }
    }
}