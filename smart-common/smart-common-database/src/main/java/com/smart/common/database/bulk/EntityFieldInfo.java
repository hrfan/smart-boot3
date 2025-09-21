package com.smart.common.database.bulk;

import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.Field;

/**
 * 实体字段信息
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class EntityFieldInfo {
    
    /** 字段对象 */
    private Field field;
    
    /** 数据库列名 */
    private String columnName;
    
    /** 字段名 */
    private String fieldName;
    
    /** 字段类型 */
    private Class<?> fieldType;
    
    /** 是否为主键 */
    private boolean isPrimaryKey;
    
    /** 是否忽略插入 */
    private boolean ignoreInsert;
    
    /**
     * 获取字段值
     * 
     * @param entity 实体对象
     * @return 字段值
     */
    public Object getFieldValue(Object entity) {
        try {
            field.setAccessible(true);
            return field.get(entity);
        } catch (Exception e) {
            throw new RuntimeException("获取字段值失败: " + fieldName, e);
        }
    }
}