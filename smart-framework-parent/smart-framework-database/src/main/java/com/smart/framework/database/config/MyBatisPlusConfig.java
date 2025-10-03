package com.smart.framework.database.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.smart.framework.database.config.MyPaginationInnerInterceptor;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * MyBatis Plus配置类
 * 配置分页插件、数据库类型等
 *
 * @author Smart Boot3
 * @since 1.0.0
 */
@Configuration
@MapperScan(value={"com.smart.**.mapper*"})
public class MyBatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        // 设置sql的limit为无限制，默认是500
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(MyPaginationInnerInterceptor.createPaginationInterceptor());
        return interceptor;
    }

    /**
     * 数据库ID提供者
     */
    @Bean
    public DatabaseIdProvider databaseIdProvider() {
        VendorDatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
        Properties properties = new Properties();
        properties.put("Oracle","oracle");
        properties.put("MySQL","mysql");
        properties.setProperty("PostgreSQL", "postgresql");
        properties.setProperty("DB2", "db2");
        properties.setProperty("SQL Server", "sqlserver");
        databaseIdProvider.setProperties(properties);
        return databaseIdProvider;
    }
}
