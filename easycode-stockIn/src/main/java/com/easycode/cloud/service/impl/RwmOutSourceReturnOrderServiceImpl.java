package com.easycode.cloud.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.easycode.cloud.domain.vo.RwmOutSourceReturnOrderDetailVo;
import com.easycode.cloud.domain.vo.RwmOutSourceReturnOrderVo;
import com.easycode.cloud.service.IRetTaskService;
import com.easycode.cloud.service.IRwmOutSourceReturnOrderDetailService;
import com.easycode.cloud.service.IRwmOutSourceReturnOrderService;
import com.weifu.cloud.common.core.exception.ServiceException;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.datascope.annotation.DataScope;
import com.weifu.cloud.common.security.utils.SecurityUtils;
import com.weifu.cloud.constant.*;
import com.easycode.cloud.domain.RwmOutSourceReturnOrder;
import com.easycode.cloud.domain.RwmOutSourceReturnOrderDetail;
import com.easycode.cloud.domain.RetTask;
import com.weifu.cloud.domain.dto.FactoryCommonDto;
import com.weifu.cloud.domain.dto.MaterialAttrDto;
import com.weifu.cloud.domain.dto.MaterialMainDto;
import com.weifu.cloud.domain.dto.SapGetHuNoParamsDto;
import com.weifu.cloud.domain.vo.PrintInfoVo;
import com.weifu.cloud.domian.InventoryDetails;
import com.weifu.cloud.domian.ReserveStorage;
import com.weifu.cloud.domian.dto.MaterialUnitDefDto;
import com.weifu.cloud.domian.dto.PrintTOParamsDto;
import com.weifu.cloud.domian.dto.WmsMaterialAttrParamsDto;
import com.weifu.cloud.domian.dto.WmsMaterialBasicDto;
import com.weifu.cloud.domian.vo.StoragePositionVo;
import com.weifu.cloud.enums.RemoteConfigEnum;
import com.easycode.cloud.mapper.RwmOutSourceReturnOrderDetailMapper;
import com.easycode.cloud.mapper.RwmOutSourceReturnOrderMapper;
import com.weifu.cloud.mapper.EsbSendCommonMapper;
import com.easycode.cloud.mapper.RetTaskMapper;
import com.weifu.cloud.service.*;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.weifu.cloud.common.core.web.domain.AjaxResult.success;

/**
 * 原材料委外发料退货单Service业务层处理
 *
 * @author fsc
 * @date 2023-03-11
 */
@Service
public class RwmOutSourceReturnOrderServiceImpl implements IRwmOutSourceReturnOrderService
{
    @Autowired
    private RwmOutSourceReturnOrderMapper RwmOutSourceReturnOrderMapper;

    @Autowired
    private ICodeRuleService iCodeRuleService;

    @Autowired
    private RwmOutSourceReturnOrderDetailMapper RwmOutSourceReturnOrderDetailMapper;

    @Autowired
    private IRetTaskService retTaskService;

    @Autowired
    private IRwmOutSourceReturnOrderDetailService RwmOutSourceReturnOrderDetailService;

    @Autowired
    private RetTaskMapper retTaskMapper;


    @Autowired
    private IMainDataService iMainDataService;

    @Autowired
    private EsbSendCommonMapper esbSendCommonMapper;

    @Autowired
    private RemoteConfigHelper remoteConfigHelper;

    @Autowired
    private SapInteractionService sapInteractionService;

    @Autowired
    private IPrintService printService;

    @Autowired
    private IInStoreService inStoreService;

    @Autowired
    public RedisTemplate redisTemplate;

    @Autowired
    private IStockOutService iStockOutService;

    /**
     * 查询原材料委外发料退货单
     *
     * @param id 原材料委外发料退货单主键
     * @return 原材料委外发料退货单
     */
    @Override
    public RwmOutSourceReturnOrder selectRwmOutSourceReturnOrderById(Long id)
    {
        return RwmOutSourceReturnOrderMapper.selectRwmOutSourceReturnOrderById(id);
    }

