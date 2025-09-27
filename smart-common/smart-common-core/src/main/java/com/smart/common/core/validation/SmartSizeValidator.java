package com.smart.common.core.validation;

import com.smart.common.core.validation.annotation.SmartSize;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;


/**
 * @author: warrior
 * @date: 2019-05-16
 */
public class SmartSizeValidator implements ConstraintValidator<SmartSize, String> {

    @Value("${dc.db.charset:GBK}")
    private String charset;

    @Value("${xdo.size.algorithm:bytes}")
    private String lengthAlgorithm;

    private SmartSize size;

    @Override
    public void initialize(SmartSize constraintAnnotation) {
        this.size = constraintAnnotation;
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
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || "".equals(value)) {
            return size.min() == 0 ? true : false;
        }
        int length;

        String algorithm = "bytes";

        if (lengthAlgorithm == null || "".equals(lengthAlgorithm)) {
            lengthAlgorithm ="bytes";
        }

        switch(lengthAlgorithm.toLowerCase()){
            case "bytes":
                algorithm = "bytes";
                break;
            case "length":
                algorithm = "length";
                break;
        }
        try {
            if("bytes".equals(algorithm)){
                //length = value.getBytes(charset).length;
                length = getCustomByteLength(value);
            }
            else{
                length = value.length();
            }
        } catch (Exception e) {
            try {
                if("bytes".equals(algorithm)){
                    //length = value.getBytes(Charset.forName("GBK")).length;
                    length = getCustomByteLength(value);
                }
                else{
                    length = value.length();
                }
            } catch (Exception e1){
                // 如果失败，虚拟机就直接狗带了，不需要了
                if("bytes".equals(algorithm)){
                    length = value.getBytes().length;
                }
                else{
                    length = value.length();
                }
            }
        }
        return (length > size.max() || length < size.min()) ? false : true;
    }

    private int getCustomByteLength(String str) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c <= 0x00FF) {
                count += 1;
            } else {
                count += 2;
            }
        }
        return count;
    }

}