package com.smart.framework.captcha.controller;

import com.smart.framework.captcha.entity.CaptchaConfigResponse;
import com.smart.framework.captcha.entity.CaptchaResponse;
import com.smart.framework.core.result.Result;
import com.smart.framework.captcha.properties.CaptchaProperties;
import com.smart.framework.captcha.service.CaptchaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

/**
 * 验证码控制器
 * 参考yuncheng项目的CaptchaController实现
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@RestController
@RequestMapping("/system/captcha")
public class CaptchaController {

    private static final Logger log = LoggerFactory.getLogger(CaptchaController.class);

    @Resource
    private CaptchaService captchaService;

    @Resource
    private CaptchaProperties captchaProperties;

    /**
     * 获取验证码配置状态
     * 前端调用此接口判断是否需要显示验证码
     * 
     * @return 验证码配置状态
     */
    @GetMapping("/config")
    public Result<CaptchaConfigResponse> getCaptchaConfig() {
        try {
            log.debug("获取验证码配置状态");
            
            CaptchaConfigResponse config = new CaptchaConfigResponse();
            config.setEnabled(captchaProperties.isEnabled());
            config.setType(captchaProperties.getType());
            config.setLength(captchaProperties.getLength());
            config.setExpireMinutes(captchaProperties.getExpireMinutes());
            
            return Result.success("获取验证码配置成功", config);
            
        } catch (Exception e) {
            log.error("获取验证码配置异常", e);
            return Result.error("获取验证码配置失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取验证码
     * 
     * @return 验证码响应
     */
    @GetMapping("/get")
    public Result<CaptchaResponse> getCaptcha() {
        try {
            log.debug("获取验证码请求");
            
            CaptchaResponse response = captchaService.generateCaptcha(captchaProperties);
            
            log.debug("获取验证码成功，ID: {}, 启用状态: {}", 
                     response.getCaptchaId(), response.isEnabled());
            return Result.success("获取验证码成功", response);
            
        } catch (Exception e) {
            log.error("获取验证码异常", e);
            return Result.error("获取验证码失败: " + e.getMessage());
        }
    }

    /**
     * 校验验证码
     * 
     * @param captchaId 验证码ID
     * @param captchaCode 验证码内容
     * @return 校验结果
     */
    @PostMapping("/check")
    public Result<Boolean> checkCaptcha(@RequestParam String captchaId, 
                                       @RequestParam String captchaCode) {
        try {
            // 检查验证码是否启用
            if (!captchaProperties.isEnabled()) {
                log.debug("验证码功能未启用，跳过验证码校验");
                return Result.success("验证码功能未启用，跳过校验", true);
            }
            
            log.debug("校验验证码请求，ID: {}", captchaId);
            
            boolean isValid = captchaService.verifyCaptcha(captchaId, captchaCode);
            
            if (isValid) {
                log.debug("验证码校验成功，ID: {}", captchaId);
                return Result.success("验证码校验成功", true);
            } else {
                log.warn("验证码校验失败，ID: {}", captchaId);
                return Result.error("验证码校验失败");
            }
            
        } catch (Exception e) {
            log.error("校验验证码异常", e);
            return Result.error("校验验证码失败: " + e.getMessage());
        }
    }

    /**
     * 删除验证码
     * 
     * @param captchaId 验证码ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{captchaId}")
    public Result<Void> deleteCaptcha(@PathVariable String captchaId) {
        try {
            log.debug("删除验证码请求，ID: {}", captchaId);
            
            captchaService.deleteCaptcha(captchaId);
            
            log.debug("删除验证码成功，ID: {}", captchaId);
            return Result.success("删除验证码成功");
            
        } catch (Exception e) {
            log.error("删除验证码异常", e);
            return Result.error("删除验证码失败: " + e.getMessage());
        }
    }

    /**
     * 清理过期验证码
     * 
     * @return 清理结果
     */
    @PostMapping("/cleanup")
    public Result<Integer> cleanupCaptchas() {
        try {
            log.debug("清理过期验证码请求");
            
            int count = captchaService.cleanupExpiredCaptchas();
            
            log.info("清理过期验证码完成，清理数量: {}", count);
            return Result.success("清理过期验证码成功", count);
            
        } catch (Exception e) {
            log.error("清理过期验证码异常", e);
            return Result.error("清理过期验证码失败: " + e.getMessage());
        }
    }
}


