package com.smart.system.role_permission.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.common.core.result.Result;
import com.smart.common.database.query.QueryBuilder;
import com.smart.system.role_permission.entity.SmartRolePermission;
import com.smart.system.role_permission.service.SmartRolePermissionService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色权限管理控制器
 * 提供角色权限关联管理相关接口
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@RestController
@RequestMapping("/system/role-permission")
public class SmartRolePermissionController {

    private static final Logger log = LoggerFactory.getLogger(SmartRolePermissionController.class);

    @Resource
    private SmartRolePermissionService smartRolePermissionService;

    /**
     * 查询角色权限关联分页列表
     * @param smartRolePermission 查询参数对象，用于构建查询条件，前端不需要传递
     * @param pageNo  当前页码，默认值为1
     * @param pageSize 每页记录数，默认值为10
     * @param req  HTTP请求对象，用于获取查询参数（主要用来构建查询条件）
     * @return 分页列表结果
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<IPage<SmartRolePermission>> queryPageList(SmartRolePermission smartRolePermission,
                                                            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                            @RequestParam(name="pageSize", defaultValue="20") Integer pageSize,
                                                            HttpServletRequest req) {
        QueryWrapper<SmartRolePermission> queryWrapper = QueryBuilder.initQueryWrapper(smartRolePermission, req.getParameterMap());
        Page<SmartRolePermission> page = new Page<SmartRolePermission>(pageNo, pageSize);
        IPage<SmartRolePermission> pageList = smartRolePermissionService.page(page, queryWrapper);
        return Result.success("查询成功", pageList);
    }

    /**
     * 新增角色权限关联
     * @param params 角色权限关联实体对象，包含新增的角色权限关联信息
     * @return 操作结果，包含操作成功标志和操作结果数据
     */
    @RequestMapping(value = "insert", method = RequestMethod.POST)
    public Result<?> insert(@RequestBody @Valid SmartRolePermission params) {
        SmartRolePermission rolePermission = smartRolePermissionService.insert(params);
        return rolePermission != null ? Result.success("新增成功", rolePermission) : Result.error("新增失败");
    }

    /**
     * 更新角色权限关联
     * @param id 角色权限关联ID
     * @param params 角色权限关联实体对象，包含更新的角色权限关联信息
     * @return 操作结果，包含操作成功标志和操作结果数据
     */
    @RequestMapping(value = "update/{id}", method = RequestMethod.PUT)
    public Result<?> update(@PathVariable(name="id", required=true) String id, @RequestBody @Valid SmartRolePermission params) {
        SmartRolePermission smartRolePermission = smartRolePermissionService.update(params);
        return smartRolePermission != null ? Result.success("更新成功", smartRolePermission) : Result.error("更新失败");
    }

    /**
     * 删除角色权限关联
     * @param id 角色权限关联ID，用于指定要删除的角色权限关联
     * @return 操作结果，包含操作成功标志和操作结果数据
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<?> delete(@RequestParam(name="id", required=true) String id) {
        boolean isSuccess = smartRolePermissionService.removeById(id);
        return isSuccess ? Result.success("删除成功") : Result.error("删除失败");
    }

    /**
     * 批量删除角色权限关联
     * @param ids 角色权限关联ID列表，用于指定要删除的角色权限关联
     * @return 操作结果，包含操作成功标志和操作结果数据
     */
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    public Result<?> deleteBatch(@RequestBody @Valid List<String> ids) {
        boolean isSuccess = smartRolePermissionService.removeByIds(ids);
        return isSuccess ? Result.success("删除成功") : Result.error("删除失败");
    }
}
