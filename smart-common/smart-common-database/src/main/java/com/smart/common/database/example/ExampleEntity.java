package com.smart.common.database.example;

import com.smart.common.database.entity.BaseEntity;
import com.smart.common.database.mapper.BaseMapper;
import com.smart.common.database.service.BaseService;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 示例实体类
 * 用于演示如何使用database模块
 * 
 * @author smart
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ExampleEntity extends BaseEntity {
    
    /**
     * 示例字段1
     */
    private String field1;
    
    /**
     * 示例字段2
     */
    private String field2;
    
    /**
     * 示例字段3
     */
    private Integer field3;
}

/**
 * 示例Mapper接口
 * 演示如何继承BaseMapper
 * 
 * @author smart
 * @since 1.0.0
 */
interface ExampleMapper extends BaseMapper<ExampleEntity> {
    // 可以添加自定义方法
}

/**
 * 示例Service类
 * 演示如何继承BaseService
 * 
 * @author smart
 * @since 1.0.0
 */
class ExampleService extends BaseService<ExampleEntity, ExampleMapper> {
    
    /**
     * 示例业务方法
     */
    public void exampleBusinessMethod() {
        // 使用继承的CRUD方法
        ExampleEntity entity = new ExampleEntity();
        entity.setField1("test");
        entity.setField2("example");
        entity.setField3(123);
        
        // 插入
        insert(entity);
        
        // 查询
        ExampleEntity found = selectById(entity.getId());
        
        // 更新
        found.setField1("updated");
        updateById(found);
        
        // 删除
        deleteById(found.getId());
    }
}
