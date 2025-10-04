package com.smart.framework.security.filter;

import com.smart.framework.captcha.properties.CaptchaProperties;
import com.smart.framework.captcha.service.CaptchaService;
import com.smart.framework.security.service.CustomUserDetailsService;
import com.smart.framework.security.exception.CaptchaException;
// import com.smart.framework.captcha.service.CaptchaService;
// import com.smart.framework.captcha.properties.CaptchaProperties;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 自定义用户名密码认证提供者
 * 处理用户名密码认证逻辑
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
@Component
public class CustomUsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(CustomUsernamePasswordAuthenticationProvider.class);

    @Resource
    private  CustomUserDetailsService userDetailsService;

    @Resource
    private  PasswordEncoder passwordEncoder;

    /**
     * 验证码服务（暂时注释，等待captcha模块编译成功）
     */
    @Resource
     private  CaptchaService captchaService;

    /**
     * 验证码配置（暂时注释，等待captcha模块编译成功）
     */
     @Resource
     private  CaptchaProperties captchaProperties;

    /**
     * 用户状态检查器
     */
    private final UserDetailsChecker userDetailsChecker = new AccountStatusUserDetailsChecker();


    /**
     * 执行认证
     * 
     * @param authentication 认证请求
     * @return 认证结果
     * @throws AuthenticationException 认证异常
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }

        CustomUsernamePasswordAuthenticationToken token = (CustomUsernamePasswordAuthenticationToken) authentication;
        String username = token.getName();
        String password = (String) token.getCredentials();
        String captcha = token.getCaptcha();
        String captchaUuid = token.getCaptchaUuid();

        log.debug("开始认证用户：{}", username);

        // 验证码校验 - 优先校验验证码，防止暴力破解
        validateCaptcha(captchaUuid, captcha);

        // 加载用户详情
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException("用户不存在：" + username);
        }

        // 验证密码
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            log.warn("用户密码错误：{}", username);
            throw new BadCredentialsException("用户名或密码错误");
        }

        // 检查用户状态
        userDetailsChecker.check(userDetails);

        log.info("用户认证成功：{}", username);

        // TODO: 记录登录日志
        // 1. 记录登录时间
        // 2. 记录IP地址
        // 3. 记录用户代理信息

        // 创建认证成功的令牌
        CustomUsernamePasswordAuthenticationToken authenticatedToken = 
            CustomUsernamePasswordAuthenticationToken.authenticated(
                userDetails, 
                password, 
                userDetails.getAuthorities()
            );
        
        authenticatedToken.setDetails(token.getDetails());
        authenticatedToken.setCaptcha(token.getCaptcha());
        authenticatedToken.setCaptchaUuid(token.getCaptchaUuid());
        authenticatedToken.setClientIp(token.getClientIp());
        authenticatedToken.setUserAgent(token.getUserAgent());

        return authenticatedToken;
    }

    /**
     * 验证码校验
     * 
     * @param captchaUuid 验证码UUID
     * @param captcha 验证码内容
     * @throws AuthenticationException 认证异常
     */
    private void validateCaptcha(String captchaUuid, String captcha) throws AuthenticationException {
        // 检查验证码是否启用（暂时注释，等待captcha模块编译成功）
        log.info("验证码功能是否启用：{}", captchaProperties.isEnabled());
        if (!captchaProperties.isEnabled()) {
            log.debug("验证码功能未启用，跳过验证码校验");
            return;
        }

        // 如果验证码UUID为空，跳过验证码校验
        if (!StringUtils.hasText(captchaUuid)) {
            log.debug("验证码UUID为空，跳过验证码校验");
            return;
        }
        
        if (!StringUtils.hasText(captcha)) {
            log.warn("验证码为空");
            throw new CaptchaException("验证码不能为空");
        }
        
        log.debug("验证码校验 - UUID：{}，验证码：{}", captchaUuid, captcha);
        
        // 调用验证码模块的服务进行校验
        boolean isValid = captchaService.verifyCaptcha(captchaUuid, captcha);
        if (!isValid) {
            log.warn("验证码校验失败，UUID：{}，验证码：{}", captchaUuid, captcha);
            throw new CaptchaException("验证码错误或已过期");
        }

        log.debug("验证码校验成功，UUID：{}", captchaUuid);
    }

    /**
     * 设置密码编码器
     * 
     * @param passwordEncoder 密码编码器
     */
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 判断是否支持该认证类型
     * 
     * @param authentication 认证类型
     * @return 是否支持
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return CustomUsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}