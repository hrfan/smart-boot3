-- =============================================
-- Smart Boot3 初始数据脚本
-- 版本: 1.0.0
-- 作者: smart-boot3
-- 描述: 系统初始数据和基础配置
-- =============================================

-- =============================================
-- 插入系统用户数据
-- =============================================
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `nickname`, `email`, `phone`, `gender`, `status`, `create_by`, `create_user_name`) VALUES
('admin-uuid-001', 'admin', '$2a$10$7JB720yubVSOfvVWzKzKjOjKzKjOjKzKjOjKzKjOjKzKjOjKzKjOjK', '系统管理员', '管理员', 'admin@smart-boot3.com', '13800138000', 1, 1, 'system', '系统'),
('user-uuid-001', 'user', '$2a$10$7JB720yubVSOfvVWzKzKjOjKzKjOjKzKjOjKzKjOjKzKjOjKzKjOjK', '普通用户', '用户', 'user@smart-boot3.com', '13800138001', 1, 1, 'admin-uuid-001', '系统管理员'),
('test-uuid-001', 'test', '$2a$10$7JB720yubVSOfvVWzKzKjOjKzKjOjKzKjOjKzKjOjKzKjOjKzKjOjK', '测试用户', '测试', 'test@smart-boot3.com', '13800138002', 0, 1, 'admin-uuid-001', '系统管理员');

-- =============================================
-- 插入系统角色数据
-- =============================================
INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `description`, `sort_order`, `status`, `create_by`, `create_user_name`) VALUES
('role-uuid-001', '超级管理员', 'SUPER_ADMIN', '拥有系统所有权限', 1, 1, 'system', '系统'),
('role-uuid-002', '系统管理员', 'ADMIN', '系统管理权限', 2, 1, 'system', '系统'),
('role-uuid-003', '普通用户', 'USER', '普通用户权限', 3, 1, 'system', '系统'),
('role-uuid-004', '访客', 'GUEST', '访客权限', 4, 1, 'system', '系统');

-- =============================================
-- 插入系统权限数据
-- =============================================
-- 一级菜单权限
INSERT INTO `sys_permission` (`id`, `permission_name`, `permission_code`, `permission_type`, `parent_id`, `path`, `component`, `icon`, `sort_order`, `status`, `create_by`, `create_user_name`) VALUES
('perm-uuid-001', '系统管理', 'system', 0, NULL, '/system', 'Layout', 'system', 1, 1, 'system', '系统'),
('perm-uuid-002', '用户管理', 'system:user', 0, 'perm-uuid-001', '/system/user', 'system/user/index', 'user', 1, 1, 'system', '系统'),
('perm-uuid-003', '角色管理', 'system:role', 0, 'perm-uuid-001', '/system/role', 'system/role/index', 'role', 2, 1, 'system', '系统'),
('perm-uuid-004', '权限管理', 'system:permission', 0, 'perm-uuid-001', '/system/permission', 'system/permission/index', 'permission', 3, 1, 'system', '系统'),
('perm-uuid-005', '文件管理', 'system:file', 0, 'perm-uuid-001', '/system/file', 'system/file/index', 'file', 4, 1, 'system', '系统'),
('perm-uuid-006', '消息管理', 'system:message', 0, 'perm-uuid-001', '/system/message', 'system/message/index', 'message', 5, 1, 'system', '系统'),
('perm-uuid-007', '系统监控', 'monitor', 0, NULL, '/monitor', 'Layout', 'monitor', 2, 1, 'system', '系统'),
('perm-uuid-008', '操作日志', 'monitor:log', 0, 'perm-uuid-007', '/monitor/log', 'monitor/log/index', 'log', 1, 1, 'system', '系统');

