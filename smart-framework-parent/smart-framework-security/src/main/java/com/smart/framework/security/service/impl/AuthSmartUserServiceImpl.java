package com.smart.framework.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.framework.security.entity.AuthSmartPermission;
import com.smart.framework.security.entity.AuthSmartUser;
import com.smart.framework.security.mapper.AuthSmartUserMapper;
import com.smart.framework.security.service.AuthSmartUserService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 认证用户服务实现类
 * 提供用户认证相关的业务逻辑
 *
 * @author Smart Boot3
 * @since 1.0.0
 */
@Service
public class AuthSmartUserServiceImpl extends ServiceImpl<AuthSmartUserMapper, AuthSmartUser> implements AuthSmartUserService {

    @Resource
    private AuthSmartUserMapper authSmartUserMapper;

    private final Logger log = LoggerFactory.getLogger(AuthSmartUserServiceImpl.class);


    /**
     * 根据用户ID查询用户权限
     *
     * @param id 用户ID
     * @return 用户权限列表
     */
    @Override
    public List<String> findByUserId(String id) {
        return authSmartUserMapper.findByUserId(id);
    }



    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户详情
     */
    @Override
    public AuthSmartUser findByUsername(String username) {
        LambdaQueryWrapper<AuthSmartUser> eq = new LambdaQueryWrapper<AuthSmartUser>()
                .eq(AuthSmartUser::getUsername, username);
        AuthSmartUser authSmartUser = authSmartUserMapper.selectOne(eq);
        if (authSmartUser == null) {
            // 用户名不存在
            log.error("用户名不存在：{}", username);
            return null;
        }
        return authSmartUser;
    }

    /**
     * 根据用户ID查询用户角色列表
     *
     * @param userId 用户ID
     * @return 角色代码列表
     */
    @Override
    public List<String> findRolesByUserId(String userId) {
        log.debug("查询用户角色，用户ID：{}", userId);
        return authSmartUserMapper.findRolesByUserId(userId);
    }

    /**
     * 根据用户ID查询用户菜单权限列表
     *
     * @param userId 用户ID
     * @return 菜单权限列表
     */
    @Override
    public List<AuthSmartPermission> findMenusByUserId(String userId) {
        log.debug("查询用户菜单权限，用户ID：{}", userId);
        return authSmartUserMapper.findMenusByUserId(userId);
    }
}
