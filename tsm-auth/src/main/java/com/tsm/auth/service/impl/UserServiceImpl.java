package com.tsm.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tsm.auth.entity.User;
import com.tsm.auth.mapper.UserMapper;
import com.tsm.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户管理服务实现类
 */
// @Service  // 暂时注释掉，使用UserServiceMockImpl
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public IPage<User> getUserList(int page, int size, String username, String realName, Integer status) {
        Page<User> pageParam = new Page<>(page, size);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        
        // 添加查询条件
        if (StringUtils.hasText(username)) {
            queryWrapper.like("username", username);
        }
        if (StringUtils.hasText(realName)) {
            queryWrapper.like("real_name", realName);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        
        // 排除已删除的记录
        queryWrapper.eq("deleted", 0);
        
        // 按创建时间倒序排列
        queryWrapper.orderByDesc("create_time");
        
        return userMapper.selectPage(pageParam, queryWrapper);
    }

    @Override
    public User getUserById(Long id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id).eq("deleted", 0);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    @Transactional
    public User createUser(User user) {
        try {
            // 检查用户名是否已存在
            if (isUsernameExists(user.getUsername())) {
                throw new RuntimeException("用户名已存在");
            }
            
            // 检查邮箱是否已存在
            if (StringUtils.hasText(user.getEmail()) && isEmailExists(user.getEmail())) {
                throw new RuntimeException("邮箱已存在");
            }
            
            // 检查手机号是否已存在
            if (StringUtils.hasText(user.getPhone()) && isPhoneExists(user.getPhone())) {
                throw new RuntimeException("手机号已存在");
            }
            
            // 加密密码
            if (StringUtils.hasText(user.getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            } else {
                // 默认密码为123456
                user.setPassword(passwordEncoder.encode("123456"));
            }
            
            // 设置默认值
            user.setCreateTime(LocalDateTime.now());
            user.setUpdateTime(LocalDateTime.now());
            user.setDeleted(0);
            if (user.getStatus() == null) {
                user.setStatus(0); // 默认启用
            }
            
            if (userMapper.insert(user) > 0) {
                // 返回创建的用户，但不包含密码
                User result = getUserById(user.getId());
                if (result != null) {
                    result.setPassword(null);
                }
                return result;
            } else {
                throw new RuntimeException("创建用户失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("创建用户失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        try {
            User existingUser = getUserById(user.getId());
            if (existingUser == null) {
                throw new RuntimeException("用户不存在");
            }
            
            // 检查用户名是否被其他用户使用
            if (!existingUser.getUsername().equals(user.getUsername()) && isUsernameExists(user.getUsername())) {
                throw new RuntimeException("用户名已被其他用户使用");
            }
            
            // 检查邮箱是否被其他用户使用
            if (StringUtils.hasText(user.getEmail()) && 
                !user.getEmail().equals(existingUser.getEmail()) && 
                isEmailExists(user.getEmail())) {
                throw new RuntimeException("邮箱已被其他用户使用");
            }
            
            // 检查手机号是否被其他用户使用
            if (StringUtils.hasText(user.getPhone()) && 
                !user.getPhone().equals(existingUser.getPhone()) && 
                isPhoneExists(user.getPhone())) {
                throw new RuntimeException("手机号已被其他用户使用");
            }
            
            // 不更新密码字段（密码单独更新）
            user.setPassword(null);
            user.setUpdateTime(LocalDateTime.now());
            
            if (userMapper.updateById(user) > 0) {
                // 返回更新后的用户，但不包含密码
                User result = getUserById(user.getId());
                if (result != null) {
                    result.setPassword(null);
                }
                return result;
            } else {
                throw new RuntimeException("更新用户失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("更新用户失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean deleteUser(Long id) {
        try {
            User user = getUserById(id);
            if (user == null) {
                throw new RuntimeException("用户不存在");
            }
            
            // 逻辑删除
            user.setDeleted(1);
            user.setUpdateTime(LocalDateTime.now());
            
            return userMapper.updateById(user) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("删除用户失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean batchDeleteUsers(List<Long> ids) {
        try {
            for (Long id : ids) {
                deleteUser(id);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("批量删除用户失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean updateUserStatus(Long id, Integer status) {
        try {
            User user = getUserById(id);
            if (user == null) {
                throw new RuntimeException("用户不存在");
            }
            
            user.setStatus(status);
            user.setUpdateTime(LocalDateTime.now());
            
            return userMapper.updateById(user) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("更新用户状态失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean resetPassword(Long id, String newPassword) {
        try {
            User user = getUserById(id);
            if (user == null) {
                throw new RuntimeException("用户不存在");
            }
            
            // 加密新密码
            String encodedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedPassword);
            user.setUpdateTime(LocalDateTime.now());
            
            return userMapper.updateById(user) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("重置密码失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean changePassword(Long id, String oldPassword, String newPassword) {
        try {
            User user = getUserById(id);
            if (user == null) {
                throw new RuntimeException("用户不存在");
            }
            
            // 验证旧密码
            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                throw new RuntimeException("原密码错误");
            }
            
            // 加密新密码
            String encodedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedPassword);
            user.setUpdateTime(LocalDateTime.now());
            
            return userMapper.updateById(user) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("修改密码失败：" + e.getMessage());
        }
    }

    @Override
    public boolean isUsernameExists(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username).eq("deleted", 0);
        return userMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public boolean isEmailExists(String email) {
        if (!StringUtils.hasText(email)) {
            return false;
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email).eq("deleted", 0);
        return userMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public boolean isPhoneExists(String phone) {
        if (!StringUtils.hasText(phone)) {
            return false;
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone).eq("deleted", 0);
        return userMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public List<User> getAllUsers() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", 0).orderByDesc("create_time");
        return userMapper.selectList(queryWrapper);
    }
}