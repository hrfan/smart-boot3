package com.smart.framework.mail.sender;

import com.smart.framework.mail.base.BaseMailSender;
import com.smart.framework.mail.common.MailConfig;
import java.util.logging.Logger;

/**
 * QQ邮件发送实现类
 */
public class QQMailSender extends BaseMailSender {
    private static final Logger logger = Logger.getLogger(QQMailSender.class.getName());

    public QQMailSender(MailConfig config) {
        super(config);
    }

    @Override
    protected boolean performSendMail(String to, String[] cc, String[] bcc, String subject, String content, boolean isAnonymous, String[] filePaths, boolean isHtml) {
        logger.info("通过QQ向 " + to + " 发送邮件，主题：" + subject);
        // QQ邮件发送使用父类的通用实现
        // 父类已经实现了完整的邮件发送逻辑，包括SMTP连接、认证、内容设置等
        return super.performSendMail(to, cc, bcc, subject, content, isAnonymous, filePaths, isHtml);
    }

    // 由于 BaseMailSender 已经提供了统一的 sendMail 方法，子类不需要重复实现各个具体方法
}
