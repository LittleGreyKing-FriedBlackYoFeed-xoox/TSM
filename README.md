# TSM分布式后台管理系统

## 项目简介

TSM（Trae System Management）是一个基于Spring Boot + Spring Cloud的分布式后台管理系统，支持细粒度的按钮级权限控制。系统采用RBAC（基于角色的访问控制）模型，并扩展支持用户级别的按钮权限管理。

## 技术栈

- **后端框架**: Spring Boot 2.7.0
- **微服务**: Spring Cloud 2021.0.3
- **数据库**: MySQL 8.0.29
- **连接池**: Druid 1.2.11
- **ORM框架**: MyBatis Plus 3.5.2
- **认证授权**: JWT 0.9.1
- **前端框架**: Layui
- **模板引擎**: Thymeleaf
- **网关**: Spring Cloud Gateway
- **构建工具**: Maven 3.8+
- **JDK版本**: JDK 17
- **JSON处理**: FastJSON 1.2.83

## 项目结构

```
TSM/
├── .vscode/             # VS Code配置
├── sql/                 # 数据库脚本
│   └── init.sql         # 数据库初始化脚本
├── tsm-common/          # 公共模块
│   ├── src/main/java/com/tsm/common/
│   │   ├── entity/      # 基础实体类（BaseEntity）
│   │   ├── enums/       # 枚举类
│   │   ├── utils/       # 工具类
│   │   └── result/      # 统一响应类
│   └── pom.xml          # 公共模块依赖配置
├── tsm-auth/            # 认证授权模块
│   ├── src/main/java/com/tsm/auth/
│   │   ├── entity/      # 用户、角色、权限、按钮实体
│   │   ├── mapper/      # 数据访问层
│   │   └── service/     # 业务逻辑层
│   └── pom.xml          # 认证模块依赖配置
├── tsm-web/             # Web应用模块
│   ├── src/main/java/com/tsm/web/
│   │   └── controller/  # 控制器（AuthController、PageController等）
│   ├── src/main/resources/
│   │   ├── templates/   # Thymeleaf模板文件
│   │   │   ├── fragments/             # 页面片段
│   │   │   │   ├── header.html        # 头部片段（含面包屑导航）
│   │   │   │   ├── footer.html        # 尾部片段（含返回顶部）
│   │   │   │   └── sidebar.html       # 侧边栏片段
│   │   │   ├── index.html             # 首页
│   │   │   ├── login.html             # 登录页面（灰白色系）
│   │   │   └── main.html              # 主页面（片段化设计）
│   │   ├── static/      # 静态资源
│   │   └── application.yml # 应用配置
│   ├── logs/            # 日志文件
│   └── pom.xml          # Web模块依赖配置
├── tsm-gateway/         # 网关模块
│   └── pom.xml          # 网关模块依赖配置
├── tsm-test/            # 测试模块
│   ├── src/test/java/   # 测试用例
│   └── pom.xml          # 测试模块依赖配置
├── pom.xml              # 父级POM配置
├── start.bat            # Windows启动脚本
└── README.md            # 项目说明文档
```

## 核心功能

### 1. 用户管理
- 用户注册、登录、注销
- 用户信息管理
- 用户状态控制

### 2. 角色管理
- 角色创建、编辑、删除
- 角色权限分配
- 角色状态管理

### 3. 权限管理
- 菜单权限管理
- 按钮权限管理
- 权限树形结构

### 4. 按钮级权限控制
- 支持页面级按钮权限配置
- 用户级按钮权限覆盖
- 权限有效期管理
- 动态按钮渲染

### 5. 系统安全
- JWT令牌认证
- 密码加密存储
- 接口权限验证
- 跨域请求处理

### 6. 界面设计
- **片段化设计**：头部、尾部、侧边栏独立抽象，便于维护和复用
- **面包屑导航**：清晰的导航路径，支持快速返回上级页面
- **灰白色系UI**：简洁优雅的现代化界面设计

## 数据库设计

### 核心表结构

1. **sys_user** - 用户表
2. **sys_role** - 角色表
3. **sys_permission** - 权限表
4. **sys_button** - 按钮表
5. **sys_user_role** - 用户角色关联表
6. **sys_role_permission** - 角色权限关联表
7. **sys_user_button** - 用户按钮权限关联表

