/**
 * 用户表单页面JavaScript
 * 负责用户新增和编辑表单的处理
 */

layui.use(['form', 'layer'], function() {
    var form = layui.form;
    var layer = layui.layer;

    // 自定义验证规则
    form.verify({
        username: function(value) {
            if (!value) {
                return '用户名不能为空';
            }
            if (!/^[a-zA-Z0-9_]{3,20}$/.test(value)) {
                return '用户名只能包含字母、数字、下划线，长度3-20位';
            }
        },
        realName: function(value) {
            if (!value) {
                return '真实姓名不能为空';
            }
            if (value.length < 2 || value.length > 20) {
                return '真实姓名长度应在2-20位之间';
            }
        },
        phone: function(value) {
            if (value && !/^1[3-9]\d{9}$/.test(value)) {
                return '手机号格式不正确';
            }
        },
        password: function(value) {
            var isEdit = $('input[name="id"]').val();
            if (!isEdit && !value) {
                return '密码不能为空';
            }
            if (value && value.length < 6) {
                return '密码长度不能少于6位';
            }
        },
        confirmPassword: function(value) {
            var password = $('input[name="password"]').val();
            var isEdit = $('input[name="id"]').val();
            if (!isEdit && value !== password) {
                return '两次密码输入不一致';
            }
        }
    });

    // 监听表单提交
    form.on('submit(userSubmit)', function(data) {
        var field = data.field;
        var isEdit = field.id;
        
        // 移除确认密码字段
        delete field.confirmPassword;
        
        // 如果是编辑且密码为空，则不传递密码字段
        if (isEdit && !field.password) {
            delete field.password;
        }
        
        var url = isEdit ? '/api/user/update' : '/api/user/create';
        var method = isEdit ? 'PUT' : 'POST';
        
        // 显示加载层
        var loadIndex = layer.load(2, {shade: [0.3, '#000']});
        
        $.ajax({
            url: url,
            type: method,
            contentType: 'application/json',
            data: JSON.stringify(field),
            success: function(res) {
                layer.close(loadIndex);
                
                if (res.code === 0) {
                    layer.msg(isEdit ? '更新成功' : '创建成功', {
                        icon: 1,
                        time: 2000
                    }, function() {
                        // 关闭当前弹窗
                        var index = parent.layer.getFrameIndex(window.name);
                        parent.layer.close(index);
                    });
                } else {
                    layer.msg(res.msg || (isEdit ? '更新失败' : '创建失败'), {
                        icon: 2
                    });
                }
            },
            error: function(xhr) {
                layer.close(loadIndex);
                
                var errorMsg = '网络错误';
                if (xhr.responseJSON && xhr.responseJSON.msg) {
                    errorMsg = xhr.responseJSON.msg;
                }
                
                layer.msg(errorMsg, {
                    icon: 2
                });
            }
        });
        
        return false;
    });

    // 检查用户名是否已存在
    $('input[name="username"]').on('blur', function() {
        var username = $(this).val();
        var userId = $('input[name="id"]').val();
        
        if (!username) return;
        
        $.ajax({
            url: '/api/user/check-username',
            type: 'GET',
            data: {
                username: username,
                excludeId: userId
            },
            success: function(res) {
                if (res.code === 1) {
                    layer.tips('用户名已存在', 'input[name="username"]', {
                        tips: [1, '#FF5722'],
                        time: 3000
                    });
                    $('input[name="username"]').focus();
                }
            }
        });
    });

    // 检查邮箱是否已存在
    $('input[name="email"]').on('blur', function() {
        var email = $(this).val();
        var userId = $('input[name="id"]').val();
        
        if (!email) return;
        
        $.ajax({
            url: '/api/user/check-email',
            type: 'GET',
            data: {
                email: email,
                excludeId: userId
            },
            success: function(res) {
                if (res.code === 1) {
                    layer.tips('邮箱已存在', 'input[name="email"]', {
                        tips: [1, '#FF5722'],
                        time: 3000
                    });
                    $('input[name="email"]').focus();
                }
            }
        });
    });

    // 密码强度检测
    $('input[name="password"]').on('input', function() {
        var password = $(this).val();
        if (!password) return;
        
        var strength = checkPasswordStrength(password);
        var tips = '';
        var color = '';
        
        switch (strength) {
            case 1:
                tips = '密码强度：弱';
                color = '#FF5722';
                break;
            case 2:
                tips = '密码强度：中';
                color = '#FF9800';
                break;
            case 3:
                tips = '密码强度：强';
                color = '#4CAF50';
                break;
        }
        
        if (tips) {
            layer.tips(tips, this, {
                tips: [1, color],
                time: 2000
            });
        }
    });

    // 密码强度检测函数
    function checkPasswordStrength(password) {
        var strength = 0;
        
        // 长度检查
        if (password.length >= 8) strength++;
        
        // 包含数字
        if (/\d/.test(password)) strength++;
        
        // 包含小写字母
        if (/[a-z]/.test(password)) strength++;
        
        // 包含大写字母
        if (/[A-Z]/.test(password)) strength++;
        
        // 包含特殊字符
        if (/[^a-zA-Z0-9]/.test(password)) strength++;
        
        if (strength <= 2) return 1; // 弱
        if (strength <= 3) return 2; // 中
        return 3; // 强
    }
});