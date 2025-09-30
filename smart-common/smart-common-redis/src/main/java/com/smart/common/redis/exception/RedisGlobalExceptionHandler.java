package com.smart.common.redis.exception;

import com.smart.common.core.result.Result;
import com.smart.common.core.result.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Redis模块全局异常处理器
 * 处理Redis相关的异常
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
//@RestControllerAdvice
public class RedisGlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RedisGlobalExceptionHandler.class);

    /**
     * 处理Redis基础异常
     * 
     * @param e Redis异常
     * @return 统一响应结果
     */
    @ExceptionHandler(RedisException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleRedisException(RedisException e) {
        log.error("Redis operation failed: errorCode={}, message={}", e.getErrorCode(), e.getMessage(), e);
        return Result.error(ResultCode.ERROR.getCode(), e.getMessage());
    }

    /**
     * 处理锁获取异常
     * 
     * @param e 锁获取异常
     * @return 统一响应结果
     */
    @ExceptionHandler(LockAcquisitionException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public Result<Void> handleLockAcquisitionException(LockAcquisitionException e) {
        log.warn("Lock acquisition failed: {}", e.getMessage());
        return Result.error(ResultCode.ERROR.getCode(), "系统繁忙，请稍后重试");
    }

    /**
     * 处理限流异常
     * 
     * @param e 限流异常
     * @return 统一响应结果
     */
    @ExceptionHandler(RateLimitExceededException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public Result<Void> handleRateLimitExceededException(RateLimitExceededException e) {
        log.warn("Rate limit exceeded: {}", e.getMessage());
        return Result.error(ResultCode.ERROR.getCode(), "请求过于频繁，请稍后重试");
    }

    /**
     * 处理缓存异常
     * 
     * @param e 缓存异常
     * @return 统一响应结果
     */
    @ExceptionHandler(CacheException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleCacheException(CacheException e) {
        log.error("Cache operation failed: {}", e.getMessage(), e);
        return Result.error(ResultCode.ERROR.getCode(), "缓存操作失败");
    }

    /**
     * 处理消息队列异常
     * 
     * @param e 消息队列异常
     * @return 统一响应结果
     */
    @ExceptionHandler(MessageQueueException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleMessageQueueException(MessageQueueException e) {
        log.error("Message queue operation failed: {}", e.getMessage(), e);
        return Result.error(ResultCode.ERROR.getCode(), "消息队列操作失败");
    }
}
