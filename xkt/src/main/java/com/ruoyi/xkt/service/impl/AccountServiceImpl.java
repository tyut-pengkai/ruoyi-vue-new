package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.xkt.domain.ExternalAccount;
import com.ruoyi.xkt.domain.InternalAccount;
import com.ruoyi.xkt.dto.account.*;
import com.ruoyi.xkt.dto.finance.FinanceBillExt;
import com.ruoyi.xkt.enums.EAccountOwnerType;
import com.ruoyi.xkt.enums.EAccountStatus;
import com.ruoyi.xkt.enums.EAccountType;
import com.ruoyi.xkt.enums.EPayChannel;
import com.ruoyi.xkt.service.IAccountService;
import com.ruoyi.xkt.service.IExternalAccountService;
import com.ruoyi.xkt.service.IFinanceBillService;
import com.ruoyi.xkt.service.IInternalAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @author liangyq
 * @date 2025-04-22 21:07
 */
@Slf4j
@Service
public class AccountServiceImpl implements IAccountService {

    @Autowired
    private IFinanceBillService financeBillService;
    @Autowired
    private IInternalAccountService internalAccountService;
    @Autowired
    private IExternalAccountService externalAccountService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public FinanceBillExt prepareWithdraw(Long storeId, BigDecimal amount, String transactionPassword,
                                          EPayChannel payChannel) {
        Assert.notNull(storeId);
        Assert.notEmpty(transactionPassword);
        Assert.isTrue(NumberUtil.isLessOrEqual(amount, BigDecimal.ZERO), "提现金额异常");
        InternalAccount internalAccount = internalAccountService.getAccountAndCheck(storeId, EAccountOwnerType.STORE);
        ExternalAccount externalAccount = externalAccountService.getAccountAndCheck(storeId, EAccountOwnerType.STORE,
                EAccountType.getByChannel(payChannel));
        if (!EAccountStatus.NORMAL.getValue().equals(internalAccount.getAccountStatus())
                || !EAccountStatus.NORMAL.getValue().equals(externalAccount.getAccountStatus())) {
            throw new ServiceException("档口账户已冻结");
        }
        if (StrUtil.isEmpty(internalAccount.getTransactionPassword())) {
            throw new ServiceException("请先设置支付密码");
        }
        if (!StrUtil.equals(SecureUtil.md5(transactionPassword), internalAccount.getTransactionPassword())) {
            throw new ServiceException("支付密码错误");
        }
        return financeBillService.createWithdrawPaymentBill(storeId, amount, payChannel);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void withdrawSuccess(Long financeBillId) {
        financeBillService.entryWithdrawPaymentBill(financeBillId);
    }

    @Override
    public AccountInfoDTO getStoreAccountInfo(Long storeId) {
        Assert.notNull(storeId);
        InternalAccount internalAccount = internalAccountService.getAccount(storeId, EAccountOwnerType.STORE);
        ExternalAccount alipayExternalAccount = externalAccountService.getAccount(storeId, EAccountOwnerType.STORE,
                EAccountType.ALI_PAY);
        return new AccountInfoDTO(BeanUtil.toBean(internalAccount, InternalAccountDTO.class),
                BeanUtil.toBean(alipayExternalAccount, ExternalAccountDTO.class));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AccountInfoDTO createInternalAccountIfNotExists(Long storeId) {
        Assert.notNull(storeId);
        InternalAccount internalAccount = internalAccountService.getAccount(storeId, EAccountOwnerType.STORE);
        if (internalAccount == null) {
            InternalAccountAddDTO addDTO = new InternalAccountAddDTO();
            addDTO.setOwnerId(storeId);
            addDTO.setOwnerType(EAccountOwnerType.STORE.getValue());
            internalAccountService.createAccount(addDTO);
        }
        return getStoreAccountInfo(storeId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AccountInfoDTO setTransactionPassword(TransactionPasswordSetDTO transactionPasswordSet) {
        Assert.notNull(transactionPasswordSet.getStoreId());
        Assert.notEmpty(transactionPasswordSet.getPhoneNumber());
        Assert.notEmpty(transactionPasswordSet.getVerifyCode());
        Assert.notEmpty(transactionPasswordSet.getTransactionPassword());
        //TODO 验证码
        InternalAccount internalAccount = internalAccountService.getAccountAndCheck(transactionPasswordSet.getStoreId(),
                EAccountOwnerType.STORE);
        internalAccountService.setTransactionPassword(internalAccount.getId(),
                SecureUtil.md5(transactionPasswordSet.getTransactionPassword()));
        return getStoreAccountInfo(transactionPasswordSet.getStoreId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AccountInfoDTO bindAlipay(AlipayBindDTO alipayBind) {
        Assert.notNull(alipayBind.getStoreId());
        Assert.notEmpty(alipayBind.getAccountOwnerName());
        Assert.notEmpty(alipayBind.getAccountOwnerNumber());
        Assert.notEmpty(alipayBind.getAccountOwnerPhoneNumber());
        //TODO 验证码
        ExternalAccount alipayExternalAccount = externalAccountService.getAccount(alipayBind.getStoreId(),
                EAccountOwnerType.STORE, EAccountType.ALI_PAY);
        if (alipayExternalAccount != null) {
            //修改
            alipayExternalAccount.setAccountOwnerName(alipayBind.getAccountOwnerName());
            alipayExternalAccount.setAccountOwnerNumber(alipayBind.getAccountOwnerNumber());
            alipayExternalAccount.setAccountOwnerPhoneNumber(alipayBind.getAccountOwnerPhoneNumber());
            alipayExternalAccount.setAccountAuthAccess(true);
            externalAccountService.modifyAccount(alipayExternalAccount);
        } else {
            //新增
            ExternalAccountAddDTO addDTO = new ExternalAccountAddDTO();
            addDTO.setOwnerId(alipayBind.getStoreId());
            addDTO.setOwnerType(EAccountOwnerType.STORE.getValue());
            addDTO.setAccountType(EAccountType.ALI_PAY.getValue());
            addDTO.setAccountOwnerName(alipayBind.getAccountOwnerName());
            addDTO.setAccountOwnerNumber(alipayBind.getAccountOwnerNumber());
            addDTO.setAccountOwnerPhoneNumber(alipayBind.getAccountOwnerPhoneNumber());
            addDTO.setAccountAuthAccess(true);
            externalAccountService.createAccount(addDTO);
        }
        return getStoreAccountInfo(alipayBind.getStoreId());
    }


}
