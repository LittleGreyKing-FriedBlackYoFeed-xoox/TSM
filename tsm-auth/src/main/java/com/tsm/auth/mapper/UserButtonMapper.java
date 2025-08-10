package com.tsm.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tsm.auth.entity.UserButton;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 用户按钮权限Mapper接口
 */
@Mapper
public interface UserButtonMapper extends BaseMapper<UserButton> {

    /**
     * 检查用户是否有指定按钮权限
     */
    @Select("SELECT COUNT(*) FROM sys_user_button ub " +
            "LEFT JOIN sys_button b ON ub.button_id = b.id " +
            "WHERE ub.user_id = #{userId} AND b.button_code = #{buttonCode} " +
            "AND ub.permission_type = 1 AND ub.deleted = 0 " +
            "AND (ub.effective_time IS NULL OR ub.effective_time <= NOW()) " +
            "AND (ub.expiry_time IS NULL OR ub.expiry_time > NOW())")
    int checkUserButtonPermission(@Param("userId") Long userId, @Param("buttonCode") String buttonCode);

    /**
     * 删除用户的所有按钮权限
     */
    @Update("UPDATE sys_user_button SET deleted = 1 WHERE user_id = #{userId}")
    int deleteUserButtonPermissions(@Param("userId") Long userId);

    /**
     * 根据用户ID和按钮ID查询权限记录
     */
    @Select("SELECT * FROM sys_user_button WHERE user_id = #{userId} AND button_id = #{buttonId} AND deleted = 0")
    UserButton findByUserIdAndButtonId(@Param("userId") Long userId, @Param("buttonId") Long buttonId);
}