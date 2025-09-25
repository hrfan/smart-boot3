package com.smart.common.mail.sender;

import com.smart.common.mail.base.BaseMailSender;
import com.smart.common.mail.common.MailConfig;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * 网易163邮件发送实现类
 * 专门处理网易邮箱的SMTP连接和发送逻辑
 */
public class NetEaseMailSender extends BaseMailSender {
    private static final Logger logger = Logger.getLogger(NetEaseMailSender.class.getName());

    public NetEaseMailSender(MailConfig config) {
        super(config);
    }

    @Override
    protected boolean performSendMail(String to, String[] cc, String[] bcc, String subject, String content, boolean isAnonymous, String[] filePaths, boolean isHtml) {
        logger.info("通过网易163向 " + to + " 发送邮件，主题：" + subject);
        
        // 网易邮箱专用SMTP配置
        Properties props = new Properties();
        props.put("mail.smtp.host", config.getHost());
        props.put("mail.smtp.port", config.getPort());
        props.put("mail.smtp.auth", "true");
        
        // 网易邮箱SSL配置
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.trust", "*");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.ssl.checkserveridentity", "false");
        
        // 连接超时设置
        props.put("mail.smtp.connectiontimeout", "30000");
        props.put("mail.smtp.timeout", "30000");
        props.put("mail.smtp.writetimeout", "30000");
        
        // 调试模式（可选）
        props.put("mail.debug", "false");
        
        // 创建会话
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(config.getUsername(), config.getPassword());
            }
        });

        try {
            // 创建邮件消息
            MimeMessage message = new MimeMessage(session);

            // 设置发件人
            if (isAnonymous) {
                message.setFrom(new InternetAddress(config.getUsername(), "匿名发送者"));
            } else {
                message.setFrom(new InternetAddress(config.getUsername()));
            }

            // 设置收件人
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // 设置抄送人
            if (cc != null && cc.length > 0) {
                InternetAddress[] ccAddresses = new InternetAddress[cc.length];
                for (int i = 0; i < cc.length; i++) {
                    ccAddresses[i] = new InternetAddress(cc[i]);
                }
                message.setRecipients(Message.RecipientType.CC, ccAddresses);
            }

            // 设置密送人
            if (bcc != null && bcc.length > 0) {
                InternetAddress[] bccAddresses = new InternetAddress[bcc.length];
                for (int i = 0; i < bcc.length; i++) {
                    bccAddresses[i] = new InternetAddress(bcc[i]);
                }
                message.setRecipients(Message.RecipientType.BCC, bccAddresses);
            }

            // 设置主题
            message.setSubject(subject, "UTF-8");

            // 设置内容和附件
            if (filePaths != null && filePaths.length > 0) {
                // 创建多部分消息
                Multipart multipart = new MimeMultipart();

                // 添加正文部分
                BodyPart textPart = new MimeBodyPart();
                if (isHtml) {
                    textPart.setContent(content, "text/html; charset=UTF-8");
                } else {
                    textPart.setText(content);
                }
                multipart.addBodyPart(textPart);

                // 添加附件部分
                for (String filePath : filePaths) {
                    java.io.File file = new java.io.File(filePath);
                    if (file.exists()) {
                        BodyPart attachmentPart = new MimeBodyPart();
                        attachmentPart.setDataHandler(new jakarta.activation.DataHandler(new jakarta.activation.FileDataSource(file)));
                        attachmentPart.setFileName(MimeUtility.encodeText(file.getName(), "UTF-8", "B"));
                        multipart.addBodyPart(attachmentPart);
                    } else {
                        logger.warning("附件文件不存在：" + filePath);
                    }
                }

                // 设置消息内容为多部分
                message.setContent(multipart);
            } else {
                // 没有附件，直接设置内容
                if (isHtml) {
                    message.setContent(content, "text/html; charset=UTF-8");
                } else {
                    message.setText(content, "UTF-8");
                }
            }

            // 发送邮件
            Transport.send(message);
            logger.info("网易邮箱邮件发送成功，收件人：" + to);
            return true;
        } catch (Exception e) {
            logger.severe("网易邮箱邮件发送失败，收件人：" + to + "，错误信息：" + e.getMessage());
            return false;
        }
    }
}
