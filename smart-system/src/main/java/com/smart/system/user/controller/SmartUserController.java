package com.smart.system.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.common.core.result.Result;
import com.smart.system.user.entity.SmartUser;
import com.smart.system.user.service.SmartUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户信息表Controller
 * 提供用户管理REST API接口
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Tag(name = "用户管理", description = "用户信息管理接口")
@RestController
@RequestMapping("/user")
public class SmartUserController {

    private static final Logger log = LoggerFactory.getLogger(SmartUserController.class);

    @Autowired
    private SmartUserService smartUserService;

    /**
     * 创建用户
     * 
     * @param user 用户信息
     * @return 创建结果
     */
    @Operation(summary = "创建用户", description = "创建新用户")
    @PostMapping("/create")
    public Result<Boolean> createUser(@RequestBody SmartUser user) {
        log.info("创建用户: {}", user.getUserName());
        boolean success = smartUserService.createUser(user);
        return Result.success(success);
    }

    /**
     * 根据用户ID获取用户详情
     * 
     * @param userId 用户ID
     * @return 用户信息
     */
    @Operation(summary = "获取用户详情", description = "根据用户ID获取用户详细信息")
    @GetMapping("/{userId}")
    public Result<SmartUser> getUserById(@Parameter(description = "用户ID") @PathVariable String userId) {
        log.info("获取用户详情: {}", userId);
        SmartUser user = smartUserService.getByUserId(userId);
        return Result.success(user);
    }

    /**
     * 更新用户
     * 
     * @param user 用户信息
     * @return 更新结果
     */
    @Operation(summary = "更新用户", description = "更新用户信息")
    @PutMapping("/update")
    public Result<Boolean> updateUser(@RequestBody SmartUser user) {
        log.info("更新用户: {}", user.getId());
        boolean success = smartUserService.updateUser(user);
        return Result.success(success);
    }

    /**
     * 删除用户
     * 
     * @param userId 用户ID
     * @return 删除结果
     */
    @Operation(summary = "删除用户", description = "根据用户ID删除用户")
    @DeleteMapping("/{userId}")
    public Result<Boolean> deleteUser(@Parameter(description = "用户ID") @PathVariable String userId) {
        log.info("删除用户: {}", userId);
        boolean success = smartUserService.deleteUser(userId);
        return Result.success(success);
    }

    /**
     * 获取用户列表
     * 
     * @return 用户列表
     */
    @Operation(summary = "获取用户列表", description = "获取所有用户列表")
    @GetMapping("/list")
    public Result<List<SmartUser>> getUserList() {
        log.info("获取用户列表");
        List<SmartUser> users = smartUserService.list();
        return Result.success(users);
    }

    /**
     * 分页查询用户
     * 
     * @param current 当前页
     * @param size 每页大小
     * @return 分页结果
     */
    @Operation(summary = "分页查询用户", description = "分页查询用户信息")
    @GetMapping("/page")
    public Result<IPage<SmartUser>> getUserPage(
            @Parameter(description = "当前页") @RequestParam(defaultValue = "1") Long current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Long size) {
        log.info("分页查询用户: current={}, size={}", current, size);
        Page<SmartUser> page = new Page<>(current, size);
        IPage<SmartUser> result = smartUserService.page(page);
        return Result.success(result);
    }

    /**
     * 根据用户名查询用户
     * 
     * @param userName 用户名
     * @return 用户信息
     */
    @Operation(summary = "根据用户名查询用户", description = "根据用户名获取用户信息")
    @GetMapping("/by-username/{userName}")
    public Result<SmartUser> getUserByUserName(@Parameter(description = "用户名") @PathVariable String userName) {
        log.info("根据用户名查询用户: {}", userName);
        SmartUser user = smartUserService.getByUserName(userName);
        return Result.success(user);
    }

    /**
     * 根据邮箱查询用户
     * 
     * @param email 邮箱
     * @return 用户信息
     */
    @Operation(summary = "根据邮箱查询用户", description = "根据邮箱获取用户信息")
    @GetMapping("/by-email/{email}")
    public Result<SmartUser> getUserByEmail(@Parameter(description = "邮箱") @PathVariable String email) {
        log.info("根据邮箱查询用户: {}", email);
        SmartUser user = smartUserService.getByEmail(email);
        return Result.success(user);
    }

