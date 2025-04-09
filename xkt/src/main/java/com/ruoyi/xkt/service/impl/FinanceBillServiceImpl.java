package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.xkt.domain.InternalAccount;
import com.ruoyi.xkt.dto.order.StoreOrderInfo;
import com.ruoyi.xkt.dto.finance.FinanceBillInfo;
import com.ruoyi.xkt.mapper.FinanceBillDetailMapper;
import com.ruoyi.xkt.mapper.FinanceBillMapper;
import com.ruoyi.xkt.service.IInternalAccountService;
import com.ruoyi.xkt.service.IFinanceBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Override
    public FinanceBillInfo createCollectionBillAfterOrderPaid(StoreOrderInfo orderInfo, String srcId) {
        //获取平台账户并加锁
        InternalAccount ca = internalAccountService.getWithLock(Constants.PLATFORM_INTERNAL_ACCOUNT_ID);
        return null;
    }
}
