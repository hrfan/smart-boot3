# Smart Boot3 Redis模块使用指南

## 概述

Smart Boot3 Redis模块提供了完整的Redis操作封装，包括基础操作、简化分布式锁、缓存管理、消息队列、会话管理等功能。

## 功能特性

- ✅ **基础Redis操作**: 支持String、Hash、List、Set、ZSet等数据类型
- ✅ **简化分布式锁**: 支持基本锁获取、释放、续期、状态检查
- ✅ **缓存管理**: Spring Cache集成、缓存预热、三防机制
- ✅ **自定义注解**: @DistributedLock注解简化使用
- ✅ **监控健康**: Spring Boot Actuator集成、性能指标监控
- ✅ **异常处理**: 完整的异常处理机制

## 快速开始

### 1. 添加依赖

在项目的`pom.xml`中添加Redis模块依赖：

```xml
<dependency>
    <groupId>com.smart</groupId>
    <artifactId>smart-common-redis</artifactId>
</dependency>
```

### 2. 配置Redis连接

在`application.yml`中配置Redis连接信息：

```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password: 
    database: 0
    timeout: 3000ms
    jedis:
      pool:
        max-active: 20
        max-idle: 10
        min-idle: 5
        max-wait: 3000ms
```

### 3. 启用Redis功能

在启动类上添加注解：

```java
@SpringBootApplication
@EnableCaching
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

## 基础Redis操作

### 注入RedisService

```java
@Autowired
private RedisService redisService;
```

### String操作

```java
// 设置值
redisService.set("user:1", "张三");

// 设置值并指定过期时间
redisService.set("user:1", "张三", 30, TimeUnit.MINUTES);

// 获取值
String user = redisService.get("user:1", String.class);

// 检查键是否存在
Boolean exists = redisService.exists("user:1");

// 删除键
Boolean deleted = redisService.delete("user:1");
```

### Hash操作

```java
// 设置Hash字段
redisService.hSet("user:profile:1", "name", "张三");
redisService.hSet("user:profile:1", "age", 25);

// 获取Hash字段
String name = redisService.hGet("user:profile:1", "name", String.class);

// 获取所有Hash字段
Map<String, Object> profile = redisService.hGetAll("user:profile:1");

// 删除Hash字段
Boolean deleted = redisService.hDel("user:profile:1", "age");
```

### List操作

```java
// 从左侧推入
redisService.lPush("task:queue", "task1", "task2");

// 从右侧推入
redisService.rPush("task:queue", "task3");

// 从左侧弹出
String task = redisService.lPop("task:queue", String.class);

// 获取列表范围
List<String> tasks = redisService.lRange("task:queue", 0, -1, String.class);
```

### Set操作

```java
// 添加元素
redisService.sAdd("user:tags:1", "vip", "premium");

// 检查元素是否存在
Boolean isVip = redisService.sIsMember("user:tags:1", "vip");

// 获取所有成员
Set<String> tags = redisService.sMembers("user:tags:1", String.class);

// 删除元素
redisService.sRem("user:tags:1", "vip");
```

### ZSet操作

```java
// 添加元素
redisService.zAdd("leaderboard", "player1", 100.0);

// 获取分数
Double score = redisService.zScore("leaderboard", "player1");

// 获取排名范围
Set<String> topPlayers = redisService.zRange("leaderboard", 0, 9, String.class);

// 按分数范围获取
Set<String> highScorePlayers = redisService.zRangeByScore("leaderboard", 90.0, 100.0, String.class);
```

## 分布式锁

Smart Boot3 Redis模块提供了简化版的分布式锁功能，支持基本的锁获取、释放、续期和状态检查。

### 功能特性

- ✅ **立即返回**: 获取不到锁时立即返回失败，不等待
- ✅ **原子操作**: 使用Lua脚本保证操作的原子性
- ✅ **自动过期**: 支持设置锁的过期时间，防止死锁
- ✅ **锁续期**: 支持手动续期锁的过期时间
- ✅ **状态检查**: 支持检查锁的有效性和剩余时间
- ✅ **注解支持**: 提供`@DistributedLock`注解简化使用

### 使用注解

```java
@Service
public class OrderService {
    
