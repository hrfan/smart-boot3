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
import java.util.Map;

/**
 * 错误响应处理过滤器
 * 当后端服务返回错误时，直接透传错误信息而不进行重新包装
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
@Component
public class ErrorResponseFilter extends AbstractGatewayFilterFactory<ErrorResponseFilter.Config> {

    private static final Logger log = LoggerFactory.getLogger(ErrorResponseFilter.class);

    public ErrorResponseFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                ServerHttpResponse response = exchange.getResponse();
                
                // 检查响应状态码是否为错误状态
                if (response.getStatusCode() != null && 
                    response.getStatusCode().isError()) {
                    
                    log.debug("检测到错误响应: {}", response.getStatusCode());
                    
                    // 如果响应体为空，说明是网关层面的错误，需要生成错误响应
                    if (response.getHeaders().getContentLength() == 0) {
                        generateErrorResponse(exchange, response);
                    }
                    // 如果响应体不为空，说明是后端服务的错误响应，直接透传
                    else {
                        log.debug("透传后端服务错误响应");
                    }
                }
            }));
        };
    }

    /**
     * 生成错误响应
     * 
     * @param exchange 服务器Web交换
     * @param response HTTP响应
     */
    private void generateErrorResponse(ServerWebExchange exchange, ServerHttpResponse response) {
        try {
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "code", response.getStatusCode().value(),
                "message", getErrorMessage(response.getStatusCode()),
                "timestamp", System.currentTimeMillis(),
                "path", exchange.getRequest().getPath().value()
            );

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(errorResponse);
            
            DataBuffer buffer = response.bufferFactory().wrap(jsonResponse.getBytes(StandardCharsets.UTF_8));
            response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            response.getHeaders().setContentLength(buffer.readableByteCount());
            
            response.writeWith(Flux.just(buffer)).subscribe();
            
        } catch (Exception e) {
            log.error("生成错误响应失败", e);
        }
    }

    /**
     * 获取错误消息
     * 
     * @param statusCode HTTP状态码
     * @return 错误消息
     */
    private String getErrorMessage(org.springframework.http.HttpStatusCode statusCode) {
        if (statusCode.value() == 404) {
            return "请求的资源不存在";
        } else if (statusCode.value() == 500) {
            return "服务器内部错误";
        } else if (statusCode.value() == 503) {
            return "服务暂时不可用";
        } else if (statusCode.value() == 502) {
            return "网关错误";
        } else if (statusCode.value() == 504) {
            return "网关超时";
        } else {
            return "请求失败";
        }
    }

    /**
     * 配置类
     */
    public static class Config {
        // 可以添加配置参数
    }
}
