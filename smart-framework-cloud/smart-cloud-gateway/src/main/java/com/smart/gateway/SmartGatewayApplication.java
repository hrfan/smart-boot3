package com.smart.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 网关启动类
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class SmartGatewayApplication {

    /**
     * 主方法
     * 
     * @param args 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(SmartGatewayApplication.class, args);
    }

    /**
     * Hello World接口
     * 
     * @return Hello World消息
     */
    @GetMapping("/hello")
    public String hello() {
        return "Hello from Smart Gateway!";
    }

    /**
     * 健康检查接口
     * 
     * @return 健康状态
     */
    @GetMapping("/health")
    public String health() {
        return "Smart Gateway is running!";
    }
}