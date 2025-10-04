-- =============================================
-- Smart Boot3 测试数据初始化脚本
-- 版本: 1.0.0
-- 作者: smart-boot3
-- 描述: 系统测试数据插入脚本
-- =============================================

-- 使用数据库
-- USE smart_boot3;

-- =============================================
-- 清理现有测试数据
-- =============================================
DELETE FROM smart_user_role WHERE user_id IN ('test-user-001', 'test-user-002', 'test-user-003');
DELETE FROM smart_role_permission WHERE role_id IN ('test-role-001', 'test-role-002', 'test-role-003');
DELETE FROM smart_permission WHERE id LIKE 'test-perm-%';
DELETE FROM smart_role WHERE id LIKE 'test-role-%';
DELETE FROM smart_user WHERE id LIKE 'test-user-%';

-- =============================================
-- 插入测试用户数据
-- =============================================
INSERT INTO smart_user (
    id, dept_id, user_name, nick_name, user_type, email, phonenumber, 
    sex, avatar, password, status, del_flag, login_ip, login_date, 
    remark, tenant_id, create_time, create_by, create_user_name, 
    update_time, update_by, update_user_name
) VALUES 
-- 超级管理员
('test-user-001', 'dept-001', 'admin', '超级管理员', '00', 'admin@smart-boot3.com', '13800138001', 
 '0', '/avatar/admin.jpg', '$2a$10$7JB720yubVSOfvVWbQjKQeKjKQeKjKQeKjKQeKjKQeKjKQeKjKQeK', '0', '0', '127.0.0.1', NOW(), 
 '系统超级管理员账户', 'tenant-001', NOW(), 'system', '系统', NOW(), 'system', '系统'),

-- 普通管理员
('test-user-002', 'dept-002', 'manager', '管理员', '00', 'manager@smart-boot3.com', '13800138002', 
 '0', '/avatar/manager.jpg', '$2a$10$7JB720yubVSOfvVWbQjKQeKjKQeKjKQeKjKQeKjKQeKjKQeKjKQeK', '0', '0', '127.0.0.1', NOW(), 
 '系统管理员账户', 'tenant-001', NOW(), 'test-user-001', '超级管理员', NOW(), 'test-user-001', '超级管理员'),

-- 普通用户
('test-user-003', 'dept-003', 'user', '普通用户', '00', 'user@smart-boot3.com', '13800138003', 
 '1', '/avatar/user.jpg', '$2a$10$7JB720yubVSOfvVWbQjKQeKjKQeKjKQeKjKQeKjKQeKjKQeKjKQeK', '0', '0', '127.0.0.1', NOW(), 
 '普通用户账户', 'tenant-001', NOW(), 'test-user-001', '超级管理员', NOW(), 'test-user-001', '超级管理员');

-- =============================================
-- 插入测试角色数据
-- =============================================
INSERT INTO smart_role (
    id, role_name, role_key, role_sort, data_scope, menu_check_strictly, 
    dept_check_strictly, status, del_flag, remark, tenant_id, 
    create_time, create_by, create_user_name, update_time, update_by, update_user_name
) VALUES 
-- 超级管理员角色
('test-role-001', '超级管理员', 'super_admin', 1, '1', 1, 1, '0', '0', 
 '拥有系统所有权限的超级管理员角色', 'tenant-001', NOW(), 'system', '系统', NOW(), 'system', '系统'),

-- 管理员角色
('test-role-002', '管理员', 'admin', 2, '2', 1, 1, '0', '0', 
 '拥有大部分系统权限的管理员角色', 'tenant-001', NOW(), 'test-user-001', '超级管理员', NOW(), 'test-user-001', '超级管理员'),

-- 普通用户角色
('test-role-003', '普通用户', 'user', 3, '3', 1, 1, '0', '0', 
 '拥有基础权限的普通用户角色', 'tenant-001', NOW(), 'test-user-001', '超级管理员', NOW(), 'test-user-001', '超级管理员');

