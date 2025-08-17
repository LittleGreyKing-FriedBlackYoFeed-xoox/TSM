package com.tsm.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tsm.api.common.PageResult;
import com.tsm.system.entity.User;

import java.util.List;

/**
 * 用户服务接口
 * 
 * @author TSM Team
 * @since 1.0.0
 */
public interface UserService extends IService<User> {

    /**
     * 根据用户名查询用户
     * 
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByUsername(String username);

    /**
     * 分页查询用户列表
     * 
     * @param current 当前页
     * @param size 每页大小
     * @param username 用户名（模糊查询）
     * @param realName 真实姓名（模糊查询）
     * @param status 状态
     * @return 分页结果
     */
    PageResult<User> getUserPage(long current, long size, String username, String realName, Integer status);

    /**
     * 查询用户及其角色信息
     * 
     * @param userId 用户ID
     * @return 用户信息（包含角色）
     */
    User getUserWithRoles(Long userId);

    /**
     * 根据角色ID查询用户列表
     * 
     * @param roleId 角色ID
     * @return 用户列表
     */
    List<User> getUsersByRoleId(Long roleId);

    /**
     * 创建用户
     * 
     * @param user 用户信息
     * @return 是否成功
     */
    boolean createUser(User user);

    /**
     * 更新用户
     * 
     * @param user 用户信息
     * @return 是否成功
     */
    boolean updateUser(User user);

    /**
     * 删除用户
     * 
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean deleteUser(Long userId);

    /**
     * 批量删除用户
     * 
     * @param userIds 用户ID列表
     * @return 是否成功
     */
    boolean deleteBatchUsers(List<Long> userIds);

    /**
     * 重置用户密码
     * 
     * @param userId 用户ID
     * @param newPassword 新密码
     * @return 是否成功
     */
    boolean resetPassword(Long userId, String newPassword);

    /**
     * 更新用户状态
     * 
     * @param userId 用户ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateUserStatus(Long userId, Integer status);

    /**
     * 分配用户角色
     * 
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 是否成功
     */
    boolean assignUserRoles(Long userId, List<Long> roleIds);

    /**
     * 检查用户名是否存在
     * 
     * @param username 用户名
     * @param excludeId 排除的用户ID（用于编辑时排除自己）
     * @return 是否存在
     */
    boolean isUsernameExists(String username, Long excludeId);

    /**
     * 检查邮箱是否存在
     * 
     * @param email 邮箱
     * @param excludeId 排除的用户ID（用于编辑时排除自己）
     * @return 是否存在
     */
    boolean isEmailExists(String email, Long excludeId);

    /**
     * 检查手机号是否存在
     * 
     * @param phone 手机号
     * @param excludeId 排除的用户ID（用于编辑时排除自己）
     * @return 是否存在
     */
    boolean isPhoneExists(String phone, Long excludeId);

    /**
     * 验证用户密码
     * 
     * @param username 用户名
     * @param password 密码
     * @return 用户信息（验证成功）或null（验证失败）
     */
    User validateUser(String username, String password);

    /**
     * 修改用户密码
     * 
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否成功
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 获取用户权限列表
     * 
     * @param userId 用户ID
     * @return 权限编码列表
     */
    List<String> getUserPermissions(Long userId);

    /**
     * 获取用户按钮权限列表
     * 
     * @param userId 用户ID
     * @return 按钮权限编码列表
     */
    List<String> getUserButtonPermissions(Long userId);
}