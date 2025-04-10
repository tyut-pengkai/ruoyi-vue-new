package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.InternalAccount;
import com.ruoyi.xkt.dto.finance.TransInfo;
import com.ruoyi.xkt.enums.EEntryStatus;
import com.ruoyi.xkt.enums.EFinBillType;
import com.ruoyi.xkt.enums.ELoanDirection;

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

}
