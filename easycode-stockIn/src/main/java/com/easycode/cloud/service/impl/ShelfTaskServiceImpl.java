package com.easycode.cloud.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import com.baomidou.lock.annotation.Lock4j;
import com.easycode.cloud.service.IShelfTaskService;
import com.weifu.cloud.common.core.exception.ServiceException;
import com.weifu.cloud.common.core.text.Convert;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.weifu.cloud.common.core.utils.SpringUtils;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.security.utils.SecurityUtils;
import com.weifu.cloud.constant.*;
import com.easycode.cloud.domain.ShelfTask;
import com.weifu.cloud.domain.StockInStdOrder;
import com.weifu.cloud.domain.StockInStdOrderDetail;
import com.weifu.cloud.domain.WmsLkDeliveryDetailRequirement;
import com.weifu.cloud.domain.dto.MaterialAttrDto;
import com.weifu.cloud.domain.dto.MaterialMainDto;
import com.easycode.cloud.domain.dto.ShelfTaskDto;
import com.weifu.cloud.domain.dto.StockInStdOrderDetailDto;
import com.weifu.cloud.domian.InventoryDetails;
import com.weifu.cloud.domian.TaskLog;
import com.weifu.cloud.domian.WmsPrinterLocation;
import com.weifu.cloud.domian.dto.*;
import com.weifu.cloud.domian.vo.InventoryDetailsVo;
import com.weifu.cloud.domian.vo.StoragePositionVo;
import com.weifu.cloud.mapper.EsbSendCommonMapper;
import com.easycode.cloud.mapper.ShelfTaskMapper;
import com.easycode.cloud.mapper.StockInStdOrderDetailMapper;
import com.easycode.cloud.mapper.StockInStdOrderMapper;
import com.weifu.cloud.service.*;
import com.weifu.cloud.tools.StringUtil;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * 上架任务Service业务层处理
 *
 * @author zhanglei
 * @date 2023-04-12
 */
@Service
@Transactional(rollbackFor = ServiceException.class)
public class ShelfTaskServiceImpl implements IShelfTaskService
{
    @Autowired
    private ShelfTaskMapper shelfTaskMapper;

    @Autowired
    private IMainDataService iMainDataService;

    @Autowired
    private IInStoreService inStoreService;

    @Autowired
    private ITaskService taskService;

    @Autowired
    private RemoteEsbSendService remoteEsbSendService;

    @Autowired
    private IPrintService printService;

    @Autowired
    private EsbSendCommonMapper esbSendCommonMapper;

    @Autowired
    private StockInStdOrderMapper stockInStdOrderMapper;

    @Autowired
    private StockInStdOrderDetailMapper stockInStdOrderDetailMapper;

    @Autowired
    private ICodeRuleService iCodeRuleService;

    @Autowired
    private LockTemplate lockTemplate;

    /**
     * 查询上架任务
     *
     * @param id 上架任务主键
     * @return 上架任务
     */
    @Override
    public ShelfTask selectShelfTaskById(Long id)
    {
        return shelfTaskMapper.selectShelfTaskById(id);
    }

    /**
     * 查询上架任务列表
     *
     * @param shelfTask 上架任务
     * @return 上架任务
     */
    @Override
    public List<ShelfTask> selectShelfTaskList(ShelfTask shelfTask)
    {
        return shelfTaskMapper.selectShelfTaskList(shelfTask);
    }

