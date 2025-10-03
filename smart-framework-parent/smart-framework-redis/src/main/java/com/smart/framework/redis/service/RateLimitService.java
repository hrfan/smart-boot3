package com.smart.framework.redis.service;

/**
 * 限流服务接口
 * 提供基于Redis的分布式限流功能
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public interface RateLimitService {

    /**
     * 令牌桶限流
     * 
     * @param key 限流key
     * @param capacity 桶容量
     * @param refillRate 填充速率（每秒填充的令牌数）
     * @param tokens 请求令牌数
     * @return 是否允许通过
     */
    boolean tokenBucketLimit(String key, int capacity, int refillRate, int tokens);

    /**
     * 滑动窗口限流
     * 
     * @param key 限流key
     * @param windowSize 窗口大小（秒）
     * @param limit 限制数量
     * @return 是否允许通过
     */
    boolean slidingWindowLimit(String key, int windowSize, int limit);

    /**
     * 固定窗口限流
     * 
     * @param key 限流key
     * @param windowSize 窗口大小（秒）
     * @param limit 限制数量
     * @return 是否允许通过
     */
    boolean fixedWindowLimit(String key, int windowSize, int limit);

    /**
     * 获取当前限流状态
     * 
     * @param key 限流key
     * @param algorithm 限流算法类型
     * @return 限流状态信息
     */
    RateLimitInfo getRateLimitInfo(String key, RateLimitAlgorithm algorithm);

    /**
     * 重置限流计数器
     * 
     * @param key 限流key
     * @param algorithm 限流算法类型
     * @return 是否重置成功
     */
    boolean resetRateLimit(String key, RateLimitAlgorithm algorithm);

    /**
     * 限流算法枚举
     */
    enum RateLimitAlgorithm {
        TOKEN_BUCKET,     // 令牌桶
        SLIDING_WINDOW,   // 滑动窗口
        FIXED_WINDOW      // 固定窗口
    }

    /**
     * 限流信息类
     */
    class RateLimitInfo {
        private final String key;
        private final RateLimitAlgorithm algorithm;
        private final int limit;
        private final int current;
        private final long windowSize;
        private final long remainingTime;

        public RateLimitInfo(String key, RateLimitAlgorithm algorithm, int limit, 
                           int current, long windowSize, long remainingTime) {
            this.key = key;
            this.algorithm = algorithm;
            this.limit = limit;
            this.current = current;
            this.windowSize = windowSize;
            this.remainingTime = remainingTime;
        }

        public String getKey() { return key; }
        public RateLimitAlgorithm getAlgorithm() { return algorithm; }
        public int getLimit() { return limit; }
        public int getCurrent() { return current; }
        public long getWindowSize() { return windowSize; }
        public long getRemainingTime() { return remainingTime; }
        public boolean isAllowed() { return current < limit; }
    }
}
