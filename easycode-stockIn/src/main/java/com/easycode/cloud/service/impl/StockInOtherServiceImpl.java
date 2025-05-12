package com.easycode.cloud.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.easycode.cloud.domain.dto.RetTaskDto;
import com.easycode.cloud.domain.dto.StockInOtherDto;
import com.easycode.cloud.domain.dto.StockInOtherParamsDto;
import com.easycode.cloud.domain.dto.StockinOtherDetailDto;
import com.easycode.cloud.service.IStockInOtherService;
import com.soa.eis.adapter.framework.message.IMsgObject;
import com.soa.eis.adapter.framework.message.impl.GroupRecord;
import com.soa.eis.adapter.framework.message.impl.MsgObject;
import com.weifu.cloud.common.core.exception.ServiceException;
import com.weifu.cloud.common.core.text.Convert;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.weifu.cloud.common.core.utils.StringUtils;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.datascope.annotation.DataScope;
import com.weifu.cloud.common.security.utils.SecurityUtils;
import com.weifu.cloud.constant.*;
import com.easycode.cloud.domain.RetTask;
import com.easycode.cloud.domain.StockInOther;
import com.easycode.cloud.domain.StockInOtherDetail;
import com.easycode.cloud.domain.TaskInfo;
import com.weifu.cloud.domian.InventoryDetails;
import com.weifu.cloud.domian.StockOutTask;
import com.weifu.cloud.domian.WmsPrinterLocation;
import com.weifu.cloud.domian.dto.HiprintTemplateDTO;
import com.weifu.cloud.domian.dto.MaterialUnitDefDto;
import com.weifu.cloud.domian.dto.WmsMaterialAttrParamsDto;
import com.weifu.cloud.domian.dto.WmsMaterialBasicDto;
import com.weifu.cloud.domian.vo.StoragePositionVo;
import com.weifu.cloud.enums.RemoteConfigEnum;
import com.easycode.cloud.mapper.RetTaskMapper;
import com.easycode.cloud.mapper.StockInOtherDetailMapper;
import com.easycode.cloud.mapper.StockInOtherMapper;
import com.easycode.cloud.mapper.TaskInfoMapper;
import com.weifu.cloud.service.*;
import com.easycode.cloud.strategy.StockInCommonService;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * 其它入库单Service业务层处理
 *
 * @author bcp
 * @date 2023-03-24
 */
@Service
@Transactional(rollbackFor = ServiceException.class)
public class StockInOtherServiceImpl implements IStockInOtherService
{
    @Autowired
    private IInStoreService iInStoreService;
    @Autowired
    private RemoteEsbSendService remoteEsbSendService;
    @Autowired
    private TaskInfoMapper taskInfoMapper;
    @Autowired
    private IMainDataService mainDataService;

    @Autowired
    private StockInOtherMapper stockInOtherMapper;

    @Autowired
    private StockInOtherDetailMapper stockInOtherDetailMapper;

    @Autowired
    private ICodeRuleService iCodeRuleService;

    @Autowired
    private RemoteConfigHelper remoteConfigHelper;

    @Autowired
    private RetTaskMapper retTaskMapper;

    @Autowired
    private StockInCommonService stockInCommonService;

    @Autowired
    private IPrintService printService;

    /**
     * 查询其它入库单
     *
     * @param id 其它入库单主键
     * @return 其它入库单
     */
    @Override
    public StockInOther selectStockInOtherById(Long id)
    {
        return stockInOtherMapper.selectStockInOtherById(id);
    }