    /**
     * 根据部门ID查询用户列表
     * 
     * @param deptId 部门ID
     * @return 用户列表
     */
    @Operation(summary = "根据部门查询用户", description = "根据部门ID获取用户列表")
    @GetMapping("/by-dept/{deptId}")
    public Result<List<SmartUser>> getUsersByDeptId(@Parameter(description = "部门ID") @PathVariable String deptId) {
        log.info("根据部门ID查询用户列表: {}", deptId);
        List<SmartUser> users = smartUserService.getByDeptId(deptId);
        return Result.success(users);
    }

    /**
     * 更新用户状态
     * 
     * @param userId 用户ID
     * @param status 状态
     * @return 更新结果
     */
    @Operation(summary = "更新用户状态", description = "更新用户状态")
    @PutMapping("/status")
    public Result<Boolean> updateUserStatus(
            @Parameter(description = "用户ID") @RequestParam String userId,
            @Parameter(description = "状态") @RequestParam String status) {
        log.info("更新用户状态: {}, 状态: {}", userId, status);
        boolean success = smartUserService.updateUserStatus(userId, status);
        return Result.success(success);
    }

    /**
     * 更新用户密码
     * 
     * @param userId 用户ID
     * @param newPassword 新密码
     * @return 更新结果
     */
    @Operation(summary = "更新用户密码", description = "更新用户密码")
    @PutMapping("/password")
    public Result<Boolean> updatePassword(
            @Parameter(description = "用户ID") @RequestParam String userId,
            @Parameter(description = "新密码") @RequestParam String newPassword) {
        log.info("更新用户密码: {}", userId);
        boolean success = smartUserService.updatePassword(userId, newPassword);
        return Result.success(success);
    }

    /**
     * 更新登录信息
     * 
     * @param userId 用户ID
     * @param loginIp 登录IP
     * @return 更新结果
     */
    @Operation(summary = "更新登录信息", description = "更新用户登录信息")
    @PutMapping("/login-info")
    public Result<Boolean> updateLoginInfo(
            @Parameter(description = "用户ID") @RequestParam String userId,
            @Parameter(description = "登录IP") @RequestParam String loginIp) {
        log.info("更新登录信息: {}, IP: {}", userId, loginIp);
        boolean success = smartUserService.updateLoginInfo(userId, loginIp);
        return Result.success(success);
    }

    /**
     * 启用用户
     * 
     * @param userId 用户ID
     * @return 操作结果
     */
    @Operation(summary = "启用用户", description = "启用用户账号")
    @PutMapping("/enable/{userId}")
    public Result<Boolean> enableUser(@Parameter(description = "用户ID") @PathVariable String userId) {
        log.info("启用用户: {}", userId);
        boolean success = smartUserService.enableUser(userId);
        return Result.success(success);
    }

    /**
     * 停用用户
     * 
     * @param userId 用户ID
     * @return 操作结果
     */
    @Operation(summary = "停用用户", description = "停用用户账号")
    @PutMapping("/disable/{userId}")
    public Result<Boolean> disableUser(@Parameter(description = "用户ID") @PathVariable String userId) {
        log.info("停用用户: {}", userId);
        boolean success = smartUserService.disableUser(userId);
        return Result.success(success);
    }

    /**
     * 统计用户总数
     * 
     * @return 用户总数
     */
    @Operation(summary = "统计用户总数", description = "获取用户总数")
    @GetMapping("/count")
    public Result<Long> countUsers() {
        log.info("统计用户总数");
        long count = smartUserService.countUsers();
        return Result.success(count);
    }

    /**
     * 根据状态统计用户数量
     * 
     * @param status 状态
     * @return 用户数量
     */
    @Operation(summary = "根据状态统计用户数量", description = "根据状态统计用户数量")
    @GetMapping("/count-by-status")
    public Result<Long> countUsersByStatus(@Parameter(description = "状态") @RequestParam String status) {
        log.info("根据状态统计用户数量: {}", status);
        long count = smartUserService.countUsersByStatus(status);
        return Result.success(count);
    }
}
