package com.ruoyi.web.controller.xkt.vo.express;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 物流信息
 *
 * @author liangyq
 * @date 2025-04-01 11:57:52.434
 **/
@ApiModel
@Data
public class ExpressFeeReqVO {

    @ApiModelProperty(value = "商品数量")
    private Integer goodsQuantity;

    @ApiModelProperty(value = "省编码")
    private String provinceCode;

    @ApiModelProperty(value = "市编码")
    private String cityCode;

    @ApiModelProperty(value = "区县编码")
    private String countyCode;
}
