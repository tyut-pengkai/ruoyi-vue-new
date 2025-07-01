#!/bin/bash

# 项目初始化脚本
# 用于在宝塔服务器上初始化BeautyClub项目

set -e

# 配置变量
PROJECT_NAME="beautyclub"
PROJECT_PATH="/www/wwwroot/${PROJECT_NAME}"
GIT_REPO="https://gitee.com/why110/BeautyClub.git"
GIT_BRANCH="main"  # 默认分支，可以通过参数修改

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
    echo "  -b, --branch BRANCH    指定要克隆的分支 (默认: main)"
    echo "  -r, --repo REPO        指定Git仓库地址"
    echo "  -p, --path PATH        指定项目路径 (默认: /www/wwwroot/beautyclub)"
    echo "  -h, --help             显示此帮助信息"
    echo
    echo "示例:"
    echo "  $0 -b develop                    # 克隆develop分支"
    echo "  $0 -b master -r https://gitee.com/user/repo.git  # 指定仓库和分支"
    echo "  $0 -b v1.0.0                     # 克隆特定标签"
}

# 解析命令行参数
parse_args() {
    while [[ $# -gt 0 ]]; do
        case $1 in
            -b|--branch)
                GIT_BRANCH="$2"
                shift 2
                ;;
            -r|--repo)
                GIT_REPO="$2"
                shift 2
                ;;
            -p|--path)
                PROJECT_PATH="$2"
                shift 2
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
    log "克隆项目代码 (分支: ${GIT_BRANCH})..."
    
    cd ${PROJECT_PATH}
    
    # 克隆指定分支
    if git clone -b ${GIT_BRANCH} ${GIT_REPO} .; then
        log "项目克隆成功 (分支: ${GIT_BRANCH})"
    else
        log "尝试克隆默认分支..."
        if git clone ${GIT_REPO} .; then
            log "项目克隆成功 (默认分支)"
            # 检查分支是否存在
            if git show-ref --verify --quiet refs/remotes/origin/${GIT_BRANCH}; then
                log "切换到指定分支: ${GIT_BRANCH}"
                git checkout ${GIT_BRANCH}
            else
                log "警告: 分支 ${GIT_BRANCH} 不存在，使用当前分支"
            fi
        else
            error_exit "项目克隆失败"
        fi
    fi
}

# 设置权限
set_permissions() {
    log "设置脚本权限..."
    
    cd ${PROJECT_PATH}
    
    # 设置脚本可执行权限
    if [ -d "scripts" ]; then
        chmod +x scripts/*.sh
        log "脚本权限设置完成"
    else
        log "警告: scripts目录不存在"
    fi
}

# 显示项目信息
show_project_info() {
    log "显示项目信息..."
    
    cd ${PROJECT_PATH}
    
    echo
    echo "=== 项目信息 ==="
    echo "项目路径: ${PROJECT_PATH}"
    echo "Git仓库: ${GIT_REPO}"
    echo "当前分支: $(git branch --show-current)"
    echo "最新提交: $(git log -1 --pretty=format:'%h - %s (%cr)')"
    echo
    
    # 显示关键文件
    echo "=== 关键文件检查 ==="
    if [ -f "Dockerfile" ]; then
        echo "✅ Dockerfile"
    else
        echo "❌ Dockerfile"
    fi
    
    if [ -f "docker-compose.yml" ]; then
        echo "✅ docker-compose.yml"
    else
        echo "❌ docker-compose.yml"
    fi
    
    if [ -d "scripts" ]; then
        echo "✅ scripts目录"
        echo "   脚本文件:"
        ls -la scripts/*.sh 2>/dev/null || echo "   无.sh文件"
    else
        echo "❌ scripts目录"
    fi
    echo
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
    echo "当前分支: ${GIT_BRANCH}"
}

# 主函数
main() {
    log "开始初始化BeautyClub项目..."
    
    # 解析命令行参数
    parse_args "$@"
    
    check_git
    init_project
    clone_project
    set_permissions
    show_project_info
    show_next_steps
    
    log "项目初始化完成！"
}

# 执行主函数
main "$@" 