package com.smart.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Smart Boot Admin 启动类
 * 微服务监控管理
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@SpringBootApplication
@EnableAdminServer
@EnableDiscoveryClient
public class SmartBootAdminApplication {

    /**
     * 主方法
     * 
     * @param args 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(SmartBootAdminApplication.class, args);
    }
}
