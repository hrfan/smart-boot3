package com.smart.framework.security.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 可缓存的用户信息类
 * 解决AuthSmartUser中GrantedAuthority无法序列化的问题
 * 用于Redis缓存，避免序列化错误
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Data
public class CacheableUser implements UserDetails {

    private static final long serialVersionUID = 1L;

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
     * 用户类型
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
     * 用户性别
     */
    private String sex;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 密码
     */
    private String password;

    /**
     * 账号状态
     */
    private String status;

    /**
     * 删除标志
     */
    private String delFlag;

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
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建人姓名
     */
    private String createUserName;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新人姓名
     */
    private String updateUserName;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 账户是否过期
     */
    private Boolean isAccountNonExpired = true;

    /**
     * 账户是否被锁定
     */
    private Boolean isAccountNonLocked = true;

    /**
     * 密码是否过期
     */
    private Boolean isCredentialsNonExpired = true;

    /**
     * 账户是否可用
     */
    private Boolean isEnabled = true;

    /**
     * 权限列表（可序列化的字符串列表）
     */
    private List<String> permissionCodes;

    /**
     * 角色列表
     */
    private List<String> roles;

    /**
     * 权限列表（不序列化，运行时动态生成）
     */
    @JsonIgnore
    private transient Collection<? extends GrantedAuthority> authorities;

    /**
     * 菜单权限列表（不序列化，运行时动态生成）
     */
    @JsonIgnore
    private transient List<AuthSmartPermission> permissionList;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

    /**
     * 设置权限信息（运行时调用）
     * 
     * @param authorities 权限集合
     */
    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    /**
     * 设置菜单权限列表（运行时调用）
     * 
     * @param permissionList 菜单权限列表
     */
    public void setPermissionList(List<AuthSmartPermission> permissionList) {
        this.permissionList = permissionList;
    }

    /**
     * 从AuthSmartUser转换为CacheableUser
     * 
     * @param authUser 原始用户对象
     * @return 可缓存的用户对象
     */
    public static CacheableUser fromAuthSmartUser(AuthSmartUser authUser) {
        CacheableUser cacheableUser = new CacheableUser();
        
        // 复制基本属性
        cacheableUser.setId(authUser.getId());
        cacheableUser.setDeptId(authUser.getDeptId());
        cacheableUser.setUserName(authUser.getUsername());
        cacheableUser.setNickName(authUser.getNickName());
        cacheableUser.setUserType(authUser.getUserType());
        cacheableUser.setEmail(authUser.getEmail());
        cacheableUser.setPhonenumber(authUser.getPhonenumber());
        cacheableUser.setSex(authUser.getSex());
        cacheableUser.setAvatar(authUser.getAvatar());
        cacheableUser.setPassword(authUser.getPassword());
        cacheableUser.setStatus(authUser.getStatus());
        cacheableUser.setDelFlag(authUser.getDelFlag());
        cacheableUser.setLoginIp(authUser.getLoginIp());
        cacheableUser.setLoginDate(authUser.getLoginDate());
        cacheableUser.setRemark(authUser.getRemark());
        cacheableUser.setCreateTime(authUser.getCreateTime());
        cacheableUser.setCreateBy(authUser.getCreateBy());
        cacheableUser.setCreateUserName(authUser.getCreateUserName());
        cacheableUser.setUpdateTime(authUser.getUpdateTime());
        cacheableUser.setUpdateBy(authUser.getUpdateBy());
        cacheableUser.setUpdateUserName(authUser.getUpdateUserName());
        cacheableUser.setTenantId(authUser.getTenantId());
        cacheableUser.setIsAccountNonExpired(authUser.getIsAccountNonExpired());
        cacheableUser.setIsAccountNonLocked(authUser.getIsAccountNonLocked());
        cacheableUser.setIsCredentialsNonExpired(authUser.getIsCredentialsNonExpired());
        cacheableUser.setIsEnabled(authUser.getIsEnabled());
        cacheableUser.setRoles(authUser.getRoles());
        
        // 设置权限代码列表（可序列化）
        if (authUser.getAuthorities() != null) {
            List<String> permissionCodes = authUser.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
            cacheableUser.setPermissionCodes(permissionCodes);
        }
        
        return cacheableUser;
    }

    /**
     * 转换为AuthSmartUser（从缓存恢复时使用）
     * 
     * @return AuthSmartUser对象
     */
    public AuthSmartUser toAuthSmartUser() {
        AuthSmartUser authUser = new AuthSmartUser();
        
        // 复制基本属性
        authUser.setId(this.id);
        authUser.setDeptId(this.deptId);
        authUser.setUserName(this.userName);
        authUser.setNickName(this.nickName);
        authUser.setUserType(this.userType);
        authUser.setEmail(this.email);
        authUser.setPhonenumber(this.phonenumber);
        authUser.setSex(this.sex);
        authUser.setAvatar(this.avatar);
        authUser.setPassword(this.password);
        authUser.setStatus(this.status);
        authUser.setDelFlag(this.delFlag);
        authUser.setLoginIp(this.loginIp);
        authUser.setLoginDate(this.loginDate);
        authUser.setRemark(this.remark);
        authUser.setCreateTime(this.createTime);
        authUser.setCreateBy(this.createBy);
        authUser.setCreateUserName(this.createUserName);
        authUser.setUpdateTime(this.updateTime);
        authUser.setUpdateBy(this.updateBy);
        authUser.setUpdateUserName(this.updateUserName);
        authUser.setTenantId(this.tenantId);
        authUser.setIsAccountNonExpired(this.isAccountNonExpired);
        authUser.setIsAccountNonLocked(this.isAccountNonLocked);
        authUser.setIsCredentialsNonExpired(this.isCredentialsNonExpired);
        authUser.setIsEnabled(this.isEnabled);
        authUser.setRoles(this.roles);
        
        return authUser;
    }
}
