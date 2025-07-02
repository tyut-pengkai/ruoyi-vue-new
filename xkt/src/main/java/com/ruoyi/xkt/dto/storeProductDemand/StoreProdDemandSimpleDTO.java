package com.ruoyi.xkt.dto.storeProductDemand;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@Builder
public class StoreProdDemandSimpleDTO {

    @ApiModelProperty(value = "档口需求详情ID")
    private Long storeProdDemandDetailId;
    @ApiModelProperty(value = "档口需求code")
    private String code;

}
