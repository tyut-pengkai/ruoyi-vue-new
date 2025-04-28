package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.account.AlipayBindDTO;
import com.ruoyi.xkt.dto.account.AssetInfoDTO;
import com.ruoyi.xkt.dto.account.TransactionPasswordSetDTO;
import com.ruoyi.xkt.dto.account.WithdrawPrepareResult;
import com.ruoyi.xkt.enums.EPayChannel;

import java.math.BigDecimal;

/**
 * @author liangyq
 * @date 2025-04-22 21:06
 */
public interface IAssetService {
    /**
     * 档口提现准备
     *
     * @param storeId
     * @param amount
     * @param transactionPassword
     * @param payChannel
     * @return
     */
    WithdrawPrepareResult prepareWithdraw(Long storeId, BigDecimal amount, String transactionPassword,
                                          EPayChannel payChannel);

    /**
     * 档口提现成功
     *
     * @param financeBillId
     */
    void withdrawSuccess(Long financeBillId);

    /**
     * 获取档口资产信息
     *
     * @param storeId
     * @return
     */
    AssetInfoDTO getStoreAssetInfo(Long storeId);

    /**
     * 获取用户资产信息
     *
     * @param userId
     * @return
     */
    AssetInfoDTO getUserAssetInfo(Long userId);

    /**
     * 创建档口账户
     *
     * @param storeId
     * @return
     */
    AssetInfoDTO createInternalAccountIfNotExists(Long storeId);

    /**
     * 设置支付密码
     *
     * @param transactionPasswordSet
     * @return
     */
    AssetInfoDTO setTransactionPassword(TransactionPasswordSetDTO transactionPasswordSet);

    /**
     * 绑定支付宝
     *
     * @param alipayBind
     * @return
     */
    AssetInfoDTO bindAlipay(AlipayBindDTO alipayBind);

}