    @DistributedLock(key = "order:lock:#orderId", timeout = 30, unit = TimeUnit.SECONDS, 
                     failStrategy = FailStrategy.THROW_EXCEPTION)
    public void processOrder(String orderId) {
        // 处理订单逻辑
        System.out.println("Processing order: " + orderId);
    }
}
```

### 注解参数说明

- `key`: 锁的键名，支持SpEL表达式
- `timeout`: 锁的过期时间
- `unit`: 时间单位
- `failStrategy`: 获取锁失败时的处理策略
  - `THROW_EXCEPTION`: 抛出异常（默认）
  - `RETURN_NULL`: 返回null
  - `RETURN_FALSE`: 返回false
  - `WAIT_RETRY`: 等待重试

### 编程式使用

#### acquireLock 方法

`acquireLock` 方法尝试获取锁并返回 `DistributedLock` 对象，无论成功与否都会立即返回：

```java
@Autowired
private DistributedLockService lockService;

public void processOrder(String orderId) {
    String lockKey = "order:lock:" + orderId;
    
    // 尝试获取锁
    DistributedLock lock = lockService.acquireLock(lockKey, 30, TimeUnit.SECONDS);
    
    // 检查锁是否获取成功
    if (lock != null && lock.isValid()) {
        try {
            // 执行业务逻辑
            System.out.println("Processing order: " + orderId);
            
            // 检查锁状态
            if (lock.isExpired()) {
                System.out.println("Lock expired, remaining TTL: " + lock.getRemainingTtl());
            }
            
            // 手动续期（如果需要）
            boolean renewed = lock.renewLock(60, TimeUnit.SECONDS);
            if (renewed) {
                System.out.println("Lock renewed successfully");
            }
            
        } finally {
            // 释放锁
            boolean unlocked = lock.unlock();
            System.out.println("Lock released: " + unlocked);
        }
    } else {
        // 锁获取失败，立即返回错误
        throw new RuntimeException("Failed to acquire lock for order: " + orderId);
    }
}
```

#### tryLock 方法

`tryLock` 方法尝试获取锁并返回布尔值，表示是否获取成功：

```java
@Autowired
private DistributedLockService lockService;

