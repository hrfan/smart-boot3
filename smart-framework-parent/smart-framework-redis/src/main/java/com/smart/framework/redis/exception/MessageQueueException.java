package com.smart.framework.redis.exception;

/**
 * 消息队列异常
 * 当消息队列操作失败时抛出
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public class MessageQueueException extends RedisException {

    /**
     * 构造函数
     * 
     * @param message 错误消息
     */
    public MessageQueueException(String message) {
        super("MESSAGE_QUEUE_ERROR", message);
    }

    /**
     * 构造函数
     * 
     * @param message 错误消息
     * @param cause 原因异常
     */
    public MessageQueueException(String message, Throwable cause) {
        super("MESSAGE_QUEUE_ERROR", message, cause);
    }

    /**
     * 构造函数
     * 
     * @param operation 操作类型
     * @param topic 主题
     * @param cause 原因异常
     */
    public MessageQueueException(String operation, String topic, Throwable cause) {
        super("MESSAGE_QUEUE_ERROR", 
            String.format("Message queue %s operation failed: topic=%s", operation, topic), cause);
    }
}
