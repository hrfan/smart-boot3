package com.smart.framework.security.filter;

import com.smart.framework.security.service.CustomUserDetailsService;
import com.smart.framework.security.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JWT认证过滤器
 * 使用现代Java特性和Spring Security 6兼容版本
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        try {
            String jwt = extractJwtFromRequest(request);
            
            if (StringUtils.hasText(jwt) && jwtUtil.validateToken(jwt)) {
                String username = jwtUtil.getUsernameFromToken(jwt);
                
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
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
            }
        } catch (Exception e) {
            log.error("JWT认证过程中发生异常: {}", e.getMessage());
            SecurityContextHolder.clearContext();
            throw e;
        }
        
        filterChain.doFilter(request, response);
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
        
        // 跳过公开端点
        return path.startsWith("/api/auth/") ||
               path.startsWith("/system/auth/") ||
               path.startsWith("/api/public/") ||
               path.startsWith("/swagger-ui/") ||
               path.startsWith("/v3/api-docs/") ||
               path.startsWith("/actuator/") ||
               path.equals("/favicon.ico") ||
               path.startsWith("/health/") ||
               path.startsWith("/hello/");
    }
}
