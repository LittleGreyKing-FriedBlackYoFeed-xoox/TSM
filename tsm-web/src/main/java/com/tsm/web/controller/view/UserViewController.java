package com.tsm.web.controller.view;

import com.tsm.auth.entity.User;
import com.tsm.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户视图控制器
 * 负责用户相关页面的跳转和数据准备
 */
@Controller
@RequestMapping("/user")
public class UserViewController {

    @Autowired
    private UserService userService;

    /**
     * 用户列表页面
     */
    @GetMapping("/list")
    public String userList() {
        return "views/user/user-list";
    }

    /**
     * 用户表单页面（新增/编辑）
     */
    @GetMapping("/form")
    public String userForm(@RequestParam(required = false) Long id, Model model) {
        if (id != null) {
            try {
                User user = userService.getUserById(id);
                if (user != null) {
                    // 不传递密码到前端
                    user.setPassword(null);
                    model.addAttribute("user", user);
                }
            } catch (Exception e) {
                // 记录日志，但不影响页面显示
                e.printStackTrace();
            }
        }
        return "views/user/user-form";
    }

    /**
     * 用户权限管理页面
     */
    @GetMapping("/permission")
    public String userPermission(@RequestParam Long id, Model model) {
        try {
            User user = userService.getUserById(id);
            if (user != null) {
                user.setPassword(null);
                model.addAttribute("user", user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "views/user/user-permission";
    }

    /**
     * 用户导入页面
     */
    @GetMapping("/import")
    public String userImport() {
        return "views/user/user-import";
    }

    /**
     * 用户详情页面
     */
    @GetMapping("/detail")
    public String userDetail(@RequestParam Long id, Model model) {
        try {
            User user = userService.getUserById(id);
            if (user != null) {
                user.setPassword(null);
                model.addAttribute("user", user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "views/user/user-detail";
    }
}