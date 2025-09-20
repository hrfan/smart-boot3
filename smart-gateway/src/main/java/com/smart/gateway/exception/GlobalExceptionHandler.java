package com.smart.gateway.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * 网关全局异常处理器
 * 专门处理网关层面的异常，返回JSON格式的错误信息
 * 后端服务的异常由UnifiedExceptionFilter统一处理
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
public class GlobalExceptionHandler implements WebExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理WebFlux异常
     * 
     * @param exchange 服务器Web交换
     * @param ex 异常
     * @return 错误响应
     */
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.error("网关捕获异常:{}", ex.getMessage());
        
        // 处理网关代理异常（服务不可用）
        if (ex instanceof NotFoundException) {
            return handleServiceNotFound(exchange, (NotFoundException) ex);
        }
        
        // 处理响应状态异常
        if (ex instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException) ex;
            return handleResponseStatusException(exchange, responseStatusException);
        }
        
        // 处理其他异常
        return handleGenericException(exchange, ex);
    }

    /**
     * 处理服务不可用异常
     * 
     * @param exchange 服务器Web交换
     * @param ex 服务未找到异常
     * @return 错误响应
     */
    private Mono<Void> handleServiceNotFound(ServerWebExchange exchange, NotFoundException ex) {
        log.warn("网关代理服务不可用: {}", ex.getMessage());
        
        Map<String, Object> errorResponse = buildErrorResponse(
            HttpStatus.SERVICE_UNAVAILABLE.value(),
            "服务暂时不可用，请稍后重试",
            exchange.getRequest().getPath().value(),
            ex.getMessage()
        );
        
        return writeErrorResponse(exchange, HttpStatus.SERVICE_UNAVAILABLE, errorResponse);
    }

    /**
     * 处理响应状态异常
     * 
     * @param exchange 服务器Web交换
     * @param ex 响应状态异常
     * @return 错误响应
     */
    private Mono<Void> handleResponseStatusException(ServerWebExchange exchange, ResponseStatusException ex) {
        log.warn("网关捕获响应状态异常: {} - {}", ex.getStatusCode(), ex.getReason());
        
        Map<String, Object> errorResponse = buildErrorResponse(
            ex.getStatusCode().value(),
            ex.getReason() != null ? ex.getReason() : "请求失败",
            exchange.getRequest().getPath().value(),
            ex.getMessage()
        );
        
        HttpStatus httpStatus = HttpStatus.valueOf(ex.getStatusCode().value());
        return writeErrorResponse(exchange, httpStatus, errorResponse);
    }

    /**
     * 处理通用异常
     * 
     * @param exchange 服务器Web交换
     * @param ex 异常
     * @return 错误响应
     */
    private Mono<Void> handleGenericException(ServerWebExchange exchange, Throwable ex) {
        log.error("网关捕获未知异常", ex);
        
        Map<String, Object> errorResponse = buildErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "网关内部错误",
            exchange.getRequest().getPath().value(),
            ex.getMessage()
        );
        
        return writeErrorResponse(exchange, HttpStatus.INTERNAL_SERVER_ERROR, errorResponse);
    }

    /**
     * 构建错误响应
     * 
     * @param code 错误码
     * @param message 错误消息
     * @param path 请求路径
     * @param error 详细错误信息
     * @return 错误响应Map
     */
    private Map<String, Object> buildErrorResponse(int code, String message, String path, String error) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("code", code);
        errorResponse.put("message", message);
        errorResponse.put("timestamp", System.currentTimeMillis());
        
        if (path != null) {
            errorResponse.put("path", path);
        }
        
        if (error != null) {
            errorResponse.put("error", error);
        }
        
        return errorResponse;
    }

    /**
     * 写入错误响应
     * 
     * @param exchange 服务器Web交换
     * @param status HTTP状态
     * @param errorResponse 错误响应数据
     * @return 响应结果
     */
    private Mono<Void> writeErrorResponse(ServerWebExchange exchange, HttpStatus status, Map<String, Object> errorResponse) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(errorResponse);
            exchange.getResponse().getHeaders().setContentLength(jsonResponse.getBytes().length);
            
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(jsonResponse.getBytes())));
        } catch (Exception e) {
            log.error("写入错误响应失败", e);
            return Mono.empty();
        }
    }
}
