package com.ruoyi.xkt.dto.storeProdColorSize;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data

public class StoreSaleSnDTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口客户ID")
    private Long storeCusId;
    @ApiModelProperty(value = "档口客户名称")
    private String storeCusName;
    @ApiModelProperty(value = "是否退货")
    private Boolean refund;
    @ApiModelProperty(value = "条码")
    private String sn;
    @ApiModelProperty(value = "条码前缀")
    private String snPrefix;

}
