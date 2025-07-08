package com.ruoyi.xkt.dto.storeProductDemand;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
public class StoreProdDemandFinishAllDTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口需求明细ID")
    private List<Long> storeProdDemandDetailIdList;
    @ApiModelProperty(value = "档口需求ID列表")
    private List<Long> storeProdDemandIdList;

}
