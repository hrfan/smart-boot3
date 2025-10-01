package com.smart.system.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.system.user.entity.SmartUser;
import com.smart.system.user.mapper.SmartUserMapper;
import com.smart.system.user.service.SmartUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户信息表Service实现类
 * 实现用户业务逻辑层功能
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Service
public class SmartUserServiceImpl extends ServiceImpl<SmartUserMapper, SmartUser> implements SmartUserService {

    private static final Logger log = LoggerFactory.getLogger(SmartUserServiceImpl.class);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SmartUser insert(SmartUser smartUser) {
        // 从spring security获取当前登录用户ID
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        smartUser.setCreateBy(currentUserId);
        smartUser.setUpdateBy(currentUserId);
        boolean b = saveOrUpdate(smartUser);
        if (b) {
            return smartUser;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SmartUser update(SmartUser smartUser) {
        // 从spring security获取当前登录用户ID
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        smartUser.setUpdateBy(currentUserId);
        boolean b = updateById(smartUser);
        if (b) {
            return smartUser;
        }
        return null;
    }
}
