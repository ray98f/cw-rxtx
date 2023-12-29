package com.rxtx.utils;

import cn.hutool.core.codec.Base64;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.Key;

/**
 * @author frp
 */
@Slf4j
public class AesUtils {

    /**
     * 加密方式
     */
    private static final String KEY_ALGORITHM = "AES";
    /**
     * 默认的加密算法
     */
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    /**
     * 16位密钥以及CBC解密所需偏移向量IV
     */
    private final static byte[] SECRET_KEY_BYTE = new byte[]{54, 62, -119, 0, 62, -24, -83, -112, 84, -30, -79, 72, 17, 123, 7, -119};


    public static String decrypt(String encryptedData) {

        //解密之前先把Base64格式的数据转成原始格式
        byte[] dataByte = Base64.decode(encryptedData);

        String data = null;

        try {
            //指定算法，模式，填充方法 创建一个Cipher实例
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);

            //生成Key对象
            Key sKeySpec = new SecretKeySpec(SECRET_KEY_BYTE, KEY_ALGORITHM);

            //把向量初始化到算法参数
            AlgorithmParameters params = AlgorithmParameters.getInstance(KEY_ALGORITHM);
            params.init(new IvParameterSpec(SECRET_KEY_BYTE));

            //指定用途，密钥，参数 初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, params);

            //执行解密
            byte[] result = cipher.doFinal(dataByte);

            //解密后转成字符串
            data = new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Error when decrypt : {}", e.getMessage());
        }
        return data;
    }

    public static String encrypt(String decryptedData) {
        SecretKeySpec sKeySpec = new SecretKeySpec(SECRET_KEY_BYTE, KEY_ALGORITHM);
        byte[] encrypted;
        try {
            //"算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);

            //使用CBC模式，需要一个向量iv，可增加加密算法的强度
            IvParameterSpec iv = new IvParameterSpec(SECRET_KEY_BYTE);
            cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, iv);
            encrypted = cipher.doFinal(decryptedData.getBytes());
            //此处使用BASE64做转码功能，同时能起到2次加密的作用。
            return Base64.encode(encrypted);
        } catch (Exception e) {
            log.error("Error when encrypt : {}", e.getMessage());
        }
        return "";
    }
}
