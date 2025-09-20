package com.smart.common.security.config;

import com.smart.common.security.filter.CustomUsernamePasswordAuthenticationFilter;
import com.smart.common.security.filter.CustomUsernamePasswordAuthenticationProvider;
import com.smart.common.security.filter.JwtAuthenticationFilter;
import com.smart.common.security.handler.CustomAccessDeniedHandler;
import com.smart.common.security.handler.CustomAuthenticationEntryPoint;
import com.smart.common.security.handler.CustomAuthenticationFailureHandler;
import com.smart.common.security.handler.CustomAuthenticationSuccessHandler;
import com.smart.common.security.handler.CustomLogoutSuccessHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Spring Security 6 配置类
 * 整合temp config中的优秀配置，使用现代配置方式和最佳实践
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler authenticationFailureHandler;
    private final CustomLogoutSuccessHandler logoutSuccessHandler;
    private final CustomUsernamePasswordAuthenticationProvider customAuthenticationProvider;

    /**
     * 从配置文件读取不需要过滤的URL列表
     */
    @Value("${security.login.no-filter}")
    private String noFilterUrls;

    /**
     * 构造函数
     */
    public SecurityConfig(UserDetailsService userDetailsService,
                         JwtAuthenticationFilter jwtAuthenticationFilter,
                         CustomAuthenticationEntryPoint authenticationEntryPoint,
                         CustomAccessDeniedHandler accessDeniedHandler,
                         CustomAuthenticationSuccessHandler authenticationSuccessHandler,
                         CustomAuthenticationFailureHandler authenticationFailureHandler,
                         CustomLogoutSuccessHandler logoutSuccessHandler,
                         CustomUsernamePasswordAuthenticationProvider customAuthenticationProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.customAuthenticationProvider = customAuthenticationProvider;
    }

    /**
     * 认证提供者
     * 
     * @return DaoAuthenticationProvider
     */
    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    /**
     * 认证管理器
     * 
     * @param config 认证配置
     * @return 认证管理器
     * @throws Exception 异常
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * 自定义用户名密码认证过滤器
     * 
     * @param authenticationManager 认证管理器
     * @return 认证过滤器
     */
    @Bean
    public CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        CustomUsernamePasswordAuthenticationFilter filter = new CustomUsernamePasswordAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(authenticationFailureHandler);
        filter.setPostOnly(true);
        return filter;
    }

    /**
     * CORS配置
     * 
     * @return CORS配置源
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * 解析不需要过滤的URL配置
     * 
     * @return URL数组
     */
    private String[] parseNoFilterUrls() {
        log.info("开始解析no-filter配置，原始配置值: '{}'", noFilterUrls);
        
        if (noFilterUrls == null || noFilterUrls.trim().isEmpty()) {
            log.warn("security.login.no-filter配置为空，使用默认配置");
            return new String[]{
                "/health",
                "/hello",
                "/actuator/**",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/api/public/**"
            };
        }
        
        // 按逗号分割并去除空格
        String[] urls = noFilterUrls.split(",");
        List<String> cleanUrls = Arrays.stream(urls)
            .map(String::trim)
            .filter(url -> !url.isEmpty())
            .collect(Collectors.toList());
        
        log.info("从配置文件读取到{}个不需要过滤的URL: {}", cleanUrls.size(), cleanUrls);
        return cleanUrls.toArray(new String[0]);
    }

    /**
     * 安全过滤器链配置
     * 整合temp config中的优秀配置
     * 
     * @param http HttpSecurity
     * @param customFilter 自定义认证过滤器
     * @return SecurityFilterChain
     * @throws Exception 异常
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomUsernamePasswordAuthenticationFilter customFilter, PasswordEncoder passwordEncoder) throws Exception {
        log.info("配置Spring Security 6过滤器链，整合temp config优秀配置");
        
        // 解析不需要过滤的URL
        String[] noFilterUrlArray = parseNoFilterUrls();
        log.info("从配置文件读取到{}个不需要过滤的URL: {}", noFilterUrlArray.length, noFilterUrlArray);
        log.info("原始配置值: '{}'", noFilterUrls);
        http
            // 禁用CSRF，因为使用JWT
            .csrf(AbstractHttpConfigurer::disable)
            
            // 配置CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // 配置会话管理为无状态
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 配置认证提供者
            .authenticationProvider(authenticationProvider(passwordEncoder))
            .authenticationProvider(customAuthenticationProvider)
            
            // 配置异常处理
            .exceptionHandling(exception -> {
                log.info("配置异常处理器 - AuthenticationEntryPoint: {}, AccessDeniedHandler: {}", 
                    authenticationEntryPoint.getClass().getSimpleName(),
                    accessDeniedHandler.getClass().getSimpleName());
                exception
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler);
            })
            
            // 配置表单登录
            .formLogin(form -> form
                .loginPage("/api/auth/login")
                .loginProcessingUrl("/api/auth/login")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .permitAll()
            )
            
            // 配置登出
            .logout(logout -> logout
                .logoutUrl("/api/auth/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
            )
            
            // 配置授权规则
            .authorizeHttpRequests(auth -> auth
                // 健康检查端点 - 明确指定
                .requestMatchers("/health", "/health/**").permitAll()
                .requestMatchers("/system/hello", "/hello/**").permitAll()
                .requestMatchers("/hello", "/hello/**").permitAll()
                .requestMatchers("/api/hello", "/api/hello/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                
                // 认证相关端点 - 必须公开
                .requestMatchers("/api/auth/login").permitAll()
                .requestMatchers("/api/auth/logout").permitAll()
                .requestMatchers("/api/auth/captcha").permitAll()
                .requestMatchers("/api/auth/refresh").permitAll()
                
                // 公开端点 - 从配置文件读取
                .requestMatchers(noFilterUrlArray).permitAll()
                
                // 系统管理端点需要相应权限
                .requestMatchers("/api/system/users/**").hasAnyAuthority(
                    "system:user:list", "system:user:query", "system:user:add", 
                    "system:user:edit", "system:user:remove"
                )
                .requestMatchers("/api/system/roles/**").hasAnyAuthority(
                    "system:role:list", "system:role:query", "system:role:add", 
                    "system:role:edit", "system:role:remove"
                )
                .requestMatchers("/api/system/permissions/**").hasAnyAuthority(
                    "system:permission:list", "system:permission:query", "system:permission:add", 
                    "system:permission:edit", "system:permission:remove"
                )
                
                // 其他请求需要认证
                .anyRequest().authenticated()
            )
            
            // 添加过滤器
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
