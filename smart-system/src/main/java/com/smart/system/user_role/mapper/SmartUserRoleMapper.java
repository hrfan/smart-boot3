package com.smart.system.user_role.mapper;

import com.smart.common.database.mapper.BaseMapper;
import com.smart.system.user_role.entity.SmartUserRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户和角色关联表Mapper接口
 * 提供用户角色数据访问层功能
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Mapper
public interface SmartUserRoleMapper extends BaseMapper<SmartUserRole> {

}
