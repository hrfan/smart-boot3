package com.smart.framework.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.framework.core.result.Result;
import com.smart.framework.core.result.ResultCode;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 自定义认证失败处理器
 * 处理用户登录失败后的逻辑
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);

    private final ObjectMapper objectMapper;

    /**
     * 构造函数
     * 
     * @param objectMapper JSON对象映射器
     */
    public CustomAuthenticationFailureHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 认证失败处理
     * 
     * @param request HTTP请求
     * @param response HTTP响应
     * @param exception 认证异常
     * @throws IOException IO异常
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        log.warn("用户登录失败：{}", exception.getMessage());
        
        String errorMessage = "登录失败";
        int errorCode = HttpServletResponse.SC_UNAUTHORIZED;
        
        // 根据异常类型设置不同的错误信息
        if (exception instanceof AccountExpiredException) {
            errorMessage = "账户已过期，请联系管理员";
        } else if (exception instanceof BadCredentialsException) {
            errorMessage = "用户名或密码错误";
        } else if (exception instanceof CredentialsExpiredException) {
            errorMessage = "密码已过期，请修改密码";
        } else if (exception instanceof DisabledException) {
            errorMessage = "账户已被禁用，请联系管理员";
        } else if (exception instanceof LockedException) {
            errorMessage = "账户已被锁定，请联系管理员";
        } else if (exception instanceof InternalAuthenticationServiceException) {
            errorMessage = StringUtils.isBlank(exception.getMessage()) ? "账户不存在" : exception.getMessage();
        } else if (exception instanceof ProviderNotFoundException) {
            errorMessage = "认证服务不可用";
        } else {
            errorMessage = StringUtils.isBlank(exception.getMessage()) ? "登录失败" : exception.getMessage();
        }
        
        // TODO: 记录登录失败日志
        // 1. 记录失败原因
        // 2. 记录IP地址
        // 3. 记录用户代理信息
        // 4. 实现登录失败次数限制
        
        // TODO: 验证码相关处理
        // 1. 验证码错误处理
        // 2. 验证码过期处理
        
        // 构建错误响应
        Result<Void> result = Result.error(errorCode, errorMessage);
        
        // 设置响应头
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorCode);
        
        // 写入响应
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(objectMapper.writeValueAsBytes(result));
        outputStream.flush();
        outputStream.close();
        
        log.warn("用户登录失败响应已发送：{}", errorMessage);
    }
}
