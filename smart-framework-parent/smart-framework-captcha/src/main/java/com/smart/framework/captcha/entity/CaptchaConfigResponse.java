package com.smart.framework.captcha.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 验证码配置响应
 */
@Data
public class CaptchaConfigResponse implements Serializable {
    /**
     * 是否启用验证码
     */
    private boolean enabled;
    
    /**
     * 验证码类型
     */
    private String type;
    
    /**
     * 验证码长度
     */
    private int length;
    
    /**
     * 过期时间（分钟）
     */
    private int expireMinutes;

}