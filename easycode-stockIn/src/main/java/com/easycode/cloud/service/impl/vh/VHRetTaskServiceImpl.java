package com.easycode.cloud.service.impl.vh;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.baomidou.lock.annotation.Lock4j;
import com.easycode.cloud.domain.RetTask;
import com.easycode.cloud.domain.ShelfTask;
import com.easycode.cloud.domain.StockInOther;
import com.easycode.cloud.domain.StockInOtherDetail;
import com.easycode.cloud.domain.vo.RetTaskVo;
import com.easycode.cloud.service.IRetTaskService;
import com.easycode.cloud.service.IShelfTaskService;
import com.soa.eis.adapter.framework.message.IMsgObject;
import com.soa.eis.adapter.framework.message.impl.GroupRecord;
import com.soa.eis.adapter.framework.message.impl.MsgObject;
import com.weifu.cloud.common.core.exception.ServiceException;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.weifu.cloud.common.core.utils.SpringUtils;
import com.weifu.cloud.domain.*;
import com.weifu.cloud.domain.dto.SapMoveLocationParamsDto;
import com.weifu.cloud.domian.InventoryHistory;
import com.weifu.cloud.domian.StoragePosition;
import org.apache.commons.lang3.StringUtils;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.security.utils.SecurityUtils;
import com.weifu.cloud.constant.*;
import com.easycode.cloud.domain.dto.RetTaskDto;
import com.weifu.cloud.domain.dto.SapOtherReceiveParamsDto;
import com.weifu.cloud.domian.InventoryDetails;
import com.weifu.cloud.domian.dto.MaterialUnitDefDto;
import com.weifu.cloud.domian.dto.StoragePositionDto;
import com.weifu.cloud.domian.dto.WmsMaterialAttrParamsDto;
import com.weifu.cloud.domian.dto.WmsMaterialBasicDto;
import com.weifu.cloud.domian.vo.InventoryDetailsVo;
import com.weifu.cloud.domian.vo.StoragePositionVo;
import com.weifu.cloud.enums.RemoteConfigEnum;
import com.easycode.cloud.mapper.RetTaskMapper;
import com.weifu.cloud.service.*;
import com.easycode.cloud.strategy.IRetTaskStrategy;
import com.easycode.cloud.strategy.StockInSubmitStrategy;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 退货任务Service业务层处理
 *
 * @author zhanglei
 * @date 2023-03-13
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class VHRetTaskServiceImpl implements IRetTaskService {
    private static final Logger logger = LoggerFactory.getLogger(VHRetTaskServiceImpl.class);
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
    private IStockOutService iStockOutService;

    @Autowired
    private ICodeRuleService iCodeRuleService;

    @Autowired
    private IShelfTaskService shelfTaskService;

    @Autowired
    private RemoteConfigHelper remoteConfigHelper;

    @Autowired
    private IMainDataService mainDataService;

    @Autowired
    private SapInteractionService sapInteractionService;

    @Autowired
    private List<StockInSubmitStrategy> stockInSubmitStrategyList;

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
        retTask.setCreateBy(SecurityUtils.getLoginUser().getUsername());
        retTask.setUpdateTime(DateUtils.getNowDate());
        retTask.setUpdateBy(SecurityUtils.getLoginUser().getUsername());
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
    @Lock4j(keys = {"#retTaskDto.id"},acquireTimeout = 10, expire = 10000)
    public int submit(RetTaskDto retTaskDto) throws Exception {
        if (ObjectUtils.isEmpty(retTaskDto)) {
            throw new ServiceException("参数不存在!");
        }
        // 获取参数配置
        String defaultBatchNo = remoteConfigHelper.getConfig(ConfigConstant.DEFAULT_SAP_BATCH);
        if("".equals(Objects.toString(defaultBatchNo,""))){
            throw new ServiceException("配置参数SAP默认批次获取失败！");
        }
      //  retTaskDto.setProductionLot(defaultBatchNo);
        //除其他入库外 默认不质检
        retTaskDto.setIsQc(CommonYesOrNo.NO);
        StoragePositionDto storagePositionDto = new StoragePositionDto();
        storagePositionDto.setFactoryCode(SecurityUtils.getComCode());
        storagePositionDto.setLocationCode(retTaskDto.getStorageLocationCode());
        storagePositionDto.setAreaCode(retTaskDto.getAreaCode());
        storagePositionDto.setPositionNo(retTaskDto.getConfirmPosition());
        AjaxResult ajaxResult = iMainDataService.getPositionCode(retTaskDto.getMaterialNo());
        if (ajaxResult.isError()) {
            throw new ServiceException("主数据服务调用失败!");
        }
        StoragePositionVo data = JSON.parseObject(JSON.toJSONString(ajaxResult.get("data")), StoragePositionVo.class);

        if (ObjectUtils.isEmpty(data)) {
            throw new ServiceException("仓位不存在！");
        }
        StockInSubmitStrategy stockInSubmitStrategy = this.stockInSubmitStrategyList.stream().
                filter(i -> i.checkType(retTaskDto.getTaskType())).findFirst().orElse(null);

        if (Optional.ofNullable(stockInSubmitStrategy).isPresent()) {
            stockInSubmitStrategy.submit(retTaskDto,  data);
        } else {
            throw new ServiceException(String.format("未查询到%s对应类型处理逻辑",retTaskDto.getTaskType()));
        }

        return 1;
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
        MaterialUnitDefDto unitDefDto = new MaterialUnitDefDto();
        //            setFactoryCode(retTaskDto.getFactoryCode());
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
        if(unitList.size() > 1){
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
     * @param id 退货任务id
     * @return 返回退货任务+物料类型
     */
    @Override
    public RetTaskVo getRetTaskByMaterialNoType(Long id) {
        RetTask retTask = retTaskMapper.selectRetTaskById(id);
        RetTaskVo retTaskVo = new RetTaskVo();
        BeanUtils.copyProperties(retTask,retTaskVo);
        // 根据物料查询对应物料类型 并校验物料类型
        List<String> materialNoList = new ArrayList<>();
        materialNoList.add(retTaskVo.getMaterialNo());
        WmsMaterialBasicDto basicDto = new WmsMaterialBasicDto();
        basicDto.setMaterialNoList(materialNoList);
        AjaxResult ajaxResult = iMainDataService.getMaterialArrInfo(basicDto);
        if (ajaxResult.isError()) {
            throw new ServiceException(ajaxResult.get("msg").toString());
        }
        List<WmsMaterialAttrParamsDto> list = JSONObject.parseArray(ajaxResult.get("data").toString(), WmsMaterialAttrParamsDto.class);
        if (ObjectUtils.isEmpty(list)) {
            throw new ServiceException("物料属性中未查询到物料相关信息！");
        }

        Map<String, String> materialMap = list.stream().collect(Collectors.toMap(
                m -> Optional.ofNullable(m).map(WmsMaterialAttrParamsDto::getMaterialNo).orElse(""),
                m -> Optional.ofNullable(m).map(WmsMaterialAttrParamsDto::getType).orElse(""),
                (a, b) -> StringUtils.isNotBlank(a) ? a : b));
        retTaskVo.setType(materialMap.get(retTaskVo.getMaterialNo()));
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
     * 其他收货sap过账
     */
    public String stockInOtherToSap(RetTaskDto retTaskDto, StockInOtherDetail detail, StockInOther order) throws Exception {
        String defaultBatchNo = remoteConfigHelper.getConfig(ConfigConstant.DEFAULT_SAP_BATCH);
        if("".equals(Objects.toString(defaultBatchNo,""))){
            throw new ServiceException("配置参数SAP默认批次获取失败！");
        }

        SapOtherReceiveParamsDto sp = new SapOtherReceiveParamsDto();
        Date now = new Date();
        sp.setBudat(DateFormatUtils.format(now, "yyyyMMdd"));
        sp.setBldat(DateFormatUtils.format(now, "yyyyMMdd"));
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
//        itemRecord.setFieldValue("BATCH", defaultBatchNo);
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
     * 更新台账
     *
     * @param retTaskDto
     * @param storagePositionVo
     */
    public Long updateInventoryDetails(RetTaskDto retTaskDto, StoragePositionVo storagePositionVo) {
        BigDecimal qty = retTaskDto.getQty();
        //原材料
        //查询当前仓位是否已有台账存在
        InventoryDetailsVo detailsVo = new InventoryDetailsVo();
        detailsVo.setMaterialNo(retTaskDto.getMaterialNo());
//        detailsVo.setFactoryId(storagePositionVo.getFactoryId());
//        detailsVo.setFactoryCode(storagePositionVo.getFactoryCode());
//        detailsVo.setWarehouseId(storagePositionVo.getWarehouseId());
//        detailsVo.setWarehouseCode(storagePositionVo.getWarehouseCode());
//        detailsVo.setAreaId(storagePositionVo.getAreaId());
//        detailsVo.setAreaCode(storagePositionVo.getAreaCode());
//        detailsVo.setLocationId(storagePositionVo.getLocationId());
        detailsVo.setLocationCode(storagePositionVo.getLocationCode());
//        detailsVo.setPositionId(storagePositionVo.getId());
        detailsVo.setPositionNo(storagePositionVo.getPositionNo());
        detailsVo.setStockInLot(retTaskDto.getLot());
        detailsVo.setIsQc(retTaskDto.getIsQc());
        detailsVo.setIsConsign(CommonYesOrNo.NO);
        detailsVo.setIsFreeze(CommonYesOrNo.NO);
        // 排除特殊库存
        detailsVo.setStockSpecFlag(CommonYesOrNo.NO);
        AjaxResult listResult = inStoreService.list(detailsVo);
        if (listResult.isError()) {
            throw new ServiceException("查询库存台账失败！");
        }
        Long inventoryId;
        List<InventoryDetails> inventoryDetailsList = JSONObject.parseArray(listResult.get("data").toString(), InventoryDetails.class);
        if (ObjectUtils.isEmpty(inventoryDetailsList)||inventoryDetailsList.size()==0) {
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
            inventoryDetails.setFactoryId(storagePositionVo.getWarehouseId());
            inventoryDetails.setFactoryCode(storagePositionVo.getWarehouseCode());
            inventoryDetails.setFactoryName(storagePositionVo.getWarehouseName());
            inventoryDetails.setProductionLot(retTaskDto.getProductionLot());
            inventoryDetails.setInventoryQty(qty);
            inventoryDetails.setAvailableQty(qty);
            inventoryDetails.setUnit(retTaskDto.getUnit());
            inventoryDetails.setOperationUnit(retTaskDto.getUnit());
            inventoryDetails.setPreparedQty(BigDecimal.ZERO);
            inventoryDetails.setOperationPreparedQty(BigDecimal.ZERO);
            inventoryDetails.setSupplierCode(retTaskDto.getSupplierCode());
            inventoryDetails.setStockInDate(DateUtils.getNowDate());
            inventoryDetails.setStockInLot(retTaskDto.getLot());
            inventoryDetails.setIsFreeze(CommonYesOrNo.YES);
            inventoryDetails.setIsQc(CommonYesOrNo.NO);
            inventoryDetails.setIsConsign(CommonYesOrNo.NO);
            inventoryDetails.setIsReserved(CommonYesOrNo.NO);
            inventoryDetails.setWarehouseId(storagePositionVo.getWarehouseId());
            inventoryDetails.setWarehouseCode(storagePositionVo.getWarehouseCode());
            inventoryDetails.setCreateBy(SecurityUtils.getUsername());
            inventoryDetails.setCreateTime(DateUtils.getNowDate());
            inventoryDetails.setUpdateBy(SecurityUtils.getUsername());
            inventoryDetails.setUpdateTime(DateUtils.getNowDate());
            AjaxResult add = inStoreService.add(inventoryDetails);
            if (add.isError()) {
                throw new ServiceException("新增库存台账失败！");
            }
            inventoryId = Long.parseLong(add.get("data").toString());
            inventoryDetails.setId(inventoryId);
            inventoryDetailsList.add(inventoryDetails);
        } else if (inventoryDetailsList.size() == 1) {
            //修改台账
            InventoryDetails inventoryDetails = inventoryDetailsList.get(0);
            inventoryDetails.setAvailableQty(qty);
            inventoryDetails.setInventoryQty(inventoryDetails.getInventoryQty().add(qty));
            inventoryDetails.setIsFreeze(CommonYesOrNo.YES);
            inventoryDetails.setUpdateBy(SecurityUtils.getUsername());
            inventoryDetails.setUpdateTime(DateUtils.getNowDate());
            if (inStoreService.subtractInventoryDetailsAvailable(inventoryDetails).isError()) {
                throw new ServiceException("修改库存台账失败！");
            }
            inventoryId = inventoryDetails.getId();
        } else {
            throw new ServiceException("库存台账不唯一！");
        }
        InventoryDetails inventoryDetails = inventoryDetailsList.get(0);
        InventoryHistory inventoryHistory = new InventoryHistory();
        inventoryHistory.setInventoryId(inventoryDetails.getId());
        inventoryHistory.setTaskNo(retTaskDto.getTaskNo());
        inventoryHistory.setInventoryLocal(inventoryDetails.getLocationCode());
        inventoryHistory.setOperationType("2");
        inventoryHistory.setBatchNo(inventoryDetails.getStockInLot());
        inventoryHistory.setPostionNo(inventoryDetails.getPositionNo());
        inventoryHistory.setCreateBy(SecurityUtils.getUsername());
        inventoryHistory.setUpdateTime(DateUtils.getNowDate());
        inventoryHistory.setQty(retTaskDto.getQty());
        inventoryHistory.setMaterialName(inventoryDetails.getMaterialName());
        inventoryHistory.setMaterialNo(inventoryDetails.getMaterialNo());
        inventoryHistory.setOldMaterialNo(inventoryDetails.getOldMaterialNo());
        inStoreService.insertInventoryHistory(inventoryHistory);
        return inventoryId;
    }

    //物料多级单位查询 入库可用
    public MaterialUnitDefDto queryMaterialUnitDef(String materialNo, String factoryCode) {
        MaterialUnitDefDto unitDefDto = new MaterialUnitDefDto();
        unitDefDto.setFactoryCode(factoryCode);
        unitDefDto.setMaterialNo(materialNo);
        unitDefDto.setStockinEnable(CommonYesOrNo.YES);
        AjaxResult ajaxResult = mainDataService.queryMaterialUnitDef(unitDefDto);
        if (ajaxResult.isError()) {
            throw new ServiceException(ajaxResult.get("msg").toString());
        }
        List<MaterialUnitDefDto> unitList = JSONObject.parseArray(ajaxResult.get("data").toString(), MaterialUnitDefDto.class);
        if (ObjectUtils.isEmpty(unitList)) {
            throw new ServiceException(String.format("物料%s不存在入库使用单位，请维护相关数据",materialNo));
        }
        if(unitList.size() > 1){
            throw new ServiceException(String.format("数据有误，物料%s存在多条入库使用单位",materialNo));
        }
        return unitList.get(0);
    }

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
            throw new ServiceException(String.format("物料%s不存在基本单位，请维护相关数据",materialNo));
        }
        if(unitList.size() > 1){
            throw new ServiceException(String.format("数据有误，物料%s存在多条基本单位",materialNo));
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
        groupRecord.setFieldValue("DOC_DATE", DateFormatUtils.format(now, "yyyyMMdd"));
        // 凭证过账日期
        groupRecord.setFieldValue("PSTNG_DATE", DateFormatUtils.format(now, "yyyyMMdd"));
        // 用户名
        groupRecord.setFieldValue("USERNAME", SecurityUtils.getUsername());
        // 凭证抬头文本
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
}
