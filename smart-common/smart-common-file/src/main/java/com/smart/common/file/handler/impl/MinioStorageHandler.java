package com.smart.common.file.handler.impl;



import com.smart.common.file.config.MinioInfo;
import com.smart.common.file.exception.FileStorageException;
import com.smart.common.file.handler.FileStorageHandler;

import com.smart.common.file.model.FileUploadResult;
import io.minio.*;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * MinIO存储处理器
 * 实现MinIO对象存储的文件操作
 * 
 * @author AI Assistant
 * @version 1.0
 * @since 2024-01-01
 */
@Component
public class MinioStorageHandler implements FileStorageHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(MinioStorageHandler.class);
    
    @Autowired
    private MinioInfo minioInfo;
    
    private MinioClient minioClient;
    
    @PostConstruct
    public void init() {
        try {
            minioClient = MinioClient.builder()
                    .endpoint(minioInfo.getEndpoint())
                    .credentials(minioInfo.getAccessKey(), minioInfo.getSecretKey())
                    .build();
            
            logger.info("MinIO存储处理器初始化成功，端点: {}", minioInfo.getEndpoint());
            
            // 确保默认存储桶存在
            ensureBucketExists(minioInfo.getBucket());
            
        } catch (Exception e) {
            logger.error("MinIO存储处理器初始化失败: {}", e.getMessage(), e);
            throw new FileStorageException("MINIO_INIT_FAILED", "MinIO初始化失败: " + e.getMessage());
        }
    }
    
    @Override
    public String getHandlerType() {
        return "minio";
    }
    
    @Override
    public String uploadFile(MultipartFile file, String fileName) {
        return uploadFile(file, fileName, fileName);
    }
    
    @Override
    public String uploadFile(MultipartFile file, String fileName, String originalFileName) {
        validateFileParams(file, fileName);
        
        try {
            // 确保存储桶存在
            ensureBucketExists(minioInfo.getBucket());
            
            // 生成唯一文件名（使用原始文件名生成）
            String uniqueFileName = generateUniqueFileName(originalFileName);
            
            // 执行上传
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(minioInfo.getBucket())
                    .object(uniqueFileName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build()
            );
            
            // 生成访问URL
            String fileUrl = generateFileUrl(minioInfo.getBucket(), uniqueFileName);
            
            logger.info("MinIO文件上传成功: {}, 原始文件名: {}, URL: {}", uniqueFileName, originalFileName, fileUrl);
            return fileUrl;
            
        } catch (IOException e) {
            logger.error("MinIO文件上传失败: {}", e.getMessage(), e);
            throw new FileStorageException("MINIO_UPLOAD_FAILED", "MinIO文件上传失败: " + e.getMessage());
        } catch (Exception e) {
            logger.error("MinIO文件上传异常: {}", e.getMessage(), e);
            throw new FileStorageException("MINIO_UPLOAD_ERROR", "MinIO文件上传异常: " + e.getMessage());
        }
    }
    
    @Override
    public FileUploadResult uploadFileWithResult(MultipartFile file, String fileName, String originalFileName) {
        validateFileParams(file, fileName);
        
        try {
            // 确保存储桶存在
            ensureBucketExists(minioInfo.getBucket());
            
            // 生成唯一文件名（使用原始文件名生成）
            String uniqueFileName = generateUniqueFileName(originalFileName);
            
            // 执行上传
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(minioInfo.getBucket())
                    .object(uniqueFileName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build()
            );
            
            // 生成访问URL
            String fileUrl = generateFileUrl(minioInfo.getBucket(), uniqueFileName);

            // 获取文件存储真实路径
            String filePath = String.format("%s/%s", minioInfo.getBucket(), uniqueFileName);
            
            logger.info("MinIO文件上传成功: {}, 原始文件名: {}, URL: {}", uniqueFileName, originalFileName, fileUrl);
            
            // 构建结果
            FileUploadResult result = FileUploadResult.success();
            result.setFileName(uniqueFileName); // 服务器上存储的真实文件名
            result.setFilePath(filePath);
            result.setOriginalFileName(originalFileName);
            result.setFileSize(file.getSize());
            result.setContentType(file.getContentType());
            result.setBucketName(minioInfo.getBucket());
            result.setUploadBy("system");
            
            return result;
            
        } catch (IOException e) {
            logger.error("MinIO文件上传失败: {}", e.getMessage(), e);
            throw new FileStorageException("MINIO_UPLOAD_FAILED", "MinIO文件上传失败: " + e.getMessage());
        } catch (Exception e) {
            logger.error("MinIO文件上传异常: {}", e.getMessage(), e);
            throw new FileStorageException("MINIO_UPLOAD_ERROR", "MinIO文件上传异常: " + e.getMessage());
        }
    }
    
    @Override
    public String uploadFile(byte[] fileData, String fileName, String contentType) {
        validateByteArrayParams(fileData, fileName);
        
        try {
            // 确保存储桶存在
            ensureBucketExists(minioInfo.getBucket());
            
            // 生成唯一文件名
            String uniqueFileName = generateUniqueFileName(fileName);
            
            // 执行上传
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(minioInfo.getBucket())
                    .object(uniqueFileName)
                    .stream(new java.io.ByteArrayInputStream(fileData), fileData.length, -1)
                    .contentType(contentType)
                    .build()
            );
            
            // 生成访问URL
            String fileUrl = generateFileUrl(minioInfo.getBucket(), uniqueFileName);
            
            logger.info("MinIO字节数组文件上传成功: {}, 大小: {} bytes, URL: {}", uniqueFileName, fileData.length, fileUrl);
            return fileUrl;
            
        } catch (Exception e) {
            logger.error("MinIO字节数组文件上传失败: {}", e.getMessage(), e);
            throw new FileStorageException("MINIO_UPLOAD_FAILED", "MinIO字节数组文件上传失败: " + e.getMessage());
        }
    }
    
    @Override
    public boolean deleteFile(String fileName) {
        validateFileName(fileName);
        
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(minioInfo.getBucket())
                    .object(fileName)
                    .build()
            );
            
            logger.info("MinIO文件删除成功: {}", fileName);
            return true;
            
        } catch (Exception e) {
            logger.error("MinIO文件删除失败: {}", e.getMessage(), e);
            throw new FileStorageException("MINIO_DELETE_FAILED", "MinIO文件删除失败: " + e.getMessage());
        }
    }
    
    @Override
    public byte[] downloadFile(String fileName) {
        validateFileName(fileName);
        
        try {
            InputStream inputStream = minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(minioInfo.getBucket())
                    .object(fileName)
                    .build()
            );
            
            byte[] fileData = inputStream.readAllBytes();
            inputStream.close();
            
            logger.info("MinIO文件下载成功: {}, 大小: {} bytes", fileName, fileData.length);
            return fileData;
            
        } catch (Exception e) {
            logger.error("MinIO文件下载失败: {}", e.getMessage(), e);
            throw new FileStorageException("MINIO_DOWNLOAD_FAILED", "MinIO文件下载失败: " + e.getMessage());
        }
    }
    
    @Override
    public boolean fileExists(String fileName) {
        validateFileName(fileName);
        
        try {
            minioClient.statObject(
                StatObjectArgs.builder()
                    .bucket(minioInfo.getBucket())
                    .object(fileName)
                    .build()
            );
            
            logger.debug("MinIO文件存在: {}", fileName);
            return true;
            
        } catch (Exception e) {
            logger.debug("MinIO文件不存在: {}", fileName);
            return false;
        }
    }
    
    @Override
    public String getFileUrl(String fileName) {
        validateFileName(fileName);
        
        try {
            String fileUrl = generateFileUrl(minioInfo.getBucket(), fileName);
            logger.debug("获取MinIO文件URL: {} -> {}", fileName, fileUrl);
            return fileUrl;
            
        } catch (Exception e) {
            logger.error("获取MinIO文件URL失败: {}", e.getMessage(), e);
            throw new FileStorageException("MINIO_URL_FAILED", "获取MinIO文件URL失败: " + e.getMessage());
        }
    }
    
    /**
     * 确保存储桶存在
     * @param bucketName 存储桶名称
     */
    private void ensureBucketExists(String bucketName) {
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                logger.info("创建MinIO存储桶: {}", bucketName);
            }
        } catch (Exception e) {
            logger.error("确保MinIO存储桶存在失败: {}", e.getMessage(), e);
            throw new FileStorageException("MINIO_BUCKET_FAILED", "确保MinIO存储桶存在失败: " + e.getMessage());
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
     * @param bucketName 存储桶名称
     * @param fileName 文件名
     * @return 访问URL
     */
    private String generateFileUrl(String bucketName, String fileName) {
        try {
            String url = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .method(io.minio.http.Method.GET)
                    .bucket(bucketName)
                    .object(fileName)
                    .expiry(-1) // 永不过期
                    .build()
            );
            return url;
        } catch (Exception e) {
            logger.warn("生成MinIO预签名URL失败，使用简单URL: {}", e.getMessage());
            String endpoint = minioInfo.getEndpoint();
            if (endpoint.endsWith("/")) {
                endpoint = endpoint.substring(0, endpoint.length() - 1);
            }
            return endpoint + "/" + bucketName + "/" + fileName;
        }
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
