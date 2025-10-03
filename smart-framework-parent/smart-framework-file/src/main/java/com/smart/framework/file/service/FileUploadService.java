package com.smart.framework.file.service;


import com.smart.framework.file.exception.FileStorageException;

import com.smart.framework.file.factory.FileStorageHandlerFactory;
import com.smart.framework.file.handler.FileStorageHandler;
import com.smart.framework.file.model.FileUploadResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传服务
 * 提供统一的文件上传、下载、删除等服务
 * 
 * @author AI Assistant
 * @version 1.0
 * @since 2024-01-01
 */
@Service
public class FileUploadService {
    
    private static final Logger logger = LoggerFactory.getLogger(FileUploadService.class);
    
    @Autowired
    private FileStorageHandlerFactory storageHandlerFactory;
    
    /**
     * 上传文件
     * @param file 文件
     * @param fileName 文件名
     * @return 上传结果
     */
    public FileUploadResult uploadFile(MultipartFile file, String fileName) {
        try {
            // 文件验证
            validateFile(file);
            
            // 获取存储处理器
            FileStorageHandler handler = storageHandlerFactory.getStorageHandler();
            
            // 执行上传
            FileUploadResult uploadResult = handler.uploadFileWithResult(file, fileName, file.getOriginalFilename());
            
            // 构建结果
            FileUploadResult result = FileUploadResult.success();
            result.setFilePath(uploadResult.getFilePath());
            result.setFileName(uploadResult.getFileName()); // 使用服务器上存储的真实文件名
            result.setOriginalFileName(file.getOriginalFilename());
            result.setFileSize(file.getSize());
            result.setContentType(file.getContentType());
            
            // 本地存储不显示bucketName
            String storageType = storageHandlerFactory.getStorageHandler().getHandlerType();
            if (!"local".equalsIgnoreCase(storageType)) {
                result.setBucketName(getDefaultBucket());
            }
            
            result.setUploadBy(getCurrentUser());
            
            logger.info("文件上传成功: {}, 大小: {} bytes", uploadResult.getFileName(), file.getSize());
            return result;
            
        } catch (Exception e) {
            logger.error("文件上传失败: {}", e.getMessage(), e);
            return FileUploadResult.failure("FILE_UPLOAD_FAILED", "文件上传失败: " + e.getMessage());
        }
    }
    
    /**
     * 上传文件（使用默认文件名）
     * @param file 文件
     * @return 上传结果
     */
    public FileUploadResult uploadFile(MultipartFile file) {
        return uploadFile(file, file.getOriginalFilename());
    }
    
