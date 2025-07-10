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
@ApiModel(value = "新增或编辑销售出库")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreSaleVO {

    @NotNull(message = "是否为返单不能为空!")
    @ApiModelProperty(value = "是否为返单:true 是返单  false 不是返单", required = true)
    private Boolean refund;
    @ApiModelProperty(value = "storeSaleId", notes = "新增为空，编辑必传")
    private Long storeSaleId;
    @ApiModelProperty(value = "档口ID", required = true)
    @NotNull(message = "档口ID不能为空!")
    private Long storeId;
    @ApiModelProperty(value = "档口客户ID", required = true)
    @NotNull(message = "档口客户ID不能为空!")
    private Long storeCusId;
    @ApiModelProperty(value = "档口客户名称", required = true)
    @NotBlank(message = "档口客户名称不能为空!")
    private String storeCusName;
    @ApiModelProperty(value = "销售类型（销售 1、退货 2、销售/退货 3）", required = true)
    @NotNull(message = "销售类型不能为空!")
    private Integer saleType;
    @NotNull(message = "支付方式不能为空!")
    @ApiModelProperty(value = "支付方式（1支付宝、2微信、3现金、4欠款）", required = true)
    private Integer payWay;
    @ApiModelProperty(value = "结款状态 1 已结清 2 欠款")
    private Integer paymentStatus;
    @ApiModelProperty(value = "抹零金额")
    private BigDecimal roundOff;
    @ApiModelProperty(value = "备注")
    private String remark;
    @NotNull(message = "销售详情列表不能为空!")
    @Valid
    @ApiModelProperty(value = "销售详情列表", required = true)
    private List<SaleDetailVO> detailList;

    @Data
    public static class SaleDetailVO {
        @NotNull(message = "档口商品ID不能为空!")
        @ApiModelProperty(value = "档口商品ID", required = true)
        private Long storeProdId;
        @NotNull(message = "档口商品颜色尺码ID不能为空!")
        @ApiModelProperty(value = "档口商品颜色尺码ID", required = true)
        private Long storeProdColorId;
        @ApiModelProperty(value = "颜色")
        private String colorName;
        @ApiModelProperty(value = "尺码")
        private Integer size;
        @ApiModelProperty(value = "销售类型（销售 1、退货 2）", required = true)
        @NotNull(message = "销售类型不能为空!")
        private Integer saleType;
        @ApiModelProperty(value = "商品货号", required = true)
        @NotBlank(message = "商品货号不能为空!")
        private String prodArtNum;
        @ApiModelProperty(value = "销售条码")
        private String sn;
        @NotNull(message = "销售单价不能为空!")
        @ApiModelProperty(value = "销售单价", required = true)
        private BigDecimal price;
        @ApiModelProperty(value = "给客户优惠后单价")
        private BigDecimal discountedPrice;
        @ApiModelProperty(value = "数量", required = true)
        @NotNull(message = "数量不能为空!")
        private Integer quantity;
        @NotNull(message = "总金额不能为空!")
        @ApiModelProperty(value = "总金额", required = true)
        private BigDecimal amount;
        @ApiModelProperty(value = "其它优惠")
        private BigDecimal otherDiscount;
    }

}
