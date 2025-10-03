package com.smart.framework.database.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.framework.database.entity.BaseEntity;
import com.smart.framework.database.entity.QueryCondition;
import com.smart.framework.database.entity.QueryConditionGroup;
import com.smart.framework.database.mapper.BaseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 条件查询基类
 * 提供复杂条件查询功能
 * 
 * @param <T> 实体类型，必须继承BaseEntity
 * @param <M> Mapper类型，必须继承BaseMapper
 * @author smart
 * @since 1.0.0
 */
public abstract class BaseQueryService<T extends BaseEntity, M extends BaseMapper<T>> extends BasePageService<T, M> {
    
    private static final Logger log = LoggerFactory.getLogger(BaseQueryService.class);
    
    /**
     * 根据多个条件查询
     * 
     * @param conditions 条件列表
     * @return 实体列表
     */
    public List<T> selectByConditions(List<QueryCondition> conditions) {
        log.debug("根据多个条件查询: {}", conditions);
        return baseMapper.selectListByConditions(conditions);
    }
    
    /**
     * 根据条件查询（支持逻辑运算符）
     * 
     * @param conditionGroup 条件组
     * @return 实体列表
     */
    public List<T> selectByConditionGroup(QueryConditionGroup conditionGroup) {
        log.debug("根据条件组查询: {}", conditionGroup);
        return baseMapper.selectListByConditionGroup(conditionGroup);
    }
    
    /**
     * 模糊查询
     * 
     * @param field 字段名
     * @param value 查询值
     * @return 实体列表
     */
    public List<T> selectLike(String field, String value) {
        log.debug("模糊查询: field={}, value={}", field, value);
        return baseMapper.selectLike(field, value);
    }
    
    /**
     * 左模糊查询
     * 
     * @param field 字段名
     * @param value 查询值
     * @return 实体列表
     */
    public List<T> selectLikeLeft(String field, String value) {
        log.debug("左模糊查询: field={}, value={}", field, value);
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeLeft(field, value);
        return baseMapper.selectList(queryWrapper);
    }
    
    /**
     * 右模糊查询
     * 
     * @param field 字段名
     * @param value 查询值
     * @return 实体列表
     */
    public List<T> selectLikeRight(String field, String value) {
        log.debug("右模糊查询: field={}, value={}", field, value);
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight(field, value);
        return baseMapper.selectList(queryWrapper);
    }
    
    /**
     * 范围查询
     * 
     * @param field 字段名
     * @param minValue 最小值
     * @param maxValue 最大值
     * @return 实体列表
     */
    public List<T> selectBetween(String field, Object minValue, Object maxValue) {
        log.debug("范围查询: field={}, minValue={}, maxValue={}", field, minValue, maxValue);
        return baseMapper.selectBetween(field, minValue, maxValue);
    }
    
    /**
     * IN查询
     * 
     * @param field 字段名
     * @param values 值列表
     * @return 实体列表
     */
    public List<T> selectIn(String field, List<Object> values) {
        log.debug("IN查询: field={}, values={}", field, values);
        return baseMapper.selectIn(field, values);
    }
    
    /**
     * 存在性查询
     * 
     * @param field 字段名
     * @param value 查询值
     * @return 是否存在
     */
    public boolean existsByField(String field, Object value) {
        log.debug("存在性查询: field={}, value={}", field, value);
        return baseMapper.existsByField(field, value);
    }
    
    /**
     * 根据条件组统计记录数
     * 
     * @param conditionGroup 条件组
     * @return 记录数
     */
    public long countByConditionGroup(QueryConditionGroup conditionGroup) {
        log.debug("根据条件组统计记录数: {}", conditionGroup);
        return baseMapper.countByConditionGroup(conditionGroup);
    }
    
    /**
     * 根据多个条件统计记录数
     * 
     * @param conditions 条件列表
     * @return 记录数
     */
    public long countByConditions(List<QueryCondition> conditions) {
        log.debug("根据多个条件统计记录数: {}", conditions);
        
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        buildQueryWrapperByConditions(queryWrapper, conditions);
        
        return baseMapper.selectCount(queryWrapper);
    }
    