-- =============================================
-- 插入测试权限数据（菜单权限）
-- =============================================
INSERT INTO smart_permission (
    id, menu_name, parent_id, order_num, path, component, query, route_name, 
    is_frame, frame_url, is_cache, menu_type, visible, status, perms, icon, 
    create_by, create_time, create_user_name, update_by, update_time, update_user_name, 
    remark, is_leaf, always_show, color, default_shortcut_menu, reminder_menu, tenant_menu, allow_visitor_access
) VALUES 
-- 系统管理（一级菜单）
('test-perm-001', '系统管理', '0', 1, '/system', 'Layout', '', 'System', 
 '1', '', '0', 'M', '0', '0', 'system:manage', 'system', 
 'system', NOW(), '系统', 'system', NOW(), '系统', 
 '系统管理模块', 0, 1, '#409EFF', '0', '0', '1', '0'),

-- 用户管理（二级菜单）
('test-perm-002', '用户管理', 'test-perm-001', 1, 'user', 'system/user/index', '', 'User', 
 '1', '', '0', 'C', '0', '0', 'system:user:list', 'user', 
 'system', NOW(), '系统', 'system', NOW(), '系统', 
 '用户管理页面', 1, 0, '#409EFF', '0', '0', '1', '0'),

-- 用户查询（按钮权限）
('test-perm-003', '用户查询', 'test-perm-002', 1, '', '', '', '', 
 '1', '', '0', 'F', '0', '0', 'system:user:query', '#', 
 'system', NOW(), '系统', 'system', NOW(), '系统', 
 '用户查询权限', 1, 0, '#409EFF', '0', '0', '1', '0'),

-- 用户新增（按钮权限）
('test-perm-004', '用户新增', 'test-perm-002', 2, '', '', '', '', 
 '1', '', '0', 'F', '0', '0', 'system:user:add', '#', 
 'system', NOW(), '系统', 'system', NOW(), '系统', 
 '用户新增权限', 1, 0, '#409EFF', '0', '0', '1', '0'),

-- 用户修改（按钮权限）
('test-perm-005', '用户修改', 'test-perm-002', 3, '', '', '', '', 
 '1', '', '0', 'F', '0', '0', 'system:user:edit', '#', 
 'system', NOW(), '系统', 'system', NOW(), '系统', 
 '用户修改权限', 1, 0, '#409EFF', '0', '0', '1', '0'),

-- 用户删除（按钮权限）
('test-perm-006', '用户删除', 'test-perm-002', 4, '', '', '', '', 
 '1', '', '0', 'F', '0', '0', 'system:user:remove', '#', 
 'system', NOW(), '系统', 'system', NOW(), '系统', 
 '用户删除权限', 1, 0, '#409EFF', '0', '0', '1', '0'),

-- 角色管理（二级菜单）
('test-perm-007', '角色管理', 'test-perm-001', 2, 'role', 'system/role/index', '', 'Role', 
 '1', '', '0', 'C', '0', '0', 'system:role:list', 'peoples', 
 'system', NOW(), '系统', 'system', NOW(), '系统', 
 '角色管理页面', 1, 0, '#409EFF', '0', '0', '1', '0'),

-- 角色查询（按钮权限）
('test-perm-008', '角色查询', 'test-perm-007', 1, '', '', '', '', 
 '1', '', '0', 'F', '0', '0', 'system:role:query', '#', 
 'system', NOW(), '系统', 'system', NOW(), '系统', 
 '角色查询权限', 1, 0, '#409EFF', '0', '0', '1', '0'),

-- 角色新增（按钮权限）
('test-perm-009', '角色新增', 'test-perm-007', 2, '', '', '', '', 
 '1', '', '0', 'F', '0', '0', 'system:role:add', '#', 
 'system', NOW(), '系统', 'system', NOW(), '系统', 
 '角色新增权限', 1, 0, '#409EFF', '0', '0', '1', '0'),

