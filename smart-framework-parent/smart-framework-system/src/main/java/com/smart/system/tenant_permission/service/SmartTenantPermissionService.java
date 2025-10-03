package com.smart.system.tenant_permission.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smart.system.tenant_permission.entity.SmartTenantPermission;

/**
 * 租户权限中间表Service接口
 * 定义租户权限业务逻辑层接口
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public interface SmartTenantPermissionService extends IService<SmartTenantPermission> {

    /**
     * 新增租户权限关联
     * @param smartTenantPermission 租户权限实体对象
     * @return 新增后的租户权限对象
     */
    SmartTenantPermission insert(SmartTenantPermission smartTenantPermission);

    /**
     * 更新租户权限关联
     * @param smartTenantPermission 租户权限实体对象
     * @return 更新后的租户权限对象
     */
    SmartTenantPermission update(SmartTenantPermission smartTenantPermission);
}
