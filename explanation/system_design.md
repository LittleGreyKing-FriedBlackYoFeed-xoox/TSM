1.环境准备阶段
•配置MySQL数据库（创建TSM库/导入初始SQL）
•安装Redis集群（作为分布式会话存储）
•搭建Nacos服务注册中心
•配置Maven私服仓库

2.基础架构搭建
•创建SpringBoot父工程tsm-parent
•构建子模块：
tsm-api（公共DTO/异常定义）
tsm-common（工具类/枚举）
tsm-auth（认证授权模块）
tsm-system（核心业务模块）
•配置分布式事务管理（Seata）

3.用户管理模块开发
•实体类：User/Role/Menu/Button
•数据库设计：
•用户表(tsm_user)
•角色表(tsm_role)
•权限表(tsm_permission)
•按钮权限关联表(tsm_role_button)
•接口开发：
•用户CRUD接口
•角色管理接口
•权限分配接口
•拦截器实现：
•Token鉴权过滤器
•按钮级权限校验切面

4.权限管理系统开发
•动态权限加载：
•实现FilterInvocationSecurityMetadataSource
•自定义AccessDecisionManager
•前端权限控制：
•路由动态加载
•按钮显隐控制
•审计日志：
•记录用户操作日志
•权限变更追踪

5.分布式架构实现
•服务拆分：
•用户服务(user-service)
•权限服务(auth-service)
•API网关配置：
•Zuul/OAuth2认证路由
•服务熔断降级
•配置中心：
•Nacos配置分发
•动态环境切换

6.前端工程构建
•Layui主题定制：
•修改变量文件(如@primary-color)
•重构组件样式
•页面结构标准化：
•头部(header.vue)
•侧边栏(sidebar.vue)
•面包屑(breadcrumb.vue)
•功能开发：
•用户列表页
•角色管理页
•权限分配页
•操作日志页

7.性能优化措施
•数据库层面：
•索引优化
•分库分表策略
•缓存优化：
•Redis缓存用户权限
•Token黑名单机制
•代码优化：
•减少HTTP请求数
•异步加载非关键资源

8.测试验收阶段
•单元测试：
•MockMvc接口测试
•Service层单元测试
•集成测试：
•分布式调用链测试
•权限穿透测试
•性能测试：
•JMeter压力测试
•接口响应时间监控

9.文档交付
•技术文档：
•架构图（微服务/数据库）
•接口文档（Swagger）
•部署拓扑图
•用户手册：
•管理员操作指南
•权限配置说明
•测试报告：
•功能测试用例
•性能测试报告

10.交付验收
•数据库初始化脚本
•容器化部署包(Docker)
•生产环境配置参数
•系统演示账号
•技术支持文档
各阶段并行实施要点：
1.开发初期完成核心接口定义
2.权限系统与UI框架同步开发
3.分布式组件逐步接入
4.持续集成自动化测试
5.性能优化贯穿全程

11.数据库配置信息
DATABASES = {
    "default": {
        "ENGINE": "django.db.backends.mysql",
        "NAME": "TSM",
        "USER": "root",
        "PASSWORD": "Root@@__135230",
        "HOST": "47.109.155.118",
        "PORT": "3309",
        "OPTIONS": {
            "connect_timeout": 60,  # 连接超时时间增加到60秒
            "read_timeout": 60,      # 读取超时时间
            "write_timeout": 60,     # 写入超时时间
            "init_command": "SET sql_mode='STRICT_TRANS_TABLES'",  # 初始化命令
            "charset": "utf8mb4",    # 使用utf8mb4字符集
        },
        "CONN_MAX_AGE": 60,          # 连接最大存活时间（秒）
        "ATOMIC_REQUESTS": False,    # 不要将每个请求包装在事务中
    }
}