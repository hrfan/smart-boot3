package com.smart.common.mail.receiver;

import com.smart.common.mail.base.BaseMailReceiver;
import com.smart.common.mail.common.MailConfig;
import com.smart.common.mail.common.MailInfo;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Logger;

/**
 * 网易163邮件接收实现类
 */
public class NetEaseMailReceiver extends BaseMailReceiver {
    private static final Logger logger = Logger.getLogger(NetEaseMailReceiver.class.getName());

    public NetEaseMailReceiver(MailConfig config) {
        super(config);
    }

    @Override
    protected List<MailInfo> fetchLatestEmails(int count) {
        logger.info("通过网易163获取最新邮件，数量：" + count);
        // 网易163邮件接收使用父类的通用实现
        // 父类已经实现了完整的邮件接收逻辑，包括IMAP连接、认证、邮件解析等
        return super.fetchLatestEmails(count);
    }

    @Override
    protected MailInfo fetchMailDetail(String mailId) {
        logger.info("通过网易163获取邮件详情，邮件ID：" + mailId);
        // 网易163邮件详情获取使用父类的通用实现
        // 父类已经实现了完整的邮件详情获取逻辑，包括内容解析、附件处理等
        return super.fetchMailDetail(mailId);
    }

    @Override
    protected boolean performDownloadAttachment(String mailId, int attachmentIndex, String savePath) {
        logger.info("通过网易163下载附件，邮件ID：" + mailId + "，附件索引：" + attachmentIndex);
        // 网易163附件下载使用父类的通用实现
        // 父类已经实现了完整的附件下载逻辑，包括文件流处理、路径管理等
        return super.performDownloadAttachment(mailId, attachmentIndex, savePath);
    }

    @Override
    protected boolean performMarkAsRead(String mailId) {
        logger.info("通过网易163标记邮件为已读，邮件ID：" + mailId);
        // 网易163标记已读使用父类的通用实现
        // 父类已经实现了完整的标记已读逻辑
        return super.performMarkAsRead(mailId);
    }

    @Override
    protected boolean performDeleteMail(String mailId) {
        logger.info("通过网易163删除邮件，邮件ID：" + mailId);
        // 网易163删除邮件使用父类的通用实现
        // 父类已经实现了完整的删除邮件逻辑
        return super.performDeleteMail(mailId);
    }
}
