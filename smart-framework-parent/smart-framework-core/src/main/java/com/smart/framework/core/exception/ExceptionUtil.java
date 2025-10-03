package com.smart.framework.core.exception;

import com.smart.framework.core.result.ResultCode;

/**
 * 异常处理工具类
 * 提供便捷的异常抛出方法
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
public class ExceptionUtil {

    /**
     * 抛出参数验证异常
     * 
     * @param message 错误信息
     */
    public static void throwParameterError(String message) {
        throw new ParameterValidationException(message);
    }

    /**
     * 抛出参数验证异常
     * 
     * @param message 错误信息
     * @param cause 原因异常
     */
    public static void throwParameterError(String message, Throwable cause) {
        throw new ParameterValidationException(message, cause);
    }

    /**
     * 抛出业务逻辑异常
     * 
     * @param message 错误信息
     */
    public static void throwBusinessError(String message) {
        throw new BusinessLogicException(message);
    }

    /**
     * 抛出业务逻辑异常
     * 
     * @param message 错误信息
     * @param cause 原因异常
     */
    public static void throwBusinessError(String message, Throwable cause) {
        throw new BusinessLogicException(message, cause);
    }

    /**
     * 抛出数据访问异常
     * 
     * @param message 错误信息
     */
    public static void throwDataAccessError(String message) {
        throw new DataAccessException(message);
    }

    /**
     * 抛出数据访问异常
     * 
     * @param message 错误信息
     * @param cause 原因异常
     */
    public static void throwDataAccessError(String message, Throwable cause) {
        throw new DataAccessException(message, cause);
    }

    /**
     * 抛出数据不存在异常
     * 
     * @param message 错误信息
     */
    public static void throwDataNotFound(String message) {
        throw new ErrorException(ResultCode.DATA_NOT_FOUND, message);
    }

    /**
     * 抛出数据已存在异常
     * 
     * @param message 错误信息
     */
    public static void throwDataAlreadyExists(String message) {
        throw new ErrorException(ResultCode.DATA_ALREADY_EXISTS, message);
    }

    /**
     * 抛出操作不允许异常
     * 
     * @param message 错误信息
     */
    public static void throwOperationNotAllowed(String message) {
        throw new ErrorException(ResultCode.OPERATION_NOT_ALLOWED, message);
    }

    /**
     * 抛出资源不足异常
     * 
     * @param message 错误信息
     */
    public static void throwInsufficientResources(String message) {
        throw new ErrorException(ResultCode.INSUFFICIENT_RESOURCES, message);
    }

    /**
     * 抛出配置错误异常
     * 
     * @param message 错误信息
     */
    public static void throwConfigurationError(String message) {
        throw new ErrorException(ResultCode.CONFIGURATION_ERROR, message);
    }

    /**
     * 抛出网络错误异常
     * 
     * @param message 错误信息
     */
    public static void throwNetworkError(String message) {
        throw new ErrorException(ResultCode.NETWORK_ERROR, message);
    }

    /**
     * 抛出第三方服务错误异常
     * 
     * @param message 错误信息
     */
    public static void throwThirdPartyServiceError(String message) {
        throw new ErrorException(ResultCode.THIRD_PARTY_SERVICE_ERROR, message);
    }

    /**
     * 抛出文件操作错误异常
     * 
     * @param message 错误信息
     */
    public static void throwFileOperationError(String message) {
        throw new ErrorException(ResultCode.FILE_OPERATION_ERROR, message);
    }

    /**
     * 抛出缓存错误异常
     * 
     * @param message 错误信息
     */
    public static void throwCacheError(String message) {
        throw new ErrorException(ResultCode.CACHE_ERROR, message);
    }

    /**
     * 抛出消息队列错误异常
     * 
     * @param message 错误信息
     */
    public static void throwMessageQueueError(String message) {
        throw new ErrorException(ResultCode.MESSAGE_QUEUE_ERROR, message);
    }
}
