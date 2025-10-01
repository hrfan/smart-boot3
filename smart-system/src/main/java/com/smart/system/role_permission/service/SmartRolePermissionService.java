package com.smart.system.role_permission.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smart.system.role_permission.entity.SmartRolePermission;

/**
 * 角色权限中间表Service接口
 * 定义角色权限业务逻辑层接口
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public interface SmartRolePermissionService extends IService<SmartRolePermission> {

    /**
     * 新增角色权限关联
     * @param smartRolePermission 角色权限实体对象
     * @return 新增后的角色权限对象
     */
    SmartRolePermission insert(SmartRolePermission smartRolePermission);

    /**
     * 更新角色权限关联
     * @param smartRolePermission 角色权限实体对象
     * @return 更新后的角色权限对象
     */
    SmartRolePermission update(SmartRolePermission smartRolePermission);
}
