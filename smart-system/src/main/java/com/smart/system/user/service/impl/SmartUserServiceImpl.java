package com.smart.system.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.system.user.entity.SmartUser;
import com.smart.system.user.mapper.SmartUserMapper;
import com.smart.system.user.service.SmartUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 用户信息表Service实现类
 * 实现用户业务逻辑层功能
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Service
public class SmartUserServiceImpl extends ServiceImpl<SmartUserMapper, SmartUser> implements SmartUserService {

    private static final Logger log = LoggerFactory.getLogger(SmartUserServiceImpl.class);

    @Autowired
    private SmartUserMapper smartUserMapper;

    /**
     * 根据用户ID查询用户信息
     * 
     * @param id 用户ID
     * @return 用户信息
     */
    @Override
    public SmartUser getByUserId(String id) {
        log.debug("根据用户ID查询用户信息: {}", id);
        return smartUserMapper.selectByUserId(id);
    }

    /**
     * 根据用户账号查询用户信息
     * 
     * @param userName 用户账号
     * @return 用户信息
     */
    @Override
    public SmartUser getByUserName(String userName) {
        log.debug("根据用户账号查询用户信息: {}", userName);
        return smartUserMapper.selectByUserName(userName);
    }

    /**
     * 根据邮箱查询用户信息
     * 
     * @param email 邮箱
     * @return 用户信息
     */
    @Override
    public SmartUser getByEmail(String email) {
        log.debug("根据邮箱查询用户信息: {}", email);
        return smartUserMapper.selectByEmail(email);
    }

    /**
     * 根据手机号查询用户信息
     * 
     * @param phonenumber 手机号
     * @return 用户信息
     */
    @Override
    public SmartUser getByPhonenumber(String phonenumber) {
        log.debug("根据手机号查询用户信息: {}", phonenumber);
        return smartUserMapper.selectByPhonenumber(phonenumber);
    }

    /**
     * 根据部门ID查询用户列表
     * 
     * @param deptId 部门ID
     * @return 用户列表
     */
    @Override
    public List<SmartUser> getByDeptId(String deptId) {
        log.debug("根据部门ID查询用户列表: {}", deptId);
        return smartUserMapper.selectByDeptId(deptId);
    }

    /**
     * 根据状态查询用户列表
     * 
     * @param status 状态
     * @return 用户列表
     */
    @Override
    public List<SmartUser> getByStatus(String status) {
        log.debug("根据状态查询用户列表: {}", status);
        return smartUserMapper.selectByStatus(status);
    }

    /**
     * 根据用户类型查询用户列表
     * 
     * @param userType 用户类型
     * @return 用户列表
     */
    @Override
    public List<SmartUser> getByUserType(String userType) {
        log.debug("根据用户类型查询用户列表: {}", userType);
        return smartUserMapper.selectByUserType(userType);
    }

    /**
     * 根据租户ID查询用户列表
     * 
     * @param tenantId 租户ID
     * @return 用户列表
     */
    @Override
    public List<SmartUser> getByTenantId(String tenantId) {
        log.debug("根据租户ID查询用户列表: {}", tenantId);
        return smartUserMapper.selectByTenantId(tenantId);
    }

    /**
     * 检查用户名是否存在
     * 
     * @param userName 用户名
     * @param excludeId 排除的用户ID
     * @return 是否存在
     */
    @Override
    public boolean checkUserNameExists(String userName, String excludeId) {
        log.debug("检查用户名是否存在: {}, 排除用户ID: {}", userName, excludeId);
        int count = smartUserMapper.countByUserName(userName, excludeId);
        return count > 0;
    }

    /**
     * 检查邮箱是否存在
     * 
     * @param email 邮箱
     * @param excludeId 排除的用户ID
     * @return 是否存在
     */
    @Override
    public boolean checkEmailExists(String email, String excludeId) {
        log.debug("检查邮箱是否存在: {}, 排除用户ID: {}", email, excludeId);
        int count = smartUserMapper.countByEmail(email, excludeId);
        return count > 0;
    }

    /**
     * 检查手机号是否存在
     * 
     * @param phonenumber 手机号
     * @param excludeId 排除的用户ID
     * @return 是否存在
     */
    @Override
    public boolean checkPhonenumberExists(String phonenumber, String excludeId) {
        log.debug("检查手机号是否存在: {}, 排除用户ID: {}", phonenumber, excludeId);
        int count = smartUserMapper.countByPhonenumber(phonenumber, excludeId);
        return count > 0;
    }

