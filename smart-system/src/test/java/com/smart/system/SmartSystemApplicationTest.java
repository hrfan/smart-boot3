package com.smart.system;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * 系统管理服务测试
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@SpringBootTest
@TestPropertySource(properties = {
    "server.port=0",  // 使用随机端口
    "spring.cloud.nacos.discovery.enabled=false",  // 测试时禁用服务发现
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration"  // 禁用数据库自动配置
})
class SmartSystemApplicationTest {

    /**
     * 测试应用上下文加载
     */
    @Test
    void contextLoads() {
        // 测试Spring上下文是否能正常加载
        System.out.println("Smart System应用测试通过！");
    }
}
