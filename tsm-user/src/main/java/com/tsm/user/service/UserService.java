package com.tsm.user.service;

import com.tsm.common.result.PageResult;
import com.tsm.user.dto.UserDTO;
import com.tsm.user.dto.UserQueryDTO;
import com.tsm.user.vo.UserVO;

import java.util.List;

/**
 * 用户服务接口
 * @author TSM
 */
public interface UserService {
    
    /**
     * 分页查询用户列表
     * @param queryDTO 查询条件
     * @return 用户分页列表
     */
    PageResult<UserVO> getUserPage(UserQueryDTO queryDTO);
    
    /**
     * 根据ID查询用户详情
     * @param id 用户ID
     * @return 用户详情
     */
    UserVO getUserById(Long id);
    
    /**
     * 新增用户
     * @param userDTO 用户信息
     * @return 用户ID
     */
    Long createUser(UserDTO userDTO);
    
    /**
     * 更新用户
     * @param userDTO 用户信息
     * @return 是否成功
     */
    boolean updateUser(UserDTO userDTO);
    
    /**
     * 删除用户
     * @param id 用户ID
     * @return 是否成功
     */
    boolean deleteUser(Long id);
    
    /**
     * 批量删除用户
     * @param ids 用户ID列表
     * @return 是否成功
     */
    boolean batchDeleteUsers(List<Long> ids);
    
    /**
     * 更新用户状态
     * @param id 用户ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateUserStatus(Long id, Integer status);
    
    /**
     * 重置用户密码
     * @param id 用户ID
     * @param newPassword 新密码
     * @return 是否成功
     */
    boolean resetPassword(Long id, String newPassword);
    
    /**
     * 分配用户角色
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 是否成功
     */
    boolean assignUserRoles(Long userId, List<Long> roleIds);
    
    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @param excludeId 排除的用户ID（更新时使用）
     * @return 是否存在
     */
    boolean checkUsernameExists(String username, Long excludeId);
    
    /**
     * 检查邮箱是否存在
     * @param email 邮箱
     * @param excludeId 排除的用户ID（更新时使用）
     * @return 是否存在
     */
    boolean checkEmailExists(String email, Long excludeId);
    
    /**
     * 检查手机号是否存在
     * @param phone 手机号
     * @param excludeId 排除的用户ID（更新时使用）
     * @return 是否存在
     */
    boolean checkPhoneExists(String phone, Long excludeId);
    
    /**
     * 获取用户统计信息
     * @return 统计信息
     */
    Object getUserStatistics();
}