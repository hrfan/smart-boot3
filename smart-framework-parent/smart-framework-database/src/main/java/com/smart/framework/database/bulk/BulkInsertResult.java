package com.smart.framework.database.bulk;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 批量插入结果
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class BulkInsertResult<T> {
    
    /** 总记录数 */
    private int totalCount;
    
    /** 成功插入记录数 */
    private int successCount;
    
    /** 失败记录数 */
    private int failureCount;
    
    /** 失败的实体列表 */
    private List<T> failedEntities;
    
    /** 错误信息 */
    private String errorMessage;
    
    /** 执行耗时(毫秒) */
    private long executionTimeMs;
    
    /** 是否全部成功 */
    private boolean allSuccess;
    
    /**
     * 计算成功率
     * 
     * @return 成功率 (0.0 - 1.0)
     */
    public double getSuccessRate() {
        return totalCount == 0 ? 0.0 : (double) successCount / totalCount;
    }
}