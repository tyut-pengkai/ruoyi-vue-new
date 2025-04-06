package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.order.StoreOrderOperationRecordAddDTO;

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

}
