# Spring Security 改造任务

## Implementation Tasks
将temp config中的Spring Security配置整合到现有项目中，完成现代化改造。

### Task Format
每个任务包含：
- **Description**: 需要实现的内容
- **Files**: 需要创建/修改的具体文件
- **Dependencies**: 必须先完成的其他任务
- **Acceptance Criteria**: 如何验证完成
- **_Prompt**: 详细的实现指导

## Task List

- [x] Task 1: 分析temp config配置并提取关键组件
  - **Files**: temp_config目录下的所有文件
  - **Dependencies**: none
  - **Acceptance Criteria**: 完成temp config配置分析，识别需要整合的组件
  - **_Prompt**: 实现spring-security-refactor spec的任务，首先运行spec-workflow-guide获取工作流指南然后实现任务：
    - **Role**: Spring Security架构师
    - **Task**: 分析temp config目录下的Spring Security配置，识别关键组件和最佳实践
    - **Restrictions**: 不要修改任何现有文件，只进行分析
    - **_Leverage**: temp_config目录下的所有配置文件
    - **_Requirements**: 需求文档中的技术分析要求
    - **Success**: 完成配置分析，识别需要整合的组件列表
    - **Instructions**: 在tasks.md中将此任务标记为进行中[-]，完成后标记为完成[x]

- [x] Task 2: 创建自定义UserDetailsService实现
  - **Files**: smart-common/smart-common-security/src/main/java/com/smart/common/security/service/CustomUserDetailsService.java
  - **Dependencies**: Task 1
  - **Acceptance Criteria**: 创建基于数据库的用户详情服务，使用TODO标记权限查询功能
  - **_Prompt**: 实现spring-security-refactor spec的任务，首先运行spec-workflow-guide获取工作流指南然后实现任务：
    - **Role**: Spring Security开发者
    - **Task**: 创建CustomUserDetailsService，参考temp config中的CustomerUserDetailsService
    - **Restrictions**: 使用TODO标记用户权限查询数据的功能
    - **_Leverage**: temp_config/security/CustomerUserDetailsService.java作为参考
    - **_Requirements**: 需求文档中的用户认证要求
    - **Success**: 创建完整的UserDetailsService实现，包含TODO标记
    - **Instructions**: 在tasks.md中将此任务标记为进行中[-]，完成后标记为完成[x]

- [x] Task 3: 创建自定义认证处理器
  - **Files**: smart-common/smart-common-security/src/main/java/com/smart/common/security/handler/
  - **Dependencies**: Task 2
  - **Acceptance Criteria**: 创建登录成功、失败、登出等处理器
  - **_Prompt**: 实现spring-security-refactor spec的任务，首先运行spec-workflow-guide获取工作流指南然后实现任务：
    - **Role**: Spring Security开发者
    - **Task**: 创建认证相关的处理器，包括登录成功、失败、登出处理器
    - **Restrictions**: 保持与现有handler的兼容性
    - **_Leverage**: temp_config/security/handler/目录下的处理器
    - **_Requirements**: 需求文档中的异常处理要求
    - **Success**: 创建所有必要的认证处理器
    - **Instructions**: 在tasks.md中将此任务标记为进行中[-]，完成后标记为完成[x]

- [x] Task 4: 创建自定义认证过滤器
  - **Files**: smart-common/smart-common-security/src/main/java/com/smart/common/security/filter/
  - **Dependencies**: Task 3
  - **Acceptance Criteria**: 创建用户名密码认证过滤器和提供者
  - **_Prompt**: 实现spring-security-refactor spec的任务，首先运行spec-workflow-guide获取工作流指南然后实现任务：
    - **Role**: Spring Security开发者
    - **Task**: 创建自定义的认证过滤器和认证提供者
    - **Restrictions**: 与现有JWT过滤器兼容
    - **_Leverage**: temp_config/security/login/目录下的认证组件
    - **_Requirements**: 需求文档中的认证机制要求
    - **Success**: 创建完整的认证过滤器和提供者
    - **Instructions**: 在tasks.md中将此任务标记为进行中[-]，完成后标记为完成[x]

