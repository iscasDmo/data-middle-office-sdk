package cn.ac.iscas.dmo.connector.support;

import javax.sql.rowset.serial.SerialException;
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
    private char[] buf;
    private int oriLen;

    public DmoClob(String str) {
        this.str = str;
        buf = str == null ? new char[0] : str.toCharArray();
        this.oriLen = buf.length;
    }

    @Override
    public long length() throws SQLException {
        return buf.length;
    }

    @Override
    public String getSubString(long pos, int length) throws SQLException {
        if (pos >= 1L && pos <= this.length()) {
            if (pos - 1L + (long) length > this.length()) {
                throw new SerialException("Invalid position and substring length");
            } else {
                try {
                    return new String(this.buf, (int) pos - 1, length);
                } catch (StringIndexOutOfBoundsException var5) {
                    throw new SerialException("StringIndexOutOfBoundsException: " + var5.getMessage());
                }
            }
        } else {
            return null;
//            throw new SerialException("Invalid position in DmoClob object set");
        }
    }

    @Override
    public Reader getCharacterStream() throws SQLException {
//        return new CharArrayReader(bytes);
        return new StringReader(str);
    }

    @Override
    public InputStream getAsciiStream() throws SQLException {
        return new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public long position(String searchstr, long start) throws SQLException {
        if (start >= 1L && start <= this.length()) {
            char[] var4 = searchstr.toCharArray();
            int var5 = (int) start - 1;
            int var6 = 0;
            long var7 = (long) var4.length;

            while ((long) var5 < this.length()) {
                if (var4[var6] == this.buf[var5]) {
                    if ((long) (var6 + 1) == var7) {
                        return (long) (var5 + 1) - (var7 - 1L);
                    }

                    ++var6;
                    ++var5;
                } else if (var4[var6] != this.buf[var5]) {
                    ++var5;
                }
            }

            return -1L;
        } else {
            return -1L;
        }
    }

    @Override
    public long position(Clob searchstr, long start) throws SQLException {
        return this.position(searchstr.getSubString(1L, (int) searchstr.length()), start);
    }

    @Override
    public int setString(long pos, String str) throws SQLException {
        return this.setString(pos, str, 0, str.length());
    }

    @Override
    public int setString(long pos, String str, int offset, int len) throws SQLException {
        String var6 = str.substring(offset);
        char[] var7 = var6.toCharArray();
        if (offset >= 0 && offset <= str.length()) {
            if (pos >= 1L && pos <= this.length()) {
                if ((long) len > this.oriLen) {
                    throw new SerialException("Buffer is not sufficient to hold the value");
                } else if (len + offset > str.length()) {
                    throw new SerialException("Invalid OffSet. Cannot have combined offset  and length that is greater that the Blob buffer");
                } else {
                    int var8 = 0;
                    --pos;

                    while (var8 < len || offset + var8 + 1 < str.length() - offset) {
                        this.buf[(int) pos + var8] = var7[offset + var8];
                        ++var8;
                    }

                    return var8;
                }
            } else {
                throw new SerialException("Invalid position in Clob object set");
            }
        } else {
            throw new SerialException("Invalid offset in byte array set");
        }
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
        if (len > this.length()) {
            throw new SerialException("Length more than what can be truncated");
        } else {
            if (len == 0L) {
                this.buf = new char[0];
            } else {
                this.buf = this.getSubString(1L, (int)len).toCharArray();
            }

        }
    }

    @Override
    public void free() throws SQLException {
        if (this.buf != null) {
            this.buf = null;
        }
    }

    @Override
    public Reader getCharacterStream(long pos, long length) throws SQLException {
        if (pos >= 1L && pos <= length()) {
            if (pos - 1L + length > length()) {
                throw new SerialException("Invalid position and substring length");
            } else if (length <= 0L) {
                throw new SerialException("Invalid length specified");
            } else {
                return new CharArrayReader(this.buf, (int)pos, (int)length);
            }
        } else {
            throw new SerialException("Invalid position in Clob object set");
        }
    }
}
