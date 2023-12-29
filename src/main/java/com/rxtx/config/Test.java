package com.rxtx.config;

import cn.hutool.core.util.StrUtil;
import com.rxtx.constant.CommonConstants;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * description:
 *
 * @author zhangxin
 * @version 1.0
 * @date 2023/11/20 10:03
 */
public class Test {

    public static void main(String[] args){
        cardChange("20101C0FBF000820");
//        String x= "92C8";
//        int y = hexToInt2(x);
//        System.out.println(y+"");
        //02000000BBF5530A
       /* String s = "0A2002000000BBF5530A00040833";
        String s1 = s.substring(s.length()-2,s.length());
        String s2 = s.substring(0,s.length()-2);
        System.out.println(s1);
        System.out.println(s2);
        String  message ="0A2002000000BBF5530A000408";
        // 长度字
        // 数据 4、7、10字节卡片序列号 2 字节 ATQA 1字节SAK
        String lengthStr = message.substring(0, 2);
        int length = hexToInt(lengthStr);
        //命令字
        String cardX20 = message.substring(2, 4);
        //数据截取计算: 减去 长度字 命令字 占的2个字节 剩余的为数据字节 乘以2 (两位16进制数作为1字节) 加上长度字和命令字的4位
        String dataStr = message.substring(4,(length - 2) * 2 + 4);
        String dataStr1  = toStr(dataStr);
        //ATQA
        String leaveStr = message.replace(lengthStr+cardX20+dataStr,"");
        String atqaStr = leaveStr.substring(0,4);
        //SAK
        String sakStr = leaveStr.substring(4,6);

        System.out.println("length:"+length);

        System.out.println("codeStr:"+cardX20);

        System.out.println("dataStr:"+dataStr);

        System.out.println("atqaStr:"+atqaStr);

        System.out.println("sakStr:"+sakStr);

        byte[] datax = hexToByte(message);
        byte checksum1 = 0;
        for (byte b : datax) {
            checksum1 = (byte)(checksum1 ^ b);
        }

        System.out.println("Checksum1: " + byteToHex(checksum1));

        System.out.println("HEX: " + message+byteToHex(checksum1));*/

        //将每2位16进制整数组装成一个字节031001 032000
        String hex = "031001";
        checkHexSum("3400020000","82");
        byte[] data = hexToByte(hex);
        byte checksum = 0;

        for (byte b : data) {
            checksum = (byte)(checksum ^ b);
        }

        System.out.println("Checksum: " + byteToHex(checksum));

        System.out.println("HEX: " + hex+byteToHex(checksum));
    }

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

    public static String cardChange(String cardNo){
        //20 101C0FBF 000820   16
        System.out.println(cardNo);
        String excludeHead = (cardNo.substring(2,cardNo.length()));
        String atqaSakStr = (cardNo.substring(cardNo.length()-6));
        String realCardStr = excludeHead.replace(atqaSakStr,"");
        System.out.println(realCardStr);
        return realCardStr;
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
    public static String toStr(String hex) {
        return new String(hexToByte(hex));
    }
    public static byte[] hexToByte(String hex){
        hex = hex.toUpperCase().replace(" ","");
        ByteArrayOutputStream bao = new ByteArrayOutputStream(hex.length() / 2);
        // 将每2位16进制整数组装成一个字节
        for (int i = 0; i < hex.length(); i += 2) {
            bao.write((CommonConstants.HEX_STRING.indexOf(hex.charAt(i)) << 4 | CommonConstants.HEX_STRING.indexOf(hex.charAt(i + 1))));
        }
        return bao.toByteArray();
    }

    public static Integer hexToInt2(String data){
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
