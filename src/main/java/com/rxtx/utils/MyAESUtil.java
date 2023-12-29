package com.rxtx.utils;

import cn.hutool.core.codec.Base64Encoder;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class MyAESUtil {

    private final static String S_KEY = "wzmtr1234eip1234";
 
    // 加密
    public static String encrypt(String sSrc) throws Exception {
        byte[] raw = S_KEY.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes(StandardCharsets.UTF_8));
        return new Base64Encoder().encode(encrypted);

    }
 
    // 解密
    public static String decrypt(String sSrc) throws Exception {
       /* byte[] raw = S_KEY.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] encrypted1 =  Base64Decoder.decodeStr(sSrc);
        byte[] original = cipher.doFinal(encrypted1);
        return new String(original, StandardCharsets.UTF_8);*/
        return null;
    }
}