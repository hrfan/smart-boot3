package com.smart.system.controller;

import com.smart.common.core.exception.ExceptionUtil;
import com.smart.common.core.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 简单异常处理测试控制器
 * 用于测试统一异常处理功能
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/public/simple-test")
public class SimpleExceptionTestController {

    /**
     * 测试成功响应
     * 
     * @return 测试结果
     */
    @GetMapping("/success")
    public Result<String> testSuccess() {
        return Result.success("测试成功");
    }

    /**
     * 测试业务异常处理
     * 
     * @return 测试结果
     */
    @GetMapping("/business-error")
    public Result<String> testBusinessError() {
        ExceptionUtil.throwBusinessError("这是一个业务异常测试");
        return Result.success("不会执行到这里");
    }

    /**
     * 测试数据不存在异常处理
     * 
     * @return 测试结果
     */
    @GetMapping("/data-not-found")
    public Result<String> testDataNotFound() {
        ExceptionUtil.throwDataNotFound("用户不存在");
        return Result.success("不会执行到这里");
    }

    /**
     * 测试系统异常处理
     * 
     * @return 测试结果
     */
    @GetMapping("/system-error")
    public Result<String> testSystemError() {
        throw new RuntimeException("这是一个系统异常");
    }
}
