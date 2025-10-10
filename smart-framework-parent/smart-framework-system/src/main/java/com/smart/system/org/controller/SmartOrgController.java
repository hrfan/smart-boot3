package com.smart.system.org.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.framework.core.result.Result;
import com.smart.framework.database.query.QueryBuilder;
import com.smart.system.org.entity.SmartOrg;
import com.smart.system.org.service.SmartOrgService;
import com.smart.system.permission.vo.TreeSelect;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 组织信息管理控制器
 * 提供组织管理相关接口
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@RestController
@RequestMapping("/system/org")
public class SmartOrgController {

    private static final Logger log = LoggerFactory.getLogger(SmartOrgController.class);

    @Resource
    private SmartOrgService smartOrgService;

    /**
     * 查询组织信息分页列表
     * @param smartOrg 查询参数对象，用于构建查询条件，前端不需要传递
     * @param pageNo  当前页码，默认值为1
     * @param pageSize 每页记录数，默认值为20
     * @param req  HTTP请求对象，用于获取查询参数（主要用来构建查询条件）
     * @return 分页列表结果
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<IPage<SmartOrg>> queryPageList(SmartOrg smartOrg,
                                                 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                 @RequestParam(name="pageSize", defaultValue="20") Integer pageSize,
                                                 HttpServletRequest req) {
        log.info("查询组织信息分页列表，页码：{}，每页大小：{}", pageNo, pageSize);
        QueryWrapper<SmartOrg> queryWrapper = QueryBuilder.initQueryWrapper(smartOrg, req.getParameterMap());
        queryWrapper.eq("del_flag", "0");
        queryWrapper.orderByAsc("org_order");
        Page<SmartOrg> page = new Page<SmartOrg>(pageNo, pageSize);
        IPage<SmartOrg> pageList = smartOrgService.page(page, queryWrapper);
        return Result.success("查询成功", pageList);
    }

    /**
     * 新增组织信息
     * @param params 组织实体对象，包含新增的组织信息
     * @return 操作结果，包含操作成功标志和操作结果数据
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result<?> insert(@RequestBody @Valid SmartOrg params) {
        log.info("新增组织信息：{}", params.getOrgName());
        SmartOrg smartOrg = smartOrgService.insert(params);
        return smartOrg != null ? Result.success("新增成功", smartOrg) : Result.error("新增失败");
    }

    /**
     * 更新组织信息
     * @param id 组织ID
     * @param params 组织实体对象，包含更新的组织信息
     * @return 操作结果，包含操作成功标志和操作结果数据
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public Result<?> update(@PathVariable(name="id", required=true) String id, @RequestBody @Valid SmartOrg params) {
        log.info("更新组织信息，ID：{}，名称：{}", id, params.getOrgName());
        SmartOrg smartOrg = smartOrgService.update(params);
        return smartOrg != null ? Result.success("更新成功", smartOrg) : Result.error("更新失败");
    }

    /**
     * 删除组织信息
     * @param id 组织ID，用于指定要删除的组织
     * @return 操作结果，包含操作成功标志和操作结果数据
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<?> delete(@RequestParam(name="id", required=true) String id) {
        log.info("删除组织信息，ID：{}", id);
        boolean isSuccess = smartOrgService.removeById(id);
        return isSuccess ? Result.success("删除成功") : Result.error("删除失败");
    }

    /**
     * 批量删除组织信息
     * @param ids 组织ID列表，用于指定要删除的组织
     * @return 操作结果，包含操作成功标志和操作结果数据
     */
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    public Result<?> deleteBatch(@RequestBody @Valid List<String> ids) {
        log.info("批量删除组织信息，数量：{}", ids.size());
        boolean isSuccess = smartOrgService.removeByIds(ids);
        return isSuccess ? Result.success("删除成功") : Result.error("删除失败");
    }


    /**
     * 获取组织树形选择器列表
     * @param smartOrg 查询条件
     * @return 组织树形选择器列表
     */
    @RequestMapping(value = "/getOrgTreeSelect", method = RequestMethod.GET)
    public Result<List<TreeSelect>> getOrgTreeSelect(SmartOrg smartOrg) {
        log.info("获取组织树形选择器列表");
        List<TreeSelect> treeList = smartOrgService.getOrgTreeSelect(smartOrg);
        return Result.success("查询成功", treeList);
    }






}
