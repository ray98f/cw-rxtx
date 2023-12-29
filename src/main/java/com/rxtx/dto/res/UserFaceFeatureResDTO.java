package com.rxtx.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zx
 */
@Data
@ApiModel
public class UserFaceFeatureResDTO {

    @ApiModelProperty(value = "用户ID")
    private String id;

    @ApiModelProperty(value = "工号")
    private String userNo;

    @ApiModelProperty(value = "特征")
    private String faceFeature;


}
