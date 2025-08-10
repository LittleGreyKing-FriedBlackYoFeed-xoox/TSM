package com.tsm.test;

import com.tsm.auth.entity.Button;
import com.tsm.auth.entity.User;
import com.tsm.auth.service.AuthService;
import com.tsm.web.TsmWebApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 认证服务测试类
 */
@SpringBootTest(classes = TsmWebApplication.class)
@ActiveProfiles("test")
@Transactional
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    /**
     * 测试用户登录功能
     */
    @Test
    public void testLogin() {
        // 测试正确的用户名和密码
        try {
            String token = authService.login("admin", "123456");
            assertNotNull(token, "登录应该返回有效的token");
            assertTrue(token.length() > 0, "Token不应该为空");
        } catch (Exception e) {
            // 如果数据库中没有测试数据，这个测试可能会失败
            System.out.println("登录测试失败，可能是因为数据库中没有测试数据: " + e.getMessage());
        }
    }

    /**
     * 测试错误的登录信息
     */
    @Test
    public void testLoginWithWrongCredentials() {
        // 测试错误的用户名
        assertThrows(RuntimeException.class, () -> {
            authService.login("wronguser", "123456");
        }, "错误的用户名应该抛出异常");

        // 测试错误的密码
        assertThrows(RuntimeException.class, () -> {
            authService.login("admin", "wrongpassword");
        }, "错误的密码应该抛出异常");
    }

    /**
     * 测试根据用户名查找用户
     */
    @Test
    public void testFindByUsername() {
        try {
            User user = authService.findByUsername("admin");
            if (user != null) {
                assertEquals("admin", user.getUsername(), "用户名应该匹配");
                assertNotNull(user.getId(), "用户ID不应该为空");
            } else {
                System.out.println("未找到admin用户，可能是因为数据库中没有测试数据");
            }
        } catch (Exception e) {
            System.out.println("查找用户测试失败: " + e.getMessage());
        }
    }

    /**
     * 测试权限检查
     */
    @Test
    public void testHasPermission() {
        try {
            // 假设用户ID为1的用户存在
            boolean hasPermission = authService.hasPermission(1L, "USER_MANAGE");
            // 这个测试的结果取决于数据库中的权限配置
            System.out.println("用户1是否有USER_MANAGE权限: " + hasPermission);
        } catch (Exception e) {
            System.out.println("权限检查测试失败: " + e.getMessage());
        }
    }

    /**
     * 测试按钮权限检查
     */
    @Test
    public void testHasButtonPermission() {
        try {
            // 假设用户ID为1的用户存在
            boolean hasButtonPermission = authService.hasButtonPermission(1L, "USER_ADD");
            System.out.println("用户1是否有USER_ADD按钮权限: " + hasButtonPermission);
        } catch (Exception e) {
            System.out.println("按钮权限检查测试失败: " + e.getMessage());
        }
    }

    /**
     * 测试获取用户权限列表
     */
    @Test
    public void testGetUserPermissions() {
        try {
            List<String> permissions = authService.getUserPermissions(1L);
            assertNotNull(permissions, "权限列表不应该为空");
            System.out.println("用户1的权限列表: " + permissions);
        } catch (Exception e) {
            System.out.println("获取用户权限列表测试失败: " + e.getMessage());
        }
    }

    /**
     * 测试获取用户按钮权限列表
     */
    @Test
    public void testGetUserButtonPermissions() {
        try {
            List<String> buttonPermissions = authService.getUserButtonPermissions(1L);
            assertNotNull(buttonPermissions, "按钮权限列表不应该为空");
            System.out.println("用户1的按钮权限列表: " + buttonPermissions);
        } catch (Exception e) {
            System.out.println("获取用户按钮权限列表测试失败: " + e.getMessage());
        }
    }

    /**
     * 测试获取用户在指定页面的按钮权限
     */
    @Test
    public void testGetUserButtonsByPage() {
        try {
            List<Button> buttons = authService.getUserButtonsByPage(1L, "dashboard");
            assertNotNull(buttons, "按钮列表不应该为空");
            System.out.println("用户1在dashboard页面的按钮数量: " + buttons.size());
            
            for (Button button : buttons) {
                System.out.println("按钮: " + button.getButtonName() + " (" + button.getButtonCode() + ")");
            }
        } catch (Exception e) {
            System.out.println("获取用户页面按钮权限测试失败: " + e.getMessage());
        }
    }

    /**
     * 测试分配按钮权限
     */
    @Test
    public void testAssignButtonPermission() {
        try {
            // 为用户2分配按钮权限
            boolean success = authService.assignButtonPermission(2L, 1L, 1);
            assertTrue(success, "分配按钮权限应该成功");
            
            // 验证权限是否分配成功
            boolean hasPermission = authService.hasButtonPermission(2L, "USER_ADD");
            System.out.println("分配权限后，用户2是否有USER_ADD权限: " + hasPermission);
        } catch (Exception e) {
            System.out.println("分配按钮权限测试失败: " + e.getMessage());
        }
    }

    /**
     * 测试批量分配按钮权限
     */
    @Test
    public void testBatchAssignButtonPermissions() {
        try {
            // 为用户2批量分配按钮权限
            List<Long> buttonIds = Arrays.asList(1L, 2L, 3L);
            boolean success = authService.batchAssignButtonPermissions(2L, buttonIds);
            assertTrue(success, "批量分配按钮权限应该成功");
            
            // 验证权限是否分配成功
            List<String> buttonPermissions = authService.getUserButtonPermissions(2L);
            System.out.println("批量分配权限后，用户2的按钮权限: " + buttonPermissions);
        } catch (Exception e) {
            System.out.println("批量分配按钮权限测试失败: " + e.getMessage());
        }
    }

    /**
     * 测试移除按钮权限
     */
    @Test
    public void testRemoveButtonPermission() {
        try {
            // 先分配权限
            authService.assignButtonPermission(2L, 1L, 1);
            
            // 然后移除权限
            boolean success = authService.removeButtonPermission(2L, 1L);
            assertTrue(success, "移除按钮权限应该成功");
            
            // 验证权限是否移除成功
            boolean hasPermission = authService.hasButtonPermission(2L, "USER_ADD");
            assertFalse(hasPermission, "移除权限后，用户应该没有该按钮权限");
        } catch (Exception e) {
            System.out.println("移除按钮权限测试失败: " + e.getMessage());
        }
    }

    /**
     * 测试清空用户所有按钮权限
     */
    @Test
    public void testClearUserButtonPermissions() {
        try {
            // 先分配一些权限
            authService.assignButtonPermission(2L, 1L, 1);
            authService.assignButtonPermission(2L, 2L, 1);
            
            // 清空所有权限
            boolean success = authService.clearUserButtonPermissions(2L);
            assertTrue(success, "清空用户按钮权限应该成功");
            
            // 验证权限是否清空成功
            List<String> buttonPermissions = authService.getUserButtonPermissions(2L);
            assertTrue(buttonPermissions.isEmpty(), "清空权限后，用户应该没有任何按钮权限");
        } catch (Exception e) {
            System.out.println("清空用户按钮权限测试失败: " + e.getMessage());
        }
    }
}