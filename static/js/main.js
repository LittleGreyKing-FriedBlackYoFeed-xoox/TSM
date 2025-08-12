// API 基础URL
const API_BASE = '/api';

// 用户管理功能
function getAllUsers() {
    $.ajax({
        url: API_BASE + '/user/list',
        method: 'GET',
        success: function(data) {
            $('#userResult').html('<h4>所有用户:</h4><pre>' + JSON.stringify(data, null, 2) + '</pre>');
        },
        error: function(xhr, status, error) {
            $('#userResult').html('<div style="color: red;">获取用户列表失败: ' + error + '</div>');
        }
    });
}

function showAddUserForm() {
    hideAllForms();
    $('#addUserForm').show();
}

function hideAddUserForm() {
    $('#addUserForm').hide();
    clearUserForm();
}

function addUser() {
    const username = $('#newUsername').val();
    const password = $('#newPassword').val();
    const nickname = $('#newNickname').val();
    const email = $('#newEmail').val();
    
    if (!username || !password) {
        layui.layer.msg('用户名和密码不能为空');
        return;
    }
    
    const userData = {
        username: username,
        password: password,
        nickname: nickname,
        email: email
    };
    
    console.log('添加用户:', userData);
    
    $.ajax({
        url: '/api/user/add',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(userData),
        success: function(response) {
            console.log('用户添加成功:', response);
            layui.layer.msg('用户添加成功', {icon: 1});
            // 清空表单
            $('#newUsername').val('');
            $('#newPassword').val('');
            $('#newNickname').val('');
            $('#newEmail').val('');
            // 返回用户列表
            showUserManagement();
        },
        error: function(xhr, status, error) {
            console.error('添加用户失败:', error);
            layui.layer.msg('添加用户失败: ' + (xhr.responseText || error), {icon: 2});
        }
    });
}

function showUpdateUserForm() {
    hideAllForms();
    $('#updateUserForm').show();
}

function hideUpdateUserForm() {
    $('#updateUserForm').hide();
    clearUserForm();
}

function updateUser() {
    const userId = $('#updateUserId').val();
    const username = $('#updateUsername').val();
    const nickname = $('#updateNickname').val();
    const email = $('#updateEmail').val();
    
    if (!username) {
        layui.layer.msg('用户名不能为空');
        return;
    }
    
    const userData = {
        id: userId,
        username: username,
        nickname: nickname,
        email: email
    };
    
    console.log('更新用户:', userData);
    
    $.ajax({
        url: '/api/user/update',
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(userData),
        success: function(response) {
            console.log('用户更新成功:', response);
            layui.layer.msg('用户更新成功', {icon: 1});
            showUserManagement();
        },
        error: function(xhr, status, error) {
            console.error('更新用户失败:', error);
            layui.layer.msg('更新用户失败: ' + (xhr.responseText || error), {icon: 2});
        }
    });
}

function showDeleteUserForm() {
    hideAllForms();
    $('#deleteUserForm').show();
}

function hideDeleteUserForm() {
    $('#deleteUserForm').hide();
    clearUserForm();
}

function deleteUser(userId) {
    layui.layer.confirm('确定要删除这个用户吗？', {
        btn: ['确定', '取消'],
        icon: 3,
        title: '删除确认'
    }, function(index) {
        $.ajax({
            url: '/api/user/delete/' + userId,
            type: 'DELETE',
            success: function(response) {
                console.log('用户删除成功:', response);
                layui.layer.msg('用户删除成功', {icon: 1});
                loadUsers();
                layui.layer.close(index);
            },
            error: function(xhr, status, error) {
                console.error('删除用户失败:', error);
                layui.layer.msg('删除用户失败: ' + (xhr.responseText || error), {icon: 2});
            }
        });
    });
}

// 权限管理功能
function getAllRoles() {
    $.ajax({
        url: API_BASE + '/role/list',
        method: 'GET',
        success: function(data) {
            $('#permissionResult').html('<h4>所有角色:</h4><pre>' + JSON.stringify(data, null, 2) + '</pre>');
        },
        error: function(xhr, status, error) {
            $('#permissionResult').html('<div style="color: red;">获取角色列表失败: ' + error + '</div>');
        }
    });
}

