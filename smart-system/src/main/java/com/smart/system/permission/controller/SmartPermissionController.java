package com.smart.system.permission.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.common.core.result.Result;
import com.smart.common.database.query.QueryBuilder;
import com.smart.system.permission.entity.SmartPermission;
import com.smart.system.permission.service.SmartPermissionService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    private static final Logger log = LoggerFactory.getLogger(SmartPermissionController.class);

    @Resource
    private SmartPermissionService smartPermissionService;


    /**
     * 查询菜单权限分页列表
     * @param smartPermission 查询参数
     * @param pageNo  当前页码，默认值为1
     * @param pageSize 每页记录数，默认值为10
     * @param req  HTTP请求对象，用于获取查询参数
     * @return 分页列表结果
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<?> queryPageList(SmartPermission smartPermission, @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                       @RequestParam(name="pageSize", defaultValue="20") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<SmartPermission> queryWrapper = QueryBuilder.initQueryWrapper(smartPermission, req.getParameterMap());
        Page<SmartPermission> page = new Page<SmartPermission>(pageNo, pageSize);
        IPage<SmartPermission> pageList = smartPermissionService.page(page, queryWrapper);
        return Result.success(pageList);
    }




    /**
     * 新增或更新菜单权限
     * @param smartPermission 菜单权限实体对象，包含新增或更新的菜单权限信息
     * @return 操作结果，包含操作成功标志和操作结果数据
     */
    @RequestMapping(value = "insert", method = RequestMethod.POST)
    public Result<?> insert(@RequestBody @Valid SmartPermission smartPermission) {

        SmartPermission permission = smartPermissionService.insert(smartPermission);

        return Result.success("操作成功", permission);
    }


     /**
     * 更新菜单权限
     * @param smartPermission 菜单权限实体对象，包含更新的菜单权限信息
     * @return 操作结果，包含操作成功标志和操作结果数据
     */
    @RequestMapping(value = "update",method = RequestMethod.PUT)
    public Result<?> update(@RequestBody @Valid SmartPermission smartPermission) {
        boolean result = smartPermissionService.updateById(smartPermission);
        return Result.success("操作成功", result);
    }




     /**
     * 删除菜单权限
     * @param id 菜单权限ID，用于指定要删除的菜单权限
     * @return 操作结果，包含操作成功标志和操作结果数据
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<?>  delete(@RequestParam(name="id",required=true) String id) {
        boolean result = smartPermissionService.removeById(id);
        return Result.success("操作成功", result);
    }



     /**
     * 批量删除菜单权限
     * @param ids 菜单权限ID列表，用于指定要删除的菜单权限
     * @return 操作结果，包含操作成功标志和操作结果数据
     */
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    public Result<?> deleteBatch(@RequestBody @Valid List<String> ids) {
        boolean result = smartPermissionService.removeByIds(ids);
        return Result.success("操作成功", result);
    }




}