package com.smart.framework.database.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;


/**
 * 自定义分页拦截器配置类
 * 设置sql的limit为无限制，默认是500
 *
 * @author Smart Boot3
 * @since 1.0.0
 */
public class MyPaginationInnerInterceptor extends PaginationInnerInterceptor {

    /**
     * 创建分页拦截器并设置无限制
     * @return PaginationInnerInterceptor实例
     */
    public static PaginationInnerInterceptor createPaginationInterceptor() {
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
        // 设置sql的limit为无限制，默认是500
        paginationInterceptor.setMaxLimit(-1L);
        // 设置数据库类型为MySQL
        paginationInterceptor.setDbType(DbType.MYSQL);
        return paginationInterceptor;
    }
}