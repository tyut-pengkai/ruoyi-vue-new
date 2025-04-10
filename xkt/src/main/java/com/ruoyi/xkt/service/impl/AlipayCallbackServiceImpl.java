package com.ruoyi.xkt.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.xkt.domain.AlipayCallback;
import com.ruoyi.xkt.domain.StoreOrder;
import com.ruoyi.xkt.dto.order.StoreOrderInfo;
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

    @Transactional
    @Override
    public int insertAlipayCallback(AlipayCallback alipayCallback) {
        Assert.notNull(alipayCallback);
        return alipayCallbackMapper.insert(alipayCallback);
    }

    @Transactional
    @Override
    public void processOrderPaid(AlipayCallback info) {
        //更新回调状态
        info.setProcessStatus(EProcessStatus.SUCCESS.getValue());
        alipayCallbackMapper.updateById(info);
        //更新订单状态
        StoreOrder order = storeOrderService.getByOrderNo(info.getOutTradeNo());
        Assert.notNull(order);
        StoreOrderInfo orderInfo = storeOrderService.paySuccess(order.getId(), info.getTotalAmount(),
                info.getReceiptAmount());
        //创建收款单
        financeBillService.createCollectionBillAfterOrderPaid(orderInfo, info.getId(), EPayChannel.ALI_PAY);
    }
}
