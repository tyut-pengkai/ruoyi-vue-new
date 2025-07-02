package com.ruoyi.xkt.dto.storeProductDemand;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
public class StoreProdDemandWorkingDTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口需求明细ID")
    private List<Long> storeProdDemandDetailIdList;
    @ApiModelProperty(value = "是否导出生产单")
    private Boolean download;

}
