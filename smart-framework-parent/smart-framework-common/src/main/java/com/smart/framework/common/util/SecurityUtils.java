package com.smart.framework.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Spring Security工具类
 * 提供获取当前登录用户信息和权限检查的便捷方法
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public class SecurityUtils {

    private static final Logger log = LoggerFactory.getLogger(SecurityUtils.class);

    /**
     * 获取当前认证对象
     * 
     * @return 认证对象，如果未登录返回null
     */
    public static Authentication getAuthentication() {
        try {
            return SecurityContextHolder.getContext().getAuthentication();
        } catch (Exception e) {
            log.warn("获取认证对象失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 获取当前登录用户详情
     * 
     * @return 用户详情，如果未登录返回null
     */
    public static UserDetails getCurrentUserDetails() {
        Authentication authentication = getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return (UserDetails) principal;
        }

        log.warn("当前认证主体不是UserDetails类型: {}", principal.getClass().getName());
        return null;
    }

    /**
     * 获取当前登录用户名
     * 
     * @return 用户名，如果未登录返回null
     */
    public static String getCurrentUsername() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        return authentication.getName();
    }

    /**
     * 获取当前登录用户ID
     * 
     * @return 用户ID，如果未登录返回null
     */
    public static String getCurrentUserId() {
        UserDetails userDetails = getCurrentUserDetails();
        if (userDetails == null) {
            return null;
        }

        // 如果UserDetails有getId方法，直接调用
        try {
            return (String) userDetails.getClass().getMethod("getId").invoke(userDetails);
        } catch (Exception e) {
            log.debug("UserDetails没有getId方法，尝试其他方式获取用户ID");
        }

        // 尝试通过反射获取ID字段
        try {
            java.lang.reflect.Field idField = userDetails.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            return (String) idField.get(userDetails);
        } catch (Exception e) {
            log.debug("无法通过反射获取用户ID字段");
        }

        log.warn("无法获取用户ID，UserDetails类型: {}", userDetails.getClass().getName());
        return null;
    }

    /**
     * 获取当前用户的所有权限
     * 
     * @return 权限列表，如果未登录返回空列表
     */
    public static List<String> getCurrentUserAuthorities() {
        UserDetails userDetails = getCurrentUserDetails();
        if (userDetails == null) {
            return List.of();
        }

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        if (CollectionUtils.isEmpty(authorities)) {
            return List.of();
        }

        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    /**
     * 检查当前用户是否拥有指定权限
     * 
     * @param permission 权限标识
     * @return 是否拥有权限
     */
    public static boolean hasPermission(String permission) {
        if (permission == null || permission.trim().isEmpty()) {
            return false;
        }

        List<String> authorities = getCurrentUserAuthorities();
        return authorities.contains(permission);
    }

    /**
     * 检查当前用户是否拥有任意一个指定权限
     * 
     * @param permissions 权限标识列表
     * @return 是否拥有任意一个权限
     */
    public static boolean hasAnyPermission(String... permissions) {
        if (permissions == null || permissions.length == 0) {
            return false;
        }

        List<String> authorities = getCurrentUserAuthorities();
        for (String permission : permissions) {
            if (authorities.contains(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查当前用户是否拥有所有指定权限
     * 
     * @param permissions 权限标识列表
     * @return 是否拥有所有权限
     */
    public static boolean hasAllPermissions(String... permissions) {
        if (permissions == null || permissions.length == 0) {
            return true;
        }

        List<String> authorities = getCurrentUserAuthorities();
        for (String permission : permissions) {
            if (!authorities.contains(permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查当前用户是否拥有指定角色
     * 
     * @param role 角色标识
     * @return 是否拥有角色
     */
    public static boolean hasRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            return false;
        }

        // 确保角色标识以ROLE_开头
        String roleWithPrefix = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        return hasPermission(roleWithPrefix);
    }

    /**
     * 检查当前用户是否拥有任意一个指定角色
     * 
     * @param roles 角色标识列表
     * @return 是否拥有任意一个角色
     */
    public static boolean hasAnyRole(String... roles) {
        if (roles == null || roles.length == 0) {
            return false;
        }

        for (String role : roles) {
            if (hasRole(role)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查当前用户是否拥有所有指定角色
     * 
     * @param roles 角色标识列表
     * @return 是否拥有所有角色
     */
    public static boolean hasAllRoles(String... roles) {
        if (roles == null || roles.length == 0) {
            return true;
        }

        for (String role : roles) {
            if (!hasRole(role)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查当前用户是否已认证
     * 
     * @return 是否已认证
     */
    public static boolean isAuthenticated() {
        Authentication authentication = getAuthentication();
        return authentication != null && authentication.isAuthenticated() 
                && !"anonymousUser".equals(authentication.getPrincipal());
    }

    /**
     * 检查当前用户是否为匿名用户
     * 
     * @return 是否为匿名用户
     */
    public static boolean isAnonymous() {
        Authentication authentication = getAuthentication();
        return authentication == null || 
                "anonymousUser".equals(authentication.getPrincipal());
    }

    /**
     * 获取当前用户的详细信息（如果支持）
     * 
     * @param <T> 用户详情类型
     * @param userDetailsClass 用户详情类型
     * @return 用户详情对象，如果类型不匹配返回null
     */
    @SuppressWarnings("unchecked")
    public static <T extends UserDetails> T getCurrentUserDetails(Class<T> userDetailsClass) {
        UserDetails userDetails = getCurrentUserDetails();
        if (userDetails == null) {
            return null;
        }

        if (userDetailsClass.isAssignableFrom(userDetails.getClass())) {
            return (T) userDetails;
        }

        log.warn("用户详情类型不匹配，期望: {}，实际: {}", 
                userDetailsClass.getName(), userDetails.getClass().getName());
        return null;
    }

    /**
     * 获取当前用户的租户ID（如果支持）
     * 
     * @return 租户ID，如果不支持返回null
     */
    public static String getCurrentTenantId() {
        UserDetails userDetails = getCurrentUserDetails();
        if (userDetails == null) {
            return null;
        }

        // 如果UserDetails有getTenantId方法，直接调用
        try {
            return (String) userDetails.getClass().getMethod("getTenantId").invoke(userDetails);
        } catch (Exception e) {
            log.debug("UserDetails没有getTenantId方法");
        }

        // 尝试通过反射获取租户ID字段
        try {
            java.lang.reflect.Field tenantIdField = userDetails.getClass().getDeclaredField("tenantId");
            tenantIdField.setAccessible(true);
            return (String) tenantIdField.get(userDetails);
        } catch (Exception e) {
            log.debug("无法通过反射获取租户ID字段");
        }

        return null;
    }

    /**
     * 获取当前用户的部门ID（如果支持）
     * 
     * @return 部门ID，如果不支持返回null
     */
    public static String getCurrentDeptId() {
        UserDetails userDetails = getCurrentUserDetails();
        if (userDetails == null) {
            return null;
        }

        // 如果UserDetails有getDeptId方法，直接调用
        try {
            return (String) userDetails.getClass().getMethod("getDeptId").invoke(userDetails);
        } catch (Exception e) {
            log.debug("UserDetails没有getDeptId方法");
        }

        // 尝试通过反射获取部门ID字段
        try {
            java.lang.reflect.Field deptIdField = userDetails.getClass().getDeclaredField("deptId");
            deptIdField.setAccessible(true);
            return (String) deptIdField.get(userDetails);
        } catch (Exception e) {
            log.debug("无法通过反射获取部门ID字段");
        }

        return null;
    }

    /**
     * 清除当前安全上下文
     * 主要用于登出操作
     */
    public static void clearSecurityContext() {
        try {
            SecurityContextHolder.clearContext();
            log.debug("安全上下文已清除");
        } catch (Exception e) {
            log.warn("清除安全上下文失败: {}", e.getMessage());
        }
    }

    /**
     * 检查当前用户是否为超级管理员
     * 
     * @return 是否为超级管理员
     */
    public static boolean isSuperAdmin() {
        return hasRole("super_admin") || hasRole("SUPER_ADMIN");
    }

    /**
     * 检查当前用户是否为管理员
     * 
     * @return 是否为管理员
     */
    public static boolean isAdmin() {
        return hasRole("admin") || hasRole("ADMIN") || isSuperAdmin();
    }

    /**
     * 获取当前用户的安全信息摘要
     * 用于日志记录和调试
     * 
     * @return 安全信息摘要
     */
    public static String getSecuritySummary() {
        if (!isAuthenticated()) {
            return "未认证用户";
        }

        String username = getCurrentUsername();
        String userId = getCurrentUserId();
        List<String> authorities = getCurrentUserAuthorities();
        String tenantId = getCurrentTenantId();

        return String.format("用户[%s, ID:%s, 租户:%s, 权限数量:%d]", 
                username, userId, tenantId, authorities.size());
    }
}
