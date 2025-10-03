package com.smart.system.user_tenant.mapper;

import com.smart.framework.database.mapper.BaseMapper;
import com.smart.system.user_tenant.entity.SmartUserTenant;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户租户中间表Mapper接口
 * 提供用户租户数据访问层功能
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Mapper
public interface SmartUserTenantMapper extends BaseMapper<SmartUserTenant> {

}
