package com.smart.system.tenant.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.common.core.result.Result;
import com.smart.common.database.query.QueryBuilder;
import com.smart.system.tenant.entity.SmartTenant;
import com.smart.system.tenant.service.SmartTenantService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 租户管理控制器
 * 提供租户管理相关接口
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@RestController
@RequestMapping("/system/tenant")
public class SmartTenantController {

    private static final Logger log = LoggerFactory.getLogger(SmartTenantController.class);

    @Resource
    private SmartTenantService smartTenantService;

    /**
     * 查询租户分页列表
     * @param smartTenant 查询参数对象，用于构建查询条件，前端不需要传递
     * @param pageNo  当前页码，默认值为1
     * @param pageSize 每页记录数，默认值为10
     * @param req  HTTP请求对象，用于获取查询参数（主要用来构建查询条件）
     * @return 分页列表结果
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<IPage<SmartTenant>> queryPageList(SmartTenant smartTenant,
                                                    @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                    @RequestParam(name="pageSize", defaultValue="20") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<SmartTenant> queryWrapper = QueryBuilder.initQueryWrapper(smartTenant, req.getParameterMap());
        Page<SmartTenant> page = new Page<SmartTenant>(pageNo, pageSize);
        IPage<SmartTenant> pageList = smartTenantService.page(page, queryWrapper);
        return Result.success("查询成功", pageList);
    }

    /**
     * 新增租户
     * @param params 租户实体对象，包含新增的租户信息
     * @return 操作结果，包含操作成功标志和操作结果数据
     */
    @RequestMapping(value = "insert", method = RequestMethod.POST)
    public Result<?> insert(@RequestBody @Valid SmartTenant params) {
        SmartTenant tenant = smartTenantService.insert(params);
        return tenant != null ? Result.success("新增成功", tenant) : Result.error("新增失败");
    }

    /**
     * 更新租户
     * @param id 租户ID
     * @param params 租户实体对象，包含更新的租户信息
     * @return 操作结果，包含操作成功标志和操作结果数据
     */
    @RequestMapping(value = "update/{id}", method = RequestMethod.PUT)
    public Result<?> update(@PathVariable(name="id", required=true) String id, @RequestBody @Valid SmartTenant params) {
        SmartTenant smartTenant = smartTenantService.update(params);
        return smartTenant != null ? Result.success("更新成功", smartTenant) : Result.error("更新失败");
    }

    /**
     * 删除租户
     * @param id 租户ID，用于指定要删除的租户
     * @return 操作结果，包含操作成功标志和操作结果数据
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<?> delete(@RequestParam(name="id", required=true) String id) {
        boolean isSuccess = smartTenantService.removeById(id);
        return isSuccess ? Result.success("删除成功") : Result.error("删除失败");
    }

    /**
     * 批量删除租户
     * @param ids 租户ID列表，用于指定要删除的租户
     * @return 操作结果，包含操作成功标志和操作结果数据
     */
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    public Result<?> deleteBatch(@RequestBody @Valid List<String> ids) {
        boolean isSuccess = smartTenantService.removeByIds(ids);
        return isSuccess ? Result.success("删除成功") : Result.error("删除失败");
    }
}
