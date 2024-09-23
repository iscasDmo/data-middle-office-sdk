package cn.ac.iscas.dmo.connector.util;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhuquanwen
 * @version 1.0
 * @date 2024/9/23 16:59
 */

public class Base64Utils {
    private Base64Utils() {}

//    public static String encode(byte[] bytes){
//        BASE64Encoder decoder = new BASE64Encoder();
//        return replaceEnter(decoder.encode(bytes));
//    }

    public static String encodeToStr(String uri){
        return Base64.encodeBase64String(uri.getBytes(StandardCharsets.UTF_8));
//        BASE64Encoder encoder = new BASE64Encoder();
//        return replaceEnter(encoder.encode(uri.getBytes(StandardCharsets.UTF_8)));
    }

    public static String replaceEnter(String str){
        String reg ="[\n-\r]";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }

    public static byte[] decode(String base64Str){
        return Base64.decodeBase64(base64Str);
//        byte[] b = null;
//        BASE64Decoder decoder = new BASE64Decoder();
//
//        try {
//            b = decoder.decodeBuffer(replaceEnter(base64Str));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return b;
    }

    public static String decodeToStr(String base64Str) {
        return new String(decode(base64Str), StandardCharsets.UTF_8);
    }
}