    /**
     * 查询其它入库单列表
     *
     * @param stockInOtherDto 其它入库单
     * @return 其它入库单
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<StockInOther> selectStockInOtherList(StockInOtherDto stockInOtherDto) {
        return stockInOtherMapper.selectStockInOtherList(stockInOtherDto);
    }

    /**
     * 新增其它入库单
     *
     * @param stockInOtherDto 其它入库单
     * @return 结果
     */
    @Override
    @GlobalTransactional
    public StockInOther insertStockInOther(StockInOtherDto stockInOtherDto) {
        if (ObjectUtils.isEmpty(stockInOtherDto)){
            throw new ServiceException("参数不存在！");
        }
        List<StockInOtherDetail> detailList = stockInOtherDto.getDetailList();

        //TODO 校验供应商 货源信息是否存在
//        List<String> supplierCodeList = detailList.stream().map(StockInOtherDetail::getSupplierCode).collect(Collectors.toList());
//        String[] supplierCodeList = detailList.stream().map(StockInOtherDetail::getSupplierCode).toArray(String[]::new);

        //根据参数配置 决定是否走自动激活单据
        RemoteConfigEnum createStockInTask = RemoteConfigEnum.CREATE_STOCK_IN_TASK;
        String isCreateStockInTask = remoteConfigHelper.getConfig(createStockInTask.getKey());
        if("".equals(Objects.toString(isCreateStockInTask,""))){
            throw new ServiceException("获取参数配置失败！");
        }
        boolean flag = createStockInTask.getValue().equals(isCreateStockInTask);
        Long tenantId = SecurityUtils.getLoginUser().getSysUser().getTenantId();
        //新增单据
        AjaxResult seqWithCodeRule = iCodeRuleService.getSeqWithTenantId(OrderNoTypeConstant.OTHER, String.valueOf(tenantId));
        if("".equals(Objects.toString(seqWithCodeRule.get("data"),"")) || seqWithCodeRule.isError()){
            throw new ServiceException("其它入库单据号生成失败！");
        }
        String orderNo = seqWithCodeRule.get("data").toString();
        StockInOther stockInOther = new StockInOther();
        BeanUtils.copyProperties(stockInOtherDto,stockInOther);
        stockInOther.setOrderNo(orderNo);
        stockInOther.setFactoryCode(SecurityUtils.getComCode());
        stockInOther.setTenantId(null);
        stockInOther.setDeptId(SecurityUtils.getLoginUser().getSysUser().getDeptId());
        stockInOther.setCreateTime(DateUtils.getNowDate());
        stockInOther.setCreateBy(SecurityUtils.getUsername());
        stockInOther.setBillStatus(flag ? OrderStatusConstant.ORDER_STATUS_ACTIVE : OrderStatusConstant.ORDER_STATUS_NEW);
        if (stockInOtherMapper.insertStockInOther(stockInOther) <= 0){
            throw new ServiceException("新增其它入库单失败！");
        }
        int lineNo = 1;
        //新增明细
        for (StockInOtherDetail detail : detailList) {
            AjaxResult sourcePositionResult = mainDataService.getPositionCodeByLocationCodeAndMaterialNo(
                    stockInOtherDto.getLocationCode(), detail.getMaterialNo());
            if(sourcePositionResult.isError()){
                throw new ServiceException(Objects.toString(sourcePositionResult.get("msg"),"获取仓位失败"));
            }
            if (ObjectUtils.isEmpty(sourcePositionResult.get("data"))){
                throw new ServiceException("推荐仓位为空，请核实仓位！！！！ ");

            }
            StoragePositionVo storagePositionVo = JSON.parseObject(JSON.toJSONString(sourcePositionResult.get("data")), StoragePositionVo.class);

            //  TODO:锁仓位
            if ("1".equals(storagePositionVo.getMixedFlag())) {
                Long[] ids = new Long[1];
                ids[0] = storagePositionVo.getId();
                mainDataService.updateStoragePositionStatus(ids,"1");
            }

            detail.setOrderNo(orderNo);
            detail.setLineNo(String.valueOf(lineNo++));
            detail.setCreateBy(SecurityUtils.getUsername());
            detail.setCreateTime(DateUtils.getNowDate());
            detail.setQty(detail.getOperationQty());
            detail.setUnit(detail.getUnit());
            detail.setStgeLoc(stockInOtherDto.getLocationCode());
            detail.setMoveType(stockInOtherDto.getMoveType());
            if (StringUtils.isEmpty(detail.getFactoryCode())) {
                detail.setFactoryCode(storagePositionVo.getFactoryCode());
            }
            if (stockInOtherDetailMapper.insertStockInOtherDetail(detail) <= 0){
                throw new ServiceException("新增其它入库明细单据失败！");
            }
            //根据配置 判断是否走自动生成任务
            if (flag){
                addRetTask(orderNo, detail,stockInOther,storagePositionVo);
            }
        }
        return stockInOther;
    }

