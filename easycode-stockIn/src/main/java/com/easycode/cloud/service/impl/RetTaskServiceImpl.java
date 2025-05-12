package com.easycode.cloud.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.lock.annotation.Lock4j;
import com.easycode.cloud.domain.*;
import com.easycode.cloud.domain.dto.RetTaskDto;
import com.easycode.cloud.domain.dto.StockInOtherDto;
import com.easycode.cloud.domain.vo.CostCenterReturnOrderVo;
import com.easycode.cloud.domain.vo.RetTaskVo;
import com.easycode.cloud.domain.vo.RwmOutSourceReturnOrderVo;
import com.easycode.cloud.mapper.*;
import com.easycode.cloud.service.IRetTaskService;
import com.easycode.cloud.service.IShelfTaskService;
import com.soa.eis.adapter.framework.message.IMsgObject;
import com.soa.eis.adapter.framework.message.impl.GroupRecord;
import com.soa.eis.adapter.framework.message.impl.MsgObject;
import com.weifu.cloud.common.core.exception.ServiceException;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.weifu.cloud.common.core.utils.SpringUtils;
import com.weifu.cloud.domain.vo.TaskInfoVo;
import com.weifu.cloud.domian.*;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.security.utils.SecurityUtils;
import com.weifu.cloud.constant.*;
import com.weifu.cloud.domain.*;
import com.weifu.cloud.domain.dto.*;
import com.weifu.cloud.domian.dto.MaterialUnitDefDto;
import com.weifu.cloud.domian.dto.StoragePositionDto;
import com.weifu.cloud.domian.dto.WmsMaterialAttrParamsDto;
import com.weifu.cloud.domian.dto.WmsMaterialBasicDto;
import com.weifu.cloud.domian.vo.InventoryDetailsVo;
import com.weifu.cloud.domian.vo.StoragePositionVo;
import com.weifu.cloud.enums.RemoteConfigEnum;
import com.weifu.cloud.mapper.*;
import com.weifu.cloud.service.*;
import com.easycode.cloud.strategy.IRetTaskStrategy;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * 退货任务Service业务层处理
 *
 * @author zhanglei
 * @date 2023-03-13
 */
@Service
@Primary
@Transactional(rollbackFor = Exception.class)
public class RetTaskServiceImpl implements IRetTaskService {
    private static final Logger logger = LoggerFactory.getLogger(RetTaskServiceImpl.class);
    private final String yyyyMMdd = "yyyyMMdd";
    @Autowired
    private RetTaskMapper retTaskMapper;

    @Autowired
    private IMainDataService iMainDataService;

    @Autowired
    private IInStoreService inStoreService;

    @Autowired
    private Map<String, IRetTaskStrategy> map;

    @Autowired
    private RemoteEsbSendService remoteEsbSendService;

    @Autowired
    private CostCenterReturnOrderDetailMapper costCenterReturnOrderDetailMapper;

    @Autowired
    private CostCenterReturnOrderMapper costCenterReturnOrderMapper;

    @Autowired
    private PrdReturnOrderDetailMapper prdReturnOrderDetailMapper;

//    @Autowired
//    private PrdReplReturnOrderMapper prdReplReturnOrderMapper;
//
//    @Autowired
//    private PrdReplReturnOrderDetailMapper prdReplReturnOrderDetailMapper;

    @Autowired
    private IStockOutService iStockOutService;

    @Autowired
    private StockInOtherMapper stockInOtherMapper;

    @Autowired
    private StockInOtherDetailMapper stockInOtherDetailMapper;

//    @Autowired
//    private DeliveryOrderRecoilMapper deliveryOrderRecoilMapper;
//
//    @Autowired
//    private DeliveryOrderRecoilDetailMapper deliveryOrderRecoilDetailMapper;

    @Autowired
    private ICodeRuleService iCodeRuleService;

    @Autowired
    private IShelfTaskService shelfTaskService;

//    @Autowired
//    private IFinPrdReturnOrderDetailService finPrdReturnOrderDetailService;
//
//    @Autowired
//    private IFinPrdReturnOrderService finPrdReturnOrderService;

    @Autowired
    private ITaskService taskService;

    @Autowired
    private RemoteConfigHelper remoteConfigHelper;

    @Autowired
    private RwmOutSourceReturnOrderMapper rwmOutSourceReturnOrderMapper;

    @Autowired
    private RwmOutSourceReturnOrderDetailMapper rwmOutSourceReturnOrderDetailMapper;
//    @Autowired
//    private IConsignReturnOrderService consignReturnOrderService;

//    @Autowired
//    private IConsignReturnOrderDetailService consignReturnOrderDetailService;

    @Autowired
    private InspectOrderMapper inspectOrderMapper;

    @Autowired
    private InspectOrderDetailsMapper inspectOrderDetailsMapper;

    @Autowired
    private IMainDataService mainDataService;

    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    private SapInteractionService sapInteractionService;

    @Autowired
    private SaleReturnOrderMapper saleReturnOrderMapper;

    @Autowired
    private SaleReturnOrderDetailMapper saleReturnOrderDetailMapper;

    /**
     * 查询退货任务
     *
     * @param id 退货任务主键
     * @return 退货任务
     */
    @Override
    public RetTask selectRetTaskById(Long id) {
        return retTaskMapper.selectRetTaskById(id);
    }

    /**
     * 查询退货任务列表
     *
     * @param retTask 退货任务
     * @return 退货任务
     */
    @Override
    public List<RetTask> selectRetTaskList(RetTask retTask) {
        return retTaskMapper.selectRetTaskList(retTask);
    }

    /**
     * 新增退货任务
     *
     * @param retTask 退货任务
     * @return 结果
     */
    @Override
    public int insertRetTask(RetTask retTask) {
        retTask.setCreateTime(DateUtils.getNowDate());
        return retTaskMapper.insertRetTask(retTask);
    }

    /**
     * 修改退货任务
     *
     * @param retTask 退货任务
     * @return 结果
     */
    @Override
    public int updateRetTask(RetTask retTask) {
        retTask.setUpdateTime(DateUtils.getNowDate());
        return retTaskMapper.updateRetTask(retTask);
    }

    /**
     * 批量删除退货任务
     *
     * @param ids 需要删除的退货任务主键
     * @return 结果
     */
    @Override
    public int deleteRetTaskByIds(Long[] ids) {
        return retTaskMapper.deleteRetTaskByIds(ids);
    }

    /**
     * 删除退货任务信息
     *
     * @param id 退货任务主键
     * @return 结果
     */
    @Override
    public int deleteRetTaskById(Long id) {
        return retTaskMapper.deleteRetTaskById(id);
    }

    /**
     * pda端查询退货任务
     *
     * @param retTaskDto 退货任务
     * @return 退货任务集合
     */
    @Override
    public List<RetTask> selectPdaRetTaskList(RetTaskDto retTaskDto) {
        return retTaskMapper.pdaRetTaskList(retTaskDto);
    }

