package com.ruoyi.xkt.dto.storeProductDemand;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品生产需求单")
@Data
@Builder
public class StoreProdDemandSimpleDTO {

    @ApiModelProperty(name = "档口需求详情ID")
    private Long storeProdDemandDetailId;
    @ApiModelProperty(name = "档口需求code")
    private String code;

}
