package com.ruoyi.web.controller.xkt.vo.storeSaleDetail;

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
@ApiModel
@Data
public class StoreTodaySaleSummaryVO {

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
    private Integer cusQuantity;
    @ApiModelProperty(value = "销售数据")
    List<STSSProdSaleVO> prodSaleList;
    @ApiModelProperty(value = "客户数据")
    List<STSSCusSaleVO> cusSaleList;


    @Data
    @ApiModel
    public static class STSSProdSaleVO {

        private Integer cusQuantity;

    }

    @Data
    @ApiModel
    public static class STSSCusSaleVO {

        private Integer cusQuantity;

    }



}
