const express = require('express');
const { createProxyMiddleware } = require('http-proxy-middleware');
const path = require('path');
const https = require('https');
const http = require('http');
const fs = require('fs');

const app = express();
const HTTP_PORT = 80;
const HTTPS_PORT = 443;

// SSL证书配置
const httpsOptions = {
  key: fs.readFileSync('./certs/private.key'),
  cert: fs.readFileSync('./certs/certificate.crt'),
  ca: fs.readFileSync('./certs/ca_bundle.crt')
};

// --- 中间件配置 ---

// 1. HTTP 重定向到 HTTPS
app.use((req, res, next) => {
  if (!req.secure) {
    return res.redirect(`https://${req.headers.host}${req.url}`);
  }
  next();
});

// 2. PayPal Webhook 专用代理
// PayPal Webhook 需要特殊处理，确保请求体不被修改
app.use('/api/paypal/webhook', createProxyMiddleware({
  target: 'http://localhost:8081',
  changeOrigin: true,
  onProxyReq: (proxyReq, req, res) => {
    // 保留原始请求头
    proxyReq.setHeader('X-Forwarded-Proto', 'https');
    proxyReq.setHeader('X-Forwarded-Port', '443');
  }
}));

// 3. 常规 API 代理
app.use('/stage-api', createProxyMiddleware({
  target: 'http://localhost:8081',
  changeOrigin: true,
  pathRewrite: {
    '^/stage-api': ''
  }
}));

// 4. 静态文件服务
app.use(express.static(path.join(__dirname, 'frontend')));

// 5. 前端路由处理
app.get('*', (req, res) => {
  res.sendFile(path.join(__dirname, 'frontend', 'index.html'));
});

// 创建 HTTPS 服务器
const httpsServer = https.createServer(httpsOptions, app);

// 创建 HTTP 服务器（在80端口）
const httpServer = http.createServer(app);

// 启动服务器
httpServer.listen(HTTP_PORT, () => {
  console.log(`HTTP Server running on http://localhost:${HTTP_PORT}`);
});

httpsServer.listen(HTTPS_PORT, () => {
  console.log(`HTTPS Server running on https://localhost:${HTTPS_PORT}`);
}); 