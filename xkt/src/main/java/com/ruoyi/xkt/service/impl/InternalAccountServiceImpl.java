package com.ruoyi.xkt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.SimpleEntity;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.xkt.domain.InternalAccount;
import com.ruoyi.xkt.domain.InternalAccountTransDetail;
import com.ruoyi.xkt.dto.account.InternalAccountAddDTO;
import com.ruoyi.xkt.dto.account.TransDetailStorePageItemDTO;
import com.ruoyi.xkt.dto.account.TransDetailUserPageItemDTO;
import com.ruoyi.xkt.dto.finance.TransInfo;
import com.ruoyi.xkt.enums.*;
import com.ruoyi.xkt.mapper.InternalAccountMapper;
import com.ruoyi.xkt.mapper.InternalAccountTransDetailMapper;
import com.ruoyi.xkt.service.IInternalAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author liangyq
 * @date 2025-04-08 21:14
 */
@Slf4j
@Service
public class InternalAccountServiceImpl implements IInternalAccountService {

    @Autowired
    private InternalAccountMapper internalAccountMapper;
    @Autowired
    private InternalAccountTransDetailMapper internalAccountTransDetailMapper;

    @Override
    public InternalAccount getById(Long id) {
        Assert.notNull(id);
        return internalAccountMapper.selectById(id);
    }

    @Override
    public InternalAccount getAccount(Long ownerId, EAccountOwnerType ownerType) {
        Assert.notNull(ownerId);
        Assert.notNull(ownerType);
        return internalAccountMapper.selectOne(Wrappers.lambdaQuery(InternalAccount.class)
                .eq(InternalAccount::getOwnerId, ownerId)
                .eq(InternalAccount::getOwnerType, ownerType.getValue())
                .eq(SimpleEntity::getDelFlag, Constants.UNDELETED));
    }

