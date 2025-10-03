package com.smart.framework.database.mapper;

import com.smart.framework.database.entity.BaseEntity;
import com.smart.framework.database.entity.PageResult;
import com.smart.framework.database.entity.QueryCondition;
import com.smart.framework.database.entity.QueryConditionGroup;
import com.smart.framework.database.entity.OrderBy;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 通用Mapper接口
 * 提供基础的数据库操作，继承MyBatis Plus的BaseMapper
 * 
 * @param <T> 实体类型，必须继承BaseEntity
 * @author smart
 * @since 1.0.0
 */
public interface BaseMapper<T extends BaseEntity> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {
    
    /**
     * 根据条件查询单个对象
     * 
     * @param condition 查询条件
     * @return 实体对象
     */
    T selectOneByCondition(@Param("condition") T condition);
    
    /**
     * 根据条件查询列表
     * 
     * @param condition 查询条件
     * @return 实体列表
     */
    List<T> selectListByCondition(@Param("condition") T condition);
    
    /**
     * 根据多个条件查询列表
     * 
     * @param conditions 条件列表
     * @return 实体列表
     */
    List<T> selectListByConditions(@Param("conditions") List<QueryCondition> conditions);
    
    /**
     * 根据条件组查询列表
     * 
     * @param conditionGroup 条件组
     * @return 实体列表
     */
    List<T> selectListByConditionGroup(@Param("conditionGroup") QueryConditionGroup conditionGroup);
    
    /**
     * 分页查询
     * 
     * @param condition 查询条件
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 分页结果
     */
    PageResult<T> selectPageByCondition(@Param("condition") T condition, 
                                       @Param("pageNum") int pageNum, 
                                       @Param("pageSize") int pageSize);
    
    /**
     * 分页查询（支持排序）
     * 
     * @param condition 查询条件
     * @param pageNum 页码
     * @param pageSize 页大小
     * @param orderBy 排序字段
     * @param orderDirection 排序方向
     * @return 分页结果
     */
    PageResult<T> selectPageByConditionWithOrder(@Param("condition") T condition, 
                                                @Param("pageNum") int pageNum, 
                                                @Param("pageSize") int pageSize,
                                                @Param("orderBy") String orderBy,
                                                @Param("orderDirection") String orderDirection);
    
    /**
     * 分页查询（支持多字段排序）
     * 
     * @param condition 查询条件
     * @param pageNum 页码
     * @param pageSize 页大小
     * @param orderList 排序列表
     * @return 分页结果
     */
    PageResult<T> selectPageByConditionWithOrders(@Param("condition") T condition, 
                                                 @Param("pageNum") int pageNum, 
                                                 @Param("pageSize") int pageSize,
                                                 @Param("orderList") List<OrderBy> orderList);
    
    /**
     * 根据条件更新
     * 
     * @param entity 实体对象
     * @param condition 更新条件
     * @return 影响行数
     */
    int updateByCondition(@Param("entity") T entity, @Param("condition") T condition);
    
    /**
     * 根据条件删除
     * 
     * @param condition 删除条件
     * @return 影响行数
     */
    int deleteByCondition(@Param("condition") T condition);
    
    /**
     * 批量删除
     * 
     * @param idList ID列表
     * @return 影响行数
     */
    int deleteBatchByIds(@Param("idList") List<String> idList);
    
    /**
     * 根据ID判断是否存在
     * 
     * @param id 主键ID
     * @return 是否存在
     */
    boolean existsById(@Param("id") String id);
    
    /**
     * 根据条件判断是否存在
     * 
     * @param condition 查询条件
     * @return 是否存在
     */
    boolean existsByCondition(@Param("condition") T condition);
    
    /**
     * 根据字段判断是否存在
     * 
     * @param field 字段名
     * @param value 字段值
     * @return 是否存在
     */
    boolean existsByField(@Param("field") String field, @Param("value") Object value);
    
    /**
     * 统计记录数
     * 
     * @param condition 查询条件
     * @return 记录数
     */
    long countByCondition(@Param("condition") T condition);
    
    /**
     * 根据条件组统计记录数
     * 
     * @param conditionGroup 条件组
     * @return 记录数
     */
    long countByConditionGroup(@Param("conditionGroup") QueryConditionGroup conditionGroup);
    
    /**
     * 模糊查询
     * 
     * @param field 字段名
     * @param value 查询值
     * @return 实体列表
     */
    List<T> selectLike(@Param("field") String field, @Param("value") String value);
    
    /**
     * 范围查询
     * 
     * @param field 字段名
     * @param minValue 最小值
     * @param maxValue 最大值
     * @return 实体列表
     */
    List<T> selectBetween(@Param("field") String field, 
                         @Param("minValue") Object minValue, 
                         @Param("maxValue") Object maxValue);
    
    /**
     * IN查询
     * 
     * @param field 字段名
     * @param values 值列表
     * @return 实体列表
     */
    List<T> selectIn(@Param("field") String field, @Param("values") List<Object> values);
    
    /**
     * 批量插入
     * 
     * @param entityList 实体列表
     * @return 影响行数
     */
    int insertBatch(@Param("entityList") List<T> entityList);
    
    /**
     * 批量更新
     * 
     * @param entityList 实体列表
     * @return 影响行数
     */
    int updateBatch(@Param("entityList") List<T> entityList);
    
    /**
     * 批量插入或更新（UPSERT操作）
     * 
     * @param entityList 实体列表
     * @return 影响行数
     */
    int upsertBatch(@Param("entityList") List<T> entityList);
}
