package com.smart.common.database.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.common.database.entity.BaseEntity;
import com.smart.common.database.entity.OrderBy;
import com.smart.common.database.entity.PageResult;
import com.smart.common.database.mapper.BaseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 分页查询基类
 * 提供分页查询的通用功能
 * 
 * @param <T> 实体类型，必须继承BaseEntity
 * @param <M> Mapper类型，必须继承BaseMapper
 * @author smart
 * @since 1.0.0
 */
public abstract class BasePageService<T extends BaseEntity, M extends BaseMapper<T>> extends BaseService<T, M> {
    
    private static final Logger log = LoggerFactory.getLogger(BasePageService.class);
    
    /**
     * 分页查询（支持排序）
     * 
     * @param condition 查询条件
     * @param pageNum 页码
     * @param pageSize 页大小
     * @param orderBy 排序字段
     * @param orderDirection 排序方向（ASC/DESC）
     * @return 分页结果
     */
    public PageResult<T> selectPage(T condition, int pageNum, int pageSize, 
                                   String orderBy, String orderDirection) {
        log.debug("分页查询（支持排序）: condition={}, pageNum={}, pageSize={}, orderBy={}, orderDirection={}", 
                 condition, pageNum, pageSize, orderBy, orderDirection);
        
        Page<T> page = new Page<>(pageNum, pageSize);
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        
        // 构建查询条件
        buildQueryWrapper(queryWrapper, condition);
        
        // 添加排序
        if (orderBy != null && !orderBy.isEmpty()) {
            if ("DESC".equalsIgnoreCase(orderDirection)) {
                queryWrapper.orderByDesc(orderBy);
            } else {
                queryWrapper.orderByAsc(orderBy);
            }
        }
        
        IPage<T> result = baseMapper.selectPage(page, queryWrapper);
        
        return PageResult.of(
            result.getRecords(),
            result.getTotal(),
            (int) result.getCurrent(),
            (int) result.getSize()
        );
    }
    
    /**
     * 分页查询（支持多字段排序）
     * 
     * @param condition 查询条件
     * @param pageNum 页码
     * @param pageSize 页大小
     * @param orderList 排序列表
     * @return 分页结果
     */
    public PageResult<T> selectPage(T condition, int pageNum, int pageSize, 
                                   List<OrderBy> orderList) {
        log.debug("分页查询（支持多字段排序）: condition={}, pageNum={}, pageSize={}, orderList={}", 
                 condition, pageNum, pageSize, orderList);
        
        Page<T> page = new Page<>(pageNum, pageSize);
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        
        // 构建查询条件
        buildQueryWrapper(queryWrapper, condition);
        
        // 添加多字段排序
        if (orderList != null && !orderList.isEmpty()) {
            for (OrderBy orderBy : orderList) {
                if (orderBy != null && orderBy.getField() != null && !orderBy.getField().isEmpty()) {
                    if (orderBy.isDesc()) {
                        queryWrapper.orderByDesc(orderBy.getField());
                    } else {
                        queryWrapper.orderByAsc(orderBy.getField());
                    }
                }
            }
        }
        
        IPage<T> result = baseMapper.selectPage(page, queryWrapper);
        
        return PageResult.of(
            result.getRecords(),
            result.getTotal(),
            (int) result.getCurrent(),
            (int) result.getSize()
        );
    }
    
    /**
     * 分页查询（支持复杂条件）
     * 
     * @param queryWrapper 查询条件包装器
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 分页结果
     */
    public PageResult<T> selectPage(QueryWrapper<T> queryWrapper, int pageNum, int pageSize) {
        log.debug("分页查询（支持复杂条件）: queryWrapper={}, pageNum={}, pageSize={}", 
                 queryWrapper, pageNum, pageSize);
        
        Page<T> page = new Page<>(pageNum, pageSize);
        IPage<T> result = baseMapper.selectPage(page, queryWrapper);
        
        return PageResult.of(
            result.getRecords(),
            result.getTotal(),
            (int) result.getCurrent(),
            (int) result.getSize()
        );
    }
    
    /**
     * 分页查询（使用Mapper方法）
     * 
     * @param condition 查询条件
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 分页结果
     */
    public PageResult<T> selectPageByMapper(T condition, int pageNum, int pageSize) {
        log.debug("分页查询（使用Mapper方法）: condition={}, pageNum={}, pageSize={}", 
                 condition, pageNum, pageSize);
        
        return baseMapper.selectPageByCondition(condition, pageNum, pageSize);
    }
    
    /**
     * 分页查询（使用Mapper方法，支持排序）
     * 
     * @param condition 查询条件
     * @param pageNum 页码
     * @param pageSize 页大小
     * @param orderBy 排序字段
     * @param orderDirection 排序方向
     * @return 分页结果
     */
    public PageResult<T> selectPageByMapperWithOrder(T condition, int pageNum, int pageSize, 
                                                    String orderBy, String orderDirection) {
        log.debug("分页查询（使用Mapper方法，支持排序）: condition={}, pageNum={}, pageSize={}, orderBy={}, orderDirection={}", 
                 condition, pageNum, pageSize, orderBy, orderDirection);
        
        return baseMapper.selectPageByConditionWithOrder(condition, pageNum, pageSize, orderBy, orderDirection);
    }
    
    /**
     * 分页查询（使用Mapper方法，支持多字段排序）
     * 
     * @param condition 查询条件
     * @param pageNum 页码
     * @param pageSize 页大小
     * @param orderList 排序列表
     * @return 分页结果
     */
    public PageResult<T> selectPageByMapperWithOrders(T condition, int pageNum, int pageSize, 
                                                     List<OrderBy> orderList) {
        log.debug("分页查询（使用Mapper方法，支持多字段排序）: condition={}, pageNum={}, pageSize={}, orderList={}", 
                 condition, pageNum, pageSize, orderList);
        
        return baseMapper.selectPageByConditionWithOrders(condition, pageNum, pageSize, orderList);
    }
    
    /**
     * 构建查询条件包装器
     * 子类可以重写此方法来定制查询条件构建逻辑
     * 
     * @param queryWrapper 查询条件包装器
     * @param condition 查询条件
     */
    protected void buildQueryWrapper(QueryWrapper<T> queryWrapper, T condition) {
        if (condition == null) {
            return;
        }
        
        // 这里可以根据具体的实体类型来构建查询条件
        // 子类可以重写此方法来实现特定的查询逻辑
        
        // 示例：根据ID查询
        if (condition.getId() != null && !condition.getId().isEmpty()) {
            queryWrapper.eq("id", condition.getId());
        }
        
        // 示例：根据创建人查询
        if (condition.getCreateBy() != null && !condition.getCreateBy().isEmpty()) {
            queryWrapper.eq("create_by", condition.getCreateBy());
        }
        
        // 示例：根据租户ID查询
        if (condition.getTenantId() != null && !condition.getTenantId().isEmpty()) {
            queryWrapper.eq("tenant_id", condition.getTenantId());
        }
        
        // 示例：排除已删除的记录
        queryWrapper.eq("deleted", 0);
    }
}
