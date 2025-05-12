package com.easycode.cloud.strategy.impl.vh.returnSubmit;

import com.alibaba.fastjson.JSONObject;
import com.easycode.cloud.domain.RetTask;
import com.easycode.cloud.domain.SaleReturnOrder;
import com.easycode.cloud.domain.SaleReturnOrderDetail;
import com.easycode.cloud.domain.dto.RetTaskDto;
import com.easycode.cloud.mapper.PrdReturnOrderDetailMapper;
import com.easycode.cloud.mapper.RetTaskMapper;
import com.easycode.cloud.mapper.SaleReturnOrderDetailMapper;
import com.easycode.cloud.mapper.SaleReturnOrderMapper;
import com.easycode.cloud.service.impl.vh.VHRetTaskServiceImpl;
import com.easycode.cloud.strategy.IRetTaskStrategy;
import com.easycode.cloud.strategy.StockInCommonService;
import com.easycode.cloud.strategy.StockInSubmitStrategy;
import com.weifu.cloud.common.core.exception.ServiceException;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.weifu.cloud.common.core.utils.SpringUtils;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.security.utils.SecurityUtils;
import com.weifu.cloud.constant.*;
import com.weifu.cloud.domain.dto.SapMoveLocationParamsDto;
import com.weifu.cloud.domian.InventoryHistory;
import com.weifu.cloud.domian.StoragePosition;
import com.weifu.cloud.domian.TaskLog;
import com.weifu.cloud.domian.vo.StoragePositionVo;
import com.weifu.cloud.service.IMainDataService;
import com.weifu.cloud.service.ITaskService;
import com.weifu.cloud.service.SapInteractionService;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 销售发货退货
 *
 * @author gf
 * @date 2024-11-1342
 */
@Service
public class SaleRetSubmitStrategy implements StockInSubmitStrategy {

    @Autowired
    private Map<String, IRetTaskStrategy> map;

    @Autowired
    private SapInteractionService sapInteractionService;

    @Autowired
    private RetTaskMapper retTaskMapper;

    @Autowired
    private PrdReturnOrderDetailMapper prdReturnOrderDetailMapper;

    @Autowired
    private SaleReturnOrderDetailMapper orderDetailMapper;

    @Resource
    private StockInCommonService stockInCommonService;

    @Resource
    private IMainDataService iMainDataService;

    @Resource
    private SaleReturnOrderMapper saleReturnOrderMapper;

    @Autowired
    private ITaskService taskService;

    @Override
    public Boolean checkType(String type) {
        return Objects.equals(type, StockInTaskConstant.SALE_RETURN_TYPE);
    }

    @Override
    @GlobalTransactional
    public int submit(RetTaskDto retTaskDto, StoragePositionVo storagePositionVo) throws Exception {
        // 物料凭证
        String voucherNo = "";
        //更新退货任务
        RetTask retTask = new RetTask();
        retTask.setId(retTaskDto.getId());
        retTask.setTaskStatus(TaskStatusConstant.TASK_STATUS_COMPLETE);
        retTask.setUpdateBy(SecurityUtils.getUsername());
        retTask.setUpdateTime(DateUtils.getNowDate());
        if (retTaskMapper.updateRetTask(retTask) <= 0) {
            throw new ServiceException("修改退货任务状态失败!");
        }

        // 单据明细表信息
        SaleReturnOrderDetail detail = orderDetailMapper.selectSaleReturnOrderDetailById(retTaskDto.getDetailId());
        RetTask retTask1 = new RetTask();
        retTask1.setStockinOrderNo(retTaskDto.getStockinOrderNo());
        List<RetTask> retTasks = retTaskMapper.selectRetTaskList(retTask1);
        SaleReturnOrder saleReturnOrder = saleReturnOrderMapper.selectSaleReturnOrderByOrderNo(retTaskDto.getStockinOrderNo());
        saleReturnOrder.setUpdateBy(SecurityUtils.getUsername());
        saleReturnOrder.setUpdateTime(DateUtils.getNowDate());
        saleReturnOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_COMPLETE);
        Boolean flag = true;
        for(RetTask e:retTasks){
            if(e.getTaskStatus().equals(TaskStatusConstant.TASK_STATUS_NEW)&&e.getId().compareTo(retTaskDto.getId())!=0){
                saleReturnOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_PART_COMPLETE);
                flag = false;
                break;
            }
        }
        saleReturnOrderMapper.updateSaleReturnOrder(saleReturnOrder);
