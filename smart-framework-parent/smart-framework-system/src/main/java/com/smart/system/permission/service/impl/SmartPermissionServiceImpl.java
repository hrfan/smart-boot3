package com.smart.system.permission.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.framework.common.util.MenuTreeUtil;
import com.smart.framework.common.util.SecurityUtils;
import com.smart.system.permission.entity.SmartPermission;
import com.smart.system.permission.mapper.SmartPermissionMapper;
import com.smart.system.permission.service.SmartPermissionService;
import com.smart.system.permission.vo.MetaVo;
import com.smart.system.permission.vo.RouterVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单权限表Service实现类
 * 实现菜单权限业务逻辑层功能
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Service
public class SmartPermissionServiceImpl extends ServiceImpl<SmartPermissionMapper, SmartPermission> implements SmartPermissionService {

    private static final Logger log = LoggerFactory.getLogger(SmartPermissionServiceImpl.class);

    @Autowired
    private SmartPermissionMapper smartPermissionMapper;



    @Override
    @Transactional(rollbackFor = Exception.class)
    public SmartPermission insert(SmartPermission smartPermission) {

        // 从spring security获取当前登录用户ID
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        smartPermission.setCreateBy(currentUserId);
        smartPermission.setUpdateBy(currentUserId);
        boolean b = saveOrUpdate(smartPermission);
        if (b) {
            return smartPermission;
        }
        return null;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public SmartPermission update(SmartPermission smartPermission) {
        // 从spring security获取当前登录用户ID
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        smartPermission.setUpdateBy(currentUserId);
        boolean b = updateById(smartPermission);
        if (b) {
            return smartPermission;
        }
        return null;
    }


    /**
     * 根据用户ID查询权限标识列表
     *
     * @param id 用户ID
     * @return 权限标识列表
     */
    @Override
    public List<String> findByUserId(String id) {

        List<String> permissionCodes = smartPermissionMapper.findByUserId(id);
        if(CollectionUtil.isEmpty(permissionCodes)){
            log.error("根据用户ID查询权限标识列表为空，用户ID：{}", id);
        }
        return permissionCodes;
    }

    /**
     * 根据用户ID查询角色标识列表
     *
     * @param id 用户ID
     * @return 角色标识列表
     */
    @Override
    public List<String> findRoleByUserId(String id) {
        return smartPermissionMapper.findRoleByUserId(id);
    }


    /**
     * 根据用户ID查询菜单权限树
     *
     * @param userId 用户ID
     * @return 菜单权限树
     */
    @Override
    public List<SmartPermission> getMenuTreeByUserId(String userId) {
        log.debug("根据用户ID查询菜单权限树，用户ID：{}", userId);
        
        // 查询用户菜单权限列表
        List<SmartPermission> menus = selectMenuTreeByUserId(userId);
        
        if (CollectionUtil.isEmpty(menus)) {
            log.info("用户无菜单权限，用户ID：{}", userId);
            return new ArrayList<>();
        }
        
        // 将扁平菜单列表转换为树形结构
        List<SmartPermission> menuTree = MenuTreeUtil.buildMenuTree(menus);
        
        log.debug("菜单权限树构建完成，用户ID：{}，菜单数量：{}，树形根节点数量：{}", 
                userId, menus.size(), menuTree.size());
        
        return menuTree;
    }

    /**
     * 根据用户ID查询菜单权限列表
     * 参考mengyuan项目的selectMenuTreeByUserId方法
     *
     * @param userId 用户ID
     * @return 菜单权限列表
     */
    @Override
    public List<SmartPermission> selectMenuTreeByUserId(String userId) {
        log.debug("根据用户ID查询菜单权限列表，用户ID：{}", userId);
        // 判断用户是否为超级管理员
        if (SecurityUtils.isAdmin()) {
            log.info("用户为超级管理员，返回所有菜单权限，用户ID：{}", userId);
            return smartPermissionMapper.selectList(null);
        }
        
        List<SmartPermission> menus = smartPermissionMapper.selectMenuTreeByUserId(userId);
        
        if (CollectionUtil.isEmpty(menus)) {
            log.info("用户无菜单权限，用户ID：{}", userId);
            return new ArrayList<>();
        }
        
        log.debug("用户菜单权限查询完成，用户ID：{}，菜单数量：{}", userId, menus.size());
        return menus;
    }

    /**
     * 构建路由菜单
     * 参考mengyuan项目的buildMenus方法
     *
     * @param menus 菜单权限列表
     * @return 路由菜单列表
     */
    @Override
    public List<RouterVo> buildMenus(List<SmartPermission> menus) {
        log.debug("开始构建路由菜单，菜单数量：{}", menus.size());
        
        List<RouterVo> routers = new ArrayList<>();
        
        for (SmartPermission menu : menus) {
            RouterVo router = new RouterVo();
            
            // 设置路由基本信息
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setHidden("1".equals(menu.getVisible()));
            router.setRedirect("noRedirect");
            router.setComponent(getComponent(menu));
            router.setQuery(menu.getQuery());
            router.setAlwaysShow(menu.getAlwaysShow());
            router.setMeta(getMeta(menu));
            
            // 处理子菜单
            List<? extends MenuTreeUtil.MenuPermission> children = menu.getChildren();
            if (CollectionUtil.isNotEmpty(children)) {
                // 转换为SmartPermission类型
                List<SmartPermission> smartChildren = children.stream()
                    .filter(child -> child instanceof SmartPermission)
                    .map(child -> (SmartPermission) child)
                    .collect(Collectors.toList());
                router.setChildren(buildMenus(smartChildren));
            }
            
            routers.add(router);
        }
        
        log.debug("路由菜单构建完成，路由数量：{}", routers.size());
        return routers;
    }

    /**
     * 获取路由名称
     */
    private String getRouteName(SmartPermission menu) {
        String routeName = menu.getRouteName();
        if (routeName == null || routeName.trim().isEmpty()) {
            // 如果没有设置路由名称，使用菜单名称的首字母大写
            String menuName = menu.getMenuName();
            if (menuName != null && !menuName.trim().isEmpty()) {
                return menuName.substring(0, 1).toUpperCase() + menuName.substring(1);
            }
        }
        return routeName;
    }

    /**
     * 获取路由路径
     */
    private String getRouterPath(SmartPermission menu) {
        String path = menu.getPath();
        if (path == null || path.trim().isEmpty()) {
            return "/";
        }
        return path;
    }

    /**
     * 获取组件路径
     */
    private String getComponent(SmartPermission menu) {
        String component = menu.getComponent();
        if (component == null || component.trim().isEmpty()) {
            // 如果是目录类型，使用Layout组件
            if ("M".equals(menu.getMenuType())) {
                return "Layout";
            }
            // 如果是菜单类型，使用默认组件
            return "index";
        }
        return component;
    }

    /**
     * 获取路由元信息
     */
    private MetaVo getMeta(SmartPermission menu) {
        MetaVo meta = new MetaVo();
        
        meta.setTitle(menu.getMenuName());
        meta.setIcon(menu.getIcon() != null ? menu.getIcon() : "");
        meta.setNoCache("1".equals(menu.getIsCache()));
        
        return meta;
    }

    /**
     * 获取用户路由菜单
     * 参考mengyuan项目的getRouters方法
     *
     * @return 路由菜单列表
     */
    @Override
    public List<RouterVo> getRouters() {
        try {
            // 获取当前登录用户ID
            String userId = SecurityUtils.getCurrentUserId();
            if (userId == null) {
                log.warn("用户未登录，无法获取路由菜单");
                return new ArrayList<>();
            }

            log.info("获取用户路由菜单，用户ID：{}", userId);

            // 查询用户菜单权限列表
            List<SmartPermission> menus = selectMenuTreeByUserId(userId);
            
            if (CollectionUtil.isEmpty(menus)) {
                log.info("用户无菜单权限，用户ID：{}", userId);
                return new ArrayList<>();
            }

            // 构建路由菜单
            List<RouterVo> routers = buildMenus(menus);

            log.info("用户路由菜单获取成功，用户ID：{}，菜单数量：{}，路由数量：{}", 
                    userId, menus.size(), routers.size());

            return routers;

        } catch (Exception e) {
            log.error("获取用户路由菜单失败", e);
            return new ArrayList<>();
        }
    }
}

