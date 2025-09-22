package com.smart.common.core.validation.exception;

import com.smart.common.core.validation.result.ValidationResult;
import lombok.Getter;

/**
 * 自定义校验异常
 * 用于程序化校验失败时抛出
 *
 * @author Smart Boot3
 * @since 1.0.0
 */
@Getter
public class ValidationException extends RuntimeException {

    private final ValidationResult validationResult;

    public ValidationException(ValidationResult validationResult) {
        super(validationResult != null ? validationResult.getAllErrorsAsString() : "校验失败");
        this.validationResult = validationResult;
    }

    public ValidationException(String message) {
        super(message);
        this.validationResult = new ValidationResult();
        this.validationResult.addError("general", message);
    }
}