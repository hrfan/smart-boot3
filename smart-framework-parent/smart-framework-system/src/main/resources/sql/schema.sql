-- =============================================
-- Smart Boot3 数据库初始化脚本
-- 版本: 1.0.0
-- 作者: smart-boot3
-- 描述: 系统核心表结构定义
-- =============================================

-- 创建数据库（如果不存在）
-- CREATE DATABASE IF NOT EXISTS smart_boot3 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- USE smart_boot3;

-- =============================================
-- 用户表
-- =============================================
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
    `id` VARCHAR(36) NOT NULL COMMENT '主键ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码',
    `real_name` VARCHAR(50) COMMENT '真实姓名',
    `nickname` VARCHAR(50) COMMENT '昵称',
    `email` VARCHAR(100) COMMENT '邮箱',
    `phone` VARCHAR(20) COMMENT '手机号',
    `avatar` VARCHAR(255) COMMENT '头像',
    `gender` TINYINT(1) DEFAULT 0 COMMENT '性别：0-女，1-男',
    `birthday` DATE COMMENT '生日',
    `status` TINYINT(1) DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `last_login_time` TIMESTAMP NULL COMMENT '最后登录时间',
    `last_login_ip` VARCHAR(50) COMMENT '最后登录IP',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by` VARCHAR(36) COMMENT '创建人',
    `create_user_name` VARCHAR(50) COMMENT '创建人姓名',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by` VARCHAR(36) COMMENT '更新人',
    `update_user_name` VARCHAR(50) COMMENT '更新人姓名',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`),
    UNIQUE KEY `uk_phone` (`phone`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户表';

-- =============================================
-- 角色表
-- =============================================
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
    `id` VARCHAR(36) NOT NULL COMMENT '主键ID',
    `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
    `role_code` VARCHAR(50) NOT NULL COMMENT '角色编码',
    `description` VARCHAR(255) COMMENT '角色描述',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `status` TINYINT(1) DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by` VARCHAR(36) COMMENT '创建人',
    `create_user_name` VARCHAR(50) COMMENT '创建人姓名',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by` VARCHAR(36) COMMENT '更新人',
    `update_user_name` VARCHAR(50) COMMENT '更新人姓名',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_code` (`role_code`),
    KEY `idx_status` (`status`),
    KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统角色表';

-- =============================================
-- 权限表
-- =============================================
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
    `id` VARCHAR(36) NOT NULL COMMENT '主键ID',
    `permission_name` VARCHAR(50) NOT NULL COMMENT '权限名称',
    `permission_code` VARCHAR(100) NOT NULL COMMENT '权限编码',
    `permission_type` TINYINT(1) DEFAULT 0 COMMENT '权限类型：0-菜单，1-按钮，2-API',
    `parent_id` VARCHAR(36) COMMENT '父权限ID',
    `path` VARCHAR(255) COMMENT '路径',
    `component` VARCHAR(255) COMMENT '组件',
    `icon` VARCHAR(100) COMMENT '图标',
    `method` VARCHAR(10) COMMENT '请求方法',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `status` TINYINT(1) DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by` VARCHAR(36) COMMENT '创建人',
    `create_user_name` VARCHAR(50) COMMENT '创建人姓名',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by` VARCHAR(36) COMMENT '更新人',
    `update_user_name` VARCHAR(50) COMMENT '更新人姓名',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_permission_code` (`permission_code`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_permission_type` (`permission_type`),
    KEY `idx_status` (`status`),
    KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统权限表';

-- =============================================
-- 用户角色关联表
-- =============================================
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
    `id` VARCHAR(36) NOT NULL COMMENT '主键ID',
    `user_id` VARCHAR(36) NOT NULL COMMENT '用户ID',
    `role_id` VARCHAR(36) NOT NULL COMMENT '角色ID',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by` VARCHAR(36) COMMENT '创建人',
    `create_user_name` VARCHAR(50) COMMENT '创建人姓名',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by` VARCHAR(36) COMMENT '更新人',
    `update_user_name` VARCHAR(50) COMMENT '更新人姓名',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- =============================================
-- 角色权限关联表
-- =============================================
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
    `id` VARCHAR(36) NOT NULL COMMENT '主键ID',
    `role_id` VARCHAR(36) NOT NULL COMMENT '角色ID',
    `permission_id` VARCHAR(36) NOT NULL COMMENT '权限ID',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by` VARCHAR(36) COMMENT '创建人',
    `create_user_name` VARCHAR(50) COMMENT '创建人姓名',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by` VARCHAR(36) COMMENT '更新人',
    `update_user_name` VARCHAR(50) COMMENT '更新人姓名',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`),
    KEY `idx_role_id` (`role_id`),
    KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';

