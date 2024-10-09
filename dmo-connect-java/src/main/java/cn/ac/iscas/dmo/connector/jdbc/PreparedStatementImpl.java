package cn.ac.iscas.dmo.connector.jdbc;

import cn.ac.iscas.dmo.connector.util.DateSafeUtils;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * @author zhuquanwen
 * @version 1.0
 * @date 2024/9/23 14:30
 */

public class PreparedStatementImpl extends StatementImpl implements PreparedStatement {

    private String[] sqlSegment;

    private String[] sqlData;

    public PreparedStatementImpl(ConnectionImpl connection) {
        super(connection);
    }

    public PreparedStatementImpl(ConnectionImpl connection, String sql) {
        super(connection);
        sqlSegment = (sql + " ").split("\\?");
        sqlData = new String[sqlSegment.length - 1];
        Arrays.fill(sqlData, "?");
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        return super.executeQuery(createExecuteSql());
    }

    @Override
    public int executeUpdate() throws SQLException {
        return super.executeUpdate(createExecuteSql());
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        sqlData[parameterIndex - 1] = "null";
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        sqlData[parameterIndex - 1] = String.valueOf(x);
    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
        sqlData[parameterIndex - 1] = String.valueOf(x);
    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
        sqlData[parameterIndex - 1] = String.valueOf(x);
    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        sqlData[parameterIndex - 1] = String.valueOf(x);
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
        sqlData[parameterIndex - 1] = String.valueOf(x);
    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
        sqlData[parameterIndex - 1] = String.valueOf(x);
    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {
        sqlData[parameterIndex - 1] = String.valueOf(x);
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        sqlData[parameterIndex - 1] = String.valueOf(x);
    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
        if (x == null) {
            sqlData[parameterIndex - 1] = "null";
        } else {
            // 转移单引号
            x = x.replace("'", "\\'");
            sqlData[parameterIndex - 1] = "'" + x + "'";
        }
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：setBytes");
    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {
        setJavaUtilDate(parameterIndex, x);
    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {
        setJavaUtilDate(parameterIndex, x);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        setJavaUtilDate(parameterIndex, x);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：setAsciiStream");
    }

    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：setUnicodeStream");
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：setBinaryStream");
    }

    @Override
    public void clearParameters() throws SQLException {
        Arrays.fill(sqlData, "?");
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        if (x == null) {
            setNull(parameterIndex, 0);
        } else if (x instanceof Boolean) {
            setBoolean(parameterIndex, (Boolean) x);
        } else if (x instanceof Byte) {
            setByte(parameterIndex, (byte) x);
        } else if (x instanceof Short) {
            setShort(parameterIndex, (short) x);
        } else if (x instanceof Long) {
            setLong(parameterIndex, (long) x);
        } else if (x instanceof Float) {
            setFloat(parameterIndex, (float) x);
        } else if (x instanceof Double) {
            setDouble(parameterIndex, (double) x);
        } else if (x instanceof Integer) {
            setInt(parameterIndex, (int) x);
        } else if (x instanceof BigDecimal) {
            setBigDecimal(parameterIndex, (BigDecimal) x);
        } else if (x instanceof String) {
            setString(parameterIndex, (String) x);
        } else if (x instanceof byte[]) {
            setBytes(parameterIndex, (byte[]) x);
        } else if (x instanceof Date) {
            setDate(parameterIndex, (Date) x);
        } else if (x instanceof Time) {
            setTime(parameterIndex, (Time) x);
        } else if (x instanceof Timestamp) {
            setTimestamp(parameterIndex, (Timestamp) x);
        } else  {
            setString(parameterIndex, x.toString());
        }
    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {
        setObject(parameterIndex, x, 0);
    }

    @Override
    public boolean execute() throws SQLException {
        return super.execute(createExecuteSql());
    }

    @Override
    public void addBatch() throws SQLException {
        super.addBatch(createExecuteSql());
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：setCharacterStream");
    }

    @Override
    public void setRef(int parameterIndex, Ref x) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：setRef");
    }

    @Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：setBlob");
    }

    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：setClob");
    }

    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：setArray");
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：getMetaData");
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        setJavaUtilDate(parameterIndex, x, cal);
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        setJavaUtilDate(parameterIndex, x, cal);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        setJavaUtilDate(parameterIndex, x, cal);
    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        setNull(parameterIndex, sqlType);
    }

    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：setURL");
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：getParameterMetaData");
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：setRowId");
    }

    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：setNString");
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：setNCharacterStream");
    }

    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：setNClob");
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：setClob");
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：setBlob");
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：setNClob");
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：setSQLXML");
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        setObject(parameterIndex, x, 0);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：setAsciiStream");
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：setBinaryStream");
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：setCharacterStream");
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：setAsciiStream");
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：setBinaryStream");
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：setCharacterStream");
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：setNCharacterStream");
    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：setClob");
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：setBlob");
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：setNClob");
    }

    private String createExecuteSql() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sqlSegment.length; i++) {
            sb.append(sqlSegment[i]);
            if (i < sqlData.length) {
                sb.append(sqlData[i]);
            }
        }
        return sb.toString();
    }
    private void setJavaUtilDate(int parameterIndex, java.util.Date x) {
        if (x == null) {
            sqlData[parameterIndex - 1] = "null";
        } else {
            String dateFormat = DateSafeUtils.format(x);
            sqlData[parameterIndex - 1] = "'" + dateFormat + "'";
        }
    }

    private void setJavaUtilDate(int parameterIndex, java.util.Date x, Calendar cal) throws SQLException {
        TimeZone timeZone = cal.getTimeZone();
        String dateFormat = DateSafeUtils.format(x, timeZone);
        setString(parameterIndex, dateFormat);
    }

}
