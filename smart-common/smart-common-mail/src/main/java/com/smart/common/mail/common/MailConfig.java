package com.smart.common.mail.common;

/**
 * 邮件服务配置类，用于存储邮件服务的配置信息
 * 支持 YAML 配置格式
 */
public class MailConfig {
    /**
     * 邮件服务类型（如 '163', 'qq'）
     */
    private String type;
    
    /**
     * 邮件服务器主机地址
     */
    private String host;
    
    /**
     * 邮件服务器端口
     */
    private int port;
    
    /**
     * 邮件服务用户名
     */
    private String username;
    
    /**
     * 邮件服务密码
     */
    private String password;
    
    /**
     * 是否启用SSL
     */
    private boolean sslEnabled;
    
    /**
     * 默认发件人地址
     */
    private String defaultFrom;
    
    /**
     * 邮件发送超时时间（毫秒）
     */
    private int timeout;

    /**
     * 获取邮件服务类型
     * @return 邮件服务类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置邮件服务类型
     * @param type 邮件服务类型
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取邮件服务器主机地址
     * @return 主机地址
     */
    public String getHost() {
        return host;
    }

    /**
     * 设置邮件服务器主机地址
     * @param host 主机地址
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * 获取邮件服务器端口
     * @return 端口号
     */
    public int getPort() {
        return port;
    }

    /**
     * 设置邮件服务器端口
     * @param port 端口号
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * 获取邮件服务用户名
     * @return 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置邮件服务用户名
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取邮件服务密码
     * @return 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置邮件服务密码
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 是否启用SSL
     * @return 是否启用SSL
     */
    public boolean isSslEnabled() {
        return sslEnabled;
    }

    /**
     * 设置是否启用SSL
     * @param sslEnabled 是否启用SSL
     */
    public void setSslEnabled(boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
    }

    /**
     * 获取默认发件人地址
     * @return 默认发件人地址
     */
    public String getDefaultFrom() {
        return defaultFrom;
    }

    /**
     * 设置默认发件人地址
     * @param defaultFrom 默认发件人地址
     */
    public void setDefaultFrom(String defaultFrom) {
        this.defaultFrom = defaultFrom;
    }

    /**
     * 获取邮件发送超时时间（毫秒）
     * @return 超时时间
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * 设置邮件发送超时时间（毫秒）
     * @param timeout 超时时间
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * 重写 toString 方法，隐藏敏感信息
     */
    @Override
    public String toString() {
        return "MailConfig{" +
                "type='" + type + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", username='" + username + '\'' +
                ", password='[PROTECTED]'" +
                ", sslEnabled=" + sslEnabled +
                ", defaultFrom='" + defaultFrom + '\'' +
                ", timeout=" + timeout +
                '}';
    }
}
