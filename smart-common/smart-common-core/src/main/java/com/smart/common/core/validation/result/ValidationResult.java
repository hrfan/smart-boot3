package com.smart.common.core.validation.result;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 校验结果封装类
 *
 * @author Smart Boot3
 * @since 1.0.0
 */
@Data
public class ValidationResult {
    /**
     * 是否有错误
     */
    private boolean hasErrors = false;

    /**
     * 错误信息列表
     */
    private List<ValidationError> errors = new ArrayList<>();

    /**
     * 添加错误
     * @param fieldName 字段名称
     * @param message 错误信息
     */
    public void addError(String fieldName, String message) {
        this.hasErrors = true;
        this.errors.add(new ValidationError(fieldName, message));
    }

    /**
     * 获取所有错误信息拼接成字符串
     * @return 错误信息字符串
     */
    public String getAllErrorsAsString() {
        StringBuilder sb = new StringBuilder();
        for (ValidationError error : errors) {
            sb.append(error.getFieldName()).append(": ").append(error.getMessage()).append("; ");
        }
        return sb.toString();
    }

    /**
     * 判断是否有错误
     * @return 是否有错误
     */
    public boolean hasErrors() {
        return hasErrors;
    }
}