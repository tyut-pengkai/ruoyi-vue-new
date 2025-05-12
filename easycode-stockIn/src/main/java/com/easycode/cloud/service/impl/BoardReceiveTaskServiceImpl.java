package com.easycode.cloud.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import com.baomidou.lock.annotation.Lock4j;
import com.baomidou.lock.executor.RedisTemplateLockExecutor;
import com.easycode.cloud.domain.dto.BoardReceiveTaskDto;
import com.easycode.cloud.domain.vo.WmsMaterialBasicVo;
import com.easycode.cloud.service.IBoardReceiveTaskService;
import com.easycode.cloud.service.ITaskInfoService;
import com.soa.eis.adapter.framework.message.IMsgObject;
import com.soa.eis.adapter.framework.message.impl.GroupRecord;
import com.soa.eis.adapter.framework.message.impl.MsgObject;
import com.weifu.cloud.common.core.exception.ServiceException;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.security.utils.DictUtils;
import com.weifu.cloud.common.security.utils.SecurityUtils;
import com.weifu.cloud.constant.*;
import com.easycode.cloud.domain.TaskInfo;
import com.easycode.cloud.domain.WmsKbAndSwTaskLog;
import com.weifu.cloud.domain.dto.SapMoveLocationParamsDto;
import com.weifu.cloud.domain.vo.TaskInfoVo;
import com.weifu.cloud.domian.InventoryDetails;
import com.weifu.cloud.domian.ReserveStorage;
import com.weifu.cloud.domian.TaskLog;
import com.weifu.cloud.domian.WmsPrinterLocation;
import com.weifu.cloud.domian.dto.HiprintTemplateDTO;
import com.weifu.cloud.domian.dto.PrintTOParamsDto;
import com.weifu.cloud.domian.dto.StorageLocationDto;
import com.weifu.cloud.domian.dto.WmsBoardInfoDto;
import com.weifu.cloud.domian.vo.InventoryDetailsVo;
import com.weifu.cloud.domian.vo.StoragePositionVo;
import com.easycode.cloud.mapper.BoardReceiveTaskMapper;
import com.easycode.cloud.mapper.TaskInfoMapper;
import com.easycode.cloud.mapper.WmsKbAndSwTaskLogMapper;
import com.weifu.cloud.service.*;
import com.weifu.cloud.system.api.domain.SysDictData;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 看板领料任务Service业务层处理
 *
 * @author bcp
 * @date 2023-02-07
 */
@Service
@Transactional(rollbackFor = ServiceException.class)
public class BoardReceiveTaskServiceImpl implements IBoardReceiveTaskService {
    private static final Logger logger = LoggerFactory.getLogger(BoardReceiveTaskServiceImpl.class);

    @Autowired
    private IMainDataService mainDataService;

    @Autowired
    private ICodeRuleService iCodeRuleService;

    @Autowired
    private ITaskInfoService taskInfoService;

    @Autowired
    private RemoteEsbSendService remoteEsbSendService;

    @Autowired
    private ITaskService taskService;

    @Autowired
    private IStockOutService stockOutService;

    @Autowired
    private IPrintService printService;

    @Autowired
    private IInStoreService inStoreService;

    @Autowired
    private BoardReceiveTaskMapper boardReceiveTaskMapper;

    @Autowired
    private IStockInService stockInService;
    @Autowired
    private WmsKbAndSwTaskLogMapper wmsKbAndSwTaskLogMapper;

