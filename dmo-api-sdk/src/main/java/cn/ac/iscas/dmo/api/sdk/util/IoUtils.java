package cn.ac.iscas.dmo.api.sdk.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author zhuquanwen
 * @version 1.0
 * @date 2024/9/23 13:59
 */

public class IoUtils {
    private IoUtils() {}

    public static void transferTo(InputStream is, OutputStream os) throws IOException {
        byte[] buff = new byte[1024];
        int len;
        while ((len = is.read(buff)) > 0) {
            os.write(buff, 0, len);
        }
    }
}
