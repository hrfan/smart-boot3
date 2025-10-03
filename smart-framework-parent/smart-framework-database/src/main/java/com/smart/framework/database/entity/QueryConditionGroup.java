package com.smart.framework.database.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询条件组
 * 用于构建复杂的查询条件组合
 * 
 * @author smart
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryConditionGroup implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 条件列表
     */
    private List<QueryCondition> conditions = new ArrayList<>();
    
    /**
     * 逻辑运算符（AND, OR）
     */
    private String logicOperator = "AND";
    
    /**
     * 子条件组列表
     */
    private List<QueryConditionGroup> subGroups = new ArrayList<>();
    
    /**
     * 构造函数
     * 
     * @param logicOperator 逻辑运算符
     */
    public QueryConditionGroup(String logicOperator) {
        this.logicOperator = logicOperator;
    }
    
    /**
     * 添加查询条件
     * 
     * @param condition 查询条件
     * @return 当前条件组
     */
    public QueryConditionGroup addCondition(QueryCondition condition) {
        if (this.conditions == null) {
            this.conditions = new ArrayList<>();
        }
        this.conditions.add(condition);
        return this;
    }
    
    /**
     * 添加子条件组
     * 
     * @param subGroup 子条件组
     * @return 当前条件组
     */
    public QueryConditionGroup addSubGroup(QueryConditionGroup subGroup) {
        if (this.subGroups == null) {
            this.subGroups = new ArrayList<>();
        }
        this.subGroups.add(subGroup);
        return this;
    }
    
    /**
     * 创建AND条件组
     * 
     * @return AND条件组
     */
    public static QueryConditionGroup and() {
        return new QueryConditionGroup("AND");
    }
    
    /**
     * 创建OR条件组
     * 
     * @return OR条件组
     */
    public static QueryConditionGroup or() {
        return new QueryConditionGroup("OR");
    }
    
    /**
     * 创建AND条件组并添加条件
     * 
     * @param conditions 查询条件
     * @return AND条件组
     */
    public static QueryConditionGroup and(QueryCondition... conditions) {
        QueryConditionGroup group = new QueryConditionGroup("AND");
        for (QueryCondition condition : conditions) {
            group.addCondition(condition);
        }
        return group;
    }
    
    /**
     * 创建OR条件组并添加条件
     * 
     * @param conditions 查询条件
     * @return OR条件组
     */
    public static QueryConditionGroup or(QueryCondition... conditions) {
        QueryConditionGroup group = new QueryConditionGroup("OR");
        for (QueryCondition condition : conditions) {
            group.addCondition(condition);
        }
        return group;
    }
    
    /**
     * 判断条件组是否为空
     * 
     * @return 是否为空
     */
    public boolean isEmpty() {
        return (conditions == null || conditions.isEmpty()) 
            && (subGroups == null || subGroups.isEmpty());
    }
    
    /**
     * 判断条件组是否有条件
     * 
     * @return 是否有条件
     */
    public boolean hasConditions() {
        return conditions != null && !conditions.isEmpty();
    }
    
    /**
     * 判断条件组是否有子组
     * 
     * @return 是否有子组
     */
    public boolean hasSubGroups() {
        return subGroups != null && !subGroups.isEmpty();
    }
}
