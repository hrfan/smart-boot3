package com.smart.system.role_permission.mapper;

import com.smart.common.database.mapper.BaseMapper;
import com.smart.system.role_permission.entity.SmartRolePermission;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色权限中间表Mapper接口
 * 提供角色权限数据访问层功能
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Mapper
public interface SmartRolePermissionMapper extends BaseMapper<SmartRolePermission> {

}
