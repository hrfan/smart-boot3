package com.smart.system.role_permission.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.common.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色权限中间表实体类
 * 映射smart_role_permission表结构
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("smart_role_permission")
public class SmartRolePermission extends BaseEntity {

    /**
     * 角色id
     * 必填字段
     */
    @TableField("role_id")
    private String roleId;

    /**
     * 权限id
     * 必填字段
     */
    @TableField("permission_id")
    private String permissionId;
}
