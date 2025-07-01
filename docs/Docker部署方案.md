# BeautyClub项目Docker自动部署方案

## 概述

本文档详细介绍了如何使用Docker在宝塔面板上自动部署基于RuoYi框架的BeautyClub项目，实现代码推送后自动构建和发布。

## 系统架构

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   本地开发环境    │    │   Gitee仓库     │    │   宝塔服务器     │
│                 │    │                 │    │                 │
│  BeautyClub     │───▶│   代码仓库       │───▶│   Docker环境    │
│   项目代码       │    │                 │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                                       │
                                                       ▼
                                              ┌─────────────────┐
                                              │   服务组件       │
                                              │                 │
                                              │  Nginx (80/443)  │
                                              │  App (8080)      │
                                              │  MySQL (3306)    │
                                              │  Redis (6379)    │
                                              └─────────────────┘
```

## 环境要求

### 宝塔服务器要求
- 操作系统：CentOS 7+ / Ubuntu 18+ / Debian 9+
- 内存：至少2GB
- 磁盘：至少20GB可用空间
- 宝塔面板：7.0+

### 软件要求
- Docker 20.10+
- Docker Compose 2.0+
- Git
- Maven 3.6+（可选，用于本地构建）

## 部署步骤

### 1. 宝塔面板环境准备

#### 1.1 安装Docker
在宝塔面板中安装Docker管理器：
1. 登录宝塔面板
2. 进入"软件商店"
3. 搜索"Docker管理器"并安装
4. 启动Docker服务

#### 1.2 安装Docker Compose
```bash
# SSH连接到服务器
sudo curl -L "https://github.com/docker/compose/releases/download/v2.20.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```

#### 1.3 创建项目目录
```bash
mkdir -p /www/wwwroot/beautyclub
cd /www/wwwroot/beautyclub
```

### 2. 项目配置

#### 2.1 克隆项目代码
```bash
cd /www/wwwroot/beautyclub
git clone https://gitee.com/your-username/BeautyClub.git .
```

#### 2.2 配置环境变量
创建环境配置文件：
```bash
cp ruoyi-admin/src/main/resources/application.yml ruoyi-admin/src/main/resources/application-prod.yml
```

编辑生产环境配置：
```yaml
# 数据库配置
spring:
  datasource:
    url: jdbc:mysql://mysql:3306/beautyclub?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
    username: beautyclub
    password: beautyclub123
    driver-class-name: com.mysql.cj.jdbc.Driver
    
  # Redis配置
  redis:
    host: redis
    port: 6379
    password: 
    database: 0
    
# 文件上传配置
ruoyi:
  profile: /app/uploadPath
```

### 3. 自动部署配置

#### 3.1 配置GitHub Actions（推荐）

1. 在GitHub仓库中设置Secrets：
   - `BT_HOST`: 宝塔服务器IP
   - `BT_USERNAME`: SSH用户名
   - `BT_PASSWORD`: SSH密码
   - `BT_PORT`: SSH端口（默认22）
   - `DOCKER_USERNAME`: Docker Hub用户名
   - `DOCKER_PASSWORD`: Docker Hub密码

2. 推送代码到GitHub，Actions会自动触发部署

#### 3.2 配置Gitee Webhook（备选方案）

1. 在宝塔面板中创建Webhook脚本：
```bash
#!/bin/bash
cd /www/wwwroot/beautyclub
./scripts/bt-deploy.sh
```

2. 在Gitee仓库中配置Webhook：
   - URL: `http://your-server-ip:8888/webhook/beautyclub`
   - 触发事件：Push

### 4. 首次部署

#### 4.1 手动部署
```bash
cd /www/wwwroot/beautyclub
chmod +x scripts/*.sh
./scripts/bt-deploy.sh
```

#### 4.2 使用Docker Compose
```bash
cd /www/wwwroot/beautyclub
docker-compose up -d --build
```

### 5. 验证部署

#### 5.1 检查服务状态
```bash
docker-compose ps
```

#### 5.2 检查应用健康状态
```bash
curl http://localhost/health
```

#### 5.3 访问应用
- 前端地址：http://your-server-ip
- API地址：http://your-server-ip/api
- 管理后台：http://your-server-ip/login

## 自动化流程

### 代码推送流程
1. 开发者在本地修改代码
2. 提交并推送到Gitee仓库
3. Webhook触发宝塔服务器部署脚本
4. 自动拉取最新代码
5. 构建Docker镜像
6. 重启服务
7. 健康检查确认部署成功

### 部署脚本功能
- 自动备份数据库和文件
- 停止现有服务
- 拉取最新代码
- 构建项目
- 启动新服务
- 健康检查
- 清理资源

## 监控和维护

### 日志查看
```bash
# 查看应用日志
docker-compose logs beautyclub-app

# 查看数据库日志
docker-compose logs mysql

# 查看Redis日志
docker-compose logs redis

# 查看Nginx日志
docker-compose logs nginx
```

### 数据备份
```bash
# 备份数据库
docker exec beautyclub-mysql mysqldump -u beautyclub -pbeautyclub123 beautyclub > backup.sql

# 备份上传文件
docker run --rm -v beautyclub_upload_path:/data -v $(pwd):/backup alpine tar czf /backup/upload_files.tar.gz -C /data .
```

### 服务管理
```bash
# 重启服务
docker-compose restart

# 停止服务
docker-compose down

# 更新服务
docker-compose pull && docker-compose up -d
```

## 故障排除

### 常见问题

#### 1. 应用启动失败
- 检查数据库连接配置
- 查看应用日志：`docker-compose logs beautyclub-app`
- 确认端口是否被占用

#### 2. 数据库连接失败
- 检查MySQL容器状态：`docker-compose ps mysql`
- 确认数据库配置正确
- 检查网络连接

#### 3. 文件上传失败
- 检查uploadPath目录权限
- 确认Nginx配置正确
- 查看Nginx错误日志

#### 4. 内存不足
- 调整JVM参数：修改Dockerfile中的JAVA_OPTS
- 增加服务器内存
- 优化应用配置

### 性能优化

#### 1. JVM调优
```dockerfile
ENV JAVA_OPTS="-Xms1g -Xmx2g -XX:+UseG1GC -XX:MaxGCPauseMillis=200"
```

#### 2. 数据库优化
- 调整MySQL配置
- 添加数据库索引
- 优化查询语句

#### 3. 缓存优化
- 配置Redis缓存
- 使用CDN加速静态资源
- 启用Nginx缓存

## 安全配置

### 1. 防火墙配置
```bash
# 只开放必要端口
firewall-cmd --permanent --add-port=80/tcp
firewall-cmd --permanent --add-port=443/tcp
firewall-cmd --permanent --add-port=22/tcp
firewall-cmd --reload
```

### 2. SSL证书配置
```nginx
server {
    listen 443 ssl;
    ssl_certificate /path/to/cert.pem;
    ssl_certificate_key /path/to/key.pem;
    # ... 其他配置
}
```

### 3. 数据库安全
- 修改默认密码
- 限制数据库访问IP
- 定期备份数据

## 总结

通过以上配置，您可以实现：
1. 代码推送后自动部署
2. 容器化部署，环境一致
3. 自动化备份和恢复
4. 监控和日志管理
5. 高可用和扩展性

这套方案可以大大提高开发和部署效率，减少人为错误，确保应用稳定运行。 