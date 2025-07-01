#!/bin/bash

# 设置错误时退出
set -e

echo "开始构建BeautyClub项目..."

# 检查Maven是否安装
if ! command -v mvn &> /dev/null; then
    echo "错误: Maven未安装，请先安装Maven"
    exit 1
fi

# 检查Docker是否安装
if ! command -v docker &> /dev/null; then
    echo "错误: Docker未安装，请先安装Docker"
    exit 1
fi

# 清理之前的构建
echo "清理之前的构建..."
mvn clean

# 编译项目
echo "编译项目..."
mvn compile

# 运行测试（可选）
# echo "运行测试..."
# mvn test

# 打包项目
echo "打包项目..."
mvn package -DskipTests

# 检查jar包是否存在
if [ ! -f "ruoyi-admin/target/ruoyi-admin.jar" ]; then
    echo "错误: 构建失败，jar包不存在"
    exit 1
fi

echo "项目构建完成！"
echo "jar包位置: ruoyi-admin/target/ruoyi-admin.jar" 