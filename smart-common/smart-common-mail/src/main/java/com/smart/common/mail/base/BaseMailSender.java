package com.smart.common.mail.base;

import com.smart.common.mail.common.MailConfig;
import com.smart.common.mail.common.AttachmentData;
import com.smart.common.mail.sender.MailSender;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import java.util.logging.Logger;
import java.io.File;

/**
 * 邮件发送基础抽象类，提供邮件发送的常用方式和标准实现
 */
public abstract class BaseMailSender implements MailSender {
    protected static final Logger logger = Logger.getLogger(BaseMailSender.class.getName());
    protected final MailConfig config;

    public BaseMailSender(MailConfig config) {
        this.config = config;
    }

    @Override
    public boolean sendSimpleMail(String to, String subject, String content) {
        logger.info("发送简单邮件到：" + to + "，主题：" + subject);
        try {
            return sendMail(to, null, null, subject, content, false, null);
        } catch (Exception e) {
            logger.severe("发送简单邮件失败，收件人：" + to + "，错误信息：" + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean sendSimpleMailWithCC(String to, String[] cc, String subject, String content) {
        logger.info("发送简单邮件到：" + to + "，抄送人：" + arrayToString(cc) + "，主题：" + subject);
        try {
            return sendMail(to, cc, null, subject, content, false, null);
        } catch (Exception e) {
            logger.severe("发送简单邮件（带抄送）失败，收件人：" + to + "，错误信息：" + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean sendAnonymousSimpleMail(String to, String subject, String content) {
        logger.info("发送匿名简单邮件到：" + to + "，主题：" + subject);
        try {
            return sendMail(to, null, null, subject, content, true, null);
        } catch (Exception e) {
            logger.severe("发送匿名简单邮件失败，收件人：" + to + "，错误信息：" + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean sendHtmlMail(String to, String subject, String content) {
        logger.info("发送HTML邮件到：" + to + "，主题：" + subject);
        try {
            return sendMail(to, null, null, subject, content, false, null, true);
        } catch (Exception e) {
            logger.severe("发送HTML邮件失败，收件人：" + to + "，错误信息：" + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean sendHtmlMailWithCC(String to, String[] cc, String subject, String content) {
        logger.info("发送HTML邮件到：" + to + "，抄送人：" + arrayToString(cc) + "，主题：" + subject);
        try {
            return sendMail(to, cc, null, subject, content, false, null, true);
        } catch (Exception e) {
            logger.severe("发送HTML邮件（带抄送）失败，收件人：" + to + "，错误信息：" + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean sendAnonymousHtmlMail(String to, String subject, String content) {
        logger.info("发送匿名HTML邮件到：" + to + "，主题：" + subject);
        try {
            return sendMail(to, null, null, subject, content, true, null, true);
        } catch (Exception e) {
            logger.severe("发送匿名HTML邮件失败，收件人：" + to + "，错误信息：" + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean sendAttachmentMail(String to, String subject, String content, String filePath) {
        logger.info("发送带附件邮件到：" + to + "，主题：" + subject + "，附件路径：" + filePath);
        try {
            return sendMail(to, null, null, subject, content, false, new String[]{filePath});
        } catch (Exception e) {
            logger.severe("发送带附件邮件失败，收件人：" + to + "，错误信息：" + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean sendAttachmentMailWithCC(String to, String[] cc, String subject, String content, String filePath) {
        logger.info("发送带附件邮件到：" + to + "，抄送人：" + arrayToString(cc) + "，主题：" + subject + "，附件路径：" + filePath);
        try {
            return sendMail(to, cc, null, subject, content, false, new String[]{filePath});
        } catch (Exception e) {
            logger.severe("发送带附件邮件（带抄送）失败，收件人：" + to + "，错误信息：" + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean sendAnonymousAttachmentMail(String to, String subject, String content, String filePath) {
        logger.info("发送匿名带附件邮件到：" + to + "，主题：" + subject + "，附件路径：" + filePath);
        try {
            return sendMail(to, null, null, subject, content, true, new String[]{filePath});
        } catch (Exception e) {
            logger.severe("发送匿名带附件邮件失败，收件人：" + to + "，错误信息：" + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean sendAttachmentMail(String to, String subject, String content, String[] filePaths) {
        logger.info("发送带多个附件邮件到：" + to + "，主题：" + subject);
        try {
            return sendMail(to, null, null, subject, content, false, filePaths);
        } catch (Exception e) {
            logger.severe("发送带多个附件邮件失败，收件人：" + to + "，错误信息：" + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean sendAttachmentMailWithCC(String to, String[] cc, String subject, String content, String[] filePaths) {
        logger.info("发送带多个附件邮件到：" + to + "，抄送人：" + arrayToString(cc) + "，主题：" + subject);
        try {
            return sendMail(to, cc, null, subject, content, false, filePaths);
        } catch (Exception e) {
            logger.severe("发送带多个附件邮件（带抄送）失败，收件人：" + to + "，错误信息：" + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean sendAnonymousAttachmentMail(String to, String subject, String content, String[] filePaths) {
        logger.info("发送匿名带多个附件邮件到：" + to + "，主题：" + subject);
        try {
            return sendMail(to, null, null, subject, content, true, filePaths);
        } catch (Exception e) {
            logger.severe("发送匿名带多个附件邮件失败，收件人：" + to + "，错误信息：" + e.getMessage());
            return false;
        }
    }

    /**
     * 将字符串数组转换为字符串，用于日志记录
     * @param array 字符串数组
     * @return 转换后的字符串
     */
    private String arrayToString(String[] array) {
        if (array == null || array.length == 0) {
            return "无";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i < array.length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    /**
     * 通用邮件发送方法，包含所有可能的参数
     * @param to 收件人地址
     * @param cc 抄送人地址数组
     * @param bcc 密送人地址数组
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param isAnonymous 是否匿名发送
     * @param filePaths 附件路径数组
     * @param isHtml 是否为HTML格式
     * @return 是否发送成功
     */
    protected boolean sendMail(String to, String[] cc, String[] bcc, String subject, String content, boolean isAnonymous, String[] filePaths, boolean isHtml) {
        // 设置邮件服务器属性
        Properties props = new Properties();
        props.put("mail.smtp.host", config.getHost());
        props.put("mail.smtp.port", config.getPort());
        props.put("mail.smtp.auth", "true");
        
        // 网易邮箱特殊配置
        if (config.getHost().contains("163.com") || config.getHost().contains("126.com")) {
            // 网易邮箱使用SSL连接
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.trust", "*");
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.starttls.required", "true");
            // 禁用主机名验证（仅用于测试环境，生产环境建议使用正确的证书）
            props.put("mail.smtp.ssl.checkserveridentity", "false");
            // 设置连接超时
            props.put("mail.smtp.connectiontimeout", "30000");
            props.put("mail.smtp.timeout", "30000");
        } else if (config.isSslEnabled()) {
            // 其他邮箱的SSL配置
            props.put("mail.smtp.socketFactory.class", "jakarta.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.port", config.getPort());
        }

        // 创建会话
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(config.getUsername(), config.getPassword());
            }
        });

        try {
            // 创建邮件消息
            MimeMessage message = new MimeMessage(session);

            // 设置发件人（如果是匿名发送，则使用默认发件人地址但不显示）
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
                    File file = new File(filePath);
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
            logger.info("邮件发送成功，收件人：" + to);
            return true;
        } catch (Exception e) {
            logger.severe("邮件发送失败，收件人：" + to + "，错误信息：" + e.getMessage());
            return false;
        }
    }

    /**
     * 通用邮件发送方法，包含所有可能的参数，默认非HTML格式
     * @param to 收件人地址
     * @param cc 抄送人地址数组
     * @param bcc 密送人地址数组
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param isAnonymous 是否匿名发送
     * @param filePaths 附件路径数组
     * @return 是否发送成功
     */
    protected boolean sendMail(String to, String[] cc, String[] bcc, String subject, String content, boolean isAnonymous, String[] filePaths) {
        return sendMail(to, cc, bcc, subject, content, isAnonymous, filePaths, false);
    }

    /**
     * 执行邮件发送的具体逻辑，由子类实现
     * 子类可以重写此方法以提供特定的实现
     * @param to 收件人地址
     * @param cc 抄送人地址数组
     * @param bcc 密送人地址数组
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param isAnonymous 是否匿名发送
     * @param filePaths 附件路径数组
     * @param isHtml 是否为HTML格式
     * @return 是否发送成功
     */
    protected boolean performSendMail(String to, String[] cc, String[] bcc, String subject, String content, boolean isAnonymous, String[] filePaths, boolean isHtml) {
        // 默认使用父类的实现
        return sendMail(to, cc, bcc, subject, content, isAnonymous, filePaths, isHtml);
    }

    // ========== Byte流附件发送方法实现 ==========

    @Override
    public boolean sendByteAttachmentMail(String to, String subject, String content, AttachmentData attachment) {
        logger.info("发送带byte流附件邮件到：" + to + "，主题：" + subject + "，附件：" + attachment.getFileName());
        try {
            return sendMailWithAttachments(to, null, null, subject, content, false, new AttachmentData[]{attachment});
        } catch (Exception e) {
            logger.severe("发送带byte流附件邮件失败，收件人：" + to + "，错误信息：" + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean sendByteAttachmentMailWithCC(String to, String[] cc, String subject, String content, AttachmentData attachment) {
        logger.info("发送带byte流附件邮件到：" + to + "，抄送人：" + arrayToString(cc) + "，主题：" + subject + "，附件：" + attachment.getFileName());
        try {
            return sendMailWithAttachments(to, cc, null, subject, content, false, new AttachmentData[]{attachment});
        } catch (Exception e) {
            logger.severe("发送带byte流附件邮件（带抄送）失败，收件人：" + to + "，错误信息：" + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean sendAnonymousByteAttachmentMail(String to, String subject, String content, AttachmentData attachment) {
        logger.info("发送匿名带byte流附件邮件到：" + to + "，主题：" + subject + "，附件：" + attachment.getFileName());
        try {
            return sendMailWithAttachments(to, null, null, subject, content, true, new AttachmentData[]{attachment});
        } catch (Exception e) {
            logger.severe("发送匿名带byte流附件邮件失败，收件人：" + to + "，错误信息：" + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean sendByteAttachmentMail(String to, String subject, String content, AttachmentData[] attachments) {
        logger.info("发送带多个byte流附件邮件到：" + to + "，主题：" + subject + "，附件数量：" + attachments.length);
        try {
            return sendMailWithAttachments(to, null, null, subject, content, false, attachments);
        } catch (Exception e) {
            logger.severe("发送带多个byte流附件邮件失败，收件人：" + to + "，错误信息：" + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean sendByteAttachmentMailWithCC(String to, String[] cc, String subject, String content, AttachmentData[] attachments) {
        logger.info("发送带多个byte流附件邮件到：" + to + "，抄送人：" + arrayToString(cc) + "，主题：" + subject + "，附件数量：" + attachments.length);
        try {
            return sendMailWithAttachments(to, cc, null, subject, content, false, attachments);
        } catch (Exception e) {
            logger.severe("发送带多个byte流附件邮件（带抄送）失败，收件人：" + to + "，错误信息：" + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean sendAnonymousByteAttachmentMail(String to, String subject, String content, AttachmentData[] attachments) {
        logger.info("发送匿名带多个byte流附件邮件到：" + to + "，主题：" + subject + "，附件数量：" + attachments.length);
        try {
            return sendMailWithAttachments(to, null, null, subject, content, true, attachments);
        } catch (Exception e) {
            logger.severe("发送匿名带多个byte流附件邮件失败，收件人：" + to + "，错误信息：" + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean sendHtmlByteAttachmentMail(String to, String subject, String content, AttachmentData attachment) {
        logger.info("发送HTML格式带byte流附件邮件到：" + to + "，主题：" + subject + "，附件：" + attachment.getFileName());
        try {
            return sendMailWithAttachments(to, null, null, subject, content, false, new AttachmentData[]{attachment}, true);
        } catch (Exception e) {
            logger.severe("发送HTML格式带byte流附件邮件失败，收件人：" + to + "，错误信息：" + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean sendHtmlByteAttachmentMailWithCC(String to, String[] cc, String subject, String content, AttachmentData attachment) {
        logger.info("发送HTML格式带byte流附件邮件到：" + to + "，抄送人：" + arrayToString(cc) + "，主题：" + subject + "，附件：" + attachment.getFileName());
        try {
            return sendMailWithAttachments(to, cc, null, subject, content, false, new AttachmentData[]{attachment}, true);
        } catch (Exception e) {
            logger.severe("发送HTML格式带byte流附件邮件（带抄送）失败，收件人：" + to + "，错误信息：" + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean sendAnonymousHtmlByteAttachmentMail(String to, String subject, String content, AttachmentData attachment) {
        logger.info("发送匿名HTML格式带byte流附件邮件到：" + to + "，主题：" + subject + "，附件：" + attachment.getFileName());
        try {
            return sendMailWithAttachments(to, null, null, subject, content, true, new AttachmentData[]{attachment}, true);
        } catch (Exception e) {
            logger.severe("发送匿名HTML格式带byte流附件邮件失败，收件人：" + to + "，错误信息：" + e.getMessage());
            return false;
        }
    }

    /**
     * 使用AttachmentData发送邮件的通用方法
     * @param to 收件人地址
     * @param cc 抄送人地址数组
     * @param bcc 密送人地址数组
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param isAnonymous 是否匿名发送
     * @param attachments 附件数据数组
     * @param isHtml 是否为HTML格式
     * @return 是否发送成功
     */
    protected boolean sendMailWithAttachments(String to, String[] cc, String[] bcc, String subject, String content, boolean isAnonymous, AttachmentData[] attachments, boolean isHtml) {
        // 设置邮件服务器属性
        Properties props = new Properties();
        props.put("mail.smtp.host", config.getHost());
        props.put("mail.smtp.port", config.getPort());
        props.put("mail.smtp.auth", "true");
        
        // 网易邮箱特殊配置
        if (config.getHost().contains("163.com") || config.getHost().contains("126.com")) {
            // 网易邮箱使用SSL连接
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.trust", "*");
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.starttls.required", "true");
            // 禁用主机名验证（仅用于测试环境，生产环境建议使用正确的证书）
            props.put("mail.smtp.ssl.checkserveridentity", "false");
            // 设置连接超时
            props.put("mail.smtp.connectiontimeout", "30000");
            props.put("mail.smtp.timeout", "30000");
        } else if (config.isSslEnabled()) {
            // 其他邮箱的SSL配置
            props.put("mail.smtp.socketFactory.class", "jakarta.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.port", config.getPort());
        }

        // 创建会话
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(config.getUsername(), config.getPassword());
            }
        });

        try {
            // 创建邮件消息
            MimeMessage message = new MimeMessage(session);

            // 设置发件人（如果是匿名发送，则使用默认发件人地址但不显示）
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
            if (attachments != null && attachments.length > 0) {
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
                for (AttachmentData attachment : attachments) {
                    BodyPart attachmentPart = new MimeBodyPart();
                    
                    if (attachment.isByteData()) {
                        // 处理byte流附件
                        attachmentPart.setDataHandler(new jakarta.activation.DataHandler(
                            new jakarta.mail.util.ByteArrayDataSource(attachment.getData(), attachment.getContentType())
                        ));
                        attachmentPart.setFileName(MimeUtility.encodeText(attachment.getFileName(), "UTF-8", "B"));
                    } else if (attachment.isFilePath()) {
                        // 处理文件路径附件
                        File file = new File(attachment.getFilePath());
                        if (file.exists()) {
                            attachmentPart.setDataHandler(new jakarta.activation.DataHandler(new jakarta.activation.FileDataSource(file)));
                            attachmentPart.setFileName(MimeUtility.encodeText(attachment.getFileName(), "UTF-8", "B"));
                        } else {
                            logger.warning("附件文件不存在：" + attachment.getFilePath());
                            continue;
                        }
                    }
                    
                    multipart.addBodyPart(attachmentPart);
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
            logger.info("邮件发送成功，收件人：" + to);
            return true;
        } catch (Exception e) {
            logger.severe("邮件发送失败，收件人：" + to + "，错误信息：" + e.getMessage());
            return false;
        }
    }

    /**
     * 使用AttachmentData发送邮件的通用方法，默认非HTML格式
     * @param to 收件人地址
     * @param cc 抄送人地址数组
     * @param bcc 密送人地址数组
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param isAnonymous 是否匿名发送
     * @param attachments 附件数据数组
     * @return 是否发送成功
     */
    protected boolean sendMailWithAttachments(String to, String[] cc, String[] bcc, String subject, String content, boolean isAnonymous, AttachmentData[] attachments) {
        return sendMailWithAttachments(to, cc, bcc, subject, content, isAnonymous, attachments, false);
    }

    // 以下方法不再需要，因为所有逻辑都统一到 sendMail 方法中
    /*
    protected abstract boolean performSendSimpleMail(String to, String subject, String content);
    protected abstract boolean performSendSimpleMailWithCC(String to, String[] cc, String subject, String content);
    protected abstract boolean performSendAnonymousSimpleMail(String to, String subject, String content);
    protected abstract boolean performSendHtmlMail(String to, String subject, String content);
    protected abstract boolean performSendHtmlMailWithCC(String to, String[] cc, String subject, String content);
    protected abstract boolean performSendAnonymousHtmlMail(String to, String subject, String content);
    protected abstract boolean performSendAttachmentMail(String to, String subject, String content, String filePath);
    protected abstract boolean performSendAttachmentMailWithCC(String to, String[] cc, String subject, String content, String filePath);
    protected abstract boolean performSendAnonymousAttachmentMail(String to, String subject, String content, String filePath);
    protected abstract boolean performSendAttachmentMail(String to, String subject, String content, String[] filePaths);
    protected abstract boolean performSendAttachmentMailWithCC(String to, String[] cc, String subject, String content, String[] filePaths);
    protected abstract boolean performSendAnonymousAttachmentMail(String to, String subject, String content, String[] filePaths);
    */
}
