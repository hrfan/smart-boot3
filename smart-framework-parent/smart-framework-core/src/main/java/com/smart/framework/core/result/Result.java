package com.smart.framework.core.result;

import java.io.Serializable;

/**
 * 统一响应结果
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 响应时间戳
     */
    private Long timestamp;

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 是否错误
     */
    private Boolean error;

    /**
     * 私有构造函数
     */
    private Result() {
        this.timestamp = System.currentTimeMillis();
        this.success = false;
        this.error = true;
    }

    /**
     * 成功响应
     * 
     * @param <T> 数据类型
     * @return 成功响应
     */
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(ResultCode.SUCCESS.getMessage());
        result.setSuccess(true);
        result.setError(false);
        return result;
    }

    /**
     * 成功响应带消息
     * 
     * @param message 响应消息
     * @return 成功响应
     */
    public static <T>Result<T> success(String message) {
        Result<T> result = new Result<T>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(message);
        result.setSuccess(true);
        result.setError(false);
        return result;
    }

    /**
     * 成功响应带数据
     * 
     * @param data 响应数据
     * @param <T> 数据类型
     * @return 成功响应
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = success();
        result.setData(data);
        return result;
    }

    /**
     * 成功响应带消息和数据
     * 
     * @param message 响应消息
     * @param data 响应数据
     * @param <T> 数据类型
     * @return 成功响应
     */
    public static <T> Result<T> success(String message, T data) {
        Result<T> result = success(data);
        result.setMessage(message);
        return result;
    }

    /**
     * 失败响应
     * 
     * @param <T> 数据类型
     * @return 失败响应
     */
    public static <T> Result<T> error() {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.ERROR.getCode());
        result.setMessage(ResultCode.ERROR.getMessage());
        result.setSuccess(false);
        result.setError(true);
        return result;
    }

    /**
     * 失败响应带消息
     * 
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 失败响应
     */
    public static <T> Result<T> error(String message) {
        Result<T> result = error();
        result.setMessage(message);
        return result;
    }

    /**
     * 失败响应带码和消息
     * 
     * @param code 错误码
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 失败响应
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = error();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    /**
     * 失败响应带码、消息和数据
     * 
     * @param code 错误码
     * @param message 错误消息
     * @param data 错误数据
     * @param <T> 数据类型
     * @return 失败响应
     */
    public static <T> Result<T> error(Integer code, String message, T data) {
        Result<T> result = error();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 失败响应带结果码
     * 
     * @param resultCode 结果码
     * @param <T> 数据类型
     * @return 失败响应
     */
    public static <T> Result<T> error(ResultCode resultCode) {
        Result<T> result = error();
        result.setCode(resultCode.getCode());
        result.setMessage(resultCode.getMessage());
        return result;
    }



    /**
     * 设置成功状态（链式调用）
     * 
     * @return 当前对象
     */
    public Result<T> setSuccess() {
        this.code = ResultCode.SUCCESS.getCode();
        this.success = true;
        this.error = false;
        return this;
    }

    /**
     * 设置成功状态并设置消息（链式调用）
     * 
     * @param message 成功消息
     * @return 当前对象
     */
    public Result<T> setSuccess(String message) {
        this.message = message;
        this.code = ResultCode.SUCCESS.getCode();
        this.success = true;
        this.error = false;
        return this;
    }

    /**
     * 设置错误状态（链式调用）
     * 
     * @return 当前对象
     */
    public Result<T> setError() {
        this.code = ResultCode.ERROR.getCode();
        this.success = false;
        this.error = true;
        return this;
    }

    /**
     * 设置错误状态并设置消息（链式调用）
     * 
     * @param message 错误消息
     * @return 当前对象
     */
    public Result<T> setError(String message) {
        this.message = message;
        this.code = ResultCode.ERROR.getCode();
        this.success = false;
        this.error = true;
        return this;
    }

    /**
     * 设置错误状态并设置码和消息（链式调用）
     * 
     * @param code 错误码
     * @param message 错误消息
     * @return 当前对象
     */
    public Result<T> setError(Integer code, String message) {
        this.code = code;
        this.message = message;
        this.success = false;
        this.error = true;
        return this;
    }

    /**
     * 设置数据（链式调用）
     * 
     * @param data 数据
     * @return 当前对象
     */
    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }

    /**
     * 设置消息（链式调用）
     * 
     * @param message 消息
     * @return 当前对象
     */
    public Result<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * 设置响应码（链式调用）
     * 
     * @param code 响应码
     * @return 当前对象
     */
    public Result<T> setCode(Integer code) {
        this.code = code;
        return this;
    }


    /**
     * 判断是否成功
     * 
     * @return 是否成功
     */
    public boolean isSuccess() {
        return ResultCode.SUCCESS.getCode().equals(this.code);
    }

    /**
     * 判断是否失败
     * 
     * @return 是否失败
     */
    public boolean isError() {
        return !isSuccess();
    }

    /**
     * 判断是否有数据
     * 
     * @return 是否有数据
     */
    public boolean hasData() {
        return this.data != null;
    }

    /**
     * 获取数据，如果为空则返回默认值
     * 
     * @param defaultValue 默认值
     * @return 数据或默认值
     */
    public T getDataOrDefault(T defaultValue) {
        return this.data != null ? this.data : defaultValue;
    }

    // ==================== Getter和Setter方法 ====================

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
        if (success != null) {
            if (success) {
                this.code = ResultCode.SUCCESS.getCode();
                this.error = false;
            } else {
                this.code = ResultCode.ERROR.getCode();
                this.error = true;
            }
        }
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}