package cn.ac.iscas.dmo.connector.support;

import javax.sql.rowset.serial.SerialException;
import java.io.*;
import java.sql.Blob;
import java.sql.SQLException;

/**
 * @author zhuquanwen
 * @version 1.0
 * @date 2024/10/8 9:09
 */

public class DmoBlob implements Blob {
    private byte[] data;
    private long len;
    private long origLen;

    public DmoBlob(byte[] data) {
        this.data = data;
        this.len = data == null ? 0L : data.length;
        origLen = len;
    }

    public DmoBlob() {
    }

    @Override
    public long length() throws SQLException {
        return len;
    }

    @Override
    public byte[] getBytes(long pos, int length) throws SQLException {
        if (data == null) {
            return new byte[0];
        }
        if ((long) length > len) {
            length = (int) len;
        }
        if (pos >= 1L && len - pos >= 0L) {
            --pos;
            byte[] buff = new byte[length];

            for (int i = 0; i < length; ++i) {
                buff[i] = data[(int) pos];
                ++pos;
            }
            return buff;
        } else {
            throw new SerialException("Invalid arguments: position cannot be less than 1 or greater than the length of the DmoBlob");
        }
    }

    @Override
    public InputStream getBinaryStream() throws SQLException {
        return new ByteArrayInputStream(data);
    }

    @Override
    public long position(byte[] pattern, long start) throws SQLException {
        if (start >= 1L && start <= this.len) {
            int pos = (int) start - 1;
            int index = 0;
            long patternLen = pattern.length;
            while ((long) pos < this.len) {
                if (pattern[index] == data[pos]) {
                    if ((long) (index + 1) == patternLen) {
                        return (long) (pos + 1) - (patternLen - 1L);
                    }
                    ++index;
                    ++pos;
                } else if (pattern[index] != data[pos]) {
                    ++pos;
                }
            }
            return -1L;
        } else {
            return -1L;
        }
    }

    @Override
    public long position(Blob pattern, long start) throws SQLException {
        return this.position(pattern.getBytes(1L, (int) pattern.length()), start);
    }

    @Override
    public int setBytes(long pos, byte[] bytes) throws SQLException {
        return this.setBytes(pos, bytes, 0, bytes.length);
    }

    @Override
    public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
        if (offset >= 0 && offset <= bytes.length) {
            if (pos >= 1L && pos <= this.length()) {
                if ((long) len > this.origLen) {
                    throw new SerialException("Buffer is not sufficient to hold the value");
                } else if (len + offset > bytes.length) {
                    throw new SerialException("Invalid OffSet. Cannot have combined offset and length that is greater that the Blob buffer");
                } else {
                    int var6 = 0;
                    --pos;

                    while (var6 < len || offset + var6 + 1 < bytes.length - offset) {
                        data[(int) pos + var6] = bytes[offset + var6];
                        ++var6;
                    }
                    return var6;
                }
            } else {
                throw new SerialException("Invalid position in BLOB object set");
            }
        } else {
            throw new SerialException("Invalid offset in byte array set");
        }

    }

    @Override
    public OutputStream setBinaryStream(long pos) throws SQLException {
        return new DmoBlobOutputStream(pos, this);
    }

    @Override
    public void truncate(long len) throws SQLException {
        if (len > this.len) {
            throw new SerialException("Length more than what can be truncated");
        } else {
            if ((int) len == 0) {
                this.data = new byte[0];
                this.len = len;
            } else {
                this.len = len;
                this.data = this.getBytes(1L, (int) this.len);
            }

        }
    }

    @Override
    public void free() throws SQLException {
        if (this.data != null) {
            this.data = null;
            this.len = 0;
        }
    }

    @Override
    public InputStream getBinaryStream(long pos, long length) throws SQLException {
        if (pos >= 1L && pos <= this.length()) {
            if (length >= 1L && length <= this.len - pos + 1L) {
                return new ByteArrayInputStream(this.data, (int)pos - 1, (int)length);
            } else {
                throw new SerialException("length is < 1 or pos + length > total number of bytes");
            }
        } else {
            throw new SerialException("Invalid position in BLOB object set");
        }
    }

    public static class DmoBlobOutputStream extends OutputStream {
        private final long pos;
        private int index;
        private DmoBlob dmoBlob;

        public DmoBlobOutputStream(long pos, DmoBlob dmoBlob) {
            this.pos = pos;
            this.dmoBlob = dmoBlob;
            index = ((Long) (pos - 1)).intValue();
            if (pos > dmoBlob.getLen()) {
                throw new RuntimeException("pos error");
            }
        }

        @Override
        public void write(int b) throws IOException {
            if (index >= dmoBlob.getData().length) {
                byte[] newData = new byte[dmoBlob.getData().length + 1];
                System.arraycopy(dmoBlob.getData(), 0, newData, 0, dmoBlob.getData().length);
                newData[newData.length - 1] = (byte) b;
                dmoBlob.setData(newData);
                dmoBlob.setLen(newData.length);
            } else {
                dmoBlob.getData()[index++] = (byte) b;
            }
        }
    }

    public byte[] getData() {
        return data;
    }

    public long getLen() {
        return len;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setLen(long len) {
        this.len = len;
    }
}