## 快速开始

### 1. 环境准备

- JDK 8+
- Maven 3.8+
- MySQL 8.0+
- Redis（可选，用于缓存）

### 2. 数据库初始化

```sql
-- 创建数据库
CREATE DATABASE TSM DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 执行初始化脚本
source sql/init.sql;
```

### 3. 配置修改

修改 `tsm-web/src/main/resources/application.yml` 中的数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://47.109.155.118:3309/TSM?useUnicode=true&characterEncoding=utf8mb4&serverTimezone=GMT%2B8&useSSL=false&allowPublicKeyRetrieval=true
    username: root
    password: Root@@__135230
    driver-class-name: com.mysql.cj.jdbc.Driver
    
# 当前项目配置
server:
  port: 8086
  servlet:
    context-path: /tsm

# JWT配置
jwt:
  secret: tsm_system_secret_key_2023
  expiration: 86400000
```

### 4. 启动应用

```bash
# 编译项目
mvn clean compile

# 启动Web服务
cd tsm-web
mvn spring-boot:run

# 或者使用提供的启动脚本（Windows）
start.bat

# 启动网关服务（可选）
cd tsm-gateway
mvn spring-boot:run
```

### 5. 访问系统

- **Web应用访问**: http://localhost:8086/tsm
- **API测试接口**: http://localhost:8086/tsm/api/test/hello
- **默认账号**: admin / 123456
- **测试账号**: test / 123456

## 按钮权限使用说明

### 1. 按钮权限配置

系统支持两级按钮权限控制：

1. **角色级权限**: 通过角色-权限关联配置基础权限
2. **用户级权限**: 通过用户-按钮关联进行精细化控制

## 页面片段化设计

### 1. 头部片段 (header.html)
- 系统Logo和标题
- 用户信息下拉菜单
- 面包屑导航系统
- 灰白色系主题设计

### 2. 侧边栏片段 (sidebar.html)
- 可折叠的导航菜单
- 图标化的菜单项
- 响应式设计
- 状态保持功能

### 3. 尾部片段 (footer.html)
- 版权信息和链接
- 返回顶部按钮
- 弹窗式帮助信息
- 简洁的页脚设计

### 4. 面包屑导航特性
- 自动记录访问路径
- 支持快速返回上级
- 清晰的层级显示
- JavaScript动态管理

## 灰白色系设计理念

- **主色调**：#6c757d (中性灰)
- **背景色**：#f8f9fa (浅灰白)
- **强调色**：#495057 (深灰)
- **边框色**：#dee2e6 (淡灰)
- **简洁优雅**：减少视觉干扰，提升用户体验
- **现代化**：符合当前主流设计趋势

### 2. 前端按钮渲染

```javascript
// 获取用户按钮权限
$.get('/api/auth/user-buttons/dashboard', function(result) {
    if (result.code === 200) {
        renderButtons(result.data);
    }
});

// 动态渲染按钮
function renderButtons(buttons) {
    buttons.forEach(function(button) {
        var btnHtml = '<button class="layui-btn" data-code="' + 
                     button.buttonCode + '">' + button.buttonName + '</button>';
        $('#button-container').append(btnHtml);
    });
}
```

### 3. 后端权限验证

```java
// 检查按钮权限
@GetMapping("/check-button-permission")
public Result<Boolean> checkButtonPermission(@RequestParam String buttonCode, 
                                           HttpServletRequest request) {
    Long userId = JwtUtils.getUserIdFromRequest(request);
    boolean hasPermission = authService.hasButtonPermission(userId, buttonCode);
    return Result.success(hasPermission);
}
```

## API接口文档

### 认证相关接口

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/auth/login` | POST | 用户登录 |
| `/api/auth/user-info` | GET | 获取用户信息 |
| `/api/auth/check-button-permission` | GET | 检查按钮权限 |
| `/api/auth/user-buttons/{pageCode}` | GET | 获取页面按钮权限 |

### 权限管理接口

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/auth/assign-button-permission` | POST | 分配按钮权限 |
| `/api/auth/batch-assign-button-permissions` | POST | 批量分配按钮权限 |

## 测试

### 运行单元测试

```bash
cd tsm-test
mvn test
```

### 测试覆盖

- 认证服务测试
- 控制器集成测试
- 按钮权限功能测试