    @Autowired
    private TaskInfoMapper taskInfoMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private LockTemplate lockTemplate;
    /**
     * pda看板生产领用提交(1)
     * @param boardCode
     */
    @Override
    @GlobalTransactional
    public void pdaProdReceiveSubmit(String boardCode) throws Exception {
        logger.info("pda看板生产领用提交, pdaProdReceiveSubmit: {}", boardCode);
        AjaxResult boardInfoAjaxResult = mainDataService.getBoardInfo(boardCode);
        if(boardInfoAjaxResult.isError()) {
            throw new ServiceException(boardInfoAjaxResult.get("msg").toString());
        }
        WmsBoardInfoDto boardInfoDto = JSONObject.parseObject(boardInfoAjaxResult.get("data").toString(), WmsBoardInfoDto.class);

        if (ObjectUtil.isEmpty(boardInfoDto)) {
            throw new ServiceException("看板信息不存在, 看板号: " + boardCode);
        }
        if("1".equals(boardInfoDto.getEnabled())){
            throw new ServiceException("看板为禁用状态: " + boardCode);
        }
        TaskInfoVo taskInfoVo = new TaskInfoVo();
        taskInfoVo.setBoardCode(boardInfoDto.getBoardCode());
        taskInfoVo.setTaskStatusArr(new String[] {TaskStatusConstant.TASK_STATUS_NEW, TaskStatusConstant.TASK_STATUS_PART_COMPLETE});
        AjaxResult ajaxResultTaskInfo = stockInService.taskInfoList(taskInfoVo);
        List<TaskInfoVo> taskInfoList = com.alibaba.fastjson.JSON.parseArray(ajaxResultTaskInfo.get("data").toString(), TaskInfoVo.class);
        if(!ObjectUtil.isEmpty(taskInfoList)) {
            throw new ServiceException("当前看板任务未完成，请勿重复提交！");
        }

        String boardStatus = boardInfoDto.getBoardStatus();
        // 判断看板状态是否已满
        if(ObjectUtil.isNotNull(boardStatus)) {
            if(CommonYesOrNo.BOARD_FULL.equals(boardStatus)) {
                throw new ServiceException("当前看板状态为: 满，请勿重复提交！");
            }
        }
        String materialNo = boardInfoDto.getMaterialNo();
        String location =  boardInfoDto.getDefaultDeliverLocationCode();
        List<InventoryDetails> inventoryDetails = new ArrayList<>();
        final LockInfo lockInfo = lockTemplate.lock(materialNo+location,10*1000,5*1000);
        if(null==lockInfo){
            throw new ServiceException("有其他业务处理中，请稍后");
        }
        TaskInfo taskInfo = new TaskInfo();
        try {
            if (StringUtils.isBlank(boardInfoDto.getDefaultDeliverPosition())) {
                // 查询台账
                AjaxResult inventoryResult = mainDataService.getInventoryByLocationAndMaterialNo(location, materialNo);
                if (inventoryResult.isError()) {
                    throw new ServiceException(inventoryResult.get("msg").toString());
                }
                inventoryDetails = com.alibaba.fastjson.JSONObject.parseArray(JSON.toJSONString(inventoryResult.get("data")), InventoryDetails.class);
                if (CollectionUtil.isEmpty(inventoryDetails)) {
                    throw new ServiceException("该物料" + boardInfoDto.getMaterialNo() + "无可用的库存台账信息！");
                }
            } else {
                InventoryDetailsVo detailsVo = new InventoryDetailsVo();
                detailsVo.setIsFreeze(CommonYesOrNo.NO);
                detailsVo.setIsConsign(CommonYesOrNo.NO);
                detailsVo.setIsQc(CommonYesOrNo.NO);
                detailsVo.setStockSpecFlag(CommonYesOrNo.NO);
                detailsVo.setMaterialNo(boardInfoDto.getMaterialNo());
                detailsVo.setLocationCode(boardInfoDto.getDefaultDeliverLocationCode());
                detailsVo.setPositionNo(boardInfoDto.getDefaultDeliverPosition());
                AjaxResult ajaxResult = inStoreService.listAllVInventory(detailsVo);
                if (ajaxResult.isError()) {
                    throw new ServiceException("查询物料号" + boardInfoDto.getMaterialNo() + "库存失败！");
                }
                inventoryDetails = JSON.parseArray(ajaxResult.get("data").toString(), InventoryDetails.class);
                if (CollectionUtil.isEmpty(inventoryDetails)) {
                    throw new ServiceException("该物料" + boardInfoDto.getMaterialNo() + "无可用的库存台账信息！");
                }
                inventoryDetails = inventoryDetails.stream().sorted(Comparator.comparing(InventoryDetails::getId, Comparator.nullsLast(Comparator.naturalOrder())))
                        .collect(Collectors.toList());
            }
            InventoryDetails inventoryDetail = inventoryDetails.get(0);

            BigDecimal availableQty = inventoryDetail.getAvailableQty();
            if (availableQty.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ServiceException("库存台账物料可用数量不足！");
            }

            Long tenantId = SecurityUtils.getLoginUser().getSysUser().getTenantId();
            // 生成任务号
            AjaxResult ajaxResult = iCodeRuleService.getSeqWithTenantId(TaskNoTypeConstant.BOARD_RECEIVE_TASK, Convert.toStr(tenantId));
            if ("".equals(Objects.toString(ajaxResult.get("data"), "")) || ajaxResult.isError()) {
                throw new ServiceException("任务号生成失败！");
            }
            String taskNo = Convert.toStr(ajaxResult.get("data"));

            AjaxResult materialBasicInfo = mainDataService.getMaterialBasicInfo(materialNo);
            if (materialBasicInfo.isError()) {
                throw new ServiceException(materialBasicInfo.get("msg").toString());
            }

            WmsMaterialBasicVo materialInfo = JSONObject.parseObject(materialBasicInfo.get("data").toString(), WmsMaterialBasicVo.class);
            BigDecimal capacity = new BigDecimal(boardInfoDto.getCapacity());
            // 预订数量取可用库存数量和看板容量的最小值
            BigDecimal reserveCapacity = availableQty.compareTo(capacity) > 0 ? capacity : availableQty;
            BigDecimal reserveCapacityTow = availableQty.compareTo(capacity) > 0 ? capacity : availableQty;

            // 生成看板领料任务
            taskInfo.setTaskType(TaskTypeConstant.BOARD_RECEIVE_TASK);
            //taskInfo.setBoardCode(boardInfoDto.getBoardCode());
            taskInfo.setTaskStatus(TaskStatusConstant.BOARD_TASK_STATUS_NEW);
            taskInfo.setMaterialNo(materialNo);
            taskInfo.setOldMaterialNo(materialInfo.getOldMaterialNo());
            taskInfo.setMaterialName(boardInfoDto.getMaterialName());
            taskInfo.setMaterialId(materialInfo.getId());
            taskInfo.setSourceLocationCode(boardInfoDto.getDefaultDeliverLocationCode());
            taskInfo.setSourcePositionNo(inventoryDetail.getPositionNo());
            taskInfo.setLocationCode(boardInfoDto.getDefaultReceiveLocationCode());
            taskInfo.setQuantityLssued(reserveCapacity);
            taskInfo.setHandlerUserName(SecurityUtils.getUsername());
            taskInfo.setHandlerUserId(SecurityUtils.getUserId());
            String receivePosition = boardInfoDto.getDefaultReceivePosition();
            // 默认使用看板配置仓位
            if (null != receivePosition) {
                taskInfo.setPositionNo(receivePosition);
            }
            taskInfo.setBoardCode(boardInfoDto.getBoardCode());
            taskInfo.setTaskNo(taskNo);
            taskInfo.setStockInOrderNo(boardCode);
            taskInfo.setBatchNo(inventoryDetail.getStockInLot());
            taskInfo.setPrintSum(1);
            taskInfo.setPrintStatus(1);
            // taskInfo.setWaterTaskNo(taskNo);
            taskInfoService.insertTaskInfo(taskInfo);
            //新增水位日志
            insertWmsKbAndSwTaskLog(taskInfo, "");
            AjaxResult result = mainDataService.getStorageLocationByLocationCode(boardInfoDto.getDefaultDeliverLocationCode());
            if (result.isError()) {
                throw new ServiceException("获取原库存地点信息失败！");
            }
            StorageLocationDto sourceLocationDto = com.alibaba.fastjson2.JSONObject.parseObject(result.get("data").toString(), StorageLocationDto.class);

            // 判断是否需要生成翻包任务
            if (CommonYesOrNo.YES.equals(boardInfoDto.getIsTurn())) {
                if (StringUtils.isEmpty(boardInfoDto.getTurnArea())) {
                    throw new ServiceException("看板未配置翻包区！");
                }
//            location =  boardInfoDto.getTurnAreaCode();
//
//            // 查询台账
//            inventoryResult = mainDataService.getInventoryByLocationAndMaterialNo(location, materialNo);
//            if(inventoryResult.isError()) {
//                throw new ServiceException(inventoryResult.get("msg").toString());
//            }
//            inventoryDetails = com.alibaba.fastjson.JSONObject.parseArray(JSON.toJSONString(inventoryResult.get("data")), InventoryDetails.class);
//            if(CollectionUtil.isEmpty(inventoryDetails)){
//                throw new ServiceException("该物料无可用的库存台账信息！");
//            }
//
//            inventoryDetail = inventoryDetails.get(0);
//            availableQty = inventoryDetail.getAvailableQty();
                //默认仓位字典
                List<SysDictData> defPositionNo = DictUtils.getDictCache("location_default_bin");
                if (ObjectUtils.isEmpty(defPositionNo) || defPositionNo.isEmpty()) {
                    throw new ServiceException("获取默认仓位字典类型失败");
                }
                defPositionNo.forEach(e -> {
                    if (e.getDictLabel().equals(boardInfoDto.getTurnAreaCode())) {
                        boardInfoDto.setPositionNo(e.getDictValue());
                    }
                });
                TaskInfo turnoverReplenishmentTaskInfo = new TaskInfo();
                turnoverReplenishmentTaskInfo.setTaskType(TaskTypeConstant.FILP_THE_BAG);
                turnoverReplenishmentTaskInfo.setTaskStatus(TaskStatusConstant.BOARD_TASK_STATUS_NEW);
                turnoverReplenishmentTaskInfo.setMaterialNo(boardInfoDto.getMaterialNo());
                turnoverReplenishmentTaskInfo.setMaterialName(materialInfo.getMaterialName());
                turnoverReplenishmentTaskInfo.setOldMaterialNo(materialInfo.getOldMaterialNo());
                turnoverReplenishmentTaskInfo.setMaterialId(materialInfo.getId());
                turnoverReplenishmentTaskInfo.setSourceLocationCode(boardInfoDto.getDefaultDeliverLocationCode());
                turnoverReplenishmentTaskInfo.setLocationCode(boardInfoDto.getTurnAreaCode());
                // 任务号生成
                AjaxResult turnoverTaskNo = iCodeRuleService.getSeqWithTenantId(TaskNoTypeConstant.TURNOVER_REPLENISHMENT_TASK, Convert.toStr(tenantId));
                if ("".equals(Objects.toString(turnoverTaskNo.get("data"), "")) || ajaxResult.isError()) {
                    throw new ServiceException("任务号生成失败！");
                }
                turnoverReplenishmentTaskInfo.setTaskNo(Convert.toStr(turnoverTaskNo.get("data")));
                turnoverReplenishmentTaskInfo.setBoardTaskId(taskInfo.getBoardTaskId());
                turnoverReplenishmentTaskInfo.setBoardTaskNo(taskNo);
                turnoverReplenishmentTaskInfo.setBoardCode(boardInfoDto.getBoardCode());
                turnoverReplenishmentTaskInfo.setBatchNo(inventoryDetails.get(0).getStockInLot());
                turnoverReplenishmentTaskInfo.setQuantityLssued(reserveCapacityTow);
                turnoverReplenishmentTaskInfo.setHandlerUserName(SecurityUtils.getUsername());
                turnoverReplenishmentTaskInfo.setPositionNo(boardInfoDto.getPositionNo());
                turnoverReplenishmentTaskInfo.setSourcePositionNo(inventoryDetail.getPositionNo());
                turnoverReplenishmentTaskInfo.setHandlerUserId(SecurityUtils.getUserId());
                turnoverReplenishmentTaskInfo.setPrintStatus(1);
                turnoverReplenishmentTaskInfo.setPrintSum(1);
                //turnoverReplenishmentTaskInfo.setWaterTaskNo(taskNo);
                taskInfoService.insertTaskInfo(turnoverReplenishmentTaskInfo);

                taskInfo.setBoardTaskNo(turnoverReplenishmentTaskInfo.getTaskNo());
                taskInfo.setSourceLocationCode(boardInfoDto.getTurnAreaCode());
                taskInfo.setSourcePositionNo(boardInfoDto.getPositionNo());
                taskInfo.setPositionNo(boardInfoDto.getDefaultReceivePosition());
                taskInfoMapper.updateTaskInfo(taskInfo);
                //生成水位日志记录
                insertWmsKbAndSwTaskLog(turnoverReplenishmentTaskInfo, "");
                PrintTOParamsDto printTOParamsDto = new PrintTOParamsDto();
                printTOParamsDto.setFactoryCode(inventoryDetails.get(0).getFactoryCode());
                printTOParamsDto.setLocationCode(turnoverReplenishmentTaskInfo.getLocationCode());
                printTOParamsDto.setMaterialName(turnoverReplenishmentTaskInfo.getMaterialName());
                printTOParamsDto.setMaterialNo(turnoverReplenishmentTaskInfo.getMaterialNo());
                printTOParamsDto.setTaskNo(turnoverReplenishmentTaskInfo.getTaskNo());
                printTOParamsDto.setPositionNo(turnoverReplenishmentTaskInfo.getPositionNo());
                printTOParamsDto.setLot(turnoverReplenishmentTaskInfo.getBatchNo());
                printTOParamsDto.setSourcePostionNo(turnoverReplenishmentTaskInfo.getSourcePositionNo());
                printTOParamsDto.setMoveType("311");
                printTOParamsDto.setMoveQty(turnoverReplenishmentTaskInfo.getQuantityLssued());
                printTOParamsDto.setPackQty(turnoverReplenishmentTaskInfo.getQuantityLssued());
                printTOParamsDto.setUserName(SecurityUtils.getUsername());
                printTOParamsDto.setSourceStorageType(sourceLocationDto.getRemark());
                printTOParamsDto.setOldMaterialNo(taskInfo.getOldMaterialNo());
                printTOParamsDto.setTaskType("翻包任务");
                printService.printTO(printTOParamsDto);
                reserveStorage(turnoverReplenishmentTaskInfo.getTaskNo(), inventoryDetail, reserveCapacityTow, turnoverReplenishmentTaskInfo.getId());
            } else {
                // 新增预订库存
                reserveStorage(taskInfo.getTaskNo(), inventoryDetail, reserveCapacity, taskInfo.getId());
            }
        }finally {
            lockTemplate.releaseLock(lockInfo);
        }
        // 打A7标签
        PrintTOParamsDto printTOParamsDto = new PrintTOParamsDto();
        printTOParamsDto.setLocationCode(taskInfo.getLocationCode());
        printTOParamsDto.setMaterialName(taskInfo.getMaterialName());
        printTOParamsDto.setMaterialNo(taskInfo.getMaterialNo());
        printTOParamsDto.setTaskNo(taskInfo.getTaskNo());
        printTOParamsDto.setPositionNo(taskInfo.getPositionNo());
        printTOParamsDto.setLot(taskInfo.getBatchNo());
        printTOParamsDto.setSourcePostionNo(taskInfo.getSourcePositionNo());
        printTOParamsDto.setMoveQty(taskInfo.getQuantityLssued());
        printTOParamsDto.setBoardNo(taskInfoVo.getBoardCode());
        printTOParamsDto.setTaskType("看板任务");
        printTOParamsDto.setOldMaterialNo(taskInfo.getOldMaterialNo());
        printLabel(printTOParamsDto);
//        taskInfo.setSourcePositionNo(inventoryDetail.getPositionNo());
//        taskInfoService.updateTaskInfo(taskInfo);
    }

