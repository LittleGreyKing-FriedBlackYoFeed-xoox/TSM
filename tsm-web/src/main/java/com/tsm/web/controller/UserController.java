package com.tsm.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tsm.auth.entity.User;
import com.tsm.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 分页查询用户列表
     */
    @GetMapping("/list")
    public Map<String, Object> getUserList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String realName,
            @RequestParam(required = false) Integer status) {
        
        Map<String, Object> result = new HashMap<>();
        try {
            IPage<User> userPage = userService.getUserList(page, size, username, realName, status);
            
            result.put("code", 0);
            result.put("msg", "查询成功");
            result.put("count", userPage.getTotal());
            result.put("data", userPage.getRecords());
        } catch (Exception e) {
            result.put("code", 1);
            result.put("msg", "查询失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 根据ID获取用户信息
     */
    @GetMapping("/{id}")
    public Map<String, Object> getUserById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            User user = userService.getUserById(id);
            if (user != null) {
                // 不返回密码字段
                user.setPassword(null);
                result.put("code", 0);
                result.put("msg", "查询成功");
                result.put("data", user);
            } else {
                result.put("code", 1);
                result.put("msg", "用户不存在");
            }
        } catch (Exception e) {
            result.put("code", 1);
            result.put("msg", "查询失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 创建用户
     */
    @PostMapping("/create")
    public Map<String, Object> createUser(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        try {
            User createdUser = userService.createUser(user);
            if (createdUser != null) {
                // 不返回密码字段
                createdUser.setPassword(null);
                result.put("code", 0);
                result.put("msg", "创建用户成功");
                result.put("data", createdUser);
            } else {
                result.put("code", 1);
                result.put("msg", "创建用户失败");
            }
        } catch (Exception e) {
            result.put("code", 1);
            result.put("msg", e.getMessage());
        }
        
        return result;
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/update")
    public Map<String, Object> updateUser(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        try {
            User updatedUser = userService.updateUser(user);
            if (updatedUser != null) {
                // 不返回密码字段
                updatedUser.setPassword(null);
                result.put("code", 0);
                result.put("msg", "更新用户成功");
                result.put("data", updatedUser);
            } else {
                result.put("code", 1);
                result.put("msg", "更新用户失败");
            }
        } catch (Exception e) {
            result.put("code", 1);
            result.put("msg", e.getMessage());
        }
        
        return result;
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteUser(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = userService.deleteUser(id);
            if (success) {
                result.put("code", 0);
                result.put("msg", "删除用户成功");
            } else {
                result.put("code", 1);
                result.put("msg", "删除用户失败");
            }
        } catch (Exception e) {
            result.put("code", 1);
            result.put("msg", e.getMessage());
        }
        
        return result;
    }

    /**
     * 批量删除用户
     */
    @DeleteMapping("/batch")
    public Map<String, Object> batchDeleteUsers(@RequestBody List<Long> ids) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = userService.batchDeleteUsers(ids);
            if (success) {
                result.put("code", 0);
                result.put("msg", "批量删除用户成功");
            } else {
                result.put("code", 1);
                result.put("msg", "批量删除用户失败");
            }
        } catch (Exception e) {
            result.put("code", 1);
            result.put("msg", e.getMessage());
        }
        
        return result;
    }

    /**
     * 启用/禁用用户
     */
    @PutMapping("/{id}/status")
    public Map<String, Object> updateUserStatus(@PathVariable Long id, @RequestBody Map<String, Integer> request) {
        Map<String, Object> result = new HashMap<>();
        try {
            Integer status = request.get("status");
            boolean success = userService.updateUserStatus(id, status);
            if (success) {
                result.put("code", 0);
                result.put("msg", "更新用户状态成功");
            } else {
                result.put("code", 1);
                result.put("msg", "更新用户状态失败");
            }
        } catch (Exception e) {
            result.put("code", 1);
            result.put("msg", e.getMessage());
        }
        
        return result;
    }

    /**
     * 重置用户密码
     */
    @PutMapping("/{id}/reset-password")
    public Map<String, Object> resetPassword(@PathVariable Long id, @RequestBody Map<String, String> request) {
        Map<String, Object> result = new HashMap<>();
        try {
            String newPassword = request.get("newPassword");
            if (newPassword == null || newPassword.trim().isEmpty()) {
                newPassword = "123456"; // 默认密码
            }
            
            boolean success = userService.resetPassword(id, newPassword);
            if (success) {
                result.put("code", 0);
                result.put("msg", "重置密码成功");
            } else {
                result.put("code", 1);
                result.put("msg", "重置密码失败");
            }
        } catch (Exception e) {
            result.put("code", 1);
            result.put("msg", e.getMessage());
        }
        
        return result;
    }

    /**
     * 修改用户密码
     */
    @PutMapping("/{id}/change-password")
    public Map<String, Object> changePassword(@PathVariable Long id, @RequestBody Map<String, String> request) {
        Map<String, Object> result = new HashMap<>();
        try {
            String oldPassword = request.get("oldPassword");
            String newPassword = request.get("newPassword");
            
            boolean success = userService.changePassword(id, oldPassword, newPassword);
            if (success) {
                result.put("code", 0);
                result.put("msg", "修改密码成功");
            } else {
                result.put("code", 1);
                result.put("msg", "修改密码失败");
            }
        } catch (Exception e) {
            result.put("code", 1);
            result.put("msg", e.getMessage());
        }
        
        return result;
    }

    /**
     * 检查用户名是否存在
     */
    @GetMapping("/check-username")
    public Map<String, Object> checkUsername(@RequestParam String username) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean exists = userService.isUsernameExists(username);
            result.put("code", 0);
            result.put("msg", "查询成功");
            result.put("exists", exists);
        } catch (Exception e) {
            result.put("code", 1);
            result.put("msg", "查询失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 检查邮箱是否存在
     */
    @GetMapping("/check-email")
    public Map<String, Object> checkEmail(@RequestParam String email) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean exists = userService.isEmailExists(email);
            result.put("code", 0);
            result.put("msg", "查询成功");
            result.put("exists", exists);
        } catch (Exception e) {
            result.put("code", 1);
            result.put("msg", "查询失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 检查手机号是否存在
     */
    @GetMapping("/check-phone")
    public Map<String, Object> checkPhone(@RequestParam String phone) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean exists = userService.isPhoneExists(phone);
            result.put("code", 0);
            result.put("msg", "查询成功");
            result.put("exists", exists);
        } catch (Exception e) {
            result.put("code", 1);
            result.put("msg", "查询失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 获取所有用户列表（不分页）
     */
    @GetMapping("/all")
    public Map<String, Object> getAllUsers() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<User> users = userService.getAllUsers();
            // 不返回密码字段
            users.forEach(user -> user.setPassword(null));
            
            result.put("code", 0);
            result.put("msg", "查询成功");
            result.put("data", users);
        } catch (Exception e) {
            result.put("code", 1);
            result.put("msg", "查询失败：" + e.getMessage());
        }
        
        return result;
    }
}