# TSM 后台管理系统

## 项目简介

TSM（Task & System Management）是一个基于Spring Boot + MyBatis + LayUI的企业级后台管理系统，采用RBAC权限模型，支持用户管理、角色管理、权限管理和按钮级权限控制。

## 技术栈

### 后端技术
- **Spring Boot 2.7.18** - 主框架
- **MyBatis** - 持久层框架
- **Spring Security** - 安全框架
- **H2 Database** - 内存数据库（开发环境）
- **Druid** - 数据库连接池
- **Maven** - 项目构建工具
- **Hutool** - 工具类库

### 前端技术
- **LayUI** - UI框架
- **jQuery** - JavaScript库
- **Layer** - 弹层组件
- **HTML5/CSS3** - 页面结构和样式

## 项目结构

```
TSM/
├── pom.xml                     # 父工程Maven配置
├── README.md                   # 项目说明文档
├── sql/                        # 数据库脚本
│   └── init.sql               # 数据库初始化脚本
├── static/                     # 静态资源目录
│   ├── css/                   # 样式文件
│   ├── images/                # 图片资源
│   ├── js/                    # JavaScript文件
│   ├── layui-v2.11.4/         # LayUI框架
│   ├── layer-v3.5.1/          # Layer弹层组件
│   ├── pages/                 # 前端页面
│   │   ├── user/              # 用户管理页面
│   │   ├── role/              # 角色管理页面
│   │   ├── permission/        # 权限管理页面
│   │   └── system/            # 系统管理页面
│   ├── index.html             # 主页面
│   └── login.html             # 登录页面
├── tsm-api/                   # API接口模块
│   ├── pom.xml
│   └── src/main/java/com/tsm/api/
│       ├── common/            # 公共响应类
│       │   └── Result.java    # 统一响应结果
│       └── dto/               # 数据传输对象
├── tsm-auth/                  # 认证模块
│   ├── pom.xml
│   └── src/main/java/com/tsm/auth/
│       ├── config/            # 认证配置
│       ├── service/           # 认证服务
│       └── util/              # 认证工具类
├── tsm-common/                # 公共模块
│   ├── pom.xml
│   └── src/main/java/com/tsm/common/
│       ├── config/            # 公共配置
│       ├── exception/         # 异常处理
│       └── util/              # 工具类
└── tsm-system/                # 系统核心模块
    ├── pom.xml
    └── src/main/java/com/tsm/
        ├── controller/        # 控制器层
        │   ├── InitController.java      # 初始化控制器
        │   ├── UserController.java      # 用户管理控制器
        │   ├── RoleController.java      # 角色管理控制器
        │   └── PermissionController.java # 权限管理控制器
        ├── system/
        │   ├── controller/    # 系统控制器
        │   │   └── AuthController.java  # 认证控制器
        │   ├── entity/        # 实体类
        │   │   ├── User.java            # 用户实体
        │   │   ├── Role.java            # 角色实体
        │   │   └── Permission.java      # 权限实体
        │   ├── mapper/        # 数据访问层
        │   │   ├── UserMapper.java      # 用户数据访问
        │   │   ├── RoleMapper.java      # 角色数据访问
        │   │   └── PermissionMapper.java # 权限数据访问
        │   ├── service/       # 业务逻辑层
        │   │   ├── UserService.java     # 用户服务
        │   │   ├── RoleService.java     # 角色服务
        │   │   ├── PermissionService.java # 权限服务
        │   │   └── impl/      # 服务实现类
        │   └── config/        # 配置类
        │       ├── PasswordConfig.java  # 密码配置
        │       ├── WebConfig.java       # Web配置
        │       └── UserDetailsServiceImpl.java # 用户详情服务
        ├── resources/         # 资源文件
        │   ├── application.yml          # 应用配置
        │   └── mapper/        # MyBatis映射文件
        │       ├── UserMapper.xml       # 用户映射
        │       ├── RoleMapper.xml       # 角色映射
        │       └── PermissionMapper.xml # 权限映射
        └── TsmSystemApplication.java    # 启动类
```

## 功能特性

### 核心功能
- ✅ **用户管理**：用户的增删改查、状态管理、密码重置
- ✅ **角色管理**：角色的增删改查、权限分配
- ✅ **权限管理**：菜单权限、按钮权限的精细化控制
- ✅ **登录认证**：基于Spring Security的安全认证
- ✅ **验证码**：登录验证码防护
- ✅ **会话管理**：用户会话控制和管理

