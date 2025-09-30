package com.smart.common.database.query;

/**
 * 通用常量类
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public class CommonConstant {

    /**
     * 字典翻译文本后缀
     */
    public static final String DICT_TEXT_SUFFIX = "_dictText";

    /**
     * 删除标记字段
     */
    public static final String DEL_FLAG = "delFlag";

    /**
     * 创建者字段
     */
    public static final String CREATE_BY = "createBy";

    /**
     * 创建时间字段
     */
    public static final String CREATE_TIME = "createTime";

    /**
     * 更新者字段
     */
    public static final String UPDATE_BY = "updateBy";

    /**
     * 更新时间字段
     */
    public static final String UPDATE_TIME = "updateTime";

    /**
     * 数据权限查询字段前缀
     */
    public static final String DATA_SCOPE_PREFIX = "scope_";

    /**
     * SQL注入攻击检测正则
     */
    public static final String SQL_INJECTION_REGEX = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
            + "(\\b(select|update|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";

}