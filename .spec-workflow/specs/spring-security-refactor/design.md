# Spring Security 改造设计

## Architecture Overview
基于现有的temp config目录下的Spring Security配置，对smart-boot3项目进行Spring Security 6的现代化改造。主要目标是整合temp config中的优秀配置到现有的SecurityConfig中，删除temp config目录，并在README.md中记录改造内容。

## Technology Stack
- **Backend**: Spring Boot 3.x, Spring Security 6.x
- **Authentication**: JWT Token
- **Password Encoding**: BCrypt
- **Database**: MySQL (通过MyBatis Plus)
- **Infrastructure**: Nacos (服务发现和配置中心)

## Module Structure
```
smart-boot3/
├── smart-common/
│   └── smart-common-security/
│       ├── config/
│       │   └── SecurityConfig.java (现有，需要改造)
│       ├── filter/
│       │   └── JwtAuthenticationFilter.java (现有)
│       ├── handler/
│       │   ├── CustomAuthenticationEntryPoint.java (现有)
│       │   └── CustomAccessDeniedHandler.java (现有)
│       ├── temp_config/ (需要删除)
│       │   └── security/
│       │       ├── SpringSecurityConfig.java (参考配置)
│       │       ├── CustomerUserDetailsService.java (参考实现)
│       │       ├── handler/ (各种处理器)
│       │       └── login/ (登录相关)
│       └── util/
│           └── JwtUtil.java (现有)
└── smart-system/
    └── src/main/resources/
        └── application.yml (需要添加安全配置)
```

## API Design
- **认证端点**: `/api/auth/login` - 用户登录
- **登出端点**: `/api/auth/logout` - 用户登出
- **公开端点**: `/api/public/**`, `/swagger-ui/**`, `/actuator/**`
- **受保护端点**: `/api/system/**` - 需要相应权限
- **JWT验证**: 所有受保护端点都需要有效的JWT令牌

## Database Design
- **用户表**: 存储用户基本信息
- **角色表**: 存储角色信息
- **权限表**: 存储权限信息
- **用户角色关联表**: 用户和角色的多对多关系
- **角色权限关联表**: 角色和权限的多对多关系

## Security Design
- **认证策略**: JWT Token + 用户名密码认证
- **授权模型**: 基于角色的访问控制(RBAC)
- **密码加密**: BCrypt算法
- **数据保护**: HTTPS传输，JWT令牌过期机制

## Deployment Design
- **容器策略**: Docker容器化部署
- **环境配置**: 通过Nacos配置中心管理
- **扩展考虑**: 无状态设计，支持水平扩展

## Integration Points
- **外部服务**: Nacos服务发现和配置中心
- **内部服务通信**: 通过JWT令牌进行服务间认证
- **数据流**: 用户登录 → JWT生成 → API访问 → 权限验证

## Performance Considerations
- **缓存策略**: 用户权限信息缓存
- **数据库优化**: 权限查询优化
- **负载均衡**: 无状态设计支持负载均衡

## Monitoring and Logging
- **日志策略**: 安全事件日志记录
- **监控工具**: Spring Boot Actuator
- **告警机制**: 认证失败告警

## 改造要点

### 1. 整合temp config中的优秀配置
- 参考temp config中的SpringSecurityConfig.java
- 整合自定义的认证过滤器和处理器
- 保留现有的JWT认证机制

### 2. 用户权限查询功能
- 使用TODO标记待实现的功能
- 参考temp config中的CustomerUserDetailsService.java
- 实现基于数据库的权限查询

### 3. 配置优化
- 统一安全配置到application.yml
- 优化CORS配置
- 完善异常处理机制

### 4. 代码清理
- 删除temp config目录
- 更新README.md记录改造内容
- 确保代码质量和可维护性

