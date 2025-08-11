/**
 * TSM 系统通用JavaScript工具库
 */

// 全局配置
window.TSM = {
    baseUrl: '/tsm',
    apiUrl: '/tsm/api',
    version: '1.0.0'
};

/**
 * HTTP请求工具
 */
window.TSM.http = {
    /**
     * GET请求
     */
    get: function(url, params) {
        return new Promise((resolve, reject) => {
            $.ajax({
                url: TSM.apiUrl + url,
                type: 'GET',
                data: params,
                dataType: 'json',
                success: function(response) {
                    resolve(response);
                },
                error: function(xhr, status, error) {
                    reject({
                        status: xhr.status,
                        message: error,
                        response: xhr.responseJSON
                    });
                }
            });
        });
    },

    /**
     * POST请求
     */
    post: function(url, data) {
        return new Promise((resolve, reject) => {
            $.ajax({
                url: TSM.apiUrl + url,
                type: 'POST',
                data: JSON.stringify(data),
                contentType: 'application/json',
                dataType: 'json',
                success: function(response) {
                    resolve(response);
                },
                error: function(xhr, status, error) {
                    reject({
                        status: xhr.status,
                        message: error,
                        response: xhr.responseJSON
                    });
                }
            });
        });
    },

    /**
     * PUT请求
     */
    put: function(url, data) {
        return new Promise((resolve, reject) => {
            $.ajax({
                url: TSM.apiUrl + url,
                type: 'PUT',
                data: JSON.stringify(data),
                contentType: 'application/json',
                dataType: 'json',
                success: function(response) {
                    resolve(response);
                },
                error: function(xhr, status, error) {
                    reject({
                        status: xhr.status,
                        message: error,
                        response: xhr.responseJSON
                    });
                }
            });
        });
    },

    /**
     * DELETE请求
     */
    delete: function(url) {
        return new Promise((resolve, reject) => {
            $.ajax({
                url: TSM.apiUrl + url,
                type: 'DELETE',
                dataType: 'json',
                success: function(response) {
                    resolve(response);
                },
                error: function(xhr, status, error) {
                    reject({
                        status: xhr.status,
                        message: error,
                        response: xhr.responseJSON
                    });
                }
            });
        });
    }
};

/**
 * 消息提示工具
 */
window.TSM.message = {
    success: function(msg, options) {
        if (typeof layer !== 'undefined') {
            layer.msg(msg, Object.assign({icon: 1, time: 2000}, options));
        } else {
            alert(msg);
        }
    },

    error: function(msg, options) {
        if (typeof layer !== 'undefined') {
            layer.msg(msg, Object.assign({icon: 2, time: 3000}, options));
        } else {
            alert(msg);
        }
    },

    warning: function(msg, options) {
        if (typeof layer !== 'undefined') {
            layer.msg(msg, Object.assign({icon: 3, time: 2000}, options));
        } else {
            alert(msg);
        }
    },

    info: function(msg, options) {
        if (typeof layer !== 'undefined') {
            layer.msg(msg, Object.assign({icon: 6, time: 2000}, options));
        } else {
            alert(msg);
        }
    }
};

/**
 * 确认对话框
 */
window.TSM.confirm = function(msg, callback, options) {
    if (typeof layer !== 'undefined') {
        layer.confirm(msg, Object.assign({
            icon: 3,
            title: '确认',
            btn: ['确定', '取消']
        }, options), function(index) {
            callback && callback();
            layer.close(index);
        });
    } else {
        if (confirm(msg)) {
            callback && callback();
        }
    }
};

/**
 * 表格工具
 */
window.TSM.table = {
    /**
     * 重新加载表格
     */
    reload: function(tableId, options) {
        if (typeof layui !== 'undefined' && layui.table) {
            layui.table.reload(tableId, options);
        }
    },

    /**
     * 获取选中的行
     */
    getCheckData: function(tableId) {
        if (typeof layui !== 'undefined' && layui.table) {
            return layui.table.checkStatus(tableId).data;
        }
        return [];
    }
};

/**
 * 表单工具
 */
window.TSM.form = {
    /**
     * 序列化表单数据
     */
    serialize: function(formSelector) {
        var data = {};
        $(formSelector).find('input, select, textarea').each(function() {
            var $this = $(this);
            var name = $this.attr('name');
            var value = $this.val();
            if (name && value !== '') {
                data[name] = value;
            }
        });
        return data;
    },

    /**
     * 重置表单
     */
    reset: function(formSelector) {
        $(formSelector)[0].reset();
        if (typeof layui !== 'undefined' && layui.form) {
            layui.form.render();
        }
    },

    /**
     * 填充表单数据
     */
    fill: function(formSelector, data) {
        for (var key in data) {
            var $field = $(formSelector).find('[name="' + key + '"]');
            if ($field.length > 0) {
                $field.val(data[key]);
            }
        }
        if (typeof layui !== 'undefined' && layui.form) {
            layui.form.render();
        }
    }
};

/**
 * 工具函数
 */
window.TSM.utils = {
    /**
     * 格式化日期
     */
    formatDate: function(date, format) {
        if (!date) return '';
        
        var d = new Date(date);
        format = format || 'yyyy-MM-dd HH:mm:ss';
        
        var year = d.getFullYear();
        var month = String(d.getMonth() + 1).padStart(2, '0');
        var day = String(d.getDate()).padStart(2, '0');
        var hours = String(d.getHours()).padStart(2, '0');
        var minutes = String(d.getMinutes()).padStart(2, '0');
        var seconds = String(d.getSeconds()).padStart(2, '0');
        
        return format
            .replace('yyyy', year)
            .replace('MM', month)
            .replace('dd', day)
            .replace('HH', hours)
            .replace('mm', minutes)
            .replace('ss', seconds);
    },

    /**
     * 生成UUID
     */
    uuid: function() {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
            var r = Math.random() * 16 | 0;
            var v = c == 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
        });
    },

    /**
     * 防抖函数
     */
    debounce: function(func, wait) {
        var timeout;
        return function() {
            var context = this;
            var args = arguments;
            clearTimeout(timeout);
            timeout = setTimeout(function() {
                func.apply(context, args);
            }, wait);
        };
    },

    /**
     * 节流函数
     */
    throttle: function(func, wait) {
        var timeout;
        return function() {
            var context = this;
            var args = arguments;
            if (!timeout) {
                timeout = setTimeout(function() {
                    timeout = null;
                    func.apply(context, args);
                }, wait);
            }
        };
    }
};

/**
 * 页面加载完成后的初始化
 */
$(document).ready(function() {
    // 全局AJAX错误处理
    $(document).ajaxError(function(event, xhr, settings, error) {
        if (xhr.status === 401) {
            TSM.message.error('登录已过期，请重新登录');
            setTimeout(function() {
                window.location.href = TSM.baseUrl + '/login';
            }, 2000);
        } else if (xhr.status === 403) {
            TSM.message.error('没有权限访问该资源');
        } else if (xhr.status === 500) {
            TSM.message.error('服务器内部错误');
        } else if (xhr.status === 0) {
            TSM.message.error('网络连接失败');
        }
    });

    // 全局加载状态
    $(document).ajaxStart(function() {
        // 可以在这里显示全局加载动画
    }).ajaxStop(function() {
        // 可以在这里隐藏全局加载动画
    });
});

console.log('TSM Common JS loaded successfully');