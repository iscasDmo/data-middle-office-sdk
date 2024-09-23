package cn.ac.iscas.dmo.connector.conf;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author zhuquanwen
 * @version 1.0
 * @date 2024/09/23
 * @since jdk1.8
 */
public class ConnectionUrl {

    private static final String REGEX = "jdbc:dmo://.+:(\\d|[1-9]\\d{1,3}|[1-5]\\d{4}|6[0-4]\\d{3}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5])";
    private static final Pattern COMPILE = Pattern.compile(REGEX);

    public static boolean acceptsUrl(String connString) {
        // jdbc:dmo://192.168.50.49:3282
        if (connString.contains("?")) {
            connString = connString.split("\\?")[0];
        }
        Matcher matcher = COMPILE.matcher(connString);
        return matcher.matches();
    }
}
