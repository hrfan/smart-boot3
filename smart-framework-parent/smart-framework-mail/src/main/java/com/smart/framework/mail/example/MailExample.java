package com.smart.framework.mail.example;

import com.smart.framework.mail.common.MailConfig;
import com.smart.framework.mail.common.MailConfigLoader;
import com.smart.framework.mail.sender.MailSender;
import com.smart.framework.mail.sender.MailSenderFactory;
import com.smart.framework.mail.receiver.MailReceiver;
import com.smart.framework.mail.receiver.MailReceiverFactory;
import com.smart.framework.mail.common.MailInfo;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * 邮件模块使用示例
 * 演示如何使用邮件发送和接收功能，包括 YAML 配置支持
 */
public class MailExample {
    private static final Logger logger = Logger.getLogger(MailExample.class.getName());

    /**
     * 邮件发送示例 - 使用 YAML 配置
     */
    public static void sendMailExampleWithYaml() {
        // 模拟从 YAML 配置中加载配置
        Map<String, Object> configMap = new HashMap<>();
        configMap.put("type", "163");
        configMap.put("host", "smtp.163.com");
        configMap.put("port", 465);
        configMap.put("username", "hrfanpig@163.com");
        configMap.put("password", "UFajGyFtBiQC7cUS");
        configMap.put("sslEnabled", true);
        configMap.put("defaultFrom", "hrfanpig@163.com");
        configMap.put("timeout", 30000);

        // 使用配置加载器加载配置
        MailConfig config = MailConfigLoader.loadFromMap(configMap);
        
        // 验证配置
        if (!MailConfigLoader.validateConfig(config)) {
            logger.severe("邮件配置验证失败");
            return;
        }

        // 创建邮件发送器
        MailSender sender = MailSenderFactory.createMailSender(config.getType(), config);

        // 发送简单邮件
        boolean success = sender.sendSimpleMail("1372302825@qq.com", "测试邮件", "这是一封测试邮件。");
        logger.info("简单邮件发送结果：" + (success ? "成功" : "失败"));

//        // 发送带抄送的邮件
//        String[] cc = {"cc1@example.com", "cc2@example.com"};
//        success = sender.sendSimpleMailWithCC("recipient@example.com", cc, "测试邮件带抄送", "这是一封带抄送的测试邮件。");
//        logger.info("带抄送邮件发送结果：" + (success ? "成功" : "失败"));
//
//        // 发送匿名邮件
//        success = sender.sendAnonymousSimpleMail("recipient@example.com", "匿名测试邮件", "这是一封匿名测试邮件。");
//        logger.info("匿名邮件发送结果：" + (success ? "成功" : "失败"));
//
//        // 发送HTML邮件
//        String htmlContent = "<h1>HTML邮件测试</h1><p>这是一封HTML格式的测试邮件。</p>";
//        success = sender.sendHtmlMail("recipient@example.com", "HTML测试邮件", htmlContent);
//        logger.info("HTML邮件发送结果：" + (success ? "成功" : "失败"));
//
//        // 发送带附件的邮件
//        success = sender.sendAttachmentMail("recipient@example.com", "带附件测试邮件", "这是一封带附件的测试邮件。", "/path/to/attachment.txt");
//        logger.info("带附件邮件发送结果：" + (success ? "成功" : "失败"));
    }

    /**
     * 邮件发送示例 - 使用默认配置
     */
    public static void sendMailExampleWithDefault() {
        // 使用默认配置
        MailConfig config = MailConfigLoader.createDefaultConfig("163");
        config.setUsername("your_username@163.com");
        config.setPassword("your_password");
        config.setDefaultFrom("your_username@163.com");

        // 创建邮件发送器
        MailSender sender = MailSenderFactory.createMailSender(config.getType(), config);

        // 发送简单邮件
        boolean success = sender.sendSimpleMail("recipient@example.com", "测试邮件", "这是一封测试邮件。");
        logger.info("简单邮件发送结果：" + (success ? "成功" : "失败"));
    }