public void processOrder(String orderId) {
    String lockKey = "order:lock:" + orderId;
    
    try {
        // 尝试获取锁，返回布尔值
        boolean success = lockService.tryLock(lockKey, 30, TimeUnit.SECONDS);
        
        if (success) {
            try {
                // 执行业务逻辑
                System.out.println("Processing order: " + orderId);
                
                // 注意：tryLock成功后需要手动释放锁
                // 由于tryLock不返回DistributedLock对象，需要重新获取锁对象来释放
                DistributedLock lock = lockService.acquireLock(lockKey, 30, TimeUnit.SECONDS);
                if (lock != null && lock.isValid()) {
                    lock.unlock();
                }
                
            } catch (Exception e) {
                // 业务逻辑异常处理
                System.err.println("Business logic failed: " + e.getMessage());
            }
        } else {
            // 锁获取失败，立即返回
            System.out.println("Failed to acquire lock, operation skipped");
        }
    } catch (Exception e) {
        System.err.println("Lock operation failed: " + e.getMessage());
    }
}
```

### acquireLock vs tryLock 区别

| 特性 | acquireLock | tryLock |
|------|-------------|---------|
| **返回值** | `DistributedLock` 对象 | `boolean` 值 |
| **锁信息** | 可以获取锁的详细信息（key、value、TTL等） | 只能知道是否获取成功 |
| **释放锁** | 通过返回的 `DistributedLock` 对象释放 | 需要重新获取锁对象来释放 |
| **使用场景** | 需要锁的详细信息或进行锁管理 | 简单的互斥控制，不需要锁的详细信息 |
| **代码复杂度** | 相对复杂，需要检查锁有效性 | 相对简单，直接判断布尔值 |

### 实际测试案例

基于 `TestController` 中的测试方法：

#### r1 方法 - acquireLock 示例
```java
@GetMapping("/r1")
public Result testAcquireLock() {
    String lockKey = "HRFAN";
    Map<String, Object> result = new HashMap<>();
    DistributedLock lock = null;

    try {
        log.info("尝试获取锁: {}", lockKey);
        lock = lockService.acquireLock(lockKey, 10, TimeUnit.SECONDS);

        if (lock.isValid()) {
            result.put("status", "锁获取成功");
            result.put("lockKey", lock.getLockKey());
            result.put("lockValue", lock.getLockValue());
            result.put("expireTime", lock.getExpireTime());
            result.put("ttl", lock.getRemainingTtl());

            log.info("业务逻辑执行中... 模拟持有锁10秒");
            Thread.sleep(10000);
            
        } else {
            result.put("status", "锁获取失败");
            result.put("message", "未能获取到有效锁，可能已被其他线程持有");
        }
    } catch (Exception e) {
        log.error("测试基本锁功能时发生异常: {}", e.getMessage(), e);
        return Result.error("测试基本锁功能失败: " + e.getMessage());
    } finally {
        if (lock != null && lock.isValid()) {
            boolean unlocked = lock.unlock();
            result.put("unlocked", unlocked);
            log.info("锁释放结果: {}", unlocked);
        }
    }

    return Result.success("基本锁测试完成", result);
}
```

#### r2 方法 - tryLock 示例
```java
@GetMapping("/r2")
public Result testTryLock() {
    String lockKey = "HRFAN";
    Map<String, Object> result = new HashMap<>();

    try {
        log.info("尝试使用tryLock获取锁: {}", lockKey);
        boolean success = lockService.tryLock(lockKey, 10, TimeUnit.SECONDS);

        result.put("tryLockResult", success);
        result.put("lockKey", lockKey);

        if (success) {
            result.put("status", "tryLock成功");
            log.info("tryLock成功，模拟业务逻辑执行10秒...");
            Thread.sleep(10000);

            // 释放锁 - tryLock成功后需要重新获取锁对象来释放
            DistributedLock lock = lockService.acquireLock(lockKey, 10, TimeUnit.SECONDS);
            if (lock != null && lock.isValid()) {
                boolean unlocked = lock.unlock();
                result.put("unlocked", unlocked);
            }
        } else {
            result.put("status", "tryLock失败");
            result.put("message", "锁可能已被其他线程持有");
        }
    } catch (Exception e) {
        log.error("测试tryLock功能时发生异常: {}", e.getMessage(), e);
        return Result.error("测试tryLock功能失败: " + e.getMessage());
    }

    return Result.success("tryLock测试完成", result);
}
```

## acquireLock 详细原理

### 核心设计思路

`acquireLock` 方法基于 Redis 的原子操作实现分布式锁，核心思路是：

1. **原子性保证**: 使用 Lua 脚本确保"检查-设置"操作的原子性
2. **唯一性标识**: 使用 UUID 作为锁值，防止误释放其他线程的锁
3. **自动过期**: 设置锁的过期时间，防止死锁
4. **立即返回**: 获取不到锁时立即返回失败，不等待

### 实现原理详解

#### 1. Lua 脚本实现

```lua
-- LOCK_SCRIPT
if redis.call('set', KEYS[1], ARGV[1], 'NX', 'EX', ARGV[2]) then 
    return 1 
else 
    return 0 
