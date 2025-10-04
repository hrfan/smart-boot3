package com.smart.framework.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码异常
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
public class CaptchaException extends AuthenticationException {

    public CaptchaException(String msg) {
        super(msg);
    }

    public CaptchaException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
