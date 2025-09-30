package com.smart.common.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT工具类
 * 使用现代Java特性和Spring Security 6兼容版本
 * 
 * @author smart-boot3
 * @since 1.0.0
 */
@Component
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    /**
     * JWT密钥
     */
    @Value("${jwt.secret:smart-boot3-jwt-secret-key-512-bit-for-hs512-algorithm-security-2025-very-long-key}")
    private String secret;

    /**
     * JWT过期时间（毫秒）
     */
    @Value("${jwt.expiration:86400000}")
    private Long expiration;

    /**
     * JWT刷新过期时间（毫秒）
     */
    @Value("${jwt.refresh-expiration:604800000}")
    private Long refreshExpiration;

    /**
     * 生成JWT Token
     * 
     * @param userId 用户ID
     * @param username 用户名
     * @param roles 角色列表
     * @return JWT Token
     */
    public String generateToken(String userId, String username, String... roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("roles", roles);
        claims.put("type", "access");
        return generateToken(claims, username);
    }

    /**
     * 生成JWT Token
     * 
     * @param claims 声明
     * @param subject 主题
     * @return JWT Token
     */
    public String generateToken(Map<String, Object> claims, String subject) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryTime = now.plusSeconds(expiration / 1000);
        
        Date nowDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        Date expiryDate = Date.from(expiryTime.atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(nowDate)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * 生成刷新Token
     * 
     * @param userId 用户ID
     * @param username 用户名
     * @return 刷新Token
     */
    public String generateRefreshToken(String userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("type", "refresh");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryTime = now.plusSeconds(refreshExpiration / 1000);
        
        Date nowDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        Date expiryDate = Date.from(expiryTime.atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(nowDate)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * 从Token中获取用户名
     * 
     * @param token JWT Token
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * 从Token中获取用户ID
     * 
     * @param token JWT Token
     * @return 用户ID
     */
    public String getUserIdFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("userId", String.class));
    }

    /**
     * 从Token中获取角色列表
     * 
     * @param token JWT Token
     * @return 角色列表
     */
    @SuppressWarnings("unchecked")
    public String[] getRolesFromToken(String token) {
        return getClaimFromToken(token, claims -> {
            Object roles = claims.get("roles");
            if (roles instanceof String[]) {
                return (String[]) roles;
            }
            return new String[0];
        });
    }

    /**
     * 从Token中获取Token类型
     * 
     * @param token JWT Token
     * @return Token类型
     */
    public String getTokenType(String token) {
        return getClaimFromToken(token, claims -> claims.get("type", String.class));
    }

    /**
     * 从Token中获取过期时间
     * 
     * @param token JWT Token
     * @return 过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * 从Token中获取指定声明
     * 
     * @param token JWT Token
     * @param claimsResolver 声明解析器
     * @param <T> 返回类型
     * @return 声明值
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 从Token中获取所有声明
     * 
     * @param token JWT Token
     * @return 所有声明
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 检查Token是否过期
     * 
     * @param token JWT Token
     * @return 是否过期
     */
    public Boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (Exception e) {
            log.error("检查Token过期时间失败: {}", e.getMessage());
            return true;
        }
    }

    /**
     * 验证Token
     * 
     * @param token JWT Token
     * @param username 用户名
     * @return 是否有效
     */
    public Boolean validateToken(String token, String username) {
        try {
            final String tokenUsername = getUsernameFromToken(token);
            return (username.equals(tokenUsername) && !isTokenExpired(token));
        } catch (Exception e) {
            log.error("验证Token失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 验证Token
     * 
     * @param token JWT Token
     * @return 是否有效
     */
    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            log.error("验证Token失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 验证刷新Token
     * 
     * @param token JWT Token
     * @return 是否有效
     */
    public Boolean validateRefreshToken(String token) {
        try {
            String tokenType = getTokenType(token);
            return "refresh".equals(tokenType) && !isTokenExpired(token);
        } catch (Exception e) {
            log.error("验证刷新Token失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 生成安全的JWT密钥（用于HS512算法）
     * 
     * @return 安全的密钥字符串
     */
    public static String generateSecureKey() {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        return java.util.Base64.getEncoder().encodeToString(key.getEncoded());
    }
    
    /**
     * 生成安全的JWT密钥（用于HS512算法）- 可读格式
     * 
     * @return 安全的密钥字符串
     */
    public static String generateSecureReadableKey() {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        byte[] keyBytes = key.getEncoded();
        StringBuilder sb = new StringBuilder();
        for (byte b : keyBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * 获取签名密钥
     * 
     * @return 签名密钥
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        
        // 检查密钥长度是否足够
        if (keyBytes.length < 64) { // 64 bytes = 512 bits
            log.warn("JWT密钥长度不足512位，当前长度: {} bits，建议使用更长的密钥", keyBytes.length * 8);
            log.warn("当前密钥: {}", secret);
            log.warn("建议使用至少64个字符的密钥以确保HS512算法的安全性");
        }
        
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 从Token中提取Bearer前缀
     * 
     * @param authHeader Authorization头
     * @return Token字符串
     */
    public String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    /**
     * 检查Token是否即将过期（在指定时间内）
     * 
     * @param token JWT Token
     * @param minutes 分钟数
     * @return 是否即将过期
     */
    public Boolean isTokenExpiringSoon(String token, int minutes) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            Date threshold = new Date(System.currentTimeMillis() + minutes * 60 * 1000);
            return expiration.before(threshold);
        } catch (Exception e) {
            log.error("检查Token即将过期失败: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 根据变量名获取对应的值
     * 用于权限规则中的变量替换
     * 
     * @param variableKey 变量键名（如: sys_user_id, sys_dept_code等）
     * @return 变量值
     */
    public static String getVariableByKey(String variableKey) {
        if (variableKey == null || variableKey.trim().isEmpty()) {
            return null;
        }
        
        // 处理系统变量
        switch (variableKey) {
            case "sys_user_id":
                // TODO: 从当前登录用户上下文获取用户ID
                return getCurrentUserId();
            case "sys_user_name":
                // TODO: 从当前登录用户上下文获取用户名
                return getCurrentUsername();
            case "sys_dept_code":
                // TODO: 从当前登录用户上下文获取部门编码
                return getCurrentDeptCode();
            case "sys_org_code":
                // TODO: 从当前登录用户上下文获取组织编码
                return getCurrentOrgCode();
            default:
                log.warn("未知的系统变量: {}", variableKey);
                return variableKey; // 返回原值
        }
    }
    
    /**
     * 获取当前登录用户ID
     * 
     * @return 用户ID
     */
    private static String getCurrentUserId() {
        // TODO: 实际项目中应该从Security上下文或线程本地变量获取
        return "1"; // 默认值
    }
    
    /**
     * 获取当前登录用户名
     * 
     * @return 用户名
     */
    private static String getCurrentUsername() {
        // TODO: 实际项目中应该从Security上下文获取
        return "admin"; // 默认值
    }
    
    /**
     * 获取当前用户部门编码
     * 
     * @return 部门编码
     */
    private static String getCurrentDeptCode() {
        // TODO: 实际项目中应该从用户信息获取
        return "001"; // 默认值
    }
    
    /**
     * 获取当前用户组织编码
     * 
     * @return 组织编码
     */
    private static String getCurrentOrgCode() {
        // TODO: 实际项目中应该从用户信息获取
        return "ORG001"; // 默认值
    }
}