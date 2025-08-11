/**
 * 登录页面JavaScript
 * 负责用户登录功能的处理
 */

layui.use(['form', 'layer'], function() {
    var form = layui.form;
    var layer = layui.layer;

    // 自定义验证规则
    form.verify({
        username: function(value) {
            if (!value) {
                return '请输入用户名';
            }
        },
        password: function(value) {
            if (!value) {
                return '请输入密码';
            }
        }
    });

    // 监听登录表单提交
    form.on('submit(login)', function(data) {
        var field = data.field;
        
        // 显示加载层
        var loadIndex = layer.load(2, {
            shade: [0.3, '#000'],
            content: '登录中...'
        });
        
        $.ajax({
            url: '/api/auth/login',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                username: field.username,
                password: field.password
            }),
            success: function(res) {
                layer.close(loadIndex);
                
                if (res.code === 200) {
                    // 保存token和用户信息
                    localStorage.setItem('token', res.data.token);
                    localStorage.setItem('userInfo', JSON.stringify(res.data.user));
                    
                    // 如果选择了记住我，保存用户名
                    if (field.remember) {
                        localStorage.setItem('rememberedUsername', field.username);
                    } else {
                        localStorage.removeItem('rememberedUsername');
                    }
                    
                    layer.msg('登录成功', {
                        icon: 1,
                        time: 1500
                    }, function() {
                        // 跳转到主页
                        window.location.href = '/tsm/main';
                    });
                } else {
                    layer.msg(res.message || '登录失败', {
                        icon: 2,
                        time: 3000
                    });
                    
                    // 清空密码
                    $('input[name="password"]').val('');
                }
            },
            error: function(xhr) {
                layer.close(loadIndex);
                
                var errorMsg = '网络连接失败，请检查网络设置';
                if (xhr.status === 401) {
                    errorMsg = '用户名或密码错误';
                } else if (xhr.status === 403) {
                    errorMsg = '账户已被禁用';
                } else if (xhr.status === 500) {
                    errorMsg = '服务器内部错误';
                }
                
                layer.msg(errorMsg, {
                    icon: 2,
                    time: 3000
                });
                
                // 清空密码
                $('input[name="password"]').val('');
            }
        });
        
        return false;
    });

    // 页面加载时检查是否有记住的用户名
    $(document).ready(function() {
        var rememberedUsername = localStorage.getItem('rememberedUsername');
        if (rememberedUsername) {
            $('input[name="username"]').val(rememberedUsername);
            $('input[name="remember"]').prop('checked', true);
            form.render('checkbox');
        }
        
        // 检查是否已经登录
        var token = localStorage.getItem('token');
        if (token) {
            // 验证token是否有效
            $.ajax({
                url: '/api/auth/userinfo',
                type: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + token
                },
                success: function(res) {
                    if (res.code === 200) {
                        // token有效，直接跳转到主页
                        window.location.href = '/tsm/main';
                    }
                }
            });
        }
    });

    // 回车键登录
    $(document).keypress(function(e) {
        if (e.which === 13) {
            $('#loginBtn').click();
        }
    });

    // 忘记密码功能
    $('.forgot-password').on('click', function(e) {
        e.preventDefault();
        
        layer.prompt({
            title: '找回密码',
            formType: 0,
            value: '',
            area: ['300px', '150px']
        }, function(value, index) {
            if (!value) {
                layer.msg('请输入邮箱地址');
                return;
            }
            
            if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) {
                layer.msg('请输入正确的邮箱地址');
                return;
            }
            
            // 发送找回密码请求
            $.ajax({
                url: '/api/auth/forgot-password',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({email: value}),
                success: function(res) {
                    if (res.code === 200) {
                        layer.msg('密码重置邮件已发送，请查收', {
                            icon: 1,
                            time: 3000
                        });
                    } else {
                        layer.msg(res.message || '发送失败', {
                            icon: 2
                        });
                    }
                },
                error: function() {
                    layer.msg('网络错误，请稍后重试', {
                        icon: 2
                    });
                }
            });
            
            layer.close(index);
        });
    });

    // 添加输入框焦点效果
    $('.login-input').on('focus', function() {
        $(this).parent().addClass('focused');
    }).on('blur', function() {
        if (!$(this).val()) {
            $(this).parent().removeClass('focused');
        }
    });

    // 密码可见性切换
    var passwordVisible = false;
    $('input[name="password"]').after('<i class="layui-icon layui-icon-eye password-toggle"></i>');
    
    $('.password-toggle').on('click', function() {
        var passwordInput = $('input[name="password"]');
        if (passwordVisible) {
            passwordInput.attr('type', 'password');
            $(this).removeClass('layui-icon-eye-invisible').addClass('layui-icon-eye');
        } else {
            passwordInput.attr('type', 'text');
            $(this).removeClass('layui-icon-eye').addClass('layui-icon-eye-invisible');
        }
        passwordVisible = !passwordVisible;
    });
});