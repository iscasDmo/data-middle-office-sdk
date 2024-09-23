package cn.ac.iscas.dmo.connector.util;

/**
 * 自动配置属性
 *
 * @author zhuquanwen
 */

public class OkHttpProps {
    /**
     * 读取超时时间毫秒
     */

    private int readTimeout = 10000;

    /**
     * 写数据超时时间毫秒
     */

    private int writeTimeout = 10000;

    /**
     * 连接超时时间毫秒
     */

    private int connectTimeout = 10000;

    /**
     * 最大空闲数目
     */
    private int maxIdleConnection = 15;

    /**
     * keep alive保持时间 分钟
     */
    private long keepAliveDuration = 5;

    /**
     * 每个域名下最多允许的请求
     */
    private int maxRequests = 100;

    /**
     * 最大允许请求数
     */
    private int maxRequestsPerHost = 100;

    /**
     * 拦截器类的全限定名
     */
    private String interceptorClasses;


    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getMaxIdleConnection() {
        return maxIdleConnection;
    }

    public void setMaxIdleConnection(int maxIdleConnection) {
        this.maxIdleConnection = maxIdleConnection;
    }

    public long getKeepAliveDuration() {
        return keepAliveDuration;
    }

    public void setKeepAliveDuration(long keepAliveDuration) {
        this.keepAliveDuration = keepAliveDuration;
    }

    public int getMaxRequests() {
        return maxRequests;
    }

    public void setMaxRequests(int maxRequests) {
        this.maxRequests = maxRequests;
    }

    public int getMaxRequestsPerHost() {
        return maxRequestsPerHost;
    }

    public void setMaxRequestsPerHost(int maxRequestsPerHost) {
        this.maxRequestsPerHost = maxRequestsPerHost;
    }

    public String getInterceptorClasses() {
        return interceptorClasses;
    }

    public void setInterceptorClasses(String interceptorClasses) {
        this.interceptorClasses = interceptorClasses;
    }
}
