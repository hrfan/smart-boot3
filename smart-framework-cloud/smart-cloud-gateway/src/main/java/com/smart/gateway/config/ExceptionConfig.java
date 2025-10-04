package com.smart.gateway.config;

import com.smart.gateway.exception.GlobalExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * 网关异常处理配置
 * 注册全局异常处理器
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
@Configuration
public class ExceptionConfig {

    /**
     * 注册全局异常处理器
     * 设置最高优先级，确保能够捕获所有异常
     * 
     * @return 全局异常处理器实例
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }
}
