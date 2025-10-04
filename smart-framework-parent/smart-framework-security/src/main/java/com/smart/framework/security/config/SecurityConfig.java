package com.smart.framework.security.config;

import com.smart.framework.security.filter.CustomUsernamePasswordAuthenticationFilter;
import com.smart.framework.security.filter.CustomUsernamePasswordAuthenticationProvider;
import com.smart.framework.security.filter.JwtAuthenticationFilter;
import com.smart.framework.security.handler.CustomAccessDeniedHandler;
import com.smart.framework.security.handler.CustomAuthenticationEntryPoint;
import com.smart.framework.security.handler.CustomAuthenticationFailureHandler;
import com.smart.framework.security.handler.CustomAuthenticationSuccessHandler;
import com.smart.framework.security.handler.CustomLogoutSuccessHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;

/**
 * Spring Security 配置类
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Value("${login.url:/system/auth/login}")
    private String loginUrl;

    @Value("${login.logout-url:/system/auth/logout}")
    private String logoutUrl;

    @Value("${login.no-filter:/actuator/**,/system/captcha/**,/system/auth/**}")
    private String noFilterUrls;

    private final UserDetailsService userDetailsService;
    private final CustomUsernamePasswordAuthenticationProvider customAuthenticationProvider;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler authenticationFailureHandler;
    private final CustomLogoutSuccessHandler logoutSuccessHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(UserDetailsService userDetailsService,
                         CustomUsernamePasswordAuthenticationProvider customAuthenticationProvider,
                         CustomAuthenticationEntryPoint authenticationEntryPoint,
                         CustomAccessDeniedHandler accessDeniedHandler,
                         CustomAuthenticationSuccessHandler authenticationSuccessHandler,
                         CustomAuthenticationFailureHandler authenticationFailureHandler,
                         CustomLogoutSuccessHandler logoutSuccessHandler,
                         JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.customAuthenticationProvider = customAuthenticationProvider;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


    /**
     * 配置认证提供者
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, PasswordEncoder passwordEncoder) throws Exception {
        // 为自定义认证提供者设置密码编码器
        customAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        // 添加认证提供者
        auth.authenticationProvider(customAuthenticationProvider);
    }

    /**
     * 配置认证管理器
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 配置认证过滤器
     */
    @Bean
    public CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        CustomUsernamePasswordAuthenticationFilter filter = new CustomUsernamePasswordAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(authenticationFailureHandler);
        // 设置登录URL, 只允许POST方法
        filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(loginUrl, "POST"));
        filter.setPostOnly(true);
        return filter;
    }

    /**
     * 配置安全过滤链
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, 
                                                  CustomUsernamePasswordAuthenticationFilter customFilter,
                                                  AuthenticationManager authenticationManager) throws Exception {
        
        // 添加JWT过滤器
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        // 添加自定义认证过滤器
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
        
        // 禁用表单登录
        http.formLogin(AbstractHttpConfigurer::disable);
        
        // 设置认证管理器
        http.authenticationManager(authenticationManager);
        
        // 配置登出
        http.logout(logout -> logout
                .logoutUrl(logoutUrl)
                .logoutSuccessHandler(logoutSuccessHandler)
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
        );
        
        // 配置会话管理为无状态
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        
        // 配置授权规则
        http.authorizeHttpRequests(auth -> {
            // 解析不需要过滤的URL
            String[] noFilterArray = noFilterUrls.split(",");
            for (String pattern : noFilterArray) {
                auth.requestMatchers(pattern.trim()).permitAll();
            }
            
            // 登录和登出接口
            auth.requestMatchers(loginUrl).permitAll()
                .requestMatchers(logoutUrl).permitAll()
                
                // 其他请求需要认证
                .anyRequest().authenticated();
        });
        
        // 配置异常处理
        http.exceptionHandling(exception -> exception
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
        );
        
        // 禁用CSRF
        http.csrf(AbstractHttpConfigurer::disable);
        
        // 配置CORS
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        
        // 配置安全头
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));
        
        return http.build();
    }

    /**
     * CORS配置
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}