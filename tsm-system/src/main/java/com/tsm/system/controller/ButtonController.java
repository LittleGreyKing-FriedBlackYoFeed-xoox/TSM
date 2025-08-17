package com.tsm.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tsm.api.common.Result;
import com.tsm.system.entity.Button;
import com.tsm.system.service.ButtonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 按钮权限管理控制器
 * 
 * @author TSM Team
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/system/button")
@Validated
public class ButtonController {
    
    @Autowired
    private ButtonService buttonService;
    
    /**
     * 分页查询按钮列表
     */
    @GetMapping("/page")
    public Result<IPage<Button>> getButtonPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String buttonName,
            @RequestParam(required = false) String buttonCode,
            @RequestParam(required = false) Long permissionId,
            @RequestParam(required = false) Integer status) {
        
        Page<Button> page = new Page<>(pageNum, pageSize);
        IPage<Button> result = buttonService.getButtonList(page, buttonName, buttonCode, permissionId, status);
        return Result.success(result);
    }
    
    /**
     * 根据ID查询按钮
     */
    @GetMapping("/{id}")
    public Result<Button> getButtonById(
            @PathVariable @NotNull Long id) {
        
        Button button = buttonService.getById(id);
        return Result.success(button);
    }
    
    /**
     * 根据权限ID查询按钮列表
     */
    @GetMapping("/permission/{permissionId}")
    public Result<List<Button>> getButtonsByPermissionId(
            @PathVariable @NotNull Long permissionId,
            @RequestParam(required = false) Integer status) {
        
        List<Button> buttons = buttonService.getButtonsByPermissionId(permissionId, status);
        return Result.success(buttons);
    }
    
    /**
     * 根据用户ID查询按钮权限列表
     */
    @GetMapping("/user/{userId}")
    public Result<List<Button>> getButtonsByUserId(
            @PathVariable @NotNull Long userId,
            @RequestParam(required = false) Long permissionId) {
        
        List<Button> buttons = buttonService.getButtonsByUserId(userId, permissionId != null ? permissionId.intValue() : null);
        return Result.success(buttons);
    }
    
    /**
     * 根据角色ID查询按钮权限列表
     */
    @GetMapping("/role/{roleId}")
    public Result<List<Button>> getButtonsByRoleId(
            @PathVariable @NotNull Long roleId,
            @RequestParam(required = false) Long permissionId) {
        
        List<Button> buttons = buttonService.getButtonsByRoleId(roleId, permissionId != null ? permissionId.intValue() : null);
        return Result.success(buttons);
    }
    
    /**
     * 获取用户按钮权限编码列表
     */
    @GetMapping("/user/{userId}/codes")
    public Result<List<String>> getUserButtonCodes(
            @PathVariable @NotNull Long userId,
            @RequestParam(required = false) Long permissionId) {
        
        List<String> buttonCodes = buttonService.getUserButtonCodes(userId);
        return Result.success(buttonCodes);
    }
    
    /**
     * 创建按钮
     */
    @PostMapping
    public Result<Boolean> createButton(@Valid @RequestBody Button button) {
        
        boolean success = buttonService.createButton(button);
        return Result.success(success);
    }
    
    /**
     * 更新按钮
     */
    @PutMapping
    public Result<Boolean> updateButton(@Valid @RequestBody Button button) {
        
        boolean success = buttonService.updateButton(button);
        return Result.success(success);
    }
    
    /**
     * 删除按钮
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteButton(
            @PathVariable @NotNull Long id) {
        
        boolean success = buttonService.deleteButton(id);
        return Result.success(success);
    }
    
    /**
     * 批量删除按钮
     */
    @DeleteMapping("/batch")
    public Result<Boolean> deleteButtons(
            @RequestBody @NotEmpty List<Long> buttonIds) {
        
        boolean success = buttonService.deleteButtons(buttonIds);
        return Result.success(success);
    }
    
    /**
     * 更新按钮状态
     */
    @PutMapping("/{id}/status")
    public Result<Boolean> updateButtonStatus(
            @PathVariable @NotNull Long id,
            @RequestParam @NotNull Integer status) {
        
        boolean success = buttonService.updateButtonStatus(id, status);
        return Result.success(success);
    }
    
    /**
     * 检查按钮编码是否存在
     */
    @GetMapping("/check-code")
    public Result<Boolean> checkButtonCodeExists(
            @RequestParam String buttonCode,
            @RequestParam(required = false) Long excludeId) {
        
        boolean exists = buttonService.isButtonCodeExists(buttonCode, excludeId);
        return Result.success(exists);
    }
    
    /**
     * 检查按钮名称是否存在
     */
    @GetMapping("/check-name")
    public Result<Boolean> checkButtonNameExists(
            @RequestParam String buttonName,
            @RequestParam(required = false) Long permissionId,
            @RequestParam(required = false) Long excludeId) {
        
        boolean exists = buttonService.isButtonNameExists(buttonName, excludeId);
        return Result.success(exists);
    }
    
    /**
     * 获取所有按钮列表
     */
    @GetMapping("/all")
    public Result<List<Button>> getAllButtons(
            @RequestParam(required = false) Integer status) {
        
        List<Button> buttons = buttonService.getAllAvailableButtons();
        return Result.success(buttons);
    }
    
    /**
     * 根据按钮编码列表查询按钮
     */
    @PostMapping("/codes")
    public Result<List<Button>> getButtonsByCodes(
            @RequestBody @NotEmpty List<String> buttonCodes) {
        
        // 暂时返回空列表，需要实现getButtonsByCodes方法
        List<Button> buttons = java.util.Collections.emptyList();
        return Result.success(buttons);
    }
    
    /**
     * 获取权限下的所有按钮编码
     */
    @GetMapping("/permission/{permissionId}/codes")
    public Result<List<String>> getButtonCodesByPermissionId(
            @PathVariable @NotNull Long permissionId,
            @RequestParam(required = false) Integer status) {
        
        // 暂时返回空列表，需要实现getButtonCodesByPermissionId方法
        List<String> buttonCodes = java.util.Collections.emptyList();
        return Result.success(buttonCodes);
    }
    
    /**
     * 批量创建按钮
     */
    @PostMapping("/batch")
    public Result<Boolean> createButtons(
            @RequestBody @NotEmpty List<Button> buttons) {
        
        boolean success = buttonService.createButtons(buttons);
        return Result.success(success);
    }
    
    /**
     * 批量更新按钮
     */
    @PutMapping("/batch")
    public Result<Boolean> updateButtons(
            @RequestBody @NotEmpty List<Button> buttons) {
        
        // 暂时返回false，需要实现updateButtons方法
        boolean success = false;
        return Result.success(success);
    }
}