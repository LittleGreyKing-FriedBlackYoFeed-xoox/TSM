package com.tsm.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tsm.auth.entity.Button;
import com.tsm.auth.entity.User;
import com.tsm.auth.entity.UserButton;
import com.tsm.auth.mapper.ButtonMapper;
import com.tsm.auth.mapper.UserButtonMapper;
import com.tsm.auth.mapper.UserMapper;
import com.tsm.auth.service.AuthService;
import com.tsm.common.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 认证服务实现类
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ButtonMapper buttonMapper;

    @Autowired
    private UserButtonMapper userButtonMapper;

    @Autowired
    private JwtUtils jwtUtils;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public String login(String username, String password) {
        User user = userMapper.findByUsername(username);
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
        return userMapper.findByUsername(username);
    }

    @Override
    public boolean hasPermission(Long userId, String permissionCode) {
        List<String> permissions = userMapper.findUserPermissions(userId);
        return permissions.contains(permissionCode);
    }

    @Override
    public boolean hasButtonPermission(Long userId, String buttonCode) {
        return userButtonMapper.checkUserButtonPermission(userId, buttonCode) > 0;
    }

    @Override
    public List<String> getUserPermissions(Long userId) {
        return userMapper.findUserPermissions(userId);
    }

    @Override
    public List<String> getUserButtonPermissions(Long userId) {
        return userMapper.findUserButtonPermissions(userId);
    }

    @Override
    public List<Button> getUserButtonsByPage(Long userId, String pageCode) {
        return buttonMapper.findUserButtonsByPage(userId, pageCode);
    }

    @Override
    @Transactional
    public boolean assignButtonPermission(Long userId, Long buttonId, Integer permissionType) {
        try {
            // 检查是否已存在权限记录
            UserButton existingPermission = userButtonMapper.findByUserIdAndButtonId(userId, buttonId);
            
            if (existingPermission != null) {
                // 更新现有权限
                existingPermission.setPermissionType(permissionType);
                existingPermission.setUpdateTime(LocalDateTime.now());
                userButtonMapper.updateById(existingPermission);
            } else {
                // 创建新权限记录
                UserButton userButton = new UserButton();
                userButton.setUserId(userId);
                userButton.setButtonId(buttonId);
                userButton.setPermissionType(permissionType);
                userButton.setEffectiveTime(LocalDateTime.now());
                userButton.setCreateTime(LocalDateTime.now());
                userButton.setDeleted(0);
                userButtonMapper.insert(userButton);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean batchAssignButtonPermissions(Long userId, List<Long> buttonIds) {
        try {
            // 先清空用户现有的按钮权限
            clearUserButtonPermissions(userId);
            
            // 批量分配新权限
            for (Long buttonId : buttonIds) {
                assignButtonPermission(userId, buttonId, 1);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean removeButtonPermission(Long userId, Long buttonId) {
        try {
            QueryWrapper<UserButton> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId)
                       .eq("button_id", buttonId)
                       .eq("deleted", 0);
            
            UserButton userButton = userButtonMapper.selectOne(queryWrapper);
            if (userButton != null) {
                userButton.setDeleted(1);
                userButton.setUpdateTime(LocalDateTime.now());
                userButtonMapper.updateById(userButton);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean clearUserButtonPermissions(Long userId) {
        try {
            userButtonMapper.deleteUserButtonPermissions(userId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}