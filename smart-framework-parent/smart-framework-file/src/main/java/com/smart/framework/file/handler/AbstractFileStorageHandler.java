package com.smart.framework.file.handler;


import com.smart.framework.file.exception.FileStorageException;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件存储处理器抽象基类
 * 提供通用的文件操作逻辑和工具方法
 * 
 * @author AI Assistant
 * @version 1.0
 * @since 2024-01-01
 */
public abstract class AbstractFileStorageHandler implements FileStorageHandler {
    
    /**
     * 无参构造函数
     * Spring需要无参构造函数来创建Bean
     */
    protected AbstractFileStorageHandler() {
    }
    
    /**
     * 获取处理器类型
     * 子类必须实现此方法
     * @return 处理器类型
     */
    @Override
    public abstract String getHandlerType();
    
    /**
     * 验证文件参数
     * @param file 文件对象
     * @param bucketName 存储桶名称
     * @param fileName 文件名
     */
    protected void validateFileParams(MultipartFile file, String bucketName, String fileName) {
        if (file == null || file.isEmpty()) {
            throw new FileStorageException("FILE_EMPTY", "上传文件不能为空");
        }
        
        if (bucketName == null || bucketName.trim().isEmpty()) {
            throw new FileStorageException("BUCKET_EMPTY", "存储桶名称不能为空");
        }
        
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new FileStorageException("FILENAME_EMPTY", "文件名不能为空");
        }
    }
    
    /**
     * 验证字节数组参数
     * @param fileData 文件数据
     * @param bucketName 存储桶名称
     * @param fileName 文件名
     */
    protected void validateByteArrayParams(byte[] fileData, String bucketName, String fileName) {
        if (fileData == null || fileData.length == 0) {
            throw new FileStorageException("FILE_DATA_EMPTY", "文件数据不能为空");
        }

        if (bucketName == null || bucketName.trim().isEmpty()) {
            throw new FileStorageException("BUCKET_EMPTY", "存储桶名称不能为空");
        }

        if (fileName == null || fileName.trim().isEmpty()) {
            throw new FileStorageException("FILENAME_EMPTY", "文件名不能为空");
        }
    }

    protected void validateByteArrayParams(byte[] fileData, String fileName) {
        if (fileData == null || fileData.length == 0) {
            throw new FileStorageException("FILE_DATA_EMPTY", "文件数据不能为空");
        }
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new FileStorageException("FILENAME_EMPTY", "文件名不能为空");
        }
    }
    
    /**
     * 验证基本参数
     * @param bucketName 存储桶名称
     * @param fileName 文件名
     */
    protected void validateBasicParams(String bucketName, String fileName) {
        if (bucketName == null || bucketName.trim().isEmpty()) {
            throw new FileStorageException("BUCKET_EMPTY", "存储桶名称不能为空");
        }
        
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new FileStorageException("FILENAME_EMPTY", "文件名不能为空");
        }
    }
    
    /**
     * 生成唯一文件名
     * @param originalFileName 原始文件名
     * @return 唯一文件名
     */
    protected String generateUniqueFileName(String originalFileName) {
        if (originalFileName == null || originalFileName.trim().isEmpty()) {
            return System.currentTimeMillis() + ".tmp";
        }
        
        String extension = "";
        int lastDotIndex = originalFileName.lastIndexOf(".");
        if (lastDotIndex > 0) {
            extension = originalFileName.substring(lastDotIndex);
        }
        
        return System.currentTimeMillis() + "_" + originalFileName.hashCode() + extension;
    }
    
    /**
     * 获取文件扩展名
     * @param fileName 文件名
     * @return 扩展名
     */
    protected String getFileExtension(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return "";
        }
        
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1).toLowerCase();
        }
        
        return "";
    }
    
    /**
     * 获取文件MIME类型
     * @param fileName 文件名
     * @return MIME类型
     */
    protected String getMimeType(String fileName) {
        String extension = getFileExtension(fileName);
        
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "pdf":
                return "application/pdf";
            case "doc":
                return "application/msword";
            case "docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "xls":
                return "application/vnd.ms-excel";
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "txt":
                return "text/plain";
            case "zip":
                return "application/zip";
            default:
                return "application/octet-stream";
        }
    }
}
