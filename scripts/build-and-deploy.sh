#!/bin/bash

# 构建和部署脚本
# 先构建项目，再部署到Docker

set -e

# 配置变量
PROJECT_PATH="/www/wwwroot/beautyclub"

# 日志函数
log() {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] $1"
}

# 错误处理
error_exit() {
    log "错误: $1"
    exit 1
}

# 检查环境
check_environment() {
    log "检查构建环境..."
    
    # 检查Maven
    if ! command -v mvn &> /dev/null; then
        error_exit "Maven未安装，请先安装Maven"
    fi
    
    # 检查Docker
    if ! command -v docker &> /dev/null; then
        error_exit "Docker未安装，请先安装Docker"
    fi
    
    # 检查Docker Compose
    if ! command -v docker-compose &> /dev/null; then
        error_exit "Docker Compose未安装，请先安装Docker Compose"
    fi
    
    log "环境检查通过"
}

# 构建项目
build_project() {
    log "开始构建项目..."
    
    cd ${PROJECT_PATH}
    
    # 清理之前的构建
    log "清理之前的构建..."
    mvn clean
    
    # 编译项目
    log "编译项目..."
    mvn compile
    
    # 打包项目
    log "打包项目..."
    mvn package -DskipTests
    
    # 检查jar包是否存在
    if [ ! -f "ruoyi-admin/target/ruoyi-admin.jar" ]; then
        error_exit "构建失败，jar包不存在"
    fi
    
    log "项目构建完成！"
    log "jar包位置: ruoyi-admin/target/ruoyi-admin.jar"
}

# 部署到Docker
deploy_to_docker() {
    log "开始部署到Docker..."
    
    cd ${PROJECT_PATH}
    
    # 停止现有服务
    log "停止现有服务..."
    docker-compose down --remove-orphans 2>/dev/null || true
    
    # 构建并启动服务
    log "构建并启动Docker服务..."
    docker-compose up -d --build
    
    if [ $? -eq 0 ]; then
        log "Docker服务启动成功"
    else
        error_exit "Docker服务启动失败"
    fi
}

# 等待服务启动
wait_for_services() {
    log "等待服务启动..."
    
    # 等待MySQL启动
    log "等待MySQL启动..."
    for i in {1..30}; do
        if docker exec beautyclub-mysql mysqladmin ping -h localhost -u beautyclub -pbeautyclub123 --silent 2>/dev/null; then
            log "MySQL启动成功"
            break
        fi
        if [ $i -eq 30 ]; then
            log "警告: MySQL启动超时，继续等待应用启动"
        fi
        sleep 2
    done
    
    # 等待应用启动
    log "等待应用启动..."
    for i in {1..60}; do
        if curl -f http://localhost/health 2>/dev/null; then
            log "应用启动成功"
            break
        fi
        if [ $i -eq 60 ]; then
            log "警告: 应用启动超时，请检查日志"
        fi
        sleep 5
    done
}

# 显示部署结果
show_result() {
    log "显示部署结果..."
    
    cd ${PROJECT_PATH}
    
    echo
    echo "=== 部署完成 ==="
    echo "项目路径: ${PROJECT_PATH}"
    echo "构建时间: $(date)"
    echo
    
    # 显示服务状态
    echo "=== 服务状态 ==="
    docker-compose ps
    
    echo
    echo "=== 访问地址 ==="
    echo "前端地址: http://$(hostname -I | awk '{print $1}')"
    echo "API地址: http://$(hostname -I | awk '{print $1}')/api"
    echo "健康检查: http://$(hostname -I | awk '{print $1}')/health"
    echo
    
    echo "=== 常用命令 ==="
    echo "查看日志: docker-compose logs beautyclub-app"
    echo "重启服务: docker-compose restart"
    echo "停止服务: docker-compose down"
    echo "重新构建: ./scripts/build-and-deploy.sh"
}

# 主函数
main() {
    log "开始构建和部署BeautyClub项目..."
    
    # 执行步骤
    check_environment
    build_project
    deploy_to_docker
    wait_for_services
    show_result
    
    log "构建和部署完成！"
}

# 执行主函数
main "$@" 