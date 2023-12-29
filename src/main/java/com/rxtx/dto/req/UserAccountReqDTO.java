package com.rxtx.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author frp
 */
@Data
@ApiModel
public class UserAccountReqDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "账号")
    private String account;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "工号")
    private String no;

    @ApiModelProperty(value = "是否锁定 0 未锁定 1 已锁定")
    private Integer isLock;

    @ApiModelProperty(value = "锁定时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date lockTime;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "用户手机号")
    private String mobile;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty(value = "邮箱")
    private String email;

}