//        if (detail == null) {
//            throw new ServiceException("获取单据明细表信息信息失败！");
//        }
//
//        //更新退货单
//        IRetTaskStrategy iRetTaskStrategy = map.get(retTaskDto.getTaskType());
//        if (ObjectUtils.isEmpty(iRetTaskStrategy)){
//            throw new ServiceException(ErrorMessage.UPDATEE_ORDER_STRATEGY_NOTHING);
//        }
//        if (iRetTaskStrategy.updateRetOrder(retTaskDto) <= 0){
//            throw new ServiceException(ErrorMessage.UPDATEE_ORDER_FAIL);
//        }
//
//        if(!retTaskDto.getSubmit()){
//            return HttpStatus.SC_OK;
//        }

        // 销售退的过账

        AjaxResult ajaxResult = iMainDataService.getPositionInfoById(retTaskDto.getPositionId());
        if (ajaxResult.isError()) {
            throw new ServiceException(ajaxResult.get("msg").toString());
        }
        storagePositionVo = JSONObject.parseObject(JSONObject.toJSONString(ajaxResult.get("data")), StoragePositionVo.class);
        if (ObjectUtils.isEmpty(storagePositionVo)) {
            throw new ServiceException(ErrorMessage.POSITION_CODE_NOTHING);
        }
        if("1".equals(storagePositionVo.getMixedFlag())){
            Long[] ids = {storagePositionVo.getId()};
            iMainDataService.updateStoragePositionStatus(ids,"0");
        }
        //更新台账
        SpringUtils.getBean(VHRetTaskServiceImpl.class).updateInventoryDetails(retTaskDto, storagePositionVo);
        // 新增记录
        addTaskLog(retTaskDto, detail.getReturnOrderNo(), detail.getLineNo(), voucherNo,detail.getFactoryCode());
        if(flag){
            SapMoveLocationParamsDto paramsDto = new SapMoveLocationParamsDto();
            paramsDto.setAufnr(saleReturnOrder.getSaleCode());
            sapInteractionService.IwmsDelivery(paramsDto);
        }
        return HttpStatus.SC_OK;
    }

    public void addTaskLog(RetTaskDto retTaskDto, String orderNoLog, String lineNoLog, String voucherNo,String factoryCode) throws Exception {
        //同步添加任务记录
        TaskLog taskLog = new TaskLog();
        taskLog.setTaskNo(retTaskDto.getTaskNo());
        taskLog.setTaskType(retTaskDto.getTaskType());
        taskLog.setMaterialNo(retTaskDto.getMaterialNo());
        taskLog.setOldMaterialNo(retTaskDto.getOldMatrialName());
        taskLog.setMaterialName(retTaskDto.getMaterialName());
        taskLog.setLot(retTaskDto.getLot());
        taskLog.setQty(retTaskDto.getQty());
        taskLog.setOperationQty(retTaskDto.getOperationCompleteQty());
        taskLog.setUnit(retTaskDto.getUnit());
        taskLog.setOperationUnit(retTaskDto.getOperationUnit());
        taskLog.setFactoryCode(SecurityUtils.getComCode());
        taskLog.setTargetLocationCode(retTaskDto.getStorageLocationCode());
        taskLog.setTargetAreaCode(retTaskDto.getAreaCode());
        taskLog.setTargetPositionCode(retTaskDto.getConfirmPosition());
        taskLog.setTargetFactoryCode(factoryCode);
        if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(retTaskDto.getStorageLocation())) {
            taskLog.setLocationCode(retTaskDto.getStorageLocation());
        }
        taskLog.setOrderType(TaskLogTypeConstant.FIN_RETURN);
        taskLog.setOrderNo(orderNoLog);
        taskLog.setLineNo(lineNoLog);
        taskLog.setIsQc(retTaskDto.getIsQc());
        taskLog.setIsFreeze(CommonYesOrNo.NO);
        taskLog.setIsConsign(CommonYesOrNo.NO);
        taskLog.setSupplierCode(retTaskDto.getSupplierCode());
        taskLog.setMaterialCertificate(voucherNo);
        taskLog.setPositionCode(retTaskDto.getPositionNo());
        taskLog.setLocationCode(retTaskDto.getStorageLocationCode());
        taskLog.setTenantId(retTaskDto.getTenantId());
        taskLog.setCreateTime(DateUtils.getNowDate());
        taskLog.setCreateBy(SecurityUtils.getUsername());
        if (taskService.add(taskLog).isError()) {
            throw new ServiceException("新增任务记录失败！");
        }
    }

}
