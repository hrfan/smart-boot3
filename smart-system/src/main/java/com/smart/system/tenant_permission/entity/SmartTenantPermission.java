package com.smart.system.tenant_permission.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.common.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户权限中间表实体类
 * 映射smart_tenant_permission表结构
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("smart_tenant_permission")
public class SmartTenantPermission extends BaseEntity {

    /**
     * 租户id
     * 必填字段
     */
    @TableField("tenant_id")
    private String tenantId;

    /**
     * 权限id
     * 必填字段
     */
    @TableField("permission_id")
    private String permissionId;
}
