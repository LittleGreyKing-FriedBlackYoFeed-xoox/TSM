package com.tsm.auth.service.impl;

import com.tsm.auth.entity.Button;
import com.tsm.auth.entity.User;
import com.tsm.auth.service.AuthService;
import com.tsm.auth.service.UserService;
import com.tsm.common.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 认证服务模拟实现类
 */
@Service
@Primary
public class AuthServiceMockImpl implements AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public String login(String username, String password) {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        if (user.getStatus() != 0) {
            throw new RuntimeException("用户已被禁用");
        }
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        
        return jwtUtils.generateToken(user.getId(), user.getUsername());
    }

    @Override
    public User findByUsername(String username) {
        return userService.getUserByUsername(username);
    }

    @Override
    public boolean hasPermission(Long userId, String permissionCode) {
        // 模拟权限检查，管理员拥有所有权限
        User user = userService.getUserById(userId);
        if (user != null && "admin".equals(user.getUsername())) {
            return true;
        }
        // 其他用户拥有基本权限
        List<String> basicPermissions = Arrays.asList("user:view", "user:create", "user:update", "user:delete");
        return basicPermissions.contains(permissionCode);
    }

    @Override
    public boolean hasButtonPermission(Long userId, String buttonCode) {
        // 模拟按钮权限检查，管理员拥有所有按钮权限
        User user = userService.getUserById(userId);
        if (user != null && "admin".equals(user.getUsername())) {
            return true;
        }
        // 其他用户拥有基本按钮权限
        List<String> basicButtons = Arrays.asList("user_add", "user_edit", "user_delete", "user_reset_password");
        return basicButtons.contains(buttonCode);
    }

    @Override
    public List<String> getUserPermissions(Long userId) {
        User user = userService.getUserById(userId);
        if (user != null && "admin".equals(user.getUsername())) {
            // 管理员拥有所有权限
            return Arrays.asList(
                "user:view", "user:create", "user:update", "user:delete",
                "role:view", "role:create", "role:update", "role:delete",
                "permission:view", "permission:create", "permission:update", "permission:delete",
                "button:view", "button:create", "button:update", "button:delete"
            );
        } else {
            // 普通用户只有基本权限
            return Arrays.asList("user:view", "user:create", "user:update");
        }
    }

    @Override
    public List<String> getUserButtonPermissions(Long userId) {
        User user = userService.getUserById(userId);
        if (user != null && "admin".equals(user.getUsername())) {
            // 管理员拥有所有按钮权限
            return Arrays.asList(
                "user_add", "user_edit", "user_delete", "user_reset_password", "user_batch_delete",
                "role_add", "role_edit", "role_delete",
                "permission_add", "permission_edit", "permission_delete",
                "button_add", "button_edit", "button_delete"
            );
        } else {
            // 普通用户只有基本按钮权限
            return Arrays.asList("user_add", "user_edit", "user_reset_password");
        }
    }

    @Override
    public List<Button> getUserButtonsByPage(Long userId, String pageCode) {
        // 模拟返回用户管理页面的按钮
        List<Button> buttons = new ArrayList<>();
        
        if ("user-management".equals(pageCode)) {
            User user = userService.getUserById(userId);
            if (user != null && "admin".equals(user.getUsername())) {
                // 管理员可以看到所有按钮
                buttons.add(createButton(1L, "user_add", "新增用户", "btn-primary"));
                buttons.add(createButton(2L, "user_edit", "编辑用户", "btn-warning"));
                buttons.add(createButton(3L, "user_delete", "删除用户", "btn-danger"));
                buttons.add(createButton(4L, "user_reset_password", "重置密码", "btn-info"));
                buttons.add(createButton(5L, "user_batch_delete", "批量删除", "btn-danger"));
            } else {
                // 普通用户只能看到部分按钮
                buttons.add(createButton(1L, "user_add", "新增用户", "btn-primary"));
                buttons.add(createButton(2L, "user_edit", "编辑用户", "btn-warning"));
                buttons.add(createButton(4L, "user_reset_password", "重置密码", "btn-info"));
            }
        }
        
        return buttons;
    }

    private Button createButton(Long id, String code, String name, String cssClass) {
        Button button = new Button();
        button.setId(id);
        button.setButtonCode(code);
        button.setButtonName(name);
        button.setButtonStyle(cssClass);
        button.setStatus(0);
        return button;
    }

    @Override
    public boolean assignButtonPermission(Long userId, Long buttonId, Integer permissionType) {
        // 模拟分配按钮权限
        return true;
    }

    @Override
    public boolean batchAssignButtonPermissions(Long userId, List<Long> buttonIds) {
        // 模拟批量分配按钮权限
        return true;
    }

    @Override
    public boolean removeButtonPermission(Long userId, Long buttonId) {
        // 模拟移除按钮权限
        return true;
    }

    @Override
    public boolean clearUserButtonPermissions(Long userId) {
        // 模拟清空用户按钮权限
        return true;
    }
}