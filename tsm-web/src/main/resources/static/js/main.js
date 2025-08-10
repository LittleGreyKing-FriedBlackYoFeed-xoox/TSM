/**
 * TSM - 主页面JavaScript
 * 包含主页面的所有功能逻辑
 */

layui.use(['element', 'layer', 'util'], function(){
    var element = layui.element;
    var layer = layui.layer;
    var util = layui.util;

    // 页面初始化
    $(document).ready(function() {
        // 检查登录状态
        checkLoginStatus();
        
        // 加载用户信息
        loadUserInfo();
        
        // 加载统计数据
        loadStatistics();
        
        // 加载按钮权限
        loadButtonPermissions();
        
        // 初始化面包屑导航
        initBreadcrumb();
    });

    /**
     * 检查登录状态
     */
    function checkLoginStatus() {
        var token = localStorage.getItem('token');
        if (!token) {
            layer.msg('请先登录', {icon: 2}, function(){
                window.location.href = '/tsm/login';
            });
            return false;
        }
        return true;
    }

    /**
     * 加载用户信息
     */
    function loadUserInfo() {
        var user = localStorage.getItem('user');
        if (user) {
            try {
                var userObj = JSON.parse(user);
                $('#username').text(userObj.username || '用户');
                
                // 更新面包屑中的用户信息
                if (window.updateBreadcrumbUser) {
                    window.updateBreadcrumbUser(userObj);
                }
            } catch (e) {
                console.error('解析用户信息失败:', e);
            }
        }
    }

    /**
     * 加载统计数据
     */
    function loadStatistics() {
        // 模拟统计数据，实际项目中应该从API获取
        var stats = {
            userCount: 156,
            roleCount: 8,
            permissionCount: 45,
            buttonCount: 128
        };

        // 动画效果更新数字
        animateNumber('#userCount', stats.userCount);
        animateNumber('#roleCount', stats.roleCount);
        animateNumber('#permissionCount', stats.permissionCount);
        animateNumber('#buttonCount', stats.buttonCount);

        // 如果有API，使用以下代码
        /*
        fetch('/tsm/api/statistics', {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('token'),
                'Content-Type': 'application/json'
            }
        })
        .then(response => response.json())
        .then(data => {
            if (data.code === 200) {
                animateNumber('#userCount', data.data.userCount);
                animateNumber('#roleCount', data.data.roleCount);
                animateNumber('#permissionCount', data.data.permissionCount);
                animateNumber('#buttonCount', data.data.buttonCount);
            }
        })
        .catch(error => {
            console.error('加载统计数据失败:', error);
        });
        */
    }

    /**
     * 数字动画效果
     */
    function animateNumber(selector, targetNumber) {
        var $element = $(selector);
        var currentNumber = 0;
        var increment = Math.ceil(targetNumber / 50);
        
        var timer = setInterval(function() {
            currentNumber += increment;
            if (currentNumber >= targetNumber) {
                currentNumber = targetNumber;
                clearInterval(timer);
            }
            $element.text(currentNumber);
        }, 30);
    }

    /**
     * 加载按钮权限
     */
    function loadButtonPermissions() {
        var token = localStorage.getItem('token');
        if (!token) return;

        // 模拟按钮权限数据
        var permissions = ['add_user', 'edit_user', 'view_user', 'export_data'];
        updateButtonDisplay(permissions);

        // 如果有API，使用以下代码
        /*
        fetch('/tsm/api/user/buttons', {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json'
            }
        })
        .then(response => response.json())
        .then(data => {
            if (data.code === 200) {
                updateButtonDisplay(data.data);
            }
        })
        .catch(error => {
            console.error('加载按钮权限失败:', error);
        });
        */
    }

    /**
     * 更新按钮显示
     */
    function updateButtonDisplay(permissions) {
        var buttonContainer = $('#buttonContainer');
        var buttons = buttonContainer.find('.demo-button');
        
        // 按钮权限映射
        var buttonPermissions = {
            'add_user': 0,      // 新增用户
            'edit_user': 1,     // 编辑用户
            'delete_user': 2,   // 删除用户
            'view_user': 3,     // 查看用户
            'export_data': 4,   // 导出数据
            'import_data': 5,   // 导入数据
            'assign_permission': 6  // 权限分配
        };

        // 隐藏所有按钮
        buttons.hide();

        // 根据权限显示按钮
        permissions.forEach(function(permission) {
            var buttonIndex = buttonPermissions[permission];
            if (buttonIndex !== undefined) {
                buttons.eq(buttonIndex).show().addClass('fadeInUp');
            }
        });

        // 如果没有权限，显示提示
        if (permissions.length === 0) {
            buttonContainer.html('<div style="text-align: center; color: var(--primary-color); padding: 20px;">暂无按钮权限</div>');
        }
    }

    /**
     * 初始化面包屑导航
     */
    function initBreadcrumb() {
        if (window.addBreadcrumb) {
            window.addBreadcrumb('首页', '/tsm/main');
        }
    }

    /**
     * 显示用户信息
     */
    window.showUserInfo = function() {
        var user = localStorage.getItem('user');
        if (user) {
            try {
                var userObj = JSON.parse(user);
                layer.open({
                    type: 1,
                    title: '个人信息',
                    area: ['400px', '300px'],
                    content: `
                        <div style="padding: 20px;">
                            <div style="margin-bottom: 15px;">
                                <strong>用户名：</strong>${userObj.username || '未知'}
                            </div>
                            <div style="margin-bottom: 15px;">
                                <strong>角色：</strong>${userObj.roleName || '未分配'}
                            </div>
                            <div style="margin-bottom: 15px;">
                                <strong>最后登录：</strong>${new Date().toLocaleString()}
                            </div>
                            <div style="text-align: center; margin-top: 20px;">
                                <button class="layui-btn layui-btn-sm" onclick="changePassword()">修改密码</button>
                            </div>
                        </div>
                    `
                });
            } catch (e) {
                layer.msg('用户信息解析失败', {icon: 2});
            }
        }
    };

    /**
     * 修改密码
     */
    window.changePassword = function() {
        layer.open({
            type: 1,
            title: '修改密码',
            area: ['400px', '350px'],
            content: `
                <form class="layui-form" style="padding: 20px;">
                    <div class="layui-form-item">
                        <label class="layui-form-label">原密码</label>
                        <div class="layui-input-block">
                            <input type="password" name="oldPassword" required lay-verify="required" 
                                   placeholder="请输入原密码" class="layui-input">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">新密码</label>
                        <div class="layui-input-block">
                            <input type="password" name="newPassword" required lay-verify="required" 
                                   placeholder="请输入新密码" class="layui-input">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">确认密码</label>
                        <div class="layui-input-block">
                            <input type="password" name="confirmPassword" required lay-verify="required" 
                                   placeholder="请确认新密码" class="layui-input">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <div class="layui-input-block">
                            <button class="layui-btn" lay-submit lay-filter="changePassword">确认修改</button>
                            <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                        </div>
                    </div>
                </form>
            `,
            success: function(layero, index) {
                // 重新渲染表单
                layui.form.render();
                
                // 监听表单提交
                layui.form.on('submit(changePassword)', function(data) {
                    // 这里应该调用修改密码的API
                    layer.msg('密码修改功能待实现', {icon: 1});
                    layer.close(index);
                    return false;
                });
            }
        });
    };

    /**
     * 退出登录
     */
    window.logout = function() {
        layer.confirm('确定要退出登录吗？', {
            icon: 3,
            title: '确认退出'
        }, function(index) {
            // 清除本地存储
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            
            layer.msg('退出成功', {icon: 1}, function(){
                window.location.href = '/tsm/login';
            });
            
            layer.close(index);
        });
    };

    /**
     * 加载内容
     */
    window.loadContent = function(type) {
        // 更新面包屑
        var breadcrumbMap = {
            'dashboard': '首页',
            'user': '用户列表',
            'role': '角色管理',
            'permission': '权限列表',
            'button': '按钮管理',
            'permission-assign': '权限分配'
        };
        
        if (window.addBreadcrumb && breadcrumbMap[type]) {
            window.addBreadcrumb(breadcrumbMap[type], '#');
        }
        
        // 这里可以实现具体的内容加载逻辑
        layer.msg('加载 ' + (breadcrumbMap[type] || type) + ' 页面', {icon: 1});
    };

    // CSS动画类
    var style = document.createElement('style');
    style.textContent = `
        .fadeInUp {
            animation: fadeInUp 0.5s ease-out;
        }
        
        @keyframes fadeInUp {
            from {
                opacity: 0;
                transform: translateY(20px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }
    `;
    document.head.appendChild(style);
});