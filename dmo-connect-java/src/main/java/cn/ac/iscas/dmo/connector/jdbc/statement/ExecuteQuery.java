package cn.ac.iscas.dmo.connector.jdbc.statement;

import cn.ac.iscas.dmo.connector.conf.HostInfo;
import cn.ac.iscas.dmo.connector.jdbc.ConnectionImpl;
import cn.ac.iscas.dmo.connector.jdbc.ResultSetImpl;
import cn.ac.iscas.dmo.connector.util.JsonUtils;
import cn.ac.iscas.dmo.connector.util.OkHttpCustomClient;
import com.fasterxml.jackson.core.type.TypeReference;
import okhttp3.Response;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 查询处理器
 *
 * @author zhuquanwen
 * @version 1.0
 * @date 2024/9/23 14:46
 */

public class ExecuteQuery {

    public static void execute(ResultSetImpl rs, String sql) throws SQLException, IOException {
        ConnectionImpl connection = (ConnectionImpl) rs.getStatement().getConnection();
        Map<String, Object> map = doExecute(connection, sql);
        if (map.containsKey("value")) {
            List<Map<String, Object>> values = (List<Map<String, Object>>) map.get("value");
            rs.setCacheData(values);
        }
    }

    public static Map<String, Object> doExecute(ConnectionImpl connection, String sql) throws SQLException, IOException {
        String token = connection.getToken();
        HostInfo origHostInfo = connection.getOrigHostInfo();
        String sqlServiceUrl = connection.getSqlServiceUrl();
        String useSsl = connection.getUseSsl();
        OkHttpCustomClient httpClient = connection.getHttpClient();
        Map<String, String> header = new HashMap<>();
        if (Objects.nonNull(token) && !token.isEmpty()) {
            header.put("Authorization", token);
        }
        String protocol = "https://";
        if ("false".equals(useSsl)) {
            protocol = "http://";
        }
        sqlServiceUrl = protocol + origHostInfo.getHost() + ":" + origHostInfo.getPort() + sqlServiceUrl;

        Response response = httpClient.doPostWithRes(sqlServiceUrl + "?sql=" + sql, header, new HashMap<>());
        int code = response.code();
        if (code != 200) {
            throw new RuntimeException("查询出错:" + response.body().string());
        }
        String string = response.body().string();
        Map<String, Object> map = JsonUtils.fromJson(string, new TypeReference<Map<String, Object>>() {
        });
        return map;
    }
}
