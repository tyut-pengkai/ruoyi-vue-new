package com.easycode.cloud.strategy.impl.vh.returnSubmit;

import com.easycode.cloud.domain.*;
import com.easycode.cloud.mapper.*;
import com.weifu.cloud.common.core.exception.ServiceException;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.weifu.cloud.common.core.utils.SpringUtils;
import org.apache.commons.lang3.StringUtils;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.security.utils.SecurityUtils;
import com.weifu.cloud.constant.*;
import com.weifu.cloud.domain.*;
import com.easycode.cloud.domain.dto.RetTaskDto;
import com.easycode.cloud.domain.dto.StockInOtherDto;
import com.weifu.cloud.domian.vo.StoragePositionVo;
import com.weifu.cloud.mapper.*;
import com.weifu.cloud.service.ICodeRuleService;
import com.easycode.cloud.service.impl.vh.VHRetTaskServiceImpl;
import com.easycode.cloud.strategy.IRetTaskStrategy;
import com.easycode.cloud.strategy.StockInCommonService;
import com.easycode.cloud.strategy.StockInSubmitStrategy;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @program: Weifu-WMS
 * @description: 生产订单发料退PDA提交策略类
 * @author: fangshucheng
 * @create: 2024-05-17 13:46
 **/
@Service
public class OtherRetSubmitStrategy implements StockInSubmitStrategy {


    @Autowired
    private Map<String, IRetTaskStrategy> map;

    @Autowired
    private RetTaskMapper retTaskMapper;

    @Resource
    private StockInCommonService stockInCommonService;

    @Autowired
    private InspectOrderMapper inspectOrderMapper;

    @Autowired
    private InspectOrderDetailsMapper inspectOrderDetailsMapper;

    @Autowired
    private StockInOtherDetailMapper stockInOtherDetailMapper;

    @Autowired
    private StockInOtherMapper stockInOtherMapper;

    @Autowired
    private ICodeRuleService iCodeRuleService;

    @Override
    public Boolean checkType(String type) {
        return Objects.equals(type, StockInTaskConstant.OTHER_TYPE);
    }

