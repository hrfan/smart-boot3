package com.smart.framework.database.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据库类型枚举
 * 支持MySQL和PostgreSQL数据库
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Getter
public enum DatabaseType {
    
    /**
     * MySQL数据库
     */
    MYSQL("mysql", "com.mysql.cj.jdbc.Driver", "MySQL数据库"),
    
    /**
     * PostgreSQL数据库
     */
    POSTGRESQL("postgresql", "org.postgresql.Driver", "PostgreSQL数据库");
    
    /**
     * 数据库类型标识
     */
    private final String type;
    
    /**
     * 数据库驱动类名
     */
    private final String driverClassName;
    
    /**
     * 数据库描述
     */
    private final String description;
    
    /**
     * 枚举构造函数
     * @param type 数据库类型
     * @param driverClassName 驱动类名
     * @param description 描述
     */
    DatabaseType(String type, String driverClassName, String description) {
        this.type = type;
        this.driverClassName = driverClassName;
        this.description = description;
    }
    
    /**
     * 获取数据库类型
     * @return 数据库类型
     */
    public String getType() {
        return type;
    }
    
    /**
     * 获取驱动类名
     * @return 驱动类名
     */
    public String getDriverClassName() {
        return driverClassName;
    }
    
    /**
     * 获取描述
     * @return 描述
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * 根据数据库URL判断数据库类型
     * 
     * @param url 数据库连接URL
     * @return 数据库类型
     */
    public static DatabaseType fromUrl(String url) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("数据库URL不能为空");
        }
        
        String lowerUrl = url.toLowerCase();
        if (lowerUrl.contains("mysql")) {
            return MYSQL;
        } else if (lowerUrl.contains("postgresql") || lowerUrl.contains("postgres")) {
            return POSTGRESQL;
        } else {
            throw new IllegalArgumentException("不支持的数据库类型: " + url);
        }
    }
    
    /**
     * 根据数据库类型标识获取枚举
     * 
     * @param type 数据库类型标识
     * @return 数据库类型枚举
     */
    public static DatabaseType fromType(String type) {
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("数据库类型不能为空");
        }
        
        for (DatabaseType dbType : values()) {
            if (dbType.getType().equalsIgnoreCase(type)) {
                return dbType;
            }
        }
        
        throw new IllegalArgumentException("不支持的数据库类型: " + type);
    }
    
    /**
     * 判断是否为MySQL数据库
     * 
     * @return 是否为MySQL数据库
     */
    public boolean isMysql() {
        return this == MYSQL;
    }
    
    /**
     * 判断是否为PostgreSQL数据库
     * 
     * @return 是否为PostgreSQL数据库
     */
    public boolean isPostgresql() {
        return this == POSTGRESQL;
    }
}