package com.easycode.cloud.strategy;

import com.easycode.cloud.domain.CostCenterReturnOrder;
import com.easycode.cloud.domain.CostCenterReturnOrderDetail;
import com.easycode.cloud.domain.dto.RetTaskDto;

/**
 * @program: Weifu-WMS
 * @description: 入库退货提交策略共通类
 * @author: fangshucheng
 * @create: 2024-05-17 14:37
 **/

public interface StockInCommonService {
    void materialReversal(String voucherNo) throws Exception;

    void addTaskLog(RetTaskDto retTaskDto, String orderNoLog, String lineNoLog, String voucherNo) throws Exception;

    String costReturnToSap(RetTaskDto retTaskDto, CostCenterReturnOrderDetail detail, CostCenterReturnOrder order) throws Exception;

    void createShelfTask(RetTaskDto retTaskDto, String orderNo, Long inventoryId);

    String moveLocationSap(RetTaskDto retTask, String sourceLocationCode) throws Exception;
}