-- 用户管理按钮权限
INSERT INTO `sys_permission` (`id`, `permission_name`, `permission_code`, `permission_type`, `parent_id`, `method`, `sort_order`, `status`, `create_by`, `create_user_name`) VALUES
('perm-uuid-011', '用户查询', 'system:user:list', 1, 'perm-uuid-002', 'GET', 1, 1, 'system', '系统'),
('perm-uuid-012', '用户详情', 'system:user:query', 1, 'perm-uuid-002', 'GET', 2, 1, 'system', '系统'),
('perm-uuid-013', '用户新增', 'system:user:add', 1, 'perm-uuid-002', 'POST', 3, 1, 'system', '系统'),
('perm-uuid-014', '用户修改', 'system:user:edit', 1, 'perm-uuid-002', 'PUT', 4, 1, 'system', '系统'),
('perm-uuid-015', '用户删除', 'system:user:remove', 1, 'perm-uuid-002', 'DELETE', 5, 1, 'system', '系统'),
('perm-uuid-016', '用户导出', 'system:user:export', 1, 'perm-uuid-002', 'POST', 6, 1, 'system', '系统'),
('perm-uuid-017', '用户导入', 'system:user:import', 1, 'perm-uuid-002', 'POST', 7, 1, 'system', '系统');

-- 角色管理按钮权限
INSERT INTO `sys_permission` (`id`, `permission_name`, `permission_code`, `permission_type`, `parent_id`, `method`, `sort_order`, `status`, `create_by`, `create_user_name`) VALUES
('perm-uuid-021', '角色查询', 'system:role:list', 1, 'perm-uuid-003', 'GET', 1, 1, 'system', '系统'),
('perm-uuid-022', '角色详情', 'system:role:query', 1, 'perm-uuid-003', 'GET', 2, 1, 'system', '系统'),
('perm-uuid-023', '角色新增', 'system:role:add', 1, 'perm-uuid-003', 'POST', 3, 1, 'system', '系统'),
('perm-uuid-024', '角色修改', 'system:role:edit', 1, 'perm-uuid-003', 'PUT', 4, 1, 'system', '系统'),
('perm-uuid-025', '角色删除', 'system:role:remove', 1, 'perm-uuid-003', 'DELETE', 5, 1, 'system', '系统'),
('perm-uuid-026', '角色导出', 'system:role:export', 1, 'perm-uuid-003', 'POST', 6, 1, 'system', '系统');

-- 权限管理按钮权限
INSERT INTO `sys_permission` (`id`, `permission_name`, `permission_code`, `permission_type`, `parent_id`, `method`, `sort_order`, `status`, `create_by`, `create_user_name`) VALUES
('perm-uuid-031', '权限查询', 'system:permission:list', 1, 'perm-uuid-004', 'GET', 1, 1, 'system', '系统'),
('perm-uuid-032', '权限详情', 'system:permission:query', 1, 'perm-uuid-004', 'GET', 2, 1, 'system', '系统'),
('perm-uuid-033', '权限新增', 'system:permission:add', 1, 'perm-uuid-004', 'POST', 3, 1, 'system', '系统'),
('perm-uuid-034', '权限修改', 'system:permission:edit', 1, 'perm-uuid-004', 'PUT', 4, 1, 'system', '系统'),
('perm-uuid-035', '权限删除', 'system:permission:remove', 1, 'perm-uuid-004', 'DELETE', 5, 1, 'system', '系统');

-- 文件管理按钮权限
INSERT INTO `sys_permission` (`id`, `permission_name`, `permission_code`, `permission_type`, `parent_id`, `method`, `sort_order`, `status`, `create_by`, `create_user_name`) VALUES
('perm-uuid-041', '文件查询', 'system:file:list', 1, 'perm-uuid-005', 'GET', 1, 1, 'system', '系统'),
('perm-uuid-042', '文件上传', 'system:file:upload', 1, 'perm-uuid-005', 'POST', 2, 1, 'system', '系统'),
('perm-uuid-043', '文件下载', 'system:file:download', 1, 'perm-uuid-005', 'GET', 3, 1, 'system', '系统'),
('perm-uuid-044', '文件删除', 'system:file:remove', 1, 'perm-uuid-005', 'DELETE', 4, 1, 'system', '系统');

