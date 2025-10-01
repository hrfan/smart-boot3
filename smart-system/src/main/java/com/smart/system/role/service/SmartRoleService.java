package com.smart.system.role.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smart.system.role.entiy.SmartRole;

/**
 * 角色信息表Service接口
 * 定义角色业务逻辑层接口
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public interface SmartRoleService extends IService<SmartRole> {

    /**
     * 新增角色
     * @param smartRole 角色实体对象
     * @return 新增后的角色对象
     */
    SmartRole insert(SmartRole smartRole);

    /**
     * 更新角色
     * @param smartRole 角色实体对象
     * @return 更新后的角色对象
     */
    SmartRole update(SmartRole smartRole);
}
