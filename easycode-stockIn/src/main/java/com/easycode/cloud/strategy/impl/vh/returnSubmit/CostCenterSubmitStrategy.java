package com.easycode.cloud.strategy.impl.vh.returnSubmit;

import com.easycode.cloud.domain.vo.CostCenterReturnOrderVo;
import com.weifu.cloud.common.core.exception.ServiceException;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.weifu.cloud.common.core.utils.SpringUtils;
import com.weifu.cloud.common.security.utils.SecurityUtils;
import com.weifu.cloud.constant.ErrorMessage;
import com.weifu.cloud.constant.StockInTaskConstant;
import com.weifu.cloud.constant.TaskStatusConstant;
import com.easycode.cloud.domain.CostCenterReturnOrder;
import com.easycode.cloud.domain.CostCenterReturnOrderDetail;
import com.easycode.cloud.domain.RetTask;
import com.easycode.cloud.domain.dto.RetTaskDto;
import com.weifu.cloud.domian.vo.StoragePositionVo;
import com.easycode.cloud.mapper.CostCenterReturnOrderDetailMapper;
import com.easycode.cloud.mapper.CostCenterReturnOrderMapper;
import com.easycode.cloud.mapper.RetTaskMapper;
import com.easycode.cloud.service.impl.vh.VHRetTaskServiceImpl;
import com.easycode.cloud.strategy.IRetTaskStrategy;
import com.easycode.cloud.strategy.StockInCommonService;
import com.easycode.cloud.strategy.StockInSubmitStrategy;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @program: Weifu-WMS
 * @description: 生产订单发料退PDA提交策略类
 * @author: fangshucheng
 * @create: 2024-05-17 13:46
 **/
@Service
public class CostCenterSubmitStrategy implements StockInSubmitStrategy {


    @Autowired
    private Map<String, IRetTaskStrategy> map;

    @Autowired
    private CostCenterReturnOrderDetailMapper costCenterReturnOrderDetailMapper;

    @Autowired
    private CostCenterReturnOrderMapper costCenterReturnOrderMapper;

    @Autowired
    private RetTaskMapper retTaskMapper;

    @Resource
    private StockInCommonService stockInCommonService;

    @Override
    public Boolean checkType(String type) {
        return Objects.equals(type, StockInTaskConstant.COST_CENTER_RETURN_TYPE);
    }

    @Override
    public int submit(RetTaskDto retTaskDto, StoragePositionVo storagePositionVo) throws Exception {
        // 物料凭证
        String voucherNo = "";
        // 单据号
        String orderNoLog;
        // 行号
        String lineNoLog;
        //查询提交前任务
        RetTask beRetTask = retTaskMapper.selectRetTaskById(retTaskDto.getId());
        // 校验数量
        int flag = beRetTask.getQty().subtract(beRetTask.getCompleteQty()).compareTo(retTaskDto.getQty());
        if (flag < 0){
            throw new ServiceException("当前任务数量需<=未完成数量!");
        }

        //更新退货任务
        RetTask retTask = new RetTask();
        retTask.setId(retTaskDto.getId());
        retTask.setStorageLocationCode(retTaskDto.getStorageLocationCode());
        retTask.setAreaCode(retTaskDto.getAreaCode());
        retTask.setPositionNo(retTaskDto.getConfirmPosition());
        retTask.setCompleteQty(retTaskDto.getQty().add(beRetTask.getCompleteQty()));
        retTask.setOperationCompleteQty(retTaskDto.getOperationCompleteQty().add(beRetTask.getOperationCompleteQty()));
        retTask.setLot(retTaskDto.getLot());
        retTask.setTaskStatus(flag == 0 ? TaskStatusConstant.TASK_STATUS_COMPLETE : TaskStatusConstant.TASK_STATUS_PART_COMPLETE);
        retTask.setUpdateBy(SecurityUtils.getUsername());
        retTask.setUpdateTime(DateUtils.getNowDate());
        if (retTaskMapper.updateRetTask(retTask) <= 0) {
            throw new ServiceException("修改退货任务状态失败!");
        }

        //更新退货单 （除其他收货、交货单反冲）
        IRetTaskStrategy iRetTaskStrategy = map.get(retTaskDto.getTaskType());
        if (ObjectUtils.isEmpty(iRetTaskStrategy)){
            throw new ServiceException(ErrorMessage.UPDATEE_ORDER_STRATEGY_NOTHING);
        }
        if (iRetTaskStrategy.updateRetOrder(retTaskDto) <= 0){
            throw new ServiceException(ErrorMessage.UPDATEE_ORDER_FAIL);
        }

        // 成本中心退料
        // 明细信息
        CostCenterReturnOrderDetail detail = costCenterReturnOrderDetailMapper.selectCostCenterReturnOrderDetailById(retTaskDto.getDetailId());
        if (detail == null) {
            throw new ServiceException("成本中心退料获取明细信息失败！");
        }
        // 单据信息
        CostCenterReturnOrderVo costCenterReturnOrderVo = new CostCenterReturnOrderVo();
        costCenterReturnOrderVo.setOrderNo(detail.getReturnOrderNo());
        List<CostCenterReturnOrder> orderList = costCenterReturnOrderMapper.selectCostCenterReturnOrderList(costCenterReturnOrderVo);
        if (orderList == null || orderList.isEmpty()) {
            throw new ServiceException("成本中心退料获取单据信息失败！");
        }
        orderNoLog = detail.getReturnOrderNo();
        lineNoLog = detail.getLineNo();
        //更新台账
        SpringUtils.getBean(VHRetTaskServiceImpl.class).updateInventoryDetails(retTaskDto, storagePositionVo);
        // sap过账
        voucherNo = stockInCommonService.costReturnToSap(retTaskDto, detail, orderList.get(0));

        stockInCommonService.addTaskLog(retTaskDto, orderNoLog, lineNoLog, voucherNo);
        return HttpStatus.SC_OK;
    }
}