    /**
     * 验证用户密码
     * 
     * @param id 用户ID
     * @param password 密码
     * @return 密码是否正确
     */
    @Override
    public boolean validatePassword(String id, String password) {
        log.debug("验证用户密码: {}", id);
        SmartUser user = getByUserId(id);
        if (user == null) {
            log.warn("用户不存在: {}", id);
            return false;
        }
        return password.equals(user.getPassword());
    }

    /**
     * 创建用户
     * 
     * @param user 用户信息
     * @return 是否成功
     */
    @Override
    public boolean createUser(SmartUser user) {
        log.debug("创建用户: {}", user.getUserName());
        return save(user);
    }

    /**
     * 更新用户
     * 
     * @param user 用户信息
     * @return 是否成功
     */
    @Override
    public boolean updateUser(SmartUser user) {
        log.debug("更新用户: {}", user.getId());
        return updateById(user);
    }

    /**
     * 删除用户
     * 
     * @param id 用户ID
     * @return 是否成功
     */
    @Override
    public boolean deleteUser(String id) {
        log.debug("删除用户: {}", id);
        return removeById(id);
    }

    /**
     * 更新用户状态
     * 
     * @param id 用户ID
     * @param status 状态
     * @return 是否成功
     */
    @Override
    public boolean updateUserStatus(String id, String status) {
        log.debug("更新用户状态: {}, 状态: {}", id, status);
        int result = smartUserMapper.updateStatus(id, status);
        return result > 0;
    }

    /**
     * 更新用户密码
     * 
     * @param id 用户ID
     * @param newPassword 新密码
     * @return 是否成功
     */
    @Override
    public boolean updatePassword(String id, String newPassword) {
        log.debug("更新用户密码: {}", id);
        int result = smartUserMapper.updatePassword(id, newPassword);
        return result > 0;
    }

    /**
     * 更新登录信息
     * 
     * @param id 用户ID
     * @param loginIp 登录IP
     * @return 是否成功
     */
    @Override
    public boolean updateLoginInfo(String id, String loginIp) {
        log.debug("更新登录信息: {}, IP: {}", id, loginIp);
        int result = smartUserMapper.updateLoginInfo(id, loginIp, new Date());
        return result > 0;
    }

    /**
     * 统计用户总数
     * 
     * @return 用户总数
     */
    @Override
    public long countUsers() {
        log.debug("统计用户总数");
        return smartUserMapper.countUsers();
    }

    /**
     * 根据状态统计用户数量
     * 
     * @param status 状态
     * @return 用户数量
     */
    @Override
    public long countUsersByStatus(String status) {
        log.debug("根据状态统计用户数量: {}", status);
        return smartUserMapper.countUsersByStatus(status);
    }

    /**
     * 根据部门ID统计用户数量
     * 
     * @param deptId 部门ID
     * @return 用户数量
     */
    @Override
    public long countUsersByDeptId(String deptId) {
        log.debug("根据部门ID统计用户数量: {}", deptId);
        return smartUserMapper.countUsersByDeptId(deptId);
    }

    /**
     * 根据租户ID统计用户数量
     * 
     * @param tenantId 租户ID
     * @return 用户数量
     */
    @Override
    public long countUsersByTenantId(String tenantId) {
        log.debug("根据租户ID统计用户数量: {}", tenantId);
        return smartUserMapper.countUsersByTenantId(tenantId);
    }

    /**
     * 批量删除用户
     * 
     * @param ids 用户ID列表
     * @return 是否成功
     */
    @Override
    public boolean deleteUsersByIds(List<String> ids) {
        log.debug("批量删除用户: {}", ids);
        return removeByIds(ids);
    }

    /**
     * 启用用户
     * 
     * @param id 用户ID
     * @return 是否成功
     */
    @Override
    public boolean enableUser(String id) {
        log.debug("启用用户: {}", id);
        return updateUserStatus(id, "0");
    }

    /**
     * 停用用户
     * 
     * @param id 用户ID
     * @return 是否成功
     */
    @Override
    public boolean disableUser(String id) {
        log.debug("停用用户: {}", id);
        return updateUserStatus(id, "1");
    }
}