-- 消息管理按钮权限
INSERT INTO `sys_permission` (`id`, `permission_name`, `permission_code`, `permission_type`, `parent_id`, `method`, `sort_order`, `status`, `create_by`, `create_user_name`) VALUES
('perm-uuid-051', '消息查询', 'system:message:list', 1, 'perm-uuid-006', 'GET', 1, 1, 'system', '系统'),
('perm-uuid-052', '消息发送', 'system:message:send', 1, 'perm-uuid-006', 'POST', 2, 1, 'system', '系统'),
('perm-uuid-053', '消息删除', 'system:message:remove', 1, 'perm-uuid-006', 'DELETE', 3, 1, 'system', '系统');

-- API权限
INSERT INTO `sys_permission` (`id`, `permission_name`, `permission_code`, `permission_type`, `parent_id`, `path`, `method`, `sort_order`, `status`, `create_by`, `create_user_name`) VALUES
('perm-uuid-061', '用户API', 'api:user', 2, NULL, '/api/system/users/**', 'ALL', 1, 1, 'system', '系统'),
('perm-uuid-062', '角色API', 'api:role', 2, NULL, '/api/system/roles/**', 'ALL', 2, 1, 'system', '系统'),
('perm-uuid-063', '权限API', 'api:permission', 2, NULL, '/api/system/permissions/**', 'ALL', 3, 1, 'system', '系统'),
('perm-uuid-064', '文件API', 'api:file', 2, NULL, '/api/system/files/**', 'ALL', 4, 1, 'system', '系统'),
('perm-uuid-065', '消息API', 'api:message', 2, NULL, '/api/system/messages/**', 'ALL', 5, 1, 'system', '系统'),
('perm-uuid-066', '认证API', 'api:auth', 2, NULL, '/api/auth/**', 'ALL', 6, 1, 'system', '系统');

-- =============================================
-- 插入用户角色关联数据
-- =============================================
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`, `create_by`, `create_user_name`) VALUES
('ur-uuid-001', 'admin-uuid-001', 'role-uuid-001', 'system', '系统'),
('ur-uuid-002', 'user-uuid-001', 'role-uuid-003', 'admin-uuid-001', '系统管理员'),
('ur-uuid-003', 'test-uuid-001', 'role-uuid-004', 'admin-uuid-001', '系统管理员');

-- =============================================
-- 插入角色权限关联数据
-- =============================================
-- 超级管理员拥有所有权限
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_by`, `create_user_name`) VALUES
('rp-uuid-001', 'role-uuid-001', 'perm-uuid-001', 'system', '系统'),
('rp-uuid-002', 'role-uuid-001', 'perm-uuid-002', 'system', '系统'),
('rp-uuid-003', 'role-uuid-001', 'perm-uuid-003', 'system', '系统'),
('rp-uuid-004', 'role-uuid-001', 'perm-uuid-004', 'system', '系统'),
('rp-uuid-005', 'role-uuid-001', 'perm-uuid-005', 'system', '系统'),
('rp-uuid-006', 'role-uuid-001', 'perm-uuid-006', 'system', '系统'),
('rp-uuid-007', 'role-uuid-001', 'perm-uuid-007', 'system', '系统'),
('rp-uuid-008', 'role-uuid-001', 'perm-uuid-008', 'system', '系统');

-- 系统管理员权限
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_by`, `create_user_name`) VALUES
('rp-uuid-011', 'role-uuid-002', 'perm-uuid-001', 'system', '系统'),
('rp-uuid-012', 'role-uuid-002', 'perm-uuid-002', 'system', '系统'),
('rp-uuid-013', 'role-uuid-002', 'perm-uuid-003', 'system', '系统'),
('rp-uuid-014', 'role-uuid-002', 'perm-uuid-004', 'system', '系统'),
('rp-uuid-015', 'role-uuid-002', 'perm-uuid-005', 'system', '系统'),
('rp-uuid-016', 'role-uuid-002', 'perm-uuid-006', 'system', '系统');

-- 普通用户权限
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_by`, `create_user_name`) VALUES
('rp-uuid-021', 'role-uuid-003', 'perm-uuid-005', 'system', '系统'),
('rp-uuid-022', 'role-uuid-003', 'perm-uuid-006', 'system', '系统');

