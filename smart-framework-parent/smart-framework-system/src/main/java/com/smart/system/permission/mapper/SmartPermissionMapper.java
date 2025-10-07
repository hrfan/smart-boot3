package com.smart.system.permission.mapper;

import com.smart.framework.database.mapper.BaseMapper;
import com.smart.system.permission.entity.SmartPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单权限表Mapper接口
 * 提供菜单权限数据访问层功能
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Mapper
public interface SmartPermissionMapper extends BaseMapper<SmartPermission> {

    /**
     * 根据用户ID查询权限标识列表
     *
     * @param id 用户ID
     * @return 权限标识列表
     */
    List<String> findByUserId(@Param("id") String id);

     /**
      * 根据用户ID查询角色标识列表
      *
      * @param id 用户ID
      * @return 角色标识列表
      */
    List<String> findRoleByUserId(@Param("userId") String id);

    /**
     * 根据用户ID查询菜单权限列表
     * 参考mengyuan项目的selectMenuTreeByUserId方法
     *
     * @param userId 用户ID
     * @return 菜单权限列表
     */
    List<SmartPermission> selectMenuTreeByUserId(@Param("userId") String userId);


    /**
     * 获取所有权限列表（包含按钮权限）
     * @return 所有权限列表
     */
    List<SmartPermission> getPermissionAllButtonByRoleId();

    /**
     * 根据查询条件获取菜单权限列表
     * @param smartPermission 查询条件
     * @return 菜单权限列表
     */
    List<SmartPermission> getList(SmartPermission smartPermission);

    /**
     * 根据父节点ID获取最大排序号
     * @param parentId 父节点ID
     * @return 最大排序号
     */
    Integer getMaxSortNo(@Param("parentId") String parentId);

     /**
      * 根据角色ID查询菜单权限ID列表
      * @param roleId 角色ID
      * @param menuCheckStrictly 角色是否设置父子联动 true 关联 false 不关联
      * @return 菜单权限ID列表
      */
    List<String> findPermissionByRoleId(@Param("roleId") String roleId,@Param("menuCheckStrictly") Boolean menuCheckStrictly);

     /**
      * 根据角色ID查询菜单权限列表 不包含按钮权限
      * @param roleId 角色ID
      * @return 菜单权限列表
      */
    List<SmartPermission> getPermissionNoButtonByRoleId(@Param("roleId") String roleId);

     /**
      * 根据角色ID查询菜单权限ID列表 不包含按钮权限
      * @param roleId 角色ID
      * @param menuCheckStrictly 角色是否设置父子联动 true 关联 false 不关联
      * @return 菜单权限ID列表
      */
    List<String> findPermissionNoButtonByRoleId(@Param("roleId") String roleId,@Param("menuCheckStrictly") Boolean menuCheckStrictly);

     /**
      * 根据角色ID和权限ID查询按钮权限ID列表
      * @param roleId 角色ID
      * @param permissionId 权限ID
      * @return 按钮权限ID列表
      */
    List<String> getPermissionButtonByRoleIdAndPermissionId(@Param("roleId") String roleId, @Param("permissionId") String permissionId);
}

