package com.smart.system.user.dto;

import lombok.Data;

import java.util.Date;

/**
 * 用户查询条件DTO
 * 用于用户查询条件的传输对象
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Data
public class UserQueryDTO {

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phonenumber;

    /**
     * 账号状态（0正常 1停用）
     */
    private String status;

    /**
     * 部门ID
     */
    private String deptId;

    /**
     * 用户类型（00系统用户）
     */
    private String userType;

    /**
     * 用户性别（0男 1女 2未知）
     */
    private String sex;

    /**
     * 创建时间开始
     */
    private Date createTimeStart;

    /**
     * 创建时间结束
     */
    private Date createTimeEnd;

    /**
     * 租户ID
     */
    private String tenantId;
}
