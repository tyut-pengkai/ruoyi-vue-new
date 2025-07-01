#!/bin/bash

# 设置错误时退出
set -e

echo "开始使用Docker Compose部署BeautyClub项目..."

# 检查Docker Compose是否安装
if ! command -v docker-compose &> /dev/null; then
    echo "错误: Docker Compose未安装，请先安装Docker Compose"
    exit 1
fi

# 检查Docker是否运行
if ! docker info &> /dev/null; then
    echo "错误: Docker未运行，请启动Docker服务"
    exit 1
fi

# 停止现有服务
echo "停止现有服务..."
docker-compose down

# 构建并启动服务
echo "构建并启动服务..."
docker-compose up -d --build

# 等待服务启动
echo "等待服务启动..."
sleep 60

# 检查服务状态
echo "检查服务状态..."
docker-compose ps

# 检查应用健康状态
echo "检查应用健康状态..."
for i in {1..15}; do
    if curl -f http://localhost/health &> /dev/null; then
        echo "应用启动成功！"
        echo "访问地址: http://localhost"
        echo "API地址: http://localhost/api"
        exit 0
    fi
    echo "等待应用启动... ($i/15)"
    sleep 10
done

echo "错误: 应用启动超时"
echo "查看日志: docker-compose logs beautyclub-app"
exit 1 