function getAllPermissions() {
    $.ajax({
        url: API_BASE + '/permission/list',
        method: 'GET',
        success: function(data) {
            $('#permissionResult').html('<h4>所有权限:</h4><pre>' + JSON.stringify(data, null, 2) + '</pre>');
        },
        error: function(xhr, status, error) {
            $('#permissionResult').html('<div style="color: red;">获取权限列表失败: ' + error + '</div>');
        }
    });
}

function showAddRoleForm() {
    hideAllForms();
    $('#addRoleForm').show();
}

function hideAddRoleForm() {
    $('#addRoleForm').hide();
    clearPermissionForm();
}

function addRole() {
    const name = $('#newRoleName').val();
    const code = $('#newRoleCode').val();
    const description = $('#newRoleDescription').val();
    
    if (!name || !code) {
        layui.layer.msg('角色名称和编码不能为空');
        return;
    }
    
    const roleData = {
        name: name,
        code: code,
        description: description
    };
    
    console.log('添加角色:', roleData);
    
    $.ajax({
        url: '/api/role/add',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(roleData),
        success: function(response) {
            console.log('角色添加成功:', response);
            layui.layer.msg('角色添加成功', {icon: 1});
            // 清空表单
            $('#newRoleName').val('');
            $('#newRoleCode').val('');
            $('#newRoleDescription').val('');
            // 返回角色列表
            showRoleManagement();
        },
        error: function(xhr, status, error) {
            console.error('添加角色失败:', error);
            layui.layer.msg('添加角色失败: ' + (xhr.responseText || error), {icon: 2});
        }
    });
}

function showAddPermissionForm() {
    hideAllForms();
    $('#addPermissionForm').show();
}

function hideAddPermissionForm() {
    $('#addPermissionForm').hide();
    clearPermissionForm();
}

function addPermission() {
    const name = $('#newPermissionName').val();
    const code = $('#newPermissionCode').val();
    const type = $('#newPermissionType').val();
    const url = $('#newPermissionUrl').val();
    
    if (!name || !code || !type) {
        layui.layer.msg('权限名称、编码和类型不能为空');
        return;
    }
    
    const permissionData = {
        name: name,
        code: code,
        type: type,
        url: url
    };
    
    console.log('添加权限:', permissionData);
    
    $.ajax({
        url: '/api/permission/add',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(permissionData),
        success: function(response) {
            console.log('权限添加成功:', response);
            layui.layer.msg('权限添加成功', {icon: 1});
            // 清空表单
            $('#newPermissionName').val('');
            $('#newPermissionCode').val('');
            $('#newPermissionType').val('');
            $('#newPermissionUrl').val('');
            // 返回权限列表
            showPermissionManagement();
        },
        error: function(xhr, status, error) {
            console.error('添加权限失败:', error);
            layui.layer.msg('添加权限失败: ' + (xhr.responseText || error), {icon: 2});
        }
    });
}

// 辅助函数
function hideAllForms() {
    $('#addUserForm').hide();
    $('#updateUserForm').hide();
    $('#deleteUserForm').hide();
    $('#addRoleForm').hide();
    $('#addPermissionForm').hide();
}

function clearUserForm() {
    $('#newUsername').val('');
    $('#newPassword').val('');
    $('#newNickname').val('');
    $('#updateUserId').val('');
    $('#updateUsername').val('');
    $('#updateNickname').val('');
    $('#deleteUserId').val('');
}

function clearPermissionForm() {
    $('#newRoleName').val('');
    $('#newRoleDesc').val('');
    $('#newPermissionName').val('');
    $('#newPermissionDesc').val('');
}

// 全局变量
let currentUsers = [];
let currentRoles = [];
let currentPermissions = [];

// 页面加载完成后初始化
$(document).ready(function() {
    console.log('页面加载完成，开始初始化...');
    
    // 初始化 Layui
    layui.use(['element', 'form', 'layer'], function(){
        const element = layui.element;
        const form = layui.form;
        const layer = layui.layer;
        
        console.log('Layui 初始化完成');
        
        // 监听表单提交
        form.on('submit(addUser)', function(data){
            console.log('添加用户表单提交:', data.field);
            addUser();
            return false; // 阻止表单跳转
        });
        
        form.on('submit(updateUser)', function(data){
            console.log('更新用户表单提交:', data.field);
            updateUser();
            return false;
        });
        
        form.on('submit(addRole)', function(data){
            console.log('添加角色表单提交:', data.field);
            addRole();
            return false;
        });
        
        form.on('submit(addPermission)', function(data){
            console.log('添加权限表单提交:', data.field);
            addPermission();
            return false;
        });
    });
    
    // 默认显示首页
    showDashboard();
});

