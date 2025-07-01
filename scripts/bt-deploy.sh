#!/bin/bash

# 宝塔面板自动部署脚本
# 用于在宝塔服务器上自动部署BeautyClub项目

set -e

# 配置变量
PROJECT_NAME="beautyclub"
PROJECT_PATH="/www/wwwroot/${PROJECT_NAME}"
DOCKER_COMPOSE_FILE="${PROJECT_PATH}/docker-compose.yml"
LOG_FILE="${PROJECT_PATH}/deploy.log"

# 日志函数
log() {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] $1" | tee -a ${LOG_FILE}
}

# 错误处理
error_exit() {
    log "错误: $1"
    exit 1
}

# 检查Docker是否安装
check_docker() {
    if ! command -v docker &> /dev/null; then
        error_exit "Docker未安装，请先安装Docker"
    fi
    
    if ! docker info &> /dev/null; then
        error_exit "Docker服务未运行，请启动Docker服务"
    fi
    
    log "Docker检查通过"
}

# 检查Docker Compose是否安装
check_docker_compose() {
    if ! command -v docker-compose &> /dev/null; then
        error_exit "Docker Compose未安装，请先安装Docker Compose"
    fi
    
    log "Docker Compose检查通过"
}

# 备份当前数据
backup_data() {
    log "开始备份数据..."
    
    BACKUP_DIR="${PROJECT_PATH}/backup/$(date +%Y%m%d_%H%M%S)"
    mkdir -p ${BACKUP_DIR}
    
    # 备份数据库
    if docker ps | grep -q "beautyclub-mysql"; then
        docker exec beautyclub-mysql mysqldump -u beautyclub -pbeautyclub123 beautyclub > ${BACKUP_DIR}/database.sql
        log "数据库备份完成: ${BACKUP_DIR}/database.sql"
    fi
    
    # 备份上传文件
    if docker volume ls | grep -q "beautyclub_upload_path"; then
        docker run --rm -v beautyclub_upload_path:/data -v ${BACKUP_DIR}:/backup alpine tar czf /backup/upload_files.tar.gz -C /data .
        log "上传文件备份完成: ${BACKUP_DIR}/upload_files.tar.gz"
    fi
    
    log "数据备份完成"
}

# 停止现有服务
stop_services() {
    log "停止现有服务..."
    
    if [ -f "${DOCKER_COMPOSE_FILE}" ]; then
        cd ${PROJECT_PATH}
        docker-compose down --remove-orphans
        log "现有服务已停止"
    else
        log "未找到docker-compose.yml文件，跳过停止服务"
    fi
}

# 拉取最新代码
pull_latest_code() {
    log "拉取最新代码..."
    
    cd ${PROJECT_PATH}
    
    if [ -d ".git" ]; then
        git fetch origin
        git reset --hard origin/main
        log "代码更新完成"
    else
        log "未找到Git仓库，跳过代码更新"
    fi
}

# 构建项目
build_project() {
    log "开始构建项目..."
    
    cd ${PROJECT_PATH}
    
    # 检查Maven是否可用
    if command -v mvn &> /dev/null; then
        log "使用Maven构建项目..."
        mvn clean package -DskipTests
        log "Maven构建完成"
    else
        log "Maven不可用，跳过本地构建"
    fi
}

# 启动服务
start_services() {
    log "启动服务..."
    
    cd ${PROJECT_PATH}
    
    # 拉取最新镜像
    docker-compose pull
    
    # 启动服务
    docker-compose up -d --build
    
    log "服务启动命令已执行"
}

# 等待服务启动
wait_for_services() {
    log "等待服务启动..."
    
    # 等待MySQL启动
    log "等待MySQL启动..."
    for i in {1..30}; do
        if docker exec beautyclub-mysql mysqladmin ping -h localhost -u beautyclub -pbeautyclub123 --silent; then
            log "MySQL启动成功"
            break
        fi
        if [ $i -eq 30 ]; then
            error_exit "MySQL启动超时"
        fi
        sleep 2
    done
    
    # 等待应用启动
    log "等待应用启动..."
    for i in {1..60}; do
        if curl -f http://localhost/health &> /dev/null; then
            log "应用启动成功"
            break
        fi
        if [ $i -eq 60 ]; then
            error_exit "应用启动超时"
        fi
        sleep 5
    done
}

# 清理资源
cleanup() {
    log "清理Docker资源..."
    
    # 清理未使用的镜像
    docker image prune -f
    
    # 清理未使用的容器
    docker container prune -f
    
    # 清理未使用的网络
    docker network prune -f
    
    log "资源清理完成"
}

# 显示服务状态
show_status() {
    log "显示服务状态..."
    
    cd ${PROJECT_PATH}
    docker-compose ps
    
    log "部署完成！"
    log "访问地址: http://localhost"
    log "API地址: http://localhost/api"
    log "健康检查: http://localhost/health"
}

# 主函数
main() {
    log "开始部署BeautyClub项目..."
    
    # 创建项目目录
    mkdir -p ${PROJECT_PATH}
    cd ${PROJECT_PATH}
    
    # 执行部署步骤
    check_docker
    check_docker_compose
    backup_data
    stop_services
    pull_latest_code
    build_project
    start_services
    wait_for_services
    cleanup
    show_status
    
    log "部署完成！"
}

# 执行主函数
main "$@" 