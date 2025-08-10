package com.tsm.auth.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tsm.auth.entity.User;

import java.util.List;

/**
 * 用户管理服务接口
 */
public interface UserService {
    
    /**
     * 分页查询用户列表
     */
    IPage<User> getUserList(int page, int size, String username, String realName, Integer status);
    
    /**
     * 根据ID获取用户信息
     */
    User getUserById(Long id);
    
    /**
     * 根据用户名获取用户信息
     */
    User getUserByUsername(String username);
    
    /**
     * 创建用户
     */
    User createUser(User user);
    
    /**
     * 更新用户信息
     */
    User updateUser(User user);
    
    /**
     * 删除用户（逻辑删除）
     */
    boolean deleteUser(Long id);
    
    /**
     * 批量删除用户
     */
    boolean batchDeleteUsers(List<Long> ids);
    
    /**
     * 启用/禁用用户
     */
    boolean updateUserStatus(Long id, Integer status);
    
    /**
     * 重置用户密码
     */
    boolean resetPassword(Long id, String newPassword);
    
    /**
     * 修改用户密码
     */
    boolean changePassword(Long id, String oldPassword, String newPassword);
    
    /**
     * 检查用户名是否存在
     */
    boolean isUsernameExists(String username);
    
    /**
     * 检查邮箱是否存在
     */
    boolean isEmailExists(String email);
    
    /**
     * 检查手机号是否存在
     */
    boolean isPhoneExists(String phone);
    
    /**
     * 获取所有用户列表（不分页）
     */
    List<User> getAllUsers();
}