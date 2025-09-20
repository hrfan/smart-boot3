package com.smart.common.core.exception;

import com.smart.common.core.result.ResultCode;

/**
 * 业务异常基类
 * 所有业务异常都应该继承此类
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
public class ErrorException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 错误码
     */
    private final Integer code;
    
    /**
     * 错误信息
     */
    private final String message;
    
    /**
     * 构造函数
     * 
     * @param resultCode 结果码枚举
     */
    public ErrorException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }
    
    /**
     * 构造函数
     * 
     * @param resultCode 结果码枚举
     * @param message 自定义错误信息
     */
    public ErrorException(ResultCode resultCode, String message) {
        super(message);
        this.code = resultCode.getCode();
        this.message = message;
    }
    
    /**
     * 构造函数
     * 
     * @param resultCode 结果码枚举
     * @param cause 原因异常
     */
    public ErrorException(ResultCode resultCode, Throwable cause) {
        super(resultCode.getMessage(), cause);
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }
    
    /**
     * 构造函数
     * 
     * @param resultCode 结果码枚举
     * @param message 自定义错误信息
     * @param cause 原因异常
     */
    public ErrorException(ResultCode resultCode, String message, Throwable cause) {
        super(message, cause);
        this.code = resultCode.getCode();
        this.message = message;
    }
    
    /**
     * 获取错误码
     * 
     * @return 错误码
     */
    public Integer getCode() {
        return code;
    }
    
    /**
     * 获取错误信息
     * 
     * @return 错误信息
     */
    @Override
    public String getMessage() {
        return message;
    }
}
