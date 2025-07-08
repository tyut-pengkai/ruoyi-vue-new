# BeautyClub Docker部署快速开始指南

## 🚀 快速部署

### 1. 宝塔面板准备
```bash
# 在宝塔面板中安装Docker管理器
# 软件商店 -> 搜索"Docker管理器" -> 安装并启动
```

### 2. 服务器环境准备
```bash
# SSH连接到服务器
ssh root@your-server-ip

# 安装Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/download/v2.20.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# 创建项目目录
mkdir -p /www/wwwroot/beautyclub
cd /www/wwwroot/beautyclub
```

### 3. 克隆项目
```bash
# 克隆项目代码
git clone https://gitee.com/your-username/BeautyClub.git .

# 设置脚本权限
chmod +x scripts/*.sh
```

### 4. 一键部署
```bash
# 使用宝塔专用部署脚本
./scripts/bt-deploy.sh
```

### 5. 验证部署
```bash
# 查看服务状态
docker-compose ps

# 检查健康状态
curl http://localhost/health

# 访问应用
# 前端: http://your-server-ip
# 后台: http://your-server-ip/login
```

## 📋 自动化配置

### 方案一：GitHub Actions（推荐）
1. 将代码推送到GitHub
2. 在GitHub仓库设置Secrets
3. 推送代码自动触发部署

### 方案二：Gitee Webhook
1. 在宝塔面板创建Webhook
2. 在Gitee仓库配置Webhook
3. 推送代码自动触发部署

## 🔧 常用命令

```bash
# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs beautyclub-app

# 重启服务
docker-compose restart

# 停止服务
docker-compose down

# 更新服务
docker-compose pull && docker-compose up -d

# 备份数据
./scripts/backup.sh
```

## 📁 项目结构

```
BeautyClub/
├── Dockerfile                 # Docker镜像构建文件
├── docker-compose.yml         # 服务编排文件
├── .dockerignore             # Docker忽略文件
├── nginx/                    # Nginx配置
│   ├── nginx.conf
│   └── conf.d/
│       └── default.conf
├── scripts/                  # 部署脚本
│   ├── build.sh             # 构建脚本
│   ├── deploy.sh            # 部署脚本
│   ├── bt-deploy.sh         # 宝塔部署脚本
│   └── docker-compose-deploy.sh
├── docs/                    # 文档
│   └── Docker部署方案.md
└── .github/workflows/       # GitHub Actions
    └── deploy.yml
```

## ⚠️ 注意事项

1. **数据库密码**：请修改默认密码
2. **端口配置**：确保80、443、8080端口未被占用
3. **内存要求**：建议至少2GB内存
4. **磁盘空间**：建议至少20GB可用空间

## 🆘 故障排除

### 常见问题
1. **应用启动失败**：检查数据库连接和日志
2. **端口冲突**：修改docker-compose.yml中的端口映射
3. **内存不足**：调整JVM参数或增加服务器内存

### 查看日志
```bash
# 应用日志
docker-compose logs beautyclub-app

# 数据库日志
docker-compose logs mysql

# Nginx日志
docker-compose logs nginx
```

## 📞 技术支持

如遇到问题，请：
1. 查看详细文档：`docs/Docker部署方案.md`
2. 检查日志文件
3. 确认环境配置

---

**快速开始完成！** 🎉 