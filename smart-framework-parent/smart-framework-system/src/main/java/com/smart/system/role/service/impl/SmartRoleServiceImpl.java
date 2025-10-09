package com.smart.system.role.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.framework.common.util.SecurityUtils;
import com.smart.system.role.entiy.SmartRole;
import com.smart.system.role.mapper.SmartRoleMapper;
import com.smart.system.role.service.SmartRoleService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 角色信息表Service实现类
 * 实现角色业务逻辑层功能
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Service
public class SmartRoleServiceImpl extends ServiceImpl<SmartRoleMapper, SmartRole> implements SmartRoleService {

    private static final Logger log = LoggerFactory.getLogger(SmartRoleServiceImpl.class);
    private final SmartRoleMapper smartRoleMapper;

    public SmartRoleServiceImpl(SmartRoleMapper smartRoleMapper) {
        this.smartRoleMapper = smartRoleMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SmartRole insert(SmartRole smartRole) {
        // 从spring security获取当前登录用户ID
        String userId = SecurityUtils.getCurrentUserId();
        smartRole.setCreateBy(userId);
        smartRole.setCreateTime(new Date());
        smartRole.setId(UUID.randomUUID().toString());


        // 插入新的中间表信息
        List<String> permissionIds = smartRole.getPermissionIds();
        if (!CollectionUtils.isEmpty(permissionIds)) {
            smartRoleMapper.batchInsertRolePermission(smartRole.getId(), permissionIds);
        }


        boolean b = saveOrUpdate(smartRole);
        if (b) {
            return smartRole;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SmartRole update(SmartRole smartRole) {
        // 从spring security获取当前登录用户ID
        String userId = SecurityUtils.getCurrentUserId();
        smartRole.setUpdateBy(userId);
        smartRole.setUpdateTime(new Date());

        // 根据角色id删除 中间表权限信息
        smartRoleMapper.deletePermissionByRoleId(smartRole.getId());

        // 插入新的中间表信息
        List<String> permissionIds = smartRole.getPermissionIds();
        if (!CollectionUtils.isEmpty(permissionIds)) {
            smartRoleMapper.batchInsertRolePermission(smartRole.getId(), permissionIds);
        }


        boolean b = updateById(smartRole);
        if (b) {
            return smartRole;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteRoleById(String id) {
        // 根据角色id删除 中间表权限信息
        int i = smartRoleMapper.deletePermissionByRoleId(id);
        if (i > 0) {
            // 删除角色
            boolean b = removeById(id);
            if (b) {
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRoleByIds(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return false;
        }
        // 根据角色id删除 中间表权限信息
        boolean deleteFlag = removeByIds(ids);

        if (deleteFlag) {
            // 删除角色中间表
            int i = smartRoleMapper.deletePermissionByRoleIds(ids);
            log.info("删除角色中间表成功，删除{}条记录", i);
            return true;
        }
        return false;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changeStatus(SmartRole params) {
        if (params == null || params.getId() == null) {
            return false;
        }
        // 从spring security获取当前登录用户ID
        String username = SecurityUtils.getCurrentUsername();

        // UPDATE smart_role
        // SET
        //    status = ?,
        //    update_by = ?,
        //    update_user_name = ?,
        //    update_time = ?
        // WHERE id = ?;
        boolean update = lambdaUpdate().eq(SmartRole::getId, params.getId())
                .set(SmartRole::getStatus, params.getStatus())
                .set(SmartRole::getUpdateBy, username)
                .set(SmartRole::getUpdateUserName, username)
                .set(SmartRole::getUpdateTime, new Date())
                .update();
        return update;
    }
}
