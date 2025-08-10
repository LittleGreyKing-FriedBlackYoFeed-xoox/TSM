package com.tsm.auth.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tsm.auth.entity.User;
import com.tsm.auth.service.UserService;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 用户管理服务模拟实现类（内存存储）
 */
@Service
@Primary
public class UserServiceMockImpl implements UserService {

    private final ConcurrentHashMap<Long, User> userStorage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserServiceMockImpl() {
        // 初始化一些测试数据
        initTestData();
    }

    private void initTestData() {
        User admin = new User();
        admin.setId(1L);
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("123456"));
        admin.setRealName("系统管理员");
        admin.setEmail("admin@tsm.com");
        admin.setPhone("13800138000");
        admin.setStatus(0);
        admin.setDeleted(0);
        admin.setCreateTime(LocalDateTime.now());
        admin.setUpdateTime(LocalDateTime.now());
        userStorage.put(1L, admin);

        User testUser = new User();
        testUser.setId(2L);
        testUser.setUsername("testuser");
        testUser.setPassword(passwordEncoder.encode("123456"));
        testUser.setRealName("测试用户");
        testUser.setEmail("test@tsm.com");
        testUser.setPhone("13800138001");
        testUser.setStatus(0);
        testUser.setDeleted(0);
        testUser.setCreateTime(LocalDateTime.now());
        testUser.setUpdateTime(LocalDateTime.now());
        userStorage.put(2L, testUser);

