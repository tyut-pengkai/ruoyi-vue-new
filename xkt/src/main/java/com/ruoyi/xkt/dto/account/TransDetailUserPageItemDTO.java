package com.ruoyi.xkt.dto.account;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liangyq
 * @date 2025-04-28 19:01
 */
@Data
public class TransDetailUserPageItemDTO {
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 交易时间
     */
    private Date transTime;
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
     * 交易类型
     */
    private String transType;
    /**
     * 支付方式
     */
    private String payType;
    /**
     * 交易对象
     */
    private String transTarget;

}
