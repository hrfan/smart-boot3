package com.smart.common.database.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.common.database.entity.BaseEntity;
import com.smart.common.database.entity.PageResult;
import com.smart.common.database.mapper.BaseMapper;
import com.smart.common.database.util.DatabaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 通用增删改查基类
 * 提供基础的CRUD操作
 * 
 * @param <T> 实体类型，必须继承BaseEntity
 * @param <M> Mapper类型，必须继承BaseMapper
 * @author smart
 * @since 1.0.0
 */
public abstract class BaseService<T extends BaseEntity, M extends BaseMapper<T>> {
    
    private static final Logger log = LoggerFactory.getLogger(BaseService.class);
    
    @Autowired
    protected M baseMapper;
    
    /**
     * 根据ID查询
     * 
     * @param id 主键ID
     * @return 实体对象
     */
    public T selectById(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        
        log.debug("根据ID查询: {}", id);
        return baseMapper.selectById(id);
    }
    
    /**
     * 根据条件查询单个对象
     * 
     * @param condition 查询条件
     * @return 实体对象
     */
    public T selectOne(T condition) {
        if (condition == null) {
            return null;
        }
        
        log.debug("根据条件查询单个对象: {}", condition);
        return baseMapper.selectOneByCondition(condition);
    }
    
    /**
     * 根据条件查询列表
     * 
     * @param condition 查询条件
     * @return 实体列表
     */
    public List<T> selectList(T condition) {
        log.debug("根据条件查询列表: {}", condition);
        return baseMapper.selectListByCondition(condition);
    }
    
    /**
     * 查询所有记录
     * 
     * @return 实体列表
     */
    public List<T> selectAll() {
        log.debug("查询所有记录");
        return baseMapper.selectList(null);
    }
    
    /**
     * 分页查询
     * 
     * @param condition 查询条件
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 分页结果
     */
    public PageResult<T> selectPage(T condition, int pageNum, int pageSize) {
        log.debug("分页查询: condition={}, pageNum={}, pageSize={}", condition, pageNum, pageSize);
        
        // 使用MyBatis Plus的分页插件
        Page<T> page = new Page<>(pageNum, pageSize);
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        
        if (condition != null) {
            // 这里可以根据condition构建查询条件
            // 具体实现可以根据业务需求来定制
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
     * 插入单条记录
     * 
     * @param entity 实体对象
     * @return 影响行数
     */
    public int insert(T entity) {
        if (entity == null) {
            return 0;
        }
        
        log.debug("插入单条记录: {}", entity);
        
        // 设置ID（如果为空）
        if (entity.getId() == null || entity.getId().isEmpty()) {
            entity.setId(DatabaseUtils.generateId());
        }
        
        return baseMapper.insert(entity);
    }
    
    /**
     * 批量插入
     * 
     * @param entityList 实体列表
     * @return 影响行数
     */
    public int insertBatch(List<T> entityList) {
        if (entityList == null || entityList.isEmpty()) {
            return 0;
        }
        
        log.debug("批量插入: {} 条记录", entityList.size());
        
        // 设置ID（如果为空）
        for (T entity : entityList) {
            if (entity.isNew()) {
                entity.setId(DatabaseUtils.generateId());
            }
        }
        
        return baseMapper.insertBatch(entityList);
    }
    
    /**
     * 根据ID更新
     * 
     * @param entity 实体对象
     * @return 影响行数
     */
    public int updateById(T entity) {
        if (entity == null || entity.getId() == null || entity.getId().isEmpty()) {
            return 0;
        }
        
        log.debug("根据ID更新: {}", entity);
        return baseMapper.updateById(entity);
    }
    
    /**
     * 根据条件更新
     * 
     * @param entity 实体对象
     * @param condition 更新条件
     * @return 影响行数
     */
    public int updateByCondition(T entity, T condition) {
        if (entity == null || condition == null) {
            return 0;
        }
        
        log.debug("根据条件更新: entity={}, condition={}", entity, condition);
        return baseMapper.updateByCondition(entity, condition);
    }
    
    /**
     * 批量更新
     * 
     * @param entityList 实体列表
     * @return 影响行数
     */
    public int updateBatch(List<T> entityList) {
        if (entityList == null || entityList.isEmpty()) {
            return 0;
        }
        
        log.debug("批量更新: {} 条记录", entityList.size());
        return baseMapper.updateBatch(entityList);
    }
    
    /**
     * 根据ID删除
     * 
     * @param id 主键ID
     * @return 影响行数
     */
    public int deleteById(String id) {
        if (id == null || id.isEmpty()) {
            return 0;
        }
        
        log.debug("根据ID删除: {}", id);
        return baseMapper.deleteById(id);
    }
    
    /**
     * 根据条件删除
     * 
     * @param condition 删除条件
     * @return 影响行数
     */
    public int deleteByCondition(T condition) {
        if (condition == null) {
            return 0;
        }
        
        log.debug("根据条件删除: {}", condition);
        return baseMapper.deleteByCondition(condition);
    }
    
    /**
     * 批量删除
     * 
     * @param idList ID列表
     * @return 影响行数
     */
    public int deleteBatch(List<String> idList) {
        if (idList == null || idList.isEmpty()) {
            return 0;
        }
        
        log.debug("批量删除: {} 条记录", idList.size());
        return baseMapper.deleteBatchByIds(idList);
    }
    
    /**
     * 根据ID判断是否存在
     * 
     * @param id 主键ID
     * @return 是否存在
     */
    public boolean existsById(String id) {
        if (id == null || id.isEmpty()) {
            return false;
        }
        
        log.debug("判断ID是否存在: {}", id);
        return baseMapper.existsById(id);
    }
    
    /**
     * 根据条件判断是否存在
     * 
     * @param condition 查询条件
     * @return 是否存在
     */
    public boolean existsByCondition(T condition) {
        if (condition == null) {
            return false;
        }
        
        log.debug("判断条件是否存在: {}", condition);
        return baseMapper.existsByCondition(condition);
    }
    
    /**
     * 统计记录数
     * 
     * @param condition 查询条件
     * @return 记录数
     */
    public long count(T condition) {
        log.debug("统计记录数: {}", condition);
        
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (condition != null) {
            // 这里可以根据condition构建查询条件
            // 具体实现可以根据业务需求来定制
        }
        
        return baseMapper.selectCount(queryWrapper);
    }
    
    /**
     * 统计总记录数
     * 
     * @return 总记录数
     */
    public long countAll() {
        log.debug("统计总记录数");
        return baseMapper.selectCount(null);
    }
    
    /**
     * 保存或更新
     * 如果ID为空则插入，否则更新
     * 
     * @param entity 实体对象
     * @return 影响行数
     */
    public int saveOrUpdate(T entity) {
        if (entity == null) {
            return 0;
        }
        
        if (entity.isNew()) {
            return insert(entity);
        } else {
            return updateById(entity);
        }
    }
    
    /**
     * 批量保存或更新
     * 
     * @param entityList 实体列表
     * @return 影响行数
     */
    public int saveOrUpdateBatch(List<T> entityList) {
        if (entityList == null || entityList.isEmpty()) {
            return 0;
        }
        
        log.debug("批量保存或更新: {} 条记录", entityList.size());
        return baseMapper.upsertBatch(entityList);
    }
}
