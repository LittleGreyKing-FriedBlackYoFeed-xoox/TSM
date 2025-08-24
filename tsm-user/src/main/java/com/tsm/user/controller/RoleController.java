package com.tsm.user.controller;

import com.tsm.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 角色控制器（用户模块）
 * @author TSM
 */
@RestController
@RequestMapping("/api/role")
public class RoleController {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    /**
     * 获取角色列表
     * @return 角色列表
     */
    @GetMapping("/list")
    public Result<List<Map<String, Object>>> getRoleList() {
        try {
            String sql = "SELECT id, role_name as roleName, role_code as roleCode, description FROM tsm_role WHERE status = 1 ORDER BY id";
            List<Map<String, Object>> roles = jdbcTemplate.queryForList(sql);
            return Result.success(roles);
        } catch (Exception e) {
            return Result.error("获取角色列表失败：" + e.getMessage());
        }
    }
}