package com.smart.framework.database.bulk;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实体元数据
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class EntityMetadata {
    
    /** 表名 */
    private String tableName;
    
    /** 实体类 */
    private Class<?> entityClass;
    
    /** 插入字段信息 */
    private List<EntityFieldInfo> insertFields;
    
    /** 主键字段 */
    private EntityFieldInfo primaryKeyField;
    
    /** 元数据缓存 */
    private static final ConcurrentHashMap<Class<?>, EntityMetadata> CACHE = new ConcurrentHashMap<>();
    
    /**
     * 获取缓存的元数据
     * 
     * @param entityClass 实体类
     * @return 元数据
     */
    public static EntityMetadata getCached(Class<?> entityClass) {
        return CACHE.get(entityClass);
    }
    
    /**
     * 缓存元数据
     * 
     * @param entityClass 实体类
     * @param metadata 元数据
     */
    public static void cache(Class<?> entityClass, EntityMetadata metadata) {
        CACHE.put(entityClass, metadata);
    }
    
    /**
     * 获取插入字段值
     * 
     * @param entity 实体对象
     * @return 字段值数组
     */
    public Object[] getInsertFieldValues(Object entity) {
        Object[] values = new Object[insertFields.size()];
        for (int i = 0; i < insertFields.size(); i++) {
            values[i] = insertFields.get(i).getFieldValue(entity);
        }
        return values;
    }
}