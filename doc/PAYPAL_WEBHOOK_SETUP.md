# PayPal Webhook 配置指南

## 前提条件

1. 已购买域名并完成DNS解析
2. 已获取SSL证书（可以使用Let's Encrypt免费证书）
3. 服务器已安装Nginx

## 配置步骤

### 1. 安装Nginx（如果未安装）

```bash
# Ubuntu/Debian
sudo apt update
sudo apt install nginx

# CentOS
sudo yum install epel-release
sudo yum install nginx
```

### 2. 配置SSL证书

1. 将SSL证书文件放置在安全位置：
```bash
sudo mkdir -p /etc/nginx/ssl
sudo cp your-certificate.crt /etc/nginx/ssl/
sudo cp your-private.key /etc/nginx/ssl/
```

2. 设置适当的权限：
```bash
sudo chmod 644 /etc/nginx/ssl/your-certificate.crt
sudo chmod 600 /etc/nginx/ssl/your-private.key
```

### 3. 配置Nginx

1. 创建Nginx配置文件：
```bash
sudo nano /etc/nginx/conf.d/paypal-webhook.conf
```

2. 复制 `paypal_nginx.conf` 中的配置内容到此文件

3. 修改配置文件中的以下内容：
   - 将 `your-domain.com` 替换为您的实际域名
   - 更新SSL证书路径为实际路径
   - 确认后端服务端口号（默认8081）

### 4. 验证和启动Nginx

1. 测试Nginx配置：
```bash
sudo nginx -t
```

2. 如果测试通过，重启Nginx：
```bash
sudo systemctl restart nginx
```

### 5. 配置防火墙

确保服务器防火墙允许80和443端口：

```bash
# Ubuntu/Debian
sudo ufw allow 80
sudo ufw allow 443

# CentOS
sudo firewall-cmd --permanent --add-service=http
sudo firewall-cmd --permanent --add-service=https
sudo firewall-cmd --reload
```

### 6. 验证配置

1. 使用浏览器访问 https://your-domain.com/api/paypal/webhook
2. 应该能看到来自后端服务的响应（可能是404，这是正常的，因为这个端点只接受POST请求）

### 7. 更新PayPal Webhook配置

1. 登录PayPal开发者控制台
2. 更新Webhook URL为：https://your-domain.com/api/paypal/webhook

## 注意事项

1. 确保域名SSL证书有效且未过期
2. 定期检查Nginx错误日志：`/var/log/nginx/error.log`
3. 确保服务器时间准确（对SSL证书验证很重要）
4. 建议设置SSL证书自动更新（如果使用Let's Encrypt）

## 故障排除

1. 如果Webhook调用失败，检查：
   - Nginx错误日志
   - 应用程序日志
   - SSL证书状态
   - 防火墙配置

2. 常见问题：
   - 证书过期
   - DNS解析问题
   - 防火墙阻止
   - 后端服务未运行

## 安全建议

1. 定期更新Nginx版本
2. 使用强SSL配置（已在配置文件中设置）
3. 启用防火墙
4. 定期检查日志文件
5. 设置SSL证书自动更新 