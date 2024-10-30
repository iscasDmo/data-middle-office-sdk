package cn.ac.iscas.dmo.api.sdk.model;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author zhuquanwen
 * @date 2017/12/25 16:41
 **/
@SuppressWarnings({"rawtypes", "unused"})

public class ResponseEntity<T> implements Serializable {

    /**
     * http状态码
     * 200：正常；401：未登陆；403：没有权限；400：请求参数校验失败；500：服务器内部错误
     */
    protected Integer status = 200;

    /**
     * 给予用户提示的信息
     * 如果为非200的返回，可以将此message提示给用户
     */
    protected String message;

    /**
     * 出现错误的详细描述(调试)
     * 错误详细描述
     */
    protected String desc;

    /**
     * 异常堆栈信息
     * 后端的异常栈信息，如果过长，只截取前面一部分
     */
    protected String stackTrace;

    /**
     * 返回值
     */
    protected T value;

    /**
     * 访问URL
     */
    protected String requestURL;

    /**
     * 当前接口访问耗时
     */
    protected long tookInMillis;

    /**
     * 可以用此函数填充一些关联字段以备不时之需
     */
    private transient Consumer<Map<String, Object>> consumer;

    /**
     * 过时的参数，未来会删除
     */
    @Deprecated
    protected int total;

    public ResponseEntity(Integer status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

    public ResponseEntity() {
        super();
        this.message = "操作成功";
    }

    public ResponseEntity(String message) {
        super();
        this.message = message;
    }

    public static <T> ResponseEntity<T> ok(T value) {
        if (Objects.isNull(value)) {
            return new ResponseEntity<>();
        }
        ResponseEntity<T> res = new ResponseEntity<>();
        res.setValue(value);
        return res;
    }

    public static <T> ResponseEntity<T> ok() {
        return ok(null);
    }

    public static ResponseEntity<Void> error(int status, String message, Exception e) {
        ResponseEntity<Void> res = new ResponseEntity<>(status, message);
        res.setDesc(e.getMessage());
        return res;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public long getTookInMillis() {
        return tookInMillis;
    }

    public void setTookInMillis(long tookInMillis) {
        this.tookInMillis = tookInMillis;
    }

    public Consumer<Map<String, Object>> getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer<Map<String, Object>> consumer) {
        this.consumer = consumer;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
