package com.ruoyi.xkt.dto.order;

import com.ruoyi.xkt.dto.BasePageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * @author liangyq
 * @date 2025-04-14 12:54
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StoreOrderQueryDTO extends BasePageDTO {
    /**
     * 档口ID
     */
    private Long storeId;
    /**
     * 下单用户ID
     */
    private Long orderUserId;
    /**
     * 订单号（模糊）
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
     * 收货人-名称（模糊）
     */
    private String destinationContactName;
    /**
     * 收货人-电话（模糊）
     */
    private String destinationContactPhoneNumber;
    /**
     * 发货方式[1:货其再发 2:有货先发]
     */
    private Integer deliveryType;
    /**
     * 下单开始时间
     */
    private Date orderTimeBegin;
    /**
     * 下单结束时间
     */
    private Date orderTimeEnd;
    /**
     * 支付开始时间
     */
    private Date payTimeBegin;
    /**
     * 支付结束时间
     */
    private Date payTimeEnd;

    /**
     * 物流运单号（模糊）
     */
    private String expressWaybillNo;
    /**
     * 商品货号（模糊）
     */
    private String prodArtNum;


}
