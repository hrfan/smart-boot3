package com.smart.system.user.mapper;

import com.smart.framework.database.mapper.BaseMapper;
import com.smart.system.user.entity.SmartUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 用户信息表Mapper接口
 * 提供用户数据访问层功能
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Mapper
public interface SmartUserMapper extends BaseMapper<SmartUser> {

    /**
     * 根据用户ID查询用户信息
     * 
     * @param id 用户ID
     * @return 用户信息
     */
    SmartUser selectByUserId(@Param("id") String id);

    /**
     * 根据用户账号查询用户信息
     * 
     * @param userName 用户账号
     * @return 用户信息
     */
    SmartUser selectByUserName(@Param("userName") String userName);

    /**
     * 根据邮箱查询用户信息
     * 
     * @param email 邮箱
     * @return 用户信息
     */
    SmartUser selectByEmail(@Param("email") String email);

    /**
     * 根据手机号查询用户信息
     * 
     * @param phonenumber 手机号
     * @return 用户信息
     */
    SmartUser selectByPhonenumber(@Param("phonenumber") String phonenumber);

    /**
     * 根据部门ID查询用户列表
     * 
     * @param deptId 部门ID
     * @return 用户列表
     */
    List<SmartUser> selectByDeptId(@Param("deptId") String deptId);

    /**
     * 根据状态查询用户列表
     * 
     * @param status 状态
     * @return 用户列表
     */
    List<SmartUser> selectByStatus(@Param("status") String status);

    /**
     * 根据用户类型查询用户列表
     * 
     * @param userType 用户类型
     * @return 用户列表
     */
    List<SmartUser> selectByUserType(@Param("userType") String userType);

    /**
     * 检查用户名是否存在
     * 
     * @param userName 用户名
     * @param excludeId 排除的用户ID
     * @return 存在数量
     */
    int countByUserName(@Param("userName") String userName, @Param("excludeId") String excludeId);

    /**
     * 检查邮箱是否存在
     * 
     * @param email 邮箱
     * @param excludeId 排除的用户ID
     * @return 存在数量
     */
    int countByEmail(@Param("email") String email, @Param("excludeId") String excludeId);

    /**
     * 检查手机号是否存在
     * 
     * @param phonenumber 手机号
     * @param excludeId 排除的用户ID
     * @return 存在数量
     */
    int countByPhonenumber(@Param("phonenumber") String phonenumber, @Param("excludeId") String excludeId);

    /**
     * 更新登录信息
     * 
     * @param id 用户ID
     * @param loginIp 登录IP
     * @param loginDate 登录时间
     * @return 更新数量
     */
    int updateLoginInfo(@Param("id") String id, @Param("loginIp") String loginIp, @Param("loginDate") Date loginDate);

    /**
     * 更新用户状态
     * 
     * @param id 用户ID
     * @param status 状态
     * @return 更新数量
     */
    int updateStatus(@Param("id") String id, @Param("status") String status);

    /**
     * 更新用户密码
     * 
     * @param id 用户ID
     * @param password 密码
     * @return 更新数量
     */
    int updatePassword(@Param("id") String id, @Param("password") String password);

    /**
     * 根据租户ID查询用户列表
     * 
     * @param tenantId 租户ID
     * @return 用户列表
     */
    List<SmartUser> selectByTenantId(@Param("tenantId") String tenantId);

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
    long countUsersByStatus(@Param("status") String status);

    /**
     * 根据部门ID统计用户数量
     * 
     * @param deptId 部门ID
     * @return 用户数量
     */
    long countUsersByDeptId(@Param("deptId") String deptId);

    /**
     * 根据租户ID统计用户数量
     * 
     * @param tenantId 租户ID
     * @return 用户数量
     */
    long countUsersByTenantId(@Param("tenantId") String tenantId);
}
