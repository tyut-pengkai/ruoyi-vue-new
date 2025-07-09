# Node.js 代理服务器配置指南

## 前提条件

1. 已安装 Node.js (建议版本 >= 14)
2. 已获取SSL证书
3. 已配置域名解析

## 安装依赖

```bash
npm install express http-proxy-middleware
```

## 配置步骤

### 1. SSL证书准备

1. 创建证书目录：
```bash
mkdir -p certs
```

2. 将SSL证书文件复制到 certs 目录：
```bash
cp your-certificate.crt certs/
cp your-private.key certs/
```

3. 设置适当的权限：
```bash
chmod 644 certs/your-certificate.crt
chmod 600 certs/your-private.key
```

### 2. 修改配置文件

1. 更新 `proxy-server.js` 中的SSL证书路径：
```javascript
const httpsOptions = {
  key: fs.readFileSync('./certs/your-private.key'),
  cert: fs.readFileSync('./certs/your-certificate.crt')
};
```

### 3. 配置系统权限

由于需要使用80和443端口，需要适当的系统权限：

#### Linux系统

方案1：使用 sudo（不推荐用于生产环境）
```bash
sudo node proxy-server.js
```

方案2：配置端口转发（推荐）
```bash
# 将80端口转发到8080
sudo iptables -t nat -A PREROUTING -p tcp --dport 80 -j REDIRECT --to-port 8080
# 将443端口转发到8443
sudo iptables -t nat -A PREROUTING -p tcp --dport 443 -j REDIRECT --to-port 8443
```

然后修改 proxy-server.js 中的端口：
```javascript
const HTTP_PORT = 8080;
const HTTPS_PORT = 8443;
```

#### Windows系统

使用管理员权限运行命令提示符，然后执行：
```bash
node proxy-server.js
```

### 4. 启动服务

```bash
node proxy-server.js
```

### 5. 验证配置

1. 访问 https://your-domain.com 确认网站可以正常访问
2. 确认 HTTP 自动重定向到 HTTPS
3. 测试 PayPal Webhook URL: https://your-domain.com/api/paypal/webhook

## 注意事项

1. 生产环境建议使用进程管理器（如 PM2）：
```bash
npm install -g pm2
pm2 start proxy-server.js
```

2. 监控日志：
```bash
pm2 logs proxy-server
```

3. 设置开机自启：
```bash
pm2 startup
pm2 save
```

## 故障排除

1. 端口被占用：
```bash
# 查看端口占用
netstat -tulpn | grep -E '80|443'
```

2. 证书问题：
- 确保证书路径正确
- 确保证书文件权限正确
- 检查证书是否过期

3. 代理问题：
- 检查后端服务（8081）是否正常运行
- 检查日志中的错误信息

## 安全建议

1. 使用非root用户运行Node.js
2. 定期更新依赖包：
```bash
npm audit
npm update
```
3. 配置适当的 CORS 策略
4. 启用请求速率限制
5. 定期更新SSL证书
6. 使用环境变量存储敏感信息 