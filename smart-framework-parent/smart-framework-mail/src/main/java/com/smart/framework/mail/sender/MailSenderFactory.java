package com.smart.framework.mail.sender;

import com.smart.framework.mail.common.MailConfig;

/**
 * 邮件发送工厂类，用于根据配置创建不同的邮件发送实现
 */
public class MailSenderFactory {
    /**
     * 根据邮件服务类型创建对应的邮件发送器
     * @param mailType 邮件服务类型（如 '163', 'qq'）
     * @param config 邮件服务配置
     * @return 邮件发送器实例
     */
    public static MailSender createMailSender(String mailType, MailConfig config) {
        if (mailType == null || mailType.isEmpty()) {
            throw new IllegalArgumentException("邮件服务类型不能为空");
        }
        switch (mailType.toLowerCase()) {
            case "163":
                return new NetEaseMailSender(config);
            case "qq":
                return new QQMailSender(config);
            default:
                throw new UnsupportedOperationException("不支持的邮件服务类型: " + mailType);
        }
    }
}
