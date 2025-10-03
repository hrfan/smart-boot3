package com.smart.framework.core.validation.annotation;


import com.smart.framework.core.validation.SmartEnumValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author: 高士勇
 * @date: 2019-06-20
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = SmartEnumValidator.class)
public @interface SmartEnum {

    /**
     * 是否必填 默认是必填的
     * @return
     */
    boolean required() default true;

    String message() default "必须是限定的值";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] values() default {};
}
