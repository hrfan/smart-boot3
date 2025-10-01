package com.smart.system.user_role.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.system.user_role.entity.SmartUserRole;
import com.smart.system.user_role.mapper.SmartUserRoleMapper;
import com.smart.system.user_role.service.SmartUserRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户和角色关联表Service实现类
 * 实现用户角色业务逻辑层功能
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Service
public class SmartUserRoleServiceImpl extends ServiceImpl<SmartUserRoleMapper, SmartUserRole> implements SmartUserRoleService {

    private static final Logger log = LoggerFactory.getLogger(SmartUserRoleServiceImpl.class);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SmartUserRole insert(SmartUserRole smartUserRole) {
        boolean b = saveOrUpdate(smartUserRole);
        if (b) {
            return smartUserRole;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SmartUserRole update(SmartUserRole smartUserRole) {
        boolean b = updateById(smartUserRole);
        if (b) {
            return smartUserRole;
        }
        return null;
    }
}
