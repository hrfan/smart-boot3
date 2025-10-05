package com.smart.system.permission.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.framework.common.util.MenuTreeUtil;
import com.smart.framework.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 菜单权限表实体类
 * 映射smart_permission表结构
 * 
 * @author Smart Boot3
 * @since 1极.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("smart_permission")
public class SmartPermission extends BaseEntity implements MenuTreeUtil.MenuPermission {



    /**
     * 菜单名称
     * 必填字段
     */
    @TableField("menu_name")
    private String menuName;

    /**
     * 父菜单ID
     * 默认值：0
     */
    @TableField("parent_id")
    private String parentId;

    /**
     * 显示顺序
     * 默认极值：0
     */
    @TableField("order_num")
    private Integer orderNum;

    /**
     * 路由地址
     * 默认值：空字符串
     */
    @TableField("path")
    private String path;

    /**
     * 组件路径
     */
    @TableField("component")
    private String component;

    /**
     * 路由参数
     */
    @TableField("query")
    private String query;

    /**
     * 路由名称
     * 默认值：空字符串
     */
    @TableField("route_name")
    private String routeName;

    /**
     * 是否为外链（0是 1否）
     * 默认值：1
     */
    @TableField("is_frame")
    private String isFrame;

    /**
     * 外链地址（http://xxxxxx）
     * 默认值：空字符串
     */
    @TableField("frame_url")
    private String frameUrl;

    /**
     * 是否缓存（0缓存 1不缓存）
     * 默认值：0
     */
    @TableField("is_cache")
    private String isCache;

    /**
     * 菜单类型（M目录 C菜单 F按钮）
     * 默认值：空字符串
     */
    @TableField("menu_type")
    private String menuType;

    /**
     * 是否显示（0显示 1隐藏）
     * 默认值：0
     */
    @TableField("visible")
    private String visible;

    /**
     * 菜单状态（0正常 1停用）
     * 默认值：0
     */
    @TableField("status")
    private String status;

    /**
     * 权限标识
     */
    @TableField("perms")
    private String perms;

    /**
     * 菜单图标
     * 默认值：#
     */
    @TableField("icon")
    private String icon;

    /**
     * 创建者
     * 默认值：空字符串
     */
    @TableField("create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 创建者姓名
     */
    @TableField("create_user_name")
    private String createUserName;

    /**
     * 更新者
     * 默认值：空字符串
     */
    @TableField("update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 更新者姓名
     */
    @TableField("update_user_name")
    private String updateUserName;

    /**
     * 备注
     * 默认值：空字符串
     */
    @TableField("remark")
    private String remark;

    /**
     * 是否叶子节点
     */
    @TableField("is_leaf")
    private Boolean isLeaf;

    /**
     * 是否总是显示
     */
    @TableField("always_show")
    private Boolean alwaysShow;

    /**
     * 颜色
     */
    @TableField("color")
    private String color;

    /**
     * 默认快捷菜单(是：1,否：0)
     */
    @TableField("default_shortcut_menu")
    private String defaultShortcutMenu;

    /**
     * 提醒菜单(是：1,否：0)
     */
    @TableField("reminder_menu")
    private String reminderMenu;

    /**
     * 租户管理员菜单(是：1,否：0)
     * 默认值：0
     */
    @TableField("tenant_menu")
    private String tenantMenu;

    /**
     * 允许游客访问：1允许，0不允许
     * 默认值：0
     */
    @TableField("allow_visitor_access")
    private String allowVisitorAccess;

    /**
     * 子菜单列表（非数据库字段）
     */
    @TableField(exist = false)
    private List<SmartPermission> children;

    // 实现MenuTreeUtil.MenuPermission接口的方法
    @Override
    public List<? extends MenuTreeUtil.MenuPermission> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<? extends MenuTreeUtil.MenuPermission> children) {
        this.children = (List<SmartPermission>) children;
    }


    /**
     * 用户ID
     */
    @TableField(exist = false)
    private String userId;
}
