package com.smart.system.org.service.impl;

import com.smart.framework.common.util.OrgTreeUtil;
import com.smart.system.org.entity.SmartOrg;
import com.smart.system.permission.vo.TreeSelect;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 组织树形选择器工具类
 * 用于将SmartOrg树形结构转换为TreeSelect结构
 * 
 * ## 基本用法
 * List<SmartOrg> orgTree = OrgTreeUtil.buildOrgTree(orgs);
 * List<TreeSelect> treeSelectList = SmartOrgConvertTreeSelectService.buildTreeSelect(orgTree);
 *
 * ## 排除指定ID的用法（比如编辑时排除自己）
 * List<TreeSelect> treeSelectList = SmartOrgConvertTreeSelectService.buildTreeSelectExclude(orgTree, currentId);
 *
 * ## 只显示特定类型的组织
 * List<TreeSelect> treeSelectList = SmartOrgConvertTreeSelectService.buildTreeSelectByOrgType(orgTree, "1", "2");
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public class SmartOrgConvertTreeSelectService {

    /**
     * 将SmartOrg树形结构转换为TreeSelect结构
     * 
     * @param orgs SmartOrg树形列表
     * @return TreeSelect树形列表
     */
    public static List<TreeSelect> buildTreeSelect(List<SmartOrg> orgs) {
        if (CollectionUtils.isEmpty(orgs)) {
            return new ArrayList<>();
        }

        return orgs.stream()
                .map(SmartOrgConvertTreeSelectService::convertToTreeSelect)
                .collect(Collectors.toList());
    }

    /**
     * 将单个SmartOrg转换为TreeSelect
     *
     * @param org SmartOrg对象
     * @return TreeSelect对象
     */
    private static TreeSelect convertToTreeSelect(SmartOrg org) {
        if (org == null) {
            return null;
        }

        TreeSelect treeSelect = new TreeSelect();

        // 设置基本信息
        treeSelect.setId(org.getId());
        treeSelect.setLabel(org.getOrgName());

        // 设置禁用状态（可以根据业务需求调整）
        treeSelect.setDisabled(isDisabled(org));

        // 处理子节点
        List<? extends OrgTreeUtil.OrgPermission> children = org.getChildren();
        if (!CollectionUtils.isEmpty(children)) {
            List<SmartOrg> smartChildren = children.stream()
                    .filter(child -> child instanceof SmartOrg)
                    .map(child -> (SmartOrg) child)
                    .collect(Collectors.toList());

            List<TreeSelect> childTreeSelects = buildTreeSelect(smartChildren);
            treeSelect.setChildren(childTreeSelects);
        }

        return treeSelect;
    }

    /**
     * 判断组织节点是否禁用
     * 可以根据业务需求自定义禁用逻辑
     *
     * @param org SmartOrg对象
     * @return 是否禁用
     */
    private static boolean isDisabled(SmartOrg org) {
        if (org == null) {
            return true;
        }
        
        // 示例：停用的组织禁用
        if ("1".equals(org.getStatus())) {
            return true;
        }
        
        // 示例：删除的组织禁用
        if ("2".equals(org.getDelFlag())) {
            return true;
        }
        
        return false;
    }

    /**
     * 将SmartOrg树形结构转换为TreeSelect结构，排除指定ID的组织
     * 
     * @param orgs SmartOrg树形列表
     * @param excludeId 要排除的组织ID
     * @return TreeSelect树形列表
     */
    public static List<TreeSelect> buildTreeSelectExclude(List<SmartOrg> orgs, String excludeId) {
        if (CollectionUtils.isEmpty(orgs)) {
            return new ArrayList<>();
        }

        return orgs.stream()
                .map(org -> convertToTreeSelectExclude(org, excludeId))
                .filter(treeSelect -> treeSelect != null)
                .collect(Collectors.toList());
    }

    /**
     * 转换单个节点，排除指定ID
     *
     * @param org SmartOrg对象
     * @param excludeId 要排除的组织ID
     * @return TreeSelect对象
     */
    private static TreeSelect convertToTreeSelectExclude(SmartOrg org, String excludeId) {
        if (org == null) {
            return null;
        }
        
        // 如果当前节点是要排除的节点，返回null
        if (excludeId != null && excludeId.equals(org.getId())) {
            return null;
        }
        
        TreeSelect treeSelect = new TreeSelect();
        treeSelect.setId(org.getId());
        treeSelect.setLabel(org.getOrgName());
        treeSelect.setDisabled(isDisabled(org));
        
        // 处理子节点
        List<? extends OrgTreeUtil.OrgPermission> children = org.getChildren();
        if (!CollectionUtils.isEmpty(children)) {
            List<SmartOrg> smartChildren = children.stream()
                    .filter(child -> child instanceof SmartOrg)
                    .map(child -> (SmartOrg) child)
                    .collect(Collectors.toList());
            
            List<TreeSelect> childTreeSelects = buildTreeSelectExclude(smartChildren, excludeId);
            if (!CollectionUtils.isEmpty(childTreeSelects)) {
                treeSelect.setChildren(childTreeSelects);
            }
        }
        
        return treeSelect;
    }

    /**
     * 将SmartOrg树形结构转换为TreeSelect结构，只显示指定类型的组织
     * 
     * @param orgs SmartOrg树形列表
     * @param orgTypes 允许的组织类型
     * @return TreeSelect树形列表
     */
    public static List<TreeSelect> buildTreeSelectByOrgType(List<SmartOrg> orgs, String... orgTypes) {
        if (CollectionUtils.isEmpty(orgs)) {
            return new ArrayList<>();
        }

        return orgs.stream()
                .map(org -> convertToTreeSelectByOrgType(org, orgTypes))
                .filter(treeSelect -> treeSelect != null)
                .collect(Collectors.toList());
    }

    /**
     * 转换单个节点，根据组织类型过滤
     *
     * @param org SmartOrg对象
     * @param orgTypes 允许的组织类型
     * @return TreeSelect对象
     */
    private static TreeSelect convertToTreeSelectByOrgType(SmartOrg org, String... orgTypes) {
        if (org == null) {
            return null;
        }
        
        // 检查当前节点是否在允许的类型中
        boolean isAllowed = false;
        if (orgTypes != null && orgTypes.length > 0) {
            for (String orgType : orgTypes) {
                if (orgType.equals(org.getOrgType())) {
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
        treeSelect.setId(org.getId());
        treeSelect.setLabel(org.getOrgName());
        treeSelect.setDisabled(isDisabled(org));
        
        // 处理子节点
        List<? extends OrgTreeUtil.OrgPermission> children = org.getChildren();
        if (!CollectionUtils.isEmpty(children)) {
            List<SmartOrg> smartChildren = children.stream()
                    .filter(child -> child instanceof SmartOrg)
                    .map(child -> (SmartOrg) child)
                    .collect(Collectors.toList());
            
            List<TreeSelect> childTreeSelects = buildTreeSelectByOrgType(smartChildren, orgTypes);
            if (!CollectionUtils.isEmpty(childTreeSelects)) {
                treeSelect.setChildren(childTreeSelects);
            }
        }
        
        return treeSelect;
    }

    /**
     * 将SmartOrg树形结构转换为TreeSelect结构，只显示指定租户的组织
     * 
     * @param orgs SmartOrg树形列表
     * @param tenantId 租户ID
     * @return TreeSelect树形列表
     */
    public static List<TreeSelect> buildTreeSelectByTenant(List<SmartOrg> orgs, String tenantId) {
        if (CollectionUtils.isEmpty(orgs)) {
            return new ArrayList<>();
        }

        return orgs.stream()
                .map(org -> convertToTreeSelectByTenant(org, tenantId))
                .filter(treeSelect -> treeSelect != null)
                .collect(Collectors.toList());
    }

    /**
     * 转换单个节点，根据租户ID过滤
     *
     * @param org SmartOrg对象
     * @param tenantId 租户ID
     * @return TreeSelect对象
     */
    private static TreeSelect convertToTreeSelectByTenant(SmartOrg org, String tenantId) {
        if (org == null) {
            return null;
        }
        
        // 如果指定了租户ID，检查当前节点是否属于该租户
        if (tenantId != null && !tenantId.isEmpty()) {
            if (!tenantId.equals(org.getTenantId())) {
                return null;
            }
        }
        
        TreeSelect treeSelect = new TreeSelect();
        treeSelect.setId(org.getId());
        treeSelect.setLabel(org.getOrgName());
        treeSelect.setDisabled(isDisabled(org));
        
        // 处理子节点
        List<? extends OrgTreeUtil.OrgPermission> children = org.getChildren();
        if (!CollectionUtils.isEmpty(children)) {
            List<SmartOrg> smartChildren = children.stream()
                    .filter(child -> child instanceof SmartOrg)
                    .map(child -> (SmartOrg) child)
                    .collect(Collectors.toList());
            
            List<TreeSelect> childTreeSelects = buildTreeSelectByTenant(smartChildren, tenantId);
            if (!CollectionUtils.isEmpty(childTreeSelects)) {
                treeSelect.setChildren(childTreeSelects);
            }
        }
        
        return treeSelect;
    }
}