    /**
     * 查询原材料委外发料退货单列表
     *
     * @param RwmOutSourceReturnOrderVo 原材料委外发料退货单
     * @return 原材料委外发料退货单
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<RwmOutSourceReturnOrder> selectRwmOutSourceReturnOrderList(RwmOutSourceReturnOrderVo RwmOutSourceReturnOrderVo)
    {
        return RwmOutSourceReturnOrderMapper.selectRwmOutSourceReturnOrderList(RwmOutSourceReturnOrderVo);
    }

    /**
     * 查询原材料委外发料退货单列表
     *
     * @param printInfo 原材料委外发料退货单
     * @return 原材料委外发料退货单
     */
    @Override
    public List<PrintInfoVo> getPrintInfoByIds(PrintInfoVo printInfo)
    {
        Long[] ids = printInfo.getIds();
        String type = com.weifu.cloud.common.core.utils.StringUtils.format("{}", printInfo.getParams().get("type"));
        List<PrintInfoVo> printInfoVos;
        if ("2".equals(type)) {
            printInfoVos = RwmOutSourceReturnOrderMapper.getPrintInfoByDetailIds(ids);
        } else {
            printInfoVos = RwmOutSourceReturnOrderMapper.getPrintInfoByIds(ids);
        }

        // 获取物料号
        List<String> materialNoList = printInfoVos.stream().map(PrintInfoVo::getMaterialNo).collect(Collectors.toList());
        // 根据物料查询对应物料类型
        WmsMaterialBasicDto materialBasicDto = new WmsMaterialBasicDto();
        materialBasicDto.setMaterialNoList(materialNoList);
        AjaxResult ajaxResult = iMainDataService.getMaterialArrInfo(materialBasicDto);
        if (ajaxResult.isError()) {
            throw new ServiceException(ajaxResult.get("msg").toString());
        }
        List<WmsMaterialAttrParamsDto> list = JSONObject.parseArray(ajaxResult.get("data").toString(), WmsMaterialAttrParamsDto.class);
        if (ObjectUtils.isEmpty(list)) {
            throw new ServiceException("物料属性表中未查询到物料相关信息！");
        }

        Map<String, String> materialInfoMap = list.stream().collect(Collectors.toMap(WmsMaterialAttrParamsDto::getMaterialNo, WmsMaterialAttrParamsDto::getType));

        for (PrintInfoVo printInfoVo : printInfoVos){
            String materialNo = printInfoVo.getMaterialNo();
            Integer deliverQty = printInfoVo.getDeliverQty().intValue();
            String materialType = materialInfoMap.get(materialNo);
            printInfoVo.setMaterialType(materialType);
            String qrCode = String.format("O%s%%D%s%%M%s%%Q%s%%B%s%%V%s%%L%s%%X1/1", "", "", materialNo,
                    deliverQty, "", "", "");
            printInfoVo.setQrCode(qrCode);
        }
        return printInfoVos;
    }

