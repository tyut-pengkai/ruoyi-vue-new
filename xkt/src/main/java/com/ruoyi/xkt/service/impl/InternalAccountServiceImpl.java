package com.ruoyi.xkt.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.xkt.domain.InternalAccount;
import com.ruoyi.xkt.domain.InternalAccountTransDetail;
import com.ruoyi.xkt.dto.finance.TransInfo;
import com.ruoyi.xkt.enums.EAccountStatus;
import com.ruoyi.xkt.enums.EEntryExecuted;
import com.ruoyi.xkt.enums.EEntryStatus;
import com.ruoyi.xkt.enums.ELoanDirection;
import com.ruoyi.xkt.mapper.InternalAccountMapper;
import com.ruoyi.xkt.mapper.InternalAccountTransDetailMapper;
import com.ruoyi.xkt.service.IInternalAccountService;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-08 21:14
 */
@Service
public class InternalAccountServiceImpl implements IInternalAccountService {

    @Autowired
    private InternalAccountMapper internalAccountMapper;
    @Autowired
    private InternalAccountTransDetailMapper internalAccountTransDetailMapper;

    @Transactional
    @Override
    public InternalAccount getWithLock(Long id) {
        return internalAccountMapper.getForUpdate(id);
    }

    @Transactional
    @Override
    public List<InternalAccount> listWithLock(Collection<Long> ids) {
        return internalAccountMapper.listForUpdate(ids);
    }

    @Transactional
    @Override
    public int addTransDetail(InternalAccount internalAccount, TransInfo transInfo, ELoanDirection loanDirection,
                              EEntryStatus entryStatus) {
        Assert.notNull(internalAccount, "内部账户不存在");
        if (!BeanValidators.exists(internalAccount)) {
            throw new ServiceException("内部账户[" + internalAccount.getId() + "]不存在");
        }
        if (!EAccountStatus.NORMAL.getValue().equals(internalAccount.getAccountStatus())) {
            throw new ServiceException("内部账户[" + internalAccount.getId() + "]已冻结");
        }
        InternalAccountTransDetail transDetail = new InternalAccountTransDetail();
        transDetail.setInternalAccountId(internalAccount.getId());
        transDetail.setSrcBillId(transInfo.getSrcBillId());
        transDetail.setSrcBillType(transInfo.getSrcBillType());
        transDetail.setLoanDirection(loanDirection.getValue());
        if (less0(transInfo.getTransAmount())) {
            throw new ServiceException("交易金额异常");
        }
        transDetail.setTransAmount(transInfo.getTransAmount());
        transDetail.setTransTime(transInfo.getTransTime());
        transDetail.setHandlerId(transInfo.getHandlerId());
        transDetail.setEntryStatus(entryStatus.getValue());
        //不考虑热点账户问题，已入账 => 已执行
        transDetail.setEntryExecuted(EEntryStatus.FINISH == entryStatus ?
                EEntryExecuted.FINISH.getValue() : EEntryExecuted.WAITING.getValue());
        transDetail.setRemark(transInfo.getRemark());
        switch (loanDirection) {
            case DEBIT:
                //借
                if (EEntryExecuted.FINISH.getValue().equals(transDetail.getEntryExecuted())) {
                    //已执行
                    //余额 = 余额 + 交易金额
                    internalAccount.setBalance(internalAccount.getBalance()
                            .add(transDetail.getTransAmount()));
                    //可用余额 = 可用余额 + 交易金额
                    internalAccount.setUsableBalance(internalAccount.getUsableBalance()
                            .add(transDetail.getTransAmount()));
                    //支付中金额不变
                } else {
                    //未执行
                    //余额不变
                    //可用余额不变
                    //支付中金额不变
                }
                break;
            case CREDIT:
                //贷
                if (EEntryExecuted.FINISH.getValue().equals(transDetail.getEntryExecuted())) {
                    //已执行
                    //余额 = 余额 - 交易金额
                    internalAccount.setBalance(internalAccount.getBalance()
                            .subtract(transDetail.getTransAmount()));
                    //可用余额 = 可用余额 - 交易金额
                    internalAccount.setUsableBalance(internalAccount.getUsableBalance()
                            .subtract(transDetail.getTransAmount()));
                    //支付中金额不变
                } else {
                    //未执行
                    //余额不变
                    //可用余额 = 可用余额 - 交易金额
                    internalAccount.setUsableBalance(internalAccount.getUsableBalance()
                            .subtract(transDetail.getTransAmount()));
                    //支付中金额 = 支付中金额 + 交易金额
                    internalAccount.setPaymentAmount(internalAccount.getPaymentAmount()
                            .add(transDetail.getTransAmount()));
                }
                break;
            default:
                throw new ServiceException("未知借贷方向");
        }
        //检查账户余额，不允许出现负数
        if (less0(internalAccount.getBalance())) {
            throw new ServiceException("账户[" + internalAccount.getId() + "]余额不足");
        }
        if (less0(internalAccount.getUsableBalance())) {
            throw new ServiceException("账户[" + internalAccount.getId() + "]可用余额不足");
        }
        if (less0(internalAccount.getPaymentAmount())) {
            throw new ServiceException("账户[" + internalAccount.getId() + "]支付中金额异常");
        }
        //更新账户余额
        internalAccountMapper.updateById(internalAccount);
        //交易明细
        return internalAccountTransDetailMapper.insert(transDetail);
    }

    /**
     * 判断数字是否小于0
     *
     * @param num
     * @return
     */
    private boolean less0(BigDecimal num) {
        return NumberUtil.isLess(num, BigDecimal.ZERO);
    }
}
