package com.rxtx.dto.req;

import lombok.Data;

/**
 * @author zx
 */
@Data
public class SocketEventReqDTO {

    private String event;
    private String port;
    private String userNo;
}
