package com.smart.framework.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.framework.common.util.MenuTreeUtil;
import com.smart.framework.core.result.Result;
import com.smart.framework.security.cache.JwtTokenCacheService;
import com.smart.framework.security.dto.UserLoginResponseDto;
import com.smart.framework.security.entity.AuthSmartPermission;
import com.smart.framework.security.entity.AuthSmartUser;
import com.smart.framework.security.service.CustomUserDetailsService;
import com.smart.framework.security.util.JwtUtil;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * 自定义认证成功处理器
 * 处理用户登录成功后的逻辑
 * 参考yuncheng项目，登录成功后将JWT Token缓存到Redis
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenCacheService jwtTokenCacheService;

    /**
     * 构造函数
     * 
     * @param objectMapper JSON对象映射器
     * @param jwtUtil JWT工具类
     * @param customUserDetailsService 用户详情服务
     * @param jwtTokenCacheService JWT Token缓存服务
     */
    public CustomAuthenticationSuccessHandler(ObjectMapper objectMapper, JwtUtil jwtUtil, 
                                            CustomUserDetailsService customUserDetailsService,
                                            JwtTokenCacheService jwtTokenCacheService) {
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenCacheService = jwtTokenCacheService;
    }

    /**
     * 认证成功处理
     * 参考yuncheng项目，登录成功后将JWT Token缓存到Redis
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
        AuthSmartUser userDetails = (AuthSmartUser) authentication.getPrincipal();
        
        // 生成JWT令牌
        String token = jwtUtil.generateToken("", userDetails.getUsername());
        long expireTime = System.currentTimeMillis() + 86400000L; // 24小时后过期
        
        // 1. 登录成功后将用户的JWT生成的Token作为k、v存储到cache缓存里面
        // 缓存有效期设置为Jwt有效时间的2倍
        jwtTokenCacheService.cacheToken(token, 86400000L); // 24小时 * 2 = 48小时缓存
        
        log.info("JWT Token已缓存到Redis，用户：{}，Token：{}", userDetails.getUsername(), token);
        
        // 查询用户角色
        List<String> roles = customUserDetailsService.getUserRoles(userDetails.getId());
        
        // 查询用户权限
        List<String> permissions = customUserDetailsService.getUserPermissions(userDetails.getId());
        
        // 查询用户菜单权限
        List<AuthSmartPermission> flatMenus = customUserDetailsService.getUserMenus(userDetails.getId());
        
        // 将扁平菜单列表转换为树形结构
        List<AuthSmartPermission> menuTree = MenuTreeUtil.buildMenuTree(flatMenus);
        
        log.debug("用户菜单权限处理完成，扁平菜单数量：{}，树形菜单根节点数量：{}", 
                flatMenus.size(), menuTree.size());
        
        // 构建用户登录响应数据
        UserLoginResponseDto userLoginResponse = new UserLoginResponseDto();
        userLoginResponse.setId(userDetails.getId());
        userLoginResponse.setDeptId(userDetails.getDeptId());
        userLoginResponse.setUserName(userDetails.getUsername());
        userLoginResponse.setNickName(userDetails.getNickName());
        userLoginResponse.setUserType(userDetails.getUserType());
        userLoginResponse.setEmail(userDetails.getEmail());
        userLoginResponse.setPhonenumber(userDetails.getPhonenumber());
        userLoginResponse.setSex(userDetails.getSex());
        userLoginResponse.setAvatar(userDetails.getAvatar());
        userLoginResponse.setStatus(userDetails.getStatus());
        userLoginResponse.setLoginIp(userDetails.getLoginIp());
        userLoginResponse.setLoginDate(userDetails.getLoginDate());
        userLoginResponse.setRemark(userDetails.getRemark());
        userLoginResponse.setTenantId(userDetails.getTenantId());
        userLoginResponse.setToken(token);
        userLoginResponse.setExpireTime(expireTime);
        userLoginResponse.setRoles(roles);
        userLoginResponse.setPermissions(permissions);
        userLoginResponse.setMenus(menuTree);
        
        Result<UserLoginResponseDto> result = Result.success("登录成功", userLoginResponse);
        
        // 设置响应头
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        
        // 写入响应
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(objectMapper.writeValueAsBytes(result));
        outputStream.flush();
        outputStream.close();
        
        log.info("用户登录成功响应已发送：{}，角色数量：{}，权限数量：{}，树形菜单根节点数量：{}", 
                authentication.getName(), roles.size(), permissions.size(), menuTree.size());
    }
}
