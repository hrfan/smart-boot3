package com.smart.framework.database.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * URL工具类
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public class URLUtil {

    /**
     * UTF-8编码
     */
    private static final String UTF8 = StandardCharsets.UTF_8.name();

    /**
     * URL解码（UTF-8）
     * 
     * @param str 需要解码的字符串
     * @return 解码后的字符串
     */
    public static String decodeByUTF8(String str) {
        if (str == null) {
            return null;
        }
        try {
            return URLDecoder.decode(str, UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("URL解码失败", e);
        }
    }

    /**
     * URL编码（UTF-8）
     * 
     * @param str 需要编码的字符串
     * @return 编码后的字符串
     */
    public static String encodeByUTF8(String str) {
        if (str == null) {
            return null;
        }
        try {
            return URLEncoder.encode(str, UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("URL编码失败", e);
        }
    }

    /**
     * URL解码
     * 
     * @param str 需要解码的字符串
     * @param charset 字符集
     * @return 解码后的字符串
     */
    public static String decode(String str, String charset) {
        if (str == null) {
            return null;
        }
        try {
            return URLDecoder.decode(str, charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("URL解码失败", e);
        }
    }

    /**
     * URL编码
     * 
     * @param str 需要编码的字符串
     * @param charset 字符集
     * @return 编码后的字符串
     */
    public static String encode(String str, String charset) {
        if (str == null) {
            return null;
        }
        try {
            return URLEncoder.encode(str, charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("URL编码失败", e);
        }
    }
}