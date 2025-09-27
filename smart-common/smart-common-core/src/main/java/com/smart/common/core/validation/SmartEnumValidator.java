package com.smart.common.core.validation;

import com.smart.common.core.validation.annotation.SmartEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

/**
 * @author: 高士勇
 * @date: 2019-06-20
 */

public class SmartEnumValidator implements ConstraintValidator<SmartEnum, Object> {

    private SmartEnum xdoEnum;
    List<String> valueList = null;

    @Override
    public void initialize(SmartEnum constraintAnnotation) {
        this.xdoEnum = constraintAnnotation;

        if (constraintAnnotation.values() != null && constraintAnnotation.values().length > 0) {
            valueList = Arrays.asList(constraintAnnotation.values());
        }
    }

    /**
     * Implements the validation logic.
     * The state of {@code value} must not be altered.
     * <p>
     * This method can be accessed concurrently, thread-safety must be ensured
     * by the implementation.
     *
     * @param value   object to validate
     * @param context context in which the constraint is evaluated
     * @return {@code false} if {@code value} does not pass the constraint
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null || "".equals(value)) {
            return this.xdoEnum.required() ? false : true;
        }

        if (valueList == null) {
            return false;
        }

        if(!valueList.contains(value.toString().toUpperCase())) {
            return false;
        }
        return true;
    }
}
