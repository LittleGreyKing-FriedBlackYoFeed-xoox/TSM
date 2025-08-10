package com.tsm.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tsm.auth.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户
     */
    @Select("SELECT * FROM sys_user WHERE username = #{username} AND deleted = 0")
    User findByUsername(@Param("username") String username);

    /**
     * 查询用户的所有权限
     */
    @Select("SELECT DISTINCT p.permission_code " +
            "FROM sys_user u " +
            "LEFT JOIN sys_user_role ur ON u.id = ur.user_id " +
            "LEFT JOIN sys_role_permission rp ON ur.role_id = rp.role_id " +
            "LEFT JOIN sys_permission p ON rp.permission_id = p.id " +
            "WHERE u.id = #{userId} AND u.deleted = 0 AND p.deleted = 0 AND p.status = 0")
    List<String> findUserPermissions(@Param("userId") Long userId);

    /**
     * 查询用户的所有按钮权限
     */
    @Select("SELECT DISTINCT b.button_code " +
            "FROM sys_user_button ub " +
            "LEFT JOIN sys_button b ON ub.button_id = b.id " +
            "WHERE ub.user_id = #{userId} AND ub.permission_type = 1 " +
            "AND ub.deleted = 0 AND b.deleted = 0 AND b.status = 0 " +
            "AND (ub.effective_time IS NULL OR ub.effective_time <= NOW()) " +
            "AND (ub.expiry_time IS NULL OR ub.expiry_time > NOW())")
    List<String> findUserButtonPermissions(@Param("userId") Long userId);

    /**
     * 查询用户的角色
     */
    @Select("SELECT r.* FROM sys_role r " +
            "LEFT JOIN sys_user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND r.deleted = 0 AND r.status = 0")
    List<com.tsm.auth.entity.Role> findUserRoles(@Param("userId") Long userId);
}