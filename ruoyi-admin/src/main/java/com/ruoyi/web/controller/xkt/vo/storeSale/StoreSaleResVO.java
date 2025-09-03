package com.ruoyi.web.controller.xkt.vo.storeSale;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel

public class StoreSaleResVO {

    @ApiModelProperty(value = "是否为返单:true 是返单  false 不是返单")
    private Boolean refund;
    @ApiModelProperty(value = "storeSaleId")
    private Long storeSaleId;
    @ApiModelProperty(value = "单据编号")
    private String code;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口客户ID")
    private Long storeCusId;
    @ApiModelProperty(value = "档口客户名称")
    private String storeCusName;
    @ApiModelProperty(value = "销售类型（销售 1、退货 2、销售/退货 3）")
    private Integer saleType;
    @ApiModelProperty(value = "支付方式（1支付宝、2微信、3现金、4欠款）")
    private Integer payWay;
    @ApiModelProperty(value = "结款状态（1 已结清、2 欠款）")
    private Integer paymentStatus;
    @ApiModelProperty(value = "抹零金额")
    private BigDecimal roundOff;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "销售时间")
    private Date createTime;
    @ApiModelProperty(value = "销售数量")
    private Integer saleQuantity;
    @ApiModelProperty(value = "退货数量")
    private Integer refundQuantity;
    @ApiModelProperty(value = "销售金额")
    private BigDecimal amount;
    @ApiModelProperty(value = "销售详情列表")
    private List<SaleDetailVO> detailList;

    @Data
    @ApiModel
    public static class SaleDetailVO {
        @ApiModelProperty(value = "档口商品ID")
        private Long storeProdId;
        @ApiModelProperty(value = "档口商品颜色尺码ID")
        private Long storeProdColorId;
        @ApiModelProperty(value = "销售类型（销售 1、退货 2）")
        private Integer saleType;
        @ApiModelProperty(value = "颜色")
        private String colorName;
        @ApiModelProperty(value = "尺码")
        private Integer size;
        @ApiModelProperty(value = "大小码加价 0 不加 1加价")
        private Integer addOverPrice;
        @ApiModelProperty(value = "商品货号")
        private String prodArtNum;
        @ApiModelProperty(value = "销售条码")
        private String sn;
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
