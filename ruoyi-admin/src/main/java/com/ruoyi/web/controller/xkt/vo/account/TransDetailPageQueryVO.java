package com.ruoyi.web.controller.xkt.vo.account;

import com.ruoyi.web.controller.xkt.vo.BasePageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * @author liangyq
 * @date 2025-04-28 18:59
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TransDetailPageQueryVO extends BasePageVO {
    /**
     * 交易开始时间
     */
    @ApiModelProperty(value = "交易开始时间")
    private Date transTimeBegin;
    /**
     * 交易结束时间
     */
    @ApiModelProperty(value = "交易结束时间")
    private Date transTimeEnd;
    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderNo;
    /**
     * 交易类型: 档口[2:档口代发订单 3:提现 5:充值 6:推广费 7:推广费退款 8:会员费 9:会员费退款] 卖家[1:下单 4:退款]
     */
    @ApiModelProperty(value = "交易类型: 档口[2:档口代发订单 3:提现 5:充值 6:推广费 7:推广费退款 8:会员费 9:会员费退款] 卖家[1:下单 4:退款]")
    private Integer transType;
    /**
     * 支付方式[1:支付宝 3:余额]
     */
    @ApiModelProperty(value = "支付方式[1:支付宝 3:余额]")
    private Integer payType;
}
