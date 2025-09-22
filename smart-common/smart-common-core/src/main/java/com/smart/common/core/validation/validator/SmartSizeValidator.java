package com.smart.common.core.validation.validator;

import com.smart.common.core.validation.annotation.SmartSize;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

/**
 * SmartSize校验器实现类
 *
 * @author Smart Boot3
 * @since 1.0.0
 */
public class SmartSizeValidator implements ConstraintValidator<SmartSize, String> {

    private int min;
    private int max;

    @Override
    public void initialize(SmartSize constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!StringUtils.hasLength(value)) {
            // 空字符串或null不进行长度校验，由@NotBlank或@NotNull处理
            return true;
        }
        int length = value.length();
        return length >= min && length <= max;
    }
}