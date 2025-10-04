package com.smart.framework.security.dto;

import com.smart.framework.security.entity.AuthSmartPermission;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 用户登录响应DTO
 * 包含用户基本信息、角色、权限和菜单信息
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Data
public class UserLoginResponseDto {

    /**
     * 用户ID
     */
    private String id;

    /**
     * 部门ID
     */
    private String deptId;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户类型（00系统用户）
     */
    private String userType;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phonenumber;

    /**
     * 用户性别（0男 1女 2未知）
     */
    private String sex;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 账号状态（0正常 1停用）
     */
    private String status;

    /**
     * 最后登录IP
     */
    private String loginIp;

    /**
     * 最后登录时间
     */
    private Date loginDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * JWT令牌
     */
    private String token;

    /**
     * 令牌过期时间
     */
    private Long expireTime;

    /**
     * 用户角色列表
     */
    private List<String> roles;

    /**
     * 用户权限列表
     */
    private List<String> permissions;

    /**
     * 用户菜单权限列表（树形结构）
     */
    private List<AuthSmartPermission> menus;
}
