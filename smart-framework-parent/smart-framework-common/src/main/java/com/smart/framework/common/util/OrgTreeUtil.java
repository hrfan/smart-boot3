package com.smart.framework.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 组织树形组装工具类
 * 将扁平的组织列表转换为树形结构
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public class OrgTreeUtil {

    private static final Logger log = LoggerFactory.getLogger(OrgTreeUtil.class);

    /**
     * 根节点ID
     */
    private static final String ROOT_NODE_ID = "0";

    /**
     * 组织权限接口，用于泛型支持
     */
    public interface OrgPermission {
        String getId();
        String getParentId();
        Integer getOrgOrder();
        String getOrgName();
        String getPath();
        String getOrgCode();
        String getOrgType();
        String getStatus();
        String getDelFlag();
        List<? extends OrgPermission> getChildren();
        void setChildren(List<? extends OrgPermission> children);
    }

    /**
     * 将扁平组织列表转换为树形结构
     * 
     * @param orgList 扁平组织列表
     * @param <T> 组织权限类型
     * @return 树形组织列表
     */
    @SuppressWarnings("unchecked")
    public static <T extends OrgPermission> List<T> buildOrgTree(List<T> orgList) {
        if (CollectionUtils.isEmpty(orgList)) {
            log.warn("组织列表为空，无法构建树形结构");
            return new ArrayList<>();
        }

        log.debug("开始构建组织树形结构，原始组织数量：{}", orgList.size());

        // 1. 创建组织ID到组织对象的映射
        Map<String, T> orgMap = orgList.stream()
                .collect(Collectors.toMap(OrgPermission::getId, org -> org));

        // 2. 构建树形结构
        List<T> rootOrgs = new ArrayList<>();
        
        for (T org : orgList) {
            String parentId = org.getParentId();
            
            // 如果是根节点或父节点为空，添加到根节点列表
            if (parentId == null || parentId.isEmpty() || ROOT_NODE_ID.equals(parentId)) {
                rootOrgs.add(org);
            } else {
                // 查找父节点并添加到父节点的子节点列表
                T parentOrg = orgMap.get(parentId);
                if (parentOrg != null) {
                    List<? extends OrgPermission> children = parentOrg.getChildren();
                    if (children == null) {
                        parentOrg.setChildren(new ArrayList<>());
                        children = parentOrg.getChildren();
                    }
                    ((List<T>) children).add(org);
                } else {
                    log.warn("未找到父组织，组织ID：{}，父组织ID：{}", org.getId(), parentId);
                    // 如果找不到父节点，将其作为根节点处理
                    rootOrgs.add(org);
                }
            }
        }

        // 3. 对每个层级的组织进行排序
        sortOrgs((List<OrgPermission>) rootOrgs);

        log.debug("组织树形结构构建完成，根节点数量：{}", rootOrgs.size());
        return rootOrgs;
    }

    /**
     * 递归排序组织列表
     * 
     * @param orgs 组织列表
     */
    @SuppressWarnings("unchecked")
    private static void sortOrgs(List<OrgPermission> orgs) {
        if (CollectionUtils.isEmpty(orgs)) {
            return;
        }

        // 按orgOrder排序
        orgs.sort(Comparator.comparing(org -> 
            org.getOrgOrder() != null ? org.getOrgOrder() : 0));

        // 递归排序子组织
        for (OrgPermission org : orgs) {
            if (!CollectionUtils.isEmpty(org.getChildren())) {
                sortOrgs((List<OrgPermission>) org.getChildren());
            }
        }
    }

    /**
     * 从树形组织中提取所有组织ID
     * 
     * @param orgTree 树形组织
     * @param <T> 组织权限类型
     * @return 所有组织ID列表
     */
    @SuppressWarnings("unchecked")
    public static <T extends OrgPermission> List<String> extractOrgIds(List<T> orgTree) {
        List<String> orgIds = new ArrayList<>();
        if (CollectionUtils.isEmpty(orgTree)) {
            return orgIds;
        }

        for (T org : orgTree) {
            orgIds.add(org.getId());
            if (!CollectionUtils.isEmpty(org.getChildren())) {
                orgIds.addAll(extractOrgIds((List<T>) org.getChildren()));
            }
        }

        return orgIds;
    }

    /**
     * 从树形组织中查找指定ID的组织
     * 
     * @param orgTree 树形组织
     * @param orgId 组织ID
     * @param <T> 组织权限类型
     * @return 找到的组织，未找到返回null
     */
    @SuppressWarnings("unchecked")
    public static <T extends OrgPermission> T findOrgById(List<T> orgTree, String orgId) {
        if (CollectionUtils.isEmpty(orgTree) || orgId == null) {
            return null;
        }

        for (T org : orgTree) {
            if (orgId.equals(org.getId())) {
                return org;
            }
            
            if (!CollectionUtils.isEmpty(org.getChildren())) {
                T found = findOrgById((List<T>) org.getChildren(), orgId);
                if (found != null) {
                    return found;
                }
            }
        }

        return null;
    }

    /**
     * 获取树形组织的深度
     * 
     * @param orgTree 树形组织
     * @param <T> 组织权限类型
     * @return 最大深度
     */
    @SuppressWarnings("unchecked")
    public static <T extends OrgPermission> int getMaxDepth(List<T> orgTree) {
        if (CollectionUtils.isEmpty(orgTree)) {
            return 0;
        }

        int maxDepth = 0;
        for (T org : orgTree) {
            int depth = 1;
            if (!CollectionUtils.isEmpty(org.getChildren())) {
                depth += getMaxDepth((List<T>) org.getChildren());
            }
            maxDepth = Math.max(maxDepth, depth);
        }

        return maxDepth;
    }

    /**
     * 打印树形组织结构（用于调试）
     * 
     * @param orgTree 树形组织
     * @param <T> 组织权限类型
     */
    public static <T extends OrgPermission> void printOrgTree(List<T> orgTree) {
        printOrgTree(orgTree, 0);
    }

    /**
     * 递归打印树形组织结构
     * 
     * @param orgTree 树形组织
     * @param level 层级
     * @param <T> 组织权限类型
     */
    @SuppressWarnings("unchecked")
    private static <T extends OrgPermission> void printOrgTree(List<T> orgTree, int level) {
        if (CollectionUtils.isEmpty(orgTree)) {
            return;
        }

        String indent = "  ".repeat(level);
        for (T org : orgTree) {
            log.info("{}{} - {} ({})", indent, org.getOrgName(), org.getPath(), org.getId());
            if (!CollectionUtils.isEmpty(org.getChildren())) {
                printOrgTree((List<T>) org.getChildren(), level + 1);
            }
        }
    }

    /**
     * 检查组织是否为叶子节点
     * 
     * @param org 组织
     * @return 是否为叶子节点
     */
    public static boolean isLeafNode(OrgPermission org) {
        return CollectionUtils.isEmpty(org.getChildren());
    }

    /**
     * 获取组织的所有父级组织ID
     * 
     * @param orgTree 树形组织
     * @param orgId 组织ID
     * @param <T> 组织权限类型
     * @return 父级组织ID列表
     */
    public static <T extends OrgPermission> List<String> getParentOrgIds(List<T> orgTree, String orgId) {
        List<String> parentIds = new ArrayList<>();
        findParentOrgIds(orgTree, orgId, parentIds);
        return parentIds;
    }

    /**
     * 递归查找父级组织ID
     * 
     * @param orgTree 树形组织
     * @param targetOrgId 目标组织ID
     * @param parentIds 父级组织ID列表
     * @param <T> 组织权限类型
     * @return 是否找到
     */
    @SuppressWarnings("unchecked")
    private static <T extends OrgPermission> boolean findParentOrgIds(List<T> orgTree, String targetOrgId, List<String> parentIds) {
        if (CollectionUtils.isEmpty(orgTree)) {
            return false;
        }

        for (T org : orgTree) {
            if (targetOrgId.equals(org.getId())) {
                return true;
            }
            
            if (!CollectionUtils.isEmpty(org.getChildren())) {
                parentIds.add(org.getId());
                if (findParentOrgIds((List<T>) org.getChildren(), targetOrgId, parentIds)) {
                    return true;
                }
                parentIds.remove(parentIds.size() - 1);
            }
        }

        return false;
    }
}
