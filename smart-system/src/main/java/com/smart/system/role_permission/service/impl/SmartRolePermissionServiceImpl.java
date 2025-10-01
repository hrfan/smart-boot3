package com.smart.system.role_permission.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.system.role_permission.entity.SmartRolePermission;
import com.smart.system.role_permission.mapper.SmartRolePermissionMapper;
import com.smart.system.role_permission.service.SmartRolePermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色权限中间表Service实现类
 * 实现角色权限业务逻辑层功能
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Service
public class SmartRolePermissionServiceImpl extends ServiceImpl<SmartRolePermissionMapper, SmartRolePermission> implements SmartRolePermissionService {

    private static final Logger log = LoggerFactory.getLogger(SmartRolePermissionServiceImpl.class);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SmartRolePermission insert(SmartRolePermission smartRolePermission) {

        boolean b = saveOrUpdate(smartRolePermission);
        if (b) {
            return smartRolePermission;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SmartRolePermission update(SmartRolePermission smartRolePermission) {
        boolean b = updateById(smartRolePermission);
        if (b) {
            return smartRolePermission;
        }
        return null;
    }
}
