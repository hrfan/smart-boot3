package com.smart.system.role.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smart.system.role.entiy.SmartRole;
import jakarta.validation.Valid;

import java.util.List;

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

    /**
     * 删除角色
     * @param id 角色ID，用于指定要删除的角色
     * @return 删除操作影响的行数
     */
    Boolean deleteRoleById(String id);

     /**
     * 批量删除角色
     * @param ids 角色ID列表，用于指定要删除的多个角色
     * @return 删除操作影响的行数
     */
    boolean deleteRoleByIds(List<String> ids);

    /**
     * 启用/禁用角色
     * @param params 角色实体对象，包含角色ID和状态
     * @return 更新操作是否成功
     */
    boolean changeStatus(SmartRole params);
}
