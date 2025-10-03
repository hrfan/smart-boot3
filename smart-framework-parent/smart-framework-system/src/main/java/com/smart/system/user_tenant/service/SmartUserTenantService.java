package com.smart.system.user_tenant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smart.system.user_tenant.entity.SmartUserTenant;

/**
 * 用户租户中间表Service接口
 * 定义用户租户业务逻辑层接口
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public interface SmartUserTenantService extends IService<SmartUserTenant> {

    /**
     * 新增用户租户关联
     * @param smartUserTenant 用户租户实体对象
     * @return 新增后的用户租户对象
     */
    SmartUserTenant insert(SmartUserTenant smartUserTenant);

    /**
     * 更新用户租户关联
     * @param smartUserTenant 用户租户实体对象
     * @return 更新后的用户租户对象
     */
    SmartUserTenant update(SmartUserTenant smartUserTenant);
}
