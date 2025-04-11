package com.ruoyi.xkt.service.impl;

import cn.hutool.core.util.IdUtil;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.xkt.domain.FinanceBill;
import com.ruoyi.xkt.domain.FinanceBillDetail;
import com.ruoyi.xkt.domain.StoreOrderDetail;
import com.ruoyi.xkt.dto.finance.FinanceBillInfo;
import com.ruoyi.xkt.dto.finance.TransInfo;
import com.ruoyi.xkt.dto.order.StoreOrderExt;
import com.ruoyi.xkt.enums.*;
import com.ruoyi.xkt.mapper.FinanceBillDetailMapper;
import com.ruoyi.xkt.mapper.FinanceBillMapper;
import com.ruoyi.xkt.service.IExternalAccountService;
import com.ruoyi.xkt.service.IFinanceBillService;
import com.ruoyi.xkt.service.IInternalAccountService;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Override
    public FinanceBillInfo createCollectionBillAfterOrderPaid(StoreOrderExt orderExt, Long payId,
                                                              EPayChannel payChannel) {
        Assert.notNull(orderExt);
        Assert.notNull(payId);
        Assert.notNull(payChannel);
        //获取平台内部账户并加锁
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
        return new FinanceBillInfo(bill, billDetails);
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
