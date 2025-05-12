package com.easycode.cloud.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.easycode.cloud.domain.dto.StockInSemiFinOrderDetailDto;
import com.easycode.cloud.domain.dto.StockInSemiFinOrderDto;
import com.easycode.cloud.domain.vo.StockInSemiFinOrderDetailVo;
import com.weifu.cloud.common.core.exception.ServiceException;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.weifu.cloud.common.core.utils.SpringUtils;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.datascope.annotation.DataScope;
import com.weifu.cloud.common.security.utils.SecurityUtils;
import com.weifu.cloud.constant.*;
import com.easycode.cloud.domain.StockInSemiFinOrder;
import com.easycode.cloud.domain.StockInSemiFinOrderDetail;
import com.easycode.cloud.domain.TaskInfo;
import com.weifu.cloud.domian.dto.MaterialUnitDefDto;
import com.easycode.cloud.mapper.StockInSemiFinOrderDetailMapper;
import com.easycode.cloud.mapper.StockInSemiFinOrderMapper;
import com.easycode.cloud.mapper.TaskInfoMapper;
import com.weifu.cloud.service.ICodeRuleService;
import com.weifu.cloud.service.IMainDataService;
import com.easycode.cloud.service.IStockInSemiFinOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 半成品入库单Service业务层处理
 *
 * @author bcp
 * @date 2023-07-22
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StockInSemiFinOrderServiceImpl implements IStockInSemiFinOrderService
{
    @Autowired
    private StockInSemiFinOrderMapper stockInSemiFinOrderMapper;

    @Autowired
    private ICodeRuleService iCodeRuleService;

    @Autowired
    private StockInSemiFinOrderDetailMapper stockInSemiFinOrderDetailMapper;

    @Autowired
    private TaskInfoMapper taskInfoMapper;

    @Autowired
    private IMainDataService mainDataService;

    /**
     * 查询半成品入库单
     *
     * @param id 半成品入库单主键
     * @return 半成品入库单
     */
    @Override
    public StockInSemiFinOrder selectStockInSemiFinOrderById(Long id)
    {
        return stockInSemiFinOrderMapper.selectStockInSemiFinOrderById(id);
    }

    /**
     * 查询半成品入库单列表
     *
     * @param stockInSemiFinOrder 半成品入库单
     * @return 半成品入库单
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<StockInSemiFinOrder> selectStockInSemiFinOrderList(StockInSemiFinOrder stockInSemiFinOrder)
    {
        return stockInSemiFinOrderMapper.selectStockInSemiFinOrderList(stockInSemiFinOrder);
    }

    /**
     * 新增半成品入库单
     *
     * @param stockInSemiFinOrderDto 半成品入库单
     * @return 结果
     */
    @Override
    public StockInSemiFinOrder insertStockInSemiFinOrder(StockInSemiFinOrderDto stockInSemiFinOrderDto)
    {
        //明细集合
        List<StockInSemiFinOrderDetailDto> detailDtoList = stockInSemiFinOrderDto.getStockInSemiFinOrderDetailDtoList();
        if (ObjectUtils.isEmpty(detailDtoList)){
            throw new ServiceException("参数不存在！");
        }
        Map<String, List<StockInSemiFinOrderDetailDto>> group = detailDtoList.stream()
                .collect(Collectors.groupingBy(StockInSemiFinOrderDetailDto::getPrdOrderNo));

        StockInSemiFinOrder stockInSemiFinOrder = new StockInSemiFinOrder();
        BeanUtils.copyProperties(stockInSemiFinOrderDto, stockInSemiFinOrder);
        Long tenantId = SecurityUtils.getLoginUser().getSysUser().getTenantId();

        // 单据类型(默认成品)
        AjaxResult ajaxResult = iCodeRuleService.getSeqWithTenantId(OrderNoTypeConstant.SEMI_FIN_ORDER, String.valueOf(tenantId));
        if ("".equals(Objects.toString(ajaxResult.get("data"), "")) || ajaxResult.isError()) {
            throw new ServiceException("半成品收货单据号生成失败！");
        }
        String orderNo = ajaxResult.get("data").toString();
        stockInSemiFinOrder.setOrderNo(orderNo);
        stockInSemiFinOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_NEW);
        stockInSemiFinOrder.setTenantId(null);
        stockInSemiFinOrder.setDeptId(SecurityUtils.getLoginUser().getSysUser().getDeptId());
        stockInSemiFinOrder.setFactoryCode(SecurityUtils.getComCode());
        stockInSemiFinOrder.setCreateBy(SecurityUtils.getUsername());
        stockInSemiFinOrder.setCreateTime(DateUtils.getNowDate());
        if (stockInSemiFinOrderMapper.insertStockInSemiFinOrder(stockInSemiFinOrder)<=0){
            throw new ServiceException("新增半成品收货单失败！");
        }
        List<String> materialNoList = detailDtoList.stream().map(StockInSemiFinOrderDetailDto::getMaterialNo).collect(Collectors.toList());
        MaterialUnitDefDto materialUnitDefDto = new MaterialUnitDefDto();
        materialUnitDefDto.setMaterialNoList(materialNoList);
        materialUnitDefDto.setStockinEnable(CommonYesOrNo.YES);
        materialUnitDefDto.setFactoryCode(SecurityUtils.getComCode());
        AjaxResult unitResult = mainDataService.batchMaterialUnitDef(materialUnitDefDto);
        if (unitResult.isError()) {
            throw new ServiceException(unitResult.get("msg").toString());
        }
        List<MaterialUnitDefDto> unitList = JSONObject.parseArray(unitResult.get("data").toString(), MaterialUnitDefDto.class);
        if (ObjectUtils.isEmpty(unitList)) {
            throw new ServiceException(String.format("不存在库内使用单位，请维护相关数据"));
        }
        Map<String, BigDecimal> map = unitList.stream().collect(Collectors.toMap(MaterialUnitDefDto::getMaterialNo, MaterialUnitDefDto::getConversDefault));
        Map<String, String> unitMap = unitList.stream().collect(Collectors.toMap(MaterialUnitDefDto::getMaterialNo, MaterialUnitDefDto::getUnit));

        for (StockInSemiFinOrderDetailDto detailDto : detailDtoList) {
            StockInSemiFinOrderDetail detail = new StockInSemiFinOrderDetail();
            BeanUtils.copyProperties(detailDto, detail);
            detail.setSemifinOrderId(stockInSemiFinOrder.getId());
            detail.setIsPrinted(CommonYesOrNo.NO);
            detail.setCreateBy(SecurityUtils.getUsername());
            detail.setCreateTime(DateUtils.getNowDate());
            BigDecimal conversDefault = map.get(detail.getMaterialNo());
            String operationUnit = unitMap.get(detail.getMaterialNo());
            detail.setOperationPlanRecivevQty(detail.getPlanRecivevQty().divide(conversDefault, 4, RoundingMode.HALF_UP));
            detail.setOperationUnit(operationUnit);
            //新建单据 明细状态为为新建
            detail.setStatus(OrderStatusConstant.ORDER_STATUS_NEW);
            //TODO 成品收货单明细批次号 需要与LD确认 目前暂为 生产订单号+成品收货单行号 成品收货单行号暂为三位 前面补零
//            String lineNo = detailDto.getLineNo();
//            int integer = Integer.parseInt(lineNo);
//            if (integer <= 9){
//                detail.setLot(detailDto.getPrdOrderNo() + "000" + detailDto.getLineNo());
//            } else if (integer <= 99){
//                detail.setLot(detailDto.getPrdOrderNo() + "00" + detailDto.getLineNo());
//            } else if (integer <= 999){
//                detail.setLot(detailDto.getPrdOrderNo() + "0" + detailDto.getLineNo());
//            } else {
//                detail.setLot(detailDto.getPrdOrderNo() + detailDto.getLineNo());
//            }
            // 批次号
//            AjaxResult result = iCodeRuleService.getSeqWithCodeRule(TaskNoTypeConstant.SEMI_FIN_LOT_NO);
//            if ("".equals(Objects.toString(result.get("data"), "")) || result.isError()) {
//                throw new ServiceException("半成品收货批次号生成失败！");
//            }
            // 获取批次号

            String lotNo = stockInSemiFinOrderDetailMapper.getMaxLot(detailDto.getPrdOrderNo());
            lotNo = "".equals(Objects.toString(lotNo,"")) ? detailDto.getPrdOrderNo() + "0000" : lotNo;
            Integer serialNo = Integer.valueOf(lotNo.substring(lotNo.length()-4)) + 1;
            lotNo = detailDto.getPrdOrderNo() + String.format("%04d", serialNo);
            detail.setLot(lotNo);
            if (stockInSemiFinOrderDetailMapper.insertStockInSemiFinOrderDetail(detail) <= 0) {
                throw new ServiceException("新半成品收货单据失败！");
            }
            SpringUtils.getBean(StockInSemiFinOrderServiceImpl.class).addTaskInfo(detail,orderNo);
        }
        return stockInSemiFinOrder;
    }

    public void addTaskInfo(StockInSemiFinOrderDetail detail, String orderNo) {
        TaskInfo taskInfo = new TaskInfo();
        Long tenantId = SecurityUtils.getLoginUser().getSysUser().getTenantId();

        // 任务号
        AjaxResult result = iCodeRuleService.getSeqWithTenantId(TaskNoTypeConstant.SEMI_FIN_TASK, String.valueOf(tenantId));
        if ("".equals(Objects.toString(result.get("data"), "")) || result.isError()) {
            throw new ServiceException("半成品收货任务号生成失败！");
        }
        taskInfo.setMaterialNo(detail.getMaterialNo());
        taskInfo.setMaterialName(detail.getMaterialName());
        taskInfo.setOldMaterialNo(detail.getOldMaterialNo());
        taskInfo.setTaskNo(result.get("data").toString());
        //TODO 成品收货对应的入库任务 detailId为成品收货单据的id
        taskInfo.setDetailId(detail.getId());
        taskInfo.setTaskStatus(TaskStatusConstant.TASK_STATUS_NEW);
        taskInfo.setTaskType(StockInTaskConstant.FIN_ORDER_TASK_TYPE_SEMI_FINISHED);
        taskInfo.setHandlerUserId(SecurityUtils.getUserId());
        taskInfo.setHandlerUserName(SecurityUtils.getUsername());
        taskInfo.setStockInOrderNo(orderNo);
        taskInfo.setCreateBy(SecurityUtils.getUsername());
        taskInfo.setCreateTime(DateUtils.getNowDate());
        // 新增库存地点 add by yangyang.zhang 2022-12-20
        taskInfo.setLocationCode(detail.getLocationCode());
        //批量添加
        if (taskInfoMapper.insertTaskInfo(taskInfo) <= 0) {
            throw new ServiceException("任务生成失败!");
        }
    }

    /**
     * 修改半成品入库单
     *
     * @param stockInSemiFinOrderDto 半成品入库单
     * @return 结果
     */
    @Override
    public int updateStockInSemiFinOrder(StockInSemiFinOrderDto stockInSemiFinOrderDto)
    {
        if (ObjectUtils.isEmpty(stockInSemiFinOrderDto)){
            throw new ServiceException("参数不存在！");
        }
        //获取当前单据最大批次
//        String maxLot = stockInSemiFinOrderDetailMapper.getMaxLot(stockInSemiFinOrderDto.getId());
        //截取最大流水号
//        String serialNo = maxLot.substring(maxLot.length() - 4);
//        int integer = Integer.parseInt(serialNo);
        //明细集合
        List<StockInSemiFinOrderDetailDto> detailDtoList = stockInSemiFinOrderDto.getStockInSemiFinOrderDetailDtoList();
        List<String> materialNoList = detailDtoList.stream().map(StockInSemiFinOrderDetailDto::getMaterialNo).collect(Collectors.toList());
        MaterialUnitDefDto materialUnitDefDto = new MaterialUnitDefDto();
        materialUnitDefDto.setMaterialNoList(materialNoList);
        materialUnitDefDto.setStockinEnable(CommonYesOrNo.YES);
        materialUnitDefDto.setFactoryCode(SecurityUtils.getComCode());
        AjaxResult unitResult = mainDataService.batchMaterialUnitDef(materialUnitDefDto);
        if (unitResult.isError()) {
            throw new ServiceException(unitResult.get("msg").toString());
        }
        List<MaterialUnitDefDto> unitList = JSONObject.parseArray(unitResult.get("data").toString(), MaterialUnitDefDto.class);
        if (ObjectUtils.isEmpty(unitList)) {
            throw new ServiceException(String.format("不存在库内使用单位，请维护相关数据"));
        }
        Map<String, BigDecimal> map = unitList.stream().collect(Collectors.toMap(MaterialUnitDefDto::getMaterialNo, MaterialUnitDefDto::getConversDefault));
        Map<String, String> unitMap = unitList.stream().collect(Collectors.toMap(MaterialUnitDefDto::getMaterialNo, MaterialUnitDefDto::getUnit));

        // 获取明细id
        StockInSemiFinOrderDetailVo detailVo = new StockInSemiFinOrderDetailVo();
        detailVo.setSemifinOrderId(stockInSemiFinOrderDto.getId());
        List<StockInSemiFinOrderDetailVo> detailVoList = stockInSemiFinOrderDetailMapper.selectStockInSemiFinOrderDetailList(detailVo);
        if (!ObjectUtils.isEmpty(detailVoList)) {
            Long[] deleteIds = detailVoList.stream().map(StockInSemiFinOrderDetailVo::getId).toArray(Long[]::new);
            //删除所有明细
            if (!ObjectUtils.isEmpty(deleteIds)) {
                //删除明细
                stockInSemiFinOrderDetailMapper.deleteStockInSemiFinOrderDetailByIds(deleteIds);
                //同步删除半成品收货任务
                taskInfoMapper.deleteTaskInfoByStockInOrderNo(stockInSemiFinOrderDto.getOrderNo(), deleteIds);
            }
        }

        for (StockInSemiFinOrderDetailDto detailDto : detailDtoList) {
            // if (ObjectUtils.isEmpty(detailDto.getId())){
            //新增的明细
            StockInSemiFinOrderDetail detail = new StockInSemiFinOrderDetail();
            BeanUtils.copyProperties(detailDto,detail);
            detail.setSemifinOrderId(stockInSemiFinOrderDto.getId());
            detail.setIsPrinted(CommonYesOrNo.NO);
            detail.setCreateBy(SecurityUtils.getUsername());
            detail.setCreateTime(DateUtils.getNowDate());
            BigDecimal conversDefault = map.get(detail.getMaterialNo());
            String operationUnit = unitMap.get(detail.getMaterialNo());
            detail.setOperationPlanRecivevQty(detail.getPlanRecivevQty().divide(conversDefault, 4, RoundingMode.HALF_UP));
            detail.setOperationUnit(operationUnit);
            String lotNo = stockInSemiFinOrderDetailMapper.getMaxLot(detailDto.getPrdOrderNo());
            lotNo = "".equals(Objects.toString(lotNo,"")) ? detailDto.getPrdOrderNo() + "0000" : lotNo;
            Integer serialNo = Integer.valueOf(lotNo.substring(lotNo.length()-4)) + 1;
            lotNo = detailDto.getPrdOrderNo() + String.format("%04d", serialNo);
            detail.setLot(lotNo);
            //新建单据 明细状态为为新建
            detail.setStatus(OrderStatusConstant.ORDER_STATUS_NEW);
            //                integer++;
//                if (integer <= 9){
//                    detail.setLot(detailDto.getPrdOrderNo() + "000" + integer);
//                } else if (integer <= 99){
//                    detail.setLot(detailDto.getPrdOrderNo() + "00" + integer);
//                } else if (integer <= 999){
//                    detail.setLot(detailDto.getPrdOrderNo() + "0" + integer);
//                } else {
//                    detail.setLot(detailDto.getPrdOrderNo() + integer);
//                }
            if (stockInSemiFinOrderDetailMapper.insertStockInSemiFinOrderDetail(detail) <= 0) {
                throw new ServiceException("新增半成品收货单据失败！");
            }
            SpringUtils.getBean(StockInSemiFinOrderServiceImpl.class).addTaskInfo(detail,stockInSemiFinOrderDto.getOrderNo());
            // } else {
            //     //更新明细
            //     stockInSemiFinOrderDetailMapper.updateStockInSemiFinOrderDetail(new StockInSemiFinOrderDetail(){{
            //         setId(detailDto.getId());
            //         setLineNo(detailDto.getLineNo());
            //         setUpdateBy(SecurityUtils.getUsername());
            //         setUpdateTime(DateUtils.getNowDate());
            //     }});
            // }
        }
        return 1;
    }

    /**
     * 批量删除半成品入库单
     *
     * @param ids 需要删除的半成品入库单主键
     * @return 结果
     */
    @Override
    public int deleteStockInSemiFinOrderByIds(Long[] ids)
    {
        return stockInSemiFinOrderMapper.deleteStockInSemiFinOrderByIds(ids);
    }

    /**
     * 删除半成品入库单信息
     *
     * @param id 半成品入库单主键
     * @return 结果
     */
    @Override
    public int deleteStockInSemiFinOrderById(Long id)
    {
        return stockInSemiFinOrderMapper.deleteStockInSemiFinOrderById(id);
    }

    /**
     * 关闭半成品入库单
     * @param ids 半成品入库单主键集合
     * @return 结果
     */
    @Override
    public int closeStockInSemiFinOrder(Long[] ids) {
        if(ObjectUtils.isEmpty(ids)){
            throw new ServiceException("参数不存在！");
        }
        for (Long id : ids) {
            // 更新收货任务状态为关闭
            StockInSemiFinOrder stockInSemiFinOrder = stockInSemiFinOrderMapper.selectStockInSemiFinOrderById(id);
            if (TaskStatusConstant.TASK_STATUS_CLOSE.equals(stockInSemiFinOrder.getOrderStatus())
                    || TaskStatusConstant.TASK_STATUS_COMPLETE.equals(stockInSemiFinOrder.getOrderStatus())) {
                throw new ServiceException("已关闭或已完成单据无法关闭！");
            }
            stockInSemiFinOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_CLOSE);
            stockInSemiFinOrder.setUpdateTime(DateUtils.getNowDate());
            stockInSemiFinOrder.setUpdateBy(SecurityUtils.getUsername());
            stockInSemiFinOrderMapper.updateStockInSemiFinOrder(stockInSemiFinOrder);

            // 更新任务状态为关闭
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setStockInOrderNo(stockInSemiFinOrder.getOrderNo());
            taskInfo.setTaskStatus(TaskStatusConstant.TASK_STATUS_CLOSE);
            taskInfo.setUpdateTime(DateUtils.getNowDate());
            taskInfo.setUpdateBy(SecurityUtils.getUsername());
            taskInfoMapper.closeTaskInfoByNo(taskInfo);
        }
        return 1;
    }

    /**
     * 获取半成品标签打印
     * @param stockInSemiFinOrderDto 半成品入库单dto
     * @return 半成品入库明细
     */
    @Override
    public List<StockInSemiFinOrderDetailVo> getStockInSemiFinOrderPrint(StockInSemiFinOrderDto stockInSemiFinOrderDto) {
        if (ObjectUtils.isEmpty(stockInSemiFinOrderDto.getId())){
            throw new ServiceException("参数不存在！");
        }
        //获取半成品收货单据
//        StockInSemiFinOrder stockInSemiFinOrder = stockInSemiFinOrderMapper.selectStockInSemiFinOrderById(stockInSemiFinOrderDto.getId());
        //获取半成品收货单明细
        StockInSemiFinOrderDetailVo detailVo = new StockInSemiFinOrderDetailVo();
        detailVo.setSemifinOrderId(stockInSemiFinOrderDto.getId());
        detailVo.setDetailStatusArr(stockInSemiFinOrderDto.getDetailStatusArr());
        List<StockInSemiFinOrderDetailVo> detailList = stockInSemiFinOrderDetailMapper.selectStockInSemiFinOrderDetailList(detailVo);
        for (StockInSemiFinOrderDetailVo stockInSemiFinOrderDetailVo : detailList) {
            //更新打印状态
            StockInSemiFinOrderDetail orderDetail = new StockInSemiFinOrderDetail();
            orderDetail.setId(stockInSemiFinOrderDetailVo.getId());
            orderDetail.setIsPrinted(CommonYesOrNo.YES);
            orderDetail.setUpdateTime(DateUtils.getNowDate());
            orderDetail.setUpdateBy(SecurityUtils.getUsername());
            stockInSemiFinOrderDetailMapper.updateStockInSemiFinOrderDetail(orderDetail);
        }
//        detailList.forEach(item->item.setOrderNo(stockInSemiFinOrder.getOrderNo()));
        return detailList;
    }

    @Override
    @DataScope(deptAlias = "d")
    public List<StockInSemiFinOrderDto> selectStockInSemiList(StockInSemiFinOrderDto dto) {
        return stockInSemiFinOrderMapper.selectStockInSemiList(dto);
    }
}
