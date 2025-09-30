package com.smart.common.core.exception;

import com.smart.common.core.result.Result;
import com.smart.common.core.result.ResultCode;
import com.smart.common.core.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.Set;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

/**
 * 全局异常处理器
 * 统一处理系统中的各种异常，返回标准化的错误响应
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
@Component
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理业务异常
     * 
     * @param e 业务异常
     * @param request HTTP请求
     * @return 统一响应结果
     */
    @ExceptionHandler(ErrorException.class)
    public ResponseEntity<Result<Void>> handleBusinessException(ErrorException e, HttpServletRequest request) {
        log.warn("业务异常 [{}] [{}]: {}", request.getRequestURI(), e.getCode(), e.getMessage());
        return ResponseEntity.ok(Result.error(e.getCode(), e.getMessage()));
    }

    /**
     * 处理参数验证异常 - @Valid 注解验证失败
     * 
     * @param e 参数验证异常
     * @param request HTTP请求
     * @return 统一响应结果
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<Void>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e, HttpServletRequest request) {
        
        StringBuilder errorMsg = new StringBuilder();
        
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errorMsg.append(fieldError.getDefaultMessage()).append("; ");
            log.warn("参数验证异常 [{}]: {} {}", request.getRequestURI(), fieldError.getField(), fieldError.getDefaultMessage());
        }
        
        String message = errorMsg.toString();

        
        return ResponseEntity.ok(Result.error(ResultCode.PARAMETER_ERROR.getCode(), message));
    }

    /**
     * 处理参数绑定异常 - @Validated 注解验证失败
     * 
     * @param e 参数绑定异常
     * @param request HTTP请求
     * @return 统一响应结果
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Result<Void>> handleBindException(BindException e, HttpServletRequest request) {
        StringBuilder errorMsg = new StringBuilder();
        
        for (ObjectError error : e.getBindingResult().getAllErrors()) {
            errorMsg.append(error.getDefaultMessage()).append("; ");

        }
        String message = errorMsg.toString();
        log.warn("参数绑定异常 [{}]: {}", request.getRequestURI(), message);
        return ResponseEntity.ok(Result.error(ResultCode.PARAMETER_ERROR.getCode(), message));
    }

    /**
     * 处理约束违反异常 - @Validated 注解在方法参数上验证失败
     * 
     * @param e 约束违反异常
     * @param request HTTP请求
     * @return 统一响应结果
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Result<Void>> handleConstraintViolationException(
            ConstraintViolationException e, HttpServletRequest request) {
        
        StringBuilder errorMsg = new StringBuilder();
        
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            errorMsg.append(violation.getMessage()).append("; ");
        }
        
        String message = errorMsg.toString();
        log.warn("约束违反异常 [{}]: {}", request.getRequestURI(), message);
        
        return ResponseEntity.ok(Result.error(ResultCode.PARAMETER_ERROR.getCode(), message));
    }

    /**
     * 处理缺少请求参数异常
     * 
     * @param e 缺少请求参数异常
     * @param request HTTP请求
     * @return 统一响应结果
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Result<Void>> handleMissingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest request) {
        String message = "缺少必需的请求参数: " + e.getParameterName();
        log.warn("缺少请求参数异常 [{}]: {}", request.getRequestURI(), message);
        return ResponseEntity.ok(Result.error(ResultCode.PARAMETER_ERROR.getCode(), message));
    }

    /**
     * 处理方法参数类型不匹配异常
     * 
     * @param e 方法参数类型不匹配异常
     * @param request HTTP请求
     * @return 统一响应结果
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Result<Void>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        
        String message = String.format("参数 '%s' 类型错误，期望类型: %s", 
                e.getName(), e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "未知");
        log.warn("参数类型不匹配异常 [{}]: {}", request.getRequestURI(), message);
        
        return ResponseEntity.ok(Result.error(ResultCode.PARAMETER_ERROR.getCode(), message));
    }

    /**
     * 处理HTTP请求方法不支持异常
     * 
     * @param e HTTP请求方法不支持异常
     * @param request HTTP请求
     * @return 统一响应结果
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Result<Void>> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        
        String message = String.format("不支持的HTTP方法: %s，支持的方法: %s", 
                e.getMethod(), String.join(", ", e.getSupportedMethods()));
        log.warn("HTTP方法不支持异常 [{}]: {}", request.getRequestURI(), message);
        
        return ResponseEntity.ok(Result.error(ResultCode.METHOD_NOT_ALLOWED.getCode(), message));
    }

    /**
     * 处理404异常 - 没有找到处理器
     * 
     * @param e 没有找到处理器异常
     * @param request HTTP请求
     * @return 统一响应结果
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Result<Void>> handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        String message = "请求的资源不存在: " + e.getRequestURL();
        log.warn("资源不存在异常 [{}]: {}", request.getRequestURI(), message);
        return ResponseEntity.ok(Result.error(ResultCode.NOT_FOUND.getCode(), message));
    }

    /**
     * 处理Spring Security认证异常
     * 
     * @param e 认证异常
     * @param request HTTP请求
     * @return 统一响应结果
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Result<Void>> handleAuthenticationException(AuthenticationException e, HttpServletRequest request) {
        String message = "认证失败: " + e.getMessage();
        log.warn("认证异常 [{}]: {}", request.getRequestURI(), message);
        return ResponseEntity.ok(Result.error(ResultCode.UNAUTHORIZED.getCode(), message));
    }

    /**
     * 处理Spring Security访问拒绝异常
     * 
     * @param e 访问拒绝异常
     * @param request HTTP请求
     * @return 统一响应结果
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Result<Void>> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        String message = "访问被拒绝: " + e.getMessage();
        log.warn("访问拒绝异常 [{}]: {}", request.getRequestURI(), message);
        return ResponseEntity.ok(Result.error(ResultCode.FORBIDDEN.getCode(), message));
    }

    /**
     * 处理Spring DataAccessException异常（包括MyBatis包装的SQL异常）
     *
     * @param e 数据访问异常
     * @param request HTTP请求
     * @return 统一响应结果
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Result<Void>> handleDataAccessException(DataAccessException e, HttpServletRequest request) {
        log.error("数据访问异常 [{}]: {}", request.getRequestURI(), e.getMessage(), e);
        return ResponseEntity.ok(Result.error(ResultCode.DATABASE_ERROR.getCode(), "数据库操作失败"));
    }

    /**
     * 处理SQL异常
     * 
     * @param e SQL异常
     * @param request HTTP请求
     * @return 统一响应结果
     */
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Result<Void>> handleSQLException(SQLException e, HttpServletRequest request) {
        log.error("SQL异常 [{}]: {}", request.getRequestURI(), e.getMessage(), e);
        return ResponseEntity.ok(Result.error(ResultCode.DATABASE_ERROR.getCode(), "数据库操作失败"));
    }

    /**
     * 处理文件访问拒绝异常
     * 
     * @param e 文件访问拒绝异常
     * @param request HTTP请求
     * @return 统一响应结果
     */
    @ExceptionHandler(java.nio.file.AccessDeniedException.class)
    public ResponseEntity<Result<Void>> handleNioAccessDeniedException(java.nio.file.AccessDeniedException e, HttpServletRequest request) {
        String message = "文件访问被拒绝: " + e.getMessage();
        log.warn("文件访问拒绝异常 [{}]: {}", request.getRequestURI(), message);
        return ResponseEntity.ok(Result.error(ResultCode.FILE_OPERATION_ERROR.getCode(), message));
    }

    /**
     * 处理其他未捕获的异常
     * 
     * @param e 异常
     * @param request HTTP请求
     * @return 统一响应结果
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Void>> handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常 [{}]: {}", request.getRequestURI(), e.getMessage(), e);
        String message = StringUtil.isBlank(e.getMessage()) ? "系统内部错误" : e.getMessage();
        return ResponseEntity.ok(Result.error(ResultCode.INTERNAL_SERVER_ERROR.getCode(),message));
    }
}