package com.rxtx.utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密方式
 *
 * @author zhangxin
 * @version 1.0
 * @date 2021/11/19 15:00
 */
public class CrytogramUtil
{

  public static String encrypt(String paramString1, String paramString2)
  {
    try
    {
      MessageDigest localMessageDigest = MessageDigest.getInstance(paramString2);
      localMessageDigest.reset();
      byte[] arrayOfByte1 = paramString1.getBytes();
      byte[] arrayOfByte2 = localMessageDigest.digest(arrayOfByte1);
      //BASE64Encoder localBASE64Encoder = new BASE64Encoder();
      //return localBASE64Encoder.encode(arrayOfByte2);
      return null;
    } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
      localNoSuchAlgorithmException.printStackTrace();
    }
    return paramString1;
  }
}
