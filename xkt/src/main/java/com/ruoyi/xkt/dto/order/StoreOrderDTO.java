package com.ruoyi.xkt.dto.order;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 代发订单
 *
 * @author liangyq
 * @date 2025-04-01 11:57:52.566
 **/
@Data
public class StoreOrderDTO {
    /**
     * 订单ID
     */
    private Long id;
    /**
     * 档口ID
     */
    private Long storeId;
    /**
     * 下单用户ID
     */
    private Long orderUserId;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 订单类型[1:销售订单 2:退货订单]
     */
    private Integer orderType;
    /**
     * 订单状态（1开头为销售订单状态，2开头为退货订单状态）[10:已取消 11:待付款 12:待发货 13:已发货 14:已完成 21:售后中 22:售后拒绝 23:平台介入 24:售后完成]
     */
    private Integer orderStatus;
    /**
     * 支付状态[1:初始 2:支付中 3:已支付]
     */
    private Integer payStatus;
    /**
     * 订单备注
     */
    private String orderRemark;
    /**
     * 商品数量
     */
    private Integer goodsQuantity;
    /**
     * 商品金额
     */
    private BigDecimal goodsAmount;
    /**
     * 快递费
     */
    private BigDecimal expressFee;
    /**
     * 总金额（商品金额+快递费）
     */
    private BigDecimal totalAmount;
    /**
     * 实际总金额（总金额-支付渠道服务费）
     */
    private BigDecimal realTotalAmount;
    /**
     * 退货原订单ID
     */
    private Long refundOrderId;
    /**
     * 退货原因
     */
    private Integer refundReasonCode;
    /**
     * 退货拒绝原因
     */
    private String refundRejectReason;
    /**
     * 物流ID
     */
    private Long expressId;
    /**
     * 发货人-名称
     */
    private String originContactName;
    /**
     * 发货人-电话
     */
    private String originContactPhoneNumber;
    /**
     * 发货人-省编码
     */
    private String originProvinceCode;
    /**
     * 发货人-市编码
     */
    private String originCityCode;
    /**
     * 发货人-区县编码
     */
    private String originCountyCode;
    /**
     * 发货人-详细地址
     */
    private String originDetailAddress;
    /**
     * 收货人-名称
     */
    private String destinationContactName;
    /**
     * 收货人-电话
     */
    private String destinationContactPhoneNumber;
    /**
     * 收货人-省编码
     */
    private String destinationProvinceCode;
    /**
     * 收货人-市编码
     */
    private String destinationCityCode;
    /**
     * 收货人-区县编码
     */
    private String destinationCountyCode;
    /**
     * 收货人-详细地址
     */
    private String destinationDetailAddress;
    /**
     * 发货方式[1:货其再发 2:有货先发]
     */
    private Integer deliveryType;
    /**
     * 最晚发货时间
     */
    private Date deliveryEndTime;
    /**
     * 自动完成时间
     */
    private Date autoEndTime;
    /**
     * 凭证日期
     */
    private Date voucherDate;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 版本号
     */
    private Long version;
}
