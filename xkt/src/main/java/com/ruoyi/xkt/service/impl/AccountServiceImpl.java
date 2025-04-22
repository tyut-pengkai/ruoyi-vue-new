package com.ruoyi.xkt.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.xkt.domain.ExternalAccount;
import com.ruoyi.xkt.domain.InternalAccount;
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
    public FinanceBillExt prepareWithdraw(Long storeId, BigDecimal amount, String password, EPayChannel payChannel) {
        Assert.notNull(storeId);
        Assert.notEmpty(password);
        Assert.isTrue(NumberUtil.isLessOrEqual(amount, BigDecimal.ZERO), "提现金额异常");
        InternalAccount internalAccount = internalAccountService.getInternalAccount(storeId, EAccountOwnerType.STORE);
        ExternalAccount externalAccount = externalAccountService.getExternalAccount(storeId, EAccountOwnerType.STORE,
                EAccountType.getByChannel(payChannel));
        if (!EAccountStatus.NORMAL.getValue().equals(internalAccount.getAccountStatus())
                || !EAccountStatus.NORMAL.getValue().equals(externalAccount.getAccountStatus())) {
            throw new ServiceException("档口账户已冻结");
        }
        if (StrUtil.isEmpty(externalAccount.getPassword())) {
            throw new ServiceException("请先设置支付密码");
        }
        if (!StrUtil.equals(password, externalAccount.getPassword())) {
            throw new ServiceException("支付密码错误");
        }
        return financeBillService.createWithdrawPaymentBill(storeId, amount, payChannel);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void withdrawSuccess(Long financeBillId) {
        financeBillService.entryWithdrawPaymentBill(financeBillId);
    }


}
