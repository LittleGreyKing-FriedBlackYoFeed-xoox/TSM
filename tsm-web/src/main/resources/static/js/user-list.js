layui.use(['table', 'form', 'layer'], function(){
    var table = layui.table;
    var form = layui.form;
    var layer = layui.layer;
    var $ = layui.$;
    
    // 渲染表格
            var tableIns = table.render({
                elem: '#userTable',
                url: 'http://localhost:8082/api/user/page',
        page: true,
        limit: 10,
        limits: [10, 20, 50, 100],
        cols: [[
            {type: 'checkbox', fixed: 'left'},
            {field: 'id', title: 'ID', width: 80, sort: true},
            {field: 'username', title: '用户名', width: 150},
            {field: 'realName', title: '真实姓名', width: 150},
            {field: 'email', title: '邮箱', width: 200},
            {field: 'phone', title: '手机号', width: 150},
            {field: 'status', title: '状态', width: 100, templet: '#statusTpl'},
            {field: 'createTime', title: '创建时间', width: 180},
            {title: '操作', width: 300, toolbar: '#operationTpl', fixed: 'right'}
        ]],
        parseData: function(res){
            return {
                "code": res.code === 200 ? 0 : res.code,
                "msg": res.message,
                "count": res.data ? res.data.total : 0,
                "data": res.data ? res.data.records : []
            };
        }
    });
    
    // 搜索
    form.on('submit(search)', function(data){
        tableIns.reload({
            where: data.field,
            page: {
                curr: 1
            }
        });
        return false;
    });
    
    // 重置
    $('#resetBtn').on('click', function(){
        $('#searchForm')[0].reset();
        form.render();
        tableIns.reload({
            where: {},
            page: {
                curr: 1
            }
        });
    });
    
    // 新增用户弹窗
    $('#addUserBtn').on('click', function(){
        layer.open({
            type: 1,
            title: '新增用户',
            area: ['600px', '500px'],
            content: $('#addUserForm'),
            btn: ['确定', '取消'],
            yes: function(index, layero){
                // 提交表单
                var formData = {
                    username: $('#add_username').val(),
                    realName: $('#add_realName').val(),
                    email: $('#add_email').val(),
                    phone: $('#add_phone').val(),
                    password: $('#add_password').val()
                };
                
                $.ajax({
                            url: 'http://localhost:8082/api/user/add',
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify(formData),
                    success: function(res){
                        if(res.code === 200){
                            layer.msg('添加成功', {icon: 1});
                            layer.close(index);
                            tableIns.reload();
                            // 重置表单
                            $('#addUserForm form')[0].reset();
                        } else {
                            layer.msg(res.message || '添加失败', {icon: 2});
                        }
                    },
                    error: function(){
                        layer.msg('网络错误', {icon: 2});
                    }
                });
            }
        });
    });
    
    // 监听工具条
    table.on('tool(userTable)', function(obj){
        var data = obj.data;
        if(obj.event === 'edit'){
            window.location.href = '/user/edit?id=' + data.id;
        } else if(obj.event === 'del'){
            layer.confirm('确定删除该用户吗？', function(index){
                $.ajax({
                            url: 'http://localhost:8082/api/user/delete/' + data.id,
                    type: 'DELETE',
                    success: function(res){
                        if(res.code === 200){
                            layer.msg('删除成功', {icon: 1});
                            tableIns.reload();
                        } else {
                            layer.msg(res.message || '删除失败', {icon: 2});
                        }
                    },
                    error: function(){
                        layer.msg('网络错误', {icon: 2});
                    }
                });
                layer.close(index);
            });
        }
    });
    
    // 退出登录
    window.logout = function() {
        layer.confirm('确定要退出登录吗？', {
            icon: 3,
            title: '提示'
        }, function(index) {
            window.location.href = '/login';
            layer.close(index);
        });
    };
});