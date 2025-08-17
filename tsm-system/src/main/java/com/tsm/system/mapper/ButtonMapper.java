package com.tsm.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tsm.system.entity.Button;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 按钮权限Mapper接口
 *
 * @author TSM
 * @since 2024-01-01
 */
@Mapper
public interface ButtonMapper extends BaseMapper<Button> {

    /**
     * 根据按钮编码查询按钮
     *
     * @param buttonCode 按钮编码
     * @return 按钮信息
     */
    Button selectButtonByCode(@Param("buttonCode") String buttonCode);

    /**
     * 分页查询按钮列表
     *
     * @param page         分页参数
     * @param buttonName   按钮名称（模糊查询）
     * @param buttonCode   按钮编码（模糊查询）
     * @param permissionId 所属权限ID
     * @param status       状态（0-禁用 1-启用）
     * @return 按钮分页列表
     */
    IPage<Button> selectButtonList(Page<Button> page,
                                   @Param("buttonName") String buttonName,
                                   @Param("buttonCode") String buttonCode,
                                   @Param("permissionId") Long permissionId,
                                   @Param("status") Integer status);

    /**
     * 查询按钮及其关联权限信息
     *
     * @param buttonId 按钮ID
     * @return 按钮及权限信息
     */
    Button selectButtonWithPermission(@Param("buttonId") Long buttonId);

    /**
     * 根据权限ID查询按钮列表
     *
     * @param permissionId 权限ID
     * @param status       状态（0-禁用 1-启用）
     * @return 按钮列表
     */
    List<Button> selectButtonsByPermissionId(@Param("permissionId") Long permissionId,
                                             @Param("status") Integer status);

    /**
     * 根据角色ID查询按钮列表
     *
     * @param roleId 角色ID
     * @param status 状态（0-禁用 1-启用）
     * @return 按钮列表
     */
    List<Button> selectButtonsByRoleId(@Param("roleId") Long roleId,
                                       @Param("status") Integer status);

    /**
     * 根据用户ID查询按钮列表
     *
     * @param userId 用户ID
     * @param status 状态（0-禁用 1-启用）
     * @return 按钮列表
     */
    List<Button> selectButtonsByUserId(@Param("userId") Long userId,
                                       @Param("status") Integer status);

    /**
     * 检查按钮编码是否存在
     *
     * @param buttonCode 按钮编码
     * @param excludeId  排除的按钮ID（用于更新时排除自己）
     * @return 存在数量
     */
    int checkButtonCodeExists(@Param("buttonCode") String buttonCode,
                              @Param("excludeId") Long excludeId);

    /**
     * 检查按钮名称是否存在
     *
     * @param buttonName 按钮名称
     * @param excludeId  排除的按钮ID（用于更新时排除自己）
     * @return 存在数量
     */
    int checkButtonNameExists(@Param("buttonName") String buttonName,
                              @Param("excludeId") Long excludeId);

    /**
     * 批量删除按钮（逻辑删除）
     *
     * @param buttonIds 按钮ID列表
     * @return 影响行数
     */
    int batchDeleteButtons(@Param("buttonIds") List<Long> buttonIds);

    /**
     * 更新按钮状态
     *
     * @param buttonId 按钮ID
     * @param status   状态（0-禁用 1-启用）
     * @return 影响行数
     */
    int updateButtonStatus(@Param("buttonId") Long buttonId,
                           @Param("status") Integer status);

    /**
     * 根据权限ID列表查询按钮列表
     *
     * @param permissionIds 权限ID列表
     * @param status        状态（0-禁用 1-启用）
     * @return 按钮列表
     */
    List<Button> selectButtonsByPermissionIds(@Param("permissionIds") List<Long> permissionIds,
                                              @Param("status") Integer status);

    /**
     * 查询所有可用按钮
     *
     * @return 按钮列表
     */
    List<Button> selectAllAvailableButtons();

    /**
     * 检查按钮是否被角色使用
     *
     * @param buttonId 按钮ID
     * @return 使用数量
     */
    int checkButtonUsedByRole(@Param("buttonId") Long buttonId);
}