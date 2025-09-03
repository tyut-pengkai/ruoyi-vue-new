package com.ruoyi.xkt.dto.storeProductDemand;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data

public class StoreProdDemandStatusCountResDTO {

    @ApiModelProperty(value = "待生产数量")
    private Integer unProductionNum;
    @ApiModelProperty(value = "生产中数量")
    private Integer inProductionNum;
    @ApiModelProperty(value = "生产完成数量")
    private Integer productionCompleteNum;

}
