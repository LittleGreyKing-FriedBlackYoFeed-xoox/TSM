# TSM分布式微服务后台管理系统

## 项目简介

TSM（分布式微服务后台管理系统）是一个基于Spring Boot微服务架构的企业级后台管理平台，主要实现用户管理和权限管理功能，采用RBAC权限模型，支持按钮级别的细粒度权限控制。

## 技术架构

### 后端技术栈
- **框架**：Spring Boot 2.7+, Spring Cloud 2021+
- **数据访问**：MyBatis Plus 3.5+
- **数据库**：MySQL 8.0
- **缓存**：Redis 6.0
- **服务注册发现**：Nacos
- **API网关**：Spring Cloud Gateway
- **构建工具**：Maven 3.8+
- **安全框架**：JWT认证

### 前端技术栈
- **UI框架**：Layui
- **脚本语言**：JavaScript, jQuery
- **样式**：CSS3, 灰白色系主题
- **模板引擎**：Thymeleaf

## 项目结构

```
TSM/
├── tsm-common/           # 公共模块
├── tsm-gateway/          # API网关服务
├── tsm-auth/             # 认证服务
├── tsm-user/             # 用户管理服务
├── tsm-permission/       # 权限管理服务
├── tsm-web/              # 前端静态资源
├── sql/                  # 数据库脚本
├── pom.xml               # 父项目配置
└── README.md             # 项目说明
```

## 核心功能

### 1. 用户管理
- 用户信息的增删改查
- 用户状态管理（启用/禁用）
- 密码重置功能
- 用户角色分配
- 用户数据统计

### 2. 权限管理
- 基于RBAC模型的权限控制
- 角色管理（增删改查）
- 权限分配（支持按钮级权限）
- 权限树形结构展示
- 动态权限验证

### 3. 认证授权
- JWT令牌认证
- 图形验证码
- 登录日志记录
- 令牌刷新机制
- 登出黑名单管理

## 环境要求

- **JDK**: 1.8+
- **Maven**: 3.6+
- **MySQL**: 8.0+
- **Redis**: 6.0+
- **Nacos**: 2.0+

## 快速开始

### 1. 环境准备

#### 安装MySQL
```bash
# 创建数据库
CREATE DATABASE TSM DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 安装Redis
```bash
# 启动Redis服务
redis-server
```

#### 安装Nacos
```bash
# 下载Nacos
wget https://github.com/alibaba/nacos/releases/download/2.2.3/nacos-server-2.2.3.tar.gz

# 解压并启动
tar -xzf nacos-server-2.2.3.tar.gz
cd nacos/bin
# Windows
startup.cmd -m standalone
# Linux/Mac
sh startup.sh -m standalone
```

### 2. 数据库初始化

执行数据库初始化脚本：
```bash
mysql -u root -p TSM < sql/init.sql
```

### 3. 配置修改

修改各服务的 `application.yml` 配置文件中的数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/TSM?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8
    username: root
    password: your_password
```

### 4. 编译项目

```bash
# 在项目根目录执行
mvn clean compile
```

### 5. 启动服务

按以下顺序启动各个服务：

```bash
# 1. 启动认证服务
cd tsm-auth
mvn spring-boot:run

# 2. 启动用户管理服务
cd tsm-user
mvn spring-boot:run

# 3. 启动权限管理服务
cd tsm-permission
mvn spring-boot:run

# 4. 启动前端Web服务
cd tsm-web
mvn spring-boot:run
```

### 6. 访问系统

打开浏览器访问：http://localhost:8080

**默认管理员账号：**
- 用户名：admin
- 密码：admin123

## 服务端口说明

| 服务名称 | 端口 | 说明 |
|---------|------|------|
| tsm-web | 8080 | 前端Web服务 |
| tsm-auth | 8081 | 认证服务 |
| tsm-user | 8082 | 用户管理服务 |
| tsm-permission | 8083 | 权限管理服务 |
| tsm-gateway | 8084 | API网关服务（可选） |

## API文档

### 认证相关接口

#### 用户登录
```
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123",
  "captcha": "ABCD",
  "captchaKey": "uuid-key"
}
```

#### 获取验证码
```
GET /api/auth/captcha
```

#### 用户登出
```
POST /api/auth/logout
Authorization: Bearer {token}
```

### 用户管理接口

#### 获取用户列表
```
GET /api/user/list?current=1&size=10
Authorization: Bearer {token}
```

