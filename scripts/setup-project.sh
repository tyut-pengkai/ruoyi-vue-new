#!/bin/bash

# 项目初始化脚本
# 用于在宝塔服务器上初始化BeautyClub项目

set -e

# 配置变量
PROJECT_NAME="beautyclub"
PROJECT_PATH="/www/wwwroot/${PROJECT_NAME}"
GIT_REPO="https://gitee.com/why110/BeautyClub.git"

# 日志函数
log() {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] $1"
}

# 错误处理
error_exit() {
    log "错误: $1"
    exit 1
}

# 检查Git是否安装
check_git() {
    if ! command -v git &> /dev/null; then
        error_exit "Git未安装，请先安装Git"
    fi
    log "Git检查通过"
}

# 初始化项目目录
init_project() {
    log "初始化项目目录..."
    
    # 创建项目目录
    mkdir -p ${PROJECT_PATH}
    cd ${PROJECT_PATH}
    
    # 检查目录是否为空
    if [ "$(ls -A)" ]; then
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
    
    log "目录已准备就绪"
}

# 克隆项目
clone_project() {
    log "克隆项目代码..."
    
    cd ${PROJECT_PATH}
    
    # 克隆项目
    git clone ${GIT_REPO} .
    
    if [ $? -eq 0 ]; then
        log "项目克隆成功"
    else
        error_exit "项目克隆失败"
    fi
}

# 设置权限
set_permissions() {
    log "设置脚本权限..."
    
    cd ${PROJECT_PATH}
    
    # 设置脚本可执行权限
    chmod +x scripts/*.sh
    
    log "权限设置完成"
}

# 显示下一步操作
show_next_steps() {
    log "项目初始化完成！"
    echo
    echo "下一步操作："
    echo "1. 修改配置文件中的数据库连接信息"
    echo "2. 运行部署脚本: ./scripts/bt-deploy.sh"
    echo "3. 或者使用Docker Compose: docker-compose up -d --build"
    echo
    echo "项目目录: ${PROJECT_PATH}"
    echo "Git仓库: ${GIT_REPO}"
}

# 主函数
main() {
    log "开始初始化BeautyClub项目..."
    
    check_git
    init_project
    clone_project
    set_permissions
    show_next_steps
    
    log "项目初始化完成！"
}

# 执行主函数
main "$@" 