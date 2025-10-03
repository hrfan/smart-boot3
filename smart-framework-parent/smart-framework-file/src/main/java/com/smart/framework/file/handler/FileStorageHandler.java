package com.smart.framework.file.handler;



import com.smart.framework.file.model.FileUploadResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件存储处理器接口
 * 定义统一的文件存储操作接口，支持多种存储方式
 * 
 * @author AI Assistant
 * @version 1.0
 * @since 2024-01-01
 */
public interface FileStorageHandler {
    
    /**
     * 获取处理器类型
     * @return 处理器类型名称
     */
    String getHandlerType();
    
    /**
     * 上传文件
     * @param file 文件对象
     * @param fileName 文件名
     * @return 文件访问URL
     */
    String uploadFile(MultipartFile file, String fileName);
    
    /**
     * 上传文件（带原始文件名）
     * @param file 文件对象
     * @param fileName 文件名
     * @param originalFileName 原始文件名
     * @return 文件访问URL
     */
    String uploadFile(MultipartFile file, String fileName, String originalFileName);
    
    /**
     * 上传文件（带原始文件名，返回实际存储的文件名）
     * @param file 文件对象
     * @param fileName 文件名
     * @param originalFileName 原始文件名
     * @return 文件上传结果，包含URL和实际存储的文件名
     */
    FileUploadResult uploadFileWithResult(MultipartFile file, String fileName, String originalFileName);
    
    /**
     * 上传字节数组
     * @param fileData 文件数据
     * @param fileName 文件名
     * @param contentType 内容类型
     * @return 文件访问URL
     */
    String uploadFile(byte[] fileData, String fileName, String contentType);
    
    /**
     * 删除文件
     * @param fileName 文件名
     * @return 删除结果
     */
    boolean deleteFile(String fileName);
    
    /**
     * 下载文件
     * @param fileName 文件名
     * @return 文件数据
     */
    byte[] downloadFile(String fileName);
    
    /**
     * 检查文件是否存在
     * @param fileName 文件名
     * @return 是否存在
     */
    boolean fileExists(String fileName);
    
    /**
     * 获取文件访问URL
     * @param fileName 文件名
     * @return 访问URL
     */
    String getFileUrl(String fileName);
}
