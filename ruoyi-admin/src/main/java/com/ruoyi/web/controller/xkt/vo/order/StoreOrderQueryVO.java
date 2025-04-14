package com.ruoyi.web.controller.xkt.vo.order;

import com.ruoyi.web.controller.xkt.vo.BasePageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author liangyq
 * @date 2025-04-14 12:54
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StoreOrderQueryVO extends BasePageVO {
    /**
     * 档口ID
     */
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
//    /**
//     * 下单用户ID
//     */
//    @ApiModelProperty(value = "下单用户ID")
//    private Long orderUserId;
    /**
     * 订单号（模糊）
     */
    @ApiModelProperty(value = "订单号（模糊）")
    private String orderNo;
    /**
     * 订单状态（1开头为销售订单状态，2开头为退货订单状态）[10:已取消 11:待付款 12:待发货 13:已发货 14:已完成 21:售后中 22:售后拒绝 23:平台介入 24:售后完成]
     */
    @ApiModelProperty(value = "订单状态（1开头为销售订单状态，2开头为退货订单状态）[10:已取消 11:待付款 12:待发货 13:已发货 14:已完成 21:售后中 22:售后拒绝 23:平台介入 24:售后完成]")
    private Integer orderStatus;
    /**
     * 收货人-名称（模糊）
     */
    @ApiModelProperty(value = "收货人-名称（模糊）")
    private String destinationContactName;
    /**
     * 收货人-电话（模糊）
     */
    @ApiModelProperty(value = "收货人-电话（模糊）")
    private String destinationContactPhoneNumber;
    /**
     * 发货方式[1:货其再发 2:有货先发]
     */
    @ApiModelProperty(value = "发货方式[1:货其再发 2:有货先发]")
    private Integer deliveryType;
    /**
     * 下单开始时间
     */
    @ApiModelProperty(value = "下单开始时间")
    private Date orderTimeBegin;
    /**
     * 下单结束时间
     */
    @ApiModelProperty(value = "下单结束时间")
    private Date orderTimeEnd;
    /**
     * 支付开始时间
     */
    @ApiModelProperty(value = "支付开始时间")
    private Date payTimeBegin;
    /**
     * 支付结束时间
     */
    @ApiModelProperty(value = "支付结束时间")
    private Date payTimeEnd;

    /**
     * 物流运单号（模糊）
     */
    @ApiModelProperty(value = "物流运单号（模糊）")
    private String expressWaybillNo;
    /**
     * 商品货号（模糊）
     */
    @ApiModelProperty(value = "商品货号（模糊）")
    private String prodArtNum;

    @NotNull(message = "来源页面不能为空")
    @ApiModelProperty(value = "来源页面[1:卖家订单列表 2:档口订单列表]")
    private Integer srcPage;

}
