package com.tsm.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tsm.system.entity.Permission;

import java.util.List;

/**
 * 权限服务接口
 *
 * @author TSM
 * @since 2024-01-01
 */
public interface PermissionService extends IService<Permission> {

    /**
     * 根据权限编码查询权限
     *
     * @param permissionCode 权限编码
     * @return 权限信息
     */
    Permission getPermissionByCode(String permissionCode);

    /**
     * 分页查询权限列表
     *
     * @param page           分页参数
     * @param permissionName 权限名称（模糊查询）
     * @param permissionCode 权限编码（模糊查询）
     * @param type           权限类型（1-菜单 2-按钮）
     * @param status         状态（0-禁用 1-启用）
     * @return 权限分页列表
     */
    IPage<Permission> getPermissionPage(Page<Permission> page, String permissionName, String permissionCode, Integer type, Integer status);

    /**
     * 查询权限树结构
     *
     * @param type   权限类型（1-菜单 2-按钮）
     * @param status 状态（0-禁用 1-启用）
     * @return 权限树列表
     */
    List<Permission> getPermissionTree(Integer type, Integer status);

    /**
     * 查询子权限列表
     *
     * @param parentId 父权限ID
     * @param type     权限类型（1-菜单 2-按钮）
     * @param status   状态（0-禁用 1-启用）
     * @return 子权限列表
     */
    List<Permission> getChildPermissions(Long parentId, Integer type, Integer status);

    /**
     * 根据角色ID查询权限列表
     *
     * @param roleId 角色ID
     * @param type   权限类型（1-菜单 2-按钮）
     * @return 权限列表
     */
    List<Permission> getPermissionsByRoleId(Long roleId, Integer type);

    /**
     * 根据用户ID查询权限列表
     *
     * @param userId 用户ID
     * @param type   权限类型（1-菜单 2-按钮）
     * @return 权限列表
     */
    List<Permission> getPermissionsByUserId(Long userId, Integer type);

    /**
     * 创建权限
     *
     * @param permission 权限信息
     * @return 是否成功
     */
    boolean createPermission(Permission permission);

    /**
     * 更新权限
     *
     * @param permission 权限信息
     * @return 是否成功
     */
    boolean updatePermission(Permission permission);

    /**
     * 删除权限（逻辑删除）
     *
     * @param permissionId 权限ID
     * @return 是否成功
     */
    boolean deletePermission(Long permissionId);

    /**
     * 批量删除权限（逻辑删除）
     *
     * @param permissionIds 权限ID列表
     * @return 是否成功
     */
    boolean deletePermissions(List<Long> permissionIds);

    /**
     * 更新权限状态
     *
     * @param permissionId 权限ID
     * @param status       状态（0-禁用 1-启用）
     * @return 是否成功
     */
    boolean updatePermissionStatus(Long permissionId, Integer status);

    /**
     * 检查权限编码是否存在
     *
     * @param permissionCode 权限编码
     * @param excludeId      排除的权限ID（用于更新时排除自己）
     * @return 是否存在
     */
    boolean isPermissionCodeExists(String permissionCode, Long excludeId);

    /**
     * 检查权限名称是否存在
     *
     * @param permissionName 权限名称
     * @param excludeId      排除的权限ID（用于更新时排除自己）
     * @return 是否存在
     */
    boolean isPermissionNameExists(String permissionName, Long excludeId);

    /**
     * 查询所有子权限ID（包括子权限的子权限）
     *
     * @param permissionId 权限ID
     * @return 所有子权限ID列表
     */
    List<Long> getAllChildPermissionIds(Long permissionId);

    /**
     * 检查是否有子权限
     *
     * @param permissionId 权限ID
     * @return 是否有子权限
     */
    boolean hasChildPermissions(Long permissionId);

    /**
     * 获取所有菜单权限（用于构建菜单树）
     *
     * @return 菜单权限列表
     */
    List<Permission> getAllMenuPermissions();

    /**
     * 获取所有按钮权限
     *
     * @return 按钮权限列表
     */
    List<Permission> getAllButtonPermissions();

    /**
     * 根据父权限ID获取按钮权限列表
     *
     * @param parentId 父权限ID
     * @return 按钮权限列表
     */
    List<Permission> getButtonPermissionsByParentId(Long parentId);

    /**
     * 构建权限树结构（递归）
     *
     * @param permissions 权限列表
     * @param parentId    父权限ID
     * @return 权限树列表
     */
    List<Permission> buildPermissionTree(List<Permission> permissions, Long parentId);

    /**
     * 获取用户菜单权限树
     *
     * @param userId 用户ID
     * @return 菜单权限树
     */
    List<Permission> getUserMenuTree(Long userId);

    /**
     * 获取用户按钮权限列表
     *
     * @param userId 用户ID
     * @return 按钮权限编码列表
     */
    List<String> getUserButtonPermissions(Long userId);
}