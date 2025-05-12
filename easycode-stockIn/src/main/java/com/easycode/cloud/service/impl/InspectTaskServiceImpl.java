package com.easycode.cloud.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.easycode.cloud.domain.InspectOrder;
import com.easycode.cloud.domain.dto.DeliveryInspectionTaskDto;
import com.easycode.cloud.domain.vo.DeliveryInspectionTaskVo;
import com.easycode.cloud.domain.vo.InspectOrderDetailsVo;
import com.weifu.cloud.common.core.exception.ServiceException;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.security.utils.SecurityUtils;
import com.weifu.cloud.constant.SQConstant;
import com.weifu.cloud.constant.SapReqOrResConstant;
import com.weifu.cloud.domain.*;
import com.weifu.cloud.domian.WmsMaterialDeliverAttr;
import com.weifu.cloud.domian.WmsPrinterLocation;
import com.weifu.cloud.domian.dto.HiprintTemplateDTO;
import com.weifu.cloud.domian.dto.PrintTOParamsDto;
import com.weifu.cloud.domian.dto.WmsMaterialAttrParamsDto;
import com.weifu.cloud.domian.dto.WmsMaterialBasicDto;
import com.easycode.cloud.mapper.DeliveryInspectionTaskMapper;
import com.easycode.cloud.mapper.DeliveryOrderMapper;
import com.easycode.cloud.mapper.InspectOrderDetailsMapper;
import com.easycode.cloud.mapper.InspectOrderMapper;
import com.easycode.cloud.service.IInspectTaskService;
import com.weifu.cloud.service.IMainDataService;
import com.weifu.cloud.service.IPrintService;
import org.apache.commons.compress.utils.Lists;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class InspectTaskServiceImpl implements IInspectTaskService {

    @Autowired
    private IPrintService printService;

    @Autowired
    private DeliveryInspectionTaskMapper deliveryInspectionTaskMapper;

    @Autowired
    private InspectOrderDetailsMapper inspectOrderDetailsMapper;

    @Autowired
    private DeliveryOrderMapper deliveryOrderMapper;

    @Autowired
    private IMainDataService iMainDataService;

    @Autowired
    private InspectOrderMapper inspectOrderMapper;

    @Override
    public List<DeliveryInspectionTaskVo> selectInspectTaskList(DeliveryInspectionTaskDto deliveryInspectionTask) {
        return deliveryInspectionTaskMapper.selectInspectTaskList(deliveryInspectionTask);
    }

    @Override
    public List<DeliveryInspectionTaskVo> getAllInspectionTask(DeliveryInspectionTaskDto deliveryInspectionTask) {
        return deliveryInspectionTaskMapper.getAllInspectionTask(deliveryInspectionTask);
    }

    /**
     * 打印检验单
     *
     * @param inspectionTask
     * @return
     */
    @Override
    public JSONObject printInspectTask(DeliveryInspectionTaskDto inspectionTask) {
        List<DeliveryInspectionTaskVo> inspectionTaskList = deliveryInspectionTaskMapper.selectInspectTaskList(inspectionTask);
        DeliveryInspectionTaskVo deliveryTask = inspectionTaskList.get(0);
        List<InspectOrderDetailsVo> inspectOrderDetails = inspectOrderDetailsMapper.selectInspectOrderDetailsListByOrderNo(deliveryTask.getOrderNo());

        InspectOrder inspectOrder = inspectOrderMapper.selectInspectOrderByNo(inspectOrderDetails.get(0).getOrderNo());
        String supplierName = null ;
        if (!ObjectUtils.isEmpty(inspectOrder)){
             supplierName = inspectOrder.getSupplierName();
        }
        List<String> materialNoList = Lists.newArrayList();
        WmsMaterialBasicDto materialBasicDto = new WmsMaterialBasicDto();
        materialNoList.add(deliveryTask.getMaterialNo());
        materialBasicDto.setMaterialNoList(materialNoList);
        AjaxResult ajaxResult = iMainDataService.getMaterialArrInfo(materialBasicDto);
        if (ajaxResult.isError()) {
            throw new ServiceException(ajaxResult.get("msg").toString());
        }
        List<WmsMaterialAttrParamsDto> list = JSONObject.parseArray(ajaxResult.get("data").toString(), WmsMaterialAttrParamsDto.class);
        if (CollectionUtils.isEmpty(list)) {
            throw new ServiceException("物料属性表中未查询到物料相关信息！");
        }

        WmsMaterialDeliverAttr dto = new WmsMaterialDeliverAttr();
        dto.setMaterialNo(deliveryTask.getMaterialNo());
        AjaxResult ajaxResult1 = iMainDataService.getDeliverAttr(dto);
        if (ajaxResult.isError()) {
            throw new ServiceException("物料包装信息未配置，物料号:" + inspectionTask.getMaterialNo() + ",请维护相关数据");
        }

        List<WmsMaterialDeliverAttr> deliverAttrList = com.alibaba.fastjson.JSONObject.parseArray(ajaxResult1.get("data").toString(), WmsMaterialDeliverAttr.class);

        PrintTOParamsDto printTOParamsDto = new PrintTOParamsDto();
        printTOParamsDto.setLot(deliveryTask.getLot());
        printTOParamsDto.setFactoryCode(deliveryTask.getFactoryCode());
        printTOParamsDto.setLocationCode(deliveryTask.getLocationCode());
        printTOParamsDto.setPositionNo(deliveryTask.getPositionNo());
        printTOParamsDto.setMoveQty(deliveryTask.getQty());
        printTOParamsDto.setPackQty(deliveryTask.getQty());
        printTOParamsDto.setMaterialName(deliveryTask.getMaterialName());
        printTOParamsDto.setMaterialNo(deliveryTask.getMaterialNo());
        printTOParamsDto.setTaskType("质检任务单");
//        printTOParamsDto.setSourcePostionNo(deliveryTaskVo.getPositionNo());
//        printTOParamsDto.setSourceLocationCode(deliveryTaskVo.getLocationCode());
//        printTOParamsDto.setSourceStorageType(deliveryTaskVo.getLocationCode());
        printTOParamsDto.setTaskNo(deliveryTask.getTaskNo());
        printTOParamsDto.setInspectNo(deliveryTask.getInspectOrderNo());
        printTOParamsDto.setOldMaterialNo(deliverAttrList.get(0).getOldMaterialNo());
        printTOParamsDto.setSourceStorageType(printTOParamsDto.getLocationCode());
        printTOParamsDto.setUserName(SecurityUtils.getUsername());
//        AjaxResult ajaxResult = printService.printTO(printTOParamsDto);
//        if (ajaxResult.isError()) {
//            throw new ServiceException(String.format("打印失败,原因：%s", ajaxResult.get("msg").toString()));
//        }

        printTOParamsDto.setCompleteTime(DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss",DateUtils.getNowDate()));
        printTOParamsDto.setVendorName(supplierName);
        printTOParamsDto.setMoveType(SapReqOrResConstant.MOVE_LOCATION_MOVE_TYPE_Q);
        printTOParamsDto.setAge(list.get(0).getDefaultValidityPeriod());
        printTOParamsDto.setVendorLot(inspectOrderDetails.get(0).getLot());
        printTOParamsDto.setPositionNo(deliveryTask.getPositionNoPrint());
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
}
