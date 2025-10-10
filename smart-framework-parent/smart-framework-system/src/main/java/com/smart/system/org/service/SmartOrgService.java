package com.smart.system.org.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smart.system.org.entity.SmartOrg;
import com.smart.system.permission.vo.TreeSelect;

import java.util.List;

/**
 * 组织信息表Service接口
 * 定义组织业务逻辑层接口
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public interface SmartOrgService extends IService<SmartOrg> {

    /**
     * 新增组织信息
     * @param smartOrg 组织实体对象
     * @return 新增后的组织对象
     */
    SmartOrg insert(SmartOrg smartOrg);

    /**
     * 更新组织信息
     * @param smartOrg 组织实体对象
     * @return 更新后的组织对象
     */
    SmartOrg update(SmartOrg smartOrg);


    /**
     * 获取组织树形选择器列表
     * @param smartOrg 查询条件，如果为空则返回所有组织树
     * @return TreeSelect格式的组织树
     */
    List<TreeSelect> getOrgTreeSelect(SmartOrg smartOrg);


}
