package com.easycode.cloud.service;

import com.alibaba.fastjson.JSONObject;
import com.easycode.cloud.domain.dto.DeliveryInspectionTaskDto;
import com.easycode.cloud.domain.vo.DeliveryInspectionTaskVo;

import java.util.List;

public interface IInspectTaskService {

    /**
     * 检验任务列表查询
     * @param deliveryInspectionTask
     * @return
     */
    List<DeliveryInspectionTaskVo> selectInspectTaskList(DeliveryInspectionTaskDto deliveryInspectionTask);

    /**
     *获取所有的检验任务
     * @param deliveryInspectionTask
     * @return
     */
    List<DeliveryInspectionTaskVo> getAllInspectionTask(DeliveryInspectionTaskDto deliveryInspectionTask);

    /**
     * 打印送检任务
     * @param inspectionTask
     * @return 处理结果
     */
    JSONObject printInspectTask(DeliveryInspectionTaskDto inspectionTask);
}
