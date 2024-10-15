package cn.ac.iscas.dmo.connector.jdbc;

import cn.ac.iscas.dmo.connector.Constants;
import cn.ac.iscas.dmo.connector.conf.HostInfo;
import cn.ac.iscas.dmo.connector.util.Base64Utils;
import cn.ac.iscas.dmo.db.rpc.GrpcUtils;
import cn.ac.iscas.dmo.db.rpc.execute.StreamServiceGrpc;

import java.sql.*;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 *  数据中台jdbc连接的实现类
 *
 * @author zhuquanwen
 * @version 1.0
 * @date 2024/9/23 13:32
 */

public class ConnectionImpl implements Connection, Constants {
    private final HostInfo origHostInfo;

    private String token;

    private String datasourceType;

    private String datasourceName;

    private boolean closed;

    private StreamServiceGrpc.StreamServiceBlockingStub stub;

    /**
     * 默认结果集类型
     * */
    private static final int DEFAULT_RESULT_SET_TYPE = ResultSet.TYPE_FORWARD_ONLY;

    private static final int DEFAULT_RESULT_SET_CONCURRENCY = ResultSet.CONCUR_READ_ONLY;

    public ConnectionImpl(HostInfo hostInfo) throws SQLException {
        this.origHostInfo = hostInfo;
        // 获取中台的一些信息
//        createDmoUrl(hostInfo);
        createDmoInfo(hostInfo);
        // 初始化grpc
        initGrpc();
        // 获取dbType
//        this.dbType = initDbType();
    }

    private void initGrpc() {
        stub = GrpcUtils.getStub(origHostInfo.getHost(), origHostInfo.getPort());
    }

//    private String initDbType() throws SQLException {
//
//        String protocol = "https://";
//        if ("false".equals(useSsl)) {
//            protocol = "http://";
//        }
//
//        String getDbTypeUrl = protocol + origHostInfo.getHost() + ":" + origHostInfo.getPort() + "/dmo/data-service/dbType?url=" + sqlServiceUrl;
//
//        Response response = null;
//        try {
//            response = httpClient.doGetWithRes(getDbTypeUrl, new HashMap<>());
//        } catch (IOException e) {
//            throw new SQLException("获取数据源类型出错", e);
//        }
//        int code = response.code();
//        String resStr;
//        try {
//            resStr = response.body().string();
//        } catch (IOException e) {
//            throw new SQLException(e.getMessage(), e);
//        }
//        if (code != 200) {
//            throw new SQLException("获取数据源类型出错:" + resStr);
//        }
//        Map<String, Object> map = JsonUtils.fromJson(resStr, new TypeReference<Map<String, Object>>() {
//        });
//        return (String) map.get("value");
//    }

//    private String initDbType() throws SQLException {
//
//        String protocol = "https://";
//        if ("false".equals(useSsl)) {
//            protocol = "http://";
//        }
//
//        String getDbTypeUrl = protocol + origHostInfo.getHost() + ":" + origHostInfo.getPort() + "/dmo/data-service/dbType?url=" + sqlServiceUrl;
//
//        Response response = null;
//        try {
//            response = httpClient.doGetWithRes(getDbTypeUrl, new HashMap<>());
//        } catch (IOException e) {
//            throw new SQLException("获取数据源类型出错", e);
//        }
//        int code = response.code();
//        String resStr;
//        try {
//            resStr = response.body().string();
//        } catch (IOException e) {
//            throw new SQLException(e.getMessage(), e);
//        }
//        if (code != 200) {
//            throw new SQLException("获取数据源类型出错:" + resStr);
//        }
//        Map<String, Object> map = JsonUtils.fromJson(resStr, new TypeReference<Map<String, Object>>() {
//        });
//        return (String) map.get("value");
//    }

    public static ConnectionImpl getInstance(HostInfo hostInfo) throws SQLException {
        // 获取链接实例
        return new ConnectionImpl(hostInfo);
    }

    @Override
    public Statement createStatement() throws SQLException {
        return createStatement(DEFAULT_RESULT_SET_TYPE, DEFAULT_RESULT_SET_CONCURRENCY);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        StatementImpl statement = new StatementImpl(this);
        statement.setResultSetType(resultSetType);
        statement.setResultSetConcurrency(resultSetConcurrency);
        return statement;
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return prepareStatement(sql, DEFAULT_RESULT_SET_TYPE, DEFAULT_RESULT_SET_CONCURRENCY);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        PreparedStatementImpl statement = new PreparedStatementImpl(this, sql);
        statement.setResultSetType(resultSetType);
        statement.setResultSetConcurrency(resultSetConcurrency);
        return statement;
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return this.prepareCall(sql, DEFAULT_RESULT_SET_TYPE, DEFAULT_RESULT_SET_CONCURRENCY);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        CallableStatementImpl statement = new CallableStatementImpl(this, sql);
        statement.setResultSetType(resultSetType);
        statement.setResultSetConcurrency(resultSetConcurrency);
        return statement;
    }


    @Override
    public String nativeSQL(String sql) throws SQLException {
        throw new UnsupportedOperationException("暂不支持方法nativeSQL");
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return false;
    }

    @Override
    public void commit() throws SQLException {

    }

    @Override
    public void rollback() throws SQLException {

    }

    @Override
    public void close() throws SQLException {
        closed = true;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return closed;
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return new DatabaseMetaDataImpl(this);
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {

    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return false;
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {

    }

    @Override
    public String getCatalog() throws SQLException {
        return null;
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {

    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return Connection.TRANSACTION_NONE;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    @Override
    public void clearWarnings() throws SQLException {

    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return null;
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {

    }

    @Override
    public void setHoldability(int holdability) throws SQLException {

    }

    @Override
    public int getHoldability() throws SQLException {
        return 0;
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return null;
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return null;
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {

    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {

    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        throw new UnsupportedOperationException("暂不支持方法prepareCall");
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return prepareStatement(sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return prepareStatement(sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return prepareStatement(sql);
    }

    @Override
    public Clob createClob() throws SQLException {
        return null;
    }

    @Override
    public Blob createBlob() throws SQLException {
        return null;
    }

    @Override
    public NClob createNClob() throws SQLException {
        return null;
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return null;
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return false;
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {

    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {

    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return null;
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return null;
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return null;
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return null;
    }

    @Override
    public void setSchema(String schema) throws SQLException {

    }

    @Override
    public String getSchema() throws SQLException {
        return null;
    }

    @Override
    public void abort(Executor executor) throws SQLException {

    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {

    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return 0;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    private void createDmoInfo(HostInfo hostInfo) throws SQLException {
        token = hostInfo.getToken();

        String tmpDsType = hostInfo.getDatasourceType();
        if (tmpDsType == null) {
            throw new SQLException("连接URL中datasourceType为空");
        }
        datasourceType = Base64Utils.decodeToStr(tmpDsType);
        String tmpDsName = hostInfo.getDatasourceName();
        if (tmpDsName == null) {
            throw new SQLException("连接URL中datasourceName为空");
        }
        datasourceName =  Base64Utils.decodeToStr(tmpDsName);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public HostInfo getOrigHostInfo() {
        return origHostInfo;
    }

    public String getDatasourceName() {
        return datasourceName;
    }

    public void setDatasourceName(String datasourceName) {
        this.datasourceName = datasourceName;
    }

    public String getDatasourceType() {
        return datasourceType;
    }

    public void setDatasourceType(String datasourceType) {
        this.datasourceType = datasourceType;
    }


    public StreamServiceGrpc.StreamServiceBlockingStub getStub() {
        return stub;
    }

}