    /**
     新增水位日志
     */
    private void insertWmsKbAndSwTaskLog(TaskInfo taskInfo1,String voucherNo){
        WmsKbAndSwTaskLog wmsKbAndSwTaskLog =new WmsKbAndSwTaskLog();
        wmsKbAndSwTaskLog.setTaskId(String.valueOf(taskInfo1.getId()));
        wmsKbAndSwTaskLog.setTaskStatus(taskInfo1.getTaskStatus());
//        wmsKbAndSwTaskLog.setSapMaterialDocument();
        wmsKbAndSwTaskLog.setSapMaterialDocument(voucherNo);
        wmsKbAndSwTaskLog.setCreateBy(SecurityUtils.getUsername());
        wmsKbAndSwTaskLog.setCreateTime(DateUtils.getNowDate());
        wmsKbAndSwTaskLogMapper.insertWmsKbAndSwTaskLog(wmsKbAndSwTaskLog);
    }
    private void reserveStorage(String taskNo, InventoryDetails inventoryDetail, BigDecimal reserveCapacity,Long taskId) {
        // 新增预定库存
        ReserveStorage reserveStorage = new ReserveStorage();
        reserveStorage.setTaskId(taskId);
        reserveStorage.setBillType(TaskTypeConstant.BOARD_RECEIVE_TASK);
        reserveStorage.setTaskNo(taskNo);
        reserveStorage.setInventoryId(inventoryDetail.getId());
        reserveStorage.setReserveQty(reserveCapacity);
        reserveStorage.setOperationReserveQty(reserveCapacity);
        reserveStorage.setUnit(inventoryDetail.getUnit());
        reserveStorage.setOperationUnit(inventoryDetail.getUnit());
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
        inStoreService.createReserveStorage(reserveStorage);
    }

