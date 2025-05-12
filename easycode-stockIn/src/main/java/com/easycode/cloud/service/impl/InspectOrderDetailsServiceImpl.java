package com.easycode.cloud.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.easycode.cloud.domain.dto.InspectOrderDto;
import com.easycode.cloud.domain.vo.InspectOrderDetailsListVo;
import com.easycode.cloud.domain.vo.InspectOrderDetailsVo;
import com.easycode.cloud.service.IInspectOrderDetailsService;
import com.easycode.cloud.service.IInspectOrderService;
import com.easycode.cloud.service.IShelfTaskService;
import com.weifu.cloud.common.core.exception.ServiceException;
import com.weifu.cloud.common.core.text.Convert;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.weifu.cloud.common.core.utils.SpringUtils;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.security.utils.SecurityUtils;
import com.weifu.cloud.constant.*;
import com.easycode.cloud.domain.InspectOrder;
import com.easycode.cloud.domain.InspectOrderDetails;
import com.easycode.cloud.domain.ShelfTask;
import com.weifu.cloud.domain.dto.*;
import com.weifu.cloud.domain.vo.InspectInfoVo;
import com.weifu.cloud.domian.InventoryDetails;
import com.weifu.cloud.domian.dto.MaterialUnitDefDto;
import com.weifu.cloud.domian.dto.TaskLogDto;
import com.weifu.cloud.mapper.EsbSendCommonMapper;
import com.easycode.cloud.mapper.InspectOrderDetailsMapper;
import com.easycode.cloud.mapper.InspectOrderMapper;
import com.weifu.cloud.service.*;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StopWatch;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 送检单明细Service业务层处理
 *
 * @author weifu
 * @date 2023-03-29
 */
