package cn.ac.iscas.dmo.connector.jdbc;

import cn.ac.iscas.dmo.connector.support.DmoBlob;
import cn.ac.iscas.dmo.connector.support.DmoClob;
import cn.ac.iscas.dmo.connector.util.DateSafeUtils;
import cn.ac.iscas.dmo.connector.util.IoUtils;
import cn.ac.iscas.dmo.connector.util.LocalDateTimeUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Date;
import java.sql.*;
import java.text.ParseException;
import java.time.*;
import java.util.*;

/**
 * 结果集实现
 *
 * @author zhuquanwen
 * @version 1.0
 * @date 2024/9/23 14:40
 */

public class ResultSetImpl implements ResultSet {

    private final DmoStatement statement;

    /**
     * 是否已经关闭RS
     */
    private boolean closed;

    /**
     * 当前缓存的数据
     */
    private List<Map<String, Object>> cacheData = new ArrayList<>();

    /**
     * 调用{@link #next()} 时，获取当前游标所在的这条记录
     */
    private Map<String, Object> currentData = new HashMap<>();

    /**
     * 返回数据的顺序索引与currentData中key的映射关系，给ResultSet的各个get函数使用
     */
    private Map<Integer, String> headerMapping = new HashMap<>();

    /**
     * 元数据
     */
    private List<Map<String, Object>> metas;

    /**
     * 返回数据的currentData中key与顺序索引的映射关系，与headerMapping相反，暂不考虑同一个key对应多个顺序索引的问题
     */
    private final Map<String, List<Integer>> headerMapping2 = new HashMap<>();

    /**
     * 调用getXXX读取到的上一个值
     */
    private Object lastData;

    /**
     * 每次拉取数据的大小，默认1000
     */
    private int fetchSize = 10;

    /**
     * 影响行数
     */
    private int updateCount = 0;

    /**
     * 游标
     */
    private int cursor = -1;


    public ResultSetImpl(DmoStatement statement) {
        this.statement = statement;
    }

    @Override
    public boolean next() throws SQLException {
        checkClosed();
        // 非pullModel方式，直接获取当前行的值
        if (++cursor < cacheData.size()) {
            currentData = cacheData.get(cursor);
            return true;
        }
        return false;
    }

    @Override
    public void close() throws SQLException {
        // 清除集合和map，不清除也可以
        Optional.ofNullable(cacheData).ifPresent(List::clear);
        Optional.ofNullable(headerMapping).ifPresent(Map::clear);
        Optional.ofNullable(currentData).ifPresent(Map::clear);
        closed = true;
    }

    @Override
    public boolean wasNull() throws SQLException {
        checkClosed();
        return Objects.isNull(lastData);
    }

    @Override
    public String getString(int columnIndex) throws SQLException {
        return getString(getDataKey(columnIndex));
    }

    @Override
    public boolean getBoolean(int columnIndex) throws SQLException {
        return getBoolean(getDataKey(columnIndex));
    }

    @Override
    public byte getByte(int columnIndex) throws SQLException {
        return getByte(getDataKey(columnIndex));
    }

    @Override
    public short getShort(int columnIndex) throws SQLException {
        return getShort(getDataKey(columnIndex));
    }

    @Override
    public int getInt(int columnIndex) throws SQLException {
        return getInt(getDataKey(columnIndex));
    }

    @Override
    public long getLong(int columnIndex) throws SQLException {
        return getLong(getDataKey(columnIndex));
    }

    @Override
    public float getFloat(int columnIndex) throws SQLException {
        return getFloat(getDataKey(columnIndex));
    }

    @Override
    public double getDouble(int columnIndex) throws SQLException {
        return getDouble(getDataKey(columnIndex));
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        return getBigDecimal(getDataKey(columnIndex), scale);
    }

    @Override
    public byte[] getBytes(int columnIndex) throws SQLException {
        return getBytes(getDataKey(columnIndex));
    }

    @Override
    public Date getDate(int columnIndex) throws SQLException {
        return getDate(getDataKey(columnIndex));
    }

    @Override
    public Time getTime(int columnIndex) throws SQLException {
        return getTime(getDataKey(columnIndex));
    }