-- 角色修改（按钮权限）
('test-perm-010', '角色修改', 'test-perm-007', 3, '', '', '', '', 
 '1', '', '0', 'F', '0', '0', 'system:role:edit', '#', 
 'system', NOW(), '系统', 'system', NOW(), '系统', 
 '角色修改权限', 1, 0, '#409EFF', '0', '0', '1', '0'),

-- 角色删除（按钮权限）
('test-perm-011', '角色删除', 'test-perm-007', 4, '', '', '', '', 
 '1', '', '0', 'F', '0', '0', 'system:role:remove', '#', 
 'system', NOW(), '系统', 'system', NOW(), '系统', 
 '角色删除权限', 1, 0, '#409EFF', '0', '0', '1', '0'),

-- 权限管理（二级菜单）
('test-perm-012', '权限管理', 'test-perm-001', 3, 'permission', 'system/permission/index', '', 'Permission', 
 '1', '', '0', 'C', '0', '0', 'system:permission:list', 'tree-table', 
 'system', NOW(), '系统', 'system', NOW(), '系统', 
 '权限管理页面', 1, 0, '#409EFF', '0', '0', '1', '0'),

-- 权限查询（按钮权限）
('test-perm-013', '权限查询', 'test-perm-012', 1, '', '', '', '', 
 '1', '', '0', 'F', '0', '0', 'system:permission:query', '#', 
 'system', NOW(), '系统', 'system', NOW(), '系统', 
 '权限查询权限', 1, 0, '#409EFF', '0', '0', '1', '0'),

-- 权限新增（按钮权限）
('test-perm-014', '权限新增', 'test-perm-012', 2, '', '', '', '', 
 '1', '', '0', 'F', '0', '0', 'system:permission:add', '#', 
 'system', NOW(), '系统', 'system', NOW(), '系统', 
 '权限新增权限', 1, 0, '#409EFF', '0', '0', '1', '0'),

-- 权限修改（按钮权限）
('test-perm-015', '权限修改', 'test-perm-012', 3, '', '', '', '', 
 '1', '', '0', 'F', '0', '0', 'system:permission:edit', '#', 
 'system', NOW(), '系统', 'system', NOW(), '系统', 
 '权限修改权限', 1, 0, '#409EFF', '0', '0', '1', '0'),

-- 权限删除（按钮权限）
('test-perm-016', '权限删除', 'test-perm-012', 4, '', '', '', '', 
 '1', '', '0', 'F', '0', '0', 'system:permission:remove', '#', 
 'system', NOW(), '系统', 'system', NOW(), '系统', 
 '权限删除权限', 1, 0, '#409EFF', '0', '0', '1', '0'),

-- 业务管理（一级菜单）
('test-perm-017', '业务管理', '0', 2, '/business', 'Layout', '', 'Business', 
 '1', '', '0', 'M', '0', '0', 'business:manage', 'shopping', 
 'system', NOW(), '系统', 'system', NOW(), '系统', 
 '业务管理模块', 0, 1, '#67C23A', '0', '0', '1', '0'),

-- 订单管理（二级菜单）
('test-perm-018', '订单管理', 'test-perm-017', 1, 'order', 'business/order/index', '', 'Order', 
 '1', '', '0', 'C', '0', '0', 'business:order:list', 'list', 
 'system', NOW(), '系统', 'system', NOW(), '系统', 
 '订单管理页面', 1, 0, '#67C23A', '0', '0', '1', '0'),

-- 订单查询（按钮权限）
('test-perm-019', '订单查询', 'test-perm-018', 1, '', '', '', '', 
 '1', '', '0', 'F', '0', '0', 'business:order:query', '#', 
 'system', NOW(), '系统', 'system', NOW(), '系统', 
 '订单查询权限', 1, 0, '#67C23A', '0', '0', '1', '0'),