end
```

**脚本解析**:
- `KEYS[1]`: 锁的键名
- `ARGV[1]`: 锁的值（UUID）
- `ARGV[2]`: 过期时间（秒）
- `NX`: 只在键不存在时设置
- `EX`: 设置过期时间（秒）

**执行流程**:
1. 检查锁键是否存在
2. 如果不存在，设置锁键和值，并设置过期时间
3. 返回 1 表示成功，返回 0 表示失败

#### 2. 锁值生成策略

```java
public static String generateLockValue() {
    return UUID.randomUUID().toString();
}
```

**设计考虑**:
- **唯一性**: UUID 确保每个锁实例都有唯一标识
- **安全性**: 防止误释放其他线程持有的锁
- **不可预测性**: 避免恶意猜测锁值

#### 3. 锁对象创建

```java
// 成功获取锁
return new DistributedLock(lockKey, lockValue, 
    System.currentTimeMillis() + unit.toMillis(timeout),
    redisTemplate, stringRedisTemplate);

// 获取失败
return new DistributedLock(lockKey, null, -1, 
    redisTemplate, stringRedisTemplate);
```

**设计特点**:
- **成功时**: 创建有效锁对象，包含锁的所有信息
- **失败时**: 创建无效锁对象（lockValue = null），便于后续判断

### 关键特性分析

#### 1. 原子性保证

**问题**: 在高并发环境下，如果"检查锁是否存在"和"设置锁"不是原子操作，可能导致：
- 多个线程同时认为锁不存在
- 多个线程同时设置锁
- 锁的竞争条件

**解决方案**: 使用 Lua 脚本
```lua
-- 原子操作：检查 + 设置
if redis.call('set', KEYS[1], ARGV[1], 'NX', 'EX', ARGV[2]) then
    return 1
else
    return 0
end
```

#### 2. 防误释放机制

**问题**: 如果锁值不是唯一的，可能出现：
- 线程A获取锁，但锁过期了
- 线程B获取了同一个锁
- 线程A尝试释放锁，误释放了线程B的锁

**解决方案**: UUID 唯一标识
```java
String lockValue = DistributedLock.generateLockValue(); // 生成唯一UUID
// 释放时检查锁值是否匹配
if redis.call('get', KEYS[1]) == ARGV[1] then
    return redis.call('del', KEYS[1])
else
    return 0
end
```

#### 3. 死锁预防

**问题**: 如果持有锁的线程崩溃，锁可能永远不会被释放

**解决方案**: 自动过期机制
```java
long expireSeconds = unit.toSeconds(timeout);
// 设置锁的过期时间
redis.call('set', KEYS[1], ARGV[1], 'NX', 'EX', ARGV[2])
```

#### 4. 立即返回策略

**问题**: 传统的分布式锁可能让线程等待，影响系统响应性

**解决方案**: 非阻塞设计
```java
// 无论成功失败都立即返回
Long result = stringRedisTemplate.execute(script, ...);
boolean success = result != null && result == 1L;

if (success) {
    // 返回有效锁对象
    return new DistributedLock(...);
} else {
    // 返回无效锁对象
    return new DistributedLock(lockKey, null, -1, ...);
}
```

### 执行时序图

```
客户端A                     Redis                    客户端B
  |                          |                         |
  |-- acquireLock("order:1") |                         |
  |                          |                         |
  |                          |-- 检查锁是否存在         |
  |                          |-- 不存在，设置锁         |
  |                          |-- 返回 1                |
  |<-- 返回有效锁对象         |                         |
  |                          |                         |
  |-- 执行业务逻辑           |                         |
  |                          |                         |
  |                          |                         |-- acquireLock("order:1")
  |                          |                         |
  |                          |-- 检查锁是否存在         |
  |                          |-- 存在，返回 0          |
  |                          |                         |
  |                          |                         |<-- 返回无效锁对象
  |                          |                         |
  |-- unlock()               |                         |
  |                          |-- 检查锁值是否匹配       |
  |                          |-- 匹配，删除锁           |
  |<-- 返回 true             |                         |