    /**
     * 新增上架任务
     *
     * @param shelfTask 上架任务
     * @return 结果
     */
    @Override
    public ShelfTask insertShelfTask(ShelfTask shelfTask)
    {
        // 上架任务号
        AjaxResult result = iCodeRuleService.getSeqWithTenantId(TaskNoTypeConstant.SHELF_TASK, com.weifu.cloud.common.core.text.Convert.toStr(SecurityUtils.getLoginUser().getSysUser().getTenantId()));
        if (result.isError() || StringUtils.isEmpty(result.get("data").toString())) {
            throw new ServiceException("上架任务号生成失败！");
        }
        String taskNo = result.get("data").toString();
        shelfTask.setTaskNo(taskNo);
        shelfTask.setCreateTime(DateUtils.getNowDate());
        shelfTask.setCreateBy(SecurityUtils.getUsername());
        shelfTask.setStatus(TaskStatusConstant.TASK_STATUS_NEW);
        shelfTask.setPrintSum(1);
        shelfTask.setOperationQty(shelfTask.getQty());
        shelfTask.setOperationUnit(shelfTask.getUnit());
        if ( shelfTaskMapper.insertShelfTask(shelfTask) <= 0) {
            throw new ServiceException("上架任务新增失败！");
        }
        return shelfTask;
    }

    /**
     * 新增上架任务
     *
     * @param shelfTaskList 上架任务
     * @return 结果
     */
    @Override
    public int insertShelfTaskList(List<ShelfTask> shelfTaskList) {
//        for (ShelfTask shelfTask : shelfTaskList) {
//            shelfTaskMapper.insertShelfTask(shelfTask);
//        }
        List<ShelfTask> taskList = Optional.ofNullable(shelfTaskList).orElse(new ArrayList<ShelfTask>());
        if (!ObjectUtils.isEmpty(taskList)) {
            shelfTaskMapper.insertShelfTaskList(taskList);
        }
        return 1;
    }

    /**
     * 修改上架任务
     *
     * @param shelfTask 上架任务
     * @return 结果
     */
    @Override
    public int updateShelfTask(ShelfTask shelfTask)
    {
        shelfTask.setUpdateTime(DateUtils.getNowDate());
        return shelfTaskMapper.updateShelfTask(shelfTask);
    }

    /**
     * 批量删除上架任务
     *
     * @param ids 需要删除的上架任务主键
     * @return 结果
     */
    @Override
    public int deleteShelfTaskByIds(Long[] ids)
    {
        return shelfTaskMapper.deleteShelfTaskByIds(ids);
    }

    /**
     * 删除上架任务信息
     *
     * @param id 上架任务主键
     * @return 结果
     */
    @Override
    public int deleteShelfTaskById(Long id)
    {
        return shelfTaskMapper.deleteShelfTaskById(id);
    }

    /**
     * 关闭上架任务信息
     */
    @Override
    public void closeShelfTask(Long[] ids) {
        for(Long id : ids){
            ShelfTask shelfTask = new ShelfTask();
            shelfTask.setUpdateBy(SecurityUtils.getUsername());
            shelfTask.setUpdateTime(DateUtils.getNowDate());
            shelfTask.setStatus(TaskStatusConstant.TASK_STATUS_CLOSE);
            shelfTask.setId(id);
            shelfTaskMapper.updateShelfTask(shelfTask);
        }
    }

    /**
     * pda端查询上架任务
     * @param shelfTaskDto 上架任务
     * @return 上架任务集合
     */
    @Override
    public List<ShelfTask> selectPdaShelfTaskList(ShelfTaskDto shelfTaskDto) {
        return shelfTaskMapper.pdaShelfTaskList(shelfTaskDto);
    }

