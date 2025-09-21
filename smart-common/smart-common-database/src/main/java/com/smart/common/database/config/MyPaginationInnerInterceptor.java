package com.smart.common.database.config;


import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

/**
 * 自定义分页拦截器
 * 设置sql的limit为无限制，默认是500
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public class MyPaginationInnerInterceptor extends PaginationInnerInterceptor {
    
    /**
     * 构造函数
     * 设置分页参数
     */
    public MyPaginationInnerInterceptor() {
        // 设置sql的limit为无限制，默认是500
        this.setMaxLimit(-1L);
        // 溢出总页数后是否进行处理
        this.setOverflow(true);
    }
}
