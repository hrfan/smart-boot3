package com.smart.system.permission.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 菜单权限规则表
 */
@Data
public class SmartPermissionDataRuleModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 对应的菜单id
     */
    private String permissionId;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 字段
     */
    private String ruleColumn;

    /**
     * 条件
     */
    private String ruleConditions;

    /**
     * 规则值
     */
    private String ruleValue;

    /**
     * 状态值 1有效 0无效
     */
    private String status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private String updateBy;



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SmartPermissionDataRuleModel model = (SmartPermissionDataRuleModel) o;
        return Objects.equals(id, model.id)&&
                Objects.equals(permissionId, model.permissionId)&&
                Objects.equals(ruleConditions, model.ruleConditions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, permissionId,ruleConditions);
    }
}
