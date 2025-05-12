package com.easycode.cloud.mapper;

import com.easycode.cloud.domain.DeliveryInspectionTask;
import com.easycode.cloud.domain.dto.DeliveryInspectionTaskDto;
import com.easycode.cloud.domain.vo.DeliveryInspectionTaskVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 送检任务Mapper接口
 *
 * @author weifu
 * @date 2023-03-29
 */
@Repository
public interface DeliveryInspectionTaskMapper
{

    /**
     * 新增送检任务
     * @param deliveryInspectionTask
     * @return
     */
    int insertDeliveryInspectionTask(DeliveryInspectionTask deliveryInspectionTask);

    List<DeliveryInspectionTaskVo> selectInspectTaskList(DeliveryInspectionTaskDto deliveryInspectionTask);

    List<DeliveryInspectionTaskVo> getAllInspectionTask(DeliveryInspectionTaskDto deliveryInspectionTask);
}

