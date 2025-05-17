package com.ruoyi.xkt.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.SimpleEntity;
import com.ruoyi.xkt.domain.AlipayCallback;
import com.ruoyi.xkt.domain.StoreOrder;
import com.ruoyi.xkt.dto.order.StoreOrderExt;
import com.ruoyi.xkt.enums.EAlipayCallbackBizType;
import com.ruoyi.xkt.enums.EPayChannel;
import com.ruoyi.xkt.enums.EProcessStatus;
import com.ruoyi.xkt.mapper.AlipayCallbackMapper;
import com.ruoyi.xkt.service.IAlipayCallbackService;
import com.ruoyi.xkt.service.IFinanceBillService;
import com.ruoyi.xkt.service.IStoreOrderService;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-08 17:40
 */
@Service
public class AlipayCallbackServiceImpl implements IAlipayCallbackService {

    @Autowired
    private AlipayCallbackMapper alipayCallbackMapper;
    @Autowired
    private IStoreOrderService storeOrderService;
    @Autowired
    private IFinanceBillService financeBillService;


    @Override
    public AlipayCallback getByNotifyId(String notifyId) {
        Assert.notNull(notifyId);
        return alipayCallbackMapper.selectOne(Wrappers.lambdaQuery(AlipayCallback.class)
                .eq(AlipayCallback::getNotifyId, notifyId));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertAlipayCallback(AlipayCallback alipayCallback) {
        Assert.notNull(alipayCallback);
        return alipayCallbackMapper.insert(alipayCallback);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void processOrderPaid(AlipayCallback info) {
        //更新回调状态
        info.setProcessStatus(EProcessStatus.SUCCESS.getValue());
        alipayCallbackMapper.updateById(info);
        //更新订单状态
        StoreOrder order = storeOrderService.getByOrderNo(info.getOutTradeNo());
        Assert.notNull(order);
        StoreOrderExt orderExt = storeOrderService.paySuccess(order.getId(), info.getTradeNo(), info.getTotalAmount(),
                info.getReceiptAmount());
        //创建收款单
        financeBillService.createOrderPaidCollectionBill(orderExt, info.getId(), EPayChannel.ALI_PAY);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void processRecharge(AlipayCallback info) {
        //更新回调状态
        info.setProcessStatus(EProcessStatus.SUCCESS.getValue());
        alipayCallbackMapper.updateById(info);
        //收款单到账
        financeBillService.entryRechargeCollectionBill(info.getOutTradeNo());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void noNeedProcess(AlipayCallback info) {
        info.setProcessStatus(EProcessStatus.NO_PROCESSING.getValue());
        alipayCallbackMapper.updateById(info);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void continueProcess(int count) {
        PageHelper.startPage(1, count, false);
        List<AlipayCallback> infoList = alipayCallbackMapper.selectList(Wrappers.lambdaQuery(AlipayCallback.class)
                .eq(AlipayCallback::getProcessStatus, EProcessStatus.INIT.getValue())
                .eq(SimpleEntity::getDelFlag, Constants.UNDELETED));
        for (AlipayCallback info : infoList) {
            if (!"TRADE_SUCCESS".equals(info.getTradeStatus())) {
                //非交易支付成功的回调不处理
                noNeedProcess(info);
                continue;
            }
            if (info.getRefundFee() != null && !NumberUtil.equals(info.getRefundFee(), BigDecimal.ZERO)) {
                //如果有退款金额，可能是部分退款的回调，这里不做处理
                noNeedProcess(info);
                continue;
            }
            if (EAlipayCallbackBizType.ORDER_PAY.getValue().equals(info.getBizType())) {
                processOrderPaid(info);
            } else if (EAlipayCallbackBizType.RECHARGE.getValue().equals(info.getBizType())) {
                processRecharge(info);
            } else {
                noNeedProcess(info);
            }
        }

    }
}
