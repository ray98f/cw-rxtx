package com.rxtx.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zx
 */
@Data
@ApiModel
public class SystemUserResDTO {

    @ApiModelProperty(value = "userid")
    private Integer userId;

    @ApiModelProperty(value = "名字")
    private String userViewName;

    @ApiModelProperty(value = "账号")
    private String userName;

    @ApiModelProperty(value = "密码")
    private String userPassword;





}
