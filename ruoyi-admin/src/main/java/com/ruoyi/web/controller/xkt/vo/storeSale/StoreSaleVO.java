package com.ruoyi.web.controller.xkt.vo.storeSale;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class StoreSaleVO {

    @NotNull(message = "是否为返单不能为空!")
    @ApiModelProperty(name = "是否为返单", notes = "true 是返单  false 不是返单")
    private Boolean refund;
    @ApiModelProperty(name = "storeSaleId", notes = "新增为空，编辑必传")
    private Long storeSaleId;
    @ApiModelProperty(name = "档口ID")
    @NotNull(message = "档口ID不能为空!")
    private Long storeId;
    @ApiModelProperty(name = "档口客户ID")
    @NotNull(message = "档口客户ID不能为空!")
    private Long storeCusId;
    @ApiModelProperty(name = "档口客户名称")
    @NotBlank(message = "档口客户名称不能为空!")
    private String storeCusName;
    @ApiModelProperty(name = "销售类型（销售 1、退货 2、销售/退货 3）")
    @NotBlank(message = "销售类型不能为空!")
    private Integer saleType;
    @NotBlank(message = "支付方式不能为空!")
    @ApiModelProperty(name = "支付方式（支付宝、微信、现金、欠款）ALIPAY WECHAT_PAY CASH DEBT")
    private Integer payWay;
    @ApiModelProperty(name = "结款状态（已结清、欠款） SETTLED、DEBT")
    private Integer paymentStatus;
    @NotNull(message = "销售详情列表不能为空!")
    @Valid
    @ApiModelProperty(name = "销售详情列表")
    private List<SaleDetailVO> detailList;


    @Data
    public static class SaleDetailVO {
        @NotNull(message = "档口商品ID不能为空!")
        @ApiModelProperty(name = "档口商品ID")
        private Long storeProdId;
        @NotNull(message = "档口商品颜色尺码ID不能为空!")
        @ApiModelProperty(name = "档口商品颜色尺码ID")
        private Long storeProdColorId;
        @ApiModelProperty(name = "颜色")
        private String colorName;
        @ApiModelProperty(name = "尺码")
        private Integer size;
        @ApiModelProperty(name = "商品货号")
        @NotBlank(message = "商品货号不能为空!")
        private String prodArtNum;
        @ApiModelProperty(name = "销售条码")
        private String sns;
        @NotNull(message = "销售单价不能为空!")
        @ApiModelProperty(name = "销售单价")
        private BigDecimal price;
        @ApiModelProperty(name = "给客户优惠后单价")
        private BigDecimal discountedPrice;
        @ApiModelProperty(name = "数量")
        @NotNull(message = "数量不能为空!")
        private Integer quantity;
        @NotNull(message = "总金额不能为空!")
        @ApiModelProperty(name = "总金额")
        private BigDecimal amount;
        @ApiModelProperty(name = "其它优惠")
        private BigDecimal otherDiscount;
    }

}
