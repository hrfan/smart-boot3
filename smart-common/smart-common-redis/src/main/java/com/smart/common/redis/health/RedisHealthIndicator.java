package com.smart.common.redis.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Redis健康检查指示器
 * 提供Redis连接状态和性能指标监控
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Component
public class RedisHealthIndicator implements HealthIndicator {

    private static final Logger log = LoggerFactory.getLogger(RedisHealthIndicator.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 健康检查
     * 
     * @return 健康状态
     */
    @Override
    public Health health() {
        try {
            // 检查Redis连接
            RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
            if (connection == null) {
                return Health.down()
                    .withDetail("redis", "Unavailable")
                    .withDetail("error", "Cannot get Redis connection")
                    .build();
            }
            String pong = connection.ping();
            connection.close();

            if ("PONG".equals(pong)) {
                // 获取Redis信息
                Map<String, Object> info = getRedisInfo();
                
                return Health.up()
                    .withDetail("redis", "Available")
                    .withDetail("ping", pong)
                    .withDetail("info", info)
                    .build();
            } else {
                return Health.down()
                    .withDetail("redis", "Unavailable")
                    .withDetail("ping", pong)
                    .build();
            }
        } catch (Exception e) {
            log.error("Redis health check failed", e);
            return Health.down()
                .withDetail("redis", "Unavailable")
                .withDetail("error", e.getMessage())
                .build();
        }
    }

    /**
     * 获取Redis信息
     * 
     * @return Redis信息映射
     */
    private Map<String, Object> getRedisInfo() {
        Map<String, Object> info = new HashMap<>();
        
        try {
            // 获取Redis服务器信息
            Properties serverInfo = redisTemplate.getConnectionFactory()
                .getConnection()
                .info("server");
            
            // 解析服务器信息
            parseRedisInfo(serverInfo, info);
            
            // 获取内存信息
            Properties memoryInfo = redisTemplate.getConnectionFactory()
                .getConnection()
                .info("memory");
            
            parseRedisInfo(memoryInfo, info);
            
            // 获取统计信息
            Properties statsInfo = redisTemplate.getConnectionFactory()
                .getConnection()
                .info("stats");
            
            parseRedisInfo(statsInfo, info);
            
        } catch (Exception e) {
            log.warn("Failed to get Redis info", e);
            info.put("error", "Failed to get Redis info: " + e.getMessage());
        }
        
        return info;
    }

    /**
     * 解析Redis信息Properties
     * 
     * @param infoProperties Redis信息Properties
     * @param infoMap 信息映射
     */
    private void parseRedisInfo(Properties infoProperties, Map<String, Object> infoMap) {
        if (infoProperties == null || infoProperties.isEmpty()) {
            return;
        }
        
        for (String key : infoProperties.stringPropertyNames()) {
            String value = infoProperties.getProperty(key);
            
            // 尝试转换为数字
            try {
                if (value.matches("\\d+")) {
                    infoMap.put(key, Long.parseLong(value));
                } else if (value.matches("\\d+\\.\\d+")) {
                    infoMap.put(key, Double.parseDouble(value));
                } else {
                    infoMap.put(key, value);
                }
            } catch (NumberFormatException e) {
                infoMap.put(key, value);
            }
        }
    }
}
