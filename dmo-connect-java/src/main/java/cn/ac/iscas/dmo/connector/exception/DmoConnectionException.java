package cn.ac.iscas.dmo.connector.exception;

import java.sql.SQLException;

/**
 * @author zhuquanwen
 * @version 1.0
 * @date 2024/11/6 16:46
 */

public class DmoConnectionException extends SQLException {
    public DmoConnectionException() {
        super();
    }

    public DmoConnectionException(String message) {
        super(message);
    }

    public DmoConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DmoConnectionException(Throwable cause) {
        super(cause);
    }

}
