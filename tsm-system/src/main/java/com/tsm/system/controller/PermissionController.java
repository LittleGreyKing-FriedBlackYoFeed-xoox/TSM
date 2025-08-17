package com.tsm.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tsm.api.common.Result;
import com.tsm.system.entity.Permission;
import com.tsm.system.service.PermissionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 权限管理控制器
 * 
 * @author TSM Team
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/system/permission")
@Validated
public class PermissionController {
    
    @Autowired
    private PermissionService permissionService;
    
    /**
     * 分页查询权限列表
     */
    @GetMapping("/page")
    public Result<IPage<Permission>> getPermissionPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String permissionName,
            @RequestParam(required = false) String permissionCode,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) Integer status) {
        
        Page<Permission> page = new Page<>(pageNum, pageSize);
        IPage<Permission> result = permissionService.getPermissionPage(page, permissionName, permissionCode, type, status);
        return Result.success(result);
    }
    
    /**
     * 查询权限树结构
     */
    @GetMapping("/tree")
    public Result<List<Permission>> getPermissionTree(
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) Integer status) {
        
        List<Permission> tree = permissionService.getPermissionTree(type, status);
        return Result.success(tree);
    }
    
    /**
     * 根据ID查询权限
     */
    @GetMapping("/{id}")
    public Result<Permission> getPermissionById(
            @PathVariable @NotNull Long id) {
        
        Permission permission = permissionService.getById(id);
        return Result.success(permission);
    }
    
    /**
     * 查询子权限列表
     */
    @GetMapping("/{parentId}/children")
    public Result<List<Permission>> getChildPermissions(
            @PathVariable @NotNull Long parentId,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) Integer status) {
        
        List<Permission> children = permissionService.getChildPermissions(parentId, type, status);
        return Result.success(children);
    }
    
    /**
     * 根据角色ID查询权限列表
     */
    @GetMapping("/role/{roleId}")
    public Result<List<Permission>> getPermissionsByRoleId(
            @PathVariable @NotNull Long roleId,
            @RequestParam(required = false) Integer type) {
        
        List<Permission> permissions = permissionService.getPermissionsByRoleId(roleId, type);
        return Result.success(permissions);
    }
    
    /**
     * 根据用户ID查询权限列表
     */
    @GetMapping("/user/{userId}")
    public Result<List<Permission>> getPermissionsByUserId(
            @PathVariable @NotNull Long userId,
            @RequestParam(required = false) Integer type) {
        
        List<Permission> permissions = permissionService.getPermissionsByUserId(userId, type);
        return Result.success(permissions);
    }
    
    /**
     * 获取用户菜单权限树
     */
    @GetMapping("/user/{userId}/menu-tree")
    public Result<List<Permission>> getUserMenuTree(
            @PathVariable @NotNull Long userId) {
        
        List<Permission> menuTree = permissionService.getUserMenuTree(userId);
        return Result.success(menuTree);
    }
    
    /**
     * 获取用户按钮权限列表
     */
    @GetMapping("/user/{userId}/buttons")
    public Result<List<String>> getUserButtonPermissions(
            @PathVariable @NotNull Long userId) {
        
        List<String> buttonCodes = permissionService.getUserButtonPermissions(userId);
        return Result.success(buttonCodes);
    }
    
    /**
     * 创建权限
     */
    @PostMapping
    public Result<Boolean> createPermission(@Valid @RequestBody Permission permission) {
        
        boolean success = permissionService.createPermission(permission);
        return Result.success(success);
    }
    
    /**
     * 更新权限
     */
    @PutMapping
    public Result<Boolean> updatePermission(@Valid @RequestBody Permission permission) {
        
        boolean success = permissionService.updatePermission(permission);
        return Result.success(success);
    }
    
    /**
     * 删除权限
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deletePermission(
            @PathVariable @NotNull Long id) {
        
        boolean success = permissionService.deletePermission(id);
        return Result.success(success);
    }
    
    /**
     * 批量删除权限
     */
    @DeleteMapping("/batch")
    public Result<Boolean> deletePermissions(
            @RequestBody @NotEmpty List<Long> permissionIds) {
        
        boolean success = permissionService.deletePermissions(permissionIds);
        return Result.success(success);
    }
    
    /**
     * 更新权限状态
     */
    @PutMapping("/{id}/status")
    public Result<Boolean> updatePermissionStatus(
            @PathVariable @NotNull Long id,
            @RequestParam @NotNull Integer status) {
        
        boolean success = permissionService.updatePermissionStatus(id, status);
        return Result.success(success);
    }
    
    /**
     * 检查权限编码是否存在
     */
    @GetMapping("/check-code")
    public Result<Boolean> checkPermissionCodeExists(
            @RequestParam String permissionCode,
            @RequestParam(required = false) Long excludeId) {
        
        boolean exists = permissionService.isPermissionCodeExists(permissionCode, excludeId);
        return Result.success(exists);
    }
    
    /**
     * 检查权限名称是否存在
     */
    @GetMapping("/check-name")
    public Result<Boolean> checkPermissionNameExists(
            @RequestParam String permissionName,
            @RequestParam(required = false) Long excludeId) {
        
        boolean exists = permissionService.isPermissionNameExists(permissionName, excludeId);
        return Result.success(exists);
    }
    
    /**
     * 获取所有菜单权限
     */
    @GetMapping("/menus")
    public Result<List<Permission>> getAllMenuPermissions() {
        
        List<Permission> menus = permissionService.getAllMenuPermissions();
        return Result.success(menus);
    }
    
    /**
     * 获取所有按钮权限
     */
    @GetMapping("/buttons")
    public Result<List<Permission>> getAllButtonPermissions() {
        
        List<Permission> buttons = permissionService.getAllButtonPermissions();
        return Result.success(buttons);
    }
    
    /**
     * 根据父权限ID获取按钮权限列表
     */
    @GetMapping("/{parentId}/buttons")
    public Result<List<Permission>> getButtonPermissionsByParentId(
            @PathVariable @NotNull Long parentId) {
        
        List<Permission> buttons = permissionService.getButtonPermissionsByParentId(parentId);
        return Result.success(buttons);
    }
    
    /**
     * 检查是否有子权限
     */
    @GetMapping("/{id}/has-children")
    public Result<Boolean> hasChildPermissions(
            @PathVariable @NotNull Long id) {
        
        boolean hasChildren = permissionService.hasChildPermissions(id);
        return Result.success(hasChildren);
    }
    
    /**
     * 获取所有子权限ID
     */
    @GetMapping("/{id}/all-children-ids")
    public Result<List<Long>> getAllChildPermissionIds(
            @PathVariable @NotNull Long id) {
        
        List<Long> childIds = permissionService.getAllChildPermissionIds(id);
        return Result.success(childIds);
    }
}