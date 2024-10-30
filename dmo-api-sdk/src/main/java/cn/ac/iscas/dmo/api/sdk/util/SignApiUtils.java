package cn.ac.iscas.dmo.api.sdk.util;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author zhuquanwen
 * @version 1.0
 * @date 2024/9/14 8:31
 */

public class SignApiUtils {
    private SignApiUtils() {
    }

    /**
     * 计算hash值
     *
     * @param method    请求方式
     * @param url       请求URL
     * @param body      请求体 没有的话为空字符串
     * @param nonceStr  随机串
     * @param timestamp 时间戳(毫秒数)
     * @return java.lang.String
     * @date 2024/9/14
     * @since jdk1.8
     */
    public static String createHash(String method, String url, String body, String nonceStr, long timestamp)
            throws NoSuchAlgorithmException {
        // 格式为：
//        HTTP请求方法\n
//        URL\n
//        请求时间戳\n
//        请求随机串\n
//        请求报文主体\n

        String formatStr = "%s\n%s\n%s\n%s\n%s\n";
        String dataStr = String.format(formatStr, method, url, nonceStr, timestamp, body);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = digest.digest(dataStr.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashedBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * 校验签名
     *
     * @param hash         hash值
     * @param signature    签名值
     * @param appPublicKey 公钥
     * @return boolean
     * @date 2024/9/14
     * @since jdk1.8
     */
    public static boolean verifySignature(String hash, String signature, String appPublicKey)
            throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, SignatureException {
        byte[] keyBytes = Base64.getDecoder().decode(appPublicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        // 校验签名
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(publicKey);
        sig.update(hash.getBytes(StandardCharsets.UTF_8));
        signature = signature.replace(" ", "+");
        signature = signature.replace("%20", "+");
        return sig.verify(Base64.getDecoder().decode(signature));
    }

    /**
     * 计算签名值
     *
     * @param hash          hash值
     * @param appPrivateKey 私钥
     * @return boolean
     * @date 2024/9/14
     * @since jdk1.8
     */
    public static String signature(String hash, String appPrivateKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, InvalidKeyException {
        byte[] keyBytes = Base64.getDecoder().decode(appPrivateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(hash.getBytes(StandardCharsets.UTF_8));
        byte[] signBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signBytes);
    }

    /**
     * 生成请求头token
     *
     * @param appId     appId
     * @param nonceStr  随机数
     * @param signature 签名值
     * @param timestamp 时间戳
     * @return java.lang.String
     * @date 2024/9/14
     * @since jdk1.8
     */
    public static String createToken(String appId, String nonceStr, String signature, long timestamp) {
        String formatStr = "DMOAPI-SHA256-RSA2048 appId=\"%s\",nonce_str=\"%s\",signature=\"%s\",timestamp=\"%s\"";
        return String.format(formatStr, appId, nonceStr, signature, timestamp);
    }

    /**
     * 生成请求头token
     *
     * @param appId         appId
     * @param method        请求方式
     * @param url           请求url
     * @param body          请求体
     * @param nonceStr      随机数
     * @param timestamp     时间戳
     * @param appPrivateKey 私钥
     * @return java.lang.String
     * @date 2024/9/14
     * @since jdk1.8
     */
    public static String createToken(String appId, String method, String url, String body, String nonceStr, long timestamp, String appPrivateKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, InvalidKeyException {
        String hash = createHash(method, url, body, nonceStr, timestamp);
        String signature = signature(hash, appPrivateKey);
        return createToken(appId, nonceStr, signature, timestamp);
    }

}
