package com.smart.framework.security.filter;

import com.smart.framework.security.cache.JwtTokenCacheService;
import com.smart.framework.security.service.CustomUserDetailsService;
import com.smart.framework.security.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JWT认证过滤器
 * 参考yuncheng项目的Token缓存机制
 * 实现Token过期时的缓存验证逻辑
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${login.no-filter:/actuator/**,/system/captcha/**,/system/auth/**}")
    private String noFilterUrls;

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final JwtTokenCacheService jwtTokenCacheService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService, 
                                  JwtTokenCacheService jwtTokenCacheService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.jwtTokenCacheService = jwtTokenCacheService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        try {
            String jwt = extractJwtFromRequest(request);
            
            if (StringUtils.isNotBlank(jwt)) {
                String username = jwtUtil.getUsernameFromToken(jwt);
                
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    
                    // 1. 首先检查Token是否有效（未过期）
                    if (jwtUtil.validateToken(jwt)) {
                        // Token有效，正常认证流程
                        authenticateUser(request, jwt, username);
                        log.debug("用户 {} Token有效，认证成功", username);
                        
                    } else {
                        // 2. Token已过期，检查缓存中是否存在
                        if (jwtTokenCacheService.isTokenCached(jwt)) {
                            // 3. Token过期但缓存中存在，表示用户一直在操作，清空用户信息要求重新登录
                            log.warn("用户 {} Token已过期但缓存中存在，清空用户信息要求重新登录", username);
                            clearUserAuthentication(username);
                            
                            // 返回401状态码，要求重新登录
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"code\":401,\"message\":\"用户信息已失效，请重新登录\"}");
                            return;
                            
                        } else {
                            // 4. Token过期且缓存中不存在，表示用户账户空闲超时
                            log.warn("用户 {} Token已过期且缓存中不存在，用户账户空闲超时", username);
                            clearUserAuthentication(username);
                            
                            // 返回401状态码，要求重新登录
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"code\":401,\"message\":\"用户信息已失效，请重新登录\"}");
                            return;
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("JWT认证过程中发生异常: {}", e.getMessage());
            SecurityContextHolder.clearContext();
            throw e;
        }
        
        filterChain.doFilter(request, response);
    }

    /**
     * 认证用户
     * 
     * @param request HTTP请求
     * @param jwt JWT Token
     * @param username 用户名
     */
    private void authenticateUser(HttpServletRequest request, String jwt, String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        
        if (jwtUtil.validateToken(jwt, username)) {
            // 获取用户角色
            String[] roles = jwtUtil.getRolesFromToken(jwt);
            List<SimpleGrantedAuthority> authorities = Arrays.stream(roles)
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());
            
            // 创建认证对象
            UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(
                    userDetails, null, authorities);
            
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // 刷新用户缓存过期时间（参考yuncheng项目）
            if (userDetailsService instanceof CustomUserDetailsService) {
                ((CustomUserDetailsService) userDetailsService).refreshUserCache(username);
            }
            
            log.debug("用户 {} 认证成功，角色: {}", username, Arrays.toString(roles));
        }
    }

    /**
     * 清空用户认证信息
     * 
     * @param username 用户名
     */
    private void clearUserAuthentication(String username) {
        // 清空Security上下文
        SecurityContextHolder.clearContext();
        
        // 清空用户缓存
        if (userDetailsService instanceof CustomUserDetailsService) {
            ((CustomUserDetailsService) userDetailsService).clearUserCache(username);
        }
        
        log.debug("用户 {} 认证信息已清空", username);
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

    /**
     * 检查请求是否应该跳过JWT验证
     * 
     * @param request HTTP请求
     * @return 是否跳过
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        log.info("请求路径: {}", path);

        // 跳过无需认证的路径，这里直接从 配置文件中的 no-filter 中获取
        // 检查是否在 no-filter 中
        if (StringUtils.isNotBlank(noFilterUrls)) {
            for (String noFilterUrl : noFilterUrls.split(",")) {
                String trimmedUrl = noFilterUrl.trim();
                if (isPathMatched(path, trimmedUrl)) {
                    log.info("路径 {} 匹配 no-filter 规则 {}，跳过JWT验证", path, trimmedUrl);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 检查路径是否匹配通配符模式
     * 支持 ** 和 * 通配符
     * 
     * @param path 请求路径
     * @param pattern 通配符模式
     * @return 是否匹配
     */
    private boolean isPathMatched(String path, String pattern) {
        if (StringUtils.isBlank(pattern)) {
            return false;
        }
        
        // 将通配符模式转换为正则表达式
        String regex = pattern
            .replace(".", "\\.")  // 转义点号
            .replace("*", ".*")    // * 匹配任意字符
            .replace("**", ".*");  // ** 匹配任意字符（包括路径分隔符）
        
        return path.matches(regex);
    }
}
