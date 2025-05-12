package com.easycode.cloud.strategy.impl.vh.returnSubmit;

import com.alibaba.fastjson.JSON;
import com.easycode.cloud.domain.PrdReturnOrderDetail;
import com.easycode.cloud.domain.RetTask;
import com.easycode.cloud.domain.RwmOutSourceReturnOrder;
import com.easycode.cloud.domain.RwmOutSourceReturnOrderDetail;
import com.easycode.cloud.domain.dto.RetTaskDto;
import com.easycode.cloud.domain.vo.RwmOutSourceReturnOrderVo;
import com.easycode.cloud.mapper.PrdReturnOrderDetailMapper;
import com.easycode.cloud.mapper.RetTaskMapper;
import com.easycode.cloud.mapper.RwmOutSourceReturnOrderDetailMapper;
import com.easycode.cloud.mapper.RwmOutSourceReturnOrderMapper;
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
import com.weifu.cloud.domain.dto.SapOutSourceReturnParamsDto;
import com.weifu.cloud.domian.InventoryDetails;
import com.weifu.cloud.domian.ReserveStorage;
import com.weifu.cloud.domian.TaskLog;
import com.weifu.cloud.domian.vo.InventoryDetailsVo;
import com.weifu.cloud.domian.vo.StoragePositionVo;
import com.weifu.cloud.service.*;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * 原材料委外发退货
 *
 * @author gf
 * @date 2024-11-1342
 */
@Service
public class RwmOutsourceRetSubmitStrategy implements StockInSubmitStrategy {

    @Autowired
    private Map<String, IRetTaskStrategy> map;

    @Autowired
    private SapInteractionService sapInteractionService;

    @Autowired
    private RetTaskMapper retTaskMapper;

    @Autowired
    private PrdReturnOrderDetailMapper prdReturnOrderDetailMapper;

    @Resource
    private StockInCommonService stockInCommonService;

    @Autowired
    private RwmOutSourceReturnOrderDetailMapper rwmOutSourceReturnOrderDetailMapper;

    @Autowired
    private RwmOutSourceReturnOrderMapper rwmOutSourceReturnOrderMapper;

    @Autowired
    private IInStoreService inStoreService;

    @Autowired
    private IStockOutService iStockOutService;

    @Autowired
    private IMainDataService mainDataService;

    @Autowired
    private ITaskService taskService;

    private static final Logger logger = LoggerFactory.getLogger(RwmOutsourceRetSubmitStrategy.class);

    private final String yyyyMMdd = "yyyyMMdd";



    @Override
    public Boolean checkType(String type) {
        return Objects.equals(type, StockInTaskConstant.RWM_OUTSOURCE_RETURN_TYPE);
    }

    @Override
    public int submit(RetTaskDto retTaskDto, StoragePositionVo storagePositionVo) throws Exception {
        // 物料凭证
        String voucherNo = "";

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

        if (TaskTypeConstant.OUT_SOURCE_RETURN_TASK.equals(retTaskDto.getTaskType())){
            dealOutSouceReturnToSap(retTaskDto, storagePositionVo, voucherNo);
            return HttpStatus.SC_OK;
        }

        // 生产订单退料
        // 生产订单发料明细信息
        PrdReturnOrderDetail detail = prdReturnOrderDetailMapper.selectPrdReturnOrderDetailById(retTaskDto.getDetailId());
        if (detail == null) {
            throw new ServiceException("获取生产订单退料明细信息失败！");
        }
        //更新台账
        SpringUtils.getBean(VHRetTaskServiceImpl.class).updateInventoryDetails(retTaskDto, storagePositionVo);

        // 原材料委外发料退过账
        SapMoveLocationParamsDto paramsDto = new SapMoveLocationParamsDto();
        Date now = new Date();
        paramsDto.setBudat(DateFormatUtils.format(now, "yyyyMMdd"));
        paramsDto.setBldat(DateFormatUtils.format(now, "yyyyMMdd"));
        paramsDto.setBktxt("");
        paramsDto.setuName(SecurityUtils.getUsername());
        paramsDto.setPlant(SecurityUtils.getComCode());
        paramsDto.setMaterial(retTaskDto.getMaterialNo());
        paramsDto.setStgeLoc(retTaskDto.getStorageLocation());
        paramsDto.setMoveType(SapReqOrResConstant.OUT_SOURCED_MOVE_TYPE);
        paramsDto.setBatch(retTaskDto.getLot());
        paramsDto.setSpecStock("");
        paramsDto.setVendor("");
        paramsDto.setMovePlant(SecurityUtils.getComCode());
        paramsDto.setMoveMat(retTaskDto.getMaterialNo());
        paramsDto.setMoveStloc(retTaskDto.getStorageLocationCode());
        paramsDto.setMoveBatch(retTaskDto.getLot());
        paramsDto.setEntryQnt(retTaskDto.getQty().toString());
        paramsDto.setEntryUom(retTaskDto.getUnit());
        paramsDto.setEntryUomIso("");
        paramsDto.setNoTransferReq("");
        sapInteractionService.IwmsWwth(paramsDto);

        stockInCommonService.addTaskLog(retTaskDto, detail.getReturnOrderNo(), detail.getLineNo(), voucherNo);
        return HttpStatus.SC_OK;
    }

