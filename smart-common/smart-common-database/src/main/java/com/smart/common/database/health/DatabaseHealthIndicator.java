package com.smart.common.database.health;

// import com.smart.common.database.config.DatabaseConfig;
import com.smart.common.database.config.DatabaseConfig;
import com.smart.common.database.enums.DatabaseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库健康检查指示器
 * 检查数据库连接状态和响应时间
 *
 * @author smart
 * @since 1.0.0
 */
@Component
public class DatabaseHealthIndicator {

    private static final Logger log = LoggerFactory.getLogger(DatabaseHealthIndicator.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DatabaseConfig databaseConfig;

    /**
     * 健康检查超时时间（毫秒）
     */
    private static final int HEALTH_CHECK_TIMEOUT = 5000;

    /**
     * 检查数据库健康状态
     *
     * @return 健康状态信息
     */
    public Map<String, Object> checkDatabaseHealth() {
        long startTime = System.currentTimeMillis();

        try (Connection connection = dataSource.getConnection()) {
            // 设置连接超时
            connection.setNetworkTimeout(null, HEALTH_CHECK_TIMEOUT);

            // 执行健康检查查询
            String healthQuery = getHealthCheckQuery();
            try (PreparedStatement statement = connection.prepareStatement(healthQuery);
                 ResultSet resultSet = statement.executeQuery()) {

                long responseTime = System.currentTimeMillis() - startTime;

                // 获取数据库信息
                Map<String, Object> details = getDatabaseDetails(connection);
                details.put("responseTime", responseTime + "ms");
                details.put("status", "UP");

                // 检查响应时间
                if (responseTime > HEALTH_CHECK_TIMEOUT) {
                    details.put("warning", "响应时间过长: " + responseTime + "ms");
                    details.put("status", "DOWN");
                }

                return details;
            }

        } catch (SQLException e) {
            long responseTime = System.currentTimeMillis() - startTime;

            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("error", e.getMessage());
            errorDetails.put("responseTime", responseTime + "ms");
            errorDetails.put("database", databaseConfig.getDatabaseType().getDescription());
            errorDetails.put("status", "DOWN");

            return errorDetails;
        }
    }

    /**
     * 获取健康检查查询语句
     *
     * @return 查询语句
     */
    private String getHealthCheckQuery() {
        DatabaseType dbType = databaseConfig.getDatabaseType();

        if (dbType.isMysql()) {
            return "SELECT 1";
        } else if (dbType.isPostgresql()) {
            return "SELECT 1";
        } else {
            return "SELECT 1";
        }
    }

    /**
     * 获取数据库详细信息
     *
     * @param connection 数据库连接
     * @return 详细信息
     */
    private Map<String, Object> getDatabaseDetails(Connection connection) {
        Map<String, Object> details = new HashMap<>();

        try {
            DatabaseType dbType = databaseConfig.getDatabaseType();
            details.put("database", dbType.getDescription());
            details.put("driver", dbType.getDriverClassName());

            // 获取数据库版本信息
            String version = getDatabaseVersion(connection, dbType);
            if (version != null) {
                details.put("version", version);
            }

            // 获取连接信息
            details.put("url", databaseConfig.getUrl());
            details.put("username", databaseConfig.getUsername());

            // 获取连接池信息（如果是Druid）
            if (dataSource.getClass().getName().contains("Druid")) {
                addDruidPoolInfo(details);
            }

        } catch (Exception e) {
            log.warn("获取数据库详细信息失败", e);
            details.put("warning", "无法获取详细信息: " + e.getMessage());
        }

        return details;
    }

    /**
     * 获取数据库版本信息
     *
     * @param connection 数据库连接
     * @param dbType 数据库类型
     * @return 版本信息
     */
    private String getDatabaseVersion(Connection connection, DatabaseType dbType) {
        try {
            String versionQuery;
            if (dbType.isMysql()) {
                versionQuery = "SELECT VERSION()";
            } else if (dbType.isPostgresql()) {
                versionQuery = "SELECT VERSION()";
            } else {
                return null;
            }

            try (PreparedStatement statement = connection.prepareStatement(versionQuery);
                 ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getString(1);
                }
            }
        } catch (SQLException e) {
            log.warn("获取数据库版本失败", e);
        }

        return null;
    }

    /**
     * 添加Druid连接池信息
     *
     * @param details 详细信息
     */
    private void addDruidPoolInfo(Map<String, Object> details) {
        try {
            // 通过反射获取Druid连接池信息
            Class<?> druidDataSourceClass = dataSource.getClass();

            // 获取活跃连接数
            Object activeCount = druidDataSourceClass.getMethod("getActiveCount").invoke(dataSource);
            details.put("activeConnections", activeCount);

            // 获取最大连接数
            Object maxActive = druidDataSourceClass.getMethod("getMaxActive").invoke(dataSource);
            details.put("maxConnections", maxActive);

            // 获取连接池状态
            Object poolingCount = druidDataSourceClass.getMethod("getPoolingCount").invoke(dataSource);
            details.put("poolingConnections", poolingCount);

        } catch (Exception e) {
            log.debug("获取Druid连接池信息失败", e);
        }
    }
}
