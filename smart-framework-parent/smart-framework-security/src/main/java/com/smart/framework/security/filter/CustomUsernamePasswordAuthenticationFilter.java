package com.smart.framework.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

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
         password = decryptPassword(password);

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
         * 解密密码
         *
         * @param encryptedPassword 加密后的密码
         * @return 解密后的密码
         */
    private String decryptPassword(String encryptedPassword) {
        // TODO: 实现密码解密逻辑
        // 这里可以使用加密算法解密密码
        // 例如：AES解密、RSA解密等
        return new BCryptPasswordEncoder().encode(encryptedPassword); // 假设直接返回加密后的密码，实际应解密后返回
    }

    /**
     * 获取用户名
     * 支持表单参数和JSON格式
     * 
     * @param request HTTP请求
     * @return 用户名
     */
    protected String obtainUsername(HttpServletRequest request) {
        // 首先尝试从表单参数获取
        String username = request.getParameter(USERNAME_PARAM);
        if (StringUtils.hasText(username)) {
            return username;
        }
        
        // 如果表单参数为空，尝试从JSON请求体获取
        try {
            Map<String, Object> jsonParams = getJsonParameters(request);
            if (jsonParams != null && jsonParams.containsKey(USERNAME_PARAM)) {
                Object value = jsonParams.get(USERNAME_PARAM);
                return value != null ? value.toString() : null;
            }
        } catch (Exception e) {
            log.debug("解析JSON参数失败，使用表单参数: {}", e.getMessage());
        }
        
        return null;
    }

    /**
     * 获取密码
     * 支持表单参数和JSON格式
     * 
     * @param request HTTP请求
     * @return 密码
     */
    protected String obtainPassword(HttpServletRequest request) {
        // 首先尝试从表单参数获取
        String password = request.getParameter(PASSWORD_PARAM);
        if (StringUtils.hasText(password)) {
            return password;
        }
        
        // 如果表单参数为空，尝试从JSON请求体获取
        try {
            Map<String, Object> jsonParams = getJsonParameters(request);
            if (jsonParams != null && jsonParams.containsKey(PASSWORD_PARAM)) {
                Object value = jsonParams.get(PASSWORD_PARAM);
                return value != null ? value.toString() : null;
            }
        } catch (Exception e) {
            log.debug("解析JSON参数失败，使用表单参数: {}", e.getMessage());
        }
        
        return null;
    }

    /**
     * 获取验证码
     * 支持表单参数和JSON格式
     * 
     * @param request HTTP请求
     * @return 验证码
     */
    protected String obtainCaptcha(HttpServletRequest request) {
        // 首先尝试从表单参数获取
        String captcha = request.getParameter(CAPTCHA_PARAM);
        if (StringUtils.hasText(captcha)) {
            return captcha;
        }
        
        // 如果表单参数为空，尝试从JSON请求体获取
        try {
            Map<String, Object> jsonParams = getJsonParameters(request);
            if (jsonParams != null && jsonParams.containsKey(CAPTCHA_PARAM)) {
                Object value = jsonParams.get(CAPTCHA_PARAM);
                return value != null ? value.toString() : null;
            }
        } catch (Exception e) {
            log.debug("解析JSON参数失败，使用表单参数: {}", e.getMessage());
        }
        
        return null;
    }

    /**
     * 获取验证码UUID
     * 支持表单参数和JSON格式
     * 
     * @param request HTTP请求
     * @return 验证码UUID
     */
    protected String obtainCaptchaUuid(HttpServletRequest request) {
        // 首先尝试从表单参数获取
        String captchaUuid = request.getParameter(CAPTCHA_UUID_PARAM);
        if (StringUtils.hasText(captchaUuid)) {
            return captchaUuid;
        }
        
        // 如果表单参数为空，尝试从JSON请求体获取
        try {
            Map<String, Object> jsonParams = getJsonParameters(request);
            if (jsonParams != null && jsonParams.containsKey(CAPTCHA_UUID_PARAM)) {
                Object value = jsonParams.get(CAPTCHA_UUID_PARAM);
                return value != null ? value.toString() : null;
            }
        } catch (Exception e) {
            log.debug("解析JSON参数失败，使用表单参数: {}", e.getMessage());
        }
        
        return null;
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

    /**
     * 从JSON请求体中获取参数
     * 
     * @param request HTTP请求
     * @return 参数Map
     * @throws IOException IO异常
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getJsonParameters(HttpServletRequest request) throws IOException {
        String contentType = request.getContentType();
        if (contentType != null && contentType.contains("application/json")) {
            StringBuilder jsonBuilder = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
            }
            
            String jsonString = jsonBuilder.toString();
            if (StringUtils.hasText(jsonString)) {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(jsonString, Map.class);
            }
        }
        return null;
    }
}