    @GlobalTransactional(timeoutMills = 15 * 10000)
    public void dealOutSouceReturnToSap(RetTaskDto retTaskDto, StoragePositionVo storagePositionVo, String voucherNo) {
        RwmOutSourceReturnOrderDetail detail = rwmOutSourceReturnOrderDetailMapper.selectRwmOutSourceReturnOrderDetailById(retTaskDto.getDetailId());
        if (detail == null) {
            throw new ServiceException("委外退料获取明细信息失败！");
        }
        if (StringUtils.isEmpty(detail.getSupplierCode())){
            throw new ServiceException("委外退料明细,确实必要的供应商信息！ " + detail.getReturnOrderNo());
        }
        // 单据信息
        RwmOutSourceReturnOrderVo sourceReturnOrder = new RwmOutSourceReturnOrderVo();
        sourceReturnOrder.setOrderNo(detail.getReturnOrderNo());
        List<RwmOutSourceReturnOrder> orderList = rwmOutSourceReturnOrderMapper.selectRwmOutSourceReturnOrderList(sourceReturnOrder);
        if (orderList == null || orderList.size() == 0) {
            throw new ServiceException("委外出库获取单据不存在！！！ 请核实！！！！");
        }
        InventoryDetailsVo inventory = updateInventoryDetailsByOutSourceReturn(retTaskDto, storagePositionVo , detail);
        // sap过账
        try {
            voucherNo = outSourceReturnToSap(retTaskDto, detail, orderList.get(0) , inventory);
            // 释放仓位
            Long[] ids = {retTaskDto.getPositionId()};
            mainDataService.updateStoragePositionStatus(ids, "0");

            String returnOrderNo = detail.getReturnOrderNo();
            RwmOutSourceReturnOrder rwmOutSourceReturnOrder = orderList.get(0);
            RwmOutSourceReturnOrder update = new RwmOutSourceReturnOrder();
            update.setOrderNo(returnOrderNo);
            update.setOrderStatus(OrderStatusConstant.ORDER_STATUS_COMPLETE);
            update.setUpdateTime(DateUtils.getNowDate());
            update.setUpdateBy(SecurityUtils.getUsername());
            update.setId(rwmOutSourceReturnOrder.getId());
            rwmOutSourceReturnOrderMapper.updateRwmOutSourceReturnOrder(update);

            Long id = detail.getId();
            RwmOutSourceReturnOrderDetail updateRwmOutSourceReturnOrder = new RwmOutSourceReturnOrderDetail();
            updateRwmOutSourceReturnOrder.setId(id);
            updateRwmOutSourceReturnOrder.setStatus("2");
            updateRwmOutSourceReturnOrder.setUpdateTime(DateUtils.getNowDate());
            updateRwmOutSourceReturnOrder.setUpdateBy(SecurityUtils.getUsername());
            rwmOutSourceReturnOrderDetailMapper.updateRwmOutSourceReturnOrderDetail(updateRwmOutSourceReturnOrder);
            List<String> taskNos = Lists.newArrayList();
            taskNos.add(retTaskDto.getTaskNo());
            inStoreService.removeByTaskNos(taskNos);
        } catch (Exception e) {
            if (!StringUtils.isEmpty(voucherNo)){
                iStockOutService.materialReversal(voucherNo);
                throw new ServiceException("sap 过账处理异常: " + e.getMessage());
            }
        }
    }