-- 订单新增（按钮权限）
('test-perm-020', '订单新增', 'test-perm-018', 2, '', '', '', '', 
 '1', '', '0', 'F', '0', '0', 'business:order:add', '#', 
 'system', NOW(), '系统', 'system', NOW(), '系统', 
 '订单新增权限', 1, 0, '#67C23A', '0', '0', '1', '0'),

-- 订单修改（按钮权限）
('test-perm-021', '订单修改', 'test-perm-018', 3, '', '', '', '', 
 '1', '', '0', 'F', '0', '0', 'business:order:edit', '#', 
 'system', NOW(), '系统', 'system', NOW(), '系统', 
 '订单修改权限', 1, 0, '#67C23A', '0', '0', '1', '0'),

-- 订单删除（按钮权限）
('test-perm-022', '订单删除', 'test-perm-018', 4, '', '', '', '', 
 '1', '', '0', 'F', '0', '0', 'business:order:remove', '#', 
 'system', NOW(), '系统', 'system', NOW(), '系统', 
 '订单删除权限', 1, 0, '#67C23A', '0', '0', '1', '0');

-- =============================================
-- 插入用户角色关联数据
-- =============================================
INSERT INTO smart_user_role (
    id, user_id, role_id, create_time, create_by, create_user_name, 
    update_time, update_by, update_user_name
) VALUES 
-- 超级管理员拥有超级管理员角色
('test-ur-001', 'test-user-001', 'test-role-001', NOW(), 'system', '系统', NOW(), 'system', '系统'),

-- 管理员拥有管理员角色
('test-ur-002', 'test-user-002', 'test-role-002', NOW(), 'test-user-001', '超级管理员', NOW(), 'test-user-001', '超级管理员'),

-- 普通用户拥有普通用户角色
('test-ur-003', 'test-user-003', 'test-role-003', NOW(), 'test-user-001', '超级管理员', NOW(), 'test-user-001', '超级管理员');

-- =============================================
-- 插入角色权限关联数据
-- =============================================
INSERT INTO smart_role_permission (
    id, role_id, permission_id, create_time, create_by, create_user_name, 
    update_time, update_by, update_user_name
) VALUES 
-- 超级管理员角色拥有所有权限
('test-rp-001', 'test-role-001', 'test-perm-001', NOW(), 'system', '系统', NOW(), 'system', '系统'),
('test-rp-002', 'test-role-001', 'test-perm-002', NOW(), 'system', '系统', NOW(), 'system', '系统'),
('test-rp-003', 'test-role-001', 'test-perm-003', NOW(), 'system', '系统', NOW(), 'system', '系统'),
('test-rp-004', 'test-role-001', 'test-perm-004', NOW(), 'system', '系统', NOW(), 'system', '系统'),
('test-rp-005', 'test-role-001', 'test-perm-005', NOW(), 'system', '系统', NOW(), 'system', '系统'),
('test-rp-006', 'test-role-001', 'test-perm-006', NOW(), 'system', '系统', NOW(), 'system', '系统'),
('test-rp-007', 'test-role-001', 'test-perm-007', NOW(), 'system', '系统', NOW(), 'system', '系统'),
('test-rp-008', 'test-role-001', 'test-perm-008', NOW(), 'system', '系统', NOW(), 'system', '系统'),
('test-rp-009', 'test-role-001', 'test-perm-009', NOW(), 'system', '系统', NOW(), 'system', '系统'),
('test-rp-010', 'test-role-001', 'test-perm-010', NOW(), 'system', '系统', NOW(), 'system', '系统'),
('test-rp-011', 'test-role-001', 'test-perm-011', NOW(), 'system', '系统', NOW(), 'system', '系统'),
('test-rp-012', 'test-role-001', 'test-perm-012', NOW(), 'system', '系统', NOW(), 'system', '系统'),
('test-rp-013', 'test-role-001', 'test-perm-013', NOW(), 'system', '系统', NOW(), 'system', '系统'),
('test-rp-014', 'test-role-001', 'test-perm-014', NOW(), 'system', '系统', NOW(), 'system', '系统'),
('test-rp-015', 'test-role-001', 'test-perm-015', NOW(), 'system', '系统', NOW(), 'system', '系统'),
('test-rp-016', 'test-role-001', 'test-perm-016', NOW(), 'system', '系统', NOW(), 'system', '系统'),
('test-rp-017', 'test-role-001', 'test-perm-017', NOW(), 'system', '系统', NOW(), 'system', '系统'),
('test-rp-018', 'test-role-001', 'test-perm-018', NOW(), 'system', '系统', NOW(), 'system', '系统'),
('test-rp-019', 'test-role-001', 'test-perm-019', NOW(), 'system', '系统', NOW(), 'system', '系统'),
('test-rp-020', 'test-role-001', 'test-perm-020', NOW(), 'system', '系统', NOW(), 'system', '系统'),
('test-rp-021', 'test-role-001', 'test-perm-021', NOW(), 'system', '系统', NOW(), 'system', '系统'),
('test-rp-022', 'test-role-001', 'test-perm-022', NOW(), 'system', '系统', NOW(), 'system', '系统'),

