package com.smart.system.role.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.common.core.result.Result;
import com.smart.system.role.entiy.SmartRole;
import com.smart.system.role.service.SmartRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色信息表Controller
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Tag(name = "角色管理", description = "角色信息管理接口")
@RestController
@RequestMapping("/role")
public class SmartRoleController {

    private static final Logger log = LoggerFactory.getLogger(SmartRoleController.class);

    @Autowired
    private SmartRoleService smartRoleService;

    /**
     * 分页查询角色列表
     * 
     * @param pageNum 页码
     * @param pageSize 页大小
     * @param roleName 角色名称（可选）
     * @param status 状态（可选）
     * @return 分页结果
     */
    @Operation(summary = "分页查询角色列表", description = "根据条件分页查询角色信息")
    @GetMapping("/page")
    public Result<IPage<SmartRole>> page(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "角色名称") @RequestParam(required = false) String roleName,
            @Parameter(description = "状态") @RequestParam(required = false) String status) {
        
        log.info("分页查询角色列表 - 页码: {}, 页大小: {}, 角色名称: {}, 状态: {}", pageNum, pageSize, roleName, status);
        
        Page<SmartRole> page = new Page<>(pageNum, pageSize);
        IPage<SmartRole> result = smartRoleService.page(page);
        
        return Result.success("查询成功", result);
    }

    /**
     * 查询所有角色列表
     * 
     * @return 角色列表
     */
    @Operation(summary = "查询所有角色列表", description = "查询所有角色信息")
    @GetMapping("/list")
    public Result<List<SmartRole>> list() {
        log.info("查询所有角色列表");
        
        List<SmartRole> list = smartRoleService.list();
        
        return Result.success("查询成功", list);
    }

    /**
     * 根据ID查询角色详情
     * 
     * @param id 角色ID
     * @return 角色详情
     */
    @Operation(summary = "查询角色详情", description = "根据ID查询角色详细信息")
    @GetMapping("/{id}")
    public Result<SmartRole> getById(@Parameter(description = "角色ID") @PathVariable String id) {
        log.info("查询角色详情 - ID: {}", id);
        
        SmartRole role = smartRoleService.getById(id);
        if (role == null) {
            return Result.error("角色不存在");
        }
        
        return Result.success("查询成功", role);
    }

    /**
     * 根据角色名称查询角色
     * 
     * @param roleName 角色名称
     * @return 角色信息
     */
    @Operation(summary = "根据角色名称查询", description = "根据角色名称查询角色信息")
    @GetMapping("/name/{roleName}")
    public Result<SmartRole> getByRoleName(@Parameter(description = "角色名称") @PathVariable String roleName) {
        log.info("根据角色名称查询角色 - 角色名称: {}", roleName);
        
        SmartRole role = smartRoleService.getByRoleName(roleName);
        if (role == null) {
            return Result.error("角色不存在");
        }
        
        return Result.success("查询成功", role);
    }

    /**
     * 根据用户ID查询角色列表
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    @Operation(summary = "根据用户ID查询角色", description = "根据用户ID查询用户拥有的角色列表")
    @GetMapping("/user/{userId}")
    public Result<List<SmartRole>> getRolesByUserId(@Parameter(description = "用户ID") @PathVariable String userId) {
        log.info("根据用户ID查询角色列表 - 用户ID: {}", userId);
        
        List<SmartRole> roles = smartRoleService.getRolesByUserId(userId);
        
        return Result.success("查询成功", roles);
    }

    /**
     * 查询所有启用的角色
     * 
     * @return 启用的角色列表
     */
    @Operation(summary = "查询启用的角色", description = "查询所有状态为启用的角色")
    @GetMapping("/enabled")
    public Result<List<SmartRole>> getEnabledRoles() {
        log.info("查询所有启用的角色");
        
        List<SmartRole> roles = smartRoleService.getEnabledRoles();
        
        return Result.success("查询成功", roles);
    }

    /**
     * 新增角色
     * 
     * @param role 角色信息
     * @return 操作结果
     */
    @Operation(summary = "新增角色", description = "新增角色信息")
    @PostMapping
    public Result<String> add(@RequestBody SmartRole role) {
        log.info("新增角色 - 角色名称: {}", role.getRoleName());
        
        try {
            boolean success = smartRoleService.insertRole(role);
            if (success) {
                return Result.success("新增成功");
            } else {
                return Result.error("新增失败");
            }
        } catch (Exception e) {
            log.error("新增角色失败", e);
            return Result.error("新增失败: " + e.getMessage());
        }
    }

    /**
     * 修改角色
     * 
     * @param role 角色信息
     * @return 操作结果
     */
    @Operation(summary = "修改角色", description = "修改角色信息")
    @PutMapping
    public Result<String> update(@RequestBody SmartRole role) {
        log.info("修改角色 - 角色ID: {}, 角色名称: {}", role.getId(), role.getRoleName());
        
        try {
            boolean success = smartRoleService.updateRole(role);
            if (success) {
                return Result.success("修改成功");
            } else {
                return Result.error("修改失败");
            }
        } catch (Exception e) {
            log.error("修改角色失败", e);
            return Result.error("修改失败: " + e.getMessage());
        }
    }

    /**
     * 删除角色
     * 
     * @param id 角色ID
     * @return 操作结果
     */
    @Operation(summary = "删除角色", description = "根据ID删除角色")
    @DeleteMapping("/{id}")
    public Result<String> delete(@Parameter(description = "角色ID") @PathVariable String id) {
        log.info("删除角色 - 角色ID: {}", id);
        
        try {
            boolean success = smartRoleService.removeById(id);
            if (success) {
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除角色失败", e);
            return Result.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除角色
     * 
     * @param roleIds 角色ID列表
     * @return 操作结果
     */
    @Operation(summary = "批量删除角色", description = "批量删除角色")
    @DeleteMapping("/batch")
    public Result<String> deleteBatch(@RequestBody List<String> roleIds) {
        log.info("批量删除角色 - 角色ID列表: {}", roleIds);
        
        try {
            boolean success = smartRoleService.deleteRolesByIds(roleIds);
            if (success) {
                return Result.success("批量删除成功");
            } else {
                return Result.error("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除角色失败", e);
            return Result.error("批量删除失败: " + e.getMessage());
        }
    }

    /**
     * 更新角色状态
     * 
     * @param id 角色ID
     * @param status 状态
     * @return 操作结果
     */
    @Operation(summary = "更新角色状态", description = "更新角色的启用/禁用状态")
    @PutMapping("/{id}/status")
    public Result<String> updateStatus(
            @Parameter(description = "角色ID") @PathVariable String id,
            @Parameter(description = "状态") @RequestParam String status) {
        
        log.info("更新角色状态 - 角色ID: {}, 状态: {}", id, status);
        
        try {
            boolean success = smartRoleService.updateRoleStatus(id, status);
            if (success) {
                return Result.success("状态更新成功");
            } else {
                return Result.error("状态更新失败");
            }
        } catch (Exception e) {
            log.error("更新角色状态失败", e);
            return Result.error("状态更新失败: " + e.getMessage());
        }
    }

    /**
     * 检查角色名称是否存在
     * 
     * @param roleName 角色名称
     * @param excludeId 排除的角色ID
     * @return 检查结果
     */
    @Operation(summary = "检查角色名称", description = "检查角色名称是否已存在")
    @GetMapping("/check/name")
    public Result<Boolean> checkRoleName(
            @Parameter(description = "角色名称") @RequestParam String roleName,
            @Parameter(description = "排除的角色ID") @RequestParam(required = false) String excludeId) {
        
        log.info("检查角色名称是否存在 - 角色名称: {}, 排除ID: {}", roleName, excludeId);
        
        boolean exists = smartRoleService.checkRoleNameExists(roleName, excludeId);
        
        return Result.success("检查完成", exists);
    }

    /**
     * 检查角色权限字符串是否存在
     * 
     * @param roleKey 角色权限字符串
     * @param excludeId 排除的角色ID
     * @return 检查结果
     */
    @Operation(summary = "检查角色权限字符串", description = "检查角色权限字符串是否已存在")
    @GetMapping("/check/key")
    public Result<Boolean> checkRoleKey(
            @Parameter(description = "角色权限字符串") @RequestParam String roleKey,
            @Parameter(description = "排除的角色ID") @RequestParam(required = false) String excludeId) {
        
        log.info("检查角色权限字符串是否存在 - 角色权限字符串: {}, 排除ID: {}", roleKey, excludeId);
        
        boolean exists = smartRoleService.checkRoleKeyExists(roleKey, excludeId);
        
        return Result.success("检查完成", exists);
    }
}
