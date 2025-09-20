package com.smart.common.redis.service.impl;

import com.smart.common.redis.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 缓存管理服务实现类
 * 提供基于Redis的缓存管理功能实现
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Service
public class CacheServiceImpl implements CacheService {

    private static final Logger log = LoggerFactory.getLogger(CacheServiceImpl.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 缓存命中计数器
     */
    private final AtomicLong hitCount = new AtomicLong(0);

    /**
     * 缓存未命中计数器
     */
    private final AtomicLong missCount = new AtomicLong(0);

    /**
     * 默认过期时间（分钟）
     */
    private static final long DEFAULT_EXPIRE_TIME = 30;

    /**
     * 缓存数据
     * 
     * @param key 缓存key
     * @param value 缓存值
     * @param timeout 过期时间
     * @param unit 时间单位
     */
    @Override
    public void cache(String key, Object value, long timeout, TimeUnit unit) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, unit);
            log.debug("Cache set successful: key={}, timeout={}, unit={}", key, timeout, unit);
        } catch (Exception e) {
            log.error("Cache set failed: key={}, timeout={}, unit={}", key, timeout, unit, e);
            throw new RuntimeException("Cache set failed", e);
        }
    }

    /**
     * 缓存数据（使用默认过期时间）
     * 
     * @param key 缓存key
     * @param value 缓存值
     */
    @Override
    public void cache(String key, Object value) {
        cache(key, value, DEFAULT_EXPIRE_TIME, TimeUnit.MINUTES);
    }

    /**
     * 获取缓存
     * 
     * @param key 缓存key
     * @param clazz 返回类型
     * @param <T> 泛型类型
     * @return 缓存值
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCache(String key, Class<T> clazz) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if (value != null) {
                hitCount.incrementAndGet();
                log.debug("Cache hit: key={}, value={}", key, value);
                return (T) value;
            } else {
                missCount.incrementAndGet();
                log.debug("Cache miss: key={}", key);
                return null;
            }
        } catch (Exception e) {
            missCount.incrementAndGet();
            log.error("Cache get failed: key={}", key, e);
            throw new RuntimeException("Cache get failed", e);
        }
    }

    /**
     * 获取缓存（如果不存在则执行回调函数）
     * 
     * @param key 缓存key
     * @param clazz 返回类型
     * @param callback 回调函数
     * @param timeout 过期时间
     * @param unit 时间单位
     * @param <T> 泛型类型
     * @return 缓存值
     */
    @Override
    public <T> T getCache(String key, Class<T> clazz, CacheCallback<T> callback, long timeout, TimeUnit unit) {
        T value = getCache(key, clazz);
        if (value != null) {
            return value;
        }
        
        try {
            T newValue = callback.call();
            if (newValue != null) {
                cache(key, newValue, timeout, unit);
            }
            return newValue;
        } catch (Exception e) {
            log.error("Cache callback failed: key={}", key, e);
            throw new RuntimeException("Cache callback failed", e);
        }
    }

    /**
     * 删除缓存
     * 
     * @param key 缓存key
     */
    @Override
    public void evictCache(String key) {
        try {
            Boolean result = redisTemplate.delete(key);
            log.debug("Cache evict {}: key={}", result ? "successful" : "failed", key);
        } catch (Exception e) {
            log.error("Cache evict failed: key={}", key, e);
            throw new RuntimeException("Cache evict failed", e);
        }
    }

    /**
     * 批量删除缓存
     * 
     * @param keys 缓存key列表
     */
    @Override
    public void evictCache(List<String> keys) {
        try {
            Long result = redisTemplate.delete(keys);
            log.debug("Cache batch evict successful: keys={}, deleted={}", keys.size(), result);
        } catch (Exception e) {
            log.error("Cache batch evict failed: keys={}", keys, e);
            throw new RuntimeException("Cache batch evict failed", e);
        }
    }

    /**
     * 清空所有缓存
     */
    @Override
    public void clearCache() {
        try {
            Set<String> keys = redisTemplate.keys("*");
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
                log.debug("Cache clear successful: keys={}", keys.size());
            } else {
                log.debug("Cache clear: no keys found");
            }
        } catch (Exception e) {
            log.error("Cache clear failed", e);
            throw new RuntimeException("Cache clear failed", e);
        }
    }

    /**
     * 检查缓存是否存在
     * 
     * @param key 缓存key
     * @return 是否存在
     */
    @Override
    public boolean exists(String key) {
        try {
            Boolean result = redisTemplate.hasKey(key);
            log.debug("Cache exists check: key={}, exists={}", key, result);
            return result != null && result;
        } catch (Exception e) {
            log.error("Cache exists check failed: key={}", key, e);
            return false;
        }
    }

    /**
     * 获取缓存过期时间
     * 
     * @param key 缓存key
     * @return 过期时间（毫秒）
     */
    @Override
    public long getExpire(String key) {
        try {
            Long ttl = redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
            long result = ttl != null ? ttl : -1;
            log.debug("Cache expire time: key={}, ttl={}", key, result);
            return result;
        } catch (Exception e) {
            log.error("Get cache expire time failed: key={}", key, e);
            return -1;
        }
    }

    /**
     * 设置缓存过期时间
     * 
     * @param key 缓存key
     * @param timeout 过期时间
     * @param unit 时间单位
     * @return 是否设置成功
     */
    @Override
    public boolean expire(String key, long timeout, TimeUnit unit) {
        try {
            Boolean result = redisTemplate.expire(key, timeout, unit);
            log.debug("Cache expire set {}: key={}, timeout={}, unit={}", 
                result ? "successful" : "failed", key, timeout, unit);
            return result != null && result;
        } catch (Exception e) {
            log.error("Set cache expire failed: key={}, timeout={}, unit={}", 
                key, timeout, unit, e);
            return false;
        }
    }

    /**
     * 缓存预热
     * 
     * @param cacheData 缓存数据
     * @param timeout 过期时间
     * @param unit 时间单位
     */
    @Override
    public void warmUpCache(Map<String, Object> cacheData, long timeout, TimeUnit unit) {
        try {
            for (Map.Entry<String, Object> entry : cacheData.entrySet()) {
                cache(entry.getKey(), entry.getValue(), timeout, unit);
            }
            log.debug("Cache warm-up successful: keys={}", cacheData.size());
        } catch (Exception e) {
            log.error("Cache warm-up failed: keys={}", cacheData.size(), e);
            throw new RuntimeException("Cache warm-up failed", e);
        }
    }

    /**
     * 获取缓存统计信息
     * 
     * @return 缓存统计信息
     */
    @Override
    public CacheStats getCacheStats() {
        long hits = hitCount.get();
        long misses = missCount.get();
        CacheStats stats = new CacheStats(hits, misses);
        log.debug("Cache stats: hits={}, misses={}, hitRate={}", 
            hits, misses, stats.getHitRate());
        return stats;
    }
}
