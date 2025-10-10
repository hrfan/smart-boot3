package com.smart.system.org.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.smart.framework.common.util.OrgTreeUtil;
import com.smart.framework.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 组织信息表实体类
 * 映射smart_org表结构
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("smart_org")
public class SmartOrg extends BaseEntity implements OrgTreeUtil.OrgPermission{



    /**
     * 父组织ID
     */
    @TableField("parent_id")
    private String parentId;

    /**
     * 父组织路径
     */
    @TableField("path")
    private String path;

    /**
     * 子节点数量
     */
    @TableField("sub_org_num")
    private Integer subOrgNum;

    /**
     * 组织名称
     */
    @TableField("org_name")
    private String orgName;

    /**
     * 组织编码
     */
    @TableField("org_code")
    private String orgCode;

    /**
     * 组织缩写
     */
    @TableField("org_name_abbr")
    private String orgNameAbbr;

    /**
     * 组织排序
     */
    @TableField("org_order")
    private Integer orgOrder;

    /**
     * 组织类型 1组织 2企业
     */
    @TableField("org_type")
    private String orgType;

    /**
     * 负责人
     */
    @TableField("leader")
    private String leader;

    /**
     * 联系电话
     */
    @TableField("phone")
    private String phone;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 部门状态（0正常 1停用）
     */
    @TableField("status")
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableField("del_flag")
    private String delFlag;

    /**
     * 所属部门
     */
    @TableField("sys_org_code")
    private String sysOrgCode;

    /**
     * 企业编码
     */
    @TableField("comp_code")
    private String compCode;

    /**
     * 统一社会信用代码
     */
    @TableField("social_credit_code")
    private String socialCreditCode;

    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private String tenantId;

    /**
     * 是否启用IP白名单(0:不启用 1:启用)
     */
    @TableField("ip_white")
    private String ipWhite;

    /**
     * ip白名单
     */
    @TableField("ip_address")
    private String ipAddress;

    /**
     * 是否密码强制过期(0:不过期 1:过期)
     */
    @TableField("enable_password_expiration")
    private String enablePasswordExpiration;

    /**
     * 密码过期时间
     */
    @TableField("password_expired_day")
    private String passwordExpiredDay;

    /**
     * 密码过期提醒时间
     */
    @TableField("password_expired_alert_day")
    private String passwordExpiredAlertDay;

    /**
     * 启用锁定账户功能(0:不启用 1：启用)
     */
    @TableField("lock_user")
    private String lockUser;

    /**
     * 启用密码黑名单(0:不启用 1：启用)
     */
    @TableField("lock_black_password")
    private String lockBlackPassword;

    /**
     * 账号多次登录(0:默认 1:禁止 2：允许)
     */
    @TableField("multiple_login")
    private String multipleLogin;

    /**
     * 大屏地址
     */
    @TableField("large_screen_address")
    private String largeScreenAddress;

    /**
     * 首页地址
     */
    @TableField("home_page_address")
    private String homePageAddress;

    /**
     * 是否"显示收费公告"： 1显示，0不显示（默认值）
     */
    @TableField("charge_notice")
    private String chargeNotice;

    /**
     * 是否强制修改密码
     */
    @TableField("force_change_pass")
    private String forceChangePass;

    /**
     * 是否显示水印
     */
    @TableField("show_water_mark")
    private String showWaterMark;

    /**
     * 工厂代码
     */
    @TableField("factory_code")
    private String factoryCode;

    /**
     * 是否启用客服，N：不启用;Y：启用
     */
    @TableField("enable_customer")
    private String enableCustomer;

    /**
     * 租户页面编码
     */
    @TableField("portlet_code")
    private String portletCode;

    /**
     * 创建者
     */
    @TableField("create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 创建人姓名
     */
    @TableField("create_user_name")
    private String createUserName;

    /**
     * 更新者
     */
    @TableField("update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 更新人姓名
     */
    @TableField("update_user_name")
    private String updateUserName;

    /**
     * 子组织列表
     */
    @TableField(exist = false)
    private List<? extends OrgTreeUtil.OrgPermission> children = new ArrayList<>();

}
