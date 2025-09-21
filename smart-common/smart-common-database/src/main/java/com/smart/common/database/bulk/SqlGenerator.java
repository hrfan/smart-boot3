package com.smart.common.database.bulk;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SQL生成器
 * 基于实体元数据生成批量插入SQL
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public class SqlGenerator {
    
    /** SQL缓存 */
    private static final ConcurrentHashMap<String, String> CACHE = new ConcurrentHashMap<>();
    
    /**
     * 生成批量插入SQL
     * 
     * @param metadata 实体元数据
     * @param batchSize 批次大小
     * @return SQL语句
     */
    public static String generateBatchInsertSQL(EntityMetadata metadata, int batchSize) {
        String cacheKey = metadata.getEntityClass().getName() + "_batch_" + batchSize;
        
        return CACHE.computeIfAbsent(cacheKey, k -> {
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO ").append(metadata.getTableName()).append(" (");
            
            // 添加列名
            List<EntityFieldInfo> insertFields = metadata.getInsertFields();
            for (int i = 0; i < insertFields.size(); i++) {
                sql.append(insertFields.get(i).getColumnName());
                if (i < insertFields.size() - 1) {
                    sql.append(", ");
                }
            }
            
            sql.append(") VALUES ");
            
            // 添加多个值占位符
            for (int batch = 0; batch < batchSize; batch++) {
                sql.append("(");
                for (int i = 0; i < insertFields.size(); i++) {
                    sql.append("?");
                    if (i < insertFields.size() - 1) {
                        sql.append(", ");
                    }
                }
                sql.append(")");
                if (batch < batchSize - 1) {
                    sql.append(", ");
                }
            }
            
            return sql.toString();
        });
    }
    
    /**
     * 生成单条插入SQL
     * 
     * @param metadata 实体元数据
     * @return SQL语句
     */
    public static String generateInsertSQL(EntityMetadata metadata) {
        String cacheKey = metadata.getEntityClass().getName() + "_single";
        
        return CACHE.computeIfAbsent(cacheKey, k -> {
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO ").append(metadata.getTableName()).append(" (");
            
            // 添加列名
            List<EntityFieldInfo> insertFields = metadata.getInsertFields();
            for (int i = 0; i < insertFields.size(); i++) {
                sql.append(insertFields.get(i).getColumnName());
                if (i < insertFields.size() - 1) {
                    sql.append(", ");
                }
            }
            
            sql.append(") VALUES (");
            
            // 添加值占位符
            for (int i = 0; i < insertFields.size(); i++) {
                sql.append("?");
                if (i < insertFields.size() - 1) {
                    sql.append(", ");
                }
            }
            
            sql.append(")");
            return sql.toString();
        });
    }
}