package com.smart.system.controller;

import com.smart.framework.core.exception.ExceptionUtil;
import com.smart.framework.core.result.Result;
import com.smart.framework.core.result.ResultCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

/**
 * 异常处理测试控制器
 * 用于测试统一异常处理功能
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
@Tag(name = "异常处理测试", description = "测试统一异常处理功能")
@RestController
@RequestMapping("/api/public/test")
public class ExceptionTestController {

    /**
     * 测试业务异常处理
     * 
     * @return 测试结果
     */
    @Operation(summary = "测试业务异常", description = "测试业务异常处理")
    @GetMapping("/business-error")
    public Result<String> testBusinessError() {
        ExceptionUtil.throwBusinessError("这是一个业务异常测试");
        return Result.success("不会执行到这里");
    }

    /**
     * 测试参数验证异常处理
     * 
     * @param name 名称
     * @param age 年龄
     * @return 测试结果
     */
    @Operation(summary = "测试参数验证异常", description = "测试参数验证异常处理")
    @GetMapping("/parameter-error")
    public Result<String> testParameterError(
            @RequestParam @NotBlank(message = "名称不能为空") String name,
            @RequestParam @NotNull(message = "年龄不能为空") Integer age) {
        return Result.success("参数验证通过: " + name + ", " + age);
    }

    /**
     * 测试数据不存在异常处理
     * 
     * @return 测试结果
     */
    @Operation(summary = "测试数据不存在异常", description = "测试数据不存在异常处理")
    @GetMapping("/data-not-found")
    public Result<String> testDataNotFound() {
        ExceptionUtil.throwDataNotFound("用户不存在");
        return Result.success("不会执行到这里");
    }

    /**
     * 测试数据已存在异常处理
     * 
     * @return 测试结果
     */
    @Operation(summary = "测试数据已存在异常", description = "测试数据已存在异常处理")
    @GetMapping("/data-already-exists")
    public Result<String> testDataAlreadyExists() {
        ExceptionUtil.throwDataAlreadyExists("用户名已存在");
        return Result.success("不会执行到这里");
    }

    /**
     * 测试操作不允许异常处理
     * 
     * @return 测试结果
     */
    @Operation(summary = "测试操作不允许异常", description = "测试操作不允许异常处理")
    @GetMapping("/operation-not-allowed")
    public Result<String> testOperationNotAllowed() {
        ExceptionUtil.throwOperationNotAllowed("当前状态不允许此操作");
        return Result.success("不会执行到这里");
    }

    /**
     * 测试数据库异常处理
     * 
     * @return 测试结果
     */
    @Operation(summary = "测试数据库异常", description = "测试数据库异常处理")
    @GetMapping("/database-error")
    public Result<String> testDatabaseError() {
        ExceptionUtil.throwDataAccessError("数据库连接失败");
        return Result.success("不会执行到这里");
    }

    /**
     * 测试文件操作异常处理
     * 
     * @return 测试结果
     */
    @Operation(summary = "测试文件操作异常", description = "测试文件操作异常处理")
    @GetMapping("/file-error")
    public Result<String> testFileError() {
        ExceptionUtil.throwFileOperationError("文件上传失败");
        return Result.success("不会执行到这里");
    }

    /**
     * 测试缓存异常处理
     * 
     * @return 测试结果
     */
    @Operation(summary = "测试缓存异常", description = "测试缓存异常处理")
    @GetMapping("/cache-error")
    public Result<String> testCacheError() {
        ExceptionUtil.throwCacheError("Redis连接失败");
        return Result.success("不会执行到这里");
    }

    /**
     * 测试系统异常处理
     * 
     * @return 测试结果
     */
    @Operation(summary = "测试系统异常", description = "测试系统异常处理")
    @GetMapping("/system-error")
    public Result<String> testSystemError() {
        throw new RuntimeException("这是一个系统异常");
    }

    /**
     * 测试POST请求参数验证
     * 
     * @param request 测试请求
     * @return 测试结果
     */
    @Operation(summary = "测试POST参数验证", description = "测试POST请求参数验证")
    @PostMapping("/post-validation")
    public Result<String> testPostValidation(@Valid @RequestBody TestRequest request) {
        return Result.success("POST参数验证通过: " + request.getName() + ", " + request.getAge());
    }

    /**
     * 测试成功响应
     * 
     * @return 测试结果
     */
    @Operation(summary = "测试成功响应", description = "测试正常成功响应")
    @GetMapping("/success")
    public Result<String> testSuccess() {
        return Result.success("测试成功");
    }

    /**
     * 测试404异常
     * 
     * @return 测试结果
     */
    @Operation(summary = "测试404异常", description = "测试404异常处理")
    @GetMapping("/not-found")
    public Result<String> testNotFound() {
        // 这个方法不会被调用，因为路径不存在
        return Result.success("不会执行到这里");
    }

    /**
     * 测试请求DTO
     */
    @Data
    public static class TestRequest {
        @NotBlank(message = "名称不能为空")
        private String name;
        
        @NotNull(message = "年龄不能为空")
        private Integer age;
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public Integer getAge() {
            return age;
        }
        
        public void setAge(Integer age) {
            this.age = age;
        }
    }
}
