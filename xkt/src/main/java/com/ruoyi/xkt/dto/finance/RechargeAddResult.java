package com.ruoyi.xkt.dto.finance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liangyq
 * @date 2025-05-09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RechargeAddResult {
    /**
     * 收款单信息
     */
    private FinanceBillExt financeBillExt;
    /**
     * 三方支付返回信息
     */
    private String payRtnStr;

}