    /**
     * pda上架任务提交
     * @param shelfTaskDto 上架任务
     * @return 结果
     */
    @Override
    @GlobalTransactional
    @Lock4j(keys = {"#shelfTaskDto.id"}, acquireTimeout = 10, expire = 10000)
    public int submit(ShelfTaskDto shelfTaskDto){
        if (ObjectUtils.isEmpty(shelfTaskDto)) {
            throw new ServiceException("参数不存在");
        }
        //记录上架仓位
        ShelfTask shelfTask = shelfTaskMapper.selectShelfTaskById(shelfTaskDto.getId());

        if (TaskStatusConstant.TASK_STATUS_COMPLETE.equals(shelfTask.getStatus())) {
            throw new ServiceException(ErrorMessage.STD_SHELT_TASK_COMPLETED);
        }

        String stockinOrderNo = shelfTaskDto.getStockinOrderNo();
        StockInStdOrder stockInStdOrder = new StockInStdOrder();
        stockInStdOrder.setOrderNo(stockinOrderNo);
        List<StockInStdOrder> stockInStdOrderList = stockInStdOrderMapper.selectStockInStdOrderList(stockInStdOrder);
        if (ObjectUtils.isEmpty(stockInStdOrderList)) {
            throw new ServiceException("未找到对应的标准订单！");
        }

        String lockKey = shelfTaskDto.getMaterialNo() + shelfTaskDto.getLocationCode() + shelfTaskDto.getPositionNo();
        final LockInfo lockInfo = lockTemplate.lock(lockKey, 10*1000,5*1000);
        if (null == lockInfo) {
            throw new ServiceException("其他上架业务处理中，请稍后重试！");
        }

        Long positionId = shelfTask.getPositionId();
        StoragePositionVo storagePositionVo = new StoragePositionVo();
        storagePositionVo.setId(positionId);
        storagePositionVo.setPositionNo(shelfTask.getPositionNo());
        storagePositionVo.setAreaCode(shelfTask.getAreaCode());
        storagePositionVo.setLocationCode(shelfTask.getLocationCode());
        storagePositionVo.setFactoryCode(stockInStdOrderList.get(0).getFactoryCode());

        // 上架任务更新
        shelfTask.setStatus(TaskStatusConstant.TASK_STATUS_COMPLETE);

        // 完成数量
        shelfTask.setCompleteQty(shelfTaskDto.getOperationQty());
        // 完成操作数量
        shelfTask.setOperationCompleteQty(shelfTask.getOperationQty());
        shelfTask.setUpdateBy(SecurityUtils.getUsername());
        shelfTask.setUpdateTime(DateUtils.getNowDate());
        shelfTask.setFinishTime(DateUtils.getNowDate());
        if (shelfTaskMapper.updateShelfTask(shelfTask) <= 0){
            lockTemplate.releaseLock(lockInfo);
            throw new ServiceException("更新上架任务失败！");
        }

        if (null != positionId) {
            iMainDataService.updateStoragePositionStatus(new Long[]{positionId}, CommonYesOrNo.UNLOCK_POSITION);
        }

        StockInStdOrderDetailDto stockInStdOrderDetailDto = new StockInStdOrderDetailDto();
        stockInStdOrderDetailDto.setPurchaseOrderNo(stockinOrderNo);
        stockInStdOrderDetailDto.setPurchaseLineNo(shelfTaskDto.getStockinLineNo());

        List<StockInStdOrderDetail> stdOrderDetailList = stockInStdOrderDetailMapper.selectStockInStdOrderDetailList(stockInStdOrderDetailDto);

        if (ObjectUtils.isEmpty(stdOrderDetailList)) {
            lockTemplate.releaseLock(lockInfo);
            throw new ServiceException("未找到对应的标准订单明细！");
        }

        List<InventoryDetails> list = getInventoryDetails(shelfTaskDto, storagePositionVo, shelfTask);
        //上架任务更新台账
        AjaxResult result = inStoreService.moveInventory(list);
        if (result.isError()) {
            lockTemplate.releaseLock(lockInfo);
            throw new ServiceException("上架任务更新库存台账失败！");
        }
        InventoryDetailsVo inventoryDetailsVo = JSON.parseObject(
                JSON.toJSONString(result.get("data")), InventoryDetailsVo.class);

        addTaskLog(stdOrderDetailList.get(0), storagePositionVo, shelfTask, inventoryDetailsVo);
        lockTemplate.releaseLock(lockInfo);
        return 1;
    }

