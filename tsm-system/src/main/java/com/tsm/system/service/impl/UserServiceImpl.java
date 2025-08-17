package com.tsm.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tsm.api.common.PageResult;
import com.tsm.system.entity.User;
import com.tsm.system.mapper.UserMapper;
import com.tsm.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户服务实现类
 * 
 * @author TSM Team
 * @since 1.0.0
 */
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User getUserByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            return null;
        }
        return userMapper.selectByUsername(username);
    }

    @Override
    public PageResult<User> getUserPage(long current, long size, String username, String realName, Integer status) {
        Page<User> page = new Page<>(current, size);
        IPage<User> result = userMapper.selectUserPage(page, username, realName, status);
        
        return new PageResult<>(
            result.getCurrent(),
            result.getSize(),
            result.getTotal(),
            result.getRecords()
        );
    }

    @Override
    public User getUserWithRoles(Long userId) {
        if (userId == null) {
            return null;
        }
        return userMapper.selectUserWithRoles(userId);
    }

    @Override
    public List<User> getUsersByRoleId(Long roleId) {
        if (roleId == null) {
            return List.of();
        }
        return userMapper.selectUsersByRoleId(roleId);
    }

    @Override
    public boolean createUser(User user) {
        if (user == null) {
            return false;
        }
        
        // 检查用户名是否已存在
        if (isUsernameExists(user.getUsername(), null)) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 检查邮箱是否已存在
        if (StringUtils.hasText(user.getEmail()) && isEmailExists(user.getEmail(), null)) {
            throw new RuntimeException("邮箱已存在");
        }
        
        // 检查手机号是否已存在
        if (StringUtils.hasText(user.getPhone()) && isPhoneExists(user.getPhone(), null)) {
            throw new RuntimeException("手机号已存在");
        }
        
        // 加密密码
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        // 设置默认值
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setDeleted(0);
        
        if (user.getStatus() == null) {
            user.setStatus(1); // 默认启用
        }
        
        return save(user);
    }

    @Override
    public boolean updateUser(User user) {
        if (user == null || user.getId() == null) {
            return false;
        }
        
        // 检查用户名是否已存在（排除自己）
        if (StringUtils.hasText(user.getUsername()) && isUsernameExists(user.getUsername(), user.getId())) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 检查邮箱是否已存在（排除自己）
        if (StringUtils.hasText(user.getEmail()) && isEmailExists(user.getEmail(), user.getId())) {
            throw new RuntimeException("邮箱已存在");
        }
        
        // 检查手机号是否已存在（排除自己）
        if (StringUtils.hasText(user.getPhone()) && isPhoneExists(user.getPhone(), user.getId())) {
            throw new RuntimeException("手机号已存在");
        }
        
        // 如果有新密码，则加密
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            // 不更新密码字段
            user.setPassword(null);
        }
        
        user.setUpdateTime(LocalDateTime.now());
        
        return updateById(user);
    }

    @Override
    public boolean deleteUser(Long userId) {
        if (userId == null) {
            return false;
        }
        
        User user = new User();
        user.setId(userId);
        user.setDeleted(1);
        user.setUpdateTime(LocalDateTime.now());
        
        return updateById(user);
    }

    @Override
    public boolean deleteBatchUsers(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return false;
        }
        
        return userMapper.deleteBatchByIds(userIds) > 0;
    }

    @Override
    public boolean resetPassword(Long userId, String newPassword) {
        if (userId == null || !StringUtils.hasText(newPassword)) {
            return false;
        }
        
        String encodedPassword = passwordEncoder.encode(newPassword);
        return userMapper.resetPassword(userId, encodedPassword) > 0;
    }

    @Override
    public boolean updateUserStatus(Long userId, Integer status) {
        if (userId == null || status == null) {
            return false;
        }
        
        return userMapper.updateStatus(userId, status) > 0;
    }

    @Override
    public boolean assignUserRoles(Long userId, List<Long> roleIds) {
        if (userId == null) {
            return false;
        }
        
        // TODO: 实现用户角色分配逻辑
        // 1. 删除用户现有角色
        // 2. 添加新的角色关联
        
        return true;
    }

    @Override
    public boolean isUsernameExists(String username, Long excludeId) {
        if (!StringUtils.hasText(username)) {
            return false;
        }
        
        return userMapper.checkUsernameExists(username, excludeId) > 0;
    }

    @Override
    public boolean isEmailExists(String email, Long excludeId) {
        if (!StringUtils.hasText(email)) {
            return false;
        }
        
        return userMapper.checkEmailExists(email, excludeId) > 0;
    }

    @Override
    public boolean isPhoneExists(String phone, Long excludeId) {
        if (!StringUtils.hasText(phone)) {
            return false;
        }
        
        return userMapper.checkPhoneExists(phone, excludeId) > 0;
    }

    @Override
    public User validateUser(String username, String password) {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            return null;
        }
        
        User user = getUserByUsername(username);
        if (user == null || user.getStatus() != 1) {
            return null;
        }
        
        // 验证密码
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        
        return null;
    }

    @Override
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        if (userId == null || !StringUtils.hasText(oldPassword) || !StringUtils.hasText(newPassword)) {
            return false;
        }
        
        User user = getById(userId);
        if (user == null || user.getStatus() != 1) {
            return false;
        }
        
        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }
        
        // 更新新密码
        return resetPassword(userId, newPassword);
    }

    @Override
    public List<String> getUserPermissions(Long userId) {
        if (userId == null) {
            return List.of();
        }
        
        // TODO: 实现获取用户权限逻辑
        // 通过用户角色关联查询权限
        
        return List.of();
    }

    @Override
    public List<String> getUserButtonPermissions(Long userId) {
        if (userId == null) {
            return List.of();
        }
        
        // TODO: 实现获取用户按钮权限逻辑
        // 通过用户角色关联查询按钮权限
        
        return List.of();
    }
}