package com.smart.system.role.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.framework.core.result.Result;
import com.smart.framework.database.query.QueryBuilder;
import com.smart.system.role.entiy.SmartRole;
import com.smart.system.role.service.SmartRoleService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色信息控制器
 * 提供角色管理相关接口
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@RestController
@RequestMapping("/system/role")
public class SmartRoleController {

    private static final Logger log = LoggerFactory.getLogger(SmartRoleController.class);

    @Resource
    private SmartRoleService smartRoleService;

    /**
     * 查询角色分页列表
     * @param smartRole 查询参数对象，用于构建查询条件，前端不需要传递
     * @param pageNo  当前页码，默认值为1
     * @param pageSize 每页记录数，默认值为10
     * @param req  HTTP请求对象，用于获取查询参数（主要用来构建查询条件）
     * @return 分页列表结果
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<IPage<SmartRole>> queryPageList(SmartRole smartRole,
                                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                  @RequestParam(name="pageSize", defaultValue="20") Integer pageSize,
                                                  HttpServletRequest req) {
        QueryWrapper<SmartRole> queryWrapper = QueryBuilder.initQueryWrapper(smartRole, req.getParameterMap());
        Page<SmartRole> page = new Page<SmartRole>(pageNo, pageSize);
        IPage<SmartRole> pageList = smartRoleService.page(page, queryWrapper);
        return Result.success("查询成功", pageList);
    }

    /**
     * 新增角色
     * @param params 角色实体对象，包含新增的角色信息
     * @return 操作结果，包含操作成功标志和操作结果数据
     */
    @RequestMapping(value = "insert", method = RequestMethod.POST)
    public Result<?> insert(@RequestBody @Valid SmartRole params) {
        SmartRole role = smartRoleService.insert(params);
        return role != null ? Result.success("新增成功", role) : Result.error("新增失败");
    }

    /**
     * 更新角色
     * @param params 角色实体对象，包含更新的角色信息
     * @return 操作结果，包含操作成功标志和操作结果数据
     */
    @RequestMapping(value = "update", method = RequestMethod.PUT)
    public Result<?> update(@RequestBody @Valid SmartRole params) {
        SmartRole smartRole = smartRoleService.update(params);
        return smartRole != null ? Result.success("更新成功", smartRole) : Result.error("更新失败");
    }

    /**
     * 删除角色
     * @param ids 角色ID，用于指定要删除的角色
     * @return 操作结果，包含操作成功标志和操作结果数据
     */
    @RequestMapping(value = "/delete/{ids}", method = RequestMethod.DELETE)
    public Result<?> delete(@PathVariable(name="ids", required=true) String ids) {
        // 解析ids参数，将其转换为角色ID列表
        List<String> roleIds = List.of(ids.split(","));
        boolean isSuccess = smartRoleService.deleteRoleByIds(roleIds);
        return isSuccess ? Result.success("删除成功") : Result.error("删除失败");
    }

    /**
     * 批量删除角色
     * @param ids 角色ID列表，用于指定要删除的角色
     * @return 操作结果，包含操作成功标志和操作结果数据
     */
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    public Result<?> deleteBatch(@RequestBody @Valid List<String> ids) {
        boolean isSuccess = smartRoleService.removeByIds(ids);
        return isSuccess ? Result.success("删除成功") : Result.error("删除失败");
    }



    /**
     * 根据ID查询角色详情
     * @param id 角色ID，用于指定要查询的角色
     * @return 操作结果，包含操作成功标志和操作结果数据
     */
    @RequestMapping(value = "/getRoleById/{id}", method = RequestMethod.GET)
    public Result<SmartRole> get(@PathVariable(name="id", required=true) String id) {
        SmartRole smartRole = smartRoleService.getById(id);
        return smartRole != null ? Result.success("查询成功", smartRole) : Result.error("查询失败");
    }
}