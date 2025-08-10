package com.tsm.test;

import com.tsm.auth.entity.User;
import com.tsm.auth.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 简化的用户管理功能测试类
 */
@SpringBootTest(classes = com.tsm.web.TsmWebApplication.class)
@ActiveProfiles("test")
public class SimpleUserTest {

    @Autowired
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        // 创建测试用户，使用时间戳确保唯一性
        long timestamp = System.currentTimeMillis();
        testUser = new User();
        testUser.setUsername("testuser" + timestamp);
        testUser.setPassword("123456");
        testUser.setRealName("测试用户");
        testUser.setEmail("test" + timestamp + "@example.com");
        testUser.setPhone("138" + String.valueOf(timestamp).substring(8));
        testUser.setStatus(0);
        testUser.setRemark("测试用户备注");
    }

    @Test
    void testCreateUser() {
        User createdUser = userService.createUser(testUser);
        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertEquals("测试用户", createdUser.getRealName());
        assertEquals(testUser.getEmail(), createdUser.getEmail());
        assertEquals(testUser.getPhone(), createdUser.getPhone());
        assertEquals(0, createdUser.getStatus());
    }

    @Test
    void testGetUserById() {
        // 创建用户
        User createdUser = userService.createUser(testUser);
        
        // 根据ID获取用户
        User foundUser = userService.getUserById(createdUser.getId());
        
        assertNotNull(foundUser);
        assertEquals(createdUser.getId(), foundUser.getId());
        assertEquals(testUser.getUsername(), foundUser.getUsername());
    }

    @Test
    void testGetUserByUsername() {
        // 创建用户
        userService.createUser(testUser);
        
        // 根据用户名获取用户
        User foundUser = userService.getUserByUsername("testuser");
        
        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUsername());
    }

    @Test
    void testUpdateUser() {
        // 创建用户
        User createdUser = userService.createUser(testUser);
        
        // 更新用户信息
        createdUser.setRealName("更新后的姓名");
        createdUser.setEmail("updated@example.com");
        
        User updatedUser = userService.updateUser(createdUser);
        
        assertNotNull(updatedUser);
        assertEquals("更新后的姓名", updatedUser.getRealName());
        assertEquals("updated@example.com", updatedUser.getEmail());
    }

    @Test
    void testDeleteUser() {
        // 创建用户
        User createdUser = userService.createUser(testUser);
        
        // 删除用户
        boolean deleted = userService.deleteUser(createdUser.getId());
        assertTrue(deleted);
        
        // 验证用户已被删除（逻辑删除）
        User deletedUser = userService.getUserById(createdUser.getId());
        assertNull(deletedUser);
    }

    @Test
    void testUsernameExists() {
        // 创建用户
        userService.createUser(testUser);
        
        // 检查用户名是否存在
        assertTrue(userService.isUsernameExists("testuser"));
        assertFalse(userService.isUsernameExists("nonexistent"));
    }

    @Test
    void testEmailExists() {
        // 创建用户
        userService.createUser(testUser);
        
        // 检查邮箱是否存在
        assertTrue(userService.isEmailExists(testUser.getEmail()));
        assertFalse(userService.isEmailExists("nonexistent@example.com"));
    }

    @Test
    void testPhoneExists() {
        // 创建用户
        userService.createUser(testUser);
        
        // 检查手机号是否存在
        assertTrue(userService.isPhoneExists("13800138000"));
        assertFalse(userService.isPhoneExists("13900139999"));
    }

    @Test
    void testUpdateUserStatus() {
        // 创建用户
        User createdUser = userService.createUser(testUser);
        
        // 更新用户状态
        boolean updated = userService.updateUserStatus(createdUser.getId(), 1);
        assertTrue(updated);
        
        // 验证状态更新
        User updatedUser = userService.getUserById(createdUser.getId());
        assertEquals(1, updatedUser.getStatus());
    }

    @Test
    void testResetPassword() {
        // 创建用户
        User createdUser = userService.createUser(testUser);
        
        // 重置密码
        boolean reset = userService.resetPassword(createdUser.getId(), "newpassword123");
        assertTrue(reset);
    }

    @Test
    void testChangePassword() {
        // 创建用户
        User createdUser = userService.createUser(testUser);
        
        // 修改密码
        boolean changed = userService.changePassword(createdUser.getId(), "123456", "newpassword123");
        assertTrue(changed);
    }

    @Test
    void testChangePasswordWithWrongOldPassword() {
        // 创建用户
        User createdUser = userService.createUser(testUser);
        
        // 使用错误的旧密码修改密码
        assertThrows(RuntimeException.class, () -> {
            userService.changePassword(createdUser.getId(), "wrongpassword", "newpassword123");
        });
    }
}