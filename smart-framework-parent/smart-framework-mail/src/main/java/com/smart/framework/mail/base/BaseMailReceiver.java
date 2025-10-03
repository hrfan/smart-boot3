package com.smart.framework.mail.base;

import com.smart.framework.mail.common.MailConfig;
import com.smart.framework.mail.common.MailInfo;
import com.smart.framework.mail.receiver.MailReceiver;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.*;
import java.util.logging.Logger;
import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * 邮件接收基础抽象类，提供邮件接收的常用方式和标准实现
 */
public abstract class BaseMailReceiver implements MailReceiver {
    protected static final Logger logger = Logger.getLogger(BaseMailReceiver.class.getName());
    protected final MailConfig config;

    public BaseMailReceiver(MailConfig config) {
        this.config = config;
    }

    @Override
    public List<MailInfo> getLatestMails(int count) {
        logger.info("获取最新邮件，数量：" + count);
        try {
            return fetchLatestEmails(count);
        } catch (Exception e) {
            logger.severe("获取最新邮件失败，错误信息：" + e.getMessage());
            return null;
        }
    }

    @Override
    public MailInfo getMailDetail(String mailId) {
        logger.info("获取邮件详情，邮件ID：" + mailId);
        try {
            return fetchMailDetail(mailId);
        } catch (Exception e) {
            logger.severe("获取邮件详情失败，邮件ID：" + mailId + "，错误信息：" + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean downloadAttachment(String mailId, int attachmentIndex, String savePath) {
        logger.info("下载附件，邮件ID：" + mailId + "，附件索引：" + attachmentIndex);
        try {
            // 检查保存路径是否存在，如果不存在则创建
            File file = new File(savePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            // 检查文件是否已存在，如果存在则添加后缀避免覆盖
            String finalPath = getUniqueFilePath(savePath);
            // 下载附件到指定路径
            return performDownloadAttachment(mailId, attachmentIndex, finalPath);
        } catch (Exception e) {
            logger.severe("下载附件失败，邮件ID：" + mailId + "，错误信息：" + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean markAsRead(String mailId) {
        logger.info("标记邮件为已读，邮件ID：" + mailId);
        try {
            return performMarkAsRead(mailId);
        } catch (Exception e) {
            logger.severe("标记邮件为已读失败，邮件ID：" + mailId + "，错误信息：" + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteMail(String mailId) {
        logger.info("删除邮件，邮件ID：" + mailId);
        try {
            return performDeleteMail(mailId);
        } catch (Exception e) {
            logger.severe("删除邮件失败，邮件ID：" + mailId + "，错误信息：" + e.getMessage());
            return false;
        }
    }

    /**
     * 获取唯一的文件路径，如果文件已存在则添加后缀
     * @param originalPath 原始文件路径
     * @return 唯一文件路径
     */
    private String getUniqueFilePath(String originalPath) {
        File file = new File(originalPath);
        if (!file.exists()) {
            return originalPath;
        }
        String baseName = file.getName();
        String extension = "";
        int dotIndex = baseName.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = baseName.substring(dotIndex);
            baseName = baseName.substring(0, dotIndex);
        }
        String parentPath = file.getParent();
        int counter = 1;
        while (file.exists()) {
            String newName = baseName + "_" + counter + extension;
            file = new File(parentPath, newName);
            counter++;
        }
        return file.getAbsolutePath();
    }

    /**
     * 获取最新邮件列表的具体逻辑
     * @param count 获取邮件的数量
     * @return 邮件信息列表
     */
    protected List<MailInfo> fetchLatestEmails(int count) {
        List<MailInfo> mailList = new ArrayList<>();
        try {
            // 设置邮件服务器属性
            Properties props = new Properties();
            props.put("mail.imap.host", config.getHost());
            props.put("mail.imap.port", config.getPort());
            props.put("mail.imap.auth", "true");
            if (config.isSslEnabled()) {
                props.put("mail.imap.socketFactory.class", "jakarta.net.ssl.SSLSocketFactory");
                props.put("mail.imap.socketFactory.port", config.getPort());
            }

            // 创建会话
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(config.getUsername(), config.getPassword());
                }
            });

            // 连接到邮件服务器
            Store store = session.getStore("imap");
            store.connect();

            // 打开收件箱
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            // 获取最新邮件
            int messageCount = inbox.getMessageCount();
            int start = Math.max(1, messageCount - count + 1);
            Message[] messages = inbox.getMessages(start, messageCount);

            // 解析邮件信息
            for (Message message : messages) {
                MailInfo mailInfo = new MailInfo();
                mailInfo.setId(String.valueOf(message.getMessageNumber()));
                mailInfo.setSubject(message.getSubject());
                mailInfo.setFrom(Arrays.toString(message.getFrom()));
                mailInfo.setSentDate(message.getSentDate());

                // 设置收件人
                Address[] toAddresses = message.getRecipients(Message.RecipientType.TO);
                if (toAddresses != null) {
                    List<String> toList = new ArrayList<>();
                    for (Address addr : toAddresses) {
                        toList.add(addr.toString());
                    }
                    mailInfo.setTo(toList);
                }

                // 设置抄送人
                Address[] ccAddresses = message.getRecipients(Message.RecipientType.CC);
                if (ccAddresses != null) {
                    List<String> ccList = new ArrayList<>();
                    for (Address addr : ccAddresses) {
                        ccList.add(addr.toString());
                    }
                    mailInfo.setCc(ccList);
                }

                // 设置密送人
                Address[] bccAddresses = message.getRecipients(Message.RecipientType.BCC);
                if (bccAddresses != null) {
                    List<String> bccList = new ArrayList<>();
                    for (Address addr : bccAddresses) {
                        bccList.add(addr.toString());
                    }
                    mailInfo.setBcc(bccList);
                }

                mailList.add(mailInfo);
            }

            // 关闭连接
            inbox.close(false);
            store.close();

            logger.info("成功获取最新邮件，数量：" + mailList.size());
            return mailList;
        } catch (Exception e) {
            logger.severe("获取最新邮件失败，错误信息：" + e.getMessage());
            return null;
        }
    }

    /**
     * 获取邮件详情的具体逻辑
     * @param mailId 邮件ID
     * @return 邮件详细信息
     */
    protected MailInfo fetchMailDetail(String mailId) {
        try {
            // 设置邮件服务器属性
            Properties props = new Properties();
            props.put("mail.imap.host", config.getHost());
            props.put("mail.imap.port", config.getPort());
            props.put("mail.imap.auth", "true");
            if (config.isSslEnabled()) {
                props.put("mail.imap.socketFactory.class", "jakarta.net.ssl.SSLSocketFactory");
                props.put("mail.imap.socketFactory.port", config.getPort());
            }

            // 创建会话
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(config.getUsername(), config.getPassword());
                }
            });

            // 连接到邮件服务器
            Store store = session.getStore("imap");
            store.connect();

            // 打开收件箱
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            // 获取指定邮件
            int messageNumber = Integer.parseInt(mailId);
            Message message = inbox.getMessage(messageNumber);

            // 解析邮件详细信息
            MailInfo mailInfo = new MailInfo();
            mailInfo.setId(mailId);
            mailInfo.setSubject(message.getSubject());
            mailInfo.setFrom(Arrays.toString(message.getFrom()));
            mailInfo.setSentDate(message.getSentDate());

            // 设置收件人
            Address[] toAddresses = message.getRecipients(Message.RecipientType.TO);
            if (toAddresses != null) {
                List<String> toList = new ArrayList<>();
                for (Address addr : toAddresses) {
                    toList.add(addr.toString());
                }
                mailInfo.setTo(toList);
            }

            // 设置抄送人
            Address[] ccAddresses = message.getRecipients(Message.RecipientType.CC);
            if (ccAddresses != null) {
                List<String> ccList = new ArrayList<>();
                for (Address addr : ccAddresses) {
                    ccList.add(addr.toString());
                }
                mailInfo.setCc(ccList);
            }

            // 设置密送人
            Address[] bccAddresses = message.getRecipients(Message.RecipientType.BCC);
            if (bccAddresses != null) {
                List<String> bccList = new ArrayList<>();
                for (Address addr : bccAddresses) {
                    bccList.add(addr.toString());
                }
                mailInfo.setBcc(bccList);
            }

            // 获取邮件内容
            String content = getMailContent(message);
            mailInfo.setContent(content);

            // 获取附件名称
            List<String> attachmentNames = getAttachmentNames(message);
            mailInfo.setAttachmentNames(attachmentNames);

            // 关闭连接
            inbox.close(false);
            store.close();

            logger.info("成功获取邮件详情，邮件ID：" + mailId);
            return mailInfo;
        } catch (Exception e) {
            logger.severe("获取邮件详情失败，邮件ID：" + mailId + "，错误信息：" + e.getMessage());
            return null;
        }
    }

    /**
     * 执行下载邮件附件的具体逻辑
     * @param mailId 邮件ID
     * @param attachmentIndex 附件索引
     * @param savePath 保存路径
     * @return 是否下载成功
     */
    protected boolean performDownloadAttachment(String mailId, int attachmentIndex, String savePath) {
        try {
            // 设置邮件服务器属性
            Properties props = new Properties();
            props.put("mail.imap.host", config.getHost());
            props.put("mail.imap.port", config.getPort());
            props.put("mail.imap.auth", "true");
            if (config.isSslEnabled()) {
                props.put("mail.imap.socketFactory.class", "jakarta.net.ssl.SSLSocketFactory");
                props.put("mail.imap.socketFactory.port", config.getPort());
            }

            // 创建会话
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(config.getUsername(), config.getPassword());
                }
            });

            // 连接到邮件服务器
            Store store = session.getStore("imap");
            store.connect();

            // 打开收件箱
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            // 获取指定邮件
            int messageNumber = Integer.parseInt(mailId);
            Message message = inbox.getMessage(messageNumber);

            // 获取附件
            if (message.isMimeType("multipart/*")) {
                Multipart multipart = (Multipart) message.getContent();
                int currentIndex = 0;
                for (int i = 0; i < multipart.getCount(); i++) {
                    BodyPart part = multipart.getBodyPart(i);
                    if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                        if (currentIndex == attachmentIndex) {
                            // 保存附件
                            try (java.io.InputStream inputStream = part.getInputStream();
                                 java.io.FileOutputStream outputStream = new java.io.FileOutputStream(savePath)) {
                                byte[] buffer = new byte[4096];
                                int bytesRead;
                                while ((bytesRead = inputStream.read(buffer)) != -1) {
                                    outputStream.write(buffer, 0, bytesRead);
                                }
                                logger.info("成功下载附件，邮件ID：" + mailId + "，附件索引：" + attachmentIndex);
                                inbox.close(false);
                                store.close();
                                return true;
                            }
                        }
                        currentIndex++;
                    }
                }
            }

            // 关闭连接
            inbox.close(false);
            store.close();

            logger.warning("未找到指定附件，邮件ID：" + mailId + "，附件索引：" + attachmentIndex);
            return false;
        } catch (Exception e) {
            logger.severe("下载附件失败，邮件ID：" + mailId + "，附件索引：" + attachmentIndex + "，错误信息：" + e.getMessage());
            return false;
        }
    }

    /**
     * 执行标记邮件为已读的具体逻辑
     * @param mailId 邮件ID
     * @return 是否标记成功
     */
    protected boolean performMarkAsRead(String mailId) {
        try {
            // 设置邮件服务器属性
            Properties props = new Properties();
            props.put("mail.imap.host", config.getHost());
            props.put("mail.imap.port", config.getPort());
            props.put("mail.imap.auth", "true");
            if (config.isSslEnabled()) {
                props.put("mail.imap.socketFactory.class", "jakarta.net.ssl.SSLSocketFactory");
                props.put("mail.imap.socketFactory.port", config.getPort());
            }

            // 创建会话
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(config.getUsername(), config.getPassword());
                }
            });

            // 连接到邮件服务器
            Store store = session.getStore("imap");
            store.connect();

            // 打开收件箱
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            // 获取指定邮件
            int messageNumber = Integer.parseInt(mailId);
            Message message = inbox.getMessage(messageNumber);

            // 标记为已读
            message.setFlag(Flags.Flag.SEEN, true);

            // 关闭连接
            inbox.close(true);
            store.close();

            logger.info("成功标记邮件为已读，邮件ID：" + mailId);
            return true;
        } catch (Exception e) {
            logger.severe("标记邮件为已读失败，邮件ID：" + mailId + "，错误信息：" + e.getMessage());
            return false;
        }
    }

    /**
     * 执行删除邮件的具体逻辑
     * @param mailId 邮件ID
     * @return 是否删除成功
     */
    protected boolean performDeleteMail(String mailId) {
        try {
            // 设置邮件服务器属性
            Properties props = new Properties();
            props.put("mail.imap.host", config.getHost());
            props.put("mail.imap.port", config.getPort());
            props.put("mail.imap.auth", "true");
            if (config.isSslEnabled()) {
                props.put("mail.imap.socketFactory.class", "jakarta.net.ssl.SSLSocketFactory");
                props.put("mail.imap.socketFactory.port", config.getPort());
            }

            // 创建会话
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(config.getUsername(), config.getPassword());
                }
            });

            // 连接到邮件服务器
            Store store = session.getStore("imap");
            store.connect();

            // 打开收件箱
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            // 获取指定邮件
            int messageNumber = Integer.parseInt(mailId);
            Message message = inbox.getMessage(messageNumber);

            // 标记为删除
            message.setFlag(Flags.Flag.DELETED, true);

            // 关闭连接
            inbox.close(true);
            store.close();

            logger.info("成功删除邮件，邮件ID：" + mailId);
            return true;
        } catch (Exception e) {
            logger.severe("删除邮件失败，邮件ID：" + mailId + "，错误信息：" + e.getMessage());
            return false;
        }
    }

    /**
     * 获取邮件内容
     * @param message 邮件消息
     * @return 邮件内容
     * @throws Exception 异常
     */
    private String getMailContent(Message message) throws Exception {
        if (message.isMimeType("text/plain") || message.isMimeType("text/html")) {
            Object content = message.getContent();
            if (content != null) {
                return content.toString();
            }
        } else if (message.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) message.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart part = multipart.getBodyPart(i);
                if (part.isMimeType("text/plain") || part.isMimeType("text/html")) {
                    Object content = part.getContent();
                    if (content != null) {
                        return content.toString();
                    }
                }
            }
        }
        return "";
    }

    /**
     * 获取附件名称列表
     * @param message 邮件消息
     * @return 附件名称列表
     * @throws Exception 异常
     */
    private List<String> getAttachmentNames(Message message) throws Exception {
        List<String> attachmentNames = new ArrayList<>();
        if (message.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) message.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart part = multipart.getBodyPart(i);
                if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                    String fileName = part.getFileName();
                    if (fileName != null) {
                        // 解码文件名
                        fileName = MimeUtility.decodeText(fileName);
                        attachmentNames.add(fileName);
                    }
                }
            }
        }
        return attachmentNames;
    }

    /**
     * 子类可以重写此方法以提供特定的实现
     * @param count 获取邮件的数量
     * @return 邮件信息列表
     */
    protected List<MailInfo> performFetchLatestEmails(int count) {
        return fetchLatestEmails(count);
    }

    /**
     * 子类可以重写此方法以提供特定的实现
     * @param mailId 邮件ID
     * @return 邮件详细信息
     */
    protected MailInfo performFetchMailDetail(String mailId) {
        return fetchMailDetail(mailId);
    }

    // 以下方法已实现，子类可以根据需要重写
    /*
    protected abstract List<MailInfo> fetchLatestEmails(int count);
    protected abstract MailInfo fetchMailDetail(String mailId);
    protected abstract boolean performDownloadAttachment(String mailId, int attachmentIndex, String savePath);
    protected abstract boolean performMarkAsRead(String mailId);
    protected abstract boolean performDeleteMail(String mailId);
    */
}
