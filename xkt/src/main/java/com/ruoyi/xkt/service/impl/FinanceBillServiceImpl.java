package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.SimpleEntity;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.xkt.domain.FinanceBill;
import com.ruoyi.xkt.domain.FinanceBillDetail;
import com.ruoyi.xkt.domain.InternalAccount;
import com.ruoyi.xkt.domain.StoreOrderDetail;
import com.ruoyi.xkt.dto.finance.FinanceBillDTO;
import com.ruoyi.xkt.dto.finance.FinanceBillExt;
import com.ruoyi.xkt.dto.finance.TransInfo;
import com.ruoyi.xkt.dto.order.StoreOrderExt;
import com.ruoyi.xkt.enums.*;
import com.ruoyi.xkt.mapper.FinanceBillDetailMapper;
import com.ruoyi.xkt.mapper.FinanceBillMapper;
import com.ruoyi.xkt.service.IExternalAccountService;
import com.ruoyi.xkt.service.IFinanceBillService;
import com.ruoyi.xkt.service.IInternalAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author liangyq
 * @date 2025-04-08 21:14
 */
@Slf4j
@Service
public class FinanceBillServiceImpl implements IFinanceBillService {

    @Autowired
    private FinanceBillMapper financeBillMapper;
    @Autowired
    private FinanceBillDetailMapper financeBillDetailMapper;
    @Autowired
    private IInternalAccountService internalAccountService;
    @Autowired
    private IExternalAccountService externalAccountService;

