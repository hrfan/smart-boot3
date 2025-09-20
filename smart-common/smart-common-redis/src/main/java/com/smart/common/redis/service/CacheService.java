package com.smart.common.redis.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 缓存管理服务接口
 * 提供基于Redis的缓存管理功能
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public interface CacheService {

    /**
     * 缓存数据
     * 
     * @param key 缓存key
     * @param value 缓存值
     * @param timeout 过期时间
     * @param unit 时间单位
     */
    void cache(String key, Object value, long timeout, TimeUnit unit);

    /**
     * 缓存数据（使用默认过期时间）
     * 
     * @param key 缓存key
     * @param value 缓存值
     */
    void cache(String key, Object value);

    /**
     * 获取缓存
     * 
     * @param key 缓存key
     * @param clazz 返回类型
     * @param <T> 泛型类型
     * @return 缓存值
     */
    <T> T getCache(String key, Class<T> clazz);

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
    <T> T getCache(String key, Class<T> clazz, CacheCallback<T> callback, long timeout, TimeUnit unit);

    /**
     * 删除缓存
     * 
     * @param key 缓存key
     */
    void evictCache(String key);

    /**
     * 批量删除缓存
     * 
     * @param keys 缓存key列表
     */
    void evictCache(List<String> keys);

    /**
     * 清空所有缓存
     */
    void clearCache();

    /**
     * 检查缓存是否存在
     * 
     * @param key 缓存key
     * @return 是否存在
     */
    boolean exists(String key);

    /**
     * 获取缓存过期时间
     * 
     * @param key 缓存key
     * @return 过期时间（毫秒）
     */
    long getExpire(String key);

    /**
     * 设置缓存过期时间
     * 
     * @param key 缓存key
     * @param timeout 过期时间
     * @param unit 时间单位
     * @return 是否设置成功
     */
    boolean expire(String key, long timeout, TimeUnit unit);

    /**
     * 缓存预热
     * 
     * @param cacheData 缓存数据
     * @param timeout 过期时间
     * @param unit 时间单位
     */
    void warmUpCache(Map<String, Object> cacheData, long timeout, TimeUnit unit);

    /**
     * 获取缓存统计信息
     * 
     * @return 缓存统计信息
     */
    CacheStats getCacheStats();

    /**
     * 缓存回调函数接口
     * 
     * @param <T> 返回类型
     */
    @FunctionalInterface
    interface CacheCallback<T> {
        T call();
    }

    /**
     * 缓存统计信息类
     */
    class CacheStats {
        private final long hitCount;
        private final long missCount;
        private final long totalCount;
        private final double hitRate;

        public CacheStats(long hitCount, long missCount) {
            this.hitCount = hitCount;
            this.missCount = missCount;
            this.totalCount = hitCount + missCount;
            this.hitRate = totalCount > 0 ? (double) hitCount / totalCount : 0.0;
        }

        public long getHitCount() { return hitCount; }
        public long getMissCount() { return missCount; }
        public long getTotalCount() { return totalCount; }
        public double getHitRate() { return hitRate; }
    }
}
