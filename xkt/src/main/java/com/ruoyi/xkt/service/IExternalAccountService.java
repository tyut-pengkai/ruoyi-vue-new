package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.ExternalAccount;
import com.ruoyi.xkt.dto.finance.TransInfo;
import com.ruoyi.xkt.enums.*;

/**
 * @author liangyq
 * @date 2025-04-08 21:14
 */
public interface IExternalAccountService {
    /**
     * 通过ID获取外部账户
     *
     * @param id
     * @return
     */
    ExternalAccount getById(Long id);

    /**
     * 获取外部账户
     *
     * @param ownerId
     * @param ownerType
     * @param accountType
     * @return
     */
    ExternalAccount getExternalAccount(Long ownerId, EAccountOwnerType ownerType, EAccountType accountType);

    /**
     * 添加交易明细
     *
     * @param externalAccountId
     * @param transInfo
     * @param loanDirection
     * @param entryStatus
     * @return
     */
    int addTransDetail(Long externalAccountId, TransInfo transInfo, ELoanDirection loanDirection,
                       EEntryStatus entryStatus);

    /**
     * 交易明细入账
     *
     * @param srcBillId
     * @param srcBillType
     */
    void entryTransDetail(Long srcBillId, EFinBillType srcBillType);

}
