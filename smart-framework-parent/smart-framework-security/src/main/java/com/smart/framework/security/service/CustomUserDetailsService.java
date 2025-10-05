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
        
        // 5. 过滤空权限值，避免AuthorityUtils.createAuthorityList异常
        permissions = permissions.stream()
                .filter(perm -> StringUtil.isNotBlank(perm))
                .collect(java.util.stream.Collectors.toList());
        
        // 6. 构建UserDetails对象
        String[] permissionArray = permissions.toArray(new String[0]);
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(permissionArray);
        user.setAuthorities(authorities);
        
        // 注意：此时用户还未通过密码验证，不应该缓存用户信息
        // 缓存将在认证成功后由认证成功处理器处理
        
        log.debug("用户信息加载完成，用户名：{}，权限数量：{}", username, permissions.size());
        
        return user;
    }
    
    /**
     * 根据用户ID查询用户权限
     * 注意：此方法在认证过程中调用，不应该缓存权限信息
     * 
     * @param userId 用户ID
     * @return 权限列表
     */
    public List<String> getUserPermissions(String userId) {
        log.debug("查询用户权限，用户ID：{}", userId);
        
        // 直接从数据库查询权限，不进行缓存
        // 缓存将在认证成功后由认证成功处理器处理
        List<String> permissions = authSmartUserService.findByUserId(userId);
        
        log.debug("用户权限查询完成，用户ID：{}，权限数量：{}", userId, permissions.size());
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
    
    /**
     * 认证成功后缓存用户信息
     * 此方法应在用户通过密码验证后调用
     * 
     * @param username 用户名
     */
    public void cacheUserAfterAuthentication(String username) {
        log.debug("开始缓存认证成功后的用户信息，用户名：{}", username);
        
        // 1. 查询用户基本信息
        AuthSmartUser user = authSmartUserService.findByUsername(username);
        if (user == null) {
            log.warn("用户不存在，无法缓存：{}", username);
            return;
        }
        
        // 2. 查询用户权限
        List<String> permissions = authSmartUserService.findByUserId(user.getId());
        permissions = permissions.stream()
                .filter(perm -> StringUtil.isNotBlank(perm))
                .collect(java.util.stream.Collectors.toList());
        
        // 3. 构建权限列表
        String[] permissionArray = permissions.toArray(new String[0]);
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(permissionArray);
        user.setAuthorities(authorities);
        
        // 4. 缓存用户信息
        userCacheService.cacheUser(username, user);
        
        // 5. 缓存权限信息
        userCacheService.cacheUserPermissions(username, permissions);
        
        log.debug("认证成功后用户信息缓存完成，用户名：{}，权限数量：{}", username, permissions.size());
    }
}

