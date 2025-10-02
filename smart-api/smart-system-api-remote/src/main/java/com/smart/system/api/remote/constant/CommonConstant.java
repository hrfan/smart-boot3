package com.smart.system.api.remote.constant;

/**
 * 微服务调用通用常量
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public class CommonConstant {

    /**
     * 系统服务名称
     */
    public static final String SYSTEM_SERVICE_NAME = "smart-system";

    /**
     * 系统服务API前缀
     */
    public static final String PREFIX_SYSTEM = "/system";

    /**
     * 用户服务名称
     */
    public static final String USER_SERVICE_NAME = "smart-user";

    /**
     * 用户服务API前缀
     */
    public static final String PREFIX_USER = "/api";

    /**
     * 订单服务名称
     */
    public static final String ORDER_SERVICE_NAME = "smart-order";

    /**
     * 订单服务API前缀
     */
    public static final String PREFIX_ORDER = "/api";

    /**
     * 商品服务名称
     */
    public static final String PRODUCT_SERVICE_NAME = "smart-product";

    /**
     * 商品服务API前缀
     */
    public static final String PREFIX_PRODUCT = "/api";

    /**
     * 默认超时时间（毫秒）
     */
    public static final int DEFAULT_TIMEOUT = 5000;

    /**
     * 默认重试次数
     */
    public static final int DEFAULT_RETRY_COUNT = 3;
}