    /**
     * 上传字节数组文件
     * @param fileData 文件数据
     * @param fileName 文件名
     * @param contentType 内容类型
     * @return 上传结果
     */
    public FileUploadResult uploadFile(byte[] fileData, String fileName, String contentType) {
        try {
            // 数据验证
            validateFileData(fileData);
            
            // 获取存储处理器
            FileStorageHandler handler = storageHandlerFactory.getStorageHandler();
            
            // 执行上传
            String fileUrl = handler.uploadFile(fileData, fileName, contentType);
            
            // 构建结果
            FileUploadResult result = FileUploadResult.success();
            result.setFilePath(fileUrl);
            result.setFileName(fileName);
            result.setOriginalFileName(fileName);
            result.setFileSize(fileData.length);
            result.setContentType(contentType);
            
            // 本地存储不显示bucketName
            String storageType = storageHandlerFactory.getStorageHandler().getHandlerType();
            if (!"local".equalsIgnoreCase(storageType)) {
                result.setBucketName(getDefaultBucket());
            }
            
            result.setUploadBy(getCurrentUser());
            
            logger.info("字节数组文件上传成功: {}, 大小: {} bytes, URL: {}", fileName, fileData.length, fileUrl);
            return result;
            
        } catch (Exception e) {
            logger.error("字节数组文件上传失败: {}", e.getMessage(), e);
            return FileUploadResult.failure("FILE_UPLOAD_FAILED", "字节数组文件上传失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除文件
     * @param fileName 文件名
     * @return 删除结果
     */
    public FileUploadResult deleteFile(String fileName) {
        try {
            // 参数验证
            if (fileName == null || fileName.trim().isEmpty()) {
                return FileUploadResult.failure("INVALID_PARAMS", "文件名不能为空");
            }
            
            // 获取存储处理器
            FileStorageHandler handler = storageHandlerFactory.getStorageHandler();
            
            // 执行删除
            boolean success = handler.deleteFile(fileName);
            
            if (success) {
                logger.info("文件删除成功: {}", fileName);
                return FileUploadResult.success();
            } else {
                return FileUploadResult.failure("FILE_DELETE_FAILED", "文件删除失败");
            }
            
        } catch (Exception e) {
            logger.error("文件删除失败: {}", e.getMessage(), e);
            return FileUploadResult.failure("FILE_DELETE_FAILED", "文件删除失败: " + e.getMessage());
        }
    }
    
    /**
     * 下载文件
     * @param fileName 文件名
     * @return 文件数据
     */
    public byte[] downloadFile(String fileName) {
        try {
            // 参数验证
            if (fileName == null || fileName.trim().isEmpty()) {
                throw new FileStorageException("INVALID_PARAMS", "文件名不能为空");
            }
            
            // 获取存储处理器
            FileStorageHandler handler = storageHandlerFactory.getStorageHandler();
            
            // 执行下载
            byte[] fileData = handler.downloadFile(fileName);
            
            logger.info("文件下载成功: {}, 大小: {} bytes", fileName, fileData.length);
            return fileData;
            
        } catch (Exception e) {
            logger.error("文件下载失败: {}", e.getMessage(), e);
            throw new FileStorageException("FILE_DOWNLOAD_FAILED", "文件下载失败: " + e.getMessage());
        }
    }
    
    /**
     * 检查文件是否存在
     * @param fileName 文件名
     * @return 是否存在
     */
    public boolean fileExists(String fileName) {
        try {
            // 参数验证
            if (fileName == null || fileName.trim().isEmpty()) {
                return false;
            }
            
            // 获取存储处理器
            FileStorageHandler handler = storageHandlerFactory.getStorageHandler();
            
            // 执行检查
            return handler.fileExists(fileName);
            
        } catch (Exception e) {
            logger.error("文件存在检查失败: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 获取文件访问URL
     * @param fileName 文件名
     * @return 访问URL
     */
    public String getFileUrl(String fileName) {
        try {
            // 参数验证
            if (fileName == null || fileName.trim().isEmpty()) {
                throw new FileStorageException("INVALID_PARAMS", "文件名不能为空");
            }
            
            // 获取存储处理器
            FileStorageHandler handler = storageHandlerFactory.getStorageHandler();
            
            // 获取URL
            String fileUrl = handler.getFileUrl(fileName);
            
            logger.debug("获取文件访问URL成功: {}", fileUrl);
            return fileUrl;
            
        } catch (Exception e) {
            logger.error("获取文件访问URL失败: {}", e.getMessage(), e);
            throw new FileStorageException("FILE_URL_FAILED", "获取文件访问URL失败: " + e.getMessage());
        }
    }
    
    /**
     * 验证文件
     * @param file 文件
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileStorageException("FILE_EMPTY", "上传文件不能为空");
        }
        
        // 可以添加更多验证逻辑，如文件类型、大小等
    }
    
    /**
     * 验证文件数据
     * @param fileData 文件数据
     */
    private void validateFileData(byte[] fileData) {
        if (fileData == null || fileData.length == 0) {
            throw new FileStorageException("FILE_DATA_EMPTY", "文件数据不能为空");
        }
    }
    
    /**
     * 获取默认存储桶
     * @return 默认存储桶名称
     */
    private String getDefaultBucket() {
        String storageType = storageHandlerFactory.getStorageHandler().getHandlerType();
        
        switch (storageType.toLowerCase()) {
            case "minio":
                return "smart"; // 从配置中读取
            case "oss":
                return "default-bucket"; // 从配置中读取
            case "local":
                return ""; // 本地存储不需要bucket
            default:
                return "";
        }
    }
    
    /**
     * 获取当前用户
     * @return 当前用户
     */
    private String getCurrentUser() {
        // 这里可以从Spring Security或其他认证机制获取当前用户
        // 暂时返回默认值
        return "system";
    }
}
