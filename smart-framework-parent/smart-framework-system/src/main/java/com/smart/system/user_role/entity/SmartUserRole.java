package com.smart.system.user_role.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.framework.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户和角色关联表实体类
 * 映射smart_user_role表结构
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("smart_user_role")
public class SmartUserRole extends BaseEntity {

    /**
     * 用户ID
     * 必填字段
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 角色ID
     * 必填字段
     */
    @TableField("role_id")
    private Long roleId;
}
