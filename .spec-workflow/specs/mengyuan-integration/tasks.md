# Mengyuan Smart Boot 功能集成任务分解文档

## 实现任务
将设计分解为原子化的、可实现的任务。

### 任务格式
每个任务包含：
- **描述**: 需要实现的内容
- **文件**: 需要创建/修改的具体文件
- **依赖**: 必须先完成的其他任务
- **验收标准**: 如何验证完成
- **_Prompt**: 详细的实现指导

## 任务列表

- [x] Task 1: 创建 smart-common-dto 模块
  - **文件**: 
    - `smart-common/smart-common-dto/pom.xml`
    - `smart-common/smart-common-dto/src/main/java/com/smart/common/dto/`
  - **依赖**: 无
  - **验收标准**: 
    - DTO模块能够独立编译
    - 包含基础的DTO类和工具类
    - 集成到父POM中
  - **_Prompt**: 
    ```
    Implement the task for spec mengyuan-integration, first run spec-workflow-guide to get the workflow guide then implement the task:
    
    Role: Maven模块架构师
    Task: 创建smart-common-dto模块，用于存放所有数据传输对象
    Restrictions: 不要修改现有代码，只创建新模块
    Leverage: 参考现有的smart-common子模块结构
    Requirements: 实现DTO模块的基础架构
    Success: 模块能够独立编译，包含在父POM中
    
    Instructions: 在tasks.md中将此任务标记为进行中[-]，完成后标记为完成[x]
    ```

- [x] Task 2: 升级依赖版本和配置
  - **文件**: 
    - `pom.xml`
    - `smart-common/smart-common-security/pom.xml`
    - `smart-system/pom.xml`
  - **依赖**: Task 1
  - **验收标准**: 
    - Spring Security 升级到 6.0+
    - 添加 MapStruct 依赖
    - 移除 springdoc 相关依赖
    - 项目能够正常编译
  - **_Prompt**: 
    ```
    Implement the task for spec mengyuan-integration, first run spec-workflow-guide to get the workflow guide then implement the task:
    
    Role: 依赖管理专家
    Task: 升级项目依赖到Spring Boot 3兼容版本
    Restrictions: 不要破坏现有功能，保持向后兼容
    Leverage: 现有的pom.xml结构和依赖管理
    Requirements: 升级Spring Security到6.0+，添加MapStruct，移除springdoc
    Success: 项目能够正常编译，所有依赖版本兼容
    
    Instructions: 在tasks.md中将此任务标记为进行中[-]，完成后标记为完成[x]
    ```

- [x] Task 3: 创建数据库实体类
  - **文件**: 
    - `smart-system/src/main/java/com/smart/system/entity/SysUser.java`
    - `smart-system/src/main/java/com/smart/system/entity/SysRole.java`
    - `smart-system/src/main/java/com/smart/system/entity/SysPermission.java`
    - `smart-system/src/main/java/com/smart/system/entity/SysFile.java`
    - `smart-system/src/main/java/com/smart/system/entity/SysMessage.java`
  - **依赖**: Task 2
  - **验收标准**: 
    - 所有实体类包含标准字段（id, create_time, create_by等）
    - 使用UUID作为主键
    - 时间字段使用timestamp类型
    - 所有字段包含中文注释
  - **_Prompt**: 
    ```
    Implement the task for spec mengyuan-integration, first run spec-workflow-guide to get the workflow guide then implement the task:
    
    Role: 数据库设计专家
    Task: 创建系统核心实体类，遵循标准字段规范
    Restrictions: 必须使用UUID主键，timestamp时间类型，包含审计字段
    Leverage: MyBatis-Plus注解和现有实体类结构
    Requirements: 创建用户、角色、权限、文件、消息等核心实体
    Success: 实体类符合规范，能够正常映射到数据库
    
    Instructions: 在tasks.md中将此任务标记为进行中[-]，完成后标记为完成[x]
    ```

