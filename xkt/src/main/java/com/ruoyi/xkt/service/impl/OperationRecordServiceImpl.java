package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.ruoyi.xkt.domain.StoreOrderOperationRecord;
import com.ruoyi.xkt.dto.order.StoreOrderOperationRecordAddDTO;
import com.ruoyi.xkt.dto.order.StoreOrderOperationRecordDTO;
import com.ruoyi.xkt.enums.EOrderAction;
import com.ruoyi.xkt.enums.EOrderTargetTypeAction;
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

    @Override
    public StoreOrderOperationRecordDTO getOneRecord(Long targetId, EOrderTargetTypeAction targetType,
                                                     EOrderAction action) {
        if (targetId == null || targetType == null || action == null) {
            return null;
        }
        PageHelper.startPage(1, 1, false);
        List<StoreOrderOperationRecord> records = storeOrderOperationRecordMapper.selectList(
                Wrappers.lambdaQuery(StoreOrderOperationRecord.class)
                        .eq(StoreOrderOperationRecord::getTargetId, targetId)
                        .eq(StoreOrderOperationRecord::getTargetType, targetType.getValue())
                        .eq(StoreOrderOperationRecord::getAction, action.getValue())
        );
        return BeanUtil.toBean(CollUtil.getFirst(records), StoreOrderOperationRecordDTO.class);
    }

}
