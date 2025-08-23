-- TSM分布式微服务后台管理系统数据库初始化脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS TSM DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE TSM;

-- 用户表 (tsm_user)
CREATE TABLE tsm_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    real_name VARCHAR(50) COMMENT '真实姓名',
    status TINYINT DEFAULT 1 COMMENT '状态：1启用，0禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='用户表';

-- 创建用户表索引
CREATE INDEX idx_user_username ON tsm_user(username);
CREATE INDEX idx_user_status ON tsm_user(status);
CREATE INDEX idx_user_create_time ON tsm_user(create_time DESC);

-- 角色表 (tsm_role)
CREATE TABLE tsm_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
    role_code VARCHAR(50) UNIQUE NOT NULL COMMENT '角色编码',
    description VARCHAR(200) COMMENT '角色描述',
    status TINYINT DEFAULT 1 COMMENT '状态：1启用，0禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='角色表';

-- 创建角色表索引
CREATE INDEX idx_role_code ON tsm_role(role_code);
CREATE INDEX idx_role_status ON tsm_role(status);

-- 权限表 (tsm_permission)
CREATE TABLE tsm_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '权限ID',
    permission_name VARCHAR(50) NOT NULL COMMENT '权限名称',
    permission_code VARCHAR(50) UNIQUE NOT NULL COMMENT '权限编码',
    menu_url VARCHAR(200) COMMENT '菜单URL',
    parent_id BIGINT DEFAULT 0 COMMENT '父权限ID',
    permission_type TINYINT DEFAULT 1 COMMENT '权限类型：1菜单，2按钮',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) COMMENT='权限表';

-- 创建权限表索引
CREATE INDEX idx_permission_code ON tsm_permission(permission_code);
CREATE INDEX idx_permission_parent ON tsm_permission(parent_id);
CREATE INDEX idx_permission_type ON tsm_permission(permission_type);

-- 用户角色关联表 (tsm_user_role)
CREATE TABLE tsm_user_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_user_role (user_id, role_id)
) COMMENT='用户角色关联表';

-- 创建用户角色关联表索引
CREATE INDEX idx_user_role_user ON tsm_user_role(user_id);
CREATE INDEX idx_user_role_role ON tsm_user_role(role_id);

-- 角色权限关联表 (tsm_role_permission)
CREATE TABLE tsm_role_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    permission_id BIGINT NOT NULL COMMENT '权限ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_role_permission (role_id, permission_id)
) COMMENT='角色权限关联表';

-- 创建角色权限关联表索引
CREATE INDEX idx_role_permission_role ON tsm_role_permission(role_id);
CREATE INDEX idx_role_permission_permission ON tsm_role_permission(permission_id);

-- 按钮权限表 (tsm_button_permission)
CREATE TABLE tsm_button_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    permission_id BIGINT NOT NULL COMMENT '权限ID',
    button_code VARCHAR(50) NOT NULL COMMENT '按钮编码',
    button_name VARCHAR(50) NOT NULL COMMENT '按钮名称',
    page_url VARCHAR(200) NOT NULL COMMENT '页面URL',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) COMMENT='按钮权限表';

-- 创建按钮权限表索引
CREATE INDEX idx_button_permission_id ON tsm_button_permission(permission_id);
CREATE INDEX idx_button_code ON tsm_button_permission(button_code);
CREATE INDEX idx_button_page_url ON tsm_button_permission(page_url);

-- 初始化超级管理员用户 (密码: admin123)
INSERT INTO tsm_user (username, password, email, real_name, status) 
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'admin@tsm.com', '超级管理员', 1);

-- 初始化角色
INSERT INTO tsm_role (role_name, role_code, description) VALUES 
('超级管理员', 'SUPER_ADMIN', '拥有所有系统权限'),
('普通管理员', 'ADMIN', '拥有用户管理权限'),
('普通用户', 'USER', '基础用户权限');

-- 初始化权限
INSERT INTO tsm_permission (permission_name, permission_code, menu_url, parent_id, permission_type) VALUES 
('系统管理', 'SYSTEM_MANAGE', '', 0, 1),
('用户管理', 'USER_MANAGE', '/user', 1, 1),
('用户列表', 'USER_LIST', '/user/list', 2, 1),
('新增用户', 'USER_ADD', '', 3, 2),
('编辑用户', 'USER_EDIT', '', 3, 2),
('删除用户', 'USER_DELETE', '', 3, 2),
('重置密码', 'USER_RESET_PASSWORD', '', 3, 2),
('启用禁用', 'USER_STATUS', '', 3, 2),
('权限管理', 'PERMISSION_MANAGE', '/permission', 1, 1),
('角色管理', 'ROLE_MANAGE', '/permission/role', 9, 1),
('新增角色', 'ROLE_ADD', '', 10, 2),
('编辑角色', 'ROLE_EDIT', '', 10, 2),
('删除角色', 'ROLE_DELETE', '', 10, 2),
('权限分配', 'PERMISSION_ASSIGN', '/permission/assign', 9, 1),
('分配权限', 'ASSIGN_PERMISSION', '', 14, 2),
('个人中心', 'PROFILE', '/profile', 0, 1),
('修改资料', 'PROFILE_EDIT', '', 16, 2),
('修改密码', 'PASSWORD_CHANGE', '', 16, 2);

-- 初始化按钮权限
INSERT INTO tsm_button_permission (permission_id, button_code, button_name, page_url) VALUES 
(4, 'btn_user_add', '新增用户', '/user/list'),
(5, 'btn_user_edit', '编辑用户', '/user/list'),
(6, 'btn_user_delete', '删除用户', '/user/list'),
(7, 'btn_user_reset', '重置密码', '/user/list'),
(8, 'btn_user_status', '启用/禁用', '/user/list'),
(11, 'btn_role_add', '新增角色', '/permission/role'),
(12, 'btn_role_edit', '编辑角色', '/permission/role'),
(13, 'btn_role_delete', '删除角色', '/permission/role'),
(15, 'btn_assign_permission', '分配权限', '/permission/assign'),
(17, 'btn_profile_edit', '修改资料', '/profile'),
(18, 'btn_password_change', '修改密码', '/profile');

-- 为超级管理员分配角色
INSERT INTO tsm_user_role (user_id, role_id) VALUES (1, 1);

-- 为超级管理员角色分配所有权限
INSERT INTO tsm_role_permission (role_id, permission_id) 
SELECT 1, id FROM tsm_permission;

-- 为普通管理员角色分配部分权限
INSERT INTO tsm_role_permission (role_id, permission_id) VALUES 
(2, 1), (2, 2), (2, 3), (2, 4), (2, 5), (2, 6), (2, 7), (2, 8), (2, 16), (2, 17), (2, 18);

-- 为普通用户角色分配基础权限
INSERT INTO tsm_role_permission (role_id, permission_id) VALUES 
(3, 16), (3, 17), (3, 18);

-- 创建测试用户
INSERT INTO tsm_user (username, password, email, real_name, status) VALUES 
('manager', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'manager@tsm.com', '管理员', 1),
('user1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'user1@tsm.com', '普通用户1', 1),
('user2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'user2@tsm.com', '普通用户2', 1);

-- 为测试用户分配角色
INSERT INTO tsm_user_role (user_id, role_id) VALUES 
(2, 2), -- manager分配普通管理员角色
(3, 3), -- user1分配普通用户角色
(4, 3); -- user2分配普通用户角色

COMMIT;