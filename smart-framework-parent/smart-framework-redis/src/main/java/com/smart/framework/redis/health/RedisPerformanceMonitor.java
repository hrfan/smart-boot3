package com.smart.framework.redis.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Redis性能监控器
 * 监控Redis的基本性能指标
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Component
public class RedisPerformanceMonitor {

    private static final Logger log = LoggerFactory.getLogger(RedisPerformanceMonitor.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 操作计数器
     */
    private final AtomicLong operationCount = new AtomicLong(0);
    private final AtomicLong errorCount = new AtomicLong(0);
    private final AtomicLong totalResponseTime = new AtomicLong(0);

    /**
     * 告警阈值配置
     */
    private static final double ERROR_RATE_THRESHOLD = 0.1;        // 错误率阈值 10%
    private static final long RESPONSE_TIME_THRESHOLD = 1000;      // 响应时间阈值 1秒

    /**
     * 记录操作
     * 
     * @param operation 操作类型
     * @param responseTime 响应时间（毫秒）
     * @param success 是否成功
     */
    public void recordOperation(String operation, long responseTime, boolean success) {
        operationCount.incrementAndGet();
        totalResponseTime.addAndGet(responseTime);
        
        if (!success) {
            errorCount.incrementAndGet();
        }
        
        log.debug("记录操作: operation={}, responseTime={}ms, success={}", 
            operation, responseTime, success);
    }


    /**
     * 获取Redis性能指标
     * 
     * @return 性能指标Map
     */
    public Map<String, Object> getPerformanceMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        try {
            // 获取Redis INFO信息
            Properties info = redisTemplate.getConnectionFactory()
                .getConnection().info();
            
            if (info != null) {
                // 基本统计信息
                metrics.put("redis_version", info.getProperty("redis_version"));
                metrics.put("used_memory", info.getProperty("used_memory"));
                metrics.put("used_memory_human", info.getProperty("used_memory_human"));
                metrics.put("connected_clients", info.getProperty("connected_clients"));
                metrics.put("total_commands_processed", info.getProperty("total_commands_processed"));
                metrics.put("instantaneous_ops_per_sec", info.getProperty("instantaneous_ops_per_sec"));
                metrics.put("keyspace_hits", info.getProperty("keyspace_hits"));
                metrics.put("keyspace_misses", info.getProperty("keyspace_misses"));
                
                // 计算命中率
                long hits = Long.parseLong(info.getProperty("keyspace_hits", "0"));
                long misses = Long.parseLong(info.getProperty("keyspace_misses", "0"));
                double hitRate = hits + misses > 0 ? (double) hits / (hits + misses) : 0.0;
                metrics.put("keyspace_hit_rate", hitRate);
                
                // 内存使用率
                long usedMemory = Long.parseLong(info.getProperty("used_memory", "0"));
                long maxMemory = Long.parseLong(info.getProperty("maxmemory", "0"));
                if (maxMemory > 0) {
                    double memoryUsageRate = (double) usedMemory / maxMemory;
                    metrics.put("memory_usage_rate", memoryUsageRate);
                }
            }
        } catch (Exception e) {
            log.error("获取Redis指标时发生错误", e);
            metrics.put("error", "获取Redis指标失败: " + e.getMessage());
        }
        
        // 添加应用层指标
        long totalOps = operationCount.get();
        long totalErrors = errorCount.get();
        long totalRespTime = totalResponseTime.get();
        
        double errorRate = totalOps > 0 ? (double) totalErrors / totalOps : 0.0;
        long avgResponseTime = totalOps > 0 ? totalRespTime / totalOps : 0;
        
        metrics.put("totalOperations", totalOps);
        metrics.put("totalErrors", totalErrors);
        metrics.put("errorRate", errorRate);
        metrics.put("avgResponseTime", avgResponseTime);
        
        return metrics;
    }

    /**
     * 重置所有计数器
     */
    public void resetMetrics() {
        operationCount.set(0);
        errorCount.set(0);
        totalResponseTime.set(0);
        
        log.info("Redis性能监控计数器已重置");
    }
}