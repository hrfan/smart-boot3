package com.smart.framework.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单树形组装工具类
 * 将扁平的菜单权限列表转换为树形结构
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public class MenuTreeUtil {

    private static final Logger log = LoggerFactory.getLogger(MenuTreeUtil.class);

    /**
     * 根节点ID
     */
    private static final String ROOT_NODE_ID = "0";

    /**
     * 菜单权限接口，用于泛型支持
     */
    public interface MenuPermission {
        String getId();
        String getParentId();
        Integer getOrderNum();
        String getMenuName();
        String getPath();
        String getComponent();
        String getIcon();
        String getMenuType();
        String getVisible();
        String getPerms();
        List<? extends MenuPermission> getChildren();
        void setChildren(List<? extends MenuPermission> children);
    }

    /**
     * 将扁平菜单列表转换为树形结构
     * 
     * @param menuList 扁平菜单列表
     * @param <T> 菜单权限类型
     * @return 树形菜单列表
     */
    @SuppressWarnings("unchecked")
    public static <T extends MenuPermission> List<T> buildMenuTree(List<T> menuList) {
        if (CollectionUtils.isEmpty(menuList)) {
            log.warn("菜单列表为空，无法构建树形结构");
            return new ArrayList<>();
        }

        log.debug("开始构建菜单树形结构，原始菜单数量：{}", menuList.size());

        // 1. 创建菜单ID到菜单对象的映射
        Map<String, T> menuMap = menuList.stream()
                .collect(Collectors.toMap(MenuPermission::getId, menu -> menu));

        // 2. 构建树形结构
        List<T> rootMenus = new ArrayList<>();
        
        for (T menu : menuList) {
            String parentId = menu.getParentId();
            
            // 如果是根节点或父节点为空，添加到根节点列表
            if (parentId == null || parentId.isEmpty() || ROOT_NODE_ID.equals(parentId)) {
                rootMenus.add(menu);
            } else {
                // 查找父节点并添加到父节点的子节点列表
                T parentMenu = menuMap.get(parentId);
                if (parentMenu != null) {
                    List<? extends MenuPermission> children = parentMenu.getChildren();
                    if (children == null) {
                        parentMenu.setChildren(new ArrayList<>());
                        children = parentMenu.getChildren();
                    }
                    ((List<T>) children).add(menu);
                } else {
                    log.warn("未找到父菜单，菜单ID：{}，父菜单ID：{}", menu.getId(), parentId);
                    // 如果找不到父节点，将其作为根节点处理
                    rootMenus.add(menu);
                }
            }
        }

        // 3. 对每个层级的菜单进行排序
        sortMenus((List<MenuPermission>) rootMenus);

        log.debug("菜单树形结构构建完成，根节点数量：{}", rootMenus.size());
        return rootMenus;
    }

    /**
     * 递归排序菜单列表
     * 
     * @param menus 菜单列表
     */
    @SuppressWarnings("unchecked")
    private static void sortMenus(List<MenuPermission> menus) {
        if (CollectionUtils.isEmpty(menus)) {
            return;
        }

        // 按orderNum排序
        menus.sort(Comparator.comparing(menu -> 
            menu.getOrderNum() != null ? menu.getOrderNum() : 0));

        // 递归排序子菜单
        for (MenuPermission menu : menus) {
            if (!CollectionUtils.isEmpty(menu.getChildren())) {
                sortMenus((List<MenuPermission>) menu.getChildren());
            }
        }
    }

    /**
     * 从树形菜单中提取所有菜单ID
     * 
     * @param menuTree 树形菜单
     * @param <T> 菜单权限类型
     * @return 所有菜单ID列表
     */
    @SuppressWarnings("unchecked")
    public static <T extends MenuPermission> List<String> extractMenuIds(List<T> menuTree) {
        List<String> menuIds = new ArrayList<>();
        if (CollectionUtils.isEmpty(menuTree)) {
            return menuIds;
        }

        for (T menu : menuTree) {
            menuIds.add(menu.getId());
            if (!CollectionUtils.isEmpty(menu.getChildren())) {
                menuIds.addAll(extractMenuIds((List<T>) menu.getChildren()));
            }
        }

        return menuIds;
    }

    /**
     * 从树形菜单中查找指定ID的菜单
     * 
     * @param menuTree 树形菜单
     * @param menuId 菜单ID
     * @param <T> 菜单权限类型
     * @return 找到的菜单，未找到返回null
     */
    @SuppressWarnings("unchecked")
    public static <T extends MenuPermission> T findMenuById(List<T> menuTree, String menuId) {
        if (CollectionUtils.isEmpty(menuTree) || menuId == null) {
            return null;
        }

        for (T menu : menuTree) {
            if (menuId.equals(menu.getId())) {
                return menu;
            }
            
            if (!CollectionUtils.isEmpty(menu.getChildren())) {
                T found = findMenuById((List<T>) menu.getChildren(), menuId);
                if (found != null) {
                    return found;
                }
            }
        }

        return null;
    }

    /**
     * 获取树形菜单的深度
     * 
     * @param menuTree 树形菜单
     * @param <T> 菜单权限类型
     * @return 最大深度
     */
    @SuppressWarnings("unchecked")
    public static <T extends MenuPermission> int getMaxDepth(List<T> menuTree) {
        if (CollectionUtils.isEmpty(menuTree)) {
            return 0;
        }

        int maxDepth = 0;
        for (T menu : menuTree) {
            int depth = 1;
            if (!CollectionUtils.isEmpty(menu.getChildren())) {
                depth += getMaxDepth((List<T>) menu.getChildren());
            }
            maxDepth = Math.max(maxDepth, depth);
        }

        return maxDepth;
    }

    /**
     * 打印树形菜单结构（用于调试）
     * 
     * @param menuTree 树形菜单
     * @param <T> 菜单权限类型
     */
    public static <T extends MenuPermission> void printMenuTree(List<T> menuTree) {
        printMenuTree(menuTree, 0);
    }

    /**
     * 递归打印树形菜单结构
     * 
     * @param menuTree 树形菜单
     * @param level 层级
     * @param <T> 菜单权限类型
     */
    @SuppressWarnings("unchecked")
    private static <T extends MenuPermission> void printMenuTree(List<T> menuTree, int level) {
        if (CollectionUtils.isEmpty(menuTree)) {
            return;
        }

        String indent = "  ".repeat(level);
        for (T menu : menuTree) {
            log.info("{}{} - {} ({})", indent, menu.getMenuName(), menu.getPath(), menu.getId());
            if (!CollectionUtils.isEmpty(menu.getChildren())) {
                printMenuTree((List<T>) menu.getChildren(), level + 1);
            }
        }
    }

    /**
     * 检查菜单是否为叶子节点
     * 
     * @param menu 菜单
     * @return 是否为叶子节点
     */
    public static boolean isLeafNode(MenuPermission menu) {
        return CollectionUtils.isEmpty(menu.getChildren());
    }

    /**
     * 获取菜单的所有父级菜单ID
     * 
     * @param menuTree 树形菜单
     * @param menuId 菜单ID
     * @param <T> 菜单权限类型
     * @return 父级菜单ID列表
     */
    public static <T extends MenuPermission> List<String> getParentMenuIds(List<T> menuTree, String menuId) {
        List<String> parentIds = new ArrayList<>();
        findParentMenuIds(menuTree, menuId, parentIds);
        return parentIds;
    }

    /**
     * 递归查找父级菜单ID
     * 
     * @param menuTree 树形菜单
     * @param targetMenuId 目标菜单ID
     * @param parentIds 父级菜单ID列表
     * @param <T> 菜单权限类型
     * @return 是否找到
     */
    @SuppressWarnings("unchecked")
    private static <T extends MenuPermission> boolean findParentMenuIds(List<T> menuTree, String targetMenuId, List<String> parentIds) {
        if (CollectionUtils.isEmpty(menuTree)) {
            return false;
        }

        for (T menu : menuTree) {
            if (targetMenuId.equals(menu.getId())) {
                return true;
            }
            
            if (!CollectionUtils.isEmpty(menu.getChildren())) {
                parentIds.add(menu.getId());
                if (findParentMenuIds((List<T>) menu.getChildren(), targetMenuId, parentIds)) {
                    return true;
                }
                parentIds.remove(parentIds.size() - 1);
            }
        }

        return false;
    }
}