-- =============================================
-- 插入系统配置数据
-- =============================================
INSERT INTO `sys_config` (`id`, `config_key`, `config_value`, `config_name`, `config_desc`, `config_type`, `status`, `create_by`, `create_user_name`) VALUES
('config-uuid-001', 'system.name', 'Smart Boot3', '系统名称', '系统显示名称', 0, 1, 'system', '系统'),
('config-uuid-002', 'system.version', '1.0.0', '系统版本', '当前系统版本号', 0, 1, 'system', '系统'),
('config-uuid-003', 'system.copyright', 'Copyright © 2024 Smart Boot3', '版权信息', '系统版权信息', 0, 1, 'system', '系统'),
('config-uuid-004', 'system.logo', '/static/images/logo.png', '系统Logo', '系统Logo路径', 0, 1, 'system', '系统'),
('config-uuid-005', 'file.upload.path', '/uploads/', '文件上传路径', '文件上传存储路径', 1, 1, 'system', '系统'),
('config-uuid-006', 'file.upload.maxSize', '10485760', '文件上传大小限制', '单个文件最大上传大小（字节）', 1, 1, 'system', '系统'),
('config-uuid-007', 'file.upload.allowedTypes', 'jpg,jpeg,png,gif,pdf,doc,docx,xls,xlsx', '允许上传的文件类型', '允许上传的文件扩展名', 1, 1, 'system', '系统'),
('config-uuid-008', 'jwt.secret', 'smart-boot3-jwt-secret-key-256-bit', 'JWT密钥', 'JWT Token签名密钥', 0, 1, 'system', '系统'),
('config-uuid-009', 'jwt.expiration', '86400000', 'JWT过期时间', 'JWT Token过期时间（毫秒）', 0, 1, 'system', '系统'),
('config-uuid-010', 'jwt.refresh-expiration', '604800000', 'JWT刷新过期时间', 'JWT刷新Token过期时间（毫秒）', 0, 1, 'system', '系统');

-- =============================================
-- 插入初始消息数据
-- =============================================
INSERT INTO `sys_message` (`id`, `title`, `content`, `message_type`, `sender_id`, `sender_name`, `receiver_id`, `receiver_name`, `is_read`, `status`, `create_by`, `create_user_name`) VALUES
('msg-uuid-001', '欢迎使用Smart Boot3', '欢迎使用Smart Boot3系统管理平台！', 0, 'system', '系统', 'admin-uuid-001', '系统管理员', 0, 1, 'system', '系统'),
('msg-uuid-002', '系统初始化完成', '系统初始化已完成，您可以开始使用各项功能。', 1, 'system', '系统', 'admin-uuid-001', '系统管理员', 0, 1, 'system', '系统'),
('msg-uuid-003', '欢迎新用户', '欢迎加入Smart Boot3系统！', 0, 'admin-uuid-001', '系统管理员', 'user-uuid-001', '普通用户', 0, 1, 'admin-uuid-001', '系统管理员');

-- =============================================
-- 插入操作日志示例数据
-- =============================================
INSERT INTO `sys_operation_log` (`id`, `user_id`, `username`, `operation`, `method`, `params`, `time`, `ip`, `location`, `user_agent`, `status`, `create_time`) VALUES
('log-uuid-001', 'admin-uuid-001', 'admin', '用户登录', 'POST', '{"username":"admin"}', 150, '127.0.0.1', '本地', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36', 1, NOW()),
('log-uuid-002', 'admin-uuid-001', 'admin', '查询用户列表', 'GET', '{"pageNum":1,"pageSize":10}', 80, '127.0.0.1', '本地', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36', 1, NOW()),
('log-uuid-003', 'admin-uuid-001', 'admin', '创建用户', 'POST', '{"username":"test","realName":"测试用户"}', 200, '127.0.0.1', '本地', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36', 1, NOW());
