package com.smart.common.database.service;

import com.smart.common.database.entity.BaseEntity;
import com.smart.common.database.mapper.BaseMapper;
import com.smart.common.database.util.DatabaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 批量操作基类
 * 提供高效的批量操作功能
 * 
 * @param <T> 实体类型，必须继承BaseEntity
 * @param <M> Mapper类型，必须继承BaseMapper
 * @author smart
 * @since 1.0.0
 */
public abstract class BaseBatchService<T extends BaseEntity, M extends BaseMapper<T>> extends BaseQueryService<T, M> {
    
    private static final Logger log = LoggerFactory.getLogger(BaseBatchService.class);
    
    /**
     * 默认批量大小
     */
    private static final int DEFAULT_BATCH_SIZE = 1000;
    
    /**
     * 线程池（用于异步批量操作）
     */
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    
    /**
     * 批量插入（使用JDBC批量操作）
     * 
     * @param entityList 实体列表
     * @param batchSize 批量大小
     * @return 影响行数
     */
    public int insertBatch(List<T> entityList, int batchSize) {
        if (entityList == null || entityList.isEmpty()) {
            return 0;
        }
        
        log.debug("批量插入: {} 条记录, 批量大小: {}", entityList.size(), batchSize);
        
        // 设置ID（如果为空）
        for (T entity : entityList) {
            if (entity.getId() == null || entity.getId().isEmpty()) {
                entity.setId(DatabaseUtils.generateId());
            }
        }
        
        int totalCount = 0;
        int size = entityList.size();
        
        // 分批处理
        for (int i = 0; i < size; i += batchSize) {
            int endIndex = Math.min(i + batchSize, size);
            List<T> batch = entityList.subList(i, endIndex);
            
            int count = baseMapper.insertBatch(batch);
            totalCount += count;
            
            log.debug("批量插入第 {} 批: {} 条记录", (i / batchSize + 1), count);
        }
        
        log.info("批量插入完成: 总共 {} 条记录", totalCount);
        return totalCount;
    }
    
    /**
     * 批量插入（使用默认批量大小）
     * 
     * @param entityList 实体列表
     * @return 影响行数
     */
    public int insertBatch(List<T> entityList) {
        return insertBatch(entityList, DEFAULT_BATCH_SIZE);
    }
    
    /**
     * 批量更新（使用JDBC批量操作）
     * 
     * @param entityList 实体列表
     * @param batchSize 批量大小
     * @return 影响行数
     */
    public int updateBatch(List<T> entityList, int batchSize) {
        if (entityList == null || entityList.isEmpty()) {
            return 0;
        }
        
        log.debug("批量更新: {} 条记录, 批量大小: {}", entityList.size(), batchSize);
        
        int totalCount = 0;
        int size = entityList.size();
        
        // 分批处理
        for (int i = 0; i < size; i += batchSize) {
            int endIndex = Math.min(i + batchSize, size);
            List<T> batch = entityList.subList(i, endIndex);
            
            int count = baseMapper.updateBatch(batch);
            totalCount += count;
            
            log.debug("批量更新第 {} 批: {} 条记录", (i / batchSize + 1), count);
        }
        
        log.info("批量更新完成: 总共 {} 条记录", totalCount);
        return totalCount;
    }
    
    /**
     * 批量更新（使用默认批量大小）
     * 
     * @param entityList 实体列表
     * @return 影响行数
     */
    public int updateBatch(List<T> entityList) {
        return updateBatch(entityList, DEFAULT_BATCH_SIZE);
    }
    
    /**
     * 批量删除（使用JDBC批量操作）
     * 
     * @param idList ID列表
     * @param batchSize 批量大小
     * @return 影响行数
     */
    public int deleteBatch(List<String> idList, int batchSize) {
        if (idList == null || idList.isEmpty()) {
            return 0;
        }
        
        log.debug("批量删除: {} 条记录, 批量大小: {}", idList.size(), batchSize);
        
        int totalCount = 0;
        int size = idList.size();
        
        // 分批处理
        for (int i = 0; i < size; i += batchSize) {
            int endIndex = Math.min(i + batchSize, size);
            List<String> batch = idList.subList(i, endIndex);
            
            int count = baseMapper.deleteBatchByIds(batch);
            totalCount += count;
            
            log.debug("批量删除第 {} 批: {} 条记录", (i / batchSize + 1), count);
        }
        
        log.info("批量删除完成: 总共 {} 条记录", totalCount);
        return totalCount;
    }
    
    /**
     * 批量删除（使用默认批量大小）
     * 
     * @param idList ID列表
     * @return 影响行数
     */
    public int deleteBatch(List<String> idList) {
        return deleteBatch(idList, DEFAULT_BATCH_SIZE);
    }
    
