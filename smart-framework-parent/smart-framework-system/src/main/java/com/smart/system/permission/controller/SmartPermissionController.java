package com.smart.system.permission.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.framework.common.util.SecurityUtils;
import com.smart.framework.core.result.Result;
import com.smart.framework.database.query.QueryBuilder;
import com.smart.system.permission.entity.SmartPermission;
import com.smart.system.permission.service.SmartPermissionService;
import com.smart.system.permission.vo.RouterVo;
import com.smart.system.permission.vo.TreeSelect;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 菜单权限控制器
 * 提供菜单权限管理相关接口
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@RestController
@RequestMapping("/system/permission")
public class SmartPermissionController {

    @Resource
    private SmartPermissionService smartPermissionService;




    /**
     * 获取路由信息
     * 参考mengyuan项目的getRouters方法
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public Result<List<RouterVo>> getRouters() {
        List<RouterVo> routers = smartPermissionService.getRouters();
        return Result.success("获取成功", routers);
    }

    /**
     * 查询菜单权限分页列表
     * @param smartPermission 查询参数对象，用于构建查询条件，前端不需要传递
     * @param pageNo  当前页码，默认值为1
     * @param pageSize 每页记录数，默认值为10
     * @param req  HTTP请求对象，用于获取查询参数（主要用来构建查询条件）
     * @return 分页列表结果
     */
    @RequestMapping(value = "/listPage", method = RequestMethod.GET)
    public Result<IPage<SmartPermission>> queryPageList(SmartPermission smartPermission,
                                                        @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                        @RequestParam(name="pageSize", defaultValue="20") Integer pageSize,
                                                        HttpServletRequest req) {
        QueryWrapper<SmartPermission> queryWrapper = QueryBuilder.initQueryWrapper(smartPermission, req.getParameterMap());
        Page<SmartPermission> page = new Page<SmartPermission>(pageNo, pageSize);
        IPage<SmartPermission> pageList = smartPermissionService.page(page, queryWrapper);
        return Result.success("查询成功", pageList);
    }





    /**
     * 查询菜单权限列表
     * @param smartPermission 查询参数对象，用于构建查询条件
     * @return 菜单权限列表结果
     */
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Result<List<SmartPermission>> list(@RequestBody SmartPermission smartPermission) {
        List<SmartPermission> list = smartPermissionService.getList(smartPermission);
        return Result.success("查询成功", list);
    }




    /**
     * 新增或更新菜单权限
     * @param params 菜单权限实体对象，包含新增或更新的菜单权限信息
     * @return 操作结果，包含操作成功标志和操作结果数据
     */
    @RequestMapping(value = "insert", method = RequestMethod.POST)
    public Result<?> insert(@RequestBody @Valid SmartPermission params) {
        SmartPermission permission = smartPermissionService.insert(params);
        return permission != null ? Result.success("新增成功", permission) : Result.error("新增失败");
    }


     /**
     * 更新菜单权限
     * @param params 菜单权限实体对象，包含更新的菜单权限信息
     * @return 操作结果，包含操作成功标志和操作结果数据
     */
    @RequestMapping(value = "update",method = RequestMethod.PUT)
    public Result<?> update(@RequestBody @Valid SmartPermission params) {
        SmartPermission smartPermission = smartPermissionService.update(params);
        return smartPermission != null ? Result.success("更新成功", smartPermission) : Result.error("更新失败");
    }


    /**
     * 根据id查询菜单权限详情
     * @param id 菜单权限ID，用于指定要查询的菜单权限
     * @return 菜单权限详情结果
     */
    @RequestMapping(value = "/getPermissionById/{id}", method = RequestMethod.GET)
    public Result<SmartPermission> detail(@PathVariable(name="id",required=true) String id) {
        SmartPermission smartPermission = smartPermissionService.getById(id);
        return Result.success("查询成功", smartPermission);
    }


