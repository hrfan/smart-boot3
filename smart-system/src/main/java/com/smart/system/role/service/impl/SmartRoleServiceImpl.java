package com.smart.system.role.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.system.role.entiy.SmartRole;
import com.smart.system.role.mapper.SmartRoleMapper;
import com.smart.system.role.service.SmartRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色信息表Service实现类
 * 实现角色业务逻辑层功能
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Service
public class SmartRoleServiceImpl extends ServiceImpl<SmartRoleMapper, SmartRole> implements SmartRoleService {

    private static final Logger log = LoggerFactory.getLogger(SmartRoleServiceImpl.class);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SmartRole insert(SmartRole smartRole) {
        // 从spring security获取当前登录用户ID
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        smartRole.setCreateBy(currentUserId);
        smartRole.setUpdateBy(currentUserId);
        boolean b = saveOrUpdate(smartRole);
        if (b) {
            return smartRole;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SmartRole update(SmartRole smartRole) {
        // 从spring security获取当前登录用户ID
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        smartRole.setUpdateBy(currentUserId);
        boolean b = updateById(smartRole);
        if (b) {
            return smartRole;
        }
        return null;
    }
}
