package com.smart.common.core.validation.util;

import com.smart.common.core.validation.exception.ValidationException;
import com.smart.common.core.validation.result.ValidationResult;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

/**
 * 校验工具类
 * 提供程序化校验方法
 *
 * @author Smart Boot3
 * @since 1.0.0
 */
public class ValidationUtil {

    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

    /**
     * 校验对象，返回校验结果
     *
     * @param obj 待校验对象
     * @param <T> 对象类型
     * @return 校验结果
     */
    public static <T> ValidationResult validate(T obj) {
        Validator validator = VALIDATOR_FACTORY.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(obj);

        ValidationResult result = new ValidationResult();
        for (ConstraintViolation<T> violation : violations) {
            result.addError(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return result;
    }

    /**
     * 校验对象，如果存在错误则抛出ValidationException
     *
     * @param obj 待校验对象
     * @param <T> 对象类型
     */
    public static <T> void validateAndThrow(T obj) {
        ValidationResult result = validate(obj);
        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
    }
}