    /**
     * 根据条件构建查询包装器
     * 
     * @param queryWrapper 查询包装器
     * @param conditions 条件列表
     */
    protected void buildQueryWrapperByConditions(QueryWrapper<T> queryWrapper, List<QueryCondition> conditions) {
        if (conditions == null || conditions.isEmpty()) {
            return;
        }
        
        for (QueryCondition condition : conditions) {
            if (condition == null || condition.getField() == null || condition.getField().isEmpty()) {
                continue;
            }
            
            String field = condition.getField();
            String operator = condition.getOperator();
            Object value = condition.getValue();
            Object secondValue = condition.getSecondValue();
            
            switch (operator.toUpperCase()) {
                case "=":
                    queryWrapper.eq(field, value);
                    break;
                case "!=":
                case "<>":
                    queryWrapper.ne(field, value);
                    break;
                case ">":
                    queryWrapper.gt(field, value);
                    break;
                case ">=":
                    queryWrapper.ge(field, value);
                    break;
                case "<":
                    queryWrapper.lt(field, value);
                    break;
                case "<=":
                    queryWrapper.le(field, value);
                    break;
                case "LIKE":
                    queryWrapper.like(field, value);
                    break;
                case "IN":
                    if (value instanceof List) {
                        queryWrapper.in(field, (List<?>) value);
                    } else if (value instanceof Object[]) {
                        queryWrapper.in(field, (Object[]) value);
                    }
                    break;
                case "BETWEEN":
                    if (secondValue != null) {
                        queryWrapper.between(field, value, secondValue);
                    }
                    break;
                case "IS NULL":
                    queryWrapper.isNull(field);
                    break;
                case "IS NOT NULL":
                    queryWrapper.isNotNull(field);
                    break;
                default:
                    log.warn("不支持的查询操作符: {}", operator);
                    break;
            }
        }
        
        // 默认排除已删除的记录
        queryWrapper.eq("deleted", 0);
    }
    
    /**
     * 根据条件组构建查询包装器
     * 
     * @param queryWrapper 查询包装器
     * @param conditionGroup 条件组
     */
    protected void buildQueryWrapperByConditionGroup(QueryWrapper<T> queryWrapper, QueryConditionGroup conditionGroup) {
        if (conditionGroup == null) {
            return;
        }
        
        // 处理当前条件组中的条件
        if (conditionGroup.hasConditions()) {
            for (QueryCondition condition : conditionGroup.getConditions()) {
                if (condition == null || condition.getField() == null || condition.getField().isEmpty()) {
                    continue;
                }
                
                String field = condition.getField();
                String operator = condition.getOperator();
                Object value = condition.getValue();
                Object secondValue = condition.getSecondValue();
                
                switch (operator.toUpperCase()) {
                    case "=":
                        queryWrapper.eq(field, value);
                        break;
                    case "!=":
                    case "<>":
                        queryWrapper.ne(field, value);
                        break;
                    case ">":
                        queryWrapper.gt(field, value);
                        break;
                    case ">=":
                        queryWrapper.ge(field, value);
                        break;
                    case "<":
                        queryWrapper.lt(field, value);
                        break;
                    case "<=":
                        queryWrapper.le(field, value);
                        break;
                    case "LIKE":
                        queryWrapper.like(field, value);
                        break;
                    case "IN":
                        if (value instanceof List) {
                            queryWrapper.in(field, (List<?>) value);
                        } else if (value instanceof Object[]) {
                            queryWrapper.in(field, (Object[]) value);
                        }
                        break;
                    case "BETWEEN":
                        if (secondValue != null) {
                            queryWrapper.between(field, value, secondValue);
                        }
                        break;
                    case "IS NULL":
                        queryWrapper.isNull(field);
                        break;
                    case "IS NOT NULL":
                        queryWrapper.isNotNull(field);
                        break;
                    default:
                        log.warn("不支持的查询操作符: {}", operator);
                        break;
                }
            }
        }
        
        // 处理子条件组
        if (conditionGroup.hasSubGroups()) {
            for (QueryConditionGroup subGroup : conditionGroup.getSubGroups()) {
                if (subGroup == null) {
                    continue;
                }
                
                if ("OR".equalsIgnoreCase(subGroup.getLogicOperator())) {
                    queryWrapper.or(wrapper -> {
                        buildQueryWrapperByConditionGroup(wrapper, subGroup);
                    });
                } else {
                    buildQueryWrapperByConditionGroup(queryWrapper, subGroup);
                }
            }
        }
        
        // 默认排除已删除的记录
        queryWrapper.eq("deleted", 0);
    }
}
