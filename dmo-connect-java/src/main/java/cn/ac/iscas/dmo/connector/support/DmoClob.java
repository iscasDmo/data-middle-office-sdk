package cn.ac.iscas.dmo.connector.support;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Clob;
import java.sql.SQLException;

/**
 * @author zhuquanwen
 * @version 1.0
 * @date 2024/9/29 16:33
 */

public class DmoClob implements Clob {
    private String str;
    private byte[] bytes;

    public DmoClob(String str) {
        this.str = str;
        bytes = str == null ? new byte[0] : str.getBytes(StandardCharsets.UTF_8);
    }
    @Override
    public long length() throws SQLException {
        return bytes.length;
    }

    @Override
    public String getSubString(long pos, int length) throws SQLException {
        return "";
    }

    @Override
    public Reader getCharacterStream() throws SQLException {
        return new StringReader(str);
    }

    @Override
    public InputStream getAsciiStream() throws SQLException {
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public long position(String searchstr, long start) throws SQLException {
        return 0;
    }

    @Override
    public long position(Clob searchstr, long start) throws SQLException {
        return 0;
    }

    @Override
    public int setString(long pos, String str) throws SQLException {
        return 0;
    }

    @Override
    public int setString(long pos, String str, int offset, int len) throws SQLException {
        return 0;
    }

    @Override
    public OutputStream setAsciiStream(long pos) throws SQLException {
        return null;
    }

    @Override
    public Writer setCharacterStream(long pos) throws SQLException {
        return null;
    }

    @Override
    public void truncate(long len) throws SQLException {

    }

    @Override
    public void free() throws SQLException {

    }

    @Override
    public Reader getCharacterStream(long pos, long length) throws SQLException {
        return null;
    }
}
