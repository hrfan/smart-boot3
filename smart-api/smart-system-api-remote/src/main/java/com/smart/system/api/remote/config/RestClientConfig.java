package com.smart.system.api.remote.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * REST客户端配置类
 * 配置RestTemplate等HTTP客户端相关Bean
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Configuration
public class RestClientConfig {

    /**
     * 创建负载均衡的RestTemplate
     * 
     * @return RestTemplate实例
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
