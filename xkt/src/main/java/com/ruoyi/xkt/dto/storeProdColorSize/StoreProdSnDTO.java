package com.ruoyi.xkt.dto.storeProdColorSize;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data

public class StoreProdSnDTO {

    @ApiModelProperty(value = "档口ID")
    private String storeId;
    @ApiModelProperty(value = "条码列表")
    private List<String> snList;

}
