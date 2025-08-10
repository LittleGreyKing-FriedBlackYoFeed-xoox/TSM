-- 插入测试用户数据
INSERT INTO sys_user (id, username, password, real_name, email, phone, status, create_by) VALUES
(1, 'admin', '$2a$10$7JB720yubVSOfvVWdBYoOe.PuiKloYAjYcGpVQw8jhZ7rA6VXqTQG', '系统管理员', 'admin@test.com', '13800138000', 1, 1),
(2, 'test', '$2a$10$7JB720yubVSOfvVWdBYoOe.PuiKloYAjYcGpVQw8jhZ7rA6VXqTQG', '测试用户', 'test@test.com', '13800138001', 1, 1);

-- 插入测试角色数据
INSERT INTO sys_role (id, role_name, role_code, description, status, sort_order, create_by) VALUES
(1, '超级管理员', 'SUPER_ADMIN', '系统超级管理员，拥有所有权限', 1, 1, 1),
(2, '普通用户', 'USER', '普通用户，拥有基本权限', 1, 2, 1);

-- 插入测试权限数据
INSERT INTO sys_permission (id, permission_name, permission_code, permission_type, parent_id, path, icon, status, sort_order, create_by) VALUES
(1, '系统管理', 'SYSTEM_MANAGE', 1, 0, '/system', 'layui-icon-set', 1, 1, 1),
(2, '用户管理', 'USER_MANAGE', 1, 1, '/system/user', 'layui-icon-user', 1, 1, 1),
(3, '角色管理', 'ROLE_MANAGE', 1, 1, '/system/role', 'layui-icon-group', 1, 2, 1),
(4, '权限管理', 'PERMISSION_MANAGE', 1, 1, '/system/permission', 'layui-icon-vercode', 1, 3, 1),
(5, '按钮管理', 'BUTTON_MANAGE', 1, 1, '/system/button', 'layui-icon-component', 1, 4, 1);

-- 插入测试按钮数据
INSERT INTO sys_button (id, button_name, button_code, description, page_code, button_type, icon, status, sort_order, create_by) VALUES
(1, '新增用户', 'USER_ADD', '新增用户按钮', 'dashboard', 'primary', 'layui-icon-add-1', 1, 1, 1),
(2, '编辑用户', 'USER_EDIT', '编辑用户按钮', 'dashboard', 'normal', 'layui-icon-edit', 1, 2, 1),
(3, '删除用户', 'USER_DELETE', '删除用户按钮', 'dashboard', 'danger', 'layui-icon-delete', 1, 3, 1),
(4, '查看详情', 'USER_VIEW', '查看用户详情按钮', 'dashboard', 'primary', 'layui-icon-about', 1, 4, 1),
(5, '导出数据', 'DATA_EXPORT', '导出数据按钮', 'dashboard', 'warm', 'layui-icon-export', 1, 5, 1);

-- 插入用户角色关联数据
INSERT INTO sys_user_role (user_id, role_id, create_by) VALUES
(1, 1, 1),
(2, 2, 1);

-- 插入角色权限关联数据
INSERT INTO sys_role_permission (role_id, permission_id, create_by) VALUES
(1, 1, 1),
(1, 2, 1),
(1, 3, 1),
(1, 4, 1),
(1, 5, 1),
(2, 2, 1);

-- 插入用户按钮权限关联数据
INSERT INTO sys_user_button (user_id, button_id, permission_type, effective_time, create_by) VALUES
(1, 1, 1, '2024-01-01 00:00:00', 1),
(1, 2, 1, '2024-01-01 00:00:00', 1),
(1, 3, 1, '2024-01-01 00:00:00', 1),
(1, 4, 1, '2024-01-01 00:00:00', 1),
(1, 5, 1, '2024-01-01 00:00:00', 1),
(2, 1, 1, '2024-01-01 00:00:00', 1),
(2, 4, 1, '2024-01-01 00:00:00', 1);