    private int printLabel(PrintTOParamsDto printTOParamsDto) {
        AjaxResult printerByLocationAjaxResult = printService.findPrinterByLocation(printTOParamsDto.getLocationCode());

        if (printerByLocationAjaxResult.isError()) {
            throw new ServiceException(String.format("打印失败,原因：%s", printerByLocationAjaxResult.get("msg").toString()));
        }

        List<WmsPrinterLocation> wmsPrinterLocationList = JSON.parseArray(JSON.toJSONString(printerByLocationAjaxResult.get("data")), WmsPrinterLocation.class);

        String printerName = null;
        if (org.springframework.util.CollectionUtils.isEmpty(wmsPrinterLocationList)) {
            printerName = "";
        } else {
            printerName = wmsPrinterLocationList.get(0).getPrintName();
        }
        // 打印TO单
        String templateCode = "A7_BOARD_NEW";
        HiprintTemplateDTO dto = new HiprintTemplateDTO();
        dto.setTemplateNo(templateCode);
        dto.setPrinterName(printerName);
        dto.setTitle("T1标签打印");
        JSONArray jsonArray = new JSONArray();

        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();

        jsonObject.put("taskType", printTOParamsDto.getTaskType());
        // 目标存储类型
        jsonObject.put("defaultReceivePosition", printTOParamsDto.getPositionNo());
        //源发地仓位
        jsonObject.put("sourcePostionNo", printTOParamsDto.getSourcePostionNo());
        // 存储类型
        jsonObject.put("taskNo", printTOParamsDto.getTaskNo());
        // 移动类型
        jsonObject.put("moveType", printTOParamsDto.getMoveType());
        // 创建日期
        jsonObject.put("date", DateUtils.parseDateToStr("yyyy-MM-dd",DateUtils.getNowDate()));
        // 批次
        jsonObject.put("lot", printTOParamsDto.getLot());
        // 看板号
        jsonObject.put("boardNo", printTOParamsDto.getBoardNo());
        // 移动数量
        jsonObject.put("qty", printTOParamsDto.getMoveQty());
        // qrCode
        jsonObject.put("barCode", "O%T" + printTOParamsDto.getTaskNo() + "%M" + printTOParamsDto.getMaterialNo());
        // 物料号
        jsonObject.put("materialNo", printTOParamsDto.getMaterialNo());
        // 物料名称
        jsonObject.put("materialName", printTOParamsDto.getMaterialName());
        // 旧物料名称
        jsonObject.put("oldMaterialNo", printTOParamsDto.getOldMaterialNo());
        jsonArray.add(jsonObject);
        dto.setDataArray(jsonArray);
        JSONObject page = new JSONObject();
        page.put("height",108*1000);
        page.put("width",78*1000);
        dto.setCustom(false);
        dto.setCustomSize(page);
        AjaxResult ajaxResult1 = printService.printByTemplate(dto);

        if (ajaxResult1.isError()) {
            throw new ServiceException(String.format("打印失败,原因：%s", ajaxResult1.get("msg").toString()));
        }
        return HttpStatus.SC_OK;
    }

