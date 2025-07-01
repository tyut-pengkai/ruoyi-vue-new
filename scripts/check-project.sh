#!/bin/bash

# 项目检查脚本
# 用于诊断项目结构和文件状态

set -e

echo "=== BeautyClub项目检查报告 ==="
echo "检查时间: $(date)"
echo "当前目录: $(pwd)"
echo

# 检查当前目录内容
echo "1. 当前目录内容:"
ls -la
echo

# 检查关键文件和目录
echo "2. 关键文件检查:"
if [ -f "Dockerfile" ]; then
    echo "✅ Dockerfile 存在"
else
    echo "❌ Dockerfile 不存在"
fi

if [ -f "docker-compose.yml" ]; then
    echo "✅ docker-compose.yml 存在"
else
    echo "❌ docker-compose.yml 不存在"
fi

if [ -d "scripts" ]; then
    echo "✅ scripts 目录存在"
    echo "   scripts目录内容:"
    ls -la scripts/
else
    echo "❌ scripts 目录不存在"
fi

if [ -d "nginx" ]; then
    echo "✅ nginx 目录存在"
else
    echo "❌ nginx 目录不存在"
fi

if [ -d ".git" ]; then
    echo "✅ .git 目录存在 (Git仓库)"
else
    echo "❌ .git 目录不存在 (不是Git仓库)"
fi

echo

# 检查Git状态
if [ -d ".git" ]; then
    echo "3. Git状态:"
    git status --porcelain
    echo
    echo "Git远程仓库:"
    git remote -v
    echo
fi

# 检查Docker环境
echo "4. Docker环境检查:"
if command -v docker &> /dev/null; then
    echo "✅ Docker 已安装"
    docker --version
else
    echo "❌ Docker 未安装"
fi

if command -v docker-compose &> /dev/null; then
    echo "✅ Docker Compose 已安装"
    docker-compose --version
else
    echo "❌ Docker Compose 未安装"
fi

echo

# 检查网络连接
echo "5. 网络连接检查:"
if ping -c 1 gitee.com &> /dev/null; then
    echo "✅ 可以访问 gitee.com"
else
    echo "❌ 无法访问 gitee.com"
fi

echo

# 提供解决方案
echo "6. 解决方案建议:"
if [ ! -d ".git" ]; then
    echo "🔧 项目不是Git仓库，需要重新克隆:"
    echo "   git clone https://gitee.com/your-username/BeautyClub.git ."
fi

if [ ! -d "scripts" ]; then
    echo "🔧 scripts目录不存在，项目可能未完整克隆"
    echo "   建议重新克隆项目"
fi

if [ ! -f "Dockerfile" ] || [ ! -f "docker-compose.yml" ]; then
    echo "🔧 缺少Docker配置文件，项目可能不完整"
    echo "   建议重新克隆项目"
fi

echo
echo "=== 检查完成 ===" 