package com.smart.framework.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.framework.core.result.Result;
import com.smart.framework.core.result.ResultCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 自定义访问拒绝处理器
 * 使用统一的Result响应格式
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, 
                      AccessDeniedException accessDeniedException) throws IOException, ServletException {
        
        log.warn("访问被拒绝: {} - {}", request.getRequestURI(), accessDeniedException.getMessage());
        
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        
        // 使用统一的Result格式
        Result<Void> result = Result.error(ResultCode.FORBIDDEN.getCode(), "访问被拒绝，权限不足");
        
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
