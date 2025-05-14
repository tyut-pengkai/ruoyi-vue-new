package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.order.StoreOrderOperationRecordAddDTO;
import com.ruoyi.xkt.dto.order.StoreOrderOperationRecordDTO;
import com.ruoyi.xkt.enums.EOrderAction;
import com.ruoyi.xkt.enums.EOrderTargetTypeAction;

import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-06 17:57
 */
public interface IOperationRecordService {

    /**
     * 添加订单操作记录
     *
     * @param recordList
     */
    void addOrderOperationRecords(List<StoreOrderOperationRecordAddDTO> recordList);

    /**
     * 获取一条操作记录
     *
     * @param targetId
     * @param targetType
     * @param action
     * @return
     */
    StoreOrderOperationRecordDTO getOneRecord(Long targetId, EOrderTargetTypeAction targetType, EOrderAction action);

}
