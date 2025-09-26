package com.smart.common.mail.common;

/**
 * 邮件附件数据模型
 * 支持byte流和文件路径两种方式
 */
public class AttachmentData {
    private String fileName;
    private byte[] data;
    private String filePath;
    private String contentType;

    /**
     * 构造函数 - 使用byte流创建附件
     * @param fileName 文件名
     * @param data 文件数据
     */
    public AttachmentData(String fileName, byte[] data) {
        this.fileName = fileName;
        this.data = data;
        this.contentType = getContentTypeFromFileName(fileName);
    }

    /**
     * 构造函数 - 使用byte流创建附件，指定内容类型
     * @param fileName 文件名
     * @param data 文件数据
     * @param contentType 内容类型
     */
    public AttachmentData(String fileName, byte[] data, String contentType) {
        this.fileName = fileName;
        this.data = data;
        this.contentType = contentType;
    }

    /**
     * 构造函数 - 使用文件路径创建附件
     * @param filePath 文件路径
     */
    public AttachmentData(String filePath) {
        this.filePath = filePath;
        this.fileName = extractFileName(filePath);
        this.contentType = getContentTypeFromFileName(this.fileName);
    }

    /**
     * 从文件路径中提取文件名
     * @param filePath 文件路径
     * @return 文件名
     */
    private String extractFileName(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return "attachment";
        }
        int lastSlash = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));
        return lastSlash >= 0 ? filePath.substring(lastSlash + 1) : filePath;
    }

    /**
     * 根据文件名推断内容类型
     * @param fileName 文件名
     * @return 内容类型
     */
    private String getContentTypeFromFileName(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "application/octet-stream";
        }
        
        String extension = fileName.toLowerCase();
        if (extension.endsWith(".txt")) {
            return "text/plain";
        } else if (extension.endsWith(".html") || extension.endsWith(".htm")) {
            return "text/html";
        } else if (extension.endsWith(".pdf")) {
            return "application/pdf";
        } else if (extension.endsWith(".doc")) {
            return "application/msword";
        } else if (extension.endsWith(".docx")) {
            return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        } else if (extension.endsWith(".xls")) {
            return "application/vnd.ms-excel";
        } else if (extension.endsWith(".xlsx")) {
            return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        } else if (extension.endsWith(".ppt")) {
            return "application/vnd.ms-powerpoint";
        } else if (extension.endsWith(".pptx")) {
            return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
        } else if (extension.endsWith(".jpg") || extension.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (extension.endsWith(".png")) {
            return "image/png";
        } else if (extension.endsWith(".gif")) {
            return "image/gif";
        } else if (extension.endsWith(".zip")) {
            return "application/zip";
        } else if (extension.endsWith(".rar")) {
            return "application/x-rar-compressed";
        } else {
            return "application/octet-stream";
        }
    }

    /**
     * 判断是否为byte流附件
     * @return 是否为byte流附件
     */
    public boolean isByteData() {
        return data != null;
    }

    /**
     * 判断是否为文件路径附件
     * @return 是否为文件路径附件
     */
    public boolean isFilePath() {
        return filePath != null && !filePath.isEmpty();
    }

    // Getter和Setter方法
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return "AttachmentData{" +
                "fileName='" + fileName + '\'' +
                ", isByteData=" + isByteData() +
                ", isFilePath=" + isFilePath() +
                ", contentType='" + contentType + '\'' +
                '}';
    }
}
