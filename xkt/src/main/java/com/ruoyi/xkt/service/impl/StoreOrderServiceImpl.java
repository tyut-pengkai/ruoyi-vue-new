package com.ruoyi.xkt.service.impl;

import com.ruoyi.xkt.mapper.StoreOrderDetailMapper;
import com.ruoyi.xkt.mapper.StoreOrderExpressTrackMapper;
import com.ruoyi.xkt.mapper.StoreOrderMapper;
import com.ruoyi.xkt.mapper.StoreOrderOperationRecordMapper;
import com.ruoyi.xkt.service.IStoreOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liangyq
 * @date 2025-04-02 13:19
 */
@Service
public class StoreOrderServiceImpl implements IStoreOrderService {
    @Autowired
    private StoreOrderMapper storeOrderMapper;
    @Autowired
    private StoreOrderDetailMapper storeOrderDetailMapper;
    @Autowired
    private StoreOrderOperationRecordMapper storeOrderOperationRecordMapper;
    @Autowired
    private StoreOrderExpressTrackMapper storeOrderExpressTrackMapper;
}
