package com.smart.framework.security.filter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 自定义用户名密码认证令牌
 * 扩展标准认证令牌，添加验证码等额外信息
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
public class CustomUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

    /**
     * 验证码
     */
    private String captcha;

    /**
     * 验证码唯一标识
     */
    private String captchaUuid;

    /**
     * 客户端IP地址
     */
    private String clientIp;

    /**
     * 用户代理信息
     */
    private String userAgent;

    /**
     * 构造函数 - 未认证状态
     * 
     * @param principal 用户名
     * @param credentials 密码
     */
    public CustomUsernamePasswordAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    /**
     * 构造函数 - 已认证状态
     * 
     * @param principal 用户信息
     * @param credentials 密码
     * @param authorities 权限列表
     */
    public CustomUsernamePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    /**
     * 创建未认证的令牌
     * 
     * @param username 用户名
     * @param password 密码
     * @return 认证令牌
     */
    public static CustomUsernamePasswordAuthenticationToken unauthenticated(String username, String password) {
        return new CustomUsernamePasswordAuthenticationToken(username, password);
    }

    /**
     * 创建已认证的令牌
     * 
     * @param principal 用户信息
     * @param credentials 密码
     * @param authorities 权限列表
     * @return 认证令牌
     */
    public static CustomUsernamePasswordAuthenticationToken authenticated(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        return new CustomUsernamePasswordAuthenticationToken(principal, credentials, authorities);
    }

    // Getter 和 Setter 方法
    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getCaptchaUuid() {
        return captchaUuid;
    }

    public void setCaptchaUuid(String captchaUuid) {
        this.captchaUuid = captchaUuid;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}

