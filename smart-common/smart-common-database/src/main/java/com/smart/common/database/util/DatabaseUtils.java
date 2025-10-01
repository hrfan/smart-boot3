package com.smart.common.database.util;

import com.smart.common.database.entity.BaseEntity;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 数据库操作工具类
 * 提供常用的数据库操作工具方法
 * 
 * @author smart
 * @since 1.0.0
 */
public class DatabaseUtils {
    
    /**
     * 生成UUID主键
     * 使用Hutool的IdUtil生成UUID，并移除连字符
     * 
     * @return UUID字符串
     */
    public static String generateId() {
        return IdUtil.simpleUUID();
    }
    
    /**
     * 生成带前缀的ID
     * 
     * @param prefix 前缀
     * @return 带前缀的ID
     */
    public static String generateId(String prefix) {
        if (StrUtil.isBlank(prefix)) {
            return generateId();
        }
        return prefix + "_" + generateId();
    }
    
    /**
     * 设置创建信息
     * 
     * @param entity 实体对象
     * @param createBy 创建人
     * @param createUserName 创建人姓名
     */
    public static void setCreateInfo(BaseEntity entity, String createBy, String createUserName) {
        if (entity == null) {
            return;
        }
        
        entity.setId(generateId());

        
        // 同时设置更新信息
        setUpdateInfo(entity, createBy, createUserName);
    }
    
    /**
     * 设置更新信息
     * 
     * @param entity 实体对象
     * @param updateBy 更新人
     * @param updateUserName 更新人姓名
     */
    public static void setUpdateInfo(BaseEntity entity, String updateBy, String updateUserName) {
        if (entity == null) {
            return;
        }

        

    }
    
    /**
     * 设置创建和更新信息
     * 
     * @param entity 实体对象
     * @param userId 用户ID
     * @param userName 用户姓名
     */
    public static void setCreateAndUpdateInfo(BaseEntity entity, String userId, String userName) {
        setCreateInfo(entity, userId, userName);
    }
    
    /**
     * 批量设置创建信息
     * 
     * @param entityList 实体列表
     * @param createBy 创建人
     * @param createUserName 创建人姓名
     */
    public static void batchSetCreateInfo(List<? extends BaseEntity> entityList, 
                                         String createBy, String createUserName) {
        if (entityList == null || entityList.isEmpty()) {
            return;
        }
        
        Timestamp now = new Timestamp(System.currentTimeMillis());
        for (BaseEntity entity : entityList) {
            if (entity != null) {
                entity.setId(generateId());
            }
        }
    }
    
    /**
     * 批量设置更新信息
     * 
     * @param entityList 实体列表
     * @param updateBy 更新人
     * @param updateUserName 更新人姓名
     */
    public static void batchSetUpdateInfo(List<? extends BaseEntity> entityList, 
                                        String updateBy, String updateUserName) {
        if (entityList == null || entityList.isEmpty()) {
            return;
        }
        
        Timestamp now = new Timestamp(System.currentTimeMillis());
        for (BaseEntity entity : entityList) {
            if (entity != null) {
            }
        }
    }
    
    /**
     * 批量设置创建和更新信息
     * 
     * @param entityList 实体列表
     * @param userId 用户ID
     * @param userName 用户姓名
     */
    public static void batchSetCreateAndUpdateInfo(List<? extends BaseEntity> entityList, 
                                                  String userId, String userName) {
        batchSetCreateInfo(entityList, userId, userName);
    }
    
    /**
     * 验证实体是否有效
     * 
     * @param entity 实体对象
     * @return 是否有效
     */
    public static boolean isValidEntity(BaseEntity entity) {
        return entity != null && StrUtil.isNotBlank(entity.getId());
    }
    
    /**
     * 验证实体列表是否有效
     * 
     * @param entityList 实体列表
     * @return 是否有效
     */
    public static boolean isValidEntityList(List<? extends BaseEntity> entityList) {
        if (entityList == null || entityList.isEmpty()) {
            return false;
        }
        
        for (BaseEntity entity : entityList) {
            if (!isValidEntity(entity)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 获取当前时间戳
     * 
     * @return 当前时间戳
     */
    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
    
    /**
     * 格式化SQL字段名
     * 将驼峰命名转换为下划线命名
     * 
     * @param fieldName 字段名
     * @return 格式化后的字段名
     */
    public static String formatSqlField(String fieldName) {
        if (StrUtil.isBlank(fieldName)) {
            return fieldName;
        }
        
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < fieldName.length(); i++) {
            char c = fieldName.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) {
                    result.append('_');
                }
                result.append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        
        return result.toString();
    }
    
    /**
     * 格式化Java字段名
     * 将下划线命名转换为驼峰命名
     * 
     * @param sqlFieldName SQL字段名
     * @return Java字段名
     */
    public static String formatJavaField(String sqlFieldName) {
        if (StrUtil.isBlank(sqlFieldName)) {
            return sqlFieldName;
        }
        
        StringBuilder result = new StringBuilder();
        boolean toUpperCase = false;
        
        for (int i = 0; i < sqlFieldName.length(); i++) {
            char c = sqlFieldName.charAt(i);
            if (c == '_') {
                toUpperCase = true;
            } else {
                if (toUpperCase) {
                    result.append(Character.toUpperCase(c));
                    toUpperCase = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            }
        }
        
        return result.toString();
    }
    
    /**
     * 生成表名
     * 将实体类名转换为数据库表名
     * 
     * @param entityClass 实体类
     * @return 表名
     */
    public static String generateTableName(Class<?> entityClass) {
        if (entityClass == null) {
            return null;
        }
        
        String className = entityClass.getSimpleName();
        // 移除Entity后缀
        if (className.endsWith("Entity")) {
            className = className.substring(0, className.length() - 6);
        }
        
        return formatSqlField(className);
    }
    
    /**
     * 生成字段名
     * 将Java字段名转换为数据库字段名
     * 
     * @param javaFieldName Java字段名
     * @return 数据库字段名
     */
    public static String generateFieldName(String javaFieldName) {
        return formatSqlField(javaFieldName);
    }
}