    @Override
    public InternalAccount getAccountAndCheck(Long ownerId, EAccountOwnerType ownerType) {
        Assert.notNull(ownerId);
        Assert.notNull(ownerType);
        InternalAccount account = internalAccountMapper.selectOne(Wrappers.lambdaQuery(InternalAccount.class)
                .eq(InternalAccount::getOwnerId, ownerId)
                .eq(InternalAccount::getOwnerType, ownerType.getValue()));
        if (!BeanValidators.exists(account)) {
            throw new ServiceException(CharSequenceUtil.format("{}[{}]内部账户不存在", ownerType.getLabel(),
                    ownerId));
        }
        return account;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addTransDetail(Long internalAccountId, TransInfo transInfo, ELoanDirection loanDirection,
                              EEntryStatus entryStatus) {
        Assert.notNull(internalAccountId);
        InternalAccount internalAccount = internalAccountMapper.getForUpdate(internalAccountId);
        checkAccountStatus(internalAccount);
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
        transDetail.setVersion(0L);
        transDetail.setDelFlag(Constants.UNDELETED);
        switch (loanDirection) {
            case DEBIT:
                //借
                if (EEntryStatus.FINISH.getValue().equals(transDetail.getEntryStatus())) {
                    //已入账&已执行
                    //余额 = 余额 + 交易金额
                    internalAccount.setBalance(internalAccount.getBalance()
                            .add(transDetail.getTransAmount()));
                    //可用余额 = 可用余额 + 交易金额
                    internalAccount.setUsableBalance(internalAccount.getUsableBalance()
                            .add(transDetail.getTransAmount()));
                    //支付中金额不变
                } else {
                    //未入账
                    //余额不变
                    //可用余额不变
                    //支付中金额不变
                }
                break;
            case CREDIT:
                //贷
                if (EEntryStatus.FINISH.getValue().equals(transDetail.getEntryStatus())) {
                    //已入账&已执行
                    //余额 = 余额 - 交易金额
                    internalAccount.setBalance(internalAccount.getBalance()
                            .subtract(transDetail.getTransAmount()));
                    //可用余额 = 可用余额 - 交易金额
                    internalAccount.setUsableBalance(internalAccount.getUsableBalance()
                            .subtract(transDetail.getTransAmount()));
                    //支付中金额不变
                } else {
                    //未入账
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
        checkAccountAmount(internalAccount);
        //更新账户余额
        internalAccountMapper.updateById(internalAccount);
        //交易明细
        return internalAccountTransDetailMapper.insert(transDetail);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void entryTransDetail(Long srcBillId, EFinBillType srcBillType) {
        Assert.notNull(srcBillId);
        Assert.notNull(srcBillType);
        List<InternalAccountTransDetail> transDetails = internalAccountTransDetailMapper.selectList(Wrappers
                .lambdaQuery(InternalAccountTransDetail.class)
                .eq(InternalAccountTransDetail::getSrcBillId, srcBillId)
                .eq(InternalAccountTransDetail::getSrcBillType, srcBillType.getValue())
                .eq(SimpleEntity::getDelFlag, Constants.UNDELETED));
        if (CollUtil.isEmpty(transDetails)) {
            log.warn("单据没有关联的交易明细:{}{}", srcBillType.getLabel(), srcBillId);
            return;
        }
        List<Long> accountIds = transDetails.stream().map(InternalAccountTransDetail::getInternalAccountId).distinct()
                .collect(Collectors.toList());
        //获取账户并加锁
        List<InternalAccount> accounts = internalAccountMapper.listForUpdate(accountIds);
        Map<Long, InternalAccount> accountMap = accounts.stream().collect(Collectors.toMap(SimpleEntity::getId, o -> o));
        //开始入账
        for (InternalAccountTransDetail transDetail : transDetails) {
            InternalAccount internalAccount = accountMap.get(transDetail.getInternalAccountId());
            checkAccountStatus(internalAccount);
            if (EEntryStatus.WAITING.getValue().equals(transDetail.getEntryStatus())) {
                //明细未入账
                transDetail.setEntryStatus(EEntryStatus.FINISH.getValue());
                //不考虑热点账户问题，已入账 => 已执行
                transDetail.setEntryExecuted(EEntryExecuted.FINISH.getValue());
                switch (Objects.requireNonNull(ELoanDirection.of(transDetail.getLoanDirection()))) {
                    case DEBIT:
                        //借
                        //余额 = 余额 + 交易金额
                        internalAccount.setBalance(internalAccount.getBalance()
                                .add(transDetail.getTransAmount()));
                        //可用余额 = 可用余额 + 交易金额
                        internalAccount.setUsableBalance(internalAccount.getUsableBalance()
                                .add(transDetail.getTransAmount()));
                        //支付中金额不变
                        break;
                    case CREDIT:
                        //贷
                        //余额 = 余额 - 交易金额
                        internalAccount.setBalance(internalAccount.getBalance()
                                .subtract(transDetail.getTransAmount()));
                        //可用余额不变
                        //支付中金额 = 支付中金额 - 交易金额
                        internalAccount.setPaymentAmount(internalAccount.getPaymentAmount()
                                .subtract(transDetail.getTransAmount()));
                        break;
                    default:
                        throw new ServiceException("未知借贷方向");
                }
                //检查账户余额，不允许出现负数
                checkAccountAmount(internalAccount);
                //更新账户余额
                internalAccountMapper.updateById(internalAccount);
                //更新交易明细
                int r = internalAccountTransDetailMapper.updateById(transDetail);
                if (r == 0) {
                    throw new ServiceException(Constants.VERSION_LOCK_ERROR_COMMON_MSG);
                }
            } else {
                log.warn("单据重复调用入账方法:{}{}", srcBillType.getLabel(), srcBillId);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public InternalAccount createAccount(InternalAccountAddDTO add) {
        Assert.notNull(add.getOwnerId());
        Assert.notNull(add.getOwnerType());
        InternalAccount internalAccount = new InternalAccount();
        internalAccount.setOwnerType(add.getOwnerType());
        internalAccount.setOwnerId(add.getOwnerId());
        internalAccount.setAccountStatus(EAccountStatus.NORMAL.getValue());
        internalAccount.setBalance(BigDecimal.ZERO);
        internalAccount.setUsableBalance(BigDecimal.ZERO);
        internalAccount.setPaymentAmount(BigDecimal.ZERO);
        internalAccount.setRemark(add.getRemark());
        internalAccount.setDelFlag(Constants.UNDELETED);
        internalAccountMapper.insert(internalAccount);
        return internalAccount;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void setTransactionPassword(Long id, String phoneNumber, String transactionPassword) {
        Assert.notNull(id);
        Assert.notEmpty(phoneNumber);
        Assert.notEmpty(transactionPassword);
        InternalAccount update = new InternalAccount();
        update.setId(id);
        update.setPhoneNumber(phoneNumber);
        update.setTransactionPassword(transactionPassword);
        internalAccountMapper.updateById(update);
    }

    @Override
    public List<TransDetailStorePageItemDTO> listStoreTransDetailPageItem(Long internalAccountId) {
        return internalAccountMapper.listStoreTransDetailPageItem(internalAccountId);
    }

    @Override
    public List<TransDetailUserPageItemDTO> listUserTransDetailPageItem(Long userId) {
        return internalAccountMapper.listUserTransDetailPageItem(userId);
    }


    /**
     * 检查账户状态
     *
     * @param internalAccount
     */
    private void checkAccountStatus(InternalAccount internalAccount) {
        Assert.notNull(internalAccount, "账户不存在");
        if (!BeanValidators.exists(internalAccount)) {
            throw new ServiceException("账户[" + internalAccount.getId() + "]已删除");
        }
        if (!EAccountStatus.NORMAL.getValue().equals(internalAccount.getAccountStatus())) {
            throw new ServiceException("账户[" + internalAccount.getId() + "]已冻结");
        }
    }

    /**
     * 检查账户余额
     *
     * @param internalAccount
     */
    private void checkAccountAmount(InternalAccount internalAccount) {
        if (less0(internalAccount.getBalance())) {
            throw new ServiceException("账户[" + internalAccount.getId() + "]余额不足");
        }
        if (less0(internalAccount.getUsableBalance())) {
            throw new ServiceException("账户[" + internalAccount.getId() + "]可用余额不足");
        }
        if (less0(internalAccount.getPaymentAmount())) {
            throw new ServiceException("账户[" + internalAccount.getId() + "]支付中金额异常");
        }
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
