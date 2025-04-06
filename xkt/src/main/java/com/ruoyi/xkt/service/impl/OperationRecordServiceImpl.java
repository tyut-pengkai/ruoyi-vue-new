package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.ruoyi.xkt.domain.StoreOrderOperationRecord;
import com.ruoyi.xkt.dto.order.StoreOrderOperationRecordAddDTO;
import com.ruoyi.xkt.mapper.StoreOrderOperationRecordMapper;
import com.ruoyi.xkt.service.IOperationRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liangyq
 * @date 2025-04-06 17:58
 */
@Service
public class OperationRecordServiceImpl implements IOperationRecordService {

    @Autowired
    private StoreOrderOperationRecordMapper storeOrderOperationRecordMapper;

    @Override
    public void addOrderOperationRecords(List<StoreOrderOperationRecordAddDTO> recordList) {
        if (CollUtil.isEmpty(recordList)) {
            return;
        }
        List<StoreOrderOperationRecord> list = recordList.stream()
                .map(dto -> BeanUtil.toBean(dto, StoreOrderOperationRecord.class))
                .collect(Collectors.toList());
        storeOrderOperationRecordMapper.batchInsert(list);
    }

}
