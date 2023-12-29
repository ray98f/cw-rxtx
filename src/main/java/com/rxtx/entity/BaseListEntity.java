package com.rxtx.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author frp
 */
@Data
public class BaseListEntity {

    @NotNull(message = "32000006")
    private List<String> ids;
}
