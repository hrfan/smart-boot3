package com.smart.common.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.common.core.result.Result;
import com.smart.common.core.result.ResultCode;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 自定义登出成功处理器
 * 处理用户登出成功后的逻辑
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomLogoutSuccessHandler.class);

    private final ObjectMapper objectMapper;

    /**
     * 构造函数
     * 
     * @param objectMapper JSON对象映射器
     */
    public CustomLogoutSuccessHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 登出成功处理
     * 
     * @param request HTTP请求
     * @param response HTTP响应
     * @param authentication 认证对象
     * @throws IOException IO异常
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String username = authentication != null ? authentication.getName() : "未知用户";
        log.info("用户登出成功：{}", username);
        
        // TODO: 清理用户会话信息
        // 1. 从Redis中删除用户令牌
        // 2. 清理用户会话缓存
        // 3. 记录登出日志
        
        // TODO: 获取JWT令牌并使其失效
        // 1. 从请求头或参数中获取令牌
        // 2. 将令牌加入黑名单
        // 3. 清理相关缓存
        
        // TODO: 记录登出日志
        // 1. 记录登出时间
        // 2. 记录IP地址
        // 3. 记录登出原因
        
        // 构建成功响应
        Result<Void> result = Result.success("登出成功", null);
        
        // 设置响应头
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        
        // 写入响应
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(objectMapper.writeValueAsBytes(result));
        outputStream.flush();
        outputStream.close();
        
        log.info("用户登出成功响应已发送：{}", username);
    }
}

