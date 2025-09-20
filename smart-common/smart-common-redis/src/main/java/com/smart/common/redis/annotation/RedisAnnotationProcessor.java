package com.smart.common.redis.annotation;

import com.smart.common.redis.service.DistributedLockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Redis注解处理器
 * 处理分布式锁注解的AOP切面
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Aspect
@Component
public class RedisAnnotationProcessor {

    private static final Logger log = LoggerFactory.getLogger(RedisAnnotationProcessor.class);

    @Autowired
    private DistributedLockService distributedLockService;

    /**
     * SpEL表达式解析器
     */
    private final ExpressionParser parser = new SpelExpressionParser();

    /**
     * 参数名发现器
     */
    private final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    /**
     * 处理分布式锁注解
     * 
     * @param joinPoint 连接点
     * @param distributedLock 分布式锁注解
     * @return 方法执行结果
     * @throws Throwable 异常
     */
    @Around("@annotation(distributedLock)")
    public Object processDistributedLock(ProceedingJoinPoint joinPoint, com.smart.common.redis.annotation.DistributedLock distributedLock) throws Throwable {
        String lockKey = parseSpEL(distributedLock.key(), joinPoint);
        
        try {
            com.smart.common.redis.service.DistributedLock lock = distributedLockService.acquireLock(
                lockKey, 
                distributedLock.timeout(), 
                distributedLock.unit()
            );
            
            // 检查锁是否获取成功
            if (lock == null || !lock.isValid()) {
                log.warn("Failed to acquire distributed lock: key={}", lockKey);
                
                switch (distributedLock.failStrategy()) {
                    case THROW_EXCEPTION:
                        throw new RuntimeException("Failed to acquire distributed lock: " + lockKey);
                    case RETURN_NULL:
                        return null;
                    case RETURN_FALSE:
                        return false;
                    case WAIT_RETRY:
                        // 简单重试机制，实际项目中可以使用更复杂的重试策略
                        Thread.sleep(100);
                        return processDistributedLock(joinPoint, distributedLock);
                    default:
                        throw new RuntimeException("Failed to acquire distributed lock: " + lockKey);
                }
            }
            
            log.debug("Distributed lock acquired: key={}", lockKey);
            
            try {
                return joinPoint.proceed();
            } finally {
                boolean unlocked = lock.unlock();
                log.debug("Distributed lock released: key={}, success={}", lockKey, unlocked);
            }
        } catch (Exception e) {
            log.error("Distributed lock operation failed: key={}", lockKey, e);
            
            switch (distributedLock.failStrategy()) {
                case THROW_EXCEPTION:
                    throw new RuntimeException("Failed to acquire distributed lock: " + lockKey, e);
                case RETURN_NULL:
                    return null;
                case RETURN_FALSE:
                    return false;
                case WAIT_RETRY:
                    // 简单重试机制，实际项目中可以使用更复杂的重试策略
                    Thread.sleep(100);
                    return processDistributedLock(joinPoint, distributedLock);
                default:
                    throw new RuntimeException("Failed to acquire distributed lock: " + lockKey, e);
            }
        }
    }

    /**
     * 解析SpEL表达式
     * 
     * @param spEL SpEL表达式
     * @param joinPoint 连接点
     * @return 解析结果
     */
    private String parseSpEL(String spEL, ProceedingJoinPoint joinPoint) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            Object[] args = joinPoint.getArgs();
            String[] paramNames = nameDiscoverer.getParameterNames(method);
            
            EvaluationContext context = new StandardEvaluationContext();
            if (paramNames != null) {
                for (int i = 0; i < paramNames.length; i++) {
                    context.setVariable(paramNames[i], args[i]);
                }
            }
            
            Expression expression = parser.parseExpression(spEL);
            Object value = expression.getValue(context);
            return value != null ? value.toString() : spEL;
        } catch (Exception e) {
            log.warn("Failed to parse SpEL expression: {}, using original value", spEL, e);
            return spEL;
        }
    }
}
