package com.smart.system.tenant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smart.system.tenant.entity.SmartTenant;

/**
 * 租户管理表Service接口
 * 定义租户管理业务逻辑层接口
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public interface SmartTenantService extends IService<SmartTenant> {

    /**
     * 新增租户
     * @param smartTenant 租户实体对象
     * @return 新增后的租户对象
     */
    SmartTenant insert(SmartTenant smartTenant);

    /**
     * 更新租户
     * @param smartTenant 租户实体对象
     * @return 更新后的租户对象
     */
    SmartTenant update(SmartTenant smartTenant);
}
