package com.smart.system.org.mapper;

import com.smart.framework.database.mapper.BaseMapper;
import com.smart.system.org.entity.SmartOrg;
import org.apache.ibatis.annotations.Mapper;

/**
 * 组织信息表Mapper接口
 * 提供组织数据访问层功能
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Mapper
public interface SmartOrgMapper extends BaseMapper<SmartOrg> {

}
