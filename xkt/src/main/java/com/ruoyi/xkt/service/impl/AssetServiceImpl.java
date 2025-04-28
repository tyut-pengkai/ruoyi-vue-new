package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.xkt.domain.ExternalAccount;
import com.ruoyi.xkt.domain.InternalAccount;
import com.ruoyi.xkt.dto.account.*;
import com.ruoyi.xkt.dto.finance.FinanceBillExt;
import com.ruoyi.xkt.enums.EAccountOwnerType;
import com.ruoyi.xkt.enums.EAccountStatus;
import com.ruoyi.xkt.enums.EAccountType;
import com.ruoyi.xkt.enums.EPayChannel;
import com.ruoyi.xkt.service.IAssetService;
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
public class AssetServiceImpl implements IAssetService {

    @Autowired
    private IFinanceBillService financeBillService;
    @Autowired
    private IInternalAccountService internalAccountService;
    @Autowired
    private IExternalAccountService externalAccountService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public WithdrawPrepareResult prepareWithdraw(Long storeId, BigDecimal amount, String transactionPassword,
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
        FinanceBillExt financeBillExt = financeBillService.createWithdrawPaymentBill(storeId, amount, payChannel);
        return new WithdrawPrepareResult(financeBillExt.getFinanceBill().getId(),
                financeBillExt.getFinanceBill().getBillNo(),
                financeBillExt.getFinanceBill().getTransAmount(),
                externalAccount.getAccountOwnerNumber(),
                externalAccount.getAccountOwnerName(),
                externalAccount.getAccountOwnerPhoneNumber());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void withdrawSuccess(Long financeBillId) {
        financeBillService.entryWithdrawPaymentBill(financeBillId);
    }

    @Override
    public AssetInfoDTO getStoreAssetInfo(Long storeId) {
        Assert.notNull(storeId);
        InternalAccount internalAccount = internalAccountService.getAccount(storeId, EAccountOwnerType.STORE);
        ExternalAccount alipayExternalAccount = externalAccountService.getAccount(storeId, EAccountOwnerType.STORE,
                EAccountType.ALI_PAY);
        return new AssetInfoDTO(BeanUtil.toBean(internalAccount, InternalAccountDTO.class),
                BeanUtil.toBean(alipayExternalAccount, ExternalAccountDTO.class));
    }

    @Override
    public AssetInfoDTO getUserAssetInfo(Long userId) {
        Assert.notNull(userId);
        ExternalAccount alipayExternalAccount = externalAccountService.getAccount(userId, EAccountOwnerType.USER,
                EAccountType.ALI_PAY);
        return new AssetInfoDTO(null,
                BeanUtil.toBean(alipayExternalAccount, ExternalAccountDTO.class));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AssetInfoDTO createInternalAccountIfNotExists(Long storeId) {
        Assert.notNull(storeId);
        InternalAccount internalAccount = internalAccountService.getAccount(storeId, EAccountOwnerType.STORE);
        if (internalAccount == null) {
            InternalAccountAddDTO addDTO = new InternalAccountAddDTO();
            addDTO.setOwnerId(storeId);
            addDTO.setOwnerType(EAccountOwnerType.STORE.getValue());
            internalAccountService.createAccount(addDTO);
        }
        return getStoreAssetInfo(storeId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AssetInfoDTO setTransactionPassword(TransactionPasswordSetDTO transactionPasswordSet) {
        Assert.notNull(transactionPasswordSet.getStoreId());
        Assert.notEmpty(transactionPasswordSet.getPhoneNumber());
        Assert.notEmpty(transactionPasswordSet.getVerifyCode());
        Assert.notEmpty(transactionPasswordSet.getTransactionPassword());
        //TODO 验证码
        InternalAccount internalAccount = internalAccountService.getAccountAndCheck(transactionPasswordSet.getStoreId(),
                EAccountOwnerType.STORE);
        internalAccountService.setTransactionPassword(internalAccount.getId(), transactionPasswordSet.getPhoneNumber(),
                SecureUtil.md5(transactionPasswordSet.getTransactionPassword()));
        return getStoreAssetInfo(transactionPasswordSet.getStoreId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AssetInfoDTO bindAlipay(AlipayBindDTO alipayBind) {
        Assert.notNull(alipayBind.getOwnerId());
        EAccountOwnerType ownerType = EAccountOwnerType.of(alipayBind.getOwnerType());
        if (EAccountOwnerType.STORE != ownerType
                && EAccountOwnerType.USER != ownerType) {
            throw new ServiceException("账户归属异常");
        }
        Assert.notEmpty(alipayBind.getAccountOwnerName());
        Assert.notEmpty(alipayBind.getAccountOwnerNumber());
        Assert.notEmpty(alipayBind.getAccountOwnerPhoneNumber());
        //TODO 验证码
        ExternalAccount alipayExternalAccount = externalAccountService.getAccount(alipayBind.getOwnerId(),
                ownerType, EAccountType.ALI_PAY);
        if (alipayExternalAccount != null) {
            //修改
            ExternalAccountUpdateDTO updateDTO = new ExternalAccountUpdateDTO();
            updateDTO.setId(alipayExternalAccount.getId());
            updateDTO.setAccountOwnerName(alipayBind.getAccountOwnerName());
            updateDTO.setAccountOwnerNumber(alipayBind.getAccountOwnerNumber());
            updateDTO.setAccountOwnerPhoneNumber(alipayBind.getAccountOwnerPhoneNumber());
            updateDTO.setAccountAuthAccess(true);
            externalAccountService.modifyAccount(updateDTO);
        } else {
            //新增
            ExternalAccountAddDTO addDTO = new ExternalAccountAddDTO();
            addDTO.setOwnerId(alipayBind.getOwnerId());
            addDTO.setOwnerType(ownerType.getValue());
            addDTO.setAccountType(EAccountType.ALI_PAY.getValue());
            addDTO.setAccountOwnerName(alipayBind.getAccountOwnerName());
            addDTO.setAccountOwnerNumber(alipayBind.getAccountOwnerNumber());
            addDTO.setAccountOwnerPhoneNumber(alipayBind.getAccountOwnerPhoneNumber());
            addDTO.setAccountAuthAccess(true);
            externalAccountService.createAccount(addDTO);
        }
        if (EAccountOwnerType.USER == ownerType) {
            return getUserAssetInfo(alipayBind.getOwnerId());
        }
        return getStoreAssetInfo(alipayBind.getOwnerId());
    }

    @Override
    public Page<TransDetailStorePageItemDTO> pageStoreTransDetail(TransDetailStoreQueryDTO queryDTO) {
        Assert.notNull(queryDTO.getStoreId());
        InternalAccount internalAccount = internalAccountService.getAccount(queryDTO.getStoreId(),
                EAccountOwnerType.STORE);
        Assert.notNull(internalAccount);
        Page<TransDetailStorePageItemDTO> page = PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        internalAccountService.listStoreTransDetailPageItem(internalAccount.getId());
        return page;
    }

    @Override
    public Page<TransDetailUserPageItemDTO> pageUserTransDetail(TransDetailUserQueryDTO queryDTO) {
        Assert.notNull(queryDTO.getUserId());
        Page<TransDetailUserPageItemDTO> page = PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        internalAccountService.listUserTransDetailPageItem(queryDTO.getUserId());
        return page;
    }


}