        idGenerator.set(3L);
    }

    @Override
    public IPage<User> getUserList(int page, int size, String username, String realName, Integer status) {
        List<User> allUsers = userStorage.values().stream()
                .filter(user -> user.getDeleted() == 0)
                .collect(Collectors.toList());

        // 应用过滤条件
        if (StringUtils.hasText(username)) {
            allUsers = allUsers.stream()
                    .filter(user -> user.getUsername().contains(username))
                    .collect(Collectors.toList());
        }
        if (StringUtils.hasText(realName)) {
            allUsers = allUsers.stream()
                    .filter(user -> user.getRealName() != null && user.getRealName().contains(realName))
                    .collect(Collectors.toList());
        }
        if (status != null) {
            allUsers = allUsers.stream()
                    .filter(user -> user.getStatus().equals(status))
                    .collect(Collectors.toList());
        }

        // 分页
        int start = (page - 1) * size;
        int end = Math.min(start + size, allUsers.size());
        List<User> pageData = start < allUsers.size() ? allUsers.subList(start, end) : new ArrayList<>();

        Page<User> pageResult = new Page<>(page, size, allUsers.size());
        pageResult.setRecords(pageData);
        return pageResult;
    }

    @Override
    public User getUserById(Long id) {
        User user = userStorage.get(id);
        return (user != null && user.getDeleted() == 0) ? user : null;
    }

    @Override
    public User getUserByUsername(String username) {
        return userStorage.values().stream()
                .filter(user -> user.getDeleted() == 0 && user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public User createUser(User user) {
        try {
            // 检查用户名是否已存在
            if (isUsernameExists(user.getUsername())) {
                throw new RuntimeException("用户名已存在");
            }

            // 检查邮箱是否已存在
            if (StringUtils.hasText(user.getEmail()) && isEmailExists(user.getEmail())) {
                throw new RuntimeException("邮箱已存在");
            }

            // 检查手机号是否已存在
            if (StringUtils.hasText(user.getPhone()) && isPhoneExists(user.getPhone())) {
                throw new RuntimeException("手机号已存在");
            }

            // 设置ID
            user.setId(idGenerator.getAndIncrement());

            // 加密密码
            if (StringUtils.hasText(user.getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            } else {
                user.setPassword(passwordEncoder.encode("123456"));
            }

            // 设置默认值
            user.setCreateTime(LocalDateTime.now());
            user.setUpdateTime(LocalDateTime.now());
            user.setDeleted(0);
            if (user.getStatus() == null) {
                user.setStatus(0);
            }

            userStorage.put(user.getId(), user);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("创建用户失败：" + e.getMessage());
        }
    }

    @Override
    public User updateUser(User user) {
        try {
            User existingUser = getUserById(user.getId());
            if (existingUser == null) {
                throw new RuntimeException("用户不存在");
            }

            // 检查用户名是否被其他用户使用
            if (!existingUser.getUsername().equals(user.getUsername()) && isUsernameExists(user.getUsername())) {
                throw new RuntimeException("用户名已被其他用户使用");
            }

            // 检查邮箱是否被其他用户使用
            if (StringUtils.hasText(user.getEmail()) &&
                    !user.getEmail().equals(existingUser.getEmail()) &&
                    isEmailExists(user.getEmail())) {
                throw new RuntimeException("邮箱已被其他用户使用");
            }

            // 检查手机号是否被其他用户使用
            if (StringUtils.hasText(user.getPhone()) &&
                    !user.getPhone().equals(existingUser.getPhone()) &&
                    isPhoneExists(user.getPhone())) {
                throw new RuntimeException("手机号已被其他用户使用");
            }

            // 更新字段（保留原密码）
            existingUser.setUsername(user.getUsername());
            existingUser.setRealName(user.getRealName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPhone(user.getPhone());
            existingUser.setStatus(user.getStatus());
            existingUser.setUpdateTime(LocalDateTime.now());

            return existingUser;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("更新用户失败：" + e.getMessage());
        }
    }

    @Override
    public boolean deleteUser(Long id) {
        try {
            User user = getUserById(id);
            if (user == null) {
                throw new RuntimeException("用户不存在");
            }

            user.setDeleted(1);
            user.setUpdateTime(LocalDateTime.now());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("删除用户失败：" + e.getMessage());
        }
    }

    @Override
    public boolean batchDeleteUsers(List<Long> ids) {
        try {
            for (Long id : ids) {
                deleteUser(id);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("批量删除用户失败：" + e.getMessage());
        }
    }

    @Override
    public boolean updateUserStatus(Long id, Integer status) {
        try {
            User user = getUserById(id);
            if (user == null) {
                throw new RuntimeException("用户不存在");
            }

            user.setStatus(status);
            user.setUpdateTime(LocalDateTime.now());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("更新用户状态失败：" + e.getMessage());
        }
    }

    @Override
    public boolean resetPassword(Long id, String newPassword) {
        try {
            User user = getUserById(id);
            if (user == null) {
                throw new RuntimeException("用户不存在");
            }

            user.setPassword(passwordEncoder.encode(newPassword));
            user.setUpdateTime(LocalDateTime.now());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("重置密码失败：" + e.getMessage());
        }
    }

    @Override
    public boolean changePassword(Long id, String oldPassword, String newPassword) {
        try {
            User user = getUserById(id);
            if (user == null) {
                throw new RuntimeException("用户不存在");
            }

            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                throw new RuntimeException("原密码错误");
            }

            user.setPassword(passwordEncoder.encode(newPassword));
            user.setUpdateTime(LocalDateTime.now());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("修改密码失败：" + e.getMessage());
        }
    }

    @Override
    public boolean isUsernameExists(String username) {
        return userStorage.values().stream()
                .anyMatch(user -> user.getDeleted() == 0 && user.getUsername().equals(username));
    }

    @Override
    public boolean isEmailExists(String email) {
        return userStorage.values().stream()
                .anyMatch(user -> user.getDeleted() == 0 && email.equals(user.getEmail()));
    }

    @Override
    public boolean isPhoneExists(String phone) {
        return userStorage.values().stream()
                .anyMatch(user -> user.getDeleted() == 0 && phone.equals(user.getPhone()));
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.values().stream()
                .filter(user -> user.getDeleted() == 0)
                .collect(Collectors.toList());
    }
}