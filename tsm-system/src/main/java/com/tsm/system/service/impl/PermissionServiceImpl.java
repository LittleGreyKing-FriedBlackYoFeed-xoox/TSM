package com.tsm.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tsm.common.core.exception.BusinessException;
import com.tsm.api.common.PageResult;
import com.tsm.common.core.result.ResultCode;
import com.tsm.system.entity.Permission;
import com.tsm.system.mapper.PermissionMapper;
import com.tsm.system.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限服务实现类
 *
 * @author TSM
 * @since 2024-01-01
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public Permission getPermissionByCode(String permissionCode) {
        if (!StringUtils.hasText(permissionCode)) {
            return null;
        }
        return permissionMapper.selectByPermissionCode(permissionCode);
    }

    @Override
    public IPage<Permission> getPermissionPage(Page<Permission> page, String permissionName, String permissionCode, Integer type, Integer status) {
        String permissionType = type != null ? type.toString() : null;
        return permissionMapper.selectPermissionPage(page, permissionName, permissionCode, permissionType, status);
    }

    @Override
    public List<Permission> getPermissionTree(Integer type, Integer status) {
        String permissionType = type != null ? type.toString() : null;
        List<Permission> allPermissions = permissionMapper.selectPermissionTree(permissionType, status);
        return buildPermissionTree(allPermissions, 0L);
    }

    @Override
    public List<Permission> getChildPermissions(Long parentId, Integer type, Integer status) {
        if (parentId == null) {
            parentId = 0L;
        }
        return permissionMapper.selectChildrenPermissions(parentId);
    }

    @Override
    public List<Permission> getPermissionsByRoleId(Long roleId, Integer type) {
        if (roleId == null) {
            return new ArrayList<>();
        }
        return permissionMapper.selectPermissionsByRoleId(roleId);
    }

    @Override
    public List<Permission> getPermissionsByUserId(Long userId, Integer type) {
        if (userId == null) {
            return new ArrayList<>();
        }
        return permissionMapper.selectPermissionsByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createPermission(Permission permission) {
        // 参数校验
        if (permission == null || !StringUtils.hasText(permission.getPermissionName()) || !StringUtils.hasText(permission.getPermissionCode())) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "权限名称和权限编码不能为空");
        }

        // 检查权限编码是否已存在
        if (isPermissionCodeExists(permission.getPermissionCode(), null)) {
            throw new BusinessException(ResultCode.DATA_ALREADY_EXISTS.getCode(), "权限编码已存在");
        }

        // 检查权限名称是否已存在
        if (isPermissionNameExists(permission.getPermissionName(), null)) {
            throw new BusinessException(ResultCode.DATA_ALREADY_EXISTS.getCode(), "权限名称已存在");
        }

        // 检查父权限是否存在
        if (permission.getParentId() != null && permission.getParentId() > 0) {
            Permission parentPermission = this.getById(permission.getParentId());
            if (parentPermission == null || parentPermission.getDeleted() == 1) {
                throw new BusinessException(ResultCode.DATA_NOT_FOUND.getCode(), "父权限不存在");
            }
        } else {
            permission.setParentId(0L);
        }

        // 设置默认值
        permission.setCreateTime(LocalDateTime.now());
        permission.setUpdateTime(LocalDateTime.now());
        if (permission.getStatus() == null) {
            permission.setStatus(1); // 默认启用
        }
        if (permission.getDeleted() == null) {
            permission.setDeleted(0); // 默认未删除
        }
        if (permission.getSortOrder() == null) {
             permission.setSortOrder(0); // 默认排序
         }
         if (permission.getPermissionType() == null) {
             permission.setPermissionType(1); // 默认菜单类型
         }

        return this.save(permission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePermission(Permission permission) {
        // 参数校验
        if (permission == null || permission.getId() == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "权限ID不能为空");
        }

        // 检查权限是否存在
        Permission existPermission = this.getById(permission.getId());
        if (existPermission == null || existPermission.getDeleted() == 1) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND.getCode(), "权限不存在");
        }

        // 检查权限编码是否已存在（排除自己）
        if (StringUtils.hasText(permission.getPermissionCode()) && isPermissionCodeExists(permission.getPermissionCode(), permission.getId())) {
            throw new BusinessException(ResultCode.DATA_ALREADY_EXISTS.getCode(), "权限编码已存在");
        }

        // 检查权限名称是否已存在（排除自己）
        if (StringUtils.hasText(permission.getPermissionName()) && isPermissionNameExists(permission.getPermissionName(), permission.getId())) {
            throw new BusinessException(ResultCode.DATA_ALREADY_EXISTS.getCode(), "权限名称已存在");
        }

        // 检查父权限是否存在且不能设置自己为父权限
        if (permission.getParentId() != null) {
            if (permission.getParentId().equals(permission.getId())) {
                throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "不能设置自己为父权限");
            }
            if (permission.getParentId() > 0) {
                Permission parentPermission = this.getById(permission.getParentId());
                if (parentPermission == null || parentPermission.getDeleted() == 1) {
                    throw new BusinessException(ResultCode.DATA_NOT_FOUND.getCode(), "父权限不存在");
                }
                // 检查是否会形成循环引用
                if (isCircularReference(permission.getId(), permission.getParentId())) {
                    throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "不能设置子权限为父权限，会形成循环引用");
                }
            }
        }

        // 设置更新时间
        permission.setUpdateTime(LocalDateTime.now());

        return this.updateById(permission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePermission(Long permissionId) {
        if (permissionId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "权限ID不能为空");
        }

        // 检查权限是否存在
        Permission permission = this.getById(permissionId);
        if (permission == null || permission.getDeleted() == 1) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND.getCode(), "权限不存在");
        }

        // 检查是否有子权限
        if (hasChildPermissions(permissionId)) {
            throw new BusinessException(ResultCode.DATA_IN_USE.getCode(), "存在子权限，无法删除");
        }

        // 逻辑删除
        permission.setDeleted(1);
        permission.setUpdateTime(LocalDateTime.now());

        return this.updateById(permission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePermissions(List<Long> permissionIds) {
        if (permissionIds == null || permissionIds.isEmpty()) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "权限ID列表不能为空");
        }

        // 检查是否有子权限
        for (Long permissionId : permissionIds) {
            if (hasChildPermissions(permissionId)) {
                throw new BusinessException(ResultCode.DATA_IN_USE.getCode(), "存在子权限，无法删除");
            }
        }

        // 批量逻辑删除
        List<Permission> permissions = new ArrayList<>();
        for (Long permissionId : permissionIds) {
            Permission permission = new Permission();
            permission.setId(permissionId);
            permission.setDeleted(1);
            permission.setUpdateTime(LocalDateTime.now());
            permissions.add(permission);
        }
        return this.updateBatchById(permissions);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePermissionStatus(Long permissionId, Integer status) {
        if (permissionId == null || status == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "权限ID和状态不能为空");
        }

        // 检查权限是否存在
        Permission permission = this.getById(permissionId);
        if (permission == null || permission.getDeleted() == 1) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND.getCode(), "权限不存在");
        }

        return permissionMapper.updateStatus(permissionId, status) > 0;
    }

    @Override
    public boolean isPermissionCodeExists(String permissionCode, Long excludeId) {
        if (!StringUtils.hasText(permissionCode)) {
            return false;
        }
        return permissionMapper.checkPermissionCodeExists(permissionCode, excludeId) > 0;
    }

    @Override
    public boolean isPermissionNameExists(String permissionName, Long excludeId) {
        if (!StringUtils.hasText(permissionName)) {
            return false;
        }
        return permissionMapper.checkPermissionNameExists(permissionName, null, excludeId) > 0;
    }

    @Override
    public List<Long> getAllChildPermissionIds(Long permissionId) {
        if (permissionId == null) {
            return new ArrayList<>();
        }
        return permissionMapper.selectAllChildrenIds(permissionId);
    }

    @Override
    public boolean hasChildPermissions(Long permissionId) {
        if (permissionId == null) {
            return false;
        }
        return permissionMapper.countChildrenPermissions(permissionId) > 0;
    }

    @Override
    public List<Permission> getAllMenuPermissions() {
        return permissionMapper.selectMenuPermissions(null);
    }

    @Override
    public List<Permission> getAllButtonPermissions() {
        return permissionMapper.selectButtonPermissions(null, null);
    }

    @Override
    public List<Permission> getButtonPermissionsByParentId(Long parentId) {
        if (parentId == null) {
            return new ArrayList<>();
        }
        return permissionMapper.selectButtonPermissions(null, parentId);
    }

    @Override
    public List<Permission> buildPermissionTree(List<Permission> permissions, Long parentId) {
        if (permissions == null || permissions.isEmpty()) {
            return new ArrayList<>();
        }

        List<Permission> tree = new ArrayList<>();
        for (Permission permission : permissions) {
            if (parentId.equals(permission.getParentId())) {
                List<Permission> children = buildPermissionTree(permissions, permission.getId());
                permission.setChildren(children);
                tree.add(permission);
            }
        }
        return tree;
    }

    @Override
    public List<Permission> getUserMenuTree(Long userId) {
        if (userId == null) {
            return new ArrayList<>();
        }
        List<Permission> menuPermissions = getPermissionsByUserId(userId, 1);
        return buildPermissionTree(menuPermissions, 0L);
    }

    @Override
    public List<String> getUserButtonPermissions(Long userId) {
        if (userId == null) {
            return new ArrayList<>();
        }
        
        // 获取用户的按钮权限
        List<Permission> buttonPermissions = getPermissionsByUserId(userId, 2);
        
        // 提取按钮权限编码
        return buttonPermissions.stream()
                .map(Permission::getPermissionCode)
                .collect(Collectors.toList());
    }

    /**
     * 检查是否会形成循环引用
     *
     * @param permissionId 权限ID
     * @param parentId     父权限ID
     * @return 是否会形成循环引用
     */
    private boolean isCircularReference(Long permissionId, Long parentId) {
        if (parentId == null || parentId == 0) {
            return false;
        }
        
        // 获取所有子权限ID
        List<Long> childIds = getAllChildPermissionIds(permissionId);
        return childIds.contains(parentId);
    }
}