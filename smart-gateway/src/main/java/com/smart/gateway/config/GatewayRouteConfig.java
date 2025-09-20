package com.smart.gateway.config;

import com.smart.gateway.filter.UnifiedExceptionFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 网关路由配置
 * 配置各个服务的路由规则
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
@Configuration
public class GatewayRouteConfig {

    /**
     * 配置路由规则
     * 
     * @param builder 路由构建器
     * @param unifiedExceptionFilter 统一异常过滤器
     * @return 路由定位器
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder, UnifiedExceptionFilter unifiedExceptionFilter) {
        return builder.routes()
                // system服务路由
                .route("system-service", r -> r
                        .path("/system/**")
                        .filters(f -> f
                                .stripPrefix(1) // 去掉路径前缀/system
                                .preserveHostHeader() // 保留原始Host头
                                .filter(unifiedExceptionFilter.apply(new UnifiedExceptionFilter.Config())) // 添加统一异常过滤器
                        )
                        .uri("lb://smart-system") // 使用负载均衡
                )
                // auth服务路由
                .route("auth-service", r -> r
                        .path("/auth/**")
                        .filters(f -> f
                                .stripPrefix(1) // 去掉路径前缀/auth
                                .preserveHostHeader() // 保留原始Host头
                                .filter(unifiedExceptionFilter.apply(new UnifiedExceptionFilter.Config())) // 添加统一异常过滤器
                        )
                        .uri("lb://smart-auth") // 使用负载均衡
                )
                // ai服务路由
                .route("ai-service", r -> r
                        .path("/ai/**")
                        .filters(f -> f
                                .stripPrefix(1) // 去掉路径前缀/ai
                                .preserveHostHeader() // 保留原始Host头
                                .filter(unifiedExceptionFilter.apply(new UnifiedExceptionFilter.Config())) // 添加统一异常过滤器
                        )
                        .uri("lb://smart-ai") // 使用负载均衡
                )
                // 网关自身测试接口路由
                .route("gateway-test", r -> r
                        .path("/test/**")
                        .uri("http://localhost:9000")
                )
                .build();
    }
}