package com.tsm.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tsm.api.common.PageResult;
import com.tsm.system.entity.Role;

import java.util.List;

/**
 * 角色服务接口
 * 
 * @author TSM Team
 * @since 1.0.0
 */
public interface RoleService extends IService<Role> {

    /**
     * 根据角色编码查询角色
     * 
     * @param roleCode 角色编码
     * @return 角色信息
     */
    Role getRoleByCode(String roleCode);

    /**
     * 分页查询角色列表
     * 
     * @param current 当前页
     * @param size 每页大小
     * @param roleName 角色名称（模糊查询）
     * @param roleCode 角色编码（模糊查询）
     * @param status 状态
     * @return 分页结果
     */
    PageResult<Role> getRolePage(long current, long size, String roleName, String roleCode, Integer status);

    /**
     * 查询角色及其权限信息
     * 
     * @param roleId 角色ID
     * @return 角色信息（包含权限）
     */
    Role getRoleWithPermissions(Long roleId);

    /**
     * 查询角色及其按钮权限信息
     * 
     * @param roleId 角色ID
     * @return 角色信息（包含按钮权限）
     */
    Role getRoleWithButtons(Long roleId);

    /**
     * 根据用户ID查询角色列表
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    List<Role> getRolesByUserId(Long userId);

    /**
     * 创建角色
     * 
     * @param role 角色信息
     * @return 是否成功
     */
    boolean createRole(Role role);

    /**
     * 更新角色
     * 
     * @param role 角色信息
     * @return 是否成功
     */
    boolean updateRole(Role role);

    /**
     * 删除角色
     * 
     * @param roleId 角色ID
     * @return 是否成功
     */
    boolean deleteRole(Long roleId);

    /**
     * 批量删除角色
     * 
     * @param roleIds 角色ID列表
     * @return 是否成功
     */
    boolean deleteBatchRoles(List<Long> roleIds);

    /**
     * 更新角色状态
     * 
     * @param roleId 角色ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateRoleStatus(Long roleId, Integer status);

    /**
     * 分配角色权限
     * 
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     * @return 是否成功
     */
    boolean assignRolePermissions(Long roleId, List<Long> permissionIds);

    /**
     * 分配角色按钮权限
     * 
     * @param roleId 角色ID
     * @param buttonIds 按钮权限ID列表
     * @return 是否成功
     */
    boolean assignRoleButtons(Long roleId, List<Long> buttonIds);

    /**
     * 检查角色编码是否存在
     * 
     * @param roleCode 角色编码
     * @param excludeId 排除的角色ID（用于编辑时排除自己）
     * @return 是否存在
     */
    boolean isRoleCodeExists(String roleCode, Long excludeId);

    /**
     * 检查角色名称是否存在
     * 
     * @param roleName 角色名称
     * @param excludeId 排除的角色ID（用于编辑时排除自己）
     * @return 是否存在
     */
    boolean isRoleNameExists(String roleName, Long excludeId);

    /**
     * 获取所有可用角色列表
     * 
     * @return 角色列表
     */
    List<Role> getAllAvailableRoles();

    /**
     * 检查角色是否被用户使用
     * 
     * @param roleId 角色ID
     * @return 是否被使用
     */
    boolean isRoleInUse(Long roleId);
}