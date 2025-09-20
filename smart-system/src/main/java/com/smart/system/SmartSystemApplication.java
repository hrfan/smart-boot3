package com.smart.system;

import com.smart.common.core.result.Result;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.smart.common.security.util.JwtUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统管理服务启动类
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.smart"})
@RestController
public class SmartSystemApplication {

    /**
     * 主方法
     * 
     * @param args 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(SmartSystemApplication.class, args);
    }

    /**
     * 健康检查接口
     * 
     * @return 健康状态
     */
    @GetMapping("/health")
    public String health() {
        return "Smart System is running!";
    }

    /**
     * Hello World接口
     * 
     * @return Hello World消息
     */
    @GetMapping("/hello")
    public Result hello() {
//        int  u =  1/0;
        int i = 1/0;
        return Result.success("响应成功！",null);
    }
}
