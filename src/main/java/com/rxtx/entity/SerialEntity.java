package com.rxtx.entity;

import lombok.Data;

/**
 * description:
 *
 * @author zhangxin
 * @version 1.0
 * @date 2023/8/8 13:58
 */
@Data
public class SerialEntity {

    private String portId;
    private int bitRate;
    private int dataBit;
    private int stopBit;
    private String checkBit;
    private String format;

}
