package cn.ac.iscas.dmo.connector.util;

import java.sql.SQLFeatureNotSupportedException;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 异常创建工具
 *
 * @author zhuquanwen
 * @version 1.0
 * @date 2022/7/4 15:53
 * @since jdk11
 */
public class Exceptions {
    private static final Pattern PATTERN = Pattern.compile("\\{\\}");

    private Exceptions() {
    }



    public static IllegalStateException illegalStateException(String msg) {
        return new IllegalStateException(msg);
    }

    public static IllegalStateException illegalStateException(Throwable e) {
        return new IllegalStateException(e);
    }

    public static IllegalArgumentException illegalArgumentException(String msg) {
        return new IllegalArgumentException(msg);
    }

    public static RuntimeException runtimeException(Throwable e) {
        return new RuntimeException(e);
    }

    public static RuntimeException runtimeException(String message) {
        return new RuntimeException(message);
    }

    public static RuntimeException runtimeException(String message, Throwable e) {
        return new RuntimeException(message, e);
    }

    private static String replaceFormat(String formatter, Object... formatVals) {
        Matcher matcher = PATTERN.matcher(formatter);
        int i = 0;
        while (matcher.find()) {
            formatter = matcher.replaceFirst("{" + i++ + "}");
            matcher = PATTERN.matcher(formatter);
        }
        return MessageFormat.format(formatter, formatVals);
    }

}
