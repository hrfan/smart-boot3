package com.smart.common.database.bulk;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 实体元数据解析器
 * 解析实体类注解，生成元数据信息
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public class EntityMetadataParser {
    
    /**
     * 解析实体类元数据
     * 
     * @param entityClass 实体类
     * @return 元数据
     */
    public static EntityMetadata parse(Class<?> entityClass) {
        EntityMetadata cached = EntityMetadata.getCached(entityClass);
        if (cached != null) {
            return cached;
        }
        
        EntityMetadata metadata = new EntityMetadata()
                .setEntityClass(entityClass)
                .setTableName(getTableName(entityClass));
        
        List<EntityFieldInfo> insertFields = new ArrayList<>();
        EntityFieldInfo primaryKeyField = null;
        
        // 解析所有字段（包括父类字段）
        Class<?> currentClass = entityClass;
        while (currentClass != null && currentClass != Object.class) {
            for (Field field : currentClass.getDeclaredFields()) {
                if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                
                EntityFieldInfo fieldInfo = parseField(field);
                if (fieldInfo != null) {
                    if (fieldInfo.isPrimaryKey()) {
                        primaryKeyField = fieldInfo;
                    }
                    if (!fieldInfo.isIgnoreInsert()) {
                        insertFields.add(fieldInfo);
                    }
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        
        metadata.setInsertFields(insertFields).setPrimaryKeyField(primaryKeyField);
        EntityMetadata.cache(entityClass, metadata);
        
        return metadata;
    }
    
    /**
     * 解析字段信息
     * 
     * @param field 字段
     * @return 字段信息
     */
    private static EntityFieldInfo parseField(Field field) {
        EntityFieldInfo fieldInfo = new EntityFieldInfo()
                .setField(field)
                .setFieldName(field.getName())
                .setFieldType(field.getType());
        
        // 解析@TableId注解
        TableId tableId = field.getAnnotation(TableId.class);
        if (tableId != null) {
            fieldInfo.setPrimaryKey(true);
            fieldInfo.setColumnName(tableId.value().isEmpty() ? field.getName() : tableId.value());
        }
        
        // 解析@TableField注解
        TableField tableField = field.getAnnotation(TableField.class);
        if (tableField != null) {
            fieldInfo.setColumnName(tableField.value().isEmpty() ? field.getName() : tableField.value());
            fieldInfo.setIgnoreInsert(tableField.insertStrategy() == com.baomidou.mybatisplus.annotation.FieldStrategy.NEVER);
        } else {
            fieldInfo.setColumnName(field.getName());
        }
        
        return fieldInfo;
    }
    
    /**
     * 获取表名
     * 
     * @param entityClass 实体类
     * @return 表名
     */
    private static String getTableName(Class<?> entityClass) {
        TableName tableName = entityClass.getAnnotation(TableName.class);
        if (tableName != null && !tableName.value().isEmpty()) {
            return tableName.value();
        }
        return camelToUnderscore(entityClass.getSimpleName());
    }
    
    /**
     * 驼峰转下划线
     * 
     * @param camelCase 驼峰命名
     * @return 下划线命名
     */
    private static String camelToUnderscore(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}