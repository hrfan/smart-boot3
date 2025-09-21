package com.smart.common.database.config;

import com.smart.common.database.enums.DatabaseType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 数据库配置类
 * 支持MySQL和PostgreSQL数据库配置
 *
 * @author Smart Boot3
 * @since 1.0.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "smart.database")
public class DatabaseConfig {

    /**
     * 数据库类型
     */
    private DatabaseType type = DatabaseType.MYSQL;

    /**
     * 数据库连接URL
     */
    private String url;

    /**
     * 数据库用户名
     */
    private String username;

    /**
     * 数据库密码
     */
    private String password;

    /**
     * 数据库名称
     */
    private String databaseName;

    /**
     * 连接池配置
     */
    private PoolConfig pool = new PoolConfig();

    /**
     * 批量操作配置
     */
    private BulkConfig bulk = new BulkConfig();

    /**
     * 连接池配置
     */
    @Data
    public static class PoolConfig {
        /**
         * 初始连接数
         */
        private int initialSize = 5;

        /**
         * 最小空闲连接数
         */
        private int minIdle = 5;

        /**
         * 最大活跃连接数
         */
        private int maxActive = 20;

        /**
         * 获取连接最大等待时间（毫秒）
         */
        private long maxWait = 60000;

        /**
         * 连接超时时间（毫秒）
         */
        private int connectionTimeout = 30000;

        /**
         * 空闲连接回收时间（毫秒）
         */
        private int timeBetweenEvictionRunsMillis = 60000;

        /**
         * 连接最小空闲时间（毫秒）
         */
        private int minEvictableIdleTimeMillis = 300000;

        /**
         * 是否开启连接泄漏检测
         */
        private boolean removeAbandoned = true;

        /**
         * 连接泄漏超时时间（毫秒）
         */
        private int removeAbandonedTimeout = 1800;

        /**
         * 是否开启SQL监控
         */
        private boolean enableSqlMonitor = true;

        /**
         * 是否开启慢SQL监控
         */
        private boolean enableSlowSqlMonitor = true;

        /**
         * 慢SQL阈值（毫秒）
         */
        private int slowSqlThreshold = 2000;
    }

    /**
     * 批量操作配置
     */
    @Data
    public static class BulkConfig {
        /**
         * 默认批量大小
         */
        private int defaultBatchSize = 1000;

        /**
         * 最大批量大小
         */
        private int maxBatchSize = 10000;

        /**
         * 是否启用线程池
         */
        private boolean enableThreadPool = false;

        /**
         * 线程池核心线程数
         */
        private int corePoolSize = 5;

        /**
         * 线程池最大线程数
         */
        private int maxPoolSize = 10;

        /**
         * 线程池队列大小
         */
        private int queueCapacity = 100;

        /**
         * 线程池空闲时间（秒）
         */
        private int keepAliveSeconds = 60;

        /**
         * 是否启用异步批量处理
         */
        private boolean enableAsync = false;

        /**
         * 批量操作超时时间（毫秒）
         */
        private long timeoutMillis = 300000;
    }

    /**
     * 获取数据库类型
     * 如果未设置，则根据URL自动判断
     *
     * @return 数据库类型
     */
    public DatabaseType getDatabaseType() {
        if (type != null) {
            return type;
        }

        if (url != null && !url.isEmpty()) {
            return DatabaseType.fromUrl(url);
        }

        return DatabaseType.MYSQL;
    }

    /**
     * 判断是否为MySQL数据库
     *
     * @return 是否为MySQL数据库
     */
    public boolean isMysql() {
        return getDatabaseType().isMysql();
    }

    /**
     * 判断是否为PostgreSQL数据库
     *
     * @return 是否为PostgreSQL数据库
     */
    public boolean isPostgresql() {
        return getDatabaseType().isPostgresql();
    }

    /**
     * 验证配置是否有效
     *
     * @return 配置是否有效
     */
    public boolean isValid() {
        return url != null && !url.isEmpty()
            && username != null && !username.isEmpty()
            && password != null;
    }
}