// 显示/隐藏不同的管理区域
function showDashboard() {
    hideAllSections();
    $('#dashboard').show();
    loadDashboardData();
}

function showUserManagement() {
    hideAllSections();
    $('#user-management').show();
    loadUsers();
}

function showAddUser() {
    hideAllSections();
    $('#add-user-form').show();
}

function showRoleManagement() {
    hideAllSections();
    $('#role-management').show();
    loadRoles();
}

function showAddRole() {
    hideAllSections();
    $('#add-role-form').show();
}

function showPermissionManagement() {
    hideAllSections();
    $('#permission-management').show();
    loadPermissions();
}

function showAddPermission() {
    hideAllSections();
    $('#add-permission-form').show();
}

function hideAllSections() {
    $('#main-content > div').hide();
}

// 加载首页数据
function loadDashboardData() {
    // 加载用户总数
    $.ajax({
        url: '/api/user/list',
        type: 'GET',
        success: function(response) {
            $('#user-count').text(response.length);
        },
        error: function() {
            $('#user-count').text('0');
        }
    });
    
    // 加载角色总数
    $.ajax({
        url: '/api/role/list',
        type: 'GET',
        success: function(response) {
            $('#role-count').text(response.length);
        },
        error: function() {
            $('#role-count').text('0');
        }
    });
    
    // 加载权限总数
    $.ajax({
        url: '/api/permission/list',
        type: 'GET',
        success: function(response) {
            $('#permission-count').text(response.length);
        },
        error: function() {
            $('#permission-count').text('0');
        }
    });
}

// 用户管理相关函数
function loadUsers() {
    console.log('开始加载用户列表...');
    
    $.ajax({
        url: '/api/user/list',
        type: 'GET',
        success: function(response) {
            console.log('用户列表加载成功:', response);
            currentUsers = response;
            displayUsers(response);
        },
        error: function(xhr, status, error) {
            console.error('加载用户列表失败:', error);
            layui.layer.msg('加载用户列表失败: ' + error);
        }
    });
}

function displayUsers(users) {
    const userList = $('#user-list');
    userList.empty();
    
    if (users && users.length > 0) {
        users.forEach(user => {
            const row = `
                <tr>
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.nickname || ''}</td>
                    <td>${user.email || ''}</td>
                    <td><span class="layui-badge ${user.enabled ? 'layui-bg-green' : 'layui-bg-gray'}">${user.enabled ? '启用' : '禁用'}</span></td>
                    <td>${user.createTime || ''}</td>
                    <td>
                        <button class="layui-btn layui-btn-xs" onclick="editUser(${user.id})">
                            <i class="layui-icon layui-icon-edit"></i> 编辑
                        </button>
                        <button class="layui-btn layui-btn-xs layui-btn-danger" onclick="deleteUser(${user.id})">
                            <i class="layui-icon layui-icon-delete"></i> 删除
                        </button>
                    </td>
                </tr>
            `;
            userList.append(row);
        });
    } else {
        userList.append('<tr><td colspan="7" style="text-align: center; color: #999;">暂无用户数据</td></tr>');
    }
}

function editUser(userId) {
    const user = currentUsers.find(u => u.id === userId);
    if (user) {
        $('#updateUserId').val(user.id);
        $('#updateUsername').val(user.username);
        $('#updateNickname').val(user.nickname || '');
        $('#updateEmail').val(user.email || '');
        
        hideAllSections();
        $('#edit-user-form').show();
    }
}

// 角色管理相关函数
function loadRoles() {
    console.log('开始加载角色列表...');
    
    $.ajax({
        url: '/api/role/list',
        type: 'GET',
        success: function(response) {
            console.log('角色列表加载成功:', response);
            currentRoles = response;
            displayRoles(response);
        },
        error: function(xhr, status, error) {
            console.error('加载角色列表失败:', error);
            layui.layer.msg('加载角色列表失败: ' + error);
        }
    });
}

