package com.smart.system.controller;

import com.smart.common.core.result.Result;
import com.smart.common.database.bulk.SmartJdbcBulkInsert;
import com.smart.common.mail.common.MailConfig;
import com.smart.common.mail.common.MailConfigLoader;
import com.smart.common.mail.common.AttachmentData;
import com.smart.common.mail.sender.MailSender;
import com.smart.common.mail.sender.MailSenderFactory;
import com.smart.common.redis.service.DistributedLock;
import com.smart.common.redis.service.DistributedLockService;
import com.smart.common.redis.service.RedisService;
import com.smart.system.role.entiy.SmartRole;
import com.smart.system.role.service.SmartRoleService;
import com.smart.system.user.entity.SmartUser;
import com.smart.system.user.service.SmartUserService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 测试控制器
 * 用于测试系统模块的异常处理功能
 *
 * @author smart-boot3
 * @since 1.0.0
 */
@RestController
@RequestMapping("/test")
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);


    @Autowired
    private DistributedLockService lockService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private SmartRoleService smartRoleService;


    @Resource
    private SmartJdbcBulkInsert smartJdbcBulkInsert;

    @Resource
    private SmartUserService smartUserService;

    /**
     * 测试正常响应
     *
     * @return 成功消息
     */
    @GetMapping("/r1")
    public Result testSuccess() {
        String lockKey = "HRFAN";
        Map<String, Object> result = new HashMap<>();
        DistributedLock lock = null;

        try {
            log.info("{} ,尝试获取锁: {}", Thread.currentThread().getName(), lockKey);
            lock = lockService.acquireLock(lockKey, 10, TimeUnit.SECONDS);

            if (lock.isValid()) {
                result.put("status", "锁获取成功");
                result.put("lockKey", lock.getLockKey());
                result.put("lockValue", lock.getLockValue());
                result.put("expireTime", lock.getExpireTime());
                result.put("ttl", lock.getRemainingTtl());

                log.info("业务逻辑执行中... 模拟持有锁3秒");
                Thread.sleep(10000);
                // 模拟业务逻辑执行完成，释放锁
                lock.unlock();

            } else {
                result.put("status", "锁获取失败");
                result.put("lockKey", lock.getLockKey());
                result.put("lockValue", lock.getLockValue());
                result.put("message", "未能获取到有效锁，可能已被其他线程持有或Redis异常");
            }
        } catch (Exception e) {
            log.error("测试基本锁功能时发生异常: {}", e.getMessage(), e);
            return Result.error("测试基本锁功能失败: " + e.getMessage());
        } finally {
            if (lock != null) {
                boolean unlocked = lock.unlock();
                result.put("unlocked", unlocked);
                log.info("锁释放结果: {}", unlocked);
            }
        }

        return Result.success("基本锁测试完成", result);
    }

    @GetMapping("/r2")
    public Result testSuccess2() {
        String lockKey = "HRFAN";
        Map<String, Object> result = new HashMap<>();

        try {
            log.info("尝试使用tryLock获取锁: {}", lockKey);
            boolean success = lockService.tryLock(lockKey, 10, TimeUnit.SECONDS);

            result.put("tryLockResult", success);
            result.put("lockKey", lockKey);

            if (success) {
                result.put("status", "tryLock成功");
                log.info("tryLock成功，模拟业务逻辑执行3秒...");
                Thread.sleep(10000);

                // 释放锁
                DistributedLock lock = lockService.acquireLock(lockKey, 10, TimeUnit.SECONDS);
                if (lock.isValid()) {
                    boolean unlocked = lock.unlock();
                    result.put("unlocked", unlocked);
                }
            } else {
                result.put("status", "tryLock失败");
                result.put("message", "锁可能已被其他线程持有");
            }
        } catch (Exception e) {
            log.error("测试tryLock功能时发生异常: {}", e.getMessage(), e);
            return Result.error("测试tryLock功能失败: " + e.getMessage());
        }

        return Result.success("tryLock测试完成", result);
    }




    @GetMapping("/r4")
    public Result testR4() {



        List<SmartRole> roleList =  new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            SmartRole smartRole = new SmartRole();
            smartRole.setId(UUID.randomUUID().toString());
            smartRole.setRoleName("ROLE_ADMIN_" + i);
            smartRole.setRoleKey("ROLE_ADMIN");
            smartRole.setRoleSort(i);
            smartRole.setRemark("测试");
            roleList.add(smartRole);
        }
        try {
            // 指定数据库对应模式 ?currentSchema=smart_system
            log.info("开始执行快速入库");

            int i = smartJdbcBulkInsert.fastImport(roleList);

            if (i > 0) {
                return Result.success("添加成功");
            }
        } catch (Exception e) {
            log.error("测试快速入库功能时发生异常: {}", e.getMessage(), e);
            throw new RuntimeException("测试快速入库功能失败: " + e.getMessage(), e);
        }
        return Result.error("添加失败");
    }

    @GetMapping("/r6")
    public Result testR6() throws UnsupportedEncodingException {
        Result.success("测试成功");
        // 模拟从 YAML 配置中加载配置
        Map<String, Object> configMap = new HashMap<>();
        configMap.put("type", "163");
        configMap.put("host", "smtp.163.com");
        configMap.put("port", 994);
        configMap.put("username", "hrfanpig@163.com");
        configMap.put("password", "UFajGyFtBiQC7cUS");
        configMap.put("sslEnabled", true);
        configMap.put("defaultFrom", "hrfanpig@163.com");
        configMap.put("timeout", 30000);

        // 使用配置加载器加载配置
        MailConfig config = MailConfigLoader.loadFromMap(configMap);

        // 验证配置
        if (!MailConfigLoader.validateConfig(config)) {
            log.error("邮件配置验证失败");
            return Result.error("邮件配置验证失败");
        }

        // 创建邮件发送器
        MailSender sender = MailSenderFactory.createMailSender(config.getType(), config);


        String rce = "1372302825@qq.com";
        // 发送简单邮件
        log.info("开始发送邮件测试...");
        boolean success = sender.sendSimpleMail(rce, "测试是否正常轰炸邮件", "这是一封测试是否正常轰炸邮件。");
        log.info("简单邮件发送结果：" + (success ? "成功" : "失败"));


        // 发送带抄送的邮件
        String[] cc = {rce, "207259515@qq.com"};
        success = sender.sendSimpleMailWithCC(rce, cc, "测试是否正常轰炸邮件带抄送", "这是一封带抄送的测试是否正常轰炸邮件。");
        log.info("带抄送邮件发送结果：" + (success ? "成功" : "失败"));

        // 发送匿名邮件
        success = sender.sendAnonymousSimpleMail(rce, "匿名测试是否正常轰炸邮件", "这是一封匿名测试是否正常轰炸邮件。");
        log.info("匿名邮件发送结果：" + (success ? "成功" : "失败"));

        // 发送HTML邮件
        String htmlContent = "<h1>HTML邮件测试是否正常轰炸</h1><p>这是一封HTML格式的测试是否正常轰炸邮件。</p>";
        success = sender.sendHtmlMail(rce, "HTML测试是否正常轰炸邮件", htmlContent);
        log.info("HTML邮件发送结果：" + (success ? "成功" : "失败"));

        // 发送带附件的邮件
        success = sender.sendAttachmentMail(rce, "带附件测试是否正常轰炸邮件", "这是一封带附件的测试是否正常轰炸邮件。", "/Users/fanhaoran/代码/开源框架/smart-common-mail_副本/pom.xml");
        log.info("带附件邮件发送结果：" + (success ? "成功" : "失败"));

        // 测试byte流附件发送
        log.info("开始测试byte流附件发送...");
        
        // 创建测试文本文件的byte流
        String testContent = "这是一个测试文件的内容\n包含中文和英文\nTest file content with Chinese and English";
        byte[] testBytes = testContent.getBytes("UTF-8");
        AttachmentData textAttachment = new AttachmentData("test.txt", testBytes, "text/plain; charset=UTF-8");
        
        // 发送带byte流附件的邮件
        success = sender.sendByteAttachmentMail(rce, "byte流附件测试邮件", "这是一封带byte流附件的测试邮件。", textAttachment);
        log.info("byte流附件邮件发送结果：" + (success ? "成功" : "失败"));
        
        // 创建测试HTML文件的byte流
        String htmlContentByte = "<html><body><h1>测试HTML文件</h1><p>这是一个HTML格式的测试文件。</p></body></html>";
        byte[] htmlBytes = htmlContentByte.getBytes("UTF-8");
        AttachmentData htmlAttachment = new AttachmentData("test.html", htmlBytes, "text/html; charset=UTF-8");
        
        // 发送HTML格式带byte流附件的邮件
        success = sender.sendHtmlByteAttachmentMail(rce, "HTML格式byte流附件测试邮件", "<h2>HTML邮件内容</h2><p>这是一封HTML格式的邮件，带有byte流附件。</p>", htmlAttachment);
        log.info("HTML格式byte流附件邮件发送结果：" + (success ? "成功" : "失败"));
        
        // 测试多个byte流附件
        AttachmentData[] multipleAttachments = {
            new AttachmentData("file1.txt", "第一个文件内容".getBytes("UTF-8"), "text/plain; charset=UTF-8"),
            new AttachmentData("file2.txt", "第二个文件内容".getBytes("UTF-8"), "text/plain; charset=UTF-8")
        };
        
        success = sender.sendByteAttachmentMail(rce, "多个byte流附件测试邮件", "这是一封带有多个byte流附件的测试邮件。", multipleAttachments);
        log.info("多个byte流附件邮件发送结果：" + (success ? "成功" : "失败"));
        
        if (success) {
            return Result.success("邮件发送测试是否正常轰炸成功！包括byte流附件功能测试完成！");
        } else {
            return Result.error("邮件发送测试是否正常轰炸失败！");
        }
    }

    @GetMapping("/r5")
    public Result<List<SmartUser>> getUserList() {
        log.info("获取用户列表");
        List<SmartUser> users = smartUserService.list();
        return Result.success("获取用户列表成功",users);
    }

    @GetMapping("/redisBase")
    public Result redisBase() {
        String orderId = "order:lock:" + "123";
        // 测试redis基础功能
        redisService.set("order:lock:redisBase", orderId, 1, TimeUnit.MINUTES);
        // 获取redis中的值
        String redisOrderId = redisService.get("order:lock:redisBase", String.class);
        log.info("redis中orderId: {}", redisOrderId);

        return Result.success("获取 Redis 中的 orderId: " + redisOrderId);
    }




    @PostMapping("/testParams")
    public Result testParams(@RequestBody  @Valid SmartRole smartRole) {
        log.info("当前角色信息:{}", smartRole);
        return Result.success("参数校验成功", smartRole);
    }

    /**
     * 测试认证异常
     *
     * @return 抛出认证异常
     */
    @GetMapping("/auth-error")
    public String testAuthError() {
        throw new AuthenticationException("认证失败") {};
    }

    /**
     * 测试访问拒绝异常
     *
     * @return 抛出访问拒绝异常
     */
    @GetMapping("/access-denied")
    public String testAccessDenied() {
        throw new AccessDeniedException("访问被拒绝");
    }

    /**
     * 测试通用异常
     *
     * @return 抛出通用异常
     */
    @GetMapping("/generic-error")
    public String testGenericError() {
        throw new RuntimeException("系统模块通用异常");
    }
}