    @Override
    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        return getTimestamp(getDataKey(columnIndex));
    }

    @Override
    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        return getAsciiStream(getDataKey(columnIndex));
    }

    @Override
    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        return getUnicodeStream(getDataKey(columnIndex));
    }

    @Override
    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        return getBinaryStream(getDataKey(columnIndex));
    }

    @Override
    public String getString(String columnLabel) throws SQLException {
        checkClosed();
        lastData = getObject(columnLabel);
        return lastData == null ? null : String.valueOf(lastData);
    }

    @Override
    public boolean getBoolean(String columnLabel) throws SQLException {
        checkClosed();
        lastData = getObject(columnLabel);
        if (lastData instanceof BigDecimal) {
            BigDecimal bigDecimal = (BigDecimal) lastData;
            return 1 == bigDecimal.intValue();
        } else if (lastData instanceof String) {
            String str = (String) lastData;
            return "1".equals(str);
        } else if (lastData instanceof Integer) {
            return 1 == (int) lastData;
        } else if (lastData instanceof Long) {
            return 1L == (long) lastData;
        } else if (lastData instanceof Short) {
            return 1 == (short) lastData;
        } else if (lastData instanceof Byte) {
            return 1 == (byte) lastData;
        }
        Boolean bool = (Boolean) lastData;
        return bool != null && bool;
    }

    @Override
    public byte getByte(String columnLabel) throws SQLException {
        checkClosed();
        lastData = getObject(columnLabel);
        if (lastData == null) {
            return 0;
        } else if (lastData instanceof Byte) {
            return (Byte) lastData;
        } else {
            return Byte.parseByte(String.valueOf(lastData));
        }
    }

    @Override
    public short getShort(String columnLabel) throws SQLException {
        checkClosed();
        lastData = getObject(columnLabel);
        if (lastData == null) {
            return 0;
        } else if (lastData instanceof Short) {
            return (Short) lastData;
        } else {
            return Short.parseShort(String.valueOf(lastData));
        }
    }

    @Override
    public int getInt(String columnLabel) throws SQLException {
        checkClosed();
        lastData = getObject(columnLabel);
        if (lastData instanceof Integer) {
            return ((Integer) lastData).intValue();
        }
        return lastData == null ? 0 : Integer.parseInt(lastData.toString());
    }

    @Override
    public long getLong(String columnLabel) throws SQLException {
        checkClosed();
        lastData = getObject(columnLabel);
        if (Objects.isNull(lastData)) {
            return 0L;
        } else if (lastData instanceof Long) {
            return (Long) lastData;
        } else if (lastData instanceof Integer) {
            return ((Integer) lastData).longValue();
        }
        return Long.parseLong(String.valueOf(lastData));
    }

    @Override
    public float getFloat(String columnLabel) throws SQLException {
        checkClosed();
        lastData = getObject(columnLabel);
        if (Objects.isNull(lastData)) {
            return 0L;
        } else if (lastData instanceof Float) {
            return (float) lastData;
        } else if (lastData instanceof Long) {
            return ((Long) lastData).floatValue();
        } else if (lastData instanceof Integer) {
            return ((Integer) lastData).floatValue();
        } else if (lastData instanceof Double) {
            return ((Double) lastData).floatValue();
        }
        return Float.parseFloat(String.valueOf(lastData));
    }

    @Override
    public double getDouble(String columnLabel) throws SQLException {
        checkClosed();
        lastData = getObject(columnLabel);
        if (Objects.isNull(lastData)) {
            return 0L;
        } else if (lastData instanceof Double) {
            return (double) lastData;
        } else if (lastData instanceof Long) {
            return ((Long) lastData).doubleValue();
        } else if (lastData instanceof Integer) {
            return ((Integer) lastData).doubleValue();
        } else if (lastData instanceof Double) {
            return (Double) lastData;
        }
        return Double.parseDouble(String.valueOf(lastData));
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
        checkClosed();
        lastData = getObject(columnLabel);
        if (Objects.isNull(lastData)) {
            return null;
        }
        BigDecimal bigDecimal = new BigDecimal(lastData.toString());
        //noinspection ResultOfMethodCallIgnored,BigDecimalMethodWithoutRoundingCalled
        bigDecimal.setScale(scale);
        return bigDecimal;
    }

    @Override
    public byte[] getBytes(String columnLabel) throws SQLException {
        lastData = getObject(columnLabel);
        if (Objects.isNull(lastData)) {
            return null;
        } else if (lastData instanceof byte[]) {
            byte[] bytes = (byte[]) lastData;
            return bytes.length == 0 ? null : bytes;
        } else if (lastData instanceof Blob) {
            Blob blob = (Blob) lastData;
            InputStream binaryStream = blob.getBinaryStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                IoUtils.transferTo(binaryStream, byteArrayOutputStream);
            } catch (IOException e) {
                throw new SQLException(e);
            }
            return byteArrayOutputStream.toByteArray();
        } else {
            throw new UnsupportedOperationException(String.format("暂时不支持getBytes中获取类型为:[%s]的数据", lastData.getClass().getName()));
        }

    }

    @Override
    public Date getDate(String columnLabel) throws SQLException {
        checkClosed();
        lastData = getObject(columnLabel);
        if (Objects.isNull(lastData)) {
            return null;
        } else if (lastData instanceof Date) {
            return (Date) lastData;
        } else if (lastData instanceof java.util.Date) {
            // java.util.Date转为java.sql.Date
            return new Date(((java.util.Date) lastData).getTime());
        } else if (lastData instanceof LocalDateTime) {
            LocalDateTime localDateTime = (LocalDateTime) lastData;
            // todo 时区暂时用默认的
            java.util.Date utilDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            return new java.sql.Date(utilDate.getTime());
        } else if (lastData instanceof LocalDate) {
            LocalDate localDate = (LocalDate) lastData;
            LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.of(0, 0));
            // todo 时区暂时用默认的
            java.util.Date utilDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            return new java.sql.Date(utilDate.getTime());
        } else if (lastData instanceof String) {
            // 仅支持yyyy-MM-dd格式的数据
            String data = (String) lastData;
            if (data.length() < 10) {
                throw new SQLException("时间转换出错, 时间字符串长度必须大于等于10");
            }
            data = data.substring(0, 10);
            try {
                java.util.Date date = DateSafeUtils.parse(data, "yyyy-MM-dd");
                return new Date(date.getTime());
            } catch (ParseException e) {
                throw new SQLException("时间转换出错", e);
            }
        } else {
            throw new SQLException(String.format("无法将类型为:[%s]的值:[%s]转为java.sql.Date类型", lastData.getClass().getName(), lastData));
        }
    }

    @Override
    public Time getTime(String columnLabel) throws SQLException {
        checkClosed();
        lastData = getObject(columnLabel);
        if (Objects.isNull(lastData)) {
            return null;
        } else if (lastData instanceof Date) {
            Date date = (Date) lastData;
            return new Time(date.getTime());
        } else if (lastData instanceof java.util.Date) {
            java.util.Date date = (java.util.Date) lastData;
            return new Time(date.getTime());
        } else if (lastData instanceof LocalDateTime) {
            LocalDateTime localDateTime = (LocalDateTime) lastData;
            LocalTime localTime = localDateTime.toLocalTime();
            return Time.valueOf(localTime);
        } else if (lastData instanceof LocalDate) {
            return Time.valueOf("00:00:00");
        } else if (lastData instanceof LocalTime) {
            LocalTime localTime = (LocalTime) lastData;
            return Time.valueOf(localTime);
        } else if (lastData instanceof String) {
            // 仅支持HH:mm:ss格式的数据
            try {
                return Time.valueOf(lastData.toString());
            } catch (Exception e) {
                throw new SQLException("时间转换出错", e);
            }
        } else {
            throw new SQLException(String.format("无法将类型为:[%s]的值:[%s]转为java.sql.Date类型", lastData.getClass().getName(), lastData));
        }
    }

    @Override
    public Timestamp getTimestamp(String columnLabel) throws SQLException {
        checkClosed();
        lastData = getObject(columnLabel);
        if (Objects.isNull(lastData)) {
            return null;
        } else if (lastData instanceof Date) {
            Date date = (Date) lastData;
            return new Timestamp(date.getTime());
        } else if (lastData instanceof java.util.Date) {
            java.util.Date date = (java.util.Date) lastData;
            return new Timestamp(date.getTime());
        } else if (lastData instanceof LocalDateTime) {
            LocalDateTime localDateTime = (LocalDateTime) lastData;
            return Timestamp.valueOf(localDateTime);
        } else if (lastData instanceof LocalDate) {
            LocalDate localDate = (LocalDate) lastData;
            LocalDateTime localDateTime = localDate.atStartOfDay();
            return Timestamp.valueOf(localDateTime);
        } else if (lastData instanceof String) {
            // 仅支持yyyy-MM-dd HH:mm:ss格式的数据
            try {
                return Timestamp.valueOf(lastData.toString());
            } catch (Exception e) {
                throw new SQLException("时间转换出错", e);
            }
        } else {
            throw new SQLException(String.format("无法将类型为:[%s]的值:[%s]转为java.sql.Date类型", lastData.getClass().getName(), lastData));
        }
    }

    @Override
    public InputStream getAsciiStream(String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：getAsciiStream");
    }

    @Override
    public InputStream getUnicodeStream(String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：getAsciiStream");
    }

    @Override
    public InputStream getBinaryStream(String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：getBinaryStream");
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        checkClosed();
        //todo 暂不处理
        return null;
    }

    @Override
    public void clearWarnings() throws SQLException {
        checkClosed();
        //todo 暂不处理
    }

    @Override
    public String getCursorName() throws SQLException {
        checkClosed();
        //todo 暂不处理
        return null;
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
//        List<Integer> indexes = new ArrayList<>(headerMapping.keySet());
//        indexes.sort(Integer::compareTo);
//        List<String> headers = indexes.stream().map(index -> headerMapping.get(index)).collect(Collectors.toList());
        return new ResultSetMetaDataImpl(metas);
    }

    @Override
    public Object getObject(int columnIndex) throws SQLException {
        return getObject(getDataKey(columnIndex));
    }

    @Override
    public Object getObject(String columnLabel) throws SQLException {
        checkClosed();
        lastData = currentData.get(columnLabel);
        return lastData;
    }

    @Override
    public int findColumn(String columnLabel) throws SQLException {
        if (headerMapping2.containsKey(columnLabel)) {
            return headerMapping2.get(columnLabel).get(0);
        }
        return 0;
    }

    @Override
    public Reader getCharacterStream(int columnIndex) throws SQLException {
        return getCharacterStream(getDataKey(columnIndex));
    }

    @Override
    public Reader getCharacterStream(String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：getCharacterStream");
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        return getBigDecimal(getDataKey(columnIndex));
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
        checkClosed();
        lastData = getObject(columnLabel);
        return lastData == null ? null : new BigDecimal(lastData.toString());
    }

    @Override
    public boolean isBeforeFirst() throws SQLException {
        checkClosed();
        return cursor == -1;
    }

    @Override
    public boolean isAfterLast() throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：isAfterLast");
    }

    @Override
    public boolean isFirst() throws SQLException {
        checkClosed();
        return cursor == 0;
    }

    @Override
    public boolean isLast() throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：isLast");
    }

    @Override
    public void beforeFirst() throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：beforeFirst");
    }

    @Override
    public void afterLast() throws SQLException {
        throw new UnsupportedOperationException("暂时支持方法：afterLast");
    }

    @Override
    public boolean first() throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：first");
    }

    @Override
    public boolean last() throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：last");
    }

    @Override
    public int getRow() throws SQLException {
        checkClosed();
        return cursor + 1;
    }

    @Override
    public boolean absolute(int row) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：absolute");
    }

    @Override
    public boolean relative(int rows) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：relative");
    }

    @Override
    public boolean previous() throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：previous");
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：setFetchDirection");
    }

    @Override
    public int getFetchDirection() throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：getFetchDirection");
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        this.fetchSize = rows;
    }

    @Override
    public int getFetchSize() throws SQLException {
        return fetchSize;
    }

    @Override
    public int getType() throws SQLException {
        // 暂时只支持向前遍历
        return ResultSet.TYPE_FORWARD_ONLY;
    }

    @Override
    public int getConcurrency() throws SQLException {
        // 暂时只支持readOnly并发模式
        return ResultSet.CONCUR_READ_ONLY;
    }

    @Override
    public boolean rowUpdated() throws SQLException {
        // todo 当前行是否已更新，直接返回false
        return false;
    }

    @Override
    public boolean rowInserted() throws SQLException {
        // todo 当前行是否已插入内容，直接返回false
        return false;
    }

    @Override
    public boolean rowDeleted() throws SQLException {
        // todo 当前行是否已删除内容，直接返回false
        return false;
    }

    @Override
    public void updateNull(int columnIndex) throws SQLException {
        updateNull(getDataKey(columnIndex));
    }

    @Override
    public void updateBoolean(int columnIndex, boolean x) throws SQLException {
        updateBoolean(getDataKey(columnIndex), x);
    }

    @Override
    public void updateByte(int columnIndex, byte x) throws SQLException {
        updateByte(getDataKey(columnIndex), x);
    }

    @Override
    public void updateShort(int columnIndex, short x) throws SQLException {
        updateShort(getDataKey(columnIndex), x);
    }

    @Override
    public void updateInt(int columnIndex, int x) throws SQLException {
        updateInt(getDataKey(columnIndex), x);
    }

    @Override
    public void updateLong(int columnIndex, long x) throws SQLException {
        updateLong(getDataKey(columnIndex), x);
    }

    @Override
    public void updateFloat(int columnIndex, float x) throws SQLException {
        updateFloat(getDataKey(columnIndex), x);
    }

    @Override
    public void updateDouble(int columnIndex, double x) throws SQLException {
        updateDouble(getDataKey(columnIndex), x);
    }

    @Override
    public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
        updateBigDecimal(getDataKey(columnIndex), x);
    }

    @Override
    public void updateString(int columnIndex, String x) throws SQLException {
        updateString(getDataKey(columnIndex), x);
    }

    @Override
    public void updateBytes(int columnIndex, byte[] x) throws SQLException {
        updateBytes(getDataKey(columnIndex), x);
    }

    @Override
    public void updateDate(int columnIndex, Date x) throws SQLException {
        updateDate(getDataKey(columnIndex), x);
    }

    @Override
    public void updateTime(int columnIndex, Time x) throws SQLException {
        updateTime(getDataKey(columnIndex), x);
    }

    @Override
    public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
        updateTimestamp(getDataKey(columnIndex), x);
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
        updateAsciiStream(getDataKey(columnIndex), x, length);
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
        updateBinaryStream(getDataKey(columnIndex), x, length);
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
        updateCharacterStream(getDataKey(columnIndex), x, length);
    }

    @Override
    public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
        updateObject(getDataKey(columnIndex), x, scaleOrLength);
    }

    @Override
    public void updateObject(int columnIndex, Object x) throws SQLException {
        updateObject(getDataKey(columnIndex), x);
    }

    @Override
    public void updateNull(String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateNull");
    }

    @Override
    public void updateBoolean(String columnLabel, boolean x) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateBoolean");
    }

    @Override
    public void updateByte(String columnLabel, byte x) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateByte");
    }

    @Override
    public void updateShort(String columnLabel, short x) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateShort");
    }

    @Override
    public void updateInt(String columnLabel, int x) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateInt");
    }

    @Override
    public void updateLong(String columnLabel, long x) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateLong");
    }

    @Override
    public void updateFloat(String columnLabel, float x) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateFloat");
    }

    @Override
    public void updateDouble(String columnLabel, double x) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateDouble");
    }

    @Override
    public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateBigDecimal");
    }

    @Override
    public void updateString(String columnLabel, String x) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateString");
    }

    @Override
    public void updateBytes(String columnLabel, byte[] x) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateBytes");
    }

    @Override
    public void updateDate(String columnLabel, Date x) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateDate");
    }

    @Override
    public void updateTime(String columnLabel, Time x) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateTime");
    }

    @Override
    public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateTimestamp");
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateAsciiStream");
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateBinaryStream");
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateCharacterStream");
    }

    @Override
    public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateObject");
    }

    @Override
    public void updateObject(String columnLabel, Object x) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateObject");
    }

    @Override
    public void insertRow() throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：insertRow");
    }

    @Override
    public void updateRow() throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateRow");
    }

    @Override
    public void deleteRow() throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：deleteRow");
    }

    @Override
    public void refreshRow() throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：refreshRow");
    }

    @Override
    public void cancelRowUpdates() throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：cancelRowUpdates");
    }

    @Override
    public void moveToInsertRow() throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：moveToInsertRow");
    }

    @Override
    public void moveToCurrentRow() throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：moveToCurrentRow");
    }

    @Override
    public Statement getStatement() throws SQLException {
        return statement;
    }

    @Override
    public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
        return getObject(getDataKey(columnIndex), map);
    }

    @Override
    public Ref getRef(int columnIndex) throws SQLException {
        return getRef(getDataKey(columnIndex));
    }

    @Override
    public Blob getBlob(int columnIndex) throws SQLException {
        return getBlob(getDataKey(columnIndex));
    }

    @Override
    public Clob getClob(int columnIndex) throws SQLException {
        return getClob(getDataKey(columnIndex));
    }

    @Override
    public Array getArray(int columnIndex) throws SQLException {
        return getArray(getDataKey(columnIndex));
    }

    @Override
    public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
        return getObject(columnLabel);
    }

    @Override
    public Ref getRef(String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：getRef");
    }

    @Override
    public Blob getBlob(String columnLabel) throws SQLException {
        checkClosed();
        lastData = getObject(columnLabel);
        return new DmoBlob((byte[]) lastData);
//        throw new UnsupportedOperationException("暂时不支持方法：getBlob");
    }

    @Override
    public Clob getClob(String columnLabel) throws SQLException {
        String str = getString(columnLabel);
        return new DmoClob(str);
//        throw new UnsupportedOperationException("暂时不支持方法：getClob");
    }

    @Override
    public Array getArray(String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：getArray");
    }

    @Override
    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        return getDate(getDataKey(columnIndex), cal);
    }

    @Override
    public Date getDate(String columnLabel, Calendar cal) throws SQLException {
        checkClosed();
        Date date = getDate(columnLabel);
        if (date != null) {
            String pattern = "yyyy-MM-dd HH:mm:ss";
            TimeZone timeZone = cal.getTimeZone();
            String format = DateSafeUtils.format(date, pattern);
            try {
                java.util.Date datex = DateSafeUtils.parse(format, pattern, timeZone);
                return new Date(datex.getTime());
            } catch (ParseException e) {
                throw new SQLException("格式化时间出错", e);
            }
        }
        return null;
    }

    @Override
    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        return getTime(getDataKey(columnIndex), cal);
    }

    @Override
    public Time getTime(String columnLabel, Calendar cal) throws SQLException {
        checkClosed();
        Time time = getTime(columnLabel);
        if (time != null) {
            String pattern = "yyyy-MM-dd HH:mm:ss";
            TimeZone timeZone = cal.getTimeZone();
            String format = DateSafeUtils.format(time, pattern);
            try {
                java.util.Date datex = DateSafeUtils.parse(format, pattern, timeZone);
                return new Time(datex.getTime());
            } catch (ParseException e) {
                throw new SQLException("格式化时间出错", e);
            }
        }
        return null;
    }

    @Override
    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        return getTimestamp(getDataKey(columnIndex), cal);
    }

    @Override
    public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
        checkClosed();
        Timestamp time = getTimestamp(columnLabel);
        if (time != null) {
            String pattern = "yyyy-MM-dd HH:mm:ss";
            TimeZone timeZone = cal.getTimeZone();
            String format = DateSafeUtils.format(time, pattern);
            try {
                java.util.Date datex = DateSafeUtils.parse(format, pattern, timeZone);
                return new Timestamp(datex.getTime());
            } catch (ParseException e) {
                throw new SQLException("格式化时间出错", e);
            }
        }
        return null;
    }

    @Override
    public URL getURL(int columnIndex) throws SQLException {
        return getURL(getDataKey(columnIndex));
    }

    @Override
    public URL getURL(String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：getURL");
    }

    @Override
    public void updateRef(int columnIndex, Ref x) throws SQLException {
        updateRef(getDataKey(columnIndex), x);
    }

    @Override
    public void updateRef(String columnLabel, Ref x) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateRef");
    }

    @Override
    public void updateBlob(int columnIndex, Blob x) throws SQLException {
        updateBlob(getDataKey(columnIndex), x);
    }

    @Override
    public void updateBlob(String columnLabel, Blob x) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateBlob");
    }

    @Override
    public void updateClob(int columnIndex, Clob x) throws SQLException {
        updateClob(getDataKey(columnIndex), x);
    }

    @Override
    public void updateClob(String columnLabel, Clob x) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateClob");
    }

    @Override
    public void updateArray(int columnIndex, Array x) throws SQLException {
        updateArray(getDataKey(columnIndex), x);
    }

    @Override
    public void updateArray(String columnLabel, Array x) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateArray");
    }

    @Override
    public RowId getRowId(int columnIndex) throws SQLException {
        return getRowId(getDataKey(columnIndex));
    }

    @Override
    public RowId getRowId(String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：getRowId");
    }

    @Override
    public void updateRowId(int columnIndex, RowId x) throws SQLException {
        updateRowId(getDataKey(columnIndex), x);
    }

    @Override
    public void updateRowId(String columnLabel, RowId x) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateRowId");
    }

    @Override
    public int getHoldability() throws SQLException {
        // 默认使用HOLD_CURSORS_OVER_COMMIT
        return HOLD_CURSORS_OVER_COMMIT;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return closed;
    }

    @Override
    public void updateNString(int columnIndex, String nString) throws SQLException {
        updateNString(getDataKey(columnIndex), nString);
    }

    @Override
    public void updateNString(String columnLabel, String nString) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateNString");
    }

    @Override
    public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
        updateNClob(getDataKey(columnIndex), nClob);
    }

    @Override
    public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateNClob");
    }

    @Override
    public NClob getNClob(int columnIndex) throws SQLException {
        return getNClob(getDataKey(columnIndex));
    }

    @Override
    public NClob getNClob(String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：NClob");
    }

    @Override
    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        return getSQLXML(getDataKey(columnIndex));
    }

    @Override
    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：SQLXML");
    }

    @Override
    public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
        updateSQLXML(getDataKey(columnIndex), xmlObject);
    }

    @Override
    public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateSQLXML");
    }

    @Override
    public String getNString(int columnIndex) throws SQLException {
        return getNString(getDataKey(columnIndex));
    }

    @Override
    public String getNString(String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：getNString");
    }

    @Override
    public Reader getNCharacterStream(int columnIndex) throws SQLException {
        return getNCharacterStream(getDataKey(columnIndex));
    }

    @Override
    public Reader getNCharacterStream(String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：getNCharacterStream");
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        updateNCharacterStream(getDataKey(columnIndex), x, length);
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateNCharacterStream");
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
        updateAsciiStream(getDataKey(columnIndex), x, length);
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateBinaryStream");
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        updateCharacterStream(getDataKey(columnIndex), x, length);
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateAsciiStream");
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateBinaryStream");
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateCharacterStream");
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
        updateBlob(getDataKey(columnIndex), inputStream, length);
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateBlob");
    }

    @Override
    public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
        updateClob(getDataKey(columnIndex), reader, length);
    }

    @Override
    public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateClob");
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
        updateNClob(getDataKey(columnIndex), reader, length);
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateNClob");
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
        updateNCharacterStream(getDataKey(columnIndex), x);
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateNCharacterStream");
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
        updateAsciiStream(getDataKey(columnIndex), x);
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateBinaryStream");
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
        updateCharacterStream(getDataKey(columnIndex), x);
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateAsciiStream");
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateBinaryStream");
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateCharacterStream");
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
        updateBlob(getDataKey(columnIndex), inputStream);
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateBlob");
    }

    @Override
    public void updateClob(int columnIndex, Reader reader) throws SQLException {
        updateClob(getDataKey(columnIndex), reader);
    }

    @Override
    public void updateClob(String columnLabel, Reader reader) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateClob");
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader) throws SQLException {
        updateNClob(getDataKey(columnIndex), reader);
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：updateNClob");
    }

    @Override
    public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
        return getObject(getDataKey(columnIndex), type);
    }

    @Override
    public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
        if (type == null) {
            throw new SQLException("type不能为空");
        }
        checkClosed();
        if (type.equals(String.class)) {
            return (T) getString(columnLabel);

        } else if (type.equals(BigDecimal.class)) {
            return (T) getBigDecimal(columnLabel);

        } else if (type.equals(BigInteger.class)) {
            return (T) getBigInteger(columnLabel);

        } else if (type.equals(Boolean.class) || type.equals(Boolean.TYPE)) {
            Boolean aBoolean = getBoolean(columnLabel);
            return (T) aBoolean;

        } else if (type.equals(Byte.class) || type.equals(Byte.TYPE)) {
            Byte aByte = getByte(columnLabel);
            return (T) aByte;
        } else if (type.equals(Short.class) || type.equals(Short.TYPE)) {
            Short s = getShort(columnLabel);
            return (T) s;
        } else if (type.equals(Integer.class) || type.equals(Integer.TYPE)) {
            Integer integer = getInt(columnLabel);
            return (T) integer;
        } else if (type.equals(Long.class) || type.equals(Long.TYPE)) {
            Long l = getLong(columnLabel);
            return (T) l;
        } else if (type.equals(Float.class) || type.equals(Float.TYPE)) {
            Float f = getFloat(columnLabel);
            return (T) f;
        } else if (type.equals(Double.class) || type.equals(Double.TYPE)) {
            Double d = getDouble(columnLabel);
            return (T) d;
        } else if (type.equals(byte[].class)) {
            return (T) getBytes(columnLabel);
        } else if (type.equals(Date.class)) {
            return (T) getDate(columnLabel);
        } else if (type.equals(Time.class)) {
            return (T) getTime(columnLabel);
        } else if (type.equals(Timestamp.class)) {
            return (T) getTimestamp(columnLabel);
        } else if (type.equals(java.util.Date.class)) {
            Timestamp ts = getTimestamp(columnLabel);
            return ts == null ? null : (T) java.util.Date.from(ts.toInstant());
        } else if (type.equals(java.util.Calendar.class)) {
            throw new UnsupportedOperationException("暂时不支持方法：getObject(columnLabel, Calendar.class)");
        } else if (type.equals(Clob.class)) {
            return (T) getClob(columnLabel);
        } else if (type.equals(Blob.class)) {
            return (T) getBlob(columnLabel);
        } else if (type.equals(Array.class)) {
            return (T) getArray(columnLabel);
        } else if (type.equals(Ref.class)) {
            return (T) getRef(columnLabel);
        } else if (type.equals(URL.class)) {
            return (T) getURL(columnLabel);
        } else if (type.equals(Struct.class)) {
            throw new SQLFeatureNotSupportedException();
        } else if (type.equals(RowId.class)) {
            return (T) getRowId(columnLabel);
        } else if (type.equals(NClob.class)) {
            return (T) getNClob(columnLabel);
        } else if (type.equals(SQLXML.class)) {
            return (T) getSQLXML(columnLabel);
        } else if (type.equals(LocalDate.class)) {
            return (T) getLocalDate(columnLabel);
        } else if (type.equals(LocalDateTime.class)) {
            return (T) getLocalDateTime(columnLabel);
        } else if (type.equals(LocalTime.class)) {
            return (T) getLocalTime(columnLabel);
        } else if (type.equals(OffsetDateTime.class)) {
            throw new UnsupportedOperationException("暂时不支持方法：getObject(columnLabel, OffsetDateTime.class)");
        } else if (type.equals(OffsetTime.class)) {
            throw new UnsupportedOperationException("暂时不支持方法：getObject(columnLabel, OffsetTime.class)");
        } else if (type.equals(ZonedDateTime.class)) {
            throw new UnsupportedOperationException("暂时不支持方法：getObject(columnLabel, ZonedDateTime.class)");
        } else if (type.equals(Duration.class)) {
            throw new UnsupportedOperationException("暂时不支持方法：getObject(columnLabel, Duration.class)");
        } else {
            return (T) getObject(columnLabel);
        }
    }

    public LocalTime getLocalTime(String columnLabel) throws SQLException {
        checkClosed();
        lastData = getObject(columnLabel);
        if (Objects.isNull(lastData)) {
            return null;
        } else if (lastData instanceof LocalTime) {
            return (LocalTime) lastData;
        } else if (lastData instanceof Date) {
            Date d = (Date) lastData;
            return LocalDateTimeUtils.parseLocalDateTime(DateSafeUtils.format(d)).toLocalTime();
        } else if (lastData instanceof java.util.Date) {
            java.util.Date d = (java.util.Date) lastData;
            return LocalDateTimeUtils.parseLocalDateTime(DateSafeUtils.format(d)).toLocalTime();
        } else if (lastData instanceof String) {
            try {
                return LocalDateTimeUtils.parseLocalDateTime((String) lastData).toLocalTime();
            } catch (Exception e) {
                throw new SQLException("时间转换出错", e);
            }
        } else {
            throw new SQLException(String.format("无法将类型为:[%s]的值:[%s]转为LocalTime类型", lastData.getClass().getName(), lastData));
        }
    }

    public LocalDateTime getLocalDateTime(String columnLabel) throws SQLException {
        checkClosed();
        lastData = getObject(columnLabel);
        if (Objects.isNull(lastData)) {
            return null;
        } else if (lastData instanceof LocalDateTime) {
            return (LocalDateTime) lastData;
        } else if (lastData instanceof Date) {
            Date d = (Date) lastData;
            return LocalDateTimeUtils.parseLocalDateTime(DateSafeUtils.format(d));
        } else if (lastData instanceof java.util.Date) {
            java.util.Date d = (java.util.Date) lastData;
            return LocalDateTimeUtils.parseLocalDateTime(DateSafeUtils.format(d));
        } else if (lastData instanceof String) {
            try {
                return LocalDateTimeUtils.parseLocalDateTime((String) lastData);
            } catch (Exception e) {
                throw new SQLException("时间转换出错", e);
            }
        } else {
            throw new SQLException(String.format("无法将类型为:[%s]的值:[%s]转为LocalDateTime类型", lastData.getClass().getName(), lastData));
        }
    }

    public LocalDate getLocalDate(String columnLabel) throws SQLException {
        checkClosed();
        lastData = getObject(columnLabel);
        if (Objects.isNull(lastData)) {
            return null;
        } else if (lastData instanceof LocalDate) {
            return (LocalDate) lastData;
        } else if (lastData instanceof Date) {
            Date d = (Date) lastData;
            return LocalDateTimeUtils.parseLocalDateTime(DateSafeUtils.format(d)).toLocalDate();
        } else if (lastData instanceof java.util.Date) {
            java.util.Date d = (java.util.Date) lastData;
            return LocalDateTimeUtils.parseLocalDateTime(DateSafeUtils.format(d)).toLocalDate();
        } else if (lastData instanceof String) {
            try {
                return LocalDateTimeUtils.parseLocalDateTime((String) lastData).toLocalDate();
            } catch (Exception e) {
                throw new SQLException("时间转换出错", e);
            }
        } else {
            throw new SQLException(String.format("无法将类型为:[%s]的值:[%s]转为LocalDate类型", lastData.getClass().getName(), lastData));
        }
    }


    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：unwrap");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new UnsupportedOperationException("暂时不支持方法：isWrapperFor");
    }

    public void checkClosed() throws SQLException {
        if (closed) {
            throw new SQLException("ResultSet已经关闭");
        }
    }

    /**
     * 根据下标索引获取返回数据中的key
     */
    public String getDataKey(int index) throws SQLException {
        if (headerMapping != null) {
            if (headerMapping.containsKey(index - 1)) {
                return headerMapping.get(index - 1);
            }
        }
        throw new SQLException(String.format("无法获取索引:%d的对应columnLabel", index));
    }

    public BigInteger getBigInteger(String columnLabel) throws SQLException {
        checkClosed();
        lastData = getObject(columnLabel);
        return new BigInteger(lastData.toString());
    }

    public void setCacheData(List<Map<String, Object>> cacheData) {
        this.cacheData = cacheData;
    }

    public void setHeaderMapping(Map<Integer, String> headerMapping) {
        this.headerMapping = headerMapping;
        if (headerMapping != null) {
            headerMapping.forEach((k, v) -> headerMapping2.computeIfAbsent(v, key -> new ArrayList<>()).add(k));
        }
    }

    public void setMetas(List<Map<String, Object>> metas) {
        this.metas = metas;
    }

}
