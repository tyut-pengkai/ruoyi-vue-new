#!/bin/bash

# BeautyClub 简单部署脚本
# 适用于宝塔Docker环境

set -e

# 配置变量
PROJECT_NAME="beautyclub"
IMAGE_NAME="beautyclub:latest"
CONTAINER_NAME="beautyclub-app"
PORT=8080

echo "=== BeautyClub 部署脚本 ==="
echo "项目名称: $PROJECT_NAME"
echo "镜像名称: $IMAGE_NAME"
echo "容器名称: $CONTAINER_NAME"
echo "端口: $PORT"
echo ""

# 1. 清理旧的容器和镜像
echo "1. 清理旧的容器和镜像..."
docker stop $CONTAINER_NAME 2>/dev/null || true
docker rm $CONTAINER_NAME 2>/dev/null || true
docker rmi $IMAGE_NAME 2>/dev/null || true

# 清理Docker缓存
echo "   清理Docker缓存..."
docker system prune -f

# 2. 构建新镜像
echo ""
echo "2. 构建新镜像..."
docker build -t $IMAGE_NAME .

# 3. 运行新容器
echo ""
echo "3. 启动新容器..."
docker run -d \
  --name $CONTAINER_NAME \
  --restart unless-stopped \
  -p $PORT:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  $IMAGE_NAME

# 4. 检查容器状态
echo ""
echo "4. 检查容器状态..."
sleep 5
docker ps | grep $CONTAINER_NAME

# 5. 检查应用健康状态
echo ""
echo "5. 检查应用健康状态..."
for i in {1..10}; do
  if curl -f http://localhost:$PORT/ >/dev/null 2>&1; then
    echo "   应用启动成功！"
    echo "   访问地址: http://localhost:$PORT"
    break
  else
    echo "   等待应用启动... ($i/10)"
    sleep 3
  fi
done

echo ""
echo "=== 部署完成 ==="
echo "容器名称: $CONTAINER_NAME"
echo "访问端口: $PORT"
echo "查看日志: docker logs $CONTAINER_NAME"
echo "停止服务: docker stop $CONTAINER_NAME" 