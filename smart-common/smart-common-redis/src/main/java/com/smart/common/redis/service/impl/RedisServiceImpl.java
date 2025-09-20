package com.smart.common.redis.service.impl;

import com.smart.common.redis.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis基础操作服务实现类
 * 提供Redis各种数据类型的操作功能实现
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Service
public class RedisServiceImpl implements RedisService {

    private static final Logger log = LoggerFactory.getLogger(RedisServiceImpl.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // ==================== String操作 ====================

    /**
     * 设置键值对
     * 
     * @param key 键
     * @param value 值
     */
    @Override
    public void set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            log.debug("Redis set operation successful: key={}, value={}", key, value);
        } catch (Exception e) {
            log.error("Redis set operation failed: key={}, value={}", key, value, e);
            throw new RuntimeException("Redis set operation failed", e);
        }
    }

    /**
     * 设置键值对，并指定过期时间
     * 
     * @param key 键
     * @param value 值
     * @param timeout 过期时间
     * @param unit 时间单位
     */
    @Override
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, unit);
            log.debug("Redis set with timeout operation successful: key={}, value={}, timeout={}, unit={}", 
                key, value, timeout, unit);
        } catch (Exception e) {
            log.error("Redis set with timeout operation failed: key={}, value={}, timeout={}, unit={}", 
                key, value, timeout, unit, e);
            throw new RuntimeException("Redis set with timeout operation failed", e);
        }
    }

    /**
     * 获取指定键的值
     * 
     * @param key 键
     * @param clazz 返回值类型
     * @param <T> 泛型类型
     * @return 值
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if (value == null) {
                log.debug("Redis get operation returned null: key={}", key);
                return null;
            }
            log.debug("Redis get operation successful: key={}, value={}", key, value);
            return (T) value;
        } catch (Exception e) {
            log.error("Redis get operation failed: key={}", key, e);
            throw new RuntimeException("Redis get operation failed", e);
        }
    }

    /**
     * 删除指定键
     * 
     * @param key 键
     * @return 是否删除成功
     */
    @Override
    public Boolean delete(String key) {
        try {
            Boolean result = redisTemplate.delete(key);
            log.debug("Redis delete operation successful: key={}, result={}", key, result);
            return result;
        } catch (Exception e) {
            log.error("Redis delete operation failed: key={}", key, e);
            throw new RuntimeException("Redis delete operation failed", e);
        }
    }

    /**
     * 检查键是否存在
     * 
     * @param key 键
     * @return 是否存在
     */
    @Override
    public Boolean exists(String key) {
        try {
            Boolean result = redisTemplate.hasKey(key);
            log.debug("Redis exists operation successful: key={}, result={}", key, result);
            return result;
        } catch (Exception e) {
            log.error("Redis exists operation failed: key={}", key, e);
            throw new RuntimeException("Redis exists operation failed", e);
        }
    }

    /**
     * 设置键的过期时间
     * 
     * @param key 键
     * @param timeout 过期时间
     * @param unit 时间单位
     * @return 是否设置成功
     */
    @Override
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        try {
            Boolean result = redisTemplate.expire(key, timeout, unit);
            log.debug("Redis expire operation successful: key={}, timeout={}, unit={}, result={}", 
                key, timeout, unit, result);
            return result;
        } catch (Exception e) {
            log.error("Redis expire operation failed: key={}, timeout={}, unit={}", 
                key, timeout, unit, e);
            throw new RuntimeException("Redis expire operation failed", e);
        }
    }

    // ==================== Hash操作 ====================

    /**
     * 设置Hash字段值
     * 
     * @param key 键
     * @param field 字段
     * @param value 值
     */
    @Override
    public void hSet(String key, String field, Object value) {
        try {
            redisTemplate.opsForHash().put(key, field, value);
            log.debug("Redis hSet operation successful: key={}, field={}, value={}", key, field, value);
        } catch (Exception e) {
            log.error("Redis hSet operation failed: key={}, field={}, value={}", key, field, value, e);
            throw new RuntimeException("Redis hSet operation failed", e);
        }
    }

    /**
     * 获取Hash字段值
     * 
     * @param key 键
     * @param field 字段
     * @param clazz 返回值类型
     * @param <T> 泛型类型
     * @return 值
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T hGet(String key, String field, Class<T> clazz) {
        try {
            Object value = redisTemplate.opsForHash().get(key, field);
            if (value == null) {
                log.debug("Redis hGet operation returned null: key={}, field={}", key, field);
                return null;
            }
            log.debug("Redis hGet operation successful: key={}, field={}, value={}", key, field, value);
            return (T) value;
        } catch (Exception e) {
            log.error("Redis hGet operation failed: key={}, field={}", key, field, e);
            throw new RuntimeException("Redis hGet operation failed", e);
        }
    }

    /**
     * 获取Hash所有字段和值
     * 
     * @param key 键
     * @return 字段值映射
     */
    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> hGetAll(String key) {
        try {
            Map<Object, Object> rawMap = redisTemplate.opsForHash().entries(key);
            Map<String, Object> result = (Map<String, Object>) (Map<?, ?>) rawMap;
            log.debug("Redis hGetAll operation successful: key={}, size={}", key, result.size());
            return result;
        } catch (Exception e) {
            log.error("Redis hGetAll operation failed: key={}", key, e);
            throw new RuntimeException("Redis hGetAll operation failed", e);
        }
    }

    /**
     * 删除Hash字段
     * 
     * @param key 键
     * @param fields 字段数组
     * @return 删除的字段数量
     */
    @Override
    public Boolean hDel(String key, String... fields) {
        try {
            Long result = redisTemplate.opsForHash().delete(key, (Object[]) fields);
            log.debug("Redis hDel operation successful: key={}, fields={}, deletedCount={}", 
                key, fields, result);
            return result > 0;
        } catch (Exception e) {
            log.error("Redis hDel operation failed: key={}, fields={}", key, fields, e);
            throw new RuntimeException("Redis hDel operation failed", e);
        }
    }

    /**
     * 检查Hash字段是否存在
     * 
     * @param key 键
     * @param field 字段
     * @return 是否存在
     */
    @Override
    public Boolean hExists(String key, String field) {
        try {
            Boolean result = redisTemplate.opsForHash().hasKey(key, field);
            log.debug("Redis hExists operation successful: key={}, field={}, result={}", 
                key, field, result);
            return result;
        } catch (Exception e) {
            log.error("Redis hExists operation failed: key={}, field={}", key, field, e);
            throw new RuntimeException("Redis hExists operation failed", e);
        }
    }

    // ==================== List操作 ====================

    /**
     * 从列表左侧推入元素
     * 
     * @param key 键
     * @param values 值数组
     * @return 列表长度
     */
    @Override
    public Long lPush(String key, Object... values) {
        try {
            Long result = redisTemplate.opsForList().leftPushAll(key, values);
            log.debug("Redis lPush operation successful: key={}, values={}, length={}", 
                key, values, result);
            return result;
        } catch (Exception e) {
            log.error("Redis lPush operation failed: key={}, values={}", key, values, e);
            throw new RuntimeException("Redis lPush operation failed", e);
        }
    }

    /**
     * 从列表右侧推入元素
     * 
     * @param key 键
     * @param values 值数组
     * @return 列表长度
     */
    @Override
    public Long rPush(String key, Object... values) {
        try {
            Long result = redisTemplate.opsForList().rightPushAll(key, values);
            log.debug("Redis rPush operation successful: key={}, values={}, length={}", 
                key, values, result);
            return result;
        } catch (Exception e) {
            log.error("Redis rPush operation failed: key={}, values={}", key, values, e);
            throw new RuntimeException("Redis rPush operation failed", e);
        }
    }

    /**
     * 从列表左侧弹出元素
     * 
     * @param key 键
     * @param clazz 返回值类型
     * @param <T> 泛型类型
     * @return 弹出的值
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T lPop(String key, Class<T> clazz) {
        try {
            Object value = redisTemplate.opsForList().leftPop(key);
            if (value == null) {
                log.debug("Redis lPop operation returned null: key={}", key);
                return null;
            }
            log.debug("Redis lPop operation successful: key={}, value={}", key, value);
            return (T) value;
        } catch (Exception e) {
            log.error("Redis lPop operation failed: key={}", key, e);
            throw new RuntimeException("Redis lPop operation failed", e);
        }
    }

    /**
     * 从列表右侧弹出元素
     * 
     * @param key 键
     * @param clazz 返回值类型
     * @param <T> 泛型类型
     * @return 弹出的值
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T rPop(String key, Class<T> clazz) {
        try {
            Object value = redisTemplate.opsForList().rightPop(key);
            if (value == null) {
                log.debug("Redis rPop operation returned null: key={}", key);
                return null;
            }
            log.debug("Redis rPop operation successful: key={}, value={}", key, value);
            return (T) value;
        } catch (Exception e) {
            log.error("Redis rPop operation failed: key={}", key, e);
            throw new RuntimeException("Redis rPop operation failed", e);
        }
    }

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
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> lRange(String key, long start, long end, Class<T> clazz) {
        try {
            List<Object> rawList = redisTemplate.opsForList().range(key, start, end);
            List<T> result = (List<T>) (List<?>) rawList;
            log.debug("Redis lRange operation successful: key={}, start={}, end={}, size={}", 
                key, start, end, result.size());
            return result;
        } catch (Exception e) {
            log.error("Redis lRange operation failed: key={}, start={}, end={}", key, start, end, e);
            throw new RuntimeException("Redis lRange operation failed", e);
        }
    }

    /**
     * 获取列表长度
     * 
     * @param key 键
     * @return 列表长度
     */
    @Override
    public Long lLen(String key) {
        try {
            Long result = redisTemplate.opsForList().size(key);
            log.debug("Redis lLen operation successful: key={}, length={}", key, result);
            return result;
        } catch (Exception e) {
            log.error("Redis lLen operation failed: key={}", key, e);
            throw new RuntimeException("Redis lLen operation failed", e);
        }
    }

    // ==================== Set操作 ====================

    /**
     * 向集合添加元素
     * 
     * @param key 键
     * @param values 值数组
     * @return 添加的元素数量
     */
    @Override
    public Long sAdd(String key, Object... values) {
        try {
            Long result = redisTemplate.opsForSet().add(key, values);
            log.debug("Redis sAdd operation successful: key={}, values={}, addedCount={}", 
                key, values, result);
            return result;
        } catch (Exception e) {
            log.error("Redis sAdd operation failed: key={}, values={}", key, values, e);
            throw new RuntimeException("Redis sAdd operation failed", e);
        }
    }

    /**
     * 从集合删除元素
     * 
     * @param key 键
     * @param values 值数组
     * @return 删除的元素数量
     */
    @Override
    public Long sRem(String key, Object... values) {
        try {
            Long result = redisTemplate.opsForSet().remove(key, values);
            log.debug("Redis sRem operation successful: key={}, values={}, removedCount={}", 
                key, values, result);
            return result;
        } catch (Exception e) {
            log.error("Redis sRem operation failed: key={}, values={}", key, values, e);
            throw new RuntimeException("Redis sRem operation failed", e);
        }
    }

    /**
     * 获取集合所有成员
     * 
     * @param key 键
     * @param clazz 返回值类型
     * @param <T> 泛型类型
     * @return 成员集合
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> Set<T> sMembers(String key, Class<T> clazz) {
        try {
            Set<Object> rawSet = redisTemplate.opsForSet().members(key);
            Set<T> result = (Set<T>) (Set<?>) rawSet;
            log.debug("Redis sMembers operation successful: key={}, size={}", key, result.size());
            return result;
        } catch (Exception e) {
            log.error("Redis sMembers operation failed: key={}", key, e);
            throw new RuntimeException("Redis sMembers operation failed", e);
        }
    }

    /**
     * 检查元素是否在集合中
     * 
     * @param key 键
     * @param value 值
     * @return 是否存在
     */
    @Override
    public Boolean sIsMember(String key, Object value) {
        try {
            Boolean result = redisTemplate.opsForSet().isMember(key, value);
            log.debug("Redis sIsMember operation successful: key={}, value={}, result={}", 
                key, value, result);
            return result;
        } catch (Exception e) {
            log.error("Redis sIsMember operation failed: key={}, value={}", key, value, e);
            throw new RuntimeException("Redis sIsMember operation failed", e);
        }
    }

    /**
     * 获取集合大小
     * 
     * @param key 键
     * @return 集合大小
     */
    @Override
    public Long sSize(String key) {
        try {
            Long result = redisTemplate.opsForSet().size(key);
            log.debug("Redis sSize operation successful: key={}, size={}", key, result);
            return result;
        } catch (Exception e) {
            log.error("Redis sSize operation failed: key={}", key, e);
            throw new RuntimeException("Redis sSize operation failed", e);
        }
    }

    // ==================== ZSet操作 ====================

    /**
     * 向有序集合添加元素
     * 
     * @param key 键
     * @param value 值
     * @param score 分数
     * @return 是否添加成功
     */
    @Override
    public Boolean zAdd(String key, Object value, double score) {
        try {
            Boolean result = redisTemplate.opsForZSet().add(key, value, score);
            log.debug("Redis zAdd operation successful: key={}, value={}, score={}, result={}", 
                key, value, score, result);
            return result;
        } catch (Exception e) {
            log.error("Redis zAdd operation failed: key={}, value={}, score={}", key, value, score, e);
            throw new RuntimeException("Redis zAdd operation failed", e);
        }
    }

    /**
     * 从有序集合删除元素
     * 
     * @param key 键
     * @param values 值数组
     * @return 删除的元素数量
     */
    @Override
    public Long zRem(String key, Object... values) {
        try {
            Long result = redisTemplate.opsForZSet().remove(key, values);
            log.debug("Redis zRem operation successful: key={}, values={}, removedCount={}", 
                key, values, result);
            return result;
        } catch (Exception e) {
            log.error("Redis zRem operation failed: key={}, values={}", key, values, e);
            throw new RuntimeException("Redis zRem operation failed", e);
        }
    }

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
    @Override
    @SuppressWarnings("unchecked")
    public <T> Set<T> zRange(String key, long start, long end, Class<T> clazz) {
        try {
            Set<Object> rawSet = redisTemplate.opsForZSet().range(key, start, end);
            Set<T> result = (Set<T>) (Set<?>) rawSet;
            log.debug("Redis zRange operation successful: key={}, start={}, end={}, size={}", 
                key, start, end, result.size());
            return result;
        } catch (Exception e) {
            log.error("Redis zRange operation failed: key={}, start={}, end={}", key, start, end, e);
            throw new RuntimeException("Redis zRange operation failed", e);
        }
    }

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
    @Override
    @SuppressWarnings("unchecked")
    public <T> Set<T> zRangeByScore(String key, double min, double max, Class<T> clazz) {
        try {
            Set<Object> rawSet = redisTemplate.opsForZSet().rangeByScore(key, min, max);
            Set<T> result = (Set<T>) (Set<?>) rawSet;
            log.debug("Redis zRangeByScore operation successful: key={}, min={}, max={}, size={}", 
                key, min, max, result.size());
            return result;
        } catch (Exception e) {
            log.error("Redis zRangeByScore operation failed: key={}, min={}, max={}", key, min, max, e);
            throw new RuntimeException("Redis zRangeByScore operation failed", e);
        }
    }

    /**
     * 获取元素的分数
     * 
     * @param key 键
     * @param value 值
     * @return 分数
     */
    @Override
    public Double zScore(String key, Object value) {
        try {
            Double result = redisTemplate.opsForZSet().score(key, value);
            log.debug("Redis zScore operation successful: key={}, value={}, score={}", 
                key, value, result);
            return result;
        } catch (Exception e) {
            log.error("Redis zScore operation failed: key={}, value={}", key, value, e);
            throw new RuntimeException("Redis zScore operation failed", e);
        }
    }

    /**
     * 获取有序集合大小
     * 
     * @param key 键
     * @return 集合大小
     */
    @Override
    public Long zSize(String key) {
        try {
            Long result = redisTemplate.opsForZSet().size(key);
            log.debug("Redis zSize operation successful: key={}, size={}", key, result);
            return result;
        } catch (Exception e) {
            log.error("Redis zSize operation failed: key={}", key, e);
            throw new RuntimeException("Redis zSize operation failed", e);
        }
    }

    // ==================== 批量操作 ====================

    /**
     * 批量设置键值对
     * 
     * @param keyValues 键值对映射
     */
    @Override
    public void batchSet(Map<String, Object> keyValues) {
        try {
            redisTemplate.opsForValue().multiSet(keyValues);
            log.debug("Redis batchSet operation successful: size={}", keyValues.size());
        } catch (Exception e) {
            log.error("Redis batchSet operation failed: size={}", keyValues.size(), e);
            throw new RuntimeException("Redis batchSet operation failed", e);
        }
    }

    /**
     * 批量获取值
     * 
     * @param keys 键列表
     * @param clazz 返回值类型
     * @param <T> 泛型类型
     * @return 值列表
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> batchGet(List<String> keys, Class<T> clazz) {
        try {
            List<Object> rawList = redisTemplate.opsForValue().multiGet(keys);
            List<T> result = (List<T>) (List<?>) rawList;
            log.debug("Redis batchGet operation successful: keys={}, size={}", keys.size(), result.size());
            return result;
        } catch (Exception e) {
            log.error("Redis batchGet operation failed: keys={}", keys, e);
            throw new RuntimeException("Redis batchGet operation failed", e);
        }
    }

    /**
     * 批量删除键
     * 
     * @param keys 键列表
     * @return 删除的键数量
     */
    @Override
    public Long batchDelete(List<String> keys) {
        try {
            Long result = redisTemplate.delete(keys);
            log.debug("Redis batchDelete operation successful: keys={}, deletedCount={}", 
                keys.size(), result);
            return result;
        } catch (Exception e) {
            log.error("Redis batchDelete operation failed: keys={}", keys, e);
            throw new RuntimeException("Redis batchDelete operation failed", e);
        }
    }
}
