package com.tsm.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据库初始化服务
 * @author TSM
 */
@Service
public class DatabaseInitService {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    /**
     * 更新角色配置
     */
    @Transactional
    public void updateRoleConfiguration() {
        try {
            // 首先创建数据库（如果不存在）
            try {
                jdbcTemplate.execute("CREATE DATABASE IF NOT EXISTS TSM DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
                jdbcTemplate.execute("USE TSM");
            } catch (Exception e) {
                System.out.println("数据库创建或切换失败，可能已存在: " + e.getMessage());
            }
            
            // 创建表结构（如果不存在）
            createTablesIfNotExists();
            
            // 删除现有的角色权限关联（如果表存在）
            try {
                jdbcTemplate.update("DELETE FROM tsm_role_permission");
            } catch (Exception e) {
                System.out.println("tsm_role_permission表不存在或删除失败: " + e.getMessage());
            }
            
            // 删除现有的用户角色关联（如果表存在）
            try {
                jdbcTemplate.update("DELETE FROM tsm_user_role");
            } catch (Exception e) {
                System.out.println("tsm_user_role表不存在或删除失败: " + e.getMessage());
            }
            
            // 更新角色数据
            jdbcTemplate.update("UPDATE tsm_role SET role_name = '管理员', role_code = 'ADMIN', description = '系统管理员，拥有系统管理权限' WHERE id = 1");
            jdbcTemplate.update("UPDATE tsm_role SET role_name = '领导', role_code = 'LEADER', description = '领导角色，拥有审批和查看权限' WHERE id = 2");
            jdbcTemplate.update("UPDATE tsm_role SET role_name = '普通用户', role_code = 'USER', description = '普通用户，拥有基础用户权限' WHERE id = 3");
            
            // 为管理员角色分配所有权限
            jdbcTemplate.update("INSERT INTO tsm_role_permission (role_id, permission_id) SELECT 1, id FROM tsm_permission");
            
            // 为领导角色分配审批和查看权限
            String[] leaderPermissions = {
                "INSERT INTO tsm_role_permission (role_id, permission_id) VALUES (2, 1)",  // 系统管理
                "INSERT INTO tsm_role_permission (role_id, permission_id) VALUES (2, 2)",  // 用户管理
                "INSERT INTO tsm_role_permission (role_id, permission_id) VALUES (2, 3)",  // 用户列表
                "INSERT INTO tsm_role_permission (role_id, permission_id) VALUES (2, 9)",  // 权限管理
                "INSERT INTO tsm_role_permission (role_id, permission_id) VALUES (2, 10)", // 角色管理
                "INSERT INTO tsm_role_permission (role_id, permission_id) VALUES (2, 14)", // 权限分配
                "INSERT INTO tsm_role_permission (role_id, permission_id) VALUES (2, 16)", // 个人中心
                "INSERT INTO tsm_role_permission (role_id, permission_id) VALUES (2, 17)", // 修改资料
                "INSERT INTO tsm_role_permission (role_id, permission_id) VALUES (2, 18)"  // 修改密码
            };
            
            for (String sql : leaderPermissions) {
                jdbcTemplate.update(sql);
            }
            
            // 为普通用户角色分配基础权限
            String[] userPermissions = {
                "INSERT INTO tsm_role_permission (role_id, permission_id) VALUES (3, 16)", // 个人中心
                "INSERT INTO tsm_role_permission (role_id, permission_id) VALUES (3, 17)", // 修改资料
                "INSERT INTO tsm_role_permission (role_id, permission_id) VALUES (3, 18)"  // 修改密码
            };
            
            for (String sql : userPermissions) {
                jdbcTemplate.update(sql);
            }
            
            // 重新分配用户角色
            jdbcTemplate.update("INSERT INTO tsm_user_role (user_id, role_id) VALUES (1, 1)"); // admin -> 管理员
            jdbcTemplate.update("INSERT INTO tsm_user_role (user_id, role_id) VALUES (2, 2)"); // manager -> 领导
            jdbcTemplate.update("INSERT INTO tsm_user_role (user_id, role_id) VALUES (3, 3)"); // user1 -> 普通用户
            jdbcTemplate.update("INSERT INTO tsm_user_role (user_id, role_id) VALUES (4, 3)"); // user2 -> 普通用户
            
            System.out.println("角色配置更新成功！");
            
        } catch (Exception e) {
            System.err.println("角色配置更新失败: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * 查询角色信息
     */
    public void queryRoleInfo() {
        try {
            System.out.println("=== 角色信息 ===");
            jdbcTemplate.query("SELECT * FROM tsm_role ORDER BY id", (rs) -> {
                System.out.println(String.format("ID: %d, 角色名: %s, 角色编码: %s, 描述: %s", 
                    rs.getLong("id"), rs.getString("role_name"), 
                    rs.getString("role_code"), rs.getString("description")));
            });
            
            System.out.println("\n=== 角色权限分配 ===");
            jdbcTemplate.query(
                "SELECT r.role_name, p.permission_name " +
                "FROM tsm_role r " +
                "JOIN tsm_role_permission rp ON r.id = rp.role_id " +
                "JOIN tsm_permission p ON rp.permission_id = p.id " +
                "ORDER BY r.id, p.id", 
                (rs) -> {
                    System.out.println(String.format("%s -> %s", 
                        rs.getString("role_name"), rs.getString("permission_name")));
                }
            );
            
        } catch (Exception e) {
            System.err.println("查询角色信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 创建表结构（如果不存在）
     */
    private void createTablesIfNotExists() {
        try {
            // 创建用户表
            jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS tsm_user (" +
                "id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID'," +
                "username VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名'," +
                "password VARCHAR(255) NOT NULL COMMENT '密码'," +
                "email VARCHAR(100) COMMENT '邮箱'," +
                "phone VARCHAR(20) COMMENT '手机号'," +
                "real_name VARCHAR(50) COMMENT '真实姓名'," +
                "status TINYINT DEFAULT 1 COMMENT '状态：1启用，0禁用'," +
                "create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'," +
                "update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'" +
                ") COMMENT='用户表'"
            );
            
            // 创建角色表
            jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS tsm_role (" +
                "id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID'," +
                "role_name VARCHAR(50) NOT NULL COMMENT '角色名称'," +
                "role_code VARCHAR(50) UNIQUE NOT NULL COMMENT '角色编码'," +
                "description VARCHAR(200) COMMENT '角色描述'," +
                "status TINYINT DEFAULT 1 COMMENT '状态：1启用，0禁用'," +
                "create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'," +
                "update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'" +
                ") COMMENT='角色表'"
            );
            
            // 创建权限表
            jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS tsm_permission (" +
                "id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '权限ID'," +
                "permission_name VARCHAR(50) NOT NULL COMMENT '权限名称'," +
                "permission_code VARCHAR(50) UNIQUE NOT NULL COMMENT '权限编码'," +
                "menu_url VARCHAR(200) COMMENT '菜单URL'," +
                "parent_id BIGINT DEFAULT 0 COMMENT '父权限ID'," +
                "permission_type TINYINT DEFAULT 1 COMMENT '权限类型：1菜单，2按钮'," +
                "create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'" +
                ") COMMENT='权限表'"
            );
            
            // 创建用户角色关联表
            jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS tsm_user_role (" +
                "id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID'," +
                "user_id BIGINT NOT NULL COMMENT '用户ID'," +
                "role_id BIGINT NOT NULL COMMENT '角色ID'," +
                "create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'," +
                "UNIQUE KEY uk_user_role (user_id, role_id)" +
                ") COMMENT='用户角色关联表'"
            );
            
            // 创建角色权限关联表
            jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS tsm_role_permission (" +
                "id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID'," +
                "role_id BIGINT NOT NULL COMMENT '角色ID'," +
                "permission_id BIGINT NOT NULL COMMENT '权限ID'," +
                "create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'," +
                "UNIQUE KEY uk_role_permission (role_id, permission_id)" +
                ") COMMENT='角色权限关联表'"
            );
            
            // 初始化基础数据
            initializeBaseData();
            
            System.out.println("数据库表结构创建成功！");
            
        } catch (Exception e) {
            System.err.println("创建表结构失败: " + e.getMessage());
        }
    }
    
    /**
     * 初始化基础数据
     */
    private void initializeBaseData() {
        try {
            // 检查是否已有数据
            Integer userCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM tsm_user", Integer.class);
            if (userCount != null && userCount > 0) {
                System.out.println("基础数据已存在，跳过初始化");
                return;
            }
            
            // 初始化超级管理员用户
            jdbcTemplate.update(
                "INSERT INTO tsm_user (username, password, email, real_name, status) VALUES (?, ?, ?, ?, ?)",
                "admin", "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi", "admin@tsm.com", "超级管理员", 1
            );
            
            // 初始化角色
            jdbcTemplate.update(
                "INSERT INTO tsm_role (role_name, role_code, description) VALUES (?, ?, ?)",
                "管理员", "ADMIN", "系统管理员，拥有系统管理权限"
            );
            jdbcTemplate.update(
                "INSERT INTO tsm_role (role_name, role_code, description) VALUES (?, ?, ?)",
                "领导", "LEADER", "领导角色，拥有审批和查看权限"
            );
            jdbcTemplate.update(
                "INSERT INTO tsm_role (role_name, role_code, description) VALUES (?, ?, ?)",
                "普通用户", "USER", "普通用户，拥有基础用户权限"
            );
            
            // 初始化权限
            String[] permissions = {
                "INSERT INTO tsm_permission (permission_name, permission_code, menu_url, parent_id, permission_type) VALUES ('系统管理', 'SYSTEM_MANAGE', '', 0, 1)",
                "INSERT INTO tsm_permission (permission_name, permission_code, menu_url, parent_id, permission_type) VALUES ('用户管理', 'USER_MANAGE', '/user', 1, 1)",
                "INSERT INTO tsm_permission (permission_name, permission_code, menu_url, parent_id, permission_type) VALUES ('用户列表', 'USER_LIST', '/user/list', 2, 1)",
                "INSERT INTO tsm_permission (permission_name, permission_code, menu_url, parent_id, permission_type) VALUES ('新增用户', 'USER_ADD', '', 3, 2)",
                "INSERT INTO tsm_permission (permission_name, permission_code, menu_url, parent_id, permission_type) VALUES ('编辑用户', 'USER_EDIT', '', 3, 2)",
                "INSERT INTO tsm_permission (permission_name, permission_code, menu_url, parent_id, permission_type) VALUES ('删除用户', 'USER_DELETE', '', 3, 2)",
                "INSERT INTO tsm_permission (permission_name, permission_code, menu_url, parent_id, permission_type) VALUES ('重置密码', 'USER_RESET_PASSWORD', '', 3, 2)",
                "INSERT INTO tsm_permission (permission_name, permission_code, menu_url, parent_id, permission_type) VALUES ('启用禁用', 'USER_STATUS', '', 3, 2)",
                "INSERT INTO tsm_permission (permission_name, permission_code, menu_url, parent_id, permission_type) VALUES ('权限管理', 'PERMISSION_MANAGE', '/permission', 1, 1)",
                "INSERT INTO tsm_permission (permission_name, permission_code, menu_url, parent_id, permission_type) VALUES ('角色管理', 'ROLE_MANAGE', '/permission/role', 9, 1)",
                "INSERT INTO tsm_permission (permission_name, permission_code, menu_url, parent_id, permission_type) VALUES ('个人中心', 'PROFILE', '/profile', 0, 1)"
            };
            
            for (String sql : permissions) {
                jdbcTemplate.update(sql);
            }
            
            System.out.println("基础数据初始化成功！");
            
        } catch (Exception e) {
            System.err.println("初始化基础数据失败: " + e.getMessage());
        }
    }
}