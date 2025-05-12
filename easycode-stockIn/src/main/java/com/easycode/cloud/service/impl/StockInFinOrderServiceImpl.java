package com.easycode.cloud.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.easycode.cloud.domain.dto.FinProductScanDetailDto;
import com.easycode.cloud.domain.vo.FinProductScanVo;
import com.weifu.cloud.common.core.exception.ServiceException;
import com.weifu.cloud.common.core.text.Convert;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.weifu.cloud.common.core.utils.SpringUtils;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.security.utils.SecurityUtils;
import com.weifu.cloud.constant.*;
import com.weifu.cloud.domain.StockInFinOrder;
import com.weifu.cloud.domain.StockInFinOrderDetail;
import com.easycode.cloud.domain.TaskInfo;
import com.weifu.cloud.domain.dto.*;
import com.weifu.cloud.domain.vo.StockInFinOrderDetailVo;
import com.weifu.cloud.domian.dto.MaterialUnitDefDto;
import com.weifu.cloud.mapper.EsbSendCommonMapper;
import com.easycode.cloud.mapper.StockInFinOrderDetailMapper;
import com.easycode.cloud.mapper.StockInFinOrderMapper;
import com.easycode.cloud.mapper.TaskInfoMapper;
import com.weifu.cloud.service.ICodeRuleService;
import com.weifu.cloud.service.IMainDataService;
import com.easycode.cloud.service.IStockInFinOrderService;
import com.weifu.cloud.service.RemoteConfigHelper;
import org.apache.commons.lang3.StringUtils;
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
 * 成品入库单Service业务层处理
 *
 * @author weifu
 * @date 2022-12-07
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StockInFinOrderServiceImpl implements IStockInFinOrderService {

    @Autowired
    private StockInFinOrderMapper stockInFinOrderMapper;

    @Autowired
    private StockInFinOrderDetailMapper stockInFinOrderDetailMapper;

    @Autowired
    private TaskInfoMapper taskInfoMapper;

    @Autowired
    private ICodeRuleService iCodeRuleService;

    @Autowired
    private RemoteConfigHelper remoteConfigHelper;

    @Autowired
    private EsbSendCommonMapper esbSendCommonMapper;

    @Autowired
    private IMainDataService mainDataService;

    /**
     * 新增标识
     */
    private static final String ADD = "1";


    /**
     * 查询成品入库单
     *
     * @param id 成品入库单主键
     * @return 成品入库单
     */
    @Override
    public StockInFinOrder selectStockInFinOrderById(Long id) {
        return stockInFinOrderMapper.selectStockInFinOrderById(id);
    }

    /**
     * 查询成品入库单列表
     *
     * @param stockInFinOrder 成品入库单
     * @return 成品入库单
     */
    @Override
//    @DataScope(deptAlias = "d")
    public List<StockInFinOrderDto> selectStockInFinOrderList(StockInFinOrder stockInFinOrder) {
        return stockInFinOrderMapper.selectStockInFinOrderList(stockInFinOrder);
    }

    /**
     * 根据成品收货单号查询打印信息
     *
     * @param stockInFinOrder 成品入库单
     * @return 成品入库单
     */
    @Override
    public List<StockInFinOrderDto> getPrintInfoByProductNo(StockInFinOrder stockInFinOrder) {
        return stockInFinOrderMapper.selectStockInFinOrderList(stockInFinOrder);
    }

    /**
     * 新增成品入库单
     *
     * @param stockInFinOrderDto 成品入库单
     * @return 结果
     */
    @Override
    public StockInFinOrder insertStockInFinOrder(StockInFinOrderDto stockInFinOrderDto) {
        List<StockInFinOrderDetailDto> finOrderDetailList = stockInFinOrderDto.getStockInFinOrderDetailDtoList();
        if (finOrderDetailList == null || finOrderDetailList.isEmpty()) {
            throw new ServiceException("成品收货明细不能为空!");
        }
        //TODO LD成品入库单与生产订单一对一  需要校验已有的成品入库单对一的生产订单
        //校验成品入库单
        StockInFinOrder inFinOrder = new StockInFinOrder();
        inFinOrder.setPrdOrderNo(stockInFinOrderDto.getPrdOrderNo());
        inFinOrder.setFactoryCode(stockInFinOrderDto.getFactoryCode());
        inFinOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_CLOSE);
        List<StockInFinOrder> stockInFinOrders = stockInFinOrderMapper.checkStockInFinOrder(inFinOrder);
        if (stockInFinOrders.size() > 0) {
            StockInFinOrder stockInFinOrder = stockInFinOrders.get(0);
            throw new ServiceException(String.format("货主：%s,生产订单号：%s,已在成品入库单:%s中！"
                    , stockInFinOrderDto.getFactoryCode(), stockInFinOrderDto.getPrdOrderNo(), stockInFinOrder.getOrderNo()));
        }
        //新增成品收货单
        StockInFinOrder finOrder = new StockInFinOrder();
        BeanUtils.copyProperties(stockInFinOrderDto, finOrder);
        // 单据类型(默认成品)
        finOrder.setOrderType(StockInOrderConstant.PRODUCT_ORDER);
        Long tenantId = SecurityUtils.getLoginUser().getSysUser().getTenantId();

        AjaxResult ajaxResult = iCodeRuleService.getSeqWithTenantId(OrderNoTypeConstant.FIN_ORDER, String.valueOf(tenantId));
        if ("".equals(Objects.toString(ajaxResult.get("data"), "")) || ajaxResult.isError()) {
            throw new ServiceException("成品收货单据号生成失败！");
        }
        String orderNo = ajaxResult.get("data").toString();
        finOrder.setOrderNo(ajaxResult.get("data").toString());
        finOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_NEW);
        finOrder.setTenantId(null);
        finOrder.setDeptId(SecurityUtils.getLoginUser().getSysUser().getDeptId());
        finOrder.setCreateBy(SecurityUtils.getUsername());
        finOrder.setCreateTime(DateUtils.getNowDate());

        List<String> materialNoList = finOrderDetailList.stream().map(StockInFinOrderDetailDto::getMaterialNo).collect(Collectors.toList());
        MaterialUnitDefDto unitDefDto = new MaterialUnitDefDto();
        unitDefDto.setMaterialNoList(materialNoList);
        unitDefDto.setStockinEnable(CommonYesOrNo.YES);
        unitDefDto.setFactoryCode(SecurityUtils.getComCode());
        AjaxResult unitResult = mainDataService.batchMaterialUnitDef(unitDefDto);
        if (unitResult.isError()) {
            throw new ServiceException(unitResult.get("msg").toString());
        }
        List<MaterialUnitDefDto> unitList = JSONObject.parseArray(unitResult.get("data").toString(), MaterialUnitDefDto.class);
        if (ObjectUtils.isEmpty(unitList)) {
            throw new ServiceException(String.format("不存在入库使用单位，请维护相关数据"));
        }
        Map<String, BigDecimal> map = unitList.stream().collect(Collectors.toMap(MaterialUnitDefDto::getMaterialNo, MaterialUnitDefDto::getConversDefault));
        Map<String, String> unitMap = unitList.stream().collect(Collectors.toMap(MaterialUnitDefDto::getMaterialNo, MaterialUnitDefDto::getUnit));
        if (stockInFinOrderMapper.insertStockInFinOrder(finOrder) > 0) {
            //新增成品收货单明细
            for (StockInFinOrderDetailDto detail : finOrderDetailList) {
                detail.setFinOrderId(finOrder.getId());
                detail.setMaterialNo(finOrder.getMaterialNo());
                detail.setMaterialId(finOrder.getMaterialId());
                detail.setMaterialName(finOrder.getMaterialName());
                detail.setCreateBy(finOrder.getCreateBy());
                detail.setCreateTime(finOrder.getCreateTime());
                BigDecimal conversDefault = map.get(detail.getMaterialNo());
                String operationUnit = unitMap.get(detail.getMaterialNo());
                if (!Optional.ofNullable(conversDefault).isPresent() || !Optional.ofNullable(operationUnit).isPresent()){
                    throw new ServiceException(String.format("%s物料号多级单位不存在或数据有误", detail.getMaterialNo()));
                }
                detail.setOperationPlanRecivevQty(detail.getPlanRecivevQty().divide(conversDefault, 4, RoundingMode.HALF_UP));
                detail.setOperationUnit(operationUnit);
                //新建单据 明细状态为为新建
                detail.setDetailStatus(OrderStatusConstant.ORDER_STATUS_NEW);
                //TODO 成品收货单明细批次号 需要与LD确认 目前暂为 生产订单号+成品收货单行号 成品收货单行号暂为三位 前面补零
                String lineNo = detail.getLineNo();
                int integer = Integer.parseInt(lineNo);
                if (integer <= 9){
                    detail.setLot(stockInFinOrderDto.getPrdOrderNo() + "000" + detail.getLineNo());
                } else if (integer <= 99){
                    detail.setLot(stockInFinOrderDto.getPrdOrderNo() + "00" + detail.getLineNo());
                } else if (integer <= 999){
                    detail.setLot(stockInFinOrderDto.getPrdOrderNo() + "0" + detail.getLineNo());
                } else {
                    detail.setLot(stockInFinOrderDto.getPrdOrderNo() + detail.getLineNo());
                }
                if (stockInFinOrderDetailMapper.insertStockInFinOrderDetail(detail) <= 0) {
                    throw new ServiceException("新增成品收货单据失败！");
                }
                SpringUtils.getBean(StockInFinOrderServiceImpl.class).addTaskInfo(detail,orderNo);
            }
        } else {
            throw new ServiceException("新增成品收货单据失败！");
        }
        return finOrder;
    }

    /**
     * 编辑成品入库单
     *
     * @param stockInFinOrderDto 成品入库单
     * @return 结果
     */
    @Override
    public int updateStockInFinOrder(StockInFinOrderDto stockInFinOrderDto) {
        if (ObjectUtils.isEmpty(stockInFinOrderDto)){
            throw new ServiceException("参数不存在！");
        }
        //获取当前单据最大批次
        String maxLot = stockInFinOrderDetailMapper.getMaxLot(stockInFinOrderDto.getId());
        //截取最大流水号
        String serialNo = Optional.ofNullable(maxLot).isPresent() ? maxLot.substring(maxLot.length() - 4) : "0001";
        int integer = StringUtils.isEmpty(serialNo) ? 1 : Integer.parseInt(serialNo);
        boolean isNewAddition = false;

        //明细集合
        List<StockInFinOrderDetailDto> detailDtoList = stockInFinOrderDto.getStockInFinOrderDetailDtoList();

        List<String> materialNoList = detailDtoList.stream().map(StockInFinOrderDetailDto::getMaterialNo).collect(Collectors.toList());
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

        for (StockInFinOrderDetailDto detailDto : detailDtoList) {
            if (ObjectUtils.isEmpty(detailDto.getId())){
                isNewAddition = true;
                //新增的明细
                StockInFinOrderDetail detail = new StockInFinOrderDetail();
                BeanUtils.copyProperties(detailDto,detail);
                detail.setFinOrderId(stockInFinOrderDto.getId());
                detail.setCreateBy(SecurityUtils.getUsername());
                detail.setCreateTime(DateUtils.getNowDate());
                BigDecimal conversDefault = map.get(detail.getMaterialNo());
                String operationUnit = unitMap.get(detail.getMaterialNo());
                detail.setOperationPlanRecivevQty(detail.getPlanRecivevQty().divide(conversDefault, 4, RoundingMode.HALF_UP));
                detail.setOperationUnit(operationUnit);
                //明细状态为为新建
                detail.setDetailStatus(OrderStatusConstant.ORDER_STATUS_NEW);
                integer++;
                if (integer <= 9){
                    detail.setLot(stockInFinOrderDto.getPrdOrderNo() + "000" + integer);
                } else if (integer <= 99){
                    detail.setLot(stockInFinOrderDto.getPrdOrderNo() + "00" + integer);
                } else if (integer <= 999){
                    detail.setLot(stockInFinOrderDto.getPrdOrderNo() + "0" + integer);
                } else {
                    detail.setLot(stockInFinOrderDto.getPrdOrderNo() + integer);
                }
                if (stockInFinOrderDetailMapper.insertStockInFinOrderDetail(detail) <= 0) {
                    throw new ServiceException("新增成品收货单据失败！");
                }
                SpringUtils.getBean(StockInFinOrderServiceImpl.class).addTaskInfo(detail,stockInFinOrderDto.getOrderNo());
            } else {
                //更新明细
                StockInFinOrderDetail stockInFinOrderDetail = new StockInFinOrderDetail();
                stockInFinOrderDetail.setId(detailDto.getId());
                stockInFinOrderDetail.setLineNo(detailDto.getLineNo());
                stockInFinOrderDetail.setUpdateBy(SecurityUtils.getUsername());
                stockInFinOrderDetail.setUpdateTime(DateUtils.getNowDate());
                stockInFinOrderDetailMapper.updateStockInFinOrderDetail(stockInFinOrderDetail);
            }
        }
        //需要删除的明细
        Long[] deleteIds = stockInFinOrderDto.getDeleteIds();
        if (!ObjectUtils.isEmpty(deleteIds)){
            //删除明细
            stockInFinOrderDetailMapper.deleteStockInFinOrderDetailByIds(deleteIds);
            //同步删除成品收货任务
            taskInfoMapper.deleteTaskInfoByStockInOrderNo(stockInFinOrderDto.getOrderNo(),deleteIds);
        }
        //单据已完成 且有新增明细时 更新单据状态为部分完成
        if (OrderStatusConstant.ORDER_STATUS_COMPLETE.equals(stockInFinOrderDto.getOrderStatus()) && isNewAddition){
            StockInFinOrder stockInFinOrder = new StockInFinOrder();
            stockInFinOrder.setId(stockInFinOrderDto.getId());
            stockInFinOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_PART_COMPLETE);
            stockInFinOrder.setUpdateBy(SecurityUtils.getUsername());
            stockInFinOrder.setUpdateTime(DateUtils.getNowDate());
            stockInFinOrderMapper.updateStockInFinOrder(stockInFinOrder);
        }
        return 1;
    }

    public void addTaskInfo(StockInFinOrderDetail detail,String orderNo) {
        TaskInfo taskInfo = new TaskInfo();
        Long tenantId = SecurityUtils.getLoginUser().getSysUser().getTenantId();

        // 任务号
        AjaxResult result = iCodeRuleService.getSeqWithTenantId(TaskNoTypeConstant.TASK_FIND_ORDER, String.valueOf(tenantId));
        if ("".equals(Objects.toString(result.get("data"), "")) || result.isError()) {
            throw new ServiceException("成品收货任务号生成失败！");
        }
        taskInfo.setMaterialNo(detail.getMaterialNo());
        taskInfo.setMaterialName(detail.getMaterialName());
        taskInfo.setTaskNo(result.get("data").toString());
        //TODO 成品收货对应的入库任务 detailId为成品收货单据的id
        taskInfo.setDetailId(detail.getId());
        taskInfo.setTaskStatus(TaskStatusConstant.TASK_STATUS_NEW);
        taskInfo.setTaskType(StockInTaskConstant.FIN_ORDER_TASK_TYPE_FINISHED);
        taskInfo.setHandlerUserId(SecurityUtils.getUserId());
        taskInfo.setHandlerUserName(SecurityUtils.getUsername());
        taskInfo.setStockInOrderNo(orderNo);
        taskInfo.setCreateBy(SecurityUtils.getUsername());
        taskInfo.setCreateTime(DateUtils.getNowDate());
        // 新增库存地点
        taskInfo.setLocationCode(detail.getLocationCode());
        //批量添加
        if (taskInfoMapper.insertTaskInfo(taskInfo) <= 0) {
            throw new ServiceException("任务生成失败!");
        }
    }

    /**
     * 关闭成品入库任务
     *
     * @param stockInFinOrderDto 成品入库单
     * @return 结果
     */
    @Override
    public int closeStockInFinOrderOne(StockInFinOrderDto stockInFinOrderDto) {
        //1.成品收货状态为已关闭
        if (ObjectUtils.isEmpty(stockInFinOrderDto)) {
            throw new ServiceException("参数不存在");
        }
        List<StockInFinOrderDetailDto> stockInFinOrderDetailList = stockInFinOrderDto.getStockInFinOrderDetailDtoList();
        for (StockInFinOrderDetailDto detail : stockInFinOrderDetailList) {
            stockInFinOrderDetailMapper.updateStockInFinOrderDetail(detail);
        }
        return 1;
    }

    /**
     * 批量删除成品入库单
     *
     * @param ids 需要删除的成品入库单主键
     * @return 结果
     */
    @Override
    public int deleteStockInFinOrderByIds(Long[] ids) {
        return stockInFinOrderMapper.deleteStockInFinOrderByIds(ids);
    }

    /**
     * 删除成品入库单信息
     *
     * @param id 成品入库单主键
     * @return 结果
     */
    @Override
    public int deleteStockInFinOrderById(Long id) {
        return stockInFinOrderMapper.deleteStockInFinOrderById(id);
    }

    /**
     * 查询成品收货明细列表
     *
     * @param stockInFinOrderDetail 成品收货明细
     * @return 成品收货集合
     */
    @Override
    public List<StockInFinOrderDetail> selectStockInFinOrderDetailList(StockInFinOrderDetailVo stockInFinOrderDetail) {
        List<StockInFinOrderDetail> finOrderDetails = stockInFinOrderDetailMapper.selectStockInFinOrderDetailList(stockInFinOrderDetail);
        return finOrderDetails;
    }

    /**
     * 关闭成品入库单
     *
     * @param ids
     * @return
     */
    @Override
    public int closeStockInFinOrder(Long[] ids) {
        if(ObjectUtils.isEmpty(ids)){
            throw new ServiceException("参数不存在！");
        }
        for (Long id : ids) {
            // 更新收货任务状态为关闭
            StockInFinOrder stockInFinOrder = stockInFinOrderMapper.selectStockInFinOrderById(id);
            if (TaskStatusConstant.TASK_STATUS_CLOSE.equals(stockInFinOrder.getOrderStatus())
                    || TaskStatusConstant.TASK_STATUS_COMPLETE.equals(stockInFinOrder.getOrderStatus())) {
                throw new ServiceException("已关闭或已完成单据无法关闭！");
            }
            stockInFinOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_CLOSE);
            stockInFinOrder.setUpdateTime(DateUtils.getNowDate());
            stockInFinOrder.setUpdateBy(SecurityUtils.getUsername());
            stockInFinOrderMapper.updateStockInFinOrder(stockInFinOrder);

            // 更新任务状态为关闭
//            TaskInfo taskInfo = new TaskInfo();
//            taskInfo.setStockInOrderNo(stockInFinOrder.getOrderNo());
//            taskInfo.setTaskStatus(TaskStatusConstant.TASK_STATUS_CLOSE);
//            taskInfo.setUpdateTime(DateUtils.getNowDate());
//            taskInfo.setUpdateBy(SecurityUtils.getUsername());
//            taskInfoMapper.closeTaskInfoByNo(taskInfo);
        }
        return 1;
    }

    /**
     * 激活成品入库单
     *
     * @param ids 成品入库单ids
     * @return 结果
     */
    @Override
    public int activeStockInFinOrderByIds(Long[] ids) {
        //校验参数
        if (ObjectUtils.isEmpty(ids)) {
            throw new ServiceException("参数不存在！");
        }
        for (Long id : ids) {
            //查询单据
            StockInFinOrder stockInFinOrder = stockInFinOrderMapper.selectStockInFinOrderByIdOne(id);
            String status = stockInFinOrder.getOrderStatus();
            String orderNo = stockInFinOrder.getOrderNo();
            if (!OrderStatusConstant.ORDER_STATUS_NEW.equals(status)) {
                throw new ServiceException("成品入库单:" + orderNo + "必须为新建状态才能激活！");
            }
            //更新状态
            stockInFinOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_ACTIVE);
            stockInFinOrder.setUpdateBy(SecurityUtils.getUsername());
            stockInFinOrder.setUpdateTime(DateUtils.getNowDate());
            if (stockInFinOrderMapper.updateStockInFinOrder(stockInFinOrder) <= 0) {
                throw new ServiceException("更新成品入库单失败！");
            }
            //查询明细
            StockInFinOrderDetailVo stockInFinOrderDetailVo = new StockInFinOrderDetailVo();
            stockInFinOrderDetailVo.setFinOrderId(id);
            List<StockInFinOrderDetail> detailList = stockInFinOrderDetailMapper.selectStockInFinOrderDetailList(stockInFinOrderDetailVo);
            //更新明细
            for (StockInFinOrderDetail detail : detailList) {
                detail.setActiveQty(detail.getPlanRecivevQty());
                detail.setUpdateTime(DateUtils.getNowDate());
                detail.setUpdateBy(SecurityUtils.getUsername());
                if (stockInFinOrderDetailMapper.updateStockInFinOrderDetail(detail) <= 0) {
                    throw new ServiceException("更新成品入库单失败！");
                }
                SpringUtils.getBean(StockInFinOrderServiceImpl.class).addTaskInfo(detail,orderNo);
            }

        }
        return 1;
    }

    @Override
    public void mesToWmsProductReceiveTask(String json) {
        Map<String,Object> map = (Map<String,Object>) JSON.parse(json);
        // 获取工厂信息
        String factoryCode = Objects.toString(map.get("plantCode"), "");
        List<FactoryCommonDto> factoryList = esbSendCommonMapper.getFactoryByCode(factoryCode);
        if(CollectionUtils.isEmpty(factoryList)){
            throw new ServiceException(String.format("工厂代码：%s，不存在！",factoryCode));
        }
        // 获取物料信息
        String materialNo = Objects.toString(map.get("productCode"),"");
        MaterialAttrDto materialAttrDto = new MaterialAttrDto();
        materialAttrDto.setFactoryCode(factoryCode);
        materialAttrDto.setMaterialNo(materialNo);
        materialAttrDto.setFactoryId(factoryList.get(0).getId());
        List<MaterialMainDto> materialList = esbSendCommonMapper.getMaterialMain(materialAttrDto);
        if(CollectionUtils.isEmpty(materialList)){
            throw new ServiceException(String.format("物料号：%s，不存在！",materialNo));
        }
        // 新增成品收货单据
        StockInFinOrderDto stockInFinOrderDto = new StockInFinOrderDto();
        stockInFinOrderDto.setMaterialNo(materialNo);
        stockInFinOrderDto.setContainerNo(Objects.toString(map.get("containerCode"),""));
        stockInFinOrderDto.setFactoryCode(factoryCode);
        stockInFinOrderDto.setFactoryName(factoryList.get(0).getFactoryName());
        stockInFinOrderDto.setFactoryId(factoryList.get(0).getId());
        stockInFinOrderDto.setSourceLocationCode(Objects.toString(map.get("warehouseLocation"),""));
        stockInFinOrderDto.setMaterialName(Objects.toString(map.get("productName"),""));
        stockInFinOrderDto.setMaterialId(materialList.get(0).getMaterialId());
        // todo 目的库存点配置获取
//            setLocationCode(Objects.toString(map.get(""),""));
//            setLocationId();
        List<Map<String,Object>> detailList = (List<Map<String,Object>>)map.get("batchDetail");
        List<StockInFinOrderDetailDto> dList = new ArrayList();
        detailList.forEach(m->{
            StockInFinOrderDetailDto dto = new StockInFinOrderDetailDto();
            dto.setLot(Objects.toString(map.get("orderbatchNo"),""));
            dto.setPlanRecivevQty(BigDecimal.valueOf(Double.valueOf(Objects.toString(map.get("batchQuantity"),"0"))));
            // todo 包装单位
            dto.setUnit(materialList.get(0).getDefaultUnit());
            dList.add(dto);
        });
        stockInFinOrderDto.setStockInFinOrderDetailDtoList(dList);
        insertStockInFinOrder(stockInFinOrderDto);
    }

    /**
     * pda扫描收货生成成品收货单
     *
     * @param finProductScanVo
     */
    @Override
    public void addFinProductTask(FinProductScanVo finProductScanVo) {
        List<FinProductScanDetailDto> finOrderDetailList = finProductScanVo.getFinProductScanDetailDtoList();
        if (finOrderDetailList == null || finOrderDetailList.isEmpty()) {
            throw new ServiceException("成品收货明细不能为空!");
        }
        //校验成品入库单
        /*String prdOrderNo = finProductScanVo.getPrdOrderNo();
        StockInFinOrder inFinOrder = new StockInFinOrder();
        inFinOrder.setPrdOrderNo(prdOrderNo);
        inFinOrder.setFactoryCode(finProductScanVo.getFactoryCode());
        inFinOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_CLOSE);
        List<StockInFinOrder> stockInFinOrders = stockInFinOrderMapper.checkStockInFinOrder(inFinOrder);
        if (stockInFinOrders.size() > 0) {
            StockInFinOrder stockInFinOrder = stockInFinOrders.get(0);
            throw new ServiceException(String.format("货主：%s,生产订单号：%s,已在成品入库单:%s中！"
                    , finProductScanVo.getFactoryCode(), prdOrderNo, stockInFinOrder.getOrderNo()));
        }*/
        //新增成品收货单
        StockInFinOrder finOrder = new StockInFinOrder();
        BeanUtils.copyProperties(finProductScanVo, finOrder);
        Long tenantId = SecurityUtils.getLoginUser().getSysUser().getTenantId();

        AjaxResult ajaxResult = iCodeRuleService.getSeqWithTenantId(OrderNoTypeConstant.FIN_ORDER, String.valueOf(tenantId));
        if ("".equals(Objects.toString(ajaxResult.get("data"), "")) || ajaxResult.isError()) {
            throw new ServiceException("成品收货单据号生成失败！");
        }
        String materialName = finProductScanVo.getMaterialDesc();
        String orderNo = ajaxResult.get("data").toString();
        finOrder.setOrderNo(orderNo);
        finOrder.setMaterialName(materialName);
        finOrder.setOrderType(finProductScanVo.getRuleType());
        //激活
        finOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_ACTIVE);
        finOrder.setCreateBy(SecurityUtils.getUsername());
        finOrder.setCreateTime(DateUtils.getNowDate());
        finOrder.setDeptId(SecurityUtils.getLoginUser().getSysUser().getDeptId());
        //总数量
        BigDecimal bigDecimal = finOrderDetailList.stream().map(FinProductScanDetailDto::getTotalQty).
                reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        finOrder.setPlanQty(bigDecimal);

        List<String> materialNoList = finOrderDetailList.stream().map(FinProductScanDetailDto::getMaterialNo).collect(Collectors.toList());
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
            throw new ServiceException(String.format("不存在入库使用单位，请维护相关数据"));
        }
        Map<String, BigDecimal> map = unitList.stream().collect(Collectors.toMap(MaterialUnitDefDto::getMaterialNo, MaterialUnitDefDto::getConversDefault));
        Map<String, String> unitMap = unitList.stream().collect(Collectors.toMap(MaterialUnitDefDto::getMaterialNo, MaterialUnitDefDto::getUnit));
        int lineNo = 0;
        if (stockInFinOrderMapper.insertStockInFinOrder(finOrder) > 0) {
            //新增成品收货单明细
            for (FinProductScanDetailDto detail : finOrderDetailList) {
                StockInFinOrderDetail finOrderDetail = new StockInFinOrderDetail();
                finOrderDetail.setFinOrderId(finOrder.getId());
                finOrderDetail.setLineNo(String.valueOf(lineNo + 1));
                finOrderDetail.setMaterialNo(finOrder.getMaterialNo());
                finOrderDetail.setMaterialName(materialName);
                finOrderDetail.setPlanRecivevQty(detail.getTotalQty());
                finOrderDetail.setLot(detail.getLotNo());
                finOrderDetail.setMinPacking(detail.getMinPacking());
                finOrderDetail.setCreateBy(finOrder.getCreateBy());
                finOrderDetail.setCreateTime(finOrder.getCreateTime());
                BigDecimal convertsDefault = map.get(detail.getMaterialNo());
                String operationUnit = unitMap.get(detail.getMaterialNo());
                if (!Optional.ofNullable(convertsDefault).isPresent() || !Optional.ofNullable(operationUnit).isPresent()){
                    throw new ServiceException(String.format("%s物料号多级单位不存在或数据有误", detail.getMaterialNo()));
                }
                finOrderDetail.setOperationPlanRecivevQty(finOrderDetail.getPlanRecivevQty().divide(convertsDefault, 4, RoundingMode.HALF_UP));
                finOrderDetail.setUnit(operationUnit);
                finOrderDetail.setOperationUnit(operationUnit);
                // 扫描收货 明细状态为激活
                finOrderDetail.setDetailStatus(OrderStatusConstant.ORDER_STATUS_ACTIVE);
                if (stockInFinOrderDetailMapper.insertStockInFinOrderDetail(finOrderDetail) <= 0) {
                    throw new ServiceException("新增成品收货单据失败！");
                }
                SpringUtils.getBean(StockInFinOrderServiceImpl.class).addTaskInfo(finOrderDetail,orderNo);
            }
        } else {
            throw new ServiceException("新增成品收货单据失败！");
        }
    }

    /**
     * 批量确认
     * @param ids
     */
    @Override
    public AjaxResult batchConfirm(Long[] ids) {
        if (ObjectUtils.isEmpty(ids)) {
            throw new ServiceException("参数不存在！");
        }
        for (Long id : ids) {
            StockInFinOrder stockInFinOrder = stockInFinOrderMapper.selectStockInFinOrderById(id);
            stockInFinOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_COMPLETE);
            stockInFinOrderMapper.updateStockInFinOrder(stockInFinOrder);
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setDetailId(id);
            TaskInfo taskInfo1 = taskInfoMapper.selectTaskInfoByDetailId(taskInfo);
//            taskInfo1.setIsConfirm(Integer.parseInt(TaskStatusConstant.TASK_STATUS_COMPLETE));
            taskInfoMapper.updateTaskInfo(taskInfo1);
        }
        return AjaxResult.success();
    }
}
