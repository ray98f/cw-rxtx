package com.rxtx.entity;

import com.rxtx.constant.ValidationGroup;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2020/12/15 8:54
 */
@Data
public class BaseEntity {

    @NotNull(message = "32000006",groups = {ValidationGroup.update.class})
    private String id;


}
