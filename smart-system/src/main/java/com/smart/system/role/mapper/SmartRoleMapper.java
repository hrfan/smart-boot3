package com.smart.system.role.mapper;

import com.smart.common.database.mapper.BaseMapper;
import com.smart.system.role.entiy.SmartRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色信息表Mapper接口
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Mapper
public interface SmartRoleMapper extends BaseMapper<SmartRole> {

    /**
     * 根据角色名称查询角色信息
     * 
     * @param roleName 角色名称
     * @return 角色信息
     */
    SmartRole selectByRoleName(@Param("roleName") String roleName);

    /**
     * 根据角色权限字符串查询角色信息
     * 
     * @param roleKey 角色权限字符串
     * @return 角色信息
     */
    SmartRole selectByRoleKey(@Param("roleKey") String roleKey);

    /**
     * 根据用户ID查询角色列表
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SmartRole> selectRolesByUserId(@Param("userId") String userId);

    /**
     * 查询所有启用的角色
     * 
     * @return 启用的角色列表
     */
    List<SmartRole> selectEnabledRoles();

    /**
     * 根据租户ID查询角色列表
     * 
     * @param tenantId 租户ID
     * @return 角色列表
     */
    List<SmartRole> selectRolesByTenantId(@Param("tenantId") String tenantId);

    /**
     * 检查角色名称是否存在
     * 
     * @param roleName 角色名称
     * @param excludeId 排除的角色ID
     * @return 存在数量
     */
    int checkRoleNameExists(@Param("roleName") String roleName, @Param("excludeId") String excludeId);

    /**
     * 检查角色权限字符串是否存在
     * 
     * @param roleKey 角色权限字符串
     * @param excludeId 排除的角色ID
     * @return 存在数量
     */
    int checkRoleKeyExists(@Param("roleKey") String roleKey, @Param("excludeId") String excludeId);

    /**
     * 批量删除角色（逻辑删除）
     * 
     * @param roleIds 角色ID列表
     * @return 删除数量
     */
    int deleteRolesByIds(@Param("roleIds") List<String> roleIds);

    /**
     * 更新角色状态
     * 
     * @param roleId 角色ID
     * @param status 状态
     * @return 更新数量
     */
    int updateRoleStatus(@Param("roleId") String roleId, @Param("status") String status);
}
