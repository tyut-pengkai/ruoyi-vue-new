package com.ruoyi.xkt.dto.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口首页商品销量前10")
@Data
public class StoreIndexCusSaleTop10ResDTO {

    @ApiModelProperty(value = "档口客户名称")
    private String storeCusName;
    @ApiModelProperty(value = "销售出库金额")
    private BigDecimal saleAmount;
    @ApiModelProperty(value = "销售退货金额")
    private BigDecimal refundAmount;
    @ApiModelProperty(value = "累计销量")
    private Integer saleNum;
    @ApiModelProperty(value = "累计退货量")
    private Integer refundNum;

}
