package com.ruoyi.web.controller.xkt.vo.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口首页今日销售额")
@Data
public class StoreIndexTodaySaleResVO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "其它销售额")
    private BigDecimal otherAmount;
    @ApiModelProperty(value = "档口商品销售额列表")
    List<SITSProdSaleVO> saleList;

    @Data
    public static class SITSProdSaleVO {
        @ApiModelProperty(value = "货号")
        private String prodArtNum;
        @ApiModelProperty(value = "销售额")
        private Long saleAmount;
    }

}
