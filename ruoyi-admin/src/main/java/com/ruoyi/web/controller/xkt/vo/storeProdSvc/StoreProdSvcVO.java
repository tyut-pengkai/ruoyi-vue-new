package com.ruoyi.web.controller.xkt.vo.storeProdSvc;

import com.ruoyi.common.annotation.Excel;
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
public class StoreProdSvcVO {

    @ApiModelProperty(name = "大小码及定制款可退")
    private String customRefund;
    @ApiModelProperty(name = "30天包退")
    private String thirtyDayRefund;
    @ApiModelProperty(name = "一件起批")
    private String oneBatchSale;
    @ApiModelProperty(name = "退款72小时到账")
    private String refundWithinThreeDay;

}
