package com.tsm.user.controller;

import com.tsm.user.service.DatabaseInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据库管理控制器
 * @author TSM
 */
@RestController
@RequestMapping("/api/database")
public class DatabaseController {
    
    @Autowired
    private DatabaseInitService databaseInitService;
    
    /**
     * 更新角色配置
     */
    @PostMapping("/update-roles")
    public String updateRoles() {
        try {
            databaseInitService.updateRoleConfiguration();
            return "角色配置更新成功！";
        } catch (Exception e) {
            return "角色配置更新失败: " + e.getMessage();
        }
    }
    
    /**
     * 查询角色信息
     */
    @GetMapping("/roles-info")
    public String getRolesInfo() {
        try {
            databaseInitService.queryRoleInfo();
            return "角色信息已输出到控制台";
        } catch (Exception e) {
            return "查询角色信息失败: " + e.getMessage();
        }
    }
}