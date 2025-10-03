package com.smart.framework.mail.common;

import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * 邮件配置加载器，用于从 YAML 配置中加载邮件配置
 */
public class MailConfigLoader {
    private static final Logger logger = Logger.getLogger(MailConfigLoader.class.getName());

    /**
     * 从配置 Map 中加载邮件配置
     * @param configMap 配置 Map
     * @return 邮件配置对象
     */
    public static MailConfig loadFromMap(Map<String, Object> configMap) {
        MailConfig config = new MailConfig();
        
        try {
            // 加载基本配置
            if (configMap.containsKey("type")) {
                config.setType((String) configMap.get("type"));
            }
            if (configMap.containsKey("host")) {
                config.setHost((String) configMap.get("host"));
            }
            if (configMap.containsKey("port")) {
                Object portObj = configMap.get("port");
                if (portObj instanceof Integer) {
                    config.setPort((Integer) portObj);
                } else if (portObj instanceof String) {
                    config.setPort(Integer.parseInt((String) portObj));
                }
            }
            if (configMap.containsKey("username")) {
                config.setUsername((String) configMap.get("username"));
            }
            if (configMap.containsKey("password")) {
                config.setPassword((String) configMap.get("password"));
            }
            if (configMap.containsKey("sslEnabled")) {
                Object sslObj = configMap.get("sslEnabled");
                if (sslObj instanceof Boolean) {
                    config.setSslEnabled((Boolean) sslObj);
                } else if (sslObj instanceof String) {
                    config.setSslEnabled(Boolean.parseBoolean((String) sslObj));
                }
            }
            if (configMap.containsKey("defaultFrom")) {
                config.setDefaultFrom((String) configMap.get("defaultFrom"));
            }
            if (configMap.containsKey("timeout")) {
                Object timeoutObj = configMap.get("timeout");
                if (timeoutObj instanceof Integer) {
                    config.setTimeout((Integer) timeoutObj);
                } else if (timeoutObj instanceof String) {
                    config.setTimeout(Integer.parseInt((String) timeoutObj));
                }
            }
            
            logger.info("成功加载邮件配置：" + config.toString());
            return config;
        } catch (Exception e) {
            logger.severe("加载邮件配置失败：" + e.getMessage());
            throw new RuntimeException("邮件配置加载失败", e);
        }
    }

    /**
     * 创建默认的邮件配置
     * @param type 邮件类型
     * @return 默认配置
     */
    public static MailConfig createDefaultConfig(String type) {
        MailConfig config = new MailConfig();
        config.setType(type);
        
        switch (type.toLowerCase()) {
            case "163":
                config.setHost("smtp.163.com");
                config.setPort(465);
                config.setSslEnabled(true);
                config.setTimeout(30000);
                break;
            case "qq":
                config.setHost("smtp.qq.com");
                config.setPort(465);
                config.setSslEnabled(true);
                config.setTimeout(30000);
                break;
            default:
                logger.warning("未知的邮件类型：" + type + "，使用默认配置");
                config.setHost("smtp.example.com");
                config.setPort(587);
                config.setSslEnabled(false);
                config.setTimeout(30000);
                break;
        }
        
        return config;
    }

    /**
     * 验证邮件配置是否完整
     * @param config 邮件配置
     * @return 是否有效
     */
    public static boolean validateConfig(MailConfig config) {
        if (config == null) {
            logger.warning("邮件配置为空");
            return false;
        }
        
        if (config.getHost() == null || config.getHost().trim().isEmpty()) {
            logger.warning("邮件服务器地址不能为空");
            return false;
        }
        
        if (config.getPort() <= 0 || config.getPort() > 65535) {
            logger.warning("邮件服务器端口无效：" + config.getPort());
            return false;
        }
        
        if (config.getUsername() == null || config.getUsername().trim().isEmpty()) {
            logger.warning("邮件用户名不能为空");
            return false;
        }
        
        if (config.getPassword() == null || config.getPassword().trim().isEmpty()) {
            logger.warning("邮件密码不能为空");
            return false;
        }
        
        logger.info("邮件配置验证通过");
        return true;
    }

    /**
     * 从 YAML 配置字符串中解析配置
     * 这是一个简化的实现，实际项目中可以使用 SnakeYAML 等库
     * @param yamlConfig YAML 配置字符串
     * @param configKey 配置键（如 "mail", "qq-mail", "netease-mail"）
     * @return 邮件配置对象
     */
    public static MailConfig loadFromYaml(String yamlConfig, String configKey) {
        // 这里是一个简化的实现
        // 在实际项目中，建议使用 SnakeYAML 库来解析 YAML
        Map<String, Object> configMap = parseSimpleYaml(yamlConfig, configKey);
        return loadFromMap(configMap);
    }

    /**
     * 简化的 YAML 解析器（仅用于演示）
     * 实际项目中应使用专业的 YAML 解析库
     * @param yamlConfig YAML 配置字符串
     * @param configKey 配置键
     * @return 配置 Map
     */
    private static Map<String, Object> parseSimpleYaml(String yamlConfig, String configKey) {
        Map<String, Object> configMap = new HashMap<>();
        
        // 这是一个非常简化的实现，仅用于演示
        // 实际项目中应使用 SnakeYAML 等专业库
        String[] lines = yamlConfig.split("\n");
        boolean inTargetSection = false;
        
        for (String line : lines) {
            line = line.trim();
            
            if (line.startsWith(configKey + ":")) {
                inTargetSection = true;
                continue;
            }
            
            if (inTargetSection) {
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                if (line.contains(":") && !line.startsWith(" ")) {
                    // 遇到新的顶级配置项，退出当前配置段
                    break;
                }
                
                if (line.contains(":")) {
                    String[] parts = line.split(":", 2);
                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String value = parts[1].trim().replaceAll("^['\"]|['\"]$", "");
                        
                        // 简单的类型转换
                        if ("port".equals(key) || "timeout".equals(key)) {
                            try {
                                configMap.put(key, Integer.parseInt(value));
                            } catch (NumberFormatException e) {
                                configMap.put(key, value);
                            }
                        } else if ("sslEnabled".equals(key)) {
                            configMap.put(key, Boolean.parseBoolean(value));
                        } else {
                            configMap.put(key, value);
                        }
                    }
                }
            }
        }
        
        return configMap;
    }
}
