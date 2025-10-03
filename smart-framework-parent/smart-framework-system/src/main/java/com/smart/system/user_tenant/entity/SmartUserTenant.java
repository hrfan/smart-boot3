package com.smart.system.user_tenant.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.framework.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户租户中间表实体类
 * 映射smart_user_tenant表结构
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("smart_user_tenant")
public class SmartUserTenant extends BaseEntity {

    /**
     * 用户id
     */
    @TableField("user_id")
    private String userId;

    /**
     * 租户id
     */
    @TableField("tenant_id")
    private String tenantId;
}