    /**
     * 邮件接收示例 - 使用 YAML 配置
     */
    public static void receiveMailExampleWithYaml() {
        // 模拟从 YAML 配置中加载配置
        Map<String, Object> configMap = new HashMap<>();
        configMap.put("type", "163");
        configMap.put("host", "imap.163.com");
        configMap.put("port", 993);
        configMap.put("username", "your_username@163.com");
        configMap.put("password", "your_password");
        configMap.put("sslEnabled", true);
        configMap.put("timeout", 30000);

        // 使用配置加载器加载配置
        MailConfig config = MailConfigLoader.loadFromMap(configMap);
        
        // 验证配置
        if (!MailConfigLoader.validateConfig(config)) {
            logger.severe("邮件配置验证失败");
            return;
        }

        // 创建邮件接收器
        MailReceiver receiver = MailReceiverFactory.createMailReceiver(config.getType(), config);

        // 获取最新邮件列表
        List<MailInfo> latestMails = receiver.getLatestMails(10);
        if (latestMails != null && !latestMails.isEmpty()) {
            logger.info("获取到最新邮件数量：" + latestMails.size());
            
            // 获取第一封邮件的详情
            MailInfo firstMail = latestMails.get(0);
            MailInfo mailDetail = receiver.getMailDetail(firstMail.getId());
            if (mailDetail != null) {
                logger.info("邮件详情 - 主题：" + mailDetail.getSubject());
                logger.info("邮件详情 - 发件人：" + mailDetail.getFrom());
                logger.info("邮件详情 - 内容：" + mailDetail.getContent());
                
                // 如果有附件，下载第一个附件
                if (mailDetail.getAttachmentNames() != null && !mailDetail.getAttachmentNames().isEmpty()) {
                    boolean downloaded = receiver.downloadAttachment(firstMail.getId(), 0, "/tmp/downloaded_attachment");
                    logger.info("附件下载结果：" + (downloaded ? "成功" : "失败"));
                }
            }
        }

        // 标记邮件为已读
        if (latestMails != null && !latestMails.isEmpty()) {
            boolean marked = receiver.markAsRead(latestMails.get(0).getId());
            logger.info("标记已读结果：" + (marked ? "成功" : "失败"));
        }
    }

    /**
     * YAML 配置解析示例
     */
    public static void yamlConfigExample() {
        // 模拟 YAML 配置字符串
        String yamlConfig = """
            mail:
              type: '163'
              host: 'smtp.163.com'
              port: 465
              username: 'your_username@163.com'
              password: 'your_password'
              sslEnabled: true
              defaultFrom: 'your_username@163.com'
              timeout: 30000
            
            qq-mail:
              type: 'qq'
              host: 'smtp.qq.com'
              port: 465
              username: 'your_username@qq.com'
              password: 'your_password'
              sslEnabled: true
              defaultFrom: 'your_username@qq.com'
              timeout: 30000
            """;

        try {
            // 解析 mail 配置
            MailConfig mailConfig = MailConfigLoader.loadFromYaml(yamlConfig, "mail");
            logger.info("解析的 mail 配置：" + mailConfig.toString());

            // 解析 qq-mail 配置
            MailConfig qqConfig = MailConfigLoader.loadFromYaml(yamlConfig, "qq-mail");
            logger.info("解析的 qq-mail 配置：" + qqConfig.toString());

        } catch (Exception e) {
            logger.severe("YAML 配置解析失败：" + e.getMessage());
        }
    }

    /**
     * 主方法，运行示例
     */
    public static void main(String[] args) {
        logger.info("开始邮件模块示例演示");
        
        // 注意：以下示例需要真实的邮件服务器配置才能正常工作
        // 请根据实际情况修改配置参数
        
        try {
            logger.info("=== YAML 配置解析示例 ===");
            yamlConfigExample();
            
            logger.info("=== 邮件发送示例（YAML配置） ===");
            sendMailExampleWithYaml();
            
            logger.info("=== 邮件发送示例（默认配置） ===");
            sendMailExampleWithDefault();
            
            logger.info("=== 邮件接收示例（YAML配置） ===");
            receiveMailExampleWithYaml();
            
            logger.info("邮件模块示例演示完成");
        } catch (Exception e) {
            logger.severe("示例运行出错：" + e.getMessage());
        }
    }
}
