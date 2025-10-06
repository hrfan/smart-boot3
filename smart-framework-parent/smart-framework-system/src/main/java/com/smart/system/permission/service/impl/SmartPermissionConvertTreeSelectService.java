package com.smart.system.permission.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.smart.system.permission.entity.SmartPermission;
import com.smart.system.permission.vo.TreeSelect;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 树形选择器工具类
 * 用于将SmartPermission树形结构转换为TreeSelect结构
 * ## 基本用法
 * List<SmartPermission> menuTree = MenuTreeUtil.buildMenuTree(menus);
 * List<TreeSelect> treeSelectList = SmartPermissionConvertTreeSelectService.buildTreeSelect(menuTree);
 *
 * ## 排除指定ID的用法（比如编辑时排除自己）
 * List<TreeSelect> treeSelectList = SmartPermissionConvertTreeSelectService.buildTreeSelectExclude(menuTree, currentId);
 *
 * ## 只显示目录和菜单，不显示按钮
 * List<TreeSelect> treeSelectList = SmartPermissionConvertTreeSelectService.buildTreeSelectByMenuType(menuTree, "M", "C");
 * @author Smart Boot3
 * @since 1.0.0
 */
public class SmartPermissionConvertTreeSelectService {


    /**
     * 将SmartPermission树形结构转换为TreeSelect结构
     * 
     * @param permissions SmartPermission树形列表
     * @return TreeSelect树形列表
     */
    public static List<TreeSelect> buildTreeSelect(List<SmartPermission> permissions) {
        if (CollectionUtil.isEmpty(permissions)) {
            return new ArrayList<>();
        }

        return permissions.stream()
                .map(SmartPermissionConvertTreeSelectService::convertToTreeSelect)
                .collect(Collectors.toList());
    }

    /**
     * 将单个SmartPermission转换为TreeSelect
     *
     * @param permission SmartPermission对象
     * @return TreeSelect对象
     */
    private static TreeSelect convertToTreeSelect(SmartPermission permission) {
        if (permission == null) {
            return null;
        }

        TreeSelect treeSelect = new TreeSelect();

        // 设置基本信息
        treeSelect.setId(permission.getId());
        treeSelect.setLabel(permission.getMenuName());

        // 设置禁用状态（可以根据业务需求调整）
        treeSelect.setDisabled(isDisabled(permission));

        // 处理子节点
        List<? extends com.smart.framework.common.util.MenuTreeUtil.MenuPermission> children = permission.getChildren();
        if (CollectionUtil.isNotEmpty(children)) {
            List<SmartPermission> smartChildren = children.stream()
                    .filter(child -> child instanceof SmartPermission)
                    .map(child -> (SmartPermission) child)
                    .collect(Collectors.toList());

            List<TreeSelect> childTreeSelects = buildTreeSelect(smartChildren);
            treeSelect.setChildren(childTreeSelects);
        }

        return treeSelect;
    }

    /**
     * 判断节点是否禁用
     * 可以根据业务需求自定义禁用逻辑
     *
     * @param permission SmartPermission对象
     * @return 是否禁用
     */
    private static boolean isDisabled(SmartPermission permission) {
        if (permission == null) {
            return true;
        }

        // 示例：根据状态判断是否禁用
        // 如果状态为禁用，则节点禁用
        if ("1".equals(permission.getStatus())) {
            return true;
        }

        // 示例：根据菜单类型判断是否禁用
        // 如果是按钮类型，可以选择禁用（根据业务需求）
        if ("F".equals(permission.getMenuType())) {
            return false; // 按钮类型不禁用，可以根据需要调整
        }

        return false;
    }

    /**
     * 根据指定ID过滤树形结构
     * 排除指定ID及其子节点
     *
     * @param permissions 原始树形列表
     * @param excludeId 要排除的ID
     * @return 过滤后的树形列表
     */
    public static List<TreeSelect> buildTreeSelectExclude(List<SmartPermission> permissions, String excludeId) {
        if (CollectionUtil.isEmpty(permissions)) {
            return new ArrayList<>();
        }

        return permissions.stream()
                .filter(permission -> !excludeId.equals(permission.getId()))
                .map(permission -> convertToTreeSelectExclude(permission, excludeId))
                .filter(treeSelect -> treeSelect != null)
                .collect(Collectors.toList());
    }