```

### 性能特点

#### 1. 时间复杂度
- **获取锁**: O(1) - 单次 Redis 操作
- **释放锁**: O(1) - 单次 Redis 操作
- **检查锁状态**: O(1) - 单次 Redis 操作

#### 2. 网络开销
- **获取锁**: 1 次网络往返
- **释放锁**: 1 次网络往返
- **总开销**: 最小化网络通信

#### 3. 内存使用
- **锁存储**: 每个锁占用一个 Redis 键
- **过期清理**: Redis 自动清理过期键
- **内存效率**: 高效的内存使用

### 适用场景

#### 1. 适合的场景
- **高并发控制**: 需要精确控制并发访问
- **资源保护**: 保护共享资源不被并发修改
- **任务调度**: 确保任务不被重复执行
- **数据一致性**: 保证数据操作的原子性

#### 2. 不适合的场景
- **长时间持有**: 锁持有时间过长可能导致性能问题
- **频繁获取**: 频繁的锁获取可能影响性能
- **网络不稳定**: Redis 连接不稳定时可能影响锁的可靠性

### 最佳实践

#### 1. 锁粒度控制
```java
// 好的做法：细粒度锁
String lockKey = "order:process:" + orderId;

// 避免：粗粒度锁
String lockKey = "global:order:lock";
```

#### 2. 过期时间设置
```java
// 根据业务处理时间设置合理的过期时间
DistributedLock lock = lockService.acquireLock(lockKey, 30, TimeUnit.SECONDS);
```

#### 3. 异常处理
```java
DistributedLock lock = lockService.acquireLock(lockKey, 30, TimeUnit.SECONDS);
try {
    if (lock != null && lock.isValid()) {
        // 业务逻辑
    } else {
        // 锁获取失败的处理
        throw new BusinessException("系统繁忙，请稍后重试");
    }
} finally {
    if (lock != null && lock.isValid()) {
        lock.unlock();
    }
}
```

### 锁状态检查

```java
@Autowired
private DistributedLockService lockService;

public void checkLockStatus(String lockKey) {
    // 检查锁是否存在
    boolean isLocked = lockService.isLocked(lockKey);
    System.out.println("Lock exists: " + isLocked);
    
    // 获取锁的剩余过期时间
    long ttl = lockService.getLockTtl(lockKey);
    System.out.println("Lock TTL: " + ttl + "ms");
}
```

### 最佳实践

1. **锁键命名规范**
   ```java
   // 使用业务相关的键名
   String lockKey = "order:process:" + orderId;
   String lockKey = "user:update:" + userId;
   String lockKey = "inventory:deduct:" + productId;
   ```

2. **合理的过期时间**
   ```java
   // 根据业务处理时间设置合理的过期时间
   DistributedLock lock = lockService.acquireLock(lockKey, 30, TimeUnit.SECONDS);
   ```

3. **异常处理**
   ```java
   try {
       DistributedLock lock = lockService.acquireLock(lockKey, 30, TimeUnit.SECONDS);
       if (lock != null && lock.isValid()) {
           // 业务逻辑
       } else {
           // 锁获取失败的处理
           throw new BusinessException("系统繁忙，请稍后重试");
       }
   } catch (Exception e) {
       log.error("Distributed lock operation failed", e);
       throw new BusinessException("操作失败，请重试");
   }
   ```

4. **避免死锁**
   ```java
   // 总是使用try-finally确保锁被释放
   DistributedLock lock = lockService.acquireLock(lockKey, 30, TimeUnit.SECONDS);
   try {
       if (lock != null && lock.isValid()) {
           // 业务逻辑
       }
   } finally {
       if (lock != null && lock.isValid()) {
           lock.unlock();
       }
   }
   ```

## 缓存管理

### 使用注解

```java
@Service
public class UserService {
    
    @Cacheable(value = "users", key = "#userId")
    public User getUserById(String userId) {
        // 从数据库查询用户
        return userRepository.findById(userId);
    }
    
    @CacheEvict(value = "users", key = "#user.id")
    public void updateUser(User user) {
        userRepository.save(user);
    }
}
```

### 编程式使用

```java
@Autowired
private CacheService cacheService;

