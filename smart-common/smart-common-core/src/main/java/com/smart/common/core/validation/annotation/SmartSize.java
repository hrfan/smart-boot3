package com.smart.common.core.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import com.smart.common.core.validation.validator.SmartSizeValidator;

import java.lang.annotation.*;

/**
 * 自定义SmartSize校验注解
 * 用于校验字符串字段的长度是否在指定范围内
 *
 * @author Smart Boot3
 * @since 1.0.0
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = SmartSizeValidator.class)
public @interface SmartSize {

    /**
     * 最小长度
     *
     * @return 最小长度
     */
    int min() default 0;

    /**
     * 最大长度
     *
     * @return 最大长度
     */
    int max() default Integer.MAX_VALUE;

    /**
     * 校验失败时的提示信息
     *
     * @return 提示信息
     */
    String message() default "字段长度超出限制";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}