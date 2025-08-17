package com.tsm.system.controller;

import com.tsm.api.common.PageResult;
import com.tsm.api.common.Result;
import com.tsm.system.entity.User;
import com.tsm.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户管理控制器
 * 
 * @author TSM Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/system/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 分页查询用户列表
     */
    @GetMapping("/page")
    public Result<PageResult<User>> getUserPage(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String realName,
            @RequestParam(required = false) Integer status) {
        
        PageResult<User> pageResult = userService.getUserPage(current, size, username, realName, status);
        return Result.success(pageResult);
    }

    /**
     * 根据ID查询用户详情
     */
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable @NotNull Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        // 清空密码字段
        user.setPassword(null);
        return Result.success(user);
    }

    /**
     * 查询用户及其角色信息
     */
    @GetMapping("/{id}/roles")
    public Result<User> getUserWithRoles(@PathVariable @NotNull Long id) {
        User user = userService.getUserWithRoles(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        // 清空密码字段
        user.setPassword(null);
        return Result.success(user);
    }

    /**
     * 创建用户
     */
    @PostMapping
    public Result<Void> createUser(@RequestBody @Valid User user) {
        try {
            boolean success = userService.createUser(user);
            if (success) {
                return Result.success();
            } else {
                return Result.error("创建用户失败");
            }
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    public Result<Void> updateUser(@PathVariable @NotNull Long id, @RequestBody @Valid User user) {
        user.setId(id);
        try {
            boolean success = userService.updateUser(user);
            if (success) {
                return Result.success();
            } else {
                return Result.error("更新用户失败");
            }
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable @NotNull Long id) {
        boolean success = userService.deleteUser(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("删除用户失败");
        }
    }

    /**
     * 批量删除用户
     */
    @DeleteMapping("/batch")
    public Result<Void> deleteBatchUsers(@RequestBody @NotEmpty List<Long> userIds) {
        boolean success = userService.deleteBatchUsers(userIds);
        if (success) {
            return Result.success();
        } else {
            return Result.error("批量删除用户失败");
        }
    }

    /**
     * 重置用户密码
     */
    @PutMapping("/{id}/reset-password")
    public Result<Void> resetPassword(@PathVariable @NotNull Long id, @RequestParam String newPassword) {
        boolean success = userService.resetPassword(id, newPassword);
        if (success) {
            return Result.success();
        } else {
            return Result.error("重置密码失败");
        }
    }

    /**
     * 更新用户状态
     */
    @PutMapping("/{id}/status")
    public Result<Void> updateUserStatus(@PathVariable @NotNull Long id, @RequestParam Integer status) {
        boolean success = userService.updateUserStatus(id, status);
        if (success) {
            return Result.success();
        } else {
            return Result.error("更新用户状态失败");
        }
    }

    /**
     * 分配用户角色
     */
    @PutMapping("/{id}/roles")
    public Result<Void> assignUserRoles(@PathVariable @NotNull Long id, @RequestBody List<Long> roleIds) {
        boolean success = userService.assignUserRoles(id, roleIds);
        if (success) {
            return Result.success();
        } else {
            return Result.error("分配用户角色失败");
        }
    }

    /**
     * 检查用户名是否存在
     */
    @GetMapping("/check-username")
    public Result<Boolean> checkUsername(@RequestParam String username, @RequestParam(required = false) Long excludeId) {
        boolean exists = userService.isUsernameExists(username, excludeId);
        return Result.success(exists);
    }

    /**
     * 检查邮箱是否存在
     */
    @GetMapping("/check-email")
    public Result<Boolean> checkEmail(@RequestParam String email, @RequestParam(required = false) Long excludeId) {
        boolean exists = userService.isEmailExists(email, excludeId);
        return Result.success(exists);
    }

    /**
     * 检查手机号是否存在
     */
    @GetMapping("/check-phone")
    public Result<Boolean> checkPhone(@RequestParam String phone, @RequestParam(required = false) Long excludeId) {
        boolean exists = userService.isPhoneExists(phone, excludeId);
        return Result.success(exists);
    }

    /**
     * 修改密码
     */
    @PutMapping("/{id}/change-password")
    public Result<Void> changePassword(
            @PathVariable @NotNull Long id,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        try {
            boolean success = userService.changePassword(id, oldPassword, newPassword);
            if (success) {
                return Result.success();
            } else {
                return Result.error("修改密码失败");
            }
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取用户权限列表
     */
    @GetMapping("/{id}/permissions")
    public Result<List<String>> getUserPermissions(@PathVariable @NotNull Long id) {
        List<String> permissions = userService.getUserPermissions(id);
        return Result.success(permissions);
    }

    /**
     * 获取用户按钮权限列表
     */
    @GetMapping("/{id}/button-permissions")
    public Result<List<String>> getUserButtonPermissions(@PathVariable @NotNull Long id) {
        List<String> buttonPermissions = userService.getUserButtonPermissions(id);
        return Result.success(buttonPermissions);
    }
}