package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.xkt.domain.InternalAccount;
import com.ruoyi.xkt.dto.order.StoreOrderInfo;
import com.ruoyi.xkt.dto.payment.PaymentBillInfo;
import com.ruoyi.xkt.mapper.PaymentBillDetailMapper;
import com.ruoyi.xkt.mapper.PaymentBillMapper;
import com.ruoyi.xkt.service.IInternalAccountService;
import com.ruoyi.xkt.service.IPaymentBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author liangyq
 * @date 2025-04-08 21:14
 */
@Service
public class PaymentBillServiceImpl implements IPaymentBillService {

    @Autowired
    private PaymentBillMapper paymentBillMapper;
    @Autowired
    private PaymentBillDetailMapper paymentBillDetailMapper;
    @Autowired
    private IInternalAccountService internalAccountService;

    @Transactional
    @Override
    public PaymentBillInfo createCollectionBillByOrder(StoreOrderInfo orderInfo) {
        InternalAccount ca = internalAccountService.getWithLock(Constants.PLATFORM_INTERNAL_ACCOUNT_ID);
        return null;
    }
}
