# FPC (Frp Process Controller)

FPC是一个用于管理和控制frpc进程的Web应用程序，基于Spring Boot开发。

## 功能特性

- **配置管理**：创建、编辑、删除frpc配置
- **代理规则管理**：为每个配置添加多个代理规则
- **进程控制**：启动、停止、重启frpc进程
- **状态监控**：实时查看frpc进程状态
- **日志查看**：查看frpc进程日志
- **实时通知**：通过WebSocket接收系统事件和通知

## 技术栈

- **后端**：Spring Boot, Spring MVC, Spring Data JPA
- **数据库**：MySQL
- **前端**：HTML, CSS, JavaScript, WebSocket
- **工具**：Lombok, MapStruct

## 系统要求

- Java 11+
- MySQL 5.7+
- Maven 3.6+

## 快速开始

1. **克隆项目**

```bash
git clone https://github.com/your-repo/fpc.git
cd fpc
```

2. **配置数据库**

- 创建MySQL数据库
- 修改`application.yml`中的数据库连接配置

3. **运行项目**

```bash
mvn spring-boot:run
```

4. **访问应用**

打开浏览器访问 [http://localhost:8080](http://localhost:8080)

## 配置说明

主要配置文件位于`src/main/resources/application.yml`，可以配置以下内容：

- 数据库连接
- 服务器端口
- 日志配置
- frpc默认路径
- 日志目录

## 数据库初始化

项目启动时会自动执行`src/main/resources/schema.sql`创建表结构。

## 许可证

MIT License
