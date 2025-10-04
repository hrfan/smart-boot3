package com.smart.framework.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.framework.core.result.Result;
import com.smart.framework.security.cache.JwtTokenCacheService;
import com.smart.framework.security.util.JwtUtil;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * 自定义登出成功处理器
 * 处理用户登出成功后的逻辑
 * 参考yuncheng项目，登出时清除JWT Token缓存
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomLogoutSuccessHandler.class);

    private final ObjectMapper objectMapper;
    private final JwtTokenCacheService jwtTokenCacheService;
    private final JwtUtil jwtUtil;

    /**
     * 构造函数
     * 
     * @param objectMapper JSON对象映射器
     * @param jwtTokenCacheService JWT Token缓存服务
     * @param jwtUtil JWT工具类
     */
    public CustomLogoutSuccessHandler(ObjectMapper objectMapper, 
                                    JwtTokenCacheService jwtTokenCacheService,
                                    JwtUtil jwtUtil) {
        this.objectMapper = objectMapper;
        this.jwtTokenCacheService = jwtTokenCacheService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 登出成功处理
     * 参考yuncheng项目，登出时清除JWT Token缓存
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
        
        // 从请求中提取JWT Token
        String jwt = extractJwtFromRequest(request);
        
        if (StringUtils.hasText(jwt)) {
            // 清除JWT Token缓存
            jwtTokenCacheService.removeTokenCache(jwt);
            log.info("JWT Token缓存已清除，用户：{}，Token：{}", username, jwt);
        }
        
        // 清除用户的所有Token缓存
        jwtTokenCacheService.clearUserTokenCache(username);
        
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

    /**
     * 从请求中提取JWT Token
     * 
     * @param request HTTP请求
     * @return JWT Token
     */
    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return jwtUtil.extractTokenFromHeader(bearerToken);
    }
}

