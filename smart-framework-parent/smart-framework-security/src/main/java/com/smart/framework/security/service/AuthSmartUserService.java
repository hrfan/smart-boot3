package com.smart.framework.security.service;

import com.smart.framework.security.entity.AuthSmartUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AuthSmartUserService {

    /**
     * 根据用户ID查询权限标识列表
     *
     * @param id 用户ID
     * @return 权限标识列表
     */
    List<String> findByUserId(@Param("id") String id);


    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户详情
     */
     AuthSmartUser findByUsername(@Param("username") String username);
}
