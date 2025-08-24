package com.tsm.user.service.impl;

import com.tsm.common.result.PageResult;
import com.tsm.user.dto.UserDTO;
import com.tsm.user.dto.UserQueryDTO;
import com.tsm.user.entity.User;
import com.tsm.user.entity.UserRole;
import com.tsm.user.mapper.UserMapper;
import com.tsm.user.service.UserService;
import com.tsm.user.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 * @author TSM
 */
@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @Override
    public PageResult<UserVO> getUserPage(UserQueryDTO queryDTO) {
        // 计算偏移量
        int offset = (int)((queryDTO.getPageNum() - 1) * queryDTO.getPageSize());
        queryDTO.setOffset(offset);
        
        // 查询用户列表
        List<User> users = userMapper.selectUserPage(queryDTO);
        
        // 查询总数
        long total = userMapper.countUserPage(queryDTO);
        
        // 转换为VO
        List<UserVO> userVOs = users.stream().map(this::convertToVO).collect(Collectors.toList());
        
        return new PageResult<>((long)queryDTO.getPageNum(), (long)queryDTO.getPageSize(), total, userVOs);
    }
    
    @Override
    public UserVO getUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return null;
        }
        
        UserVO userVO = convertToVO(user);
        
        // 查询用户角色
        List<UserRole> userRoles = userMapper.selectUserRoles(id);
        if (userRoles != null && !userRoles.isEmpty()) {
            List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
            // TODO: 查询角色详细信息，这里暂时只设置角色ID
            // userVO.setRoles(roleInfos);
        }
        
        return userVO;
    }
    
    @Override
    @Transactional
    public Long createUser(UserDTO userDTO) {
        // 检查用户名是否存在
        if (checkUsernameExists(userDTO.getUsername(), null)) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 检查邮箱是否存在
        if (StringUtils.hasText(userDTO.getEmail()) && checkEmailExists(userDTO.getEmail(), null)) {
            throw new RuntimeException("邮箱已存在");
        }
        
        // 检查手机号是否存在
        if (StringUtils.hasText(userDTO.getPhone()) && checkPhoneExists(userDTO.getPhone(), null)) {
            throw new RuntimeException("手机号已存在");
        }
        
        // 创建用户
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        
        // 加密密码
        if (StringUtils.hasText(userDTO.getPassword())) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        } else {
            // 默认密码
            user.setPassword(passwordEncoder.encode("123456"));
        }
        
        // 默认状态为启用
        if (user.getStatus() == null) {
            user.setStatus(1);
        }
        
        // 插入用户
        userMapper.insert(user);
        
        // 分配角色
        if (userDTO.getRoleIds() != null && !userDTO.getRoleIds().isEmpty()) {
            assignUserRoles(user.getId(), userDTO.getRoleIds());
        }
        
        return user.getId();
    }
    
    @Override
    @Transactional
    public boolean updateUser(UserDTO userDTO) {
        if (userDTO.getId() == null) {
            throw new RuntimeException("用户ID不能为空");
        }
        
        // 检查用户是否存在
        User existUser = userMapper.selectById(userDTO.getId());
        if (existUser == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 检查用户名是否存在
        if (StringUtils.hasText(userDTO.getUsername()) && checkUsernameExists(userDTO.getUsername(), userDTO.getId())) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 检查邮箱是否存在
        if (StringUtils.hasText(userDTO.getEmail()) && checkEmailExists(userDTO.getEmail(), userDTO.getId())) {
            throw new RuntimeException("邮箱已存在");
        }
        
        // 检查手机号是否存在
        if (StringUtils.hasText(userDTO.getPhone()) && checkPhoneExists(userDTO.getPhone(), userDTO.getId())) {
            throw new RuntimeException("手机号已存在");
        }
        
        // 更新用户
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        
        // 如果有新密码，则加密
        if (StringUtils.hasText(userDTO.getPassword())) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        } else {
            user.setPassword(null); // 不更新密码
        }
        
        int result = userMapper.update(user);
        
        // 更新角色
        if (userDTO.getRoleIds() != null) {
            assignUserRoles(userDTO.getId(), userDTO.getRoleIds());
        }
        
        return result > 0;
    }
    
    @Override
    @Transactional
    public boolean deleteUser(Long id) {
        if (id == null) {
            throw new RuntimeException("用户ID不能为空");
        }
        
        // 检查用户是否存在
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 删除用户角色关联
        userMapper.deleteUserRoles(id);
        
        // 删除用户
        int result = userMapper.deleteById(id);
        
        return result > 0;
    }
    
    @Override
    @Transactional
    public boolean batchDeleteUsers(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException("用户ID列表不能为空");
        }
        
        // 删除用户角色关联
        for (Long id : ids) {
            userMapper.deleteUserRoles(id);
        }
        
        // 批量删除用户
        int result = userMapper.batchDeleteByIds(ids);
        
        return result > 0;
    }
    
    @Override
    public boolean updateUserStatus(Long id, Integer status) {
        if (id == null) {
            throw new RuntimeException("用户ID不能为空");
        }
        
        if (status == null || (status != 0 && status != 1)) {
            throw new RuntimeException("状态值无效");
        }
        
        // 检查用户是否存在
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        int result = userMapper.updateStatus(id, status);
        
        return result > 0;
    }
    
    @Override
    public boolean resetPassword(Long id, String newPassword) {
        if (id == null) {
            throw new RuntimeException("用户ID不能为空");
        }
        
        // 检查用户是否存在
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 使用默认密码或指定密码
        String password = StringUtils.hasText(newPassword) ? newPassword : "123456";
        String encodedPassword = passwordEncoder.encode(password);
        
        int result = userMapper.resetPassword(id, encodedPassword);
        
        return result > 0;
    }
    
    @Override
    @Transactional
    public boolean assignUserRoles(Long userId, List<Long> roleIds) {
        if (userId == null) {
            throw new RuntimeException("用户ID不能为空");
        }
        
        // 删除原有角色关联
        userMapper.deleteUserRoles(userId);
        
        // 添加新的角色关联
        if (roleIds != null && !roleIds.isEmpty()) {
            List<UserRole> userRoles = roleIds.stream()
                    .map(roleId -> new UserRole(userId, roleId))
                    .collect(Collectors.toList());
            
            userMapper.batchInsertUserRoles(userRoles);
        }
        
        return true;
    }
    
    @Override
    public boolean checkUsernameExists(String username, Long excludeId) {
        if (!StringUtils.hasText(username)) {
            return false;
        }
        
        int count = userMapper.countByUsername(username, excludeId);
        return count > 0;
    }
    
    @Override
    public boolean checkEmailExists(String email, Long excludeId) {
        if (!StringUtils.hasText(email)) {
            return false;
        }
        
        int count = userMapper.countByEmail(email, excludeId);
        return count > 0;
    }
    
    @Override
    public boolean checkPhoneExists(String phone, Long excludeId) {
        if (!StringUtils.hasText(phone)) {
            return false;
        }
        
        int count = userMapper.countByPhone(phone, excludeId);
        return count > 0;
    }
    
    @Override
    public Object getUserStatistics() {
        return userMapper.getUserStatistics();
    }
    
    /**
     * 转换为VO
     */
    private UserVO convertToVO(User user) {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        
        return userVO;
    }
}