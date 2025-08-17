package com.tsm.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tsm.system.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色Mapper接口
 * 
 * @author TSM Team
 * @since 1.0.0
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据角色编码查询角色
     * 
     * @param roleCode 角色编码
     * @return 角色信息
     */
    Role selectByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 分页查询角色列表
     * 
     * @param page 分页参数
     * @param roleName 角色名称（模糊查询）
     * @param roleCode 角色编码（模糊查询）
     * @param status 状态
     * @return 分页结果
     */
    IPage<Role> selectRolePage(Page<Role> page, @Param("roleName") String roleName, 
                               @Param("roleCode") String roleCode, @Param("status") Integer status);

    /**
     * 查询角色及其权限信息
     * 
     * @param roleId 角色ID
     * @return 角色信息（包含权限）
     */
    Role selectRoleWithPermissions(@Param("roleId") Long roleId);

    /**
     * 查询角色及其按钮权限信息
     * 
     * @param roleId 角色ID
     * @return 角色信息（包含按钮权限）
     */
    Role selectRoleWithButtons(@Param("roleId") Long roleId);

    /**
     * 根据用户ID查询角色列表
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    List<Role> selectRolesByUserId(@Param("userId") Long userId);

    /**
     * 检查角色编码是否存在
     * 
     * @param roleCode 角色编码
     * @param excludeId 排除的角色ID（用于编辑时排除自己）
     * @return 存在数量
     */
    int checkRoleCodeExists(@Param("roleCode") String roleCode, @Param("excludeId") Long excludeId);

    /**
     * 检查角色名称是否存在
     * 
     * @param roleName 角色名称
     * @param excludeId 排除的角色ID（用于编辑时排除自己）
     * @return 存在数量
     */
    int checkRoleNameExists(@Param("roleName") String roleName, @Param("excludeId") Long excludeId);

    /**
     * 批量删除角色
     * 
     * @param roleIds 角色ID列表
     * @return 影响行数
     */
    int deleteBatchByIds(@Param("roleIds") List<Long> roleIds);

    /**
     * 更新角色状态
     * 
     * @param roleId 角色ID
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(@Param("roleId") Long roleId, @Param("status") Integer status);

    /**
     * 删除角色权限关联
     * 
     * @param roleId 角色ID
     * @return 影响行数
     */
    int deleteRolePermissions(@Param("roleId") Long roleId);

    /**
     * 批量插入角色权限关联
     * 
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     * @return 影响行数
     */
    int insertRolePermissions(@Param("roleId") Long roleId, @Param("permissionIds") List<Long> permissionIds);

    /**
     * 删除角色按钮权限关联
     * 
     * @param roleId 角色ID
     * @return 影响行数
     */
    int deleteRoleButtons(@Param("roleId") Long roleId);

    /**
     * 批量插入角色按钮权限关联
     * 
     * @param roleId 角色ID
     * @param buttonIds 按钮权限ID列表
     * @return 影响行数
     */
    int insertRoleButtons(@Param("roleId") Long roleId, @Param("buttonIds") List<Long> buttonIds);

    /**
     * 删除用户角色关联
     * 
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteUserRoles(@Param("userId") Long userId);

    /**
     * 批量插入用户角色关联
     * 
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 影响行数
     */
    int insertUserRoles(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);
}