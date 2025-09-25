package com.smart.common.mail.common;

import java.util.Date;
import java.util.List;

/**
 * 邮件信息类，用于存储邮件的各种信息
 */
public class MailInfo {
    /**
     * 邮件ID
     */
    private String id;
    
    /**
     * 邮件主题
     */
    private String subject;
    
    /**
     * 发件人
     */
    private String from;
    
    /**
     * 收件人列表
     */
    private List<String> to;
    
    /**
     * 抄送人列表
     */
    private List<String> cc;
    
    /**
     * 密送人列表
     */
    private List<String> bcc;
    
    /**
     * 发送日期
     */
    private Date sentDate;
    
    /**
     * 邮件内容
     */
    private String content;
    
    /**
     * 附件名称列表
     */
    private List<String> attachmentNames;

    /**
     * 获取邮件ID
     * @return 邮件ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置邮件ID
     * @param id 邮件ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取邮件主题
     * @return 邮件主题
     */
    public String getSubject() {
        return subject;
    }

    /**
     * 设置邮件主题
     * @param subject 邮件主题
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * 获取发件人
     * @return 发件人
     */
    public String getFrom() {
        return from;
    }

    /**
     * 设置发件人
     * @param from 发件人
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * 获取收件人列表
     * @return 收件人列表
     */
    public List<String> getTo() {
        return to;
    }

    /**
     * 设置收件人列表
     * @param to 收件人列表
     */
    public void setTo(List<String> to) {
        this.to = to;
    }

    /**
     * 获取抄送人列表
     * @return 抄送人列表
     */
    public List<String> getCc() {
        return cc;
    }

    /**
     * 设置抄送人列表
     * @param cc 抄送人列表
     */
    public void setCc(List<String> cc) {
        this.cc = cc;
    }

    /**
     * 获取密送人列表
     * @return 密送人列表
     */
    public List<String> getBcc() {
        return bcc;
    }

    /**
     * 设置密送人列表
     * @param bcc 密送人列表
     */
    public void setBcc(List<String> bcc) {
        this.bcc = bcc;
    }

    /**
     * 获取发送日期
     * @return 发送日期
     */
    public Date getSentDate() {
        return sentDate;
    }

    /**
     * 设置发送日期
     * @param sentDate 发送日期
     */
    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    /**
     * 获取邮件内容
     * @return 邮件内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置邮件内容
     * @param content 邮件内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取附件名称列表
     * @return 附件名称列表
     */
    public List<String> getAttachmentNames() {
        return attachmentNames;
    }

    /**
     * 设置附件名称列表
     * @param attachmentNames 附件名称列表
     */
    public void setAttachmentNames(List<String> attachmentNames) {
        this.attachmentNames = attachmentNames;
    }
}
