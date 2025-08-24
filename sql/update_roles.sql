-- 更新角色配置脚本
-- 将角色体系调整为：管理员、领导、普通用户

USE TSM;

-- 删除现有的角色权限关联
DELETE FROM tsm_role_permission;

-- 删除现有的用户角色关联
DELETE FROM tsm_user_role;

-- 更新角色数据
UPDATE tsm_role SET 
    role_name = '管理员',
    role_code = 'ADMIN',
    description = '系统管理员，拥有系统管理权限'
WHERE id = 1;

UPDATE tsm_role SET 
    role_name = '领导',
    role_code = 'LEADER',
    description = '领导角色，拥有审批和查看权限'
WHERE id = 2;

UPDATE tsm_role SET 
    role_name = '普通用户',
    role_code = 'USER',
    description = '普通用户，拥有基础用户权限'
WHERE id = 3;

-- 为管理员角色分配所有权限
INSERT INTO tsm_role_permission (role_id, permission_id) 
SELECT 1, id FROM tsm_permission;

-- 为领导角色分配审批和查看权限
INSERT INTO tsm_role_permission (role_id, permission_id) VALUES 
(2, 1),  -- 系统管理
(2, 2),  -- 用户管理
(2, 3),  -- 用户列表
(2, 9),  -- 权限管理
(2, 10), -- 角色管理
(2, 14), -- 权限分配
(2, 16), -- 个人中心
(2, 17), -- 修改资料
(2, 18); -- 修改密码

-- 为普通用户角色分配基础权限
INSERT INTO tsm_role_permission (role_id, permission_id) VALUES 
(3, 16), -- 个人中心
(3, 17), -- 修改资料
(3, 18); -- 修改密码

-- 重新分配用户角色
-- admin用户分配管理员角色
INSERT INTO tsm_user_role (user_id, role_id) VALUES (1, 1);

-- manager用户分配领导角色
INSERT INTO tsm_user_role (user_id, role_id) VALUES (2, 2);

-- 其他用户分配普通用户角色
INSERT INTO tsm_user_role (user_id, role_id) VALUES 
(3, 3), -- user1
(4, 3); -- user2

COMMIT;

-- 查询更新后的角色信息
SELECT * FROM tsm_role ORDER BY id;

-- 查询角色权限分配情况
SELECT r.role_name, p.permission_name 
FROM tsm_role r 
JOIN tsm_role_permission rp ON r.id = rp.role_id 
JOIN tsm_permission p ON rp.permission_id = p.id 
ORDER BY r.id, p.id;