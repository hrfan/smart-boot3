package com.smart.system.role.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.system.role.entiy.SmartRole;
import com.smart.system.role.mapper.SmartRoleMapper;
import com.smart.system.role.service.SmartRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 角色信息表Service实现类
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Service
public class SmartRoleServiceImpl extends ServiceImpl<SmartRoleMapper, SmartRole> implements SmartRoleService {

    private static final Logger log = LoggerFactory.getLogger(SmartRoleServiceImpl.class);

    @Autowired
    private SmartRoleMapper smartRoleMapper;

    /**
     * 根据角色名称查询角色信息
     * 
     * @param roleName 角色名称
     * @return 角色信息
     */
    @Override
    public SmartRole getByRoleName(String roleName) {
        log.debug("根据角色名称查询角色信息: {}", roleName);
        return smartRoleMapper.selectByRoleName(roleName);
    }

    /**
     * 根据角色权限字符串查询角色信息
     * 
     * @param roleKey 角色权限字符串
     * @return 角色信息
     */
    @Override
    public SmartRole getByRoleKey(String roleKey) {
        log.debug("根据角色权限字符串查询角色信息: {}", roleKey);
        return smartRoleMapper.selectByRoleKey(roleKey);
    }

    /**
     * 根据用户ID查询角色列表
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    @Override
    public List<SmartRole> getRolesByUserId(String userId) {
        log.debug("根据用户ID查询角色列表: {}", userId);
        return smartRoleMapper.selectRolesByUserId(userId);
    }

    /**
     * 查询所有启用的角色
     * 
     * @return 启用的角色列表
     */
    @Override
    public List<SmartRole> getEnabledRoles() {
        log.debug("查询所有启用的角色");
        return smartRoleMapper.selectEnabledRoles();
    }

    /**
     * 根据租户ID查询角色列表
     * 
     * @param tenantId 租户ID
     * @return 角色列表
     */
    @Override
    public List<SmartRole> getRolesByTenantId(String tenantId) {
        log.debug("根据租户ID查询角色列表: {}", tenantId);
        return smartRoleMapper.selectRolesByTenantId(tenantId);
    }

    /**
     * 检查角色名称是否存在
     * 
     * @param roleName 角色名称
     * @param excludeId 排除的角色ID
     * @return 是否存在
     */
    @Override
    public boolean checkRoleNameExists(String roleName, String excludeId) {
        log.debug("检查角色名称是否存在: {}, 排除ID: {}", roleName, excludeId);
        int count = smartRoleMapper.checkRoleNameExists(roleName, excludeId);
        return count > 0;
    }

    /**
     * 检查角色权限字符串是否存在
     * 
     * @param roleKey 角色权限字符串
     * @param excludeId 排除的角色ID
     * @return 是否存在
     */
    @Override
    public boolean checkRoleKeyExists(String roleKey, String excludeId) {
        log.debug("检查角色权限字符串是否存在: {}, 排除ID: {}", roleKey, excludeId);
        int count = smartRoleMapper.checkRoleKeyExists(roleKey, excludeId);
        return count > 0;
    }

    /**
     * 新增角色
     * 
     * @param role 角色信息
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertRole(SmartRole role) {
        log.info("新增角色: {}", role.getRoleName());
        
        // 检查角色名称是否已存在
        if (checkRoleNameExists(role.getRoleName(), null)) {
            log.warn("角色名称已存在: {}", role.getRoleName());
            throw new RuntimeException("角色名称已存在");
        }
        
        // 检查角色权限字符串是否已存在
        if (checkRoleKeyExists(role.getRoleKey(), null)) {
            log.warn("角色权限字符串已存在: {}", role.getRoleKey());
            throw new RuntimeException("角色权限字符串已存在");
        }
        
        // 设置默认值
        if (role.getStatus() == null) {
            role.setStatus("0"); // 默认正常
        }
        if (role.getDelFlag() == null) {
            role.setDelFlag("0"); // 默认未删除
        }
        if (role.getCreateTime() == null) {
            role.setCreateTime(new Date());
        }
        
        return save(role);
    }

    /**
     * 修改角色
     * 
     * @param role 角色信息
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRole(SmartRole role) {
        log.info("修改角色: {}", role.getRoleName());
        
        // 检查角色名称是否已存在（排除当前角色）
        if (checkRoleNameExists(role.getRoleName(), role.getId())) {
            log.warn("角色名称已存在: {}", role.getRoleName());
            throw new RuntimeException("角色名称已存在");
        }
        
        // 检查角色权限字符串是否已存在（排除当前角色）
        if (checkRoleKeyExists(role.getRoleKey(), role.getId())) {
            log.warn("角色权限字符串已存在: {}", role.getRoleKey());
            throw new RuntimeException("角色权限字符串已存在");
        }
        
        // 设置更新时间
        role.setUpdateTime(new Date());
        
        return updateById(role);
    }

    /**
     * 批量删除角色
     * 
     * @param roleIds 角色ID列表
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRolesByIds(List<String> roleIds) {
        log.info("批量删除角色: {}", roleIds);
        
        if (roleIds == null || roleIds.isEmpty()) {
            log.warn("角色ID列表为空");
            return false;
        }
        
        int count = smartRoleMapper.deleteRolesByIds(roleIds);
        log.info("成功删除 {} 个角色", count);
        
        return count > 0;
    }

    /**
     * 更新角色状态
     * 
     * @param roleId 角色ID
     * @param status 状态
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRoleStatus(String roleId, String status) {
        log.info("更新角色状态: {}, 状态: {}", roleId, status);
        
        int count = smartRoleMapper.updateRoleStatus(roleId, status);
        log.info("成功更新角色状态: {}", count > 0);
        
        return count > 0;
    }

    /**
     * 分配角色给用户
     * 
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignRolesToUser(String userId, List<String> roleIds) {
        log.info("分配角色给用户: {}, 角色列表: {}", userId, roleIds);
        
        // TODO: 实现用户角色分配逻辑
        // 这里需要操作用户角色关联表
        
        return true;
    }

    /**
     * 取消用户角色分配
     * 
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeRolesFromUser(String userId, List<String> roleIds) {
        log.info("取消用户角色分配: {}, 角色列表: {}", userId, roleIds);
        
        // TODO: 实现用户角色取消分配逻辑
        // 这里需要操作用户角色关联表
        
        return true;
    }
}
