package com.smart.common.database.util;

import com.smart.common.database.query.CommonConstant;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * 通用工具类
 *
 * @author Smart Boot3
 * @since 1.0.0
 */
public class CommonUtils {

    /**
     * 驼峰转下划线
     *
     * @param camelCase 驼峰字符串
     * @return 下划线字符串
     */
    public static String camel2UnderScore(String camelCase) {
        if (StringUtils.isEmpty(camelCase)) {
            return camelCase;
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < camelCase.length(); i++) {
            char ch = camelCase.charAt(i);
            if (Character.isUpperCase(ch)) {
                // 如果不是第一个字符，添加下划线
                if (i > 0) {
                    result.append('_');
                }
                result.append(Character.toLowerCase(ch));
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    /**
     * 下划线转驼峰
     *
     * @param underScore 下划线字符串
     * @return 驼峰字符串
     */
    public static String underScore2Camel(String underScore) {
        if (StringUtils.isEmpty(underScore)) {
            return underScore;
        }

        StringBuilder result = new StringBuilder();
        boolean nextUpperCase = false;

        for (int i = 0; i < underScore.length(); i++) {
            char ch = underScore.charAt(i);
            if (ch == '_') {
                nextUpperCase = true;
            } else {
                if (nextUpperCase) {
                    result.append(Character.toUpperCase(ch));
                    nextUpperCase = false;
                } else {
                    result.append(ch);
                }
            }
        }
        return result.toString();
    }

    /**
     * SQL注入过滤
     *
     * @param str 需要过滤的字符串
     * @throws IllegalArgumentException 如果检测到SQL注入攻击
     */
    public static void filterSqlInject(String str) {
        if (StringUtils.isEmpty(str)) {
            return;
        }

        // 转换为小写进行检查
        String lowerStr = str.toLowerCase().trim();

        // 检查SQL注入关键字
        if (Pattern.matches(CommonConstant.SQL_INJECTION_REGEX, lowerStr)) {
            throw new IllegalArgumentException("检测到SQL注入攻击: " + str);
        }

        // 检查常见的SQL注入模式
        String[] dangerousPatterns = {
                "'", "\"", ";", "--", "/*", "*/", "xp_", "sp_",
                "select", "insert", "update", "delete", "drop", "create",
                "alter", "exec", "execute", "union", "declare"
        };

        for (String pattern : dangerousPatterns) {
            if (lowerStr.contains(pattern)) {
                throw new IllegalArgumentException("检测到潜在的SQL注入攻击: " + str);
            }
        }
    }

    /**
     * 检查字符串是否为空或null
     *
     * @param str 字符串
     * @return 是否为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 检查字符串是否不为空
     *
     * @param str 字符串
     * @return 是否不为空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 安全的字符串转换
     *
     * @param obj 对象
     * @return 字符串
     */
    public static String safeToString(Object obj) {
        return obj == null ? "" : obj.toString();
    }
}