- [x] Task 4: 创建DTO类
  - **文件**: 
    - `smart-common/smart-common-dto/src/main/java/com/smart/common/dto/user/UserDTO.java`
    - `smart-common/smart-common-dto/src/main/java/com/smart/common/dto/user/UserParams.java`
    - `smart-common/smart-common-dto/src/main/java/com/smart/common/dto/role/RoleDTO.java`
    - `smart-common/smart-common-dto/src/main/java/com/smart/common/dto/role/RoleParams.java`
    - `smart-common/smart-common-dto/src/main/java/com/smart/common/dto/permission/PermissionDTO.java`
  - **依赖**: Task 1, Task 3
  - **验收标准**: 
    - DTO类包含完整的字段注释
    - Params类用于接收前端参数
    - DTO类用于返回数据给前端
    - 使用Lombok简化代码
  - **_Prompt**: 
    ```
    Implement the task for spec mengyuan-integration, first run spec-workflow-guide to get the workflow guide then implement the task:
    
    Role: DTO设计专家
    Task: 创建数据传输对象类，实现参数和返回值的分离
    Restrictions: 必须包含完整注释，使用Lombok，遵循命名规范
    Leverage: 现有的DTO模块结构和实体类
    Requirements: 创建用户、角色、权限相关的DTO和Params类
    Success: DTO类结构清晰，注释完整，能够正常使用
    
    Instructions: 在tasks.md中将此任务标记为进行中[-]，完成后标记为完成[x]
    ```

- [x] Task 5: 创建MapStruct映射器
  - **文件**: 
    - `smart-system/src/main/java/com/smart/system/mapper/UserMapper.java`
    - `smart-system/src/main/java/com/smart/system/mapper/RoleMapper.java`
    - `smart-system/src/main/java/com/smart/system/mapper/PermissionMapper.java`
  - **依赖**: Task 4
  - **验收标准**: 
    - MapStruct映射器能够正常编译
    - 实现Params到Entity的转换
    - 实现Entity到DTO的转换
    - 包含自定义映射方法
  - **_Prompt**: 
    ```
    Implement the task for spec mengyuan-integration, first run spec-workflow-guide to get the workflow guide then implement the task:
    
    Role: MapStruct映射专家
    Task: 创建对象映射器，实现Params->Entity->DTO的转换
    Restrictions: 必须使用MapStruct注解，处理复杂字段映射
    Leverage: MapStruct框架和现有的DTO/Entity类
    Requirements: 创建用户、角色、权限的映射器
    Success: 映射器能够正常编译，转换逻辑正确
    
    Instructions: 在tasks.md中将此任务标记为进行中[-]，完成后标记为完成[x]
    ```

- [x] Task 6: 创建DAO层
  - **文件**: 
    - `smart-system/src/main/java/com/smart/system/dao/SysUserDao.java`
    - `smart-system/src/main/java/com/smart/system/dao/SysRoleDao.java`
    - `smart-system/src/main/java/com/smart/system/dao/SysPermissionDao.java`
  - **依赖**: Task 3
  - **验收标准**: 
    - DAO接口继承MyBatis-Plus的BaseMapper
    - 包含自定义查询方法
    - 方法注释完整
    - 支持分页查询
  - **_Prompt**: 
    ```
    Implement the task for spec mengyuan-integration, first run spec-workflow-guide to get the workflow guide then implement the task:
    
    Role: 数据访问层专家
    Task: 创建DAO接口，提供数据访问方法
    Restrictions: 必须继承BaseMapper，包含完整注释
    Leverage: MyBatis-Plus框架和实体类
    Requirements: 创建用户、角色、权限的DAO接口
    Success: DAO接口方法完整，注释清晰，支持复杂查询
    
    Instructions: 在tasks.md中将此任务标记为进行中[-]，完成后标记为完成[x]
    ```

