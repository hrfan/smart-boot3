package com.smart.common.core.exception;

import com.smart.common.core.result.ResultCode;

/**
 * 数据访问异常
 * 用于数据库操作失败时抛出
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
public class DataAccessException extends ErrorException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 构造函数
     * 
     * @param message 错误信息
     */
    public DataAccessException(String message) {
        super(ResultCode.DATABASE_ERROR, message);
    }
    
    /**
     * 构造函数
     * 
     * @param message 错误信息
     * @param cause 原因异常
     */
    public DataAccessException(String message, Throwable cause) {
        super(ResultCode.DATABASE_ERROR, message, cause);
    }
}
