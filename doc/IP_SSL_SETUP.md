# 基于IP地址的SSL证书配置指南

## 方案一：使用ZeroSSL获取免费证书（推荐）

1. 访问 ZeroSSL 官网：https://zerossl.com/

2. 注册免费账号

3. 在控制台点击 "New Certificate"

4. 选择 "90-Day Free SSL"

5. 在 "Domain Names" 中输入您的外网IP地址

6. 验证方式选择 "HTTP File Upload" 或 "DNS" (推荐HTTP File Upload，比较简单)

7. 按照网站提示完成验证步骤

8. 下载证书文件：
   - 私钥文件 (private.key)
   - 证书文件 (certificate.crt)
   - CA Bundle文件 (ca_bundle.crt)

## 配置Node.js代理服务器

1. 创建证书目录：
```bash
mkdir -p certs
```

2. 复制证书文件：
```bash
cp private.key certs/
cp certificate.crt certs/
cp ca_bundle.crt certs/
```

3. 修改 proxy-server.js 中的证书配置：
```javascript
const httpsOptions = {
  key: fs.readFileSync('./certs/private.key'),
  cert: fs.readFileSync('./certs/certificate.crt'),
  ca: fs.readFileSync('./certs/ca_bundle.crt')
};
```

4. 启动服务：
```bash
# Windows (管理员权限)
node proxy-server.js

# Linux (使用端口转发)
sudo iptables -t nat -A PREROUTING -p tcp --dport 80 -j REDIRECT --to-port 8080
sudo iptables -t nat -A PREROUTING -p tcp --dport 443 -j REDIRECT --to-port 8443
node proxy-server.js
```

## 端口说明

- HTTP: 80 (标准HTTP端口)
- HTTPS: 443 (PayPal要求)
- 后端服务: 8081 (保持不变)

## PayPal配置

1. 在PayPal开发者控制台中配置Webhook URL：
```
https://YOUR_IP_ADDRESS/api/paypal/webhook
```

2. 确保使用的是完整的HTTPS URL，例如：
```
https://203.0.113.1/api/paypal/webhook
```

## 注意事项

1. SSL证书有效期为90天，需要在到期前续期

2. 确保服务器防火墙开放了80和443端口：
```bash
# Ubuntu/Debian
sudo ufw allow 80
sudo ufw allow 443

# CentOS
sudo firewall-cmd --permanent --add-port=80/tcp
sudo firewall-cmd --permanent --add-port=443/tcp
sudo firewall-cmd --reload
```

3. 如果使用云服务器，还需要在云服务商的安全组中开放这些端口

4. 每90天需要更新一次证书，建议设置日历提醒

## 证书更新步骤

1. 在证书到期前访问 ZeroSSL 控制台

2. 找到对应证书点击 "Renew"

3. 按照步骤完成验证

4. 下载新证书

5. 替换服务器上的证书文件

6. 重启Node.js服务

## 故障排除

1. 证书不被信任：
   - 确保使用了完整的证书链（包括 ca_bundle.crt）
   - 确认证书未过期

2. 端口访问问题：
   - 检查80和443端口是否开放
   - 检查云服务商安全组配置
   - 确认端口未被其他服务占用

3. PayPal Webhook测试失败：
   - 确认URL使用了HTTPS（443端口）
   - 确认使用了正确的IP地址
   - 检查证书是否被PayPal信任

4. HTTP到HTTPS重定向问题：
   - 访问 http://YOUR_IP 应该会自动重定向到 https://YOUR_IP
   - 如果重定向不正常，检查Node.js服务日志

## 权限说明

1. Windows系统：
   - 需要以管理员权限运行命令提示符
   - 使用管理员权限启动Node.js

2. Linux系统：
   - 使用端口转发避免需要root权限运行Node.js
   - 或使用 authbind 允许非root用户绑定特权端口 