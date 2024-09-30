package cn.ac.iscas.dmo.connector.conf;

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
    private final String token;
    private final Map<String, String> hostProperties = new HashMap<>();
    private final String datasourceType;
    private final String datasourceName;
    private final String url;

    public HostInfo(String url, String host, int port, String datasourceType, String datasourceName,
                    String token, Properties properties) {
        this.url = url;
        this.host = host;
        this.port = port;
        this.datasourceType = datasourceType;
        this.token = token;
        this.datasourceName = datasourceName;
        if (Objects.nonNull(properties)) {
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                this.hostProperties.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
            }
        }
    }

    public static HostInfo getInstance(String url, Properties info) {
        String props = null;
        String oriUrl = url;
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
        String datasourceType = (String) info.get("datasourceType");
        String token = (String) info.get("Authorization");
        String datasourceName = (String) info.get("datasourceName");
        String[] strs = url.split(":");
        String host = strs[2].substring(strs[2].indexOf("//") + 2);
        int port = Integer.parseInt(strs[3].split("/")[0]);
        return new HostInfo(oriUrl, host, port, datasourceType, datasourceName, token, info);
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }


    public String getToken() {
        return token;
    }

    public Map<String, String> getHostProperties() {
        return hostProperties;
    }

    public String getDatasourceType() {
        return datasourceType;
    }

    public String getDatasourceName() {
        return datasourceName;
    }

    public String getUrl() {
        return url;
    }
}
