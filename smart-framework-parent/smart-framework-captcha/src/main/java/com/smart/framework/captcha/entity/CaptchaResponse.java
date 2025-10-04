package com.smart.framework.captcha.entity;

/**
 * 验证码响应实体
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public class CaptchaResponse {
    /**
     * 验证码ID
     */
    private String captchaId;
    
    /**
     * Base64编码的验证码图片
     */
    private String imageBase64;
    
    /**
     * 过期时间戳
     */
    private long expireTime;
    
    /**
     * 验证码是否启用
     */
    private boolean enabled;

    /**
     * 构造函数
     * 
     * @param captchaId 验证码ID
     * @param imageBase64 Base64编码的验证码图片
     * @param expireTime 过期时间戳
     * @param enabled 验证码是否启用
     */
    public CaptchaResponse(String captchaId, String imageBase64, long expireTime, boolean enabled) {
        this.captchaId = captchaId;
        this.imageBase64 = imageBase64;
        this.expireTime = expireTime;
        this.enabled = enabled;
    }

    /**
     * 构造函数（兼容旧版本）
     * 
     * @param captchaId 验证码ID
     * @param imageBase64 Base64编码的验证码图片
     * @param expireTime 过期时间戳
     */
    public CaptchaResponse(String captchaId, String imageBase64, long expireTime) {
        this.captchaId = captchaId;
        this.imageBase64 = imageBase64;
        this.expireTime = expireTime;
        this.enabled = true; // 默认启用
    }

    /**
     * 获取验证码ID
     * 
     * @return 验证码ID
     */
    public String getCaptchaId() { 
        return captchaId; 
    }

    /**
     * 设置验证码ID
     * 
     * @param captchaId 验证码ID
     */
    public void setCaptchaId(String captchaId) { 
        this.captchaId = captchaId; 
    }

    /**
     * 获取Base64编码的验证码图片
     * 
     * @return Base64编码的验证码图片
     */
    public String getImageBase64() { 
        return imageBase64; 
    }

    /**
     * 设置Base64编码的验证码图片
     * 
     * @param imageBase64 Base64编码的验证码图片
     */
    public void setImageBase64(String imageBase64) { 
        this.imageBase64 = imageBase64; 
    }

    /**
     * 获取过期时间戳
     * 
     * @return 过期时间戳
     */
    public long getExpireTime() { 
        return expireTime; 
    }

    /**
     * 设置过期时间戳
     * 
     * @param expireTime 过期时间戳
     */
    public void setExpireTime(long expireTime) { 
        this.expireTime = expireTime; 
    }

    /**
     * 获取验证码是否启用
     * 
     * @return 验证码是否启用
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * 设置验证码是否启用
     * 
     * @param enabled 验证码是否启用
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
