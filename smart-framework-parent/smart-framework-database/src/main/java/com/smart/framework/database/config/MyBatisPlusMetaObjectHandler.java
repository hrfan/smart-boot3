package com.smart.framework.database.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * MyBatis Plus自动填充处理器
 * 自动填充创建时间、创建人、更新时间、更新人等字段
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Component
public class MyBatisPlusMetaObjectHandler implements MetaObjectHandler {
    
    private static final Logger log = LoggerFactory.getLogger(MyBatisPlusMetaObjectHandler.class);
    
    /**
     * 插入时自动填充
     * 
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("开始插入填充...");
        
        // 填充创建时间
        this.strictInsertFill(metaObject, "createTime", Timestamp.class, new Timestamp(System.currentTimeMillis()));
        
        // 填充更新时间
        this.strictInsertFill(metaObject, "updateTime", Timestamp.class, new Timestamp(System.currentTimeMillis()));
        
        // 填充版本号
        this.strictInsertFill(metaObject, "version", Integer.class, 1);
        
        // 填充逻辑删除标识
        this.strictInsertFill(metaObject, "deleted", Integer.class, 0);
        
        // 填充创建人和更新人（如果为空）
        Object createByObj = getFieldValByName("createBy", metaObject);
        String createBy = createByObj != null ? createByObj.toString() : null;
        if (createBy == null || createBy.isEmpty()) {
            this.strictInsertFill(metaObject, "createBy", String.class, getCurrentUser());
        }
        
        Object createUserNameObj = getFieldValByName("createUserName", metaObject);
        String createUserName = createUserNameObj != null ? createUserNameObj.toString() : null;
        if (createUserName == null || createUserName.isEmpty()) {
            this.strictInsertFill(metaObject, "createUserName", String.class, getCurrentUserName());
        }
        
        Object updateByObj = getFieldValByName("updateBy", metaObject);
        String updateBy = updateByObj != null ? updateByObj.toString() : null;
        if (updateBy == null || updateBy.isEmpty()) {
            this.strictInsertFill(metaObject, "updateBy", String.class, getCurrentUser());
        }
        
        Object updateUserNameObj = getFieldValByName("updateUserName", metaObject);
        String updateUserName = updateUserNameObj != null ? updateUserNameObj.toString() : null;
        if (updateUserName == null || updateUserName.isEmpty()) {
            this.strictInsertFill(metaObject, "updateUserName", String.class, getCurrentUserName());
        }
        
        // 填充租户ID（如果为空）
        Object tenantIdObj = getFieldValByName("tenantId", metaObject);
        String tenantId = tenantIdObj != null ? tenantIdObj.toString() : null;
        if (tenantId == null || tenantId.isEmpty()) {
            this.strictInsertFill(metaObject, "tenantId", String.class, getCurrentTenantId());
        }
    }
    
    /**
     * 更新时自动填充
     * 
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("开始更新填充...");
        
        // 填充更新时间
        this.strictUpdateFill(metaObject, "updateTime", Timestamp.class, new Timestamp(System.currentTimeMillis()));
        
        // 填充更新人
        this.strictUpdateFill(metaObject, "updateBy", String.class, getCurrentUser());
        this.strictUpdateFill(metaObject, "updateUserName", String.class, getCurrentUserName());
        
        // 增加版本号
        Object versionObj = getFieldValByName("version", metaObject);
        Integer version = versionObj instanceof Integer ? (Integer) versionObj : null;
        if (version != null) {
            this.strictUpdateFill(metaObject, "version", Integer.class, version + 1);
        }
    }
    
    /**
     * 获取当前用户ID
     * 这里可以从SecurityContext或其他地方获取
     * 
     * @return 当前用户ID
     */
    private String getCurrentUser() {
        // TODO: 从SecurityContext或其他地方获取当前用户ID
        // 这里先返回默认值
        return "system";
    }
    
    /**
     * 获取当前用户名
     * 这里可以从SecurityContext或其他地方获取
     * 
     * @return 当前用户名
     */
    private String getCurrentUserName() {
        // TODO: 从SecurityContext或其他地方获取当前用户名
        // 这里先返回默认值
        return "系统用户";
    }
    
    /**
     * 获取当前租户ID
     * 这里可以从ThreadLocal或其他地方获取
     * 
     * @return 当前租户ID
     */
    private String getCurrentTenantId() {
        // TODO: 从ThreadLocal或其他地方获取当前租户ID
        // 这里先返回默认值
        return "default";
    }
}