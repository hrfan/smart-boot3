package com.smart.framework.security.service;

import com.smart.framework.core.util.StringUtil;
import com.smart.framework.security.cache.UserCacheService;
import com.smart.framework.security.entity.AuthSmartPermission;
import com.smart.framework.security.entity.AuthSmartUser;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 自定义用户详情服务
 * 用于Spring Security认证和授权
 * 参考yuncheng项目的缓存机制，避免重复查询数据库
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
    
    @Resource
    private UserCacheService userCacheService;

    /**
     * 根据用户名查询用户信息
     * 优先从缓存获取，缓存未命中时查询数据库并缓存结果
     * 
     * @param username 用户名
     * @return 用户详情
     * @throws UsernameNotFoundException 用户名不存在异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("开始加载用户信息，用户名：{}", username);
        
        // 1. 参数校验
        if (StringUtil.isBlank(username)) {
            throw new UsernameNotFoundException("用户名不能为空");
        }
        
        // 2. 优先从缓存获取用户信息
        AuthSmartUser cachedUser = userCacheService.getCachedUser(username);
        if (cachedUser != null) {
            log.debug("从缓存获取用户信息成功，用户名：{}", username);
            return cachedUser;
        }
        
        // 3. 缓存未命中，从数据库查询用户基本信息
        AuthSmartUser user = authSmartUserService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在：" + username);
        }
        
        // 4. 查询用户角色和权限
        List<String> permissions = getUserPermissions(user.getId());
        
        // 5. 构建UserDetails对象
        String[] permissionArray = permissions.toArray(new String[0]);
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(permissionArray);
        user.setAuthorities(authorities);
        
        // 6. 将用户信息缓存到Redis
        userCacheService.cacheUser(username, user);
        
        log.debug("用户信息加载完成并已缓存，用户名：{}，权限数量：{}", username, permissions.size());
        
        return user;
    }
    
    /**
     * 根据用户ID查询用户权限
     * 优先从缓存获取，缓存未命中时查询数据库并缓存结果
     * 
     * @param userId 用户ID
     * @return 权限列表
     */
    public List<String> getUserPermissions(String userId) {
        log.debug("查询用户权限，用户ID：{}", userId);
        
        // 1. 优先从缓存获取权限信息
        AuthSmartUser user = authSmartUserService.findByUsername(userId);
        if (user != null) {
            List<String> cachedPermissions = userCacheService.getCachedUserPermissions(user.getUsername());
            if (cachedPermissions != null) {
                log.debug("从缓存获取用户权限成功，用户ID：{}", userId);
                return cachedPermissions;
            }
        }
        
        // 2. 缓存未命中，从数据库查询权限
        List<String> permissions = authSmartUserService.findByUserId(userId);
        
        // 3. 将权限信息缓存到Redis
        if (user != null) {
            userCacheService.cacheUserPermissions(user.getUsername(), permissions);
        }
        
        log.debug("用户权限查询完成并已缓存，用户ID：{}，权限数量：{}", userId, permissions.size());
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
        List<String> roles = authSmartUserService.findRolesByUserId(userId);
        
        log.debug("用户角色查询完成，用户ID：{}，角色数量：{}", userId, roles.size());
        return roles;
    }
    
    /**
     * 根据用户ID查询用户菜单权限
     * 
     * @param userId 用户ID
     * @return 菜单权限列表
     */
    public List<AuthSmartPermission> getUserMenus(String userId) {
        log.debug("查询用户菜单权限，用户ID：{}", userId);
        
        // 1. 查询用户菜单权限
        // 2. 返回菜单权限列表
        List<AuthSmartPermission> menus = authSmartUserService.findMenusByUserId(userId);
        
        log.debug("用户菜单权限查询完成，用户ID：{}，菜单数量：{}", userId, menus.size());
        return menus;
    }
    
    /**
     * 清除用户缓存
     * 当用户信息发生变更时调用此方法
     * 
     * @param username 用户名
     */
    public void clearUserCache(String username) {
        userCacheService.clearUserCache(username);
        log.debug("用户缓存已清除，用户名：{}", username);
    }
    
    /**
     * 刷新用户缓存
     * 延长用户缓存的过期时间
     * 
     * @param username 用户名
     */
    public void refreshUserCache(String username) {
        userCacheService.refreshUserCache(username);
        log.debug("用户缓存已刷新，用户名：{}", username);
    }
}

