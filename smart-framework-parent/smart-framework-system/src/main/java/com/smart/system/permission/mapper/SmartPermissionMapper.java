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
}