-- 管理员角色拥有系统管理和部分业务管理权限
('test-rp-023', 'test-role-002', 'test-perm-001', NOW(), 'test-user-001', '超级管理员', NOW(), 'test-user-001', '超级管理员'),
('test-rp-024', 'test-role-002', 'test-perm-002', NOW(), 'test-user-001', '超级管理员', NOW(), 'test-user-001', '超级管理员'),
('test-rp-025', 'test-role-002', 'test-perm-003', NOW(), 'test-user-001', '超级管理员', NOW(), 'test-user-001', '超级管理员'),
('test-rp-026', 'test-role-002', 'test-perm-004', NOW(), 'test-user-001', '超级管理员', NOW(), 'test-user-001', '超级管理员'),
('test-rp-027', 'test-role-002', 'test-perm-005', NOW(), 'test-user-001', '超级管理员', NOW(), 'test-user-001', '超级管理员'),
('test-rp-028', 'test-role-002', 'test-perm-007', NOW(), 'test-user-001', '超级管理员', NOW(), 'test-user-001', '超级管理员'),
('test-rp-029', 'test-role-002', 'test-perm-008', NOW(), 'test-user-001', '超级管理员', NOW(), 'test-user-001', '超级管理员'),
('test-rp-030', 'test-role-002', 'test-perm-009', NOW(), 'test-user-001', '超级管理员', NOW(), 'test-user-001', '超级管理员'),
('test-rp-031', 'test-role-002', 'test-perm-010', NOW(), 'test-user-001', '超级管理员', NOW(), 'test-user-001', '超级管理员'),
('test-rp-032', 'test-role-002', 'test-perm-017', NOW(), 'test-user-001', '超级管理员', NOW(), 'test-user-001', '超级管理员'),
('test-rp-033', 'test-role-002', 'test-perm-018', NOW(), 'test-user-001', '超级管理员', NOW(), 'test-user-001', '超级管理员'),
('test-rp-034', 'test-role-002', 'test-perm-019', NOW(), 'test-user-001', '超级管理员', NOW(), 'test-user-001', '超级管理员'),
('test-rp-035', 'test-role-002', 'test-perm-020', NOW(), 'test-user-001', '超级管理员', NOW(), 'test-user-001', '超级管理员'),
('test-rp-036', 'test-role-002', 'test-perm-021', NOW(), 'test-user-001', '超级管理员', NOW(), 'test-user-001', '超级管理员'),

