-- TSM系统初始化数据
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