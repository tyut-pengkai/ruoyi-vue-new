package com.ruoyi.xkt.dto.store;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
public class StoreIndexSaleTop10ResDTO {

    @ApiModelProperty(value = "商品ID")
    private Long storeProdId;
    @ApiModelProperty(value = "主图")
    private String mainPic;
    @ApiModelProperty(value = "货号")
    private String prodArtNum;
    @ApiModelProperty(value = "销售出库金额")
    private BigDecimal saleAmount;
    @ApiModelProperty(value = "销售退货金额")
    private BigDecimal refundAmount;
    @ApiModelProperty(value = "累计销量")
    private Integer saleNum;
    @ApiModelProperty(value = "累计退货量")
    private Integer refundNum;
    @ApiModelProperty(value = "退货率")
    private BigDecimal refundRate;

}