    @Override
    public FinanceBill getByBillNo(String billNo) {
        Assert.notEmpty(billNo);
        return financeBillMapper.selectOne(Wrappers.lambdaQuery(FinanceBill.class)
                .eq(FinanceBill::getBillNo, billNo));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public FinanceBillExt createOrderPaidCollectionBill(StoreOrderExt orderExt, Long payId,
                                                        EPayChannel payChannel) {
        Assert.notNull(orderExt);
        Assert.notNull(payId);
        Assert.notNull(payChannel);
        FinanceBill bill = new FinanceBill();
        bill.setBillNo(generateBillNo(EFinBillType.COLLECTION));
        bill.setBillType(EFinBillType.COLLECTION.getValue());
        //直接标记成功
        bill.setBillStatus(EFinBillStatus.SUCCESS.getValue());
        bill.setSrcType(EFinBillSrcType.STORE_ORDER_PAID.getValue());
        bill.setSrcId(payId);
        bill.setRelType(EFinBillRelType.STORE_ORDER.getValue());
        bill.setRelId(orderExt.getOrder().getId());
        //支付渠道+回调ID构成业务唯一键，防止重复创建收款单
        String businessUniqueKey = payChannel.getPrefix() + payId;
        bill.setBusinessUniqueKey(businessUniqueKey);
        bill.setInputInternalAccountId(Constants.PLATFORM_INTERNAL_ACCOUNT_ID);
        bill.setInputExternalAccountId(payChannel.getPlatformExternalAccountId());
        bill.setBusinessAmount(orderExt.getOrder().getTotalAmount());
        bill.setTransAmount(orderExt.getOrder().getRealTotalAmount());
        bill.setRemark(CharSequenceUtil.format("档口代发订单{}双，共计{}元",
                orderExt.getOrder().getGoodsQuantity(),
                orderExt.getOrder().getRealTotalAmount().toPlainString()));
        bill.setVersion(0L);
        bill.setDelFlag(Constants.UNDELETED);
        financeBillMapper.insert(bill);
        List<FinanceBillDetail> billDetails = new ArrayList<>(orderExt.getOrderDetails().size());
        for (StoreOrderDetail orderDetail : orderExt.getOrderDetails()) {
            FinanceBillDetail billDetail = new FinanceBillDetail();
            billDetail.setFinanceBillId(bill.getId());
            billDetail.setRelType(EFinBillDetailRelType.STORE_ORDER_DETAIL.getValue());
            billDetail.setRelId(orderDetail.getId());
            billDetail.setBusinessAmount(orderDetail.getTotalAmount());
            billDetail.setTransAmount(orderDetail.getRealTotalAmount());
            billDetail.setDelFlag(Constants.UNDELETED);
            financeBillDetailMapper.insert(billDetail);
            billDetails.add(billDetail);
        }
        TransInfo transInfo = TransInfo.builder()
                .srcBillId(bill.getId())
                .srcBillType(bill.getBillType())
                .transAmount(bill.getTransAmount())
                .transTime(new Date())
                .handlerId(null)
                .remark("订单支付完成")
                .build();
        //内部账户
        internalAccountService.addTransDetail(Constants.PLATFORM_INTERNAL_ACCOUNT_ID, transInfo,
                ELoanDirection.DEBIT, EEntryStatus.FINISH);
        //外部账户
        externalAccountService.addTransDetail(payChannel.getPlatformExternalAccountId(), transInfo,
                ELoanDirection.DEBIT, EEntryStatus.FINISH);
        return new FinanceBillExt(bill, billDetails);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public FinanceBillExt createOrderCompletedTransferBill(StoreOrderExt orderExt) {
        //TODO 需要先批量获取内部账户避免出现死锁？
        Assert.notNull(orderExt);
        FinanceBill bill = new FinanceBill();
        bill.setBillNo(generateBillNo(EFinBillType.TRANSFER));
        bill.setBillType(EFinBillType.TRANSFER.getValue());
        //直接标记成功
        bill.setBillStatus(EFinBillStatus.SUCCESS.getValue());
        bill.setSrcType(EFinBillSrcType.STORE_ORDER_COMPLETED.getValue());
        bill.setSrcId(orderExt.getOrder().getId());
        bill.setRelType(EFinBillRelType.STORE_ORDER.getValue());
        bill.setRelId(orderExt.getOrder().getId());
        //业务唯一键
        String businessUniqueKey = "STORE_ORDER_COMPLETED_" + orderExt.getOrder().getId();
        bill.setBusinessUniqueKey(businessUniqueKey);
        Long inputInternalAccountId = internalAccountService.getAccountAndCheck(orderExt.getOrder().getStoreId(),
                EAccountOwnerType.STORE).getId();
        bill.setInputInternalAccountId(inputInternalAccountId);
        bill.setOutputInternalAccountId(Constants.PLATFORM_INTERNAL_ACCOUNT_ID);
        bill.setBusinessAmount(orderExt.getOrder().getTotalAmount());
        bill.setTransAmount(orderExt.getOrder().getRealTotalAmount());
        bill.setRemark(CharSequenceUtil.format("档口代发订单{}双，共计{}元",
                orderExt.getOrder().getGoodsQuantity(),
                orderExt.getOrder().getRealTotalAmount().toPlainString()));
        bill.setVersion(0L);
        bill.setDelFlag(Constants.UNDELETED);
        financeBillMapper.insert(bill);
        List<FinanceBillDetail> billDetails = new ArrayList<>(orderExt.getOrderDetails().size());
        for (StoreOrderDetail orderDetail : orderExt.getOrderDetails()) {
            FinanceBillDetail billDetail = new FinanceBillDetail();
            billDetail.setFinanceBillId(bill.getId());
            billDetail.setRelType(EFinBillDetailRelType.STORE_ORDER_DETAIL.getValue());
            billDetail.setRelId(orderDetail.getId());
            billDetail.setBusinessAmount(orderDetail.getTotalAmount());
            billDetail.setTransAmount(orderDetail.getRealTotalAmount());
            billDetail.setDelFlag(Constants.UNDELETED);
            financeBillDetailMapper.insert(billDetail);
            billDetails.add(billDetail);
        }
        TransInfo transInfo = TransInfo.builder()
                .srcBillId(bill.getId())
                .srcBillType(bill.getBillType())
                .transAmount(bill.getTransAmount())
                .transTime(new Date())
                .handlerId(null)
                .remark("订单完成")
                .build();
        //内部账户
        internalAccountService.addTransDetail(Constants.PLATFORM_INTERNAL_ACCOUNT_ID, transInfo,
                ELoanDirection.CREDIT, EEntryStatus.FINISH);
        internalAccountService.addTransDetail(inputInternalAccountId, transInfo,
                ELoanDirection.DEBIT, EEntryStatus.FINISH);
        return new FinanceBillExt(bill, billDetails);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public FinanceBillExt createOrderCompletedTransferBill(StoreOrderExt orderExt,
                                                           List<StoreOrderExt> afterSaleOrderExts) {
        //TODO 需要先批量获取内部账户避免出现死锁？
        Assert.notNull(orderExt);
        FinanceBill bill = new FinanceBill();
        bill.setBillNo(generateBillNo(EFinBillType.TRANSFER));
        bill.setBillType(EFinBillType.TRANSFER.getValue());
        //直接标记成功
        bill.setBillStatus(EFinBillStatus.SUCCESS.getValue());
        bill.setSrcType(EFinBillSrcType.STORE_ORDER_COMPLETED.getValue());
        bill.setSrcId(orderExt.getOrder().getId());
        bill.setRelType(EFinBillRelType.STORE_ORDER.getValue());
        bill.setRelId(orderExt.getOrder().getId());
        //业务唯一键
        String businessUniqueKey = "STORE_ORDER_COMPLETED_" + orderExt.getOrder().getId();
        bill.setBusinessUniqueKey(businessUniqueKey);
        Long inputInternalAccountId = internalAccountService.getAccountAndCheck(orderExt.getOrder().getStoreId(),
                EAccountOwnerType.STORE).getId();
        bill.setInputInternalAccountId(inputInternalAccountId);
        bill.setOutputInternalAccountId(Constants.PLATFORM_INTERNAL_ACCOUNT_ID);

        List<String> afterSaleBillUks = new ArrayList<>(afterSaleOrderExts.size());
        Map<Long, StoreOrderDetail> afterSaleOrderDetailMap = new HashMap<>();
        for (StoreOrderExt afterSaleOrderExt : afterSaleOrderExts) {
            afterSaleBillUks.add("STORE_ORDER_REFUND_" + afterSaleOrderExt.getOrder().getId());
            for (StoreOrderDetail afterSaleOrderDetail : CollUtil.emptyIfNull(afterSaleOrderExt.getOrderDetails())) {
                afterSaleOrderDetailMap.put(afterSaleOrderDetail.getOriginOrderDetailId(), afterSaleOrderDetail);
            }
        }
        Map<Long, FinanceBill> refundPaymentBillMap = MapUtil.empty();
        Map<Long, FinanceBillDetail> refundPaymentBillDetailMap = MapUtil.empty();
        if (CollUtil.isNotEmpty(afterSaleBillUks)) {
            refundPaymentBillMap = financeBillMapper.selectList(
                    Wrappers.lambdaQuery(FinanceBill.class)
                            .in(FinanceBill::getBusinessUniqueKey, afterSaleBillUks)
                            .eq(SimpleEntity::getDelFlag, Constants.UNDELETED))
                    .stream()
                    .collect(Collectors.toMap(SimpleEntity::getId, Function.identity()));
            Assert.notEmpty(refundPaymentBillMap);
            refundPaymentBillDetailMap = financeBillDetailMapper.selectList(
                    Wrappers.lambdaQuery(FinanceBillDetail.class)
                            .in(FinanceBillDetail::getFinanceBillId, refundPaymentBillMap.keySet())
                            .eq(SimpleEntity::getDelFlag, Constants.UNDELETED)).stream()
                    .collect(Collectors.toMap(FinanceBillDetail::getRelId, Function.identity()));
        }
        BigDecimal businessAmount = BigDecimal.ZERO;
        BigDecimal transAmount = BigDecimal.ZERO;
        Integer goodsQuantity = 0;
        List<FinanceBillDetail> billDetails = new ArrayList<>(orderExt.getOrderDetails().size());
        for (StoreOrderDetail orderDetail : orderExt.getOrderDetails()) {
            FinanceBillDetail billDetail = new FinanceBillDetail();
            billDetail.setRelType(EFinBillDetailRelType.STORE_ORDER_DETAIL.getValue());
            billDetail.setRelId(orderDetail.getId());
            billDetail.setBusinessAmount(orderDetail.getTotalAmount());
            billDetail.setTransAmount(orderDetail.getRealTotalAmount());
            goodsQuantity += orderDetail.getGoodsQuantity();
            billDetail.setDelFlag(Constants.UNDELETED);
            billDetails.add(billDetail);
            StoreOrderDetail afterSaleOrderDetail = afterSaleOrderDetailMap.get(orderDetail.getId());
            if (afterSaleOrderDetail != null
                    && EPayStatus.PAID.getValue().equals(afterSaleOrderDetail.getPayStatus())) {
                //扣除已退款部分数量
                goodsQuantity -= afterSaleOrderDetail.getGoodsQuantity();
                //退款单据
                FinanceBillDetail refundPaymentBillDetail = refundPaymentBillDetailMap
                        .get(afterSaleOrderDetail.getId());
                Assert.notNull(refundPaymentBillDetail);
                FinanceBill refundPaymentBill = refundPaymentBillMap
                        .get(refundPaymentBillDetail.getFinanceBillId());
                Assert.notNull(refundPaymentBill);
                Assert.isTrue(EFinBillStatus.SUCCESS.getValue().equals(refundPaymentBill.getBillStatus()));
                //扣除已退款部分金额
                billDetail.setBusinessAmount(NumberUtil.sub(billDetail.getBusinessAmount(),
                        refundPaymentBillDetail.getBusinessAmount()));
                billDetail.setTransAmount(NumberUtil.sub(billDetail.getTransAmount(),
                        refundPaymentBillDetail.getTransAmount()));
            }
            businessAmount = NumberUtil.add(businessAmount, billDetail.getBusinessAmount());
            transAmount = NumberUtil.add(transAmount, billDetail.getTransAmount());
        }
        if (NumberUtil.equals(BigDecimal.ZERO, transAmount)) {
            log.info("订单[{}]已全部退款，完成时不再创建转移单", orderExt.getOrder().getId());
            return null;
        }
        bill.setBusinessAmount(businessAmount);
        bill.setTransAmount(transAmount);
        bill.setRemark(CharSequenceUtil.format("档口代发订单{}双，共计{}元", goodsQuantity,
                transAmount.toPlainString()));
        bill.setVersion(0L);
        bill.setDelFlag(Constants.UNDELETED);
        financeBillMapper.insert(bill);
        for (FinanceBillDetail billDetail : billDetails) {
            billDetail.setFinanceBillId(bill.getId());
            financeBillDetailMapper.insert(billDetail);
        }
        TransInfo transInfo = TransInfo.builder()
                .srcBillId(bill.getId())
                .srcBillType(bill.getBillType())
                .transAmount(bill.getTransAmount())
                .transTime(new Date())
                .handlerId(null)
                .remark("订单完成")
                .build();
        //内部账户
        internalAccountService.addTransDetail(Constants.PLATFORM_INTERNAL_ACCOUNT_ID, transInfo,
                ELoanDirection.CREDIT, EEntryStatus.FINISH);
        internalAccountService.addTransDetail(inputInternalAccountId, transInfo,
                ELoanDirection.DEBIT, EEntryStatus.FINISH);
        return new FinanceBillExt(bill, billDetails);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public FinanceBillExt createRefundOrderPaymentBill(StoreOrderExt orderExt) {
        Assert.notNull(orderExt);
        FinanceBill bill = new FinanceBill();
        bill.setBillNo(generateBillNo(EFinBillType.PAYMENT));
        bill.setBillType(EFinBillType.PAYMENT.getValue());
        //执行中
        bill.setBillStatus(EFinBillStatus.PROCESSING.getValue());
        bill.setSrcType(EFinBillSrcType.STORE_ORDER_REFUND.getValue());
        bill.setSrcId(orderExt.getOrder().getId());
        bill.setRelType(EFinBillRelType.STORE_ORDER.getValue());
        bill.setRelId(orderExt.getOrder().getId());
        //业务唯一键
        String businessUniqueKey = "STORE_ORDER_REFUND_" + orderExt.getOrder().getId();
        bill.setBusinessUniqueKey(businessUniqueKey);
        bill.setOutputInternalAccountId(Constants.PLATFORM_INTERNAL_ACCOUNT_ID);
        //售后订单金额以明细为准
        BigDecimal businessAmount = BigDecimal.ZERO;
        BigDecimal transferAmount = BigDecimal.ZERO;
        Integer goodsQuantity = 0;
        for (StoreOrderDetail orderDetail : orderExt.getOrderDetails()) {
            businessAmount = NumberUtil.add(businessAmount, orderDetail.getTotalAmount());
            transferAmount = NumberUtil.add(transferAmount, orderDetail.getRealTotalAmount());
            goodsQuantity += orderDetail.getGoodsQuantity();
        }
        bill.setBusinessAmount(businessAmount);
        bill.setTransAmount(transferAmount);
        bill.setRemark(CharSequenceUtil.format("档口代发退货{}双，共计{}元", goodsQuantity,
                transferAmount.toPlainString()));
        bill.setVersion(0L);
        bill.setDelFlag(Constants.UNDELETED);
        financeBillMapper.insert(bill);
        List<FinanceBillDetail> billDetails = new ArrayList<>(orderExt.getOrderDetails().size());
        for (StoreOrderDetail orderDetail : orderExt.getOrderDetails()) {
            FinanceBillDetail billDetail = new FinanceBillDetail();
            billDetail.setFinanceBillId(bill.getId());
            billDetail.setRelType(EFinBillDetailRelType.STORE_ORDER_DETAIL.getValue());
            billDetail.setRelId(orderDetail.getId());
            billDetail.setBusinessAmount(orderDetail.getTotalAmount());
            billDetail.setTransAmount(orderDetail.getRealTotalAmount());
            billDetail.setDelFlag(Constants.UNDELETED);
            financeBillDetailMapper.insert(billDetail);
            billDetails.add(billDetail);
        }
        TransInfo transInfo = TransInfo.builder()
                .srcBillId(bill.getId())
                .srcBillType(bill.getBillType())
                .transAmount(bill.getTransAmount())
                .transTime(new Date())
                .handlerId(null)
                .remark("订单售后退款")
                .build();
        //内部账户
        internalAccountService.addTransDetail(Constants.PLATFORM_INTERNAL_ACCOUNT_ID, transInfo,
                ELoanDirection.CREDIT, EEntryStatus.WAITING);
        return new FinanceBillExt(bill, billDetails);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void entryRefundOrderPaymentBill(Long storeOrderId) {
        Assert.notNull(storeOrderId);
        //业务唯一键
        String businessUniqueKey = "STORE_ORDER_REFUND_" + storeOrderId;
        FinanceBill financeBill = financeBillMapper.selectOne(Wrappers.lambdaQuery(FinanceBill.class)
                .eq(FinanceBill::getBusinessUniqueKey, businessUniqueKey));
        if (!BeanValidators.exists(financeBill)) {
            throw new ServiceException(CharSequenceUtil.format("售后订单[{}]对应付款单不存在", storeOrderId));
        }
        if (!EFinBillType.PAYMENT.getValue().equals(financeBill.getBillType())
                || !EFinBillSrcType.STORE_ORDER_REFUND.getValue().equals(financeBill.getSrcType())) {
            throw new ServiceException(CharSequenceUtil.format("单据[{}]类型异常", financeBill.getId()));
        }
        if (!EFinBillStatus.PROCESSING.getValue().equals(financeBill.getBillStatus())) {
            throw new ServiceException(CharSequenceUtil.format("付款单[{}]状态异常", financeBill.getId()));
        }
        financeBill.setBillStatus(EFinBillStatus.SUCCESS.getValue());
        int r = financeBillMapper.updateById(financeBill);
        if (r == 0) {
            throw new ServiceException(Constants.VERSION_LOCK_ERROR_COMMON_MSG);
        }
        //内部账户入账
        internalAccountService.entryTransDetail(financeBill.getId(), EFinBillType.of(financeBill.getBillType()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public FinanceBillExt createWithdrawPaymentBill(Long storeId, BigDecimal amount, EPayChannel payChannel) {
        Assert.notNull(storeId);
        Assert.isTrue(NumberUtil.isLessOrEqual(amount, BigDecimal.ZERO), "提现金额异常");
        Assert.notNull(payChannel);
        FinanceBill bill = new FinanceBill();
        bill.setBillNo(generateBillNo(EFinBillType.PAYMENT));
        bill.setBillType(EFinBillType.PAYMENT.getValue());
        //执行中
        bill.setBillStatus(EFinBillStatus.PROCESSING.getValue());
        bill.setSrcType(EFinBillSrcType.WITHDRAW.getValue());
        bill.setSrcId(storeId);
        //业务唯一键
        String businessUniqueKey = IdUtil.simpleUUID();
        bill.setBusinessUniqueKey(businessUniqueKey);
        Long outputInternalAccountId = internalAccountService.getAccountAndCheck(storeId, EAccountOwnerType.STORE)
                .getId();
        Long inputExternalAccountId = externalAccountService.getAccountAndCheck(storeId, EAccountOwnerType.STORE,
                EAccountType.getByChannel(payChannel)).getId();
        bill.setOutputInternalAccountId(outputInternalAccountId);
        bill.setInputExternalAccountId(inputExternalAccountId);
        bill.setOutputExternalAccountId(payChannel.getPlatformExternalAccountId());
        bill.setBusinessAmount(amount);
        bill.setTransAmount(amount);
        bill.setRemark(CharSequenceUtil.format("账户提现{}元", amount.toPlainString()));
        bill.setVersion(0L);
        bill.setDelFlag(Constants.UNDELETED);
        financeBillMapper.insert(bill);
        TransInfo transInfo = TransInfo.builder()
                .srcBillId(bill.getId())
                .srcBillType(bill.getBillType())
                .transAmount(bill.getTransAmount())
                .transTime(new Date())
                .handlerId(null)
                .remark("账户提现")
                .build();
        //内部账户
        internalAccountService.addTransDetail(outputInternalAccountId, transInfo,
                ELoanDirection.CREDIT, EEntryStatus.WAITING);
        //外部账户
        externalAccountService.addTransDetail(inputExternalAccountId, transInfo,
                ELoanDirection.DEBIT, EEntryStatus.WAITING);
        externalAccountService.addTransDetail(payChannel.getPlatformExternalAccountId(), transInfo,
                ELoanDirection.CREDIT, EEntryStatus.WAITING);
        return new FinanceBillExt(bill, ListUtil.empty());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void entryWithdrawPaymentBill(Long financeBillId) {
        Assert.notNull(financeBillId);
        //业务唯一键
        FinanceBill financeBill = financeBillMapper.selectById(financeBillId);
        if (!BeanValidators.exists(financeBill)) {
            throw new ServiceException(CharSequenceUtil.format("提现付款单[{}]不存在", financeBillId));
        }
        if (!EFinBillType.PAYMENT.getValue().equals(financeBill.getBillType())
                || !EFinBillSrcType.WITHDRAW.getValue().equals(financeBill.getSrcType())) {
            throw new ServiceException(CharSequenceUtil.format("单据[{}]类型异常", financeBillId));
        }
        if (!EFinBillStatus.PROCESSING.getValue().equals(financeBill.getBillStatus())) {
            throw new ServiceException(CharSequenceUtil.format("付款单[{}]状态异常", financeBill.getId()));
        }
        financeBill.setBillStatus(EFinBillStatus.SUCCESS.getValue());
        int r = financeBillMapper.updateById(financeBill);
        if (r == 0) {
            throw new ServiceException(Constants.VERSION_LOCK_ERROR_COMMON_MSG);
        }
        //内部账户入账
        internalAccountService.entryTransDetail(financeBill.getId(), EFinBillType.of(financeBill.getBillType()));
        //外部账户入账
        externalAccountService.entryTransDetail(financeBill.getId(), EFinBillType.of(financeBill.getBillType()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public FinanceBillExt createRechargeCollectionBill(Long storeId, BigDecimal amount, EPayChannel payChannel) {
        Assert.notNull(storeId);
        Assert.notNull(amount);
        Assert.notNull(payChannel);
        FinanceBill bill = new FinanceBill();
        String billNo = generateBillNo(EFinBillType.COLLECTION);
        bill.setBillNo(billNo);
        bill.setBillType(EFinBillType.COLLECTION.getValue());
        //直接标记成功
        bill.setBillStatus(EFinBillStatus.PROCESSING.getValue());
        bill.setSrcType(EFinBillSrcType.RECHARGE.getValue());
        bill.setBusinessUniqueKey(billNo);
        InternalAccount inputInternalAccount = internalAccountService.getAccount(storeId, EAccountOwnerType.STORE);
        Assert.notNull(inputInternalAccount, "未创建档口账户");
        bill.setInputInternalAccountId(inputInternalAccount.getId());
        bill.setInputExternalAccountId(payChannel.getPlatformExternalAccountId());
        bill.setBusinessAmount(amount);
        bill.setTransAmount(amount);
        bill.setRemark(CharSequenceUtil.format("档口充值{}元", amount.toPlainString()));
        bill.setVersion(0L);
        bill.setDelFlag(Constants.UNDELETED);
        financeBillMapper.insert(bill);
        TransInfo transInfo = TransInfo.builder()
                .srcBillId(bill.getId())
                .srcBillType(bill.getBillType())
                .transAmount(bill.getTransAmount())
                .transTime(new Date())
                .handlerId(null)
                .remark("档口充值")
                .build();
        //内部账户
        internalAccountService.addTransDetail(inputInternalAccount.getId(), transInfo,
                ELoanDirection.DEBIT, EEntryStatus.WAITING);
        //外部账户
        externalAccountService.addTransDetail(payChannel.getPlatformExternalAccountId(), transInfo,
                ELoanDirection.DEBIT, EEntryStatus.WAITING);
        return new FinanceBillExt(bill, ListUtil.empty());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void entryRechargeCollectionBill(String billNo) {
        Assert.notEmpty(billNo);
        //业务唯一键
        FinanceBill financeBill = financeBillMapper.selectOne(Wrappers.lambdaQuery(FinanceBill.class)
                .eq(FinanceBill::getBillNo, billNo));
        if (!BeanValidators.exists(financeBill)) {
            throw new ServiceException(CharSequenceUtil.format("充值收款单[{}]不存在", billNo));
        }
        if (!EFinBillType.COLLECTION.getValue().equals(financeBill.getBillType())
                || !EFinBillSrcType.RECHARGE.getValue().equals(financeBill.getSrcType())) {
            throw new ServiceException(CharSequenceUtil.format("单据[{}]类型异常", financeBill.getId()));
        }
        if (!EFinBillStatus.PROCESSING.getValue().equals(financeBill.getBillStatus())) {
            throw new ServiceException(CharSequenceUtil.format("收款单[{}]状态异常", financeBill.getId()));
        }
        financeBill.setBillStatus(EFinBillStatus.SUCCESS.getValue());
        int r = financeBillMapper.updateById(financeBill);
        if (r == 0) {
            throw new ServiceException(Constants.VERSION_LOCK_ERROR_COMMON_MSG);
        }
        //内部账户入账
        internalAccountService.entryTransDetail(financeBill.getId(), EFinBillType.of(financeBill.getBillType()));
        //外部账户入账
        externalAccountService.entryTransDetail(financeBill.getId(), EFinBillType.of(financeBill.getBillType()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public FinanceBillExt createInternalTransferBill(Long inputAccountId, Long outputAccountId, BigDecimal amount,
                                                     Integer srcType, Long srcId, Integer relType, Long relId,
                                                     String remark) {
        //TODO 需要先批量获取内部账户避免出现死锁？
        Assert.notNull(inputAccountId);
        Assert.notNull(outputAccountId);
        Assert.notNull(amount);
        FinanceBill bill = new FinanceBill();
        String billNo = generateBillNo(EFinBillType.TRANSFER);
        bill.setBillNo(billNo);
        bill.setBillType(EFinBillType.TRANSFER.getValue());
        //直接标记成功
        bill.setBillStatus(EFinBillStatus.SUCCESS.getValue());
        bill.setSrcType(srcType);
        bill.setSrcId(srcId);
        bill.setRelType(relType);
        bill.setRelId(relId);
        //业务唯一键
        bill.setBusinessUniqueKey(billNo);
        bill.setInputInternalAccountId(inputAccountId);
        bill.setOutputInternalAccountId(outputAccountId);
        bill.setBusinessAmount(amount);
        bill.setTransAmount(amount);
        bill.setRemark(remark);
        bill.setVersion(0L);
        bill.setDelFlag(Constants.UNDELETED);
        financeBillMapper.insert(bill);
        TransInfo transInfo = TransInfo.builder()
                .srcBillId(bill.getId())
                .srcBillType(bill.getBillType())
                .transAmount(bill.getTransAmount())
                .transTime(new Date())
                .handlerId(null)
                .remark(remark)
                .build();
        //内部账户
        internalAccountService.addTransDetail(outputAccountId, transInfo,
                ELoanDirection.CREDIT, EEntryStatus.FINISH);
        internalAccountService.addTransDetail(inputAccountId, transInfo,
                ELoanDirection.DEBIT, EEntryStatus.FINISH);
        return new FinanceBillExt(bill, ListUtil.empty());
    }

    @Override
    public List<FinanceBillDTO> listByStatus(EFinBillStatus billStatus, EFinBillType billType,
                                             EFinBillSrcType billSrcType, Integer count) {
        if (count != null) {
            PageHelper.startPage(1, count, false);
        }
        List<FinanceBill> doList = financeBillMapper.selectList(Wrappers.lambdaQuery(FinanceBill.class)
                .eq(FinanceBill::getBillStatus, Optional.ofNullable(billStatus).map(EFinBillStatus::getValue).orElse(null))
                .eq(FinanceBill::getBillType, Optional.ofNullable(billType).map(EFinBillType::getValue).orElse(null))
                .eq(FinanceBill::getSrcType, Optional.ofNullable(billSrcType).map(EFinBillSrcType::getValue).orElse(null))
                .eq(FinanceBill::getDelFlag, Constants.UNDELETED));
        return BeanUtil.copyToList(doList, FinanceBillDTO.class);
    }


    /**
     * 生成单号
     *
     * @param billType
     * @return
     */
    public String generateBillNo(EFinBillType billType) {
        //未确定规则，暂时用UUID代替
        return IdUtil.simpleUUID();
    }
}
