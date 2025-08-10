package com.tsm.auth.service.impl;

import com.tsm.auth.entity.Button;
import com.tsm.auth.entity.User;
import com.tsm.auth.service.AuthService;
import com.tsm.auth.service.UserService;
import com.tsm.common.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 认证服务实现类
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserService userService;

    // 暂时注释掉数据库依赖
    // @Autowired
    // private ButtonMapper buttonMapper;

    // @Autowired
    // private UserButtonMapper userButtonMapper;

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
        // 模拟权限检查，实际应该从数据库查询
        return true; // 暂时返回true，表示有权限
    }

    @Override
    public boolean hasButtonPermission(Long userId, String buttonCode) {
        // 模拟按钮权限检查
        return true; // 暂时返回true，表示有权限
    }

    @Override
    public List<String> getUserPermissions(Long userId) {
        // 模拟返回权限列表
        return List.of("user:read", "user:write");
    }

    @Override
    public List<String> getUserButtonPermissions(Long userId) {
        // 模拟返回按钮权限列表
        return List.of("btn:add", "btn:edit", "btn:delete");
    }

    @Override
    public List<Button> getUserButtonsByPage(Long userId, String pageCode) {
        // 模拟返回按钮列表
        return List.of(
            createButton(1L, "btn:add", "新增", "btn-primary"),
            createButton(2L, "btn:edit", "编辑", "btn-warning"),
            createButton(3L, "btn:delete", "删除", "btn-danger")
        );
    }

    @Override
    public boolean assignButtonPermission(Long userId, Long buttonId, Integer permissionType) {
        // 模拟权限分配成功
        return true;
    }

    @Override
    public boolean batchAssignButtonPermissions(Long userId, List<Long> buttonIds) {
        // 模拟批量权限分配成功
        return true;
    }

    @Override
    public boolean removeButtonPermission(Long userId, Long buttonId) {
        // 模拟权限移除成功
        return true;
    }

    @Override
    public boolean clearUserButtonPermissions(Long userId) {
        // 模拟清空权限成功
        return true;
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
}