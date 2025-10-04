package com.smart.framework.security.mapper;

import com.smart.framework.database.mapper.BaseMapper;
import com.smart.framework.security.entity.AuthSmartUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AuthSmartUserMapper extends BaseMapper<AuthSmartUser> {


    /**
     * 根据用户ID查询用户权限
     *
     * @param id 用户ID
     * @return 用户权限列表
     */
    List<String> findByUserId(@Param("id") String id);
}
