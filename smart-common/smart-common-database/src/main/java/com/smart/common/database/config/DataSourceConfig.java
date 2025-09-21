package com.smart.common.database.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.smart.common.database.enums.DatabaseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * 数据源配置类
 * 配置Druid连接池，支持MySQL和PostgreSQL
 *
 * @author smart
 * @since 1.0.0
 */
@Configuration
public class DataSourceConfig {

    private static final Logger log = LoggerFactory.getLogger(DataSourceConfig.class);

    @Autowired
    private DatabaseConfig databaseConfig;

    /**
     * 创建数据源
     *
     * @return 数据源
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean(DataSource.class)
    public DataSource dataSource() {
        log.info("开始创建数据源，数据库类型: {}", databaseConfig.getDatabaseType().getDescription());

        DruidDataSource dataSource = new DruidDataSource();

        // 基本配置
        dataSource.setUrl(databaseConfig.getUrl());
        dataSource.setUsername(databaseConfig.getUsername());
        dataSource.setPassword(databaseConfig.getPassword());
        dataSource.setDriverClassName(databaseConfig.getDatabaseType().getDriverClassName());

        // 连接池配置
        DatabaseConfig.PoolConfig poolConfig = databaseConfig.getPool();
        dataSource.setInitialSize(poolConfig.getInitialSize());
        dataSource.setMinIdle(poolConfig.getMinIdle());
        dataSource.setMaxActive(poolConfig.getMaxActive());
        dataSource.setMaxWait(poolConfig.getMaxWait());
        // DruidDataSource没有setConnectionTimeout方法，使用setMaxWait代替
        // dataSource.setConnectionTimeout(poolConfig.getConnectionTimeout());
        dataSource.setTimeBetweenEvictionRunsMillis(poolConfig.getTimeBetweenEvictionRunsMillis());
        dataSource.setMinEvictableIdleTimeMillis(poolConfig.getMinEvictableIdleTimeMillis());
        dataSource.setRemoveAbandoned(poolConfig.isRemoveAbandoned());
        dataSource.setRemoveAbandonedTimeout(poolConfig.getRemoveAbandonedTimeout());

        // 数据库特定配置
        configureDatabaseSpecific(dataSource);

        // 监控配置
        configureMonitoring(dataSource);

        try {
            dataSource.init();
            log.info("数据源创建成功，数据库类型: {}", databaseConfig.getDatabaseType().getDescription());
        } catch (SQLException e) {
            log.error("数据源初始化失败", e);
            throw new RuntimeException("数据源初始化失败", e);
        }

        return dataSource;
    }

    /**
     * 配置数据库特定设置
     *
     * @param dataSource 数据源
     */
    private void configureDatabaseSpecific(DruidDataSource dataSource) {
        DatabaseType dbType = databaseConfig.getDatabaseType();

        if (dbType.isMysql()) {
            configureMysql(dataSource);
        } else if (dbType.isPostgresql()) {
            configurePostgresql(dataSource);
        }
    }

    /**
     * 配置MySQL特定设置
     *
     * @param dataSource 数据源
     */
    private void configureMysql(DruidDataSource dataSource) {
        log.info("配置MySQL数据库特定设置");

        // MySQL连接参数
        dataSource.setConnectionProperties(
            "useUnicode=true;characterEncoding=utf8;useSSL=false;serverTimezone=Asia/Shanghai;allowPublicKeyRetrieval=true"
        );

        // MySQL验证查询
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
    }

    /**
     * 配置PostgreSQL特定设置
     *
     * @param dataSource 数据源
     */
    private void configurePostgresql(DruidDataSource dataSource) {
        log.info("配置PostgreSQL数据库特定设置");

        // PostgreSQL连接参数
        dataSource.setConnectionProperties(
            "useUnicode=true;characterEncoding=utf8;ssl=false"
        );

        // PostgreSQL验证查询
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
    }

    /**
     * 配置监控设置
     *
     * @param dataSource 数据源
     */
    private void configureMonitoring(DruidDataSource dataSource) {
        DatabaseConfig.PoolConfig poolConfig = databaseConfig.getPool();

        if (poolConfig.isEnableSqlMonitor()) {
            log.info("启用SQL监控");
            try {
                dataSource.setFilters("stat,wall");
            } catch (SQLException e) {
                log.warn("设置SQL监控过滤器失败", e);
            }
        }

        if (poolConfig.isEnableSlowSqlMonitor()) {
            log.info("启用慢SQL监控，阈值: {}ms", poolConfig.getSlowSqlThreshold());
            // DruidDataSource的慢SQL监控通过其他方式配置
            // 这里暂时注释掉，可以通过其他配置方式实现
        }
    }
}