    /**
     * 新增原材料委外发料退货单
     *
     * @param RwmOutSourceReturnOrder 原材料委外发料退货单
     * @return 结果
     */
    @Override
    public int insertRwmOutSourceReturnOrder(RwmOutSourceReturnOrder RwmOutSourceReturnOrder)
    {
        RwmOutSourceReturnOrder.setCreateTime(DateUtils.getNowDate());
        SapGetHuNoParamsDto paramsDto = new SapGetHuNoParamsDto();
        try {
            String huNo = sapInteractionService.getHu(paramsDto);
            RwmOutSourceReturnOrder.setHuNo(huNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RwmOutSourceReturnOrderMapper.insertRwmOutSourceReturnOrder(RwmOutSourceReturnOrder);
    }

    /**
     * 关闭原材料委外发料退货单
     *
     * @param RwmOutSourceReturnOrder 原材料委外发料退货单
     * @return 结果
     */
    @Override
    public int closeRwmOutSourceOrder(RwmOutSourceReturnOrder RwmOutSourceReturnOrder)
    {
        RwmOutSourceReturnOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_CLOSE);
        RwmOutSourceReturnOrder.setUpdateBy(SecurityUtils.getUsername());
        RwmOutSourceReturnOrder.setCreateTime(DateUtils.getNowDate());

        RwmOutSourceReturnOrderMapper.updateRwmOutSourceReturnOrder(RwmOutSourceReturnOrder);

        RwmOutSourceReturnOrderDetail rwmReturnOrderDetail = new RwmOutSourceReturnOrderDetail();
        rwmReturnOrderDetail.setReturnOrderNo(RwmOutSourceReturnOrder.getOrderNo());
        List<RwmOutSourceReturnOrderDetail> detailList = RwmOutSourceReturnOrderDetailService.selectRwmOutSourceReturnOrderDetailList(rwmReturnOrderDetail);

        RetTask retTask = null;
        for(RwmOutSourceReturnOrderDetail detail : detailList){

            retTask = new RetTask();
            retTask.setDetailId(detail.getId());
            retTask.setTaskType(StockInTaskConstant.RWM_OUTSOURCE_RETURN_TYPE);
            retTask.setTaskStatus(TaskStatusConstant.TASK_STATUS_CLOSE);
            retTask.setUpdateBy(SecurityUtils.getUsername());
            retTask.setCreateTime(DateUtils.getNowDate());
            retTaskMapper.updateRetTaskByDetailId(retTask);
        }

        List<Long> detailIdList = detailList.stream().map(RwmOutSourceReturnOrderDetail::getId).collect(Collectors.toList());
        // 关闭退货任务
        retTaskMapper.updateStatusByDetailList(detailIdList);
        return 1;
    }

    /**
     * 新增原材料委外发料退货单及明细
     *
     * @param rwmOutSourceReturnOrderVo 原材料委外发料退货单
     * @return 结果
     */
    @Override
    @GlobalTransactional(timeoutMills = 15 * 10000 )
    public RwmOutSourceReturnOrder addRwmOutSourceReturn(RwmOutSourceReturnOrderVo rwmOutSourceReturnOrderVo)
    {
        // 明细数据
        if (ObjectUtils.isEmpty(rwmOutSourceReturnOrderVo) || CollectionUtils.isEmpty(rwmOutSourceReturnOrderVo.getDetailList())){
            throw new ServiceException("请输入必要参数和明细！！！！");
        }

        Long tenantId = SecurityUtils.getLoginUser().getSysUser().getTenantId();

        List<RwmOutSourceReturnOrderDetail> detailList = rwmOutSourceReturnOrderVo.getDetailList();

        RwmOutSourceReturnOrder RwmOutSourceReturnOrder = new RwmOutSourceReturnOrder();
        BeanUtils.copyProperties(rwmOutSourceReturnOrderVo, RwmOutSourceReturnOrder);

        // 生成退货单号
        AjaxResult ajaxResult = iCodeRuleService.getSeqWithTenantId(OrderNoTypeConstant.RWM_OUTSOURCE_ORDER_RETURN, String.valueOf(tenantId));
        if (ajaxResult.isError() || StringUtils.isEmpty(ajaxResult.get("data").toString())){
            throw new ServiceException("原材料委外发料退货单号生成失败！");
        }
        String returnOrderNo = ajaxResult.get("data").toString();
        RwmOutSourceReturnOrder.setOrderNo(returnOrderNo);
        RwmOutSourceReturnOrder.setInnerOrderNo(rwmOutSourceReturnOrderVo.getInnerOrderNo());
        RwmOutSourceReturnOrder.setCreateTime(DateUtils.getNowDate());
        RwmOutSourceReturnOrder.setCreateBy(SecurityUtils.getUsername());
        RwmOutSourceReturnOrder.setTenantId(tenantId);
        RwmOutSourceReturnOrder.setDeptId(SecurityUtils.getLoginUser().getSysUser().getDeptId());
        RwmOutSourceReturnOrder.setCustomerCode(rwmOutSourceReturnOrderVo.getSupplierCode());
        RwmOutSourceReturnOrder.setInnerOrderNo(rwmOutSourceReturnOrderVo.getOutsourcedStockoutOrderNo());
        RwmOutSourceReturnOrder.setRemark(rwmOutSourceReturnOrderVo.getRemark());
        RwmOutSourceReturnOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_ACTIVE);
        // 存入主表数据
        if (RwmOutSourceReturnOrderMapper.insertRwmOutSourceReturnOrder(RwmOutSourceReturnOrder) <= 0) {
            throw new ServiceException("新增原材料委外发料退货单失败！");
        }
        // 明细表数据
        int lineNo = 1;
        for (RwmOutSourceReturnOrderDetail detail : detailList){
            //校验退货批次 :  得到推荐库存地点
            Map<String, String> map = checkParams(detail, rwmOutSourceReturnOrderVo);

            //获得仓位
            StoragePositionVo storagePositionVo = JSON.parseObject(map.get("StoragePositionVo"), StoragePositionVo.class);
            //锁定仓位
            iMainDataService.updateStoragePositionStatus(new Long[]{storagePositionVo.getId()}, CommonYesOrNo.LOCK_POSITION);

            detail.setLineNo(String.valueOf(lineNo++));
            detail.setReturnOrderNo(returnOrderNo);
            detail.setFactoryCode(detail.getFactoryCode());
            detail.setCreateTime(DateUtils.getNowDate());
            detail.setCreateBy(SecurityUtils.getUsername());
            detail.setReturnQty(detail.getOperationQty());
            detail.setPositionId(storagePositionVo.getId());
            detail.setPositionNo(storagePositionVo.getPositionNo());
            detail.setOutsourcedStockoutOrderDetailId(detail.getDetailId());
            detail.setLocationCode(detail.getLocaltionCode());
            if (RwmOutSourceReturnOrderDetailMapper.insertRwmOutSourceReturnOrderDetail(detail) <= 0) {
                throw new ServiceException("新增原材料委外发料退货单失败！");
            }

            RetTask retTask = addRetTaskReturn(returnOrderNo, detail);

            //预定O库存
            reserveInventDetalis(detail, retTask);
            //更新出库明细表状态
            com.alibaba.fastjson2.JSONObject update = new com.alibaba.fastjson2.JSONObject();
            update.put("id", detail.getDetailId());
            update.put("status", "9");
            iStockOutService.outsourcedStockOutOrderDetailUpdate(update);
        }
        updateRwmOutSourceReturnOrderStatus(RwmOutSourceReturnOrder);

        return RwmOutSourceReturnOrder;
    }