-- 普通用户角色只拥有业务管理的查询权限
('test-rp-037', 'test-role-003', 'test-perm-017', NOW(), 'test-user-001', '超级管理员', NOW(), 'test-user-001', '超级管理员'),
('test-rp-038', 'test-role-003', 'test-perm-018', NOW(), 'test-user-001', '超级管理员', NOW(), 'test-user-001', '超级管理员'),
('test-rp-039', 'test-role-003', 'test-perm-019', NOW(), 'test-user-001', '超级管理员', NOW(), 'test-user-001', '超级管理员');

-- =============================================
-- 验证数据插入结果
-- =============================================
-- 查询用户信息
SELECT '用户信息' as table_name, COUNT(*) as count FROM smart_user WHERE id LIKE 'test-%';

-- 查询角色信息
SELECT '角色信息' as table_name, COUNT(*) as count FROM smart_role WHERE id LIKE 'test-%';

-- 查询权限信息
SELECT '权限信息' as table_name, COUNT(*) as count FROM smart_permission WHERE id LIKE 'test-%';

-- 查询用户角色关联
SELECT '用户角色关联' as table_name, COUNT(*) as count FROM smart_user_role WHERE id LIKE 'test-%';

-- 查询角色权限关联
SELECT '角色权限关联' as table_name, COUNT(*) as count FROM smart_role_permission WHERE id LIKE 'test-%';

-- =============================================
-- 测试查询语句示例
-- =============================================
-- 查询用户admin的角色
SELECT r.role_name, r.role_key 
FROM smart_user u 
JOIN smart_user_role ur ON u.id = ur.user_id 
JOIN smart_role r ON ur.role_id = r.id 
WHERE u.user_name = 'admin';

-- 查询用户admin的权限
SELECT DISTINCT p.perms 
FROM smart_user u 
JOIN smart_user_role ur ON u.id = ur.user_id 
JOIN smart_role r ON ur.role_id = r.id 
JOIN smart_role_permission rp ON r.id = rp.role_id 
JOIN smart_permission p ON rp.permission_id = p.id 
WHERE u.user_name = 'admin' AND p.perms IS NOT NULL AND p.perms != '';

-- 查询用户admin的菜单权限
SELECT p.menu_name, p.path, p.component, p.icon, p.parent_id, p.order_num 
FROM smart_user u 
JOIN smart_user_role ur ON u.id = ur.user_id 
JOIN smart_role r ON ur.role_id = r.id 
JOIN smart_role_permission rp ON r.id = rp.role_id 
JOIN smart_permission p ON rp.permission_id = p.id 
WHERE u.user_name = 'admin' AND p.menu_type IN ('M', 'C') AND p.visible = '0' 
ORDER BY p.order_num;

-- =============================================
-- 测试数据说明
-- =============================================
/*
测试数据说明：

1. 用户数据：
   - admin: 超级管理员，密码为加密后的"123456"
   - manager: 管理员，密码为加密后的"123456"  
   - user: 普通用户，密码为加密后的"123456"

2. 角色数据：
   - super_admin: 超级管理员角色，拥有所有权限
   - admin: 管理员角色，拥有系统管理和部分业务管理权限
   - user: 普通用户角色，只拥有业务管理的查询权限

3. 权限数据：
   - 系统管理模块：用户管理、角色管理、权限管理
   - 业务管理模块：订单管理
   - 每个模块包含查询、新增、修改、删除等按钮权限

4. 关联关系：
   - admin用户 -> super_admin角色 -> 所有权限
   - manager用户 -> admin角色 -> 系统管理+部分业务管理权限
   - user用户 -> user角色 -> 只读业务管理权限

5. 密码说明：
   - 所有测试用户的密码都是"123456"
   - 密码已使用BCrypt加密存储
   - 实际使用时请修改为安全的密码

6. 使用说明：
   - 可以直接使用admin/admin登录测试
   - 可以使用manager/manager登录测试
   - 可以使用user/user登录测试
   - 登录后可以查看不同角色的权限差异
*/
