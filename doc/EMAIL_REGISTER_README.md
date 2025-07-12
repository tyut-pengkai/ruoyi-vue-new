# 若依Vue项目邮箱注册功能开发说明 20250616 cursor

## 功能概述

本次开发为若依Vue项目添加了完整的邮箱注册功能，包括：

- 邮箱验证码发送
- 邮箱格式验证
- 邮箱唯一性检查
- 邮箱验证码校验
- 用户注册时邮箱信息保存

## 新增文件

### 后端文件

1. **EmailService.java** - 邮箱服务类
   - 位置：`ruoyi-framework/src/main/java/com/ruoyi/framework/web/service/EmailService.java`
   - 功能：发送邮箱验证码、验证邮箱验证码

2. **SysEmailController.java** - 邮箱控制器
   - 位置：`ruoyi-admin/src/main/java/com/ruoyi/web/controller/system/SysEmailController.java`
   - 功能：处理邮箱验证码发送请求

3. **EmailServiceImpl.java** - 邮件发送服务实现
   - 位置：`ruoyi-framework/src/main/java/com/ruoyi/framework/web/service/impl/EmailServiceImpl.java`
   - 功能：实际邮件发送逻辑

4. **EmailConfig.java** - 邮件配置类
   - 位置：`ruoyi-framework/src/main/java/com/ruoyi/framework/config/EmailConfig.java`
   - 功能：邮件服务器配置

### 前端文件

1. **email.js** - 邮箱API
   - 位置：`ruoyi-ui/src/api/email.js`
   - 功能：前端邮箱相关API调用

### 修改文件

1. **RegisterBody.java** - 添加邮箱字段
2. **SysRegisterService.java** - 添加邮箱验证逻辑
3. **AsyncFactory.java** - 添加邮件发送任务
4. **CacheConstants.java** - 添加邮箱验证码缓存key
5. **register.vue** - 添加邮箱输入和验证码功能
6. **application.yml** - 添加邮件配置
7. **SecurityConfig.java**  - 在允许匿名访问的URL列表中添加了 /sendEmailCode

## 配置说明

### 1. 邮件服务器配置

在 `application.yml` 中配置邮件服务器信息：

```yaml
ruoyi:
  email:
    host: smtp.qq.com          # 邮件服务器地址
    port: 587                  # 邮件服务器端口
    username: your-email@qq.com # 发件人邮箱
    password: your-password     # 发件人密码（邮箱授权码）
    ssl: false                 # 是否启用SSL
    tls: true                  # 是否启用TLS
```

### 2. 常见邮件服务器配置

#### QQ邮箱
```yaml
host: smtp.qq.com
port: 587
ssl: false
tls: true
```

#### 163邮箱
```yaml
host: smtp.163.com
port: 25
ssl: false
tls: false
```

#### Gmail
```yaml
host: smtp.gmail.com
port: 587
ssl: false
tls: true
```

### 3. 开启注册功能

在系统配置中将 `sys.account.registerUser` 设置为 `true`，或者在数据库中执行：

```sql
UPDATE sys_config SET config_value = 'true' WHERE config_key = 'sys.account.registerUser';
```

## 使用流程

1. **用户访问注册页面**
   - 输入用户名、邮箱、密码等信息
   - 点击"发送验证码"按钮

2. **系统发送验证码**
   - 验证邮箱格式
   - 生成6位随机验证码
   - 发送邮件到用户邮箱
   - 验证码有效期5分钟

3. **用户完成注册**
   - 输入收到的邮箱验证码
   - 填写其他注册信息
   - 提交注册表单

4. **系统验证注册**
   - 验证用户名唯一性
   - 验证邮箱唯一性
   - 验证邮箱验证码
   - 验证图形验证码（如果启用）
   - 创建用户账号

## 功能特点

### 安全性
- 邮箱验证码有效期限制（5分钟）
- 验证码使用后立即删除
- 邮箱格式严格验证
- 邮箱唯一性检查

### 用户体验
- 实时邮箱格式验证
- 验证码发送状态提示
- 友好的错误提示信息
- 响应式界面设计

### 可扩展性
- 支持多种邮件服务器
- 可配置的邮件模板
- 异步邮件发送
- 完整的日志记录

## 注意事项

1. **邮件服务器配置**
   - 确保邮件服务器配置正确
   - 使用邮箱授权码而非登录密码
   - 注意防火墙和端口设置

2. **验证码安全**
   - 验证码有效期不宜过长
   - 限制验证码发送频率
   - 记录验证码发送日志

3. **用户体验**
   - 提供清晰的错误提示
   - 优化验证码发送流程
   - 考虑网络延迟问题

## 后续优化建议

1. **邮件模板**
   - 使用HTML邮件模板
   - 支持多语言邮件
   - 自定义邮件样式

2. **安全增强**
   - 添加验证码发送频率限制
   - 实现邮箱黑名单功能
   - 增加IP地址限制

3. **功能扩展**
   - 支持手机号注册
   - 添加第三方登录
   - 实现邮箱找回密码

## 测试建议

1. **功能测试**
   - 测试邮箱格式验证
   - 测试验证码发送和验证
   - 测试重复注册检查

2. **性能测试**
   - 测试并发注册性能
   - 测试邮件发送性能
   - 测试验证码验证性能

3. **安全测试**
   - 测试验证码安全性
   - 测试邮箱注入攻击
   - 测试重复提交攻击 