package com.ruoyi.xkt.dto.storeSale;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreSaleDTO {

    @ApiModelProperty(value = "是否为返单", notes = "true 是返单  false 不是返单")
    private Boolean refund;
    @ApiModelProperty("档口销售ID")
    private Long storeSaleId;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口客户ID")
    private Long storeCusId;
    @ApiModelProperty(value = "档口客户名称")
    private String storeCusName;
    @ApiModelProperty(value = "销售类型（销售 1、退货 2、销售/退货 3）")
    private Integer saleType;
    @ApiModelProperty(value = "支付方式（支付宝、微信、现金、欠款）1ALIPAY 2WECHAT_PAY 3CASH 4DEBT")
    private Integer payWay;
    @ApiModelProperty(value = "结款状态（已结清、欠款） 1、2")
    private Integer paymentStatus;
    @ApiModelProperty(value = "抹零金额")
    private BigDecimal roundOff;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "销售详情列表")
    private List<SaleDetailVO> detailList;

    @Data
    public static class SaleDetailVO {
        @ApiModelProperty(value = "档口商品ID")
        private Long storeProdId;
        @ApiModelProperty(value = "档口商品颜色ID")
        private Long storeProdColorId;
        @ApiModelProperty(value = "颜色")
        private String colorName;
        @ApiModelProperty(value = "尺码")
        private Integer size;
        @ApiModelProperty(value = "商品货号")
        private String prodArtNum;
        @ApiModelProperty(value = "销售条码")
        private String sns;
        @ApiModelProperty(value = "销售单价")
        private BigDecimal price;
        @ApiModelProperty(value = "给客户优惠后单价")
        private BigDecimal discountedPrice;
        @ApiModelProperty(value = "数量")
        private Integer quantity;
        @ApiModelProperty(value = "总金额")
        private BigDecimal amount;
        @ApiModelProperty(value = "其它优惠")
        private BigDecimal otherDiscount;
    }

}