    @GlobalTransactional
    private void reserveInventDetalis(RwmOutSourceReturnOrderDetail detail, RetTask retTask) {
        InventoryDetails inventoryDetails = new InventoryDetails();
        inventoryDetails.setStockSpecType("O");
        inventoryDetails.setStockSpecFlag("1");
        inventoryDetails.setSupplierCode(detail.getSupplierCode());
        inventoryDetails.setMaterialNo(detail.getMaterialNo());
        AjaxResult result = inStoreService.getInventory(inventoryDetails);
        if ("".equals(Objects.toString(result.get("data"), "")) || result.isError()){
            throw new ServiceException(String.format("预定台账失败 ； 在%s供应商下 ；未查询到对应台账信息" , detail.getSupplierCode() ));
        }

        List<InventoryDetails> inventoryList = JSON.parseArray(result.get("data").toString(), InventoryDetails.class);

        if (CollectionUtils.isEmpty(inventoryList)){
            throw new ServiceException(String.format("对应供应商%s台账不存在 ！！！！" , detail.getSupplierCode()));
        }
        BigDecimal sumInventoryAvailableQty = inventoryList.stream().map(InventoryDetails::getAvailableQty).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (detail.getReturnQty().compareTo(sumInventoryAvailableQty) >0 ){
            throw new ServiceException("退货数量不能大于总库存的可用量！！！");
        }
        for (  InventoryDetails  inventoryDetail : inventoryList) {
            if (retTask.getQty().compareTo(inventoryDetail.getAvailableQty()) <= 0) {
                //直接预定  todo redis 做分布式锁; 解决并发问题
//       String cacheKey = String.format("%s-%s-%s", TaskNoTypeConstant.RWM_OUTSOURCE_ORDER_RETURN_TASK, SecurityUtils.getUsername() , SecurityUtils.getUsername());
//       this.redisTemplate.opsForValue().set(cacheKey, retTask.getMaterialNo(), 5L, TimeUnit.MINUTES);
                addRserveStorage(retTask, inventoryDetail);
                break;
            } else {
                retTask.setQty(retTask.getQty().subtract(inventoryDetail.getAvailableQty()));
                addRserveStorage(retTask, inventoryDetail);
            }
        }
    }


    private void updateRwmOutSourceReturnOrderStatus(RwmOutSourceReturnOrder RwmOutSourceReturnOrder) {
        RwmOutSourceReturnOrder update= new RwmOutSourceReturnOrder();
        update.setId(RwmOutSourceReturnOrder.getId());
        update.setOrderStatus(OrderStatusConstant.ORDER_STATUS_ACTIVE);
        update.setUpdateTime(DateUtils.getNowDate());
        RwmOutSourceReturnOrderMapper.updateRwmOutSourceReturnOrder(update);
    }

