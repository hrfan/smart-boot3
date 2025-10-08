package com.smart.system.permission.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.framework.common.util.MenuTreeUtil;
import com.smart.framework.common.util.SecurityUtils;
import com.smart.framework.core.exception.ErrorException;
import com.smart.framework.core.result.Result;
import com.smart.framework.core.result.ResultCode;
import com.smart.system.permission.entity.SmartPermission;
import com.smart.system.permission.mapper.SmartPermissionMapper;
import com.smart.system.permission.service.SmartPermissionService;
import com.smart.system.permission.vo.MetaVo;
import com.smart.system.permission.vo.RouterVo;
import com.smart.system.permission.vo.TreeSelect;
import com.smart.system.role.entiy.SmartRole;
import com.smart.system.role.mapper.SmartRoleMapper;
import com.smart.system.role_permission.entity.SmartRolePermission;
import com.smart.system.role_permission.mapper.SmartRolePermissionMapper;
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

    @Autowired
    private SmartRoleMapper smartRoleMapper;

    @Autowired
    private SmartRolePermissionMapper smartRolePermissionMapper;



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
        String username = SecurityUtils.getCurrentUsername();
        smartPermission.setUpdateBy(username);
        smartPermission.setUpdateTime(new Date());
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
            router.setHidden("1".equals(menu.getVisible()));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setQuery(menu.getQuery());
            router.setMeta(getMeta(menu));
            
            // 获取子菜单
            List<? extends MenuTreeUtil.MenuPermission> children = menu.getChildren();
            List<SmartPermission> smartChildren = null;
            if (CollectionUtil.isNotEmpty(children)) {
                smartChildren = children.stream()
                    .filter(child -> child instanceof SmartPermission)
                    .map(child -> (SmartPermission) child)
                    .collect(Collectors.toList());
            }
            
            // 根据菜单类型和子菜单情况设置不同的属性
            if (CollectionUtil.isNotEmpty(smartChildren) && "M".equals(menu.getMenuType())) {
                // 目录类型且有子菜单
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(smartChildren));
            } else if (isMenuFrame(menu)) {
                // 菜单框架类型
                router.setMeta(null);
                List<RouterVo> childrenList = new ArrayList<>();
                RouterVo childRouter = new RouterVo();
                childRouter.setPath(menu.getPath());
                childRouter.setComponent(menu.getComponent());
                childRouter.setName(getRouteName(menu.getRouteName(), menu.getPath()));
                childRouter.setMeta(getMeta(menu));
                childRouter.setQuery(menu.getQuery());
                childrenList.add(childRouter);
                router.setChildren(childrenList);
            } else if ("0".equals(menu.getParentId()) && isInnerLink(menu)) {
                // 外链类型
                router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));
                router.setPath("/");
                List<RouterVo> childrenList = new ArrayList<>();
                RouterVo childRouter = new RouterVo();
                String routerPath = innerLinkReplaceEach(menu.getPath());
                childRouter.setPath(routerPath);
                childRouter.setComponent("InnerLink");
                childRouter.setName(getRouteName(menu.getRouteName(), routerPath));
                childRouter.setMeta(getMeta(menu, menu.getPath()));
                childrenList.add(childRouter);
                router.setChildren(childrenList);
            }
            
            routers.add(router);
        }
        
        log.debug("路由菜单构建完成，路由数量：{}", routers.size());
        return routers;
    }

    /**
     * 获取路由名称
     * 参考mengyuan项目的getRouteName方法
     */
    private String getRouteName(SmartPermission menu) {
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame(menu)) {
            return "";
        }
        return getRouteName(menu.getRouteName(), menu.getPath());
    }

    /**
     * 获取路由名称，如没有配置路由名称则取路由地址
     * 参考mengyuan项目的getRouteName方法
     * 
     * @param name 路由名称
     * @param path 路由地址
     * @return 路由名称（驼峰格式）
     */
    private String getRouteName(String name, String path) {
        String routerName = (name != null && !name.trim().isEmpty()) ? name : path;
        if (routerName == null || routerName.trim().isEmpty()) {
            return "";
        }
        // 首字母大写
        return routerName.substring(0, 1).toUpperCase() + routerName.substring(1);
    }

    /**
     * 获取路由路径
     * 参考mengyuan项目的getRouterPath方法
     */
    private String getRouterPath(SmartPermission menu) {
        String routerPath = menu.getPath();
        // 内链打开外网方式
        if (!"0".equals(menu.getParentId()) && isInnerLink(menu)) {
            routerPath = innerLinkReplaceEach(routerPath);
        }
        // 非外链并且是一级目录（类型为目录）
        if ("0".equals(menu.getParentId()) && "M".equals(menu.getMenuType()) && "1".equals(menu.getIsFrame())) {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取组件信息
     * 参考mengyuan项目的getComponent方法
     * 
     * @param menu 菜单信息
     * @return 组件信息
     */
    private String getComponent(SmartPermission menu) {
        String component = "Layout";
        if (menu.getComponent() != null && !menu.getComponent().trim().isEmpty() && !isMenuFrame(menu)) {
            component = menu.getComponent();
        } else if ((menu.getComponent() == null || menu.getComponent().trim().isEmpty()) && !"0".equals(menu.getParentId()) && isInnerLink(menu)) {
            component = "InnerLink";
        } else if ((menu.getComponent() == null || menu.getComponent().trim().isEmpty()) && isParentView(menu)) {
            component = "ParentView";
        }
        return component;
    }

    /**
     * 是否为parent_view组件
     * 参考mengyuan项目的isParentView方法
     * 
     * @param menu 菜单信息
     * @return 结果
     */
    private boolean isParentView(SmartPermission menu) {
        return !"0".equals(menu.getParentId()) && "M".equals(menu.getMenuType());
    }

    /**
     * 获取路由元信息
     * 参考mengyuan项目的MetaVo构造方法
     */
    private MetaVo getMeta(SmartPermission menu) {
        return new MetaVo(menu.getMenuName(), menu.getIcon(), "1".equals(menu.getIsCache()), menu.getPath());
    }

    /**
     * 获取路由元信息（重载方法）
     * 参考mengyuan项目的MetaVo构造方法
     */
    private MetaVo getMeta(SmartPermission menu, String link) {
        MetaVo meta = new MetaVo();
        meta.setTitle(menu.getMenuName());
        meta.setIcon(menu.getIcon());
        meta.setNoCache("1".equals(menu.getIsCache()));
        meta.setLink(link);
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

            // 查询用户菜单权限列表（扁平列表）
            List<SmartPermission> menus = selectMenuTreeByUserId(userId);
            
            if (CollectionUtil.isEmpty(menus)) {
                log.info("用户无菜单权限，用户ID：{}", userId);
                return new ArrayList<>();
            }

            // 构建树形结构，参考mengyuan项目的getChildPerms方法
            List<SmartPermission> menuTree = getChildPerms(menus, "0");
            
            // 构建路由菜单
            List<RouterVo> routers = buildMenus(menuTree);

            log.info("用户路由菜单获取成功，用户ID：{}，菜单数量：{}，树形根节点数量：{}，路由数量：{}", 
                    userId, menus.size(), menuTree.size(), routers.size());

            return routers;

        } catch (Exception e) {
            log.error("获取用户路由菜单失败", e);
            return new ArrayList<>();
        }
    }


    /**
     * 根据用户ID查询菜单权限树 不包含按钮权限
     * @param smartPermission 菜单权限查询条件
     * @return 菜单权限树
     */
    @Override
    public List<SmartPermission> selectMenuTree(SmartPermission smartPermission) {
        // 从数据库查询用户菜单权限列表（扁平列表）
        String userId = SecurityUtils.getCurrentUserId();
        List<SmartPermission> menus = smartPermissionMapper.selectMenuTreeByUserId(userId);
        if (CollectionUtil.isEmpty(menus)) {
            throw new ErrorException(ResultCode.ERROR, "用户无菜单权限");
        }
        // 构建树形结构
        return getChildPerms(menus, "0");
    }


    /**
     * 根据查询条件获取菜单权限列表
     * @param smartPermission 查询条件
     * @return 菜单权限列表
     */
    @Override
    public List<SmartPermission> getList(SmartPermission smartPermission) {
        // TODO 此处应该判断是否是管理员，如果是管理员 应该可以看到全部的菜单

        // 从数据库查询菜单权限列表（扁平列表）
        List<SmartPermission> smartPermissions = smartPermissionMapper.getList(smartPermission);
        if (CollectionUtil.isEmpty(smartPermissions)) {
            throw new ErrorException(ResultCode.ERROR, "无菜单权限");
        }
        return smartPermissions;
    }



    /**
     * 根据parentId获取最大序号
     * @param parentId 父菜单权限ID，用于指定要查询的父菜单权限
     * @return 最大序号结果
     */
    @Override
    public Integer getMaxSortNo(String parentId) {
        // 从数据库查询最大序号
        Integer maxSortNo = smartPermissionMapper.getMaxSortNo(parentId);
        if (maxSortNo == null) {
            maxSortNo = 0;
        }
        return maxSortNo + 1;
    }



     /**
     * 根据角色id查询对应菜单权限列表
     * @param roleId 角色ID，用于指定要查询的角色
     * @return 菜单权限列表结果
     */
    @Override
    public Map<String, Object> getPermissionByRoleId(String roleId) {
        HashMap<String, Object> map = new HashMap<>();
        // 获取当前所有菜单
        // String userId = SecurityUtils.getCurrentUserId();
        List<SmartPermission> menus = smartPermissionMapper.getPermissionAllButtonByRoleId();

        // 将menus转为树形结构
        if (CollectionUtil.isEmpty(menus)) {
            throw new ErrorException(ResultCode.ERROR, "用户无菜单权限");
        }
        // 构建树形结构
        List<SmartPermission> menuTree = MenuTreeUtil.buildMenuTree(menus);

        // 将菜单结构 转为 treeSelect 格式
        List<TreeSelect> treeSelectList = SmartPermissionConvertTreeSelectService.buildTreeSelect(menuTree);

        map.put("menus", treeSelectList);

        // 获取角色信息
        SmartRole role = smartRoleMapper.selectById(roleId);
        if (role == null) {
            throw new ErrorException(ResultCode.ERROR, "角色不存在");
        }
        map.put("role", role);
        // 判断角色是否设置父子联动
        Boolean menuCheckStrictly = role.getMenuCheckStrictly();

        // 根据角色id获取 角色已经配置的菜单id，用于前端的回显
        List<String> roleMenus = smartPermissionMapper.findPermissionByRoleId(roleId,menuCheckStrictly);
        map.put("checkedKeys", roleMenus);

        return map;
    }

    /**
     * 获取所有菜单权限树(将数据返回格式转为treeSelect格式)
     * 用于前端的树选择组件展示
     * @return 菜单权限树
     */
    @Override
    public List<TreeSelect> getTreeSelect() {
// 获取当前所有菜单
        // String userId = SecurityUtils.getCurrentUserId();
        List<SmartPermission> menus = smartPermissionMapper.getPermissionAllButtonByRoleId();

        // 将menus转为树形结构
        if (CollectionUtil.isEmpty(menus)) {
            throw new ErrorException(ResultCode.ERROR, "用户无菜单权限");
        }
        // 构建树形结构
        List<SmartPermission> menuTree = MenuTreeUtil.buildMenuTree(menus);

        // 将菜单结构 转为 treeSelect 格式
        List<TreeSelect> treeSelectList = SmartPermissionConvertTreeSelectService.buildTreeSelect(menuTree);
        return treeSelectList;
    }



    /**
     * 根据角色id查询对应菜单权限列表（不包含按钮）
     * @param roleId 角色ID，用于指定要查询的角色
     * @return 菜单权限列表结果
     */
    @Override
    public Map<String, Object> getPermissionNoButtonByRoleId(String roleId) {
        HashMap<String, Object> map = new HashMap<>();
        // 获取当前所有菜单
        // String userId = SecurityUtils.getCurrentUserId();
        List<SmartPermission> menus = smartPermissionMapper.getPermissionNoButtonByRoleId(null);

        // 将menus转为树形结构
        if (CollectionUtil.isEmpty(menus)) {
            throw new ErrorException(ResultCode.ERROR, "用户无菜单权限");
        }
        // 构建树形结构
        List<SmartPermission> menuTree = MenuTreeUtil.buildMenuTree(menus);

        // 将菜单结构 转为 treeSelect 格式
        List<TreeSelect> treeSelectList = SmartPermissionConvertTreeSelectService.buildTreeSelect(menuTree);

        map.put("menus", treeSelectList);

        // 获取角色信息
        SmartRole role = smartRoleMapper.selectById(roleId);
        if (role == null) {
            throw new ErrorException(ResultCode.ERROR, "角色不存在");
        }
        map.put("role", role);
        // 判断角色是否设置父子联动
        Boolean menuCheckStrictly = role.getMenuCheckStrictly();

        // 根据角色id获取 角色已经配置的菜单id，用于前端的回显
        List<String> roleMenus = smartPermissionMapper.findPermissionNoButtonByRoleId(roleId,menuCheckStrictly);
        map.put("checkedKeys", roleMenus);

        return map;
    }



    /**
     * 根据角色ID和权限ID查询按钮权限列表
     * @param roleId 角色ID，用于指定要查询的角色
     * @param permissionId 权限ID，用于指定要查询的权限
     * @return 按钮权限列表结果
     */
    @Override
    public Map<String, Object> getButtonListByPermissionId(String roleId, String permissionId) {
        HashMap<String, Object> map = new HashMap<>();
        // 获取当前权限对应的所有按钮列表
        LambdaQueryWrapper<SmartPermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SmartPermission::getParentId, permissionId);
        queryWrapper.eq(SmartPermission::getMenuType,"F");
        List<SmartPermission> smartPermissions = smartPermissionMapper.selectList(queryWrapper);
        map.put("menus", smartPermissions);


        // 根据角色ID + 权限ID获取当前角色已经配置的按钮id，用于前端的回显
        List<String> roleButtons = smartPermissionMapper.getPermissionButtonByRoleIdAndPermissionId(roleId, permissionId);
        map.put("checkedKeys", roleButtons);
        return map;
    }


    /**
     * 根据菜单ID列表查询所有相关的按钮权限ID
     * @param menuIds 菜单ID列表，用于指定要查询的多个菜单
     * @return 所有相关的按钮权限ID列表
     */
    @Override
    public List<String> getAllButtonIdsByMenuIds(String menuIds) {

        // menuIds时多个字符串拼接的 ，1,23,4
        LambdaQueryWrapper<SmartPermission> queryWrapper = new LambdaQueryWrapper<SmartPermission>()
                .in(SmartPermission::getParentId, menuIds.split(","))
                .eq(SmartPermission::getMenuType, "F");
        // 根据菜单ID列表查询所有相关的按钮权限ID
        List<SmartPermission> smartPermissions = smartPermissionMapper.selectList(queryWrapper);
        List<String> buttonIds = smartPermissions.stream()
                .map(SmartPermission::getId)
                .collect(Collectors.toList());
        return buttonIds;
    }

    /**
     * 根据角色ID查询所有相关的按钮权限ID
     * @param roleId 角色ID，用于指定要查询的角色
     * @return 所有相关的按钮权限ID列表
     */
    @Override
    public List<String> getRolePermissionsButtonById(String roleId) {
        List<String> buttonIds = smartPermissionMapper.getRolePermissionsButtonById(roleId);
        return buttonIds;
    }


    /**
     * 根据父节点的ID获取所有子节点
     * 参考mengyuan项目的getChildPerms方法
     * 
     * @param list 分类表
     * @param parentId 传入的父节点ID
     * @return 树形结构列表
     */
    public List<SmartPermission> getChildPerms(List<SmartPermission> list, String parentId) {
        List<SmartPermission> returnList = new ArrayList<>();
        for (Iterator<SmartPermission> iterator = list.iterator(); iterator.hasNext();) {
            SmartPermission t = iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (parentId.equals(t.getParentId())) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     * 参考mengyuan项目的recursionFn方法
     * 
     * @param list 分类表
     * @param t 子节点
     */
    private void recursionFn(List<SmartPermission> list, SmartPermission t) {
        // 得到子节点列表
        List<SmartPermission> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SmartPermission tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     * 参考mengyuan项目的getChildList方法
     */
    private List<SmartPermission> getChildList(List<SmartPermission> list, SmartPermission t) {
        List<SmartPermission> tlist = new ArrayList<>();
        Iterator<SmartPermission> it = list.iterator();
        while (it.hasNext()) {
            SmartPermission n = it.next();
            if (t.getId().equals(n.getParentId())) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     * 参考mengyuan项目的hasChild方法
     */
    private boolean hasChild(List<SmartPermission> list, SmartPermission t) {
        return getChildList(list, t).size() > 0;
    }

    /**
     * 是否为菜单框架
     * 参考mengyuan项目的isMenuFrame方法
     * 
     * @param menu 菜单信息
     * @return 结果
     */
    private boolean isMenuFrame(SmartPermission menu) {
        return "0".equals(menu.getParentId()) && "C".equals(menu.getMenuType()) && "1".equals(menu.getIsFrame());
    }

    /**
     * 是否为内链组件
     * 参考mengyuan项目的isInnerLink方法
     * 
     * @param menu 菜单信息
     * @return 结果
     */
    private boolean isInnerLink(SmartPermission menu) {
        return "1".equals(menu.getIsFrame()) && isHttp(menu.getPath());
    }

    /**
     * 判断是否为http链接
     * 
     * @param path 路径
     * @return 结果
     */
    private boolean isHttp(String path) {
        return path != null && (path.startsWith("http://") || path.startsWith("https://"));
    }

    /**
     * 内链域名特殊字符替换
     * 参考mengyuan项目的innerLinkReplaceEach方法
     * 
     * @param path 路径
     * @return 替换后的内链域名
     */
    private String innerLinkReplaceEach(String path) {
        if (path == null) {
            return "";
        }
        return path.replace("http://", "")
                  .replace("https://", "")
                  .replace("www.", "")
                  .replace(".", "/")
                  .replace(":", "/");
    }
}