    /**
     * SAP调拨
     * @param shelfTask
     * @param storagePositionVo
     */
    private String moveLocationSap(ShelfTask shelfTask, StoragePositionVo storagePositionVo) {
        // sap调拨
        Map<String,String> map = new HashMap<>();
        map.put("factoryCode", storagePositionVo.getFactoryCode());
        map.put("materialNo", shelfTask.getMaterialNo());
        map.put("lotNo", shelfTask.getLot());
        map.put("sourceLocation",shelfTask.getSourceLocationCode());
        map.put("comCode", SecurityUtils.getComCode());
        map.put("targetLocation", shelfTask.getLocationCode());
        map.put("qty", Convert.toStr(shelfTask.getQty()));
        // 获取物料默认单位
        MaterialAttrDto materialAttrDto = new MaterialAttrDto();
        materialAttrDto.setMaterialNo(shelfTask.getMaterialNo());
        List<MaterialMainDto> materialList = esbSendCommonMapper.getMaterialMain(materialAttrDto);
        if(CollectionUtils.isEmpty(materialList)){
            throw new ServiceException(String.format("物料号：%s，获取默认单位为空！", shelfTask.getMaterialNo()));
        }
        map.put("unit",materialList.get(0).getDefaultUnit());
        map.put("isConsign", CommonYesOrNo.NO);
        AjaxResult res = inStoreService.moveLocationSap(map);
//        if ("".equals(Objects.toString(res.get("data"), "")) || res.isError()) {
//            throw new ServiceException(Objects.toString(res.get("msg").toString(), "调用库内作业服务失败"));
//        }
        return JSON.toJSONString(res);
    }
    /**
     * 添加任务
     * @param stdOrderDetail
     * @param storagePositionVo 库位
     * @param shelfTask 上架任务
     * @return 库存明细
     */
    private void addTaskLog(StockInStdOrderDetail stdOrderDetail, StoragePositionVo storagePositionVo, ShelfTask shelfTask, InventoryDetailsVo data) {
        //同步添加上架任务记录
        TaskLog taskLog = new TaskLog();
        taskLog.setTaskNo(shelfTask.getTaskNo());
        taskLog.setTaskType(TaskLogTypeConstant.SHELF);
        taskLog.setOrderType(TaskLogTypeConstant.SHELF);
        taskLog.setMaterialNo(shelfTask.getMaterialNo());
        taskLog.setOldMaterialNo(shelfTask.getOldMaterialNo());
        taskLog.setMaterialName(shelfTask.getMaterialName());
        taskLog.setLot(shelfTask.getLot());
        taskLog.setQty(shelfTask.getQty());
        taskLog.setUnit(shelfTask.getUnit());
        taskLog.setOperationQty(shelfTask.getOperationQty());
        taskLog.setOperationUnit(shelfTask.getOperationUnit());
        taskLog.setFactoryCode(stdOrderDetail.getFactoryCode());
        taskLog.setLocationCode(shelfTask.getSourceLocationCode());
        taskLog.setAreaCode(shelfTask.getSourceAreaCode());
        taskLog.setPositionCode(shelfTask.getSourcePositionNo());
        taskLog.setTargetPositionCode(storagePositionVo.getPositionNo());
        taskLog.setTargetLocationCode(storagePositionVo.getLocationCode());
        taskLog.setTargetAreaCode(storagePositionVo.getAreaCode());
        taskLog.setTargetFactoryCode(storagePositionVo.getFactoryCode());
        taskLog.setOrderNo(shelfTask.getStockinOrderNo());
        taskLog.setLineNo(shelfTask.getStockinLineNo());
        taskLog.setIsConsign(data.getIsConsign());
        taskLog.setIsQc(data.getIsQc());
        taskLog.setIsFreeze(data.getIsFreeze());
        taskLog.setSupplierCode(data.getSupplierCode());
        taskLog.setSupplierName(data.getSupplierName());
        taskLog.setMaterialCertificate(data.getVoucherNo());
        if (taskService.add(taskLog).isError()){
            throw new ServiceException("新增任务记录失败！");
        }
    }