    @GlobalTransactional
    private void  addRserveStorage(RetTask retTask, InventoryDetails inventoryDetail) {

        ReserveStorage reserveStorage = new ReserveStorage();
        BigDecimal qty = retTask.getQty().compareTo(inventoryDetail.getAvailableQty()) > 0 ? inventoryDetail.getAvailableQty() : retTask.getQty();
        reserveStorage.setReserveQty(qty);
        reserveStorage.setTaskId(retTask.getId());
        reserveStorage.setBillType(retTask.getTaskType());
        reserveStorage.setTaskNo(retTask.getTaskNo());
        reserveStorage.setUnit(inventoryDetail.getUnit());
        reserveStorage.setOperationUnit(inventoryDetail.getOperationUnit());
        reserveStorage.setInventoryId(inventoryDetail.getId());
        reserveStorage.setMaterialNo(inventoryDetail.getMaterialNo());
        reserveStorage.setMaterialName(inventoryDetail.getMaterialName());
        reserveStorage.setOldMaterialNo(inventoryDetail.getOldMaterialNo());
        reserveStorage.setLocationId(inventoryDetail.getLocationId());
        reserveStorage.setLocationCode(inventoryDetail.getLocationCode());
        reserveStorage.setAreaId(inventoryDetail.getAreaId());
        reserveStorage.setAreaCode(inventoryDetail.getAreaCode());
        reserveStorage.setWarehouseCode(inventoryDetail.getWarehouseCode());
        reserveStorage.setWarehouseId(inventoryDetail.getWarehouseId());
        reserveStorage.setFactoryId(inventoryDetail.getFactoryId());
        reserveStorage.setFactoryCode(inventoryDetail.getFactoryCode());
        reserveStorage.setLot(inventoryDetail.getStockInLot());
        reserveStorage.setContainer(inventoryDetail.getContainerNo());
        reserveStorage.setCreateBy(SecurityUtils.getUsername());
        reserveStorage.setCreateTime(DateUtils.getNowDate());
        AjaxResult createResult = inStoreService.createReserveStorage(reserveStorage);
        if (createResult.isError()){
//            String cacheKey = Optional.ofNullable(map.get("cacheKey")).orElse("").toString();
//            this.redisTemplate.delete(cacheKey);
            throw new ServiceException("新增预定库存台账失败！");
        }
    }

    private Map<String , String > checkParams(RwmOutSourceReturnOrderDetail detail , RwmOutSourceReturnOrderVo rwmOutSourceReturnOrderVo ) {
        HashMap<String, String> data = new HashMap<>();
        Long tenantId = SecurityUtils.getLoginUser().getSysUser().getTenantId();
//        String lotNo;
//        if ("".equals(Objects.toString(detail.getLot(), ""))){

//            AjaxResult lotNoResult = iCodeRuleService.getSeqWithTenantId(StockInTaskConstant.RETURN_LOT_NO, String.valueOf(tenantId));
//            if ("".equals(Objects.toString(lotNoResult.get("data"), "")) || lotNoResult.isError()) {
//                throw new ServiceException("退货批次号生成失败！");
//            }
////            lotNo = lotNoResult.get("data").toString();
//
//            if (StockInTaskConstant.MOVE_TYPE_RESEARCH.equals(rwmOutSourceReturnOrderVo.getMoveType())){
//                lotNo = lotNo.substring(0, lotNo.length() - 1);
//            }
//        } else {
//            if (detail.getLot().length() != 11){
//                throw new ServiceException("退货批次需输入11位！");
//            }
//            lotNo = detail.getLot();
//            //TODO 退货批次 后续需要可配置
//            // 移动类型为研发的话，不带T
//            if (!StockInTaskConstant.MOVE_TYPE_RESEARCH.equals(rwmOutSourceReturnOrderVo.getMoveType())){
//                lotNo = lotNo + StockInTaskConstant.RETURN_LOT_NO_SUFFIX;
//            }
//        }

        String materialNo = detail.getMaterialNo();
        String locationCode = detail.getLocaltionCode();
        if (!StringUtils.isEmpty(materialNo)){
            AjaxResult ajax = iMainDataService.getPositionCodeByLocationAndMaterialNo(locationCode , materialNo);

            if (ObjectUtils.isEmpty(ajax) || ajax.isError()){
                throw new ServiceException("请求仓位接口处理失败！！！");
            }

            StoragePositionVo vo = JSON.parseObject(JSON.toJSONString(ajax.get("data")), StoragePositionVo.class);
            if (ObjectUtils.isEmpty(vo)){
                throw new ServiceException("无仓位，请核实仓位！！！");
            }
            data.put("StoragePositionVo", com.alibaba.fastjson.JSON.toJSONString(vo));
        }
//        data.put("lotNo" , lotNo );
        return data;
    }

