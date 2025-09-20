package com.smart.common.core.exception;

import com.smart.common.core.result.ResultCode;

/**
 * 业务逻辑异常
 * 用于业务逻辑处理失败时抛出
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
public class BusinessLogicException extends ErrorException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 构造函数
     * 
     * @param message 错误信息
     */
    public BusinessLogicException(String message) {
        super(ResultCode.BUSINESS_ERROR, message);
    }
    
    /**
     * 构造函数
     * 
     * @param message 错误信息
     * @param cause 原因异常
     */
    public BusinessLogicException(String message, Throwable cause) {
        super(ResultCode.BUSINESS_ERROR, message, cause);
    }
}