- [x] Task 7: 创建Service层
  - **文件**: 
    - `smart-system/src/main/java/com/smart/system/service/UserService.java`
    - `smart-system/src/main/java/com/smart/system/service/impl/UserServiceImpl.java`
    - `smart-system/src/main/java/com/smart/system/service/RoleService.java`
    - `smart-system/src/main/java/com/smart/system/service/impl/RoleServiceImpl.java`
  - **依赖**: Task 5, Task 6
  - **验收标准**: 
    - Service接口定义业务方法
    - ServiceImpl实现业务逻辑
    - 使用MapStruct进行对象转换
    - 包含完整的异常处理
  - **_Prompt**: 
    ```
    Implement the task for spec mengyuan-integration, first run spec-workflow-guide to get the workflow guide then implement the task:
    
    Role: 业务逻辑专家
    Task: 创建Service层，实现业务逻辑处理
    Restrictions: 必须使用MapStruct转换，包含异常处理
    Leverage: DAO层和MapStruct映射器
    Requirements: 实现用户和角色的业务逻辑
    Success: Service层逻辑完整，异常处理完善
    
    Instructions: 在tasks.md中将此任务标记为进行中[-]，完成后标记为完成[x]
    ```

- [x] Task 8: 创建Controller层
  - **文件**: 
    - `smart-system/src/main/java/com/smart/system/controller/UserController.java`
    - `smart-system/src/main/java/com/smart/system/controller/RoleController.java`
    - `smart-system/src/main/java/com/smart/system/controller/PermissionController.java`
  - **依赖**: Task 7
  - **验收标准**: 
    - Controller接收Params参数
    - 返回统一的响应格式
    - 包含完整的API注释
    - 支持RESTful API设计
  - **_Prompt**: 
    ```
    Implement the task for spec mengyuan-integration, first run spec-workflow-guide to get the workflow guide then implement the task:
    
    Role: API设计专家
    Task: 创建Controller层，提供RESTful API接口
    Restrictions: 必须使用Params接收参数，返回统一响应格式
    Leverage: Service层和统一响应工具类
    Requirements: 创建用户、角色、权限的API接口
    Success: API接口完整，响应格式统一，注释清晰
    
    Instructions: 在tasks.md中将此任务标记为进行中[-]，完成后标记为完成[x]
    ```

- [x] Task 9: 升级Spring Security配置
  - **文件**: 
    - `smart-common/smart-common-security/src/main/java/com/smart/common/security/config/SecurityConfig.java`
    - `smart-common/smart-common-security/src/main/java/com/smart/common/security/util/JwtUtil.java`
  - **依赖**: Task 2
  - **验收标准**: 
    - Spring Security 6配置正确
    - JWT工具类兼容新版本
    - 安全配置能够正常工作
    - 支持JWT认证和授权
  - **_Prompt**: 
    ```
    Implement the task for spec mengyuan-integration, first run spec-workflow-guide to get the workflow guide then implement the task:
    
    Role: 安全配置专家
    Task: 升级Spring Security到6.0+版本，更新配置和工具类
    Restrictions: 必须兼容Spring Boot 3，使用新的配置方式
    Leverage: 现有的安全配置和JWT工具类
    Requirements: 升级安全配置，更新JWT工具类
    Success: 安全配置正常工作，JWT认证功能完整
    
    Instructions: 在tasks.md中将此任务标记为进行中[-]，完成后标记为完成[x]
    ```

- [x] Task 10: 创建数据库初始化脚本
  - **文件**: 
    - `smart-system/src/main/resources/sql/schema.sql`
    - `smart-system/src/main/resources/sql/data.sql`
  - **依赖**: Task 3
  - **验收标准**: 
    - 数据库表结构完整
    - 包含所有标准字段
    - 字段注释完整
    - 初始数据正确
  - **_Prompt**: 
    ```
    Implement the task for spec mengyuan-integration, first run spec-workflow-guide to get the workflow guide then implement the task:
    
    Role: 数据库专家
    Task: 创建数据库初始化脚本，包含表结构和初始数据
    Restrictions: 必须使用UUID主键，timestamp时间类型，包含审计字段
    Leverage: 实体类设计和标准字段规范
    Requirements: 创建完整的数据库初始化脚本
    Success: 数据库脚本完整，字段注释清晰，能够正常执行
    
    Instructions: 在tasks.md中将此任务标记为进行中[-]，完成后标记为完成[x]
    ```