#### 新增用户
```
POST /api/user
Content-Type: application/json
Authorization: Bearer {token}

{
  "username": "testuser",
  "password": "123456",
  "email": "test@example.com",
  "realName": "测试用户",
  "roleIds": [2]
}
```

## 数据库设计

### 核心表结构

#### 用户表 (tsm_user)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 用户ID |
| username | VARCHAR(50) | 用户名 |
| password | VARCHAR(255) | 密码 |
| email | VARCHAR(100) | 邮箱 |
| phone | VARCHAR(20) | 手机号 |
| real_name | VARCHAR(50) | 真实姓名 |
| status | TINYINT | 状态 |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |

#### 角色表 (tsm_role)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 角色ID |
| role_name | VARCHAR(50) | 角色名称 |
| role_code | VARCHAR(50) | 角色编码 |
| description | VARCHAR(200) | 角色描述 |
| status | TINYINT | 状态 |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |

#### 权限表 (tsm_permission)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 权限ID |
| permission_name | VARCHAR(50) | 权限名称 |
| permission_code | VARCHAR(50) | 权限编码 |
| menu_url | VARCHAR(200) | 菜单URL |
| parent_id | BIGINT | 父权限ID |
| permission_type | TINYINT | 权限类型 |
| create_time | DATETIME | 创建时间 |

## 部署说明

### Docker部署

1. 构建镜像
```bash
# 构建各服务镜像
mvn clean package -DskipTests
docker build -t tsm-auth:latest tsm-auth/
docker build -t tsm-user:latest tsm-user/
docker build -t tsm-permission:latest tsm-permission/
docker build -t tsm-web:latest tsm-web/
```

2. 使用Docker Compose部署
```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: Root@@__135230
      MYSQL_DATABASE: TSM
    ports:
      - "3306:3306"
  
  redis:
    image: redis:6.0
    ports:
      - "6379:6379"
  
  nacos:
    image: nacos/nacos-server:v2.2.3
    environment:
      MODE: standalone
    ports:
      - "8848:8848"
  
  tsm-auth:
    image: tsm-auth:latest
    ports:
      - "8081:8081"
    depends_on:
      - mysql
      - redis
      - nacos
  
  tsm-user:
    image: tsm-user:latest
    ports:
      - "8082:8082"
    depends_on:
      - mysql
      - redis
      - nacos
  
  tsm-permission:
    image: tsm-permission:latest
    ports:
      - "8083:8083"
    depends_on:
      - mysql
      - redis
      - nacos
  
  tsm-web:
    image: tsm-web:latest
    ports:
      - "8080:8080"
    depends_on:
      - tsm-auth
      - tsm-user
      - tsm-permission
```

### 生产环境部署

1. 服务器要求
   - CPU: 4核心以上
   - 内存: 8GB以上
   - 磁盘: 100GB以上
   - 操作系统: CentOS 7+ / Ubuntu 18+

2. 环境配置
   - 安装JDK 1.8+
   - 安装MySQL 8.0
   - 安装Redis 6.0
   - 安装Nginx（可选）

3. 性能优化
   - JVM参数调优
   - 数据库连接池配置
   - Redis缓存策略
   - 负载均衡配置

## 常见问题

### Q1: 启动时连接数据库失败
A: 检查数据库连接配置，确保MySQL服务正常运行，数据库已创建。

### Q2: 验证码不显示
A: 检查静态资源路径配置，确保前端能正常访问后端API。

### Q3: 登录后页面空白
A: 检查浏览器控制台错误信息，确认前端静态资源加载正常。

### Q4: 服务注册失败
A: 检查Nacos服务是否正常启动，网络连接是否正常。

## 开发指南

### 代码规范
- 遵循阿里巴巴Java开发手册
- 使用统一的代码格式化配置
- 添加必要的注释和文档

### 新增功能
1. 在对应的服务模块中添加功能
2. 更新数据库脚本
3. 编写单元测试
4. 更新API文档

### 测试
```bash
# 运行单元测试
mvn test

# 运行集成测试
mvn verify
```

## 版本历史

- **v1.0.0** (2024-01-20)
  - 初始版本发布
  - 实现用户管理功能
  - 实现权限管理功能
  - 实现JWT认证

## 贡献指南

1. Fork 项目
2. 创建功能分支
3. 提交代码
4. 创建 Pull Request

## 许可证

MIT License

## 联系方式

- 项目地址：https://github.com/your-username/TSM
- 问题反馈：https://github.com/your-username/TSM/issues
- 邮箱：admin@tsm.com

---

**感谢使用TSM分布式微服务后台管理系统！**