-- =============================================
-- 文件表
-- =============================================
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file` (
    `id` VARCHAR(36) NOT NULL COMMENT '主键ID',
    `file_name` VARCHAR(255) NOT NULL COMMENT '文件名',
    `original_name` VARCHAR(255) COMMENT '原始文件名',
    `file_path` VARCHAR(500) NOT NULL COMMENT '文件路径',
    `file_type` VARCHAR(50) COMMENT '文件类型',
    `file_size` BIGINT COMMENT '文件大小（字节）',
    `file_ext` VARCHAR(20) COMMENT '文件扩展名',
    `mime_type` VARCHAR(100) COMMENT 'MIME类型',
    `md5` VARCHAR(32) COMMENT '文件MD5值',
    `upload_ip` VARCHAR(50) COMMENT '上传IP',
    `status` TINYINT(1) DEFAULT 1 COMMENT '状态：0-删除，1-正常',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by` VARCHAR(36) COMMENT '创建人',
    `create_user_name` VARCHAR(50) COMMENT '创建人姓名',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by` VARCHAR(36) COMMENT '更新人',
    `update_user_name` VARCHAR(50) COMMENT '更新人姓名',
    PRIMARY KEY (`id`),
    KEY `idx_file_name` (`file_name`),
    KEY `idx_file_type` (`file_type`),
    KEY `idx_md5` (`md5`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统文件表';

-- =============================================
-- 消息表
-- =============================================
DROP TABLE IF EXISTS `sys_message`;
CREATE TABLE `sys_message` (
    `id` VARCHAR(36) NOT NULL COMMENT '主键ID',
    `title` VARCHAR(255) NOT NULL COMMENT '消息标题',
    `content` TEXT COMMENT '消息内容',
    `message_type` TINYINT(1) DEFAULT 0 COMMENT '消息类型：0-系统消息，1-通知消息，2-提醒消息',
    `sender_id` VARCHAR(36) COMMENT '发送人ID',
    `sender_name` VARCHAR(50) COMMENT '发送人姓名',
    `receiver_id` VARCHAR(36) COMMENT '接收人ID',
    `receiver_name` VARCHAR(50) COMMENT '接收人姓名',
    `is_read` TINYINT(1) DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
    `read_time` TIMESTAMP NULL COMMENT '阅读时间',
    `status` TINYINT(1) DEFAULT 1 COMMENT '状态：0-删除，1-正常',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by` VARCHAR(36) COMMENT '创建人',
    `create_user_name` VARCHAR(50) COMMENT '创建人姓名',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by` VARCHAR(36) COMMENT '更新人',
    `update_user_name` VARCHAR(50) COMMENT '更新人姓名',
    PRIMARY KEY (`id`),
    KEY `idx_sender_id` (`sender_id`),
    KEY `idx_receiver_id` (`receiver_id`),
    KEY `idx_message_type` (`message_type`),
    KEY `idx_is_read` (`is_read`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统消息表';

-- =============================================
-- 系统配置表
-- =============================================
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
    `id` VARCHAR(36) NOT NULL COMMENT '主键ID',
    `config_key` VARCHAR(100) NOT NULL COMMENT '配置键',
    `config_value` TEXT COMMENT '配置值',
    `config_name` VARCHAR(100) COMMENT '配置名称',
    `config_desc` VARCHAR(255) COMMENT '配置描述',
    `config_type` TINYINT(1) DEFAULT 0 COMMENT '配置类型：0-系统配置，1-业务配置',
    `status` TINYINT(1) DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by` VARCHAR(36) COMMENT '创建人',
    `create_user_name` VARCHAR(50) COMMENT '创建人姓名',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by` VARCHAR(36) COMMENT '更新人',
    `update_user_name` VARCHAR(50) COMMENT '更新人姓名',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_key` (`config_key`),
    KEY `idx_config_type` (`config_type`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- =============================================
-- 操作日志表
-- =============================================
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log` (
    `id` VARCHAR(36) NOT NULL COMMENT '主键ID',
    `user_id` VARCHAR(36) COMMENT '用户ID',
    `username` VARCHAR(50) COMMENT '用户名',
    `operation` VARCHAR(100) COMMENT '操作',
    `method` VARCHAR(10) COMMENT '请求方法',
    `params` TEXT COMMENT '请求参数',
    `time` BIGINT COMMENT '执行时长（毫秒）',
    `ip` VARCHAR(50) COMMENT 'IP地址',
    `location` VARCHAR(255) COMMENT '操作地点',
    `user_agent` VARCHAR(500) COMMENT '用户代理',
    `status` TINYINT(1) DEFAULT 1 COMMENT '状态：0-失败，1-成功',
    `error_msg` TEXT COMMENT '错误信息',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_operation` (`operation`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统操作日志表';

-- =============================================
-- 添加外键约束
-- =============================================
-- 用户角色关联表外键
ALTER TABLE `sys_user_role` 
ADD CONSTRAINT `fk_user_role_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE,
ADD CONSTRAINT `fk_user_role_role` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE;

-- 角色权限关联表外键
ALTER TABLE `sys_role_permission` 
ADD CONSTRAINT `fk_role_permission_role` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE,
ADD CONSTRAINT `fk_role_permission_permission` FOREIGN KEY (`permission_id`) REFERENCES `sys_permission` (`id`) ON DELETE CASCADE;

-- 权限表自关联外键
ALTER TABLE `sys_permission` 
ADD CONSTRAINT `fk_permission_parent` FOREIGN KEY (`parent_id`) REFERENCES `sys_permission` (`id`) ON DELETE SET NULL;