    /**
     * 新增退货入库任务
     * @param orderNo 单据号
     * @param detail 单据明细
     */
    public void addRetTask(String orderNo, RwmOutSourceReturnOrderDetail detail) {
        Long tenantId = SecurityUtils.getLoginUser().getSysUser().getTenantId();

        // 生成退货任务表
        AjaxResult result = iCodeRuleService.getSeqWithTenantId(TaskNoTypeConstant.RWM_OUTSOURCE_ORDER_RETURN_TASK, String.valueOf(tenantId));
        if("".equals(Objects.toString(result.get("data"),"")) || result.isError()){
            throw new ServiceException("原材料委外发料退料任务号生成失败！");
        }
        String taskNo = result.get("data").toString();
        // 生成退货任务
        RetTask retTask = new RetTask();
        retTask.setDetailId(detail.getId());
        retTask.setTaskNo(taskNo);
        retTask.setMaterialNo(detail.getMaterialNo());
        retTask.setMaterialName(detail.getMaterialName());
        retTask.setOldMatrialName(detail.getOldMaterialNo());
        retTask.setStockinOrderNo(orderNo);
        retTask.setLot(detail.getLot());
        retTask.setUnit(detail.getUnit());
        retTask.setQty(detail.getReturnQty());
        retTask.setCompleteQty(BigDecimal.ZERO);
        retTask.setOperationUnit(detail.getOperationUnit());
        retTask.setOperationCompleteQty(BigDecimal.ZERO);
        retTask.setOperationQty(detail.getOperationQty());
        retTask.setTaskStatus(TaskStatusConstant.TASK_STATUS_NEW);
        retTask.setTaskType(StockInTaskConstant.RWM_OUTSOURCE_RETURN_TYPE);
        retTask.setCreateBy(SecurityUtils.getUsername());
        retTask.setCreateTime(DateUtils.getNowDate());
        retTask.setPositionId(detail.getPositionId());
        retTask.setStorageLocationCode(detail.getLocaltionCode());
        retTask.setPositionNo(detail.getPositionNo());
        retTask.setPositionId(detail.getPositionId());
        retTaskService.insertRetTask(retTask);
        try {
            PrintTOParamsDto printTOParamsDto = new PrintTOParamsDto();
            BeanUtils.copyProperties(retTask , printTOParamsDto );
            printTOParamsDto.setVendorName(detail.getSupplierCode());
            if (TaskTypeConstant.OUT_SOURCE_RETURN_TASK.equals(retTask.getTaskType())){
                printTOParamsDto.setTaskType("委外发料退货任务");
                printTOParamsDto.setTargetSotrageType(detail.getFactoryCode());
                printTOParamsDto.setFactoryCode(detail.getLocaltionCode());
                printTOParamsDto.setFacotryOrLocation(retTask.getStorageLocationCode());
                printTOParamsDto.setMoveType(SapReqOrResConstant.OUT_SOURCED_MOVE_TYPE_RETURN);
                printTOParamsDto.setVendorLot(detail.getLot());
                printTOParamsDto.setMoveQty(detail.getReturnQty());

            }
            printService.printTO(printTOParamsDto);
        } catch (ServiceException e) {
            throw new ServiceException("打印服务处理异常！！！！");
        }
    }


    /**
     * 新增退货入库任务
     * @param orderNo 单据号
     * @param detail 单据明细
     */
    public RetTask addRetTaskReturn(String orderNo, RwmOutSourceReturnOrderDetail detail) {
        Long tenantId = SecurityUtils.getLoginUser().getSysUser().getTenantId();

        // 生成退货任务表
        AjaxResult result = iCodeRuleService.getSeqWithTenantId(TaskNoTypeConstant.RWM_OUTSOURCE_ORDER_RETURN_TASK, String.valueOf(tenantId));
        if("".equals(Objects.toString(result.get("data"),"")) || result.isError()){
            throw new ServiceException("原材料委外发料退料任务号生成失败！");
        }
        String taskNo = result.get("data").toString();
        // 生成退货任务
        RetTask retTask = new RetTask();
        retTask.setDetailId(detail.getId());
        retTask.setTaskNo(taskNo);
        retTask.setMaterialNo(detail.getMaterialNo());
        retTask.setMaterialName(detail.getMaterialName());
        retTask.setOldMatrialName(detail.getOldMaterialNo());
        retTask.setStockinOrderNo(orderNo);
        retTask.setLot(detail.getLot());
        retTask.setUnit(detail.getUnit());
        retTask.setQty(detail.getReturnQty());
        retTask.setCompleteQty(BigDecimal.ZERO);
        retTask.setOperationUnit(detail.getOperationUnit());
        retTask.setOperationCompleteQty(BigDecimal.ZERO);
        retTask.setOperationQty(detail.getOperationQty());
        retTask.setTaskStatus(TaskStatusConstant.TASK_STATUS_NEW);
        retTask.setTaskType(StockInTaskConstant.RWM_OUTSOURCE_RETURN_TYPE);
        retTask.setCreateBy(SecurityUtils.getUsername());
        retTask.setCreateTime(DateUtils.getNowDate());
        retTask.setPositionId(detail.getPositionId());
        retTask.setStorageLocationCode(detail.getLocaltionCode());
        retTask.setPositionNo(detail.getPositionNo());
        retTask.setPositionId(detail.getPositionId());
        retTaskService.insertRetTask(retTask);
        try {
            PrintTOParamsDto printTOParamsDto = new PrintTOParamsDto();
            BeanUtils.copyProperties(retTask , printTOParamsDto );
            printTOParamsDto.setVendorName(detail.getSupplierCode());
            if (TaskTypeConstant.OUT_SOURCE_RETURN_TASK.equals(retTask.getTaskType())){
                printTOParamsDto.setTaskType("委外发料退货任务");
                printTOParamsDto.setTargetSotrageType(detail.getFactoryCode());
                printTOParamsDto.setFactoryCode(detail.getLocaltionCode());
                printTOParamsDto.setFacotryOrLocation(retTask.getStorageLocationCode());
                printTOParamsDto.setMoveType(SapReqOrResConstant.OUT_SOURCED_MOVE_TYPE_RETURN);
                printTOParamsDto.setVendorLot(detail.getLot());
                printTOParamsDto.setMoveQty(detail.getReturnQty());

            }
//            printService.printTO(printTOParamsDto);
        } catch (ServiceException e) {
            throw new ServiceException("打印服务处理异常！！！！");
        }
        return retTask;
    }

