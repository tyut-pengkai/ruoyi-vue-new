package com.ruoyi.xkt.service;

import com.github.pagehelper.Page;
import com.ruoyi.xkt.dto.account.*;
import com.ruoyi.xkt.dto.finance.RechargeAddDTO;
import com.ruoyi.xkt.dto.finance.RechargeAddResult;
import com.ruoyi.xkt.enums.EPayChannel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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

    /**
     * 分页查询档口交易明细
     *
     * @param queryDTO
     * @return
     */
    Page<TransDetailStorePageItemDTO> pageStoreTransDetail(TransDetailStoreQueryDTO queryDTO);

    /**
     * 分页查询卖家交易明细
     *
     * @param queryDTO
     * @return
     */
    Page<TransDetailUserPageItemDTO> pageUserTransDetail(TransDetailUserQueryDTO queryDTO);

    /**
     * 档口充值
     *
     * @param rechargeAddDTO
     * @return
     */
    RechargeAddResult rechargeByStore(RechargeAddDTO rechargeAddDTO);

    /**
     * 充值是否成功
     *
     * @param finBillNo
     * @return
     */
    boolean isRechargeSuccess(String finBillNo);

    /**
     * 校验支付密码
     *
     * @param storeId
     * @param transactionPassword
     * @return
     */
    boolean checkTransactionPassword(Long storeId, String transactionPassword);

    /**
     * 支付推广费
     *
     * @param storeId
     * @param amount
     */
    void payAdvertFee(Long storeId, BigDecimal amount);

    /**
     * 退回推广费
     *
     * @param storeId
     * @param amount
     */
    void refundAdvertFee(Long storeId, BigDecimal amount);

    /**
     * 支付会员费
     *
     * @param storeId
     * @param amount
     */
    void payVipFee(Long storeId, BigDecimal amount);

    /**
     * 退回会员费
     *
     * @param storeId
     * @param amount
     */
    void refundVipFee(Long storeId, BigDecimal amount);

    /**
     * 获取需要继续处理的提现信息
     *
     * @param count
     * @return
     */
    Map<EPayChannel, List<WithdrawPrepareResult>> getNeedContinueWithdrawGroupMap(Integer count);

    /**
     * 短信验证码
     *
     * @param phoneNumber
     */
    void sendSmsVerificationCode(String phoneNumber);

    /**
     * 获取档口手机号
     *
     * @param storeId
     * @return
     */
    String getStorePhoneNumber(Long storeId);

    /**
     * 获取卖家手机号
     *
     * @param userId
     * @return
     */
    String getUserPhoneNumber(Long userId);
}
