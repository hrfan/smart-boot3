# 微服务脚手架技术设计文档

## 架构概述
基于SpringCloud微服务架构，采用前后端分离设计，支持容器化部署。整体架构包括网关层、服务层、数据层和基础设施层，确保高可用、高性能和易扩展。

## 技术栈
- **后端框架**: SpringBoot 3.2+, SpringCloud 2023.0+
- **AI集成**: Spring AI 1.0+
- **安全框架**: SpringSecurity 6.0+
- **服务注册与发现**: Nacos 2.3+
- **API网关**: SpringCloud Gateway 4.0+
- **数据库**: MySQL 8.0+, Redis 7.0+
- **消息队列**: RabbitMQ 3.12+
- **容器化**: Docker, Docker Compose
- **构建工具**: Maven 3.9+
- **JDK版本**: OpenJDK 17+

## 模块结构
```
smart-boot3/
├── smart-common/                 # 公共模块
│   ├── smart-common-core/        # 核心工具类
│   ├── smart-common-security/    # 安全组件
│   ├── smart-common-redis/       # Redis组件
│   ├── smart-common-database/    # 数据库组件
│   └── smart-common-ai/          # AI组件
├── smart-gateway/               # API网关
├── smart-auth/                  # 认证授权服务
├── smart-system/                # 系统管理服务
├── smart-ai/                    # AI服务
├── smart-file/                  # 文件服务
├── smart-message/               # 消息服务
├── smart-monitor/               # 监控服务
├── smart-admin/                 # 管理后台前端
├── docker/                      # Docker配置
├── docs/                        # 项目文档
└── scripts/                     # 部署脚本
```

## API设计
- **RESTful API**: 遵循REST设计原则
- **统一响应格式**: 
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {},
    "timestamp": "2025-01-27T10:00:00Z"
  }
  ```
- **认证方式**: JWT Token + SpringSecurity
- **API文档**: 集成Swagger/OpenAPI 3.0

## 数据库设计
- **主数据库**: MySQL 8.0+ (业务数据)
- **缓存数据库**: Redis 7.0+ (缓存、会话)
- **数据库连接池**: HikariCP
- **ORM框架**: MyBatis-Plus 3.5+
- **数据库迁移**: Flyway

### 核心表结构
```sql
-- 用户表
CREATE TABLE sys_user (
    id VARCHAR(32) PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    status TINYINT DEFAULT 1 COMMENT '状态',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by VARCHAR(32) COMMENT '创建人',
    create_user_name VARCHAR(50) COMMENT '创建人姓名',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    update_by VARCHAR(32) COMMENT '更新人',
    update_user_name VARCHAR(50) COMMENT '更新人姓名'
);

-- 角色表
CREATE TABLE sys_role (
    id VARCHAR(32) PRIMARY KEY COMMENT '角色ID',
    role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
    role_code VARCHAR(50) NOT NULL COMMENT '角色编码',
    description VARCHAR(255) COMMENT '描述',
    status TINYINT DEFAULT 1 COMMENT '状态',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by VARCHAR(32) COMMENT '创建人',
    create_user_name VARCHAR(50) COMMENT '创建人姓名',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    update_by VARCHAR(32) COMMENT '更新人',
    update_user_name VARCHAR(50) COMMENT '更新人姓名'
);
```

## 安全设计
- **认证策略**: JWT Token认证
- **授权模型**: RBAC (基于角色的访问控制)
- **密码加密**: BCrypt
- **接口安全**: SpringSecurity + 自定义过滤器
- **数据保护**: 敏感数据加密存储

## 部署设计
- **容器化**: Docker + Docker Compose
- **环境配置**: 
  - dev: 开发环境
  - test: 测试环境  
  - prod: 生产环境
- **扩缩容**: 支持水平扩展
- **负载均衡**: Nginx + SpringCloud Gateway

## 集成点
- **外部服务**: 
  - Nacos (服务注册发现、配置管理)
  - Redis (缓存、会话存储)
  - MySQL (数据持久化)
  - RabbitMQ (消息队列)
- **内部服务通信**: 
  - Feign (服务间调用)
  - SpringCloud Gateway (统一网关)
- **数据流**: 
  - 请求 → Gateway → 微服务 → 数据库
  - 异步消息 → RabbitMQ → 消费者服务

## 性能考虑
- **缓存策略**: 
  - Redis缓存热点数据
  - 本地缓存 (Caffeine)
- **数据库优化**: 
  - 读写分离
  - 连接池优化
  - 索引优化
- **负载均衡**: 
  - Gateway负载均衡
  - 服务实例负载均衡

## 监控和日志
- **日志策略**: 
  - SLF4J + Logback
  - 统一日志格式
  - 日志级别管理
- **监控工具**: 
  - SpringBoot Actuator
  - Micrometer + Prometheus
- **告警机制**: 
  - 健康检查
  - 性能指标监控
  - 异常告警
