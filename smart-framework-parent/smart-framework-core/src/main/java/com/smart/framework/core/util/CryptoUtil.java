package com.smart.framework.core.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.symmetric.AES;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * 加密工具类
 * 
 * @author Smart Boot3
 * @since 1.0.0
 */
public class CryptoUtil {

    /**
     * AES密钥长度
     */
    private static final int AES_KEY_LENGTH = 16;

    /**
     * 默认AES密钥
     */
    private static final String DEFAULT_AES_KEY = "smart-boot3-key";

    /**
     * 生成UUID
     * 
     * @return UUID字符串
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * MD5加密
     * 
     * @param data 待加密数据
     * @return 加密后的字符串
     */
    public static String md5(String data) {
        if (StrUtil.isBlank(data)) {
            return null;
        }
        return DigestUtil.md5Hex(data);
    }

    /**
     * SHA256加密
     * 
     * @param data 待加密数据
     * @return 加密后的字符串
     */
    public static String sha256(String data) {
        if (StrUtil.isBlank(data)) {
            return null;
        }
        return DigestUtil.sha256Hex(data);
    }

    /**
     * AES加密
     * 
     * @param data 待加密数据
     * @return 加密后的字符串
     */
    public static String aesEncrypt(String data) {
        return aesEncrypt(data, DEFAULT_AES_KEY);
    }

    /**
     * AES加密
     * 
     * @param data 待加密数据
     * @param key 密钥
     * @return 加密后的字符串
     */
    public static String aesEncrypt(String data, String key) {
        if (StrUtil.isBlank(data) || StrUtil.isBlank(key)) {
            return null;
        }
        try {
            AES aes = SecureUtil.aes(key.getBytes(StandardCharsets.UTF_8));
            return aes.encryptHex(data);
        } catch (Exception e) {
            System.err.println("AES加密失败: " + e.getMessage());
            return null;
        }
    }

    /**
     * AES解密
     * 
     * @param encryptedData 加密数据
     * @return 解密后的字符串
     */
    public static String aesDecrypt(String encryptedData) {
        return aesDecrypt(encryptedData, DEFAULT_AES_KEY);
    }

    /**
     * AES解密
     * 
     * @param encryptedData 加密数据
     * @param key 密钥
     * @return 解密后的字符串
     */
    public static String aesDecrypt(String encryptedData, String key) {
        if (StrUtil.isBlank(encryptedData) || StrUtil.isBlank(key)) {
            return null;
        }
        try {
            AES aes = SecureUtil.aes(key.getBytes(StandardCharsets.UTF_8));
            return aes.decryptStr(encryptedData);
        } catch (Exception e) {
            System.err.println("AES解密失败: " + e.getMessage());
            return null;
        }
    }

    /**
     * 生成随机密钥
     * 
     * @return 随机密钥
     */
    public static String generateRandomKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 密码加密（用于用户密码）
     * 
     * @param password 原始密码
     * @param salt 盐值
     * @return 加密后的密码
     */
    public static String encryptPassword(String password, String salt) {
        if (StrUtil.isBlank(password) || StrUtil.isBlank(salt)) {
            return null;
        }
        return sha256(password + salt);
    }

    /**
     * 验证密码
     * 
     * @param password 原始密码
     * @param salt 盐值
     * @param encryptedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean verifyPassword(String password, String salt, String encryptedPassword) {
        if (StrUtil.isBlank(password) || StrUtil.isBlank(salt) || StrUtil.isBlank(encryptedPassword)) {
            return false;
        }
        String encrypted = encryptPassword(password, salt);
        return encryptedPassword.equals(encrypted);
    }
}