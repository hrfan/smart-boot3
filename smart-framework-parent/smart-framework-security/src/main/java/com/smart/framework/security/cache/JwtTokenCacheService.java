package com.smart.framework.security.cache;

import com.smart.framework.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * JWT Token缓存服务
 * 参考yuncheng项目的Token缓存机制
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class JwtTokenCacheService {

    private final RedisService redisService;
    private final UserCacheConfig cacheConfig;

    /**
     * Token缓存key前缀
     */
    private static final String TOKEN_CACHE_PREFIX = "smart:jwt:token:";

    /**
     * 缓存JWT Token
     * 登录成功后将用户的JWT生成的Token作为k、v存储到cache缓存里面
     * 缓存有效期设置为Jwt有效时间的2倍
     * 
     * @param token JWT Token
     * @param jwtExpirationMillis JWT过期时间（毫秒）
     */
    public void cacheToken(String token, long jwtExpirationMillis) {
        if (!cacheConfig.isEnabled()) {
            log.debug("JWT Token缓存功能已禁用，跳过缓存: {}", token);
            return;
        }

        try {
            String key = TOKEN_CACHE_PREFIX + token;
            // 缓存有效期设置为JWT有效时间的2倍
            long cacheExpirationMillis = jwtExpirationMillis * 2;
            redisService.set(key, token, cacheExpirationMillis, TimeUnit.MILLISECONDS);
            log.debug("JWT Token已缓存到Redis: {}, 过期时间: {}ms", token, cacheExpirationMillis);
        } catch (Exception e) {
            log.error("缓存JWT Token到Redis失败: {}", token, e);
        }
    }

    /**
     * 检查Token是否在缓存中存在
     * 当该用户这次请求jwt生成的token值已经超时，但该token对应cache中的k还是存在，
     * 则表示该用户一直在操作只是JWT的token失效了
     * 
     * @param token JWT Token
     * @return 是否存在
     */
    public boolean isTokenCached(String token) {
        if (!cacheConfig.isEnabled()) {
            log.debug("JWT Token缓存功能已禁用，跳过缓存查询: {}", token);
            return false;
        }

        try {
            String key = TOKEN_CACHE_PREFIX + token;
            String cachedToken = redisService.get(key, String.class);
            boolean exists = cachedToken != null;
            log.debug("JWT Token缓存查询结果: {}, 存在: {}", token, exists);
            return exists;
        } catch (Exception e) {
            log.error("从Redis缓存查询JWT Token失败: {}", token, e);
            return false;
        }
    }

    /**
     * 获取缓存中的Token
     * 
     * @param token JWT Token
     * @return 缓存中的Token
     */
    public String getCachedToken(String token) {
        if (!cacheConfig.isEnabled()) {
            log.debug("JWT Token缓存功能已禁用，跳过缓存获取: {}", token);
            return null;
        }

        try {
            String key = TOKEN_CACHE_PREFIX + token;
            String cachedToken = redisService.get(key, String.class);
            log.debug("JWT Token缓存获取结果: {}, 缓存值: {}", token, cachedToken);
            return cachedToken;
        } catch (Exception e) {
            log.error("从Redis缓存获取JWT Token失败: {}", token, e);
            return null;
        }
    }

    /**
     * 刷新Token缓存
     * 程序会给token对应的k映射的v值重新生成JWTToken并覆盖v值，该缓存生命周期重新计算
     * 
     * @param oldToken 旧Token
     * @param newToken 新Token
     * @param jwtExpirationMillis JWT过期时间（毫秒）
     */
    public void refreshTokenCache(String oldToken, String newToken, long jwtExpirationMillis) {
        if (!cacheConfig.isEnabled()) {
            log.debug("JWT Token缓存功能已禁用，跳过Token刷新: {}", oldToken);
            return;
        }

        try {
            // 删除旧Token缓存
            removeTokenCache(oldToken);
            
            // 缓存新Token
            cacheToken(newToken, jwtExpirationMillis);
            
            log.debug("JWT Token缓存已刷新: {} -> {}", oldToken, newToken);
        } catch (Exception e) {
            log.error("刷新JWT Token缓存失败: {} -> {}", oldToken, newToken, e);
        }
    }

    /**
     * 移除Token缓存
     * 当该用户这次请求jwt在生成的token值已经超时，并在cache中不存在对应的k，
     * 则表示该用户账户空闲超时，返回用户信息已失效，请重新登录
     * 
     * @param token JWT Token
     */
    public void removeTokenCache(String token) {
        if (!cacheConfig.isEnabled()) {
            log.debug("JWT Token缓存功能已禁用，跳过Token删除: {}", token);
            return;
        }

        try {
            String key = TOKEN_CACHE_PREFIX + token;
            redisService.delete(key);
            log.debug("JWT Token缓存已删除: {}", token);
        } catch (Exception e) {
            log.error("删除JWT Token缓存失败: {}", token, e);
        }
    }

    /**
     * 清除用户的所有Token缓存
     * 用户登出时调用
     * 
     * @param username 用户名
     */
    public void clearUserTokenCache(String username) {
        if (!cacheConfig.isEnabled()) {
            log.debug("JWT Token缓存功能已禁用，跳过用户Token清理: {}", username);
            return;
        }

        try {
            // 这里可以根据实际需求实现，比如通过用户名前缀删除所有相关Token
            // 由于Token是随机生成的，这里暂时记录日志
            log.debug("用户Token缓存清理: {}", username);
        } catch (Exception e) {
            log.error("清理用户Token缓存失败: {}", username, e);
        }
    }
}
