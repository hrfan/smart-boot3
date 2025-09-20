# Smart Boot3 微服务脚手架

## 项目简介

Smart Boot3 是一个基于 SpringBoot3 + SpringCloud + Spring AI 的现代化微服务脚手架项目，旨在为企业级应用开发提供快速、安全、可扩展的基础架构。

## 技术栈

- **JDK**: 17+
- **Spring Boot**: 3.2.0
- **Spring Cloud**: 2023.0.0
- **Spring AI**: 1.0.0-M3
- **Spring Security**: 6.0+
- **服务注册与发现**: Nacos 2.3.0
- **API网关**: SpringCloud Gateway 4.0+
- **数据库**: MySQL 8.0+ + Redis 7.0+
- **ORM框架**: MyBatis-Plus 3.5.4.1
- **消息队列**: RabbitMQ 3.12.0
- **容器化**: Docker + Docker Compose

## 项目结构

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
├── docker/                      # Docker配置
├── docs/                        # 项目文档
└── scripts/                     # 部署脚本
```

## 核心功能

- ✅ **微服务架构**: 基于SpringCloud的微服务架构
- ✅ **API网关**: 统一网关入口，支持路由转发、负载均衡
- ✅ **认证授权**: JWT Token认证，RBAC权限模型
- ✅ **服务注册发现**: 基于Nacos的服务注册与发现
- ✅ **配置管理**: 基于Nacos的配置中心
- ✅ **AI集成**: 集成Spring AI，支持AI对话和知识库问答
- ✅ **文件管理**: 支持文件上传、下载、预览
- ✅ **消息队列**: 基于RabbitMQ的消息处理
- ✅ **系统监控**: 基于Actuator的系统监控
- ✅ **容器化部署**: Docker + Docker Compose一键部署

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.9+
- MySQL 8.0+
- Redis 7.0+
- RabbitMQ 3.12+
- Nacos 2.3.0
- Docker & Docker Compose (可选)

### 本地开发

1. **克隆项目**
   ```bash
   git clone https://github.com/your-org/smart-boot3.git
   cd smart-boot3
   ```

2. **启动基础设施**
   ```bash
   # 启动Nacos
   docker run -d --name nacos -p 8848:8848 -p 9848:9848 nacos/nacos-server:v2.3.0
   
   # 启动MySQL
   docker run -d --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 mysql:8.0
   
   # 启动Redis
   docker run -d --name redis -p 6379:6379 redis:7.0
   
   # 启动RabbitMQ
   docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.12-management
   ```

3. **编译项目**
   ```bash
   mvn clean compile
   ```

4. **启动服务**
   ```bash
   # 启动网关
   cd smart-gateway
   mvn spring-boot:run
   
   # 启动认证服务
   cd smart-auth
   mvn spring-boot:run
   
   # 启动系统管理服务
   cd smart-system
   mvn spring-boot:run
   ```

### Docker部署

```bash
# 一键启动所有服务
./scripts/deploy.sh deploy
```

## 服务端口

| 服务 | 端口 | 描述 |
|------|------|------|
| smart-gateway | 8080 | API网关 |
| smart-auth | 8081 | 认证授权服务 |
| smart-system | 8082 | 系统管理服务 |
| smart-ai | 8083 | AI服务 |
| smart-file | 8084 | 文件服务 |
| smart-message | 8085 | 消息服务 |
| smart-monitor | 8086 | 监控服务 |

## API文档

启动服务后，访问以下地址查看API文档：

- 网关API文档: http://localhost:8080/swagger-ui.html
- 各服务API文档: http://localhost:808x/swagger-ui.html

## 配置说明

### Nacos配置

1. 访问 http://localhost:8848/nacos
2. 用户名/密码: nacos/nacos
3. 创建命名空间和配置

### 数据库配置

1. 创建数据库: `smart_boot3`
2. 执行SQL脚本: `docs/sql/init.sql`

## 开发指南

### 添加新服务

1. 在根目录创建新的模块目录
2. 在父POM中添加模块引用
3. 创建模块的POM文件
4. 实现服务功能

### 代码规范

- 使用Java 17+特性
- 遵循阿里巴巴Java开发手册
- 所有类和方法必须有注释
- 使用Lombok减少样板代码

## 贡献指南

1. Fork项目
2. 创建特性分支
3. 提交更改
4. 推送到分支
5. 创建Pull Request

## 许可证

本项目采用 Apache 2.0 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 联系方式

- 项目地址: https://github.com/your-org/smart-boot3
- 问题反馈: https://github.com/your-org/smart-boot3/issues
- 邮箱: your-email@example.com
