package com.smart.gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 统一异常处理过滤器
 * 捕获后端服务的异常响应，统一处理并返回标准格式
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
@Component
public class UnifiedExceptionFilter extends AbstractGatewayFilterFactory<UnifiedExceptionFilter.Config> {

    private static final Logger log = LoggerFactory.getLogger(UnifiedExceptionFilter.class);

    public UnifiedExceptionFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                ServerHttpResponse response = exchange.getResponse();
                
                // 检查响应状态码是否为错误状态
                if (response.getStatusCode() != null && response.getStatusCode().isError()) {
                    log.debug("检测到错误响应: {} - {}", response.getStatusCode(), response.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE));
                    
                    // 如果响应体为空，说明是网关层面的错误，需要生成错误响应
                    if (response.getHeaders().getContentLength() == 0) {
                        generateUnifiedErrorResponse(exchange, response);
                    }
                    // 如果响应体不为空，检查是否需要统一处理
                    else {
                        handleBackendErrorResponse(exchange, response);
                    }
                }
            }));
        };
    }

    /**
     * 生成统一的错误响应
     * 
     * @param exchange 服务器Web交换
     * @param response HTTP响应
     */
    private void generateUnifiedErrorResponse(ServerWebExchange exchange, ServerHttpResponse response) {
        try {
            Map<String, Object> errorResponse = buildUnifiedErrorResponse(
                response.getStatusCode().value(),
                getErrorMessage(response.getStatusCode()),
                exchange.getRequest().getPath().value(),
                "网关处理异常",
                "gateway"
            );

            writeErrorResponse(response, errorResponse);
            
        } catch (Exception e) {
            log.error("生成统一错误响应失败", e);
        }
    }

    /**
     * 处理后端服务的错误响应
     * 
     * @param exchange 服务器Web交换
     * @param response HTTP响应
     */
    private void handleBackendErrorResponse(ServerWebExchange exchange, ServerHttpResponse response) {
        try {
            // 获取后端服务名称
            String serviceName = extractServiceName(exchange.getRequest().getPath().value());
            
            Map<String, Object> errorResponse = buildUnifiedErrorResponse(
                response.getStatusCode().value(),
                "服务异常",
                exchange.getRequest().getPath().value(),
                String.format("服务 %s 返回错误", serviceName),
                serviceName
            );

            writeErrorResponse(response, errorResponse);
            
        } catch (Exception e) {
            log.error("处理后端错误响应失败", e);
        }
    }

    /**
     * 构建统一的错误响应
     * 
     * @param code 错误码
     * @param message 错误消息
     * @param path 请求路径
     * @param error 详细错误信息
     * @param service 服务名称
     * @return 错误响应Map
     */
    private Map<String, Object> buildUnifiedErrorResponse(int code, String message, String path, String error, String service) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("code", code);
        errorResponse.put("message", message);
        errorResponse.put("timestamp", System.currentTimeMillis());
        errorResponse.put("path", path);
        errorResponse.put("error", error);
        errorResponse.put("service", service);
        
        return errorResponse;
    }

    /**
     * 写入错误响应
     * 
     * @param response HTTP响应
     * @param errorResponse 错误响应数据
     */
    private void writeErrorResponse(ServerHttpResponse response, Map<String, Object> errorResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(errorResponse);
            
            response.getHeaders().clear();
            response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            response.getHeaders().setContentLength(jsonResponse.getBytes(StandardCharsets.UTF_8).length);
            
            DataBuffer buffer = response.bufferFactory().wrap(jsonResponse.getBytes(StandardCharsets.UTF_8));
            response.writeWith(Flux.just(buffer)).subscribe();
            
        } catch (Exception e) {
            log.error("写入错误响应失败", e);
        }
    }

    /**
     * 获取错误消息
     * 
     * @param statusCode HTTP状态码
     * @return 错误消息
     */
    private String getErrorMessage(org.springframework.http.HttpStatusCode statusCode) {
        return switch (statusCode.value()) {
            case 400 -> "请求参数错误";
            case 401 -> "未认证，请先登录";
            case 403 -> "访问被拒绝，权限不足";
            case 404 -> "请求的资源不存在";
            case 500 -> "服务器内部错误";
            case 502 -> "网关错误";
            case 503 -> "服务暂时不可用";
            case 504 -> "网关超时";
            default -> "请求失败";
        };
    }

    /**
     * 从路径中提取服务名称
     * 
     * @param path 请求路径
     * @return 服务名称
     */
    private String extractServiceName(String path) {
        if (path.startsWith("/system/")) {
            return "smart-system";
        } else if (path.startsWith("/auth/")) {
            return "smart-auth";
        } else if (path.startsWith("/ai/")) {
            return "smart-ai";
        } else {
            return "unknown-service";
        }
    }

    /**
     * 配置类
     */
    public static class Config {
        // 可以添加配置参数
    }
}
