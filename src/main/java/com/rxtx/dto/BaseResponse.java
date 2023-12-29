package com.rxtx.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2020/12/10 15:58
 */
@ApiModel
@Data
public class BaseResponse {

    private static final BaseResponse SUCCESS;

    private static final BaseResponse AUTH_FAILED;

    private Integer code;

    private String message;

    private String url;

    public BaseResponse(AppStatus status) {
        this.code = status.getCode();
        this.message = status.getMessage();
    }

    public BaseResponse() {
    }

    public BaseResponse code(Integer code) {
        this.code = code;
        return this;
    }

    public BaseResponse message(String message) {
        this.message = message;
        return this;
    }

    public BaseResponse url(String url) {
        this.url = url;
        return this;
    }

    static {
        SUCCESS = new BaseResponse(AppStatus.SUCCESS);
        AUTH_FAILED = new BaseResponse(AppStatus.AUTH_FAILED);
    }

    protected enum AppStatus {
        /**
         *
         */
        SUCCESS(0, "success"),
        AUTH_FAILED(401, "auth failed");

        private String message;
        private Integer code;

        AppStatus(Integer code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getMessage() {
            return this.message;
        }

        public Integer getCode() {
            return this.code;
        }
    }

}