public User getUserById(String userId) {
    String cacheKey = "user:" + userId;
    
    // 尝试从缓存获取
    User user = cacheService.getCache(cacheKey, User.class);
    
    if (user == null) {
        // 从数据库查询
        user = userRepository.findById(userId);
        
        // 缓存结果
        cacheService.cache(cacheKey, user, 30, TimeUnit.MINUTES);
    }
    
    return user;
}
```

## 监控和健康检查

### 健康检查端点

访问 `http://localhost:8080/actuator/health` 查看Redis健康状态：

```json
{
  "status": "UP",
  "components": {
    "redis": {
      "status": "UP",
      "details": {
        "redis": "Available",
        "ping": "PONG",
        "info": {
          "redis_version": "7.0.0",
          "used_memory": "1234567",
          "connected_clients": "5"
        }
      }
    }
  }
}
```

### 性能监控

```java
@Autowired
private RedisPerformanceMonitor performanceMonitor;

public void monitorPerformance() {
    Map<String, Object> metrics = performanceMonitor.getPerformanceMetrics();
    
    System.out.println("Total Operations: " + metrics.get("totalOperations"));
    System.out.println("Error Rate: " + metrics.get("errorRate"));
    System.out.println("Average Response Time: " + metrics.get("averageResponseTime"));
}
```

## 配置选项

### Redis连接配置

```yaml
spring:
  redis:
    # 单机模式
    host: localhost
    port: 6379
    password: your_password
    database: 0
    
    # 集群模式
    cluster:
      nodes:
        - 192.168.1.1:6379
        - 192.168.1.2:6379
        - 192.168.1.3:6379
    
    # 哨兵模式
    sentinel:
      master: mymaster
      nodes:
        - 192.168.1.1:26379
        - 192.168.1.2:26379
        - 192.168.1.3:26379
    
    # 连接池配置
    jedis:
      pool:
        max-active: 20
        max-idle: 10
        min-idle: 5
        max-wait: 3000ms
```

### 缓存配置

```yaml
spring:
  cache:
    type: redis
    redis:
      time-to-live: 30m
      key-prefix: "smart:cache:"
      use-key-prefix: true
      cache-null-values: false
```

## 最佳实践

### 1. 键命名规范

```java
// 使用有意义的键名
String userKey = "user:profile:" + userId;
String orderKey = "order:detail:" + orderId;
String cacheKey = "cache:data:" + dataId;
```

### 2. 过期时间设置

```java
// 根据业务需求设置合理的过期时间
redisService.set("session:" + sessionId, userInfo, 30, TimeUnit.MINUTES);
redisService.set("temp:data:" + tempId, data, 1, TimeUnit.HOURS);
```

### 3. 异常处理

```java
try {
    redisService.set(key, value);
} catch (RedisException e) {
    log.error("Redis operation failed: {}", e.getMessage(), e);
    // 降级处理
    fallbackOperation();
}
```

### 4. 性能优化

```java
// 使用批量操作
Map<String, Object> batchData = new HashMap<>();
batchData.put("key1", "value1");
batchData.put("key2", "value2");
redisService.batchSet(batchData);

// 使用管道操作
redisService.executePipelined(operations);
```

## 故障排除

### 常见问题

1. **连接超时**
   - 检查Redis服务是否启动
   - 检查网络连接
   - 调整连接超时配置

2. **内存不足**
   - 检查Redis内存使用情况
   - 设置合理的过期时间
   - 清理无用数据

3. **性能问题**
   - 使用批量操作
   - 优化键名长度
   - 使用连接池

### 日志配置

```yaml
logging:
  level:
    com.smart.common.redis: DEBUG
    org.springframework.data.redis: INFO
```

## 更新日志

### v1.1.0
- 简化分布式锁实现，移除复杂功能
- 优化锁获取逻辑，获取失败时立即返回
- 移除限流功能，专注核心Redis操作
- 改进异常处理和错误提示

### v1.0.0
- 初始版本发布
- 支持基础Redis操作
- 支持分布式锁
- 支持缓存管理
- 支持监控健康检查
