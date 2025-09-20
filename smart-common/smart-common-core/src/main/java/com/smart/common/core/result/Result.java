package com.smart.common.core.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDateTime;

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
     * 响应时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    /**
     * 私有构造函数
     */
    private Result() {
        this.timestamp = LocalDateTime.now();
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

    // Getter和Setter方法
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}