    /**
     * 获取菜单树列表 不包含按钮
     */
    @RequestMapping(value = "/listTree", method = RequestMethod.POST)
    public Result<List<SmartPermission>> listTree(@RequestBody SmartPermission smartPermission) {
        // 查询菜单树
        List<SmartPermission> menus = smartPermissionService.selectMenuTree(smartPermission);
        return Result.success("查询成功", menus);
    }





    /**
     * 删除菜单权限
     * @param id 菜单权限ID，用于指定要删除的菜单权限
     * @return 操作结果，包含操作成功标志和操作结果数据
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public Result<?>  delete(@PathVariable(name="id",required=true) String id) {
        boolean isSuccess = smartPermissionService.removeById(id);
        return isSuccess ? Result.success("删除成功") : Result.error("删除失败");
    }



     /**
     * 批量删除菜单权限
     * @param ids 菜单权限ID列表，用于指定要删除的菜单权限
     * @return 操作结果，包含操作成功标志和操作结果数据
     */
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    public Result<?> deleteBatch(@RequestBody @Valid List<String> ids) {
        boolean isSuccess = smartPermissionService.removeByIds(ids);
        return isSuccess ? Result.success("删除成功") : Result.error("删除失败");
    }


    /**
     * 根据parentId获取最大序号
     * @param parentId 父菜单权限ID，用于指定要查询的父菜单权限
     * @return 最大序号结果
     */
     @RequestMapping(value = "/getMaxSortNo/{parentId}", method = RequestMethod.GET)
    public Result<Integer> getMaxSortNo(@PathVariable(name="parentId",required=true) String parentId) {
        Integer maxSortNo = smartPermissionService.getMaxSortNo(parentId);
        return Result.success("查询成功", maxSortNo);
    }


    /**
     * 根据角色id查询对应菜单权限列表
     * @param roleId 角色ID，用于指定要查询的角色
     * @return 菜单权限列表结果
     */
     @RequestMapping(value = "/getPermissionByRoleId/{roleId}", method = RequestMethod.GET)
    public Result<Map<String, Object>> getPermissionByRoleId(@PathVariable(name="roleId",required=true) String roleId) {
        Map<String, Object> map = smartPermissionService.getPermissionByRoleId(roleId);
        return Result.success("查询成功", map);
    }



    /**
     * 根据角色id查询对应菜单权限列表 不包含按钮
     * @param roleId 角色ID，用于指定要查询的角色
     * @return 菜单权限列表结果
     */
    @RequestMapping(value = "/getPermissionNoButtonByRoleId/{roleId}", method = RequestMethod.GET)
    public Result<Map<String, Object>> getPermissionNoButtonByRoleId(@PathVariable(name="roleId",required=true) String roleId) {
        Map<String, Object> map = smartPermissionService.getPermissionNoButtonByRoleId(roleId);
        return Result.success("查询成功", map);
    }


    /**
     * 根据角色id和权限id查询对应按钮详情列表
     * @param roleId 角色ID，用于指定要查询的角色
     * @param permissionId 权限ID，用于指定要查询的权限
     * @return 按钮详情列表结果(保存当前角色已经分配的按钮权限)
     */
     @RequestMapping(value = "/getButtonListByPermissionId/{roleId}/{permissionId}", method = RequestMethod.GET)
    public Result<Map<String, Object>> getButtonListByPermissionId(@PathVariable(name="roleId",required=true) String roleId,
                                                                   @PathVariable(name="permissionId",required=true) String permissionId) {
         Map<String, Object> map = smartPermissionService.getButtonListByPermissionId(roleId, permissionId);
        return Result.success("查询成功", map);
    }


    /**
     * 获取所有的权限列表
     * @return 所有的权限列表结果
     */
     @RequestMapping(value = "/treeSelect", method = RequestMethod.GET)
    public Result<List<TreeSelect>> listAll() {
         List<TreeSelect> list = smartPermissionService.getTreeSelect();
         return Result.success("查询成功", list);
    }



}