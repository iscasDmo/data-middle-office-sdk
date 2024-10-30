package cn.ac.iscas.dmo.api.sdk.exception;

/**
 * @author zhuquanwen
 * @version 1.0
 * @date 2024/10/29 15:46
 */

public class DmoApiSdkException extends Exception {
    public DmoApiSdkException() {
        super();
    }

    public DmoApiSdkException(String message) {
        super(message);
    }

    public DmoApiSdkException(String message, Throwable cause) {
        super(message, cause);
    }

    public DmoApiSdkException(Throwable cause) {
        super(cause);
    }

    protected DmoApiSdkException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
