package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.ruoyi.xkt.domain.StoreOrderOperationRecord;
import com.ruoyi.xkt.dto.order.StoreOrderOperationRecordAddDTO;
import com.ruoyi.xkt.mapper.StoreOrderOperationRecordMapper;
import com.ruoyi.xkt.service.IOperationRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-06 17:58
 */
@Service
public class OperationRecordServiceImpl implements IOperationRecordService {

    @Autowired
    private StoreOrderOperationRecordMapper storeOrderOperationRecordMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addOrderOperationRecords(List<StoreOrderOperationRecordAddDTO> recordList) {
        if (CollUtil.isEmpty(recordList)) {
            return;
        }
        List<StoreOrderOperationRecord> list = BeanUtil.copyToList(recordList, StoreOrderOperationRecord.class);
        storeOrderOperationRecordMapper.batchInsert(list);
    }

}
