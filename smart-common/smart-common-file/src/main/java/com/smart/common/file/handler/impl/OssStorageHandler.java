package com.smart.common.file.handler.impl;



import com.smart.common.file.config.FileStorageProperties;
import com.smart.common.file.exception.FileStorageException;
import com.smart.common.file.handler.FileStorageHandler;
import com.smart.common.file.model.FileUploadResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 阿里云OSS存储处理器
 * 实现阿里云对象存储的文件操作
 * 
 * @author AI Assistant
 * @version 1.0
 * @since 2024-01-01
 */
@Component
public class OssStorageHandler implements FileStorageHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(OssStorageHandler.class);
    
    @Autowired
    private FileStorageProperties fileStorageProperties;
    
    @Override
    public String getHandlerType() {
        return "oss";
    }
    
    @Override
    public String uploadFile(MultipartFile file, String fileName) {
        return uploadFile(file, fileName, fileName);
    }
    
    @Override
    public String uploadFile(MultipartFile file, String fileName, String originalFileName) {
        validateFileParams(file, fileName);
        
        // 生成唯一文件名（使用原始文件名生成）
        String uniqueFileName = generateUniqueFileName(originalFileName);
        
        // 生成访问URL（模拟）
        String fileUrl = generateFileUrl(fileStorageProperties.getOss().getBucket(), uniqueFileName);
        
        logger.info("OSS文件上传成功（模拟）: {}, 原始文件名: {}, URL: {}", uniqueFileName, originalFileName, fileUrl);
        logger.warn("注意：当前为简化版本，实际文件未上传到OSS");
        
        return fileUrl;
    }
    
    @Override
    public FileUploadResult uploadFileWithResult(MultipartFile file, String fileName, String originalFileName) {
        validateFileParams(file, fileName);
        
        // 生成唯一文件名（使用原始文件名生成）
        String uniqueFileName = generateUniqueFileName(originalFileName);
        
        // 生成访问URL（模拟）
        String fileUrl = generateFileUrl(fileStorageProperties.getOss().getBucket(), uniqueFileName);
        // 获取文件存储真实路径
        String filePath = String.format("%s/%s", fileStorageProperties.getOss().getBucket(), uniqueFileName);


        logger.info("OSS文件上传成功（模拟）: {}, 原始文件名: {}, URL: {}", uniqueFileName, originalFileName, fileUrl);
        logger.warn("注意：当前为简化版本，实际文件未上传到OSS");
        
        // 构建结果
        FileUploadResult result = FileUploadResult.success();
        result.setFilePath(fileUrl);
        result.setFileName(uniqueFileName); // 服务器上存储的真实文件名
        result.setOriginalFileName(originalFileName);
        result.setFileSize(file.getSize());
        result.setContentType(file.getContentType());
        result.setBucketName(fileStorageProperties.getOss().getBucket());
        result.setUploadBy("system");
        
        return result;
    }
    
    @Override
    public String uploadFile(byte[] fileData, String fileName, String contentType) {
        validateByteArrayParams(fileData, fileName);
        
        // 生成唯一文件名
        String uniqueFileName = generateUniqueFileName(fileName);
        
        // 生成访问URL（模拟）
        String fileUrl = generateFileUrl(fileStorageProperties.getOss().getBucket(), uniqueFileName);
        
        logger.info("OSS字节数组文件上传成功（模拟）: {}, 大小: {} bytes, URL: {}", uniqueFileName, fileData.length, fileUrl);
        logger.warn("注意：当前为简化版本，实际文件未上传到OSS");
        
        return fileUrl;
    }
    
    @Override
    public boolean deleteFile(String fileName) {
        validateFileName(fileName);
        
        logger.info("OSS文件删除成功（模拟）: {}", fileName);
        logger.warn("注意：当前为简化版本，实际文件未从OSS删除");
        
        return true;
    }
    
    @Override
    public byte[] downloadFile(String fileName) {
        validateFileName(fileName);
        
        logger.info("OSS文件下载成功（模拟）: {}", fileName);
        logger.warn("注意：当前为简化版本，实际文件未从OSS下载");
        
        // 返回模拟数据
        return "模拟OSS文件内容".getBytes();
    }
    
    @Override
    public boolean fileExists(String fileName) {
        validateFileName(fileName);
        
        logger.debug("OSS文件存在性检查（模拟）: {} -> true", fileName);
        logger.warn("注意：当前为简化版本，实际文件未在OSS中检查");
        
        return true;
    }
    
    @Override
    public String getFileUrl(String fileName) {
        validateFileName(fileName);
        
        String fileUrl = generateFileUrl(fileStorageProperties.getOss().getBucket(), fileName);
        logger.debug("获取OSS文件URL（模拟）: {} -> {}", fileName, fileUrl);
        
        return fileUrl;
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
     * @param bucketName 存储桶名称
     * @param fileName 文件名
     * @return 访问URL
     */
    private String generateFileUrl(String bucketName, String fileName) {
        String endpoint = fileStorageProperties.getOss().getEndpoint();
        if (endpoint.endsWith("/")) {
            endpoint = endpoint.substring(0, endpoint.length() - 1);
        }
        return endpoint + "/" + bucketName + "/" + fileName;
    }
    
    /**
     * 验证文件参数
     * @param file 文件
     * @param fileName 文件名
     */
    private void validateFileParams(MultipartFile file, String fileName) {
        if (file == null || file.isEmpty()) {
            throw new FileStorageException("FILE_EMPTY", "上传文件不能为空");
        }
        
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new FileStorageException("FILENAME_EMPTY", "文件名不能为空");
        }
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
