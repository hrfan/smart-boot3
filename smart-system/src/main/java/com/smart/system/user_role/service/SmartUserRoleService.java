package com.smart.system.user_role.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smart.system.user_role.entity.SmartUserRole;

/**
 * 用户和角色关联表Service接口
 * 定义用户角色业务逻辑层接口
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public interface SmartUserRoleService extends IService<SmartUserRole> {

    /**
     * 新增用户角色关联
     * @param smartUserRole 用户角色实体对象
     * @return 新增后的用户角色对象
     */
    SmartUserRole insert(SmartUserRole smartUserRole);

    /**
     * 更新用户角色关联
     * @param smartUserRole 用户角色实体对象
     * @return 更新后的用户角色对象
     */
    SmartUserRole update(SmartUserRole smartUserRole);
}