function displayRoles(roles) {
    const roleList = $('#role-list');
    roleList.empty();
    
    if (roles && roles.length > 0) {
        roles.forEach(role => {
            const row = `
                <tr>
                    <td>${role.id}</td>
                    <td>${role.name}</td>
                    <td>${role.code}</td>
                    <td>${role.description || ''}</td>
                    <td><span class="layui-badge ${role.enabled ? 'layui-bg-green' : 'layui-bg-gray'}">${role.enabled ? '启用' : '禁用'}</span></td>
                    <td>${role.createTime || ''}</td>
                    <td>
                        <button class="layui-btn layui-btn-xs" onclick="editRole(${role.id})">
                            <i class="layui-icon layui-icon-edit"></i> 编辑
                        </button>
                        <button class="layui-btn layui-btn-xs layui-btn-danger" onclick="deleteRole(${role.id})">
                            <i class="layui-icon layui-icon-delete"></i> 删除
                        </button>
                    </td>
                </tr>
            `;
            roleList.append(row);
        });
    } else {
        roleList.append('<tr><td colspan="7" style="text-align: center; color: #999;">暂无角色数据</td></tr>');
    }
}

function editRole(roleId) {
    // TODO: 实现角色编辑功能
    layui.layer.msg('角色编辑功能待实现');
}

function deleteRole(roleId) {
    layui.layer.confirm('确定要删除这个角色吗？', {
        btn: ['确定', '取消'],
        icon: 3,
        title: '删除确认'
    }, function(index) {
        $.ajax({
            url: '/api/role/delete/' + roleId,
            type: 'DELETE',
            success: function(response) {
                console.log('角色删除成功:', response);
                layui.layer.msg('角色删除成功', {icon: 1});
                loadRoles();
                layui.layer.close(index);
            },
            error: function(xhr, status, error) {
                console.error('删除角色失败:', error);
                layui.layer.msg('删除角色失败: ' + (xhr.responseText || error), {icon: 2});
            }
        });
    });
}

// 权限管理相关函数
function loadPermissions() {
    console.log('开始加载权限列表...');
    
    $.ajax({
        url: '/api/permission/list',
        type: 'GET',
        success: function(response) {
            console.log('权限列表加载成功:', response);
            currentPermissions = response;
            displayPermissions(response);
        },
        error: function(xhr, status, error) {
            console.error('加载权限列表失败:', error);
            layui.layer.msg('加载权限列表失败: ' + error);
        }
    });
}

function displayPermissions(permissions) {
    const permissionList = $('#permission-list');
    permissionList.empty();
    
    if (permissions && permissions.length > 0) {
        permissions.forEach(permission => {
            const row = `
                <tr>
                    <td>${permission.id}</td>
                    <td>${permission.name}</td>
                    <td>${permission.code}</td>
                    <td><span class="layui-badge ${permission.type === 'MENU' ? 'layui-bg-blue' : 'layui-bg-orange'}">${permission.type}</span></td>
                    <td>${permission.url || ''}</td>
                    <td><span class="layui-badge ${permission.enabled ? 'layui-bg-green' : 'layui-bg-gray'}">${permission.enabled ? '启用' : '禁用'}</span></td>
                    <td>${permission.createTime || ''}</td>
                    <td>
                        <button class="layui-btn layui-btn-xs" onclick="editPermission(${permission.id})">
                            <i class="layui-icon layui-icon-edit"></i> 编辑
                        </button>
                        <button class="layui-btn layui-btn-xs layui-btn-danger" onclick="deletePermission(${permission.id})">
                            <i class="layui-icon layui-icon-delete"></i> 删除
                        </button>
                    </td>
                </tr>
            `;
            permissionList.append(row);
        });
    } else {
        permissionList.append('<tr><td colspan="8" style="text-align: center; color: #999;">暂无权限数据</td></tr>');
    }
}

function editPermission(permissionId) {
    // TODO: 实现权限编辑功能
    layui.layer.msg('权限编辑功能待实现');
}

function deletePermission(permissionId) {
    layui.layer.confirm('确定要删除这个权限吗？', {
        btn: ['确定', '取消'],
        icon: 3,
        title: '删除确认'
    }, function(index) {
        $.ajax({
            url: '/api/permission/delete/' + permissionId,
            type: 'DELETE',
            success: function(response) {
                console.log('权限删除成功:', response);
                layui.layer.msg('权限删除成功', {icon: 1});
                loadPermissions();
                layui.layer.close(index);
            },
            error: function(xhr, status, error) {
                console.error('删除权限失败:', error);
                layui.layer.msg('删除权限失败: ' + (xhr.responseText || error), {icon: 2});
            }
        });
    });
}