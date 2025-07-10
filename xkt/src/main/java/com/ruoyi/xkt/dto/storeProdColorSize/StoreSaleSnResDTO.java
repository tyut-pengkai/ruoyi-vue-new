package com.ruoyi.xkt.dto.storeProdColorSize;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreSaleSnResDTO {

    @ApiModelProperty(value = "是否成功")
    private Boolean success;
    @ApiModelProperty(value = "档口商品ID")
    private Long storeProdId;
    @ApiModelProperty(value = "档口商品颜色尺码ID")
    private Long storeProdColorId;
    @ApiModelProperty(value = "颜色")
    private String colorName;
    @ApiModelProperty(value = "尺码")
    private Integer size;
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "销售条码")
    private String sn;
    @ApiModelProperty(value = "销售单价")
    private BigDecimal price;
    @ApiModelProperty(value = "档口客户优惠金额")
    private BigDecimal discount;

}
