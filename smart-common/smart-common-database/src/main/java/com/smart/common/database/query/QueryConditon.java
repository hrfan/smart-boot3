package com.smart.common.database.query;

public class QueryConditon {
    private String name;
    private Object value;
    private QueryRuleEnum queryRuleEnum;

    public QueryConditon(String name, Object value, QueryRuleEnum queryRuleEnum) {
        this.name = name;
        this.value = value;
        this.queryRuleEnum = queryRuleEnum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public QueryRuleEnum getQueryRuleEnum() {
        return queryRuleEnum;
    }

    public void setQueryRuleEnum(QueryRuleEnum queryRuleEnum) {
        this.queryRuleEnum = queryRuleEnum;
    }
}

