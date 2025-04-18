package com.ruoyi.web.controller.xkt.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liangyq
 * @date 2025-04-18 14:36
 */
@ApiModel
@Data
public class StoreOrderPrintRespVO {

    @ApiModelProperty(value = "物流单号")
    private String expressWaybillNo;
    
    @ApiModelProperty(value = "文件流Base64编码")
    private String result;

}
