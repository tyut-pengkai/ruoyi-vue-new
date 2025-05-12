package com.easycode.cloud.strategy.impl.vh.returnSubmit;

import com.easycode.cloud.domain.CostCenterReturnOrder;
import com.easycode.cloud.domain.CostCenterReturnOrderDetail;
import com.easycode.cloud.domain.ShelfTask;
import com.easycode.cloud.domain.dto.RetTaskDto;
import com.easycode.cloud.service.IShelfTaskService;
import com.easycode.cloud.strategy.StockInCommonService;
import com.soa.eis.adapter.framework.message.IMsgObject;
import com.soa.eis.adapter.framework.message.impl.GroupRecord;
import com.soa.eis.adapter.framework.message.impl.MsgObject;
import com.weifu.cloud.common.core.exception.ServiceException;
import com.weifu.cloud.common.core.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.security.utils.SecurityUtils;
import com.weifu.cloud.constant.*;
import com.weifu.cloud.domain.dto.SapCostCenterReturnDto;
import com.weifu.cloud.domain.dto.SapMoveLocationParamsDto;
import com.weifu.cloud.domian.TaskLog;
import com.weifu.cloud.enums.RemoteConfigEnum;
import com.weifu.cloud.service.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @program: Weifu-WMS
 * @description: 入库退货提交策略实现类
 * @author: fangshucheng
 * @create: 2024-05-17 14:39
 **/
@Service
public class StockInCommonServiceImpl implements StockInCommonService {


    @Autowired
    private RemoteEsbSendService remoteEsbSendService;

    @Autowired
    private ITaskService taskService;

    @Autowired
    private RemoteConfigHelper remoteConfigHelper;

    @Autowired
    private ICodeRuleService iCodeRuleService;

    @Autowired
    private IShelfTaskService shelfTaskService;

    @Autowired
    private SapInteractionService sapInteractionService;

    /**
     * 物料凭证冲销
     */
    @Override
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
        String errorMsg = resMo.getResValue("ERRORMSG");
        String status = resMo.getResValue("STATUS");
        if (!SapReqOrResConstant.SAP_RETURN_TYPE.equals(status)) {
            throw new ServiceException("物料凭证冲销失败!" + errorMsg);
        }
    }

    @Override
    public void addTaskLog(RetTaskDto retTaskDto, String orderNoLog, String lineNoLog, String voucherNo) throws Exception {
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
        taskLog.setTargetFactoryCode(SecurityUtils.getComCode());
        taskLog.setTargetLocationCode(retTaskDto.getStorageLocationCode());
        taskLog.setTargetAreaCode(retTaskDto.getAreaCode());
        taskLog.setTargetPositionCode(retTaskDto.getConfirmPosition());
        taskLog.setSupplierCode(retTaskDto.getSupplierCode());
        if (ObjectUtils.isNotEmpty(retTaskDto.getStorageLocation())) {
            taskLog.setLocationCode(retTaskDto.getStorageLocation());
        }
        taskLog.setOrderType(TaskLogTypeConstant.OTHER);
        taskLog.setOrderNo(orderNoLog);
        taskLog.setLineNo(lineNoLog);
        taskLog.setIsQc(retTaskDto.getIsQc());
        taskLog.setIsFreeze(CommonYesOrNo.NO);
        taskLog.setIsConsign(CommonYesOrNo.NO);
        taskLog.setSupplierCode(retTaskDto.getSupplierCode());
        taskLog.setMaterialCertificate(voucherNo);
        taskLog.setTenantId(retTaskDto.getTenantId());
        taskLog.setCreateTime(DateUtils.getNowDate());
        taskLog.setCreateBy(SecurityUtils.getUsername());
        if (taskService.add(taskLog).isError()) {
            if (!"".equals(voucherNo)) {
                this.materialReversal(voucherNo);
            }
            throw new ServiceException("新增任务记录失败！");
        }
    }

    /**
     * 成本中心sap过账
     */
    @Override
    public String costReturnToSap(RetTaskDto retTaskDto, CostCenterReturnOrderDetail detail, CostCenterReturnOrder order) throws Exception{
        String defaultBatchNo = remoteConfigHelper.getConfig(ConfigConstant.DEFAULT_SAP_BATCH);
        if("".equals(Objects.toString(defaultBatchNo,""))){
            throw new ServiceException("配置参数SAP默认批次获取失败！");
        }

        SapCostCenterReturnDto sp = new SapCostCenterReturnDto();
        Date now = new Date();
        sp.setBudat(DateFormatUtils.format(now, "yyyyMMdd"));
        sp.setBldat(DateFormatUtils.format(now, "yyyyMMdd"));
        sp.setBktxt("");
        sp.setuName(SecurityUtils.getUsername());

        // 物料
        sp.setMatnr(retTaskDto.getMaterialNo());
        sp.setPlant(order.getFactoryCode());
        sp.setMvtInd("");
        sp.setBwart(order.getMoveType());
        sp.setPoPrQnt(retTaskDto.getQty().toString());
        sp.setOrderPrUn(detail.getUnit());
        sp.setBatch(retTaskDto.getLot());
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
//        itemRecord.setFieldValue("BATCH", defaultBatchNo);
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
//            throw new ServiceException("sap成本中心退料过账失败!" + errorMsg);
//        }
//        // 凭证号
//        voucherNo = resMo.getResValue("MBLNR");
//        return voucherNo;
    }

    /**
     * 生成上架任务
     */
    @Override
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
    @Override
    public String moveLocationSap(RetTaskDto retTask, String sourceLocationCode) throws Exception {
        String defaultBatchNo = remoteConfigHelper.getConfig(ConfigConstant.DEFAULT_SAP_BATCH);
        if("".equals(Objects.toString(defaultBatchNo,""))){
            throw new ServiceException("配置参数SAP默认批次获取失败！");
        }

        SapMoveLocationParamsDto sp = new SapMoveLocationParamsDto();
        Date now = new Date();
        sp.setBudat(DateFormatUtils.format(now, "yyyyMMdd"));
        sp.setBldat(DateFormatUtils.format(now, "yyyyMMdd"));
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
//        itemRecord.setFieldValue("BATCH", defaultBatchNo);
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
//        itemRecord.setFieldValue("MOVE_BATCH", defaultBatchNo);
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
//        String errorMsg = resMo.getResValue("ERRORMSG");
//        String flag = resMo.getResValue("FLAG");
//        if (!SapReqOrResConstant.SAP_RETURN_TYPE.equals(flag)) {
//            throw new ServiceException("sap过账失败!原因：" + errorMsg);
//        }
//        // 凭证号
//        voucherNo = resMo.getResValue("MBLNR");
//        return voucherNo;
    }
}
