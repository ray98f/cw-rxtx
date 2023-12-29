package com.rxtx.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2020/10/10 15:57
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class DataResponse<T> extends com.rxtx.dto.BaseResponse implements Serializable {

    private T data;

    private DataResponse(T data) {
        super(AppStatus.SUCCESS);
        this.data = data;
    }

    public static <T> DataResponse<T> of(T data) {
        return new DataResponse<>(data);
    }

    public static <T> DataResponse<T> success() {
        return new DataResponse<T>(null);
    }

}