    /**
     * 委外出库单退货
     */
    public String outSourceReturnToSap(RetTaskDto retTaskDto, RwmOutSourceReturnOrderDetail detail, RwmOutSourceReturnOrder order , InventoryDetailsVo inventoryDetails) throws Exception{

        SapOutSourceReturnParamsDto sp = new SapOutSourceReturnParamsDto();
        Date now = new Date();
        sp.setBudat(DateFormatUtils.format(now, yyyyMMdd));
        sp.setBldat(DateFormatUtils.format(now, yyyyMMdd));
        sp.setBktxt("");
        sp.setuName(SecurityUtils.getUsername());

        // 物料
        sp.setPlant(inventoryDetails.getFactoryCode());
        sp.setMaterial(inventoryDetails.getMaterialNo());
        sp.setStgeLoc(retTaskDto.getStorageLocationCode());
        sp.setBatch(retTaskDto.getLot());
        sp.setSpecStock("");
        sp.setMoveType(SapReqOrResConstant.OUT_SOURCED_MOVE_TYPE_RETURN);

        String padded_s = String.format("%010d", Integer.parseInt(detail.getSupplierCode()));
        sp.setVendor(padded_s);
        sp.setMovePlant("");
        sp.setMoveMat("");
        sp.setMoveStloc("");
        sp.setMoveBatch("");
        sp.setEntryQnt(retTaskDto.getQty());
        sp.setEntryUom(retTaskDto.getUnit());
        sp.setNoTransferReq("");
        sp.setWbsElem("");
        sp.setNetwork("");
        sp.setItemText(retTaskDto.getTaskNo());
        sp.setSalesOrd("");
        sp.setValSOrdItem("");
        sp.setXstob("X");
        logger.info("发送 sap data :{} " , JSON.toJSONString(sp));
        return sapInteractionService.outSourceReturnToSap(sp);
    }


