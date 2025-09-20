package com.smart.common.core.result;

/**
 * 响应结果码枚举
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public enum ResultCode {

    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 失败
     */
    ERROR(500, "操作失败"),

    /**
     * 参数错误
     */
    PARAMETER_ERROR(400, "参数错误"),

    /**
     * 未授权
     */
    UNAUTHORIZED(401, "未授权"),

    /**
     * 禁止访问
     */
    FORBIDDEN(403, "禁止访问"),

    /**
     * 资源不存在
     */
    NOT_FOUND(404, "资源不存在"),

    /**
     * 方法不允许
     */
    METHOD_NOT_ALLOWED(405, "方法不允许"),

    /**
     * 请求超时
     */
    REQUEST_TIMEOUT(408, "请求超时"),

    /**
     * 服务器内部错误
     */
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),

    /**
     * 服务不可用
     */
    SERVICE_UNAVAILABLE(503, "服务不可用"),

    /**
     * 网关超时
     */
    GATEWAY_TIMEOUT(504, "网关超时"),

    /**
     * 业务错误
     */
    BUSINESS_ERROR(1000, "业务处理失败"),

    /**
     * 数据不存在
     */
    DATA_NOT_FOUND(1001, "数据不存在"),

    /**
     * 数据已存在
     */
    DATA_ALREADY_EXISTS(1002, "数据已存在"),

    /**
     * 数据状态错误
     */
    DATA_STATE_ERROR(1003, "数据状态错误"),

    /**
     * 操作不允许
     */
    OPERATION_NOT_ALLOWED(1004, "操作不允许"),

    /**
     * 资源不足
     */
    INSUFFICIENT_RESOURCES(1005, "资源不足"),

    /**
     * 配置错误
     */
    CONFIGURATION_ERROR(1006, "配置错误"),

    /**
     * 网络错误
     */
    NETWORK_ERROR(1007, "网络错误"),

    /**
     * 第三方服务错误
     */
    THIRD_PARTY_SERVICE_ERROR(1008, "第三方服务错误"),

    /**
     * 文件操作错误
     */
    FILE_OPERATION_ERROR(1009, "文件操作错误"),

    /**
     * 数据库错误
     */
    DATABASE_ERROR(1010, "数据库操作失败"),

    /**
     * 缓存错误
     */
    CACHE_ERROR(1011, "缓存操作失败"),

    /**
     * 消息队列错误
     */
    MESSAGE_QUEUE_ERROR(1012, "消息队列操作失败");

    /**
     * 响应码
     */
    private final Integer code;

    /**
     * 响应消息
     */
    private final String message;

    /**
     * 构造函数
     * 
     * @param code 响应码
     * @param message 响应消息
     */
    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取响应码
     * 
     * @return 响应码
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 获取响应消息
     * 
     * @return 响应消息
     */
    public String getMessage() {
        return message;
    }
}