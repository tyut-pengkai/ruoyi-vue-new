package com.ruoyi.xkt.dto.account;

import lombok.Data;

import java.util.Date;

/**
 * @author liangyq
 * @date 2025-08-17
 */
@Data
public class TransDetailQueryDTO {
    /**
     * 账户ID: 查询档口明细
     */
    private Long internalAccountId;
    /**
     * 用户ID: 查询卖家明细
     */
    private Long userId;
    /**
     * 交易开始时间
     */
    private Date transTimeBegin;
    /**
     * 交易结束时间
     */
    private Date transTimeEnd;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 交易类型: 档口[2:档口代发订单 3:提现 5:充值 6:推广费 7:推广费退款 8:会员费 9:会员费退款] 卖家[1:下单 4:退款]
     */
    private Integer transType;
    /**
     * 支付方式[1:支付宝 3:余额]
     */
    private Integer payType;
}
