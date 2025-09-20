package com.smart.common.core.exception;

import com.smart.common.core.result.ResultCode;

/**
 * 参数验证异常
 * 用于参数校验失败时抛出
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
public class ParameterValidationException extends ErrorException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 构造函数
     * 
     * @param message 错误信息
     */
    public ParameterValidationException(String message) {
        super(ResultCode.PARAMETER_ERROR, message);
    }
    
    /**
     * 构造函数
     * 
     * @param message 错误信息
     * @param cause 原因异常
     */
    public ParameterValidationException(String message, Throwable cause) {
        super(ResultCode.PARAMETER_ERROR, message, cause);
    }
}
