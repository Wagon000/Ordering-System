# 点餐系统 (Restaurant Ordering System)

## 作者信息 (Author Information)
- **Author**: Zeyu Wang
- **Version**: 2.0.0
- **Last Updated**: 2024-02-26

## 项目介绍

这是一个基于 Spring Boot + Vue3 的现代化餐厅点餐系统，提供了完整的点餐、订单管理、用户管理等功能。系统采用前后端分离架构，后端使用 Spring Boot + MyBatis + Redis，前端使用 Vue3 + Element Plus。

### 主要功能

1. **用户功能**
   - 用户注册/登录
   - 浏览菜品列表
   - 购物车管理
   - 订单创建和查看
   - 订单评分
   - 余额查看和消费记录

2. **管理员功能**
   - 菜品管理（添加、修改、删除、上下架）
   - 订单管理（查看、处理订单）
   - 用户管理（查看、编辑用户信息）
   - 系统设置（桌位数量、配送费等）

3. **系统特色**
   - 支持堂食和外卖两种模式
   - 实时桌位管理
   - 订单状态实时更新
   - 图片上传和预览
   - 响应式设计，支持移动端访问

## 技术栈

### 后端技术栈

- **Spring Boot 2.7.5**: 应用程序框架
- **Spring Security**: 安全框架
- **MyBatis**: ORM框架
- **Redis**: 缓存服务
- **MySQL**: 数据库
- **JWT**: 用户认证
- **Maven**: 项目管理工具

### 前端技术栈

- **Vue 3**: 前端框架
- **Vite**: 构建工具
- **Element Plus**: UI组件库
- **Pinia**: 状态管理
- **Vue Router**: 路由管理
- **Axios**: HTTP客户端

## 环境要求

- JDK 1.8+
- Maven 3.6+
- MySQL 5.7+
- Redis 5.0+
- Node.js 16+
- npm 8+

## 快速开始

### 1. 环境准备

1. **安装 MySQL**
   ```bash
   # 创建数据库
   CREATE DATABASE db DEFAULT CHARACTER SET utf8mb4;
   ```

2. **安装 Redis**
   - Windows: 下载并安装 [Redis for Windows](https://github.com/microsoftarchive/redis/releases)
   - Linux: `sudo apt install redis-server` 或 `yum install redis`
   - macOS: `brew install redis`

3. **安装 Node.js**
   - 从 [Node.js官网](https://nodejs.org/) 下载安装

### 2. 配置项目

1. **克隆项目**
   ```bash
   git clone <项目地址>
   cd Ordering-System
   ```

2. **配置后端**
   
   修改 `src/main/resources/application.yml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/db?useSSL=false&characterEncoding=utf-8
       username: your_username
       password: your_password
     
     redis:
       host: localhost
       port: 6379
       password: your_redis_password # 如果有的话
   ```

3. **配置前端**
   
   修改 `frontend/.env`:
   ```
   VITE_API_BASE_URL=http://localhost:8082/api
   ```

### 3. 启动服务

1. **启动后端**
   ```bash
   mvn spring-boot:run
   ```
   后端服务将在 8082 端口启动

2. **启动前端**
   ```bash
   cd frontend
   npm install
   npm run dev
   ```
   前端服务将在 5173 端口启动

3. **使用快速启动脚本**
   ```bash
   # Windows
   start_services.bat
   ```

### 4. 访问系统

- 前端页面: http://localhost:5173
- API文档: http://localhost:8082/api/swagger-ui.html
- 管理员账户:
  - 用户名: admin
  - 密码: admin123

## 目录结构

```
Ordering-System/
├── src/                           # 后端源码
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/orderingsystem/
│   │   │       ├── config/       # 配置类
│   │   │       ├── controller/   # 控制器
│   │   │       ├── entity/       # 实体类
│   │   │       ├── mapper/       # MyBatis接口
│   │   │       ├── service/      # 服务层
│   │   │       └── security/     # 安全相关
│   │   └── resources/
│   │       ├── mapper/          # MyBatis XML
│   │       └── application.yml  # 配置文件
│   
├── frontend/                     # 前端源码
│   ├── src/
│   │   ├── api/                # API接口
│   │   ├── components/         # 组件
│   │   ├── router/            # 路由配置
│   │   ├── stores/            # 状态管理
│   │   └── views/             # 页面
│   └── package.json
│
└── pom.xml                      # Maven配置
```

## 部署说明

### 开发环境

1. 按照"快速开始"部分的说明进行配置和启动
2. 使用 IDE（如 IntelliJ IDEA）导入项目
3. 运行 `OrderingSystemApplication.java` 启动后端
4. 在 frontend 目录下运行 `npm run dev` 启动前端

### 生产环境

1. **后端打包**
   ```bash
   mvn clean package
   ```
   生成的 JAR 文件在 `target` 目录下

2. **前端打包**
   ```bash
   cd frontend
   npm run build
   ```
   生成的文件在 `frontend/dist` 目录下

3. **部署**
   - 后端: `java -jar ordering-system-0.0.1-SNAPSHOT.jar`
   - 前端: 将 dist 目录下的文件部署到 Web 服务器（如 Nginx）

## 常见问题

1. **Redis连接失败**
   - 检查Redis服务是否启动
   - 验证Redis密码配置
   - 确认防火墙设置

2. **图片上传失败**
   - 检查上传目录权限
   - 确认文件大小限制配置
   - 验证图片格式是否支持

3. **前端API调用失败**
   - 检查后端服务是否正常运行
   - 确认API基础URL配置
   - 验证跨域设置

## 更新日志

### v2.0.0 (2024-02-26)
- 升级Spring Boot到2.7.5
- 添加Redis缓存支持
- 优化图片上传功能
- 改进订单管理功能
- 添加用户余额管理
- 完善系统设置功能
- 优化前端UI/UX
- 修复已知问题

## 许可证

[MIT](LICENSE) 