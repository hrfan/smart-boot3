package com.smart.common.core.oss.handler.impl;


import com.smart.common.core.oss.exception.FileStorageException;
import com.smart.common.core.oss.handler.FileStorageHandler;
import com.smart.common.core.oss.model.FileUploadResult;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 本地文件存储处理器
 * 实现本地文件系统的文件存储操作
 * 
 * @author AI Assistant
 * @version 1.0
 * @since 2024-01-01
 */
@Component
public class LocalStorageHandler implements FileStorageHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(LocalStorageHandler.class);
    
    @Value("${file.storage.local.uploadPath}")
    private String uploadPath;
    
    @PostConstruct
    public void init() {
        logger.info("初始化本地存储处理器，上传路径: {}", uploadPath);
        createStorageDirectory();
    }
    
    @Override
    public String getHandlerType() {
        return "local";
    }
    
    @Override
    public String uploadFile(MultipartFile file, String fileName) {
        return uploadFile(file, fileName, fileName);
    }
    
    @Override
    public String uploadFile(MultipartFile file, String fileName, String originalFileName) {
        // 本地存储只需要验证file和fileName
        if (file == null || file.isEmpty()) {
            throw new FileStorageException("FILE_EMPTY", "上传文件不能为空");
        }
        
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new FileStorageException("FILENAME_EMPTY", "文件名不能为空");
        }
        
        try {
            // 创建存储目录
            Path storageDir = createStorageDirectory();
            
            // 生成唯一文件名（使用原始文件名生成）
            String uniqueFileName = generateUniqueFileName(originalFileName);
            
            // 构建文件路径
            Path filePath = storageDir.resolve(uniqueFileName);
            
            // 保存文件
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
            
            // 生成访问URL
            String fileUrl = generateFileUrl(uniqueFileName);
            
            logger.info("本地文件上传成功: {}, 原始文件名: {}, 路径: {}, URL: {}", uniqueFileName, originalFileName, filePath, fileUrl);
            return fileUrl;
            
        } catch (IOException e) {
            logger.error("本地文件上传失败: {}", e.getMessage(), e);
            throw new FileStorageException("LOCAL_UPLOAD_FAILED", "本地文件上传失败: " + e.getMessage());
        } catch (Exception e) {
            logger.error("本地文件上传异常: {}", e.getMessage(), e);
            throw new FileStorageException("LOCAL_UPLOAD_ERROR", "本地文件上传异常: " + e.getMessage());
        }
    }
    
    @Override
    public FileUploadResult uploadFileWithResult(MultipartFile file, String fileName, String originalFileName) {
        // 本地存储只需要验证file和fileName
        if (file == null || file.isEmpty()) {
            throw new FileStorageException("FILE_EMPTY", "上传文件不能为空");
        }
        
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new FileStorageException("FILENAME_EMPTY", "文件名不能为空");
        }
        
        try {
            // 创建存储目录
            Path storageDir = createStorageDirectory();
            
            // 生成唯一文件名（使用原始文件名生成）
            String uniqueFileName = generateUniqueFileName(originalFileName);
            
            // 构建文件路径
            Path filePath = storageDir.resolve(uniqueFileName);
            
            // 保存文件
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
            
            // 生成访问URL
            String fileUrl = generateFileUrl(uniqueFileName);

            logger.info("本地文件上传成功: {}, 原始文件名: {}, 路径: {}, URL: {}", uniqueFileName, originalFileName, filePath, fileUrl);
            
            // 构建结果
            FileUploadResult result =  FileUploadResult.success();
            // 存储路径 + 文件名
            result.setFilePath(fileUrl);
            result.setFileName(uniqueFileName); // 服务器上存储的真实文件名
            result.setOriginalFileName(originalFileName);
            result.setFileSize(file.getSize());
            result.setContentType(file.getContentType());
            result.setUploadBy("system");
            
            return result;
            
        } catch (IOException e) {
            logger.error("本地文件上传失败: {}", e.getMessage(), e);
            throw new FileStorageException("LOCAL_UPLOAD_FAILED", "本地文件上传失败: " + e.getMessage());
        } catch (Exception e) {
            logger.error("本地文件上传异常: {}", e.getMessage(), e);
            throw new FileStorageException("LOCAL_UPLOAD_ERROR", "本地文件上传异常: " + e.getMessage());
        }
    }
    
    @Override
    public String uploadFile(byte[] fileData, String fileName, String contentType) {
        validateByteArrayParams(fileData, fileName);
        
        try {
            // 创建存储目录
            Path storageDir = createStorageDirectory();
            
            // 生成唯一文件名
            String uniqueFileName = generateUniqueFileName(fileName);
            
            // 构建文件路径
            Path filePath = storageDir.resolve(uniqueFileName);
            
            // 保存文件
            Files.write(filePath, fileData);
            
            // 生成访问URL
            String fileUrl = generateFileUrl(uniqueFileName);
            
            logger.info("本地字节数组文件上传成功: {}, 大小: {} bytes, URL: {}", uniqueFileName, fileData.length, fileUrl);
            return fileUrl;
            
        } catch (IOException e) {
            logger.error("本地字节数组文件上传失败: {}", e.getMessage(), e);
            throw new FileStorageException("LOCAL_UPLOAD_FAILED", "本地字节数组文件上传失败: " + e.getMessage());
        } catch (Exception e) {
            logger.error("本地字节数组文件上传异常: {}", e.getMessage(), e);
            throw new FileStorageException("LOCAL_UPLOAD_ERROR", "本地字节数组文件上传异常: " + e.getMessage());
        }
    }
    
    @Override
    public boolean deleteFile(String fileName) {
        validateFileName(fileName);
        
        try {
            Path filePath = Paths.get(uploadPath, fileName);
            
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                logger.info("本地文件删除成功: {}", fileName);
                return true;
            } else {
                logger.warn("本地文件不存在，无法删除: {}", fileName);
                return false;
            }
            
        } catch (IOException e) {
            logger.error("本地文件删除失败: {}", e.getMessage(), e);
            throw new FileStorageException("LOCAL_DELETE_FAILED", "本地文件删除失败: " + e.getMessage());
        } catch (Exception e) {
            logger.error("本地文件删除异常: {}", e.getMessage(), e);
            throw new FileStorageException("LOCAL_DELETE_ERROR", "本地文件删除异常: " + e.getMessage());
        }
    }
    
    @Override
    public byte[] downloadFile(String fileName) {
        validateFileName(fileName);
        
        try {
            Path filePath = Paths.get(uploadPath, fileName);
            
            if (Files.exists(filePath)) {
                byte[] fileData = Files.readAllBytes(filePath);
                logger.info("本地文件下载成功: {}, 大小: {} bytes", fileName, fileData.length);
                return fileData;
            } else {
                logger.warn("本地文件不存在: {}", fileName);
                throw new FileStorageException("FILE_NOT_FOUND", "文件不存在: " + fileName);
            }
            
        } catch (IOException e) {
            logger.error("本地文件下载失败: {}", e.getMessage(), e);
            throw new FileStorageException("LOCAL_DOWNLOAD_FAILED", "本地文件下载失败: " + e.getMessage());
        } catch (Exception e) {
            logger.error("本地文件下载异常: {}", e.getMessage(), e);
            throw new FileStorageException("LOCAL_DOWNLOAD_ERROR", "本地文件下载异常: " + e.getMessage());
        }
    }
    
    @Override
    public boolean fileExists(String fileName) {
        validateFileName(fileName);
        
        try {
            Path filePath = Paths.get(uploadPath, fileName);
            boolean exists = Files.exists(filePath);
            logger.debug("本地文件存在性检查: {} -> {}", fileName, exists);
            return exists;
            
        } catch (Exception e) {
            logger.error("本地文件存在性检查失败: {}", e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public String getFileUrl(String fileName) {
        validateFileName(fileName);
        
        try {
            String fileUrl = generateFileUrl(fileName);
            logger.debug("获取本地文件URL: {} -> {}", fileName, fileUrl);
            return fileUrl;
            
        } catch (Exception e) {
            logger.error("获取本地文件URL失败: {}", e.getMessage(), e);
            throw new FileStorageException("LOCAL_URL_FAILED", "获取本地文件URL失败: " + e.getMessage());
        }
    }
    
    /**
     * 创建存储目录
     * @return 存储目录路径
     */
    private Path createStorageDirectory() {
        try {
            Path storageDir = Paths.get(uploadPath);
            if (!Files.exists(storageDir)) {
                Files.createDirectories(storageDir);
                logger.debug("创建存储目录: {}", storageDir);
            }
            return storageDir;
        } catch (IOException e) {
            logger.error("创建存储目录失败: {}", e.getMessage(), e);
            throw new FileStorageException("LOCAL_STORAGE_FAILED", "创建存储目录失败: " + e.getMessage());
        }
    }
    
    /**
     * 生成唯一文件名
     * @param originalFileName 原始文件名
     * @return 唯一文件名
     */
    private String generateUniqueFileName(String originalFileName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String randomSuffix = String.valueOf(System.nanoTime()).substring(8);
        
        if (StringUtils.isNotBlank(originalFileName)) {
            String extension = "";
            int lastDotIndex = originalFileName.lastIndexOf('.');
            if (lastDotIndex > 0) {
                extension = originalFileName.substring(lastDotIndex);
            }
            return timestamp + "_" + randomSuffix + extension;
        } else {
            return timestamp + "_" + randomSuffix;
        }
    }
    
    /**
     * 生成文件访问URL
     * @param fileName 文件名
     * @return 访问URL
     */
    private String generateFileUrl(String fileName) {
        String encodedFileName = fileName.replace(" ", "%20");
        return uploadPath + "/" + encodedFileName;
    }
    
    /**
     * 验证字节数组参数
     * @param fileData 文件数据
     * @param fileName 文件名
     */
    private void validateByteArrayParams(byte[] fileData, String fileName) {
        if (fileData == null || fileData.length == 0) {
            throw new FileStorageException("FILE_DATA_EMPTY", "文件数据不能为空");
        }
        
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new FileStorageException("FILENAME_EMPTY", "文件名不能为空");
        }
    }
    
    /**
     * 验证文件名
     * @param fileName 文件名
     */
    private void validateFileName(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new FileStorageException("FILENAME_EMPTY", "文件名不能为空");
        }
    }
}
