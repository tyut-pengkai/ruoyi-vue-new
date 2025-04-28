package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.InternalAccount;
import com.ruoyi.xkt.dto.account.InternalAccountAddDTO;
import com.ruoyi.xkt.dto.account.TransDetailStorePageItemDTO;
import com.ruoyi.xkt.dto.account.TransDetailUserPageItemDTO;
import com.ruoyi.xkt.dto.finance.TransInfo;
import com.ruoyi.xkt.enums.EAccountOwnerType;
import com.ruoyi.xkt.enums.EEntryStatus;
import com.ruoyi.xkt.enums.EFinBillType;
import com.ruoyi.xkt.enums.ELoanDirection;

import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-08 21:14
 */
public interface IInternalAccountService {
    /**
     * 通过ID获取内部账户
     *
     * @param id
     * @return
     */
    InternalAccount getById(Long id);

    /**
     * 获取内部账户
     *
     * @param ownerId
     * @param ownerType
     * @return
     */
    InternalAccount getAccount(Long ownerId, EAccountOwnerType ownerType);

    /**
     * 获取内部账户
     *
     * @param ownerId
     * @param ownerType
     * @return
     */
    InternalAccount getAccountAndCheck(Long ownerId, EAccountOwnerType ownerType);

    /**
     * 添加交易明细
     *
     * @param internalAccountId
     * @param transInfo
     * @param loanDirection
     * @param entryStatus
     * @return
     */
    int addTransDetail(Long internalAccountId, TransInfo transInfo, ELoanDirection loanDirection,
                       EEntryStatus entryStatus);

    /**
     * 交易明细入账
     * 根据来源单据获取待入账的交易明细执行入账
     *
     * @param srcBillId
     * @param srcBillType
     */
    void entryTransDetail(Long srcBillId, EFinBillType srcBillType);

    /**
     * 创建账户
     *
     * @param add
     * @return
     */
    InternalAccount createAccount(InternalAccountAddDTO add);

    /**
     * 设置支付密码
     *
     * @param id                  ID
     * @param phoneNumber         电话号码
     * @param transactionPassword md5
     */
    void setTransactionPassword(Long id, String phoneNumber, String transactionPassword);

    /**
     * 档口交易明细
     *
     * @param internalAccountId
     * @return
     */
    List<TransDetailStorePageItemDTO> listStoreTransDetailPageItem(Long internalAccountId);

    /**
     * 卖家交易明细
     *
     * @param userId
     * @return
     */
    List<TransDetailUserPageItemDTO> listUserTransDetailPageItem(Long userId);
}
