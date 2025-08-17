-- TSM后台管理系统数据库初始化脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS `TSM` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `TSM`;

-- 用户表
CREATE TABLE `tsm_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `avatar` varchar(200) DEFAULT NULL COMMENT '头像',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标志：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 角色表
CREATE TABLE `tsm_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(50) NOT NULL COMMENT '角色名称',
  `role_code` varchar(50) NOT NULL COMMENT '角色编码',
  `description` varchar(200) DEFAULT NULL COMMENT '角色描述',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标志：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 权限表（菜单权限）
CREATE TABLE `tsm_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '权限ID',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父权限ID',
  `permission_name` varchar(50) NOT NULL COMMENT '权限名称',
  `permission_code` varchar(100) NOT NULL COMMENT '权限编码',
  `permission_type` tinyint(1) NOT NULL COMMENT '权限类型：1-菜单，2-按钮',
  `menu_url` varchar(200) DEFAULT NULL COMMENT '菜单URL',
  `menu_icon` varchar(50) DEFAULT NULL COMMENT '菜单图标',
  `sort_order` int(11) DEFAULT '0' COMMENT '排序',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标志：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_permission_code` (`permission_code`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_permission_type` (`permission_type`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- 按钮权限表
CREATE TABLE `tsm_button` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '按钮ID',
  `permission_id` bigint(20) NOT NULL COMMENT '所属权限ID',
  `button_name` varchar(50) NOT NULL COMMENT '按钮名称',
  `button_code` varchar(100) NOT NULL COMMENT '按钮编码',
  `button_icon` varchar(50) DEFAULT NULL COMMENT '按钮图标',
  `sort_order` int(11) DEFAULT '0' COMMENT '排序',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标志：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_button_code` (`button_code`),
  KEY `idx_permission_id` (`permission_id`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_button_permission` FOREIGN KEY (`permission_id`) REFERENCES `tsm_permission` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='按钮权限表';

-- 用户角色关联表
CREATE TABLE `tsm_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`,`role_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_role_id` (`role_id`),
  CONSTRAINT `fk_user_role_user` FOREIGN KEY (`user_id`) REFERENCES `tsm_user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_user_role_role` FOREIGN KEY (`role_id`) REFERENCES `tsm_role` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 角色权限关联表
CREATE TABLE `tsm_role_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `permission_id` bigint(20) NOT NULL COMMENT '权限ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_permission` (`role_id`,`permission_id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_permission_id` (`permission_id`),
  CONSTRAINT `fk_role_permission_role` FOREIGN KEY (`role_id`) REFERENCES `tsm_role` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_role_permission_permission` FOREIGN KEY (`permission_id`) REFERENCES `tsm_permission` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- 角色按钮权限关联表
CREATE TABLE `tsm_role_button` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `button_id` bigint(20) NOT NULL COMMENT '按钮ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_button` (`role_id`,`button_id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_button_id` (`button_id`),
  CONSTRAINT `fk_role_button_role` FOREIGN KEY (`role_id`) REFERENCES `tsm_role` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_role_button_button` FOREIGN KEY (`button_id`) REFERENCES `tsm_button` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色按钮权限关联表';

-- 操作日志表
CREATE TABLE `tsm_operation_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '操作用户ID',
  `username` varchar(50) DEFAULT NULL COMMENT '操作用户名',
  `operation` varchar(100) NOT NULL COMMENT '操作内容',
  `method` varchar(200) DEFAULT NULL COMMENT '请求方法',
  `params` text COMMENT '请求参数',
  `ip` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- 初始化数据
-- 插入超级管理员用户（密码：admin123）
INSERT INTO `tsm_user` (`id`, `username`, `password`, `real_name`, `email`, `status`) VALUES
(1, 'admin', '$2a$10$7JB720yubVSOfvVWbazBuOWShWvheWjxVYaGYoUaxMhHOkKVmqQTy', '超级管理员', 'admin@tsm.com', 1);

-- 插入角色
INSERT INTO `tsm_role` (`id`, `role_name`, `role_code`, `description`, `status`) VALUES
(1, '超级管理员', 'SUPER_ADMIN', '系统超级管理员，拥有所有权限', 1),
(2, '普通管理员', 'ADMIN', '普通管理员，拥有部分管理权限', 1),
(3, '普通用户', 'USER', '普通用户，只有基本查看权限', 1);

-- 插入权限（菜单）
INSERT INTO `tsm_permission` (`id`, `parent_id`, `permission_name`, `permission_code`, `permission_type`, `menu_url`, `menu_icon`, `sort_order`, `status`) VALUES
(1, 0, '系统管理', 'system', 1, NULL, 'layui-icon-set', 1, 1),
(2, 1, '用户管理', 'system:user', 1, '/system/user', 'layui-icon-user', 1, 1),
(3, 1, '角色管理', 'system:role', 1, '/system/role', 'layui-icon-group', 2, 1),
(4, 1, '权限管理', 'system:permission', 1, '/system/permission', 'layui-icon-vercode', 3, 1),
(5, 1, '操作日志', 'system:log', 1, '/system/log', 'layui-icon-file', 4, 1);

-- 插入按钮权限
INSERT INTO `tsm_button` (`id`, `permission_id`, `button_name`, `button_code`, `button_icon`, `sort_order`, `status`) VALUES
-- 用户管理按钮
(1, 2, '新增用户', 'system:user:add', 'layui-icon-add-1', 1, 1),
(2, 2, '编辑用户', 'system:user:edit', 'layui-icon-edit', 2, 1),
(3, 2, '删除用户', 'system:user:delete', 'layui-icon-delete', 3, 1),
(4, 2, '重置密码', 'system:user:reset', 'layui-icon-refresh', 4, 1),
-- 角色管理按钮
(5, 3, '新增角色', 'system:role:add', 'layui-icon-add-1', 1, 1),
(6, 3, '编辑角色', 'system:role:edit', 'layui-icon-edit', 2, 1),
(7, 3, '删除角色', 'system:role:delete', 'layui-icon-delete', 3, 1),
(8, 3, '分配权限', 'system:role:assign', 'layui-icon-set', 4, 1),
-- 权限管理按钮
(9, 4, '新增权限', 'system:permission:add', 'layui-icon-add-1', 1, 1),
(10, 4, '编辑权限', 'system:permission:edit', 'layui-icon-edit', 2, 1),
(11, 4, '删除权限', 'system:permission:delete', 'layui-icon-delete', 3, 1);

-- 分配用户角色
INSERT INTO `tsm_user_role` (`user_id`, `role_id`, `create_by`) VALUES
(1, 1, 1);

-- 分配角色权限（超级管理员拥有所有权限）
INSERT INTO `tsm_role_permission` (`role_id`, `permission_id`, `create_by`) VALUES
(1, 1, 1), (1, 2, 1), (1, 3, 1), (1, 4, 1), (1, 5, 1);

-- 分配角色按钮权限（超级管理员拥有所有按钮权限）
INSERT INTO `tsm_role_button` (`role_id`, `button_id`, `create_by`) VALUES
(1, 1, 1), (1, 2, 1), (1, 3, 1), (1, 4, 1), (1, 5, 1), 
(1, 6, 1), (1, 7, 1), (1, 8, 1), (1, 9, 1), (1, 10, 1), (1, 11, 1);

COMMIT;