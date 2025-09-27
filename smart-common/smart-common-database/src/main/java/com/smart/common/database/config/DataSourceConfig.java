package com.smart.common.database.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * 数据源配置
 * 根据spring.datasource配置自动选择数据源类型
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Configuration
public class DataSourceConfig {

    private static final Logger log = LoggerFactory.getLogger(DataSourceConfig.class);

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.druid.initial-size:5}")
    private int initialSize;

    @Value("${spring.datasource.druid.min-idle:5}")
    private int minIdle;

    @Value("${spring.datasource.druid.max-active:20}")
    private int maxActive;

    @Value("${spring.datasource.druid.max-wait:60000}")
    private long maxWait;

    @Value("${spring.datasource.druid.time-between-eviction-runs-millis:60000}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.druid.min-evictable-idle-time-millis:300000}")
    private int minEvictableIdleTimeMillis;

    @Value("${spring.datasource.druid.validation-query:SELECT 1}")
    private String validationQuery;

    @Value("${spring.datasource.druid.test-while-idle:true}")
    private boolean testWhileIdle;

    @Value("${spring.datasource.druid.test-on-borrow:false}")
    private boolean testOnBorrow;

    @Value("${spring.datasource.druid.test-on-return:false}")
    private boolean testOnReturn;

    @Value("${spring.datasource.druid.pool-prepared-statements:true}")
    private boolean poolPreparedStatements;

    @Value("${spring.datasource.druid.max-pool-prepared-statement-per-connection-size:20}")
    private int maxPoolPreparedStatementPerConnectionSize;

    @Value("${spring.datasource.druid.filters:stat,wall}")
    private String filters;

    @Value("${spring.datasource.druid.connection-properties:}")
    private String connectionProperties;

    /**
     * 创建数据源
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean(DataSource.class)
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        
        // 基本配置
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        // 连接池配置
        dataSource.setInitialSize(initialSize);
        dataSource.setMinIdle(minIdle);
        dataSource.setMaxActive(maxActive);
        dataSource.setMaxWait(maxWait);
        dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        dataSource.setValidationQuery(validationQuery);
        dataSource.setTestWhileIdle(testWhileIdle);
        dataSource.setTestOnBorrow(testOnBorrow);
        dataSource.setTestOnReturn(testOnReturn);
        dataSource.setPoolPreparedStatements(poolPreparedStatements);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);

        // 监控配置
        if (filters != null && !filters.isEmpty()) {
            try {
                dataSource.setFilters(filters);
            } catch (SQLException e) {
                log.warn("设置Druid过滤器失败", e);
            }
        }

        if (connectionProperties != null && !connectionProperties.isEmpty()) {
            dataSource.setConnectionProperties(connectionProperties);
        }

        // 数据库特定配置
        configureDatabaseSpecific(dataSource);

        try {
            dataSource.init();
            log.info("数据源初始化成功，数据库类型: {}", getDatabaseType());
        } catch (SQLException e) {
            log.error("数据源初始化失败", e);
            throw new RuntimeException("数据源初始化失败", e);
        }

        return dataSource;
    }

    /**
     * 配置数据库特定设置
     */
    private void configureDatabaseSpecific(DruidDataSource dataSource) {
        String dbType = getDatabaseType();
        
        if ("mysql".equalsIgnoreCase(dbType)) {
            log.info("配置MySQL数据库特定设置");
            // MySQL特定配置已在URL中设置
        } else if ("postgresql".equalsIgnoreCase(dbType)) {
            log.info("配置PostgreSQL数据库特定设置");
            // PostgreSQL特定配置已在URL中设置
        }
    }

    /**
     * 获取数据库类型
     */
    private String getDatabaseType() {
        if (driverClassName.contains("mysql")) {
            return "mysql";
        } else if (driverClassName.contains("postgresql")) {
            return "postgresql";
        } else {
            return "unknown";
        }
    }
}