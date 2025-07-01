#!/bin/bash

# 一键更新和部署脚本
# 支持指定分支更新代码并部署

set -e

# 配置变量
PROJECT_PATH="/www/wwwroot/beautyclub"
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
    echo "  -b, --branch BRANCH    指定要更新的分支 (默认: main)"
    echo "  -f, --force            强制重新部署（清理缓存）"
    echo "  -h, --help             显示此帮助信息"
    echo
    echo "示例:"
    echo "  $0 -b main              # 更新main分支并部署"
    echo "  $0 -b develop           # 更新develop分支并部署"
    echo "  $0 -b develop -f        # 强制更新develop分支并重新部署"
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
    log "检查环境..."
    
    # 检查Git
    if ! command -v git &> /dev/null; then
        error_exit "Git未安装，请先安装Git"
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

# 更新代码
update_code() {
    log "更新项目代码 (分支: ${GIT_BRANCH})..."
    
    cd ${PROJECT_PATH}
    
    if [ -d ".git" ]; then
        # 已经是Git仓库，更新代码
        log "检测到Git仓库，更新代码..."
        
        # 保存当前分支
        CURRENT_BRANCH=$(git branch --show-current 2>/dev/null || echo "unknown")
        log "当前分支: ${CURRENT_BRANCH}"
        
        # 获取最新代码
        git fetch origin
        
        # 切换到指定分支
        if git checkout ${GIT_BRANCH} 2>/dev/null; then
            log "切换到分支: ${GIT_BRANCH}"
        else
            log "分支 ${GIT_BRANCH} 不存在，创建并切换到该分支"
            git checkout -b ${GIT_BRANCH} origin/${GIT_BRANCH}
        fi
        
        # 拉取最新代码
        git pull origin ${GIT_BRANCH}
        
        log "代码更新完成"
    else
        # 不是Git仓库，克隆代码
        log "克隆项目代码..."
        
        # 清空目录
        if [ "$(ls -A)" ]; then
            log "目录不为空，清空目录..."
            rm -rf ./*
            rm -rf ./.* 2>/dev/null || true
        fi
        
        # 克隆指定分支
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
    
    # 显示更新结果
    log "更新结果:"
    log "  当前分支: $(git branch --show-current)"
    log "  最新提交: $(git log -1 --pretty=format:'%h - %s (%cr)')"
    log "  提交者: $(git log -1 --pretty=format:'%an')"
    
    # 设置脚本权限
    if [ -d "scripts" ]; then
        chmod +x scripts/*.sh
        log "脚本权限设置完成"
    fi
}

# 运行调试检查
run_debug_check() {
    log "运行项目调试检查..."
    
    cd ${PROJECT_PATH}
    
    if [ -f "scripts/debug-build.sh" ]; then
        ./scripts/debug-build.sh
    else
        log "调试脚本不存在，跳过调试检查"
    fi
}

# 清理Docker缓存
clean_docker_cache() {
    log "清理Docker缓存..."
    
    # 停止现有服务
    docker-compose down 2>/dev/null || true
    
    # 清理缓存
    docker system prune -f
    docker builder prune -f
    
    log "Docker缓存清理完成"
}

# 构建和部署
build_and_deploy() {
    log "构建和部署项目..."
    
    cd ${PROJECT_PATH}
    
    # 检查必要文件
    if [ ! -f "docker-compose.yml" ]; then
        error_exit "未找到docker-compose.yml文件"
    fi
    
    if [ ! -f "Dockerfile" ]; then
        error_exit "未找到Dockerfile文件"
    fi
    
    # 构建并启动服务
    log "使用Docker Compose构建和启动服务..."
    
    if [ "$FORCE_DEPLOY" = true ]; then
        log "强制模式：不使用缓存构建"
        docker-compose build --no-cache beautyclub-app
    fi
    
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
    echo "=== 更新和部署完成 ==="
    echo "项目路径: ${PROJECT_PATH}"
    echo "更新分支: ${GIT_BRANCH}"
    echo "Git仓库: ${GIT_REPO}"
    echo "当前分支: $(git branch --show-current 2>/dev/null || echo '未知')"
    echo "最新提交: $(git log -1 --pretty=format:'%h - %s (%cr)' 2>/dev/null || echo '未知')"
    echo "更新时间: $(date)"
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
    echo "更新代码: ./scripts/update-and-deploy.sh -b ${GIT_BRANCH}"
}

# 主函数
main() {
    log "开始更新和部署BeautyClub项目..."
    
    # 解析命令行参数
    parse_args "$@"
    
    log "操作配置:"
    log "  分支: ${GIT_BRANCH}"
    log "  强制模式: ${FORCE_DEPLOY}"
    log "  项目路径: ${PROJECT_PATH}"
    echo
    
    # 执行步骤
    check_environment
    update_code
    run_debug_check
    clean_docker_cache
    build_and_deploy
    wait_for_services
    show_result
    
    log "更新和部署完成！"
}

# 执行主函数
main "$@" 