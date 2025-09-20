#!/bin/bash

echo "=== 统一异常处理功能测试 ==="
echo

# 等待服务启动
echo "等待服务启动..."
sleep 20

# 测试成功响应
echo "1. 测试成功响应:"
curl -s "http://localhost:9100/api/public/test/success" | jq .
echo

# 测试业务异常
echo "2. 测试业务异常:"
curl -s "http://localhost:9100/api/public/test/business-error" | jq .
echo

# 测试参数验证异常
echo "3. 测试参数验证异常:"
curl -s "http://localhost:9100/api/public/test/parameter-error?name=&age=" | jq .
echo

# 测试数据不存在异常
echo "4. 测试数据不存在异常:"
curl -s "http://localhost:9100/api/public/test/data-not-found" | jq .
echo

# 测试数据库异常
echo "5. 测试数据库异常:"
curl -s "http://localhost:9100/api/public/test/database-error" | jq .
echo

# 测试系统异常
echo "6. 测试系统异常:"
curl -s "http://localhost:9100/api/public/test/system-error" | jq .
echo

echo "=== 测试完成 ==="
