#!/bin/bash

# Smart Boot3 微服务脚手架测试脚本
# 作者: Smart Boot3
# 版本: 1.0.0

echo "=========================================="
echo "Smart Boot3 微服务脚手架测试脚本"
echo "=========================================="

# 检查Java版本
echo "1. 检查Java版本..."
java -version
echo ""

# 检查Maven版本
echo "2. 检查Maven版本..."
mvn -version
echo ""

# 编译整个项目
echo "3. 编译整个项目..."
mvn clean compile
if [ $? -eq 0 ]; then
    echo "✅ 项目编译成功"
else
    echo "❌ 项目编译失败"
    exit 1
fi
echo ""

# 运行测试
echo "4. 运行测试..."
mvn test
if [ $? -eq 0 ]; then
    echo "✅ 所有测试通过"
else
    echo "❌ 测试失败"
    exit 1
fi
echo ""

# 打包项目
echo "5. 打包项目..."
mvn clean package -DskipTests
if [ $? -eq 0 ]; then
    echo "✅ 项目打包成功"
else
    echo "❌ 项目打包失败"
    exit 1
fi
echo ""

echo "=========================================="
echo "🎉 所有测试完成！项目状态："
echo "✅ 编译成功"
echo "✅ 测试通过"
echo "✅ 打包成功"
echo ""
echo "可用的模块："
echo "- smart-common (公共模块)"
echo "- smart-gateway (API网关)"
echo "- smart-system (系统管理)"
echo ""
echo "下一步："
echo "1. 启动Nacos服务器"
echo "2. 运行 smart-gateway: mvn spring-boot:run -pl smart-gateway"
echo "3. 运行 smart-system: mvn spring-boot:run -pl smart-system"
echo "4. 测试API接口"
echo "=========================================="
