package com.ruoyi.xkt.dto.finance;

import com.ruoyi.xkt.enums.EPayChannel;
import com.ruoyi.xkt.enums.EPayPage;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liangyq
 * @date 2025-05-09
 */
@Data
public class RechargeAddDTO {
    /**
     * 档口ID
     */
    private Long storeId;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 支付渠道
     */
    private EPayChannel payChannel;
    /**
     * 支付来源
     */
    private EPayPage payPage;
    /**
     * 支付完成后跳转url，若为空默认跳转商城首页
     */
    private String returnUrl;
}
