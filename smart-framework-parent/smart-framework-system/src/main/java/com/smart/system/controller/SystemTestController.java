package com.smart.system.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统测试控制器
 * 用于测试网关代理和错误处理功能
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api")
public class SystemTestController {

    /**
     * 测试正常响应
     * 
     * @return 成功消息
     */
    @GetMapping("/success")
    public ResponseEntity<Map<String, Object>> testSuccess() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "系统服务正常");
        response.put("data", "Hello from System Service!");
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }

    /**
     * 测试系统错误响应
     * 
     * @return 错误响应
     */
    @GetMapping("/error")
    public ResponseEntity<Map<String, Object>> testError() {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.put("message", "系统内部错误");
        errorResponse.put("error", "这是一个系统服务抛出的错误");
        errorResponse.put("timestamp", System.currentTimeMillis());
        errorResponse.put("service", "smart-system");
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * 测试业务异常响应
     * 
     * @return 业务异常响应
     */
    @GetMapping("/business-error")
    public ResponseEntity<Map<String, Object>> testBusinessError() {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("code", 400);
        errorResponse.put("message", "业务参数错误");
        errorResponse.put("error", "用户ID不能为空");
        errorResponse.put("timestamp", System.currentTimeMillis());
        errorResponse.put("service", "smart-system");
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
