package com.rxtx.dto.req;

import lombok.Data;

/**
 * @author zx
 */
@Data
public class UserLoginReqDTO {

    private String username;
    private String password;
    private String fingerTpl;
    private String cardNo;
    private String loginType;
}