    @Override
    public int submit(RetTaskDto retTaskDto, StoragePositionVo storagePositionVo) throws Exception {
        // 物料凭证
        String voucherNo = "";
        // 单据号
        String orderNoLog;
        // 行号
        String lineNoLog;
        //查询提交前任务
        RetTask beRetTask = retTaskMapper.selectRetTaskById(retTaskDto.getId());
        // 校验数量
        int flag = beRetTask.getQty().subtract(beRetTask.getCompleteQty()).compareTo(retTaskDto.getQty());
        if (flag < 0){
            throw new ServiceException("当前任务数量需<=未完成数量!");
        }

        //更新退货任务
        RetTask retTask = new RetTask();
        retTask.setId(retTaskDto.getId());
        retTask.setStorageLocationCode(retTaskDto.getStorageLocationCode());
        retTask.setAreaCode(retTaskDto.getAreaCode());
        retTask.setPositionNo(retTaskDto.getConfirmPosition());
        retTask.setCompleteQty(retTaskDto.getQty().add(beRetTask.getCompleteQty()));
        retTask.setOperationCompleteQty(retTaskDto.getOperationCompleteQty().add(beRetTask.getOperationCompleteQty()));
        retTask.setLot(retTaskDto.getLot());
        retTask.setTaskStatus(flag == 0 ? TaskStatusConstant.TASK_STATUS_COMPLETE : TaskStatusConstant.TASK_STATUS_PART_COMPLETE);
        retTask.setUpdateBy(SecurityUtils.getUsername());
        retTask.setUpdateTime(DateUtils.getNowDate());
        if (retTaskMapper.updateRetTask(retTask) <= 0) {
            throw new ServiceException("修改退货任务状态失败!");
        }

        // 其他收货
        // 明细信息
        StockInOtherDetail detail = stockInOtherDetailMapper.selectStockInOtherDetailById(retTaskDto.getDetailId());
        if (detail == null) {
            throw new ServiceException("其他收货获取明细信息失败！");
        }
        // 单据信息
        StockInOtherDto stockInOtherDto = new StockInOtherDto();
        stockInOtherDto.setOrderNo(detail.getOrderNo());
        List<StockInOther> orderList = stockInOtherMapper.selectStockInOtherList(stockInOtherDto);
        if (orderList == null || orderList.isEmpty()) {
            throw new ServiceException("其他收货获取单据信息失败！");
        }
        orderNoLog = detail.getOrderNo();
        lineNoLog = detail.getLineNo();
        //是否质检 跟着其他入库单明细走
        retTaskDto.setIsQc(detail.getIsQc());
        //设置供应商信息
        retTaskDto.setSupplierCode(detail.getSupplierCode());
        //更新台账
        Long inventoryId = SpringUtils.getBean(VHRetTaskServiceImpl.class).updateInventoryDetails(retTaskDto, storagePositionVo);
        if ("503".equals(orderList.get(0).getBillType()) && CommonYesOrNo.YES.equals(detail.getIsQc())) {
            Long tenantId = SecurityUtils.getLoginUser().getSysUser().getTenantId();

            // 创建送检单单号
            AjaxResult result = iCodeRuleService.getSeqWithTenantId(OrderNoTypeConstant.INSPECT, String.valueOf(tenantId));
            if (result.isError() || StringUtils.isEmpty(result.get("data").toString())) {
                throw new ServiceException("送检单单号生成失败！");
            }
            String orderNo = result.get("data").toString();
            // 创建质检单
            InspectOrder inspectOrder = new InspectOrder();
            inspectOrder.setOrderNo(orderNo);
            inspectOrder.setBillStatus(InspectOrderStatusConstant.ORDER_STATUS_NEW);
            inspectOrder.setFactoryCode(detail.getFactoryCode());
            inspectOrder.setIsConsign(detail.getIsConsigned());
            inspectOrder.setLocationCode(retTaskDto.getStorageLocationCode());
            inspectOrder.setMaterialNo(detail.getMaterialNo());
            inspectOrder.setMaterialName(detail.getMaterialName());
            inspectOrder.setReceiveDate(DateUtils.getNowDate());
            inspectOrder.setReceiveOrderNo(orderList.get(0).getOrderNo());
            inspectOrder.setSupplierCode(detail.getSupplierCode());
            inspectOrder.setCreateBy(SecurityUtils.getUsername());
            inspectOrder.setCreateTime(DateUtils.getNowDate());
            inspectOrderMapper.insertInspectOrder(inspectOrder);
            // 送检单详情
            InspectOrderDetails inspectOrderDetails = new InspectOrderDetails();
            inspectOrderDetails.setOrderNo(orderNo);
            inspectOrderDetails.setLineNo(detail.getLineNo());
            inspectOrderDetails.setMaterialNo(detail.getMaterialNo());
            inspectOrderDetails.setQcQty(retTaskDto.getQty());
            inspectOrderDetails.setLot(retTaskDto.getLot());
            inspectOrderDetails.setPrdLot(retTaskDto.getLot());
            inspectOrderDetails.setPositionNo(retTaskDto.getConfirmPosition());
            inspectOrderDetails.setUnit(retTaskDto.getUnit());
            inspectOrderDetails.setCreateBy(SecurityUtils.getUsername());
            inspectOrderDetails.setCreateTime(DateUtils.getNowDate());
            inspectOrderDetails.setOperationUnit(retTaskDto.getOperationUnit());
            inspectOrderDetails.setInventoryId(inventoryId);
            inspectOrderDetailsMapper.insertInspectOrderDetails(inspectOrderDetails);
        } else {
            // 上架任务
            stockInCommonService.createShelfTask(retTaskDto, orderList.get(0).getOrderNo(), inventoryId);
        }
        //更新退货单（其他收货）
        IRetTaskStrategy iRetTaskStrategy = map.get(retTaskDto.getTaskType());
        if (ObjectUtils.isEmpty(iRetTaskStrategy)){
            throw new ServiceException(ErrorMessage.UPDATEE_ORDER_STRATEGY_NOTHING);
        }
        if (iRetTaskStrategy.updateRetOrder(retTaskDto) <= 0){
            throw new ServiceException(ErrorMessage.UPDATEE_ORDER_FAIL);
        }
        // sap过账
        voucherNo = SpringUtils.getBean(VHRetTaskServiceImpl.class).stockInOtherToSap(retTaskDto, detail, orderList.get(0));

        // 上架任务
        stockInCommonService.createShelfTask(retTaskDto, orderList.get(0).getOrderNo(), inventoryId);

        stockInCommonService.addTaskLog(retTaskDto, orderNoLog, lineNoLog, voucherNo);
        return HttpStatus.SC_OK;
    }

}
