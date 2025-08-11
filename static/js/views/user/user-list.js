/**
 * 用户列表页面JavaScript
 * 负责用户列表的展示、搜索、操作等功能
 */

layui.use(['table', 'form', 'layer', 'element'], function() {
    var table = layui.table;
    var form = layui.form;
    var layer = layui.layer;
    var element = layui.element;

    // 用户表格配置
    var userTable = table.render({
        elem: '#userTable',
        url: '/api/user/list',
        toolbar: '#toolbarDemo',
        defaultToolbar: ['filter', 'exports', 'print'],
        cols: [[
            {type: 'checkbox', fixed: 'left'},
            {field: 'id', title: 'ID', width: 80, sort: true},
            {field: 'username', title: '用户名', width: 120},
            {field: 'realName', title: '真实姓名', width: 120},
            {field: 'email', title: '邮箱', width: 180},
            {field: 'phone', title: '手机号', width: 130},
            {field: 'status', title: '状态', width: 80, templet: '#statusTpl'},
            {field: 'createTime', title: '创建时间', width: 160, sort: true},
            {field: 'updateTime', title: '更新时间', width: 160, sort: true},
            {title: '操作', width: 200, align: 'center', fixed: 'right', templet: '#operationTpl'}
        ]],
        page: true,
        height: 'full-220',
        cellMinWidth: 80,
        request: {
            pageName: 'page',
            limitName: 'size'
        },
        response: {
            statusName: 'code',
            statusCode: 0,
            msgName: 'msg',
            countName: 'count',
            dataName: 'data'
        }
    });

    // 搜索功能
    form.on('submit(search)', function(data) {
        var field = data.field;
        
        // 重新加载表格
        table.reload('userTable', {
            where: field,
            page: {
                curr: 1
            }
        });
        
        return false;
    });

    // 新增用户
    $('#addUser').on('click', function() {
        layer.open({
            type: 2,
            title: '新增用户',
            shadeClose: true,
            shade: 0.8,
            area: ['800px', '600px'],
            content: '/tsm/user/form',
            end: function() {
                // 刷新表格
                table.reload('userTable');
            }
        });
    });

    // 批量删除
    $('#batchDelete').on('click', function() {
        var checkStatus = table.checkStatus('userTable');
        var data = checkStatus.data;
        
        if (data.length === 0) {
            layer.msg('请选择要删除的用户');
            return;
        }
        
        layer.confirm('确定要删除选中的 ' + data.length + ' 个用户吗？', {
            icon: 3,
            title: '提示'
        }, function(index) {
            var ids = data.map(function(item) {
                return item.id;
            });
            
            // 发送删除请求
            $.ajax({
                url: '/api/user/batch',
                type: 'DELETE',
                contentType: 'application/json',
                data: JSON.stringify(ids),
                success: function(res) {
                    if (res.code === 0) {
                        layer.msg('删除成功');
                        table.reload('userTable');
                    } else {
                        layer.msg(res.msg || '删除失败');
                    }
                },
                error: function() {
                    layer.msg('网络错误');
                }
            });
            
            layer.close(index);
        });
    });

    // 导出数据
    $('#exportData').on('click', function() {
        layer.msg('导出功能开发中...');
    });

    // 导入数据
    $('#importData').on('click', function() {
        layer.open({
            type: 2,
            title: '导入用户数据',
            shadeClose: true,
            shade: 0.8,
            area: ['600px', '400px'],
            content: '/tsm/user/import'
        });
    });

    // 监听表格行工具事件
    table.on('tool(userTable)', function(obj) {
        var data = obj.data;
        var layEvent = obj.event;
        var tr = obj.tr;

        if (layEvent === 'edit') {
            // 编辑用户
            layer.open({
                type: 2,
                title: '编辑用户',
                shadeClose: true,
                shade: 0.8,
                area: ['800px', '600px'],
                content: '/tsm/user/form?id=' + data.id,
                end: function() {
                    // 刷新表格
                    table.reload('userTable');
                }
            });
        } else if (layEvent === 'del') {
            // 删除用户
            layer.confirm('确定要删除用户 "' + data.username + '" 吗？', {
                icon: 3,
                title: '提示'
            }, function(index) {
                $.ajax({
                    url: '/api/user/' + data.id,
                    type: 'DELETE',
                    success: function(res) {
                        if (res.code === 0) {
                            layer.msg('删除成功');
                            obj.del();
                        } else {
                            layer.msg(res.msg || '删除失败');
                        }
                    },
                    error: function() {
                        layer.msg('网络错误');
                    }
                });
                layer.close(index);
            });
        } else if (layEvent === 'permission') {
            // 权限管理
            layer.open({
                type: 2,
                title: '用户权限管理',
                shadeClose: true,
                shade: 0.8,
                area: ['900px', '700px'],
                content: '/tsm/user/permission?id=' + data.id
            });
        }
    });

    // 监听状态开关
    form.on('switch(statusSwitch)', function(obj) {
        var status = obj.elem.checked ? 0 : 1;
        var userId = obj.elem.value;
        
        $.ajax({
            url: '/api/user/' + userId + '/status',
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify({status: status}),
            success: function(res) {
                if (res.code === 0) {
                    layer.msg('状态更新成功');
                } else {
                    layer.msg(res.msg || '状态更新失败');
                    // 恢复开关状态
                    obj.elem.checked = !obj.elem.checked;
                    form.render('switch');
                }
            },
            error: function() {
                layer.msg('网络错误');
                // 恢复开关状态
                obj.elem.checked = !obj.elem.checked;
                form.render('switch');
            }
        });
    });
});