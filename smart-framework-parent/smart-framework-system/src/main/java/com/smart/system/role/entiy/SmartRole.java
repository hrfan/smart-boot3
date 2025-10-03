package com.smart.system.role.entiy;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.framework.core.validation.annotation.SmartSize;
import com.smart.framework.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


/**
 * 角色信息表实体类
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("smart_role")
public class SmartRole extends BaseEntity {

    /**
     * 角色名称
     */
    @TableField("role_name")
    @SmartSize(min = 1, max = 100, message = "角色名称长度必须在 1 到 100 个字符之间")
    private String roleName;

    /**
     * 角色权限字符串
     */
    @TableField("role_key")
    private String roleKey;

    /**
     * 显示顺序
     */
    @TableField("role_sort")
    private Integer roleSort;

    /**
     * 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）
     */
    @TableField("data_scope")
    private String dataScope;

    /**
     * 菜单树选择项是否关联显示
     */
    @TableField("menu_check_strictly")
    private Boolean menuCheckStrictly;

    /**
     * 部门树选择项是否关联显示
     */
    @TableField("dept_check_strictly")
    private Boolean deptCheckStrictly;

    /**
     * 角色状态（0正常 1停用）
     */
    @TableField("status")
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableField("del_flag")
    private String delFlag;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;



    /**
     * 创建时间
     * 自动填充创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建人
     * 自动填充创建人
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建人姓名
     * 自动填充创建人姓名
     */
    @TableField(value = "create_user_name", fill = FieldFill.INSERT)
    private String createUserName;

    /**
     * 更新时间
     * 自动填充更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 更新人
     * 自动填充更新人
     */
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新人姓名
     * 自动填充更新人姓名
     */
    @TableField(value = "update_user_name", fill = FieldFill.INSERT_UPDATE)
    private String updateUserName;

    /**
     * 租户ID
     * 用于多租户场景
     */
     @TableField(value = "tenant_id")
    private String tenantId;

}