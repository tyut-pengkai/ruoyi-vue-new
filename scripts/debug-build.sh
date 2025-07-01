#!/bin/bash

# 调试构建脚本
# 用于诊断Docker构建问题

set -e

echo "=== Docker构建调试脚本 ==="
echo "时间: $(date)"
echo "当前目录: $(pwd)"
echo

# 检查Dockerfile
echo "1. 检查Dockerfile:"
if [ -f "Dockerfile" ]; then
    echo "✅ Dockerfile存在"
    echo "Dockerfile内容预览:"
    head -20 Dockerfile
    echo "..."
else
    echo "❌ Dockerfile不存在"
    exit 1
fi
echo

# 检查项目结构
echo "2. 检查项目结构:"
echo "当前目录内容:"
ls -la
echo

echo "检查关键目录:"
for dir in ruoyi-admin ruoyi-common ruoyi-framework ruoyi-system ruoyi-quartz ruoyi-generator; do
    if [ -d "$dir" ]; then
        echo "✅ $dir 目录存在"
        if [ -d "$dir/src" ]; then
            echo "  ✅ $dir/src 目录存在"
        else
            echo "  ❌ $dir/src 目录不存在"
        fi
        if [ -f "$dir/pom.xml" ]; then
            echo "  ✅ $dir/pom.xml 文件存在"
        else
            echo "  ❌ $dir/pom.xml 文件不存在"
        fi
    else
        echo "❌ $dir 目录不存在"
    fi
done
echo

# 检查pom.xml
echo "3. 检查pom.xml:"
if [ -f "pom.xml" ]; then
    echo "✅ pom.xml存在"
    echo "pom.xml内容预览:"
    head -10 pom.xml
    echo "..."
else
    echo "❌ pom.xml不存在"
fi
echo

# 清理Docker缓存
echo "4. 清理Docker缓存:"
docker system prune -f
docker builder prune -f
echo "✅ Docker缓存已清理"
echo

# 测试Docker构建
echo "5. 测试Docker构建:"
echo "开始构建测试..."

# 创建临时Dockerfile进行测试
cat > Dockerfile.test << 'EOF'
# 测试构建阶段
FROM maven:3.8-openjdk-8 AS builder

WORKDIR /build

# 复制整个项目
COPY . .

# 显示构建环境
RUN echo "=== 构建环境信息 ===" && \
    echo "工作目录: $(pwd)" && \
    echo "目录内容:" && \
    ls -la && \
    echo "Maven版本:" && \
    mvn --version && \
    echo "Java版本:" && \
    java -version

# 尝试构建
RUN echo "=== 开始构建 ===" && \
    mvn clean package -DskipTests

# 检查构建结果
RUN echo "=== 构建结果 ===" && \
    echo "target目录内容:" && \
    ls -la ruoyi-admin/target/ 2>/dev/null || echo "target目录不存在" && \
    echo "查找jar文件:" && \
    find . -name "*.jar" -type f
EOF

echo "创建测试Dockerfile完成"
echo

# 执行测试构建
echo "执行测试构建..."
if docker build -f Dockerfile.test -t beautyclub-test .; then
    echo "✅ 测试构建成功"
    
    # 检查构建结果
    echo "检查构建结果:"
    docker run --rm beautyclub-test find /build -name "*.jar" -type f
    
else
    echo "❌ 测试构建失败"
    echo "查看详细错误信息..."
    docker build -f Dockerfile.test -t beautyclub-test . 2>&1 | tail -20
fi

# 清理测试文件
rm -f Dockerfile.test
docker rmi beautyclub-test 2>/dev/null || true

echo
echo "=== 调试完成 ===" 