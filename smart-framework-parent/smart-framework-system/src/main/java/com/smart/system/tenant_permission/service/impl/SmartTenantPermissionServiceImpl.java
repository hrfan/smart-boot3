package com.smart.system.tenant_permission.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.system.tenant_permission.entity.SmartTenantPermission;
import com.smart.system.tenant_permission.mapper.SmartTenantPermissionMapper;
import com.smart.system.tenant_permission.service.SmartTenantPermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 租户权限中间表Service实现类
 * 实现租户权限业务逻辑层功能
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Service
public class SmartTenantPermissionServiceImpl extends ServiceImpl<SmartTenantPermissionMapper, SmartTenantPermission> implements SmartTenantPermissionService {

    private static final Logger log = LoggerFactory.getLogger(SmartTenantPermissionServiceImpl.class);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SmartTenantPermission insert(SmartTenantPermission smartTenantPermission) {
        boolean b = saveOrUpdate(smartTenantPermission);
        if (b) {
            return smartTenantPermission;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SmartTenantPermission update(SmartTenantPermission smartTenantPermission) {
        boolean b = updateById(smartTenantPermission);
        if (b) {
            return smartTenantPermission;
        }
        return null;
    }
}