    /**
     * pda退货任务提交
     *
     * @param retTaskDto 退货任务
     * @return 结果
     */
    @Override
    @GlobalTransactional
    @Lock4j(keys = {"#retTaskDto.id"}, acquireTimeout = 10, expire = 10000)
    public int submit(RetTaskDto retTaskDto) throws Exception {
        if (ObjectUtils.isEmpty(retTaskDto)) {
            throw new ServiceException("参数不存在!");
        }
        //除其他入库外 默认不质检
        retTaskDto.setIsQc(CommonYesOrNo.NO);
        AjaxResult ajaxResult = iMainDataService.getPositionInfoById(retTaskDto.getPositionId());
        if (ajaxResult.isError()) {
            throw new ServiceException("主数据服务调用失败!");
        }
        StoragePositionVo storagePositionVo = JSONObject.parseObject(JSONObject.toJSONString(ajaxResult.get("data")), StoragePositionVo.class);
        if (ObjectUtils.isEmpty(storagePositionVo)) {
            throw new ServiceException("仓位不存在！");
        }

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
        if (flag < 0) {
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
        retTask.setTaskStatus(TaskStatusConstant.TASK_STATUS_COMPLETE);
        retTask.setUpdateBy(SecurityUtils.getUsername());
        retTask.setUpdateTime(DateUtils.getNowDate());
        if (retTaskMapper.updateRetTask(retTask) <= 0) {
            throw new ServiceException("修改退货任务状态失败!");
        }

        //更新退货单 （除其他收货、交货单反冲）
        if (!StockInTaskConstant.OTHER_TYPE.equals(retTaskDto.getTaskType()) && !StockInTaskConstant.RECOIL_TYPE.equals(retTaskDto.getTaskType())) {
            IRetTaskStrategy iRetTaskStrategy = map.get(retTaskDto.getTaskType());
            if (ObjectUtils.isEmpty(iRetTaskStrategy)) {
                throw new ServiceException("更新单据策略不存在");
            }
            if (iRetTaskStrategy.updateRetOrder(retTaskDto) <= 0) {
                throw new ServiceException("更新单据失败!");
            }
        }

        if (StockInTaskConstant.PRO_DELIVER_RETURN_TYPE.equals(retTaskDto.getTaskType())) {
            // 生产订单退料
            // 生产订单发料明细信息
            PrdReturnOrderDetail detail = prdReturnOrderDetailMapper.selectPrdReturnOrderDetailById(retTaskDto.getDetailId());
            if (detail == null) {
                throw new ServiceException("获取生产订单退料明细信息失败！");
            }
            // 生产订单明细信息
            ProductionOrderInfo order = getOrder(detail.getPrdOrderNo());
            orderNoLog = detail.getReturnOrderNo();
            lineNoLog = detail.getLineNo();
            //更新台账
            SpringUtils.getBean(RetTaskServiceImpl.class).updateInventoryDetails(retTaskDto, storagePositionVo);
            // sap过账
            voucherNo = prdReturnToSap(retTaskDto, detail, order);
        }
//        else if (StockInTaskConstant.PRD_REPL_RETURN_TYPE.equals(retTaskDto.getTaskType())) {
//            // 生产订单补料退料
//            // 明细信息
//            PrdReplReturnOrderDetail detail = prdReplReturnOrderDetailMapper.selectPrdReplReturnOrderDetailById(retTaskDto.getDetailId());
//            if (detail == null) {
//                throw new ServiceException("生产订单补料退料获取明细信息失败！");
//            }
//            // 单据信息
//            PrdReplReturnOrder prdReplReturnOrder = new PrdReplReturnOrder();
//            prdReplReturnOrder.setReturnOrderNo(detail.getReturnOrderNo());
//            List<PrdReplReturnOrder> orderList = prdReplReturnOrderMapper.selectPrdReplReturnOrderList(prdReplReturnOrder);
//            if (orderList == null || orderList.size() == 0) {
//                throw new ServiceException("生产订单补料退料获取单据信息失败！");
//            }
//            orderNoLog = detail.getReturnOrderNo();
//            lineNoLog = detail.getLineNo();
//            //更新台账
//            SpringUtils.getBean(RetTaskServiceImpl.class).updateInventoryDetails(retTaskDto, storagePositionVo);
//            // sap过账
//            voucherNo = prdReplReturnToSap(retTaskDto, detail, orderList.get(0));
//        }
        else if (StockInTaskConstant.COST_CENTER_RETURN_TYPE.equals(retTaskDto.getTaskType())) {
            // 成本中心退料
            // 明细信息
            CostCenterReturnOrderDetail detail = costCenterReturnOrderDetailMapper.selectCostCenterReturnOrderDetailById(retTaskDto.getDetailId());
            if (detail == null) {
                throw new ServiceException("成本中心退料获取明细信息失败！");
            }
            // 单据信息
            CostCenterReturnOrderVo centerReturnOrderVo = new CostCenterReturnOrderVo();
            centerReturnOrderVo.setOrderNo(detail.getReturnOrderNo());
            List<CostCenterReturnOrder> orderList = costCenterReturnOrderMapper.selectCostCenterReturnOrderList(centerReturnOrderVo);
            if (orderList == null || orderList.size() == 0) {
                throw new ServiceException("成本中心退料获取单据信息失败！");
            }
            orderNoLog = detail.getReturnOrderNo();
            lineNoLog = detail.getLineNo();
            //更新台账
            SpringUtils.getBean(RetTaskServiceImpl.class).updateInventoryDetails(retTaskDto, storagePositionVo);
            // sap过账
            voucherNo = costReturnToSap(retTaskDto, detail, orderList.get(0));
        } else if (StockInTaskConstant.OTHER_TYPE.equals(retTaskDto.getTaskType())) {
            // 其他收货
            // 明细信息
            StockInOtherDetail detail = stockInOtherDetailMapper.selectStockInOtherDetailById(retTaskDto.getDetailId());
            if (detail == null) {
                throw new ServiceException("其他收货获取明细信息失败！");
            }
            // 单据信息
            StockInOtherDto stockInOtherDto = new StockInOtherDto();
            stockInOtherDto.setOrderNo(detail.getOrderNo());
            List<StockInOther> orderList = stockInOtherMapper.selectStockInOtherList(stockInOtherDto);
            if (orderList == null || orderList.size() == 0) {
                throw new ServiceException("其他收货获取单据信息失败！");
            }
            orderNoLog = detail.getOrderNo();
            lineNoLog = detail.getLineNo();
            //是否质检 跟着其他入库单明细走
            retTaskDto.setIsQc(detail.getIsQc());
            //设置供应商信息
            retTaskDto.setSupplierCode(detail.getSupplierCode());
            //更新台账
            Long inventoryId = SpringUtils.getBean(RetTaskServiceImpl.class).updateInventoryDetails(retTaskDto, storagePositionVo);
            if ("503".equals(orderList.get(0).getBillType()) && CommonYesOrNo.YES.equals(detail.getIsQc())) {
                Long tenantId = SecurityUtils.getLoginUser().getSysUser().getTenantId();

                // 创建送检单单号
                AjaxResult result = iCodeRuleService.getSeqWithTenantId(OrderNoTypeConstant.INSPECT, String.valueOf(tenantId));
                if (ajaxResult.isError() || StringUtils.isEmpty(ajaxResult.get("data").toString())) {
                    throw new ServiceException("送检单单号生成失败！");
                }
                String orderNo = result.get("data").toString();
                // 创建质检单
                InspectOrder inspectOrder = new InspectOrder();
                inspectOrder.setOrderNo(orderNo);
                inspectOrder.setBillStatus(InspectOrderStatusConstant.ORDER_STATUS_NEW);
                inspectOrder.setFactoryCode(detail.getFactoryCode());
                inspectOrder.setIsConsign(detail.getIsConsigned());
                inspectOrder.setLocationCode(retTaskDto.getStorageLocationCode());
                inspectOrder.setMaterialNo(detail.getMaterialNo());
                inspectOrder.setMaterialName(detail.getMaterialName());
                inspectOrder.setReceiveDate(DateUtils.getNowDate());
                inspectOrder.setReceiveOrderNo(orderList.get(0).getOrderNo());
                inspectOrder.setSupplierCode(detail.getSupplierCode());
                inspectOrder.setCreateBy(SecurityUtils.getUsername());
                inspectOrder.setCreateTime(DateUtils.getNowDate());
                inspectOrderMapper.insertInspectOrder(inspectOrder);
                // 送检单详情
                InspectOrderDetails inspectOrderDetails = new InspectOrderDetails();
                inspectOrderDetails.setOrderNo(orderNo);
                inspectOrderDetails.setLineNo(detail.getLineNo());
                inspectOrderDetails.setMaterialNo(detail.getMaterialNo());
                inspectOrderDetails.setQcQty(retTaskDto.getQty());
                inspectOrderDetails.setLot(retTaskDto.getLot());
                inspectOrderDetails.setPrdLot(retTaskDto.getLot());
                inspectOrderDetails.setPositionNo(retTaskDto.getConfirmPosition());
                inspectOrderDetails.setUnit(retTaskDto.getUnit());
                inspectOrderDetails.setCreateBy(SecurityUtils.getUsername());
                inspectOrderDetails.setCreateTime(DateUtils.getNowDate());
                inspectOrderDetails.setOperationUnit(retTaskDto.getOperationUnit());
                inspectOrderDetails.setInventoryId(inventoryId);
                inspectOrderDetailsMapper.insertInspectOrderDetails(inspectOrderDetails);
            } else {
                // 上架任务
                createShelfTask(retTaskDto, orderList.get(0).getOrderNo(), inventoryId);
            }
            //更新退货单（其他收货）
            IRetTaskStrategy iRetTaskStrategy = map.get(retTaskDto.getTaskType());
            if (ObjectUtils.isEmpty(iRetTaskStrategy)) {
                throw new ServiceException("更新单据策略不存在");
            }
            if (iRetTaskStrategy.updateRetOrder(retTaskDto) <= 0) {
                throw new ServiceException("更新单据失败!");
            }
            // sap过账
            voucherNo = SpringUtils.getBean(RetTaskServiceImpl.class).stockInOtherToSap(retTaskDto, detail, orderList.get(0));
        }
//        else if (StockInTaskConstant.RECOIL_TYPE.equals(retTaskDto.getTaskType())) {
//            // 交货单反冲
//            // 明细信息
//            DeliveryOrderRecoilDetail detail = deliveryOrderRecoilDetailMapper.selectDeliveryOrderRecoilDetailById(retTaskDto.getDetailId());
//            if (detail == null) {
//                throw new ServiceException("交货单反冲获取明细信息失败！");
//            }
//            // 单据信息
//            DeliveryOrderRecoilVo orderRecoilVo = new DeliveryOrderRecoilVo();
//            orderRecoilVo.setOrderNo(detail.getOrderNo());
//            List<DeliveryOrderRecoil> orderList = deliveryOrderRecoilMapper.selectDeliveryOrderRecoilList(orderRecoilVo);
//            if (orderList == null || orderList.size() == 0) {
//                throw new ServiceException("交货单反冲获取单据信息失败！");
//            }
//            orderNoLog = detail.getOrderNo();
//            lineNoLog = detail.getLineNo();
//            //是否质检
//            retTaskDto.setIsQc(detail.getIsQc());
//            //设置供应商信息
//            retTaskDto.setSupplierCode(detail.getSupplierCode());
//            retTaskDto.setLot(detail.getLotNo());
//            //更新台账
//            Long inventoryId = SpringUtils.getBean(RetTaskServiceImpl.class).updateInventoryDetails(retTaskDto, storagePositionVo);
//            //更新退货单（交货单反冲）
//            IRetTaskStrategy iRetTaskStrategy = map.get(retTaskDto.getTaskType());
//            if (ObjectUtils.isEmpty(iRetTaskStrategy)){
//                throw new ServiceException("更新单据策略不存在");
//            }
//            if (iRetTaskStrategy.updateRetOrder(retTaskDto) <= 0){
//                throw new ServiceException("更新单据失败!");
//            }
//            // 上架任务
//            createShelfTask(retTaskDto, orderList.get(0).getOrderNo(), inventoryId);
//        }
//        else if (StockInTaskConstant.FIN_RETURN_TYPE.equals(retTaskDto.getTaskType())) {
//            // 成品调拨退货
//            // 明细信息
//            FinPrdReturnOrderDetail detail = finPrdReturnOrderDetailService.selectFinPrdReturnOrderDetailById(retTaskDto.getDetailId());
//            if (detail == null) {
//                throw new ServiceException("成品退货获取明细信息失败！");
//            }
//            // 单据信息
//            FinPrdReturnOrderVo finPrdReturnOrderVo = new FinPrdReturnOrderVo();
//            finPrdReturnOrderVo.setOrderNo(detail.getReturnOrderNo());
//            List<FinPrdReturnOrder> orderList = finPrdReturnOrderService.selectFinPrdReturnOrderList(finPrdReturnOrderVo);
//            if (orderList == null || orderList.size() == 0) {
//                throw new ServiceException("成品退货获取单据信息失败！");
//            }
//            orderNoLog = detail.getReturnOrderNo();
//            lineNoLog = detail.getLineNo();
//            //更新台账
//            SpringUtils.getBean(RetTaskServiceImpl.class).updateInventoryDetails(retTaskDto, storagePositionVo);
//            // sap过账
//            voucherNo = moveLocationSap(retTaskDto, orderList.get(0).getOriLocationCode());
//        }
//        else if (StockInTaskConstant.CONSIGN_RETURN_TYPE.equals(retTaskDto.getTaskType())) {
//            // 寄售调拨退货
//            // 获取明细信息
//            ConsignReturnOrderDetail detail = consignReturnOrderDetailService.selectConsignReturnOrderDetailById(retTaskDto.getDetailId());
//            if (detail == null) {
//                throw new ServiceException("寄售调拨退货获取明细信息失败！");
//            }
//            // 单据信息
//            ConsignReturnOrderDto consignReturnOrderDto = new ConsignReturnOrderDto();
//            consignReturnOrderDto.setOrderNo(detail.getReturnOrderNo());
//            List<ConsignReturnOrder> orderList = consignReturnOrderService.selectConsignReturnOrderList(consignReturnOrderDto);
//            if (orderList == null || orderList.size() == 0) {
//                throw new ServiceException("寄售调拨退货获取单据信息失败！");
//            }
//            orderNoLog = detail.getReturnOrderNo();
//            lineNoLog = detail.getLineNo();
//            //更新台账
//            SpringUtils.getBean(RetTaskServiceImpl.class).updateInventoryDetails(retTaskDto, storagePositionVo);
//            // sap过账
//            voucherNo = moveLocationSap(retTaskDto, orderList.get(0).getSourceLocationCode());
//        }
        else if (StockInTaskConstant.RWM_OUTSOURCE_RETURN_TYPE.equals(retTaskDto.getTaskType())) {
            // 原材料退料
            // 明细信息
            handleSendSapOutSourceReturn(retTaskDto, storagePositionVo, voucherNo);
            return 1 ;
        } else {
            if (!"".equals(voucherNo)) {
                materialReversal(voucherNo);
            }
            throw new ServiceException("退货单据类型有误！");
        }

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
        taskLog.setOrderType(retTaskDto.getTaskType());
        taskLog.setOrderNo(orderNoLog);
        taskLog.setLineNo(lineNoLog);
        taskLog.setIsQc(retTaskDto.getIsQc());
        taskLog.setIsFreeze(CommonYesOrNo.NO);
        taskLog.setIsConsign(CommonYesOrNo.NO);
        taskLog.setSupplierCode(retTaskDto.getSupplierCode());
        taskLog.setMaterialCertificate(voucherNo);
        if (taskService.add(taskLog).isError()) {
            if (!"".equals(voucherNo)) {
                materialReversal(voucherNo);
            }
            throw new ServiceException("新增任务记录失败！");
        }
        return 1;
    }
    @GlobalTransactional(timeoutMills = 15 * 10000)
    public void handleSendSapOutSourceReturn(RetTaskDto retTaskDto, StoragePositionVo storagePositionVo, String voucherNo) throws Exception {
        String lineNoLog;
        String orderNoLog;
        RwmOutSourceReturnOrderDetail detail = rwmOutSourceReturnOrderDetailMapper.selectRwmOutSourceReturnOrderDetailById(retTaskDto.getDetailId());
        if (detail == null) {
            throw new ServiceException("委外退料获取明细信息失败！");
        }
        // 单据信息
        RwmOutSourceReturnOrderVo sourceReturnOrder = new RwmOutSourceReturnOrderVo();
        sourceReturnOrder.setOrderNo(detail.getReturnOrderNo());
        List<RwmOutSourceReturnOrder> orderList = rwmOutSourceReturnOrderMapper.selectRwmOutSourceReturnOrderList(sourceReturnOrder);
        if (orderList == null || orderList.size() == 0) {
            throw new ServiceException("委外出库获取单据不存在！！！ 请核实！！！！");
        }
        orderNoLog = detail.getReturnOrderNo();
        lineNoLog = detail.getLineNo();
        //更新台账
        InventoryDetailsVo inventory = updateInventoryDetailsByOutSourceReturn(retTaskDto, storagePositionVo ,detail);

        //同步添加任务记录
        addTaskLogNew(retTaskDto, voucherNo, orderNoLog, lineNoLog ,detail);
        // sap过账
        try {
            voucherNo = outSourceReturnToSap(retTaskDto, detail, orderList.get(0), inventory);
            // 释放仓位
            if ("1".equals(storagePositionVo.getMixedFlag())) {
                Long[] ids = {storagePositionVo.getId()};
                mainDataService.updateStoragePositionStatus(ids, "0");
            }
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

            List<String> removeList = Lists.newArrayList();
            removeList.add(retTaskDto.getTaskNo());
            inStoreService.removeByTaskNos(removeList);

        } catch (Exception e) {
            if (!StringUtils.isEmpty(voucherNo) && !"".equals(voucherNo)){
                materialReversal(voucherNo);
            }

            throw new ServiceException("过账失败！" +  e.getMessage());
        }
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
        taskLog.setMaterialCertificate(voucherNo);
        if (taskService.add(taskLog).isError()) {
            if (!"".equals(voucherNo)) {
                materialReversal(voucherNo);
            }
            throw new ServiceException("新增任务记录失败！");
        }
    }

    /**
     * 根据任务表中的单据明细id获取对应操作单位及操作单位数量
     *
     * @param retTaskDto 退货任务
     * @return 结果
     */
    @Override
    public RetTaskDto getOperationInfo(RetTaskDto retTaskDto) throws Exception {
        if (ObjectUtils.isEmpty(retTaskDto)) {
            throw new ServiceException("参数不存在!");
        }
        // 根据基础单位换算为入库使用单位
        // TODO 工厂代码
        MaterialUnitDefDto unitDefDto = new MaterialUnitDefDto();
        unitDefDto.setMaterialNo(retTaskDto.getMaterialNo());
        unitDefDto.setStockinEnable(CommonYesOrNo.YES);
        AjaxResult ajaxResult = mainDataService.queryMaterialUnitDef(unitDefDto);
        if (ajaxResult.isError()) {
            throw new ServiceException(ajaxResult.get("msg").toString());
        }
        List<MaterialUnitDefDto> unitList = JSONObject.parseArray(ajaxResult.get("data").toString(), MaterialUnitDefDto.class);
        if (ObjectUtils.isEmpty(unitList)) {
            throw new ServiceException(String.format("物料%s不存在入库使用单位，请维护相关数据", "", retTaskDto.getMaterialNo()));
        }
        if (unitList.size() > 1) {
            throw new ServiceException(String.format("数据有误，物料%s存在多条入库使用单位", "", retTaskDto.getMaterialNo()));
        }
        // 查询物料属性

        MaterialUnitDefDto materialUnitDefDto = unitList.get(0);
        RetTaskDto resultRetTask = new RetTaskDto();
        resultRetTask.setOperationUnit(materialUnitDefDto.getUnit());
        resultRetTask.setConversDefault(materialUnitDefDto.getConversDefault());
//        if (StockInTaskConstant.PRO_DELIVER_RETURN_TYPE.equals(retTaskDto.getTaskType())) {
//        } else if (StockInTaskConstant.PRD_REPL_RETURN_TYPE.equals(retTaskDto.getTaskType())) {
//        } else if (StockInTaskConstant.COST_CENTER_RETURN_TYPE.equals(retTaskDto.getTaskType())) {
//            CostCenterReturnOrderDetail detail = costCenterReturnOrderDetailMapper.selectCostCenterReturnOrderDetailById(retTaskDto.getDetailId());
////            resultRetTask.setOperationQty(detail.);
//        } else if (StockInTaskConstant.OTHER_TYPE.equals(retTaskDto.getTaskType())) {
//        } else if (StockInTaskConstant.FIN_RETURN_TYPE.equals(retTaskDto.getTaskType())) {
//        } else if (StockInTaskConstant.CONSIGN_RETURN_TYPE.equals(retTaskDto.getTaskType())) {
//        } else {
//            throw new ServiceException("退货单据类型有误！");
//        }
        return resultRetTask;
    }

    @Override
    public int closeTask(Long[] ids) {
        // 关闭任务
        for (Long id : ids) {
            RetTaskDto retTaskDto = new RetTaskDto();
            retTaskDto.setUpdateTime(DateUtils.getNowDate());
            retTaskDto.setUpdateBy(SecurityUtils.getUsername());
            retTaskDto.setTaskStatus(TaskStatusConstant.TASK_STATUS_CLOSE);
            retTaskDto.setId(id);
            retTaskMapper.updateRetTask(retTaskDto);
        }
        return 1;
    }

    /**
     * 退货任务查询
     *
     * @param id 退货任务id
     * @return 返回退货任务+物料类型
     */
    @Override
    public RetTaskVo getRetTaskByMaterialNoType(Long id) {
        RetTaskVo retTaskVo = retTaskMapper.selectRetTaskById(id);

        if (!ObjectUtils.isEmpty(retTaskVo)
                && TaskTypeConstant.OUT_SOURCE_RETURN_TASK.equals(retTaskVo.getTaskType())) {
            RwmOutSourceReturnOrderDetail detail = rwmOutSourceReturnOrderDetailMapper.selectRwmOutSourceReturnOrderDetailById(retTaskVo.getDetailId());
            if (ObjectUtils.isEmpty(detail)) {
                throw new ServiceException("退货明细不存在，请核实！！！！");
            }
            retTaskVo.setLineNo(detail.getLineNo());
            retTaskVo.setQty(detail.getReturnQty());
            retTaskVo.setLot(detail.getLot());
            //todo 库存地点 ；物料类型
            retTaskVo.setUnit(detail.getUnit());
            String supplierCode = detail.getSupplierCode();
            String supplierName = detail.getSupplierName();
            retTaskVo.setSupplierName(supplierName);
            retTaskVo.setSupplierCode(supplierCode);
            retTaskVo.setFactoryCode(detail.getFactoryCode());
        } else {
            SaleReturnOrderDetail saleReturnOrderDetail = saleReturnOrderDetailMapper.selectSaleReturnOrderDetailById(retTaskVo.getDetailId());
            if (ObjectUtils.isEmpty(saleReturnOrderDetail)) {
                throw new ServiceException("退货明细不存在，请核实！！！！");
            }
            retTaskVo.setLineNo(saleReturnOrderDetail.getLineNo());
        }

        //        // 获取退货任务原库存地点
//        PrdReturnOrderDetail prdReturnOrderDetail = prdReturnOrderDetailMapper.selectPrdReturnOrderDetailById(retTask.getDetailId());
//        if (!ObjectUtils.isEmpty(prdReturnOrderDetail) && StringUtils.isNotEmpty(prdReturnOrderDetail.getStorageLocation())) {
//            retTaskVo.setStorageLocation(prdReturnOrderDetail.getStorageLocation());
//        }
        // 获取退货任务目标库存地点
//        AjaxResult result = iMainDataService.getPositionInfoById(retTask.getPositionId());
//        if (result.isError()) {
//            throw new ServiceException(result.get("msg").toString());
//        }
//        StoragePositionVo storagePositionVo = JSONObject.parseObject(JSONObject.toJSONString(result.get("data")), StoragePositionVo.class);
//        if (ObjectUtils.isEmpty(storagePositionVo)) {
//            throw new ServiceException(ErrorMessage.POSITION_CODE_NOTHING);
//        }
//        retTaskVo.setStorageLocationCode(storagePositionVo.getLocationCode());
//        retTaskVo.setPositionNo(storagePositionVo.getPositionNo());
//
//        // 根据物料查询对应物料类型 并校验物料类型
//        List<String> materialNoList = new ArrayList<>();
//        materialNoList.add(retTaskVo.getMaterialNo());
//        WmsMaterialBasicDto materialBasicDto = new WmsMaterialBasicDto();
//        materialBasicDto.setMaterialNoList(materialNoList);
//        AjaxResult ajaxResult = iMainDataService.getMaterialArrInfo(materialBasicDto);
//        if (ajaxResult.isError()) {
//            throw new ServiceException(ajaxResult.get("msg").toString());
//        }
//        List<WmsMaterialAttrParamsDto> list = JSONObject.parseArray(ajaxResult.get("data").toString(), WmsMaterialAttrParamsDto.class);
//        if (ObjectUtils.isEmpty(list)) {
//            throw new ServiceException("物料属性中未查询到物料相关信息！");
//        }
//
//        Map<String, String> materialMap = list.stream().collect(Collectors.toMap(
//                m -> Optional.ofNullable(m).map(WmsMaterialAttrParamsDto::getMaterialNo).orElse(""),
//                m -> Optional.ofNullable(m).map(WmsMaterialAttrParamsDto::getType).orElse(""),
//                (a, b) -> StringUtils.isNotBlank(a) ? a : b));
//        retTaskVo.setType(materialMap.get(retTaskVo.getMaterialNo()));
        return retTaskVo;
    }

    /**
     * 生成上架任务
     */
    public void createShelfTask(RetTaskDto retTaskDto, String orderNo, Long inventoryId) {
        // 生成上架任务
        RemoteConfigEnum createShelfTask = RemoteConfigEnum.CREATE_SHELF_TASK_YCL;
        String value = remoteConfigHelper.getConfig(createShelfTask.getKey());
        if ("".equals(Objects.toString(value, ""))) {
            throw new ServiceException("获取参数配置失败！");
        }
        if (createShelfTask.getValue().equals(value)) {
            Long tenantId = SecurityUtils.getLoginUser().getSysUser().getTenantId();

            // 上架任务号
            AjaxResult result = iCodeRuleService.getSeqWithTenantId(TaskNoTypeConstant.SHELF_TASK, String.valueOf(tenantId));
            if (result.isError() || StringUtils.isEmpty(result.get("data").toString())) {
                throw new ServiceException("上架任务号生成失败！");
            }
            String taskNo = result.get("data").toString();
            ShelfTask shelfTask = new ShelfTask();
            shelfTask.setTaskNo(taskNo);
            shelfTask.setTaskType(retTaskDto.getTaskType());
            shelfTask.setStatus(TaskStatusConstant.TASK_STATUS_NEW);
            shelfTask.setAllocateTime(DateUtils.getNowDate());
            shelfTask.setMaterialNo(retTaskDto.getMaterialNo());
            shelfTask.setMaterialName(retTaskDto.getMaterialName());
            shelfTask.setOldMaterialNo(retTaskDto.getOldMatrialName());
            shelfTask.setLot(retTaskDto.getLot());
            shelfTask.setStockinOrderNo(orderNo);
            shelfTask.setSourcePositionNo(retTaskDto.getConfirmPosition());
            shelfTask.setSourceLocationCode(retTaskDto.getStorageLocationCode());
            shelfTask.setSourceAreaCode(retTaskDto.getAreaCode());
            shelfTask.setInventoryId(inventoryId);
            shelfTask.setQty(retTaskDto.getQty());
            shelfTask.setUnit(retTaskDto.getUnit());
            shelfTask.setOperationUnit(retTaskDto.getOperationUnit());
            shelfTask.setOperationQty(retTaskDto.getOperationQty());
            shelfTask.setCompleteQty(BigDecimal.ZERO);
            shelfTask.setOperationCompleteQty(BigDecimal.ZERO);
            shelfTask.setCreateBy(SecurityUtils.getUsername());
            shelfTask.setCreateTime(DateUtils.getNowDate());
            shelfTaskService.insertShelfTask(shelfTask);
        }
    }

    /**
     * 调拨sap过账
     */
    private String moveLocationSap(RetTaskDto retTask, String sourceLocationCode) throws Exception {

        SapMoveLocationParamsDto sp = new SapMoveLocationParamsDto();
        Date now = new Date();
        sp.setBudat(DateFormatUtils.format(now, yyyyMMdd));
        sp.setBldat(DateFormatUtils.format(now, yyyyMMdd));
        sp.setBktxt("");
        sp.setuName(SecurityUtils.getUsername());
        sp.setPlant(SecurityUtils.getComCode());
        sp.setMaterial(retTask.getMaterialNo());
        sp.setStgeLoc(sourceLocationCode);
        sp.setMoveType(SapReqOrResConstant.MOVE_LOCATION_MOVE_TYPE);
        sp.setBatch(retTask.getLot());
        sp.setSpecStock("");
        sp.setVendor("");
        sp.setMovePlant(SecurityUtils.getComCode());
        sp.setMoveMat(retTask.getMaterialNo());
        sp.setMoveStloc(retTask.getStorageLocationCode());
        sp.setMoveBatch(retTask.getLot());
        sp.setEntryQnt(retTask.getQty().toString());
        sp.setEntryUom(retTask.getUnit());
        sp.setEntryUomIso("");
        sp.setNoTransferReq("");
        sp.setItemText(retTask.getTaskNo());
        return sapInteractionService.moveLocationSap(sp);
//        String voucherNo = "";
//        IMsgObject reqMo = new MsgObject(IMsgObject.MOType.initSR);
//        List<GroupRecord> list = new ArrayList<>();
//        GroupRecord groupRecord = new GroupRecord();
//        Date now = new Date();
//        groupRecord.setName("IS_HEAD");
//        // 过账日期
//        groupRecord.setFieldValue("BUDAT", DateFormatUtils.format(now, "yyyyMMdd"));
//        // 凭证日期
//        groupRecord.setFieldValue("BLDAT", DateFormatUtils.format(now, "yyyyMMdd"));
//        // 用户名
//        groupRecord.setFieldValue("UNAME", SecurityUtils.getUsername());
//        // 凭证抬头文本
//        groupRecord.setFieldValue("BKTXT", "");
//        GroupRecord itemRecord = new GroupRecord();
//        itemRecord.setName("IT_ITEM");
//        // 工厂
//        itemRecord.setFieldValue("PLANT", SecurityUtils.getComCode());
//        // 物料编号
//        itemRecord.setFieldValue("MATERIAL", retTask.getMaterialNo());
//        // 存储地点
//        itemRecord.setFieldValue("STGE_LOC", sourceLocationCode);
//        // 移动类型
//        itemRecord.setFieldValue("MOVE_TYPE", SapReqOrResConstant.MOVE_LOCATION_MOVE_TYPE);
//        // 批次
//        itemRecord.setFieldValue("BATCH", retTask.getLot());
//        // 特殊库存标识
//        itemRecord.setFieldValue("SPEC_STOCK", "");
//        // 供应商账号
//        itemRecord.setFieldValue("VENDOR", "");
//        // 收货工厂/发货工厂
//        itemRecord.setFieldValue("MOVE_PLANT", SecurityUtils.getComCode());
//        // 接收/发出物料
//        itemRecord.setFieldValue("MOVE_MAT", retTask.getMaterialNo());
//        // 收货/发货库存地点
//        itemRecord.setFieldValue("MOVE_STLOC", retTask.getStorageLocationCode());
//        // 收货/发货批量
//        itemRecord.setFieldValue("MOVE_BATCH", retTask.getLot());
//        // 数量
//        itemRecord.setFieldValue("ENTRY_QNT", retTask.getQty().toString());
//        // 单位
//        itemRecord.setFieldValue("ENTRY_UOM", retTask.getUnit());
//        // 计量单位的 ISO 代码
//        itemRecord.setFieldValue("ENTRY_UOM_ISO", "");
//        // 标识: 未创建转移要求
//        itemRecord.setFieldValue("NO_TRANSFER_REQ", "");
//        list.add(groupRecord);
//        list.add(itemRecord);
//        reqMo.setReqGroupRecord(list);
//        byte[] result = remoteEsbSendService.sendToEsb(EsbSendSapConstant.Z_IWMS_ALLOCATE, reqMo.toString().getBytes());
//        // 转化为标准结果
//        MsgObject resMo = new MsgObject(result, IMsgObject.MOType.initSR);
//        logger.info("esb返回结果：" + resMo.toString());
//        String errorMsg = resMo.getResValue("ERRORMSG");
//        String flag = resMo.getResValue("FLAG");
//        if (!SapReqOrResConstant.SAP_RETURN_TYPE.equals(flag)) {
//            logger.error(errorMsg);
//            throw new ServiceException("sap过账失败!原因：" + errorMsg);
//        }
//        // 凭证号
//        voucherNo = resMo.getResValue("MBLNR");
//        return voucherNo;
    }

    /**
     * 其他收货sap过账
     */
    public String stockInOtherToSap(RetTaskDto retTaskDto, StockInOtherDetail detail, StockInOther order) throws Exception {

        SapOtherReceiveParamsDto sp = new SapOtherReceiveParamsDto();
        Date now = new Date();
        sp.setBudat(DateFormatUtils.format(now, yyyyMMdd));
        sp.setBldat(DateFormatUtils.format(now, yyyyMMdd));
        sp.setBktxt("");
        sp.setuName(SecurityUtils.getUsername());
        sp.setPlant(detail.getFactoryCode());
        sp.setMatnr(retTaskDto.getMaterialNo());
        sp.setGrRcpt("");
        sp.setStgeLoc(retTaskDto.getStorageLocationCode());
        sp.setMoveStloc(retTaskDto.getStorageLocationCode());
        sp.setMoveType(order.getBillType());
        sp.setBatch(detail.getLot());
        sp.setEntryQnt(retTaskDto.getQty().toString());
        sp.setEntryUom(detail.getUnit());
        sp.setDelivNumbToSearc("");
        if (SapReqOrResConstant.STOCK_IN_OTHER_BY_PRODUCT.equals(order.getBillType())) {
            // 必填
            sp.setOrderId(order.getPrdOrderNo());
        } else {
            sp.setOrderId("");
        }
        return sapInteractionService.stockInOtherToSap(sp);

//        String voucherNo = "";
//        IMsgObject reqMo = new MsgObject(IMsgObject.MOType.initSR);
//        List<GroupRecord> list = new ArrayList<>();
//        GroupRecord groupRecord = new GroupRecord();
//        // 入参抬头
//        groupRecord.setName("IS_HEAD");
//        // 过账日期
//        groupRecord.setFieldValue("BUDAT", DateUtils.dateTime());
//        // 凭证日期
//        groupRecord.setFieldValue("BLDAT", DateUtils.dateTime());
//        // 凭证抬头
//        groupRecord.setFieldValue("BKTXT", "");
//        // 用户名
//        groupRecord.setFieldValue("UNAME", SecurityUtils.getUsername());
//        GroupRecord itemRecord = new GroupRecord();
//        // 入参行项目
//        itemRecord.setName("IT_ITEM");
//        // 物料号
//        itemRecord.setFieldValue("MATNR", retTaskDto.getMaterialNo());
//        // 工厂
//        itemRecord.setFieldValue("PLANT", detail.getFactoryCode());
//        // 收货方
//        itemRecord.setFieldValue("GR_RCPT", "");
//        // 源库存地点
//        itemRecord.setFieldValue("STGE_LOC", retTaskDto.getStorageLocationCode());
//        // 目的库存地点
//        itemRecord.setFieldValue("MOVE_STLOC", retTaskDto.getStorageLocationCode());
//        // 移动类型
//        itemRecord.setFieldValue("MOVE_TYPE", order.getBillType());
//        // 数量
//        itemRecord.setFieldValue("ENTRY_QNT", retTaskDto.getQty().toString());
//        // 单位
//        itemRecord.setFieldValue("ENTRY_UOM", detail.getUnit());
//        // 批次编号
//        itemRecord.setFieldValue("BATCH", detail.getLot());
//        // 20230414 SAP过账失败，需注释   特殊库存标识(寄售,自有,冻结)
////            itemRecord.setFieldValue("SPEC_STOCK", detail.getIsQc());
//        // 交货
//        itemRecord.setFieldValue("DELIV_NUMB_TO_SEARC", "");
//        // 生产订单号(531必传)
//        if (SapReqOrResConstant.STOCK_IN_OTHER_BY_PRODUCT.equals(order.getBillType())) {
//            // 必填
//            itemRecord.setFieldValue("ORDERID", order.getPrdOrderNo());
//        } else {
//            itemRecord.setFieldValue("ORDERID", "");
//        }
//        list.add(groupRecord);
//        list.add(itemRecord);
//        reqMo.setReqGroupRecord(list);
//        byte[] result = remoteEsbSendService.sendToEsb(EsbSendSapConstant.Z_IWMS_RECEIPT, reqMo.toString().getBytes());
//        MsgObject resMo = new MsgObject(result, IMsgObject.MOType.initSR);
//        String errorMsg = resMo.getResValue("ERRORMSG");
//        String flag = resMo.getResValue("FLAG");
//        if (!SapReqOrResConstant.SAP_RETURN_TYPE.equals(flag)) {
//            logger.error(errorMsg);
//            throw new ServiceException("sap其他收货过账失败!" + errorMsg);
//        }
//        // 凭证号
//        voucherNo = resMo.getResValue("MBLNR");
//        return voucherNo;
    }

    /**
     * 获取生产订单发料信息
     *
     * @param orderNo
     * @return
     */
    public ProductionOrderInfo getOrder(String orderNo) {
        ProductionOrderInfo productionOrderInfo = new ProductionOrderInfo();
        productionOrderInfo.setOrderNo(orderNo);
        AjaxResult result = iStockOutService.getList(productionOrderInfo);
        if ("".equals(Objects.toString(result.get("data"), "")) || result.isError()) {
            throw new ServiceException("获取生产订单信息失败！");
        }
        List<ProductionOrderInfo> orderList = JSON.parseArray(result.get("data").toString(), ProductionOrderInfo.class);
        if (orderList == null || orderList.size() == 0) {
            throw new ServiceException("生产订单信息为空！");
        }
        return orderList.get(0);
    }

    /**
     * 生产订单退料
     */
    public String prdReturnToSap(RetTaskDto retTaskDto, PrdReturnOrderDetail detail, ProductionOrderInfo order) throws Exception {

        SapProReturnParamsDto sp = new SapProReturnParamsDto();
        Date now = new Date();
        sp.setBudat(DateFormatUtils.format(now, yyyyMMdd));
        sp.setBldat(DateFormatUtils.format(now, yyyyMMdd));
        sp.setBktxt("");
        sp.setuName(SecurityUtils.getUsername());

        // 物料
        sp.setMatnr(retTaskDto.getMaterialNo());
        sp.setMvtInd("");
        sp.setBwart(SapReqOrResConstant.PRODUCT_MOVE_TYPE);
        sp.setErfmg(retTaskDto.getQty().toString());
        sp.setErfme(detail.getUnit());
        sp.setBatch(detail.getLot());
        sp.setLgort(retTaskDto.getStorageLocationCode());
        sp.setReservNo(order.getReserveNo());
        sp.setResItem(detail.getPrdOrderLineNo());
        sp.setStckType(SapReqOrResConstant.STCK_TYPE);
        sp.setSobkz(SapReqOrResConstant.IS_NOT_CONSIGN);
        sp.setStgeBin("");
        sp.setStgeType("");
        sp.setXstob(SapReqOrResConstant.COST_XSTOB);
        sp.setOrderId(order.getOrderNo());

        return sapInteractionService.prdReturnToSap(sp);

//        String voucherNo = "";
//        IMsgObject reqMo = new MsgObject(IMsgObject.MOType.initSR);
//        List<GroupRecord> list = new ArrayList<>();
//        GroupRecord groupRecord = new GroupRecord();
//        // 入参抬头
//        groupRecord.setName("IS_HEAD");
//        // 过账日期
//        groupRecord.setFieldValue("BUDAT", DateUtils.dateTime());
//        // 凭证日期
//        groupRecord.setFieldValue("BLDAT", DateUtils.dateTime());
//        // 凭证抬头
//        groupRecord.setFieldValue("BKTXT", "");
//        // 用户名
//        groupRecord.setFieldValue("UNAME", SecurityUtils.getUsername());
//        GroupRecord itemRecord = new GroupRecord();
//        // 入参行项目
//        itemRecord.setName("IT_ITEM");
//        // 物料号
//        itemRecord.setFieldValue("MATNR", retTaskDto.getMaterialNo());
//        // 移动标识
//        itemRecord.setFieldValue("MVT_IND", "");
//        // 移动类型
//        itemRecord.setFieldValue("BWART", SapReqOrResConstant.PRODUCT_MOVE_TYPE);
//        // 数量
//        itemRecord.setFieldValue("ERFMG", retTaskDto.getQty().toString());
//        // 单位
//        itemRecord.setFieldValue("ERFME", detail.getUnit());
//        // 批次编号
//        itemRecord.setFieldValue("BATCH", detail.getLot());
//        // 存储地点
//        itemRecord.setFieldValue("LGORT", retTaskDto.getStorageLocationCode());
//        // 预留号
//        itemRecord.setFieldValue("RESERV_NO", order.getReserveNo());
//        // 预留行号
//        itemRecord.setFieldValue("RES_ITEM", detail.getPrdOrderLineNo());
//        // 库存类型(是否质检)
//        itemRecord.setFieldValue("STCK_TYPE", SapReqOrResConstant.STCK_TYPE);
//        // 特殊库存标识(寄售,自有,冻结)
//        itemRecord.setFieldValue("SOBKZ", SapReqOrResConstant.IS_NOT_CONSIGN);
//        // 仓位(非wm不传)
//        itemRecord.setFieldValue("STGE_BIN", "");
//        // 仓储类型(非wm不传)
//        itemRecord.setFieldValue("STGE_TYPE", "");
//        // 使用冲销移动类型标识
//        itemRecord.setFieldValue("XSTOB", SapReqOrResConstant.COST_XSTOB);
//        // 生产订单号
//        itemRecord.setFieldValue("ORDERID", order.getOrderNo());
//        list.add(groupRecord);
//        list.add(itemRecord);
//        reqMo.setReqGroupRecord(list);
//        byte[] result = remoteEsbSendService.sendToEsb(EsbSendSapConstant.Z_IWMS_PLANNED, reqMo.toString().getBytes());
//        MsgObject resMo = new MsgObject(result, IMsgObject.MOType.initSR);
//        String errorMsg = resMo.getResValue("ERRORMSG");
//        String flag = resMo.getResValue("FLAG");
//        if (!SapReqOrResConstant.SAP_RETURN_TYPE.equals(flag)) {
//            logger.error(errorMsg);
//            throw new ServiceException("sap生产订单退料过账失败!" + errorMsg);
//        }
//        // 凭证号
//        voucherNo = resMo.getResValue("MBLNR");
//        return voucherNo;
    }

//    /**
//     * 生产订单补料退料
//     */
//    public String prdReplReturnToSap(RetTaskDto retTaskDto, PrdReplReturnOrderDetail detail, PrdReplReturnOrder order) throws Exception {
//
//        SapReplReturnParamsDto sp = new SapReplReturnParamsDto();
//        Date now = new Date();
//        sp.setBudat(DateFormatUtils.format(now, "yyyyMMdd"));
//        sp.setBldat(DateFormatUtils.format(now, "yyyyMMdd"));
//        sp.setBktxt("");
//        sp.setuName(SecurityUtils.getUsername());
//
//        // 物料
//        sp.setMatnr(retTaskDto.getMaterialNo());
//        sp.setPlant(order.getFactoryCode());
//        sp.setOrderId(order.getPrdOrderNo());
//        sp.setMvtInd("");
//        sp.setBwark(SapReqOrResConstant.LD_PRODUCT_MOVE_TYPE);
//        sp.setErfmg(retTaskDto.getQty().toString());
//        sp.setErfme(detail.getUnit());
//        sp.setBatch(detail.getLot());
//        sp.setLgort(retTaskDto.getStorageLocationCode());
//        sp.setStckType(SapReqOrResConstant.STCK_TYPE);
//
//        sp.setSobkz(SapReqOrResConstant.IS_NOT_CONSIGN);
//        sp.setStgeBin("");
//        sp.setStgeType("");
//        sp.setXstob(SapReqOrResConstant.COST_XSTOB);
//
//        return sapInteractionService.prdReplReturnToSap(sp);

//        String voucherNo = "";
//        IMsgObject reqMo = new MsgObject(IMsgObject.MOType.initSR);
//        List<GroupRecord> list = new ArrayList<>();
//        GroupRecord groupRecord = new GroupRecord();
//        // 入参抬头
//        groupRecord.setName("IS_HEAD");
//        // 过账日期
//        groupRecord.setFieldValue("BUDAT", DateUtils.dateTime());
//        // 凭证日期
//        groupRecord.setFieldValue("BLDAT", DateUtils.dateTime());
//        // 凭证抬头
//        groupRecord.setFieldValue("BKTXT", "");
//        // 用户名
//        groupRecord.setFieldValue("UNAME", SecurityUtils.getUsername());
//        GroupRecord itemRecord = new GroupRecord();
//        // 入参行项目
//        itemRecord.setName("IT_ITEM");
//        // 工厂
//        itemRecord.setFieldValue("PLANT", order.getFactoryCode());
//        // 物料号
//        itemRecord.setFieldValue("MATNR", retTaskDto.getMaterialNo());
//        // 生产订单号
//        itemRecord.setFieldValue("ORDERID", order.getPrdOrderNo());
//        // 移动标识
//        itemRecord.setFieldValue("MVT_IND", "");
//        // 移动类型
//        itemRecord.setFieldValue("BWART", SapReqOrResConstant.LD_PRODUCT_MOVE_TYPE);
//        // 数量
//        itemRecord.setFieldValue("ERFMG", retTaskDto.getQty().toString());
//        // 单位
//        itemRecord.setFieldValue("ERFME", detail.getUnit());
//        // 批次编号
//        itemRecord.setFieldValue("BATCH", detail.getLot());
//        // 存储地点
//        itemRecord.setFieldValue("LGORT", retTaskDto.getStorageLocationCode());
//        // 库存类型(是否质检)
//        itemRecord.setFieldValue("STCK_TYPE", SapReqOrResConstant.STCK_TYPE);
//        // 特殊库存标识(寄售,自有,冻结)
//        itemRecord.setFieldValue("SOBKZ", SapReqOrResConstant.IS_NOT_CONSIGN);
//        // 仓位(非wm不传)
//        itemRecord.setFieldValue("STGE_BIN", "");
//        // 仓储类型(非wm不传)
//        itemRecord.setFieldValue("STGE_TYPE", "");
//        // 使用冲销移动类型标识
//        itemRecord.setFieldValue("XSTOB", SapReqOrResConstant.COST_XSTOB);
//        list.add(groupRecord);
//        list.add(itemRecord);
//        reqMo.setReqGroupRecord(list);
//        byte[] result = remoteEsbSendService.sendToEsb(EsbSendSapConstant.Z_IWMS_UNPLANNED, reqMo.toString().getBytes());
//        MsgObject resMo = new MsgObject(result, IMsgObject.MOType.initSR);
//        String errorMsg = resMo.getResValue("ERRORMSG");
//        String flag = resMo.getResValue("FLAG");
//        if (!SapReqOrResConstant.SAP_RETURN_TYPE.equals(flag)) {
//            logger.error(errorMsg);
//            throw new ServiceException("sap生产订单补料退料过账失败!" + errorMsg);
//        }
//        // 凭证号
//        voucherNo = resMo.getResValue("MBLNR");
//        return voucherNo;
//    }

    /**
     * 成本中心sap过账
     */
    public String costReturnToSap(RetTaskDto retTaskDto, CostCenterReturnOrderDetail detail, CostCenterReturnOrder order) throws Exception {
        SapCostCenterReturnDto sp = new SapCostCenterReturnDto();
        Date now = new Date();
        sp.setBudat(DateFormatUtils.format(now, yyyyMMdd));
        sp.setBldat(DateFormatUtils.format(now, yyyyMMdd));
        sp.setBktxt("");
        sp.setuName(SecurityUtils.getUsername());

        // 物料
        sp.setMatnr(retTaskDto.getMaterialNo());
        sp.setPlant(order.getFactoryCode());
        sp.setMvtInd("");
        sp.setBwart(order.getMoveType());
        sp.setPoPrQnt(retTaskDto.getQty().toString());
        sp.setOrderPrUn(detail.getUnit());
        sp.setBatch(detail.getLot());
        sp.setLgort(retTaskDto.getStorageLocationCode());
        // 成本中心
        sp.setCostCenter(order.getCostCenterCode());
        if (order.getCostMgType().equals(CostCenterConstant.COST_TYP_YF)) {
            // 订单编号(研发类型必传)
            sp.setOrderId(order.getInnerOrderNo());
        }
        sp.setStckType(SapReqOrResConstant.STCK_TYPE);
        sp.setSobkz(SapReqOrResConstant.IS_NOT_CONSIGN);
        sp.setStgeBin("");
        sp.setStgeType("");
        sp.setXstob("");
        sp.setOrderId(order.getOrderNo());

        return sapInteractionService.costReturnToSap(sp);
//        String voucherNo = "";
//        IMsgObject reqMo = new MsgObject(IMsgObject.MOType.initSR);
//        List<GroupRecord> list = new ArrayList<>();
//        GroupRecord groupRecord = new GroupRecord();
//        // 入参抬头
//        groupRecord.setName("IS_HEAD");
//        // 过账日期
//        groupRecord.setFieldValue("BUDAT", DateUtils.dateTime());
//        // 凭证日期
//        groupRecord.setFieldValue("BLDAT", DateUtils.dateTime());
//        // 凭证抬头
//        groupRecord.setFieldValue("BKTXT", "");
//        // 用户名
//        groupRecord.setFieldValue("UNAME", SecurityUtils.getUsername());
//        GroupRecord itemRecord = new GroupRecord();
//        // 入参行项目
//        itemRecord.setName("IT_ITEM");
//        // 物料号
//        itemRecord.setFieldValue("MATNR", retTaskDto.getMaterialNo());
//        // 移动类型
//        itemRecord.setFieldValue("BWART", order.getMoveType());
//        // 工厂代码
//        itemRecord.setFieldValue("PLANT", order.getFactoryCode());
//        // 移动标识
//        itemRecord.setFieldValue("MVT_IND", "");
//        // 数量
//        itemRecord.setFieldValue("PO_PR_QNT", retTaskDto.getQty().toString());
//        // 单位
//        itemRecord.setFieldValue("ORDERPR_UN", detail.getUnit());
//        // 批次编号
//        itemRecord.setFieldValue("BATCH", detail.getLot());
//        // 存储地点
//        itemRecord.setFieldValue("LGORT", retTaskDto.getStorageLocationCode());
//        // 成本中心
//        itemRecord.setFieldValue("COSTCENTER", order.getCostCenterCode());
//        if (order.getCostMgType().equals(CostCenterConstant.COST_TYP_YF)) {
//            // 订单编号(研发类型必传)
//            itemRecord.setFieldValue("ORDERID", order.getInnerOrderNo());
//        }
//        // 库存类型(是否质检)
//        itemRecord.setFieldValue("STCK_TYPE", SapReqOrResConstant.STCK_TYPE);
//        // 特殊库存标识(寄售,自有,冻结)
//        itemRecord.setFieldValue("SOBKZ", SapReqOrResConstant.IS_NOT_CONSIGN);
//        // 仓位(非wm不传)
//        itemRecord.setFieldValue("STGE_BIN", "");
//        // 仓储类型(非wm不传)
//        itemRecord.setFieldValue("STGE_TYPE", "");
//        // 使用冲销移动类型标识
//        itemRecord.setFieldValue("XSTOB", "");
//        list.add(groupRecord);
//        list.add(itemRecord);
//        reqMo.setReqGroupRecord(list);
//        byte[] result = remoteEsbSendService.sendToEsb(EsbSendSapConstant.Z_IWMS_COST, reqMo.toString().getBytes());
//        MsgObject resMo = new MsgObject(result, IMsgObject.MOType.initSR);
//        String errorMsg = resMo.getResValue("ERRORMSG");
//        String flag = resMo.getResValue("FLAG");
//        if (!SapReqOrResConstant.SAP_RETURN_TYPE.equals(flag)) {
//            logger.error(errorMsg);
//            throw new ServiceException("sap成本中心退料过账失败!" + errorMsg);
//        }
//        // 凭证号
//        voucherNo = resMo.getResValue("MBLNR");
//        return voucherNo;
    }

    /**
     * 更新台账
     *
     * @param retTaskDto
     * @param storagePositionVo
     */
    public Long updateInventoryDetails(RetTaskDto retTaskDto, StoragePositionVo storagePositionVo) {
        BigDecimal qty = retTaskDto.getQty();
//        MaterialUnitDefDto materialUnitDefDto = SpringUtils.getBean(RetTaskServiceImpl.class)
//                .queryMaterialUnitDef(retTaskDto.getMaterialNo(), storagePositionVo.getFactoryCode());
        //成品/半成品
//        if (MainDataConstant.MATERIAL_TYPE_HALF_PRODUCT.equals(retTaskDto.getType()) || MainDataConstant.MATERIAL_TYPE_PRODUCT.equals(retTaskDto.getType())){
//            //新增台账
//            InventoryDetailsDto inventoryDetailsDto = new InventoryDetailsDto();
//            inventoryDetailsDto.setMaterialNo(retTaskDto.getMaterialNo());
//            inventoryDetailsDto.setMaterialName(retTaskDto.getMaterialName());
//            inventoryDetailsDto.setOldMaterialNo(retTaskDto.getOldMatrialName());
//            inventoryDetailsDto.setPositionNo(storagePositionVo.getPositionNo());
//            inventoryDetailsDto.setPositionId(storagePositionVo.getId());
//            inventoryDetailsDto.setAreaId(storagePositionVo.getAreaId());
//            inventoryDetailsDto.setAreaCode(storagePositionVo.getAreaCode());
//            inventoryDetailsDto.setLocationId(storagePositionVo.getLocationId());
//            inventoryDetailsDto.setLocationCode(storagePositionVo.getLocationCode());
//            inventoryDetailsDto.setFactoryId(storagePositionVo.getFactoryId());
//            inventoryDetailsDto.setFactoryCode(storagePositionVo.getFactoryCode());
//            inventoryDetailsDto.setFactoryName(storagePositionVo.getFactoryName());
//            inventoryDetailsDto.setInventoryQty(qty);
//            inventoryDetailsDto.setAvailableQty(qty);
//            inventoryDetailsDto.setOperationInventoryQty(qty.divide(materialUnitDefDto.getConversDefault(),4, RoundingMode.HALF_UP));
//            inventoryDetailsDto.setOperationAvailableQty(qty.divide(materialUnitDefDto.getConversDefault(),4, RoundingMode.HALF_UP));
//            inventoryDetailsDto.setOperationUnit(materialUnitDefDto.getUnit());
//            inventoryDetailsDto.setConversDefault(materialUnitDefDto.getConversDefault());
//            inventoryDetailsDto.setPreparedQty(BigDecimal.ZERO);
//            inventoryDetailsDto.setOperationPreparedQty(BigDecimal.ZERO);
//            inventoryDetailsDto.setSupplierCode(retTaskDto.getSupplierCode());
//            inventoryDetailsDto.setStockInDate(DateUtils.getNowDate());
//            inventoryDetailsDto.setContainerNo(retTaskDto.getContainerNo());
//            inventoryDetailsDto.setIsFreeze(CommonYesOrNo.NO);
//            inventoryDetailsDto.setIsConsign(CommonYesOrNo.NO);
//            inventoryDetailsDto.setIsQc(CommonYesOrNo.NO);
//            inventoryDetailsDto.setStockInLot(retTaskDto.getLot());
//            inventoryDetailsDto.setWarehouseId(storagePositionVo.getWarehouseId());
//            inventoryDetailsDto.setWarehouseCode(storagePositionVo.getWarehouseCode());
//            List<ContainerLedger> consignReturnOrdersList = new ArrayList<>();
//            //TODO 成品物料退货暂时不允许混箱 容器台账只有一条
//            consignReturnOrdersList.add(new ContainerLedger(){{
//                setContainerNo(retTaskDto.getContainerNo());
//                setContainerType(retTaskDto.getContainerType());
//                setMaterialNo(retTaskDto.getMaterialNo());
//                setOldMaterialNo(retTaskDto.getOldMatrialName());
//                setMaterialName(retTaskDto.getMaterialName());
//                setLot(retTaskDto.getLot());
//                setLotQty(qty);
//                setOperationLotQty(qty.divide(materialUnitDefDto.getConversDefault(),4, RoundingMode.HALF_UP));
//                setFactoryCode(storagePositionVo.getFactoryCode());
//                setConversDefault(materialUnitDefDto.getConversDefault());
//                setOperationUnit(materialUnitDefDto.getUnit());
//            }});
//            inventoryDetailsDto.setContainerLedgerList(consignReturnOrdersList);
//            AjaxResult add = inStoreService.addFin(inventoryDetailsDto);
//            if (add.isError()) {
//                throw new ServiceException("新增库存台账失败！");
//            }
//            return Long.parseLong(add.get("data").toString());
//        }

        //原材料
        //查询当前仓位是否已有台账存在
        InventoryDetailsVo inventoryDetailsVo = new InventoryDetailsVo();
        inventoryDetailsVo.setMaterialNo(retTaskDto.getMaterialNo());
        inventoryDetailsVo.setFactoryId(storagePositionVo.getFactoryId());
        inventoryDetailsVo.setFactoryCode(storagePositionVo.getFactoryCode());
        inventoryDetailsVo.setWarehouseId(storagePositionVo.getWarehouseId());
        inventoryDetailsVo.setWarehouseCode(storagePositionVo.getWarehouseCode());
        inventoryDetailsVo.setAreaId(storagePositionVo.getAreaId());
        inventoryDetailsVo.setAreaCode(storagePositionVo.getAreaCode());
        inventoryDetailsVo.setLocationId(storagePositionVo.getLocationId());
        inventoryDetailsVo.setLocationCode(storagePositionVo.getLocationCode());
        inventoryDetailsVo.setPositionId(storagePositionVo.getId());
        inventoryDetailsVo.setPositionNo(storagePositionVo.getPositionNo());
        inventoryDetailsVo.setStockInLot(retTaskDto.getLot());
        inventoryDetailsVo.setIsQc(retTaskDto.getIsQc());
        inventoryDetailsVo.setIsConsign(CommonYesOrNo.NO);
        inventoryDetailsVo.setIsFreeze(CommonYesOrNo.NO);
        // 排除特殊库存
        inventoryDetailsVo.setStockSpecFlag(CommonYesOrNo.NO);
        AjaxResult listResult = inStoreService.list(inventoryDetailsVo);
        if (listResult.isError()) {
            throw new ServiceException("查询库存台账失败！");
        }
        Long inventoryId;
        List<InventoryDetails> inventoryDetailsList = JSONObject.parseArray(listResult.get("data").toString(), InventoryDetails.class);
        if (ObjectUtils.isEmpty(inventoryDetailsList)) {
            //新增台账
            InventoryDetails inventoryDetails = new InventoryDetails();
            inventoryDetails.setMaterialNo(retTaskDto.getMaterialNo());
            inventoryDetails.setMaterialName(retTaskDto.getMaterialName());
            inventoryDetails.setOldMaterialNo(retTaskDto.getOldMatrialName());
            inventoryDetails.setPositionNo(storagePositionVo.getPositionNo());
            inventoryDetails.setPositionId(storagePositionVo.getId());
            inventoryDetails.setAreaId(storagePositionVo.getAreaId());
            inventoryDetails.setAreaCode(storagePositionVo.getAreaCode());
            inventoryDetails.setLocationId(storagePositionVo.getLocationId());
            inventoryDetails.setLocationCode(storagePositionVo.getLocationCode());
            inventoryDetails.setFactoryId(storagePositionVo.getFactoryId());
            inventoryDetails.setFactoryCode(storagePositionVo.getFactoryCode());
            inventoryDetails.setFactoryName(storagePositionVo.getFactoryName());
            inventoryDetails.setInventoryQty(qty);
            inventoryDetails.setAvailableQty(qty);
//            inventoryDetails.setOperationInventoryQty(qty.divide(materialUnitDefDto.getConversDefault(),4, RoundingMode.HALF_UP));
//            inventoryDetails.setOperationAvailableQty(qty.divide(materialUnitDefDto.getConversDefault(),4, RoundingMode.HALF_UP));
//            inventoryDetails.setOperationUnit(materialUnitDefDto.getUnit());
//            inventoryDetails.setConversDefault(materialUnitDefDto.getConversDefault());
            inventoryDetails.setPreparedQty(BigDecimal.ZERO);
            inventoryDetails.setOperationPreparedQty(BigDecimal.ZERO);
//            inventoryDetails.setSupplierId(taskInfoDto.getVendorId());
            inventoryDetails.setSupplierCode(retTaskDto.getSupplierCode());
//            inventoryDetails.setSupplierName(taskInfoDto.getVendorName());
            inventoryDetails.setStockInDate(DateUtils.getNowDate());
            inventoryDetails.setStockInLot(retTaskDto.getLot());
            inventoryDetails.setIsFreeze(CommonYesOrNo.NO);
            inventoryDetails.setIsQc(retTaskDto.getIsQc());
            inventoryDetails.setIsConsign(CommonYesOrNo.NO);
            inventoryDetails.setWarehouseId(storagePositionVo.getWarehouseId());
            inventoryDetails.setWarehouseCode(storagePositionVo.getWarehouseCode());
            inventoryDetails.setCreateBy(SecurityUtils.getUsername());
            inventoryDetails.setCreateTime(DateUtils.getNowDate());
            AjaxResult add = inStoreService.add(inventoryDetails);
            if (add.isError()) {
                throw new ServiceException("新增库存台账失败！");
            }
            inventoryId = Long.parseLong(add.get("data").toString());
        } else if (inventoryDetailsList.size() == 1) {
            //修改台账
            InventoryDetails inventoryDetails = inventoryDetailsList.get(0);
            BigDecimal sumInventoryQty = inventoryDetails.getInventoryQty().add(qty);
            BigDecimal sumAvailableQty = inventoryDetails.getAvailableQty().add(qty);
            inventoryDetails.setInventoryQty(sumInventoryQty);
            inventoryDetails.setAvailableQty(sumAvailableQty);
//            inventoryDetails.setOperationInventoryQty(sumInventoryQty.divide(materialUnitDefDto.getConversDefault(),4, RoundingMode.HALF_UP));
//            inventoryDetails.setOperationAvailableQty(sumAvailableQty.divide(materialUnitDefDto.getConversDefault(),4, RoundingMode.HALF_UP));
//            inventoryDetails.setOperationUnit(materialUnitDefDto.getUnit());
//            inventoryDetails.setConversDefault(materialUnitDefDto.getConversDefault());
            inventoryDetails.setUpdateBy(SecurityUtils.getUsername());
            inventoryDetails.setUpdateTime(DateUtils.getNowDate());
            if (inStoreService.edit(inventoryDetails).isError()) {
                throw new ServiceException("修改库存台账失败！");
            }
            inventoryId = inventoryDetails.getId();
        } else {
            throw new ServiceException("库存台账不唯一！");
        }
        return inventoryId;
    }

    //物料多级单位查询 入库可用
//    public MaterialUnitDefDto queryMaterialUnitDef(String materialNo, String factoryCode) {
//        MaterialUnitDefDto materialUnitDefDto = new MaterialUnitDefDto();
//        materialUnitDefDto.setFactoryCode(factoryCode);
//        materialUnitDefDto.setMaterialNo(materialNo);
//        materialUnitDefDto.setStockinEnable(CommonYesOrNo.YES);
//        AjaxResult ajaxResult = mainDataService.queryMaterialUnitDef(materialUnitDefDto);
//        if (ajaxResult.isError()) {
//            throw new ServiceException(ajaxResult.get("msg").toString());
//        }
//        List<MaterialUnitDefDto> unitList = JSONObject.parseArray(ajaxResult.get("data").toString(), MaterialUnitDefDto.class);
//        if (ObjectUtils.isEmpty(unitList)) {
//            throw new ServiceException(String.format("物料%s不存在入库使用单位，请维护相关数据",materialNo));
//        }
//        if(unitList.size() > 1){
//            throw new ServiceException(String.format("数据有误，物料%s存在多条入库使用单位",materialNo));
//        }
//        return unitList.get(0);
//    }

    //物料多级单位查询 查询基本单位
    public MaterialUnitDefDto queryMaterialBasicUnitDef(String materialNo, String factoryCode) {
        MaterialUnitDefDto materialUnitDefDto = new MaterialUnitDefDto();
        materialUnitDefDto.setFactoryCode(factoryCode);
        materialUnitDefDto.setMaterialNo(materialNo);
        materialUnitDefDto.setIsDefault(CommonYesOrNo.YES);
        AjaxResult ajaxResult = mainDataService.queryMaterialUnitDef(materialUnitDefDto);
        if (ajaxResult.isError()) {
            throw new ServiceException(ajaxResult.get("msg").toString());
        }
        List<MaterialUnitDefDto> unitList = JSONObject.parseArray(ajaxResult.get("data").toString(), MaterialUnitDefDto.class);
        if (ObjectUtils.isEmpty(unitList)) {
            throw new ServiceException(String.format("物料%s不存在基本单位，请维护相关数据", materialNo));
        }
        if (unitList.size() > 1) {
            throw new ServiceException(String.format("数据有误，物料%s存在多条基本单位", materialNo));
        }
        return unitList.get(0);
    }

    /**
     * 物料凭证冲销
     */
    public void materialReversal(String voucherNo) throws Exception {
        IMsgObject reqMo = new MsgObject(IMsgObject.MOType.initSR);
        Date now = new Date();
        List<GroupRecord> list = new ArrayList<>();
        GroupRecord groupRecord = new GroupRecord();
        // 入参抬头
        groupRecord.setName("IS_HEAD");
        // 物料凭证编号
        groupRecord.setFieldValue("MAT_DOC", voucherNo);
        // 物料凭证年份
        groupRecord.setFieldValue("DOC_YEAR", DateFormatUtils.format(now, "yyyy"));
        // 凭证日期
        groupRecord.setFieldValue("DOC_DATE", DateFormatUtils.format(now, yyyyMMdd));
        // 凭证过账日期
        groupRecord.setFieldValue("PSTNG_DATE", DateFormatUtils.format(now, yyyyMMdd));
        // 用户名
        groupRecord.setFieldValue("USERNAME", SecurityUtils.getUsername());
        // 凭证抬头文本 todo
        groupRecord.setFieldValue("HEADER_TXT", "报错自动冲销");

        list.add(groupRecord);
        reqMo.setReqGroupRecord(list);
        // feign 调用
        byte[] result = remoteEsbSendService.sendToEsb(EsbSendSapConstant.Z_IWMS_REVERSAL, reqMo.toString().getBytes());
        // 转化为标准结果
        MsgObject resMo = new MsgObject(result, IMsgObject.MOType.initSR);
        logger.info("esb返回结果：" + resMo.toString());
        String errorMsg = resMo.getResValue("ERRORMSG");
        String status = resMo.getResValue("STATUS");
        if (!SapReqOrResConstant.SAP_RETURN_TYPE.equals(status)) {
            logger.error(errorMsg);
            throw new ServiceException("物料凭证冲销失败!" + errorMsg);
        }
    }


    /**
     * 委外出库单退货
     */
    public String outSourceReturnToSap(RetTaskDto retTaskDto, RwmOutSourceReturnOrderDetail detail, RwmOutSourceReturnOrder order, InventoryDetailsVo inventoryDetails) throws Exception {

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
        logger.info("发送 sap data :{} ", JSON.toJSONString(sp));
        return sapInteractionService.outSourceReturnToSap(sp);
    }


    private void addTaskLog(RetTaskDto task, InventoryDetails inventoryDetails, RwmOutSourceReturnOrderDetail rwmOutSourceReturnOrderDetail) throws Exception {
        //同步添加任务记录
        TaskLog taskLog = new TaskLog();
        taskLog.setTaskNo(task.getTaskNo());
        taskLog.setTaskType(task.getTaskType());
        taskLog.setMaterialNo(task.getMaterialNo());
        taskLog.setMaterialName(task.getMaterialName());
        taskLog.setOrderNo(task.getStockinOrderNo());
        taskLog.setLot(inventoryDetails.getStockInLot());
        taskLog.setQty(task.getQty());
        taskLog.setFactoryCode(inventoryDetails.getFactoryCode());
        taskLog.setPositionCode(inventoryDetails.getPositionNo());
        taskLog.setLocationCode(inventoryDetails.getLocationCode());
//        taskLog.setMaterialCertificate(stockOutTask.getRemark());
        taskLog.setTargetFactoryCode(inventoryDetails.getFactoryCode());
        taskLog.setTargetLocationCode(inventoryDetails.getLocationCode());
        taskLog.setTargetAreaCode(inventoryDetails.getAreaCode());
        taskLog.setTargetPositionCode(inventoryDetails.getPositionNo());
        taskLog.setUnit(rwmOutSourceReturnOrderDetail.getUnit());
        taskLog.setOperationUnit(inventoryDetails.getOperationUnit());
        taskLog.setOrderNo(rwmOutSourceReturnOrderDetail.getReturnOrderNo());
        taskLog.setOrderType(TaskLogTypeConstant.OUT_SOURCE_RETURN);
        taskLog.setIsConsign(inventoryDetails.getIsConsign());
        taskLog.setIsFreeze(inventoryDetails.getIsFreeze());
        taskLog.setIsQc(inventoryDetails.getIsQc());
        taskLog.setSupplierCode(inventoryDetails.getSupplierCode());
        taskLog.setSupplierName(inventoryDetails.getSupplierName());
        taskLog.setTenantId(SecurityUtils.getLoginUser().getSysUser().getTenantId());
        taskLog.setCreateBy(SecurityUtils.getUsername());
        taskLog.setCreateTime(DateUtils.getNowDate());
        if (taskService.add(taskLog).isError()) {
            throw new ServiceException("新增任务记录失败！");
        }
    }


    /**
     * 更新台账
     *  @param retTaskDto
     * @param storagePositionVo
     * @param detail
     */
    @GlobalTransactional
    public InventoryDetailsVo updateInventoryDetailsByOutSourceReturn(RetTaskDto retTaskDto, StoragePositionVo storagePositionVo, RwmOutSourceReturnOrderDetail detail) {
        InventoryDetailsVo inventory = null;
        //直接查预定表台账
        ReserveStorage reserveStorage = new ReserveStorage();
        reserveStorage.setTaskNo(retTaskDto.getTaskNo());
        AjaxResult revertResult = inStoreService.getReserveStorage(reserveStorage);
        if (revertResult.isError()) {
            throw new ServiceException("查询预定台账失败！");
        }

        String supplierCode = getSupplierCode(retTaskDto);
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

                //新增
                InventoryDetails insert = new InventoryDetails();
                BeanUtils.copyProperties(inventory,insert);
                insert.setInventoryQty(reserveStorage1.getReserveQty());
                insert.setAvailableQty(reserveStorage1.getReserveQty());
                //取推荐仓位的
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

                //关闭出库单状态
                Long outsourcedStockoutOrderDetailId = detail.getOutsourcedStockoutOrderDetailId();
                com.alibaba.fastjson2.JSONObject param = new com.alibaba.fastjson2.JSONObject();
                param.put("outsourcedStockoutOrderDetailId", outsourcedStockoutOrderDetailId );
                param.put("billStatus",OrderStatusConstant.ORDER_STATUS_RETURNED);
                try {
                    AjaxResult ajaxResult1 = inStoreService.updateInventory(inventory);
                    AjaxResult ajaxResult2 = inStoreService.insertWmsInventoryDetails(insert);
                    AjaxResult ajaxResult3 = inStoreService.deleteReserveStorage(new Long[]{reserveStorage1.getId()});
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
