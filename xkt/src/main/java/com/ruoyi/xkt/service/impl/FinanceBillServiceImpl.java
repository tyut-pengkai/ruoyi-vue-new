package com.ruoyi.xkt.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.xkt.domain.FinanceBill;
import com.ruoyi.xkt.domain.FinanceBillDetail;
import com.ruoyi.xkt.domain.StoreOrderDetail;
import com.ruoyi.xkt.dto.finance.FinanceBillExt;
import com.ruoyi.xkt.dto.finance.TransInfo;
import com.ruoyi.xkt.dto.order.StoreOrderExt;
import com.ruoyi.xkt.enums.*;
import com.ruoyi.xkt.mapper.FinanceBillDetailMapper;
import com.ruoyi.xkt.mapper.FinanceBillMapper;
import com.ruoyi.xkt.service.IExternalAccountService;
import com.ruoyi.xkt.service.IFinanceBillService;
import com.ruoyi.xkt.service.IInternalAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-08 21:14
 */
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
        Long inputInternalAccountId = internalAccountService.getInternalAccount(orderExt.getOrder().getStoreId(),
                EAccountOwnerType.STORE).getId();
        bill.setInputInternalAccountId(inputInternalAccountId);
        bill.setOutputInternalAccountId(Constants.PLATFORM_INTERNAL_ACCOUNT_ID);
        bill.setBusinessAmount(orderExt.getOrder().getTotalAmount());
        bill.setTransAmount(orderExt.getOrder().getRealTotalAmount());
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
        for (StoreOrderDetail orderDetail : orderExt.getOrderDetails()) {
            businessAmount = NumberUtil.add(businessAmount, orderDetail.getTotalAmount());
            transferAmount = NumberUtil.add(transferAmount, orderDetail.getRealTotalAmount());
        }
        bill.setBusinessAmount(businessAmount);
        bill.setTransAmount(transferAmount);
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
        Long outputInternalAccountId = internalAccountService.getInternalAccount(storeId, EAccountOwnerType.STORE)
                .getId();
        Long inputExternalAccountId = externalAccountService.getExternalAccount(storeId, EAccountOwnerType.STORE,
                EAccountType.getByChannel(payChannel)).getId();
        bill.setOutputInternalAccountId(outputInternalAccountId);
        bill.setInputExternalAccountId(inputExternalAccountId);
        bill.setOutputExternalAccountId(payChannel.getPlatformExternalAccountId());
        bill.setBusinessAmount(amount);
        bill.setTransAmount(amount);
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
        //内部账户入账
        internalAccountService.entryTransDetail(financeBill.getId(), EFinBillType.of(financeBill.getBillType()));
        //外部账户入账
        externalAccountService.entryTransDetail(financeBill.getId(), EFinBillType.of(financeBill.getBillType()));
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
