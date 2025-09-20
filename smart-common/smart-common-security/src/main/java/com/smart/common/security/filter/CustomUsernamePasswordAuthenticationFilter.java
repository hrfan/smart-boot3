package com.smart.common.security.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * 自定义用户名密码认证过滤器
 * 处理用户名密码登录请求
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
public class CustomUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final Logger log = LoggerFactory.getLogger(CustomUsernamePasswordAuthenticationFilter.class);

    /**
     * 默认登录路径
     */
    private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH = new AntPathRequestMatcher("/api/auth/login", "POST");

    /**
     * 用户名参数名
     */
    public static final String USERNAME_PARAM = "username";

    /**
     * 密码参数名
     */
    public static final String PASSWORD_PARAM = "password";

    /**
     * 验证码参数名
     */
    public static final String CAPTCHA_PARAM = "captcha";

    /**
     * 验证码UUID参数名
     */
    public static final String CAPTCHA_UUID_PARAM = "captchaUuid";

    /**
     * 是否仅支持POST请求
     */
    private boolean postOnly = true;

    /**
     * 构造函数
     */
    public CustomUsernamePasswordAuthenticationFilter() {
        super(DEFAULT_LOGIN_PATH);
    }

    /**
     * 构造函数
     * 
     * @param loginPath 登录路径
     */
    public CustomUsernamePasswordAuthenticationFilter(String loginPath) {
        super(new AntPathRequestMatcher(loginPath, "POST"));
    }

    /**
     * 尝试认证
     * 
     * @param request HTTP请求
     * @param response HTTP响应
     * @return 认证结果
     * @throws AuthenticationException 认证异常
     * @throws IOException IO异常
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        if (postOnly && !"POST".equals(request.getMethod())) {
            throw new BadCredentialsException("认证方法不支持：" + request.getMethod());
        }

        // 获取请求参数
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        String captcha = obtainCaptcha(request);
        String captchaUuid = obtainCaptchaUuid(request);

        // 参数验证
        if (!StringUtils.hasText(username)) {
            throw new BadCredentialsException("用户名不能为空");
        }
        if (!StringUtils.hasText(password)) {
            throw new BadCredentialsException("密码不能为空");
        }

        // 清理参数
        username = username.trim();

        // TODO: 密码解密处理
        // 如果前端传递的是加密密码，这里需要解密
        // password = decryptPassword(password);

        // 获取客户端信息
        String clientIp = getClientIpAddress(request);
        String userAgent = request.getHeader("User-Agent");

        log.debug("用户登录尝试 - 用户名：{}，IP：{}", username, clientIp);

        // 创建认证令牌
        CustomUsernamePasswordAuthenticationToken authRequest = CustomUsernamePasswordAuthenticationToken.unauthenticated(username, password);
        authRequest.setCaptcha(captcha);
        authRequest.setCaptchaUuid(captchaUuid);
        authRequest.setClientIp(clientIp);
        authRequest.setUserAgent(userAgent);

        // 设置认证详情
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));

        // 执行认证
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * 获取用户名
     * 
     * @param request HTTP请求
     * @return 用户名
     */
    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(USERNAME_PARAM);
    }

    /**
     * 获取密码
     * 
     * @param request HTTP请求
     * @return 密码
     */
    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(PASSWORD_PARAM);
    }

    /**
     * 获取验证码
     * 
     * @param request HTTP请求
     * @return 验证码
     */
    protected String obtainCaptcha(HttpServletRequest request) {
        return request.getParameter(CAPTCHA_PARAM);
    }

    /**
     * 获取验证码UUID
     * 
     * @param request HTTP请求
     * @return 验证码UUID
     */
    protected String obtainCaptchaUuid(HttpServletRequest request) {
        return request.getParameter(CAPTCHA_UUID_PARAM);
    }

    /**
     * 获取客户端IP地址
     * 
     * @param request HTTP请求
     * @return IP地址
     */
    protected String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(xForwardedFor) && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (StringUtils.hasText(xRealIp) && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }

    /**
     * 设置是否仅支持POST请求
     * 
     * @param postOnly 是否仅支持POST
     */
    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }
}

