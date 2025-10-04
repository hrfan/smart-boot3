package com.smart.framework.security.filter;

import com.smart.framework.security.service.CustomUserDetailsService;
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

    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 用户状态检查器
     */
    private final UserDetailsChecker userDetailsChecker = new AccountStatusUserDetailsChecker();

    /**
     * 构造函数
     * 
     * @param userDetailsService 用户详情服务
     * @param passwordEncoder 密码编码器
     */
    public CustomUsernamePasswordAuthenticationProvider(CustomUserDetailsService userDetailsService, 
                                                        PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

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

        log.debug("开始认证用户：{}", username);

        // TODO: 验证码校验
        // 1. 从Redis中获取验证码
        // 2. 比较验证码是否正确
        // 3. 验证码使用后删除
        // validateCaptcha(token);

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