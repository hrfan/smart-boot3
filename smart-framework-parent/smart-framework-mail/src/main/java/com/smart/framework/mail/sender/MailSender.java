package com.smart.framework.mail.sender;

import com.smart.framework.mail.common.AttachmentData;

/**
 * 邮件发送接口，定义邮件发送的基本方法
 */
public interface MailSender {
    /**
     * 发送简单文本邮件
     * @param to 收件人地址
     * @param subject 邮件主题
     * @param content 邮件内容
     * @return 是否发送成功
     */
    boolean sendSimpleMail(String to, String subject, String content);

    /**
     * 发送简单文本邮件，支持抄送人
     * @param to 收件人地址
     * @param cc 抄送人地址数组
     * @param subject 邮件主题
     * @param content 邮件内容
     * @return 是否发送成功
     */
    boolean sendSimpleMailWithCC(String to, String[] cc, String subject, String content);

    /**
     * 发送匿名简单文本邮件
     * @param to 收件人地址
     * @param subject 邮件主题
     * @param content 邮件内容
     * @return 是否发送成功
     */
    boolean sendAnonymousSimpleMail(String to, String subject, String content);

    /**
     * 发送HTML格式邮件
     * @param to 收件人地址
     * @param subject 邮件主题
     * @param content HTML格式的邮件内容
     * @return 是否发送成功
     */
    boolean sendHtmlMail(String to, String subject, String content);

    /**
     * 发送HTML格式邮件，支持抄送人
     * @param to 收件人地址
     * @param cc 抄送人地址数组
     * @param subject 邮件主题
     * @param content HTML格式的邮件内容
     * @return 是否发送成功
     */
    boolean sendHtmlMailWithCC(String to, String[] cc, String subject, String content);

    /**
     * 发送匿名HTML格式邮件
     * @param to 收件人地址
     * @param subject 邮件主题
     * @param content HTML格式的邮件内容
     * @return 是否发送成功
     */
    boolean sendAnonymousHtmlMail(String to, String subject, String content);

    /**
     * 发送带附件的邮件
     * @param to 收件人地址
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param filePath 附件路径
     * @return 是否发送成功
     */
    boolean sendAttachmentMail(String to, String subject, String content, String filePath);

    /**
     * 发送带附件的邮件，支持抄送人
     * @param to 收件人地址
     * @param cc 抄送人地址数组
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param filePath 附件路径
     * @return 是否发送成功
     */
    boolean sendAttachmentMailWithCC(String to, String[] cc, String subject, String content, String filePath);

    /**
     * 发送匿名带附件的邮件
     * @param to 收件人地址
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param filePath 附件路径
     * @return 是否发送成功
     */
    boolean sendAnonymousAttachmentMail(String to, String subject, String content, String filePath);

    /**
     * 发送带多个附件的邮件
     * @param to 收件人地址
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param filePaths 多个附件路径
     * @return 是否发送成功
     */
    boolean sendAttachmentMail(String to, String subject, String content, String[] filePaths);

    /**
     * 发送带多个附件的邮件，支持抄送人
     * @param to 收件人地址
     * @param cc 抄送人地址数组
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param filePaths 多个附件路径
     * @return 是否发送成功
     */
    boolean sendAttachmentMailWithCC(String to, String[] cc, String subject, String content, String[] filePaths);

    /**
     * 发送匿名带多个附件的邮件
     * @param to 收件人地址
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param filePaths 多个附件路径
     * @return 是否发送成功
     */
    boolean sendAnonymousAttachmentMail(String to, String subject, String content, String[] filePaths);

    // ========== Byte流附件发送方法 ==========

    /**
     * 发送带byte流附件的邮件
     * @param to 收件人地址
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param attachment 附件数据
     * @return 是否发送成功
     */
    boolean sendByteAttachmentMail(String to, String subject, String content, AttachmentData attachment);

    /**
     * 发送带byte流附件的邮件，支持抄送人
     * @param to 收件人地址
     * @param cc 抄送人地址数组
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param attachment 附件数据
     * @return 是否发送成功
     */
    boolean sendByteAttachmentMailWithCC(String to, String[] cc, String subject, String content, AttachmentData attachment);

    /**
     * 发送匿名带byte流附件的邮件
     * @param to 收件人地址
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param attachment 附件数据
     * @return 是否发送成功
     */
    boolean sendAnonymousByteAttachmentMail(String to, String subject, String content, AttachmentData attachment);

    /**
     * 发送带多个byte流附件的邮件
     * @param to 收件人地址
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param attachments 多个附件数据
     * @return 是否发送成功
     */
    boolean sendByteAttachmentMail(String to, String subject, String content, AttachmentData[] attachments);

    /**
     * 发送带多个byte流附件的邮件，支持抄送人
     * @param to 收件人地址
     * @param cc 抄送人地址数组
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param attachments 多个附件数据
     * @return 是否发送成功
     */
    boolean sendByteAttachmentMailWithCC(String to, String[] cc, String subject, String content, AttachmentData[] attachments);

    /**
     * 发送匿名带多个byte流附件的邮件
     * @param to 收件人地址
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param attachments 多个附件数据
     * @return 是否发送成功
     */
    boolean sendAnonymousByteAttachmentMail(String to, String subject, String content, AttachmentData[] attachments);

    /**
     * 发送HTML格式带byte流附件的邮件
     * @param to 收件人地址
     * @param subject 邮件主题
     * @param content HTML格式的邮件内容
     * @param attachment 附件数据
     * @return 是否发送成功
     */
    boolean sendHtmlByteAttachmentMail(String to, String subject, String content, AttachmentData attachment);

    /**
     * 发送HTML格式带byte流附件的邮件，支持抄送人
     * @param to 收件人地址
     * @param cc 抄送人地址数组
     * @param subject 邮件主题
     * @param content HTML格式的邮件内容
     * @param attachment 附件数据
     * @return 是否发送成功
     */
    boolean sendHtmlByteAttachmentMailWithCC(String to, String[] cc, String subject, String content, AttachmentData attachment);

    /**
     * 发送匿名HTML格式带byte流附件的邮件
     * @param to 收件人地址
     * @param subject 邮件主题
     * @param content HTML格式的邮件内容
     * @param attachment 附件数据
     * @return 是否发送成功
     */
    boolean sendAnonymousHtmlByteAttachmentMail(String to, String subject, String content, AttachmentData attachment);
}