@Service
public class InspectOrderDetailsServiceImpl implements IInspectOrderDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(InspectOrderDetailsServiceImpl.class);

    @Autowired
    private InspectOrderDetailsMapper inspectOrderDetailsMapper;

    @Autowired
    private InspectOrderMapper inspectOrderMapper;

    @Autowired
    private IStockInService iStockInService;

    @Autowired
    private IStockOutService iStockOutService;

    @Autowired
    private ICodeRuleService iCodeRuleService;

    @Autowired
    private IInStoreService inStoreService;

    @Autowired
    private RemoteEsbSendService remoteEsbSendService;


    @Autowired
    private EsbSendCommonMapper esbSendCommonMapper;

    @Autowired
    private IShelfTaskService shelfTaskService;


    @Autowired
    private ITaskService taskService;

    @Autowired
    private IMainDataService mainDataService;

    @Autowired
    private RemoteConfigHelper remoteConfigHelper;

    @Autowired
    private SapInteractionService sapInteractionService;

    @Autowired
    private IInspectOrderService inspectOrderService;
    /**
     * 查询送检单明细
     *
     * @param id 送检单明细主键
     * @return 送检单明细
     */
    @Override
    public InspectOrderDetails selectInspectOrderDetailsById(Long id) {
        return inspectOrderDetailsMapper.selectInspectOrderDetailsById(id);
    }

    /**
     * 查询送检单明细列表
     *
     * @param inspectOrderDetails 送检单明细
     * @return 送检单明细
     */
    @Override
    public List<InspectOrderDetailsVo> selectInspectOrderDetailsList(InspectOrderDetails inspectOrderDetails) {
        List<InspectOrderDetailsVo> inspectOrderDetailsVos = inspectOrderDetailsMapper.selectInspectOrderDetailsList(inspectOrderDetails);
//        BigDecimal completedQty = inspectOrderDetailsVos
        for (InspectOrderDetailsVo inspectOrderDetailsVo : inspectOrderDetailsVos) {
            BigDecimal returnQty = Optional.ofNullable(inspectOrderDetailsVo.getReturnQty()).isPresent() ? inspectOrderDetailsVo.getReturnQty() : new BigDecimal(0);
            BigDecimal concessionQty = Optional.ofNullable(inspectOrderDetailsVo.getConcessionQty()).isPresent() ? inspectOrderDetailsVo.getConcessionQty() : new BigDecimal(0);
            BigDecimal releaseQty = Optional.ofNullable(inspectOrderDetailsVo.getReleaseQty()).isPresent() ? inspectOrderDetailsVo.getReleaseQty() : new BigDecimal(0);
            BigDecimal destructQty = Optional.ofNullable(inspectOrderDetailsVo.getDestructQty()).isPresent() ? inspectOrderDetailsVo.getDestructQty() : new BigDecimal(0);
            BigDecimal pickQty = Optional.ofNullable(inspectOrderDetailsVo.getPickQty()).isPresent() ? inspectOrderDetailsVo.getPickQty() : new BigDecimal(0);

            BigDecimal completedQty = returnQty.add(concessionQty).add(releaseQty).add(destructQty).add(pickQty);
            inspectOrderDetailsVo.setCompletedQty(completedQty);
            inspectOrderDetailsVo.setCurrentConcessionQty(new BigDecimal(0));
            inspectOrderDetailsVo.setCurrentDestructQty(new BigDecimal(0));
            inspectOrderDetailsVo.setCurrentReleaseQty(new BigDecimal(0));
            inspectOrderDetailsVo.setCurrentReturnQty(new BigDecimal(0));
            inspectOrderDetailsVo.setCurrentPickQty(new BigDecimal(0));
        }
        return inspectOrderDetailsVos;
    }

    /**
     * 查询送检单明细打印信息
     *
     * @param inspectInfoVo 送检单明细
     * @return 送检单明细
     */
    @Override
    public List<InspectInfoVo> getPrintInfoByIds(InspectInfoVo inspectInfoVo) {

        List<InspectInfoVo> printInfo = inspectOrderDetailsMapper.getPrintInfoByIds(inspectInfoVo.getIds());
        printInfo.forEach(item -> item.setQrCode(item.getOrderNo()));
        return printInfo;
    }

    /**
     * 新增送检单明细
     *
     * @param inspectOrderDetails 送检单明细
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertInspectOrderDetails(InspectOrderDetails inspectOrderDetails) {
        inspectOrderDetails.setCreateTime(DateUtils.getNowDate());
        return inspectOrderDetailsMapper.insertInspectOrderDetails(inspectOrderDetails);
    }

    /**
     * 根据物料list、单据状态list及检验结果查询待检数量
     * @param stock 检验单信息
     * @Date 2024/05/13
     * @Author fsc
     * @return 物料待检数量集合
     */
    @Override
    public List<StockInOrderCommonDto> queryWaitInspectNumGroupByMaterialNo(StockInOrderCommonDto stock) {
        return inspectOrderDetailsMapper.queryWaitInspectNumGroupByMaterialNo(stock);
    }

    /**
     * 修改送检单明细
     *
     * @param inspectOrderDetails 送检单明细
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateInspectOrderDetails(InspectOrderDetails inspectOrderDetails) {
        inspectOrderDetails.setUpdateTime(DateUtils.getNowDate());
        return inspectOrderDetailsMapper.updateInspectOrderDetails(inspectOrderDetails);
    }

    /**
     * 批量修改送检单明细
     *
     * @param detailsListVo 送检单明细
     * @return 结果
     */
    @Override
    @GlobalTransactional( rollbackFor = Exception.class)
    public int updateDetailsByList(InspectOrderDetailsListVo detailsListVo) throws Exception {
        logger.info("请求内容：{}", JSONObject.toJSONString(detailsListVo));
        StopWatch stopWatch     = new StopWatch();
        stopWatch.start( RootContext.getXID()+"-释放送检单");
        logger.info("**************事务Id{}", RootContext.getXID());
        logger.info("检验单号：{}", detailsListVo.getOrderNo());
        // 退货一次生成一条单据，送检明细对应单据下的明细
        List<InspectOrderDetailsVo> returnList = new ArrayList<InspectOrderDetailsVo>();
        // 释放/让步接收/挑选 挑选为不合格 但是不走退货
        List<InspectOrderDetailsVo> releaseList = new ArrayList<InspectOrderDetailsVo>();


        for (InspectOrderDetailsVo detailsVo : detailsListVo.getDetailsList()) {
            inspectOrderDetailsMapper.updateInspectOrderDetails(detailsVo);
            BigDecimal totalQty = detailsVo.getConcessionQty().add(detailsVo.getDestructQty())
                    .add(detailsVo.getReleaseQty()).add(detailsVo.getReturnQty()).add(detailsVo.getPickQty());
            if (totalQty.compareTo(detailsVo.getQcQty()) == 0) {
                // 数量之和的等于质检数量，更新送检单表检验状态为已完成
                logger.info("送检单{} 已经完成", detailsVo.getOrderNo());
                InspectOrder inspectOrder = new InspectOrder();
                inspectOrder.setOrderNo(detailsVo.getOrderNo());
                inspectOrder.setLastDisposeTime(DateUtils.getNowDate());
                inspectOrder.setBillStatus(InspectOrderStatusConstant.ORDER_STAT_COMPLETE);
                inspectOrderMapper.updateInspectOrderByNo(inspectOrder);
            } else if (totalQty.compareTo(detailsVo.getQcQty()) < 0) {
                // 数量之和的小于质检数量，更新送检单表检验状态为部分完成
                InspectOrder inspectOrder = new InspectOrder();
                inspectOrder.setOrderNo(detailsVo.getOrderNo());
                inspectOrder.setLastDisposeTime(DateUtils.getNowDate());
//                inspectOrder.setBillStatus(InspectOrderStatusConstant.ORDER_STATUS_PART_COMPLETE);
                inspectOrderMapper.updateInspectOrderByNo(inspectOrder);
            } else {
                throw new ServiceException(String.format("行号%s数据计算异常", detailsVo.getLineNo()));
            }

            // 释放\让步接收\挑选数量大于0
            if (detailsVo.getCurrentReleaseQty().compareTo(new BigDecimal(0)) > 0 ||
                    detailsVo.getCurrentConcessionQty().compareTo(new BigDecimal(0)) > 0 ||
                    detailsVo.getCurrentPickQty().compareTo(new BigDecimal(0)) > 0) {
                // TODO 解Q
                releaseList.add(detailsVo);
            }
            // 退货\破坏大于0
            if (detailsVo.getCurrentReturnQty().compareTo(new BigDecimal(0)) > 0 ||
                    detailsVo.getCurrentDestructQty().compareTo(new BigDecimal(0)) > 0 ) {
                returnList.add(detailsVo);
            }
        }
        List<String> voucherNoList = new ArrayList<>();
        // 释放明细
        if (releaseList.size() > 0) {

            List<InventoryDetails> inventoryDetailsList = new ArrayList<InventoryDetails>();
            // 根据送检单号查询相关信息
            InspectOrder inspectOrder = inspectOrderMapper.selectInspectOrderByNo(detailsListVo.getOrderNo());

            List<FactoryCommonDto> factoryInfo = esbSendCommonMapper.getFactoryByCode(inspectOrder.getFactoryCode());
            if (factoryInfo.size() <= 0) {
                throw new ServiceException(String.format("根据工厂code:%s未查询到相关信息", inspectOrder.getFactoryCode()));
            }

            for (InspectOrderDetailsVo details : releaseList) {
                InventoryDetails inventoryDetails = new InventoryDetails();
                inventoryDetails.setId(detailsListVo.getDetailsList().get(0).getInventoryId());
                inventoryDetails.setIsQc("1");
                inventoryDetails.setMaterialNo(details.getMaterialNo());
                inventoryDetails.setStockInLot(details.getLot());
                inventoryDetails.setFactoryCode(inspectOrder.getFactoryCode());
                // 页面输入让步接收数量和释放数量不会同时有值
                inventoryDetails.setInventoryQty(details.getCurrentReleaseQty().add(details.getCurrentConcessionQty()).add(details.getCurrentPickQty()));
                inventoryDetailsList.add(inventoryDetails);

            }
            // list 只会有一条
            if (inventoryDetailsList.size() > 1) {
                throw new ServiceException("送检详情不允许超过一条");
            }
            //
            AjaxResult isConsignResult = inStoreService.checkIsConsign(inventoryDetailsList.get(0));
            if (isConsignResult.isError() || StringUtils.isEmpty(isConsignResult.get("data").toString())) {
                throw new ServiceException(Objects.toString(isConsignResult.get("msg"), "查询台账是否寄售失败"));
            }
            List<CommonDto> data = JSONObject.parseArray(isConsignResult.get("data").toString(), CommonDto.class);
            String isConsign = data.get(0).getIsConsign();

            //返回操作的台账id
            AjaxResult ajaxResult = inStoreService.batchEdit(inventoryDetailsList);
            if (!"200".equals(Objects.toString(ajaxResult.get("code"), "500")) || ajaxResult.isError()) {
                throw new ServiceException(Objects.toString(ajaxResult.get("msg"), "同步台账异常"));
            }
            String resultId = ajaxResult.get("data").toString();

            // 与SAP释放/让步接收
            for (InspectOrderDetailsVo details : releaseList) {
                BigDecimal qty = details.getCurrentReleaseQty().add(details.getCurrentConcessionQty()).add(details.getCurrentPickQty());
                String moveReason = "";
                if (details.getCurrentReleaseQty().compareTo(new BigDecimal(0)) > 0) {
                    moveReason = InspectMoveReasonConstant.INSPECT_QUALIFIED;
                } else if (details.getCurrentConcessionQty().compareTo(new BigDecimal(0)) > 0) {
                    moveReason = InspectMoveReasonConstant.INSPECT_QUALIFIED;
                } else if (details.getCurrentPickQty().compareTo(new BigDecimal(0)) > 0) {
                    moveReason = InspectMoveReasonConstant.INSPECT_QUALIFIED;
                } else {
                    throw new ServiceException("数据异常");
                }

                MaterialAttrDto materialAttrDto = new MaterialAttrDto();
                materialAttrDto.setFactoryId(factoryInfo.get(0).getId());
                materialAttrDto.setMaterialNo(details.getMaterialNo());
                List<MaterialMainDto> materialMain = esbSendCommonMapper.getMaterialMain(materialAttrDto);
                if (materialMain.size() <= 0) {
                    throw new ServiceException(String.format("根据物料code:%s未查询到相关信息", details.getMaterialNo()));
                }
                // 获取多级单位信息
                MaterialUnitDefDto unitDefDto = new MaterialUnitDefDto();
                unitDefDto.setFactoryCode(inspectOrder.getFactoryCode());
                unitDefDto.setMaterialNo(details.getMaterialNo());
                unitDefDto.setStockinEnable(CommonYesOrNo.YES);
                AjaxResult unitResult = mainDataService.queryMaterialUnitDef(unitDefDto);
                if (unitResult.isError()) {
                    throw new ServiceException(unitResult.get("msg").toString());
                }
                List<MaterialUnitDefDto> unitList = JSONObject.parseArray(unitResult.get("data").toString(), MaterialUnitDefDto.class);
                if (ObjectUtils.isEmpty(unitList)) {
                    throw new ServiceException(String.format("物料%s、货主%s不存在入库使用单位，请维护相关数据", inspectOrder.getMaterialNo(), inspectOrder.getFactoryCode()));
                }
                if (unitList.size() > 1) {
                    throw new ServiceException(String.format("数据有误，物料%s、货主%s存在多条入库使用单位", details.getMaterialNo(), inspectOrder.getFactoryCode()));
                }
                // 换算单位
                BigDecimal conversDefault = unitList.get(0).getConversDefault();
                BigDecimal operationQty = qty.divide(conversDefault, 4, RoundingMode.HALF_UP);
                Long tenantId = SecurityUtils.getLoginUser().getSysUser().getTenantId();

                // 生成上架任务
                // 上架任务号
                AjaxResult result = iCodeRuleService.getSeqWithTenantId(TaskNoTypeConstant.SHELF_TASK, String.valueOf(tenantId));
                if (result.isError() || StringUtils.isEmpty(result.get("data").toString())) {
                    throw new ServiceException("上架任务号生成失败！");
                }
                String taskNo = result.get("data").toString();
                ShelfTask shelfTask = new ShelfTask();
                shelfTask.setTaskNo(taskNo);
                shelfTask.setTaskType(StockInTaskConstant.INSPECT_TYPE);
                shelfTask.setStatus(TaskStatusConstant.TASK_STATUS_NEW);
                shelfTask.setAllocateTime(DateUtils.getNowDate());
                shelfTask.setMaterialNo(details.getMaterialNo());
                //TODO 质检物料名称、旧物料号
                shelfTask.setMaterialName(materialMain.get(0).getMaterialName());
                shelfTask.setStockinOrderNo(details.getOrderNo());
                shelfTask.setSourceLocationCode(inspectOrder.getLocationCode());
                shelfTask.setSourcePositionNo(details.getPositionNo());
                shelfTask.setLot(details.getLot());
                shelfTask.setUnit(details.getUnit());
                shelfTask.setOperationUnit(details.getOperationUnit());
                shelfTask.setOperationCompleteQty(BigDecimal.ZERO);
                shelfTask.setInventoryId(Long.parseLong(resultId));
                shelfTask.setQty(qty);
                shelfTask.setOperationQty(operationQty);
                shelfTask.setCompleteQty(BigDecimal.ZERO);
                shelfTask.setCreateBy(SecurityUtils.getUsername());
                shelfTask.setCreateTime(DateUtils.getNowDate());
                shelfTaskService.insertShelfTask(shelfTask);

//                inspectReleaseSendSap(details.getMaterialNo(), inspectOrder.getFactoryCode(), inspectOrder.getSupplierCode(), qty,
//                        materialMain.get(0).getDefaultUnit(), inspectOrder.getLocationCode(), moveReason, details.getPrdLot(), isConsign);


            }
        }
        // 有退货明细
        if (returnList.size() > 0) {
            // 根据送检单号查询相关信息
            InspectOrder inspectOrder = inspectOrderMapper.selectInspectOrderByNo(detailsListVo.getOrderNo());
            List<InventoryDetails> inventoryDetailsList = new ArrayList<InventoryDetails>();
            for (InspectOrderDetailsVo details : returnList) {
                InventoryDetails inventoryDetails = new InventoryDetails();
                inventoryDetails.setIsQc("1");
                inventoryDetails.setMaterialNo(details.getMaterialNo());
                inventoryDetails.setStockInLot(details.getLot());
                inventoryDetails.setFactoryCode(inspectOrder.getFactoryCode());
                inventoryDetails.setId(detailsListVo.getDetailsList().get(0).getInventoryId());
                // 页面输入让步接收数量和释放数量不会同时有值
                inventoryDetails.setInventoryQty(details.getCurrentReturnQty().add(details.getCurrentDestructQty()));
                inventoryDetailsList.add(inventoryDetails);
            }
            logger.info("Seata全局事务id=================>{}", RootContext.getXID());
            System.out.println(RootContext.getXID());

            AjaxResult ajaxResultFirst = inStoreService.batchDeduction(inventoryDetailsList);
            if (!"200".equals(Objects.toString(ajaxResultFirst.get("code"), "500")) || ajaxResultFirst.isError()) {
                // 冲销
                materialReversal(voucherNoList);
                throw new ServiceException(Objects.toString(ajaxResultFirst.get("msg"), "同步台账异常"));
            }
            Long tenantId = SecurityUtils.getLoginUser().getSysUser().getTenantId();

            // 生成单据
            //生成单号
            AjaxResult ajaxResult = iCodeRuleService.getSeqWithTenantId(OrderNoTypeConstant.RAW, String.valueOf(tenantId));
            if ("".equals(Objects.toString(ajaxResult.get("data"), "")) || ajaxResult.isError()) {
                // 冲销
                materialReversal(voucherNoList);
                throw new ServiceException("原材料退货单号生成失败！");
            }
            String order = ajaxResult.get("data").toString();
            // 原材料退货中插入一条已完成的数据
            RwmReturnOrderDto rwmReturnOrderDto = new RwmReturnOrderDto();
            rwmReturnOrderDto.setOrderNo(order);
            rwmReturnOrderDto.setPurchaseOrderNo(inspectOrder.getPurchaseOrderNo());
            rwmReturnOrderDto.setFactoryCode(inspectOrder.getFactoryCode());
            List<FactoryCommonDto> factoryInfo = esbSendCommonMapper.getFactoryByCode(inspectOrder.getFactoryCode());
            if (factoryInfo.size() <= 0) {
                // 冲销
                materialReversal(voucherNoList);
                throw new ServiceException(String.format("根据工厂code:%s未查询到相关信息", inspectOrder.getFactoryCode()));
            }
            rwmReturnOrderDto.setFactoryId(factoryInfo.get(0).getId());
            rwmReturnOrderDto.setIsShortage(CommonYesOrNo.NO);
            rwmReturnOrderDto.setBillStatus(OrderStatusConstant.ORDER_STATUS_COMPLETE);
            rwmReturnOrderDto.setSupplierCode(inspectOrder.getSupplierCode());
            SupplierCommonDto supplierInfoByCode = esbSendCommonMapper.getSupplierInfoByCode(inspectOrder.getSupplierCode());
            if (!Optional.ofNullable(supplierInfoByCode).isPresent()) {
                // 冲销
                materialReversal(voucherNoList);
                throw new ServiceException(String.format("根据供应商code:%s未查询到相关信息", inspectOrder.getSupplierCode()));
            }
            rwmReturnOrderDto.setSupplierId(supplierInfoByCode.getId());
            rwmReturnOrderDto.setLocationCode(inspectOrder.getLocationCode());
            // TODO 退货方式
//            rwmReturnOrderDto.setReturnMethod();
            iStockOutService.addRwmOrderTask(rwmReturnOrderDto);

            List<String> materialList = new ArrayList<>();
            returnList.forEach(item -> materialList.add(item.getMaterialNo()));

            MaterialUnitDefDto materialUnitDefDto = new MaterialUnitDefDto();
            materialUnitDefDto.setMaterialNoList(materialList);
            materialUnitDefDto.setStockoutEnable(CommonYesOrNo.YES);
            materialUnitDefDto.setFactoryCode(SecurityUtils.getComCode());
            AjaxResult unitResult = mainDataService.batchMaterialUnitDef(materialUnitDefDto);
            if (unitResult.isError()) {
                throw new ServiceException(unitResult.get("msg").toString());
            }
            List<MaterialUnitDefDto> unitList = JSONObject.parseArray(unitResult.get("data").toString(), MaterialUnitDefDto.class);
            if (ObjectUtils.isEmpty(unitList)) {
                // 冲销
                materialReversal(voucherNoList);
                throw new ServiceException(String.format("不存在出库使用单位，请维护相关数据"));
            }
            Map<String, BigDecimal> map = unitList.stream().collect(Collectors.toMap(MaterialUnitDefDto::getMaterialNo, MaterialUnitDefDto::getConversDefault));

            // 原材料退货明细
            RwmReturnOrderDetailsDto returnDetails = null;
            for (InspectOrderDetailsVo details : returnList) {
                returnDetails = new RwmReturnOrderDetailsDto();
                returnDetails.setFactoryCode(rwmReturnOrderDto.getFactoryCode());
                returnDetails.setLineNo(details.getLineNo());
                returnDetails.setDeliveryOrderNo(inspectOrder.getReceiveOrderNo());
                // TODO 送货单行号
                returnDetails.setDeliveryLineNo("1");
                returnDetails.setLot(details.getLot());
                returnDetails.setMaterialCode(details.getMaterialNo());

                // 根据物料、批次、任务类型、数量查询任务记录中的对应数据，获取物料凭证
                TaskLogDto taskLogDto = new TaskLogDto();
                taskLogDto.setMaterialNo(details.getMaterialNo());
                taskLogDto.setLot(details.getPrdLot());
                taskLogDto.setQty(details.getQcQty());
                AjaxResult result = taskService.queryTaskLogList(taskLogDto);

                if ("".equals(Objects.toString(result.get("data"), "")) || result.isError()) {
                    // 冲销
                    materialReversal(voucherNoList);
                    throw new ServiceException("获取任务执行记录信息失败！");
                }
                String str = JSON.toJSONString(result.get("data"));
                List<TaskLogDto> list = JSON.parseArray(str, TaskLogDto.class);
                if (list.size() == 0) {
                    // 冲销
                    materialReversal(voucherNoList);
                    throw new ServiceException(String.format("根据物料号%s批次号%s及数量%s未查询到标准入库任务记录", details.getMaterialNo(), details.getPrdLot(), details.getQcQty()));
                }

                returnDetails.setWriteOffCert(list.get(0).getMaterialCertificate());

                MaterialAttrDto materialAttrDto = new MaterialAttrDto();
                materialAttrDto.setFactoryId(factoryInfo.get(0).getId());
                materialAttrDto.setMaterialNo(details.getMaterialNo());
                List<MaterialMainDto> materialMain = esbSendCommonMapper.getMaterialMain(materialAttrDto);
                if (materialMain.size() <= 0) {
                    // 冲销
                    materialReversal(voucherNoList);
                    throw new ServiceException(String.format("根据物料code:%s未查询到相关信息", details.getMaterialNo()));
                }
                returnDetails.setMaterialName(materialMain.get(0).getMaterialName());
                returnDetails.setOldMaterialNo(materialMain.get(0).getOldMaterialNo());
                returnDetails.setUnit(materialMain.get(0).getDefaultUnit());
                returnDetails.setOderNo(rwmReturnOrderDto.getOrderNo());
                returnDetails.setPurchaseOrderNo(rwmReturnOrderDto.getPurchaseOrderNo());
                returnDetails.setPurchaseLineNo(inspectOrder.getPurchaseLineNo());

                BigDecimal converterDefault = Optional.ofNullable(map.get(returnDetails.getMaterialCode())).orElse(BigDecimal.ONE);
                returnDetails.setActiveQty(details.getCurrentReturnQty().add(details.getCurrentDestructQty()));
                returnDetails.setCompleteQty(details.getCurrentReturnQty().add(details.getCurrentDestructQty()));
                returnDetails.setQty(details.getCurrentReturnQty().add(details.getCurrentDestructQty()));
                BigDecimal operationQty = returnDetails.getQty().divide(converterDefault, 4, RoundingMode.HALF_UP);
                BigDecimal operationActiveQty = returnDetails.getActiveQty().divide(converterDefault, 4, RoundingMode.HALF_UP);
                BigDecimal operationCompleteQty = returnDetails.getCompleteQty().divide(converterDefault, 4, RoundingMode.HALF_UP);
                returnDetails.setOperationQty(operationQty);
                returnDetails.setOperationActiveQty(operationActiveQty);
                returnDetails.setOperationCompleteQty(operationCompleteQty);
                returnDetails.setOderNo(order);
                iStockOutService.addRwmOrderTaskDetail(returnDetails);

                // 采购订单数量回写
                writeBackPurchase(voucherNoList, inspectOrder, details.getCurrentReturnQty().add(details.getCurrentDestructQty()));

//                 退货原材料退货SAP过账
                purchaseReturnToSap(inspectOrder, details, returnDetails);
            }
        }
        stopWatch.stop();
        logger.info("质检释放耗时：" + stopWatch.prettyPrint());
        return HttpStatus.SC_OK;
    }

    /**
     * 物料凭证冲销
     *
     * @param voucherNoList
     */
    public void materialReversal(List<String> voucherNoList) {
        if (!CollectionUtils.isEmpty(voucherNoList)) {
            for (String voucherNo : voucherNoList) {
                AjaxResult result = iStockOutService.materialReversal(voucherNo);
                if (result.isError()) {
                    throw new ServiceException("调用物料凭证冲销接口失败！");
                }
            }
        }
    }



    /**
     * @Description: 退货SAP过账
     * @Param: [inspectOrder, inspectOrderDetailsVo, returnOrderDetailsDto]
     * @return: java.lang.String
     * @Author: fangshucheng
     * @Date: 2023/5/17
     */
    public String purchaseReturnToSap(InspectOrder inspectOrder, InspectOrderDetailsVo inspectOrderDetailsVo, RwmReturnOrderDetailsDto returnOrderDetailsDto) throws Exception {
        String defaultBatchNo = remoteConfigHelper.getConfig(ConfigConstant.DEFAULT_SAP_BATCH);
        if("".equals(Objects.toString(defaultBatchNo,""))){
            throw new ServiceException("配置参数SAP默认批次获取失败！");
        }

        Date now = new Date();
        SapPurchaseReturnParamsDto sp = new SapPurchaseReturnParamsDto();

        sp.setBudat(DateFormatUtils.format(now, "yyyyMMdd"));
        sp.setBldat(DateFormatUtils.format(now, "yyyyMMdd"));
        sp.setBktxt(SecurityUtils.getUsername());
        sp.setuName(SecurityUtils.getUsername());
        // 物料
        sp.setMatnr(inspectOrderDetailsVo.getMaterialNo());
        sp.setBwark("122");
        sp.setPlant(inspectOrder.getFactoryCode());
        sp.setBatch(inspectOrderDetailsVo.getLot());
        sp.setWempf(inspectOrder.getLocationCode());
        sp.setLgtyp("");
        sp.setLgpla("");
        // 以录入项单位表示的数量
        String qty = inspectOrderDetailsVo.getCurrentReturnQty().add(inspectOrderDetailsVo.getCurrentDestructQty()).toString();
        sp.setErfmg(qty);
        sp.setErfme(returnOrderDetailsDto.getUnit());
        sp.setInsmk(SapReqOrResConstant.EXEMPTED_MAP.get(CommonYesOrNo.YES));
        sp.setSpecStock(SapReqOrResConstant.SPECIAL_MAP.get(inspectOrder.getIsConsign()));
        sp.setLifnr(inspectOrder.getSupplierCode());
        sp.setPoNumber(inspectOrder.getPurchaseOrderNo());
        sp.setPoItem(inspectOrder.getPurchaseLineNo());
        sp.setMvtInd("B");
        sp.setNoTransferReq("");
        sp.setMoveReas("0001");
        sp.setRefDoc(returnOrderDetailsDto.getWriteOffCert());
//        sp.setVoucherNos(voucherNos);

        return sapInteractionService.purchaseReturnToSap(sp);

//        Date now = new Date();
//        IMsgObject reqMo = new MsgObject(IMsgObject.MOType.initSR);
//        List<GroupRecord> list = new ArrayList<>();
//        GroupRecord groupRecord = new GroupRecord();
//        groupRecord.setName("IS_HEAD");
//        groupRecord.setFieldValue("BUDAT", DateFormatUtils.format(now, "yyyyMMdd"));
//        groupRecord.setFieldValue("BLDAT", DateFormatUtils.format(now, "yyyyMMdd"));
//        groupRecord.setFieldValue("BKTXT", SecurityUtils.getUsername());
//        groupRecord.setFieldValue("UNAME", SecurityUtils.getUsername());
//        GroupRecord itemRecord = new GroupRecord();
//        itemRecord.setName("IT_ITEM");
//        // 物料
//        itemRecord.setFieldValue("MATNR", inspectOrderDetailsVo.getMaterialNo());
//        itemRecord.setFieldValue("BWART", "122");
//        // 工厂
//        itemRecord.setFieldValue("PLANT", inspectOrder.getFactoryCode());
//        // 批次编号
//        itemRecord.setFieldValue("BATCH", defaultBatchNo);
//        // 库存地点
//        itemRecord.setFieldValue("WEMPF", inspectOrder.getLocationCode());
//        // 仓储类型
//        itemRecord.setFieldValue("LGTYP", "");
//        // 仓位
//        itemRecord.setFieldValue("LGPLA", "");
//        // 以录入项单位表示的数量
//        String qty = inspectOrderDetailsVo.getCurrentReturnQty().add(inspectOrderDetailsVo.getCurrentDestructQty()).toString();
//        itemRecord.setFieldValue("ERFMG", qty);
//        // 条目单位
//        itemRecord.setFieldValue("ERFME", returnOrderDetailsDto.getUnit());
//        // 库存类型
//        itemRecord.setFieldValue("INSMK", SapReqOrResConstant.EXEMPTED_MAP.get(CommonYesOrNo.YES));
//        // TODO 特殊库存标识
//        itemRecord.setFieldValue("SPEC_STOCK", SapReqOrResConstant.SPECIAL_MAP.get(inspectOrder.getIsConsign()));
//        // 供应商帐户号
//        itemRecord.setFieldValue("LIFNR", inspectOrder.getSupplierCode());
//        // 采购订单号
//        itemRecord.setFieldValue("PO_NUMBER", inspectOrder.getPurchaseOrderNo());
//        // 采购订单行号
//        itemRecord.setFieldValue("PO_ITEM", inspectOrder.getPurchaseLineNo());
//        // 移动标识
//        itemRecord.setFieldValue("MVT_IND", "B");
//        // 标识: 未创建转移要求
//        itemRecord.setFieldValue("NO_TRANSFER_REQ", "");
//        // 移动原因
//        itemRecord.setFieldValue("MOVE_REAS", "0001");
//        // 参考凭证的凭证号
//        itemRecord.setFieldValue("REF_DOC", returnOrderDetailsDto.getWriteOffCert());
//        list.add(groupRecord);
//        list.add(itemRecord);
//        reqMo.setReqGroupRecord(list);
//        byte[] result = remoteEsbSendService.sendToEsb(EsbSendSapConstant.Z_IWMS_RETURN, reqMo.toString().getBytes());
//        // 转化为标准结果
//        MsgObject resMo = new MsgObject(result, IMsgObject.MOType.initSR);
//        logger.info("esb返回结果：" + resMo.toString());
//        String errorMsg = resMo.getResValue("ERRORMSG");
//        String flag = resMo.getResValue("FLAG");
//        if (!SapReqOrResConstant.SAP_RETURN_TYPE.equals(flag)) {
//            logger.error(errorMsg);
//            throw new ServiceException("采购订单退货sap过账失败!" + errorMsg);
//        }
//        // 凭证号
//        voucherNo = resMo.getResValue("MBLNR");

    }

    public void writeBackPurchase(List<String> voucherNoList, InspectOrder inspectOrder, BigDecimal qty) {

        // 调用供应商服务 根据采购单号、采购单行号 累加收货数量
        PurchaseOrderDetailDto purchaseOrderDetailDto = new PurchaseOrderDetailDto();
        purchaseOrderDetailDto.setPurchaseOrderNo(inspectOrder.getPurchaseOrderNo());
        purchaseOrderDetailDto.setPurchaseLineNo(Convert.toStr(inspectOrder.getPurchaseLineNo()));
        purchaseOrderDetailDto.setReturnQty(qty);
        AjaxResult result = iStockInService.updatePurchaseReturnQty(purchaseOrderDetailDto);
        if ("".equals(Objects.toString(result.get("data"), "")) || result.isError()) {
            materialReversal(voucherNoList);
            throw new ServiceException("质检退货回写采购单数量方法失败！");
        }
    }

    /**
     * 批量删除送检单明细
     *
     * @param ids 需要删除的送检单明细主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteInspectOrderDetailsByIds(Long[] ids) {
        return inspectOrderDetailsMapper.deleteInspectOrderDetailsByIds(ids);
    }

    /**
     * 删除送检单明细信息
     *
     * @param id 送检单明细主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteInspectOrderDetailsById(Long id) {
        return inspectOrderDetailsMapper.deleteInspectOrderDetailsById(id);
    }

    /**
     * 批量释放送检单
     * @param inspectOrderDto 送检单dto
     * @return 未成功的送检单号
     */
    @Override
    public InspectOrderDto releaseBatchInspectOrderDetails(InspectOrderDto inspectOrderDto) {
        if (ObjectUtils.isEmpty(inspectOrderDto)){
            throw new ServiceException("参数不存在!");
        }
        List<InspectOrderDetailsVo> inspectOrderDetailsVos = inspectOrderDetailsMapper.selectInspectOrderDetailsListByOrderNos(inspectOrderDto);
        //失败送检单号
        List<String> orderNos = new ArrayList<>();
        List<InspectOrderDetailsVo> list = new ArrayList<>();
        for (InspectOrderDetailsVo inspectOrderDetailsVo : inspectOrderDetailsVos) {
            //退货数量
            BigDecimal returnQty = Optional.ofNullable(inspectOrderDetailsVo.getReturnQty()).isPresent() ? inspectOrderDetailsVo.getReturnQty() : new BigDecimal(0);
            //让步接收数量
            BigDecimal concessionQty = Optional.ofNullable(inspectOrderDetailsVo.getConcessionQty()).isPresent() ? inspectOrderDetailsVo.getConcessionQty() : new BigDecimal(0);
            //释放数量
            BigDecimal releaseQty = Optional.ofNullable(inspectOrderDetailsVo.getReleaseQty()).isPresent() ? inspectOrderDetailsVo.getReleaseQty() : new BigDecimal(0);
            //破坏数量
            BigDecimal destructQty = Optional.ofNullable(inspectOrderDetailsVo.getDestructQty()).isPresent() ? inspectOrderDetailsVo.getDestructQty() : new BigDecimal(0);
            //挑选数量
            BigDecimal pickQty = Optional.ofNullable(inspectOrderDetailsVo.getPickQty()).isPresent() ? inspectOrderDetailsVo.getPickQty() : new BigDecimal(0);

            BigDecimal completedQty = returnQty.add(concessionQty).add(releaseQty).add(destructQty).add(pickQty);
            //质检数量 等于 完成数量时不进行处理
            if(inspectOrderDetailsVo.getQcQty().compareTo(completedQty) <= 0){
                continue;
            }
            inspectOrderDetailsVo.setCompletedQty(completedQty);
            inspectOrderDetailsVo.setCurrentConcessionQty(BigDecimal.ZERO);
            inspectOrderDetailsVo.setCurrentDestructQty(BigDecimal.ZERO);
            inspectOrderDetailsVo.setCurrentReleaseQty(inspectOrderDetailsVo.getQcQty().subtract(completedQty));
            inspectOrderDetailsVo.setCurrentReturnQty(BigDecimal.ZERO);
            inspectOrderDetailsVo.setCurrentPickQty(BigDecimal.ZERO);

            inspectOrderDetailsVo.setConcessionQty(concessionQty);
            inspectOrderDetailsVo.setDestructQty(destructQty);
            inspectOrderDetailsVo.setReleaseQty(inspectOrderDetailsVo.getQcQty().subtract(completedQty));
            inspectOrderDetailsVo.setReturnQty(returnQty);
            inspectOrderDetailsVo.setPickQty(pickQty);

            list.add(inspectOrderDetailsVo);
            InspectOrderDetailsListVo inspectOrderDetailsListVo = new InspectOrderDetailsListVo();
            inspectOrderDetailsListVo.setDetailsList(list);
            inspectOrderDetailsListVo.setOrderNo(inspectOrderDetailsVo.getOrderNo());
            //执行释放逻辑
            try {
                SpringUtils.getBean(InspectOrderDetailsServiceImpl.class).updateDetailsByList(inspectOrderDetailsListVo);
            } catch (Exception e) {
                orderNos.add(inspectOrderDetailsVo.getOrderNo());
                logger.info(String.format("送检单:%s释放失败",inspectOrderDetailsVo.getOrderNo()));
                logger.error(String.format("送检单:%s释放失败",inspectOrderDetailsVo.getOrderNo()),e);

            } finally {
                list.clear();
            }
        }
        inspectOrderDto.setOrderNos(orderNos);

        // 取消质检接口
        inspectOrderService.cancelInspectTask(inspectOrderDto.getOrderNo());
        return inspectOrderDto;
    }
}
