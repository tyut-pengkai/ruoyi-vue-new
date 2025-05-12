package com.easycode.cloud.service.impl.vh;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import com.baomidou.lock.annotation.Lock4j;
import com.easycode.cloud.domain.*;
import com.easycode.cloud.domain.dto.TaskInfoDto;
import com.easycode.cloud.mapper.*;
import com.easycode.cloud.service.*;
import com.soa.eis.adapter.framework.message.IMsgObject;
import com.soa.eis.adapter.framework.message.impl.GroupRecord;
import com.soa.eis.adapter.framework.message.impl.MsgObject;
import com.weifu.cloud.common.core.exception.ServiceException;
import com.weifu.cloud.common.core.text.Convert;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.weifu.cloud.common.core.utils.SpringUtils;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.security.utils.DictUtils;
import com.weifu.cloud.common.security.utils.SecurityUtils;
import com.weifu.cloud.constant.*;
import com.weifu.cloud.domain.*;
import com.weifu.cloud.domain.dto.*;
import com.easycode.cloud.domain.vo.StockInDetailPrintVo;
import com.weifu.cloud.domain.vo.TaskInfoVo;
import com.easycode.cloud.domain.vo.WmsMaterialBasicVo;
import com.weifu.cloud.domian.*;
import com.weifu.cloud.domian.dto.*;
import com.weifu.cloud.domian.vo.FactoryVo;
import com.weifu.cloud.domian.vo.InventoryDetailsVo;
import com.weifu.cloud.domian.vo.StoragePositionVo;
import com.weifu.cloud.mapper.*;
import com.weifu.cloud.service.*;
import com.easycode.cloud.service.impl.RetTaskServiceImpl;
import com.weifu.cloud.system.api.domain.SysDictData;
import com.weifu.cloud.system.service.ISysConfigService;
import com.weifu.cloud.tools.DateUtil;
import com.weifu.cloud.tools.StringUtil;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 仓管任务Service业务层处理
 *
 * @author weifu
 * @date 2024-11-12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class VHTaskInfoServiceImpl implements ITaskInfoService {

    private static final Logger logger = LoggerFactory.getLogger(VHTaskInfoServiceImpl.class);
    public static final String SQ_RESULT_SUCCESS = "S";

    @Autowired
    private TaskInfoMapper taskInfoMapper;

    @Autowired
    private StockInStdOrderMapper stockInStdOrderMapper;

    @Autowired
    private StockInFinOrderMapper stockInFinOrderMapper;

    @Autowired
    private StockInFinOrderDetailMapper stockInFinOrderDetailMapper;

    @Autowired
    private StockInStdOrderDetailMapper stockInStdOrderDetailMapper;

    @Autowired
    private InspectOrderDetailsMapper inspectOrderDetailsMapper;

    @Autowired
    private InspectOrderMapper inspectOrderMapper;

    @Autowired
    private IInStoreService inStoreService;

    @Autowired
    private IMainDataService iMainDataService;

    @Autowired
    private IPurchaseService purchaseService;

    @Autowired
    private DeliveryInspectionTaskMapper deliveryInspectionTaskMapper;

    @Autowired
    private IDeliveryOrderService deliveryOrderService;

    @Autowired
    private EsbSendCommonMapper esbSendCommonMapper;

    @Autowired
    private RemoteEsbSendService remoteEsbSendService;

    @Autowired
    private ICodeRuleService iCodeRuleService;

    @Autowired
    private IShelfTaskService shelfTaskService;

    @Autowired
    private RemoteConfigHelper remoteConfigHelper;

    @Autowired
    private ITaskService taskService;

    @Autowired
    private IStockOutService iStockOutService;

    @Autowired
    private IStockInFinOrderService stockInFinOrderService;

    @Autowired
    private RetTaskServiceImpl retTaskService;

    @Autowired
    private StockInSemiFinOrderMapper stockInSemiFinOrderMapper;

    @Autowired
    private StockInSemiFinOrderDetailMapper stockInSemiFinOrderDetailMapper;

    @Autowired
    private SapInteractionService sapInteractionService;

    @Autowired
    private IMainDataService mainDataService;

    @Autowired
    private RetTaskMapper retTaskMapper;

    @Autowired
    private IStockInStdOrderService stockInStdOrderService;


    @Autowired
    private IInspectOrderService inspectOrderService;

    @Autowired
    private WmsKbAndSwTaskLogMapper wmsKbAndSwTaskLogMapper;

    @Autowired
    private IPrintService printService;

    @Autowired
    private IStockInOtherDetailService stockInOtherDetailService;

    @Autowired
    private CostCenterReturnOrderDetailMapper costCenterReturnOrderDetailMapper;

    @Autowired
    private SaleReturnOrderDetailMapper saleReturnOrderDetailMapper;

    @Autowired
    private SaleReturnOrderMapper saleReturnOrderMapper;

    @Autowired
    ISysConfigService sysConfigService;

    /**
     * 查询仓管任务
     *
     * @param id 仓管任务主键
     * @return 仓管任务
     */
    @Override
    public TaskInfo selectTaskInfoById(Long id) {
        TaskInfo taskInfo = taskInfoMapper.selectTaskInfoById(id);
        if(taskInfo!=null && taskInfo.getMaterialNo()!=null){
            AjaxResult materialBasicInfo = iMainDataService.getMaterialAttrByMaterialNo(taskInfo.getMaterialNo());//selectWmsMaterialBasicById
            List<WmsMaterialBasicVo> rows = com.alibaba.fastjson.JSON.parseArray(com.alibaba.fastjson.JSON.toJSONString(materialBasicInfo.get("data")), WmsMaterialBasicVo.class);
            if(rows!=null && rows.size()>0){
                taskInfo.setAddr(rows.get(0).getFactoryCode());
                taskInfo.setMaterialName(rows.get(0).getMaterialName());
                taskInfo.setOldMaterialNo(rows.get(0).getOldMaterialNo());
                taskInfo.setPackageCount(rows.get(0).getPackageCount());
            }
        }
        return taskInfo;
    }
    /**
     * 查询仓管任务
     *
     * @param taskInfoVo 仓管任务主键
     * @return 仓管任务
     */
    @Override
    public TaskInfo selectTaskInfoVById(TaskInfoVo taskInfoVo) {
        return taskInfoMapper.selectTaskInfoVById(taskInfoVo);
    }

    @Override
    public int updateTaskInfoById(TaskInfo taskInfo) {
        return taskInfoMapper.updateTaskInfo(taskInfo);
    }

    @Override
    public JSONObject printInfo(TaskInfoVo taskInfo) {
        PrintTOParamsDto printTOParamsDto = new PrintTOParamsDto();
        printTOParamsDto.setUserName(SecurityUtils.getUsername());
        // 根据任务号查询任务信息
        List<TaskInfoVo> taskInfoVos = taskInfoMapper.selectTaskInfoList(taskInfo);
        if (CollectionUtils.isEmpty(taskInfoVos)) {
            throw new ServiceException("该任务号未查询到相关信息!");
        }
        TaskInfoVo taskInfoVo = taskInfoVos.get(0);
        // 需要将任务类型的值解析为对应的中文字符
        List<SysDictData> taskTypes = DictUtils.getDictCache("task_type");
        Map<String, String> map = taskTypes.stream().collect(
                Collectors.toMap(SysDictData::getDictValue, SysDictData::getDictLabel, (v1, v2) -> v1));
        String taskType = "";
        if (!StringUtils.isEmpty(taskInfoVo.getTaskType()) && null != map.get(taskInfoVo.getTaskType())) {
            taskType = taskInfoVo.getTaskType();
            taskInfoVo.setTaskType(map.get(taskInfoVo.getTaskType()));
        }
        // TODO:根据任务类型去查询对应的单据明细对TaskInfo其他值进行赋值目前
        if (taskType.equals(TaskTypeConstant.STOCKIN_OTHER)) {
            StockInDetailPrintVo stockInOtherDetailPrintVo = stockInOtherDetailService.queryStockInOtherDetailByTaskNo(taskInfo.getTaskNo());
            if (null != stockInOtherDetailPrintVo) {
                printTOParamsDto.setTaskType("其他入库任务");
                printTOParamsDto.setFactoryCode(stockInOtherDetailPrintVo.getFactoryCode());
                printTOParamsDto.setLocationCode(stockInOtherDetailPrintVo.getLocationCode());
                printTOParamsDto.setMoveType(stockInOtherDetailPrintVo.getMoveType());
                printTOParamsDto.setLot(stockInOtherDetailPrintVo.getLot());
                printTOParamsDto.setPositionNo(stockInOtherDetailPrintVo.getPositionNo());
                printTOParamsDto.setMaterialNo(stockInOtherDetailPrintVo.getMaterialNo());
                printTOParamsDto.setMaterialName(stockInOtherDetailPrintVo.getMaterialName());
                printTOParamsDto.setMoveQty(stockInOtherDetailPrintVo.getQyt());
                printTOParamsDto.setPackQty(stockInOtherDetailPrintVo.getQyt());
                printTOParamsDto.setOldMaterialNo(stockInOtherDetailPrintVo.getOldMaterialNo());
                printTOParamsDto.setTaskNo(stockInOtherDetailPrintVo.getTaskNo());
                printTOParamsDto.setSourcePostionNo(taskInfo.getSourcePositionNo());
            }
        }
        if (taskType.equals(TaskTypeConstant.DELIVERY_INSPECTION_TASK)) {
            StockInDetailPrintVo stockInOtherDetailPrintVo = inspectOrderService.selectInspectOrderByTaskNo(taskInfo.getTaskNo());
            if (null != stockInOtherDetailPrintVo) {
                printTOParamsDto.setFactoryCode(stockInOtherDetailPrintVo.getFactoryCode());
                printTOParamsDto.setLocationCode(stockInOtherDetailPrintVo.getLocationCode());
                printTOParamsDto.setMoveType(stockInOtherDetailPrintVo.getMoveType());
                printTOParamsDto.setLot(stockInOtherDetailPrintVo.getLot());
                printTOParamsDto.setPositionNo(stockInOtherDetailPrintVo.getPositionNo());
                printTOParamsDto.setMaterialNo(stockInOtherDetailPrintVo.getMaterialNo());
                printTOParamsDto.setMaterialName(stockInOtherDetailPrintVo.getMaterialName());
                printTOParamsDto.setMoveQty(stockInOtherDetailPrintVo.getQyt());
                printTOParamsDto.setPackQty(stockInOtherDetailPrintVo.getQyt());
                printTOParamsDto.setTaskType("质检任务");
                printTOParamsDto.setOldMaterialNo(stockInOtherDetailPrintVo.getOldMaterialNo());
                printTOParamsDto.setTaskNo(stockInOtherDetailPrintVo.getTaskNo());
                printTOParamsDto.setSourcePostionNo(taskInfo.getSourcePositionNo());
            }
        }
        if (taskType.equals(TaskTypeConstant.FINISHED_PRODUCT_LISTING_TASK)) {
            StockInFinOrder stockInFinOrder = stockInFinOrderMapper.selectStockInFinOrderByOrderNo(taskInfoVo.getStockInOrderNo());
            if(ObjectUtils.isEmpty(stockInFinOrder)){
                throw new ServiceException("成品半成品入库单据查询失败。");
            }
            TaskInfo taskInfo1 = taskInfoMapper.getTaskInfoListByTaskNo(taskInfoVo.getTaskNo());
            if(ObjectUtils.isEmpty(taskInfo1)){
                throw new ServiceException("成品半成品入库任务查询失败。");
            }
            AjaxResult result = mainDataService.getStoragePositionByNo(taskInfo1.getSourcePositionNo());
            if(result.isError()){
                throw new ServiceException("查询仓位信息失败！");
            }
            List<StoragePositionVo> storagePositionVoList =  JSONObject.parseArray(com.alibaba.fastjson.JSON.toJSONString(result.get("data")), StoragePositionVo.class);
            if(org.springframework.util.CollectionUtils.isEmpty(storagePositionVoList)){
                throw new ServiceException("仓位信息为空！");
            }
            if(stockInFinOrder.getLgort().equals("2050")||stockInFinOrder.getLgort().equals("2060")||stockInFinOrder.getLgort().equals("2000")){
                if(stockInFinOrder.getLgort().equals("2000")){
                    printTOParamsDto.setLocationCode("3050");
                }else{
                    printTOParamsDto.setLocationCode("3000");
                }
                printTOParamsDto.setMoveType(stockInFinOrder.getBwart()+",311");
            }else{
                printTOParamsDto.setLocationCode(stockInFinOrder.getLgort());
                printTOParamsDto.setMoveType(stockInFinOrder.getBwart());
            }
            printTOParamsDto.setSourcePostionNo(taskInfo1.getSourcePositionNo());
            printTOParamsDto.setTaskType("成品/半成品入库任务");
            printTOParamsDto.setFactoryCode(stockInFinOrder.getWerks());
            printTOParamsDto.setMaterialName(stockInFinOrder.getMaktx());
            printTOParamsDto.setMaterialNo(stockInFinOrder.getMatnr());
            printTOParamsDto.setTaskNo(taskInfoVo.getTaskNo());
            printTOParamsDto.setPositionNo(taskInfo1.getPositionNo());
            printTOParamsDto.setLot(stockInFinOrder.getCharg());
            printTOParamsDto.setMoveQty(new BigDecimal(stockInFinOrder.getMenge()));
            printTOParamsDto.setPackQty(new BigDecimal(stockInFinOrder.getMenge()));
            printTOParamsDto.setUserName(stockInFinOrder.getUname());
            printTOParamsDto.setSourceStorageType(storagePositionVoList.get(0).getRemark());
            printTOParamsDto.setOldMaterialNo(taskInfo1.getOldMaterialNo());
        }
        if (taskType.equals(StockInTaskConstant.COST_CENTER_RETURN_TYPE)) {
            RetTask retTask = retTaskMapper.selectRetTaskById(taskInfoVo.getId());
            if(ObjectUtils.isEmpty(retTask)){
                throw new ServiceException("成本中心任务详情查询失败。");
            }
            CostCenterReturnOrderDetail costCenterReturnOrderDetail = costCenterReturnOrderDetailMapper.selectCostCenterReturnOrderDetailById(retTask.getDetailId());
            if(ObjectUtils.isEmpty(costCenterReturnOrderDetail)){
                throw new ServiceException("成本中心领料详情查询失败。");
            }
            AjaxResult ajaxResult = mainDataService.getStoragePositionByNo(retTask.getPositionNo());
            if(ajaxResult.isError()){
                throw new ServiceException("查询仓位信息失败！");
            }
            List<StoragePositionVo> storagePositionVoList =  JSONObject.parseArray(com.alibaba.fastjson.JSON.toJSONString(ajaxResult.get("data")), StoragePositionVo.class);
            if(org.springframework.util.CollectionUtils.isEmpty(storagePositionVoList)){
                throw new ServiceException("仓位信息为空！");
            }
            String moveType = retTask.getMoveType().substring(1);
            Integer result = Integer.valueOf(moveType)+1;
            moveType = String.format("%02d", result);
            moveType = retTask.getMoveType().substring(0, moveType.length() - 1) + moveType;
            printTOParamsDto.setLot(retTask.getLot());
            printTOParamsDto.setFactoryCode(costCenterReturnOrderDetail.getFactoryCode());
            printTOParamsDto.setMoveQty(retTask.getQty());
            printTOParamsDto.setPositionNo(retTask.getPositionNo());
            printTOParamsDto.setPackQty(retTask.getQty());
            printTOParamsDto.setLocationCode(retTask.getStorageLocationCode());
            printTOParamsDto.setMaterialName(retTask.getMaterialName());
            printTOParamsDto.setMaterialNo(retTask.getMaterialNo());
            printTOParamsDto.setTaskType("成本中心退货任务");
            printTOParamsDto.setSourcePostionNo(retTask.getPositionNo());
            printTOParamsDto.setTaskNo(retTask.getTaskNo());
            printTOParamsDto.setMoveType(moveType);
            printTOParamsDto.setSourceStorageType(storagePositionVoList.get(0).getRemark());
            printTOParamsDto.setOldMaterialNo(retTask.getOldMatrialName());
        }
        if (taskType.equals(StockInTaskConstant.SALE_RETURN_TYPE)) {
            RetTask retTask = retTaskMapper.selectRetTaskById(taskInfoVo.getId());
            if(ObjectUtils.isEmpty(retTask)){
                throw new ServiceException("销售发货退详情查询失败。");
            }
            SaleReturnOrderDetail detail = saleReturnOrderDetailMapper.selectSaleReturnOrderDetailById(retTask.getDetailId());
            if(ObjectUtils.isEmpty(detail)){
                throw new ServiceException("销售发货退详情查询失败。");
            }
            AjaxResult ajaxResult = mainDataService.getStoragePositionByNo(retTask.getPositionNo());
            if(ajaxResult.isError()){
                throw new ServiceException("查询仓位信息失败！");
            }
            List<StoragePositionVo> storagePositionVoList =  JSONObject.parseArray(com.alibaba.fastjson.JSON.toJSONString(ajaxResult.get("data")), StoragePositionVo.class);
            if(org.springframework.util.CollectionUtils.isEmpty(storagePositionVoList)){
                throw new ServiceException("仓位信息为空！");
            }
            SaleReturnOrder saleReturnOrder = saleReturnOrderMapper.selectSaleReturnOrderByOrderNo(detail.getReturnOrderNo());
            printTOParamsDto.setFactoryCode(detail.getFactoryCode());
            printTOParamsDto.setLocationCode(detail.getLocationCode());
            printTOParamsDto.setMaterialName(detail.getMaterialName());
            printTOParamsDto.setMaterialNo(detail.getMaterialNo());
            printTOParamsDto.setTaskNo(retTask.getTaskNo());
            printTOParamsDto.setPositionNo(retTask.getPositionNo());
            printTOParamsDto.setLot(detail.getLot());
            printTOParamsDto.setMoveQty(retTask.getQty());
            printTOParamsDto.setPackQty(retTask.getQty());
            printTOParamsDto.setUserName(saleReturnOrder.getFactoryName());
            printTOParamsDto.setSourcePostionNo(retTask.getPositionNo());
            printTOParamsDto.setSourceStorageType(storagePositionVoList.get(0).getRemark());
            printTOParamsDto.setOldMaterialNo(retTask.getOldMatrialName());
        }
        // 更新打印状态以及打印次数
        taskInfoVo.setPrintStatus(1);
        int num = 0;
        if (null == taskInfoVo.getPrintSum()) {
            num = 1;
        } else if (null != taskInfoVo.getPrintSum() && 0 == taskInfoVo.getPrintSum()){
            num = 1;
        } else {
            num = taskInfoVo.getPrintSum().intValue();
            num++;
        }
        taskInfoVo.setTaskType(taskType);
        taskInfoVo.setPrintSum(num);
        taskInfoMapper.updateTaskInfo(taskInfoVo);
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
     * 多选打印
     * @param taskInfo
     * @return
     */
    @Override
    public List<JSONObject> printInfoAll(TaskInfoVo taskInfo) {
        PrintTOParamsDto printTOParamsDto = new PrintTOParamsDto();
        printTOParamsDto.setUserName(SecurityUtils.getUsername());
        // 根据任务号查询任务信息
        List<TaskInfoVo> taskInfoVos = taskInfoMapper.selectTaskInfoList(taskInfo);
        if (CollectionUtils.isEmpty(taskInfoVos)) {
            throw new ServiceException("该任务号未查询到相关信息!");
        }
        List<com.alibaba.fastjson.JSONObject> jsonObjectList = new ArrayList<>();
        for(TaskInfoVo taskInfoVo: taskInfoVos){
            // 需要将任务类型的值解析为对应的中文字符
            List<SysDictData> taskTypes = DictUtils.getDictCache("task_type");
            Map<String, String> map = taskTypes.stream().collect(
                    Collectors.toMap(SysDictData::getDictValue, SysDictData::getDictLabel, (v1, v2) -> v1));
            String taskType = "";
            if (!StringUtils.isEmpty(taskInfoVo.getTaskType()) && null != map.get(taskInfoVo.getTaskType())) {
                taskType = taskInfoVo.getTaskType();
                taskInfoVo.setTaskType(map.get(taskInfoVo.getTaskType()));
            }
            // TODO:根据任务类型去查询对应的单据明细对TaskInfo其他值进行赋值目前
            if (taskType.equals(TaskTypeConstant.STOCKIN_OTHER)) {
                StockInDetailPrintVo stockInOtherDetailPrintVo = stockInOtherDetailService.queryStockInOtherDetailByTaskNo(taskInfoVo.getTaskNo());
                if (null != stockInOtherDetailPrintVo) {
                    printTOParamsDto.setTaskType("其他入库任务");
                    printTOParamsDto.setFactoryCode(stockInOtherDetailPrintVo.getFactoryCode());
                    printTOParamsDto.setLocationCode(stockInOtherDetailPrintVo.getLocationCode());
                    printTOParamsDto.setMoveType(stockInOtherDetailPrintVo.getMoveType());
                    printTOParamsDto.setLot(stockInOtherDetailPrintVo.getLot());
                    printTOParamsDto.setPositionNo(stockInOtherDetailPrintVo.getPositionNo());
                    printTOParamsDto.setMaterialNo(stockInOtherDetailPrintVo.getMaterialNo());
                    printTOParamsDto.setMaterialName(stockInOtherDetailPrintVo.getMaterialName());
                    printTOParamsDto.setMoveQty(stockInOtherDetailPrintVo.getQyt());
                    printTOParamsDto.setPackQty(stockInOtherDetailPrintVo.getQyt());
                    printTOParamsDto.setOldMaterialNo(stockInOtherDetailPrintVo.getOldMaterialNo());
                    printTOParamsDto.setTaskNo(stockInOtherDetailPrintVo.getTaskNo());
                    printTOParamsDto.setSourcePostionNo(taskInfo.getSourcePositionNo());
                }
            }
            if (taskType.equals(TaskTypeConstant.DELIVERY_INSPECTION_TASK)) {
                StockInDetailPrintVo stockInOtherDetailPrintVo = inspectOrderService.selectInspectOrderByTaskNo(taskInfoVo.getTaskNo());
                if (null != stockInOtherDetailPrintVo) {
                    printTOParamsDto.setFactoryCode(stockInOtherDetailPrintVo.getFactoryCode());
                    printTOParamsDto.setLocationCode(stockInOtherDetailPrintVo.getLocationCode());
                    printTOParamsDto.setMoveType(stockInOtherDetailPrintVo.getMoveType());
                    printTOParamsDto.setLot(stockInOtherDetailPrintVo.getLot());
                    printTOParamsDto.setPositionNo(stockInOtherDetailPrintVo.getPositionNo());
                    printTOParamsDto.setMaterialNo(stockInOtherDetailPrintVo.getMaterialNo());
                    printTOParamsDto.setMaterialName(stockInOtherDetailPrintVo.getMaterialName());
                    printTOParamsDto.setMoveQty(stockInOtherDetailPrintVo.getQyt());
                    printTOParamsDto.setPackQty(stockInOtherDetailPrintVo.getQyt());
                    printTOParamsDto.setTaskType("质检任务");
                    printTOParamsDto.setOldMaterialNo(stockInOtherDetailPrintVo.getOldMaterialNo());
                    printTOParamsDto.setTaskNo(stockInOtherDetailPrintVo.getTaskNo());
                    printTOParamsDto.setSourcePostionNo(taskInfo.getSourcePositionNo());
                }
            }
            if (taskType.equals(TaskTypeConstant.FINISHED_PRODUCT_LISTING_TASK)) {
                StockInFinOrder stockInFinOrder = stockInFinOrderMapper.selectStockInFinOrderByOrderNo(taskInfoVo.getStockInOrderNo());
                if(ObjectUtils.isEmpty(stockInFinOrder)){
                    throw new ServiceException("成品半成品入库单据查询失败。");
                }
                TaskInfo taskInfo1 = taskInfoMapper.getTaskInfoListByTaskNo(taskInfoVo.getTaskNo());
                if(ObjectUtils.isEmpty(taskInfo1)){
                    throw new ServiceException("成品半成品入库任务查询失败。");
                }
                AjaxResult result = mainDataService.getStoragePositionByNo(taskInfo1.getSourcePositionNo());
                if(result.isError()){
                    throw new ServiceException("查询仓位信息失败！");
                }
                List<StoragePositionVo> storagePositionVoList =  JSONObject.parseArray(com.alibaba.fastjson.JSON.toJSONString(result.get("data")), StoragePositionVo.class);
                if(org.springframework.util.CollectionUtils.isEmpty(storagePositionVoList)){
                    throw new ServiceException("仓位信息为空！");
                }
                if(stockInFinOrder.getLgort().equals("2050")||stockInFinOrder.getLgort().equals("2060")||stockInFinOrder.getLgort().equals("2000")){
                    if(stockInFinOrder.getLgort().equals("2000")){
                        printTOParamsDto.setLocationCode("3050");
                    }else{
                        printTOParamsDto.setLocationCode("3000");
                    }
                    printTOParamsDto.setMoveType(stockInFinOrder.getBwart()+",311");
                }else{
                    printTOParamsDto.setLocationCode(stockInFinOrder.getLgort());
                    printTOParamsDto.setMoveType(stockInFinOrder.getBwart());
                }
                printTOParamsDto.setSourcePostionNo(taskInfo1.getSourcePositionNo());
                printTOParamsDto.setTaskType("成品/半成品入库任务");
                printTOParamsDto.setFactoryCode(stockInFinOrder.getWerks());
                printTOParamsDto.setMaterialName(stockInFinOrder.getMaktx());
                printTOParamsDto.setMaterialNo(stockInFinOrder.getMatnr());
                printTOParamsDto.setTaskNo(taskInfoVo.getTaskNo());
                printTOParamsDto.setPositionNo(taskInfo1.getPositionNo());
                printTOParamsDto.setLot(stockInFinOrder.getCharg());
                printTOParamsDto.setMoveQty(new BigDecimal(stockInFinOrder.getMenge()));
                printTOParamsDto.setPackQty(new BigDecimal(stockInFinOrder.getMenge()));
                printTOParamsDto.setUserName(stockInFinOrder.getUname());
                printTOParamsDto.setSourceStorageType(storagePositionVoList.get(0).getRemark());
                printTOParamsDto.setOldMaterialNo(taskInfo1.getOldMaterialNo());
            }
            if (taskType.equals(StockInTaskConstant.COST_CENTER_RETURN_TYPE)) {
                RetTask retTask = retTaskMapper.selectRetTaskById(taskInfoVo.getId());
                if(ObjectUtils.isEmpty(retTask)){
                    throw new ServiceException("成本中心任务详情查询失败。");
                }
                CostCenterReturnOrderDetail costCenterReturnOrderDetail = costCenterReturnOrderDetailMapper.selectCostCenterReturnOrderDetailById(retTask.getDetailId());
                if(ObjectUtils.isEmpty(costCenterReturnOrderDetail)){
                    throw new ServiceException("成本中心领料详情查询失败。");
                }
                AjaxResult ajaxResult = mainDataService.getStoragePositionByNo(retTask.getPositionNo());
                if(ajaxResult.isError()){
                    throw new ServiceException("查询仓位信息失败！");
                }
                List<StoragePositionVo> storagePositionVoList =  JSONObject.parseArray(com.alibaba.fastjson.JSON.toJSONString(ajaxResult.get("data")), StoragePositionVo.class);
                if(org.springframework.util.CollectionUtils.isEmpty(storagePositionVoList)){
                    throw new ServiceException("仓位信息为空！");
                }
                String moveType = retTask.getMoveType().substring(1);
                Integer result = Integer.valueOf(moveType)+1;
                moveType = String.format("%02d", result);
                moveType = retTask.getMoveType().substring(0, moveType.length() - 1) + moveType;
                printTOParamsDto.setLot(retTask.getLot());
                printTOParamsDto.setFactoryCode(costCenterReturnOrderDetail.getFactoryCode());
                printTOParamsDto.setMoveQty(retTask.getQty());
                printTOParamsDto.setPositionNo(retTask.getPositionNo());
                printTOParamsDto.setPackQty(retTask.getQty());
                printTOParamsDto.setLocationCode(retTask.getStorageLocationCode());
                printTOParamsDto.setMaterialName(retTask.getMaterialName());
                printTOParamsDto.setMaterialNo(retTask.getMaterialNo());
                printTOParamsDto.setTaskType("成本中心退货任务");
                printTOParamsDto.setSourcePostionNo(retTask.getPositionNo());
                printTOParamsDto.setTaskNo(retTask.getTaskNo());
                printTOParamsDto.setMoveType(moveType);
                printTOParamsDto.setSourceStorageType(storagePositionVoList.get(0).getRemark());
                printTOParamsDto.setOldMaterialNo(retTask.getOldMatrialName());
            }
            if (taskType.equals(StockInTaskConstant.SALE_RETURN_TYPE)) {
                RetTask retTask = retTaskMapper.selectRetTaskById(taskInfoVo.getId());
                if(ObjectUtils.isEmpty(retTask)){
                    throw new ServiceException("销售发货退详情查询失败。");
                }
                SaleReturnOrderDetail detail = saleReturnOrderDetailMapper.selectSaleReturnOrderDetailById(retTask.getDetailId());
                if(ObjectUtils.isEmpty(detail)){
                    throw new ServiceException("销售发货退详情查询失败。");
                }
                AjaxResult ajaxResult = mainDataService.getStoragePositionByNo(retTask.getPositionNo());
                if(ajaxResult.isError()){
                    throw new ServiceException("查询仓位信息失败！");
                }
                List<StoragePositionVo> storagePositionVoList =  JSONObject.parseArray(com.alibaba.fastjson.JSON.toJSONString(ajaxResult.get("data")), StoragePositionVo.class);
                if(org.springframework.util.CollectionUtils.isEmpty(storagePositionVoList)){
                    throw new ServiceException("仓位信息为空！");
                }
                SaleReturnOrder saleReturnOrder = saleReturnOrderMapper.selectSaleReturnOrderByOrderNo(detail.getReturnOrderNo());
                printTOParamsDto.setFactoryCode(detail.getFactoryCode());
                printTOParamsDto.setLocationCode(detail.getLocationCode());
                printTOParamsDto.setMaterialName(detail.getMaterialName());
                printTOParamsDto.setMaterialNo(detail.getMaterialNo());
                printTOParamsDto.setTaskNo(retTask.getTaskNo());
                printTOParamsDto.setPositionNo(retTask.getPositionNo());
                printTOParamsDto.setLot(detail.getLot());
                printTOParamsDto.setMoveQty(retTask.getQty());
                printTOParamsDto.setPackQty(retTask.getQty());
                printTOParamsDto.setUserName(saleReturnOrder.getFactoryName());
                printTOParamsDto.setSourcePostionNo(retTask.getPositionNo());
                printTOParamsDto.setSourceStorageType(storagePositionVoList.get(0).getRemark());
                printTOParamsDto.setOldMaterialNo(retTask.getOldMatrialName());
            }
            // 更新打印状态以及打印次数
            taskInfoVo.setPrintStatus(1);
            int num = 0;
            if (null == taskInfoVo.getPrintSum()) {
                num = 1;
            } else if (null != taskInfoVo.getPrintSum() && 0 == taskInfoVo.getPrintSum()){
                num = 1;
            } else {
                num = taskInfoVo.getPrintSum().intValue();
                num++;
            }
            taskInfoVo.setTaskType(taskType);
            taskInfoVo.setPrintSum(num);
            taskInfoMapper.updateTaskInfo(taskInfoVo);

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
            jsonObjectList.add(jsonObject);
        }
        return jsonObjectList;
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

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("taskType", printTOParamsDto.getTaskType());
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
     * 打印
     * @param taskInfoVo
     */
    public void printList( TaskInfoVo taskInfoVo) {
        AjaxResult printerByLocationAjaxResult = printService.findPrinterByLocation(taskInfoVo.getLocationCode());

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
        com.alibaba.fastjson2.JSONArray jsonArray = new JSONArray();

        JSONObject jsonObject = new JSONObject();
        // 目标存储类型库存地点的备注
        jsonObject.put("targetSotrageType", taskInfoVo.getLocationCode());
        // 工厂或库存地点
        jsonObject.put("factoryCodeOrLocationCode", String.format("%s/%s", taskInfoVo.getFactoryCode(), taskInfoVo.getLocationCode()));
        // 操作类型
        jsonObject.put("taskType", taskInfoVo.getTaskType());
        // 存储类型
        jsonObject.put("storageType", "");
        // 移动类型
        jsonObject.put("moveType", taskInfoVo.getMoveType());
        // 仓位
        jsonObject.put("postionNo", taskInfoVo.getPositionNo());
        // 创建日期
        jsonObject.put("createDate", DateFormatUtils.format(taskInfoVo.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        // 创建时间
//        jsonObject.put("createTime", DateFormatUtils.format(taskInfoVo.getCreateTime(), "HH:MM:SS"));
        // 供应商名称
        jsonObject.put("vendorName", taskInfoVo.getVendorName());
        // 用户名
        jsonObject.put("userName", SecurityUtils.getUsername());
        // 货架寿命
        jsonObject.put("life", "");
        // 源发地仓储类型
        jsonObject.put("sourceStorageType", "");
        // 源发地仓位
        jsonObject.put("sourcePostionNo", taskInfoVo.getSourcePositionNo());
        // 重量
        jsonObject.put("weight", "");
        // 供应商批次
        jsonObject.put("vendorLot", "");
        // 物料号
        jsonObject.put("materialNo", taskInfoVo.getMaterialNo());
        // 物料描述
        jsonObject.put("materialName", taskInfoVo.getMaterialName());
        // 移动数量
        jsonObject.put("moveQty", taskInfoVo.getDeliverQty());
        // 批次
        jsonObject.put("lot", taskInfoVo.getBatchNo());
        // 包装型号
        jsonObject.put("packageType", "");
        // 完成时间
        jsonObject.put("completeTime", "");
        // 包装数量
        jsonObject.put("packQty", taskInfoVo.getDeliverQty());
        // 任务流水号
        jsonObject.put("taskNo", taskInfoVo.getTaskNo());
        // 检验流水号
        jsonObject.put("inspectNo", "");
        // qrCode  O%T任务号%M物料号
        jsonObject.put("qrCode", "O%T" + taskInfoVo.getTaskNo() + "%M" + taskInfoVo.getMaterialNo());
        // 任务流水号
        jsonObject.put("oldMaterialNo", taskInfoVo.getOldMaterialNo());
        jsonArray.add(jsonObject);
        dto.setDataArray(jsonArray);
        AjaxResult ajaxResult1 = printService.printByTemplate(dto);
        if (ajaxResult1.isError()) {
            throw new ServiceException(String.format("打印失败,原因：%s", ajaxResult1.get("msg").toString()));
        }
    }



    /**
     * 查询仓管任务列表
     *
     * @param taskInfo 仓管任务
     * @return 仓管任务
     */
    @Override
    public List<TaskInfoVo> selectTaskInfoList(TaskInfoVo taskInfo) {
        return taskInfoMapper.selectTaskInfoList(taskInfo);
    }

    /**
     * 查询标准入库任务列表
     *
     * @param taskInfo 仓管任务
     * @return 仓管任务
     */
    @Override
    public List<TaskInfoVo> selectStdTaskInfoList(TaskInfoVo taskInfo) {
        return taskInfoMapper.selectStdTaskInfoList(taskInfo);
    }

    @Override
    public void updatePrinterStatus(TaskInfoVo taskInfoVo) {
        taskInfoVo.setPrintStatus(1);
        taskInfoVo.setPrintSum(taskInfoVo.getPrintSum()+1);
        taskInfoVo.setUpdateTime(DateUtils.getNowDate());
        taskInfoVo.setUpdateBy(SecurityUtils.getUsername());
        if(taskInfoVo.getWhichTable().equals("2")){
            taskInfoMapper.updatePrinterStatus(taskInfoVo);
        }else{
            retTaskMapper.updatePrinterStatus(taskInfoVo);
        }
    }

    /**
     * 查询看板任务列表
     *
     * @param taskInfo
     * @return
     */
    @Override
    public List<TaskInfoVo> listBoardTask(TaskInfoVo taskInfo) {
        return taskInfoMapper.selectBoardTaskList(taskInfo);
    }

    /**
     * 新增仓管任务
     *
     * @param taskInfo 仓管任务
     * @return 结果
     */
    @Override
    public int insertTaskInfo(TaskInfo taskInfo) {
        taskInfo.setCreateTime(DateUtils.getNowDate());
        return taskInfoMapper.insertTaskInfo(taskInfo);
    }

    /**
     * 修改仓管任务
     *
     * @param taskInfo 仓管任务
     * @return 结果
     */
    @Override
    @GlobalTransactional
    public int updateTaskInfo(TaskInfo taskInfo) throws Exception {
        String taskStatus = taskInfo.getTaskStatus();
        if (TaskStatusConstant.TASK_STATUS_CLOSE.equals(taskStatus)) {
            throw new ServiceException("任务状态异常");
        }

        TaskInfoVo taskInfoV = new TaskInfoVo();
        taskInfoV.setId(taskInfo.getId());
        List<TaskInfoVo> taskInfoList = taskInfoMapper.selectBoardTaskList(taskInfoV);
        if(CollectionUtils.isEmpty(taskInfoList)) {
            throw new ServiceException("任务不存在！");
        }

        TaskInfoVo taskInfo1 = taskInfoList.get(0);
        if (TaskTypeConstant.WATERLEVEL_INSPECTION_TASK.equals(taskInfo1.getTaskType())) {
            BigDecimal reserveCapacity = null;

            TaskInfoVo taskInf = new TaskInfoVo();
            taskInf.setTaskNo(taskInfo1.getBoardTaskNo());
            taskInf.setTaskType(TaskTypeConstant.FILP_THE_BAG);
            List<TaskInfoVo> taskInfoVos = taskInfoMapper.selectBoardTaskList(taskInf);
            if(CollectionUtil.isNotEmpty(taskInfoVos)){
                String taskStatuso = taskInfoVos.get(0).getTaskStatus();
                if(!TaskStatusConstant.TASK_STATUS_COMPLETE.equals(taskStatuso)){
                    throw new ServiceException("翻包任务未完成！");
                }
            }

            if (TaskStatusConstant.TASK_STATUS_COMPLETE.equals(taskStatus)) {
                //查询预定库存表并删除
                logger.info(""+ taskInfo1);
                // sap过账
                selectPurposeInventory(taskInfo1);//查询库存台账表并减去库存
                // 1源库存地点-目的库存地点
                selectLedgerAndAddPurposeInventory(taskInfo1);
                // 2 水位日志 新增wms_kb_and_sw_task_log数据 task_id为任务号task_no
                taskInfo1.setTaskStatus(TaskStatusConstant.TASK_STATUS_COMPLETE);
                //获取数量
                reserveCapacity = taskInfo.getQuantityLssued();
                String voucherNo =  paramsSap(taskInfo1);
                //生成移动记录
                instTaskLog(taskInfo1,voucherNo);//voucherNo
            }
            insertWmsKbAndSwTaskLog(taskInfo);
            taskInfo.setQuantityLssued(reserveCapacity);
        }
        taskInfo.setUpdateBy(SecurityUtils.getUsername());
        taskInfo.setUpdateTime(DateUtils.getNowDate());
        return taskInfoMapper.updateTaskInfo(taskInfo);
    }

    /**生成任务记录--移动记录
     * @param taskInfo
     */
   private void instTaskLog(TaskInfoVo taskInfo,String voucherNo){

       AjaxResult materialBasicInfo = mainDataService.getMaterialBasicInfo(taskInfo.getMaterialNo());

       if(materialBasicInfo.isError()) {
           throw new ServiceException(materialBasicInfo.get("msg").toString());
       }
       WmsMaterialBasicVo materialInfo = com.alibaba.fastjson2.JSONObject.parseObject(materialBasicInfo.get("data").toString(), WmsMaterialBasicVo.class);
       AjaxResult location = mainDataService.getStorageLocationByLocationCode(taskInfo.getLocationCode());
       if(location.isError()) {
           throw new ServiceException("获取原库存地点信息失败！");
       }
       StorageLocationDto sourceLocationDto = com.alibaba.fastjson2.JSONObject.parseObject(location.get("data").toString(), StorageLocationDto.class);
       String propertyValue =null;
       if(sourceLocationDto!=null){
           FactoryVo factoryVo = new FactoryVo();
           factoryVo.setId(sourceLocationDto.getFactoryId());
           AjaxResult factory = mainDataService.getFactory(factoryVo);
           List<FactoryDto> factoryDtos = com.alibaba.fastjson.JSON.parseArray(com.alibaba.fastjson.JSON.toJSONString(factory.get("data")), FactoryDto.class);
           propertyValue = factoryDtos != null && !factoryDtos.isEmpty() ? factoryDtos.get(0).getFactoryName() : null;
       }
       // 生成执行记录
       TaskLog taskLog = new TaskLog();
       taskLog.setTaskNo(taskInfo.getTaskNo());
       taskLog.setTaskType(taskInfo.getTaskType());
       taskLog.setMaterialNo(taskInfo.getMaterialNo());
       taskLog.setOldMaterialNo(taskInfo.getOldMaterialNo());
       taskLog.setMaterialName(materialInfo.getMaterialName());
       taskLog.setUnit(materialInfo.getDefaultUnit());
       taskLog.setFactoryCode(sourceLocationDto != null ? sourceLocationDto.getFactoryCode() : null);
       taskLog.setLocationCode(taskInfo.getSourceLocationCode());
       taskLog.setTargetLocationCode(taskInfo.getLocationCode());
       taskLog.setTargetPositionCode(taskInfo.getPositionNo());
       taskLog.setOrderType(TaskLogTypeConstant.WATERLEVEL_TASK_RECEIVE);
       taskLog.setOrderNo(taskInfo.getOrderNo());
       taskLog.setLineNo("");
       taskLog.setIsQc(taskInfo.getIsQc());
       taskLog.setIsFreeze(CommonYesOrNo.NO);
       taskLog.setIsConsign(CommonYesOrNo.NO);
       taskLog.setSupplierCode(taskInfo.getVendorCode());
       taskLog.setMaterialCertificate(voucherNo);
       taskLog.setQty(taskInfo.getQuantityLssued());
       taskLog.setLot(taskInfo.getBatchNo());
       taskLog.setPositionCode(taskInfo.getSourcePositionNo());
//       taskLog.setTargetFactoryCode(propertyValue);
       taskLog.setTargetFactoryCode(sourceLocationDto.getFactoryCode());
       if (taskService.add(taskLog).isError()) {
           throw new ServiceException("新增任务记录失败！");
       }
    }


    /**
     *获取数量
     * @param taskInfo
     * @return
     */
   private BigDecimal  getQuantityLssued(TaskInfoVo taskInfo){
       BigDecimal reserveCapacity = null;
       AjaxResult boardInfoAjaxResult = mainDataService.getBoardInfo(taskInfo.getBoardCode());
       if(boardInfoAjaxResult.isError()) {
           throw new ServiceException(boardInfoAjaxResult.get("msg").toString());
       }
       WmsBoardInfoDto boardInfoDto = com.alibaba.fastjson2.JSONObject.parseObject(boardInfoAjaxResult.get("data").toString(), WmsBoardInfoDto.class);
       BigDecimal capacity = new BigDecimal(boardInfoDto.getCapacity());
       String materialNo = boardInfoDto.getMaterialNo();
       String location =  boardInfoDto.getDefaultDeliverLocationCode();

       // 查询台账
       AjaxResult inventoryResult = mainDataService.getInventoryByLocationAndMaterialNo(location, materialNo);
       if(inventoryResult.isError()) {
           throw new ServiceException(inventoryResult.get("msg").toString());
       }
       List<InventoryDetails> inventoryDetails = JSONObject.parseArray(JSON.toJSONString(inventoryResult.get("data")), InventoryDetails.class);
       if(CollectionUtil.isEmpty(inventoryDetails)){
           throw new ServiceException("该物料无可用的库存台账信息！");
       }

       BigDecimal availableQty = inventoryDetails.stream().map(InventoryDetails::getAvailableQty).reduce(BigDecimal.ZERO, BigDecimal::add);
       if(availableQty.compareTo(new BigDecimal(boardInfoDto.getCapacity())) < 0){
           throw new ServiceException("库存台账物料可用数量不足！");
       }
       reserveCapacity = availableQty.compareTo(capacity) > 0 ? capacity : availableQty;
       return  reserveCapacity;
    }

    /**
     新增水位日志
     */
    private void insertWmsKbAndSwTaskLog(TaskInfo taskInfo1){
        WmsKbAndSwTaskLog wmsKbAndSwTaskLog =new WmsKbAndSwTaskLog();
        wmsKbAndSwTaskLog.setTaskId(String.valueOf(taskInfo1.getId()));
        wmsKbAndSwTaskLog.setTaskStatus(taskInfo1.getTaskStatus());
//        wmsKbAndSwTaskLog.setSapMaterialDocument(taskInfo1.getAsnNo());
        wmsKbAndSwTaskLog.setCreateBy(SecurityUtils.getUsername());
        wmsKbAndSwTaskLog.setCreateTime(DateUtils.getNowDate());
        wmsKbAndSwTaskLogMapper.insertWmsKbAndSwTaskLog(wmsKbAndSwTaskLog);
    }

    /**
     新增水位日志
     */
    private void insertWmsKbAndSwTaskLogVo(TaskInfoVo taskInfo1){
        WmsKbAndSwTaskLog wmsKbAndSwTaskLog =new WmsKbAndSwTaskLog();
        wmsKbAndSwTaskLog.setTaskId(String.valueOf(taskInfo1.getId()));
        wmsKbAndSwTaskLog.setTaskStatus(taskInfo1.getTaskStatus());
        wmsKbAndSwTaskLog.setSapMaterialDocument(taskInfo1.getAsnNo());
        wmsKbAndSwTaskLog.setCreateBy(SecurityUtils.getUsername());
        wmsKbAndSwTaskLog.setCreateTime(DateUtils.getNowDate());
        wmsKbAndSwTaskLogMapper.insertWmsKbAndSwTaskLog(wmsKbAndSwTaskLog);
    }
    /**
     * sap过账
     */
    public String paramsSap(TaskInfoVo taskInfo1) throws Exception {
        AjaxResult locationCode = mainDataService.getStorageLocationByLocationCode(taskInfo1.getLocationCode());
        StorageLocationDto storageLocationDto = com.alibaba.fastjson2.JSONObject.parseObject(locationCode.get("data").toString(), StorageLocationDto.class);
        SapMoveLocationParamsDto paramsDto = new SapMoveLocationParamsDto();
        paramsDto.setBudat(DateUtils.getDate());
        paramsDto.setBldat(DateUtils.getDate());
        paramsDto.setPlant(storageLocationDto.getFactoryCode());
        paramsDto.setMaterial(taskInfo1.getMaterialNo());
        paramsDto.setStgeLoc(taskInfo1.getSourceLocationCode());
        paramsDto.setMoveType("311");
        paramsDto.setVendor(taskInfo1.getVendorCode());
        paramsDto.setMovePlant(storageLocationDto.getFactoryCode());
        paramsDto.setMoveMat(taskInfo1.getMaterialNo());
        paramsDto.setMoveStloc(taskInfo1.getLocationCode());
        if(taskInfo1.getQuantityLssued()==null){
            paramsDto.setEntryQnt("1");
        }else {
            paramsDto.setEntryQnt(String.valueOf(taskInfo1.getQuantityLssued()));
        }
        paramsDto.setEntryUom(taskInfo1.getUnit());
        paramsDto.setItemText(taskInfo1.getTaskNo());
        return  moveLocationSaps(paramsDto);
    }

    /**
     * 查询库存台账表并增加目的库存 ---台账
     */
    public void selectLedgerAndAddPurposeInventory(TaskInfoVo taskInfo1){
        //查询预定库存表并删除
        ReserveStorage reserveStorage = new ReserveStorage();
        reserveStorage.setTaskNo(taskInfo1.getTaskNo());
        AjaxResult revertResult = inStoreService.getReserveStorage(reserveStorage);
        if (revertResult.isError()) {
            throw new ServiceException("查询预存表失败！");
        }
        List<ReserveStorage> reserveStorages = com.alibaba.fastjson.JSON.parseArray(com.alibaba.fastjson.JSON.toJSONString(revertResult.get("rows")), ReserveStorage.class);
        if (reserveStorages == null || reserveStorages.isEmpty()) {
            logger.error("查询预存表失败,预测台账数据为空！");
        }
        AjaxResult materialBasicInfo = mainDataService.getMaterialBasicInfo(taskInfo1.getMaterialNo());

        if(materialBasicInfo.isError()) {
            throw new ServiceException(materialBasicInfo.get("msg").toString());
        }
        WmsMaterialBasicVo materialInfo = com.alibaba.fastjson2.JSONObject.parseObject(materialBasicInfo.get("data").toString(), WmsMaterialBasicVo.class);
        AjaxResult location = mainDataService.getStorageLocationByLocationCode(taskInfo1.getLocationCode());
        if(location.isError()) {
            throw new ServiceException("获取原库存地点信息失败！");
        }
        StorageLocationDto sourceLocationDto = com.alibaba.fastjson2.JSONObject.parseObject(location.get("data").toString(), StorageLocationDto.class);
        InventoryDetails inventoryDetail = new InventoryDetails();
        inventoryDetail.setMaterialNo(taskInfo1.getMaterialNo());
        inventoryDetail.setLocationCode(taskInfo1.getLocationCode());
        inventoryDetail.setPositionNo(taskInfo1.getPositionNo());
        inventoryDetail.setIsFreeze(CommonYesOrNo.NO);
        inventoryDetail.setIsConsign(CommonYesOrNo.NO);
        inventoryDetail.setIsQc(CommonYesOrNo.NO);
        inventoryDetail.setStockSpecFlag(CommonYesOrNo.NO);
        AjaxResult result = inStoreService.getInventory(inventoryDetail);
        if ("".equals(Objects.toString(result.get("data"), "")) || result.isError()) {
            //查询库存台账表并减去库存
            for (ReserveStorage reserve : reserveStorages) {
                List<InventoryDetails> inventoryList = JSON.parseArray(result.get("data").toString(), InventoryDetails.class);
                if (inventoryList != null && inventoryList.size() > 0) {
                    InventoryDetails inventoryDetails = inventoryList.get(0);
                    if (null != reserve.getReserveQty()) {
                        inventoryDetails.setAvailableQty(inventoryDetails.getAvailableQty().add(reserve.getReserveQty()));
                        inventoryDetails.setInventoryQty(inventoryDetails.getInventoryQty().add(reserve.getReserveQty()));
                    }
                    inventoryDetails.setUpdateTime(DateUtils.getNowDate());
                    inventoryDetails.setUpdateBy(SecurityUtils.getLoginUser().getUsername());
                    inStoreService.updateInventory(inventoryDetails);
                }
            }
        }else {
            //新增台账
            instInventoryDetails(taskInfo1,sourceLocationDto,materialInfo);
        }
        //删除预定库存
        List<Long> reserveIds = reserveStorages.stream().map(ReserveStorage::getId).collect(Collectors.toList());
        inStoreService.removeByReserveIds(reserveIds);
    }

    /**
     * 新增台账消息
     * @param taskInfo1
     * @param sourceLocationDto
     * @param materialInfo
     */
    public  void instInventoryDetails(TaskInfoVo taskInfo1,StorageLocationDto sourceLocationDto,WmsMaterialBasicVo materialInfo){
        InventoryDetails inventoryDetails = new InventoryDetails();
        inventoryDetails.setMaterialNo(taskInfo1.getMaterialNo());
        inventoryDetails.setMaterialName(taskInfo1.getMaterialName());
        inventoryDetails.setOldMaterialNo(taskInfo1.getOldMaterialNo());
        inventoryDetails.setPositionNo(taskInfo1.getPositionNo());
        //inventoryDetails.setPositionId(taskInfo1.getPositionId());
        inventoryDetails.setLocationCode(taskInfo1.getLocationCode());
        inventoryDetails.setFactoryId(sourceLocationDto.getFactoryId());
        inventoryDetails.setFactoryCode(sourceLocationDto != null ? sourceLocationDto.getFactoryCode() : null);
        inventoryDetails.setFactoryName(sourceLocationDto.getFactoryName());
        inventoryDetails.setInventoryQty(taskInfo1.getQuantityLssued());
        inventoryDetails.setAvailableQty(taskInfo1.getQuantityLssued());
        inventoryDetails.setOperationUnit(taskInfo1.getUnit());
        inventoryDetails.setUnit(materialInfo.getDefaultUnit());
        inventoryDetails.setPreparedQty(BigDecimal.ZERO);
        inventoryDetails.setOperationPreparedQty(BigDecimal.ZERO);
        inventoryDetails.setStockInDate(DateUtils.getNowDate());
        inventoryDetails.setContainerNo(taskInfo1.getContainerNo());
        inventoryDetails.setStockInLot(taskInfo1.getBatchNo());
        inventoryDetails.setIsQc(CommonYesOrNo.NO);
        inventoryDetails.setIsConsign(CommonYesOrNo.NO);
        inventoryDetails.setIsFreeze(CommonYesOrNo.NO);
        inventoryDetails.setStockSpecFlag(CommonYesOrNo.NO);
        inventoryDetails.setCreateBy(SecurityUtils.getUsername());
        inventoryDetails.setCreateTime(DateUtils.getNowDate());
        inventoryDetails.setTenantId(SecurityUtils.getLoginUser().getTenantId());
        inventoryDetails.setTaskNo(taskInfo1.getTaskNo());
        inStoreService.add(inventoryDetails);//新增台账
    }

    /**
     * 查询指定库存 并处理
     */
    public void selectPurposeInventory(TaskInfoVo taskInfo){
        ReserveStorage reserveStorage = new ReserveStorage();
        reserveStorage.setTaskNo(taskInfo.getTaskNo());
        AjaxResult revertResult = inStoreService.getReserveStorage(reserveStorage);
        if (revertResult.isError()) {
//            throw new ServiceException("查询预存表失败！");
            logger.error("查询预存表失败，任务编号：{}，错误原因：{}", taskInfo.getTaskNo());
            return;
        }
        List<ReserveStorage> reserveStorages = com.alibaba.fastjson.JSON.parseArray(com.alibaba.fastjson.JSON.toJSONString(revertResult.get("rows")), ReserveStorage.class);
        if (reserveStorages == null || reserveStorages.isEmpty()) {
            throw new ServiceException("查询预存表失败！");
        }

        //查询库存台账表并减去库存
        for (ReserveStorage reserve : reserveStorages) {
            InventoryDetailsVo inventoryDetailsVo = new InventoryDetailsVo();
            inventoryDetailsVo.setId(reserve.getInventoryId());
            AjaxResult ajaxResult = inStoreService.list(inventoryDetailsVo);
            if (ajaxResult.isError()) {
                continue;
            }
            List<InventoryDetails> inventoryDetailsList = com.alibaba.fastjson.JSON.parseArray(ajaxResult.get("data").toString(), InventoryDetails.class);
            if (inventoryDetailsList == null || inventoryDetailsList.isEmpty()) {
                continue;
            }
            InventoryDetails inventoryDetails = inventoryDetailsList.get(0);
            if(reserve.getReserveQty()!=null){
                inventoryDetails.setAvailableQty(inventoryDetails.getAvailableQty().subtract(reserve.getReserveQty()));
                inventoryDetails.setInventoryQty(inventoryDetails.getInventoryQty().subtract(reserve.getReserveQty()));
            }
            inventoryDetails.setUpdateTime(DateUtils.getNowDate());
            inventoryDetails.setUpdateBy(SecurityUtils.getLoginUser().getUsername());
            taskInfo.setUnit(inventoryDetails.getUnit());
            inStoreService.updateInventory(inventoryDetails);
        }
//        List<Long> reserveIds = reserveStorages.stream().map(ReserveStorage::getId).collect(Collectors.toList());
//        inStoreService.removeByReserveIds(reserveIds);
    }

    /**
     * 修改看板领料任务
     *
     * @param taskInfo 仓管任务
     * @return 结果
     */
    @Override
    public int updateBoardTaskInfo(TaskInfo taskInfo) {
        String taskStatus = taskInfo.getTaskStatus();
        if (TaskStatusConstant.TASK_STATUS_CLOSE.equals(taskStatus)) {
            throw new ServiceException("任务状态异常");
        }
        taskInfo.setUpdateBy(SecurityUtils.getUsername());
        taskInfo.setUpdateTime(DateUtils.getNowDate());
        return taskInfoMapper.updateTaskInfo(taskInfo);
    }

    /**
     * 批量删除仓管任务
     *
     * @param ids 需要删除的仓管任务主键
     * @return 结果
     */
    @Override
    public int deleteTaskInfoByIds(Long[] ids) {
        return taskInfoMapper.deleteTaskInfoByIds(ids);
    }

    /**
     * 删除仓管任务信息
     *
     * @param id 仓管任务主键
     * @return 结果
     */
    @Override
    public int deleteTaskInfoById(Long id) {
        return taskInfoMapper.deleteTaskInfoById(id);
    }

    /**
     * 查询成品收货任务列表
     *
     * @param taskInfoDto
     * @return TaskInfoVo集合
     */
    @Override
    public List<TaskInfoVo> selectTaskInfoFinList(TaskInfoDto taskInfoDto) {
        return taskInfoMapper.selectTaskInfoFinList(taskInfoDto);
    }

    /**
     * 获取成品收货任务详细信息
     */
    @Override
    public List<TaskInfoVo> selectTaskFinInfoById(Long id) {
        List<TaskInfoVo> list = taskInfoMapper.selectTaskFinInfoById(id);
        if (ObjectUtils.isEmpty(list)) {
            throw new ServiceException("成品收货任务不存在");
        }
        return list;
    }

    /**
     * 查询标准入库任务列表
     */
    @Override
    public List<TaskInfoVo> selectTaskInfoListByStd(TaskInfoVo taskInfoVo) {
        return taskInfoMapper.selectTaskInfoListByStd(taskInfoVo);
    }

    /**
     * 标准入库任务根据asnNo获取任务详情
     * @param taskInfo
     * @return
     */
    @Override
    public TaskInfoVo getStdOrderTaskDetail(TaskInfoVo taskInfo) {
        TaskInfoVo taskInfoVo = taskInfoMapper.getStdOrderTaskDetail(taskInfo);
        if (ObjectUtils.isEmpty(taskInfoVo)) {
            throw new ServiceException("标准入库任务不存在");
        }

        // 获取物料属性信息
        WmsMaterialBasicDto unitDefDto = new WmsMaterialBasicDto();

        List<String> materialNoList =  new ArrayList<>();
        materialNoList.add(taskInfoVo.getMaterialNo());
        unitDefDto.setMaterialNoList(materialNoList);
        AjaxResult unitResult = iMainDataService.getMaterialArrInfo(unitDefDto);

        if(unitResult.isError()) {
            throw new ServiceException(unitResult.get("msg").toString());
        }
        List<WmsMaterialAttrParamsDto> materialAttrParamsList = JSONObject.parseArray(unitResult.get("data").toString(), WmsMaterialAttrParamsDto.class);

        if(materialAttrParamsList == null || materialAttrParamsList.isEmpty()) {
            throw new ServiceException(String.format("物料”%s“,过期时间获取失败！", taskInfoVo.getMaterialNo()));
        }

        WmsMaterialAttrParamsDto materialUnitDefDto = materialAttrParamsList.get(0);
        if(ObjectUtils.isEmpty(materialUnitDefDto)) {
            throw new ServiceException("过期时间获取失败！");
        }
        // 获取生产日期 过期日期
        taskInfoVo.setProductTime(DateUtils.getDate());
        String defaultValidityPeriod = materialUnitDefDto.getDefaultValidityPeriod();

        if(ObjectUtils.isEmpty(defaultValidityPeriod)) {
            throw new ServiceException("默认有效期获取失败!");
        }
        taskInfoVo.setExpireTime(DateUtils.dateTime(DateUtils.addDays(new Date(), Integer.valueOf(defaultValidityPeriod))));
        return taskInfoVo;
    }


    /**
     * ASN入库任务提交
     * @param taskInfoDto 入库任务dto
     * @return
     * @throws Exception
     */
    @Override
    @GlobalTransactional
    @Lock4j(keys = {"#taskInfoDto.id"}, acquireTimeout = 10000, expire = 30000)
    public int submitStdAsnOrderTask(TaskInfoDto taskInfoDto) throws Exception {
        // 质检单号
        InspectOrder inspectOrder = null;
        String voucherNo = null ;
        StockInStdOrderDetail stockInStdOrderDetail = null;
        try {
            if (ObjectUtils.isEmpty(taskInfoDto)) {
                throw new ServiceException(ErrorMessage.PARAMS_NOTHING_STR);
            }

            TaskInfoVo taskInfoVo = taskInfoMapper.getTaskById(taskInfoDto.getId());
            if (TaskStatusConstant.TASK_STATUS_COMPLETE.equals(taskInfoVo.getTaskStatus())) {
                throw new ServiceException(ErrorMessage.STD_STOCK_IN_TASK_COMPLETED);
            }

            StockInStdOrder stdOrder = stockInStdOrderMapper.selectStockInStdOrderByAsnNo(taskInfoDto.getAsnNo());
            if(ObjectUtils.isEmpty(stdOrder)) {
                throw new ServiceException(ErrorMessage.STD_STOCK_IN_NOTHING);
            }

            StockInStdOrderDetailDto stockInStdOrderDetailDto = new StockInStdOrderDetailDto();
            stockInStdOrderDetailDto.setId(taskInfoDto.getDetailId());
            List<StockInStdOrderDetail> stockInStdOrderDetailList = stockInStdOrderDetailMapper.selectStockInStdOrderDetailList(stockInStdOrderDetailDto);
            if (ObjectUtils.isEmpty(stockInStdOrderDetailList)) {
                throw new ServiceException(ErrorMessage.STD_STOCK_IN_DETAIL_NOTHING);
            }

            // 获取任务单关联单据的明细详情
            stockInStdOrderDetail = stockInStdOrderDetailList.get(0);

            WmsMaterialDeliverAttr dto = new WmsMaterialDeliverAttr();
            dto.setMaterialNo(taskInfoDto.getMaterialNo());
            AjaxResult materialDeliverAttrResult = mainDataService.getDeliverAttr(dto);

            if (materialDeliverAttrResult.isError()) {
                throw new ServiceException(materialDeliverAttrResult.get("msg").toString());
            }

            List<WmsMaterialDeliverAttr> deliverAttrList = JSONObject.parseArray(materialDeliverAttrResult.get("data").toString(), WmsMaterialDeliverAttr.class);

            if(ObjectUtils.isEmpty(deliverAttrList)) {
                throw new ServiceException("物料包装信息不存在");
            }

            WmsMaterialDeliverAttr materialDeliverAttr = deliverAttrList.get(0);

            String locationCode = taskInfoDto.getLocationCode();
            AjaxResult ajaxResult;
            if (StringUtil.isEmpty(locationCode)) {
                // 收货缓冲区
                locationCode = sysConfigService.selectConfigByKey("receive_buffer_location");;
            }
            // 跨工厂收货只通过字典配置的虚拟仓位记录台账，不需要质检和上架。
            if (!"VH00".equals(taskInfoDto.getFactoryCode())) {
//                //查询目标仓位
//                StoragePositionDto positionDto = new StoragePositionDto();
//                positionDto.setFactoryCode(taskInfoDto.getFactoryCode());
//                positionDto.setLocationCode(locationCode);
//                AjaxResult positionResult = iMainDataService.getInfoListLike(positionDto);
//                if (positionResult.isError()) {
//                    throw new ServiceException("获取默认仓位失败! 库存地点：" + locationCode);
//                }
//                List<StoragePositionVo> list = JSON.parseArray(positionResult.get("data").toString(), StoragePositionVo.class);
//                if (ObjectUtils.isEmpty(list)) {
//                    throw new ServiceException("移入仓位未在主数据维护! 库存地点：" + locationCode);
//                }
//                StoragePositionVo defaultPosition = list.get(0);
                //默认仓位字典
                List<SysDictData> defPositionNo = DictUtils.getDictCache("factory_default_position");
                if (ObjectUtils.isEmpty(defPositionNo)) {
                    throw new ServiceException("获取默认仓位字典类型失败");
                }
                StoragePositionVo defaultPosition = new StoragePositionVo();
                String finalLocationCode = locationCode;
                defPositionNo.forEach(e -> {
                    if (e.getDictLabel().equals(finalLocationCode)) {
                        defaultPosition.setPositionNo(e.getDictValue());
                    }
                });
                defaultPosition.setLocationCode(locationCode);
                defaultPosition.setFactoryCode(taskInfoDto.getFactoryCode());
                // 更新库存台账信息
                updateInventoryDetailsRaw(taskInfoDto, defaultPosition);
                // 进行sap 过账
                voucherNo = syncToSap(taskInfoDto, stockInStdOrderDetail);
                addTaskLog(stdOrder, stockInStdOrderDetail, taskInfoDto, defaultPosition, voucherNo);
                return HttpStatus.SC_OK;
            }
            AjaxResult positionResult = mainDataService.getPositionCodeByLocationCodeAndMaterialNo(
                    locationCode, taskInfoDto.getMaterialNo());

            if(positionResult.isError()) {
                throw new ServiceException("查询入库单据的推荐仓位失败，库存地点代码" + stockInStdOrderDetail.getLocationCode());
            }
            StoragePositionVo storagePositionVo = JSON.parseObject(JSON.toJSONString(positionResult.get("data")), StoragePositionVo.class);

            String mixedFlag = storagePositionVo.getMixedFlag();
            if (CommonYesOrNo.NOT_MIXED.equals(mixedFlag) && !ObjectUtils.isEmpty(storagePositionVo.getId())) {
                // 非混放仓位，锁定推荐仓位
                mainDataService.updateStoragePositionStatus(new Long[]{storagePositionVo.getId()}, CommonYesOrNo.LOCK_POSITION);
            }
            taskInfoDto.setFactoryCode(storagePositionVo.getFactoryCode());
            taskInfoDto.setLocationCode(storagePositionVo.getLocationCode());
            taskInfoDto.setPositionNo(storagePositionVo.getPositionNo());

            // 更新库存台账信息
            Long inventoryId = updateInventoryDetailsRaw(taskInfoDto, storagePositionVo);

            if (CommonYesOrNo.NOT_MIXED.equals(mixedFlag) && !ObjectUtils.isEmpty(storagePositionVo.getId())) {
                // 非混放仓位，锁定推荐仓位
                mainDataService.updateStoragePositionStatus(new Long[]{storagePositionVo.getId()}, CommonYesOrNo.UNLOCK_POSITION);
            }

            logger.info("质检单流程开始: {}", taskInfoDto.getIsQc());

            // 是否质检，是的话需要创建送检单
            if (CommonYesOrNo.YES.equals(taskInfoDto.getIsQc())) {
                // 异步保存esb 日志
                logger.info("开始创建质检单: {}", DateUtil.getNowTime());
                // 生成送检单据
                inspectOrder = generateInspectOrder(taskInfoDto, stockInStdOrderDetail, storagePositionVo);
                if (ObjectUtils.isEmpty(inspectOrder) || StringUtil.isEmpty(inspectOrder.getQcQty())) {
                    throw new ServiceException("生成质检单据失败！");
                }
                if (inspectOrder.getQcQty().compareTo(BigDecimal.ZERO) == 0) {
                    // 抽检数量为0时，生成上架任务，其他场景不需要生成上架任务
                    generateShelfTask(taskInfoDto, stockInStdOrderDetail, materialDeliverAttr, inspectOrder,
                            storagePositionVo, inventoryId);
                }
                logger.info("送检单创建流程完成");
            }

            // 非质检
            if(CommonYesOrNo.NO.equals(taskInfoDto.getIsQc())) {
                // 生成上架任务
                generateShelfTaskNoInspect(taskInfoDto, stockInStdOrderDetail, materialDeliverAttr,
                        storagePositionVo, inventoryId);
                logger.info("非质检场景生成上架任务: {}", DateUtil.getNowTime());
            }
            //更新任务状态
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setId(taskInfoDto.getId());
            taskInfo.setTaskStatus(TaskStatusConstant.TASK_STATUS_COMPLETE);
            taskInfo.setLocationCode(taskInfoDto.getLocation());
            taskInfo.setPositionNo(taskInfoDto.getPositionNo());
            taskInfo.setUpdateBy(SecurityUtils.getUsername());
            taskInfo.setUpdateTime(DateUtils.getNowDate());
            int updateTaskInfo = taskInfoMapper.updateTaskInfo(taskInfo);
            if (updateTaskInfo <= 0) {
                throw new ServiceException("修改标准入库任务状态失败！");
            }
            //任务全部完成或已关闭时 修改标准入库单据状态为 4已完成
            TaskInfoVo relTaskInfoVo = new TaskInfoVo();
            relTaskInfoVo.setStockInOrderNo(taskInfoDto.getAsnNo());
            relTaskInfoVo.setTaskStatusArr(new String[] {TaskStatusConstant.TASK_STATUS_NEW,
                    TaskStatusConstant.TASK_STATUS_PART_COMPLETE});
            List<TaskInfoVo> taskInfos = taskInfoMapper.selectStdTaskInfoList(relTaskInfoVo);
            if (taskInfos.isEmpty()) {
                StockInStdOrder stockInStdOrder = new StockInStdOrder();
                stockInStdOrder.setId(stdOrder.getId());
                stockInStdOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_COMPLETE);
                stockInStdOrder.setUpdateBy(SecurityUtils.getUsername());
                stockInStdOrder.setUpdateTime(DateUtils.getNowDate());
                stockInStdOrderMapper.updateStockInStdOrder(stockInStdOrder);
                logger.info("更新入库单单据信息");
            }
            stockInStdOrderService.updateOutSourcedInventory(stdOrder);

            // 跳检场景需要先进行sap 过账，才能移库
            voucherNo = syncToSap(taskInfoDto, stockInStdOrderDetail);
            addTaskLog(stdOrder, stockInStdOrderDetail, taskInfoDto, storagePositionVo, voucherNo);

            // 跳检触发送检处理
            if (!ObjectUtils.isEmpty(inspectOrder)
                    && !StringUtil.isEmpty(inspectOrder.getQcQty())
                    && inspectOrder.getQcQty().compareTo(BigDecimal.ZERO) == 0) {
                try {
                    SendInspectResultDto detailDto = new SendInspectResultDto();
                    detailDto.setTaskCode(inspectOrder.getOrderNo());
                    detailDto.setResult(SQConstant.SQ_INSPECT_PASS);
                    detailDto.setRemark("跳检");
                    inspectOrderService.sendInspectResult(detailDto);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
            logger.info("submitStdAsnOrderTask finish");
        } catch (ServiceException e) {
            rollbackOrder(inspectOrder, voucherNo, taskInfoDto, stockInStdOrderDetail);
            logger.error("ASN单据提交失败", e);
            throw new ServiceException("ASN单据提交失败，" + e.getMessage());
        } catch (Exception e) {
            // 取消质检任务 回滚
            rollbackOrder(inspectOrder, voucherNo, taskInfoDto, stockInStdOrderDetail);
            logger.info("ASN单据提交失败: {}", e.getMessage());
            throw new ServiceException("ASN单据提交失败，请联系管理员");
        }
        return HttpStatus.SC_OK;
    }

    private void rollbackOrder(InspectOrder inspectOrder, String voucherNo, TaskInfoDto taskInfoDto,
                               StockInStdOrderDetail stockInStdOrderDetail) {
        // 取消质检任务 回滚
        if (!ObjectUtils.isEmpty(inspectOrder)) {
            try {
                inspectOrderService.cancelInspectTask(inspectOrder.getOrderNo());
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
        }
        if (!StringUtils.isEmpty(voucherNo)){
            try {
                JSONObject param = new JSONObject();
                param.put("voucherNo" , voucherNo);
                inspectOrderService.materialReversal(param);
            } catch (Exception exception) {
                logger.error(exception.getMessage());
            }
        }
    }

    private String syncToSap(TaskInfoDto taskInfoDto, StockInStdOrderDetail stockInStdOrderDetail) throws Exception {
        //sap过账
        SapPurchaseParamsDto sapPurchaseParams = new SapPurchaseParamsDto();
        sapPurchaseParams.setMaterialNo(taskInfoDto.getMaterialNo());
        sapPurchaseParams.setFactoryCode(taskInfoDto.getFactoryCode());
        sapPurchaseParams.setMoveType(stockInStdOrderDetail.getBwart());
        sapPurchaseParams.setLotNo(taskInfoDto.getLotNo());
        sapPurchaseParams.setLocationCode(stockInStdOrderDetail.getLocationCode());
        sapPurchaseParams.setReceivedQty(taskInfoDto.getReceivedQty());
        sapPurchaseParams.setUnit(taskInfoDto.getUnit());
        sapPurchaseParams.setIsConsign(taskInfoDto.getIsConsign());
        sapPurchaseParams.setIsQc(taskInfoDto.getIsQc());
        sapPurchaseParams.setTaskType(taskInfoDto.getTaskType());
        sapPurchaseParams.setPrdOrderNo(taskInfoDto.getPrdOrderNo());
        sapPurchaseParams.setDeliveryOrderNo(stockInStdOrderDetail.getThirdDeliveryOrderNo());
        sapPurchaseParams.setDeliveryLineNo(stockInStdOrderDetail.getThirdDeliveryLineNo());
        sapPurchaseParams.setMoveFlag("B");
        logger.info("sap过账参数列表: {}", sapPurchaseParams);

        String voucherNo = sendToSap(sapPurchaseParams);
        logger.info("sap过账成功");

        stockInStdOrderDetail.setVoucherNo(voucherNo);
        stockInStdOrderDetailMapper.updateStockInStdOrderDetail(stockInStdOrderDetail);
        logger.info("更新交货单信息完成");

        return voucherNo;
    }

    private void generateShelfTaskNoInspect(TaskInfoDto taskInfoDto, StockInStdOrderDetail stockInStdOrderDetail,
                                            WmsMaterialDeliverAttr deliverAttr, StoragePositionVo storagePositionVo, Long inventoryId) {
        BigDecimal shelfQty = stockInStdOrderDetail.getReceivedQty();
        logger.info("非质检场景待上架物料数量: {}", shelfQty);

        List<ShelfTask> shelfTaskList = generateShelfTaskInfo(taskInfoDto, stockInStdOrderDetail, deliverAttr,
                storagePositionVo, shelfQty, inventoryId);

        shelfTaskService.insertShelfTaskList(shelfTaskList);
        logger.info("生成上架任务成功");

        // 打印TO单
        List<Long> ids = shelfTaskList.stream().map(ShelfTask::getId).collect(Collectors.toList());
        shelfTaskService.printList(stockInStdOrderDetail.getLocationCode(), ids);
        logger.info("上架任务打印完成");
    }

    private void generateShelfTask(TaskInfoDto taskInfoDto, StockInStdOrderDetail stockInStdOrderDetail,
                                   WmsMaterialDeliverAttr deliverAttr, InspectOrder inspectOrder,
                                   StoragePositionVo sourceStoragePositionVo, Long inventoryId) {
        BigDecimal receivedQty = taskInfoDto.getReceivedQty();
        if (null == receivedQty) {
            throw new ServiceException("交货单接收数量为空，请检查相关单据。");
        }

        // 抽检数量
        BigDecimal insertQty = inspectOrder.getQcQty() == null ? BigDecimal.ZERO : inspectOrder.getQcQty();
        // 上架数量
        BigDecimal shelfQty = receivedQty.subtract(insertQty);

        logger.info("单据抽检数量: {}, 生成上架数量: {}", insertQty, shelfQty);

        List<ShelfTask> shelfTaskList = generateShelfTaskInfo(taskInfoDto, stockInStdOrderDetail, deliverAttr,
                sourceStoragePositionVo, shelfQty, inventoryId);

        shelfTaskService.insertShelfTaskList(shelfTaskList);
        logger.info("生成上架任务结束：{}", DateUtil.getNowTime());

        List<Long> taskIdList = shelfTaskList.stream().map(ShelfTask::getId).collect(Collectors.toList());
        // 打印上架任务
        shelfTaskService.printList(stockInStdOrderDetail.getLocationCode(), taskIdList);
        logger.info("打印上架任务标签完成");
    }

    private List<ShelfTask> generateShelfTaskInfo(TaskInfoDto taskInfoDto, StockInStdOrderDetail stockInStdOrderDetail,
                                                  WmsMaterialDeliverAttr deliverAttr, StoragePositionVo sourceStoragePositionVo,
                                                  BigDecimal shelfQty, Long inventoryId) {

        logger.info("生成上架任务开始：{}", DateUtil.getNowTime());
        Long tenantId = SecurityUtils.getLoginUser().getSysUser().getTenantId();
        List<ShelfTask> shelfTaskList = new ArrayList<>();

        // 获取每托数量，后续根据每托数量拆分
        BigDecimal palletQty = deliverAttr.getPalletQty();
        if (ObjectUtils.isEmpty(palletQty)) {
            throw new ServiceException("物料每托数量配置为空，请检查相关基础数据。");
        }

        // 总任务数
        BigDecimal taskQty = shelfQty.divide(palletQty, 0, RoundingMode.CEILING);

        logger.info("生成上架任务总任务数: {}", taskQty);

        // 上架任务
        for (int i = 0; i < taskQty.intValue(); i++) {
            // 上架任务号
            AjaxResult result = iCodeRuleService.getSeqWithTenantId(TaskNoTypeConstant.SHELF_TASK, String.valueOf(tenantId));
            if (result.isError() || StringUtils.isEmpty(result.get("data").toString())) {
                throw new ServiceException("上架任务号生成失败！");
            }
            String taskNo = result.get("data").toString();

            AjaxResult positionResult = iMainDataService.getPositionCode(stockInStdOrderDetail.getMaterialNo());

            if(positionResult.isError()) {
                throw new ServiceException("查询入库单据的仓位失败，物料代码" + stockInStdOrderDetail.getMaterialNo());
            }
            StoragePositionVo storagePositionVo = JSON.parseObject(JSON.toJSONString(positionResult.get("data")), StoragePositionVo.class);

            if (null == storagePositionVo.getId()) {
                throw new ServiceException("查询入库单据的仓位失败，物料代码" + stockInStdOrderDetail.getMaterialNo());
            }

            String mixedFlag = storagePositionVo.getMixedFlag();
            if (CommonYesOrNo.NOT_MIXED.equals(mixedFlag) && !ObjectUtils.isEmpty(storagePositionVo.getId())) {
                // 非混放仓位，锁定推荐仓位
                iMainDataService.updateStoragePositionStatus(new Long[]{storagePositionVo.getId()}, CommonYesOrNo.LOCK_POSITION);
            }
            ShelfTask shelfTask = new ShelfTask();
            shelfTask.setTaskNo(taskNo);
            shelfTask.setTaskType(TaskLogTypeConstant.SHELF);
            shelfTask.setStatus(TaskStatusConstant.TASK_STATUS_NEW);
            shelfTask.setAllocateTime(DateUtils.getNowDate());
            shelfTask.setMaterialNo(taskInfoDto.getMaterialNo());
            shelfTask.setMaterialName(taskInfoDto.getMaterialName());
            shelfTask.setOldMaterialNo(taskInfoDto.getOldMaterialNo());
            shelfTask.setStockinOrderNo(stockInStdOrderDetail.getPurchaseOrderNo());
            shelfTask.setStockinLineNo(stockInStdOrderDetail.getPurchaseLineNo());
            shelfTask.setLocationCode(storagePositionVo.getLocationCode());
            shelfTask.setPositionNo(storagePositionVo.getPositionNo());
            shelfTask.setPositionId(storagePositionVo.getId());
            shelfTask.setContainerNo(stockInStdOrderDetail.getWbsElement());
            shelfTask.setSourcePositionNo(sourceStoragePositionVo.getPositionNo());
            shelfTask.setSourceLocationCode(sourceStoragePositionVo.getLocationCode());
            shelfTask.setQty(palletQty);
            shelfTask.setOperationQty(palletQty);
            shelfTask.setUnit(taskInfoDto.getUnit());
            shelfTask.setOperationUnit(taskInfoDto.getOperationUnit());
            shelfTask.setCompleteQty(BigDecimal.ZERO);
            shelfTask.setOperationCompleteQty(BigDecimal.ZERO);
            shelfTask.setLot(taskInfoDto.getLotNo());
            shelfTask.setCreateBy(SecurityUtils.getUsername());
            shelfTask.setCreateTime(DateUtils.getNowDate());
            shelfTask.setTenantId(tenantId);

            // 最后一条数量取余数
            if (i == taskQty.intValue() - 1) {
                BigDecimal qty = shelfQty.remainder(palletQty);
                if (qty.compareTo(BigDecimal.ZERO) > 0) {
                    shelfTask.setQty(qty);
                    shelfTask.setOperationQty(qty);
                }
            }

            shelfTask.setInventoryId(inventoryId);
            shelfTaskList.add(shelfTask);
            logger.info("生成上架任务收货数量: {}", shelfTask.getQty());
        }
        return shelfTaskList;
    }




    @Override
    public void reSendInspectOrder(StockInStdOrderDetailDto stockInStdOrderDetailDto) throws Exception {
        if (ObjectUtils.isEmpty(stockInStdOrderDetailDto) || ObjectUtils.isEmpty(stockInStdOrderDetailDto.getId())) {
            throw new ServiceException("生成质检单失败，未选择关联的入库单据信息，请重新确认。");
        }
        // 现根据id重新查询一次，后续可以改成前端传递赋值
        List<StockInStdOrderDetail> stockInStdOrderDetails = stockInStdOrderDetailMapper.selectStockInStdOrderDetailList(stockInStdOrderDetailDto);
        StockInStdOrderDetail stockInStdOrderDetail = stockInStdOrderDetails.get(0);
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setDetailId(stockInStdOrderDetail.getId());
        taskInfo.setTaskType(TaskTypeConstant.STOCK_IN_ASN_TASK);
        TaskInfo taskInfo1 = taskInfoMapper.selectTaskInfoByDetailId(taskInfo);
        TaskInfoDto taskInfoDto = new TaskInfoDto();
        BeanUtils.copyProperties(taskInfo1, taskInfoDto);

        StockInStdOrder stdOrder = stockInStdOrderMapper.selectStockInStdOrderByNo(taskInfoDto.getStockInOrderNo());
        taskInfoDto.setReceivedQty(stockInStdOrderDetail.getReceivedQty());
        taskInfoDto.setFactoryCode(stockInStdOrderDetail.getFactoryCode());
        taskInfoDto.setIsConsign(stockInStdOrderDetail.getIsConsign());
        taskInfoDto.setVendorCode(stdOrder.getVendorCode());
        taskInfoDto.setVendorName(stdOrder.getVendorName());
        taskInfoDto.setUnit(stockInStdOrderDetail.getUnit());
        taskInfoDto.setOperationUnit(stockInStdOrderDetail.getOperationUnit());

        StoragePositionVo storagePositionVo = new StoragePositionVo();
        storagePositionVo.setPositionNo(taskInfoDto.getPositionNo());
        storagePositionVo.setPositionNo(taskInfoDto.getPositionNo());
        generateInspectOrder(taskInfoDto, stockInStdOrderDetail, storagePositionVo);
    }

    private InspectOrder generateInspectOrder(TaskInfoDto taskInfoDto, StockInStdOrderDetail stockInStdOrderDetail,
                                              StoragePositionVo storagePositionVo) throws Exception {
        Long tenantId = SecurityUtils.getLoginUser().getSysUser().getTenantId();
        // 新增送检单
        AjaxResult ajaxResult = iCodeRuleService.getSeqWithTenantId(OrderNoTypeConstant.INSPECT, String.valueOf(tenantId));
        if (ajaxResult.isError() || StringUtils.isEmpty(ajaxResult.get("data").toString())) {
            throw new ServiceException("送检单号生成失败！");
        }
        String inspectTaskCode = ajaxResult.get("data").toString();
        logger.info("新增送检单号: {}", inspectTaskCode);

        // 创建质检单
        InspectOrder inspectOrder = new InspectOrder();
        inspectOrder.setOrderNo(inspectTaskCode);
        inspectOrder.setQty(BigDecimal.ZERO);
        inspectOrder.setBillStatus(InspectOrderStatusConstant.ORDER_STATUS_NEW);
        inspectOrder.setFactoryCode(taskInfoDto.getFactoryCode());
        inspectOrder.setIsConsign(taskInfoDto.getIsConsign());
        inspectOrder.setPurchaseOrderNo(stockInStdOrderDetail.getPurchaseOrderNo());
        inspectOrder.setPurchaseLineNo(stockInStdOrderDetail.getPurchaseLineNo());
        inspectOrder.setLocationCode(stockInStdOrderDetail.getLocationCode());
        inspectOrder.setMaterialNo(taskInfoDto.getMaterialNo());
        inspectOrder.setMaterialName(taskInfoDto.getMaterialName());

        inspectOrder.setReceiveDate(DateUtils.getNowDate());
        inspectOrder.setReceiveOrderNo(taskInfoDto.getTaskNo());
        inspectOrder.setSupplierCode(taskInfoDto.getVendorCode());
        inspectOrder.setSupplierName(taskInfoDto.getVendorName());
        inspectOrder.setCreateBy(SecurityUtils.getUsername());
        inspectOrder.setCreateTime(DateUtils.getNowDate());
        inspectOrderMapper.insertInspectOrder(inspectOrder);
        logger.info("新增送检单信息: {}", inspectTaskCode);

        // 送检单详情
        InspectOrderDetails inspectOrderDetails = new InspectOrderDetails();
        inspectOrderDetails.setOrderNo(inspectTaskCode);
        inspectOrderDetails.setMaterialNo(taskInfoDto.getMaterialNo());
        inspectOrderDetails.setMaterialName(stockInStdOrderDetail.getMaterialName());
        inspectOrderDetails.setLot(stockInStdOrderDetail.getLotNo());
        inspectOrderDetails.setPrdLot(stockInStdOrderDetail.getLotNo());
        inspectOrderDetails.setLineNo(stockInStdOrderDetail.getPurchaseLineNo());
        inspectOrderDetails.setPositionNo(storagePositionVo.getPositionNo());
        inspectOrderDetails.setCreateBy(SecurityUtils.getUsername());
        inspectOrderDetails.setCreateTime(DateUtils.getNowDate());
        inspectOrderDetails.setOperationUnit(taskInfoDto.getOperationUnit());
        inspectOrderDetails.setUnit(taskInfoDto.getUnit());
        inspectOrderDetails.setOldMaterialNo(taskInfoDto.getOldMaterialNo());
        inspectOrderDetailsMapper.insertInspectOrderDetails(inspectOrderDetails);
        logger.info("新增检验单明细信息: {}", inspectTaskCode);

        IMsgObject msgObject = new MsgObject(IMsgObject.MOType.initSR);
        msgObject.setReqValue("TaskCode", inspectTaskCode); // 收货单任务号
        msgObject.setReqValue("ReceiveCode", taskInfoDto.getTaskNo()); // 收货单号
        msgObject.setReqValue("Company", SecurityUtils.getComCode()); //公司
        msgObject.setReqValue("Plant", taskInfoDto.getFactoryCode()); // 工厂（AC00，MA00）字典
        msgObject.setReqValue("PartNumber", taskInfoDto.getMaterialNo()); // 物料编码

        msgObject.setReqValue("PartName", taskInfoDto.getMaterialName()); // 物料名称
        msgObject.setReqValue("SupplyCode", taskInfoDto.getVendorCode()); // 供应商代码
        msgObject.setReqValue("SupplyName", taskInfoDto.getVendorName()); // 供应商名称
        msgObject.setReqValue("ProductBatch", stockInStdOrderDetail.getLotNo()); // 生产批次
        msgObject.setReqValue("InputBatch", stockInStdOrderDetail.getLotNo()); // 入库批次
        msgObject.setReqValue("InspectReason", "送检"); // 预留检验原因（进货）
        msgObject.setReqValue("TotalQuantity", Convert.toStr(Convert.toInt(stockInStdOrderDetail.getDeliverQty()))); // 来料数量
        msgObject.setReqValue("Warehouse", stockInStdOrderDetail.getLocationCode()); // 库存地点（四位数字）
        msgObject.setReqValue("ReceiveUser", SecurityUtils.getUsername()); // 收货人
        msgObject.setReqValue("ReceiveTime",DateUtils.dateTime()); // 收货时间
        msgObject.setReqValue("Remark", taskInfoDto.getRemark()); // 备注

        // 发送检验任务
        MsgObject inspectTaskMsg = sendInspectTask(msgObject);

        String flag = inspectTaskMsg.getResValue("Flag");
        if (!SQ_RESULT_SUCCESS.equals(flag)) {
            throw new ServiceException("送检失败，原因:"+inspectTaskMsg.getResValue("ErrorMsg"));
        }
        // 获取抽检数量
        String extractCountStr = inspectTaskMsg.getResValue("ExtractCount");
        logger.info("记录SQ平台返回抽检数量信息, extractCount:{}", extractCountStr);
       // 没有抽检数量 报错
        if (StringUtils.isBlank(extractCountStr)) {
            throw new ServiceException("获取抽检数量失败");
        }

        if (!ObjectUtils.isEmpty(extractCountStr) ) {
            BigDecimal extractCount = Convert.toBigDecimal(extractCountStr);
            if(ObjectUtils.isEmpty(extractCount)) {
                throw new ServiceException("获取抽检数量失败");
            }

            // 记录抽检数量
            inspectOrder.setQcQty(extractCount);
            inspectOrderDetails.setQcQty(extractCount);
            inspectOrderMapper.updateInspectOrder(inspectOrder);
            inspectOrderDetailsMapper.updateInspectOrderDetails(inspectOrderDetails);
        }
        return inspectOrder;
    }

    /**
     * SAP调拨
     * @param stockInStdOrderDetail
     * @param shelfTask
     * @param storagePositionVo
     */
    private String moveLocationSap(StockInStdOrderDetail stockInStdOrderDetail, ShelfTask shelfTask, StoragePositionVo storagePositionVo) {
        // sap调拨
        Map<String,String> map = new HashMap<>();
        map.put("factoryCode", stockInStdOrderDetail.getFactoryCode());
        map.put("materialNo", stockInStdOrderDetail.getMaterialNo());
        map.put("lotNo", stockInStdOrderDetail.getLotNo());
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
        map.put("isConsign",CommonYesOrNo.NO);
        AjaxResult res = inStoreService.moveLocationSap(map);
        if ("".equals(Objects.toString(res.get("data"), "")) || res.isError()) {
            throw new ServiceException(Objects.toString(res.get("msg").toString(), "调用库内作业服务失败"));
        }
        return JSON.parseObject(res.get("data").toString(), String.class);
    }

    /**
     * 添加移库记录
     * @param stockInStdOrderDetail
     * @param shelfTask
     * @param storagePositionVo
     * @return
     */
    private String addStockMove(StockInStdOrderDetail stockInStdOrderDetail, ShelfTask shelfTask, StoragePositionVo storagePositionVo) {
        StockMoveDto stockMoveDto = new StockMoveDto();
        stockMoveDto.setOrderNo(stockInStdOrderDetail.getPurchaseOrderNo());
        stockMoveDto.setFactoryCode(stockInStdOrderDetail.getFactoryCode());
        stockMoveDto.setFromLocationCode(shelfTask.getSourceLocationCode());
        stockMoveDto.setFromPositionCode(shelfTask.getSourcePositionNo());
        stockMoveDto.setTargetLocationCode(shelfTask.getLocationCode());
        stockMoveDto.setTargetPositionCode(shelfTask.getPositionNo());
        stockMoveDto.setOrderType("1");
        stockMoveDto.setMaterialName(stockInStdOrderDetail.getMaterialName());
        stockMoveDto.setMaterialNo(stockInStdOrderDetail.getMaterialNo());
        stockMoveDto.setQty(shelfTask.getQty());
        stockMoveDto.setOrderStatus(OrderStatusConstant.ORDER_STATUS_NEW);
        InventoryDetails inventoryDetails = new InventoryDetails();
        inventoryDetails.setTenantId(shelfTask.getTenantId());
        inventoryDetails.setMaterialNo(shelfTask.getMaterialNo());
        inventoryDetails.setLocationCode(shelfTask.getSourceLocationCode());
        inventoryDetails.setIsQc(stockInStdOrderDetail.getIsQc());
        inventoryDetails.setStockInLot(stockInStdOrderDetail.getLotNo());
        inventoryDetails.setIsFreeze("0");
        inventoryDetails.setIsConsign("0");
        inventoryDetails.setIsReserved("0");
        // 台账列表
        AjaxResult inventoryDetailListAjaxResult = inStoreService.selectWmsInventoryDetails2(inventoryDetails);
        if (inventoryDetailListAjaxResult.isError()) {
            throw new ServiceException("查询库存台账失败！");
        }
        List<InventoryDetailsDto> inventoryDetailsList = JSON.parseArray(JSON.toJSONString(inventoryDetailListAjaxResult.get("data")), InventoryDetailsDto.class);

        if (CollectionUtils.isEmpty(inventoryDetailsList)) {
            throw new ServiceException("查询库存台账失败！");
        }
        inventoryDetailsList = inventoryDetailsList.subList(0, 1);
        stockMoveDto.setInventoryList(inventoryDetailsList);
        stockMoveDto.setTenantId(shelfTask.getTenantId());
        stockMoveDto.setCreateBy(stockInStdOrderDetail.getCreateBy());
        stockMoveDto.setCreateTime(DateUtils.getNowDate());
        stockMoveDto.setUpdateTime(DateUtils.getNowDate());

        AjaxResult ajaxResult = inStoreService.addStockMove(stockMoveDto);
        if ("".equals(Objects.toString(ajaxResult.get("data"), "")) || ajaxResult.isError()) {
            throw new ServiceException(Objects.toString(ajaxResult.get("msg").toString(), "调用库内作业服务失败"));
        }
        // 添加移库记录
        StockMoveLog stockMoveLog = new StockMoveLog();
        stockMoveLog.setMaterialName(stockInStdOrderDetail.getMaterialName());
        stockMoveLog.setMaterialNo(stockInStdOrderDetail.getMaterialNo());
        stockMoveLog.setLot(stockInStdOrderDetail.getLotNo());
        stockMoveLog.setOldLocationCode(shelfTask.getSourceLocationCode());
        stockMoveLog.setOldAreaCode(shelfTask.getSourceAreaCode());
        stockMoveLog.setOldPositionNo(shelfTask.getSourcePositionNo());
        stockMoveLog.setFactoryCode(stockInStdOrderDetail.getFactoryCode());
        stockMoveLog.setNewPositionCode(shelfTask.getPositionNo());
        stockMoveLog.setNewLocationCode(shelfTask.getLocationCode());
        stockMoveLog.setNewAreaCode(shelfTask.getAreaCode());
        stockMoveLog.setMoveQty(shelfTask.getQty());
        stockMoveLog.setContainerNo(stockInStdOrderDetail.getWbsElement());
        stockMoveLog.setTenantId(stockInStdOrderDetail.getTenantId());
        stockMoveLog.setCreateBy(stockInStdOrderDetail.getCreateBy());
        stockMoveLog.setCreateTime(DateUtils.getNowDate());
        stockMoveLog.setUpdateTime(DateUtils.getNowDate());
        inStoreService.addStockMoveLog(stockMoveLog);
        return JSON.parseObject(JSON.toJSONString(ajaxResult.get("data")), String.class);
    }

    /**
     * 根据任务类型获取任务列表
     */
    @Override
    public List<TaskInfoVo> getTaskListByTaskType(TaskInfoDto taskInfoDto) {
        return taskInfoMapper.getTaskListByTaskType(taskInfoDto);
    }

    /**
     * 成品/半成品出库任务确认
     */
    @Override
    @GlobalTransactional
    public int submitTask(TaskInfoDto taskInfoDto) throws Exception{
        if(taskInfoDto.getTaskStatus().equals(TaskStatusConstant.TASK_STATUS_NEW)){
            taskInfoDto.setTaskStatus(TaskStatusConstant.TASK_STATUS_PART_COMPLETE);
            taskInfoMapper.updateTaskInfo(taskInfoDto);
        }else{
            submitFinOrderTask(taskInfoDto);
        }
        return 1;
    }

    /**
     * 根据ID获取任务详情
     * @param id
     * @return
     */
    @Override
    public TaskInfoVo getTaskById(Long id) throws Exception{
        TaskInfoVo taskInfoVo = taskInfoMapper.getTaskById(id);
        return taskInfoVo;
    }


    /**
     * 添加移动记录
     */
    private void addTaskLog(StockInStdOrder stdOrder, StockInStdOrderDetail stockInStdOrderDetail, TaskInfoDto taskInfoDto,
                            StoragePositionVo storagePositionVo, String voucherNo) {
        // 新增任务记录
        TaskLog taskLog = new TaskLog();
        taskLog.setTaskNo(taskInfoDto.getTaskNo());
        taskLog.setFactoryCode(taskInfoDto.getFactoryCode());
        taskLog.setMaterialNo(taskInfoDto.getMaterialNo());
        taskLog.setMaterialName(taskInfoDto.getMaterialName());
        taskLog.setOldMaterialNo(taskInfoDto.getOldMaterialNo());
        taskLog.setLot(taskInfoDto.getLotNo());
        taskLog.setQty(stockInStdOrderDetail.getReceivedQty());
        taskLog.setOperationQty(stockInStdOrderDetail.getReceivedQty());
        taskLog.setLocationCode(taskInfoDto.getSourceLocationCode());
        taskLog.setPositionCode(taskInfoDto.getSourcePositionNo());
        taskLog.setTargetPositionCode(storagePositionVo.getPositionNo());
        taskLog.setTargetLocationCode(storagePositionVo.getLocationCode());
        taskLog.setTargetFactoryCode(storagePositionVo.getFactoryCode());
        taskLog.setAreaCode(storagePositionVo.getAreaCode());
        taskLog.setTaskType(TaskLogTypeConstant.STD_ORDER_ASN);
        taskLog.setOrderNo(stdOrder.getOrderNo());
        taskLog.setMaterialCertificate(voucherNo);
        taskLog.setIsQc(taskInfoDto.getIsQc());
        taskLog.setIsConsign(taskInfoDto.getIsConsign());
        taskLog.setOrderType(TaskLogTypeConstant.STD_ORDER_ASN);
        taskLog.setUnit(stockInStdOrderDetail.getUnit());
        taskLog.setSupplierCode(stdOrder.getVendorCode());
        taskLog.setOperationUnit(stockInStdOrderDetail.getOperationUnit());
        if (taskService.add(taskLog).isError()) {
            logger.error("新增任务记录失败！");
        }
    }

    /**
     * 发送质检任务
     * @param msgObject
     */
    private MsgObject sendInspectTask(IMsgObject msgObject) throws Exception {
        byte[] result = remoteEsbSendService.sendToEsb(EsbSendSapConstant.WS_SEND_INSPECT_TASK, msgObject.getBytes());

        MsgObject resMo = new MsgObject(result, IMsgObject.MOType.initSR);
        logger.info("记录发送检验任务（WS_SEND_INSPECT_TASK）结果信息，resMo: {}", resMo);

        if ("FAIL".equals(resMo.getServResStatus()) || "E".equals(resMo.getServResStatus())) {
            throw new ServiceException(resMo.getServResDesc());
        }
        if("E".equals(resMo.getResValue("Flag"))) {
            throw new ServiceException(resMo.getResValue("ErrorMsg"));
        }

        return resMo;
    }


    /**
     * sap移动库位
     * @param sapMoveLocationParamsDto
     * @return
     * @throws Exception
     */
    public String moveLocationSaps(SapMoveLocationParamsDto sapMoveLocationParamsDto) {
        String voucherNo = "";
        String flag = "";
        String errorMsg = "";
        try {
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
            errorMsg = resMo.getResValue("ERRORMSG");
            flag = resMo.getResValue("FLAG");
            if (!SapReqOrResConstant.SAP_RETURN_TYPE.equals(flag)) {
//            materialReversalList(sapMoveLocationParamsDto.getVoucherNos());
                throw new ServiceException("调拨过账失败!" + errorMsg);
            }

            // 凭证号
            voucherNo = resMo.getResValue("MBLNR");
        }catch (Exception e){
           // if (!SapReqOrResConstant.SAP_RETURN_TYPE.equals(flag)) {
//            materialReversalList(sapMoveLocationParamsDto.getVoucherNos());
                throw new ServiceException("调拨过账失败!" + e.getMessage());
          //  }
        }

        return voucherNo;
    }

    public String moveLocationSap(TaskInfoVo taskInfoVo) throws Exception {
        String defaultBatchNo = remoteConfigHelper.getConfig(ConfigConstant.DEFAULT_SAP_BATCH);
        if("".equals(Objects.toString(defaultBatchNo,""))){
            throw new ServiceException("配置参数SAP默认批次获取失败！");
        }
        StockInFinOrder stockInFinOrder = stockInFinOrderMapper.selectStockInFinOrderById(taskInfoVo.getDetailId());
        SapMoveLocationParamsDto sp = new SapMoveLocationParamsDto();
        Date now = new Date();
        sp.setBudat(DateFormatUtils.format(now, "yyyyMMdd"));
        sp.setBldat(DateFormatUtils.format(now, "yyyyMMdd"));
        sp.setBktxt("");
        sp.setuName(SecurityUtils.getUsername());
        sp.setPlant(stockInFinOrder.getWerks());
        sp.setMaterial(stockInFinOrder.getMatnr());
        sp.setStgeLoc(stockInFinOrder.getLgort());
        sp.setMoveType(SapReqOrResConstant.MOVE_LOCATION_MOVE_TYPE);
        sp.setBatch(stockInFinOrder.getCharg());
        sp.setSpecStock(stockInFinOrder.getSobkz());
        sp.setVendor("");
        sp.setMovePlant(stockInFinOrder.getWerks());
        sp.setMoveMat(stockInFinOrder.getMatnr());
        if("2050".equals(stockInFinOrder.getLgort())||"2060".equals(stockInFinOrder.getLgort())||"2000".equals(stockInFinOrder.getLgort())){
            if("2000".equals(stockInFinOrder.getLgort())){
                sp.setMoveStloc("3050");
            }else{
                sp.setMoveStloc("3000");
            }
        }else{
            sp.setMoveStloc(stockInFinOrder.getLgort());
        }
        sp.setMoveBatch("");
        sp.setEntryQnt(stockInFinOrder.getMenge());
        sp.setEntryUom(stockInFinOrder.getMeins());
        sp.setEntryUomIso("");
        sp.setNoTransferReq("");
        sp.setWbsElem(stockInFinOrder.getWbsElem());
        sp.setNetWork("");
        sp.setItemText(taskInfoVo.getTaskNo());
        sp.setSalesOrd("");
        sp.setsOrdItem("");
        sp.setValSalesOrd("");
        return sapInteractionService.moveLocationSap(sp);
    }


    /**
     * 标准入库过账
     * @param sapPurchaseParamsDto
     * @return
     * @throws Exception
     */
    public String sendToSap(SapPurchaseParamsDto sapPurchaseParamsDto) throws Exception {
        return sapInteractionService.IwmsPurchase(sapPurchaseParamsDto);
    }

    /**
     * 物料凭证冲销
     * @param voucherNo
     */
    public void materialReversal(String voucherNo){
        AjaxResult result = iStockOutService.materialReversal(voucherNo);
        if (result.isError()) {
            throw new ServiceException(Objects.toString(result.get("msg").toString(),"物料冲销接口调用失败！"));
        }
    }

    /**
     * 更新台账 原材料
     *
     * @param taskInfoDto
     * @param shelfTask
     * @param storagePositionVo
     * @return inventoryId 台账id
     */
    public Long updateInventoryDetailsRaw(TaskInfoDto taskInfoDto, ShelfTask shelfTask, StoragePositionVo storagePositionVo) {
        MaterialAttrDto materialAttrDto = new MaterialAttrDto();
        materialAttrDto.setMaterialNo(taskInfoDto.getMaterialNo());
        materialAttrDto.setFactoryId(taskInfoDto.getFactoryId());
        materialAttrDto.setFactoryCode(taskInfoDto.getFactoryCode());
        List<MaterialAttrDto> materialList = esbSendCommonMapper.getMaterialAttr(materialAttrDto);
        if (materialList == null || materialList.isEmpty()) {
            throw new ServiceException(String.format("“%s”:物料主数据“物料属性”未维护!", taskInfoDto.getMaterialNo()));
        }
        //查询当前仓位是否已有台账存在
        InventoryDetailsVo inventoryDetailsVo = new InventoryDetailsVo();
        inventoryDetailsVo.setMaterialNo(taskInfoDto.getMaterialNo());
        inventoryDetailsVo.setFactoryId(taskInfoDto.getFactoryId());
        inventoryDetailsVo.setFactoryCode(taskInfoDto.getFactoryCode());
        inventoryDetailsVo.setWarehouseId(storagePositionVo.getWarehouseId());
        inventoryDetailsVo.setWarehouseCode(storagePositionVo.getWarehouseCode());
        inventoryDetailsVo.setAreaId(storagePositionVo.getAreaId());
        inventoryDetailsVo.setAreaCode(storagePositionVo.getAreaCode());
        inventoryDetailsVo.setLocationId(storagePositionVo.getLocationId());
        inventoryDetailsVo.setLocationCode(storagePositionVo.getLocationCode());
        inventoryDetailsVo.setPositionId(storagePositionVo.getId());
        inventoryDetailsVo.setPositionNo(storagePositionVo.getPositionNo());
        inventoryDetailsVo.setStockInLot(taskInfoDto.getLotNo());
        inventoryDetailsVo.setIsConsign(taskInfoDto.getIsConsign());
        inventoryDetailsVo.setIsFreeze(CommonYesOrNo.NO);
        // 排除特殊库存
        inventoryDetailsVo.setStockSpecFlag(CommonYesOrNo.NO);
        inventoryDetailsVo.setIsQc(taskInfoDto.getIsQc());
        inventoryDetailsVo.setProductionLot(taskInfoDto.getProductionLot());
        AjaxResult listResult = inStoreService.list(inventoryDetailsVo);
        if (listResult.isError()) {
            throw new ServiceException("查询库存台账失败！");
        }
        BigDecimal receivedQty = taskInfoDto.getReceivedQty();
        List<InventoryDetails> inventoryDetailsList = JSONObject.parseArray(listResult.get("data").toString(), InventoryDetails.class);
        Long inventoryId;

        if (ObjectUtils.isEmpty(inventoryDetailsList)) {
            //新增台账
            InventoryDetails inventoryDetails = new InventoryDetails();
            inventoryDetails.setMaterialNo(shelfTask.getMaterialNo());
            inventoryDetails.setMaterialName(shelfTask.getMaterialName());
            inventoryDetails.setOldMaterialNo(shelfTask.getOldMaterialNo());
            inventoryDetails.setPositionNo(storagePositionVo.getPositionNo());
            inventoryDetails.setPositionId(storagePositionVo.getId());
            inventoryDetails.setAreaId(storagePositionVo.getAreaId());
            inventoryDetails.setAreaCode(storagePositionVo.getAreaCode());
            inventoryDetails.setLocationId(storagePositionVo.getLocationId());
            inventoryDetails.setLocationCode(storagePositionVo.getLocationCode());
            inventoryDetails.setFactoryId(storagePositionVo.getFactoryId());
            inventoryDetails.setFactoryCode(storagePositionVo.getFactoryCode());
            inventoryDetails.setFactoryName(storagePositionVo.getFactoryName());
            inventoryDetails.setProductionLot(shelfTask.getLot());
            inventoryDetails.setInventoryQty(shelfTask.getQty());
            inventoryDetails.setAvailableQty(shelfTask.getQty());
            inventoryDetails.setOperationInventoryQty(receivedQty);
            inventoryDetails.setOperationAvailableQty(receivedQty);
            inventoryDetails.setOperationUnit(taskInfoDto.getUnit());
            inventoryDetails.setUnit(taskInfoDto.getUnit());
            inventoryDetails.setConversDefault(taskInfoDto.getConversDefault());
            inventoryDetails.setPreparedQty(BigDecimal.ZERO);
            inventoryDetails.setOperationPreparedQty(BigDecimal.ZERO);
            inventoryDetails.setSupplierId(taskInfoDto.getVendorId());
            inventoryDetails.setSupplierCode(taskInfoDto.getVendorCode());
            inventoryDetails.setSupplierName(taskInfoDto.getVendorName());
            inventoryDetails.setStockInDate(DateUtils.getNowDate());
            inventoryDetails.setContainerNo(shelfTask.getContainerNo());
//            BigDecimal extValidityDays = new BigDecimal(wmsMaterialGroupList.get(0).getDefaultExtValiditydays());
//            inventoryDetails.setExpiryDate(DateUtils.addDays(DateUtils.getNowDate(), extValidityDays.intValue()));
//            inventoryDetails.setValidDays(extValidityDays);
            inventoryDetails.setStockInLot(taskInfoDto.getLotNo());
            inventoryDetails.setIsQc(taskInfoDto.getIsQc());
            inventoryDetails.setIsConsign(taskInfoDto.getIsConsign());
            inventoryDetails.setIsFreeze(CommonYesOrNo.NO);
            inventoryDetails.setWarehouseId(storagePositionVo.getWarehouseId());
            inventoryDetails.setWarehouseCode(storagePositionVo.getWarehouseCode());
            inventoryDetails.setCreateBy(SecurityUtils.getUsername());
            inventoryDetails.setCreateTime(DateUtils.getNowDate());
            inventoryDetails.setUpdateTime(DateUtils.getNowDate());

            AjaxResult result = inStoreService.add(inventoryDetails);
            if (result.isError()) {
                throw new ServiceException("新增库存台账失败！");
            }
            inventoryId = Long.parseLong(result.get("data").toString());
        } else if (inventoryDetailsList.size() == 1) {
            //修改台账
            InventoryDetails inventoryDetails = inventoryDetailsList.get(0);
            BigDecimal sumInventoryQty = inventoryDetails.getInventoryQty().add(taskInfoDto.getReceivedQty());
            BigDecimal sumAvailableQty = inventoryDetails.getAvailableQty().add(taskInfoDto.getReceivedQty());
            inventoryDetails.setInventoryQty(sumInventoryQty);
            inventoryDetails.setAvailableQty(sumAvailableQty);
            inventoryDetails.setOperationInventoryQty(sumInventoryQty);
            inventoryDetails.setOperationAvailableQty(sumAvailableQty);
            inventoryDetails.setUpdateBy(SecurityUtils.getUsername());
            inventoryDetails.setIsQc(taskInfoDto.getIsQc());
            inventoryDetails.setUpdateTime(DateUtils.getNowDate());
            inventoryId = inventoryDetails.getId();
            if (inStoreService.edit(inventoryDetails).isError()) {
                throw new ServiceException("修改库存台账失败！");
            }
        } else {
            throw new ServiceException("库存台账不唯一！");
        }
        return inventoryId;
    }

    /**
     * 成品收货任务提交
     */
    @Override
    @Lock4j(keys = {"#taskInfoDto.id"}, acquireTimeout = 10,expire = 10000)
    public int submitFinOrderTask(TaskInfoDto taskInfoDto) throws Exception{
        //参数校验
        if (ObjectUtils.isEmpty(taskInfoDto)) {
            throw new ServiceException(ErrorMessage.PARAMS_NOTHING_STR);
        }
        //根据收货库位(仓位code) 库存地点code 获取工厂信息、库存信息、区域信息、仓位信息
        AjaxResult ajaxResult = iMainDataService.getPositionInfoById(taskInfoDto.getPositionId());
        if (ajaxResult.isError()) {
            throw new ServiceException(ajaxResult.get("msg").toString());
        }
        StoragePositionVo storagePositionVo = JSONObject.parseObject(JSONObject.toJSONString(ajaxResult.get("data")), StoragePositionVo.class);
        if (ObjectUtils.isEmpty(storagePositionVo)) {
            throw new ServiceException(ErrorMessage.POSITION_CODE_NOTHING);
        }
        Long id = taskInfoDto.getId();
        //查询任务
        TaskInfoVo taskInfoVo = taskInfoMapper.getTaskById(id);
        taskInfoDto.setMaterialNo(taskInfoVo.getMaterialNo());
        taskInfoDto.setMaterialName(taskInfoVo.getMaterialName());
        taskInfoDto.setOldMaterialNo(taskInfoVo.getOldMaterialNo());
        // 获取参数配置
        String defaultBatchNo = remoteConfigHelper.getConfig(ConfigConstant.DEFAULT_SAP_BATCH);
        if("".equals(Objects.toString(defaultBatchNo,""))){
            throw new ServiceException("配置参数SAP默认批次获取失败！");
        }
        //taskInfoDto.setProductionLot(defaultBatchNo);
        StockInFinOrder firstStockInFinOrder = stockInFinOrderMapper.selectStockInFinOrderByOrderNo(taskInfoVo.getStockInOrderNo());
        if("1".equals(storagePositionVo.getMixedFlag())){
            Long[] ids = {storagePositionVo.getId()};
            //释放仓库
            mainDataService.updateStoragePositionStatus(ids,"0");
        }

        //更新台账变更记录
        InventoryHistory inventoryHistory = new InventoryHistory();
        inventoryHistory.setTaskNo(taskInfoVo.getTaskNo());
        ajaxResult = inStoreService.selectInventoryHistoryList(inventoryHistory);
        if(ajaxResult.isError()){
            throw new ServiceException("获取台账变更历史失败");
        }
        List<InventoryHistory> inventoryHistories = com.alibaba.fastjson2.JSON.parseArray(JSON.toJSONString(ajaxResult.get("data")), InventoryHistory.class);
        if(inventoryHistories==null||inventoryHistories.size()<=0){
            throw new ServiceException("获取台账变更历史失败");
        }
        // 根据单据号获取明细
        StockInFinOrder stockInFinOrder = stockInFinOrderMapper.selectStockInFinOrderByOrderNo(taskInfoVo.getStockInOrderNo());
        stockInFinOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_COMPLETE);
        stockInFinOrder.setUpdateBy(SecurityUtils.getUsername());
        stockInFinOrder.setUpdateTime(DateUtils.getNowDate());
        stockInFinOrderMapper.updateStockInFinOrder(stockInFinOrder);
        //更新成品收货单据、明细、任务
        SpringUtils.getBean(VHTaskInfoServiceImpl.class).updateFin(taskInfoVo,inventoryHistories,storagePositionVo,stockInFinOrder);
        //LD成品收货sap过账
        String voucherNo =  "";
                //moveLocationSap(taskInfoVo);
        if(!"BC".equals(stockInFinOrder.getHuNo())){
            //查询预定库存表并删除
            ReserveStorage reserveStorage = new ReserveStorage();
            reserveStorage.setTaskNo(taskInfoVo.getTaskNo());
            AjaxResult revertResult = inStoreService.getReserveStorage(reserveStorage);
            if (revertResult.isError()) {
                throw new ServiceException("查询预存表失败！");
            }
            List<ReserveStorage> reserveStorages = com.alibaba.fastjson.JSON.parseArray(com.alibaba.fastjson.JSON.toJSONString(revertResult.get("rows")), ReserveStorage.class);
            if (reserveStorages == null || reserveStorages.isEmpty()) {
                logger.error("查询预存表失败,预测台账数据为空！");
            }
            List<Long> reserveIds = reserveStorages.stream().map(ReserveStorage::getId).collect(Collectors.toList());
            inStoreService.removeByReserveIds(reserveIds);
            voucherNo = moveLocationSap(taskInfoVo);

        }
        for(InventoryHistory e:inventoryHistories){
            if(StringUtils.isNotBlank(voucherNo)){
                e.setMblnr(voucherNo);
            }
            e.setUpdateBy(SecurityUtils.getUsername());
            e.setUpdateTime(DateUtils.getNowDate());
            inStoreService.updateInventoryHistory(e);
        }
        //同步添加任务记录
        addTaskRecord(taskInfoVo, storagePositionVo, taskInfoDto, voucherNo,firstStockInFinOrder);
        return 1;
    }

    /**
     * 更新成品收货单据、明细、任务
     * @param taskInfoVo 入库任务vo
     */
    public void updateFin(TaskInfoVo taskInfoVo,List<InventoryHistory> inventoryHistories,StoragePositionVo storagePositionVo,StockInFinOrder stockInFinOrder) {
        //完成任务
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setId(taskInfoVo.getId());
        taskInfo.setTaskStatus(TaskStatusConstant.TASK_STATUS_COMPLETE);
        taskInfo.setHandlerUserId(SecurityUtils.getUserId());
        taskInfo.setHandlerUserName(SecurityUtils.getUsername());
        taskInfo.setUpdateTime(DateUtils.getNowDate());
        taskInfo.setUpdateBy(SecurityUtils.getUsername());


        if(stockInFinOrder.getLgort().equals("2050")||stockInFinOrder.getLgort().equals("2060")||stockInFinOrder.getLgort().equals("2000")){
            Long[] ids = {storagePositionVo.getId()};
            //释放仓库
            mainDataService.updateStoragePositionStatus(ids,"0");
            InventoryDetails inventoryDetails = new InventoryDetails();
            inventoryDetails.setId(inventoryHistories.get(0).getInventoryId());
            AjaxResult ajaxResult = inStoreService.selectWmsInventoryDetails2(inventoryDetails);
            if(ajaxResult.isError()){
                throw new ServiceException("获取台账失败");
            }
            List<InventoryDetails> inventoryDetailsList = com.alibaba.fastjson.JSON.parseArray(com.alibaba.fastjson2.JSON.toJSONString(ajaxResult.get("data")), InventoryDetails.class);
            if (inventoryDetailsList == null || inventoryDetailsList.isEmpty()) {
                throw new ServiceException("查询库存台账表失败！");
            }
            inventoryDetails = inventoryDetailsList.get(0);
            inventoryDetails.setPositionNo(taskInfo.getPositionNo());
            if(stockInFinOrder.getLgort().equals("2000")){
                inventoryDetails.setPositionNo(taskInfoVo.getPositionNo());
                inventoryDetails.setLocationCode("3050");
            }else{
                inventoryDetails.setPositionNo(taskInfoVo.getPositionNo());
                inventoryDetails.setLocationCode("3000");
            }
            inStoreService.updateWmsInventoryDetails(inventoryDetails);
        }
        if (taskInfoMapper.updateTaskInfo(taskInfo) <= 0){
            throw new ServiceException("更新成品收货任务失败！");
        }
    }

    public String sapSend(TaskInfoDto taskInfoDto, TaskInfoVo taskInfoVo) {
        // sap过账
        Map<String,String> map = new HashMap<>();
        map.put("factoryCode", taskInfoVo.getFactoryCode());
        map.put("materialNo", taskInfoVo.getMaterialNo());
        map.put("lotNo", taskInfoVo.getLotNo());
//        WmsMaterialWarehouseVo wmsMaterialWarehouseVo = new WmsMaterialWarehouseVo();
//        wmsMaterialWarehouseVo.setMaterialNo(taskInfoVo.getMaterialNo());
//        wmsMaterialWarehouseVo.setType(MainDataConstant.MATERIAL_RECEIVE);
//        AjaxResult materialResult = iMainDataService.getRecommend(wmsMaterialWarehouseVo);
//        if ("".equals(Objects.toString(materialResult.get("data"), "")) || materialResult.isError()) {
//            throw new ServiceException("获取推荐库存地点，区域，仓位信息失败！");
//        }
        map.put("sourceLocation","9999");
        map.put("comCode", SecurityUtils.getComCode());
        map.put("targetLocation", taskInfoDto.getLocation());
        map.put("qty", taskInfoVo.getPlanRecivevQty().toString());
        // 获取物料默认单位
        MaterialAttrDto materialAttrDto = new MaterialAttrDto();
        materialAttrDto.setMaterialNo(taskInfoVo.getMaterialNo());
        List<MaterialMainDto> materialList = esbSendCommonMapper.getMaterialMain(materialAttrDto);
        if(CollectionUtils.isEmpty(materialList)){
            throw new ServiceException(String.format("物料号：%s，获取默认单位为空！", taskInfoDto.getMaterialNo()));
        }
        map.put("unit",materialList.get(0).getDefaultUnit());
        map.put("isConsign",CommonYesOrNo.NO);
        AjaxResult res = inStoreService.moveLocationSap(map);
        if ("".equals(Objects.toString(res.get("data"), "")) || res.isError()) {
            throw new ServiceException(Objects.toString(res.get("msg").toString(), "调用库内作业服务失败"));
        }
        return JSON.parseObject(res.get("data").toString(), String.class);
    }

    /**
     * 获取成品收货单详细信息
     */
    @Override
    public TaskInfoVo selectFinDetailById(Long id) {
        return taskInfoMapper.selectFinDetailById(id);
    }

    /**
     * 查询成品收货单详情列表
     */
    @Override
    public List<TaskInfoVo> selectFinDetailListList(TaskInfo taskInfo) {
        return taskInfoMapper.selectFinDetailListList(taskInfo);
    }

    /**
     * 获取标准收货记录
     * @param stockInStdOrderDetail
     * @return TaskInfoVo集合
     */
    @Override
    public List<TaskInfoVo> getTaskInfoListByOrderNo(StockInStdOrderDetail stockInStdOrderDetail) {
        List<TaskInfoVo> taskInfoListByOrderNo = taskInfoMapper.getTaskInfoListByOrderNo(stockInStdOrderDetail);
        if (ObjectUtils.isEmpty(taskInfoListByOrderNo)){
            return taskInfoListByOrderNo;
        }

        List<String> materialNoList = taskInfoListByOrderNo.stream().map(TaskInfo::getMaterialNo).collect(Collectors.toList());
        MaterialUnitDefDto materialUnitDefDto = new MaterialUnitDefDto();
        materialUnitDefDto.setMaterialNoList(materialNoList);
        materialUnitDefDto.setStockoutEnable(CommonYesOrNo.YES);
        materialUnitDefDto.setFactoryCode(SecurityUtils.getComCode());
        AjaxResult unitResult = iMainDataService.batchMaterialUnitDef(materialUnitDefDto);
        if (unitResult.isError()) {
            throw new ServiceException(unitResult.get("msg").toString());
        }
        List<MaterialUnitDefDto> unitList = JSONObject.parseArray(unitResult.get("data").toString(), MaterialUnitDefDto.class);
        if (ObjectUtils.isEmpty(unitList)) {
            throw new ServiceException(String.format("不存在出库使用单位，请维护相关数据"));
        }
        Map<String, BigDecimal> map = unitList.stream()
                .collect(Collectors.toMap(MaterialUnitDefDto::getMaterialNo, MaterialUnitDefDto::getConversDefault,(key1,key2)->key1));
        Map<String, String> unitMap = unitList.stream()
                .collect(Collectors.toMap(MaterialUnitDefDto::getMaterialNo, MaterialUnitDefDto::getUnit,(key1,key2)->key1));
        taskInfoListByOrderNo.forEach(taskInfoVo -> {
            taskInfoVo.setConversDefault(map.getOrDefault(taskInfoVo.getMaterialNo(),BigDecimal.ONE));
            taskInfoVo.setUnit(unitMap.getOrDefault(taskInfoVo.getMaterialNo(),null));
        });
        return taskInfoListByOrderNo;
    }

    @Override
    public TaskInfoDto mesToWmsContainer(String containerNo) {
        // 数据库获取url
        String url = "";
        Map<String,Object> map = new HashMap<>();
        map.put("containerCode",containerNo);
        map.put("plantCode",SecurityUtils.getComCode());
        // 从mom获取容器号信息
//        ResponseEntity<Map> result = restTemplate.postForEntity(url, map, Map.class);
//        Map<String,Object> resMap = result.getBody();
        // todo 模拟返回数据
        Map<String,Object> resMap = new HashMap<>();
        List<Map<String,Object>> detailList = new ArrayList<>();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("orderbatchNo","20240912002");
        hashMap.put("batchQuantity","20");
        detailList.add(hashMap);

        HashMap<String, Object> hashMap1 = new HashMap<>();
        hashMap1.put("orderbatchNo","20240912002");
        hashMap1.put("batchQuantity","80");
        detailList.add(hashMap1);
        resMap.put("containerCode","11111");
        resMap.put("productCode","1506100183");
        resMap.put("productName","衬套Bushing(SQ测试)");
        resMap.put("totalQuantity","100");
        resMap.put("warehouseLocation","5100");
        resMap.put("plantCode","VH00");
        resMap.put("batchDetail",detailList);

        TaskInfoDto taskInfoDto = new TaskInfoDto();
        taskInfoDto.setContainerNo(Objects.toString(resMap.get("containerCode"),""));
        taskInfoDto.setMaterialNo(Objects.toString(resMap.get("productCode"),""));
        taskInfoDto.setMaterialDesc(Objects.toString(resMap.get("productName"),""));
        taskInfoDto.setTotalQty(BigDecimal.valueOf(Double.valueOf(Objects.toString(resMap.get("totalQuantity"),"0"))));
        taskInfoDto.setSourceLocation(Objects.toString(resMap.get("warehouseLocation"),""));
        taskInfoDto.setFactoryCode(Objects.toString(resMap.get("plantCode"),""));
        taskInfoDto.setCreateBy(SecurityUtils.getUsername());
        taskInfoDto.setCreateTime(DateUtils.getNowDate());
        List<Map<String,Object>> batchDetail = (List<Map<String,Object>>)resMap.get("batchDetail");
        List<LotDto> lotList = new ArrayList<>();
        batchDetail.forEach(d->{
            LotDto lotDto = new LotDto();
            taskInfoDto.setMaterialNo(Objects.toString(resMap.get("productCode"),""));
            lotDto.setLotNo(Objects.toString(d.get("orderbatchNo"),""));
            lotDto.setLotNoNum(BigDecimal.valueOf(Double.parseDouble(Objects.toString(d.get("batchQuantity"),"0"))));
            lotList.add(lotDto);
        });
        taskInfoDto.setLotNoNumList(lotList);
        return taskInfoDto;
    }

    @Override
    public int submitByMom(TaskInfoDto taskInfoDto) throws Exception {
        // 新增成品收货,明细,创建成品收货任务
        // 获取工厂信息
        List<FactoryCommonDto> factoryList = esbSendCommonMapper.getFactoryByCode(taskInfoDto.getFactoryCode());
        if(CollectionUtils.isEmpty(factoryList)){
            throw new ServiceException(String.format("工厂代码：%s，不存在！",taskInfoDto.getFactoryCode()));
        }
        // 获取物料信息
        MaterialAttrDto materialAttrDto = new MaterialAttrDto();
        materialAttrDto.setFactoryCode(taskInfoDto.getFactoryCode());
        materialAttrDto.setMaterialNo(taskInfoDto.getMaterialNo());
        materialAttrDto.setFactoryId(factoryList.get(0).getId());
        List<MaterialMainDto> materialList = esbSendCommonMapper.getMaterialMain(materialAttrDto);
        if(CollectionUtils.isEmpty(materialList)){
            throw new ServiceException(String.format("物料号：%s，不存在！",taskInfoDto.getMaterialNo()));
        }
        // 获取库存地点信息
        LocationDto locationDto = new LocationDto();
        locationDto.setLocationCode(taskInfoDto.getLocation());
        List<LocationDto> locationList = esbSendCommonMapper.selectLocationByCode(locationDto);
        if(CollectionUtils.isEmpty(locationList)){
            throw new ServiceException(String.format("库存地点：%s，不存在！",taskInfoDto.getLocation()));
        }

        List<LotDto> detailList = taskInfoDto.getLotNoNumList();
        List<StockInFinOrderDetailDto> dList = new ArrayList();

        StockInFinOrderDetailDto stockInFinOrderDetailDto;
        for (LotDto l : detailList) {
            stockInFinOrderDetailDto = new StockInFinOrderDetailDto();
            stockInFinOrderDetailDto.setLot(l.getLotNo());
            stockInFinOrderDetailDto.setPlanRecivevQty(l.getLotNoNum());
            stockInFinOrderDetailDto.setUnit(materialList.get(0).getDefaultUnit());
            dList.add(stockInFinOrderDetailDto);
        }

        StockInFinOrderDto stockInFinOrderDto = new StockInFinOrderDto();
        stockInFinOrderDto.setMaterialNo(taskInfoDto.getMaterialNo());
        stockInFinOrderDto.setContainerNo(taskInfoDto.getContainerNo());
        stockInFinOrderDto.setFactoryCode(taskInfoDto.getFactoryCode());
        stockInFinOrderDto.setFactoryName(factoryList.get(0).getFactoryName());
        stockInFinOrderDto.setFactoryId(factoryList.get(0).getId());
        stockInFinOrderDto.setSourceLocationCode(taskInfoDto.getSourceLocation());
        stockInFinOrderDto.setMaterialName(materialList.get(0).getMaterialName());
        stockInFinOrderDto.setMaterialId(materialList.get(0).getMaterialId());
        stockInFinOrderDto.setLocationCode(taskInfoDto.getLocation());
        stockInFinOrderDto.setLocationId(locationList.get(0).getId());
        stockInFinOrderDto.setLocationName(locationList.get(0).getLocationDesc());
        stockInFinOrderDto.setFlag(true);
        stockInFinOrderDto.setStockInFinOrderDetailDtoList(dList);
        stockInFinOrderService.insertStockInFinOrder(stockInFinOrderDto);
        // 根据容器号获取成品收货信息
        TaskInfoDto firstTaskInfo = new TaskInfoDto();
        firstTaskInfo.setContainerNo(taskInfoDto.getContainerNo());
        List<TaskInfoVo> taskList = taskInfoMapper.selectTaskInfoFinList(firstTaskInfo);
        if(CollectionUtils.isEmpty(taskList)){
            throw new ServiceException(String.format("容器号：%s，成品收货任务不存在！",taskInfoDto.getContainerNo()));
        }
        // 成品收货提交处理
        firstTaskInfo = new TaskInfoDto();
        firstTaskInfo.setId(taskList.get(0).getId());
        firstTaskInfo.setAddress(taskInfoDto.getAddress());
        firstTaskInfo.setLocation(taskInfoDto.getLocation());
        firstTaskInfo.setReceivedQty(taskInfoDto.getTotalQty());
        firstTaskInfo.setContainerNo(taskInfoDto.getContainerNo());
        submitFinOrderTask(firstTaskInfo);
        return 1;
    }

    /**
     * 同步激活成品收货单、明细、任务(pda进入成品收货页面后触发)
     * @param id 任务id
     * @return 结果
     */
    @Override
    @Lock4j(keys = {"#id"},acquireTimeout = 10,expire = 10000)
    public int activeFin(Long id) {
        if(id == null){
            throw new ServiceException("参数不存在！");
        }
        //查询任务
        TaskInfo taskInfo = taskInfoMapper.selectTaskInfoById(id);
        //激活成品收货单据明细
        StockInFinOrderDetail stockInFinOrderDetail = new StockInFinOrderDetail();
        stockInFinOrderDetail.setId(taskInfo.getDetailId());
        stockInFinOrderDetail.setDetailStatus(OrderStatusConstant.ORDER_STATUS_ACTIVE);
        stockInFinOrderDetail.setUpdateBy(SecurityUtils.getUsername());
        stockInFinOrderDetail.setUpdateTime(DateUtils.getNowDate());
        if (stockInFinOrderDetailMapper.updateStockInFinOrderDetail(stockInFinOrderDetail)<=0){
            throw new ServiceException("更新成品收货单明细失败！");
        }

        //激活成品收货单
        StockInFinOrder stockInFinOrder = new StockInFinOrder();
        stockInFinOrder.setOrderNo(taskInfo.getStockInOrderNo());
        stockInFinOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_ACTIVE);
        stockInFinOrder.setUpdateBy(SecurityUtils.getUsername());
        stockInFinOrder.setUpdateTime(DateUtils.getNowDate());
        if (stockInFinOrderMapper.updateStockInFinOrder(stockInFinOrder)<=0){
            throw new ServiceException("更新成品收货单失败！");
        }
        return 1;
    }

    /**
     * 查询半成品收货任务列表
     * @param taskInfoDto 入库任务dto
     * @return 入库任务vo集合
     */
    @Override
    public List<TaskInfoVo> getSemiFinTaskList(TaskInfoDto taskInfoDto) {
        return taskInfoMapper.getSemiFinTaskList(taskInfoDto);
    }

    /**
     * 同步激活半成品收货单(pda进入半成品收货页面后触发)
     * @param id 任务id
     * @return 结果
     */
    @Override
    @Lock4j(keys = {"#id"},acquireTimeout = 10,expire = 10000)
    public int activeSemiFinTask(Long id) {
        if(id == null){
            throw new ServiceException("参数不存在！");
        }
        //查询任务
        TaskInfo taskInfo = taskInfoMapper.selectTaskInfoById(id);
        //激活半成品收货单
        StockInSemiFinOrder stockInSemiFinOrder = new StockInSemiFinOrder();
        stockInSemiFinOrder.setOrderNo(taskInfo.getStockInOrderNo());
        stockInSemiFinOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_ACTIVE);
        stockInSemiFinOrder.setUpdateBy(SecurityUtils.getUsername());
        stockInSemiFinOrder.setUpdateTime(DateUtils.getNowDate());
        if (stockInSemiFinOrderMapper.updateStockInSemiFinOrder(stockInSemiFinOrder)<=0){
            throw new ServiceException("更新半成品收货单失败！");
        }

        //激活半成品收货单
        StockInSemiFinOrderDetail stockInSemiFinOrderDetail = new StockInSemiFinOrderDetail();
        stockInSemiFinOrderDetail.setId(taskInfo.getDetailId());
        stockInSemiFinOrderDetail.setStatus(OrderStatusConstant.ORDER_STATUS_ACTIVE);
        stockInSemiFinOrderDetail.setUpdateBy(SecurityUtils.getUsername());
        stockInSemiFinOrderDetail.setUpdateTime(DateUtils.getNowDate());
        if (stockInSemiFinOrderDetailMapper.updateStockInSemiFinOrderDetail(stockInSemiFinOrderDetail)<=0){
            throw new ServiceException("更新成品收货单失败！");
        }
        return 1;
    }

    /**
     * 获取半成品收货任务(pda)
     * @param id 半成品收货任务id
     * @return 入库任务vo
     */
    @Override
    public TaskInfoVo getSemiFinTaskById(Long id) {
        return taskInfoMapper.getSemiFinTaskById(id);
    }

    /**
     * 半成品收货任务提交(pda)
     * @param taskInfoDto 入库任务dto
     * @return 结果
     */
    @Override
    @GlobalTransactional
    @Lock4j(keys = {"#taskInfoDto.id"},acquireTimeout = 10,expire = 10000)
    public int submitSemiFinOrderTask(TaskInfoDto taskInfoDto) throws Exception {
        //参数校验
        if (ObjectUtils.isEmpty(taskInfoDto)) {
            throw new ServiceException(ErrorMessage.PARAMS_NOTHING_STR);
        }
        //根据收货库位(仓位code) 库存地点code 获取工厂信息、库存信息、区域信息、仓位信息
        StoragePositionDto storagePositionDto = new StoragePositionDto();
        storagePositionDto.setLocationCode(taskInfoDto.getLocation());
        storagePositionDto.setPositionNo(taskInfoDto.getAddress());
        storagePositionDto.setFactoryCode(SecurityUtils.getComCode());
        AjaxResult ajaxResult = iMainDataService.getInfoListLike(storagePositionDto);
        if (ajaxResult.isError()) {
            throw new ServiceException(ajaxResult.get("msg").toString());
        }
        List<StoragePositionVo> list = JSONObject.parseArray(ajaxResult.get("data").toString(), StoragePositionVo.class);
        if (ObjectUtils.isEmpty(list)) {
            throw new ServiceException(ErrorMessage.POSITION_CODE_NOTHING);
        }
        if (list.size() > 1) {
            throw new ServiceException("收货库位或仓位有误！");
        }
        StoragePositionVo storagePositionVo = list.get(0);
        Long id = taskInfoDto.getId();
        //查询任务
        TaskInfoVo taskInfoVo = taskInfoMapper.getSemiFinTaskById(id);
        taskInfoDto.setMaterialNo(taskInfoVo.getMaterialNo());
        taskInfoDto.setMaterialName(taskInfoVo.getMaterialName());
        taskInfoDto.setOldMaterialNo(taskInfoVo.getOldMaterialNo());
        // 获取参数配置
        String defaultBatchNo = remoteConfigHelper.getConfig(ConfigConstant.DEFAULT_SAP_BATCH);
        if("".equals(Objects.toString(defaultBatchNo,""))){
            throw new ServiceException("配置参数SAP默认批次获取失败！");
        }
        taskInfoDto.setProductionLot(defaultBatchNo);
        //更新台账
        SpringUtils.getBean(VHTaskInfoServiceImpl.class).updateInventoryDetailsProduct(taskInfoDto,taskInfoVo, storagePositionVo);

        Long detailId = taskInfoDto.getDetailId();
        StockInStdOrderDetailDto stdOrderDetail = new StockInStdOrderDetailDto();
        stdOrderDetail.setId(detailId);
        List<StockInStdOrderDetail> stockInStdOrderDetails = stockInStdOrderDetailMapper.selectStockInStdOrderDetailList(stdOrderDetail);

        if(CollectionUtils.isEmpty(stockInStdOrderDetails)) {
            throw new ServiceException("标准入库单明细不存在！");
        }

        //更新半成品收货单据、明细、任务
        SpringUtils.getBean(VHTaskInfoServiceImpl.class).updateSemiFin(taskInfoVo);
        //LD半成品收货sap过账
        SapPurchaseParamsDto firstTaskInfoDto = new SapPurchaseParamsDto();
        firstTaskInfoDto.setMaterialNo(taskInfoVo.getMaterialNo());
        firstTaskInfoDto.setFactoryCode(taskInfoVo.getFactoryCode());
        firstTaskInfoDto.setLotNo(taskInfoVo.getLotNo());
        firstTaskInfoDto.setMoveType(stockInStdOrderDetails.get(0).getBwart());
        firstTaskInfoDto.setLocationCode(taskInfoVo.getLocationCode());
        firstTaskInfoDto.setReceivedQty(taskInfoVo.getPlanRecivevQty());
        firstTaskInfoDto.setUnit(taskInfoVo.getUnit());
        firstTaskInfoDto.setIsConsign(CommonYesOrNo.NO);
        firstTaskInfoDto.setIsQc(CommonYesOrNo.NO);
        firstTaskInfoDto.setTaskType(taskInfoVo.getTaskType());
        firstTaskInfoDto.setPrdOrderNo(taskInfoVo.getPrdOrderNo());
        String voucherNo = sendToSap(firstTaskInfoDto);
        //同步添加任务记录
        // 添加任务记录
        //addTaskRecord(taskInfoVo, storagePositionVo, taskInfoDto, voucherNo,s);
        return 1;
    }

    public void updateSemiFin(TaskInfoVo taskInfoVo) {
        //完成任务
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setId(taskInfoVo.getId());
        taskInfo.setTaskStatus(TaskStatusConstant.TASK_STATUS_COMPLETE);
        taskInfo.setHandlerUserId(SecurityUtils.getUserId());
        taskInfo.setHandlerUserName(SecurityUtils.getUsername());
        taskInfo.setUpdateTime(DateUtils.getNowDate());
        taskInfo.setUpdateBy(SecurityUtils.getUsername());
        if (taskInfoMapper.updateTaskInfo(taskInfo) <= 0){
            throw new ServiceException("更新成品收货任务失败！");
        }
        //完成半成品收货单
        StockInSemiFinOrder stockInSemiFinOrder = new StockInSemiFinOrder();
        stockInSemiFinOrder.setOrderNo(taskInfoVo.getStockInOrderNo());
        stockInSemiFinOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_COMPLETE);
        stockInSemiFinOrder.setUpdateBy(SecurityUtils.getUsername());
        stockInSemiFinOrder.setUpdateTime(DateUtils.getNowDate());
        stockInFinOrderMapper.updateStockInSemiFinOrderByAllComplete(stockInSemiFinOrder);
        //完成半成品收货单
        StockInFinOrderDetail stockInFinOrderDetail = new StockInFinOrderDetail();
        stockInFinOrderDetail.setId(taskInfoVo.getDetailId());
        stockInFinOrderDetail.setDetailStatus(OrderStatusConstant.ORDER_STATUS_COMPLETE);
        stockInFinOrderDetail.setUpdateBy(SecurityUtils.getUsername());
        stockInFinOrderDetail.setUpdateTime(DateUtils.getNowDate());
        stockInFinOrderDetailMapper.updateStockInFinOrderDetail(stockInFinOrderDetail);
    }

    /**
     * 更新台账 成品/半成品
     *
     * @param taskInfoDto,
     * @param storagePositionVo
     * @param taskInfoVo
     * @return inventoryId 台账id
     */
    public Long updateInventoryDetailsProduct(TaskInfoDto taskInfoDto,TaskInfoVo taskInfoVo, StoragePositionVo storagePositionVo) {
        StockInFinOrder stockInFinOrder = stockInFinOrderMapper.selectStockInFinOrderById(taskInfoVo.getDetailId());
        //新增台账
        InventoryDetailsDto inventoryDetailsDto = new InventoryDetailsDto();
        inventoryDetailsDto.setMaterialNo(stockInFinOrder.getMatnr());
        inventoryDetailsDto.setMaterialName(stockInFinOrder.getMaktx());
        inventoryDetailsDto.setPositionNo(storagePositionVo.getPositionNo());
        inventoryDetailsDto.setPositionId(storagePositionVo.getId());
        inventoryDetailsDto.setAreaId(storagePositionVo.getAreaId());
        inventoryDetailsDto.setAreaCode(storagePositionVo.getAreaCode());
        inventoryDetailsDto.setLocationId(storagePositionVo.getLocationId());
        inventoryDetailsDto.setLocationCode(storagePositionVo.getLocationCode());
        inventoryDetailsDto.setFactoryId(storagePositionVo.getFactoryId());
        inventoryDetailsDto.setProductionLot(taskInfoDto.getProductionLot());
        inventoryDetailsDto.setFactoryCode(storagePositionVo.getFactoryCode());
        inventoryDetailsDto.setFactoryName(storagePositionVo.getFactoryName());
        inventoryDetailsDto.setInventoryQty(new BigDecimal(stockInFinOrder.getMenge()));
        inventoryDetailsDto.setAvailableQty(new BigDecimal(stockInFinOrder.getMenge()));
        inventoryDetailsDto.setUnit(stockInFinOrder.getMeins());
        inventoryDetailsDto.setPreparedQty(BigDecimal.ZERO);
        inventoryDetailsDto.setOperationPreparedQty(BigDecimal.ZERO);
        inventoryDetailsDto.setStockInDate(DateUtils.getNowDate());
        inventoryDetailsDto.setStockInLot(stockInFinOrder.getCharg());
        inventoryDetailsDto.setContainerNo(stockInFinOrder.getExidv2());
        inventoryDetailsDto.setIsFreeze(CommonYesOrNo.NO);
        inventoryDetailsDto.setIsConsign(CommonYesOrNo.NO);
        inventoryDetailsDto.setIsQc(CommonYesOrNo.NO);
        inventoryDetailsDto.setWarehouseId(storagePositionVo.getWarehouseId());
        inventoryDetailsDto.setWarehouseCode(storagePositionVo.getWarehouseCode());
        AjaxResult add = inStoreService.addFin(inventoryDetailsDto);
        if (add.isError()) {
            throw new ServiceException(add.get("msg").toString());
        }
        return Long.parseLong(add.get("data").toString());
    }

    /**
     * 新增任务记录
     * @param taskInfoVo 仓管任务Vo
     * @param storagePositionVo 仓位Vo
     * @param taskInfoDto 仓管任务Dto
     * @param voucherNo 凭证
     * @date 2024/05/24
     * @author fsc
     */
    private void addTaskRecord(TaskInfoVo taskInfoVo, StoragePositionVo storagePositionVo, TaskInfoDto taskInfoDto,
                               String voucherNo,StockInFinOrder stockInFinOrder){
        //同步添加任务记录
        TaskLog taskLog = new TaskLog();
        taskLog.setTaskNo(taskInfoVo.getTaskNo());
        taskLog.setTaskType(taskInfoVo.getTaskType());
        taskLog.setMaterialNo(taskInfoVo.getMaterialNo());
        taskLog.setMaterialName(taskInfoVo.getMaterialName());
        taskLog.setOldMaterialNo(taskInfoVo.getOldMaterialNo());
        taskLog.setLot(stockInFinOrder.getCharg());
        taskLog.setQty(new BigDecimal(stockInFinOrder.getMenge()));
        taskLog.setUnit(stockInFinOrder.getMeins());
        taskLog.setFactoryCode(storagePositionVo.getFactoryCode());
        if (!ObjectUtils.isEmpty(taskInfoDto.getSourceLocationCode())) {
            taskLog.setLocationCode(taskInfoDto.getSourceLocationCode());
        }
        taskLog.setOrderType(TaskLogTypeConstant.FINISHED_AND_SEMIFINISHED);
        taskLog.setTargetFactoryCode(storagePositionVo.getFactoryCode());
        taskLog.setLocationCode(stockInFinOrder.getLgort());
        taskLog.setPositionCode(taskInfoVo.getSourcePositionNo());
        taskLog.setTargetLocationCode(storagePositionVo.getLocationCode());
        taskLog.setTargetAreaCode(storagePositionVo.getAreaCode());
        taskLog.setTargetPositionCode(storagePositionVo.getPositionNo());
        taskLog.setOrderNo(taskInfoVo.getStockInOrderNo());
        taskLog.setSupplierName(taskInfoDto.getVendorName());
        taskLog.setSupplierCode(taskInfoDto.getVendorCode());
        taskLog.setIsFreeze(CommonYesOrNo.NO);
        taskLog.setIsConsign(CommonYesOrNo.NO);
        taskLog.setIsQc(CommonYesOrNo.NO);
        taskLog.setMaterialCertificate(voucherNo);
        if (taskService.add(taskLog).isError()) {
            // 物料凭证冲销
//            if(!StringUtils.isBlank(voucherNo)){
//                materialReversal(voucherNo);
//            }
            throw new ServiceException("新增任务记录失败！");
        }
    }

    /**
     * 根据任务号获取数据
     * @param taskNo
     * @return
     */
    @Override
    public TaskInfo getTaskInfoListByTastNo(String taskNo) {
        return taskInfoMapper.getTaskInfoListByTaskNo(taskNo);
    }

    /**
     * 新增水位日志
     * @param wmsKbAndSwTaskLog
     * @return
     */
    @Override
    public int addKbAndSwTaskLog(WmsKbAndSwTaskLog wmsKbAndSwTaskLog) {
        return wmsKbAndSwTaskLogMapper.insertWmsKbAndSwTaskLog(wmsKbAndSwTaskLog);
    }

    /**
     * 交货单出库任务删除
     * @param taskInfo
     * @return
     */
    @Override
    public int deleteByOrderNoAndType(TaskInfo taskInfo) {
        return taskInfoMapper.deleteByOrderNoAndType(taskInfo);
    }


    /**
     * 按任务和仓位更新库存台账信息
     * @param taskInfoDto
     * @param storagePositionVo
     * @return
     */
    public Long updateInventoryDetailsRaw(TaskInfoDto taskInfoDto, StoragePositionVo storagePositionVo) {
        Date productTime = taskInfoDto.getProductTime();
        Date expireTime = taskInfoDto.getExpireTime();

        if (ObjectUtils.isEmpty(productTime)) {
            productTime = new Date();
        }
        if (ObjectUtils.isEmpty(expireTime)) {
            // 超期日期为空时，获取物料有效期重新计算：超期日期 = 生产日期 + 有效期间隔
            WmsMaterialBasicDto materialBasic =  new WmsMaterialBasicDto();
            materialBasic.setMaterialNoList(Collections.singletonList(taskInfoDto.getMaterialNo()));
            AjaxResult result = mainDataService.getMaterialArrInfo(materialBasic);
            if (result.isError()) {
                throw new ServiceException("获取物料信息失败，物料号: {}" + taskInfoDto.getMaterialNo());
            }
            List<WmsMaterialAttrParamsDto> materialAttList = com.alibaba.fastjson.JSONObject.parseArray(result.get("data").toString(), WmsMaterialAttrParamsDto.class);

            if(ObjectUtils.isEmpty(materialAttList) || ObjectUtils.isEmpty(materialAttList.get(0))) {
                throw new ServiceException("获取物料属性信息失败，物料号: {}" + taskInfoDto.getMaterialNo());
            }
            // 获取物料有效期
            String defaultValidityPeriod = materialAttList.get(0).getDefaultValidityPeriod();
            if(ObjectUtils.isEmpty(defaultValidityPeriod)) {
                throw new ServiceException("物料的默认有效期为空，请确认物料基础数据!");
            }
            expireTime = DateUtils.addDays(productTime, Integer.parseInt(defaultValidityPeriod));
        }

        //查询当前仓位是否已有台账存在
        InventoryDetailsVo inventoryDetailsVo = new InventoryDetailsVo();
        inventoryDetailsVo.setMaterialNo(taskInfoDto.getMaterialNo());
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
        inventoryDetailsVo.setStockInLot(taskInfoDto.getLotNo());
        inventoryDetailsVo.setIsConsign(taskInfoDto.getIsConsign());
        inventoryDetailsVo.setIsFreeze(CommonYesOrNo.NO);
        // 排除特殊库存
        inventoryDetailsVo.setStockSpecFlag(CommonYesOrNo.NO);
        inventoryDetailsVo.setIsQc(taskInfoDto.getIsQc());
        inventoryDetailsVo.setProductionLot(taskInfoDto.getRequirementContent());
        AjaxResult listResult = inStoreService.list(inventoryDetailsVo);
        if (listResult.isError()) {
            throw new ServiceException("查询库存台账失败！");
        }
        BigDecimal receivedQty = taskInfoDto.getReceivedQty();
        List<InventoryDetails> inventoryDetailsList = JSONObject.parseArray(listResult.get("data").toString(), InventoryDetails.class);
        Long inventoryId;
        if (ObjectUtils.isEmpty(inventoryDetailsList)) {
            //新增台账
            InventoryDetails inventoryDetails = new InventoryDetails();
            inventoryDetails.setMaterialNo(taskInfoDto.getMaterialNo());
            inventoryDetails.setMaterialName(taskInfoDto.getMaterialName());
            inventoryDetails.setOldMaterialNo(taskInfoDto.getOldMaterialNo());
            inventoryDetails.setPositionNo(storagePositionVo.getPositionNo());
            inventoryDetails.setPositionId(storagePositionVo.getId());
            inventoryDetails.setAreaId(storagePositionVo.getAreaId());
            inventoryDetails.setAreaCode(storagePositionVo.getAreaCode());
            inventoryDetails.setLocationId(storagePositionVo.getLocationId());
            inventoryDetails.setLocationCode(storagePositionVo.getLocationCode());
            inventoryDetails.setFactoryId(storagePositionVo.getFactoryId());
            inventoryDetails.setFactoryCode(storagePositionVo.getFactoryCode());
            inventoryDetails.setFactoryName(storagePositionVo.getFactoryName());
            inventoryDetails.setWarehouseId(storagePositionVo.getWarehouseId());
            inventoryDetails.setWarehouseCode(storagePositionVo.getWarehouseCode());
            inventoryDetails.setInventoryQty(receivedQty);
            inventoryDetails.setAvailableQty(receivedQty);
            inventoryDetails.setUnit(taskInfoDto.getUnit());
            inventoryDetails.setSupplierId(taskInfoDto.getVendorId());
            inventoryDetails.setSupplierCode(taskInfoDto.getVendorCode());
            inventoryDetails.setSupplierName(taskInfoDto.getVendorName());
            inventoryDetails.setStockInDate(DateUtils.getNowDate());
            inventoryDetails.setStockInLot(taskInfoDto.getLotNo());
            inventoryDetails.setProductionLot(taskInfoDto.getRequirementContent());
            inventoryDetails.setIsQc(taskInfoDto.getIsQc());
            inventoryDetails.setIsConsign(taskInfoDto.getIsConsign());
            inventoryDetails.setIsFreeze(CommonYesOrNo.NO);
            inventoryDetails.setIsReserved(CommonYesOrNo.NO);
            inventoryDetails.setStockSpecFlag(CommonYesOrNo.NO);
            inventoryDetails.setCreateBy(SecurityUtils.getUsername());
            inventoryDetails.setCreateTime(DateUtils.getNowDate());
            inventoryDetails.setUpdateBy(SecurityUtils.getUsername());
            inventoryDetails.setProductTime(productTime);
            inventoryDetails.setExpiryDate(expireTime);

            if (CommonYesOrNo.YES.equals(taskInfoDto.getIsQc())) {
                inventoryDetailsVo.setLocationType(SAPConstant.STOCK_TYPE_INSPECT);
            }
            AjaxResult result = inStoreService.add(inventoryDetails);
            if (result.isError()) {
                throw new ServiceException("新增库存台账失败！");
            }
            inventoryId = Long.parseLong(result.get("data").toString());
        } else if (inventoryDetailsList.size() == 1) {
            //修改台账
            InventoryDetails inventoryDetails = inventoryDetailsList.get(0);
            BigDecimal sumInventoryQty = inventoryDetails.getInventoryQty().add(taskInfoDto.getReceivedQty());
            BigDecimal sumAvailableQty = inventoryDetails.getAvailableQty().add(taskInfoDto.getReceivedQty());
            inventoryDetails.setInventoryQty(sumInventoryQty);
            inventoryDetails.setAvailableQty(sumAvailableQty);
            inventoryDetails.setUpdateBy(SecurityUtils.getUsername());
            inventoryDetails.setUpdateTime(DateUtils.getNowDate());
            inventoryDetails.setIsQc(taskInfoDto.getIsQc());
            if (CommonYesOrNo.YES.equals(taskInfoDto.getIsQc())) {
                inventoryDetailsVo.setLocationType(SAPConstant.STOCK_TYPE_INSPECT);
            }
            inventoryId = inventoryDetails.getId();
            if (inStoreService.edit(inventoryDetails).isError()) {
                throw new ServiceException("修改库存台账失败！");
            }
        } else {
            throw new ServiceException("库存台账不唯一！");
        }
        return inventoryId;
    }
}
