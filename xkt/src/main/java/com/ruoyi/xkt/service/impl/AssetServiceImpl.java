package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.XktBaseEntity;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.framework.sms.SmsClientWrapper;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.xkt.domain.*;
import com.ruoyi.xkt.dto.account.*;
import com.ruoyi.xkt.dto.finance.*;
import com.ruoyi.xkt.enums.*;
import com.ruoyi.xkt.manager.PaymentManager;
import com.ruoyi.xkt.mapper.StoreCertificateMapper;
import com.ruoyi.xkt.mapper.StoreMapper;
import com.ruoyi.xkt.service.IAssetService;
import com.ruoyi.xkt.service.IExternalAccountService;
import com.ruoyi.xkt.service.IFinanceBillService;
import com.ruoyi.xkt.service.IInternalAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    private List<PaymentManager> paymentManagers;
    @Autowired
    private SmsClientWrapper smsClient;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private StoreMapper storeMapper;
    @Autowired
    private StoreCertificateMapper storeCertificateMapper;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public WithdrawPrepareResult prepareWithdraw(Long storeId, BigDecimal amount, String transactionPassword,
                                                 EPayChannel payChannel) {
        Assert.notNull(storeId);
        Assert.notEmpty(transactionPassword);
        Assert.isTrue(NumberUtil.isGreaterOrEqual(amount, BigDecimal.ONE), "提现金额不能低于1元");
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
        FinanceBillExtInfo.PaymentWithdraw extInfo = FinanceBillExtInfo.PaymentWithdraw
                .parse(financeBillExt.getFinanceBill().getExtInfo());
        return new WithdrawPrepareResult(financeBillExt.getFinanceBill().getId(),
                financeBillExt.getFinanceBill().getBillNo(),
                financeBillExt.getFinanceBill().getTransAmount(),
                extInfo.getAccountOwnerNumber(),
                extInfo.getAccountOwnerName(),
                extInfo.getAccountOwnerPhoneNumber());
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
        //必须是档口注册人的手机号
        if (!StrUtil.equals(transactionPasswordSet.getPhoneNumber(),
                getStorePhoneNumber(transactionPasswordSet.getStoreId()))) {
            throw new ServiceException("请输入档口供应商注册账号绑定的手机号");
        }
        validateSmsVerificationCode(transactionPasswordSet.getPhoneNumber(), transactionPasswordSet.getVerifyCode());
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
        if (EAccountOwnerType.STORE == ownerType) {
            //必须是档口注册人的手机号
            if (!StrUtil.equals(alipayBind.getAccountOwnerPhoneNumber(),
                    getStorePhoneNumber(alipayBind.getOwnerId()))) {
                throw new ServiceException("请输入档口供应商注册账号绑定的手机号");
            }
            //档口认证信息
            StoreCertificate certificate = CollUtil.getFirst(storeCertificateMapper.selectList(Wrappers.lambdaQuery(StoreCertificate.class)
                    .eq(StoreCertificate::getStoreId, alipayBind.getOwnerId())
                    .eq(XktBaseEntity::getDelFlag, Constants.UNDELETED)));
            if (certificate == null) {
                throw new ServiceException("档口未认证，无法绑定支付宝");
            }
            if (!StrUtil.equals(certificate.getPhone(), alipayBind.getAccountOwnerNumber())) {
                throw new ServiceException("支付宝账号必须是企业法人手机号");
            }
            if (!StrUtil.equals(certificate.getLegalName(), alipayBind.getAccountOwnerName())) {
                throw new ServiceException("真实姓名必须是企业法人姓名");
            }
        } else if (EAccountOwnerType.USER == ownerType) {
            //必须是登录用户的手机号
            if (!StrUtil.equals(alipayBind.getAccountOwnerPhoneNumber(),
                    getUserPhoneNumber(alipayBind.getOwnerId()))) {
                throw new ServiceException("请输入当前用户绑定的手机号");
            }
        } else {
            throw new ServiceException("账户归属异常");
        }
        Assert.notEmpty(alipayBind.getAccountOwnerName());
        Assert.notEmpty(alipayBind.getAccountOwnerNumber());
        Assert.notEmpty(alipayBind.getAccountOwnerPhoneNumber());
        validateSmsVerificationCode(alipayBind.getAccountOwnerPhoneNumber(), alipayBind.getVerifyCode());
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
        Page<TransDetailStorePageItemDTO> page = PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize(),
                "iatd.trans_time DESC");
        TransDetailQueryDTO tdq = BeanUtil.toBean(queryDTO, TransDetailQueryDTO.class);
        tdq.setInternalAccountId(internalAccount.getId());
        internalAccountService.listStoreTransDetailPageItem(tdq);
        return page;
    }

    @Override
    public Page<TransDetailUserPageItemDTO> pageUserTransDetail(TransDetailUserQueryDTO queryDTO) {
        Assert.notNull(queryDTO.getUserId());
        Page<TransDetailUserPageItemDTO> page = PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize(),
                "fb.create_time DESC");
        TransDetailQueryDTO tdq = BeanUtil.toBean(queryDTO, TransDetailQueryDTO.class);
        tdq.setUserId(queryDTO.getUserId());
        internalAccountService.listUserTransDetailPageItem(tdq);
        return page;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RechargeAddResult rechargeByStore(RechargeAddDTO rechargeAddDTO) {
        String billNo = financeBillService.generateBillNo(EFinBillType.COLLECTION);
        //缓存7天
        redisCache.setCacheObject(CacheConstants.RECHARGE_BILL_NO_CACHE.concat(billNo),
                new RechargeCacheDTO(rechargeAddDTO.getStoreId(), rechargeAddDTO.getAmount()),
                7, TimeUnit.DAYS);
        PaymentManager paymentManager = getPaymentManager(rechargeAddDTO.getPayChannel());
        String payRtnStr = paymentManager.pay(billNo, rechargeAddDTO.getAmount(),
                "档口充值", rechargeAddDTO.getPayPage(), null, rechargeAddDTO.getReturnUrl());
        return new RechargeAddResult(billNo, payRtnStr);
    }

    @Override
    public boolean isRechargeSuccess(String finBillNo) {
        FinanceBill financeBill = financeBillService.getByBillNo(finBillNo);
        return BeanValidators.exists(financeBill)
                && EFinBillStatus.SUCCESS.getValue().equals(financeBill.getBillStatus());
    }

    @Override
    public boolean checkTransactionPassword(Long storeId, String transactionPassword) {
        if (storeId == null || transactionPassword == null) {
            return false;
        }
        InternalAccount internalAccount = internalAccountService.getAccountAndCheck(storeId, EAccountOwnerType.STORE);
        if (internalAccount == null) {
            throw new ServiceException("档口账户未创建");
        }
        if (StrUtil.isEmpty(internalAccount.getTransactionPassword())) {
            throw new ServiceException("请先设置支付密码");
        }
        return StrUtil.equals(SecureUtil.md5(transactionPassword), internalAccount.getTransactionPassword());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void payAdvertFee(Long storeId, BigDecimal amount) {
        Assert.notNull(storeId);
        Assert.notNull(amount);
        InternalAccount internalAccount = internalAccountService.getAccountAndCheck(storeId, EAccountOwnerType.STORE);
        if (!EAccountStatus.NORMAL.getValue().equals(internalAccount.getAccountStatus())) {
            throw new ServiceException("档口账户已冻结");
        }
        if (StrUtil.isEmpty(internalAccount.getTransactionPassword())) {
            throw new ServiceException("请先设置支付密码");
        }
        financeBillService.createInternalTransferBill(Constants.PLATFORM_INTERNAL_ACCOUNT_ID, internalAccount.getId(),
                amount, EFinBillSrcType.ADVERT_PAID.getValue(), null, null, null, "推广费");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void refundAdvertFee(Long storeId, BigDecimal amount) {
        Assert.notNull(storeId);
        Assert.notNull(amount);
        InternalAccount internalAccount = internalAccountService.getAccountAndCheck(storeId, EAccountOwnerType.STORE);
        financeBillService.createInternalTransferBill(internalAccount.getId(), Constants.PLATFORM_INTERNAL_ACCOUNT_ID,
                amount, EFinBillSrcType.ADVERT_REFUND.getValue(), null, null, null,
                "推广费退款");

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void payVipFee(Long storeId, BigDecimal amount) {
        Assert.notNull(storeId);
        Assert.notNull(amount);
        InternalAccount internalAccount = internalAccountService.getAccountAndCheck(storeId, EAccountOwnerType.STORE);
        if (!EAccountStatus.NORMAL.getValue().equals(internalAccount.getAccountStatus())) {
            throw new ServiceException("档口账户已冻结");
        }
        if (StrUtil.isEmpty(internalAccount.getTransactionPassword())) {
            throw new ServiceException("请先设置支付密码");
        }
        financeBillService.createInternalTransferBill(Constants.PLATFORM_INTERNAL_ACCOUNT_ID, internalAccount.getId(),
                amount, EFinBillSrcType.VIP_PAID.getValue(), null, null, null, "会员费");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void refundVipFee(Long storeId, BigDecimal amount) {
        Assert.notNull(storeId);
        Assert.notNull(amount);
        InternalAccount internalAccount = internalAccountService.getAccountAndCheck(storeId, EAccountOwnerType.STORE);
        financeBillService.createInternalTransferBill(internalAccount.getId(), Constants.PLATFORM_INTERNAL_ACCOUNT_ID,
                amount, EFinBillSrcType.VIP_REFUND.getValue(), null, null, null,
                "会员费退款");
    }

    @Override
    public Map<EPayChannel, List<WithdrawPrepareResult>> getNeedContinueWithdrawGroupMap(Integer count) {
        Map<EPayChannel, List<WithdrawPrepareResult>> rtn = new HashMap<>(EPayChannel.values().length);
        List<FinanceBillDTO> bills = financeBillService.listByStatus(EFinBillStatus.PROCESSING,
                EFinBillType.PAYMENT, EFinBillSrcType.WITHDRAW, count);
        for (FinanceBillDTO bill : bills) {
            if (StrUtil.isEmpty(bill.getExtInfo())) {
                continue;
            }
            FinanceBillExtInfo.PaymentWithdraw withdrawInfo = FinanceBillExtInfo.PaymentWithdraw
                    .parse(bill.getExtInfo());
            EPayChannel payChannel = EPayChannel.of(withdrawInfo.getPayChannel());
            rtn.computeIfAbsent(payChannel, k -> new ArrayList<>())
                    .add(new WithdrawPrepareResult(bill.getId(),
                            bill.getBillNo(),
                            bill.getTransAmount(),
                            withdrawInfo.getAccountOwnerNumber(),
                            withdrawInfo.getAccountOwnerName(),
                            withdrawInfo.getAccountOwnerPhoneNumber()));
        }
        return rtn;
    }

    @Override
    public void sendSmsVerificationCode(String phoneNumber) {
        boolean success = smsClient.sendVerificationCode(CacheConstants.SMS_ASSET_CAPTCHA_CODE_CD_PHONE_NUM_KEY,
                CacheConstants.SMS_ASSET_CAPTCHA_CODE_KEY, phoneNumber, RandomUtil.randomNumbers(6));
        if (!success) {
            throw new ServiceException("短信发送失败");
        }
    }

    @Override
    public String getStorePhoneNumber(Long storeId) {
        Store store = storeMapper.selectById(storeId);
        Assert.notNull(store);
        SysUser user = userMapper.selectById(store.getUserId());
        Assert.notNull(user);
        return user.getPhonenumber();
    }

    @Override
    public String getUserPhoneNumber(Long userId) {
        SysUser user = userMapper.selectById(userId);
        Assert.notNull(user);
        return user.getPhonenumber();
    }

    /**
     * 短信验证码验证
     *
     * @param phoneNumber 电话号码
     * @param code        验证码
     * @return
     */
    private void validateSmsVerificationCode(String phoneNumber, String code) {
        boolean match = smsClient.matchVerificationCode(CacheConstants.SMS_ASSET_CAPTCHA_CODE_KEY, phoneNumber, code);
        if (!match) {
            throw new ServiceException("验证码错误或已过期");
        }
    }

    /**
     * 根据支付渠道匹配支付类
     *
     * @param payChannel
     * @return
     */
    private PaymentManager getPaymentManager(EPayChannel payChannel) {
        if (payChannel == null) {
            throw new ServiceException("请先选择支付渠道");
        }
        for (PaymentManager paymentManager : paymentManagers) {
            if (paymentManager.channel() == payChannel) {
                return paymentManager;
            }
        }
        throw new ServiceException("未知支付渠道");
    }


}
