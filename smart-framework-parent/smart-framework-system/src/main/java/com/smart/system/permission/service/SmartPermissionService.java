package com.smart.system.permission.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smart.system.permission.entity.SmartPermission;
import com.smart.system.permission.vo.RouterVo;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 菜单权限表Service接口
 * 定义菜单权限业务逻辑层接口
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public interface SmartPermissionService extends IService<SmartPermission> {

    SmartPermission insert(SmartPermission smartPermission);


    SmartPermission update(SmartPermission smartPermission);

    /**
     * 根据用户ID查询权限标识列表
     *
     * @param id 用户ID
     * @return 权限标识列表
     */
    List<String> findByUserId(String id);


    /**
     * 根据用户ID查询角色标识列表
     *
     * @param id 用户ID
     * @return 角色标识列表
     */
    List<String> findRoleByUserId(String id);


    /**
     * 根据用户ID查询菜单权限树
     *
     * @param userId 用户ID
     * @return 菜单权限树
     */
    List<SmartPermission> getMenuTreeByUserId(String userId);

    /**
     * 根据用户ID查询菜单权限列表
     * 参考mengyuan项目的selectMenuTreeByUserId方法
     *
     * @param userId 用户ID
     * @return 菜单权限列表
     */
    List<SmartPermission> selectMenuTreeByUserId(String userId);

    /**
     * 构建路由菜单
     * 参考mengyuan项目的buildMenus方法
     *
     * @param menus 菜单权限列表
     * @return 路由菜单列表
     */
    List<RouterVo> buildMenus(List<SmartPermission> menus);

    /**
     * 获取用户路由菜单
     * 参考mengyuan项目的getRouters方法
     *
     * @return 路由菜单列表
     */
    List<RouterVo> getRouters();

     /**
     * 根据用户ID查询菜单权限树 不包含按钮权限
     * @param smartPermission 菜单权限查询条件
     * @return 菜单权限树
     */
    List<SmartPermission> selectMenuTree(SmartPermission smartPermission);


    /**
     * 根据查询条件获取菜单权限列表
     * @param smartPermission 查询条件
     * @return 菜单权限列表
     */
    List<SmartPermission> getList(SmartPermission smartPermission);
}

