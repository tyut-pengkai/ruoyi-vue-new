package com.ruoyi.xkt.dto.account;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liangyq
 * @date 2025-04-28 19:01
 */
@Data
public class TransDetailStorePageItemDTO {
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 交易时间
     */
    private Date transTime;
    /**
     * 交易金额
     */
    private BigDecimal transAmount;
    /**
     * 借贷方向[1:借(D) 2:贷(C)]
     */
    private Integer loanDirection;
    /**
     * 收入
     */
    private BigDecimal inputAmount;
    /**
     * 支出
     */
    private BigDecimal outputAmount;
    /**
     * 交易说明
     */
    private String remark;
    /**
     * 来源类型[1:代发订单支付 2:代发订单完成 3:提现]
     */
    private Integer srcType;
    /**
     * 交易类型
     */
    private String transType;
    /**
     * 单据类型[1:收款 2:付款 3:转移]
     */
    private Integer billType;
    /**
     * 支付方式
     */
    private String payType;
}
