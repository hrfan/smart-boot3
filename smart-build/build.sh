#!/bin/bash

# Smart Framework 构建脚本
# 用于统一构建、测试和打包

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 日志函数
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 显示帮助信息
show_help() {
    echo "Smart Framework 构建脚本"
    echo ""
    echo "用法: $0 [选项]"
    echo ""
    echo "选项:"
    echo "  clean       清理项目"
    echo "  compile     编译项目"
    echo "  test        运行测试"
    echo "  package     打包项目"
    echo "  install     安装到本地仓库"
    echo "  deploy      部署到远程仓库"
    echo "  docker      构建Docker镜像"
    echo "  check       代码质量检查"
    echo "  site        生成项目文档"
    echo "  all         执行完整构建流程"
    echo "  help        显示此帮助信息"
    echo ""
    echo "环境变量:"
    echo "  PROFILE     构建环境 (dev/test/prod)"
    echo "  SKIP_TESTS  跳过测试 (true/false)"
    echo ""
    echo "示例:"
    echo "  $0 clean"
    echo "  $0 compile"
    echo "  $0 package"
    echo "  PROFILE=prod $0 all"
}

# 检查Java版本
check_java_version() {
    log_info "检查Java版本..."
    if ! java -version 2>&1 | grep -q "21"; then
        log_error "需要Java 21，当前版本:"
        java -version
        exit 1
    fi
    log_success "Java版本检查通过"
}

# 设置环境变量
setup_environment() {
    # 设置Java环境
    export JAVA_HOME=${JAVA_HOME:-/Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home}
    
    # 设置Maven选项
    export MAVEN_OPTS="-Xmx2048m"
    
    # 设置构建环境
    export PROFILE=${PROFILE:-dev}
    
    log_info "构建环境: $PROFILE"
    log_info "Java Home: $JAVA_HOME"
}

# 清理项目
clean_project() {
    log_info "清理项目..."
    cd smart-framework-top
    mvn clean -q
    cd ..
    log_success "项目清理完成"
}

# 编译项目
compile_project() {
    log_info "编译项目..."
    cd smart-framework-top
    mvn compile -q
    cd ..
    log_success "项目编译完成"
}

# 运行测试
run_tests() {
    if [ "$SKIP_TESTS" = "true" ]; then
        log_warning "跳过测试"
        return
    fi
    
    log_info "运行测试..."
    cd smart-framework-top
    mvn test -q
    cd ..
    log_success "测试完成"
}

# 打包项目
package_project() {
    log_info "打包项目..."
    cd smart-framework-top
    mvn package -DskipTests -q
    cd ..
    log_success "项目打包完成"
}

# 安装到本地仓库
install_project() {
    log_info "安装到本地仓库..."
    cd smart-framework-top
    mvn install -DskipTests -q
    cd ..
    log_success "安装完成"
}

# 部署到远程仓库
deploy_project() {
    log_info "部署到远程仓库..."
    cd smart-framework-top
    mvn deploy -DskipTests -q
    cd ..
    log_success "部署完成"
}

# 构建Docker镜像
build_docker() {
    log_info "构建Docker镜像..."
    cd smart-framework-top
    mvn clean package -Pdocker -DskipTests -q
    cd ..
    log_success "Docker镜像构建完成"
}

# 代码质量检查
check_code_quality() {
    log_info "执行代码质量检查..."
    cd smart-framework-top
    # 在开发阶段，代码质量检查不强制失败
    mvn checkstyle:check -q || log_warning "代码质量检查发现问题，请查看详细报告"
    mvn spotbugs:check -q || log_warning "静态代码分析发现问题，请查看详细报告"
    cd ..
    log_success "代码质量检查完成"
}

# 生成项目文档
generate_site() {
    log_info "生成项目文档..."
    cd smart-framework-top
    mvn site -q
    cd ..
    log_success "项目文档生成完成"
}

# 完整构建流程
build_all() {
    log_info "开始完整构建流程..."
    
    check_java_version
    setup_environment
    clean_project
    compile_project
    run_tests
    package_project
    check_code_quality
    
    log_success "完整构建流程完成！"
}

# 主函数
main() {
    case "${1:-help}" in
        clean)
            check_java_version
            setup_environment
            clean_project
            ;;
        compile)
            check_java_version
            setup_environment
            compile_project
            ;;
        test)
            check_java_version
            setup_environment
            run_tests
            ;;
        package)
            check_java_version
            setup_environment
            package_project
            ;;
        install)
            check_java_version
            setup_environment
            install_project
            ;;
        deploy)
            check_java_version
            setup_environment
            deploy_project
            ;;
        docker)
            check_java_version
            setup_environment
            build_docker
            ;;
        check)
            check_java_version
            setup_environment
            check_code_quality
            ;;
        site)
            check_java_version
            setup_environment
            generate_site
            ;;
        all)
            build_all
            ;;
        help|--help|-h)
            show_help
            ;;
        *)
            log_error "未知选项: $1"
            show_help
            exit 1
            ;;
    esac
}

# 执行主函数
main "$@"
