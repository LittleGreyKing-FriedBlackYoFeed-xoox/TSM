package com.tsm.user.controller;

import com.tsm.common.result.PageResult;
import com.tsm.common.result.Result;
import com.tsm.user.dto.UserDTO;
import com.tsm.user.dto.UserQueryDTO;
import com.tsm.user.service.UserService;
import com.tsm.user.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户控制器
 * @author TSM
 */
@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * 分页查询用户列表
     * @param queryDTO 查询条件
     * @return 用户分页列表
     */
    @GetMapping("/page")
    public Result<PageResult<UserVO>> getUserPage(@Valid UserQueryDTO queryDTO) {
        try {
            PageResult<UserVO> pageResult = userService.getUserPage(queryDTO);
            return Result.success(pageResult);
        } catch (Exception e) {
            return Result.error("查询用户列表失败：" + e.getMessage());
        }
    }
    
    /**
     * 根据ID查询用户详情
     * @param id 用户ID
     * @return 用户详情
     */
    @GetMapping("/{id}")
    public Result<UserVO> getUserById(@PathVariable @NotNull Long id) {
        try {
            UserVO userVO = userService.getUserById(id);
            if (userVO == null) {
                return Result.error("用户不存在");
            }
            return Result.success(userVO);
        } catch (Exception e) {
            return Result.error("查询用户详情失败：" + e.getMessage());
        }
    }
    
    /**
     * 新增用户
     * @param userDTO 用户信息
     * @return 用户ID
     */
    @PostMapping
    public Result<Long> createUser(@RequestBody @Valid UserDTO userDTO) {
        try {
            Long userId = userService.createUser(userDTO);
            return Result.success("用户创建成功", userId);
        } catch (Exception e) {
            return Result.error("创建用户失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新用户
     * @param userDTO 用户信息
     * @return 是否成功
     */
    @PutMapping
    public Result<Boolean> updateUser(@RequestBody @Valid UserDTO userDTO) {
        try {
            boolean success = userService.updateUser(userDTO);
            return success ? Result.success("用户更新成功", true) : Result.error("用户更新失败");
        } catch (Exception e) {
            return Result.error("更新用户失败：" + e.getMessage());
        }
    }
    
    /**
     * 删除用户
     * @param id 用户ID
     * @return 是否成功
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteUser(@PathVariable @NotNull Long id) {
        try {
            boolean success = userService.deleteUser(id);
            return success ? Result.success("用户删除成功", true) : Result.error("用户删除失败");
        } catch (Exception e) {
            return Result.error("删除用户失败：" + e.getMessage());
        }
    }
    
    /**
     * 批量删除用户
     * @param ids 用户ID列表
     * @return 是否成功
     */
    @DeleteMapping("/batch")
    public Result<Boolean> batchDeleteUsers(@RequestBody @NotEmpty List<Long> ids) {
        try {
            boolean success = userService.batchDeleteUsers(ids);
            return success ? Result.success("批量删除用户成功", true) : Result.error("批量删除用户失败");
        } catch (Exception e) {
            return Result.error("批量删除用户失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新用户状态（启用/禁用）
     * @param id 用户ID
     * @param status 状态：1启用，0禁用
     * @return 是否成功
     */
    @PutMapping("/{id}/status")
    public Result<Boolean> updateUserStatus(@PathVariable @NotNull Long id, 
                                           @RequestParam @NotNull Integer status) {
        try {
            boolean success = userService.updateUserStatus(id, status);
            String message = status == 1 ? "用户启用成功" : "用户禁用成功";
            return success ? Result.success(message, true) : Result.error("更新用户状态失败");
        } catch (Exception e) {
            return Result.error("更新用户状态失败：" + e.getMessage());
        }
    }
    
    /**
     * 冻结用户账号
     * @param id 用户ID
     * @return 是否成功
     */
    @PutMapping("/{id}/freeze")
    public Result<Boolean> freezeUser(@PathVariable @NotNull Long id) {
        try {
            boolean success = userService.updateUserStatus(id, 0);
            return success ? Result.success("用户账号冻结成功", true) : Result.error("冻结用户账号失败");
        } catch (Exception e) {
            return Result.error("冻结用户账号失败：" + e.getMessage());
        }
    }
    
    /**
     * 解冻用户账号
     * @param id 用户ID
     * @return 是否成功
     */
    @PutMapping("/{id}/unfreeze")
    public Result<Boolean> unfreezeUser(@PathVariable @NotNull Long id) {
        try {
            boolean success = userService.updateUserStatus(id, 1);
            return success ? Result.success("用户账号解冻成功", true) : Result.error("解冻用户账号失败");
        } catch (Exception e) {
            return Result.error("解冻用户账号失败：" + e.getMessage());
        }
    }
    
    /**
     * 重置用户密码
     * @param id 用户ID
     * @param newPassword 新密码（可选，默认为123456）
     * @return 是否成功
     */
    @PutMapping("/{id}/reset-password")
    public Result<Boolean> resetPassword(@PathVariable @NotNull Long id, 
                                        @RequestParam(required = false) String newPassword) {
        try {
            boolean success = userService.resetPassword(id, newPassword);
            return success ? Result.success("密码重置成功", true) : Result.error("密码重置失败");
        } catch (Exception e) {
            return Result.error("重置密码失败：" + e.getMessage());
        }
    }
    
    /**
     * 分配用户角色
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 是否成功
     */
    @PutMapping("/{userId}/roles")
    public Result<Boolean> assignUserRoles(@PathVariable @NotNull Long userId, 
                                          @RequestBody List<Long> roleIds) {
        try {
            boolean success = userService.assignUserRoles(userId, roleIds);
            return success ? Result.success("角色分配成功", true) : Result.error("角色分配失败");
        } catch (Exception e) {
            return Result.error("分配角色失败：" + e.getMessage());
        }
    }
    
    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @param excludeId 排除的用户ID（更新时使用）
     * @return 是否存在
     */
    @GetMapping("/check-username")
    public Result<Boolean> checkUsernameExists(@RequestParam String username, 
                                              @RequestParam(required = false) Long excludeId) {
        try {
            boolean exists = userService.checkUsernameExists(username, excludeId);
            return Result.success(exists);
        } catch (Exception e) {
            return Result.error("检查用户名失败：" + e.getMessage());
        }
    }
    
    /**
     * 检查邮箱是否存在
     * @param email 邮箱
     * @param excludeId 排除的用户ID（更新时使用）
     * @return 是否存在
     */
    @GetMapping("/check-email")
    public Result<Boolean> checkEmailExists(@RequestParam String email, 
                                           @RequestParam(required = false) Long excludeId) {
        try {
            boolean exists = userService.checkEmailExists(email, excludeId);
            return Result.success(exists);
        } catch (Exception e) {
            return Result.error("检查邮箱失败：" + e.getMessage());
        }
    }
    
    /**
     * 检查手机号是否存在
     * @param phone 手机号
     * @param excludeId 排除的用户ID（更新时使用）
     * @return 是否存在
     */
    @GetMapping("/check-phone")
    public Result<Boolean> checkPhoneExists(@RequestParam String phone, 
                                           @RequestParam(required = false) Long excludeId) {
        try {
            boolean exists = userService.checkPhoneExists(phone, excludeId);
            return Result.success(exists);
        } catch (Exception e) {
            return Result.error("检查手机号失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取用户统计信息
     * @return 统计信息
     */
    @GetMapping("/statistics")
    public Result<Object> getUserStatistics() {
        try {
            Object statistics = userService.getUserStatistics();
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error("获取用户统计信息失败：" + e.getMessage());
        }
    }
}