- [x] Task 5: 改造现有SecurityConfig
  - **Files**: smart-common/smart-common-security/src/main/java/com/smart/common/security/config/SecurityConfig.java
  - **Dependencies**: Task 4
  - **Acceptance Criteria**: 整合temp config中的优秀配置到现有SecurityConfig
  - **_Prompt**: 实现spring-security-refactor spec的任务，首先运行spec-workflow-guide获取工作流指南然后实现任务：
    - **Role**: Spring Security配置专家
    - **Task**: 改造现有SecurityConfig，整合temp config中的最佳实践
    - **Restrictions**: 保持现有API兼容性，不破坏现有功能
    - **_Leverage**: 现有SecurityConfig.java和temp_config/security/SpringSecurityConfig.java
    - **_Requirements**: 需求文档中的所有安全要求
    - **Success**: 完成SecurityConfig改造，整合所有优秀配置
    - **Instructions**: 在tasks.md中将此任务标记为进行中[-]，完成后标记为完成[x]

- [x] Task 6: 添加安全配置到application.yml
  - **Files**: smart-system/src/main/resources/application.yml
  - **Dependencies**: Task 5
  - **Acceptance Criteria**: 在application.yml中添加安全相关配置
  - **_Prompt**: 实现spring-security-refactor spec的任务，首先运行spec-workflow-guide获取工作流指南然后实现任务：
    - **Role**: Spring Boot配置专家
    - **Task**: 在application.yml中添加安全相关配置
    - **Restrictions**: 不破坏现有配置
    - **_Leverage**: temp config中的配置参数
    - **_Requirements**: 需求文档中的配置要求
    - **Success**: 添加完整的安全配置
    - **Instructions**: 在tasks.md中将此任务标记为进行中[-]，完成后标记为完成[x]

- [x] Task 7: 删除temp config目录
  - **Files**: smart-common/smart-common-security/src/main/java/com/smart/common/security/temp_config/
  - **Dependencies**: Task 6
  - **Acceptance Criteria**: 完全删除temp config目录及其所有文件
  - **_Prompt**: 实现spring-security-refactor spec的任务，首先运行spec-workflow-guide获取工作流指南然后实现任务：
    - **Role**: 代码清理专家
    - **Task**: 删除temp config目录及其所有文件
    - **Restrictions**: 确保所有配置已经整合到正式文件中
    - **_Leverage**: 无
    - **_Requirements**: 需求文档中的清理要求
    - **Success**: 完全删除temp config目录
    - **Instructions**: 在tasks.md中将此任务标记为进行中[-]，完成后标记为完成[x]

- [x] Task 8: 更新README.md记录改造内容
  - **Files**: README.md
  - **Dependencies**: Task 7
  - **Acceptance Criteria**: 在README.md中详细记录Spring Security改造内容
  - **_Prompt**: 实现spring-security-refactor spec的任务，首先运行spec-workflow-guide获取工作流指南然后实现任务：
    - **Role**: 技术文档专家
    - **Task**: 更新README.md，记录Spring Security改造的详细内容
    - **Restrictions**: 保持文档格式一致
    - **_Leverage**: 改造过程中的所有变更
    - **_Requirements**: 需求文档中的文档要求
    - **Success**: 完成README.md更新，详细记录改造内容
    - **Instructions**: 在tasks.md中将此任务标记为进行中[-]，完成后标记为完成[x]

## Implementation Order
任务按依赖关系排序，基础任务优先：
1. Task 1: 分析temp config配置
2. Task 2: 创建UserDetailsService
3. Task 3: 创建认证处理器
4. Task 4: 创建认证过滤器
5. Task 5: 改造SecurityConfig
6. Task 6: 添加配置到application.yml
7. Task 7: 删除temp config目录
8. Task 8: 更新README.md
