package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.account.AccountInfoDTO;
import com.ruoyi.xkt.dto.account.AlipayBindDTO;
import com.ruoyi.xkt.dto.account.TransactionPasswordSetDTO;
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
     * @param transactionPassword
     * @param payChannel
     * @return
     */
    FinanceBillExt prepareWithdraw(Long storeId, BigDecimal amount, String transactionPassword, EPayChannel payChannel);

    /**
     * 档口提现成功
     *
     * @param financeBillId
     */
    void withdrawSuccess(Long financeBillId);

    /**
     * 获取档口账户信息
     *
     * @param storeId
     * @return
     */
    AccountInfoDTO getStoreAccountInfo(Long storeId);

    /**
     * 创建档口账户
     *
     * @param storeId
     * @return
     */
    AccountInfoDTO createInternalAccountIfNotExists(Long storeId);

    /**
     * 设置支付密码
     *
     * @param transactionPasswordSet
     * @return
     */
    AccountInfoDTO setTransactionPassword(TransactionPasswordSetDTO transactionPasswordSet);

    /**
     * 绑定支付宝
     *
     * @param alipayBind
     * @return
     */
    AccountInfoDTO bindAlipay(AlipayBindDTO alipayBind);

}
