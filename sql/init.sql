-- TSM数据库初始化脚本

-- 创建数据库
CREATE DATABASE IF NOT EXISTS TSM DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE TSM;

-- 用户表
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `avatar` varchar(200) DEFAULT NULL COMMENT '头像',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人ID',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 角色表
CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(50) NOT NULL COMMENT '角色名称',
  `role_code` varchar(50) NOT NULL COMMENT '角色编码',
  `description` varchar(200) DEFAULT NULL COMMENT '角色描述',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `sort_order` int(11) DEFAULT '0' COMMENT '排序',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人ID',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 权限表
CREATE TABLE `sys_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '权限ID',
  `permission_name` varchar(50) NOT NULL COMMENT '权限名称',
  `permission_code` varchar(50) NOT NULL COMMENT '权限编码',
  `permission_type` tinyint(1) DEFAULT '1' COMMENT '权限类型（1菜单 2按钮）',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父权限ID',
  `path` varchar(200) DEFAULT NULL COMMENT '路径',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标',
  `component` varchar(200) DEFAULT NULL COMMENT '组件',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `sort_order` int(11) DEFAULT '0' COMMENT '排序',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人ID',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_permission_code` (`permission_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- 按钮表
CREATE TABLE `sys_button` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '按钮ID',
  `button_name` varchar(50) NOT NULL COMMENT '按钮名称',
  `button_code` varchar(50) NOT NULL COMMENT '按钮编码',
  `description` varchar(200) DEFAULT NULL COMMENT '按钮描述',
  `page_code` varchar(50) NOT NULL COMMENT '所属页面/模块',
  `button_type` tinyint(1) DEFAULT '7' COMMENT '按钮类型（1新增 2编辑 3删除 4查看 5导出 6导入 7其他）',
  `button_style` varchar(50) DEFAULT NULL COMMENT '按钮样式',
  `icon` varchar(50) DEFAULT NULL COMMENT '按钮图标',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `sort_order` int(11) DEFAULT '0' COMMENT '排序',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人ID',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_button_code` (`button_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='按钮表';

-- 用户角色关联表
CREATE TABLE `sys_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人ID',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 角色权限关联表
CREATE TABLE `sys_role_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `permission_id` bigint(20) NOT NULL COMMENT '权限ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人ID',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_permission` (`role_id`,`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- 用户按钮权限关联表
CREATE TABLE `sys_user_button` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `button_id` bigint(20) NOT NULL COMMENT '按钮ID',
  `permission_type` tinyint(1) DEFAULT '1' COMMENT '权限类型（1允许 0禁止）',
  `effective_time` datetime DEFAULT NULL COMMENT '生效时间',
  `expiry_time` datetime DEFAULT NULL COMMENT '失效时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人ID',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`id`),
  KEY `idx_user_button` (`user_id`,`button_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户按钮权限关联表';

-- 插入初始数据

-- 插入管理员用户（密码：123456，已加密）
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `email`, `phone`, `status`, `create_by`) VALUES
('admin', '$2a$10$7JB720yubVSOfvVWbfXCNOaWpDDvNVjpBYJvuiueu2J/.8cBFcmO2', '系统管理员', 'admin@tsm.com', '13800138000', 0, 1),
('test', '$2a$10$7JB720yubVSOfvVWbfXCNOaWpDDvNVjpBYJvuiueu2J/.8cBFcmO2', '测试用户', 'test@tsm.com', '13800138001', 0, 1);

-- 插入角色
INSERT INTO `sys_role` (`role_name`, `role_code`, `description`, `status`, `sort_order`, `create_by`) VALUES
('超级管理员', 'SUPER_ADMIN', '系统超级管理员，拥有所有权限', 0, 1, 1),
('普通用户', 'USER', '普通用户，拥有基本权限', 0, 2, 1);

-- 插入权限
INSERT INTO `sys_permission` (`permission_name`, `permission_code`, `permission_type`, `parent_id`, `path`, `icon`, `status`, `sort_order`, `create_by`) VALUES
('系统管理', 'SYSTEM', 1, 0, '/system', 'layui-icon-set', 0, 1, 1),
('用户管理', 'USER_MANAGE', 1, 1, '/user', 'layui-icon-user', 0, 1, 1),
('角色管理', 'ROLE_MANAGE', 1, 1, '/role', 'layui-icon-group', 0, 2, 1),
('权限管理', 'PERMISSION_MANAGE', 1, 1, '/permission', 'layui-icon-vercode', 0, 3, 1),
('按钮管理', 'BUTTON_MANAGE', 1, 1, '/button', 'layui-icon-component', 0, 4, 1);

-- 插入按钮
INSERT INTO `sys_button` (`button_name`, `button_code`, `description`, `page_code`, `button_type`, `icon`, `status`, `sort_order`, `create_by`) VALUES
('新增用户', 'USER_ADD', '新增用户按钮', 'dashboard', 1, 'layui-icon-add-1', 0, 1, 1),
('编辑用户', 'USER_EDIT', '编辑用户按钮', 'dashboard', 2, 'layui-icon-edit', 0, 2, 1),
('删除用户', 'USER_DELETE', '删除用户按钮', 'dashboard', 3, 'layui-icon-delete', 0, 3, 1),
('查看用户', 'USER_VIEW', '查看用户按钮', 'dashboard', 4, 'layui-icon-search', 0, 4, 1),
('导出数据', 'DATA_EXPORT', '导出数据按钮', 'dashboard', 5, 'layui-icon-export', 0, 5, 1),
('导入数据', 'DATA_IMPORT', '导入数据按钮', 'dashboard', 6, 'layui-icon-upload', 0, 6, 1),
('权限分配', 'PERMISSION_ASSIGN', '权限分配按钮', 'dashboard', 7, 'layui-icon-set-sm', 0, 7, 1);

-- 插入用户角色关联
INSERT INTO `sys_user_role` (`user_id`, `role_id`, `create_by`) VALUES
(1, 1, 1),  -- admin用户分配超级管理员角色
(2, 2, 1);  -- test用户分配普通用户角色

-- 插入角色权限关联
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`, `create_by`) VALUES
(1, 1, 1),  -- 超级管理员拥有系统管理权限
(1, 2, 1),  -- 超级管理员拥有用户管理权限
(1, 3, 1),  -- 超级管理员拥有角色管理权限
(1, 4, 1),  -- 超级管理员拥有权限管理权限
(1, 5, 1),  -- 超级管理员拥有按钮管理权限
(2, 2, 1);  -- 普通用户拥有用户管理权限

-- 插入用户按钮权限关联（演示按钮级权限控制）
INSERT INTO `sys_user_button` (`user_id`, `button_id`, `permission_type`, `effective_time`, `create_by`) VALUES
-- admin用户拥有所有按钮权限
(1, 1, 1, NOW(), 1),  -- 新增用户
(1, 2, 1, NOW(), 1),  -- 编辑用户
(1, 3, 1, NOW(), 1),  -- 删除用户
(1, 4, 1, NOW(), 1),  -- 查看用户
(1, 5, 1, NOW(), 1),  -- 导出数据
(1, 6, 1, NOW(), 1),  -- 导入数据
(1, 7, 1, NOW(), 1),  -- 权限分配

-- test用户只有部分按钮权限
(2, 4, 1, NOW(), 1),  -- 查看用户
(2, 5, 1, NOW(), 1);  -- 导出数据

-- 创建索引
CREATE INDEX idx_user_username ON sys_user(username);
CREATE INDEX idx_user_status ON sys_user(status);
CREATE INDEX idx_role_code ON sys_role(role_code);
CREATE INDEX idx_permission_code ON sys_permission(permission_code);
CREATE INDEX idx_button_code ON sys_button(button_code);
CREATE INDEX idx_button_page ON sys_button(page_code);
CREATE INDEX idx_user_role_user ON sys_user_role(user_id);
CREATE INDEX idx_user_role_role ON sys_user_role(role_id);
CREATE INDEX idx_role_permission_role ON sys_role_permission(role_id);
CREATE INDEX idx_role_permission_permission ON sys_role_permission(permission_id);
CREATE INDEX idx_user_button_user ON sys_user_button(user_id);
CREATE INDEX idx_user_button_button ON sys_user_button(button_id);