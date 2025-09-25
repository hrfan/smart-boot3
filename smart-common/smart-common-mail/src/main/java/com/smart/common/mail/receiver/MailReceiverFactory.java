package com.smart.common.mail.receiver;

import com.smart.common.mail.common.MailConfig;

/**
 * 邮件接收工厂类，用于根据配置创建不同的邮件接收实现
 */
public class MailReceiverFactory {
    /**
     * 根据邮件服务类型创建对应的邮件接收器
     * @param mailType 邮件服务类型（如 '163', 'qq'）
     * @param config 邮件服务配置
     * @return 邮件接收器实例
     */
    public static MailReceiver createMailReceiver(String mailType, MailConfig config) {
        if (mailType == null || mailType.isEmpty()) {
            throw new IllegalArgumentException("邮件服务类型不能为空");
        }
        switch (mailType.toLowerCase()) {
            case "163":
                return new NetEaseMailReceiver(config);
            case "qq":
                return new QQMailReceiver(config);
            default:
                throw new UnsupportedOperationException("不支持的邮件服务类型: " + mailType);
        }
    }
}
