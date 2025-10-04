package com.smart.framework.security.cache;

import com.smart.framework.redis.service.RedisService;
import com.smart.framework.security.entity.AuthSmartUser;
import com.smart.framework.security.entity.CacheableUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用户信息缓存服务
 * 参考yuncheng项目的缓存机制，避免重复查询数据库
 * 使用Redis缓存，支持分布式部署
 * 解决AuthSmartUser序列化问题
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserCacheService {

    private final RedisService redisService;
    private final UserCacheConfig cacheConfig;

    /**
     * 缓存用户信息
     * 使用CacheableUser避免序列化问题
     * 
     * @param username 用户名
     * @param user 用户信息
     */
    public void cacheUser(String username, AuthSmartUser user) {
        if (!cacheConfig.isEnabled()) {
            log.debug("用户缓存功能已禁用，跳过缓存: {}", username);
            return;
        }
        
        try {
            String key = cacheConfig.getKeyPrefix() + username;
            // 转换为可序列化的用户对象
            CacheableUser cacheableUser = CacheableUser.fromAuthSmartUser(user);
            redisService.set(key, cacheableUser, cacheConfig.getUserExpireMinutes(), TimeUnit.MINUTES);
            log.debug("用户信息已缓存到Redis: {}", username);
        } catch (Exception e) {
            log.error("缓存用户信息到Redis失败: {}", username, e);
        }
    }

    /**
     * 从缓存获取用户信息
     * 
     * @param username 用户名
     * @return 用户信息，如果不存在返回null
     */
    public AuthSmartUser getCachedUser(String username) {
        if (!cacheConfig.isEnabled()) {
            log.debug("用户缓存功能已禁用，跳过缓存查询: {}", username);
            return null;
        }
        
        try {
            String key = cacheConfig.getKeyPrefix() + username;
            CacheableUser cacheableUser = redisService.get(key, CacheableUser.class);
            if (cacheableUser != null) {
                log.debug("从Redis缓存获取用户信息: {}", username);
                // 转换为AuthSmartUser对象
                return cacheableUser.toAuthSmartUser();
            }
            return null;
        } catch (Exception e) {
            log.error("从Redis缓存获取用户信息失败: {}", username, e);
            return null;
        }
    }

    /**
     * 缓存用户权限信息
     * 
     * @param username 用户名
     * @param permissions 权限列表
     */
    public void cacheUserPermissions(String username, List<String> permissions) {
        if (!cacheConfig.isEnabled()) {
            log.debug("用户缓存功能已禁用，跳过权限缓存: {}", username);
            return;
        }
        
        try {
            String key = cacheConfig.getPermissionKeyPrefix() + username;
            redisService.set(key, permissions, cacheConfig.getPermissionExpireMinutes(), TimeUnit.MINUTES);
            log.debug("用户权限已缓存到Redis: {}", username);
        } catch (Exception e) {
            log.error("缓存用户权限到Redis失败: {}", username, e);
        }
    }

    /**
     * 从缓存获取用户权限信息
     * 
     * @param username 用户名
     * @return 权限列表，如果不存在返回null
     */
    @SuppressWarnings("unchecked")
    public List<String> getCachedUserPermissions(String username) {
        if (!cacheConfig.isEnabled()) {
            log.debug("用户缓存功能已禁用，跳过权限缓存查询: {}", username);
            return null;
        }
        
        try {
            String key = cacheConfig.getPermissionKeyPrefix() + username;
            List<String> permissions = redisService.get(key, List.class);
            if (permissions != null) {
                log.debug("从Redis缓存获取用户权限: {}", username);
            }
            return permissions;
        } catch (Exception e) {
            log.error("从Redis缓存获取用户权限失败: {}", username, e);
            return null;
        }
    }

    /**
     * 清除用户缓存
     * 
     * @param username 用户名
     */
    public void clearUserCache(String username) {
        try {
            String userKey = cacheConfig.getKeyPrefix() + username;
            String permissionKey = cacheConfig.getPermissionKeyPrefix() + username;
            
            redisService.delete(userKey);
            redisService.delete(permissionKey);
            
            log.debug("用户缓存已从Redis清除: {}", username);
        } catch (Exception e) {
            log.error("从Redis清除用户缓存失败: {}", username, e);
        }
    }

    /**
     * 清除所有用户缓存
     */
    public void clearAllUserCache() {
        try {
            // 注意：这里需要根据实际情况实现批量删除
            // 可以使用Redis的SCAN命令或者维护一个用户列表
            log.warn("清除所有用户缓存功能需要根据实际Redis配置实现");
        } catch (Exception e) {
            log.error("清除所有用户缓存失败", e);
        }
    }

    /**
     * 检查用户是否在缓存中
     * 
     * @param username 用户名
     * @return 是否在缓存中
     */
    public boolean isUserCached(String username) {
        if (!cacheConfig.isEnabled()) {
            return false;
        }
        
        try {
            String key = cacheConfig.getKeyPrefix() + username;
            return Boolean.TRUE.equals(redisService.exists(key));
        } catch (Exception e) {
            log.error("检查用户缓存状态失败: {}", username, e);
            return false;
        }
    }

    /**
     * 刷新用户缓存过期时间
     * 参考yuncheng项目的token刷新机制
     * 
     * @param username 用户名
     */
    public void refreshUserCache(String username) {
        if (!cacheConfig.isEnabled()) {
            log.debug("用户缓存功能已禁用，跳过缓存刷新: {}", username);
            return;
        }
        
        try {
            String userKey = cacheConfig.getKeyPrefix() + username;
            String permissionKey = cacheConfig.getPermissionKeyPrefix() + username;
            
            redisService.expire(userKey, cacheConfig.getUserExpireMinutes(), TimeUnit.MINUTES);
            redisService.expire(permissionKey, cacheConfig.getPermissionExpireMinutes(), TimeUnit.MINUTES);
            
            log.debug("用户缓存已刷新: {}", username);
        } catch (Exception e) {
            log.error("刷新用户缓存失败: {}", username, e);
        }
    }

    /**
     * 获取缓存统计信息
     * 
     * @return 缓存统计信息
     */
    public String getCacheStats() {
        try {
            StringBuilder stats = new StringBuilder();
            stats.append("Redis缓存服务运行正常\n");
            stats.append("缓存功能状态: ").append(cacheConfig.isEnabled() ? "启用" : "禁用").append("\n");
            stats.append("用户缓存过期时间: ").append(cacheConfig.getUserExpireMinutes()).append("分钟\n");
            stats.append("权限缓存过期时间: ").append(cacheConfig.getPermissionExpireMinutes()).append("分钟\n");
            stats.append("缓存key前缀: ").append(cacheConfig.getKeyPrefix()).append("\n");
            return stats.toString();
        } catch (Exception e) {
            log.error("获取缓存统计信息失败", e);
            return "Redis缓存服务异常";
        }
    }
}