    /**
    * 生成源台账信息以及目的台账信息
    * @param shelfTaskDto 上架dto, storagePositionVo 仓位 出参数据vo, shelfTask 上架参数
    * @date 2024/06/27
    * @author fsc
    * @return 库存台账集合
    */
    private List<InventoryDetails> getInventoryDetails(ShelfTaskDto shelfTaskDto, StoragePositionVo storagePositionVo, ShelfTask shelfTask) {
        List<InventoryDetails> list = new ArrayList<>(2);
        //源台账信息
        InventoryDetails inventory = new InventoryDetails();
//        inventory.setId(shelfTask.getInventoryId());
        inventory.setMaterialNo(shelfTask.getMaterialNo());
        inventory.setStockInLot(shelfTask.getLot());
        inventory.setLocationCode(shelfTaskDto.getSourceLocationCode());
        inventory.setPositionNo(shelfTaskDto.getSourcePositionNo());
        inventory.setFactoryId(storagePositionVo.getFactoryId());
        inventory.setFactoryCode(storagePositionVo.getFactoryCode());
        inventory.setFactoryName(storagePositionVo.getFactoryName());
        inventory.setWarehouseId(storagePositionVo.getWarehouseId());
        inventory.setWarehouseCode(storagePositionVo.getWarehouseCode());
        inventory.setTaskNo(shelfTaskDto.getTaskNo());
//        inventory.setContainerNo(shelfTaskDto.getContainerNo());
        list.add(inventory);
        //目标台账信息
        InventoryDetails inventoryDetails = new InventoryDetails();
        inventoryDetails.setMaterialNo(shelfTask.getMaterialNo());
        inventoryDetails.setPositionId(shelfTaskDto.getId());
        inventoryDetails.setPositionNo(shelfTaskDto.getPositionNo());
        inventoryDetails.setAreaCode(shelfTaskDto.getAreaCode());
        inventoryDetails.setLocationId(storagePositionVo.getLocationId());
        inventoryDetails.setLocationCode(shelfTaskDto.getLocationCode());
        inventoryDetails.setFactoryId(storagePositionVo.getFactoryId());
        inventoryDetails.setFactoryCode(storagePositionVo.getFactoryCode());
        inventoryDetails.setFactoryName(storagePositionVo.getFactoryName());
        inventoryDetails.setInventoryQty(shelfTaskDto.getOperationQty());
        inventoryDetails.setAvailableQty(shelfTaskDto.getOperationQty());
        inventoryDetails.setWarehouseId(storagePositionVo.getWarehouseId());
        inventoryDetails.setWarehouseCode(storagePositionVo.getWarehouseCode());
        inventoryDetails.setTaskNo(shelfTaskDto.getTaskNo());
//        inventoryDetails.setContainerNo(shelfTaskDto.getContainerNo());
        list.add(inventoryDetails);
        return list;
    }

    /**
     * 根据任务单号查询任务
     *
     * @param taskNo
     * @return
     */
    @Override
    public ShelfTask selectShelfTaskByTaskNo(String taskNo) {
        ShelfTask shelfTask = new ShelfTask();
        shelfTask.setTaskNo(taskNo);
        List<ShelfTask> shelfTasks = shelfTaskMapper.selectShelfTaskList(shelfTask);
        if(!CollectionUtils.isEmpty(shelfTasks)) {
            return shelfTasks.get(0);
        }
        return null;
    }

