package com.smart.framework.database.bulk;

import java.util.List;

/**
 * 批量插入接口
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public interface IBulkInsert {
    
    /**
     * 快速导入（原有方法，保持向后兼容）
     * 
     * @param list 实体列表
     * @param <T> 实体类型
     * @return 插入的记录数
     * @throws Exception 插入异常
     */
    <T> int fastImport(List<T> list) throws Exception;
    
    /**
     * 快速导入（原有方法，保持向后兼容）
     * 
     * @param list 实体列表
     * @param batchSize 批次大小
     * @param <T> 实体类型
     * @return 插入的记录数
     * @throws Exception 插入异常
     */
    <T> int fastImport(List<T> list, int batchSize) throws Exception;
    
    /**
     * 批量插入实体（新方法）
     * 
     * @param list 实体列表
     * @param <T> 实体类型
     * @return 批量插入结果
     * @throws Exception 插入异常
     */
    <T> BulkInsertResult<T> bulkInsert(List<T> list) throws Exception;
    
    /**
     * 批量插入实体（新方法，支持配置）
     * 
     * @param list 实体列表
     * @param config 批量插入配置
     * @param <T> 实体类型
     * @return 批量插入结果
     * @throws Exception 插入异常
     */
    <T> BulkInsertResult<T> bulkInsert(List<T> list, BulkInsertConfig config) throws Exception;
}