package com.easycode.cloud.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.easycode.cloud.domain.dto.PrdReturnOrderDetailDto;
import com.easycode.cloud.domain.dto.PrdReturnOrderDto;
import com.easycode.cloud.service.IPrdReturnOrderService;
import com.weifu.cloud.common.core.exception.ServiceException;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.weifu.cloud.common.core.utils.SpringUtils;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.datascope.annotation.DataScope;
import com.weifu.cloud.common.security.utils.SecurityUtils;
import com.weifu.cloud.constant.*;
import com.easycode.cloud.domain.PrdReturnOrder;
import com.easycode.cloud.domain.PrdReturnOrderDetail;
import com.easycode.cloud.domain.RetTask;
import com.easycode.cloud.domain.dto.RetTaskDto;
import com.weifu.cloud.domian.ProductionOrderInfo;
import com.weifu.cloud.domian.dto.ProductionOrderDeliverDto;
import com.weifu.cloud.domian.dto.ProductionOrderDetailDto;
import com.weifu.cloud.domian.vo.WmsMaterialWarehouseVo;
import com.weifu.cloud.enums.RemoteConfigEnum;
import com.easycode.cloud.mapper.PrdReturnOrderDetailMapper;
import com.easycode.cloud.mapper.PrdReturnOrderMapper;
import com.easycode.cloud.mapper.RetTaskMapper;
import com.weifu.cloud.service.*;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 生产订单发货退料单Service业务层处理
 *
 * @author bcp 2023-03-11
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PrdReturnOrderServiceImpl implements IPrdReturnOrderService {
    @Autowired
    private PrdReturnOrderMapper prdReturnOrderMapper;

    @Autowired
    private PrdReturnOrderDetailMapper prdReturnOrderDetailMapper;
    @Autowired
    private IStockOutService iStockOutService;

    @Autowired
    private ICodeRuleService iCodeRuleService;

    @Autowired
    private RemoteConfigHelper remoteConfigHelper;

    @Autowired
    private RetTaskMapper retTaskMapper;

    @Autowired
    private IMainDataService iMainDataService;


    /**
     * 查询生产订单发货退料单
     *
     * @param id 生产订单发货退料单主键
     * @return 生产订单发货退料单
     */
    @Override
    public PrdReturnOrder selectPrdReturnOrderById(Long id) {
        return prdReturnOrderMapper.selectPrdReturnOrderById(id);
    }

    /**
     * 查询生产订单发货退料单列表
     *
     * @param prdReturnOrderDto 生产订单发货退料单
     * @return 生产订单发货退料单
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<PrdReturnOrder> selectPrdReturnOrderList(PrdReturnOrderDto prdReturnOrderDto) {
        return prdReturnOrderMapper.selectPrdReturnOrderList(prdReturnOrderDto);
    }

    /**
     * 新增生产订单发货退料单
     *
     * @param prdReturnOrderDto 生产订单发货退料单dto
     * @return 结果
     */
    @Override

    public PrdReturnOrder insertPrdReturnOrder(PrdReturnOrderDto prdReturnOrderDto) {
        if (ObjectUtils.isEmpty(prdReturnOrderDto)) {
            throw new ServiceException("参数不存在！");
        }
        //查询生产订单
        String prdOrderNo = prdReturnOrderDto.getPrdOrderNo();
        ProductionOrderInfo orderInfo = new ProductionOrderInfo();
        orderInfo.setOrderNo(prdOrderNo);
        AjaxResult ajaxResult = iStockOutService.getList(orderInfo);
        if (ajaxResult.isError()) {
            throw new ServiceException("出库服务调用失败！");
        }
        List<ProductionOrderInfo> list = JSONObject.parseArray(ajaxResult.get("data").toString(), ProductionOrderInfo.class);
        if (list.size() != 1) {
            throw new ServiceException("数据有误！生产订单号不唯一！");
        }
        ProductionOrderInfo productionOrderInfo = list.get(0);
        Long tenantId = SecurityUtils.getLoginUser().getSysUser().getTenantId();

        //新增生产发货退料单
        AjaxResult seqWithCodeRule = iCodeRuleService.getSeqWithTenantId(OrderNoTypeConstant.PRODUCT_DELIVER_ORDER_RETURN, String.valueOf(tenantId));
        if ("".equals(Objects.toString(seqWithCodeRule.get("data"), "")) || seqWithCodeRule.isError()) {
            throw new ServiceException("生产订单发料退料单据号生成失败！");
        }
        String returnOrderNo = seqWithCodeRule.get("data").toString();
        PrdReturnOrder prdReturnOrder = new PrdReturnOrder();
        prdReturnOrder.setReturnOrderNo(returnOrderNo);
        prdReturnOrder.setPrdOrderNo(productionOrderInfo.getOrderNo());
        prdReturnOrder.setProductCode(productionOrderInfo.getProductCode());
        prdReturnOrder.setProductDes(productionOrderInfo.getProductDesc());
        prdReturnOrder.setFactoryCode(productionOrderInfo.getFactoryCode());
//        prdReturnOrder.setTenantId(productionOrderInfo.getTenantId());
        //TODO 生产订单没有货主名称需要set货主名称或是删除字段
        prdReturnOrder.setCreateBy(SecurityUtils.getUsername());
        prdReturnOrder.setCreateTime(DateUtils.getNowDate());
        prdReturnOrder.setDeptId(SecurityUtils.getLoginUser().getSysUser().getDeptId());
        //根据参数配置 决定是否走自动激活单据
        RemoteConfigEnum createStockInTask = RemoteConfigEnum.CREATE_STOCK_IN_TASK;
        String isCreateStockInTask = remoteConfigHelper.getConfig(createStockInTask.getKey());
        if ("".equals(Objects.toString(isCreateStockInTask, ""))) {
            throw new ServiceException("获取参数配置失败！");
        }
        boolean flag = createStockInTask.getValue().equals(isCreateStockInTask);
        if (flag) {
            prdReturnOrder.setPrdOrderStatus(OrderStatusConstant.ORDER_STATUS_ACTIVE);
        } else {
            prdReturnOrder.setPrdOrderStatus(OrderStatusConstant.ORDER_STATUS_NEW);
        }
        if (prdReturnOrderMapper.insertPrdReturnOrder(prdReturnOrder) <= 0) {
            throw new ServiceException("新增生产订单发料退料单据失败！");
        }
        //新增生产发货退料明细 根据工厂 物料号 批次号 生产发货单行号 合并
        List<PrdReturnOrderDetailDto> detailList = prdReturnOrderDto.getDetailList().stream()
                .collect(Collectors.toMap(d -> d.getFactoryCode() + d.getMaterialNo() + d.getLot() + d.getPrdOrderLineNo() , a -> a, (o1, o2) -> {
                    o1.setReturnQty(o1.getReturnQty().add(o2.getReturnQty()));
                    o1.setOperationReturnQty(o1.getOperationReturnQty().add(o2.getOperationReturnQty()));
                    return o1;
                })).values().stream().collect(Collectors.toList());
        int lineNo = 1;
        for (PrdReturnOrderDetailDto detail : detailList) {
//            AjaxResult lotNoResult = iCodeRuleService.getSeqWithCodeRule(StockInTaskConstant.RETURN_LOT_NO);
//            if("".equals(Objects.toString(lotNoResult.get("data"),"")) || lotNoResult.isError()){
//                throw new ServiceException("退货批次号生成失败！");
//            }
//            String lotNo = lotNoResult.get("data").toString();
//            detail.setLot(lotNo);
//            detail.setTenantId(productionOrderInfo.getTenantId());
            detail.setReturnOrderNo(returnOrderNo);
            detail.setCreateBy(SecurityUtils.getUsername());
            detail.setCreateTime(DateUtils.getNowDate());
            detail.setLot(detail.getLot() + StockInTaskConstant.RETURN_LOT_NO_SUFFIX);
            detail.setCompleteQty(BigDecimal.ZERO);
            detail.setOperationCompleteQty(BigDecimal.ZERO);
            detail.setLineNo(Integer.toString(lineNo++));
            if (prdReturnOrderDetailMapper.insertPrdReturnOrderDetail(detail) <= 0) {
                throw new ServiceException("新增生产订单发料退货明细失败！");
            }
            if (flag) {
                //新增退货入库任务
                SpringUtils.getBean(PrdReturnOrderServiceImpl.class).addRetTask(returnOrderNo, detail);
//                AjaxResult taskNumberAjaxResult = iCodeRuleService.getSeqWithCodeRule(TaskNoTypeConstant.PRO_DELIVER_RETURN_TASK);
//                if ("".equals(Objects.toString(taskNumberAjaxResult.get("data"), "")) || taskNumberAjaxResult.isError()) {
//                    throw new ServiceException("生产订单发料退货任务号生成失败！");
//                }
//                RetTask retTask = new RetTask();
//                retTask.setDetailId(detail.getId());
//                retTask.setTaskNo(taskNumberAjaxResult.get("data").toString());
//                retTask.setMaterialNo(detail.getMaterialNo());
//                retTask.setMaterialName(detail.getMaterialName());
//                retTask.setOldMatrialName(detail.getOldMaterialNo());
//                retTask.setStockinOrderNo(returnOrderNo);
//                retTask.setLot(detail.getLot());
//                retTask.setUnit(detail.getUnit());
//                retTask.setQty(detail.getReturnQty());
//                retTask.setOperationUnit(detail.getOperationUnit());
//                retTask.setOperationQty(detail.getOperationReturnQty());
//                retTask.setCompleteQty(BigDecimal.ZERO);
//                retTask.setOperationCompleteQty(BigDecimal.ZERO);
//                retTask.setTaskStatus(TaskStatusConstant.TASK_STATUS_NEW);
//                retTask.setTaskType(StockInTaskConstant.PRO_DELIVER_RETURN_TYPE);
//                retTask.setCreateBy(SecurityUtils.getUsername());
//                retTask.setCreateTime(DateUtils.getNowDate());
//                if (retTaskMapper.insertRetTask(retTask) <= 0) {
//                    throw new ServiceException("新增生产订单发料退料任务失败！");
//                }
            }
        }
        return prdReturnOrder;
    }

    /**
     * 修改生产订单发货退料单
     *
     * @param prdReturnOrder 生产订单发货退料单
     * @return 结果
     */
    @Override
    public int updatePrdReturnOrder(PrdReturnOrder prdReturnOrder) {
        prdReturnOrder.setUpdateTime(DateUtils.getNowDate());
        return prdReturnOrderMapper.updatePrdReturnOrder(prdReturnOrder);
    }

    /**
     * 批量删除生产订单发货退料单
     *
     * @param ids 需要删除的生产订单发货退料单主键
     * @return 结果
     */
    @Override
    public int deletePrdReturnOrderByIds(Long[] ids) {
        return prdReturnOrderMapper.deletePrdReturnOrderByIds(ids);
    }

    /**
     * 删除生产订单发货退料单信息
     *
     * @param id 生产订单发货退料单主键
     * @return 结果
     */
    @Override
    public int deletePrdReturnOrderById(Long id) {
        return prdReturnOrderMapper.deletePrdReturnOrderById(id);
    }

    /**
     * 关闭生产发货退料单
     *
     * @param id 主键id
     * @return 结果
     */
    @Override
    @GlobalTransactional
    public int close(Long id) {
        //校验参数
        if (ObjectUtils.isEmpty(id)) {
            throw new ServiceException("参数不存在！");
        }
        //查询单据
        PrdReturnOrder prdReturnOrder = prdReturnOrderMapper.selectPrdReturnOrderById(id);
        String status = prdReturnOrder.getPrdOrderStatus();
        if (OrderStatusConstant.ORDER_STATUS_CLOSE.equals(status) || OrderStatusConstant.ORDER_STATUS_COMPLETE.equals(status)) {
            throw new ServiceException("当前生产订单发货退料单已完成或已关闭！");
        }
        //关闭单据
        prdReturnOrder.setPrdOrderStatus(OrderStatusConstant.ORDER_STATUS_CLOSE);
        prdReturnOrder.setUpdateTime(DateUtils.getNowDate());
        prdReturnOrder.setUpdateBy(SecurityUtils.getUsername());
        if (prdReturnOrderMapper.updatePrdReturnOrder(prdReturnOrder) <= 0) {
            throw new ServiceException("关闭生产订单发货退料单失败！");
        }
        if (OrderStatusConstant.ORDER_STATUS_NEW.equals(status)) {
            return 1;
        }
        //查询明细
        PrdReturnOrderDetail prdReturnOrderDetail = new PrdReturnOrderDetail();
        prdReturnOrderDetail.setReturnOrderNo(prdReturnOrder.getReturnOrderNo());
        List<PrdReturnOrderDetail> prdReturnOrderDetails = prdReturnOrderDetailMapper.selectPrdReturnOrderDetailList(prdReturnOrderDetail);
        List<ProductionOrderDetailDto> productionOrderDetailDto = new ArrayList<>();
        for (PrdReturnOrderDetail detail : prdReturnOrderDetails) {
            //关闭明细对应退货任务
            RetTaskDto retTaskDto = new RetTaskDto();
            retTaskDto.setDetailId(detail.getId());
            retTaskDto.setCloseTask(TaskStatusConstant.TASK_STATUS_CLOSE);
            retTaskDto.setTaskType(StockInTaskConstant.PRO_DELIVER_RETURN_TYPE);
            retTaskDto.setTaskStatus(TaskStatusConstant.TASK_STATUS_COMPLETE);
            retTaskDto.setUpdateBy(SecurityUtils.getUsername());
            retTaskDto.setUpdateTime(DateUtils.getNowDate());
            if (retTaskMapper.updateRetTaskStatus(retTaskDto) <= 0) {
                throw new ServiceException("关闭退货任务失败！");
            }
            if (detail.getReturnQty().compareTo(detail.getCompleteQty()) > 0) {
                ProductionOrderDetailDto orderDetailDto = new ProductionOrderDetailDto();
                orderDetailDto.setProductionOrderNo(detail.getPrdOrderNo());
                orderDetailDto.setLineNo(detail.getPrdOrderLineNo());
                orderDetailDto.setFinishReturnQty(detail.getCompleteQty());
                productionOrderDetailDto.add(orderDetailDto);
            } else if (detail.getReturnQty().compareTo(detail.getCompleteQty()) < 0) {
                throw new ServiceException("已完成退货数量大于退货数量！");
            }
        }
        // 回写生产发料明细数量
        ProductionOrderDeliverDto orderDeliverDto = new ProductionOrderDeliverDto();
        orderDeliverDto.setDetailDtoList(productionOrderDetailDto);
        AjaxResult ajaxResult = iStockOutService.updateDetailByLineNoAndOrderNo(orderDeliverDto);
        if (ajaxResult.isError()) {
            throw new ServiceException("回写生产发料明细失败！");
        }
        return 1;
    }

    /**
     * 批量激活生产发料退货单
     * @param ids 生产发料退货单ids
     * @return 结果
     */
    @Override
    public int activePrdReturnOrderByIds(Long[] ids) {
        //校验参数
        if (ObjectUtils.isEmpty(ids)) {
            throw new ServiceException("参数不存在！");
        }
        for (Long id : ids) {
            //查询单据
            PrdReturnOrder prdReturnOrder = prdReturnOrderMapper.selectPrdReturnOrderById(id);
            String status = prdReturnOrder.getPrdOrderStatus();
            String orderNo = prdReturnOrder.getReturnOrderNo();
            if (!OrderStatusConstant.ORDER_STATUS_NEW.equals(status)) {
                throw new ServiceException("生产发料退货单:"+ orderNo+"必须为新建状态才能激活！");
            }
            //更新状态
            prdReturnOrder.setPrdOrderStatus(OrderStatusConstant.ORDER_STATUS_ACTIVE);
            prdReturnOrder.setUpdateBy(SecurityUtils.getUsername());
            prdReturnOrder.setUpdateTime(DateUtils.getNowDate());
            if (prdReturnOrderMapper.updatePrdReturnOrder(prdReturnOrder) <= 0) {
                throw new ServiceException("更新生产发料退货单失败！");
            }
            //查询明细
            PrdReturnOrderDetail prdReturnOrderDetail = new PrdReturnOrderDetail();
            prdReturnOrderDetail.setReturnOrderNo(orderNo);
            List<PrdReturnOrderDetail> detailList = prdReturnOrderDetailMapper.selectPrdReturnOrderDetailList(prdReturnOrderDetail);
            //新增退货入库任务
            for (PrdReturnOrderDetail detail : detailList) {
                SpringUtils.getBean(PrdReturnOrderServiceImpl.class).addRetTask(orderNo, detail);
            }
        }
        return 1;
    }
    /**
     * 新增退货入库任务
     * @param orderNo 单据号
     * @param detail 单据明细
     */
    public void addRetTask(String orderNo, PrdReturnOrderDetail detail) {
        Long tenantId = SecurityUtils.getLoginUser().getSysUser().getTenantId();

        // 任务号
        AjaxResult taskNoResult = iCodeRuleService.getSeqWithTenantId(TaskNoTypeConstant.PRO_DELIVER_RETURN_TASK, String.valueOf(tenantId));
        if("".equals(Objects.toString(taskNoResult.get("data"),"")) || taskNoResult.isError()){
            throw new ServiceException("生产发料退货任务号生成失败！");
        }
        String taskNo = taskNoResult.get("data").toString();
        // 获取推荐仓位
        WmsMaterialWarehouseVo wmsMaterialWarehouseVo = new WmsMaterialWarehouseVo();
        wmsMaterialWarehouseVo.setMaterialNo(detail.getMaterialNo());
        AjaxResult result = iMainDataService.getRecommend(wmsMaterialWarehouseVo);
        if (result.isError()) {
            throw new ServiceException(Objects.toString(result.get("msg").toString(),"调用主数据服务失败"));
        }
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
        retTask.setQty(detail.getReturnQty());
        retTask.setOperationQty(detail.getOperationReturnQty());
        retTask.setCompleteQty(BigDecimal.ZERO);
        retTask.setOperationCompleteQty(BigDecimal.ZERO);
        retTask.setTaskStatus(TaskStatusConstant.TASK_STATUS_NEW);
        retTask.setTaskType(StockInTaskConstant.PRO_DELIVER_RETURN_TYPE);
        retTask.setCreateBy(SecurityUtils.getUsername());
        retTask.setCreateTime(DateUtils.getNowDate());
        if (!ObjectUtils.isEmpty(result.get("data"))) {
            WmsMaterialWarehouseVo material = JSON.parseObject(result.get("data").toString(), WmsMaterialWarehouseVo.class);
            retTask.setPositionNo(material.getStoBin());
            retTask.setStorageLocationCode(material.getLocation());
        }
        // 生成退货任务
        if (retTaskMapper.insertRetTask(retTask) <= 0){
            throw new ServiceException("新增生产发料退货任务失败！");
        }
    }
}
