#!/bin/bash

# Smart Boot3 微服务脚手架部署脚本
# 作者: Smart Boot3
# 版本: 1.0.0

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 日志函数
log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

log_debug() {
    echo -e "${BLUE}[DEBUG]${NC} $1"
}

# 检查Docker是否安装
check_docker() {
    if ! command -v docker &> /dev/null; then
        log_error "Docker未安装，请先安装Docker"
        exit 1
    fi
    
    if ! command -v docker-compose &> /dev/null; then
        log_error "Docker Compose未安装，请先安装Docker Compose"
        exit 1
    fi
    
    log_info "Docker环境检查通过"
}

# 检查Maven是否安装
check_maven() {
    if ! command -v mvn &> /dev/null; then
        log_error "Maven未安装，请先安装Maven"
        exit 1
    fi
    
    log_info "Maven环境检查通过"
}

# 编译项目
build_project() {
    log_info "开始编译项目..."
    
    # 清理并编译
    mvn clean compile -DskipTests
    
    if [ $? -eq 0 ]; then
        log_info "项目编译成功"
    else
        log_error "项目编译失败"
        exit 1
    fi
}

# 构建Docker镜像
build_images() {
    log_info "开始构建Docker镜像..."
    
    # 构建网关镜像
    log_info "构建网关镜像..."
    docker build -f docker/Dockerfile.gateway -t smart-gateway:latest .
    
    # 构建认证服务镜像
    log_info "构建认证服务镜像..."
    docker build -f docker/Dockerfile.auth -t smart-auth:latest .
    
    # 构建系统管理服务镜像
    log_info "构建系统管理服务镜像..."
    docker build -f docker/Dockerfile.system -t smart-system:latest .
    
    log_info "Docker镜像构建完成"
}

# 启动服务
start_services() {
    log_info "启动所有服务..."
    
    # 启动基础设施服务
    log_info "启动基础设施服务..."
    docker-compose -f docker/docker-compose.yml up -d nacos mysql redis rabbitmq
    
    # 等待基础设施服务启动
    log_info "等待基础设施服务启动..."
    sleep 30
    
    # 启动业务服务
    log_info "启动业务服务..."
    docker-compose -f docker/docker-compose.yml up -d smart-gateway smart-auth smart-system
    
    log_info "所有服务启动完成"
}

# 停止服务
stop_services() {
    log_info "停止所有服务..."
    docker-compose -f docker/docker-compose.yml down
    log_info "所有服务已停止"
}

# 重启服务
restart_services() {
    log_info "重启所有服务..."
    stop_services
    start_services
}

# 查看服务状态
status_services() {
    log_info "查看服务状态..."
    docker-compose -f docker/docker-compose.yml ps
}

# 查看服务日志
logs_services() {
    local service=$1
    if [ -z "$service" ]; then
        log_info "查看所有服务日志..."
        docker-compose -f docker/docker-compose.yml logs -f
    else
        log_info "查看 $service 服务日志..."
        docker-compose -f docker/docker-compose.yml logs -f $service
    fi
}

# 清理资源
cleanup() {
    log_info "清理Docker资源..."
    docker-compose -f docker/docker-compose.yml down -v
    docker system prune -f
    log_info "清理完成"
}

# 显示帮助信息
show_help() {
    echo "Smart Boot3 微服务脚手架部署脚本"
    echo ""
    echo "用法: $0 [选项]"
    echo ""
    echo "选项:"
    echo "  build     编译项目"
    echo "  build-img 构建Docker镜像"
    echo "  start     启动所有服务"
    echo "  stop      停止所有服务"
    echo "  restart   重启所有服务"
    echo "  status    查看服务状态"
    echo "  logs      查看服务日志"
    echo "  logs [服务名] 查看指定服务日志"
    echo "  cleanup   清理Docker资源"
    echo "  deploy    完整部署（编译+构建+启动）"
    echo "  help      显示帮助信息"
    echo ""
    echo "示例:"
    echo "  $0 deploy          # 完整部署"
    echo "  $0 start            # 启动服务"
    echo "  $0 logs smart-gateway  # 查看网关日志"
}

# 完整部署
deploy() {
    log_info "开始完整部署..."
    
    check_docker
    check_maven
    build_project
    build_images
    start_services
    
    log_info "部署完成！"
    log_info "访问地址:"
    log_info "  - 网关: http://localhost:8080"
    log_info "  - Nacos: http://localhost:8848/nacos"
    log_info "  - RabbitMQ管理界面: http://localhost:15672"
    log_info "  - 用户名/密码: admin/admin"
}

# 主函数
main() {
    case "${1:-help}" in
        "build")
            check_maven
            build_project
            ;;
        "build-img")
            check_docker
            build_images
            ;;
        "start")
            check_docker
            start_services
            ;;
        "stop")
            check_docker
            stop_services
            ;;
        "restart")
            check_docker
            restart_services
            ;;
        "status")
            check_docker
            status_services
            ;;
        "logs")
            check_docker
            logs_services $2
            ;;
        "cleanup")
            check_docker
            cleanup
            ;;
        "deploy")
            deploy
            ;;
        "help"|*)
            show_help
            ;;
    esac
}

# 执行主函数
main "$@"
