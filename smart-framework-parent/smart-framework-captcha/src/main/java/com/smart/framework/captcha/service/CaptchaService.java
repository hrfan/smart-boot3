package com.smart.framework.captcha.service;

import com.smart.framework.captcha.entity.CaptchaResponse;
import com.smart.framework.captcha.properties.CaptchaProperties;
import com.smart.framework.captcha.util.CaptchaImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 验证码服务
 * 参考yuncheng项目的验证码实现
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Service
public class CaptchaService {

    private static final Logger log = LoggerFactory.getLogger(CaptchaService.class);

    /**
     * 验证码存储（实际项目中可以使用Redis）
     */
    private final ConcurrentHashMap<String, CaptchaInfo> captchaStorage = new ConcurrentHashMap<>();

    /**
     * 验证码信息
     */
    public static class CaptchaInfo {
        private String code;
        private long expireTime;
        private boolean used;

        public CaptchaInfo(String code, long expireTime) {
            this.code = code;
            this.expireTime = expireTime;
            this.used = false;
        }

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }

        public long getExpireTime() { return expireTime; }
        public void setExpireTime(long expireTime) { this.expireTime = expireTime; }

        public boolean isUsed() { return used; }
        public void setUsed(boolean used) { this.used = used; }

        public boolean isExpired() {
            return System.currentTimeMillis() > expireTime;
        }
    }

    /**
     * 生成验证码
     * 
     * @param captchaProperties 验证码配置属性
     * @return 验证码响应
     */
    public CaptchaResponse generateCaptcha(CaptchaProperties captchaProperties) {
        try {
            // 检查验证码是否启用
            if (!captchaProperties.isEnabled()) {
                log.debug("验证码功能未启用，返回空响应");
                return new CaptchaResponse(null, null, 0, false);
            }
            
            // 生成4位随机数字
            String code = generateRandomCode();
            String captchaId = generateCaptchaId();
            
            // 生成验证码图片
            String imageBase64 = CaptchaImageUtil.generate(code);
            
            // 存储验证码信息（使用配置的过期时间）
            long expireTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(captchaProperties.getExpireMinutes());
            captchaStorage.put(captchaId, new CaptchaInfo(code, expireTime));
            
            log.debug("生成验证码成功，ID: {}", captchaId);
            
            return new CaptchaResponse(captchaId, imageBase64, expireTime, captchaProperties.isEnabled());
            
        } catch (Exception e) {
            log.error("生成验证码失败", e);
            throw new RuntimeException("生成验证码失败: " + e.getMessage());
        }
    }

    /**
     * 生成验证码（兼容旧版本，默认启用）
     * 
     * @return 验证码响应
     */
    public CaptchaResponse generateCaptcha() {
        CaptchaProperties defaultProperties = new CaptchaProperties();
        return generateCaptcha(defaultProperties);
    }

    /**
     * 验证验证码
     * 
     * @param captchaId 验证码ID
     * @param userCode 用户输入的验证码
     * @return 验证结果
     */
    public boolean verifyCaptcha(String captchaId, String userCode) {
        if (captchaId == null || userCode == null) {
            return false;
        }

        CaptchaInfo captchaInfo = captchaStorage.get(captchaId);
        if (captchaInfo == null) {
            log.warn("验证码不存在，ID: {}", captchaId);
            return false;
        }

        if (captchaInfo.isExpired()) {
            log.warn("验证码已过期，ID: {}", captchaId);
            captchaStorage.remove(captchaId);
            return false;
        }

        if (captchaInfo.isUsed()) {
            log.warn("验证码已使用，ID: {}", captchaId);
            return false;
        }

        boolean isValid = captchaInfo.getCode().equalsIgnoreCase(userCode.trim());
        if (isValid) {
            captchaInfo.setUsed(true);
            log.debug("验证码验证成功，ID: {}", captchaId);
        } else {
            log.warn("验证码验证失败，ID: {}, 期望: {}, 实际: {}", 
                    captchaId, captchaInfo.getCode(), userCode);
        }

        return isValid;
    }

    /**
     * 删除验证码
     * 
     * @param captchaId 验证码ID
     */
    public void deleteCaptcha(String captchaId) {
        captchaStorage.remove(captchaId);
        log.debug("删除验证码，ID: {}", captchaId);
    }

    /**
     * 清理过期验证码
     * 
     * @return 清理数量
     */
    public int cleanupExpiredCaptchas() {
        int count = 0;
        captchaStorage.entrySet().removeIf(entry -> {
            if (entry.getValue().isExpired()) {
                return true;
            }
            return false;
        });
        
        log.debug("清理过期验证码完成，清理数量: {}", count);
        return count;
    }

    /**
     * 生成随机验证码
     * 
     * @return 4位随机数字
     */
    private String generateRandomCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * 生成验证码ID
     * 
     * @return 验证码ID
     */
    private String generateCaptchaId() {
        return "captcha_" + System.currentTimeMillis() + "_" + new Random().nextInt(10000);
    }
}
