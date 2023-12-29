package com.rxtx.utils;

/**
 * description:
 *
 * @author zhangxin
 * @version 1.0
 * @date 2023/8/8 10:58
 */
public class Crc16Modbus {

/**
 * CRC 循环冗余校验 即通过生成多项式对原始数据进行计算,将计算结果拼接到数据上一起发送
 *     接收方计算接收到的数据校验接收结果是否准确
 * CRC 即对生成多项式的模二运算
 *
 * 1.预置1个16位的寄存器为十六进制 FFFF（即全为1），称此寄存器为CRC寄存器
 * 2.把第1个8位二进制数据（帧头字节）与 CRC 寄存器的低8位相异或并写回寄存器 高8位数据不变
 * 3.把 CRC 循环右移 高位补 0 取得移出位
 * 4.如果移出位为 0 继续右移 如果移出位为 1 则 CRC 寄存器与多项式 A001（1010 0000 0000 0001）进行异或运算
 * 5.重复步骤 3 和 4 直到右移 8 次
 * 6.重复步骤 2 到 5 进行数据帧下一个字节的处理 直到将数据帧所有字节按上述步骤计算
 * 7.根据需要将寄存器的高、低字节进行交换 得到最终 CRC码
 *
 */

    /**
     * 初始值 CRC-16 寄存器
     */
    private static final int INITIAL_VALUE = 0xFFFF;
    private static final boolean IS_OUT_PUT_OVER_TURN = true;

    /**
     * 原始数据 + CRC码
     *
     * @param hexes 16 进制字符串
     * @return
     */
    public static byte[] getData(String... hexes) {
        byte[] data = new byte[hexes.length];
        int i = 0;
        for (String hex:hexes){
            //先转为数字在转为 byte
            data[i++] = (byte) Integer.parseInt(hex, 16);
        }
        return merge(data);
    }

    /**
     * 原始数据 + CRC码
     *
     * @param data
     * @return
     */
    public static byte[] merge(byte[] data) {
        byte[] crc = getCrc16(data);
        int dLen = data.length;
        int cLen = crc.length;
        byte[] result = new byte[dLen + cLen];
        System.arraycopy(data,0,result,0,dLen);
        System.arraycopy(crc,0,result,dLen,cLen);
        return result;
    }

    /**
     * 基于 CRC16 Modbus 计算校验码
     * CRC 16 Modbus 默认多项式为 x16+x15+x2+1 => 8005 反转即 A001
     *
     * @param data
     * @return
     */
    private static byte[] getCrc16(byte[] data) {
        int len = data.length;
        int crc = INITIAL_VALUE;
        int i, j;
        for (i = 0; i < len; i++) {
            // 把第一个 8 位二进制数据 与 16 位的 CRC寄存器的低 8 位相异或, 把结果放于 CRC寄存器
            crc = ((crc & 0xFF00) | (crc & 0x00FF) ^ (data[i] & 0xFF));
            for (j = 0; j < 8; j++) {
                // 把 CRC 寄存器的内容右移一位(朝低位)用 0 填补最高位, 并检查右移后的移出位
                if ((crc & 0x0001) > 0) {
                    // 如果移出位为 1, CRC寄存器与多项式A001进行异或
                    crc = crc >> 1;
                    crc = crc ^ 0xA001;
                } else {
                    // 如果移出位为 0,再次右移一位
                    crc = crc >> 1;
                }
            }
        }
        return intToBytes(crc);
    }

    /**
     * 将 int 转换成 byte 数组 低位在前 高位在后
     */
    private static byte[] intToBytes(int value)  {
        byte[] src = new byte[2];
        byte hig = (byte) ((value>>8) & 0xFF);
        byte low = (byte) (value & 0xFF);
        if (IS_OUT_PUT_OVER_TURN){
            src[0] = low;
            src[1] = hig;
        } else {
            src[0] = hig;
            src[1] = low;
        }
        return src;
    }

    /**
     * 将字节数组转换成十六进制字符串
     */
    public static String byteTo16String(byte[] data) {
        StringBuffer buffer = new StringBuffer();
        for (byte b : data) {
            byteToHex(buffer,b);
        }
        return buffer.toString().toUpperCase();
    }

    /**
     * 将字节转换成十六进制字符串
     *
     * int 转 byte 对照表
     * [128,255],0,[1,128)
     * [-128,-1],0,[1,128)
     */
    public static void byteToHex(StringBuffer buffer ,byte b) {
        if (b < 0) {
            buffer.append(Integer.toString(b + 256, 16));
        } else if (b == 0) {
            buffer.append("00 ");
        } else if (b > 0 && b <= 15) {
            buffer.append("0" + Integer.toString(b, 16));
        } else if (b > 15) {
            buffer.append(Integer.toString(b, 16));
        }
        buffer.append(" ");
    }

}