- [x] Task 11: 创建文件管理模块
  - **文件**: 
    - `smart-file/src/main/java/com/smart/file/controller/FileController.java`
    - `smart-file/src/main/java/com/smart/file/service/FileService.java`
    - `smart-file/src/main/java/com/smart/file/service/impl/FileServiceImpl.java`
  - **依赖**: Task 8
  - **验收标准**: 
    - 支持文件上传、下载、预览
    - 文件存储路径可配置
    - 包含文件类型验证
    - 支持文件删除和查询
  - **_Prompt**: 
    ```
    Implement the task for spec mengyuan-integration, first run spec-workflow-guide to get the workflow guide then implement the task:
    
    Role: 文件管理专家
    Task: 创建文件管理模块，提供文件上传下载功能
    Restrictions: 必须包含文件类型验证，路径可配置
    Leverage: Spring Boot文件处理功能和现有架构
    Requirements: 实现完整的文件管理功能
    Success: 文件管理功能完整，安全可靠
    
    Instructions: 在tasks.md中将此任务标记为进行中[-]，完成后标记为完成[x]
    ```

- [x] Task 12: 创建消息管理模块
  - **文件**: 
    - `smart-message/src/main/java/com/smart/message/controller/MessageController.java`
    - `smart-message/src/main/java/com/smart/message/service/MessageService.java`
    - `smart-message/src/main/java/com/smart/message/config/RabbitMQConfig.java`
  - **依赖**: Task 8
  - **验收标准**: 
    - 集成RabbitMQ消息队列
    - 支持消息发送和接收
    - 包含消息模板功能
    - 支持异步消息处理
  - **_Prompt**: 
    ```
    Implement the task for spec mengyuan-integration, first run spec-workflow-guide to get the workflow guide then implement the task:
    
    Role: 消息队列专家
    Task: 创建消息管理模块，集成RabbitMQ
    Restrictions: 必须支持异步处理，包含消息模板
    Leverage: RabbitMQ和Spring Boot消息功能
    Requirements: 实现完整的消息管理功能
    Success: 消息功能完整，异步处理正常
    
    Instructions: 在tasks.md中将此任务标记为进行中[-]，完成后标记为完成[x]
    ```

- [x] Task 13: 集成测试和验证
  - **文件**: 
    - `smart-system/src/test/java/com/smart/system/SystemIntegrationTest.java`
    - `scripts/test-integration.sh`
  - **依赖**: Task 12
  - **验收标准**: 
    - 所有模块能够正常启动
    - API接口测试通过
    - 数据库连接正常
    - 消息队列工作正常
  - **_Prompt**: 
    ```
    Implement the task for spec mengyuan-integration, first run spec-workflow-guide to get the workflow guide then implement the task:
    
    Role: 测试专家
    Task: 创建集成测试，验证整个系统功能
    Restrictions: 必须覆盖核心功能，确保系统可运行
    Leverage: Spring Boot测试框架和现有测试结构
    Requirements: 创建完整的集成测试
    Success: 所有测试通过，系统功能正常
    
    Instructions: 在tasks.md中将此任务标记为进行中[-]，完成后标记为完成[x]
    ```

## 实现顺序
任务按依赖关系排序，基础任务优先：
1. Task 1: 创建DTO模块（基础架构）
2. Task 2: 升级依赖版本（技术栈升级）
3. Task 3: 创建数据库实体（数据模型）
4. Task 4: 创建DTO类（数据传输）
5. Task 5: 创建MapStruct映射器（对象转换）
6. Task 6: 创建DAO层（数据访问）
7. Task 7: 创建Service层（业务逻辑）
8. Task 8: 创建Controller层（API接口）
9. Task 9: 升级Spring Security配置（安全框架）
10. Task 10: 创建数据库初始化脚本（数据初始化）
11. Task 11: 创建文件管理模块（文件功能）
12. Task 12: 创建消息管理模块（消息功能）
13. Task 13: 集成测试和验证（系统验证）
