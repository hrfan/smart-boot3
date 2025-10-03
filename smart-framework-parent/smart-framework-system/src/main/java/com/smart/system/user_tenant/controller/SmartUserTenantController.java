package com.smart.system.user_tenant.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.framework.core.result.Result;
import com.smart.framework.database.query.QueryBuilder;
import com.smart.system.user_tenant.entity.SmartUserTenant;
import com.smart.system.user_tenant.service.SmartUserTenantService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户租户管理控制器
 * 提供用户租户关联管理相关接口
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@RestController
@RequestMapping("/system/user-tenant")
public class SmartUserTenantController {

    private static final Logger log = LoggerFactory.getLogger(SmartUserTenantController.class);

    @Resource
    private SmartUserTenantService smartUserTenantService;

    /**
     * 查询用户租户关联分页列表
     * @param smartUserTenant 查询参数对象，用于构建查询条件，前端不需要传递
     * @param pageNo  当前页码，默认值为1
     * @param pageSize 每页记录数，默认值为10
     * @param req  HTTP请求对象，用于获取查询参数（主要用来构建查询条件）
     * @return 分页列表结果
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<IPage<SmartUserTenant>> queryPageList(SmartUserTenant smartUserTenant,
                                                        @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                        @RequestParam(name="pageSize", defaultValue="20") Integer pageSize,
                                                        HttpServletRequest req) {
        QueryWrapper<SmartUserTenant> queryWrapper = QueryBuilder.initQueryWrapper(smartUserTenant, req.getParameterMap());
        Page<SmartUserTenant> page = new Page<SmartUserTenant>(pageNo, pageSize);
        IPage<SmartUserTenant> pageList = smartUserTenantService.page(page, queryWrapper);
        return Result.success("查询成功", pageList);
    }

    /**
     * 新增用户租户关联
     * @param params 用户租户关联实体对象，包含新增的用户租户关联信息
     * @return 操作结果，包含操作成功标志和操作结果数据
     */
    @RequestMapping(value = "insert", method = RequestMethod.POST)
    public Result<?> insert(@RequestBody @Valid SmartUserTenant params) {
        SmartUserTenant userTenant = smartUserTenantService.insert(params);
        return userTenant != null ? Result.success("新增成功", userTenant) : Result.error("新增失败");
    }

    /**
     * 更新用户租户关联
     * @param id 用户租户关联ID
     * @param params 用户租户关联实体对象，包含更新的用户租户关联信息
     * @return 操作结果，包含操作成功标志和操作结果数据
     */
    @RequestMapping(value = "update/{id}", method = RequestMethod.PUT)
    public Result<?> update(@PathVariable(name="id", required=true) String id, @RequestBody @Valid SmartUserTenant params) {
        SmartUserTenant smartUserTenant = smartUserTenantService.update(params);
        return smartUserTenant != null ? Result.success("更新成功", smartUserTenant) : Result.error("更新失败");
    }

    /**
     * 删除用户租户关联
     * @param id 用户租户关联ID，用于指定要删除的用户租户关联
     * @return 操作结果，包含操作成功标志和操作结果数据
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<?> delete(@RequestParam(name="id", required=true) String id) {
        boolean isSuccess = smartUserTenantService.removeById(id);
        return isSuccess ? Result.success("删除成功") : Result.error("删除失败");
    }

    /**
     * 批量删除用户租户关联
     * @param ids 用户租户关联ID列表，用于指定要删除的用户租户关联
     * @return 操作结果，包含操作成功标志和操作结果数据
     */
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    public Result<?> deleteBatch(@RequestBody @Valid List<String> ids) {
        boolean isSuccess = smartUserTenantService.removeByIds(ids);
        return isSuccess ? Result.success("删除成功") : Result.error("删除失败");
    }
}
