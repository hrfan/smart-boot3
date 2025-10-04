package com.smart.system.permission.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smart.system.permission.entity.SmartPermission;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 菜单权限表Service接口
 * 定义菜单权限业务逻辑层接口
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public interface SmartPermissionService extends IService<SmartPermission> {

    SmartPermission insert(SmartPermission smartPermission);


    SmartPermission update(SmartPermission smartPermission);

    /**
     * 根据用户ID查询权限标识列表
     *
     * @param id 用户ID
     * @return 权限标识列表
     */
    List<String> findByUserId(String id);


    /**
     * 根据用户ID查询角色标识列表
     *
     * @param id 用户ID
     * @return 角色标识列表
     */
    List<String> findRoleByUserId(String id);
}

