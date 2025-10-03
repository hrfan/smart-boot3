package com.smart.framework.redis.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis基础操作服务接口
 * 提供Redis各种数据类型的操作功能
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public interface RedisService {

    // ==================== String操作 ====================

    /**
     * 设置键值对
     * 
     * @param key 键
     * @param value 值
     */
    void set(String key, Object value);

    /**
     * 设置键值对，并指定过期时间
     * 
     * @param key 键
     * @param value 值
     * @param timeout 过期时间
     * @param unit 时间单位
     */
    void set(String key, Object value, long timeout, TimeUnit unit);

    /**
     * 获取指定键的值
     * 
     * @param key 键
     * @param clazz 返回值类型
     * @param <T> 泛型类型
     * @return 值
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * 删除指定键
     * 
     * @param key 键
     * @return 是否删除成功
     */
    Boolean delete(String key);

    /**
     * 检查键是否存在
     * 
     * @param key 键
     * @return 是否存在
     */
    Boolean exists(String key);

    /**
     * 设置键的过期时间
     * 
     * @param key 键
     * @param timeout 过期时间
     * @param unit 时间单位
     * @return 是否设置成功
     */
    Boolean expire(String key, long timeout, TimeUnit unit);

    // ==================== Hash操作 ====================

    /**
     * 设置Hash字段值
     * 
     * @param key 键
     * @param field 字段
     * @param value 值
     */
    void hSet(String key, String field, Object value);

    /**
     * 获取Hash字段值
     * 
     * @param key 键
     * @param field 字段
     * @param clazz 返回值类型
     * @param <T> 泛型类型
     * @return 值
     */
    <T> T hGet(String key, String field, Class<T> clazz);

    /**
     * 获取Hash所有字段和值
     * 
     * @param key 键
     * @return 字段值映射
     */
    Map<String, Object> hGetAll(String key);

    /**
     * 删除Hash字段
     * 
     * @param key 键
     * @param fields 字段数组
     * @return 删除的字段数量
     */
    Boolean hDel(String key, String... fields);

    /**
     * 检查Hash字段是否存在
     * 
     * @param key 键
     * @param field 字段
     * @return 是否存在
     */
    Boolean hExists(String key, String field);

    // ==================== List操作 ====================

    /**
     * 从列表左侧推入元素
     * 
     * @param key 键
     * @param values 值数组
     * @return 列表长度
     */
    Long lPush(String key, Object... values);

    /**
     * 从列表右侧推入元素
     * 
     * @param key 键
     * @param values 值数组
     * @return 列表长度
     */
    Long rPush(String key, Object... values);

    /**
     * 从列表左侧弹出元素
     * 
     * @param key 键
     * @param clazz 返回值类型
     * @param <T> 泛型类型
     * @return 弹出的值
     */
    <T> T lPop(String key, Class<T> clazz);

    /**
     * 从列表右侧弹出元素
     * 
     * @param key 键
     * @param clazz 返回值类型
     * @param <T> 泛型类型
     * @return 弹出的值
     */
    <T> T rPop(String key, Class<T> clazz);

    /**
     * 获取列表指定范围的元素
     * 
     * @param key 键
     * @param start 开始位置
     * @param end 结束位置
     * @param clazz 返回值类型
     * @param <T> 泛型类型
     * @return 元素列表
     */
    <T> List<T> lRange(String key, long start, long end, Class<T> clazz);

    /**
     * 获取列表长度
     * 
     * @param key 键
     * @return 列表长度
     */
    Long lLen(String key);

    // ==================== Set操作 ====================

    /**
     * 向集合添加元素
     * 
     * @param key 键
     * @param values 值数组
     * @return 添加的元素数量
     */
    Long sAdd(String key, Object... values);

    /**
     * 从集合删除元素
     * 
     * @param key 键
     * @param values 值数组
     * @return 删除的元素数量
     */
    Long sRem(String key, Object... values);

    /**
     * 获取集合所有成员
     * 
     * @param key 键
     * @param clazz 返回值类型
     * @param <T> 泛型类型
     * @return 成员集合
     */
    <T> Set<T> sMembers(String key, Class<T> clazz);

    /**
     * 检查元素是否在集合中
     * 
     * @param key 键
     * @param value 值
     * @return 是否存在
     */
    Boolean sIsMember(String key, Object value);

    /**
     * 获取集合大小
     * 
     * @param key 键
     * @return 集合大小
     */
    Long sSize(String key);

    // ==================== ZSet操作 ====================

    /**
     * 向有序集合添加元素
     * 
     * @param key 键
     * @param value 值
     * @param score 分数
     * @return 是否添加成功
     */
    Boolean zAdd(String key, Object value, double score);

    /**
     * 从有序集合删除元素
     * 
     * @param key 键
     * @param values 值数组
     * @return 删除的元素数量
     */
    Long zRem(String key, Object... values);

    /**
     * 获取有序集合指定范围的元素
     * 
     * @param key 键
     * @param start 开始位置
     * @param end 结束位置
     * @param clazz 返回值类型
     * @param <T> 泛型类型
     * @return 元素集合
     */
    <T> Set<T> zRange(String key, long start, long end, Class<T> clazz);

    /**
     * 获取有序集合指定分数范围的元素
     * 
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     * @param clazz 返回值类型
     * @param <T> 泛型类型
     * @return 元素集合
     */
    <T> Set<T> zRangeByScore(String key, double min, double max, Class<T> clazz);

    /**
     * 获取元素的分数
     * 
     * @param key 键
     * @param value 值
     * @return 分数
     */
    Double zScore(String key, Object value);

    /**
     * 获取有序集合大小
     * 
     * @param key 键
     * @return 集合大小
     */
    Long zSize(String key);

    // ==================== 批量操作 ====================

    /**
     * 批量设置键值对
     * 
     * @param keyValues 键值对映射
     */
    void batchSet(Map<String, Object> keyValues);

    /**
     * 批量获取值
     * 
     * @param keys 键列表
     * @param clazz 返回值类型
     * @param <T> 泛型类型
     * @return 值列表
     */
    <T> List<T> batchGet(List<String> keys, Class<T> clazz);

    /**
     * 批量删除键
     * 
     * @param keys 键列表
     * @return 删除的键数量
     */
    Long batchDelete(List<String> keys);
}
