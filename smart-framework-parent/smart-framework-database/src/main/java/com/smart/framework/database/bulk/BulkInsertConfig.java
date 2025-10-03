package com.smart.framework.database.bulk;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 批量插入配置
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class BulkInsertConfig {
    
    /** 批次大小，默认1000 */
    private int batchSize = 1000;
    
    /** 是否启用事务，默认true */
    private boolean enableTransaction = true;
    
    /** 是否启用SQL日志，默认false */
    private boolean enableSqlLog = true;
    
    /**
     * 创建默认配置
     * 
     * @return 默认配置
     */
    public static BulkInsertConfig defaultConfig() {
        return new BulkInsertConfig();
    }
    
    /**
     * 创建高性能配置
     * 
     * @return 高性能配置
     */
    public static BulkInsertConfig highPerformanceConfig() {
        return new BulkInsertConfig()
                .setBatchSize(5000)
                .setEnableTransaction(true)
                .setEnableSqlLog(false);
    }
    
    /**
     * 创建调试配置
     * 
     * @return 调试配置
     */
    public static BulkInsertConfig debugConfig() {
        return new BulkInsertConfig()
                .setBatchSize(100)
                .setEnableTransaction(true)
                .setEnableSqlLog(true);
    }
}