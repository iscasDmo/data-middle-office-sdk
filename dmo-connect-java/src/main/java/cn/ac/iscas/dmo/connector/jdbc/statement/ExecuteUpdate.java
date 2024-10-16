package cn.ac.iscas.dmo.connector.jdbc.statement;

import cn.ac.iscas.dmo.connector.jdbc.ConnectionImpl;
import cn.ac.iscas.dmo.connector.jdbc.StatementImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

/**
 * 查询处理器
 *
 * @author zhuquanwen
 * @version 1.0
 * @date 2024/9/23 14:46
 */

public class ExecuteUpdate {

    public static int execute(StatementImpl statement, String sql) throws SQLException, IOException {
        ConnectionImpl connection = (ConnectionImpl) statement.getConnection();
        Map<String, Object> mapValue = ExecuteQuery.doExecute(connection, sql);
        if (mapValue != null) {
            Object data = mapValue.get("data");
            Object generateValue = mapValue.get("generateValue");
            if (Objects.nonNull(generateValue)) {
                statement.setGenerateValue(generateValue);
            }
            if (data instanceof Integer) {
                return (int) data;
            }
        }
        return 0;
    }

}
