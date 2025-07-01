#!/bin/bash

# BeautyClub 完全清理脚本
# 清理所有相关的Docker容器、镜像、缓存和卷

set -e

echo "=== BeautyClub 完全清理脚本 ==="
echo "警告：此脚本将清理所有BeautyClub相关的Docker资源"
echo ""

# 确认操作
read -p "确定要执行完全清理吗？(y/N): " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo "操作已取消"
    exit 1
fi

# 1. 停止并删除容器
echo "1. 停止并删除容器..."
docker stop beautyclub-backend 2>/dev/null || true
docker rm beautyclub-backend 2>/dev/null || true

# 2. 删除镜像
echo "2. 删除镜像..."
docker rmi beautyclub:latest 2>/dev/null || true
docker rmi beautyclub-app:latest 2>/dev/null || true

# 3. 清理未使用的镜像
echo "3. 清理未使用的镜像..."
docker image prune -f

# 4. 清理构建缓存
echo "4. 清理构建缓存..."
docker builder prune -f

# 5. 清理系统缓存
echo "5. 清理系统缓存..."
docker system prune -f

# 6. 清理网络
echo "6. 清理未使用的网络..."
docker network prune -f

# 7. 清理卷（可选）
echo "7. 清理未使用的卷..."
docker volume prune -f

echo ""
echo "=== 清理完成 ==="
echo "所有BeautyClub相关的Docker资源已清理"
echo ""
echo "当前Docker状态："
docker system df 