    /**
     * 转换单个节点，排除指定ID的子节点
     *
     * @param permission SmartPermission对象
     * @param excludeId 要排除的ID
     * @return TreeSelect对象
     */
    private static TreeSelect convertToTreeSelectExclude(SmartPermission permission, String excludeId) {
        if (permission == null) {
            return null;
        }

        TreeSelect treeSelect = new TreeSelect();
        treeSelect.setId(permission.getId());
        treeSelect.setLabel(permission.getMenuName());
        treeSelect.setDisabled(isDisabled(permission));

        // 处理子节点，排除指定ID
        List<? extends com.smart.framework.common.util.MenuTreeUtil.MenuPermission> children = permission.getChildren();
        if (CollectionUtil.isNotEmpty(children)) {
            List<SmartPermission> smartChildren = children.stream()
                    .filter(child -> child instanceof SmartPermission)
                    .map(child -> (SmartPermission) child)
                    .filter(child -> !excludeId.equals(child.getId()))
                    .collect(Collectors.toList());

            if (CollectionUtil.isNotEmpty(smartChildren)) {
                List<TreeSelect> childTreeSelects = buildTreeSelectExclude(smartChildren, excludeId);
                treeSelect.setChildren(childTreeSelects);
            }
        }

        return treeSelect;
    }

    /**
     * 根据菜单类型过滤树形结构
     *
     * @param permissions 原始树形列表
     * @param menuTypes 允许的菜单类型（M-目录，C-菜单，F-按钮）
     * @return 过滤后的树形列表
     */
    public static List<TreeSelect> buildTreeSelectByMenuType(List<SmartPermission> permissions, String... menuTypes) {
        if (CollectionUtil.isEmpty(permissions)) {
            return new ArrayList<>();
        }

        return permissions.stream()
                .map(permission -> convertToTreeSelectByMenuType(permission, menuTypes))
                .filter(treeSelect -> treeSelect != null)
                .collect(Collectors.toList());
    }

    /**
     * 转换单个节点，根据菜单类型过滤
     *
     * @param permission SmartPermission对象
     * @param menuTypes 允许的菜单类型
     * @return TreeSelect对象
     */
    private static TreeSelect convertToTreeSelectByMenuType(SmartPermission permission, String... menuTypes) {
        if (permission == null) {
            return null;
        }
        
        // 检查当前节点是否在允许的类型中
        boolean isAllowed = false;
        if (menuTypes != null && menuTypes.length > 0) {
            for (String menuType : menuTypes) {
                if (menuType.equals(permission.getMenuType())) {
                    isAllowed = true;
                    break;
                }
            }
        } else {
            isAllowed = true; // 如果没有指定类型，则允许所有
        }
        
        if (!isAllowed) {
            return null;
        }
        
        TreeSelect treeSelect = new TreeSelect();
        treeSelect.setId(permission.getId());
        treeSelect.setLabel(permission.getMenuName());
        treeSelect.setDisabled(isDisabled(permission));
        
        // 处理子节点
        List<? extends com.smart.framework.common.util.MenuTreeUtil.MenuPermission> children = permission.getChildren();
        if (CollectionUtil.isNotEmpty(children)) {
            List<SmartPermission> smartChildren = children.stream()
                    .filter(child -> child instanceof SmartPermission)
                    .map(child -> (SmartPermission) child)
                    .collect(Collectors.toList());
            
            List<TreeSelect> childTreeSelects = buildTreeSelectByMenuType(smartChildren, menuTypes);
            if (CollectionUtil.isNotEmpty(childTreeSelects)) {
                treeSelect.setChildren(childTreeSelects);
            }
        }
        
        return treeSelect;
    }
}