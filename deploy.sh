#!/bin/bash

echo "开始部署若依管理系统..."

# 1. 构建前端
echo "1. 构建前端项目..."
cd ruoyi-ui
npm run build:prod
cd ..

# 2. 构建后端
echo "2. 构建后端项目..."
mvn clean package -Dmaven.test.skip=true

# 3. 停止现有服务
echo "3. 停止现有服务..."
pkill -f "ruoyi-admin"

# 4. 部署前端到Nginx目录
echo "4. 部署前端..."
sudo rm -rf /www/wwwroot/ruoyi-ui/*
sudo cp -r ruoyi-ui/dist/* /www/wwwroot/ruoyi-ui/

# 5. 启动后端服务
echo "5. 启动后端服务..."
nohup java -jar ruoyi-admin/target/ruoyi-admin.jar --spring.profiles.active=prod > ruoyi.log 2>&1 &

# 6. 重启Nginx
echo "6. 重启Nginx..."
sudo nginx -t && sudo nginx -s reload

echo "部署完成！"
echo "前端访问地址: http://121.40.218.168"
echo "后端服务地址: http://121.40.218.168:18080" 