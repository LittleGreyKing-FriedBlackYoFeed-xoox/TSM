package com.tsm.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tsm.system.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限Mapper接口
 * 
 * @author TSM Team
 * @since 1.0.0
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 根据权限编码查询权限
     * 
     * @param permissionCode 权限编码
     * @return 权限信息
     */
    Permission selectByPermissionCode(@Param("permissionCode") String permissionCode);

    /**
     * 分页查询权限列表
     * 
     * @param page 分页参数
     * @param permissionName 权限名称（模糊查询）
     * @param permissionCode 权限编码（模糊查询）
     * @param permissionType 权限类型
     * @param status 状态
     * @return 分页结果
     */
    IPage<Permission> selectPermissionPage(Page<Permission> page, @Param("permissionName") String permissionName,
                                           @Param("permissionCode") String permissionCode, 
                                           @Param("permissionType") String permissionType,
                                           @Param("status") Integer status);

    /**
     * 查询权限树结构
     * 
     * @param permissionType 权限类型（可选）
     * @param status 状态（可选）
     * @return 权限树列表
     */
    List<Permission> selectPermissionTree(@Param("permissionType") String permissionType, @Param("status") Integer status);

    /**
     * 查询子权限列表
     * 
     * @param parentId 父权限ID
     * @return 子权限列表
     */
    List<Permission> selectChildrenPermissions(@Param("parentId") Long parentId);

    /**
     * 根据角色ID查询权限列表
     * 
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<Permission> selectPermissionsByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据用户ID查询权限列表
     * 
     * @param userId 用户ID
     * @return 权限列表
     */
    List<Permission> selectPermissionsByUserId(@Param("userId") Long userId);

    /**
     * 检查权限编码是否存在
     * 
     * @param permissionCode 权限编码
     * @param excludeId 排除的权限ID（用于编辑时排除自己）
     * @return 存在数量
     */
    int checkPermissionCodeExists(@Param("permissionCode") String permissionCode, @Param("excludeId") Long excludeId);

    /**
     * 检查权限名称是否存在
     * 
     * @param permissionName 权限名称
     * @param parentId 父权限ID
     * @param excludeId 排除的权限ID（用于编辑时排除自己）
     * @return 存在数量
     */
    int checkPermissionNameExists(@Param("permissionName") String permissionName, 
                                  @Param("parentId") Long parentId, @Param("excludeId") Long excludeId);

    /**
     * 批量删除权限
     * 
     * @param permissionIds 权限ID列表
     * @return 影响行数
     */
    int deleteBatchByIds(@Param("permissionIds") List<Long> permissionIds);

    /**
     * 更新权限状态
     * 
     * @param permissionId 权限ID
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(@Param("permissionId") Long permissionId, @Param("status") Integer status);

    /**
     * 查询权限的所有子权限ID（递归）
     * 
     * @param permissionId 权限ID
     * @return 子权限ID列表
     */
    List<Long> selectAllChildrenIds(@Param("permissionId") Long permissionId);

    /**
     * 检查是否有子权限
     * 
     * @param permissionId 权限ID
     * @return 子权限数量
     */
    int countChildrenPermissions(@Param("permissionId") Long permissionId);

    /**
     * 查询菜单权限列表（用于构建菜单树）
     * 
     * @param userId 用户ID（可选，用于权限过滤）
     * @return 菜单权限列表
     */
    List<Permission> selectMenuPermissions(@Param("userId") Long userId);

    /**
     * 查询按钮权限列表
     * 
     * @param userId 用户ID（可选，用于权限过滤）
     * @param parentId 父权限ID（可选）
     * @return 按钮权限列表
     */
    List<Permission> selectButtonPermissions(@Param("userId") Long userId, @Param("parentId") Long parentId);
}