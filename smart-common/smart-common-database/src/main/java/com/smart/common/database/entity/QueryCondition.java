package com.smart.common.database.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;

/**
 * 查询条件类
 * 用于构建动态查询条件
 * 
 * @author smart
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryCondition implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 字段名
     */
    private String field;
    
    /**
     * 操作符
     * 支持：=, !=, >, <, >=, <=, LIKE, IN, BETWEEN, IS NULL, IS NOT NULL
     */
    private String operator;
    
    /**
     * 字段值
     */
    private Object value;
    
    /**
     * 第二个值（用于BETWEEN操作）
     */
    private Object secondValue;
    
    /**
     * 构造函数
     * 
     * @param field 字段名
     * @param operator 操作符
     * @param value 字段值
     */
    public QueryCondition(String field, String operator, Object value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }
    
    /**
     * 创建等于条件
     * 
     * @param field 字段名
     * @param value 字段值
     * @return 查询条件
     */
    public static QueryCondition eq(String field, Object value) {
        return new QueryCondition(field, "=", value);
    }
    
    /**
     * 创建不等于条件
     * 
     * @param field 字段名
     * @param value 字段值
     * @return 查询条件
     */
    public static QueryCondition ne(String field, Object value) {
        return new QueryCondition(field, "!=", value);
    }
    
    /**
     * 创建大于条件
     * 
     * @param field 字段名
     * @param value 字段值
     * @return 查询条件
     */
    public static QueryCondition gt(String field, Object value) {
        return new QueryCondition(field, ">", value);
    }
    
    /**
     * 创建小于条件
     * 
     * @param field 字段名
     * @param value 字段值
     * @return 查询条件
     */
    public static QueryCondition lt(String field, Object value) {
        return new QueryCondition(field, "<", value);
    }
    
    /**
     * 创建大于等于条件
     * 
     * @param field 字段名
     * @param value 字段值
     * @return 查询条件
     */
    public static QueryCondition ge(String field, Object value) {
        return new QueryCondition(field, ">=", value);
    }
    
    /**
     * 创建小于等于条件
     * 
     * @param field 字段名
     * @param value 字段值
     * @return 查询条件
     */
    public static QueryCondition le(String field, Object value) {
        return new QueryCondition(field, "<=", value);
    }
    
    /**
     * 创建模糊查询条件
     * 
     * @param field 字段名
     * @param value 字段值
     * @return 查询条件
     */
    public static QueryCondition like(String field, String value) {
        return new QueryCondition(field, "LIKE", "%" + value + "%");
    }
    
    /**
     * 创建左模糊查询条件
     * 
     * @param field 字段名
     * @param value 字段值
     * @return 查询条件
     */
    public static QueryCondition likeLeft(String field, String value) {
        return new QueryCondition(field, "LIKE", "%" + value);
    }
    
    /**
     * 创建右模糊查询条件
     * 
     * @param field 字段名
     * @param value 字段值
     * @return 查询条件
     */
    public static QueryCondition likeRight(String field, String value) {
        return new QueryCondition(field, "LIKE", value + "%");
    }
    
    /**
     * 创建IN查询条件
     * 
     * @param field 字段名
     * @param values 字段值列表
     * @return 查询条件
     */
    public static QueryCondition in(String field, Object... values) {
        return new QueryCondition(field, "IN", values);
    }
    
    /**
     * 创建BETWEEN查询条件
     * 
     * @param field 字段名
     * @param minValue 最小值
     * @param maxValue 最大值
     * @return 查询条件
     */
    public static QueryCondition between(String field, Object minValue, Object maxValue) {
        return new QueryCondition(field, "BETWEEN", minValue, maxValue);
    }
    
    /**
     * 创建IS NULL条件
     * 
     * @param field 字段名
     * @return 查询条件
     */
    public static QueryCondition isNull(String field) {
        return new QueryCondition(field, "IS NULL", null);
    }
    
    /**
     * 创建IS NOT NULL条件
     * 
     * @param field 字段名
     * @return 查询条件
     */
    public static QueryCondition isNotNull(String field) {
        return new QueryCondition(field, "IS NOT NULL", null);
    }
}
