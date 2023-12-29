package com.rxtx.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zx
 */
@Data
@ApiModel
public class TrainScheduleDTO {

    @ApiModelProperty(value = "记录ID")
    private String id;

    @ApiModelProperty(value = "车次号")
    private String trainId;

    @ApiModelProperty(value = "车站")
    private String stationName;

    @ApiModelProperty(value = "车站类型1始发站 2终点站")
    private String stationType;

    @ApiModelProperty(value = "到站时间")
    private String arrive;

    @ApiModelProperty(value = "离站时间")
    private String depart;

}
