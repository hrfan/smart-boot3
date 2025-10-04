package com.smart.framework.security.service;

import com.smart.framework.security.entity.AuthSmartUser;
import com.smart.framework.security.util.JwtUtil;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 自定义用户详情服务
 * 用于Spring Security认证和授权
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Resource
    private AuthSmartUserService authSmartUserService;
    /**
     * 根据用户名查询用户信息
     * 
     * @param username 用户名
     * @return 用户详情
     * @throws UsernameNotFoundException 用户名不存在异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("开始加载用户信息，用户名：{}", username);
        
        // 1. 参数校验
        if (username == null || username.trim().isEmpty()) {
            throw new UsernameNotFoundException("用户名不能为空");
        }
        
        // 2. 从数据库查询用户基本信息
        AuthSmartUser user = authSmartUserService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在：" + username);
        }
        
        // 3. 查询用户角色和权限
        List<String> permissions = getUserPermissions(user.getId());
        
        // 4. 构建UserDetails对象
        String[] permissionArray = permissions.toArray(new String[0]);
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(permissionArray);
        user.setAuthorities(authorities);
        
        log.debug("用户信息加载完成，用户名：{}，权限数量：{}", username, permissions.size());
        
        return user;
    }
    
    /**
     * 根据用户ID查询用户权限
     * 
     * @param userId 用户ID
     * @return 权限列表
     */
    public List<String> getUserPermissions(String userId) {
        log.debug("查询用户权限，用户ID：{}", userId);
        
        // 1. 查询用户角色
        // 2. 查询角色对应的权限
        // 3. 返回权限代码列表
        List<String> permissions = authSmartUserService.findByUserId(userId);
        
        // 后续可能需要归并多个权限来源 如 租户、角色、用户本身的权限
        return permissions;
    }
    
    /**
     * 根据用户ID查询用户角色
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    public List<String> getUserRoles(String userId) {
        log.debug("查询用户角色，用户ID：{}", userId);
        
        // 1. 查询用户角色关联表
        // 2. 返回角色代码列表
        
        return List.of();
    }
}