    /**
     * 修改原材料委外发料退货单
     *
     * @param RwmOutSourceReturnOrder 原材料委外发料退货单
     * @return 结果
     */
    @Override
    public int updateRwmOutSourceReturnOrder(RwmOutSourceReturnOrder RwmOutSourceReturnOrder)
    {
        RwmOutSourceReturnOrder.setUpdateTime(DateUtils.getNowDate());
        return RwmOutSourceReturnOrderMapper.updateRwmOutSourceReturnOrder(RwmOutSourceReturnOrder);
    }

    /**
     * 批量删除原材料委外发料退货单
     *
     * @param ids 需要删除的原材料委外发料退货单主键
     * @return 结果
     */
    @Override
    public int deleteRwmOutSourceReturnOrderByIds(Long[] ids)
    {
        return RwmOutSourceReturnOrderMapper.deleteRwmOutSourceReturnOrderByIds(ids);
    }

    /**
     * 删除原材料委外发料退货单信息
     *
     * @param id 原材料委外发料退货单主键
     * @return 结果
     */
    @Override
    public int deleteRwmOutSourceReturnOrderById(Long id)
    {
        return RwmOutSourceReturnOrderMapper.deleteRwmOutSourceReturnOrderById(id);
    }

    /**
     * 批量激活原材料委外发料退货单
     *
     * @param ids 原材料委外发料退货单主键集合
     * @return 结果
     */
    @Override
    public int activeRwmOutSourceOrderReturnByIds(Long[] ids) {
        //校验参数
        if (ObjectUtils.isEmpty(ids)) {
            throw new ServiceException("参数不存在！");
        }
        for (Long id : ids) {
            //查询单据
            RwmOutSourceReturnOrder RwmOutSourceReturnOrder = RwmOutSourceReturnOrderMapper.selectRwmOutSourceReturnOrderById(id);
            String status = RwmOutSourceReturnOrder.getOrderStatus();
            String orderNo = RwmOutSourceReturnOrder.getOrderNo();
            if (!OrderStatusConstant.ORDER_STATUS_NEW.equals(status)) {
                throw new ServiceException("原材料委外发料退货单:"+ orderNo+"必须为新建状态才能激活！");
            }
            //更新状态
            RwmOutSourceReturnOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_ACTIVE);
            RwmOutSourceReturnOrder.setUpdateBy(SecurityUtils.getUsername());
            RwmOutSourceReturnOrder.setUpdateTime(DateUtils.getNowDate());
            if (RwmOutSourceReturnOrderMapper.updateRwmOutSourceReturnOrder(RwmOutSourceReturnOrder) <= 0) {
                throw new ServiceException("更新原材料委外发料退货单失败！");
            }
            //查询明细
            RwmOutSourceReturnOrderDetail rwmReturnOrderDetail = new RwmOutSourceReturnOrderDetail();
            rwmReturnOrderDetail.setReturnOrderNo(orderNo);
            List<RwmOutSourceReturnOrderDetail> detailList = RwmOutSourceReturnOrderDetailMapper.selectRwmOutSourceReturnOrderDetailList(rwmReturnOrderDetail);
            //新增退货入库任务
            for (RwmOutSourceReturnOrderDetail detail : detailList) {
                addRetTask(orderNo, detail);
            }
        }
        return 1;
    }

    @Override
    public AjaxResult importData(List<RwmOutSourceReturnOrderDetailVo> stockList) {
        StringBuffer errorMsg = new StringBuffer();
        List<RwmOutSourceReturnOrderDetailVo> resultList = new ArrayList<>();
        List<String> materialList = stockList.stream().map(RwmOutSourceReturnOrderDetailVo::getMaterialNo).distinct().collect(Collectors.toList());

        MaterialUnitDefDto materialUnitDefDto = new MaterialUnitDefDto();
        materialUnitDefDto.setMaterialNoList(materialList);
        materialUnitDefDto.setStockinEnable(CommonYesOrNo.YES);
        materialUnitDefDto.setFactoryCode(SecurityUtils.getComCode());
        AjaxResult unitResult = iMainDataService.batchMaterialUnitDef(materialUnitDefDto);
        if (unitResult.isError()) {
            throw new ServiceException(unitResult.get("msg").toString());
        }
        List<MaterialUnitDefDto> unitList = JSONObject.parseArray(unitResult.get("data").toString(), MaterialUnitDefDto.class);
        if (ObjectUtils.isEmpty(unitList)) {
            throw new ServiceException(String.format("当前导入全部物料多级单位未维护！"));
        }
        Map<String, BigDecimal> map = unitList.stream().collect(Collectors.toMap(MaterialUnitDefDto::getMaterialNo, MaterialUnitDefDto::getConversDefault));
        Map<String, String> unitMap = unitList.stream().collect(Collectors.toMap(MaterialUnitDefDto::getMaterialNo, MaterialUnitDefDto::getUnit));



//        if(materialList.size() != stockList.size()){
//            throw new ServiceException("导入物料重复,导入失败!");
//        }
        List<RwmOutSourceReturnOrderDetailVo> list = stockList.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getMaterialNo() + "-" + o.getLot()))),
                        ArrayList::new
                ));
        if(list.size() != stockList.size()){
            throw new ServiceException("导入物料批次重复,导入失败!");
        }
        if(stockList != null && stockList.size() > 0){
            for (RwmOutSourceReturnOrderDetailVo detail : stockList) {
                if (detail.getMaterialNo() == null || "".equals(detail.getMaterialNo()) || detail.getOperationQty() == null) {
                    throw new ServiceException("物料号，数量不能为空！");
                }
                // 调用主数据模块
                AjaxResult result = iMainDataService.getMatrialNoCount(detail.getMaterialNo());
                // 获取物料描述,旧物料号信息
                MaterialAttrDto materialAttrDto = new MaterialAttrDto();
                materialAttrDto.setMaterialNo(detail.getMaterialNo());
                List<MaterialMainDto> materialMainList = esbSendCommonMapper.getMaterialMain(materialAttrDto);
                if(materialMainList != null && materialMainList.size() > 0){
                    detail.setMaterialName(materialMainList.get(0).getMaterialName());
                    detail.setOldMaterialNo(materialMainList.get(0).getOldMaterialNo());
                    detail.setUnit(materialMainList.get(0).getDefaultUnit());
                    detail.setFactoryCode(materialMainList.get(0).getFactoryCode());
                    detail.setType(materialMainList.get(0).getType());
                }
                if (result.isError()) {
                    throw new ServiceException("调用主数据模块失败！");
                }

                BigDecimal conversDefault = map.get(detail.getMaterialNo());
                String operationUnit = unitMap.get(detail.getMaterialNo());

                detail.setOperationUnit(operationUnit);
//                detail.setConverterDefault(conversDefault);
                BigDecimal requestQty = detail.getOperationQty().multiply(conversDefault);
                detail.setReturnQty(requestQty);

                int num =  (int) result.get("data");
                if (num == 0) {
                    errorMsg.append(detail.getMaterialNo() + "，");
                }else {
                    resultList.add(detail);
                }
            }
        }else{
            throw new ServiceException("导入数据为空!");
        }
        if(!"".equals(errorMsg.toString())){
            errorMsg.append("物料主数据未维护！");
        }
        return success(errorMsg.toString(),resultList);
    }
}
