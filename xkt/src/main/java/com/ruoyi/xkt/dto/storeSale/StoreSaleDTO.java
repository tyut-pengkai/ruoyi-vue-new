package com.ruoyi.xkt.dto.storeSale;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品销售")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreSaleDTO {

    @ApiModelProperty(name = "是否为返单", notes = "true 是返单  false 不是返单")
    private Boolean refund;
    @ApiModelProperty("档口销售ID")
    private Long storeSaleId;
    @ApiModelProperty(name = "档口ID")
    private Long storeId;
    @ApiModelProperty(name = "档口客户ID")
    private Long storeCusId;
    @ApiModelProperty(name = "档口客户名称")
    private String storeCusName;
    @ApiModelProperty(name = "销售类型（普通销售 GENERAL_SALE、销售退货 SALE_REFUND、普通销售/销售退货 SALE_AND_REFUND）")
    private String saleType;
    @ApiModelProperty(name = "支付方式（支付宝、微信、现金、欠款）ALIPAY WECHAT_PAY CASH DEBT")
    private String payWay;
    @ApiModelProperty(name = "结款状态（已结清、欠款） SETTLED、DEBT")
    private String paymentStatus;
    @ApiModelProperty(name = "销售详情列表")
    private List<SaleDetailVO> detailList;

    @Data
    public static class SaleDetailVO {
        @ApiModelProperty(name = "档口商品ID")
        private Long storeProdId;
        @ApiModelProperty(name = "档口商品颜色ID")
        private Long storeProdColorId;
        @ApiModelProperty(name = "颜色")
        private String colorName;
        @ApiModelProperty(name = "尺码")
        private Integer size;
        @ApiModelProperty(name = "商品货号")
        private String prodArtNum;
        @ApiModelProperty(name = "销售条码")
        private String sns;
        @ApiModelProperty(name = "销售单价")
        private BigDecimal price;
        @ApiModelProperty(name = "给客户优惠后单价")
        private BigDecimal discountedPrice;
        @ApiModelProperty(name = "数量")
        private Integer quantity;
        @ApiModelProperty(name = "总金额")
        private BigDecimal amount;
        @ApiModelProperty(name = "其它优惠")
        private BigDecimal otherDiscount;
    }

}
