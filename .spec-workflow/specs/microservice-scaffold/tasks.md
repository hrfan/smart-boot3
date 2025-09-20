# 微服务脚手架任务分解文档

## 实现任务
将技术设计分解为原子化的、可实现的任务。

## 任务列表

- [x] Task 1: 创建项目根目录结构和Maven父POM
  - **文件**: pom.xml, README.md, .gitignore
  - **依赖**: 无
  - **验收标准**: 项目根目录创建完成，Maven父POM配置正确
  - **_Prompt**: Implement the task for spec microservice-scaffold, first run spec-workflow-guide to get the workflow guide then implement the task:
    - Role: Maven项目架构师
    - Task: 创建smart-boot3微服务脚手架项目的根目录结构和Maven父POM配置
    - Restrictions: 必须使用JDK17+，SpringBoot3.2+，SpringCloud2023.0+，所有模块使用smart-前缀
    - _Leverage: 参考JeecgBoot的Maven配置结构
    - _Requirements: 项目能够正常构建，支持多模块管理
    - Success: 项目根目录创建完成，父POM能够正确管理所有子模块

- [x] Task 2: 创建smart-common公共模块
  - **文件**: smart-common/pom.xml, smart-common-core/, smart-common-security/, smart-common-redis/, smart-common-database/, smart-common-ai/
  - **依赖**: Task 1
  - **验收标准**: 公共模块创建完成，包含核心工具类、安全组件、Redis组件、数据库组件、AI组件
  - **_Prompt**: Implement the task for spec microservice-scaffold, first run spec-workflow-guide to get the workflow guide then implement the task:
    - Role: SpringBoot公共组件开发工程师
    - Task: 创建smart-common公共模块，包含核心工具类、安全组件、Redis组件、数据库组件、AI组件
    - Restrictions: 必须使用SpringBoot3.2+，SpringSecurity6.0+，Spring AI1.0+，所有组件要有完整注释
    - _Leverage: 参考JeecgBoot的公共模块设计
    - _Requirements: 提供统一的工具类、安全配置、Redis操作、数据库操作、AI集成
    - Success: 公共模块能够被其他微服务模块正常引用和使用

- [-] Task 3: 创建smart-gateway网关模块
  - **文件**: smart-gateway/pom.xml, smart-gateway/src/main/java/, smart-gateway/src/main/resources/
  - **依赖**: Task 2
  - **验收标准**: 网关模块创建完成，支持路由转发、负载均衡、认证授权
  - **_Prompt**: Implement the task for spec microservice-scaffold, first run spec-workflow-guide to get the workflow guide then implement the task:
    - Role: SpringCloud Gateway开发工程师
    - Task: 创建smart-gateway网关模块，实现API网关功能
    - Restrictions: 必须使用SpringCloud Gateway4.0+，支持JWT认证，集成SpringSecurity
    - _Leverage: 使用smart-common-security安全组件
    - _Requirements: 实现统一网关入口，支持路由转发、负载均衡、认证授权、限流熔断
    - Success: 网关能够正常启动，能够转发请求到后端微服务

- [ ] Task 4: 创建smart-auth认证授权服务
  - **文件**: smart-auth/pom.xml, smart-auth/src/main/java/, smart-auth/src/main/resources/
  - **依赖**: Task 2
  - **验收标准**: 认证服务创建完成，支持用户登录、JWT Token生成、权限验证
  - **_Prompt**: Implement the task for spec microservice-scaffold, first run spec-workflow-guide to get the workflow guide then implement the task:
    - Role: SpringSecurity认证授权开发工程师
    - Task: 创建smart-auth认证授权服务，实现用户认证和权限管理
    - Restrictions: 必须使用SpringSecurity6.0+，JWT Token认证，RBAC权限模型
    - _Leverage: 使用smart-common-security和smart-common-database组件
    - _Requirements: 实现用户登录、JWT Token生成、权限验证、角色管理
    - Success: 认证服务能够正常启动，支持用户登录和权限验证

- [ ] Task 5: 创建smart-system系统管理服务
  - **文件**: smart-system/pom.xml, smart-system/src/main/java/, smart-system/src/main/resources/
  - **依赖**: Task 2
  - **验收标准**: 系统管理服务创建完成，支持用户管理、角色管理、菜单管理
  - **_Prompt**: Implement the task for spec microservice-scaffold, first run spec-workflow-guide to get the workflow guide then implement the task:
    - Role: 系统管理服务开发工程师
    - Task: 创建smart-system系统管理服务，实现系统管理功能
    - Restrictions: 必须使用MyBatis-Plus3.5+，支持CRUD操作，所有字段要有注释
    - _Leverage: 使用smart-common-database和smart-common-security组件
    - _Requirements: 实现用户管理、角色管理、菜单管理、部门管理等系统管理功能
    - Success: 系统管理服务能够正常启动，提供完整的系统管理API

