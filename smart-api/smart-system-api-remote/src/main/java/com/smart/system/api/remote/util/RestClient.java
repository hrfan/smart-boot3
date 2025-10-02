package com.smart.system.api.remote.util;

import com.smart.common.core.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * REST客户端工具类
 * 提供微服务间HTTP调用的统一封装
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
@Component
public class RestClient {

    private static final Logger log = LoggerFactory.getLogger(RestClient.class);

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 执行GET请求
     * 
     * @param serviceName 服务名称
     * @param url 请求URL
     * @param responseType 响应类型
     * @param <T> 泛型类型
     * @return 响应结果
     */
    public <T> Result<T> doGet(String serviceName, String url, ParameterizedTypeReference<Result<T>> responseType) {
        return doGet(serviceName, url, null, responseType);
    }

    /**
     * 执行GET请求（带请求头）
     * 
     * @param serviceName 服务名称
     * @param url 请求URL
     * @param headers 请求头
     * @param responseType 响应类型
     * @param <T> 泛型类型
     * @return 响应结果
     */
    public <T> Result<T> doGet(String serviceName, String url, HttpHeaders headers, ParameterizedTypeReference<Result<T>> responseType) {
        try {
            log.debug("执行GET请求: serviceName={}, url={}", serviceName, url);
            
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<Result<T>> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, responseType);
            
            Result<T> result = response.getBody();
            log.debug("GET请求响应: {}", result);
            return result;
            
        } catch (Exception e) {
            log.error("GET请求失败: serviceName={}, url={}, error={}", serviceName, url, e.getMessage(), e);
            return Result.error("服务调用失败: " + e.getMessage());
        }
    }

    /**
     * 执行POST请求
     * 
     * @param serviceName 服务名称
     * @param url 请求URL
     * @param requestBody 请求体
     * @param responseType 响应类型
     * @param <T> 泛型类型
     * @return 响应结果
     */
    public <T> Result<T> doPost(String serviceName, String url, Object requestBody, ParameterizedTypeReference<Result<T>> responseType) {
        return doPost(serviceName, url, requestBody, null, responseType);
    }

    /**
     * 执行POST请求（带请求头）
     * 
     * @param serviceName 服务名称
     * @param url 请求URL
     * @param requestBody 请求体
     * @param headers 请求头
     * @param responseType 响应类型
     * @param <T> 泛型类型
     * @return 响应结果
     */
    public <T> Result<T> doPost(String serviceName, String url, Object requestBody, HttpHeaders headers, ParameterizedTypeReference<Result<T>> responseType) {
        try {
            log.debug("执行POST请求: serviceName={}, url={}", serviceName, url);
            
            HttpEntity<Object> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Result<T>> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, responseType);
            
            Result<T> result = response.getBody();
            log.debug("POST请求响应: {}", result);
            return result;
            
        } catch (Exception e) {
            log.error("POST请求失败: serviceName={}, url={}, error={}", serviceName, url, e.getMessage(), e);
            return Result.error("服务调用失败: " + e.getMessage());
        }
    }

    /**
     * 执行PUT请求
     * 
     * @param serviceName 服务名称
     * @param url 请求URL
     * @param requestBody 请求体
     * @param responseType 响应类型
     * @param <T> 泛型类型
     * @return 响应结果
     */
    public <T> Result<T> doPut(String serviceName, String url, Object requestBody, ParameterizedTypeReference<Result<T>> responseType) {
        return doPut(serviceName, url, requestBody, null, responseType);
    }

    /**
     * 执行PUT请求（带请求头）
     * 
     * @param serviceName 服务名称
     * @param url 请求URL
     * @param requestBody 请求体
     * @param headers 请求头
     * @param responseType 响应类型
     * @param <T> 泛型类型
     * @return 响应结果
     */
    public <T> Result<T> doPut(String serviceName, String url, Object requestBody, HttpHeaders headers, ParameterizedTypeReference<Result<T>> responseType) {
        try {
            log.debug("执行PUT请求: serviceName={}, url={}", serviceName, url);
            
            HttpEntity<Object> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Result<T>> response = restTemplate.exchange(
                    url, HttpMethod.PUT, entity, responseType);
            
            Result<T> result = response.getBody();
            log.debug("PUT请求响应: {}", result);
            return result;
            
        } catch (Exception e) {
            log.error("PUT请求失败: serviceName={}, url={}, error={}", serviceName, url, e.getMessage(), e);
            return Result.error("服务调用失败: " + e.getMessage());
        }
    }

    /**
     * 执行DELETE请求
     * 
     * @param serviceName 服务名称
     * @param url 请求URL
     * @param responseType 响应类型
     * @param <T> 泛型类型
     * @return 响应结果
     */
    public <T> Result<T> doDelete(String serviceName, String url, ParameterizedTypeReference<Result<T>> responseType) {
        return doDelete(serviceName, url, null, responseType);
    }

    /**
     * 执行DELETE请求（带请求头）
     * 
     * @param serviceName 服务名称
     * @param url 请求URL
     * @param headers 请求头
     * @param responseType 响应类型
     * @param <T> 泛型类型
     * @return 响应结果
     */
    public <T> Result<T> doDelete(String serviceName, String url, HttpHeaders headers, ParameterizedTypeReference<Result<T>> responseType) {
        try {
            log.debug("执行DELETE请求: serviceName={}, url={}", serviceName, url);
            
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<Result<T>> response = restTemplate.exchange(
                    url, HttpMethod.DELETE, entity, responseType);
            
            Result<T> result = response.getBody();
            log.debug("DELETE请求响应: {}", result);
            return result;
            
        } catch (Exception e) {
            log.error("DELETE请求失败: serviceName={}, url={}, error={}", serviceName, url, e.getMessage(), e);
            return Result.error("服务调用失败: " + e.getMessage());
        }
    }
}