    /**
     * 打印
     * @param retTask
     */
    public void printList( RetTask retTask, StockInOtherDetail stockInOtherDetail) {
        AjaxResult printerByLocationAjaxResult = printService.findPrinterByLocation(retTask.getStorageLocationCode());

        if (printerByLocationAjaxResult.isError()) {
            throw new ServiceException(String.format("打印失败,原因：%s", printerByLocationAjaxResult.get("msg").toString()));
        }

        List<WmsPrinterLocation> wmsPrinterLocationList = JSON.parseArray(JSON.toJSONString(printerByLocationAjaxResult.get("data")), WmsPrinterLocation.class);

        String printerName = null;
        if (CollectionUtils.isEmpty(wmsPrinterLocationList)) {
            printerName = "";
        } else {
            printerName = wmsPrinterLocationList.get(0).getPrintName();
        }

        // 打印TO单
        String templateCode = "TO";
        HiprintTemplateDTO dto = new HiprintTemplateDTO();
        dto.setTemplateNo(templateCode);
        dto.setPrinterName(printerName);
        dto.setTitle("T1标签打印");
        JSONArray jsonArray = new JSONArray();

        JSONObject jsonObject = new JSONObject();
        // 目标存储类型库存地点的备注
        jsonObject.put("targetSotrageType", retTask.getStorageLocationCode());
        // 工厂或库存地点
        jsonObject.put("factoryCodeOrLocationCode", stockInOtherDetail.getFactoryCode());
        // 操作类型
        jsonObject.put("taskType", "其他入库任务");
        // 存储类型
        jsonObject.put("storageType", "");
        // 移动类型
        jsonObject.put("moveType", stockInOtherDetail.getMoveType());
        // 仓位
        jsonObject.put("postionNo", retTask.getPositionNo());
        // 创建日期
        jsonObject.put("createDate", DateFormatUtils.format(retTask.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        // 创建时间
//        jsonObject.put("createTime", DateFormatUtils.format(retTask.getCreateTime(), "HH:MM"));
        // 供应商名称
        jsonObject.put("vendorName", stockInOtherDetail.getSupplierCode());
        // 用户名
        jsonObject.put("userName", SecurityUtils.getUsername());
        // 货架寿命
        jsonObject.put("life", SecurityUtils.getUsername());
        // 源发地仓储类型
        jsonObject.put("sourceStorageType", "");
        // 源发地仓位
        jsonObject.put("sourcePostionNo", "");
        // 重量
        jsonObject.put("weight", "");
        // 供应商批次
        jsonObject.put("vendorLot", "");
        // 物料号
        jsonObject.put("materialNo", stockInOtherDetail.getMaterialNo());
        // 物料描述
        jsonObject.put("materialName", stockInOtherDetail.getMaterialName());
        // 移动数量
        jsonObject.put("moveQty", stockInOtherDetail.getQty());
        // 批次
        jsonObject.put("lot", stockInOtherDetail.getLot());
        // 包装型号
        jsonObject.put("packageType", "");
        // 完成时间
        jsonObject.put("completeTime", "");
        // 包装数量
        jsonObject.put("packQty", "");
        // 任务流水号
        jsonObject.put("taskNo", retTask.getTaskNo());
        // 检验流水号
        jsonObject.put("inspectNo", "");
        // qrCode  O%T任务号%M物料号
        jsonObject.put("qrCode", "O%T" + retTask.getTaskNo() + "%M" + retTask.getMaterialNo());
        jsonArray.add(jsonObject);
        dto.setDataArray(jsonArray);
        AjaxResult ajaxResult1 = printService.printByTemplate(dto);
        if (ajaxResult1.isError()) {
            throw new ServiceException(String.format("打印失败,原因：%s", ajaxResult1.get("msg").toString()));
        }
    }


    public void addRetTask(String orderNo, StockInOtherDetail detail,StockInOther stockInOther,StoragePositionVo storagePositionVo) {
        Long tenantId = SecurityUtils.getLoginUser().getSysUser().getTenantId();

        AjaxResult taskNumberAjaxResult= iCodeRuleService.getSeqWithTenantId(TaskNoTypeConstant.OTHER_TASK, String.valueOf(tenantId));
        if("".equals(Objects.toString(taskNumberAjaxResult.get("data"),"")) || taskNumberAjaxResult.isError()){
            throw new ServiceException("其它入库任务号生成失败！");
        }
        String taskNo = taskNumberAjaxResult.get("data").toString();
        RetTask retTask = new RetTask();
        retTask.setDetailId(detail.getId());
        retTask.setTaskNo(taskNo);
        retTask.setMaterialNo(detail.getMaterialNo());
        retTask.setMaterialName(detail.getMaterialName());
        retTask.setOldMatrialName(detail.getOldMaterialNo());
        retTask.setStockinOrderNo(orderNo);
        retTask.setLot(detail.getLot());
        retTask.setUnit(detail.getUnit());
        retTask.setOperationUnit(detail.getOperationUnit());
        retTask.setOperationQty(detail.getOperationQty());
        retTask.setOperationCompleteQty(BigDecimal.ZERO);
        retTask.setQty(detail.getQty());
        retTask.setCompleteQty(BigDecimal.ZERO);
        retTask.setTaskStatus(TaskStatusConstant.TASK_STATUS_NEW);
        retTask.setTaskType(StockInTaskConstant.OTHER_TYPE);
        retTask.setCreateBy(SecurityUtils.getUsername());
        retTask.setCreateTime(DateUtils.getNowDate());
        retTask.setMoveType(stockInOther.getMoveType());
        retTask.setPositionNo(storagePositionVo.getPositionNo());
        retTask.setPositionId(storagePositionVo.getId());
        retTask.setStorageLocationCode(detail.getStgeLoc());
        if (retTaskMapper.insertRetTask(retTask) <= 0){
            throw new ServiceException("新增其它入库任务失败！");
        }
        // 打印
//        printList(retTask, detail);
    }

    /**
     * 修改其它入库单
     *
     * @param stockInOther 其它入库单
     * @return 结果
     */
    @Override
    public int updateStockInOther(StockInOther stockInOther)
    {
        stockInOther.setUpdateTime(DateUtils.getNowDate());
        return stockInOtherMapper.updateStockInOther(stockInOther);
    }

    /**
     * 批量删除其它入库单
     *
     * @param ids 需要删除的其它入库单主键
     * @return 结果
     */
    @Override
    public int deleteStockInOtherByIds(Long[] ids)
    {
        return stockInOtherMapper.deleteStockInOtherByIds(ids);
    }

    /**
     * 删除其它入库单信息
     *
     * @param id 其它入库单主键
     * @return 结果
     */
    @Override
    public int deleteStockInOtherById(Long id)
    {
        return stockInOtherMapper.deleteStockInOtherById(id);
    }

    /**
     * 关闭其它入库单
     * @param id 其它入库主键id
     * @return 结果
     */
    @Override
    public int close(Long id) {
        //校验参数
        if (ObjectUtils.isEmpty(id)){
            throw new ServiceException("参数不存在！");
        }
        //查询单据
        StockInOther stockInOther = stockInOtherMapper.selectStockInOtherById(id);
        String status = stockInOther.getBillStatus();
        if (OrderStatusConstant.ORDER_STATUS_CLOSE.equals(status) || OrderStatusConstant.ORDER_STATUS_COMPLETE.equals(status)){
            throw new ServiceException("当前其他入库单已完成或已关闭！");
        }
        //关闭单据
        stockInOther.setBillStatus(OrderStatusConstant.ORDER_STATUS_CLOSE);
        stockInOther.setUpdateTime(DateUtils.getNowDate());
        stockInOther.setUpdateBy(SecurityUtils.getUsername());
        if (stockInOtherMapper.updateStockInOther(stockInOther) <= 0){
            throw new ServiceException("关闭其他入库单失败！");
        }
        if (OrderStatusConstant.ORDER_STATUS_NEW.equals(status)){
            return 1;
        }
        //查询明细
        StockInOtherDetail stockInOtherDetail = new StockInOtherDetail();
        stockInOtherDetail.setOrderNo(stockInOther.getOrderNo());
        List<StockInOtherDetail> detailList = stockInOtherDetailMapper.selectStockInOtherDetailList(stockInOtherDetail);
        for (StockInOtherDetail detail : detailList) {
            //关闭明细对应退货任务
            RetTaskDto retTaskDto = new RetTaskDto();
            retTaskDto.setDetailId(detail.getId());
            retTaskDto.setCloseTask(TaskStatusConstant.TASK_STATUS_CLOSE);
            retTaskDto.setTaskType(StockInTaskConstant.OTHER_TYPE);
            retTaskDto.setTaskStatus(TaskStatusConstant.TASK_STATUS_COMPLETE);
            retTaskDto.setUpdateBy(SecurityUtils.getUsername());
            retTaskDto.setUpdateTime(DateUtils.getNowDate());
            if (retTaskMapper.updateRetTaskStatus(retTaskDto) <= 0){
                throw new ServiceException("关闭退货任务失败！");
            }
        }
        return 1;
    }

    /**
     * 批量激活其它入库单
     * @param ids 其它入库主键ids
     * @return 结果
     */
    @Override
    public int activeStockInOtherByIds(Long[] ids) {
        //校验参数
        if (ObjectUtils.isEmpty(ids)) {
            throw new ServiceException("参数不存在！");
        }
        for (Long id : ids) {
            //查询单据
            StockInOther stockInOther = stockInOtherMapper.selectStockInOtherById(id);
            String status = stockInOther.getBillStatus();
            String orderNo = stockInOther.getOrderNo();
            if (!OrderStatusConstant.ORDER_STATUS_NEW.equals(status)) {
                throw new ServiceException("其他入库单:"+ orderNo +"必须为新建状态才能激活！");
            }
            //更新状态
            stockInOther.setBillStatus(OrderStatusConstant.ORDER_STATUS_ACTIVE);
            stockInOther.setUpdateBy(SecurityUtils.getUsername());
            stockInOther.setUpdateTime(DateUtils.getNowDate());
            if (stockInOtherMapper.updateStockInOther(stockInOther) <= 0) {
                throw new ServiceException("更新其他入库单失败！");
            }
            //查询明细
            StockInOtherDetail stockInOtherDetail = new StockInOtherDetail();
            stockInOtherDetail.setOrderNo(orderNo);
            List<StockInOtherDetail> detailList = stockInOtherDetailMapper.selectStockInOtherDetailList(stockInOtherDetail);
            //新增退货入库任务
            for (StockInOtherDetail detail : detailList) {
                AjaxResult sourcePositionResult = mainDataService.getPositionCodeByLocationCodeAndMaterialNo(
                        detail.getStgeLoc(), detail.getMaterialNo());
                if(sourcePositionResult.isError()){
                    throw new ServiceException(Objects.toString(sourcePositionResult.get("msg"),"获取仓位失败"));
                }
                StoragePositionVo storagePositionVo = JSON.parseObject(JSON.toJSONString(sourcePositionResult.get("data")), StoragePositionVo.class);
                addRetTask(orderNo, detail,stockInOther,storagePositionVo);
            }
            //新增其他入库任务
            for (Long id1 : ids) {
                StockInOther stockInOther1 = stockInOtherMapper.selectStockInOtherById(id1);
                Long tenantId = SecurityUtils.getLoginUser().getSysUser().getTenantId();
                AjaxResult ajaxResult = iCodeRuleService.getSeqWithTenantId(OrderNoTypeConstant.OTHER, String.valueOf(tenantId));
                if (ajaxResult.isError() || StringUtils.isEmpty(ajaxResult.get("data").toString())) {
                    throw new ServiceException("检验单号生成失败！");
                }
                String taskCode = ajaxResult.get("data").toString();
                TaskInfo taskInfo = new TaskInfo();
                taskInfo.setTaskNo(taskCode);
                taskInfo.setTaskType(TaskTypeConstant.STOCKIN_OTHER);
                taskInfo.setTaskStatus(TaskStatusConstant.TASK_STATUS_NEW);
                taskInfo.setCreateTime(DateUtils.getNowDate());
                taskInfo.setLocationCode(stockInOther1.getLocationCode());
                taskInfo.setMoveType(stockInOther1.getMoveType());
            }
        }
        return 1;
    }

    @Override
    public StockinOtherDetailDto stockInOtherOrderDetailList(Long id) {
        StockinOtherDetailDto stockinOtherDetailDto = stockInOtherDetailMapper.stockInOtherOrderDetailList(id);
        stockinOtherDetailDto.setCreateTime(DateUtils.getNowDate());
        WmsMaterialBasicDto unitDefDto = new WmsMaterialBasicDto();

        // 根据物料号和库存地点查询仓位信息

        List<String> materialNoList =  new ArrayList<>();
        materialNoList.add(stockinOtherDetailDto.getMaterialNo());
        unitDefDto.setMaterialNoList(materialNoList);
        AjaxResult unitResult = mainDataService.getMaterialArrInfo(unitDefDto);
        if(unitResult.isError()) {
            throw new ServiceException(unitResult.get("msg").toString());
        }
        List<WmsMaterialAttrParamsDto> materialAttrParamsList = JSONObject.parseArray(unitResult.get("data").toString(), WmsMaterialAttrParamsDto.class);

        if(materialAttrParamsList == null || materialAttrParamsList.isEmpty()) {
            throw new ServiceException(String.format("物料”%s“,过期时间获取失败！", stockinOtherDetailDto.getMaterialNo()));
        }

        WmsMaterialAttrParamsDto materialUnitDefDto = materialAttrParamsList.get(0);
        if(ObjectUtils.isEmpty(materialUnitDefDto)) {
            throw new ServiceException("过期时间获取失败！");
        }
        // 获取生产日期 过期日期
        String defaultValidityPeriod = materialUnitDefDto.getDefaultValidityPeriod();

        if(ObjectUtils.isEmpty(defaultValidityPeriod)) {
            throw new ServiceException("默认有效期获取失败!");
        }
        stockinOtherDetailDto.setFinishTime(DateUtils.addDays(new Date(), Integer.valueOf(defaultValidityPeriod)));
        return stockinOtherDetailDto;
    }

    @Override
    public List<StockInOtherParamsDto> stockInOrderList(StockInOtherParamsDto stockInOtherParamsDto) {
        return stockInOtherMapper.stockInOrderList(stockInOtherParamsDto);

    }

    @Override
    @GlobalTransactional
    public AjaxResult submitStockInOrderTask(StockinOtherDetailDto stockinOtherDetailDto) throws Exception {
        StockInOtherDetail stockInOtherDetail = stockInOtherDetailMapper.selectStockInOtherDetailById(stockinOtherDetailDto.getId());

        RetTask retTask = new RetTask();
        retTask.setTaskNo(stockinOtherDetailDto.getTaskNo());
        List<RetTask> retTasks = retTaskMapper.selectRetTaskList(retTask);
        RetTask retTask1 = retTasks.get(0);
        // 判断任务状态是否已完成
        if (TaskStatusConstant.TASK_STATUS_COMPLETE.equals(retTask1.getTaskStatus())) {
            throw new ServiceException("该任务已完成,请勿重复提交!");
        }

        String mblnr = null;
        //过账 远程调用ESB服务
        if(StringUtils.isNotEmpty(retTask1.getMoveType())&&!retTask1.getMoveType().equals("Z99")) {
            mblnr = remoteEsbSendService(retTask1, stockInOtherDetail);
        }

        retTask1.setTaskStatus(TaskStatusConstant.TASK_STATUS_COMPLETE);
        retTask1.setUpdateTime(DateUtils.getNowDate());
        retTask1.setUpdateBy(SecurityUtils.getUsername());
        BeanUtils.copyProperties(retTask1, retTask);
        retTaskMapper.updateRetTask(retTask);

        // 根据单据号去查询是否有1,,2状态任务，如果没有则将单据更新为完成，否则不更新
        StockInOtherParamsDto stockInOtherParamsDto = new StockInOtherParamsDto();
        stockInOtherParamsDto.setTaskType(StockInTaskConstant.OTHER_TYPE);
        stockInOtherParamsDto.setOrderNo(retTask.getStockinOrderNo());
        List<StockInOtherParamsDto> list = stockInOtherMapper.stockInOrderListByTaskTypeAndOderNo(stockInOtherParamsDto);
        if (CollectionUtils.isEmpty(list)) {
            StockInOther stockInOther = stockInOtherMapper.selectStockInOtherByOrderNo(retTask.getStockinOrderNo());
            stockInOther.setBillStatus(OrderStatusConstant.ORDER_STATUS_COMPLETE);
            stockInOtherMapper.updateStockInOther(stockInOther);
        } else {
            StockInOther stockInOther = stockInOtherMapper.selectStockInOtherByOrderNo(retTask.getStockinOrderNo());
            stockInOther.setBillStatus(OrderStatusConstant.ORDER_STATUS_PART_COMPLETE);
            stockInOtherMapper.updateStockInOther(stockInOther);
        }

        // 入库台账信息
        InventoryDetails inventoryDetails = new InventoryDetails();
        inventoryDetails.setMaterialNo(stockInOtherDetail.getMaterialNo());
        inventoryDetails.setMaterialName(stockInOtherDetail.getMaterialName());
        inventoryDetails.setLocationCode(stockInOtherDetail.getStgeLoc());
        inventoryDetails.setStockInLot(stockInOtherDetail.getLot());
        inventoryDetails.setFactoryCode(stockInOtherDetail.getFactoryCode());
        inventoryDetails.setInventoryQty(stockInOtherDetail.getQty());
        inventoryDetails.setAvailableQty(stockInOtherDetail.getQty());
        inventoryDetails.setOperationAvailableQty(stockInOtherDetail.getQty());
        inventoryDetails.setSpecialType(stockInOtherDetail.getSpecialType());
        inventoryDetails.setHuNo(stockInOtherDetail.getHuNo());
        inventoryDetails.setOldMesContainerNo(stockInOtherDetail.getMesContainerNo());
        inventoryDetails.setUnit(stockInOtherDetail.getUnit());
        inventoryDetails.setSupplierCode(stockInOtherDetail.getSupplierCode());
        inventoryDetails.setPositionNo(retTask.getPositionNo());
        inventoryDetails.setCreateTime(DateUtils.getNowDate());
        inventoryDetails.setUpdateTime(DateUtils.getNowDate());
        inventoryDetails.setUpdateBy(SecurityUtils.getUsername());
        inventoryDetails.setExpiryDate(stockinOtherDetailDto.getFinishTime());
        inventoryDetails.setProductTime(stockinOtherDetailDto.getCreateTime());
        inventoryDetails.setStockInDate(DateUtils.getNowDate());
        inventoryDetails.setTaskNo(retTask.getTaskNo());
        // 库存台账查询写死6
        inventoryDetails.setTenantId(6L);
        iInStoreService.add(inventoryDetails);

        // 增加移动记录
        RetTaskDto retTaskDto = new RetTaskDto();
        BeanUtils.copyProperties(retTask,retTaskDto);
        // 仓位地点
        retTaskDto.setConfirmPosition(retTask.getPositionNo());
        // 库存地点
        retTaskDto.setStorageLocationCode(stockInOtherDetail.getStgeLoc());
        retTaskDto.setSupplierCode(stockInOtherDetail.getSupplierCode());
        // 物料移动记录
        stockInCommonService.addTaskLog(retTaskDto, stockInOtherDetail.getOrderNo(), stockInOtherDetail.getLineNo(), mblnr==null?"":mblnr);

        // 查询仓位信息
        AjaxResult ajaxResult = mainDataService.getPositionInfoById(retTaskDto.getPositionId());
        if (ajaxResult.isError()) {
            throw new ServiceException(ajaxResult.get("msg").toString());
        }
        StoragePositionVo storagePositionVo = JSONObject.parseObject(JSONObject.toJSONString(ajaxResult.get("data")), StoragePositionVo.class);
        if (ObjectUtils.isEmpty(storagePositionVo)) {
            throw new ServiceException(ErrorMessage.POSITION_CODE_NOTHING);
        }
        // 释放仓位
        if("1".equals(storagePositionVo.getMixedFlag())){
            Long[] ids = {storagePositionVo.getId()};
            mainDataService.updateStoragePositionStatus(ids,"0");
        }
        Long[] ids = new Long[1];
        ids[0] = retTaskDto.getPositionId();
        mainDataService.updateStoragePositionStatus(ids,"0");

        return AjaxResult.success();
    }

    @Override
    public StockinOtherDetailDto stockInOtherOrderDetailListByTaskNo(String taskNo) {
        StockinOtherDetailDto stockinOtherDetailDto = stockInOtherDetailMapper.stockInOtherOrderDetailListByTaskNo(taskNo);
        return stockinOtherDetailDto;
    }

    private String remoteEsbSendService(RetTask retTask, StockInOtherDetail stockInOtherDetail) throws Exception {
            IMsgObject reqMo = new MsgObject(IMsgObject.MOType.initSR);
            Date now = new Date();
            List<GroupRecord> list = new ArrayList<>();
            GroupRecord groupRecord = new GroupRecord();
            groupRecord.setName("IS_HEAD");
            groupRecord.setFieldValue("BUDAT", DateFormatUtils.format(now, "yyyyMMdd"));
            groupRecord.setFieldValue("BLDAT", DateFormatUtils.format(now, "yyyyMMdd"));
            groupRecord.setFieldValue("BKTXT", SecurityUtils.getUsername());
            groupRecord.setFieldValue("UNAME", SecurityUtils.getUsername());
            GroupRecord itemRecord = new GroupRecord();
            itemRecord.setName("IT_ITEM");
            // 工厂
            itemRecord.setFieldValue("PLANT", stockInOtherDetail.getFactoryCode());
            // 物料
            itemRecord.setFieldValue("MATERIAL", stockInOtherDetail.getMaterialNo());
            // 存储地点
            itemRecord.setFieldValue("STGE_LOC", stockInOtherDetail.getStgeLoc());
            // 移动类型(库存管理)
            itemRecord.setFieldValue("MOVE_TYPE", retTask.getMoveType());
            // 批次编号
            itemRecord.setFieldValue("BATCH", stockInOtherDetail.getLot());
            // 特殊库存标识
            itemRecord.setFieldValue("SPEC_STOCK", "");
            //项目文本
            itemRecord.setFieldValue("ITEM_TEXT", retTask.getTaskNo());
            // 供应商帐户号
            if (!StringUtils.isEmpty(stockInOtherDetail.getSupplierCode())) {
                if (stockInOtherDetail.getSupplierCode().length() >= 10) {
                    itemRecord.setFieldValue("VENDOR", stockInOtherDetail.getSupplierCode());
                }
                int num = (10 - stockInOtherDetail.getSupplierCode().length());
                if (num > 0) {
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < num; i++) {
                        sb.append("0");
                    }
                    itemRecord.setFieldValue("VENDOR", sb.toString() + stockInOtherDetail.getSupplierCode());
                }
            } else {
                itemRecord.setFieldValue("VENDOR","");
            }
            // 以录入项单位表示的数量
            itemRecord.setFieldValue("ENTRY_QNT", stockInOtherDetail.getQty().toString());
            // 条目单位
            itemRecord.setFieldValue("ENTRY_UOM", stockInOtherDetail.getUnit());
            // 成本中心(报废必传)
            itemRecord.setFieldValue("COSTCENTER", stockInOtherDetail.getCostCenterCode());
            list.add(groupRecord);
            list.add(itemRecord);
            reqMo.setReqGroupRecord(list);
            byte[] result = remoteEsbSendService.sendToEsb(EsbSendSapConstant.Z_IWMS_SCRAP, reqMo.toString().getBytes());
            // 转化为标准结果
            MsgObject resMo = new MsgObject(result, IMsgObject.MOType.initSR);
            String errorMsg = resMo.getResValue("ERRORMSG");
            String flag = resMo.getResValue("FLAG");
            if (!SapReqOrResConstant.SAP_RETURN_TYPE.equals(flag)) {
                throw new ServiceException("其他入库sap过账失败!" + errorMsg);
            }
        // 物料凭证
        String mblnr = resMo.getResValue("MBLNR");
        return mblnr;
    }

}
