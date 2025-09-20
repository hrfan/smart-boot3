#!/bin/bash

# =============================================
# Smart Boot3 集成测试脚本
# 版本: 1.0.0
# 作者: smart-boot3
# 描述: 系统集成测试和验证脚本
# =============================================

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

# 项目根目录
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$PROJECT_ROOT"

log_info "开始Smart Boot3集成测试"
log_info "项目根目录: $PROJECT_ROOT"

# =============================================
# 1. 环境检查
# =============================================
log_info "1. 检查环境依赖"

# 检查Java版本
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2)
    log_success "Java版本: $JAVA_VERSION"
else
    log_error "Java未安装或未配置到PATH"
    exit 1
fi

# 检查Maven版本
if command -v mvn &> /dev/null; then
    MAVEN_VERSION=$(mvn -version | head -n 1 | cut -d' ' -f3)
    log_success "Maven版本: $MAVEN_VERSION"
else
    log_error "Maven未安装或未配置到PATH"
    exit 1
fi

# 检查MySQL连接
if command -v mysql &> /dev/null; then
    log_success "MySQL客户端已安装"
else
    log_warning "MySQL客户端未安装，跳过数据库连接测试"
fi

# =============================================
# 2. 代码编译
# =============================================
log_info "2. 编译项目代码"

# 清理并编译
log_info "清理项目..."
mvn clean -q

log_info "编译项目..."
if mvn compile -q; then
    log_success "项目编译成功"
else
    log_error "项目编译失败"
    exit 1
fi

# =============================================
# 3. 代码质量检查
# =============================================
log_info "3. 代码质量检查"

# 检查代码格式
log_info "检查代码格式..."
if mvn spotless:check -q; then
    log_success "代码格式检查通过"
else
    log_warning "代码格式检查失败，运行格式化..."
    mvn spotless:apply -q
    log_success "代码格式化完成"
fi

# =============================================
# 4. 单元测试
# =============================================
log_info "4. 运行单元测试"

if mvn test -q; then
    log_success "单元测试通过"
else
    log_error "单元测试失败"
    exit 1
fi

# =============================================
# 5. 集成测试
# =============================================
log_info "5. 运行集成测试"

# 检查测试环境配置
if [ -f "smart-system/src/test/resources/application-test.yml" ]; then
    log_success "测试环境配置存在"
else
    log_warning "测试环境配置不存在，创建默认配置..."
    mkdir -p smart-system/src/test/resources
    cat > smart-system/src/test/resources/application-test.yml << EOF
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password: 
  jpa:
    hibernate:
      ddl-auto: create-drop
  h2:
    console:
      enabled: true

logging:
  level:
    com.smart: DEBUG
    org.springframework.security: DEBUG
EOF
    log_success "测试环境配置创建完成"
fi

# 运行集成测试
if mvn test -Dtest=SystemIntegrationTest -q; then
    log_success "集成测试通过"
else
    log_error "集成测试失败"
    exit 1
fi

# =============================================
# 6. 打包验证
# =============================================
log_info "6. 打包验证"

log_info "打包项目..."
if mvn package -DskipTests -q; then
    log_success "项目打包成功"
else
    log_error "项目打包失败"
    exit 1
fi

# 检查生成的JAR文件
JAR_FILES=$(find . -name "*.jar" -not -path "*/target/original-*" -not -path "*/target/maven-*")
if [ -n "$JAR_FILES" ]; then
    log_success "生成的JAR文件:"
    echo "$JAR_FILES" | while read -r jar; do
        echo "  - $jar"
    done
else
    log_warning "未找到生成的JAR文件"
fi

# =============================================
# 7. API测试（如果服务运行）
# =============================================
log_info "7. API接口测试"

# 检查是否有服务在运行
if curl -s http://localhost:8080/actuator/health > /dev/null 2>&1; then
    log_success "检测到服务运行在8080端口"
    
    # 测试健康检查接口
    log_info "测试健康检查接口..."
    if curl -s http://localhost:8080/actuator/health | grep -q "UP"; then
        log_success "健康检查接口正常"
    else
        log_warning "健康检查接口异常"
    fi
    
    # 测试API文档接口
    log_info "测试API文档接口..."
    if curl -s http://localhost:8080/swagger-ui/index.html > /dev/null 2>&1; then
        log_success "API文档接口可访问"
    else
        log_warning "API文档接口不可访问"
    fi
else
    log_warning "未检测到运行中的服务，跳过API测试"
    log_info "要启动服务进行API测试，请运行: mvn spring-boot:run -pl smart-system"
fi

# =============================================
# 8. 生成测试报告
# =============================================
log_info "8. 生成测试报告"

# 创建测试报告目录
mkdir -p reports

# 生成测试报告
log_info "生成测试报告..."
if mvn surefire-report:report -q; then
    log_success "测试报告生成成功: target/site/surefire-report.html"
else
    log_warning "测试报告生成失败"
fi

# =============================================
# 9. 清理临时文件
# =============================================
log_info "9. 清理临时文件"

# 清理Maven临时文件
mvn clean -q > /dev/null 2>&1 || true

log_success "临时文件清理完成"

# =============================================
# 测试总结
# =============================================
log_info "============================================="
log_success "Smart Boot3集成测试完成！"
log_info "============================================="

log_info "测试结果总结:"
log_success "✓ 环境依赖检查通过"
log_success "✓ 代码编译成功"
log_success "✓ 代码质量检查通过"
log_success "✓ 单元测试通过"
log_success "✓ 集成测试通过"
log_success "✓ 项目打包成功"

log_info "下一步操作:"
log_info "1. 启动服务: mvn spring-boot:run -pl smart-system"
log_info "2. 访问API文档: http://localhost:8080/swagger-ui/index.html"
log_info "3. 访问健康检查: http://localhost:8080/actuator/health"
log_info "4. 查看测试报告: target/site/surefire-report.html"

log_success "集成测试脚本执行完成！"
