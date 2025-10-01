package com.smart.system.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smart.system.user.entity.SmartUser;

/**
 * 用户信息表Service接口
 * 定义用户业务逻辑层接口
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public interface SmartUserService extends IService<SmartUser> {

    /**
     * 新增用户
     * @param smartUser 用户实体对象
     * @return 新增后的用户对象
     */
    SmartUser insert(SmartUser smartUser);

    /**
     * 更新用户
     * @param smartUser 用户实体对象
     * @return 更新后的用户对象
     */
    SmartUser update(SmartUser smartUser);
}
