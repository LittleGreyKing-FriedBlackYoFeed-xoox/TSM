package com.tsm.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tsm.auth.entity.Button;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 按钮Mapper接口
 */
@Mapper
public interface ButtonMapper extends BaseMapper<Button> {

    /**
     * 根据页面编码查询按钮
     */
    @Select("SELECT * FROM sys_button WHERE page_code = #{pageCode} AND deleted = 0 AND status = 0 ORDER BY sort_order")
    List<Button> findByPageCode(@Param("pageCode") String pageCode);

    /**
     * 根据按钮编码查询按钮
     */
    @Select("SELECT * FROM sys_button WHERE button_code = #{buttonCode} AND deleted = 0")
    Button findByButtonCode(@Param("buttonCode") String buttonCode);

    /**
     * 查询用户在指定页面的按钮权限
     */
    @Select("SELECT b.* FROM sys_button b " +
            "LEFT JOIN sys_user_button ub ON b.id = ub.button_id " +
            "WHERE ub.user_id = #{userId} AND b.page_code = #{pageCode} " +
            "AND ub.permission_type = 1 AND ub.deleted = 0 " +
            "AND b.deleted = 0 AND b.status = 0 " +
            "AND (ub.effective_time IS NULL OR ub.effective_time <= NOW()) " +
            "AND (ub.expiry_time IS NULL OR ub.expiry_time > NOW()) " +
            "ORDER BY b.sort_order")
    List<Button> findUserButtonsByPage(@Param("userId") Long userId, @Param("pageCode") String pageCode);
}