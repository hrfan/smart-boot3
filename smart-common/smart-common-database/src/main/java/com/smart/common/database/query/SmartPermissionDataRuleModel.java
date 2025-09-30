package com.smart.common.database.query;

import java.io.Serializable;

/**
 * 权限数据规则模型
 * 用于数据权限控制
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public class SmartPermissionDataRuleModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 规则ID
     */
    private String id;

    /**
     * 规则字段名
     */
    private String ruleColumn;

    /**
     * 规则条件（eq、ne、gt、lt、in等）
     */
    private String ruleConditions;

    /**
     * 规则值
     */
    private String ruleValue;

    /**
     * 规则描述
     */
    private String ruleDescription;

    public SmartPermissionDataRuleModel() {
    }

    public SmartPermissionDataRuleModel(String ruleColumn, String ruleConditions, String ruleValue) {
        this.ruleColumn = ruleColumn;
        this.ruleConditions = ruleConditions;
        this.ruleValue = ruleValue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRuleColumn() {
        return ruleColumn;
    }

    public void setRuleColumn(String ruleColumn) {
        this.ruleColumn = ruleColumn;
    }

    public String getRuleConditions() {
        return ruleConditions;
    }

    public void setRuleConditions(String ruleConditions) {
        this.ruleConditions = ruleConditions;
    }

    public String getRuleValue() {
        return ruleValue;
    }

    public void setRuleValue(String ruleValue) {
        this.ruleValue = ruleValue;
    }

    public String getRuleDescription() {
        return ruleDescription;
    }

    public void setRuleDescription(String ruleDescription) {
        this.ruleDescription = ruleDescription;
    }

    @Override
    public String toString() {
        return "SmartPermissionDataRuleModel{" +
                "id='" + id + '\'' +
                ", ruleColumn='" + ruleColumn + '\'' +
                ", ruleConditions='" + ruleConditions + '\'' +
                ", ruleValue='" + ruleValue + '\'' +
                ", ruleDescription='" + ruleDescription + '\'' +
                '}';
    }
}