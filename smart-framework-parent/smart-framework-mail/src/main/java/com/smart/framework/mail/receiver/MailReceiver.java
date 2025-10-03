package com.smart.framework.mail.receiver;

import com.smart.framework.mail.common.MailInfo;
import java.util.List;

/**
 * 邮件接收接口，定义邮件接收的基本方法
 */
public interface MailReceiver {
    /**
     * 获取最新邮件列表
     * @param count 获取邮件的数量
     * @return 邮件信息列表
     */
    List<MailInfo> getLatestMails(int count);

    /**
     * 获取指定邮件的详细信息
     * @param mailId 邮件ID
     * @return 邮件详细信息
     */
    MailInfo getMailDetail(String mailId);

    /**
     * 下载邮件附件
     * @param mailId 邮件ID
     * @param attachmentIndex 附件索引
     * @param savePath 保存路径
     * @return 是否下载成功
     */
    boolean downloadAttachment(String mailId, int attachmentIndex, String savePath);

    /**
     * 标记邮件为已读
     * @param mailId 邮件ID
     * @return 是否标记成功
     */
    boolean markAsRead(String mailId);

    /**
     * 删除邮件
     * @param mailId 邮件ID
     * @return 是否删除成功
     */
    boolean deleteMail(String mailId);
}
