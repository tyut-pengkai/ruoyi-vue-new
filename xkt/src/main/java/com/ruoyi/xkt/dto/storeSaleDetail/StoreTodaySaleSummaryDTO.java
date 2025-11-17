package com.ruoyi.xkt.dto.storeSaleDetail;

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
@ApiModel
@Data
@Accessors(chain = true)
public class StoreTodaySaleSummaryDTO {

    @ApiModelProperty(value = "销售金额")
    private BigDecimal saleAmount;
    @ApiModelProperty(value = "退货金额")
    private BigDecimal refundAmount;
    @ApiModelProperty(value = "销售数量")
    private Integer saleQuantity;
    @ApiModelProperty(value = "退货数量")
    private Integer refundQuantity;
    @ApiModelProperty(value = "退货率")
    private String refundRate;
    @ApiModelProperty(value = "采购客户数量")
    private Long cusQuantity;
    @ApiModelProperty(value = "销售数据")
    List<STSSProdSaleDTO> prodSaleList;
    @ApiModelProperty(value = "客户数据")
    List<STSSCusSaleDTO> cusSaleList;


    @Data
    @ApiModel
    @Accessors(chain = true)
    public static class STSSProdSaleDTO {
        @ApiModelProperty(value = "货号")
        private String prodArtNum;
        @ApiModelProperty(value = "颜色")
        private String colorName;
        @ApiModelProperty(value = "颜色销售数量")
        private Integer colorSaleQuantity;
        @ApiModelProperty(value = "颜色销售金额")
        private BigDecimal colorSaleAmount;
        @ApiModelProperty(value = "颜色退货数量")
        private Integer colorRefundQuantity;
        @ApiModelProperty(value = "颜色退货金额")
        private BigDecimal colorRefundAmount;
        @ApiModelProperty(value = "颜色总的销售数量")
        private Integer colorTotalQuantity;
        @ApiModelProperty(value = "颜色总销售金额")
        private BigDecimal colorTotalAmount;
        @ApiModelProperty(value = "销售金额")
        private BigDecimal saleAmount;
        @ApiModelProperty(value = "退货金额")
        private BigDecimal refundAmount;
        @ApiModelProperty(value = "销售数量")
        private Integer saleQuantity;
        @ApiModelProperty(value = "退货数量")
        private Integer refundQuantity;
        @ApiModelProperty(value = "退货率")
        private String refundRate;
    }

    @Data
    @ApiModel
    @Accessors(chain = true)
    public static class STSSCusSaleDTO {
        @ApiModelProperty(value = "客户名称")
        private String storeCusName;
        @ApiModelProperty(value = "销售金额")
        private BigDecimal saleAmount;
        @ApiModelProperty(value = "退货金额")
        private BigDecimal refundAmount;
        @ApiModelProperty(value = "销售数量")
        private Integer saleQuantity;
        @ApiModelProperty(value = "退货数量")
        private Integer refundQuantity;
        @ApiModelProperty(value = "合计销售金额")
        private BigDecimal totalAmount;
        @ApiModelProperty(value = "合计销售数量")
        private Integer totalQuantity;
        @ApiModelProperty(value = "退货率")
        private String refundRate;
    }


}
