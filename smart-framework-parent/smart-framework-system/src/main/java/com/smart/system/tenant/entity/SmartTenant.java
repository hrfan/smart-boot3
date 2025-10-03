package com.smart.system.tenant.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.framework.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 租户管理表实体类
 * 映射smart_tenant表结构
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("smart_tenant")
public class SmartTenant extends BaseEntity {

    /**
     * 租户编码
     * 必填字段
     */
    @TableField("tenant_code")
    private String tenantCode;

    /**
     * 租户名称
     */
    @TableField("tenant_name")
    private String tenantName;

    /**
     * 运维权限（1-有，2-无）
     * 必填字段
     */
    @TableField("operation_flag")
    private String operationFlag;

    /**
     * 创建人
     */
    @TableField("create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 创建人姓名
     */
    @TableField("create_user_name")
    private String createUserName;

    /**
     * 更新人
     */
    @TableField("update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 更新人姓名
     */
    @TableField("update_user_name")
    private String updateUserName;

    /**
     * 所属部门
     */
    @TableField("sys_org_code")
    private String sysOrgCode;

    /**
     * 登录网址
     */
    @TableField("login_url")
    private String loginUrl;

    /**
     * 最大用户数量
     */
    @TableField("max_users")
    private String maxUsers;

    /**
     * 是否启用事业部(0:不启用 1:启用)
     * 默认值：0
     */
    @TableField("enable_division")
    private String enableDivision;

    /**
     * 是否代理(0:否 1:是)
     * 默认值：0
     */
    @TableField("enable_proxy")
    private String enableProxy;

    /**
     * 是否启用忽略大小写(1:启用 0: 不启用)
     * 默认值：0
     */
    @TableField("enable_ignore_case")
    private String enableIgnoreCase;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 是否集团企业(0:否 1:是)
     */
    @TableField("is_corporate_tenant")
    private String isCorporateTenant;

    /**
     * 主要产品（关联租户可用菜单）
     */
    @TableField("main_menus")
    private String mainMenus;

    /**
     * 门户编码
     */
    @TableField("portlet_code")
    private String portletCode;
}
