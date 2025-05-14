package com.ruoyi.web.controller.xkt.vo.order;

import com.ruoyi.xkt.dto.order.StoreOrderDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-14 11:18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StoreOrderPageItemVO extends StoreOrderDTO {
    /**
     * 订单ID
     */
    @ApiModelProperty(value = "订单ID")
    private Long id;
    /**
     * 档口ID
     */
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    /**
     * 档口名称
     */
    @ApiModelProperty(value = "档口名称")
    private String storeName;
    /**
     * 品牌名称
     */
    @ApiModelProperty(value = "品牌名称")
    private String brandName;
    /**
     * 下单用户ID
     */
    @ApiModelProperty(value = "下单用户ID")
    private Long orderUserId;
    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderNo;
    /**
     * 订单类型[1:销售订单 2:退货订单]
     */
    @ApiModelProperty(value = "订单类型[1:销售订单 2:退货订单]")
    private Integer orderType;
    /**
     * 订单状态（1开头为销售订单状态，2开头为退货订单状态）[10:已取消 11:待付款 12:待发货 13:已发货 14:已完成 21:售后中 22:售后拒绝 23:平台介入 24:售后完成]
     */
    @ApiModelProperty(value = "订单状态（1开头为销售订单状态，2开头为退货订单状态）[10:已取消 11:待付款 12:待发货 13:已发货 14:已完成 21:售后中 22:售后拒绝 23:平台介入 24:售后完成]")
    private Integer orderStatus;
    /**
     * 支付状态[1:初始 2:支付中 3:已支付]
     */
    @ApiModelProperty(value = "支付状态[1:初始 2:支付中 3:已支付]")
    private Integer payStatus;
    /**
     * 支付渠道[1:支付宝]
     */
    @ApiModelProperty(value = "支付渠道[1:支付宝]")
    private Integer payChannel;
    /**
     * 订单备注
     */
    @ApiModelProperty(value = "订单备注")
    private String orderRemark;
    /**
     * 商品数量
     */
    @ApiModelProperty(value = "商品数量")
    private Integer goodsQuantity;
    /**
     * 商品金额
     */
    @ApiModelProperty(value = "商品金额")
    private BigDecimal goodsAmount;
    /**
     * 快递费
     */
    @ApiModelProperty(value = "快递费")
    private BigDecimal expressFee;
    /**
     * 总金额（商品金额+快递费）
     */
    @ApiModelProperty(value = "总金额")
    private BigDecimal totalAmount;
    /**
     * 实际总金额（总金额-支付渠道服务费）
     */
    @ApiModelProperty(value = "实际总金额")
    private BigDecimal realTotalAmount;
    /**
     * 退货原订单ID
     */
    @ApiModelProperty(value = "退货原订单ID")
    private Long originOrderId;
    /**
     * 退货原因
     */
    @ApiModelProperty(value = "退货原因")
    private String refundReasonCode;
    /**
     * 退货拒绝原因
     */
    @ApiModelProperty(value = "退货拒绝原因")
    private String refundRejectReason;
    /**
     * 物流ID
     */
    @ApiModelProperty(value = "物流ID")
    private Long expressId;
    /**
     * 发货人-名称
     */
    @ApiModelProperty(value = "发货人-名称")
    private String originContactName;
    /**
     * 发货人-电话
     */
    @ApiModelProperty(value = "发货人-电话")
    private String originContactPhoneNumber;
    /**
     * 发货人-省编码
     */
    @ApiModelProperty(value = "发货人-省编码")
    private String originProvinceCode;
    /**
     * 发货人-市编码
     */
    @ApiModelProperty(value = "发货人-市编码")
    private String originCityCode;
    /**
     * 发货人-区县编码
     */
    @ApiModelProperty(value = "发货人-区县编码")
    private String originCountyCode;
    /**
     * 发货人-详细地址
     */
    @ApiModelProperty(value = "发货人-详细地址")
    private String originDetailAddress;
    /**
     * 收货人-名称
     */
    @ApiModelProperty(value = "收货人-名称")
    private String destinationContactName;
    /**
     * 收货人-电话
     */
    @ApiModelProperty(value = "收货人-电话")
    private String destinationContactPhoneNumber;
    /**
     * 收货人-省编码
     */
    @ApiModelProperty(value = "收货人-省编码")
    private String destinationProvinceCode;
    /**
     * 收货人-市编码
     */
    @ApiModelProperty(value = "收货人-市编码")
    private String destinationCityCode;
    /**
     * 收货人-区县编码
     */
    @ApiModelProperty(value = "收货人-区县编码")
    private String destinationCountyCode;
    /**
     * 收货人-详细地址
     */
    @ApiModelProperty(value = "收货人-详细地址")
    private String destinationDetailAddress;
    /**
     * 发货方式[1:货其再发 2:有货先发]
     */
    @ApiModelProperty(value = "发货方式[1:货其再发 2:有货先发]")
    private Integer deliveryType;
    /**
     * 最晚发货时间
     */
    @ApiModelProperty(value = "最晚发货时间")
    private Date deliveryEndTime;
    /**
     * 自动完成时间
     */
    @ApiModelProperty(value = "自动完成时间")
    private Date autoEndTime;
    /**
     * 凭证日期
     */
    @ApiModelProperty(value = "凭证日期")
    private Date voucherDate;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）")
    private String delFlag;
    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private String createBy;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 更新者
     */
    @ApiModelProperty(value = "更新者")
    private String updateBy;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "快递名")
    private String expressName;

    @ApiModelProperty(value = "发货人-省名称")
    private String originProvinceName;

    @ApiModelProperty(value = "发货人-市名称")
    private String originCityName;

    @ApiModelProperty(value = "发货人-区县名称")
    private String originCountyName;

    @ApiModelProperty(value = "收货人-省名称")
    private String destinationProvinceName;

    @ApiModelProperty(value = "收货人-市名称")
    private String destinationCityName;

    @ApiModelProperty(value = "收货人-区县名称")
    private String destinationCountyName;

    @ApiModelProperty(value = "订单明细")
    private List<Detail> orderDetails;

    @ApiModel
    @Data
    public static class Detail {
        /**
         * 预览图url
         */
        @ApiModelProperty(value = "预览图url")
        private String firstMainPicUrl;
        /**
         * 订单明细ID
         */
        @ApiModelProperty(value = "订单明细ID")
        private Long id;
        /**
         * 订单ID
         */
        @ApiModelProperty(value = "订单ID")
        private Long storeOrderId;
        /**
         * 商品颜色尺码ID
         */
        @ApiModelProperty(value = "商品颜色尺码ID")
        private Long storeProdColorSizeId;
        /**
         * 商品ID
         */
        @ApiModelProperty(value = "商品ID")
        private Long storeProdId;
        /**
         * 订单明细状态（同订单状态）[10:已取消 11:待付款 12:待发货 13:已发货 14:已完成 21:售后中 22:售后拒绝 23:平台介入 24:售后完成]
         */
        @ApiModelProperty(value = "订单明细状态（同订单状态）[10:已取消 11:待付款 12:待发货 13:已发货 14:已完成 21:售后中 22:售后拒绝 23:平台介入 24:售后完成]")
        private Integer detailStatus;
        /**
         * 支付状态[1:初始 2:支付中 3:已支付]
         */
        @ApiModelProperty(value = "支付状态[1:初始 2:支付中 3:已支付]")
        private Integer payStatus;
        /**
         * 物流ID
         */
        @ApiModelProperty(value = "物流ID")
        private Long expressId;
        /**
         * 物流类型[1:平台物流 2:档口物流]
         */
        @ApiModelProperty(value = "物流类型[1:平台物流 2:档口物流]")
        private Integer expressType;
        /**
         * 物流状态[1:初始 2:下单中 3:已下单 4:取消中 5:已揽件 6:拦截中 99:已结束]
         */
        @ApiModelProperty(value = "物流状态[1:初始 2:下单中 3:已下单 4:取消中 5:已揽件 6:拦截中 99:已结束]")
        private Integer expressStatus;
        /**
         * 物流请求单号
         */
        @ApiModelProperty(value = "物流请求单号")
        private String expressReqNo;
        /**
         * 物流运单号（快递单号），档口/用户自己填写时可能存在多个，使用“,”分割
         */
        @ApiModelProperty(value = "物流运单号（快递单号），档口/用户自己填写时可能存在多个，使用“,”分割")
        private String expressWaybillNo;
        /**
         * 商品单价
         */
        @ApiModelProperty(value = "商品单价")
        private BigDecimal goodsPrice;
        /**
         * 商品数量
         */
        @ApiModelProperty(value = "商品数量")
        private Integer goodsQuantity;
        /**
         * 商品金额（商品单价*商品数量）
         */
        @ApiModelProperty(value = "商品金额")
        private BigDecimal goodsAmount;
        /**
         * 快递费
         */
        @ApiModelProperty(value = "快递费")
        private BigDecimal expressFee;
        /**
         * 总金额（商品金额+快递费）
         */
        @ApiModelProperty(value = "总金额")
        private BigDecimal totalAmount;
        /**
         * 实际总金额（总金额-支付渠道服务费）
         */
        @ApiModelProperty(value = "实际总金额")
        private BigDecimal realTotalAmount;
        /**
         * 退货原订单明细ID
         */
        @ApiModelProperty(value = "退货原订单明细ID")
        private Long originOrderDetailId;
        /**
         * 退货原因
         */
        @ApiModelProperty(value = "退货原因")
        private String refundReasonCode;
        /**
         * 退货拒绝原因
         */
        @ApiModelProperty(value = "退货拒绝原因")
        private String refundRejectReason;
        /**
         * 删除标志（0代表存在 2代表删除）
         */
        @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）")
        private String delFlag;
        /**
         * 创建者
         */
        @ApiModelProperty(value = "创建者")
        private String createBy;
        /**
         * 创建时间
         */
        @ApiModelProperty(value = "创建时间")
        private Date createTime;
        /**
         * 更新者
         */
        @ApiModelProperty(value = "更新者")
        private String updateBy;
        /**
         * 更新时间
         */
        @ApiModelProperty(value = "更新时间")
        private Date updateTime;

        @ApiModelProperty(value = "档口商品名称")
        private String prodName;

        @ApiModelProperty(value = "商品货号")
        private String prodArtNum;

        @ApiModelProperty(value = "商品标题")
        private String prodTitle;

        @ApiModelProperty(value = "档口颜色ID")
        private Long storeColorId;

        @ApiModelProperty(value = "颜色名称")
        private String colorName;

        @ApiModelProperty(value = "商品尺码")
        private Integer size;

        @ApiModelProperty(value = "退货订单ID")
        private Long refundOrderId;

        @ApiModelProperty(value = "退货订单号")
        private String refundOrderNo;

        @ApiModelProperty(value = "退货订单明细ID")
        private Long refundOrderDetailId;

        @ApiModelProperty(value = "退货订单明细状态[10:已取消 11:待付款 12:待发货 13:已发货 14:已完成 21:售后中 22:售后拒绝 23:平台介入 24:售后完成]")
        private Integer refundOrderDetailStatus;

        @ApiModelProperty(value = "退货商品数量")
        private Integer refundGoodsQuantity;

        @ApiModelProperty(value = "退货原订单ID")
        private Long originOrderId;

        @ApiModelProperty(value = "退货原订单号")
        private String originOrderNo;

        @ApiModelProperty(value = "退货原订单明细状态[10:已取消 11:待付款 12:待发货 13:已发货 14:已完成 21:售后中 22:售后拒绝 23:平台介入 24:售后完成]")
        private Integer originOrderDetailStatus;

        @ApiModelProperty(value = "退货原商品数量")
        private Integer originGoodsQuantity;
    }
}
