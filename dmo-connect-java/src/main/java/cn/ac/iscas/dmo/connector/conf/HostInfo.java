package cn.ac.iscas.dmo.connector.conf;

import cn.ac.iscas.dmo.connector.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * 连接信息的处理类
 *
 * @author zhuquanwen
 * @version 1.0
 * @date 2024/9/23 13:34
 */

public class HostInfo {
    private static final String HOST_PORT_SEPARATOR = ":";

    private final String host;
    private final int port;
    private final String sqlServiceUrl;
    private final String token;
    private final Map<String, String> hostProperties = new HashMap<>();
    private final String useSsl;


    public HostInfo(String host, int port, String sqlServiceUrl, String token, String useSsl, Properties properties) {
        this.host = host;
        this.port = port;
        this.sqlServiceUrl = sqlServiceUrl;
        this.token = token;
        this.useSsl = useSsl;
        if (Objects.nonNull(properties)) {
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                this.hostProperties.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
            }
        }
    }

    public static HostInfo getInstance(String url, Properties info) {
        String props = null;
        if (url.contains("?")) {
            String tmpUrl = url;
            url = tmpUrl.substring(0, tmpUrl.indexOf("?"));
            props = tmpUrl.substring(tmpUrl.indexOf("?") + 1);
        }
        if (Objects.nonNull(props)) {
            String[] splits = props.split("&");
            for (String split : splits) {
                if (Objects.nonNull(split) && !split.isEmpty()) {
                    if (split.contains("=")) {
                        String key = split.substring(0, split.indexOf("="));
                        String value = split.substring(split.indexOf("=") + 1);
                        info.put(key, value);
                    }
                }
            }
        }
        String sqlServiceUrl = (String) info.get("sqlServiceUrl");
        String token = (String) info.get("Authorization");
        String useSsl = (String) info.get("useSsl");
        useSsl = Objects.isNull(useSsl) ? "false" : useSsl;
        String[] strs = url.split(":");
        String host = strs[2].substring(strs[2].indexOf("//") + 2);
        int port = Integer.parseInt(strs[3].split("/")[0]);
        return new HostInfo(host, port, sqlServiceUrl, token, useSsl, info);
    }


    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getSqlServiceUrl() {
        return sqlServiceUrl;
    }

    public String getToken() {
        return token;
    }

    public Map<String, String> getHostProperties() {
        return hostProperties;
    }

    public String getUseSsl() {
        return useSsl;
    }
}