    /**
     * 批量插入或更新（UPSERT操作）
     * 
     * @param entityList 实体列表
     * @param batchSize 批量大小
     * @return 影响行数
     */
    public int upsertBatch(List<T> entityList, int batchSize) {
        if (entityList == null || entityList.isEmpty()) {
            return 0;
        }
        
        log.debug("批量插入或更新: {} 条记录, 批量大小: {}", entityList.size(), batchSize);
        
        // 设置ID（如果为空）
        for (T entity : entityList) {
            if (entity.getId() == null || entity.getId().isEmpty()) {
                entity.setId(DatabaseUtils.generateId());
            }
        }
        
        int totalCount = 0;
        int size = entityList.size();
        
        // 分批处理
        for (int i = 0; i < size; i += batchSize) {
            int endIndex = Math.min(i + batchSize, size);
            List<T> batch = entityList.subList(i, endIndex);
            
            int count = baseMapper.upsertBatch(batch);
            totalCount += count;
            
            log.debug("批量插入或更新第 {} 批: {} 条记录", (i / batchSize + 1), count);
        }
        
        log.info("批量插入或更新完成: 总共 {} 条记录", totalCount);
        return totalCount;
    }
    
    /**
     * 批量插入或更新（使用默认批量大小）
     * 
     * @param entityList 实体列表
     * @return 影响行数
     */
    public int upsertBatch(List<T> entityList) {
        return upsertBatch(entityList, DEFAULT_BATCH_SIZE);
    }
    
    /**
     * 异步批量插入
     * 
     * @param entityList 实体列表
     * @param batchSize 批量大小
     * @return CompletableFuture
     */
    public CompletableFuture<Integer> insertBatchAsync(List<T> entityList, int batchSize) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return insertBatch(entityList, batchSize);
            } catch (Exception e) {
                log.error("异步批量插入失败", e);
                throw new RuntimeException("异步批量插入失败", e);
            }
        }, executorService);
    }
    
    /**
     * 异步批量插入（使用默认批量大小）
     * 
     * @param entityList 实体列表
     * @return CompletableFuture
     */
    public CompletableFuture<Integer> insertBatchAsync(List<T> entityList) {
        return insertBatchAsync(entityList, DEFAULT_BATCH_SIZE);
    }
    
    /**
     * 异步批量更新
     * 
     * @param entityList 实体列表
     * @param batchSize 批量大小
     * @return CompletableFuture
     */
    public CompletableFuture<Integer> updateBatchAsync(List<T> entityList, int batchSize) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return updateBatch(entityList, batchSize);
            } catch (Exception e) {
                log.error("异步批量更新失败", e);
                throw new RuntimeException("异步批量更新失败", e);
            }
        }, executorService);
    }
    
    /**
     * 异步批量更新（使用默认批量大小）
     * 
     * @param entityList 实体列表
     * @return CompletableFuture
     */
    public CompletableFuture<Integer> updateBatchAsync(List<T> entityList) {
        return updateBatchAsync(entityList, DEFAULT_BATCH_SIZE);
    }
    
    /**
     * 异步批量删除
     * 
     * @param idList ID列表
     * @param batchSize 批量大小
     * @return CompletableFuture
     */
    public CompletableFuture<Integer> deleteBatchAsync(List<String> idList, int batchSize) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return deleteBatch(idList, batchSize);
            } catch (Exception e) {
                log.error("异步批量删除失败", e);
                throw new RuntimeException("异步批量删除失败", e);
            }
        }, executorService);
    }
    
    /**
     * 异步批量删除（使用默认批量大小）
     * 
     * @param idList ID列表
     * @return CompletableFuture
     */
    public CompletableFuture<Integer> deleteBatchAsync(List<String> idList) {
        return deleteBatchAsync(idList, DEFAULT_BATCH_SIZE);
    }
    
    /**
     * 异步批量插入或更新
     * 
     * @param entityList 实体列表
     * @param batchSize 批量大小
     * @return CompletableFuture
     */
    public CompletableFuture<Integer> upsertBatchAsync(List<T> entityList, int batchSize) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return upsertBatch(entityList, batchSize);
            } catch (Exception e) {
                log.error("异步批量插入或更新失败", e);
                throw new RuntimeException("异步批量插入或更新失败", e);
            }
        }, executorService);
    }
    
    /**
     * 异步批量插入或更新（使用默认批量大小）
     * 
     * @param entityList 实体列表
     * @return CompletableFuture
     */
    public CompletableFuture<Integer> upsertBatchAsync(List<T> entityList) {
        return upsertBatchAsync(entityList, DEFAULT_BATCH_SIZE);
    }
    
    /**
     * 关闭线程池
     * 在应用关闭时调用
     */
    public void shutdown() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            log.info("批量操作线程池已关闭");
        }
    }
}
