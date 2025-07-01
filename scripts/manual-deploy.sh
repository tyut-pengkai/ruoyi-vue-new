#!/bin/bash

# 手动部署脚本 - 支持指定分支
# 用于在宝塔服务器上手动部署BeautyClub项目

set -e

# 配置变量
PROJECT_NAME="beautyclub"
PROJECT_PATH="/www/wwwroot/${PROJECT_NAME}"
GIT_REPO="https://gitee.com/why110/BeautyClub.git"
GIT_BRANCH="main"  # 默认分支

# 日志函数
log() {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] $1"
}

# 错误处理
error_exit() {
    log "错误: $1"
    exit 1
}

# 显示帮助信息
show_help() {
    echo "用法: $0 [选项]"
    echo "选项:"
    echo "  -b, --branch BRANCH    指定要部署的分支 (默认: main)"
    echo "  -f, --force            强制重新部署（停止现有服务）"
    echo "  -h, --help             显示此帮助信息"
    echo
    echo "示例:"
    echo "  $0 -b main              # 部署main分支"
    echo "  $0 -b develop           # 部署develop分支"
    echo "  $0 -b develop -f        # 强制重新部署develop分支"
}

# 解析命令行参数
parse_args() {
    FORCE_DEPLOY=false
    while [[ $# -gt 0 ]]; do
        case $1 in
            -b|--branch)
                GIT_BRANCH="$2"
                shift 2
                ;;
            -f|--force)
                FORCE_DEPLOY=true
                shift
                ;;
            -h|--help)
                show_help
                exit 0
                ;;
            *)
                echo "未知选项: $1"
                show_help
                exit 1
                ;;
        esac
    done
}

# 检查环境
check_environment() {
    log "检查部署环境..."
    
    # 检查Docker
    if ! command -v docker &> /dev/null; then
        error_exit "Docker未安装，请先安装Docker"
    fi
    
    # 检查Docker Compose
    if ! command -v docker-compose &> /dev/null; then
        error_exit "Docker Compose未安装，请先安装Docker Compose"
    fi
    
    # 检查Git
    if ! command -v git &> /dev/null; then
        error_exit "Git未安装，请先安装Git"
    fi
    
    log "环境检查通过"
}

# 准备项目目录
prepare_project() {
    log "准备项目目录..."
    
    # 创建项目目录
    mkdir -p ${PROJECT_PATH}
    cd ${PROJECT_PATH}
    
    # 如果目录不为空，询问是否清空
    if [ "$(ls -A)" ] && [ "$FORCE_DEPLOY" = true ]; then
        log "强制模式：清空现有目录..."
        rm -rf ./*
        rm -rf ./.* 2>/dev/null || true
    elif [ "$(ls -A)" ]; then
        log "目录不为空，询问是否清空..."
        read -p "目录 ${PROJECT_PATH} 不为空，是否清空？(y/N): " -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            log "清空目录..."
            rm -rf ./*
            rm -rf ./.* 2>/dev/null || true
        else
            error_exit "用户取消操作"
        fi
    fi
}

# 克隆或更新代码
update_code() {
    log "更新项目代码 (分支: ${GIT_BRANCH})..."
    
    cd ${PROJECT_PATH}
    
    if [ -d ".git" ]; then
        # 已经是Git仓库，更新代码
        log "检测到Git仓库，更新代码..."
        git fetch origin
        git checkout ${GIT_BRANCH} 2>/dev/null || git checkout -b ${GIT_BRANCH} origin/${GIT_BRANCH}
        git pull origin ${GIT_BRANCH}
    else
        # 不是Git仓库，克隆代码
        log "克隆项目代码..."
        if git clone -b ${GIT_BRANCH} ${GIT_REPO} .; then
            log "项目克隆成功 (分支: ${GIT_BRANCH})"
        else
            log "尝试克隆默认分支..."
            if git clone ${GIT_REPO} .; then
                log "项目克隆成功，切换到指定分支..."
                git checkout ${GIT_BRANCH} 2>/dev/null || log "分支 ${GIT_BRANCH} 不存在，使用当前分支"
            else
                error_exit "项目克隆失败"
            fi
        fi
    fi
    
    # 设置脚本权限
    if [ -d "scripts" ]; then
        chmod +x scripts/*.sh
        log "脚本权限设置完成"
    fi
}

# 停止现有服务
stop_services() {
    log "停止现有服务..."
    
    cd ${PROJECT_PATH}
    
    if [ -f "docker-compose.yml" ]; then
        docker-compose down --remove-orphans 2>/dev/null || true
        log "现有服务已停止"
    else
        log "未找到docker-compose.yml，跳过停止服务"
    fi
}

# 构建和启动服务
start_services() {
    log "构建和启动服务..."
    
    cd ${PROJECT_PATH}
    
    # 检查必要文件
    if [ ! -f "docker-compose.yml" ]; then
        error_exit "未找到docker-compose.yml文件"
    fi
    
    if [ ! -f "Dockerfile" ]; then
        error_exit "未找到Dockerfile文件"
    fi
    
    # 构建并启动服务
    log "使用Docker Compose启动服务..."
    docker-compose up -d --build
    
    if [ $? -eq 0 ]; then
        log "服务启动命令执行成功"
    else
        error_exit "服务启动失败"
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
    echo "部署分支: ${GIT_BRANCH}"
    echo "Git仓库: ${GIT_REPO}"
    echo "当前分支: $(git branch --show-current 2>/dev/null || echo '未知')"
    echo "最新提交: $(git log -1 --pretty=format:'%h - %s (%cr)' 2>/dev/null || echo '未知')"
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
    echo "更新代码: ./scripts/manual-deploy.sh -b ${GIT_BRANCH}"
}

# 主函数
main() {
    log "开始手动部署BeautyClub项目..."
    
    # 解析命令行参数
    parse_args "$@"
    
    log "部署配置:"
    log "  分支: ${GIT_BRANCH}"
    log "  强制模式: ${FORCE_DEPLOY}"
    log "  项目路径: ${PROJECT_PATH}"
    echo
    
    # 执行部署步骤
    check_environment
    prepare_project
    update_code
    stop_services
    start_services
    wait_for_services
    show_result
    
    log "手动部署完成！"
}

# 执行主函数
main "$@" 