package com.smart.system.api.remote.impl;

import com.smart.common.core.result.Result;
import com.smart.system.api.SmartPermissionApi;
import com.smart.system.api.remote.constant.CommonConstant;
import com.smart.system.api.remote.util.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 系统用户API实现类
 * 提供用户相关的查询和管理功能的远程调用实现
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Component
public class SmartPermissionRemote implements SmartPermissionApi {

    private static final Logger log = LoggerFactory.getLogger(SmartPermissionRemote.class);

    @Autowired
    private RestClient restClient;

    /**
     * 获取权限信息列表
     * @return 权限列表信息
     */
    @Override
    public Result<Map<String, Object>> getPermissionList() {
        log.error("开始获取权限列表...");
        Result<Map<String, Object>> result = restClient.doGet(
                CommonConstant.SYSTEM_SERVICE_NAME,
                CommonConstant.PREFIX_SYSTEM + "/permission/list",
                new ParameterizedTypeReference<Result<Map<String, Object>>>() {}
        );
        return result;
    }
}
