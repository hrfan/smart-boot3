package com.smart.framework.captcha.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 验证码配置属性
 * 控制验证码功能的启用状态和相关参数
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Component
@ConfigurationProperties(prefix = "smart.captcha")
public class CaptchaProperties {

    /**
     * 是否启用验证码功能
     * 默认启用
     */
    private boolean enabled = true;

    /**
     * 验证码类型
     * 默认数字验证码
     */
    private String type = "DEFAULT";

    /**
     * 验证码长度
     * 默认4位
     */
    private int length = 4;

    /**
     * 验证码过期时间（分钟）
     * 默认5分钟
     */
    private int expireMinutes = 5;

    /**
     * 验证码存储类型
     * 支持：memory（内存）、redis（Redis）
     */
    private String storageType = "memory";

    /**
     * 水印文字
     */
    private String waterMark = "Smart Boot3";

    /**
     * 水印字体
     */
    private String waterFont = "Arial";

    /**
     * 验证码字体
     */
    private String fontType = "宋体";

    /**
     * 是否启用AES加密
     */
    private boolean aesStatus = true;

    /**
     * 干扰选项
     */
    private String interferenceOptions = "0";

    /**
     * 缓存数量阈值
     */
    private String cacheNumber = "1000";

    /**
     * 定时清理时间（秒）
     */
    private String timingClear = "180";

    // Getters and Setters
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getExpireMinutes() {
        return expireMinutes;
    }

    public void setExpireMinutes(int expireMinutes) {
        this.expireMinutes = expireMinutes;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public String getWaterMark() {
        return waterMark;
    }

    public void setWaterMark(String waterMark) {
        this.waterMark = waterMark;
    }

    public String getWaterFont() {
        return waterFont;
    }

    public void setWaterFont(String waterFont) {
        this.waterFont = waterFont;
    }

    public String getFontType() {
        return fontType;
    }

    public void setFontType(String fontType) {
        this.fontType = fontType;
    }

    public boolean isAesStatus() {
        return aesStatus;
    }

    public void setAesStatus(boolean aesStatus) {
        this.aesStatus = aesStatus;
    }

    public String getInterferenceOptions() {
        return interferenceOptions;
    }

    public void setInterferenceOptions(String interferenceOptions) {
        this.interferenceOptions = interferenceOptions;
    }

    public String getCacheNumber() {
        return cacheNumber;
    }

    public void setCacheNumber(String cacheNumber) {
        this.cacheNumber = cacheNumber;
    }

    public String getTimingClear() {
        return timingClear;
    }

    public void setTimingClear(String timingClear) {
        this.timingClear = timingClear;
    }
}