    public int printInspectTask(PrintTOParamsDto printTOParamsDto) {
        AjaxResult printerByLocationAjaxResult = printService.findPrinterByLocation(printTOParamsDto.getLocationCode());

        if (printerByLocationAjaxResult.isError()) {
            throw new ServiceException(String.format("打印失败,原因：%s", printerByLocationAjaxResult.get("msg").toString()));
        }

        List<WmsPrinterLocation> wmsPrinterLocationList = JSON.parseArray(JSON.toJSONString(printerByLocationAjaxResult.get("data")), WmsPrinterLocation.class);

        String printerName = null;
        if (org.springframework.util.CollectionUtils.isEmpty(wmsPrinterLocationList)) {
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

        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();

        jsonObject.put("taskType", "翻包任务");
        // 目标存储类型
        jsonObject.put("postionNo", printTOParamsDto.getPositionNo());
        // 工厂或库存地点
        jsonObject.put("facotryOrLocation", String.format("%s/%s", printTOParamsDto.getFactoryCode(), printTOParamsDto.getLocationCode()));
        //源发地仓位
        jsonObject.put("sourcePostionNo", printTOParamsDto.getSourcePostionNo());
        // 存储类型
        jsonObject.put("taskNo", printTOParamsDto.getTaskNo());
        // 移动类型
        jsonObject.put("moveType", printTOParamsDto.getMoveType());
        // 创建日期
        jsonObject.put("createDate", DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss",DateUtils.getNowDate()));
        //时间
        jsonObject.put("createTime", DateUtils.parseDateToStr("HH:mm:ss",DateUtils.getNowDate()));
        // 批次
        jsonObject.put("lot", printTOParamsDto.getLot());
        // 用户名
        jsonObject.put("userName", printTOParamsDto.getUserName());
        // 货架寿命
        jsonObject.put("age", "");
        // 源发货库存地点
        jsonObject.put("oriiginLoction", printTOParamsDto.getLocationCode());
        // 移动数量
        jsonObject.put("moveQty", printTOParamsDto.getMoveQty());
        // 包装数量
        jsonObject.put("packQty", printTOParamsDto.getPackQty());
        // qrCode
        jsonObject.put("qrCode", "O%T" + printTOParamsDto.getTaskNo() + "%M" + printTOParamsDto.getMaterialNo());
        // 供应商批次
        jsonObject.put("vendorBatchNo", "");
        // 物料号
        jsonObject.put("materialNo", printTOParamsDto.getMaterialNo());
        // 物料名称
        jsonObject.put("materialName", printTOParamsDto.getMaterialName());
        // 源发地仓储类型
        jsonObject.put("sourceStorageType", printTOParamsDto.getSourceStorageType());
        jsonArray.add(jsonObject);
        dto.setDataArray(jsonArray);

        AjaxResult ajaxResult1 = printService.printByTemplate(dto);

        if (ajaxResult1.isError()) {
            throw new ServiceException(String.format("打印失败,原因：%s", ajaxResult1.get("msg").toString()));
        }
        return HttpStatus.SC_OK;
    }

    /**
     * pda看板领料提交(2)
     * @param taskNo
     */
    @Override
    public void pdaProdPickUpSubmit(String taskNo) {
        // 扫描A7标签取走实物
        // 任务状态改为进行中
        TaskInfoVo taskInfoVo = new TaskInfoVo();
        taskInfoVo.setTaskNo(taskNo);
        List<TaskInfoVo> taskInfoList = taskInfoService.listBoardTask(taskInfoVo);

        if(ObjectUtil.isEmpty(taskInfoList)) {
            logger.error("看板任务不存在！看板号: {}", taskNo);
            throw new ServiceException("看板任务不存在！");
        }

        TaskInfoVo taskInfo = taskInfoList.get(0);
        if(ObjectUtil.isNotEmpty(taskInfo)) {
            if(TaskStatusConstant.BOARD_TASK_STATUS_PART_COMPLETE.equals(taskInfo.getTaskStatus())) {
                throw new ServiceException("看板任务已取货，请勿重复操作！");
            }
        }

        // 查询翻包任务是否完成
        TaskInfoVo param = new TaskInfoVo();
        param.setTaskNo(taskInfo.getBoardTaskNo());
        param.setTaskType(TaskTypeConstant.FILP_THE_BAG);
        List<TaskInfoVo> taskInfoVos = taskInfoService.listBoardTask(param);
        if(CollectionUtil.isNotEmpty(taskInfoVos)){
            for (TaskInfoVo bagTaskInfo : taskInfoVos) {
                String taskStatus = bagTaskInfo.getTaskStatus();
                if(!TaskStatusConstant.TASK_STATUS_COMPLETE.equals(taskStatus)){
                    throw new ServiceException("翻包任务未完成！");
                }
            }
        }
        taskInfo.setTaskStatus(TaskStatusConstant.BOARD_TASK_STATUS_PART_COMPLETE);
        taskInfoService.updateBoardTaskInfo(taskInfo);

        AjaxResult boardInfoAjaxResult = mainDataService.getBoardInfo(taskInfo.getBoardCode());
        if(boardInfoAjaxResult.isSuccess()) {
            WmsBoardInfoDto boardInfoDto = JSONObject.parseObject(boardInfoAjaxResult.get("data").toString(), WmsBoardInfoDto.class);
            // 看板状态置“满”
            boardInfoDto.setBoardStatus(CommonYesOrNo.BOARD_FULL);
            mainDataService.updateBoardInfo(boardInfoDto);
        }
        //生成水位日志记录
        insertWmsKbAndSwTaskLog(taskInfo,"");
    }

    /**
     * pda看板物流确认提交(3)
     * @param taskNo
     */
    @Override
    @GlobalTransactional
    public void pdaBoardSubmit(String taskNo) throws Exception {
        // 物料扫A7确认领料
        // sap过账
        // 看板变为空
        // 看板领料任务完成
        TaskInfoVo taskInfoVo = new TaskInfoVo();
        taskInfoVo.setTaskNo(taskNo);
        List<TaskInfoVo> taskInfoList = taskInfoService.listBoardTask(taskInfoVo);

        if(ObjectUtil.isEmpty(taskInfoList)) {
            throw new ServiceException("看板任务不存在！");
        }

        TaskInfoVo taskInfo = taskInfoList.get(0);

        if (TaskStatusConstant.BOARD_TASK_STATUS_NEW.equals(taskInfo.getTaskStatus())) {
            throw new ServiceException("请先完成生产取货！");
        }
        else if(TaskStatusConstant.BOARD_TASK_STATUS_COMPLETE.equals(taskInfo.getTaskStatus())) {
            throw new ServiceException("请勿重复操作！");
        }

        // 查询看板信息
        AjaxResult boardInfoAjaxResult = mainDataService.getBoardInfo(taskInfo.getBoardCode());
        if(boardInfoAjaxResult.isError()) {
            throw new ServiceException(boardInfoAjaxResult.get("msg").toString());
        }
        WmsBoardInfoDto boardInfoDto = JSONObject.parseObject(boardInfoAjaxResult.get("data").toString(), WmsBoardInfoDto.class);

        // sap过账
        String sourceLocation = boardInfoDto.getDefaultDeliverLocationCode();
        if (CommonYesOrNo.YES.equals(boardInfoDto.getIsTurn())) {
            sourceLocation = boardInfoDto.getTurnAreaCode();
        }
        AjaxResult location = mainDataService.getStorageLocationByLocationCode(sourceLocation);
        if(location.isError()) {
            throw new ServiceException("获取原库存地点信息失败！");
        }
        StorageLocationDto sourceLocationDto = com.alibaba.fastjson2.JSONObject.parseObject(location.get("data").toString(), StorageLocationDto.class);

        AjaxResult targetLocation = mainDataService.getStorageLocationByLocationCode(boardInfoDto.getDefaultReceiveLocationCode());
        if(location.isError()) {
            throw new ServiceException("获取目的库存地点信息失败！");
        }
        StorageLocationDto targetLocationDto = com.alibaba.fastjson2.JSONObject.parseObject(targetLocation.get("data").toString(), StorageLocationDto.class);

        // sap过账
        if (ObjectUtils.isEmpty(taskInfo.getQuantityLssued())) {
            throw new ServiceException("领用数量为空，SAP过账异常，请检查任务信息！");
        }
        SapMoveLocationParamsDto paramsDto = new SapMoveLocationParamsDto();
        paramsDto.setBudat(DateUtils.getDate());
        paramsDto.setBldat(DateUtils.getDate());
        paramsDto.setPlant(sourceLocationDto.getFactoryCode());
        paramsDto.setMaterial(taskInfo.getMaterialNo());
        paramsDto.setStgeLoc(sourceLocationDto.getLocationCode());
        paramsDto.setMoveType("311");
        paramsDto.setVendor(taskInfo.getVendorCode());
        paramsDto.setBatch(taskInfo.getBatchNo());
        paramsDto.setMovePlant(targetLocationDto.getFactoryCode());
        paramsDto.setMoveMat(taskInfo.getMaterialNo());
        paramsDto.setMoveStloc(targetLocationDto.getLocationCode());
        paramsDto.setEntryQnt(taskInfo.getQuantityLssued().toString());
        paramsDto.setEntryUom(boardInfoDto.getUnit());
        paramsDto.setItemText(taskInfo.getTaskNo());
        taskInfo.setTaskStatus(TaskStatusConstant.BOARD_TASK_STATUS_COMPLETE);
        taskInfo.setFinishTime(new Date());
        taskInfoService.updateBoardTaskInfo(taskInfo);
        //查询预定库存表并删除
        ReserveStorage reserveStorage = new ReserveStorage();
        reserveStorage.setTaskNo(taskNo);
        AjaxResult revertResult = inStoreService.getReserveStorage(reserveStorage);
        if (revertResult.isError()) {
            throw new ServiceException("查询预存表失败！");
        }
        List<ReserveStorage> reserveStorages = com.alibaba.fastjson.JSON.parseArray(com.alibaba.fastjson.JSON.toJSONString(revertResult.get("rows")), ReserveStorage.class);
        if (reserveStorages == null || reserveStorages.isEmpty()) {
            logger.error("查询预存表失败,预测台账数据为空！");
        }
        String sourcePositionNo = taskInfo.getSourcePositionNo();
        if (!ObjectUtils.isEmpty(reserveStorages)) {
            //查询库存台账表并减去库存
            for (ReserveStorage reserve : reserveStorages) {
                InventoryDetails inventoryDetailsVo = new InventoryDetails();
                inventoryDetailsVo.setId(reserve.getInventoryId());
                AjaxResult ajaxResult = inStoreService.selectWmsInventoryDetails2(inventoryDetailsVo);
                if (ajaxResult.isError()) {
                    throw new ServiceException("查询库存台账表失败！");
                }
                List<InventoryDetails> inventoryDetailsList = com.alibaba.fastjson.JSON.parseArray(com.alibaba.fastjson2.JSON.toJSONString(ajaxResult.get("data")), InventoryDetails.class);
                if (inventoryDetailsList == null || inventoryDetailsList.isEmpty()) {
                    throw new ServiceException("查询库存台账表失败！");
                }
                InventoryDetails inventoryDetails = inventoryDetailsList.get(0);
                inventoryDetails.setAvailableQty(inventoryDetails.getAvailableQty().subtract(reserve.getReserveQty()));
                inventoryDetails.setInventoryQty(inventoryDetails.getInventoryQty().subtract(reserve.getReserveQty()));
                inventoryDetails.setUpdateTime(DateUtils.getNowDate());
                inventoryDetails.setUpdateBy(SecurityUtils.getLoginUser().getUsername());
                inStoreService.updateInventory(inventoryDetails);
                // 记录移动日志源仓位信息
                sourcePositionNo = inventoryDetails.getPositionNo();
                updateTargetInventoryDetails(boardInfoDto, targetLocationDto, taskInfo,inventoryDetails);
            }

            List<Long> reserveIds = reserveStorages.stream().map(ReserveStorage::getId).collect(Collectors.toList());
            inStoreService.removeByReserveIds(reserveIds);

            // 更新库存台账

            // 看板状态设置为空
            AjaxResult result = mainDataService.getBoardInfo(taskInfo.getBoardCode());
            if(result.isSuccess()) {
                WmsBoardInfoDto boardInfo = JSONObject.parseObject(result.get("data").toString(), WmsBoardInfoDto.class);
                boardInfo.setBoardStatus(CommonYesOrNo.BOARD_EMPTY);
                mainDataService.updateBoardInfo(boardInfo);
            }
            String voucherNo = moveLocationSap(paramsDto);
            //生成日志记录
            insertWmsKbAndSwTaskLog(taskInfo,voucherNo);
            // 记录移动日志
            addTaskLog(taskInfo, boardInfoDto, sourceLocationDto, sourcePositionNo, voucherNo,targetLocationDto);

            // 看板领料任务状态设置为Y
            taskInfo.setTaskFlag("Y");
            taskInfoService.updateBoardTaskInfo(taskInfo);
        }
    }

    private void updateTargetInventoryDetails(WmsBoardInfoDto boardInfoDto, StorageLocationDto targetLocationDto, TaskInfoVo taskInfo,InventoryDetails details) {
        //查询库存台账表并增加目的库存
        InventoryDetails inventoryDetails = new InventoryDetails();
        inventoryDetails.setMaterialNo(boardInfoDto.getMaterialNo());
        inventoryDetails.setMaterialName(boardInfoDto.getMaterialName());
        inventoryDetails.setOldMaterialNo(boardInfoDto.getOldMaterialNo());
        inventoryDetails.setPositionNo(boardInfoDto.getDefaultReceivePosition());
        inventoryDetails.setLocationCode(boardInfoDto.getDefaultReceiveLocationCode());
        inventoryDetails.setFactoryId(targetLocationDto.getFactoryId());
        inventoryDetails.setFactoryCode(targetLocationDto.getFactoryCode());
        inventoryDetails.setFactoryName(targetLocationDto.getFactoryName());
        inventoryDetails.setInventoryQty(taskInfo.getQuantityLssued());
        inventoryDetails.setAvailableQty(taskInfo.getQuantityLssued());
        inventoryDetails.setOperationUnit(boardInfoDto.getUnit());
        inventoryDetails.setUnit(boardInfoDto.getUnit());
        inventoryDetails.setPreparedQty(BigDecimal.ZERO);
        inventoryDetails.setOperationPreparedQty(BigDecimal.ZERO);
        inventoryDetails.setStockInDate(DateUtils.getNowDate());
        inventoryDetails.setContainerNo(taskInfo.getContainerNo());
        inventoryDetails.setStockInLot(taskInfo.getBatchNo());
        inventoryDetails.setIsQc(CommonYesOrNo.NO);
        inventoryDetails.setIsConsign(CommonYesOrNo.NO);
        inventoryDetails.setIsFreeze(CommonYesOrNo.NO);
        inventoryDetails.setStockSpecFlag(CommonYesOrNo.NO);
        inventoryDetails.setCreateBy(SecurityUtils.getUsername());
        inventoryDetails.setCreateTime(DateUtils.getNowDate());
        inventoryDetails.setUpdateBy(SecurityUtils.getUsername());
        inventoryDetails.setUpdateTime(DateUtils.getNowDate());
        inventoryDetails.setTenantId(taskInfo.getTenantId());
        inventoryDetails.setTaskNo(taskInfo.getTaskNo());
        inventoryDetails.setStockInDate(details.getStockInDate());
        inventoryDetails.setExpiryDate(details.getExpiryDate());
        inventoryDetails.setSupplierCode(details.getSupplierCode());
        inventoryDetails.setSupplierName(details.getSupplierName());
        inStoreService.add(inventoryDetails);
    }

    private void addTaskLog(TaskInfoVo taskInfo, WmsBoardInfoDto boardInfoDto, StorageLocationDto sourceLocationDto, String sourcePositionNo, String voucherNo, StorageLocationDto targetLocationDto) {
        // 生成执行记录
        TaskLog taskLog = new TaskLog();
        taskLog.setTaskNo(taskInfo.getTaskNo());
        taskLog.setTaskType(taskInfo.getTaskType());
        taskLog.setMaterialNo(taskInfo.getMaterialNo());
        taskLog.setOldMaterialNo(taskInfo.getOldMaterialNo());
        taskLog.setMaterialName(boardInfoDto.getMaterialName());
        taskLog.setQty(taskInfo.getQuantityLssued());
        taskLog.setOperationQty(taskInfo.getQuantityLssued());
        taskLog.setUnit(taskInfo.getUnit());
        taskLog.setOperationUnit(taskInfo.getOperationUnit());
        taskLog.setFactoryCode(sourceLocationDto.getFactoryCode());
        taskLog.setLocationCode(sourceLocationDto.getLocationCode());
        taskLog.setPositionCode(sourcePositionNo);
        taskLog.setTargetLocationCode(boardInfoDto.getDefaultReceiveLocationCode());
        taskLog.setTargetPositionCode(boardInfoDto.getDefaultReceivePosition());
        taskLog.setTargetFactoryCode(targetLocationDto.getFactoryCode());
        taskLog.setOrderType(TaskLogTypeConstant.BOARD_TASK_RECEIVE);
        taskLog.setLineNo("");
        taskLog.setIsQc(taskInfo.getIsQc());
        taskLog.setIsFreeze(CommonYesOrNo.NO);
        taskLog.setIsConsign(CommonYesOrNo.NO);
        taskLog.setSupplierCode(taskInfo.getVendorCode());
        taskLog.setTenantId(taskInfo.getTenantId());
        taskLog.setLot(taskInfo.getBatchNo());
        taskLog.setMaterialCertificate(voucherNo);

        if (taskService.add(taskLog).isError()) {
            throw new ServiceException("新增任务记录失败！");
        }
    }

    /**
     * 是否缺料
     * @param materialNo
     * @return
     */
    private Boolean isExistMaterial(String materialNo) {
        InventoryDetails inventoryDetails = new InventoryDetails();
        inventoryDetails.setIsQc("Q");
        inventoryDetails.setIsFreeze("0");
        inventoryDetails.setIsConsign("0");
        inventoryDetails.setIsReserved("0");
        inventoryDetails.setMaterialNo(materialNo);
        AjaxResult ajaxResult = inStoreService.selectWmsInventoryDetails2(inventoryDetails);

        if (ajaxResult.isError()) {
            throw new ServiceException("查询库存台账失败！");
        }
        List<InventoryDetails> inventoryDetails1 = JSON.parseArray(JSON.toJSONString(ajaxResult.get("data")), InventoryDetails.class);
        BigDecimal total = BigDecimal.ZERO;

        for (InventoryDetails details : inventoryDetails1) {
            total = total.add(details.getAvailableQty());
        }

        return total.intValue() < 0;
    }

    /**
     * 看板领用列表
     * @param boardReceiveTaskDto
     * @return
     */
    @Override
    public List<BoardReceiveTaskDto> selectBoardReceiveTaskList(BoardReceiveTaskDto boardReceiveTaskDto) {
        return boardReceiveTaskMapper.selectBoardReceiveTaskList(boardReceiveTaskDto);
    }

    /**
     * sap移动库位
     * @param sapMoveLocationParamsDto
     * @return
     * @throws Exception
     */
    public String moveLocationSap(SapMoveLocationParamsDto sapMoveLocationParamsDto) throws Exception {
        String voucherNo = "";
        IMsgObject reqMo = new MsgObject(IMsgObject.MOType.initSR);
        List<GroupRecord> list = new ArrayList<>();
        GroupRecord groupRecord = new GroupRecord();
        groupRecord.setName("IS_HEAD");
        // 过账日期
        groupRecord.setFieldValue("BUDAT", sapMoveLocationParamsDto.getBudat());
        // 凭证日期
        groupRecord.setFieldValue("BLDAT", sapMoveLocationParamsDto.getBldat());
        // 用户名
        groupRecord.setFieldValue("UNAME", sapMoveLocationParamsDto.getuName());
        // 凭证抬头文本
        groupRecord.setFieldValue("BKTXT", sapMoveLocationParamsDto.getBktxt());
        GroupRecord itemRecord = new GroupRecord();
        itemRecord.setName("IT_ITEM");
        // 工厂
        itemRecord.setFieldValue("PLANT", sapMoveLocationParamsDto.getPlant());
        // 物料编号
        itemRecord.setFieldValue("MATERIAL", sapMoveLocationParamsDto.getMaterial());
        // 存储地点
        itemRecord.setFieldValue("STGE_LOC", sapMoveLocationParamsDto.getStgeLoc());
        // 移动类型
        itemRecord.setFieldValue("MOVE_TYPE", sapMoveLocationParamsDto.getMoveType());
        // 批次
        itemRecord.setFieldValue("BATCH", sapMoveLocationParamsDto.getBatch());
        // 特殊库存标识
        itemRecord.setFieldValue("SPEC_STOCK", sapMoveLocationParamsDto.getSpecStock());
        // 供应商账号
        itemRecord.setFieldValue("VENDOR", sapMoveLocationParamsDto.getVendor());
        // 收货工厂/发货工厂
        itemRecord.setFieldValue("MOVE_PLANT", sapMoveLocationParamsDto.getMovePlant());
        // 接收/发出物料
        itemRecord.setFieldValue("MOVE_MAT", sapMoveLocationParamsDto.getMoveMat());
        // 收货/发货库存地点
        itemRecord.setFieldValue("MOVE_STLOC", sapMoveLocationParamsDto.getMoveStloc());
        // 收货/发货批量
        itemRecord.setFieldValue("MOVE_BATCH", sapMoveLocationParamsDto.getMoveBatch());
        // 数量
        itemRecord.setFieldValue("ENTRY_QNT", sapMoveLocationParamsDto.getEntryQnt());
        // 单位
        itemRecord.setFieldValue("ENTRY_UOM", sapMoveLocationParamsDto.getEntryUom());
        // 计量单位的 ISO 代码
        itemRecord.setFieldValue("ENTRY_UOM_ISO", sapMoveLocationParamsDto.getEntryUomIso());
        // 标识: 未创建转移要求
        itemRecord.setFieldValue("NO_TRANSFER_REQ", sapMoveLocationParamsDto.getNoTransferReq());
        // 工作分解结构元素 (WBS 元素)
        itemRecord.setFieldValue("WBS_ELEM", sapMoveLocationParamsDto.getWbsElem());
        // 科目分配的网络号
        itemRecord.setFieldValue("NETWORK", sapMoveLocationParamsDto.getNetWork());
        // 项目文本(任务号)
        itemRecord.setFieldValue("ITEM_TEXT", sapMoveLocationParamsDto.getItemText());
        // 销售订单数
        itemRecord.setFieldValue("SALES_ORD", sapMoveLocationParamsDto.getSalesOrd());
        // 销售订单中的项目编号
        itemRecord.setFieldValue("S_ORD_ITEM", sapMoveLocationParamsDto.getsOrdItem());
        // 评估销售定单库存的销售定单号
        itemRecord.setFieldValue("VAL_SALES_ORD", sapMoveLocationParamsDto.getValSalesOrd());
        list.add(groupRecord);
        list.add(itemRecord);
        reqMo.setReqGroupRecord(list);
        byte[] result = remoteEsbSendService.sendToEsb(EsbSendSapConstant.Z_IWMS_ALLOCATE, reqMo.toString().getBytes());
        // 转化为标准结果
        MsgObject resMo = new MsgObject(result, IMsgObject.MOType.initSR);
        String errorMsg = resMo.getResValue("ERRORMSG");
        String flag = resMo.getResValue("FLAG");
        if (!SapReqOrResConstant.SAP_RETURN_TYPE.equals(flag)) {
            logger.error("调拨过账失败!" + errorMsg);
            throw new ServiceException("调拨过账失败!" + errorMsg);
        }
        // 凭证号
        voucherNo = resMo.getResValue("MBLNR");
        return voucherNo;
    }

    public BoardReceiveTaskDto getBoardReceiveTask(String taskNo) {
        return boardReceiveTaskMapper.getBoardReceiveTask(taskNo);
    }

    /***
     *  批量确认
     * @param ids
     * @return
     */
    @Override
    public int selectionChangeIds(Long[] ids) {
        TaskInfoVo taskInf = new TaskInfoVo();
        taskInf.setTaskStatus(TaskStatusConstant.BOARD_TASK_STATUS_NEW);
        taskInf.setTaskType(TaskTypeConstant.FILP_THE_BAG);
        List<TaskInfoVo> taskInfoVos = taskInfoMapper.selectBoardTaskList(taskInf);
        Map<String,TaskInfoVo>  boardTaskMap = taskInfoVos.stream().collect(Collectors.toMap(TaskInfoVo::getTaskNo,p -> p));
        List<WmsKbAndSwTaskLog> wmsKbAndSwTaskLogs = new ArrayList<>();
        for (Long id : ids) {
            TaskInfo taskInfo  =  taskInfoService.selectTaskInfoById(id);
            if(taskInfo!=null){
                if(taskInfo.getTaskStatus().equals(TaskStatusConstant.BOARD_TASK_STATUS_PART_COMPLETE)){
                    throw new ServiceException(String.format("该任务编号 %s 已经在进行中！", taskInfo.getTaskNo()));
                }
                if(taskInfo.getTaskStatus().equals(TaskStatusConstant.BOARD_TASK_STATUS_COMPLETE)){
                    throw new ServiceException(String.format("该任务编号 %s 该任务已经完成！", taskInfo.getTaskNo()));
                }
                if(!ObjectUtils.isEmpty(boardTaskMap.get(taskInfo.getBoardTaskNo()))){
                    throw new ServiceException(String.format("该任务编号 %s 存在未完成的翻包任务！", taskInfo.getTaskNo()));
                }
                if(taskInfo.getTaskType().equals(TaskTypeConstant.BOARD_RECEIVE_TASK)){
                    AjaxResult boardInfoAjaxResult = mainDataService.getBoardInfo(taskInfo.getBoardCode());
                    if(boardInfoAjaxResult.isSuccess()) {
                        WmsBoardInfoDto boardInfoDto = JSONObject.parseObject(boardInfoAjaxResult.get("data").toString(), WmsBoardInfoDto.class);
                        // 看板状态置“满”
                        boardInfoDto.setBoardStatus(CommonYesOrNo.BOARD_FULL);
                        mainDataService.updateBoardInfo(boardInfoDto);
                    }
                }
                WmsKbAndSwTaskLog wmsKbAndSwTaskLog =new WmsKbAndSwTaskLog();
                wmsKbAndSwTaskLog.setTaskId(String.valueOf(taskInfo.getId()));
                wmsKbAndSwTaskLog.setTaskStatus(taskInfo.getTaskStatus());
                wmsKbAndSwTaskLog.setCreateBy(SecurityUtils.getUsername());
                wmsKbAndSwTaskLog.setCreateTime(DateUtils.getNowDate());
                wmsKbAndSwTaskLogs.add(wmsKbAndSwTaskLog);
                taskInfo.setUpdateBy(SecurityUtils.getUsername());
                taskInfo.setUpdateTime(DateUtils.getNowDate());
                taskInfo.setTaskStatus(TaskStatusConstant.TASK_STATUS_PART_COMPLETE);
                taskInfoMapper.updateTaskInfo(taskInfo);
            }
        }
        if(!CollectionUtils.isEmpty(wmsKbAndSwTaskLogs)){
            wmsKbAndSwTaskLogMapper.batchInsertWmsKbAndSwTaskLog(wmsKbAndSwTaskLogs);
        }
        return 1;
    }

    @Override
    public int printTask(BoardReceiveTaskDto boardReceiveTaskDto) {
        if("31".equals(boardReceiveTaskDto.getTaskType())||"26".equals(boardReceiveTaskDto.getTaskType())){
            PrintTOParamsDto printTOParamsDto = new PrintTOParamsDto();
            printTOParamsDto.setLocationCode(boardReceiveTaskDto.getDefaultReceiveLocation());
            printTOParamsDto.setMaterialName(boardReceiveTaskDto.getMaterialName());
            printTOParamsDto.setMaterialNo(boardReceiveTaskDto.getMaterialNo());
            printTOParamsDto.setTaskNo(boardReceiveTaskDto.getTaskNo());
            printTOParamsDto.setPositionNo(boardReceiveTaskDto.getPositionNo());
            printTOParamsDto.setLot(boardReceiveTaskDto.getBatchNo());
            printTOParamsDto.setSourcePostionNo(boardReceiveTaskDto.getSourcePositionNo());
            printTOParamsDto.setMoveQty(boardReceiveTaskDto.getQuantityLssued());
            printTOParamsDto.setBoardNo(boardReceiveTaskDto.getPositionNo());
            printTOParamsDto.setTaskType("26".equals(boardReceiveTaskDto.getTaskType())?"看板任务":"水位任务");
            printTOParamsDto.setOldMaterialNo(boardReceiveTaskDto.getOldMaterialNo());
            printLabel(printTOParamsDto);
        }
        if("27".equals(boardReceiveTaskDto.getTaskType())){
            AjaxResult result = mainDataService.getStorageLocationByLocationCode(boardReceiveTaskDto.getDefaultDeliverLocation());
            if(result.isError()) {
                throw new ServiceException("获取原库存地点信息失败！");
            }
            StorageLocationDto sourceLocationDto = com.alibaba.fastjson2.JSONObject.parseObject(result.get("data").toString(), StorageLocationDto.class);

            PrintTOParamsDto printTOParamsDto = new PrintTOParamsDto();
            printTOParamsDto.setFactoryCode(sourceLocationDto.getFactoryCode());
            printTOParamsDto.setLocationCode(boardReceiveTaskDto.getDefaultReceiveLocation());
            printTOParamsDto.setMaterialName(boardReceiveTaskDto.getMaterialName());
            printTOParamsDto.setMaterialNo(boardReceiveTaskDto.getMaterialNo());
            printTOParamsDto.setTaskNo(boardReceiveTaskDto.getTaskNo());
            printTOParamsDto.setPositionNo(boardReceiveTaskDto.getPositionNo());
            printTOParamsDto.setLot(boardReceiveTaskDto.getBatchNo());
            printTOParamsDto.setSourcePostionNo(boardReceiveTaskDto.getSourcePositionNo());
            printTOParamsDto.setMoveType("311");
            printTOParamsDto.setMoveQty(boardReceiveTaskDto.getQuantityLssued());
            printTOParamsDto.setPackQty(boardReceiveTaskDto.getQuantityLssued());
            printTOParamsDto.setUserName(SecurityUtils.getUsername());
            printTOParamsDto.setSourceStorageType(sourceLocationDto.getRemark());
            printTOParamsDto.setTaskType("翻包任务");
            printTOParamsDto.setOldMaterialNo(boardReceiveTaskDto.getOldMaterialNo());
            printService.printTO(printTOParamsDto);
        }
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setId(boardReceiveTaskDto.getId());
        taskInfo.setPrintSum(boardReceiveTaskDto.getPrintSum()+1);
        taskInfo.setPrintStatus(1);
        taskInfoMapper.updateTaskInfo(taskInfo);
        return 1;
    }

}