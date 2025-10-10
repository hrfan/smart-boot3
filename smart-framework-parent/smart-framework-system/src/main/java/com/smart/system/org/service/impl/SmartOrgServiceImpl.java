package com.smart.system.org.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.framework.common.util.OrgTreeUtil;
import com.smart.system.org.entity.SmartOrg;
import com.smart.system.org.mapper.SmartOrgMapper;
import com.smart.system.org.service.SmartOrgService;
import com.smart.system.permission.vo.TreeSelect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 组织信息表Service实现类
 * 实现组织业务逻辑层功能
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Service
public class SmartOrgServiceImpl extends ServiceImpl<SmartOrgMapper, SmartOrg> implements SmartOrgService {

    private static final Logger log = LoggerFactory.getLogger(SmartOrgServiceImpl.class);
    private final SmartOrgMapper smartOrgMapper;

    public SmartOrgServiceImpl(SmartOrgMapper smartOrgMapper) {
        this.smartOrgMapper = smartOrgMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SmartOrg insert(SmartOrg smartOrg) {
        log.info("新增组织信息: {}", smartOrg.getOrgName());
        boolean b = saveOrUpdate(smartOrg);
        if (b) {
            return smartOrg;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SmartOrg update(SmartOrg smartOrg) {
        log.info("更新组织信息: {}", smartOrg.getOrgName());
        boolean b = updateById(smartOrg);
        if (b) {
            return smartOrg;
        }
        return null;
    }





    /**
     * 获取组织树形选择器列表
     * @return TreeSelect格式的组织树
     */
    @Override
    public List<TreeSelect> getOrgTreeSelect(SmartOrg smartOrg) {
        log.info("获取组织树形选择器列表");

        // 查询所有有效组织
        LambdaQueryWrapper<SmartOrg> queryWrapper = new LambdaQueryWrapper<SmartOrg>()
                .eq(SmartOrg::getStatus, "0")
                .eq(SmartOrg::getDelFlag, "0")
                .orderByAsc(SmartOrg::getOrgOrder);
        List<SmartOrg> allOrgList = smartOrgMapper.selectList(queryWrapper);

        if (CollectionUtil.isEmpty(allOrgList)) {
            return List.of();
        }

        // 如果没有查询条件，返回所有组织树
        if (smartOrg == null || smartOrg.getOrgName() == null || smartOrg.getOrgName().trim().isEmpty()) {
            List<SmartOrg> orgTree = OrgTreeUtil.buildOrgTree(allOrgList);
            return SmartOrgConvertTreeSelectService.buildTreeSelect(orgTree);
        }

        // 根据条件查询匹配的组织
        LambdaQueryWrapper<SmartOrg> searchWrapper = new LambdaQueryWrapper<SmartOrg>()
                .like(SmartOrg::getOrgName, smartOrg.getOrgName())
                .eq(SmartOrg::getStatus, "0")
                .eq(SmartOrg::getDelFlag, "0");
        List<SmartOrg> matchedOrgList = smartOrgMapper.selectList(searchWrapper);

        if (CollectionUtil.isEmpty(matchedOrgList)) {
            return List.of();
        }

        // 获取需要展示的组织ID集合
        List<String> orgIdsToShow = getOrgIdsToShow(allOrgList, matchedOrgList);

        // 过滤出需要展示的组织
        List<SmartOrg> filteredOrgList = allOrgList.stream()
                .filter(org -> orgIdsToShow.contains(org.getId()))
                .collect(java.util.stream.Collectors.toList());

        // 构建组织树
        List<SmartOrg> orgTree = OrgTreeUtil.buildOrgTree(filteredOrgList);

        // 转换为TreeSelect格式
        return SmartOrgConvertTreeSelectService.buildTreeSelect(orgTree);
    }
    
    /**
     * 获取需要展示的组织ID集合
     * 规则：
     * 1. 如果匹配的组织有子节点，则包含所有子节点
     * 2. 如果匹配的组织是叶子节点，则包含所有父级节点
     * 
     * @param allOrgList 所有组织列表
     * @param matchedOrgList 匹配的组织列表
     * @return 需要展示的组织ID集合
     */
    private List<String> getOrgIdsToShow(List<SmartOrg> allOrgList, List<SmartOrg> matchedOrgList) {
        List<String> orgIdsToShow = new java.util.ArrayList<>();
        
        // 创建组织ID到组织的映射
        java.util.Map<String, SmartOrg> orgMap = allOrgList.stream()
                .collect(java.util.stream.Collectors.toMap(SmartOrg::getId, org -> org));
        
        // 创建父ID到子组织列表的映射
        java.util.Map<String, List<SmartOrg>> parentChildrenMap = allOrgList.stream()
                .filter(org -> org.getParentId() != null && !org.getParentId().isEmpty())
                .collect(java.util.stream.Collectors.groupingBy(SmartOrg::getParentId));
        
        for (SmartOrg matchedOrg : matchedOrgList) {
            orgIdsToShow.add(matchedOrg.getId());
            
            // 检查是否有子节点
            List<SmartOrg> children = parentChildrenMap.get(matchedOrg.getId());
            if (CollectionUtil.isNotEmpty(children)) {
                // 如果有子节点，添加所有子节点（递归）
                addAllChildren(orgIdsToShow, children, parentChildrenMap);
            } else {
                // 如果是叶子节点，添加所有父级节点
                addAllParents(orgIdsToShow, matchedOrg, orgMap);
            }
        }
        
        return orgIdsToShow;
    }
    
    /**
     * 递归添加所有子节点
     * 
     * @param orgIdsToShow 需要展示的组织ID集合
     * @param children 子组织列表
     * @param parentChildrenMap 父ID到子组织列表的映射
     */
    private void addAllChildren(List<String> orgIdsToShow, List<SmartOrg> children, 
                               java.util.Map<String, List<SmartOrg>> parentChildrenMap) {
        for (SmartOrg child : children) {
            orgIdsToShow.add(child.getId());
            
            // 递归添加子节点的子节点
            List<SmartOrg> grandChildren = parentChildrenMap.get(child.getId());
            if (CollectionUtil.isNotEmpty(grandChildren)) {
                addAllChildren(orgIdsToShow, grandChildren, parentChildrenMap);
            }
        }
    }
    
    /**
     * 递归添加所有父级节点
     * 
     * @param orgIdsToShow 需要展示的组织ID集合
     * @param org 当前组织
     * @param orgMap 组织ID到组织的映射
     */
    private void addAllParents(List<String> orgIdsToShow, SmartOrg org, 
                              java.util.Map<String, SmartOrg> orgMap) {
        if (org.getParentId() != null && !org.getParentId().isEmpty() && !"0".equals(org.getParentId())) {
            SmartOrg parent = orgMap.get(org.getParentId());
            if (parent != null) {
                orgIdsToShow.add(parent.getId());
                // 递归添加父级的父级
                addAllParents(orgIdsToShow, parent, orgMap);
            }
        }
    }






}
