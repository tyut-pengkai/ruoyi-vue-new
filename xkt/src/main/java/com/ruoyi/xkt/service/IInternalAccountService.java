package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.InternalAccount;
import com.ruoyi.xkt.dto.finance.TransInfo;
import com.ruoyi.xkt.enums.EEntryStatus;
import com.ruoyi.xkt.enums.ELoanDirection;

import java.util.Collection;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-08 21:14
 */
public interface IInternalAccountService {

    /**
     * 获取内部账户（加锁）
     *
     * @param id
     * @return
     */
    InternalAccount getWithLock(Long id);

    /**
     * 获取内部账户列表（加锁）
     *
     * @param ids
     * @return
     */
    List<InternalAccount> listWithLock(Collection<Long> ids);

    /**
     * 添加交易明细
     * 余额会根据明细更新
     * 调用方法时入参账户必须处于加锁状态！
     *
     * @param internalAccount
     * @param transInfo
     * @param loanDirection
     * @param entryStatus
     * @return
     */
    int addTransDetail(InternalAccount internalAccount, TransInfo transInfo, ELoanDirection loanDirection,
                       EEntryStatus entryStatus);

}
