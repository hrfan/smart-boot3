#!/bin/bash

# Nacos连接测试脚本
# 用于测试Nacos服务器是否可访问

echo "=== Nacos连接测试 ==="

# Nacos服务器地址
NACOS_SERVER="117.72.47.147"
NACOS_PORT="8848"
NACOS_USERNAME="nacos"
NACOS_PASSWORD="nacos"

echo "测试Nacos服务器连接: ${NACOS_SERVER}:${NACOS_PORT}"

# 测试HTTP连接
echo "1. 测试HTTP连接..."
HTTP_RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" "http://${NACOS_SERVER}:${NACOS_PORT}/nacos/v1/console/health")
if [ "$HTTP_RESPONSE" = "200" ]; then
    echo "✓ HTTP连接成功 (状态码: $HTTP_RESPONSE)"
else
    echo "✗ HTTP连接失败 (状态码: $HTTP_RESPONSE)"
fi

# 测试登录接口
echo "2. 测试登录接口..."
LOGIN_RESPONSE=$(curl -s -X POST "http://${NACOS_SERVER}:${NACOS_PORT}/nacos/v1/auth/login" \
    -H "Content-Type: application/x-www-form-urlencoded" \
    -d "username=${NACOS_USERNAME}&password=${NACOS_PASSWORD}")

if echo "$LOGIN_RESPONSE" | grep -q "accessToken"; then
    echo "✓ 登录成功"
    echo "响应: $LOGIN_RESPONSE"
else
    echo "✗ 登录失败"
    echo "响应: $LOGIN_RESPONSE"
fi

# 测试配置接口
echo "3. 测试配置接口..."
CONFIG_RESPONSE=$(curl -s "http://${NACOS_SERVER}:${NACOS_PORT}/nacos/v1/cs/configs?dataId=smart-dev.yml&group=DEFAULT_GROUP")
if [ -n "$CONFIG_RESPONSE" ]; then
    echo "✓ 配置接口可访问"
    echo "配置内容: $CONFIG_RESPONSE"
else
    echo "✗ 配置接口无法访问"
fi

# 测试服务发现接口
echo "4. 测试服务发现接口..."
DISCOVERY_RESPONSE=$(curl -s "http://${NACOS_SERVER}:${NACOS_PORT}/nacos/v1/ns/instance/list?serviceName=smart-system")
if [ -n "$DISCOVERY_RESPONSE" ]; then
    echo "✓ 服务发现接口可访问"
    echo "服务列表: $DISCOVERY_RESPONSE"
else
    echo "✗ 服务发现接口无法访问"
fi

echo "=== 测试完成 ==="
