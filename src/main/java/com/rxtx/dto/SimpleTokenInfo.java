package com.rxtx.dto;

import lombok.Data;

/**
 * @author frp
 */
@Data
public class SimpleTokenInfo {
    private String personNo;
    private String personName;


    public SimpleTokenInfo(){}

    public SimpleTokenInfo(String personNo, String personName) {
        this.personNo = personNo;
        this.personName = personName;

    }
}
