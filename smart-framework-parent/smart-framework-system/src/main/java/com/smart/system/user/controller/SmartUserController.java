package com.smart.system.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.framework.common.util.SecurityUtils;
import com.smart.framework.core.result.Result;
import com.smart.framework.core.util.StringUtil;
import com.smart.framework.database.query.QueryBuilder;
import com.smart.system.permission.service.SmartPermissionService;
import com.smart.system.user.entity.SmartUser;
import com.smart.system.user.service.SmartUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 用户信息控制器
 * 提供用户管理相关接口
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@RestController
@RequestMapping("/system/user")
public class SmartUserController {

    private static final Logger log = LoggerFactory.getLogger(SmartUserController.class);

    @Resource
    private SmartUserService smartUserService;

    @Resource
    private SmartPermissionService smartPermissionService;


    /**
     * 查询用户分页列表
     * @param smartUser 查询参数对象，用于构建查询条件，前端不需要传递
     * @param pageNo  当前页码，默认值为1
     * @param pageSize 每页记录数，默认值为10
     * @param req  HTTP请求对象，用于获取查询参数（主要用来构建查询条件）
     * @return 分页列表结果
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<IPage<SmartUser>> queryPageList(SmartUser smartUser,
                                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                  @RequestParam(name="pageSize", defaultValue="20") Integer pageSize,
                                                  HttpServletRequest req) {
        QueryWrapper<SmartUser> queryWrapper = QueryBuilder.initQueryWrapper(smartUser, req.getParameterMap());
        Page<SmartUser> page = new Page<SmartUser>(pageNo, pageSize);
        IPage<SmartUser> pageList = smartUserService.page(page, queryWrapper);
        return Result.success("查询成功", pageList);
    }

    /**
     * 新增用户
     * @param params 用户实体对象，包含新增的用户信息
     * @return 操作结果，包含操作成功标志和操作结果数据
     */
    @RequestMapping(value = "insert", method = RequestMethod.POST)
    public Result<?> insert(@RequestBody @Valid SmartUser params) {
        SmartUser user = smartUserService.insert(params);
        return user != null ? Result.success("新增成功", user) : Result.error("新增失败");
    }

    /**
     * 更新用户
     * @param id 用户ID
     * @param params 用户实体对象，包含更新的用户信息
     * @return 操作结果，包含操作成功标志和操作结果数据
     */
    @RequestMapping(value = "update/{id}", method = RequestMethod.PUT)
    public Result<?> update(@PathVariable(name="id", required=true) String id, @RequestBody @Valid SmartUser params) {
        SmartUser smartUser = smartUserService.update(params);
        return smartUser != null ? Result.success("更新成功", smartUser) : Result.error("更新失败");
    }

    /**
     * 删除用户
     * @param id 用户ID，用于指定要删除的用户
     * @return 操作结果，包含操作成功标志和操作结果数据
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<?> delete(@RequestParam(name="id", required=true) String id) {
        boolean isSuccess = smartUserService.removeById(id);
        return isSuccess ? Result.success("删除成功") : Result.error("删除失败");
    }

    /**
     * 批量删除用户
     * @param ids 用户ID列表，用于指定要删除的用户
     * @return 操作结果，包含操作成功标志和操作结果数据
     */
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    public Result<?> deleteBatch(@RequestBody @Valid List<String> ids) {
        boolean isSuccess = smartUserService.removeByIds(ids);
        return isSuccess ? Result.success("删除成功") : Result.error("删除失败");
    }


    @GetMapping("getInfo")
    public Result<?> getInfo() {
        String userId = SecurityUtils.getCurrentUserId();
        if (StringUtil.isBlank(userId)){
            return Result.error("用户未登录");
        }
        SmartUser user = smartUserService.getById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        // 角色集合
        List<String> roles = smartPermissionService.findRoleByUserId(user.getId());
        // 权限集合
        // 获取后端数据库中的权限集合
        List<String> permissions = smartPermissionService.findByUserId(user.getId());
        // TODO 对比前端传递的权限集合和后端数据库中的权限集合，是否一致 ,如果不一致,则更新前端传递的权限集合到后端数据库中
        // if (!user.getPermissionList().equals(permissions)) {
        //     loginUser.setPermissions(permissions);
        //     tokenService.refreshToken(loginUser);
        // }

        Map<String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("roles", roles);
        data.put("permissions", permissions);

        return Result.success("查询成功", data);
    }
}