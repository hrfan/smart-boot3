package com.smart.common.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.common.core.result.Result;
import com.smart.common.core.result.ResultCode;
import com.smart.common.security.util.JwtUtil;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义认证成功处理器
 * 处理用户登录成功后的逻辑
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;

    /**
     * 构造函数
     * 
     * @param objectMapper JSON对象映射器
     * @param jwtUtil JWT工具类
     */
    public CustomAuthenticationSuccessHandler(ObjectMapper objectMapper, JwtUtil jwtUtil) {
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 认证成功处理
     * 
     * @param request HTTP请求
     * @param response HTTP响应
     * @param authentication 认证对象
     * @throws IOException IO异常
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("用户登录成功：{}", authentication.getName());
        
        // 获取用户信息
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        // TODO: 生成JWT令牌
        // 1. 使用JwtUtil生成访问令牌
        // 2. 生成刷新令牌（可选）
        // 3. 设置令牌过期时间
        
        String token = jwtUtil.generateToken("", userDetails.getUsername());
        long expireTime = System.currentTimeMillis() + 86400000L; // 24小时后过期
        
        // TODO: 将令牌存储到Redis缓存
        // 1. 存储访问令牌
        // 2. 存储用户会话信息
        // 3. 设置过期时间
        
        // 构建响应数据
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("expireTime", expireTime);
        data.put("username", userDetails.getUsername());
        data.put("authorities", userDetails.getAuthorities());
        
        // TODO: 添加用户详细信息
        // data.put("userInfo", userInfo);
        // data.put("permissions", permissions);
        // data.put("roles", roles);
        
        Result<Map<String, Object>> result = Result.success("登录成功", data);
        
        // 设置响应头
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        
        // 写入响应
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(objectMapper.writeValueAsBytes(result));
        outputStream.flush();
        outputStream.close();
        
        log.info("用户登录成功响应已发送：{}", authentication.getName());
    }
}
