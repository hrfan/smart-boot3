package com.smart.system.user_tenant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.system.user_tenant.entity.SmartUserTenant;
import com.smart.system.user_tenant.mapper.SmartUserTenantMapper;
import com.smart.system.user_tenant.service.SmartUserTenantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户租户中间表Service实现类
 * 实现用户租户业务逻辑层功能
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Service
public class SmartUserTenantServiceImpl extends ServiceImpl<SmartUserTenantMapper, SmartUserTenant> implements SmartUserTenantService {

    private static final Logger log = LoggerFactory.getLogger(SmartUserTenantServiceImpl.class);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SmartUserTenant insert(SmartUserTenant smartUserTenant) {
        // 从spring security获取当前登录用户ID
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean b = saveOrUpdate(smartUserTenant);
        if (b) {
            return smartUserTenant;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SmartUserTenant update(SmartUserTenant smartUserTenant) {
        // 从spring security获取当前登录用户ID
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean b = updateById(smartUserTenant);
        if (b) {
            return smartUserTenant;
        }
        return null;
    }
}
