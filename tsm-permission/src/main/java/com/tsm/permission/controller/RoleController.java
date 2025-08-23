package com.tsm.permission.controller;

import com.tsm.common.result.Result;
import org.springframework.web.bind.annotation.*;

/**
 * 角色控制器
 * @author TSM
 */
@RestController
@RequestMapping("/api/role")
public class RoleController {
    
    /**
     * 获取角色列表
     */
    @GetMapping("/list")
    public Result<Object> getRoleList() {
        // TODO: 实现角色列表查询
        return Result.success("角色列表查询成功");
    }
    
    /**
     * 新增角色
     */
    @PostMapping
    public Result<Void> createRole(@RequestBody Object roleDTO) {
        // TODO: 实现角色创建
        return Result.success("角色创建成功");
    }
    
    /**
     * 更新角色
     */
    @PutMapping
    public Result<Void> updateRole(@RequestBody Object roleDTO) {
        // TODO: 实现角色更新
        return Result.success("角色更新成功");
    }
    
    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteRole(@PathVariable Long id) {
        // TODO: 实现角色删除
        return Result.success("角色删除成功");
    }
    
    /**
     * 分配权限
     */
    @PostMapping("/{id}/permissions")
    public Result<Void> assignPermissions(@PathVariable Long id, @RequestBody Object permissionIds) {
        // TODO: 实现权限分配
        return Result.success("权限分配成功");
    }
}