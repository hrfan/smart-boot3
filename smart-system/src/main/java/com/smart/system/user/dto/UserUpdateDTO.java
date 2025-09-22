package com.smart.system.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 用户更新DTO
 * 用于用户更新请求的传输对象
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Data
public class UserUpdateDTO {

    /**
     * 用户ID
     * 必填字段，用于标识要更新的用户
     */
    @NotBlank(message = "用户ID不能为空")
    private String id;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户邮箱
     * 邮箱格式验证
     */
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 手机号码
     * 手机号格式验证
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phonenumber;

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
     * 头像地址
     */
    private String avatar;

    /**
     * 账号状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 租户ID
     */
    private String tenantId;
}
