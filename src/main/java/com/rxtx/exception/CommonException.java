package com.rxtx.exception;

import com.rxtx.enums.ErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2020/4/22 15:52
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommonException extends RuntimeException {

    private Integer code;

    private String message;

    private String url;

    private String[] params;

    public CommonException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public CommonException(Integer code, String message, String url) {
        this.code = code;
        this.message = message;
        this.url = url;
    }

    public CommonException(ErrorCode error) {
        this.code = error.code();
        this.message = error.message();
        this.url = error.url();
    }

    public CommonException(ErrorCode error, String... params) {
        this.code = error.code();
        this.message = error.message();
        this.params = params;
    }

}
