package com.smart.gateway.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 请求ID生成器
 * 用于生成唯一的请求ID，便于链路追踪
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
@Component
public class RequestIdGenerator {

    /**
     * 生成请求ID
     * 
     * @return 请求ID
     */
    public String generateRequestId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成带前缀的请求ID
     * 
     * @param prefix 前缀
     * @return 请求ID
     */
    public String generateRequestId(String prefix) {
        return prefix + "_" + generateRequestId();
    }

    /**
     * 生成网关请求ID
     * 
     * @return 网关请求ID
     */
    public String generateGatewayRequestId() {
        return generateRequestId("GW");
    }
}
