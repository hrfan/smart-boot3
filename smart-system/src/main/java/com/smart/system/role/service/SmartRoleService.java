package com.smart.system.role.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smart.system.role.entiy.SmartRole;

import java.util.List;

/**
 * 角色信息表Service接口
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public interface SmartRoleService extends IService<SmartRole> {

    /**
     * 根据角色名称查询角色信息
     * 
     * @param roleName 角色名称
     * @return 角色信息
     */
    SmartRole getByRoleName(String roleName);

    /**
     * 根据角色权限字符串查询角色信息
     * 
     * @param roleKey 角色权限字符串
     * @return 角色信息
     */
    SmartRole getByRoleKey(String roleKey);

    /**
     * 根据用户ID查询角色列表
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SmartRole> getRolesByUserId(String userId);

    /**
     * 查询所有启用的角色
     * 
     * @return 启用的角色列表
     */
    List<SmartRole> getEnabledRoles();

    /**
     * 根据租户ID查询角色列表
     * 
     * @param tenantId 租户ID
     * @return 角色列表
     */
    List<SmartRole> getRolesByTenantId(String tenantId);

    /**
     * 检查角色名称是否存在
     * 
     * @param roleName 角色名称
     * @param excludeId 排除的角色ID
     * @return 是否存在
     */
    boolean checkRoleNameExists(String roleName, String excludeId);

    /**
     * 检查角色权限字符串是否存在
     * 
     * @param roleKey 角色权限字符串
     * @param excludeId 排除的角色ID
     * @return 是否存在
     */
    boolean checkRoleKeyExists(String roleKey, String excludeId);

    /**
     * 新增角色
     * 
     * @param role 角色信息
     * @return 是否成功
     */
    boolean insertRole(SmartRole role);

    /**
     * 修改角色
     * 
     * @param role 角色信息
     * @return 是否成功
     */
    boolean updateRole(SmartRole role);

    /**
     * 批量删除角色
     * 
     * @param roleIds 角色ID列表
     * @return 是否成功
     */
    boolean deleteRolesByIds(List<String> roleIds);

    /**
     * 更新角色状态
     * 
     * @param roleId 角色ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateRoleStatus(String roleId, String status);

    /**
     * 分配角色给用户
     * 
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 是否成功
     */
    boolean assignRolesToUser(String userId, List<String> roleIds);

    /**
     * 取消用户角色分配
     * 
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 是否成功
     */
    boolean removeRolesFromUser(String userId, List<String> roleIds);
}
