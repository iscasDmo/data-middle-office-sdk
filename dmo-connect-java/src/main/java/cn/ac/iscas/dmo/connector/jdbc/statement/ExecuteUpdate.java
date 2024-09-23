package cn.ac.iscas.dmo.connector.jdbc.statement;

import cn.ac.iscas.dmo.connector.jdbc.ConnectionImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

/**
 * 查询处理器
 *
 * @author zhuquanwen
 * @version 1.0
 * @date 2024/9/23 14:46
 */

public class ExecuteUpdate {

    public static int execute(ConnectionImpl connection, String sql) throws SQLException, IOException {
        Map<String, Object> map = ExecuteQuery.doExecute(connection, sql);
        if (map.containsKey("value")) {
            Object o = map.get("value");
            if (o instanceof Integer) {
                return (int) o;
            }
        }
        return 0;
    }

}
