package com.rxtx.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * description:
 *
 * @author zhangxin
 * @version 1.0
 * @date 2021/8/02 14:41
 */
@Data
public class TPerson implements Serializable {

    private String id;

    private String no;

    private String name;

}
