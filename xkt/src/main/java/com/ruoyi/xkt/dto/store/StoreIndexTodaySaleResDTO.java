package com.ruoyi.xkt.dto.store;

import io.swagger.annotations.ApiModel;
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
@ApiModel("档口首页今日销售额")
@Data
@Accessors(chain = true)
public class StoreIndexTodaySaleResDTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "其它销售额")
    private BigDecimal otherAmount;
    @ApiModelProperty(value = "档口商品销售额列表")
    List<SITSProdSaleDTO> saleList;

    @Data
    @Accessors(chain = true)
    public static class SITSProdSaleDTO {
        @ApiModelProperty(value = "货号")
        private String prodArtNum;
        @ApiModelProperty(value = "销售额")
        private BigDecimal saleAmount;
    }

}
