package com.smart.framework.database.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * 基础实体类
 * 包含通用字段：id、创建时间、创建人、更新时间、更新人等
 * 所有业务实体类都应该继承此类
 * 
 * @author smart
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class BaseEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键ID
     * 使用UUID作为主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    
    /**
     * 判断是否为新记录
     * 
     * @return 是否为新记录
     */
    @JsonIgnore
    public boolean isNew() {
        return this.id == null || this.id.isEmpty();
    }
    

    

}