    /**
     * 打印
     *
     * @param locationCode
     * @param id
     */
    @Override
    public JSONObject print(String locationCode, Long id) {
        ShelfTask shelfTask = selectShelfTaskById(id);

        AjaxResult sourceLocation = iMainDataService.getStorageLocationByLocationCode(shelfTask.getSourceLocationCode());
        if(sourceLocation.isError()) {
            throw new ServiceException("获取原库存地点信息失败！");
        }
        StorageLocationDto sourceLocationDto = com.alibaba.fastjson2.JSONObject.parseObject(sourceLocation.get("data").toString(), StorageLocationDto.class);

        AjaxResult targetLocation = iMainDataService.getStorageLocationByLocationCode(shelfTask.getLocationCode());
        if(targetLocation.isError()) {
            throw new ServiceException("获取目的库存地点信息失败！");
        }
        StorageLocationDto targetLocationDto = com.alibaba.fastjson2.JSONObject.parseObject(targetLocation.get("data").toString(), StorageLocationDto.class);

        PrintTOParamsDto printTOParamsDto = new PrintTOParamsDto();
        printTOParamsDto.setUserName(SecurityUtils.getUsername());
        // 获取物料属性信息
        WmsMaterialBasicDto unitDefDto = new WmsMaterialBasicDto();
        unitDefDto.setMaterialNoList(Collections.singletonList(shelfTask.getMaterialNo()));
        AjaxResult unitResult = iMainDataService.getMaterialArrInfo(unitDefDto);
        if(!unitResult.isError()) {
            List<WmsMaterialAttrParamsDto> materialAttrParamsList = JSONObject.parseArray(unitResult.get("data").toString(), WmsMaterialAttrParamsDto.class);
            if(!ObjectUtils.isEmpty(materialAttrParamsList)) {
                WmsMaterialAttrParamsDto materialUnitDefDto = materialAttrParamsList.get(0);
                // 获取有效期
                printTOParamsDto.setAge(materialUnitDefDto.getDefaultValidityPeriod());
            }
        }

        String stockinOrderNo = shelfTask.getStockinOrderNo();
        StockInStdOrder stockInStdOrder = new StockInStdOrder();
        stockInStdOrder.setOrderNo(stockinOrderNo);
        List<StockInStdOrder> stockInStdOrderList = stockInStdOrderMapper.selectStockInStdOrderList(stockInStdOrder);
        if (!ObjectUtils.isEmpty(stockInStdOrderList)) {
            printTOParamsDto.setVendorName(stockInStdOrderList.get(0).getVendorName());
        }
        //操作类型	taskType
        printTOParamsDto.setTaskType("上架任务");
        //工厂/库存地	facotryOrLocation
        printTOParamsDto.setFactoryCode(targetLocationDto.getFactoryCode());
        printTOParamsDto.setLocationCode(shelfTask.getLocationCode());
        //仓位	postionNo
        printTOParamsDto.setPositionNo(shelfTask.getPositionNo());
        //源发地仓储类型	sourceStorageType
        printTOParamsDto.setSourceStorageType(sourceLocationDto.getLocationTypeDesc());
        printTOParamsDto.setSourceLocationCode(sourceLocationDto.getLocationCode());
        //源发地仓位	sourcePostionNo
        printTOParamsDto.setSourcePostionNo(shelfTask.getSourcePositionNo());
        //物料描述	materialName
        printTOParamsDto.setMaterialName(shelfTask.getMaterialName());
        //物料号	materialNo
        printTOParamsDto.setMaterialNo(shelfTask.getMaterialNo());
        //任务流水号	taskNo
        printTOParamsDto.setTaskNo(shelfTask.getTaskNo());
        //批次	lot
        printTOParamsDto.setLot(shelfTask.getLot());
        //移动数量	moveQty
        printTOParamsDto.setMoveQty(shelfTask.getQty());
        //包装数量	packQty
        printTOParamsDto.setPackQty(shelfTask.getQty());
        //用户名	userName
        printTOParamsDto.setUserName(SecurityUtils.getLoginUser().getUsername());
        // TODO 需要判断是否带Q上架
        //移动类型	moveType
        printTOParamsDto.setMoveType(SapReqOrResConstant.MOVE_LOCATION_MOVE_TYPE);
        //旧物料号
        printTOParamsDto.setOldMaterialNo(shelfTask.getOldMaterialNo());
//        AjaxResult ajaxResult = printService.printTO(printTOParamsDto);
//        if(ajaxResult.isError()) {
//            throw new ServiceException(String.format("打印失败,原因：%s",ajaxResult.get("msg").toString()));
//        } else {
            //更新打印次数
            updateShelfTaskPrintSum(shelfTask);
        //}
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        //任务类型
        jsonObject.put("taskType", printTOParamsDto.getTaskType());
        // 目标仓位
        jsonObject.put("postionNo", printTOParamsDto.getPositionNo());
        // 工厂或库存地点
        jsonObject.put("facotryOrLocation", String.format("%s/%s", printTOParamsDto.getFactoryCode(), printTOParamsDto.getLocationCode()));
        //源发地仓位
        jsonObject.put("sourcePostionNo", printTOParamsDto.getSourcePostionNo());
        //源库存地点
        jsonObject.put("sourceLocationCode", printTOParamsDto.getLocationCode());
        // 任务号
        jsonObject.put("taskNo", printTOParamsDto.getTaskNo());
        // 移动类型
        jsonObject.put("moveType", printTOParamsDto.getMoveType());
        // 创建日期
        jsonObject.put("createDate", DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss",DateUtils.getNowDate()));
        // 批次
        jsonObject.put("lot", printTOParamsDto.getLot());
        // 用户名
        jsonObject.put("userName", printTOParamsDto.getUserName());
        // 货架寿命
        jsonObject.put("age", printTOParamsDto.getAge());
        // 货架寿命
        jsonObject.put("life", printTOParamsDto.getAge());
        // 移动数量
        jsonObject.put("moveQty", printTOParamsDto.getMoveQty());
        // 包装数量
        jsonObject.put("packQty", printTOParamsDto.getPackQty());
        // qrCode
        jsonObject.put("qrCode", "O%T" + printTOParamsDto.getTaskNo() + "%M" + printTOParamsDto.getMaterialNo());
        // 供应商批次
        jsonObject.put("vendorBatchNo", printTOParamsDto.getVendorBatchNo());
        // 物料号
        jsonObject.put("materialNo", printTOParamsDto.getMaterialNo());
        // 物料名称
        jsonObject.put("materialName", printTOParamsDto.getMaterialName());
        // 供应商
        jsonObject.put("vendorName", printTOParamsDto.getVendorName());
        // 检验流水号
        jsonObject.put("inspectNo", printTOParamsDto.getInspectNo());
        // 原存储类型
        jsonObject.put("sourceStorageType", printTOParamsDto.getSourceStorageType());
        //目标存储类型
        jsonObject.put("targetSotrageType", printTOParamsDto.getTargetSotrageType());
        //存储类型
        jsonObject.put("storageType", printTOParamsDto.getStorageType());
        //重量
        jsonObject.put("weight", printTOParamsDto.getWeight());
        //供应商批次
        jsonObject.put("vendorLot", printTOParamsDto.getVendorLot());
        //包装型号
        jsonObject.put("packageType", printTOParamsDto.getPackageType());
        //完成时间
        jsonObject.put("completeTime", printTOParamsDto.getCompleteTime());
        //旧物料号
        jsonObject.put("oldMaterialNo", printTOParamsDto.getOldMaterialNo());
        return jsonObject;
    }


    /**
     * 打印
     * @param locationCode
     * @param ids
     */
    @Override
    public void printList(String locationCode, List<Long> ids) {
        for (Long id : ids) {
            print(locationCode, id);
        }
    }

    /**
     * 更新打印次数
     *
     */
    @Override
    public void updateShelfTaskPrintSum(ShelfTask shelfTask) {
        try {
            int num= 1;
            if(null != shelfTask.getPrintSum()){
                num = shelfTask.getPrintSum().intValue();
                num++;
            }
            shelfTask.setPrintSum(num);
            shelfTask.setUpdateBy(SecurityUtils.getUsername());
            shelfTask.setUpdateTime(new Date());
            shelfTaskMapper.updateShelfTask(shelfTask);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
