package com.smart.framework.database.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 排序条件类
 * 用于构建查询排序条件
 * 
 * @author smart
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class OrderBy implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 排序字段
     */
    private String field;
    
    /**
     * 排序方向（ASC, DESC）
     */
    private String direction = "ASC";
    
    /**
     * 构造函数
     * 
     * @param field 排序字段
     * @param direction 排序方向
     */
    public OrderBy(String field, String direction) {
        this.field = field;
        this.direction = direction != null ? direction.toUpperCase() : "ASC";
    }
    
    /**
     * 创建升序排序
     * 
     * @param field 排序字段
     * @return 排序条件
     */
    public static OrderBy asc(String field) {
        return new OrderBy(field, "ASC");
    }
    
    /**
     * 创建降序排序
     * 
     * @param field 排序字段
     * @return 排序条件
     */
    public static OrderBy desc(String field) {
        return new OrderBy(field, "DESC");
    }
    
    /**
     * 判断是否为升序
     * 
     * @return 是否为升序
     */
    public boolean isAsc() {
        return "ASC".equalsIgnoreCase(this.direction);
    }
    
    /**
     * 判断是否为降序
     * 
     * @return 是否为降序
     */
    public boolean isDesc() {
        return "DESC".equalsIgnoreCase(this.direction);
    }
    
    /**
     * 获取排序SQL片段
     * 
     * @return 排序SQL片段
     */
    public String toSql() {
        return field + " " + direction;
    }
}
