package com.tsm.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tsm.api.common.Result;
import com.tsm.system.entity.Role;
import com.tsm.system.service.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 角色管理控制器
 *
 * @author TSM
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/system/role")
@Validated
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 分页查询角色列表
     */
    @GetMapping("/list")
    public Result<com.tsm.api.common.PageResult<Role>> getRoleList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String roleName,
            @RequestParam(required = false) String roleCode,
            @RequestParam(required = false) Integer status) {
        
        com.tsm.api.common.PageResult<Role> roleList = roleService.getRolePage(pageNum, pageSize, roleName, roleCode, status);
        return Result.success(roleList);
    }

    /**
     * 根据ID查询角色
     */
    @GetMapping("/{id}")
    public Result<Role> getRoleById(
            @PathVariable @NotNull Long id) {
        
        Role role = roleService.getById(id);
        return Result.success(role);
    }

    /**
     * 查询角色及其权限信息
     */
    @GetMapping("/{id}/permissions")
    public Result<Role> getRoleWithPermissions(
            @PathVariable @NotNull Long id) {
        
        Role role = roleService.getRoleWithPermissions(id);
        return Result.success(role);
    }

    /**
     * 查询角色及其按钮权限信息
     */
    @GetMapping("/{id}/button-permissions")
    public Result<Role> getRoleWithButtonPermissions(
            @PathVariable @NotNull Long id) {
        
        Role role = roleService.getRoleWithButtons(id);
        return Result.success(role);
    }

    /**
     * 根据用户ID查询角色列表
     */
    @GetMapping("/user/{userId}")
    public Result<List<Role>> getRolesByUserId(
            @PathVariable @NotNull Long userId) {
        
        List<Role> roles = roleService.getRolesByUserId(userId);
        return Result.success(roles);
    }

    /**
     * 创建角色
     */
    @PostMapping
    public Result<Boolean> createRole(@Valid @RequestBody Role role) {
        boolean success = roleService.createRole(role);
        return Result.success(success);
    }

    /**
     * 更新角色
     */
    @PutMapping
    public Result<Boolean> updateRole(@Valid @RequestBody Role role) {
        boolean success = roleService.updateRole(role);
        return Result.success(success);
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteRole(
            @PathVariable @NotNull Long id) {
        
        boolean success = roleService.deleteRole(id);
        return Result.success(success);
    }

    /**
     * 批量删除角色
     */
    @DeleteMapping("/batch")
    public Result<Boolean> deleteRoles(
            @RequestBody @NotEmpty List<Long> roleIds) {
        
        boolean success = roleService.deleteBatchRoles(roleIds);
        return Result.success(success);
    }

    /**
     * 更新角色状态
     */
    @PutMapping("/{id}/status")
    public Result<Boolean> updateRoleStatus(
            @PathVariable @NotNull Long id,
            @RequestParam @NotNull Integer status) {
        
        boolean success = roleService.updateRoleStatus(id, status);
        return Result.success(success);
    }

    /**
     * 分配权限给角色
     */
    @PostMapping("/{id}/permissions")
    public Result<Boolean> assignPermissions(
            @PathVariable @NotNull Long id,
            @RequestBody List<Long> permissionIds) {
        
        boolean success = roleService.assignRolePermissions(id, permissionIds);
        return Result.success(success);
    }

    /**
     * 分配按钮权限给角色
     */
    @PostMapping("/{id}/button-permissions")
    public Result<Boolean> assignButtonPermissions(
            @PathVariable @NotNull Long id,
            @RequestBody List<Long> buttonIds) {
        
        boolean success = roleService.assignRoleButtons(id, buttonIds);
        return Result.success(success);
    }

    /**
     * 检查角色编码是否存在
     */
    @GetMapping("/check-code")
    public Result<Boolean> checkRoleCodeExists(
            @RequestParam String roleCode,
            @RequestParam(required = false) Long excludeId) {
        
        boolean exists = roleService.isRoleCodeExists(roleCode, excludeId);
        return Result.success(exists);
    }

    /**
     * 检查角色名称是否存在
     */
    @GetMapping("/check-name")
    public Result<Boolean> checkRoleNameExists(
            @RequestParam String roleName,
            @RequestParam(required = false) Long excludeId) {
        
        boolean exists = roleService.isRoleNameExists(roleName, excludeId);
        return Result.success(exists);
    }

    /**
     * 获取所有可用角色
     */
    @GetMapping("/available")
    public Result<List<Role>> getAllAvailableRoles() {
        List<Role> roles = roleService.getAllAvailableRoles();
        return Result.success(roles);
    }

    /**
     * 检查角色是否被用户使用
     */
    @GetMapping("/{id}/used")
    public Result<Boolean> checkRoleUsed(
            @PathVariable @NotNull Long id) {
        
        boolean used = roleService.isRoleInUse(id);
        return Result.success(used);
    }
}