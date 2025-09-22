package com.smart.system.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smart.system.user.entity.SmartUser;

import java.util.List;

/**
 * 用户信息表Service接口
 * 定义用户业务逻辑层接口
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public interface SmartUserService extends IService<SmartUser> {

    /**
     * 根据用户ID查询用户信息
     * 
     * @param id 用户ID
     * @return 用户信息
     */
    SmartUser getByUserId(String id);

    /**
     * 根据用户账号查询用户信息
     * 
     * @param userName 用户账号
     * @return 用户信息
     */
    SmartUser getByUserName(String userName);

    /**
     * 根据邮箱查询用户信息
     * 
     * @param email 邮箱
     * @return 用户信息
     */
    SmartUser getByEmail(String email);

    /**
     * 根据手机号查询用户信息
     * 
     * @param phonenumber 手机号
     * @return 用户信息
     */
    SmartUser getByPhonenumber(String phonenumber);

    /**
     * 根据部门ID查询用户列表
     * 
     * @param deptId 部门ID
     * @return 用户列表
     */
    List<SmartUser> getByDeptId(String deptId);

    /**
     * 根据状态查询用户列表
     * 
     * @param status 状态
     * @return 用户列表
     */
    List<SmartUser> getByStatus(String status);

    /**
     * 根据用户类型查询用户列表
     * 
     * @param userType 用户类型
     * @return 用户列表
     */
    List<SmartUser> getByUserType(String userType);

    /**
     * 根据租户ID查询用户列表
     * 
     * @param tenantId 租户ID
     * @return 用户列表
     */
    List<SmartUser> getByTenantId(String tenantId);

    /**
     * 检查用户名是否存在
     * 
     * @param userName 用户名
     * @param excludeId 排除的用户ID
     * @return 是否存在
     */
    boolean checkUserNameExists(String userName, String excludeId);

    /**
     * 检查邮箱是否存在
     * 
     * @param email 邮箱
     * @param excludeId 排除的用户ID
     * @return 是否存在
     */
    boolean checkEmailExists(String email, String excludeId);

    /**
     * 检查手机号是否存在
     * 
     * @param phonenumber 手机号
     * @param excludeId 排除的用户ID
     * @return 是否存在
     */
    boolean checkPhonenumberExists(String phonenumber, String excludeId);

    /**
     * 验证用户密码
     * 
     * @param id 用户ID
     * @param password 密码
     * @return 密码是否正确
     */
    boolean validatePassword(String id, String password);

    /**
     * 创建用户
     * 
     * @param user 用户信息
     * @return 是否成功
     */
    boolean createUser(SmartUser user);

    /**
     * 更新用户
     * 
     * @param user 用户信息
     * @return 是否成功
     */
    boolean updateUser(SmartUser user);

    /**
     * 删除用户
     * 
     * @param id 用户ID
     * @return 是否成功
     */
    boolean deleteUser(String id);

    /**
     * 更新用户状态
     * 
     * @param id 用户ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateUserStatus(String id, String status);

    /**
     * 更新用户密码
     * 
     * @param id 用户ID
     * @param newPassword 新密码
     * @return 是否成功
     */
    boolean updatePassword(String id, String newPassword);

    /**
     * 更新登录信息
     * 
     * @param id 用户ID
     * @param loginIp 登录IP
     * @return 是否成功
     */
    boolean updateLoginInfo(String id, String loginIp);

    /**
     * 统计用户总数
     * 
     * @return 用户总数
     */
    long countUsers();

    /**
     * 根据状态统计用户数量
     * 
     * @param status 状态
     * @return 用户数量
     */
    long countUsersByStatus(String status);

    /**
     * 根据部门ID统计用户数量
     * 
     * @param deptId 部门ID
     * @return 用户数量
     */
    long countUsersByDeptId(String deptId);

    /**
     * 根据租户ID统计用户数量
     * 
     * @param tenantId 租户ID
     * @return 用户数量
     */
    long countUsersByTenantId(String tenantId);

    /**
     * 批量删除用户
     * 
     * @param ids 用户ID列表
     * @return 是否成功
     */
    boolean deleteUsersByIds(List<String> ids);

    /**
     * 启用用户
     * 
     * @param id 用户ID
     * @return 是否成功
     */
    boolean enableUser(String id);

    /**
     * 停用用户
     * 
     * @param id 用户ID
     * @return 是否成功
     */
    boolean disableUser(String id);
}
