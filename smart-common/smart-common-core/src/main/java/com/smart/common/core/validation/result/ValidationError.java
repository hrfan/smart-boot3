package com.smart.common.core.validation.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 校验错误信息封装类
 *
 * @author Smart Boot3
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationError {
    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 错误信息
     */
    private String message;

    /**
     * 被拒绝的值
     */
    private Object rejectedValue;

    public ValidationError(String fieldName, String message) {
        this(fieldName, message, null);
    }
}