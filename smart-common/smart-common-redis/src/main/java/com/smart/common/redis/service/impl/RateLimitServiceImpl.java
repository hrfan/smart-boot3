package com.smart.common.redis.service.impl;

import com.smart.common.redis.service.RateLimitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 限流服务实现类
 * 提供基于Redis的分布式限流功能实现
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Service
public class RateLimitServiceImpl implements RateLimitService {

    private static final Logger log = LoggerFactory.getLogger(RateLimitServiceImpl.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 令牌桶限流脚本
     */
    private static final String TOKEN_BUCKET_SCRIPT = 
        "local key = KEYS[1] " +
        "local capacity = tonumber(ARGV[1]) " +
        "local refillRate = tonumber(ARGV[2]) " +
        "local tokens = tonumber(ARGV[3]) " +
        "local now = tonumber(ARGV[4]) " +
        "local window = tonumber(ARGV[5]) " +
        "local bucket = redis.call('HMGET', key, 'tokens', 'lastRefill') " +
        "local currentTokens = tonumber(bucket[1]) or capacity " +
        "local lastRefill = tonumber(bucket[2]) or now " +
        "local timePassed = now - lastRefill " +
        "local tokensToAdd = math.floor(timePassed * refillRate / window) " +
        "currentTokens = math.min(capacity, currentTokens + tokensToAdd) " +
        "if currentTokens >= tokens then " +
        "  currentTokens = currentTokens - tokens " +
        "  redis.call('HMSET', key, 'tokens', currentTokens, 'lastRefill', now) " +
        "  redis.call('EXPIRE', key, window) " +
        "  return 1 " +
        "else " +
        "  redis.call('HMSET', key, 'tokens', currentTokens, 'lastRefill', now) " +
        "  redis.call('EXPIRE', key, window) " +
        "  return 0 " +
        "end";

    /**
     * 滑动窗口限流脚本
     */
    private static final String SLIDING_WINDOW_SCRIPT = 
        "local key = KEYS[1] " +
        "local windowSize = tonumber(ARGV[1]) " +
        "local limit = tonumber(ARGV[2]) " +
        "local now = tonumber(ARGV[3]) " +
        "local windowStart = now - windowSize " +
        "redis.call('ZREMRANGEBYSCORE', key, 0, windowStart) " +
        "local current = redis.call('ZCARD', key) " +
        "if current < limit then " +
        "  redis.call('ZADD', key, now, now .. ':' .. math.random()) " +
        "  redis.call('EXPIRE', key, windowSize) " +
        "  return 1 " +
        "else " +
        "  return 0 " +
        "end";

    /**
     * 固定窗口限流脚本
     */
    private static final String FIXED_WINDOW_SCRIPT = 
        "local key = KEYS[1] " +
        "local windowSize = tonumber(ARGV[1]) " +
        "local limit = tonumber(ARGV[2]) " +
        "local now = tonumber(ARGV[3]) " +
        "local windowStart = math.floor(now / windowSize) * windowSize " +
        "local windowKey = key .. ':' .. windowStart " +
        "local current = tonumber(redis.call('GET', windowKey) or 0) " +
        "if current < limit then " +
        "  redis.call('INCR', windowKey) " +
        "  redis.call('EXPIRE', windowKey, windowSize) " +
        "  return 1 " +
        "else " +
        "  return 0 " +
        "end";

    /**
     * 获取限流信息脚本
     */
    private static final String GET_RATE_LIMIT_INFO_SCRIPT = 
        "local key = KEYS[1] " +
        "local algorithm = ARGV[1] " +
        "local limit = tonumber(ARGV[2]) " +
        "local windowSize = tonumber(ARGV[3]) " +
        "local now = tonumber(ARGV[4]) " +
        "if algorithm == 'TOKEN_BUCKET' then " +
        "  local bucket = redis.call('HMGET', key, 'tokens', 'lastRefill') " +
        "  local currentTokens = tonumber(bucket[1]) or 0 " +
        "  local lastRefill = tonumber(bucket[2]) or now " +
        "  local timePassed = now - lastRefill " +
        "  local refillRate = tonumber(ARGV[5]) " +
        "  currentTokens = math.min(limit, currentTokens + math.floor(timePassed * refillRate / windowSize)) " +
        "  return {currentTokens, limit, windowSize, 0} " +
        "elseif algorithm == 'SLIDING_WINDOW' then " +
        "  local windowStart = now - windowSize " +
        "  redis.call('ZREMRANGEBYSCORE', key, 0, windowStart) " +
        "  local current = redis.call('ZCARD', key) " +
        "  local remainingTime = windowSize - (now - windowStart) " +
        "  return {current, limit, windowSize, remainingTime} " +
        "elseif algorithm == 'FIXED_WINDOW' then " +
        "  local windowStart = math.floor(now / windowSize) * windowSize " +
        "  local windowKey = key .. ':' .. windowStart " +
        "  local current = tonumber(redis.call('GET', windowKey) or 0) " +
        "  local remainingTime = windowSize - (now - windowStart) " +
        "  return {current, limit, windowSize, remainingTime} " +
        "else " +
        "  return {0, limit, windowSize, 0} " +
        "end";

    /**
     * 令牌桶限流
     * 
     * @param key 限流key
     * @param capacity 桶容量
     * @param refillRate 填充速率（每秒填充的令牌数）
     * @param tokens 请求令牌数
     * @return 是否允许通过
     */
    @Override
    public boolean tokenBucketLimit(String key, int capacity, int refillRate, int tokens) {
        try {
            DefaultRedisScript<Long> script = new DefaultRedisScript<>();
            script.setScriptText(TOKEN_BUCKET_SCRIPT);
            script.setResultType(Long.class);
            
            long now = System.currentTimeMillis();
            long window = 1000; // 1秒窗口
            
            Long result = redisTemplate.execute(script, 
                Collections.singletonList(key), 
                String.valueOf(capacity),
                String.valueOf(refillRate),
                String.valueOf(tokens),
                String.valueOf(now),
                String.valueOf(window));
            
            boolean allowed = result != null && result == 1L;
            log.debug("Token bucket limit {}: key={}, capacity={}, refillRate={}, tokens={}", 
                allowed ? "passed" : "blocked", key, capacity, refillRate, tokens);
            return allowed;
        } catch (Exception e) {
            log.error("Token bucket limit failed: key={}, capacity={}, refillRate={}, tokens={}", 
                key, capacity, refillRate, tokens, e);
            return false;
        }
    }

    /**
     * 滑动窗口限流
     * 
     * @param key 限流key
     * @param windowSize 窗口大小（秒）
     * @param limit 限制数量
     * @return 是否允许通过
     */
    @Override
    public boolean slidingWindowLimit(String key, int windowSize, int limit) {
        try {
            DefaultRedisScript<Long> script = new DefaultRedisScript<>();
            script.setScriptText(SLIDING_WINDOW_SCRIPT);
            script.setResultType(Long.class);
            
            long now = System.currentTimeMillis();
            long windowMs = windowSize * 1000L;
            
            Long result = redisTemplate.execute(script, 
                Collections.singletonList(key), 
                String.valueOf(windowMs),
                String.valueOf(limit),
                String.valueOf(now));
            
            boolean allowed = result != null && result == 1L;
            log.debug("Sliding window limit {}: key={}, windowSize={}, limit={}", 
                allowed ? "passed" : "blocked", key, windowSize, limit);
            return allowed;
        } catch (Exception e) {
            log.error("Sliding window limit failed: key={}, windowSize={}, limit={}", 
                key, windowSize, limit, e);
            return false;
        }
    }

    /**
     * 固定窗口限流
     * 
     * @param key 限流key
     * @param windowSize 窗口大小（秒）
     * @param limit 限制数量
     * @return 是否允许通过
     */
    @Override
    public boolean fixedWindowLimit(String key, int windowSize, int limit) {
        try {
            DefaultRedisScript<Long> script = new DefaultRedisScript<>();
            script.setScriptText(FIXED_WINDOW_SCRIPT);
            script.setResultType(Long.class);
            
            long now = System.currentTimeMillis();
            long windowMs = windowSize * 1000L;
            
            Long result = redisTemplate.execute(script, 
                Collections.singletonList(key), 
                String.valueOf(windowMs),
                String.valueOf(limit),
                String.valueOf(now));
            
            boolean allowed = result != null && result == 1L;
            log.debug("Fixed window limit {}: key={}, windowSize={}, limit={}", 
                allowed ? "passed" : "blocked", key, windowSize, limit);
            return allowed;
        } catch (Exception e) {
            log.error("Fixed window limit failed: key={}, windowSize={}, limit={}", 
                key, windowSize, limit, e);
            return false;
        }
    }

    /**
     * 获取当前限流状态
     * 
     * @param key 限流key
     * @param algorithm 限流算法类型
     * @return 限流状态信息
     */
    @Override
    public RateLimitInfo getRateLimitInfo(String key, RateLimitAlgorithm algorithm) {
        try {
            DefaultRedisScript<List> script = new DefaultRedisScript<>();
            script.setScriptText(GET_RATE_LIMIT_INFO_SCRIPT);
            script.setResultType(List.class);
            
            long now = System.currentTimeMillis();
            long windowMs = 1000L; // 默认1秒窗口
            int limit = 100; // 默认限制
            int refillRate = 10; // 默认填充速率
            
            List<Long> result = redisTemplate.execute(script, 
                Collections.singletonList(key), 
                algorithm.name(),
                String.valueOf(limit),
                String.valueOf(windowMs),
                String.valueOf(now),
                String.valueOf(refillRate));
            
            if (result != null && result.size() >= 4) {
                int current = result.get(0).intValue();
                int limitValue = result.get(1).intValue();
                long windowSize = result.get(2);
                long remainingTime = result.get(3);
                
                log.debug("Rate limit info: key={}, algorithm={}, current={}, limit={}", 
                    key, algorithm, current, limitValue);
                
                return new RateLimitInfo(key, algorithm, limitValue, current, windowSize, remainingTime);
            } else {
                log.warn("Failed to get rate limit info: key={}, algorithm={}", key, algorithm);
                return new RateLimitInfo(key, algorithm, limit, 0, windowMs, 0);
            }
        } catch (Exception e) {
            log.error("Error getting rate limit info: key={}, algorithm={}", key, algorithm, e);
            return new RateLimitInfo(key, algorithm, 100, 0, 1000, 0);
        }
    }

    /**
     * 重置限流计数器
     * 
     * @param key 限流key
     * @param algorithm 限流算法类型
     * @return 是否重置成功
     */
    @Override
    public boolean resetRateLimit(String key, RateLimitAlgorithm algorithm) {
        try {
            Boolean result = redisTemplate.delete(key);
            log.debug("Rate limit reset {}: key={}, algorithm={}", 
                result ? "successful" : "failed", key, algorithm);
            return result != null && result;
        } catch (Exception e) {
            log.error("Error resetting rate limit: key={}, algorithm={}", key, algorithm, e);
            return false;
        }
    }
}
