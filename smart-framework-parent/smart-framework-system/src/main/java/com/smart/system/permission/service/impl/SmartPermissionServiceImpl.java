package com.smart.system.permission.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.system.permission.entity.SmartPermission;
import com.smart.system.permission.mapper.SmartPermissionMapper;
import com.smart.system.permission.service.SmartPermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表Service实现类
 * 实现菜单权限业务逻辑层功能
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Service
public class SmartPermissionServiceImpl extends ServiceImpl<SmartPermissionMapper, SmartPermission> implements SmartPermissionService {

    private static final Logger log = LoggerFactory.getLogger(SmartPermissionServiceImpl.class);

    @Autowired
    private SmartPermissionMapper smartPermissionMapper;



    @Override
    @Transactional(rollbackFor = Exception.class)
    public SmartPermission insert(SmartPermission smartPermission) {

        // 从spring security获取当前登录用户ID
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        smartPermission.setCreateBy(currentUserId);
        smartPermission.setUpdateBy(currentUserId);
        boolean b = saveOrUpdate(smartPermission);
        if (b) {
            return smartPermission;
        }
        return null;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public SmartPermission update(SmartPermission smartPermission) {
        // 从spring security获取当前登录用户ID
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        smartPermission.setUpdateBy(currentUserId);
        boolean b = updateById(smartPermission);
        if (b) {
            return smartPermission;
        }
        return null;
    }


    /**
     * 根据用户ID查询权限标识列表
     *
     * @param id 用户ID
     * @return 权限标识列表
     */
    @Override
    public List<String> findByUserId(String id) {

        List<String> permissionCodes = smartPermissionMapper.findByUserId(id);
        if(CollectionUtil.isEmpty(permissionCodes)){
            log.error("根据用户ID查询权限标识列表为空，用户ID：{}", id);
        }
        return permissionCodes;
    }

    /**
     * 根据用户ID查询角色标识列表
     *
     * @param id 用户ID
     * @return 角色标识列表
     */
    @Override
    public List<String> findRoleByUserId(String id) {
        return smartPermissionMapper.findRoleByUserId(id);
    }
}

