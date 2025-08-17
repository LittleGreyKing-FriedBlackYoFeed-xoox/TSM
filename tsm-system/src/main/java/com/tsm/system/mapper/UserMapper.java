package com.tsm.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tsm.system.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper接口
 * 
 * @author TSM Team
 * @since 1.0.0
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户
     * 
     * @param username 用户名
     * @return 用户信息
     */
    User selectByUsername(@Param("username") String username);

    /**
     * 分页查询用户列表
     * 
     * @param page 分页参数
     * @param username 用户名（模糊查询）
     * @param realName 真实姓名（模糊查询）
     * @param status 状态
     * @return 用户列表
     */
    IPage<User> selectUserPage(Page<User> page, 
                              @Param("username") String username,
                              @Param("realName") String realName,
                              @Param("status") Integer status);

    /**
     * 查询用户及其角色信息
     * 
     * @param userId 用户ID
     * @return 用户信息（包含角色）
     */
    User selectUserWithRoles(@Param("userId") Long userId);

    /**
     * 根据角色ID查询用户列表
     * 
     * @param roleId 角色ID
     * @return 用户列表
     */
    List<User> selectUsersByRoleId(@Param("roleId") Long roleId);

    /**
     * 检查用户名是否存在
     * 
     * @param username 用户名
     * @param excludeId 排除的用户ID（用于编辑时排除自己）
     * @return 存在数量
     */
    int checkUsernameExists(@Param("username") String username, @Param("excludeId") Long excludeId);

    /**
     * 检查邮箱是否存在
     * 
     * @param email 邮箱
     * @param excludeId 排除的用户ID（用于编辑时排除自己）
     * @return 存在数量
     */
    int checkEmailExists(@Param("email") String email, @Param("excludeId") Long excludeId);

    /**
     * 检查手机号是否存在
     * 
     * @param phone 手机号
     * @param excludeId 排除的用户ID（用于编辑时排除自己）
     * @return 存在数量
     */
    int checkPhoneExists(@Param("phone") String phone, @Param("excludeId") Long excludeId);

    /**
     * 批量删除用户
     * 
     * @param userIds 用户ID列表
     * @return 删除数量
     */
    int deleteBatchByIds(@Param("userIds") List<Long> userIds);

    /**
     * 重置用户密码
     * 
     * @param userId 用户ID
     * @param newPassword 新密码
     * @return 更新数量
     */
    int resetPassword(@Param("userId") Long userId, @Param("newPassword") String newPassword);

    /**
     * 更新用户状态
     * 
     * @param userId 用户ID
     * @param status 状态
     * @return 更新数量
     */
    int updateStatus(@Param("userId") Long userId, @Param("status") Integer status);
}