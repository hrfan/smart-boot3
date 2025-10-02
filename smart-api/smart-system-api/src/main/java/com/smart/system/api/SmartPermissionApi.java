package com.smart.system.api;

import com.smart.common.core.result.Result;
import java.util.List;
import java.util.Map;

/**
 * 系统用户API接口
 * 提供用户相关的查询和管理功能
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public interface SmartPermissionApi {

    /**
     * 获取权限信息列表
     * @return 权限列表信息
     */
    Result<Map<String, Object>> getPermissionList();


}
