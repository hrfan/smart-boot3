package com.smart.system.tenant_permission.mapper;

import com.smart.framework.database.mapper.BaseMapper;
import com.smart.system.tenant_permission.entity.SmartTenantPermission;
import org.apache.ibatis.annotations.Mapper;

/**
 * 租户权限中间表Mapper接口
 * 提供租户权限数据访问层功能
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Mapper
public interface SmartTenantPermissionMapper extends BaseMapper<SmartTenantPermission> {

}
