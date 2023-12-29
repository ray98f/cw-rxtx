package com.rxtx.utils;

import com.rxtx.constant.CommonConstants;
import gnu.io.SerialPort;
import io.swagger.models.auth.In;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * description:
 *
 * @author zhangxin
 * @version 1.0
 * @date 2023/8/8 11:18
 */
public class SerialUtil {

    /**
     * 转为 HEX
     * @param str
     * @return
     */
    public static String toHex(String str){
        StringBuffer sbf = new StringBuffer();
        byte[] b = str.getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sbf.append(hex.toUpperCase() + "  ");
        }
        return sbf.toString().trim();
    }

    /**
     *
     * @param hex
     * @return
     */
    public static String toStr(String hex) {
        return new String(hexToByte(hex));
    }

    /**
     * 转 HEX 字节
     * @param hex
     * @return
     */
    public static byte[] hexToByte(String hex){
        hex = hex.toUpperCase().replace(" ","");
        ByteArrayOutputStream bao = new ByteArrayOutputStream(hex.length() / 2);
        // 将每2位16进制整数组装成一个字节
        for (int i = 0; i < hex.length(); i += 2) {
            bao.write((CommonConstants.HEX_STRING.indexOf(hex.charAt(i)) << 4 | CommonConstants.HEX_STRING.indexOf(hex.charAt(i + 1))));
        }
        return bao.toByteArray();
    }

    /**
     * 获取校验位配置
     * @param checkBit
     * @return
     */
    public static int getParity(String checkBit){
        if (CommonConstants.NONE.equals(checkBit)){
            return SerialPort.PARITY_NONE;
        } else if (CommonConstants.ODD.equals(checkBit)){
            return SerialPort.PARITY_ODD;
        } else if (CommonConstants.EVEN.equals(checkBit)){
            return SerialPort.PARITY_EVEN;
        } else {
            return SerialPort.PARITY_NONE;
        }
    }

    /**
     * 读取数据
     * @param in
     * @return
     */
    public static byte[] readFromPort(InputStream in) {
        byte[] bytes = {};
        try {
            // 缓冲区大小为一个字节
            byte[] readBuffer = new byte[1];
            int bytesNum = in.read(readBuffer);
            while (bytesNum > 0) {
                bytes = concat(bytes, readBuffer);
                bytesNum = in.read(readBuffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                    in = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }

    /**
     * 字节转换
     * @param format
     * @param b
     * @return
     */
    public static String printHexString(String format, byte[] b) {
        String result = new String(b);
        if (CommonConstants.FORMAT_HEX.equals(format)){
            return SerialUtil.toHex(result);
        }
        return result;
    }

    /**
     * 合并数组
     *
     * @param firstArray  第一个数组
     * @param secondArray 第二个数组
     * @return 合并后的数组
     */
    public static byte[] concat(byte[] firstArray, byte[] secondArray) {
        if (firstArray == null || secondArray == null) {
            if (firstArray != null) {
                return firstArray;
            }
            if (secondArray != null) {
                return secondArray;
            }
            return null;
        }
        byte[] bytes = new byte[firstArray.length + secondArray.length];
        System.arraycopy(firstArray, 0, bytes, 0, firstArray.length);
        System.arraycopy(secondArray, 0, bytes, firstArray.length, secondArray.length);
        return bytes;
    }

    //酒测数据校验和
    public static Boolean checkHexSum(String data,String tail){
        //7E01 340002004C 82

        int checksum = 0;
        for (int i = 0; i < data.length(); i += 2) {
            String hex = data.substring(i, i + 2);
            int value = Integer.parseInt(hex, 16);
            checksum += value;
        }
        if (checksum > 0xFF) {
            checksum = (checksum + 1) & 0xFF;
        }

        if(tail.equals(Integer.toHexString(checksum))){
            return true;
        }else{
            return false;
        }
    }


    // 刷卡数据校验字
    public static String cardCheckSum(String hex){
        // 031002
        byte[] data = hexToByte(hex);

        byte checksum = 0;

        for (byte b : data) {
            checksum = (byte)(checksum ^ b);//逐字节异或
        }
        String checksumStr = byteToHex(checksum);

        return hex+checksumStr;

    }

    public static Integer hexToInt(String data){
        int sum = 0;
        for (int i = 0; i < data.length(); i += 2) {
            String hex = data.substring(i, i + 2);
            int value = Integer.parseInt(hex, 16);
            sum += value;
        }
        if (sum > 0xFF) {
            sum = (sum + 1) & 0xFF;
        }
        return sum;
    }

    public static String byteToHex(byte b){
        StringBuffer sbf = new StringBuffer();

        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        sbf.append(hex.toUpperCase() + "  ");

        return sbf.toString().trim();
    }

}