    /**
     * 更新台账
     *  @param retTaskDto
     * @param storagePositionVo
     * @param detail
     */
    @GlobalTransactional(timeoutMills = 15 * 10000 )
    public InventoryDetailsVo updateInventoryDetailsByOutSourceReturn(RetTaskDto retTaskDto, StoragePositionVo storagePositionVo, RwmOutSourceReturnOrderDetail detail) {
        InventoryDetailsVo inventory = null;
        //直接查预定表台账
        ReserveStorage reserveStorage = new ReserveStorage();
        reserveStorage.setTaskNo(retTaskDto.getTaskNo());
        AjaxResult revertResult = inStoreService.getReserveStorage(reserveStorage);
        if (revertResult.isError()) {
            throw new ServiceException("查询预定台账失败！");
        }

        List<ReserveStorage> reserveStorages = com.alibaba.fastjson.JSON.parseArray(com.alibaba.fastjson.JSON.toJSONString(revertResult.get("rows")), ReserveStorage.class);

        if (CollectionUtils.isEmpty(reserveStorages)) {
            throw new ServiceException("查询预定台账信息为空; 请核实 ！！！");
        }

        for (ReserveStorage reserveStorage1 : reserveStorages) {
            AjaxResult ajaxResult = inStoreService.inventoryDetail(reserveStorage1.getInventoryId());
            if (ajaxResult.isError()) {
                throw new ServiceException("查询可用台账失败！" + ajaxResult.get("msg"));
            }
            //回查台账
            inventory = com.alibaba.fastjson2.JSON.parseObject(com.alibaba.fastjson2.JSON.toJSONString(ajaxResult.get("data")), InventoryDetailsVo.class);
            inventory.setInventoryQty(inventory.getInventoryQty().subtract(reserveStorage1.getReserveQty()));
            inventory.setAvailableQty(inventory.getAvailableQty().subtract(reserveStorage1.getReserveQty()));
            inventory.setUpdateTime(DateUtils.getNowDate());
            inventory.setUpdateBy(SecurityUtils.getUsername());

            String supplierCode = getSupplierCode(retTaskDto);
            //新增
            InventoryDetails insert = new InventoryDetails();
            BeanUtils.copyProperties(inventory,insert);
            insert.setInventoryQty(reserveStorage1.getReserveQty());
            insert.setAvailableQty(reserveStorage1.getReserveQty());
            insert.setPositionNo(retTaskDto.getPositionNo());
            insert.setPositionId(retTaskDto.getPositionId());
            insert.setId(null);
            insert.setUpdateTime(null);
            insert.setCreateBy(SecurityUtils.getUsername());
            insert.setCreateTime(DateUtils.getNowDate());
            insert.setStockSpecType(null);
            insert.setStockSpecFlag(CommonYesOrNo.NO);
            insert.setIsQc(CommonYesOrNo.NO);
            insert.setIsFreeze(CommonYesOrNo.NO);
            insert.setIsConsign(CommonYesOrNo.NO);
            insert.setSupplierCode(supplierCode);



            try {
                AjaxResult ajaxResult1 = inStoreService.updateInventory(inventory);
                AjaxResult ajaxResult2 = inStoreService.insertWmsInventoryDetails(insert);
                AjaxResult ajaxResult3 = inStoreService.deleteReserveStorage(new Long[]{reserveStorage1.getId()});
                addTaskLogNew(retTaskDto,"" ,retTaskDto.getStockinOrderNo(),"" , detail);
            } catch (Exception e) {
                new ServiceException("更新台账失败 ！！！" + e.getMessage());
            }

        }
        //关闭出库单状态
        Long outsourcedStockoutOrderDetailId = detail.getOutsourcedStockoutOrderDetailId();
        com.alibaba.fastjson2.JSONObject param = new com.alibaba.fastjson2.JSONObject();
        param.put("outsourcedStockoutOrderDetailId", outsourcedStockoutOrderDetailId );
        AjaxResult ajaxResult4 = iStockOutService.finishSourcedStockoutOrderStatus(param);
        if (ajaxResult4.isError()){
            throw new ServiceException("更新出库单据失败");
        }
        return inventory;
    }

    public void addTaskLogNew(RetTaskDto retTaskDto, String voucherNo, String orderNoLog, String lineNoLog, RwmOutSourceReturnOrderDetail detail) throws Exception {
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
        taskLog.setOrderType(TaskLogTypeConstant.OUTSOURCING_RETURN);
        taskLog.setOrderNo(orderNoLog);
        taskLog.setLineNo(lineNoLog);
        taskLog.setIsQc(retTaskDto.getIsQc());
        taskLog.setIsFreeze(CommonYesOrNo.NO);
        taskLog.setIsConsign(CommonYesOrNo.NO);
        taskLog.setSupplierCode(retTaskDto.getSupplierCode());
        taskLog.setFactoryCode(detail.getFactoryCode());
        taskLog.setTargetFactoryCode(detail.getFactoryCode());
//        taskLog.setMaterialCertificate(voucherNo);
        taskService.add(taskLog).isError();

    }


    private String getSupplierCode(RetTaskDto retTaskDto) {
        String stockInTaskNo = retTaskDto.getStockInTaskNo();
        RwmOutSourceReturnOrderDetail returnOrderDetailQuery = new RwmOutSourceReturnOrderDetail();
        returnOrderDetailQuery.setReturnOrderNo(stockInTaskNo);
        List<RwmOutSourceReturnOrderDetail> rwmOutSourceReturnOrderDetails = rwmOutSourceReturnOrderDetailMapper.selectRwmOutSourceReturnOrderDetailList(returnOrderDetailQuery);
        if (CollectionUtils.isEmpty(rwmOutSourceReturnOrderDetails)){
            throw new ServiceException(String.format("原材料退货单据不存在%s",stockInTaskNo ));
        }
        //取供应商
        String supplierCode = rwmOutSourceReturnOrderDetails.get(0).getSupplierCode();
        return supplierCode;
    }

}
