package com.smart.framework.core.util;

import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public class StringUtil {
    
    private static final Logger log = LoggerFactory.getLogger(StringUtil.class);

    /**
     * 日期时间格式化器
     */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 邮箱正则表达式
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    /**
     * 手机号正则表达式
     */
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    /**
     * 判断字符串是否为空
     * 
     * @param str 字符串
     * @return 是否为空
     */
    public static boolean isEmpty(String str) {
        return StrUtil.isEmpty(str);
    }

    /**
     * 判断字符串是否不为空
     * 
     * @param str 字符串
     * @return 是否不为空
     */
    public static boolean isNotEmpty(String str) {
        return StrUtil.isNotEmpty(str);
    }

    /**
     * 判断字符串是否为空白
     * 
     * @param str 字符串
     * @return 是否为空白
     */
    public static boolean isBlank(String str) {
        return StrUtil.isBlank(str);
    }

    /**
     * 判断字符串是否不为空白
     * 
     * @param str 字符串
     * @return 是否不为空白
     */
    public static boolean isNotBlank(String str) {
        return StrUtil.isNotBlank(str);
    }

    /**
     * 去除字符串两端空白
     * 
     * @param str 字符串
     * @return 去除空白后的字符串
     */
    public static String trim(String str) {
        return StrUtil.trim(str);
    }

    /**
     * 去除字符串所有空白
     * 
     * @param str 字符串
     * @return 去除空白后的字符串
     */
    public static String trimAll(String str) {
        if (isBlank(str)) {
            return str;
        }
        return str.replaceAll("\\s+", "");
    }

    /**
     * 字符串转小写
     * 
     * @param str 字符串
     * @return 小写字符串
     */
    public static String toLowerCase(String str) {
        return StrUtil.lowerFirst(str);
    }

    /**
     * 字符串转大写
     * 
     * @param str 字符串
     * @return 大写字符串
     */
    public static String toUpperCase(String str) {
        return StrUtil.upperFirst(str);
    }

    /**
     * 首字母转小写
     * 
     * @param str 字符串
     * @return 首字母小写字符串
     */
    public static String lowerFirst(String str) {
        return StrUtil.lowerFirst(str);
    }

    /**
     * 首字母转大写
     * 
     * @param str 字符串
     * @return 首字母大写字符串
     */
    public static String upperFirst(String str) {
        return StrUtil.upperFirst(str);
    }

    /**
     * 驼峰转下划线
     * 
     * @param str 驼峰字符串
     * @return 下划线字符串
     */
    public static String camelToUnderline(String str) {
        return StrUtil.toUnderlineCase(str);
    }

    /**
     * 下划线转驼峰
     * 
     * @param str 下划线字符串
     * @return 驼峰字符串
     */
    public static String underlineToCamel(String str) {
        return StrUtil.toCamelCase(str);
    }

    /**
     * 验证邮箱格式
     * 
     * @param email 邮箱地址
     * @return 是否有效
     */
    public static boolean isValidEmail(String email) {
        if (isBlank(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * 验证手机号格式
     * 
     * @param phone 手机号
     * @return 是否有效
     */
    public static boolean isValidPhone(String phone) {
        if (isBlank(phone)) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * 格式化当前时间
     * 
     * @return 格式化后的时间字符串
     */
    public static String formatNow() {
        return LocalDateTime.now().format(DATE_TIME_FORMATTER);
    }

    /**
     * 格式化时间
     * 
     * @param dateTime 时间
     * @return 格式化后的时间字符串
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    /**
     * 隐藏手机号中间4位
     * 
     * @param phone 手机号
     * @return 隐藏后的手机号
     */
    public static String hidePhone(String phone) {
        if (isBlank(phone) || phone.length() != 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }

    /**
     * 隐藏邮箱中间部分
     * 
     * @param email 邮箱
     * @return 隐藏后的邮箱
     */
    public static String hideEmail(String email) {
        if (isBlank(email) || !email.contains("@")) {
            return email;
        }
        String[] parts = email.split("@");
        if (parts[0].length() <= 2) {
            return email;
        }
        String username = parts[0];
        String domain = parts[1];
        return username.substring(0, 2) + "***" + "@" + domain;
    }

    /**
     * 截取字符串
     * 
     * @param str 字符串
     * @param maxLength 最大长度
     * @return 截取后的字符串
     */
    public static String truncate(String str, int maxLength) {
        if (isBlank(str) || str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength) + "...";
    }


    /**
     * 判断地址是否为http 或 https 协议
     *
     * @param link 地址
     * @return 是否为http 或 https 协议
     */
    public static boolean isHttp(String link) {
        if (StrUtil.isBlank(link)) {
            return false;
        }
        return link.startsWith("http://") || link.startsWith("https://");
    }
}