- [ ] Task 6: 创建smart-ai AI服务
  - **文件**: smart-ai/pom.xml, smart-ai/src/main/java/, smart-ai/src/main/resources/
  - **依赖**: Task 2
  - **验收标准**: AI服务创建完成，集成Spring AI，支持AI对话、知识库问答
  - **_Prompt**: Implement the task for spec microservice-scaffold, first run spec-workflow-guide to get the workflow guide then implement the task:
    - Role: Spring AI集成开发工程师
    - Task: 创建smart-ai AI服务，集成Spring AI功能
    - Restrictions: 必须使用Spring AI1.0+，支持多种AI模型，提供AI对话和知识库问答
    - _Leverage: 使用smart-common-ai组件
    - _Requirements: 实现AI对话、知识库问答、AI模型管理等功能
    - Success: AI服务能够正常启动，能够与AI模型进行交互

- [ ] Task 7: 创建smart-file文件服务
  - **文件**: smart-file/pom.xml, smart-file/src/main/java/, smart-file/src/main/resources/
  - **依赖**: Task 2
  - **验收标准**: 文件服务创建完成，支持文件上传、下载、管理
  - **_Prompt**: Implement the task for spec microservice-scaffold, first run spec-workflow-guide to get the workflow guide then implement the task:
    - Role: 文件服务开发工程师
    - Task: 创建smart-file文件服务，实现文件管理功能
    - Restrictions: 必须支持多种文件类型，提供文件上传、下载、删除、预览功能
    - _Leverage: 使用smart-common-core工具类
    - _Requirements: 实现文件上传、下载、管理、预览等功能
    - Success: 文件服务能够正常启动，支持文件的各种操作

- [ ] Task 8: 创建smart-message消息服务
  - **文件**: smart-message/pom.xml, smart-message/src/main/java/, smart-message/src/main/resources/
  - **依赖**: Task 2
  - **验收标准**: 消息服务创建完成，支持消息发送、接收、管理
  - **_Prompt**: Implement the task for spec microservice-scaffold, first run spec-workflow-guide to get the workflow guide then implement the task:
    - Role: 消息服务开发工程师
    - Task: 创建smart-message消息服务，实现消息管理功能
    - Restrictions: 必须使用RabbitMQ3.12+，支持消息队列、消息推送
    - _Leverage: 使用smart-common-core工具类
    - _Requirements: 实现消息发送、接收、管理、推送等功能
    - Success: 消息服务能够正常启动，支持消息的各种操作

- [ ] Task 9: 创建smart-monitor监控服务
  - **文件**: smart-monitor/pom.xml, smart-monitor/src/main/java/, smart-monitor/src/main/resources/
  - **依赖**: Task 2
  - **验收标准**: 监控服务创建完成，支持系统监控、性能监控、日志监控
  - **_Prompt**: Implement the task for spec microservice-scaffold, first run spec-workflow-guide to get the workflow guide then implement the task:
    - Role: 监控服务开发工程师
    - Task: 创建smart-monitor监控服务，实现系统监控功能
    - Restrictions: 必须使用SpringBoot Actuator，Micrometer，支持健康检查、性能监控
    - _Leverage: 使用smart-common-core工具类
    - _Requirements: 实现系统监控、性能监控、日志监控、告警等功能
    - Success: 监控服务能够正常启动，提供完整的监控功能

- [ ] Task 10: 创建Docker配置和部署脚本
  - **文件**: docker/docker-compose.yml, docker/Dockerfile.*, scripts/deploy.sh
  - **依赖**: Task 1-9
  - **验收标准**: Docker配置创建完成，支持一键部署
  - **_Prompt**: Implement the task for spec microservice-scaffold, first run spec-workflow-guide to get the workflow guide then implement the task:
    - Role: DevOps工程师
    - Task: 创建Docker配置和部署脚本，实现容器化部署
    - Restrictions: 必须使用Docker和Docker Compose，支持一键部署
    - _Leverage: 使用所有已创建的微服务模块
    - _Requirements: 实现容器化部署，支持开发、测试、生产环境
    - Success: 能够使用Docker Compose一键启动所有服务

- [ ] Task 11: 创建项目文档和启动指南
  - **文件**: docs/README.md, docs/API.md, docs/DEPLOYMENT.md
  - **依赖**: Task 1-10
  - **验收标准**: 项目文档创建完成，包含完整的启动指南
  - **_Prompt**: Implement the task for spec microservice-scaffold, first run spec-workflow-guide to get the workflow guide then implement the task:
    - Role: 技术文档工程师
    - Task: 创建项目文档和启动指南，提供完整的使用说明
    - Restrictions: 必须提供中文文档，包含安装、配置、启动、使用说明
    - _Leverage: 使用所有已创建的功能模块
    - _Requirements: 提供完整的项目文档，包括API文档、部署文档、使用指南
    - Success: 文档完整，用户能够根据文档快速启动和使用项目

## 实现顺序
任务按照依赖关系排序，基础任务优先执行：
1. 项目根目录结构 (Task 1)
2. 公共模块 (Task 2)
3. 核心服务模块 (Task 3-9，可并行开发)
4. 部署配置 (Task 10)
5. 项目文档 (Task 11)
