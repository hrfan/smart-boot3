package com.smart.system.api.remote.util;

import com.smart.common.core.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST客户端工具类
 * 提供结果处理的工具方法
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public class RestClientUtils {

    private static final Logger log = LoggerFactory.getLogger(RestClientUtils.class);

    /**
     * 获取结果数据，如果失败则返回null
     * 
     * @param result 结果对象
     * @param <T> 泛型类型
     * @return 数据或null
     */
    public static <T> T getResult(Result<T> result) {
        if (result == null) {
            log.warn("结果对象为空");
            return null;
        }
        
        if (!result.isSuccess()) {
            log.warn("调用失败: code={}, message={}", result.getCode(), result.getMessage());
            return null;
        }
        
        return result.getData();
    }

    /**
     * 获取结果数据，如果失败则返回默认值
     * 
     * @param result 结果对象
     * @param defaultValue 默认值
     * @param <T> 泛型类型
     * @return 数据或默认值
     */
    public static <T> T getResult(Result<T> result, T defaultValue) {
        T data = getResult(result);
        return data != null ? data : defaultValue;
    }

    /**
     * 检查调用是否成功
     * 
     * @param result 结果对象
     * @return 是否成功
     */
    public static boolean isSuccess(Result<?> result) {
        return result != null && result.isSuccess();
    }

    /**
     * 检查调用是否失败
     * 
     * @param result 结果对象
     * @return 是否失败
     */
    public static boolean isError(Result<?> result) {
        return result == null || !result.isSuccess();
    }

    /**
     * 获取错误信息
     * 
     * @param result 结果对象
     * @return 错误信息
     */
    public static String getErrorMessage(Result<?> result) {
        if (result == null) {
            return "结果对象为空";
        }
        
        if (result.isSuccess()) {
            return null;
        }
        
        return String.format("调用失败: code=%d, message=%s", result.getCode(), result.getMessage());
    }
}
