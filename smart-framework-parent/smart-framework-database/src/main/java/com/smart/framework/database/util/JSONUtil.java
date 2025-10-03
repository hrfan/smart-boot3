package com.smart.framework.database.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * JSON工具类
 * 兼容原有的JSON.parseArray方法
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public class JSONUtil {

    private static final Logger log = LoggerFactory.getLogger(JSONUtil.class);
    
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 解析JSON数组字符串为指定类型的列表
     * 兼容fastjson的parseArray方法
     * 
     * @param text JSON数组字符串
     * @param clazz 目标类型
     * @param <T> 类型参数
     * @return 解析后的列表
     */
    public static <T> List<T> parseArray(String text, Class<T> clazz) {
        if (text == null || text.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        try {
            TypeReference<List<T>> typeRef = new TypeReference<List<T>>() {};
            return objectMapper.readValue(text, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (JsonProcessingException e) {
            log.error("JSON解析失败: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * 解析JSON字符串为指定类型的对象
     * 
     * @param text JSON字符串
     * @param clazz 目标类型
     * @param <T> 类型参数
     * @return 解析后的对象
     */
    public static <T> T parseObject(String text, Class<T> clazz) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        
        try {
            return objectMapper.readValue(text, clazz);
        } catch (JsonProcessingException e) {
            log.error("JSON解析失败: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 将对象转换为JSON字符串
     * 
     * @param object 对象
     * @return JSON字符串
     */
    public static String toJSONString(Object object) {
        if (object == null) {
            return null;
        }
        
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("JSON序列化失败: {}", e.getMessage(), e);
            return null;
        }
    }
}