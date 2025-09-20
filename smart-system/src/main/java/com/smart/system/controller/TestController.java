package com.smart.system.controller;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制器
 * 用于测试系统模块的异常处理功能
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
@RestController
@RequestMapping("/test")
public class TestController {

    /**
     * 测试正常响应
     * 
     * @return 成功消息
     */
    @GetMapping("/success")
    public String testSuccess() {
        return "系统模块测试成功";
    }

    /**
     * 测试认证异常
     * 
     * @return 抛出认证异常
     */
    @GetMapping("/auth-error")
    public String testAuthError() {
        throw new AuthenticationException("认证失败") {};
    }

    /**
     * 测试访问拒绝异常
     * 
     * @return 抛出访问拒绝异常
     */
    @GetMapping("/access-denied")
    public String testAccessDenied() {
        throw new AccessDeniedException("访问被拒绝");
    }

    /**
     * 测试通用异常
     * 
     * @return 抛出通用异常
     */
    @GetMapping("/generic-error")
    public String testGenericError() {
        throw new RuntimeException("系统模块通用异常");
    }
}
