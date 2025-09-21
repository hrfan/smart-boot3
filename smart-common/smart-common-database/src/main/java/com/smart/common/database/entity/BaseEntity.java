package com.smart.common.database.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


import java.io.Serializable;
import java.util.Date;


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
     * 创建时间
     * 自动填充创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
    
    /**
     * 创建人
     * 自动填充创建人
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;
    
    /**
     * 创建人姓名
     * 自动填充创建人姓名
     */
    @TableField(value = "create_user_name", fill = FieldFill.INSERT)
    private String createUserName;
    
    /**
     * 更新时间
     * 自动填充更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    
    /**
     * 更新人
     * 自动填充更新人
     */
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;
    
    /**
     * 更新人姓名
     * 自动填充更新人姓名
     */
    @TableField(value = "update_user_name", fill = FieldFill.INSERT_UPDATE)
    private String updateUserName;
    

    /**
     * 租户ID
     * 用于多租户场景
     */
    @TableField(value = "tenant_id")
    private String tenantId;
    

    
    /**
     * 构造函数
     */
    public BaseEntity() {
        // 默认构造函数
    }
    
    /**
     * 设置创建信息
     * 
     * @param createBy 创建人
     * @param createUserName 创建人姓名
     */
    public void setCreateInfo(String createBy, String createUserName) {
        this.createBy = createBy;
        this.createUserName = createUserName;
        this.createTime = new Date(System.currentTimeMillis());
        this.updateTime = this.createTime;
        this.updateBy = createBy;
        this.updateUserName = createUserName;
    }
    
    /**
     * 设置更新信息
     * 
     * @param updateBy 更新人
     * @param updateUserName 更新人姓名
     */
    public void setUpdateInfo(String updateBy, String updateUserName) {
        this.updateBy = updateBy;
        this.updateUserName = updateUserName;
        this.updateTime = new Date(System.currentTimeMillis());
    }
    
    /**
     * 设置创建和更新信息
     * 
     * @param userId 用户ID
     * @param userName 用户姓名
     */
    public void setCreateAndUpdateInfo(String userId, String userName) {
        setCreateInfo(userId, userName);
    }
    
    /**
     * 判断是否为新记录
     * 
     * @return 是否为新记录
     */
    public boolean isNew() {
        return this.id == null || this.id.isEmpty();
    }
    

    

}
