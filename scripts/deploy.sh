#!/bin/bash

# 设置错误时退出
set -e

# 配置变量
PROJECT_NAME="beautyclub"
IMAGE_NAME="${PROJECT_NAME}-app"
CONTAINER_NAME="${PROJECT_NAME}-app"
NETWORK_NAME="${PROJECT_NAME}-network"

echo "开始部署BeautyClub项目..."

# 检查Docker是否运行
if ! docker info &> /dev/null; then
    echo "错误: Docker未运行，请启动Docker服务"
    exit 1
fi

# 停止并删除旧容器
echo "停止旧容器..."
docker stop ${CONTAINER_NAME} 2>/dev/null || true
docker rm ${CONTAINER_NAME} 2>/dev/null || true

# 删除旧镜像
echo "删除旧镜像..."
docker rmi ${IMAGE_NAME} 2>/dev/null || true

# 构建新镜像
echo "构建Docker镜像..."
docker build -t ${IMAGE_NAME} .

# 检查镜像是否构建成功
if [ $? -ne 0 ]; then
    echo "错误: Docker镜像构建失败"
    exit 1
fi

# 创建网络（如果不存在）
echo "创建Docker网络..."
docker network create ${NETWORK_NAME} 2>/dev/null || true

# 启动新容器
echo "启动新容器..."
docker run -d \
    --name ${CONTAINER_NAME} \
    --network ${NETWORK_NAME} \
    -p 8080:8080 \
    -e SPRING_PROFILES_ACTIVE=prod \
    -e SPRING_DATASOURCE_URL="jdbc:mysql://121.40.218.168:13306/mmclub?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8" \
    -e SPRING_DATASOURCE_USERNAME="mmclub" \
    -e SPRING_DATASOURCE_PASSWORD="8DDPpAQBrbxx!" \
    -e SPRING_REDIS_HOST="121.40.218.168" \
    -e SPRING_REDIS_PORT="26739" \
    -v app_logs:/app/logs \
    -v upload_path:/app/uploadPath \
    --restart always \
    ${IMAGE_NAME}

# 等待应用启动
echo "等待应用启动..."
sleep 30

# 检查应用健康状态
echo "检查应用健康状态..."
for i in {1..10}; do
    if curl -f http://localhost:8080/actuator/health &> /dev/null; then
        echo "应用启动成功！"
        echo "访问地址: http://localhost:8080"
        exit 0
    fi
    echo "等待应用启动... ($i/10)"
    sleep 10
done

echo "错误: 应用启动超时"
exit 1 