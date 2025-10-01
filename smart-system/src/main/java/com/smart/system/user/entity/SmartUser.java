package com.smart.system.user.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.common.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 用户信息表实体类
 * 映射smart_user表结构
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("smart_user")
public class SmartUser extends BaseEntity {



    /**
     * 部门ID
     */
    @TableField("dept_id")
    private String deptId;

    /**
     * 用户账号
     * 必填字段，用于登录
     */
    @TableField("user_name")
    private String userName;

    /**
     * 用户昵称
     * 必填字段，用于显示
     */
    @TableField("nick_name")
    private String nickName;

    /**
     * 用户类型（00系统用户）
     * 默认值：00
     */
    @TableField("user_type")
    private String userType;

    /**
     * 用户邮箱
     * 默认值：空字符串
     */
    @TableField("email")
    private String email;

    /**
     * 手机号码
     * 默认值：空字符串
     */
    @TableField("phonenumber")
    private String phonenumber;

    /**
     * 用户性别（0男 1女 2未知）
     * 默认值：0
     */
    @TableField("sex")
    private String sex;

    /**
     * 头像地址
     * 默认值：空字符串
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 密码
     * 默认值：空字符串
     */
    @TableField("password")
    private String password;

    /**
     * 账号状态（0正常 1停用）
     * 默认值：0
     */
    @TableField("status")
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     * 默认值：0
     */
    @TableField("del_flag")
    private String delFlag;

    /**
     * 最后登录IP
     * 默认值：空字符串
     */
    @TableField("login_ip")
    private String loginIp;

    /**
     * 最后登录时间
     * timestamp类型，使用Date
     */
    @TableField("login_date")
    private Date loginDate;

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


}
