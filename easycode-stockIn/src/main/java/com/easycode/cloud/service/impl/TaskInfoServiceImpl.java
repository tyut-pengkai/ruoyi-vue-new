//package com.weifu.cloud.service.impl;
//
//import com.alibaba.fastjson.JSONObject;
//import com.alibaba.fastjson2.JSON;
//import com.baomidou.lock.annotation.Lock4j;
//import com.soa.eis.adapter.framework.message.IMsgObject;
//import com.soa.eis.adapter.framework.message.impl.GroupRecord;
//import com.soa.eis.adapter.framework.message.impl.MsgObject;
//import com.weifu.cloud.common.core.exception.ServiceException;
//import com.weifu.cloud.common.core.text.Convert;
//import com.weifu.cloud.common.core.utils.DateUtils;
//import com.weifu.cloud.common.core.utils.SpringUtils;
//import com.weifu.cloud.common.core.web.domain.AjaxResult;
//import com.weifu.cloud.common.security.utils.SecurityUtils;
//import com.weifu.cloud.constant.*;
//import com.weifu.cloud.domain.*;
//import com.weifu.cloud.domain.dto.*;
//import com.weifu.cloud.domain.vo.DeliveryOrderVo;
//import com.weifu.cloud.domain.vo.StockInFinOrderDetailVo;
//import com.weifu.cloud.domain.vo.TaskInfoVo;
//import com.weifu.cloud.domian.ContainerLedger;
//import com.weifu.cloud.domian.InventoryDetails;
//import com.weifu.cloud.domian.TaskLog;
//import com.weifu.cloud.domian.dto.InventoryDetailsDto;
//import com.weifu.cloud.domian.dto.MaterialUnitDefDto;
//import com.weifu.cloud.domian.dto.StoragePositionDto;
//import com.weifu.cloud.domian.vo.InventoryDetailsVo;
//import com.weifu.cloud.domian.vo.StoragePositionVo;
//import com.weifu.cloud.enums.RemoteConfigEnum;
//import com.weifu.cloud.mapper.*;
//import com.weifu.cloud.service.*;
//import io.seata.spring.annotation.GlobalTransactional;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Primary;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.CollectionUtils;
//import org.springframework.util.ObjectUtils;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.util.*;
//import java.util.stream.Collectors;
//
///**
// * 仓管任务Service业务层处理
// *
// * @author weifu
// * @date 2022-12-12
// */
//@Service
//@Primary
//@Transactional(rollbackFor = Exception.class)
//public class TaskInfoServiceImpl implements ITaskInfoService {
//
//    private static final Logger logger = LoggerFactory.getLogger(TaskInfoServiceImpl.class);
//
//    @Autowired
//    private TaskInfoMapper taskInfoMapper;
//
//    @Autowired
//    private StockInStdOrderMapper stockInStdOrderMapper;
//
//    @Autowired
//    private StockInFinOrderMapper stockInFinOrderMapper;
//
//    @Autowired
//    private StockInFinOrderDetailMapper stockInFinOrderDetailMapper;
//
//    @Autowired
//    private StockInStdOrderDetailMapper stockInStdOrderDetailMapper;
//
//    @Autowired
//    private InspectOrderDetailsMapper inspectOrderDetailsMapper;
//
//    @Autowired
//    private InspectOrderMapper inspectOrderMapper;
//
//    @Autowired
//    private IInStoreService inStoreService;
//
//    @Autowired
//    private IMainDataService iMainDataService;
//
//
//    @Autowired
//    private EsbSendCommonMapper esbSendCommonMapper;
//
//    @Autowired
//    private RemoteEsbSendService remoteEsbSendService;
//
//    @Autowired
//    private ICodeRuleService iCodeRuleService;
//
//    @Autowired
//    private IShelfTaskService shelfTaskService;
//
//    @Autowired
//    private IStockInService iStockInService;
//
//    @Autowired
//    private RemoteConfigHelper remoteConfigHelper;
//
//    @Autowired
//    private ITaskService taskService;
//
//    @Autowired
//    private IStockOutService iStockOutService;
//
//    @Autowired
//    private IStockInFinOrderService stockInFinOrderService;
//
//    @Autowired
//    private RetTaskServiceImpl retTaskService;
//
//    @Autowired
//    private StockInSemiFinOrderMapper stockInSemiFinOrderMapper;
//
//    @Autowired
//    private StockInSemiFinOrderDetailMapper stockInSemiFinOrderDetailMapper;
//
//    @Autowired
//    private SapInteractionService sapInteractionService;
//
//    /**
//     * 查询仓管任务
//     *
//     * @param id 仓管任务主键
//     * @return 仓管任务
//     */
//    @Override
//    public TaskInfo selectTaskInfoById(Long id) {
//        return taskInfoMapper.selectTaskInfoById(id);
//    }
//
//    @Override
//    public int submitStdAsnOrderTask(TaskInfoDto taskInfoDto) throws Exception {
//        return 0;
//    }
//
//    @Override
//    public TaskInfoVo getStdOrderTaskDetail(Long id) {
//        TaskInfoVo taskInfoVo = taskInfoMapper.getStdOrderTaskDetail(id);
//        if (ObjectUtils.isEmpty(taskInfoVo)) {
//            throw new ServiceException("标准入库任务不存在");
//        }
//
//        if (CommonYesOrNo.YES.equals(taskInfoVo.getIsQc())) {
//            StoragePositionDto storagePositionDto = new StoragePositionDto();
//            storagePositionDto.setLocationCode(taskInfoVo.getLocationCode());
//            storagePositionDto.setFactoryCode(taskInfoVo.getFactoryCode());
//            AjaxResult ajaxResult = iMainDataService.getInfoListLike(storagePositionDto);
//            if (ajaxResult.isError()) {
//                throw new ServiceException(ajaxResult.get("msg").toString());
//            }
//            List<StoragePositionVo> list = JSONObject.parseArray(ajaxResult.get("data").toString(), StoragePositionVo.class);
//            if (ObjectUtils.isEmpty(list)) {
//                throw new ServiceException(ErrorMessage.POSITION_CODE_NOTHING);
//            }
//            taskInfoVo.setAddress(list.get(0).getPositionNo());
//        }
//        return taskInfoVo;
//    }
//
//    /**
//     * 查询仓管任务列表
//     *
//     * @param taskInfo 仓管任务
//     * @return 仓管任务
//     */
//    @Override
//    public List<TaskInfoVo> selectTaskInfoList(TaskInfoVo taskInfo) {
//        return taskInfoMapper.selectTaskInfoList(taskInfo);
//    }
//
//    /**
//     * 新增仓管任务
//     *
//     * @param taskInfo 仓管任务
//     * @return 结果
//     */
//    @Override
//    public int insertTaskInfo(TaskInfo taskInfo) {
//        taskInfo.setCreateTime(DateUtils.getNowDate());
//        return taskInfoMapper.insertTaskInfo(taskInfo);
//    }
//
//    /**
//     * 修改仓管任务
//     *
//     * @param taskInfo 仓管任务
//     * @return 结果
//     */
//    @Override
//    public int updateTaskInfo(TaskInfo taskInfo) {
//        String taskStatus = taskInfo.getTaskStatus();
//        if (TaskStatusConstant.TASK_STATUS_CLOSE.equals(taskStatus)) {
//            throw new ServiceException("任务状态异常");
//        }
//        taskInfo.setTaskStatus(TaskStatusConstant.TASK_STATUS_CLOSE);
//        taskInfo.setUpdateBy(SecurityUtils.getUsername());
//        taskInfo.setUpdateTime(DateUtils.getNowDate());
//        return taskInfoMapper.updateTaskInfo(taskInfo);
//    }
//
//    /**
//     * 批量删除仓管任务
//     *
//     * @param ids 需要删除的仓管任务主键
//     * @return 结果
//     */
//    @Override
//    public int deleteTaskInfoByIds(Long[] ids) {
//        return taskInfoMapper.deleteTaskInfoByIds(ids);
//    }
//
//    /**
//     * 删除仓管任务信息
//     *
//     * @param id 仓管任务主键
//     * @return 结果
//     */
//    @Override
//    public int deleteTaskInfoById(Long id) {
//        return taskInfoMapper.deleteTaskInfoById(id);
//    }
//
//    /**
//     * 查询成品收货任务列表
//     *
//     * @param taskInfoDto
//     * @return TaskInfoVo集合
//     */
//    @Override
//    public List<TaskInfoVo> selectTaskInfoFinList(TaskInfoDto taskInfoDto) {
//        return taskInfoMapper.selectTaskInfoFinList(taskInfoDto);
//    }
//
//    /**
//     * 获取成品收货任务详细信息
//     */
//    @Override
//    public List<TaskInfoVo> selectTaskFinInfoById(Long id) {
//        List<TaskInfoVo> list = taskInfoMapper.selectTaskFinInfoById(id);
//        if (ObjectUtils.isEmpty(list)) {
//            throw new ServiceException("成品收货任务不存在");
//        }
//        return list;
//    }
//
//    /**
//     * 查询标准入库任务列表
//     */
//    @Override
//    public List<TaskInfoVo> selectTaskInfoListByStd(TaskInfoVo taskInfoVo) {
//        return taskInfoMapper.selectTaskInfoListByStd(taskInfoVo);
//    }
//
////    /**
////     * 查询标准入库任务入库明细
////     */
////    @Override
////    public TaskInfoVo getStdOrderTaskDetail(Long id) {
////        TaskInfoVo taskInfoVo = taskInfoMapper.getStdOrderTaskDetail(id);
////        if (ObjectUtils.isEmpty(taskInfoVo)) {
////            throw new ServiceException("标准入库任务不存在");
////        }
////
////        if (CommonYesOrNo.YES.equals(taskInfoVo.getIsQc())){
////            //LD需求 根据库存地点获取对应部门 如果部门id有修改 需同步修改
////            //部门id为 201124(LD制造一部)时返回5000库
////            //部门id为 201145(LD制造二部)时返回5100库
////            List<String>  deptIds = taskInfoMapper.getDeptStorageLocation(taskInfoVo.getLocationCode());
////
////            if (!ObjectUtils.isEmpty(deptIds)){
////                Long deptId = Long.valueOf(deptIds.get(0));
////                if (201124 == deptId){
////                    taskInfoVo.setLocationCode("5000");
////                } else if (201145 == deptId){
////                    taskInfoVo.setLocationCode("5100");
////                }
////                StoragePositionDto storagePositionDto = new StoragePositionDto();
////                storagePositionDto.setLocationCode(taskInfoVo.getLocationCode());
////                storagePositionDto.setFactoryCode(taskInfoVo.getFactoryCode());
////                AjaxResult ajaxResult = iMainDataService.getInfoListLike(storagePositionDto);
////                if (ajaxResult.isError()) {
////                    throw new ServiceException(ajaxResult.get("msg").toString());
////                }
////                List<StoragePositionVo> list = JSON.parseArray(ajaxResult.get("data").toString(), StoragePositionVo.class);
////                if (ObjectUtils.isEmpty(list)) {
////                    throw new ServiceException(ErrorMessage.POSITION_CODE_NOTHING);
////                }
////                taskInfoVo.setAddress(list.get(0).getPositionNo());
////            }
////        }
////        return taskInfoVo;
////    }
//
//    /**
//     * 标准入库任务提交
//     */
//    @Override
//    @GlobalTransactional
//    @Lock4j(keys = {"#taskInfoDto.detailId"},acquireTimeout = 10,expire = 10000 )
//    public int submitStdOrderTask(TaskInfoDto taskInfoDto) throws Exception {
//        if (ObjectUtils.isEmpty(taskInfoDto)) {
//            throw new ServiceException(ErrorMessage.PARAMS_NOTHING_STR);
//        }
//        StockInStdOrderDetailDto stockInStdOrderDetailDto = new StockInStdOrderDetailDto();
//        stockInStdOrderDetailDto.setId(taskInfoDto.getDetailId());
//        List<StockInStdOrderDetail> stockInStdOrderDetailList = stockInStdOrderDetailMapper.selectStockInStdOrderDetailList(stockInStdOrderDetailDto);
//        if (ObjectUtils.isEmpty(stockInStdOrderDetailList)) {
//            throw new ServiceException(ErrorMessage.STD_STOCK_IN_DETAIL_NOTHING);
//        }
//        StockInStdOrderDetail stockInStdOrderDetail = stockInStdOrderDetailList.get(0);
//        // 根据送货单明细下的采购单号和采购单行号
//        PurchaseOrderDetailDto purchaseOrderDetailDto = new PurchaseOrderDetailDto();
//
//        List<PurchaseOrderDetail> purchaseOrderDetails = new ArrayList<>();
//        for (int i = 0; i < stockInStdOrderDetailList.size(); i++) {
//            StockInStdOrderDetail detail = stockInStdOrderDetailList.get(i);
//
//            PurchaseOrderDetail orderDetail = new PurchaseOrderDetail();
//            orderDetail.setPurchaseOrderNo(detail.getPurchaseOrderNo());
//            orderDetail.setPurchaseLineNo(detail.getPurchaseLineNo());
//
//            purchaseOrderDetails.add(orderDetail);
//        }
//        purchaseOrderDetailDto.setPurchaseOrderDetailList(purchaseOrderDetails);
//
//        AjaxResult iVendorResult = iStockInService.getPurchaseOrderDetailByNoAndLine(purchaseOrderDetailDto);
//
//        if (iVendorResult.isError()){
//            throw new ServiceException("供应商服务调用失败！");
//        }
//
//        List<PurchaseOrderDetail> purchaseOrderDetailList = JSON.parseArray(iVendorResult.get("data").toString(), PurchaseOrderDetail.class);
//        // 校验采购单
//        if (ObjectUtils.isEmpty(purchaseOrderDetailList)) {
//            throw new ServiceException("采购单明细不存在！");
//        }
//        if ("X".equals(purchaseOrderDetailList.get(0).getExcFinFlag())){
//            throw new ServiceException(String.format("采购单号%s采购单行号%s当前交货状态为已完成，请检查", stockInStdOrderDetail.getPurchaseOrderNo(), stockInStdOrderDetail.getPurchaseLineNo()));
//        }
//
//        //送货数量
//        BigDecimal beDeliverQty = stockInStdOrderDetail.getDeliverQty();
//        //已完成的收货数量
//        BigDecimal beReceivedQty = stockInStdOrderDetail.getReceivedQty();
//        //已完成的收货数量（操作单位对应的数量）
//        BigDecimal beOperationReceivedQty = stockInStdOrderDetail.getOperationReceivedQty();
//        //剩余的送货数量
//        BigDecimal deliverQty = beDeliverQty.subtract(beReceivedQty);
//        //收货数量
//        BigDecimal receivedQty = taskInfoDto.getReceivedQty();
//
//        BigDecimal operationReceivedQty = taskInfoDto.getOperationReceivedQty();
//        //校验收货数量
//        if (receivedQty.compareTo(deliverQty) > 0) {
//            throw new ServiceException("收货数量大于待收货数量!");
//        }
//        //根据收货库位(仓位code) 库存地点code 获取工厂信息、库存信息、区域信息、仓位信息
//        StoragePositionDto storagePositionDto = new StoragePositionDto();
//        storagePositionDto.setLocationCode(taskInfoDto.getLocation());
//        storagePositionDto.setPositionNo(taskInfoDto.getAddress());
//        storagePositionDto.setFactoryCode(taskInfoDto.getFactoryCode());
//        AjaxResult ajaxResult = iMainDataService.getInfoListLike(storagePositionDto);
//        if (ajaxResult.isError()) {
//            throw new ServiceException(ajaxResult.get("msg").toString());
//        }
//        List<StoragePositionVo> list = JSON.parseArray(ajaxResult.get("data").toString(), StoragePositionVo.class);
//        if (ObjectUtils.isEmpty(list)) {
//            throw new ServiceException(ErrorMessage.POSITION_CODE_NOTHING);
//        }
//        if (list.size() > 1) {
//            throw new ServiceException(ErrorMessage.POSITION_CODE_ERROR);
//        }
//        StoragePositionVo storagePositionVo = list.get(0);
//
//        //修改标准入库单明细收货数量
//        BigDecimal add = beReceivedQty.add(receivedQty);
//        if (add.compareTo(beDeliverQty) > 0) {
//            throw new ServiceException("累计收货数量大于送货数量！");
//        }
//        stockInStdOrderDetail.setReceivedQty(add);
//        stockInStdOrderDetail.setOperationReceivedQty(beOperationReceivedQty.add(operationReceivedQty));
//        stockInStdOrderDetail.setUpdateBy(SecurityUtils.getUsername());
//        stockInStdOrderDetail.setUpdateTime(DateUtils.getNowDate());
//        if (stockInStdOrderDetailMapper.updateStockInStdOrderDetail(stockInStdOrderDetail) <= 0) {
//            throw new ServiceException("修改标准入库单明细失败！");
//        }
//        //标准入库单为激活状态时 修改入库单状态为 3部分完成
//        StockInStdOrder firstStockInStdOrder = new StockInStdOrder();
//        firstStockInStdOrder.setOrderNo(taskInfoDto.getStockInOrderNo());
//        StockInStdOrder stockInStdOrder = stockInStdOrderMapper.selectStockInStdOrder(firstStockInStdOrder);
//        if (OrderStatusConstant.ORDER_STATUS_ACTIVE.equals(stockInStdOrder.getOrderStatus())) {
//            stockInStdOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_PART_COMPLETE);
//            stockInStdOrder.setUpdateBy(SecurityUtils.getUsername());
//            stockInStdOrder.setUpdateTime(DateUtils.getNowDate());
//            if (stockInStdOrderMapper.updateStockInStdOrder(stockInStdOrder) <= 0) {
//                throw new ServiceException("修改标准入库单状态为部分完成失败！");
//            }
//        }
//
//        //更新台账 返回台账id
//        Long inventoryId = updateInventoryDetailsRaw(taskInfoDto, storagePositionVo);
//        // 是否质检，是的话需要创建送检单
//        if (CommonYesOrNo.YES.equals(taskInfoDto.getIsQc())){
//            Long tenantId = SecurityUtils.getLoginUser().getSysUser().getTenantId();
//
//            // 创建送检单单号
//            AjaxResult result = iCodeRuleService.getSeqWithTenantId(OrderNoTypeConstant.INSPECT, String.valueOf(tenantId));
//            if (ajaxResult.isError() || StringUtils.isEmpty(ajaxResult.get("data").toString())) {
//                throw new ServiceException("送检单单号生成失败！");
//            }
//            String orderNo = result.get("data").toString();
//            // 创建质检单
//            InspectOrder inspectOrder = new InspectOrder();
//            inspectOrder.setOrderNo(orderNo);
//            inspectOrder.setBillStatus(InspectOrderStatusConstant.ORDER_STATUS_NEW);
//            inspectOrder.setFactoryCode(taskInfoDto.getFactoryCode());
//            inspectOrder.setIsConsign(taskInfoDto.getIsConsign());
//            inspectOrder.setPurchaseOrderNo(taskInfoDto.getPurchaseOrderNo());
//            inspectOrder.setLocationCode(taskInfoDto.getLocation());
//            inspectOrder.setMaterialNo(taskInfoDto.getMaterialNo());
//            inspectOrder.setMaterialName(taskInfoDto.getMaterialName());
//            inspectOrder.setPurchaseLineNo(taskInfoDto.getPurchaseLineNo().toString());
//            inspectOrder.setReceiveDate(DateUtils.getNowDate());
//            inspectOrder.setReceiveOrderNo(taskInfoDto.getTaskNo());
//            inspectOrder.setReceiveOrderNo(taskInfoDto.getStockInOrderNo());
//            inspectOrder.setSupplierCode(taskInfoDto.getVendorCode());
//            inspectOrder.setSupplierName(taskInfoDto.getVendorName());
//            inspectOrder.setCreateBy(SecurityUtils.getUsername());
//            inspectOrder.setCreateTime(DateUtils.getNowDate());
//            inspectOrderMapper.insertInspectOrder(inspectOrder);
//
//            // 送检单详情
//            InspectOrderDetails inspectOrderDetails = new InspectOrderDetails();
//            inspectOrderDetails.setOrderNo(orderNo);
//            inspectOrderDetails.setMaterialNo(taskInfoDto.getMaterialNo());
//            inspectOrderDetails.setQcQty(receivedQty);
//            inspectOrderDetails.setLot(taskInfoDto.getLotNo());
//            inspectOrderDetails.setPrdLot(taskInfoDto.getLotNo());
//            inspectOrderDetails.setPositionNo(storagePositionVo.getPositionNo());
//            inspectOrderDetails.setInventoryId(inventoryId);
//            inspectOrderDetails.setLineNo(taskInfoDto.getPurchaseLineNo().toString());
//            inspectOrderDetails.setCreateBy(SecurityUtils.getUsername());
//            inspectOrderDetails.setCreateTime(DateUtils.getNowDate());
//            inspectOrderDetails.setOperationUnit(taskInfoDto.getOperationUnit());
//            inspectOrderDetails.setUnit(taskInfoDto.getUnit());
//            inspectOrderDetailsMapper.insertInspectOrderDetails(inspectOrderDetails);
//
//            GroupRecord gr = new GroupRecord();
//            gr.setName("WS_SEND_INSPECT_TASK");
//            gr.setFieldValue("TaskCode", inspectOrder.getOrderNo()); // 收货单任务号
//            gr.setFieldValue("ReceiveCode", taskInfoDto.getTaskNo()); // 收货单号
//            gr.setFieldValue("Company", taskInfoDto.getFactoryName()); //公司
//            gr.setFieldValue("Plant", taskInfoDto.getFactoryCode()); // 工厂（AC00，MA00）字典
//            gr.setFieldValue("PartNumber", inspectOrder.getMaterialNo()); // 物料编码
//            gr.setFieldValue("PartName", inspectOrder.getMaterialName()); // 物料名称
//            gr.setFieldValue("SupplyCode", inspectOrder.getSupplierCode()); // 供应商代码
//            gr.setFieldValue("SupplyName", inspectOrder.getSupplierName()); // 供应商名称
////            gr.setFieldValue("FurnaceNo", ""); // 炉号
//            gr.setFieldValue("ProductBatch", taskInfoDto.getProductionLot()); // 生产批次
//            gr.setFieldValue("InputBatch", taskInfoDto.getLotNo()); // 入库批次
//            gr.setFieldValue("InspectReason", taskInfoDto.getTaskType()); // 预留检验原因（进货）
//            gr.setFieldValue("TotalQuantity", stockInStdOrderDetail.getMinPacking()); // 来料数量
//            gr.setFieldValue("Warehouse", inspectOrder.getLocationCode()); // 库存地点（四位数字）
//            // todo 收货人待完善
//            gr.setFieldValue("ReceiveUser", ""); // 收货人
//            gr.setFieldValue("ReceiveTime",""); // 收货时间
//            gr.setFieldValue("Remark", inspectOrder.getRemark()); // 备注
//
//            // todo 发送检验任务
//            MsgObject msgObject = sendInspectTask(gr);
//            int qty = Convert.toInt(msgObject.getResValue("ExtractCount"));
//
//            inspectOrder.setQty(qty);
//            inspectOrderMapper.updateInspectOrder(inspectOrder);
//
//        }
//
//
//        // 非质检
//        if(CommonYesOrNo.NO.equals(taskInfoDto.getIsQc())){
//            // 根据参数配置是否自动生成上架任务
//            RemoteConfigEnum createShelfTask = RemoteConfigEnum.CREATE_SHELF_TASK_YCL;
//            String value = remoteConfigHelper.getConfig(createShelfTask.getKey());
//            if("".equals(Objects.toString(value,""))){
//                throw new ServiceException("获取参数配置失败！");
//            }
//            // 生成上架任务
//            if(createShelfTask.getValue().equals(value)){
//                Long tenantId = SecurityUtils.getLoginUser().getSysUser().getTenantId();
//
//                // 上架任务号
//                AjaxResult result = iCodeRuleService.getSeqWithTenantId(TaskNoTypeConstant.SHELF_TASK, String.valueOf(tenantId));
//                if (result.isError() || StringUtils.isEmpty(result.get("data").toString())){
//                    throw new ServiceException("上架任务号生成失败！");
//                }
//                String taskNo = result.get("data").toString();
//                BigDecimal multiplyQty = receivedQty.divide(taskInfoDto.getConversDefault(), 4, RoundingMode.HALF_UP);
//
//                ShelfTask shelfTask = new ShelfTask();
//                shelfTask.setTaskNo(taskNo);
//                shelfTask.setTaskType(taskInfoDto.getTaskType());
//                shelfTask.setStatus(TaskStatusConstant.TASK_STATUS_NEW);
//                shelfTask.setAllocateTime(DateUtils.getNowDate());
//                shelfTask.setMaterialNo(taskInfoDto.getMaterialNo());
//                shelfTask.setMaterialName(taskInfoDto.getMaterialName());
//                shelfTask.setOldMaterialNo(taskInfoDto.getOldMaterialNo());
//                shelfTask.setStockinOrderNo(taskInfoDto.getStockInOrderNo());
//                shelfTask.setStockinLineNo(taskInfoDto.getPurchaseLineNo().toString());
//                shelfTask.setSourcePositionNo(storagePositionVo.getPositionNo());
//                shelfTask.setSourceLocationCode(storagePositionVo.getLocationCode());
//                shelfTask.setSourceAreaCode(storagePositionVo.getAreaCode());
//                shelfTask.setQty(receivedQty);
//                shelfTask.setOperationQty(multiplyQty);
//                shelfTask.setUnit(taskInfoDto.getUnit());
//                shelfTask.setOperationUnit(taskInfoDto.getOperationUnit());
//                shelfTask.setCompleteQty(BigDecimal.ZERO);
//                shelfTask.setOperationCompleteQty(BigDecimal.ZERO);
//                shelfTask.setLot(taskInfoDto.getLotNo());
//                shelfTask.setInventoryId(inventoryId);
//                shelfTask.setCreateBy(SecurityUtils.getUsername());
//                shelfTask.setCreateTime(DateUtils.getNowDate());
//                shelfTaskService.insertShelfTask(shelfTask);
//            }
//        }
//        boolean flag = deliverQty.compareTo(receivedQty) > 0;
//        //更新任务状态
//        TaskInfo taskInfo = new TaskInfo();
//        taskInfo.setId(taskInfoDto.getId());
//        taskInfo.setTaskStatus(flag ? TaskStatusConstant.TASK_STATUS_PART_COMPLETE : TaskStatusConstant.TASK_STATUS_COMPLETE);
//        taskInfo.setUpdateBy(SecurityUtils.getUsername());
//        taskInfo.setUpdateTime(DateUtils.getNowDate());
//        int updateTaskInfo = taskInfoMapper.updateTaskInfo(taskInfo);
//        if (updateTaskInfo <= 0) {
//            throw new ServiceException("修改标准入库任务状态失败！");
//        }
//        //任务全部完成或已关闭时 修改标准入库单据状态为 4已完成
//        TaskInfoVo taskInfoVo = new TaskInfoVo();
//        taskInfoVo.setStockInOrderNo(taskInfoDto.getStockInOrderNo());
//        List<TaskInfoVo> taskInfos = taskInfoMapper.selectTaskInfoListByStd(taskInfoVo);
//        if (taskInfos.isEmpty()) {
//            StockInStdOrder firstStockInOrder = new StockInStdOrder();
//            firstStockInOrder.setId(taskInfoDto.getStockInOrderId());
//            firstStockInOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_COMPLETE);
//            firstStockInOrder.setUpdateBy(SecurityUtils.getUsername());
//            firstStockInOrder.setUpdateTime(DateUtils.getNowDate());
//            int resultOrder = stockInStdOrderMapper.updateStockInStdOrder(firstStockInOrder);
//            if (resultOrder <= 0) {
//                throw new ServiceException("修改标准入库单状态为已完成失败！");
//            }
//            //同步修改送货单为 4已完成
//            DeliveryOrderVo deliveryOrderVo = new DeliveryOrderVo();
//            deliveryOrderVo.setId(stockInStdOrder.getDeliveryOrderId());
//            deliveryOrderVo.setStatus(OrderStatusConstant.ORDER_STATUS_COMPLETE);
//            AjaxResult updateAjaxResult = iStockInService.updateDeliveryOrderStatus(deliveryOrderVo);
//            if (updateAjaxResult.isError()) {
//                throw new ServiceException("修改送货单失败！");
//            }
//        }
//        logger.info("准备同步数量了");
//        // 回写采购单wms收货数量、sap收货数量
//        AjaxResult vendorResult = updatePurchaseReceivedQty(stockInStdOrderDetail, receivedQty);
//        if (vendorResult.isError()) {
//            throw new ServiceException(Objects.toString(vendorResult.get("msg"),"供应商服务调用失败"));
//        }
//        //sap过账
//        String voucherNo = sendToSap(taskInfoDto);
//        // 添加任务记录
//        addStdTaskRecord(storagePositionVo, taskInfoDto, voucherNo);
//        return 1;
//    }
//
//    /**
//     * 发送质检任务
//     * @param groupRecord
//     */
//    private MsgObject sendInspectTask(GroupRecord groupRecord) {
//        try {
//            IMsgObject msgObject = new MsgObject(IMsgObject.MOType.initSR);
//
//            List<GroupRecord> list = new ArrayList();
//            list.add(groupRecord);
//
//            msgObject.setResGroupRecord(list);
//            byte[] bytes = remoteEsbSendService.sendToEsb(EsbSendSapConstant.WS_SEND_INSPECT_TASK, msgObject.getBytes());
//
//            MsgObject resMo = new MsgObject(bytes, IMsgObject.MOType.initSR);
//            return resMo;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//    public String sendToSap(TaskInfoDto taskInfoDto) throws Exception {
//        // 管控类型(非批次管控类型批次号均为空字符串)
//        MaterialAttrDto materialAttrDto = new MaterialAttrDto();
//        materialAttrDto.setMaterialNo(taskInfoDto.getMaterialNo());
//        materialAttrDto.setFactoryId(taskInfoDto.getFactoryId());
//        materialAttrDto.setFactoryCode(taskInfoDto.getFactoryCode());
//        List<MaterialAttrDto> materialList = esbSendCommonMapper.getMaterialAttr(materialAttrDto);
//        if (materialList == null || materialList.isEmpty()) {
//            throw new ServiceException("物料主数据未维护!");
//        }
//        String controlType = Objects.toString(materialList.get(0).getControlType(), "");
//        String lotNo = !controlType.equals(MainDataConstant.CONTROL_TYPE_LOT) ? "" : taskInfoDto.getLotNo();
//
//        taskInfoDto.setLotNo(lotNo);
//        taskInfoDto.setSourceLocationCode(taskInfoDto.getLocation());
//        SapPurchaseParamsDto sapPurchaseParamsDto = new SapPurchaseParamsDto();
//        BeanUtils.copyProperties(taskInfoDto, sapPurchaseParamsDto);
//
//        return sapInteractionService.IwmsPurchase(sapPurchaseParamsDto);
////        IMsgObject reqMo = new MsgObject(IMsgObject.MOType.initSR);
////        List<GroupRecord> list = new ArrayList<>();
////        GroupRecord groupRecord = new GroupRecord();
////        // 入参抬头
////        groupRecord.setName("IS_HEAD");
////        // 过账日期
////        groupRecord.setFieldValue("BUDAT", DateUtils.dateTime());
////        // 凭证日期
////        groupRecord.setFieldValue("BLDAT", DateUtils.dateTime());
////        // 凭证抬头
////        groupRecord.setFieldValue("BKTXT", "");
////        // 用户名
////        groupRecord.setFieldValue("UNAME", SecurityUtils.getUsername());
////        GroupRecord itemRecord = new GroupRecord();
////        // 入参行项目
////        itemRecord.setName("IT_ITEM");
////        // 物料号
////        itemRecord.setFieldValue("MATNR", taskInfoDto.getMaterialNo());
////        // 工厂代码
////        itemRecord.setFieldValue("PLANT", taskInfoDto.getFactoryCode());
////        // 收货方
////        itemRecord.setFieldValue("WEMPF", taskInfoDto.getFactoryCode());
////        // 收货/发货库存地点
////        itemRecord.setFieldValue("UMLGO", taskInfoDto.getLocation());
////        // 存储地点
////        itemRecord.setFieldValue("LGORT", taskInfoDto.getLocation());
////        // 批次编号
////        itemRecord.setFieldValue("BATCH", lotNo);
////        // 移动类型(库存管理) 固定101
////        itemRecord.setFieldValue("BWART", SapReqOrResConstant.RECEIVE_MOVE_TYPE);
////        // 数量
////        itemRecord.setFieldValue("ERFMG", taskInfoDto.getReceivedQty().toString());
////        // 单位
////        itemRecord.setFieldValue("ERFME", taskInfoDto.getUnit());
////        // 特殊库存标识(寄售自有)
////        itemRecord.setFieldValue("SOBKZ", SapReqOrResConstant.SPECIAL_MAP.get(taskInfoDto.getIsConsign()));
////        // 库存类型(是否质检)
////        itemRecord.setFieldValue("INSMK", SapReqOrResConstant.EXEMPTED_MAP.get(taskInfoDto.getIsQc()));
////        if (StockInTaskConstant.STD_ORDER_TASK_TYPE_DUMP.equals(taskInfoDto.getTaskType())) {
////            // 转储(交货单)
////            // 交货单号
////            itemRecord.setFieldValue("VBELN_VL", taskInfoDto.getPurchaseOrderNo());
////            // 交货单行号
////            itemRecord.setFieldValue("POSNR_VL", taskInfoDto.getPurchaseLineNo().toString());
////            // 移动标识
////            itemRecord.setFieldValue("KZBEW", SapReqOrResConstant.RECEIVE_MOVE_LOGO);
////        } else if(StockInTaskConstant.FIN_ORDER_TASK_TYPE_FINISHED.equals(taskInfoDto.getTaskType()) ||
////                StockInTaskConstant.FIN_ORDER_TASK_TYPE_SEMI_FINISHED.equals(taskInfoDto.getTaskType())){
////            // 成品收货(即生产订单收货)
////            groupRecord.setFieldValue("FLAG", SapReqOrResConstant.IS_PRODUCE);
////            itemRecord.setFieldValue("ORDERID",taskInfoDto.getPrdOrderNo());
////            // 移动标识
////            itemRecord.setFieldValue("KZBEW", SapReqOrResConstant.PRODUCE_MOVE_LOGO);
////        }else {
////            // 采购订单号
////            itemRecord.setFieldValue("PO_NUMBER", taskInfoDto.getPurchaseOrderNo());
////            // 采购凭证项目编号
////            itemRecord.setFieldValue("PO_ITEM", taskInfoDto.getPurchaseLineNo().toString());
////            // 移动标识
////            itemRecord.setFieldValue("KZBEW", SapReqOrResConstant.RECEIVE_MOVE_LOGO);
////        }
////        // 标识:未创建转移要求
////        itemRecord.setFieldValue("NO_TRANSFER_REQ", SapReqOrResConstant.NO_TRANSFER_REQ);
////        list.add(groupRecord);
////        list.add(itemRecord);
////        reqMo.setReqGroupRecord(list);
////        byte[] result = remoteEsbSendService.sendToEsb(EsbSendSapConstant.Z_IWMS_PURCHASE, reqMo.toString().getBytes());
////        MsgObject resMo = new MsgObject(result, IMsgObject.MOType.initSR);
////        String errorMsg = resMo.getResValue("ERRORMSG");
////        String flag = resMo.getResValue("FLAG");
////        if (!SapReqOrResConstant.SAP_RETURN_TYPE.equals(flag)) {
////            logger.error(errorMsg);
////            throw new ServiceException("sap标准收货过账失败!" + errorMsg);
////        }
////        // 凭证号
////        voucherNo = resMo.getResValue("MBLNR");
//    }
//
//    /**
//     * 物料凭证冲销
//     * @param voucherNo
//     */
//    public void materialReversal(String voucherNo){
//        AjaxResult result = iStockOutService.materialReversal(voucherNo);
//        if (result.isError()) {
//            throw new ServiceException(Objects.toString(result.get("msg").toString(),"物料冲销接口调用失败！"));
//        }
//    }
//
//    /**
//     * 更新采购单wms收货数量、sap收货数量
//     *
//     * @param stockInStdOrderDetail 标准入库单明细
//     * @param receivedQty           收货数量
//     */
//
//    public AjaxResult updatePurchaseReceivedQty(StockInStdOrderDetail stockInStdOrderDetail, BigDecimal receivedQty) {
//        // 调用供应商服务 根据采购单号、采购单行号 累加收货数量
//        PurchaseOrderDetailDto purchaseOrderDetailDto = new PurchaseOrderDetailDto();
//        purchaseOrderDetailDto.setPurchaseOrderNo(stockInStdOrderDetail.getPurchaseOrderNo());
//        purchaseOrderDetailDto.setPurchaseLineNo(stockInStdOrderDetail.getPurchaseLineNo());
//        purchaseOrderDetailDto.setReceivedQty(receivedQty);
//        purchaseOrderDetailDto.setDetailType(stockInStdOrderDetail.getDetailType());
//        return iStockInService.updatePurchaseReceivedQty(purchaseOrderDetailDto);
//    }
//
//    /**
//     * 更新台账 原材料
//     *
//     * @param taskInfoDto
//     * @param storagePositionVo
//     * @return inventoryId 台账id
//     */
//    public Long updateInventoryDetailsRaw(TaskInfoDto taskInfoDto, StoragePositionVo storagePositionVo) {
//        MaterialUnitDefDto materialUnitDefDto = retTaskService.queryMaterialUnitDef(taskInfoDto.getMaterialNo(), taskInfoDto.getFactoryCode());
//
//        // 基本单位
//        MaterialUnitDefDto basicUnitDto = retTaskService.queryMaterialUnitDef(taskInfoDto.getMaterialNo(), taskInfoDto.getFactoryCode());
//        //查询当前仓位是否已有台账存在
//        InventoryDetailsVo inventoryDetailsVo = new InventoryDetailsVo();
//        inventoryDetailsVo.setMaterialNo(taskInfoDto.getMaterialNo());
//        inventoryDetailsVo.setFactoryId(storagePositionVo.getFactoryId());
//        inventoryDetailsVo.setFactoryCode(storagePositionVo.getFactoryCode());
//        inventoryDetailsVo.setWarehouseId(storagePositionVo.getWarehouseId());
//        inventoryDetailsVo.setWarehouseCode(storagePositionVo.getWarehouseCode());
//        inventoryDetailsVo.setAreaId(storagePositionVo.getAreaId());
//        inventoryDetailsVo.setAreaCode(storagePositionVo.getAreaCode());
//        inventoryDetailsVo.setLocationId(storagePositionVo.getLocationId());
//        inventoryDetailsVo.setLocationCode(storagePositionVo.getLocationCode());
//        inventoryDetailsVo.setPositionId(storagePositionVo.getId());
//        inventoryDetailsVo.setPositionNo(storagePositionVo.getPositionNo());
//        inventoryDetailsVo.setStockInLot(taskInfoDto.getLotNo());
//        inventoryDetailsVo.setIsConsign(taskInfoDto.getIsConsign());
//        inventoryDetailsVo.setIsFreeze(CommonYesOrNo.NO);
//        inventoryDetailsVo.setIsQc(taskInfoDto.getIsQc());
//        inventoryDetailsVo.setProductionLot(taskInfoDto.getRequirementContent());
//        AjaxResult listResult = inStoreService.list(inventoryDetailsVo);
//        if (listResult.isError()) {
//            throw new ServiceException("查询库存台账失败！");
//        }
//        BigDecimal receivedQty = taskInfoDto.getReceivedQty();
//        List<InventoryDetails> inventoryDetailsList = JSON.parseArray(listResult.get("data").toString(), InventoryDetails.class);
//        Long inventoryId;
//        if (ObjectUtils.isEmpty(inventoryDetailsList)) {
//            //新增台账
//            InventoryDetails inventoryDetails = new InventoryDetails();
//            inventoryDetails.setMaterialNo(taskInfoDto.getMaterialNo());
//            inventoryDetails.setMaterialName(taskInfoDto.getMaterialName());
//            inventoryDetails.setOldMaterialNo(taskInfoDto.getOldMaterialNo());
//            inventoryDetails.setPositionNo(taskInfoDto.getAddress());
//            inventoryDetails.setPositionId(storagePositionVo.getId());
//            inventoryDetails.setAreaId(storagePositionVo.getAreaId());
//            inventoryDetails.setAreaCode(storagePositionVo.getAreaCode());
//            inventoryDetails.setLocationId(storagePositionVo.getLocationId());
//            inventoryDetails.setLocationCode(taskInfoDto.getLocation());
//            inventoryDetails.setFactoryId(storagePositionVo.getFactoryId());
//            inventoryDetails.setFactoryCode(storagePositionVo.getFactoryCode());
//            inventoryDetails.setFactoryName(storagePositionVo.getFactoryName());
//            inventoryDetails.setInventoryQty(receivedQty);
//            inventoryDetails.setAvailableQty(receivedQty);
//            inventoryDetails.setOperationInventoryQty(receivedQty.divide(materialUnitDefDto.getConversDefault(),4, RoundingMode.HALF_UP));
//            inventoryDetails.setOperationAvailableQty(receivedQty.divide(materialUnitDefDto.getConversDefault(),4, RoundingMode.HALF_UP));
//            inventoryDetails.setOperationUnit(materialUnitDefDto.getUnit());
//            inventoryDetails.setUnit(basicUnitDto.getUnit());
//            inventoryDetails.setConversDefault(materialUnitDefDto.getConversDefault());
//            inventoryDetails.setPreparedQty(BigDecimal.ZERO);
//            inventoryDetails.setOperationPreparedQty(BigDecimal.ZERO);
//            inventoryDetails.setSupplierId(taskInfoDto.getVendorId());
//            inventoryDetails.setSupplierCode(taskInfoDto.getVendorCode());
//            inventoryDetails.setSupplierName(taskInfoDto.getVendorName());
//            inventoryDetails.setStockInDate(DateUtils.getNowDate());
//            inventoryDetails.setStockInLot(taskInfoDto.getLotNo());
//            inventoryDetails.setProductionLot(taskInfoDto.getRequirementContent());
//            inventoryDetails.setIsQc(taskInfoDto.getIsQc());
//            inventoryDetails.setIsConsign(taskInfoDto.getIsConsign());
//            inventoryDetails.setIsFreeze(CommonYesOrNo.NO);
//            inventoryDetails.setWarehouseId(storagePositionVo.getWarehouseId());
//            inventoryDetails.setWarehouseCode(storagePositionVo.getWarehouseCode());
//            inventoryDetails.setCreateBy(SecurityUtils.getUsername());
//            inventoryDetails.setCreateTime(DateUtils.getNowDate());
//            AjaxResult result = inStoreService.add(inventoryDetails);
//            if (result.isError()) {
//                throw new ServiceException("新增库存台账失败！");
//            }
//            inventoryId = Long.parseLong(result.get("data").toString());
//        } else if (inventoryDetailsList.size() == 1) {
//            //修改台账
//            InventoryDetails inventoryDetails = inventoryDetailsList.get(0);
//            BigDecimal sumInventoryQty = inventoryDetails.getInventoryQty().add(taskInfoDto.getReceivedQty());
//            BigDecimal sumAvailableQty = inventoryDetails.getAvailableQty().add(taskInfoDto.getReceivedQty());
//            inventoryDetails.setInventoryQty(sumInventoryQty);
//            inventoryDetails.setAvailableQty(sumAvailableQty);
//            inventoryDetails.setOperationInventoryQty(sumInventoryQty.divide(materialUnitDefDto.getConversDefault(),4, RoundingMode.HALF_UP));
//            inventoryDetails.setOperationAvailableQty(sumAvailableQty.divide(materialUnitDefDto.getConversDefault(),4, RoundingMode.HALF_UP));
//            inventoryDetails.setUpdateBy(SecurityUtils.getUsername());
//            inventoryDetails.setIsQc(taskInfoDto.getIsQc());
//            inventoryDetails.setUpdateTime(DateUtils.getNowDate());
//            inventoryId = inventoryDetails.getId();
//            if (inStoreService.edit(inventoryDetails).isError()) {
//                throw new ServiceException("修改库存台账失败！");
//            }
//        } else {
//            throw new ServiceException("库存台账不唯一！");
//        }
//        return inventoryId;
//    }
//
//    /**
//     * 成品收货任务提交
//     */
//    @Override
//    @GlobalTransactional
//    @Lock4j(keys = {"#taskInfoDto.id"}, acquireTimeout = 10,expire = 10000)
//    public int submitFinOrderTask(TaskInfoDto taskInfoDto) throws Exception{
//        //参数校验
//        if (ObjectUtils.isEmpty(taskInfoDto)) {
//            throw new ServiceException(ErrorMessage.PARAMS_NOTHING_STR);
//        }
//        //根据收货库位(仓位code) 库存地点code 获取工厂信息、库存信息、区域信息、仓位信息
//        StoragePositionDto storagePositionDto = new StoragePositionDto();
//        storagePositionDto.setLocationCode(taskInfoDto.getLocation());
//        storagePositionDto.setPositionNo(taskInfoDto.getAddress());
//        storagePositionDto.setFactoryCode(SecurityUtils.getComCode());
//        AjaxResult ajaxResult = iMainDataService.getInfoListLike(storagePositionDto);
//        if (ajaxResult.isError()) {
//            throw new ServiceException(ajaxResult.get("msg").toString());
//        }
//        List<StoragePositionVo> list = JSON.parseArray(ajaxResult.get("data").toString(), StoragePositionVo.class);
//        if (ObjectUtils.isEmpty(list)) {
//            throw new ServiceException(ErrorMessage.POSITION_CODE_NOTHING);
//        }
//        if (list.size() > 1) {
//            throw new ServiceException("收货库位或仓位有误！");
//        }
//        StoragePositionVo storagePositionVo = list.get(0);
//        Long id = taskInfoDto.getId();
//        //查询任务
//        List<TaskInfoVo> taskList = taskInfoMapper.selectTaskFinInfoById(id);
//        TaskInfoVo taskInfoVo = taskList.get(0);
//        taskInfoDto.setMaterialNo(taskInfoVo.getMaterialNo());
//        taskInfoDto.setMaterialName(taskInfoVo.getMaterialName());
//        taskInfoDto.setOldMaterialNo(taskInfoVo.getOldMaterialNo());
//        //更新台账
//        SpringUtils.getBean(TaskInfoServiceImpl.class).updateInventoryDetailsProduct(taskInfoDto,taskInfoVo, storagePositionVo);
//        //更新成品收货单据、明细、任务
//        SpringUtils.getBean(TaskInfoServiceImpl.class).updateFin(taskInfoVo);
//        //LD成品收货sap过账
//        TaskInfoDto newTaskInfoDto = new TaskInfoDto();
//        newTaskInfoDto.setMaterialNo(taskInfoVo.getMaterialNo());
//        newTaskInfoDto.setFactoryCode(taskInfoVo.getFactoryCode());
//        newTaskInfoDto.setLotNo(taskInfoVo.getLotNo());
//        newTaskInfoDto.setLocation(taskInfoDto.getLocation());
//        newTaskInfoDto.setReceivedQty(taskInfoVo.getPlanRecivevQty());
//        newTaskInfoDto.setUnit(taskInfoVo.getUnit());
//        newTaskInfoDto.setIsConsign(CommonYesOrNo.NO);
//        newTaskInfoDto.setIsQc(CommonYesOrNo.NO);
//        newTaskInfoDto.setTaskType(taskInfoVo.getTaskType());
//        newTaskInfoDto.setPrdOrderNo(taskInfoVo.getPrdOrderNo());
//        String voucherNo = sendToSap(newTaskInfoDto);
//        //同步添加任务记录
//        addTaskRecord(taskInfoVo, storagePositionVo, taskInfoDto, voucherNo);
//        return 1;
//    }
//
//    /**
//     * 更新成品收货单据、明细、任务
//     * @param taskInfoVo 入库任务vo
//     */
//    public void updateFin(TaskInfoVo taskInfoVo) {
//        //完成任务
//        TaskInfo taskInfo = new TaskInfo();
//        taskInfo.setId(taskInfoVo.getId());
//        taskInfo.setTaskStatus(TaskStatusConstant.TASK_STATUS_COMPLETE);
//        taskInfo.setHandlerUserId(SecurityUtils.getUserId());
//        taskInfo.setHandlerUserName(SecurityUtils.getUsername());
//        taskInfo.setUpdateTime(DateUtils.getNowDate());
//        taskInfo.setUpdateBy(SecurityUtils.getUsername());
//        if (taskInfoMapper.updateTaskInfo(taskInfo) <= 0){
//            throw new ServiceException("更新成品收货任务失败！");
//        }
//        //完成成品收货单据明细
//        StockInFinOrderDetail stockInFinOrderDetail = new StockInFinOrderDetail();
//        stockInFinOrderDetail.setId(taskInfoVo.getDetailId());
//        stockInFinOrderDetail.setRecievedQty(taskInfoVo.getPlanRecivevQty());
//        stockInFinOrderDetail.setDetailStatus(OrderStatusConstant.ORDER_STATUS_COMPLETE);
//        stockInFinOrderDetail.setUpdateBy(SecurityUtils.getUsername());
//        stockInFinOrderDetail.setUpdateTime(DateUtils.getNowDate());
//        if (stockInFinOrderDetailMapper.updateStockInFinOrderDetail(stockInFinOrderDetail)<=0){
//            throw new ServiceException("更新成品收货单明细失败！");
//        }
//        // 根据单据号获取明细
//        StockInFinOrder firstStockInFinOrder = new StockInFinOrder();
//        firstStockInFinOrder.setOrderNo(taskInfoVo.getStockInOrderNo());
//        List<StockInFinOrderDto> orderList = stockInFinOrderMapper.selectStockInFinOrderList(firstStockInFinOrder);
//        StockInFinOrderDetailVo stockInFinOrderDetailVo = new StockInFinOrderDetailVo();
//        stockInFinOrderDetailVo.setFinOrderId(orderList.get(0).getId());
//        List<StockInFinOrderDetail> detailList = stockInFinOrderDetailMapper.selectStockInFinOrderDetailList(stockInFinOrderDetailVo);
//        int count = 0;
//        for (StockInFinOrderDetail d : detailList){
//            if(OrderStatusConstant.ORDER_STATUS_COMPLETE.equals(d.getDetailStatus())){
//                count ++;
//            }
//        }
//        if(detailList.size() == count){
//            // 完成
//            StockInFinOrder stockInFinOrder = new StockInFinOrder();
//            stockInFinOrder.setOrderNo(taskInfoVo.getStockInOrderNo());
//            stockInFinOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_COMPLETE);
//            stockInFinOrder.setUpdateBy(SecurityUtils.getUsername());
//            stockInFinOrder.setUpdateTime(DateUtils.getNowDate());
//            stockInFinOrderMapper.updateStockInFinOrderByAllComplete(stockInFinOrder);
//        }else{
//            // 部分完成
//            StockInFinOrder stockInFinOrder = new StockInFinOrder();
//            stockInFinOrder.setOrderNo(taskInfoVo.getStockInOrderNo());
//            stockInFinOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_PART_COMPLETE);
//            stockInFinOrder.setUpdateBy(SecurityUtils.getUsername());
//            stockInFinOrder.setUpdateTime(DateUtils.getNowDate());
//            stockInFinOrderMapper.updateStockInFinOrderByAllComplete(stockInFinOrder);
//        }
//    }
//
//    public String sapSend(TaskInfoDto taskInfoDto, TaskInfoVo taskInfoVo) {
//        // sap过账
//        Map<String,String> map = new HashMap<>();
//        map.put("factoryCode", taskInfoVo.getFactoryCode());
//        map.put("materialNo", taskInfoVo.getMaterialNo());
//        map.put("lotNo", taskInfoVo.getLotNo());
////        WmsMaterialWarehouseVo wmsMaterialWarehouseVo = new WmsMaterialWarehouseVo();
////        wmsMaterialWarehouseVo.setMaterialNo(taskInfoVo.getMaterialNo());
////        wmsMaterialWarehouseVo.setType(MainDataConstant.MATERIAL_RECEIVE);
////        AjaxResult materialResult = iMainDataService.getRecommend(wmsMaterialWarehouseVo);
////        if ("".equals(Objects.toString(materialResult.get("data"), "")) || materialResult.isError()) {
////            throw new ServiceException("获取推荐库存地点，区域，仓位信息失败！");
////        }
//        map.put("sourceLocation","9999");
//        map.put("factoryCode", SecurityUtils.getComCode());
//        map.put("targetLocation", taskInfoDto.getLocation());
//        map.put("qty", taskInfoVo.getPlanRecivevQty().toString());
//        // 获取物料默认单位
//        MaterialAttrDto materialAttrDto = new MaterialAttrDto();
//        materialAttrDto.setMaterialNo(taskInfoVo.getMaterialNo());
//        List<MaterialMainDto> materialList = esbSendCommonMapper.getMaterialMain(materialAttrDto);
//        if(CollectionUtils.isEmpty(materialList)){
//            throw new ServiceException(String.format("物料号：%s，获取默认单位为空！", taskInfoDto.getMaterialNo()));
//        }
//        map.put("unit",materialList.get(0).getDefaultUnit());
//        map.put("isConsign",CommonYesOrNo.NO);
//        AjaxResult res = inStoreService.moveLocationSap(map);
//        if ("".equals(Objects.toString(res.get("data"), "")) || res.isError()) {
//            throw new ServiceException(Objects.toString(res.get("msg").toString(), "调用库内作业服务失败"));
//        }
//        return JSON.parseObject(res.get("data").toString(), String.class);
//    }
//
//    /**
//     * 获取成品收货单详细信息
//     */
//    @Override
//    public TaskInfoVo selectFinDetailById(Long id) {
//        return taskInfoMapper.selectFinDetailById(id);
//    }
//
//    /**
//     * 查询成品收货单详情列表
//     */
//    @Override
//    public List<TaskInfoVo> selectFinDetailListList(TaskInfo taskInfo) {
//        return taskInfoMapper.selectFinDetailListList(taskInfo);
//    }
//
//    /**
//     * 获取标准收货记录
//     * @param stockInStdOrderDetail
//     * @return TaskInfoVo集合
//     */
//    @Override
//    public List<TaskInfoVo> getTaskInfoListByOrderNo(StockInStdOrderDetail stockInStdOrderDetail) {
//        List<TaskInfoVo> taskInfoListByOrderNo = taskInfoMapper.getTaskInfoListByOrderNo(stockInStdOrderDetail);
//        if (ObjectUtils.isEmpty(taskInfoListByOrderNo)){
//            return taskInfoListByOrderNo;
//        }
//
//        List<String> materialNoList = taskInfoListByOrderNo.stream().map(TaskInfo::getMaterialNo).collect(Collectors.toList());
//        MaterialUnitDefDto materialUnitDefDto = new MaterialUnitDefDto();
//        materialUnitDefDto.setMaterialNoList(materialNoList);
//        materialUnitDefDto.setStockoutEnable(CommonYesOrNo.YES);
//        materialUnitDefDto.setFactoryCode(SecurityUtils.getComCode());
//        AjaxResult unitResult = iMainDataService.batchMaterialUnitDef(materialUnitDefDto);
//        if (unitResult.isError()) {
//            throw new ServiceException(unitResult.get("msg").toString());
//        }
//        List<MaterialUnitDefDto> unitList = JSON.parseArray(unitResult.get("data").toString(), MaterialUnitDefDto.class);
//        if (ObjectUtils.isEmpty(unitList)) {
//            throw new ServiceException(String.format("不存在出库使用单位，请维护相关数据"));
//        }
//        Map<String, BigDecimal> map = unitList.stream()
//                .collect(Collectors.toMap(MaterialUnitDefDto::getMaterialNo, MaterialUnitDefDto::getConversDefault,(key1,key2)->key1));
//        Map<String, String> unitMap = unitList.stream()
//                .collect(Collectors.toMap(MaterialUnitDefDto::getMaterialNo, MaterialUnitDefDto::getUnit,(key1,key2)->key1));
//        taskInfoListByOrderNo.forEach(taskInfoVo -> {
//            taskInfoVo.setConversDefault(map.getOrDefault(taskInfoVo.getMaterialNo(),BigDecimal.ONE));
//            taskInfoVo.setUnit(unitMap.getOrDefault(taskInfoVo.getMaterialNo(),null));
//        });
//        return taskInfoListByOrderNo;
//    }
//
//    @Override
//    public TaskInfoDto mesToWmsContainer(String containerNo) {
//        // 数据库获取url
//        String url = "";
//        Map<String,Object> map = new HashMap<>();
//        map.put("containerCode",containerNo);
//        map.put("plantCode",SecurityUtils.getComCode());
//        // 从mom获取容器号信息
////        ResponseEntity<Map> result = restTemplate.postForEntity(url, map, Map.class);
////        Map<String,Object> resMap = result.getBody();
//        // todo 模拟返回数据
//        Map<String,Object> resMap = new HashMap<>();
//        List<Map<String,Object>> detailList = new ArrayList<>();
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("orderbatchNo","20230529001");
//        hashMap.put("batchQuantity","20");
//        detailList.add(hashMap);
//        HashMap<String, Object> hashMap1 = new HashMap<>();
//        hashMap1.put("orderbatchNo","20230529002");
//        hashMap1.put("batchQuantity","80");
//        detailList.add(hashMap1);
//        resMap.put("containerCode","11111");
//        resMap.put("productCode","1203013771");
//        resMap.put("productName","物料描述111");
//        resMap.put("totalQuantity","100");
//        resMap.put("warehouseLocation","5100");
//        resMap.put("plantCode","LD00");
//        resMap.put("batchDetail",detailList);
//
//
//        TaskInfoDto taskInfoDto = new TaskInfoDto();
//        taskInfoDto.setContainerNo(Objects.toString(resMap.get("containerCode"),""));
//        taskInfoDto.setMaterialNo(Objects.toString(resMap.get("productCode"),""));
//        taskInfoDto.setMaterialDesc(Objects.toString(resMap.get("productName"),""));
//        taskInfoDto.setTotalQty(BigDecimal.valueOf(Double.valueOf(Objects.toString(resMap.get("totalQuantity"),"0"))));
//        taskInfoDto.setSourceLocation(Objects.toString(resMap.get("warehouseLocation"),""));
//        taskInfoDto.setFactoryCode(Objects.toString(resMap.get("plantCode"),""));
//        taskInfoDto.setCreateBy(SecurityUtils.getUsername());
//        taskInfoDto.setCreateTime(DateUtils.getNowDate());
//        List<Map<String,Object>> batchDetail = (List<Map<String,Object>>)resMap.get("batchDetail");
//        List<LotDto> lotList = new ArrayList<>();
//        batchDetail.forEach(d->{
//            LotDto lotDto = new LotDto();
//            taskInfoDto.setMaterialNo(Objects.toString(resMap.get("productCode"),""));
//            lotDto.setLotNo(Objects.toString(d.get("orderbatchNo"),""));
//            lotDto.setLotNoNum(BigDecimal.valueOf(Double.parseDouble(Objects.toString(d.get("batchQuantity"),"0"))));
//            lotList.add(lotDto);
//        });
//        taskInfoDto.setLotNoNumList(lotList);
//
//        return taskInfoDto;
//    }
//
//    @Override
//    public int submitByMom(TaskInfoDto taskInfoDto) throws Exception {
//        // 新增成品收货,明细,创建成品收货任务
//        // 获取工厂信息
//        List<FactoryCommonDto> factoryList = esbSendCommonMapper.getFactoryByCode(taskInfoDto.getFactoryCode());
//        if(CollectionUtils.isEmpty(factoryList)){
//            throw new ServiceException(String.format("工厂代码：%s，不存在！",taskInfoDto.getFactoryCode()));
//        }
//        // 获取物料信息
//        MaterialAttrDto materialAttrDto = new MaterialAttrDto();
//        materialAttrDto.setFactoryCode(taskInfoDto.getFactoryCode());
//        materialAttrDto.setMaterialNo(taskInfoDto.getMaterialNo());
//        materialAttrDto.setFactoryId(factoryList.get(0).getId());
//        List<MaterialMainDto> materialList = esbSendCommonMapper.getMaterialMain(materialAttrDto);
//        if(CollectionUtils.isEmpty(materialList)){
//            throw new ServiceException(String.format("物料号：%s，不存在！",taskInfoDto.getMaterialNo()));
//        }
//        // 获取库存地点信息
//        LocationDto locationDto = new LocationDto();
//        locationDto.setLocationCode(taskInfoDto.getLocation());
//        List<LocationDto> locationList = esbSendCommonMapper.selectLocationByCode(locationDto);
//        if(CollectionUtils.isEmpty(locationList)) {
//            throw new ServiceException(String.format("库存地点：%s，不存在！",taskInfoDto.getLocation()));
//        }
//
//        List<LotDto> detailList = taskInfoDto.getLotNoNumList();
//        List<StockInFinOrderDetailDto> dList = new ArrayList();
//
//        StockInFinOrderDetailDto stockInFinOrderDetailDto;
//        for (LotDto l : detailList) {
//            stockInFinOrderDetailDto = new StockInFinOrderDetailDto();
//            stockInFinOrderDetailDto.setLot(l.getLotNo());
//            stockInFinOrderDetailDto.setPlanRecivevQty(l.getLotNoNum());
//            stockInFinOrderDetailDto.setUnit(materialList.get(0).getDefaultUnit());
//            dList.add(stockInFinOrderDetailDto);
//        }
//
//        StockInFinOrderDto stockInFinOrderDto = new StockInFinOrderDto();
//        stockInFinOrderDto.setMaterialNo(taskInfoDto.getMaterialNo());
//        stockInFinOrderDto.setContainerNo(taskInfoDto.getContainerNo());
//        stockInFinOrderDto.setFactoryCode(taskInfoDto.getFactoryCode());
//        stockInFinOrderDto.setFactoryName(factoryList.get(0).getFactoryName());
//        stockInFinOrderDto.setFactoryId(factoryList.get(0).getId());
//        stockInFinOrderDto.setSourceLocationCode(taskInfoDto.getSourceLocation());
//        stockInFinOrderDto.setMaterialName(materialList.get(0).getMaterialName());
//        stockInFinOrderDto.setMaterialId(materialList.get(0).getMaterialId());
//        stockInFinOrderDto.setLocationCode(taskInfoDto.getLocation());
//        stockInFinOrderDto.setLocationId(locationList.get(0).getId());
//        stockInFinOrderDto.setLocationName(locationList.get(0).getLocationDesc());
//        stockInFinOrderDto.setFlag(true);
//        stockInFinOrderDto.setStockInFinOrderDetailDtoList(dList);
//        stockInFinOrderService.insertStockInFinOrder(stockInFinOrderDto);
//        // 根据容器号获取成品收货信息
//        TaskInfoDto firstTaskInfo = new TaskInfoDto();
//        firstTaskInfo.setContainerNo(taskInfoDto.getContainerNo());
//        List<TaskInfoVo> taskList = taskInfoMapper.selectTaskInfoFinList(firstTaskInfo);
//        if(CollectionUtils.isEmpty(taskList)){
//            throw new ServiceException(String.format("容器号：%s，成品收货任务不存在！",taskInfoDto.getContainerNo()));
//        }
//        // 成品收货提交处理
//        firstTaskInfo = new TaskInfoDto();
//        firstTaskInfo.setId(taskList.get(0).getId());
//        firstTaskInfo.setAddress(taskInfoDto.getAddress());
//        firstTaskInfo.setLocation(taskInfoDto.getLocation());
//        firstTaskInfo.setReceivedQty(taskInfoDto.getTotalQty());
//        firstTaskInfo.setContainerNo(taskInfoDto.getContainerNo());
//        submitFinOrderTask(firstTaskInfo);
//        return 1;
//    }
//
//    /**
//     * 同步激活成品收货单、明细、任务(pda进入成品收货页面后触发)
//     * @param id 任务id
//     * @return 结果
//     */
//    @Override
//    @Lock4j(keys = {"#id"},acquireTimeout = 10,expire = 10000)
//    public int activeFin(Long id) {
//        if(id == null){
//            throw new ServiceException("参数不存在！");
//        }
//        //查询任务
//        TaskInfo taskInfo = taskInfoMapper.selectTaskInfoById(id);
//        //激活成品收货单据明细
//        StockInFinOrderDetail stockInFinOrderDetail = new StockInFinOrderDetail();
//        stockInFinOrderDetail.setId(taskInfo.getDetailId());
//        stockInFinOrderDetail.setDetailStatus(OrderStatusConstant.ORDER_STATUS_ACTIVE);
//        stockInFinOrderDetail.setUpdateBy(SecurityUtils.getUsername());
//        stockInFinOrderDetail.setUpdateTime(DateUtils.getNowDate());
//        if (stockInFinOrderDetailMapper.updateStockInFinOrderDetail(stockInFinOrderDetail)<=0){
//            throw new ServiceException("更新成品收货单明细失败！");
//        }
//
//        //激活成品收货单
//        StockInFinOrder stockInFinOrder = new StockInFinOrder();
//        stockInFinOrder.setOrderNo(taskInfo.getStockInOrderNo());
//        stockInFinOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_ACTIVE);
//        stockInFinOrder.setUpdateBy(SecurityUtils.getUsername());
//        stockInFinOrder.setUpdateTime(DateUtils.getNowDate());
//        if (stockInFinOrderMapper.updateStockInFinOrder(stockInFinOrder)<=0){
//            throw new ServiceException("更新成品收货单失败！");
//        }
//        return 1;
//    }
//
//    /**
//     * 查询半成品收货任务列表
//     * @param taskInfoDto 入库任务dto
//     * @return 入库任务vo集合
//     */
//    @Override
//    public List<TaskInfoVo> getSemiFinTaskList(TaskInfoDto taskInfoDto) {
//        return taskInfoMapper.getSemiFinTaskList(taskInfoDto);
//    }
//
//    /**
//     * 同步激活半成品收货单(pda进入半成品收货页面后触发)
//     * @param id 任务id
//     * @return 结果
//     */
//    @Override
//    @Lock4j(keys = {"#id"},acquireTimeout = 10,expire = 10000)
//    public int activeSemiFinTask(Long id) {
//        if(id == null){
//            throw new ServiceException("参数不存在！");
//        }
//        //查询任务
//        TaskInfo taskInfo = taskInfoMapper.selectTaskInfoById(id);
//        //激活半成品收货单
//        StockInSemiFinOrder stockInSemiFinOrder = new StockInSemiFinOrder();
//        stockInSemiFinOrder.setOrderNo(taskInfo.getStockInOrderNo());
//        stockInSemiFinOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_ACTIVE);
//        stockInSemiFinOrder.setUpdateBy(SecurityUtils.getUsername());
//        stockInSemiFinOrder.setUpdateTime(DateUtils.getNowDate());
//        if (stockInSemiFinOrderMapper.updateStockInSemiFinOrder(stockInSemiFinOrder)<=0){
//            throw new ServiceException("更新半成品收货单失败！");
//        }
//
//        //激活半成品收货单
//        StockInSemiFinOrderDetail stockInSemiFinOrderDetail = new StockInSemiFinOrderDetail();
//        stockInSemiFinOrderDetail.setId(taskInfo.getDetailId());
//        stockInSemiFinOrderDetail.setStatus(OrderStatusConstant.ORDER_STATUS_ACTIVE);
//        stockInSemiFinOrderDetail.setUpdateBy(SecurityUtils.getUsername());
//        stockInSemiFinOrderDetail.setUpdateTime(DateUtils.getNowDate());
//        if (stockInSemiFinOrderDetailMapper.updateStockInSemiFinOrderDetail(stockInSemiFinOrderDetail)<=0){
//            throw new ServiceException("更新成品收货单失败！");
//        }
//        return 1;
//    }
//
//    /**
//     * 获取半成品收货任务(pda)
//     * @param id 半成品收货任务id
//     * @return 入库任务vo
//     */
//    @Override
//    public TaskInfoVo getSemiFinTaskById(Long id) {
//        return taskInfoMapper.getSemiFinTaskById(id);
//    }
//
//    /**
//     * 半成品收货任务提交(pda)
//     * @param taskInfoDto 入库任务dto
//     * @return 结果
//     */
//    @Override
//    @GlobalTransactional
//    @Lock4j(keys = {"#taskInfoDto.id"},acquireTimeout = 10,expire = 10000)
//    public int submitSemiFinOrderTask(TaskInfoDto taskInfoDto) throws Exception {
//        //参数校验
//        if (ObjectUtils.isEmpty(taskInfoDto)) {
//            throw new ServiceException(ErrorMessage.PARAMS_NOTHING_STR);
//        }
//        //根据收货库位(仓位code) 库存地点code 获取工厂信息、库存信息、区域信息、仓位信息
//        StoragePositionDto storagePositionDto = new StoragePositionDto();
//        storagePositionDto.setLocationCode(taskInfoDto.getLocation());
//        storagePositionDto.setPositionNo(taskInfoDto.getAddress());
//        storagePositionDto.setFactoryCode(SecurityUtils.getComCode());
//        AjaxResult ajaxResult = iMainDataService.getInfoListLike(storagePositionDto);
//        if (ajaxResult.isError()) {
//            throw new ServiceException(ajaxResult.get("msg").toString());
//        }
//        List<StoragePositionVo> list = JSON.parseArray(ajaxResult.get("data").toString(), StoragePositionVo.class);
//        if (ObjectUtils.isEmpty(list)) {
//            throw new ServiceException(ErrorMessage.POSITION_CODE_NOTHING);
//        }
//        if (list.size() > 1) {
//            throw new ServiceException("收货库位或仓位有误！");
//        }
//        StoragePositionVo storagePositionVo = list.get(0);
//        Long id = taskInfoDto.getId();
//        //查询任务
//        TaskInfoVo taskInfoVo = taskInfoMapper.getSemiFinTaskById(id);
//        taskInfoDto.setMaterialNo(taskInfoVo.getMaterialNo());
//        taskInfoDto.setMaterialName(taskInfoVo.getMaterialName());
//        taskInfoDto.setOldMaterialNo(taskInfoVo.getOldMaterialNo());
//        //更新台账
//        SpringUtils.getBean(TaskInfoServiceImpl.class).updateInventoryDetailsProduct(taskInfoDto,taskInfoVo, storagePositionVo);
//
//        //更新半成品收货单据、明细、任务
//        SpringUtils.getBean(TaskInfoServiceImpl.class).updateSemiFin(taskInfoVo);
//        //LD半成品收货sap过账
//        TaskInfoDto firstTaskInfoDto = new TaskInfoDto();
//        firstTaskInfoDto.setMaterialNo(taskInfoVo.getMaterialNo());
//        firstTaskInfoDto.setFactoryCode(taskInfoVo.getFactoryCode());
//        firstTaskInfoDto.setLotNo(taskInfoVo.getLotNo());
//        firstTaskInfoDto.setLocation(taskInfoVo.getLocationCode());
//        firstTaskInfoDto.setReceivedQty(taskInfoVo.getPlanRecivevQty());
//        firstTaskInfoDto.setUnit(taskInfoVo.getUnit());
//        firstTaskInfoDto.setIsConsign(CommonYesOrNo.NO);
//        firstTaskInfoDto.setIsQc(CommonYesOrNo.NO);
//        firstTaskInfoDto.setTaskType(taskInfoVo.getTaskType());
//        firstTaskInfoDto.setPrdOrderNo(taskInfoVo.getPrdOrderNo());
//        String voucherNo = sendToSap(firstTaskInfoDto);
//        //同步添加任务记录
//        // 添加任务记录
//        addTaskRecord(taskInfoVo, storagePositionVo, taskInfoDto, voucherNo);
//        return 1;
//    }
//
//    public void updateSemiFin(TaskInfoVo taskInfoVo) {
//        //完成任务
//        TaskInfo taskInfo = new TaskInfo();
//        taskInfo.setId(taskInfoVo.getId());
//        taskInfo.setTaskStatus(TaskStatusConstant.TASK_STATUS_COMPLETE);
//        taskInfo.setHandlerUserId(SecurityUtils.getUserId());
//        taskInfo.setHandlerUserName(SecurityUtils.getUsername());
//        taskInfo.setUpdateTime(DateUtils.getNowDate());
//        taskInfo.setUpdateBy(SecurityUtils.getUsername());
//        if (taskInfoMapper.updateTaskInfo(taskInfo) <= 0){
//            throw new ServiceException("更新成品收货任务失败！");
//        }
//        //完成半成品收货单
//        StockInSemiFinOrder stockInSemiFinOrder = new StockInSemiFinOrder();
//        stockInSemiFinOrder.setOrderNo(taskInfoVo.getStockInOrderNo());
//        stockInSemiFinOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_COMPLETE);
//        stockInSemiFinOrder.setUpdateBy(SecurityUtils.getUsername());
//        stockInSemiFinOrder.setUpdateTime(DateUtils.getNowDate());
//        stockInFinOrderMapper.updateStockInSemiFinOrderByAllComplete(stockInSemiFinOrder);
//        //完成半成品收货单
//        StockInFinOrderDetail stockInFinOrderDetail = new StockInFinOrderDetail();
//        stockInFinOrderDetail.setId(taskInfoVo.getDetailId());
//        stockInFinOrderDetail.setDetailStatus(OrderStatusConstant.ORDER_STATUS_COMPLETE);
//        stockInFinOrderDetail.setUpdateBy(SecurityUtils.getUsername());
//        stockInFinOrderDetail.setUpdateTime(DateUtils.getNowDate());
//        stockInFinOrderDetailMapper.updateStockInFinOrderDetail(stockInFinOrderDetail);
//    }
//
//    /**
//     * 更新台账 成品/半成品
//     *
//     * @param taskInfoDto,
//     * @param storagePositionVo
//     * @param taskInfoVo
//     * @return inventoryId 台账id
//     */
//    public Long updateInventoryDetailsProduct(TaskInfoDto taskInfoDto,TaskInfoVo taskInfoVo, StoragePositionVo storagePositionVo) {
//        MaterialUnitDefDto materialUnitDefDto = retTaskService.queryMaterialUnitDef(taskInfoDto.getMaterialNo(), taskInfoDto.getFactoryCode());
//        MaterialAttrDto materialAttrDto = new MaterialAttrDto();
//        materialAttrDto.setMaterialNo(taskInfoDto.getMaterialNo());
//        List<MaterialMainDto> materialList = esbSendCommonMapper.getMaterialMain(materialAttrDto);
//        if(materialList.isEmpty()){
//            throw new ServiceException(String.format("物料%s不存在",taskInfoDto.getMaterialNo()));
//        }
//        if(materialList.size() > 1){
//            throw new ServiceException(String.format("物料%s存在多条",taskInfoDto.getMaterialNo()));
//        }
//
//        //新增台账
//        InventoryDetailsDto inventoryDetailsDto = new InventoryDetailsDto();
//        inventoryDetailsDto.setMaterialNo(taskInfoDto.getMaterialNo());
//        inventoryDetailsDto.setMaterialName(taskInfoDto.getMaterialName());
//        inventoryDetailsDto.setOldMaterialNo(taskInfoDto.getOldMaterialNo());
//        inventoryDetailsDto.setPositionNo(storagePositionVo.getPositionNo());
//        inventoryDetailsDto.setPositionId(storagePositionVo.getId());
//        inventoryDetailsDto.setAreaId(storagePositionVo.getAreaId());
//        inventoryDetailsDto.setAreaCode(storagePositionVo.getAreaCode());
//        inventoryDetailsDto.setLocationId(storagePositionVo.getLocationId());
//        inventoryDetailsDto.setLocationCode(storagePositionVo.getLocationCode());
//        inventoryDetailsDto.setFactoryId(storagePositionVo.getFactoryId());
//        inventoryDetailsDto.setFactoryCode(storagePositionVo.getFactoryCode());
//        inventoryDetailsDto.setFactoryName(storagePositionVo.getFactoryName());
//        inventoryDetailsDto.setInventoryQty(taskInfoVo.getPlanRecivevQty());
//        inventoryDetailsDto.setAvailableQty(taskInfoVo.getPlanRecivevQty());
//        inventoryDetailsDto.setUnit(materialList.get(0).getDefaultUnit());
//        inventoryDetailsDto.setOperationInventoryQty(taskInfoVo.getPlanRecivevQty()
//                .divide(materialUnitDefDto.getConversDefault(),4, RoundingMode.HALF_UP));
//        inventoryDetailsDto.setOperationAvailableQty(taskInfoVo.getPlanRecivevQty()
//                .divide(materialUnitDefDto.getConversDefault(),4, RoundingMode.HALF_UP));
//        inventoryDetailsDto.setOperationUnit(materialUnitDefDto.getUnit());
//        inventoryDetailsDto.setConversDefault(materialUnitDefDto.getConversDefault());
//        inventoryDetailsDto.setPreparedQty(BigDecimal.ZERO);
//        inventoryDetailsDto.setOperationPreparedQty(BigDecimal.ZERO);
//        inventoryDetailsDto.setStockInDate(DateUtils.getNowDate());
//        inventoryDetailsDto.setStockInLot(taskInfoVo.getLotNo());
//        inventoryDetailsDto.setContainerNo(taskInfoVo.getLotNo());
//        inventoryDetailsDto.setIsFreeze(CommonYesOrNo.NO);
//        inventoryDetailsDto.setIsConsign(CommonYesOrNo.NO);
//        inventoryDetailsDto.setIsQc(CommonYesOrNo.NO);
//        inventoryDetailsDto.setWarehouseId(storagePositionVo.getWarehouseId());
//        inventoryDetailsDto.setWarehouseCode(storagePositionVo.getWarehouseCode());
//        inventoryDetailsDto.setWarehouseCode(storagePositionVo.getWarehouseCode());
//        List<ContainerLedger> consignReturnOrdersList = new ArrayList<>();
//
//        ContainerLedger containerLedger = new ContainerLedger();
//        containerLedger.setContainerNo(taskInfoVo.getLotNo());
//        containerLedger.setContainerType(taskInfoVo.getUnit());
//        containerLedger.setMaterialNo(taskInfoVo.getMaterialNo());
//        containerLedger.setOldMaterialNo(taskInfoVo.getOldMaterialNo());
//        containerLedger.setMaterialName(taskInfoVo.getMaterialName());
//        containerLedger.setLot(taskInfoVo.getLotNo());
//        containerLedger.setLotQty(taskInfoVo.getPlanRecivevQty());
//        containerLedger.setOperationLotQty(taskInfoVo.getPlanRecivevQty().divide(materialUnitDefDto.getConversDefault(),4, RoundingMode.HALF_UP));
//        containerLedger.setFactoryCode(storagePositionVo.getFactoryCode());
//        containerLedger.setConversDefault(materialUnitDefDto.getConversDefault());
//        containerLedger.setOperationUnit(materialUnitDefDto.getUnit());
//        consignReturnOrdersList.add(containerLedger);
//        inventoryDetailsDto.setContainerLedgerList(consignReturnOrdersList);
//        AjaxResult add = inStoreService.addFin(inventoryDetailsDto);
//        if (add.isError()) {
//            throw new ServiceException(add.get("msg").toString());
//        }
//        return Long.parseLong(add.get("data").toString());
//    }
//
//    /**
//    * 新增任务记录
//    * @param taskInfoVo 仓管任务Vo
//    * @param storagePositionVo 仓位Vo
//    * @param taskInfoDto 仓管任务Dto
//    * @param voucherNo 凭证
//    * @date 2024/05/24
//    * @author fsc
//    */
//    private void addTaskRecord(TaskInfoVo taskInfoVo, StoragePositionVo storagePositionVo, TaskInfoDto taskInfoDto,
//                               String voucherNo){
//        //同步添加任务记录
//        TaskLog taskLog = new TaskLog();
//        taskLog.setTaskNo(taskInfoVo.getTaskNo());
//        taskLog.setTaskType(taskInfoVo.getTaskType());
//        taskLog.setMaterialNo(taskInfoVo.getMaterialNo());
//        taskLog.setOldMaterialNo(taskInfoVo.getOldMaterialNo());
//        taskLog.setLot(taskInfoVo.getLotNo());
//        taskLog.setQty(taskInfoDto.getReceivedQty());
//        taskLog.setUnit(taskInfoVo.getUnit());
//        taskLog.setFactoryCode(storagePositionVo.getFactoryCode());
//        taskLog.setTargetLocationCode(storagePositionVo.getLocationCode());
//        taskLog.setTargetAreaCode(storagePositionVo.getAreaCode());
//        taskLog.setTargetPositionCode(storagePositionVo.getPositionNo());
//        taskLog.setOrderNo(taskInfoVo.getStockInOrderNo());
//        taskLog.setOrderType(taskInfoVo.getTaskType());
//        taskLog.setSupplierName(taskInfoDto.getVendorName());
//        taskLog.setSupplierCode(taskInfoDto.getVendorCode());
//        taskLog.setIsFreeze(CommonYesOrNo.NO);
//        taskLog.setIsConsign(CommonYesOrNo.NO);
//        taskLog.setIsQc(CommonYesOrNo.NO);
//        taskLog.setMaterialCertificate(voucherNo);
//        if (taskService.add(taskLog).isError()) {
//            // 物料凭证冲销
//            if(!StringUtils.isBlank(voucherNo)){
//                materialReversal(voucherNo);
//            }
//            throw new ServiceException("新增任务记录失败！");
//        }
//    }
//
//
//    /**
//     * 新增任务记录
//     * @param storagePositionVo 仓位Vo
//     * @param taskInfoDto 仓管任务Dto
//     * @param voucherNo 凭证
//     * @date 2024/05/24
//     * @author fsc
//     */
//    private void addStdTaskRecord(StoragePositionVo storagePositionVo, TaskInfoDto taskInfoDto,
//                               String voucherNo){
//        //同步添加任务记录
//        TaskLog taskLog = new TaskLog();
//        taskLog.setTaskNo(taskInfoDto.getTaskNo());
//        taskLog.setTaskType(taskInfoDto.getTaskType());
//        taskLog.setMaterialNo(taskInfoDto.getMaterialNo());
//        taskLog.setOldMaterialNo(taskInfoDto.getOldMaterialNo());
//        taskLog.setOldMaterialNo(taskInfoDto.getMaterialName());
//        taskLog.setLot(taskInfoDto.getLotNo());
//        taskLog.setQty(taskInfoDto.getReceivedQty());
//        taskLog.setUnit(taskInfoDto.getUnit());
//        taskLog.setFactoryCode(storagePositionVo.getFactoryCode());
//        taskLog.setTargetLocationCode(storagePositionVo.getLocationCode());
//        taskLog.setTargetAreaCode(storagePositionVo.getAreaCode());
//        taskLog.setTargetPositionCode(storagePositionVo.getPositionNo());
//        taskLog.setOrderNo(taskInfoDto.getStockInOrderNo());
//        taskLog.setOrderType(taskInfoDto.getTaskType());
//        taskLog.setSupplierName(taskInfoDto.getVendorName());
//        taskLog.setSupplierCode(taskInfoDto.getVendorCode());
//        taskLog.setIsFreeze(CommonYesOrNo.NO);
//        taskLog.setIsConsign(CommonYesOrNo.NO);
//        taskLog.setIsQc(CommonYesOrNo.NO);
//        taskLog.setMaterialCertificate(voucherNo);
//        if (taskService.add(taskLog).isError()) {
//            // 物料凭证冲销
//            if(!StringUtils.isBlank(voucherNo)){
//                materialReversal(voucherNo);
//            }
//            throw new ServiceException("新增任务记录失败！");
//        }
//    }
//
//}
