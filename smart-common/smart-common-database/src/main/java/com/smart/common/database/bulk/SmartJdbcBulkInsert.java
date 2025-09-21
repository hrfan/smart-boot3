package com.smart.common.database.bulk;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 智能批量插入实现
 * 支持实体类批量插入，兼容MyBatis-Plus注解
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Slf4j
@Component
public class SmartJdbcBulkInsert implements IBulkInsert {
    
    @Autowired
    private DataSource dataSource;
    
    @Override
    public <T> int fastImport(List<T> list) throws Exception {
        return fastImport(list, 1000);
    }
    
    @Override
    public <T> int fastImport(List<T> list, int batchSize) throws Exception {
        BulkInsertConfig config = BulkInsertConfig.defaultConfig().setBatchSize(batchSize);
        BulkInsertResult<T> result = bulkInsert(list, config);
        return result.getSuccessCount();
    }
    
    @Override
    public <T> BulkInsertResult<T> bulkInsert(List<T> list) throws Exception {
        return bulkInsert(list, BulkInsertConfig.defaultConfig());
    }
    
    @Override
    @Transactional
    public <T> BulkInsertResult<T> bulkInsert(List<T> list, BulkInsertConfig config) throws Exception {
        if (list == null || list.isEmpty()) {
            return new BulkInsertResult<T>().setTotalCount(0).setSuccessCount(0).setAllSuccess(true);
        }
        
        long startTime = System.currentTimeMillis();
        BulkInsertResult<T> result = new BulkInsertResult<T>()
                .setTotalCount(list.size())
                .setFailedEntities(new ArrayList<>());
        
        try {
            // 解析实体元数据
            EntityMetadata metadata = EntityMetadataParser.parse(list.get(0).getClass());
            
            // 执行批量插入
            int successCount = doBulkInsert(list, metadata, config);
            
            result.setSuccessCount(successCount)
                    .setFailureCount(list.size() - successCount)
                    .setAllSuccess(successCount == list.size())
                    .setExecutionTimeMs(System.currentTimeMillis() - startTime);
            
            if (config.isEnableSqlLog()) {
                log.info("批量插入完成: 总数={}, 成功={}, 失败={}, 耗时={}ms", 
                        result.getTotalCount(), result.getSuccessCount(), 
                        result.getFailureCount(), result.getExecutionTimeMs());
            }
            
        } catch (Exception e) {
            result.setErrorMessage(e.getMessage())
                    .setFailureCount(list.size())
                    .setExecutionTimeMs(System.currentTimeMillis() - startTime);
            log.error("批量插入失败", e);
            throw e;
        }
        
        return result;
    }
    
    /**
     * 执行批量插入
     * 
     * @param list 实体列表
     * @param metadata 元数据
     * @param config 配置
     * @return 成功插入数量
     * @throws SQLException SQL异常
     */
    private <T> int doBulkInsert(List<T> list, EntityMetadata metadata, BulkInsertConfig config) throws SQLException {
        int batchSize = config.getBatchSize();
        int totalCount = list.size();
        int successCount = 0;
        
        try (Connection connection = dataSource.getConnection()) {
            
            // 设置事务
            if (config.isEnableTransaction()) {
                connection.setAutoCommit(false);
            }
            
            // 分批处理
            for (int i = 0; i < totalCount; i += batchSize) {
                int endIndex = Math.min(i + batchSize, totalCount);
                List<T> batch = list.subList(i, endIndex);
                
                // 生成批量SQL
                String sql = SqlGenerator.generateBatchInsertSQL(metadata, batch.size());
                
                if (config.isEnableSqlLog()) {
                    log.info("执行批量SQL: {}", sql);
                }
                
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                    // 设置所有参数
                    int paramIndex = 1;
                    for (T entity : batch) {
                        Object[] values = metadata.getInsertFieldValues(entity);
                        for (Object value : values) {
                            ps.setObject(paramIndex++, value);
                        }
                    }
                    
                    // 执行批量插入
                    int result = ps.executeUpdate();
                    successCount += result;
                    
                    if (config.isEnableSqlLog()) {
                        log.info("批次 {}-{} 插入完成，成功: {}", i + 1, endIndex, result);
                    }
                }
            }
            
            // 提交事务
            if (config.isEnableTransaction()) {
                connection.commit();
            }
            
        } catch (SQLException e) {
            log.error("批量插入SQL执行失败", e);
            throw e;
        }
        
        return successCount;
    }
}