package com.ruoyi.xkt.dto.storeProdSvc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品当前颜色")
@Data
public class StoreProdSvcDTO {

    @ApiModelProperty(value = "大小码及定制款可退")
    private Integer customRefund;
    @ApiModelProperty(value = "30天包退")
    private Integer thirtyDayRefund;
    @ApiModelProperty(value = "一件起批")
    private Integer oneBatchSale;
    @ApiModelProperty(value = "退款72小时到账")
    private Integer refundWithinThreeDay;

}