### 权限模型
- **RBAC模型**：用户-角色-权限的经典权限模型
- **按钮级权限**：支持页面按钮级别的权限控制
- **菜单权限**：动态菜单权限控制

### 系统特性
- **响应式设计**：基于LayUI的响应式界面
- **数据库监控**：集成Druid数据库连接池监控
- **安全防护**：CSRF防护、XSS防护
- **模块化架构**：清晰的模块划分，便于维护和扩展

## 环境要求

- **JDK**: 1.8+
- **Maven**: 3.6+
- **IDE**: IntelliJ IDEA / Eclipse（推荐IntelliJ IDEA）

## 快速开始

### 1. 克隆项目

```bash
git clone <repository-url>
cd TSM
```

### 2. 编译项目

```bash
# 清理并编译项目
mvn clean compile

# 或者直接打包
mvn clean package
```

### 3. 启动应用

#### 方式一：使用Maven命令启动

```bash
# 在项目根目录执行
mvn spring-boot:run -f tsm-system/pom.xml
```

#### 方式二：使用IDE启动

1. 导入项目到IDE
2. 找到 `tsm-system/src/main/java/com/tsm/TsmSystemApplication.java`
3. 右键运行 `main` 方法

#### 方式三：使用jar包启动

```bash
# 先打包
mvn clean package

# 启动jar包
java -jar tsm-system/target/tsm-system-1.0.0.jar
```

### 4. 访问应用

- **应用地址**: http://localhost:8080/tsm
- **登录账号**: admin
- **登录密码**: 123456
- **数据库监控**: http://localhost:8080/druid（用户名/密码：admin/123456）

### 5. 数据库说明

项目使用H2内存数据库，应用启动时会自动初始化数据库和基础数据：

- 数据库文件：`~/tsm_db`（用户主目录下）
- 初始化脚本：`sql/init.sql`
- 默认创建admin用户和基础权限数据

## 开发指南

### 项目启动流程

1. **应用启动**：`TsmSystemApplication.main()` 启动Spring Boot应用
2. **数据库初始化**：应用启动时自动执行 `sql/init.sql` 初始化数据库
3. **Spring Security配置**：加载安全配置，设置认证和授权规则
4. **静态资源映射**：配置静态资源访问路径
5. **控制器注册**：注册所有REST API接口
6. **服务就绪**：应用启动完成，可以接收请求

### 核心配置文件

- `application.yml`：应用主配置文件
- `pom.xml`：Maven依赖配置
- `InitController.java`：Spring Security安全配置
- `WebConfig.java`：Web相关配置

### 开发规范

1. **代码结构**：遵循MVC分层架构
2. **命名规范**：使用驼峰命名法
3. **注释规范**：重要方法和类添加JavaDoc注释
4. **异常处理**：统一异常处理和错误响应
5. **日志规范**：使用SLF4J进行日志记录

## 部署说明

### 生产环境部署

1. **修改数据库配置**：将H2数据库改为MySQL等生产数据库
2. **修改应用配置**：调整 `application.yml` 中的生产环境配置
3. **打包应用**：`mvn clean package -Pprod`
4. **部署jar包**：将生成的jar包部署到服务器
5. **启动应用**：`java -jar tsm-system-1.0.0.jar --spring.profiles.active=prod`

### Docker部署（可选）

```dockerfile
# Dockerfile示例
FROM openjdk:8-jre-alpine
COPY tsm-system/target/tsm-system-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 常见问题

### Q1: 启动时提示端口被占用
**A**: 修改 `application.yml` 中的 `server.port` 配置，或者停止占用8080端口的进程。

### Q2: 登录时提示验证码错误
**A**: 刷新页面重新获取验证码，确保验证码输入正确。

### Q3: 数据库连接失败
**A**: 检查H2数据库文件权限，确保应用有读写权限。

### Q4: 静态资源无法访问
**A**: 检查 `WebConfig.java` 中的静态资源映射配置。

## 技术支持

如有问题，请通过以下方式联系：

- 提交Issue到项目仓库
- 发送邮件到技术支持邮箱

## 版本历史

- **v1.0.0** (2024-01-01)
  - 初始版本发布
  - 实现基础的用户、角色、权限管理
  - 集成Spring Security认证
  - 支持LayUI前端界面

## 许可证

本项目采用 MIT 许可证，详情请参阅 [LICENSE](LICENSE) 文件。