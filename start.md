# TSM分布式微服务后台管理系统启动指南

## 项目概述

TSM（Task & Service Management）是一个基于Spring Boot + Spring Cloud的分布式微服务后台管理系统，采用微服务架构设计，包含多个独立的服务模块。

### 系统架构

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   API Gateway   │    │   Web Service   │    │  Auth Service   │
│   (端口: 8080)   │    │   (端口: 8084)   │    │   (端口: 8081)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
┌─────────────────┐    ┌─────────────────┐
│  User Service   │    │Permission Service│
│   (端口: 8082)   │    │   (端口: 8083)   │
└─────────────────┘    └─────────────────┘
```

### 服务模块说明

- **tsm-gateway (8080)**: API网关服务，负责路由转发和负载均衡
- **tsm-auth (8081)**: 认证服务，处理用户登录、JWT令牌生成和验证
- **tsm-user (8082)**: 用户服务，管理用户信息和相关操作
- **tsm-permission (8083)**: 权限服务，管理角色和权限控制
- **tsm-web (8084)**: Web前端服务，提供用户界面和静态资源
- **tsm-common**: 公共模块，包含共享的工具类和配置

## 启动前准备

### 环境要求

- Java 8 或更高版本
- Maven 3.6 或更高版本
- MySQL 数据库（已配置外部数据库）
- Redis 缓存（已配置外部Redis）

### 数据库配置

系统已配置外部数据库连接：
- **MySQL**: 47.109.155.118:3309/TSM
- **Redis**: 47.109.155.118:6379

## 详细启动步骤

### 第一步：编译项目

在项目根目录执行以下命令编译整个Maven多模块项目：

```bash
mvn clean compile
```

**说明**: 这一步会下载所有依赖并编译所有模块，确保项目代码没有编译错误。

### 第二步：启动认证服务 (tsm-auth)

```bash
mvn spring-boot:run -pl tsm-auth
```

- **端口**: 8081
- **功能**: 用户认证、JWT令牌管理
- **启动顺序**: 第一个启动（其他服务可能依赖认证功能）

### 第三步：启动用户服务 (tsm-user)

```bash
mvn spring-boot:run -pl tsm-user
```

- **端口**: 8082
- **功能**: 用户信息管理
- **依赖**: 认证服务

### 第四步：启动权限服务 (tsm-permission)

```bash
mvn spring-boot:run -pl tsm-permission
```

- **端口**: 8083
- **功能**: 角色和权限管理
- **依赖**: 认证服务

### 第五步：启动Web服务 (tsm-web)

```bash
mvn spring-boot:run -pl tsm-web
```

- **端口**: 8084
- **功能**: 前端界面和静态资源
- **依赖**: 认证、用户、权限服务

### 第六步：启动网关服务 (tsm-gateway)

```bash
mvn spring-boot:run -pl tsm-gateway
```

- **端口**: 8080
- **功能**: API路由和负载均衡
- **依赖**: 所有其他服务
- **注意**: 网关服务应最后启动，确保所有后端服务已就绪

## 验证服务运行状态

### 1. 检查服务端口

使用以下命令检查各服务端口是否正常监听：

```bash
netstat -an | findstr "8081 8082 8083 8084 8080"
```

### 2. 服务健康检查

访问各服务的健康检查端点：

- 认证服务: http://localhost:8081/actuator/health
- 用户服务: http://localhost:8082/actuator/health
- 权限服务: http://localhost:8083/actuator/health
- Web服务: http://localhost:8084/actuator/health
- 网关服务: http://localhost:8080/actuator/health

### 3. 查看服务日志

观察各服务的启动日志，确保没有错误信息，看到类似以下成功启动的标志：

```
Started [ServiceName]Application in X.XXX seconds
```

## 访问系统

### 主要访问方式

1. **通过Web服务直接访问**: http://localhost:8084
2. **通过网关访问**: http://localhost:8080

### API接口访问

- 认证接口: http://localhost:8081/auth/
- 用户接口: http://localhost:8082/user/
- 权限接口: http://localhost:8083/permission/

### 通过网关的统一访问

- 认证接口: http://localhost:8080/api/auth/
- 用户接口: http://localhost:8080/api/user/
- 权限接口: http://localhost:8080/api/permission/

## 常见问题和解决方案

### 1. 端口占用问题

**问题**: 启动时提示端口被占用

**解决方案**:
```bash
# 查看端口占用
netstat -ano | findstr :8081
# 结束占用进程
taskkill /PID <进程ID> /F
```

### 2. 数据库连接失败

**问题**: 无法连接到MySQL数据库

**解决方案**:
- 检查数据库服务是否正常运行
- 验证数据库连接配置（用户名、密码、URL）
- 确认网络连接正常

### 3. Redis连接失败

**问题**: 无法连接到Redis缓存

**解决方案**:
- 检查Redis服务状态
- 验证Redis连接配置
- 确认防火墙设置

### 4. 服务间调用失败

**问题**: 微服务之间无法正常通信

**解决方案**:
- 确保所有依赖服务已启动
- 检查服务注册与发现配置
- 验证网络连接和端口访问权限

### 5. 内存不足

**问题**: 启动多个服务时内存不足

**解决方案**:
```bash
# 为每个服务设置JVM内存参数
mvn spring-boot:run -pl tsm-auth -Dspring-boot.run.jvmArguments="-Xmx512m"
```

## 停止服务

### 优雅停止顺序

1. 停止网关服务 (tsm-gateway)
2. 停止Web服务 (tsm-web)
3. 停止权限服务 (tsm-permission)
4. 停止用户服务 (tsm-user)
5. 停止认证服务 (tsm-auth)

### 停止命令

在各服务的终端窗口中按 `Ctrl + C` 或使用以下命令：

```bash
# 查找Java进程
jps
# 停止指定进程
taskkill /PID <进程ID> /F
```

## 开发调试

### 开发模式启动

在开发环境中，可以使用IDE直接运行各服务的主类：

- `com.tsm.auth.AuthApplication`
- `com.tsm.user.UserApplication`
- `com.tsm.permission.PermissionApplication`
- `com.tsm.web.WebApplication`
- `com.tsm.gateway.GatewayApplication`

### 调试端口配置

如需远程调试，可以添加JVM调试参数：

```bash
mvn spring-boot:run -pl tsm-auth -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
```

## 总结

TSM微服务系统采用了标准的Spring Cloud微服务架构，通过合理的启动顺序和配置，可以确保系统稳定运行。建议在生产环境中使用Docker容器化部署，并配置适当的监控和日志收集系统。

---

**最后更新**: 2024年1月
**版本**: 1.0.0
**维护团队**: TSM开发团队