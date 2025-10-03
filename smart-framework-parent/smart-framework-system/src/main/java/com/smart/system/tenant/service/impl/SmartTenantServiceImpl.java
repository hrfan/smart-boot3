package com.smart.system.tenant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.system.tenant.entity.SmartTenant;
import com.smart.system.tenant.mapper.SmartTenantMapper;
import com.smart.system.tenant.service.SmartTenantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 租户管理表Service实现类
 * 实现租户管理业务逻辑层功能
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Service
public class SmartTenantServiceImpl extends ServiceImpl<SmartTenantMapper, SmartTenant> implements SmartTenantService {

    private static final Logger log = LoggerFactory.getLogger(SmartTenantServiceImpl.class);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SmartTenant insert(SmartTenant smartTenant) {
        // 从spring security获取当前登录用户ID
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        smartTenant.setCreateBy(currentUserId);
        smartTenant.setUpdateBy(currentUserId);
        boolean b = saveOrUpdate(smartTenant);
        if (b) {
            return smartTenant;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SmartTenant update(SmartTenant smartTenant) {
        // 从spring security获取当前登录用户ID
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        smartTenant.setUpdateBy(currentUserId);
        boolean b = updateById(smartTenant);
        if (b) {
            return smartTenant;
        }
        return null;
    }
}
