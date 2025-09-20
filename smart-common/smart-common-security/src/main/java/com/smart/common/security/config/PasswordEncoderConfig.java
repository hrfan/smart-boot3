package com.smart.common.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码编码器配置类
 * 独立配置，避免循环依赖
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
@Configuration
public class PasswordEncoderConfig {

    /**
     * 密码编码器
     * 
     * @return BCrypt密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
