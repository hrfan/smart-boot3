package com.smart.framework.file.factory;


import com.smart.framework.file.handler.FileStorageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 文件存储处理器工厂
 * 根据配置创建对应的存储处理器实例
 * 
 * @author AI Assistant
 * @version 1.0
 * @since 2024-01-01
 */
@Component
public class FileStorageHandlerFactory {
    
    private static final Logger logger = LoggerFactory.getLogger(FileStorageHandlerFactory.class);
    
    /**
     * Spring应用上下文
     */
    @Autowired
    private ApplicationContext applicationContext;
    
    /**
     * 存储类型配置
     */
    @Value("${file.storage.type:minio}")
    private String storageType;
    
    /**
     * 获取文件存储处理器
     * @return 存储处理器实例
     */
    public FileStorageHandler getStorageHandler() {
        try {
            String beanName = storageType + "StorageHandler";
            FileStorageHandler handler = applicationContext.getBean(beanName, FileStorageHandler.class);
            logger.debug("获取存储处理器成功: {}, 类型: {}", beanName, handler.getHandlerType());
            return handler;
        } catch (Exception e) {
            logger.error("获取存储处理器失败: {}", e.getMessage(), e);
            throw new RuntimeException("获取存储处理器失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 根据类型获取文件存储处理器
     * @param handlerType 处理器类型
     * @return 存储处理器实例
     */
    public FileStorageHandler getStorageHandler(String handlerType) {
        try {
            String beanName = handlerType + "StorageHandler";
            FileStorageHandler handler = applicationContext.getBean(beanName, FileStorageHandler.class);
            logger.debug("根据类型获取存储处理器成功: {}, 类型: {}", beanName, handler.getHandlerType());
            return handler;
        } catch (Exception e) {
            logger.error("根据类型获取存储处理器失败: {}", e.getMessage(), e);
            throw new RuntimeException("根据类型获取存储处理器失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 获取当前配置的存储类型
     * @return 存储类型
     */
    public String getCurrentStorageType() {
        return storageType;
    }
    
    /**
     * 检查存储类型是否支持
     * @param handlerType 处理器类型
     * @return 是否支持
     */
    public boolean isStorageTypeSupported(String handlerType) {
        try {
            String beanName = handlerType + "StorageHandler";
            applicationContext.getBean(beanName, FileStorageHandler.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
