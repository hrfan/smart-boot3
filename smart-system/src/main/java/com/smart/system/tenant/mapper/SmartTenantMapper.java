package com.smart.system.tenant.mapper;

import com.smart.common.database.mapper.BaseMapper;
import com.smart.system.tenant.entity.SmartTenant;
import org.apache.ibatis.annotations.Mapper;

/**
 * 租户管理表Mapper接口
 * 提供租户管理数据访问层功能
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Mapper
public interface SmartTenantMapper extends BaseMapper<SmartTenant> {

}
