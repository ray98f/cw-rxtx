package com.rxtx.enums;

/**
 * token状态
 *
 * @author frp
 */
public enum TokenStatus {
    /**
     * 过期的
     */
    EXPIRED("EXPIRED"),
    /**
     * 无效的
     */
    INVALID("INVALID"),
    /**
     * 有效的
     */
    VALID("VALID");

    private final String status;

    TokenStatus(String status) {
        this.status = status;
    }

    public String value() {
        return this.status;
    }
}