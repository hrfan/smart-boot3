package com.smart.framework.security.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 用户缓存配置类
 * 配置用户信息缓存的各项参数
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
@Configuration
@ConfigurationProperties(prefix = "smart.security.cache")
public class UserCacheConfig {

    /**
     * 是否启用用户缓存
     */
    private boolean enabled = true;

    /**
     * 用户信息缓存过期时间（分钟）
     */
    private long userExpireMinutes = 30;

    /**
     * 用户权限缓存过期时间（分钟）
     */
    private long permissionExpireMinutes = 30;

    /**
     * 缓存key前缀
     */
    private String keyPrefix = "smart:user:cache:";

    /**
     * 权限缓存key前缀
     */
    private String permissionKeyPrefix = "smart:user:permission:";

    /**
     * 是否启用缓存预热
     */
    private boolean enablePreload = false;

    /**
     * 缓存预热延迟时间（秒）
     */
    private long preloadDelaySeconds = 10;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public long getUserExpireMinutes() {
        return userExpireMinutes;
    }

    public void setUserExpireMinutes(long userExpireMinutes) {
        this.userExpireMinutes = userExpireMinutes;
    }

    public long getPermissionExpireMinutes() {
        return permissionExpireMinutes;
    }

    public void setPermissionExpireMinutes(long permissionExpireMinutes) {
        this.permissionExpireMinutes = permissionExpireMinutes;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public String getPermissionKeyPrefix() {
        return permissionKeyPrefix;
    }

    public void setPermissionKeyPrefix(String permissionKeyPrefix) {
        this.permissionKeyPrefix = permissionKeyPrefix;
    }

    public boolean isEnablePreload() {
        return enablePreload;
    }

    public void setEnablePreload(boolean enablePreload) {
        this.enablePreload = enablePreload;
    }

    public long getPreloadDelaySeconds() {
        return preloadDelaySeconds;
    }

    public void setPreloadDelaySeconds(long preloadDelaySeconds) {
        this.preloadDelaySeconds = preloadDelaySeconds;
    }
}
