package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.finance.FinanceBillExt;
import com.ruoyi.xkt.enums.EPayChannel;

import java.math.BigDecimal;

/**
 * @author liangyq
 * @date 2025-04-22 21:06
 */
public interface IAccountService {
    /**
     * 档口提现准备
     *
     * @param storeId
     * @param amount
     * @param password
     * @param payChannel
     * @return
     */
    FinanceBillExt prepareWithdraw(Long storeId, BigDecimal amount, String password, EPayChannel payChannel);

    /**
     * 档口提现成功
     *
     * @param financeBillId
     */
    void withdrawSuccess(Long financeBillId);
}
