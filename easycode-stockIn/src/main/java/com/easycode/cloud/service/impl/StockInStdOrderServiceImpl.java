package com.easycode.cloud.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.easycode.cloud.domain.ShelfTask;
import com.easycode.cloud.domain.StockInSemiFinOrder;
import com.easycode.cloud.domain.StockInSemiFinOrderDetail;
import com.easycode.cloud.domain.TaskInfo;
import com.easycode.cloud.domain.dto.TaskInfoDto;
import com.easycode.cloud.domain.vo.StockInOrederVo;
import com.easycode.cloud.domain.vo.StockInStdOrderDetailVo;
import com.easycode.cloud.domain.vo.WmsMaterialBasicVo;
import com.easycode.cloud.mapper.*;
import com.easycode.cloud.service.IShelfTaskService;
import com.easycode.cloud.service.IStockInStdOrderService;
import com.easycode.cloud.service.impl.vh.VHTaskInfoServiceImpl;
import com.mybatis.mybatis.plugin.RuleFieldThreadLocal;
import com.soa.eis.adapter.framework.message.IMsgObject;
import com.soa.eis.adapter.framework.message.impl.GroupRecord;
import com.soa.eis.adapter.framework.message.impl.MsgObject;
import com.weifu.cloud.common.core.exception.ServiceException;
import com.weifu.cloud.common.core.text.Convert;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.weifu.cloud.common.core.utils.SpringUtils;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.security.auth.AuthUtil;
import com.weifu.cloud.common.security.utils.DictUtils;
import com.weifu.cloud.common.security.utils.SecurityUtils;
import com.weifu.cloud.constant.*;
import com.weifu.cloud.domain.*;
import com.weifu.cloud.domain.dto.*;
import com.weifu.cloud.domain.vo.*;
import com.weifu.cloud.domian.*;
import com.weifu.cloud.domian.dto.*;
import com.weifu.cloud.domian.vo.BomInventoryVo;
import com.weifu.cloud.domian.vo.InventoryDetailsVo;
import com.weifu.cloud.domian.vo.StoragePositionVo;
import com.weifu.cloud.enums.RemoteConfigEnum;
import com.weifu.cloud.mapper.*;
import com.weifu.cloud.service.*;
import com.weifu.cloud.system.api.domain.SysDictData;
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
import org.springframework.util.ObjectUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static com.weifu.cloud.constant.OrderStatusConstant.ORDER_STATUS_NEW;


/**
 * 标准入库单Service业务层处理
 *
 * @author weifu
 * @date 2022-12-07
 */
@Service
@Transactional(rollbackFor = ServiceException.class)
public class StockInStdOrderServiceImpl implements IStockInStdOrderService {

    private static final Logger logger = LoggerFactory.getLogger(StockInStdOrderServiceImpl.class);
    @Autowired
    private StockInStdOrderMapper stockInStdOrderMapper;

    @Autowired
    private StockInStdOrderDetailMapper stockInStdOrderDetailMapper;

    @Autowired
    private StockInFinOrderMapper stockInFinOrderMapper;

    @Autowired
    private StockInFinOrderDetailMapper stockInFinOrderDetailMapper;

    @Autowired
    private StockInSemiFinOrderMapper stockInSemiFinOrderMapper;

    @Autowired
    private StockInSemiFinOrderDetailMapper stockInSemiFinOrderDetailMapper;

    @Autowired
    private DeliveryOrderDetailMapper deliveryOrderDetailMapper;

    @Autowired
    private EsbSendCommonMapper esbSendCommonMapper;

    @Autowired
    private TaskInfoMapper taskInfoMapper;

    @Autowired
    private RemoteConfigHelper remoteConfigHelper;

    @Autowired
    private ICodeRuleService iCodeRuleService;

    @Autowired
    private ITaskService taskService;

    @Autowired
    private IStockInService iStockInService;

    @Autowired
    private IMainDataService mainDataService;

    @Autowired
    private IMessageService messageService;

    @Autowired
    private IShelfTaskService shelfTaskService;
    @Autowired
    private RemoteEsbSendService remoteEsbSendService;

    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    private IInStoreService iInStoreService;

    @Autowired
    private StockInItemBomMapper stockInItemBomMapper;

    @Autowired
    private IReportService reportService;

    @Autowired
    private IPrintService printService;

    /**
     * 查询标准入库单
     *
     * @param id 标准入库单主键
     * @return 标准入库单
     */
    @Override
    public StockInStdOrder selectStockInStdOrderById(Long id) {
        StockInStdOrder stockInStdOrder = new StockInStdOrder();
        stockInStdOrder.setId(id);
        return stockInStdOrderMapper.selectStockInStdOrder(stockInStdOrder);
    }

    @Override
    public List<StockInStdOrderDetail> queryWmsStockinStdOrderDetailById(Long id) {
        return stockInStdOrderMapper.queryWmsStockinStdOrderDetailById(id);
    }

    /**
     * 查询标准入库单列表
     *
     * @param stockInStdOrder 标准入库单
     * @return 标准入库单
     */
    @Override
    public List<StockInStdOrder> selectStockInStdOrderList(StockInStdOrder stockInStdOrder) {
        return stockInStdOrderMapper.selectStockInStdOrderList(stockInStdOrder);
    }

    /**
     * 查询标准入库单列表
     *
     * @param printInfo 标准入库单
     * @return 标准入库单
     */
    @Override
    public List<PrintInfoVo> getPrintInfoByIds(PrintInfoVo printInfo) {
        Long[] ids = printInfo.getIds();
        List<PrintInfoVo> printInfoVos = stockInStdOrderMapper.getPrintInfoByIds(ids);
        List<GoodsSourceDef> goodsSourceDefList = new ArrayList<>();
        for (PrintInfoVo printInfoVo : printInfoVos) {
            GoodsSourceDef goodsSourceDef = new GoodsSourceDef();
            goodsSourceDef.setSupplierCode(printInfoVo.getVendorCode());
            goodsSourceDef.setMaterialNo(printInfoVo.getMaterialNo());
            goodsSourceDefList.add(goodsSourceDef);
        }
        AjaxResult ajaxResult = mainDataService.queryMaterialInfo(goodsSourceDefList);
        if (ajaxResult.isError()) {
            throw new ServiceException(ajaxResult.get("msg").toString());
        }
        List<GoodsSourceDef> minPackingList = JSON.parseArray(ajaxResult.get("data").toString(), GoodsSourceDef.class);
        for (GoodsSourceDef goods : minPackingList) {
            goods.setMaterialNo(String.format("%s-%s", goods.getMaterialNo(), goods.getSupplierCode()));
        }
        Map<String, BigDecimal> minPackingMap = minPackingList.stream().collect(Collectors.toMap(GoodsSourceDef::getMaterialNo, GoodsSourceDef::getMinPacking));

        for (PrintInfoVo printInfoVo : printInfoVos) {
            String purchaseOrderNo = printInfoVo.getPurchaseOrderNo();
            String deliveryOrderNo = printInfoVo.getDeliveryOrderNo();
            String materialNo = printInfoVo.getMaterialNo();
            String lotNo = printInfoVo.getLotNo();
            BigDecimal deliverQty = printInfoVo.getDeliverQty().setScale(0);
            String vendorCode = printInfoVo.getVendorCode();
            String purchaseLineNo = printInfoVo.getPurchaseLineNo();
            BigDecimal minPacking = Optional.ofNullable(minPackingMap.get(String.format("%s-%s", materialNo, vendorCode))).orElse(deliverQty);
            int copies = deliverQty.divide(minPacking).setScale(0, BigDecimal.ROUND_UP).intValue();
            printInfoVo.setCopies(copies);
            String qrCode = String.format("O%s%%D%s%%M%s%%Q%s%%B%s%%V%s%%L%s%%X1/1", purchaseOrderNo, deliveryOrderNo, materialNo,
                    deliverQty, lotNo, vendorCode, purchaseLineNo);
            printInfoVo.setQrCode(qrCode);
            printInfoVo.setMinPacking(minPacking.setScale(0).toString());
        }
        return printInfoVos;
    }

    /**
     * 新增标准入库单
     *
     * @param stockInStdOrderDtoList 标准入库单集合
     * @return 结果
     */
    @Override
    public int insertStockInStdOrder(String stockInStdOrderDtoList) {
        List<StockInStdOrderDto> list = JSON.parseArray(stockInStdOrderDtoList, StockInStdOrderDto.class);
        if (ObjectUtils.isEmpty(list)) {
            throw new ServiceException("数据不存在！");
        }
        Long tenantId = null;
        String fromType = "0";
        if (SrmConstant.SOURCE_SRM.equals(AuthUtil.getLoginUser(SecurityUtils.getToken()).getTenantId().toString())) {
            RuleFieldThreadLocal.set(new HashMap<>());
            tenantId = list.get(0).getTenantId();
            fromType = "1";
        }

        //校验入库单
        StockInStdOrder firstOrder = new StockInStdOrder();
        for (StockInStdOrderDto stockInStdOrderDto : list) {
            firstOrder.setOrderNo(stockInStdOrderDto.getOrderNo());
            firstOrder.setTenantId(tenantId);
            StockInStdOrder stockInStdOrder = stockInStdOrderMapper.selectStockInStdOrder(firstOrder);
            if (!Objects.isNull(stockInStdOrder)) {
                throw new ServiceException("入库单已存在！");
            }
        }
        RemoteConfigEnum createStockInTask = RemoteConfigEnum.CREATE_STOCK_IN_TASK;
        String isCreateStockInTask = remoteConfigHelper.getConfig(createStockInTask.getKey());
        if ("".equals(Objects.toString(isCreateStockInTask, ""))) {
            throw new ServiceException("获取参数配置失败！");
        }
        List<Long> ids = new ArrayList<>(list.size());
        //新增标准入库单
        for (StockInStdOrderDto stockInStdOrderDto : list) {
            StockInStdOrder stockInStdOrder = new StockInStdOrder();
            BeanUtils.copyProperties(stockInStdOrderDto, stockInStdOrder);
            //根据配置 set入库单状态
            if (createStockInTask.getValue().equals(isCreateStockInTask)) {
                stockInStdOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_ACTIVE);
            } else {
                stockInStdOrder.setOrderStatus(ORDER_STATUS_NEW);
            }
            stockInStdOrder.setOrderType("1");
            // SRM平台调用wms的方法时，token 中不存在租户id，所以不能将tenantId设置null
            if (!SrmConstant.SOURCE_SRM.equals(AuthUtil.getLoginUser(SecurityUtils.getToken()).getTenantId().toString())) {
                stockInStdOrder.setTenantId(null);
            }
            stockInStdOrder.setCreateBy(SecurityUtils.getUsername());
            stockInStdOrder.setCreateTime(DateUtils.getNowDate());
            if (stockInStdOrderMapper.insertStockInStdOrder(stockInStdOrder) <= 0) {
                throw new ServiceException("新增标准入库单失败!");
            }
            ids.add(stockInStdOrder.getId());

            List<StockInStdOrderDetailDto> detailList = stockInStdOrderDto.getStockInStdOrderDetailDtoList();
            //新增标准入库单明细
            int lineNo = 1;
            for (StockInStdOrderDetailDto s : detailList) {
                s.setStdOrderId(stockInStdOrder.getId());
                s.setLineNo(Convert.toStr(lineNo++));
                s.setCreateBy(SecurityUtils.getUsername());
                s.setCreateTime(DateUtils.getNowDate());
                // 根据基础单位换算为入库使用单位
                MaterialUnitDefDto firstUnitDef = new MaterialUnitDefDto();
                firstUnitDef.setFactoryCode(stockInStdOrder.getFactoryCode());
                firstUnitDef.setMaterialNo(s.getMaterialNo());
                firstUnitDef.setStockinEnable(CommonYesOrNo.YES);
                firstUnitDef.setTenantId(tenantId);
                AjaxResult ajaxResult = mainDataService.queryMaterialUnitDef(firstUnitDef);
                if (ajaxResult.isError()) {
                    throw new ServiceException(ajaxResult.get("msg").toString());
                }
                List<MaterialUnitDefDto> unitList = JSON.parseArray(ajaxResult.get("data").toString(), MaterialUnitDefDto.class);
                if (ObjectUtils.isEmpty(unitList)) {
                    throw new ServiceException(String.format("物料%s、货主%s不存在入库使用单位，请维护相关数据", s.getMaterialNo(), stockInStdOrder.getFactoryCode()));
                }
                if (unitList.size() > 1) {
                    throw new ServiceException(String.format("数据有误，物料%s、货主%s存在多条入库使用单位", s.getMaterialNo(), stockInStdOrder.getFactoryCode()));
                }
                MaterialUnitDefDto materialUnitDefDto = unitList.get(0);
                s.setOperationUnit(materialUnitDefDto.getUnit());
                s.setConversDefault(materialUnitDefDto.getConversDefault());
                s.setOperationQty(s.getDeliverQty().divide(materialUnitDefDto.getConversDefault(), 4, RoundingMode.HALF_UP));
                s.setOperationReceivedQty(new BigDecimal(0));
                s.setOperationPackageQty(new BigDecimal(0));
                s.setTenantId(tenantId);
                s.setStatus(ORDER_STATUS_NEW);
                if (stockInStdOrderDetailMapper.insertStockInStdOrderDetail(s) <= 0) {
                    throw new ServiceException("新增标准入库单失败!");
                }
            }
        }
        // 根据标配置项判断 是否走自动生成任务
        if (createStockInTask.getValue().equals(isCreateStockInTask)) {
            Long[] longs = ids.toArray(new Long[ids.size()]);
            StockInStdOrderDto stockInStdOrderDto = new StockInStdOrderDto();
            stockInStdOrderDto.setIds(longs);
            stockInStdOrderDto.setTenantId(tenantId);
            stockInStdOrderDto.setFromType(fromType);
            int result = activeStockInStdOrder(stockInStdOrderDto);
            if (result <= 0) {
                throw new ServiceException("自动生成标准入库任务失败！");
            }
        }
        return 1;
    }

    /**
     * 关闭标准入库单
     *
     * @param stockInStdOrder 标准入库单
     * @return 结果
     */
    @Override
    @GlobalTransactional
    public int closeStockInStdOrder(StockInStdOrder stockInStdOrder) {
        StockInStdOrder inStdOrder = new StockInStdOrder();
        inStdOrder.setId(stockInStdOrder.getId());
        StockInStdOrder order = stockInStdOrderMapper.selectStockInStdOrder(inStdOrder);
        String orderStatus = order.getOrderStatus();
        //1.入库单状态为已关闭
        if (OrderStatusConstant.ORDER_STATUS_CLOSE.equals(orderStatus)) {
            throw new ServiceException("入库单已关闭状态异常！！");
        }

        //2.入库单状态为新建、激活、部分完成
        if (OrderStatusConstant.ORDER_STATUS_PART_COMPLETE.equals(orderStatus)
                || ORDER_STATUS_NEW.equals(orderStatus)
                || OrderStatusConstant.ORDER_STATUS_ACTIVE.equals(orderStatus)) {
            closeByStdOrderId(order);
        }
        //3.调用供应商服务 修改送货单状态为关闭,修改明细状态为已删除
        DeliveryOrderVo deliveryOrderVo = new DeliveryOrderVo();
        deliveryOrderVo.setId(order.getDeliveryOrderId());
        deliveryOrderVo.setStatus(OrderStatusConstant.ORDER_STATUS_CLOSE);
        AjaxResult ajaxResult = iStockInService.updateDeliveryOrderStatus(deliveryOrderVo);
        if (ajaxResult.isError()) {
            throw new ServiceException("修改送货单失败！");
        }
        //4.关闭对应入库任务
        TaskInfoVo taskInfoVo = new TaskInfoVo();
        taskInfoVo.setStockInOrderNo(order.getOrderNo());
        List<TaskInfoVo> taskInfoList = taskInfoMapper.selectTaskInfoList(taskInfoVo);
        if (!ObjectUtils.isEmpty(taskInfoList)) {
            for (TaskInfoVo taskInfo : taskInfoList) {
                if (TaskStatusConstant.TASK_STATUS_CLOSE.equals(taskInfo.getTaskStatus())) {
                    continue;
                }
                TaskInfo info = new TaskInfo();
                info.setId(taskInfo.getId());
                info.setTaskStatus(TaskStatusConstant.TASK_STATUS_CLOSE);
                info.setUpdateBy(SecurityUtils.getUsername());
                info.setUpdateTime(DateUtils.getNowDate());
                int result = taskInfoMapper.updateTaskInfo(info);
                if (result != 1) {
                    throw new ServiceException("关闭入库任务失败！");
                }
            }
        }
        //5.入库单状态为已完成  修改为已关闭
        stockInStdOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_CLOSE);
        stockInStdOrder.setUpdateBy(SecurityUtils.getUsername());
        stockInStdOrder.setUpdateTime(DateUtils.getNowDate());
        return stockInStdOrderMapper.updateStockInStdOrder(stockInStdOrder);
    }

    /**
     * 入库单关闭(从单据关闭入口)
     *
     * @param stockInStdOrder 入库单
     */
    public void closeByStdOrderId(StockInStdOrder stockInStdOrder) {
        // 2.1根据入库单id查询入库明细列表
        StockInStdOrderDetailDto dto = new StockInStdOrderDetailDto();
        dto.setStdOrderId(stockInStdOrder.getId());
        List<StockInStdOrderDetail> list = stockInStdOrderDetailMapper.selectStockInStdOrderDetailList(dto);
        processingReceipt(list, stockInStdOrder.getDeliveryOrderId());
    }

    /**
     * 入库任务关闭(从任务关闭入口)
     *
     * @param taskInfo 入库任务
     * @param flag     是否是否关闭送货单
     */
    public void closeByTaskId(TaskInfo taskInfo, boolean flag) {
        if (ObjectUtils.isEmpty(taskInfo)) {
            throw new ServiceException("入库任务不存在！");
        }
        StockInStdOrderDetailDto dto = new StockInStdOrderDetailDto();

        //
        if (Objects.isNull(taskInfo.getStockInOrderNo())) {
            throw new ServiceException("入库单据不存在！");
        }
        StockInStdOrder stockInStdOrder1 = stockInStdOrderMapper.selectStockInStdOrderByNo(taskInfo.getStockInOrderNo());
        dto.setId(taskInfo.getDetailId());
        List<StockInStdOrderDetail> list = stockInStdOrderDetailMapper.selectStockInStdOrderDetailList(dto);
        processingReceipt(list, stockInStdOrder1.getDeliveryOrderId());
        if (flag) {
            StockInStdOrder stockInStdOrder = new StockInStdOrder();
            //修改标准入库状态为已关闭
            stockInStdOrder.setOrderNo(taskInfo.getStockInOrderNo());
            stockInStdOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_CLOSE);
            stockInStdOrder.setUpdateBy(SecurityUtils.getUsername());
            stockInStdOrder.setUpdateTime(DateUtils.getNowDate());
            stockInStdOrderMapper.updateStockInStdOrder(stockInStdOrder);

            //修改送货单状态为关闭
            DeliveryOrderVo deliveryOrderVo = new DeliveryOrderVo();
            deliveryOrderVo.setDeliveryOrderNo(taskInfo.getStockInOrderNo());
            deliveryOrderVo.setStatus(OrderStatusConstant.ORDER_STATUS_CLOSE);
            deliveryOrderVo.setUpdateBy(SecurityUtils.getUsername());
            deliveryOrderVo.setUpdateTime(DateUtils.getNowDate());
            AjaxResult ajaxResult = iStockInService.updateDeliveryOrderStatus(deliveryOrderVo);
            if (ajaxResult.isError()) {
                throw new ServiceException("修改送货单失败！");
            }
        }
    }

    /**
     * 关闭入库任务业务处理
     *
     * @param list 入库单明细集合
     */
    public void processingReceipt(List<StockInStdOrderDetail> list, Long deliveryOrderId) {
        if (ObjectUtils.isEmpty(list)) {
            return;
        }
        //去除已完成,已关闭的标准入库明细
        List<StockInStdOrderDetail> finalList = list.stream()
                .filter(s -> !OrderStatusConstant.ORDER_STATUS_COMPLETE.equals(s.getStatus()))
                .filter(s -> !OrderStatusConstant.ORDER_STATUS_CLOSE.equals(s.getStatus())).collect(Collectors.toList());
        //标准入库单明细 均已完成或已关闭 则不需要回写数量
        if (ObjectUtils.isEmpty(finalList)) {
            return;
        }
        List<PurchaseOrderDetail> purchaseOrderDetails = new ArrayList<>();
        for (StockInStdOrderDetail stockInStdOrderDetail : finalList) {
            PurchaseOrderDetail detail = new PurchaseOrderDetail();
            detail.setPurchaseOrderNo(stockInStdOrderDetail.getPurchaseOrderNo());
            detail.setPurchaseLineNo(stockInStdOrderDetail.getPurchaseLineNo());
            detail.setMaterialNo(stockInStdOrderDetail.getMaterialNo());
            detail.setWmsReceivedQty(stockInStdOrderDetail.getReceivedQty());
            // 因为需求数量新的计算逻辑，需要送货单id，将送货单id传给供应商服务
            detail.setPurchaseOrderId(deliveryOrderId);
            detail.setMadeQty(stockInStdOrderDetail.getDeliverQty().subtract(stockInStdOrderDetail.getReceivedQty()));
            purchaseOrderDetails.add(detail);
        }
        String params = JSON.toJSONString(purchaseOrderDetails);
        // 调用供应商服务 更新采购单需求数量及已制数量
        AjaxResult ajaxResult = iStockInService.updatePurchaseOrderDetailBatch(params);
        if (ajaxResult.isError()) {
            throw new ServiceException("更新采购单失败！");
        }
    }

    /**
     * 数量计算
     *
     * @param unreceivedQty       未收货数量
     * @param purchaseOrderDetail 采购明细
     * @return
     */
    public PurchaseOrderDetail updateQty(BigDecimal unreceivedQty, PurchaseOrderDetail purchaseOrderDetail) {

        //总需求数量 100
        BigDecimal totalQty = purchaseOrderDetail.getTotalQty();
        //sap收货数量 90
        BigDecimal sapReceivedQty = purchaseOrderDetail.getSapReceivedQty();
        //wms总需求数量 80 = 总需求数量 100 - (sap收货数量 90 - wms收货数量 70)
        BigDecimal totalPlanQty = purchaseOrderDetail.getTotalPlanQty();
        //wms收货数量 70
        BigDecimal beWmsReceivedQty = purchaseOrderDetail.getWmsReceivedQty();
        //已制数量 70
        BigDecimal beMadeQty = purchaseOrderDetail.getMadeQty();
        // sap自收数量 20
        BigDecimal sapQty = sapReceivedQty.subtract(beWmsReceivedQty);
        //防止入库任务未完成时 sap收货数量有变动 导致多收
//        if (totalPlanQty.compareTo(totalQty.subtract(sapQty)) > 0){
//            throw new ServiceException("sap收货数量有变动，请核对！");
//        }
        PurchaseOrderDetail detail = new PurchaseOrderDetail();
        BeanUtils.copyProperties(purchaseOrderDetail, detail);
        //TODO 可回写数量
        if (unreceivedQty.compareTo(beMadeQty) < 0) {
            BigDecimal newMadeQty = beMadeQty.subtract(unreceivedQty);
            detail.setTotalPlanQty(totalPlanQty);
            detail.setPlanQty(totalPlanQty.subtract(newMadeQty));
            detail.setMadeQty(newMadeQty);

        } else {
            detail.setMadeQty(BigDecimal.ZERO);
            detail.setPlanQty(totalPlanQty);
            detail.setTotalPlanQty(totalPlanQty);
        }
        return detail;
    }

    /**
     * 查询入标准入口单明细列表
     *
     * @param stockInStdOrderDetailDto
     * @return
     */
    @Override
    public List<StockInStdOrderDetail> selectStockInStdOrderDetailList(StockInStdOrderDetailDto stockInStdOrderDetailDto) {
        return stockInStdOrderDetailMapper.selectStockInStdOrderDetailList(stockInStdOrderDetailDto);
    }

    /**
     * 批量激活标准入库单
     *
     * @param stockInStdOrderDto 标准入库单dto
     * @return 结果
     */
    @Override
    public int activeStockInStdOrder(StockInStdOrderDto stockInStdOrderDto) {

        Long[] ids = stockInStdOrderDto.getIds();
        // 1 为srm调用
        String fromType = stockInStdOrderDto.getFromType();
        if (CommonYesOrNo.NO.equals(fromType)) {
            stockInStdOrderDto.setTenantId(AuthUtil.getLoginUser(SecurityUtils.getToken()).getTenantId());
        }
        //收集数据list
        List<TaskInfo> taskInfoList = new ArrayList<>();
        //校验入库单
        for (Long id : ids) {
            StockInStdOrder paramsStockInStdOrder = new StockInStdOrder();
            paramsStockInStdOrder.setId(id);
            paramsStockInStdOrder.setTenantId(stockInStdOrderDto.getTenantId());
            StockInStdOrder stockInStdOrder = stockInStdOrderMapper.selectStockInStdOrder(paramsStockInStdOrder);
            if (Objects.isNull(stockInStdOrder)) {
                throw new ServiceException("标准入库单不存在！");
            }
            StockInStdOrderDetailDto stockInStdOrderDetailDto = new StockInStdOrderDetailDto();
            stockInStdOrderDetailDto.setStdOrderId(id);
            stockInStdOrderDetailDto.setTenantId(stockInStdOrderDto.getTenantId());
            List<StockInStdOrderDetail> detailList = stockInStdOrderDetailMapper.selectStockInStdOrderDetailList(stockInStdOrderDetailDto);
            if (ObjectUtils.isEmpty(detailList)) {
                throw new ServiceException("标准入库单明细不存在！");
            }
            //修改标准入库单状态为激活
            StockInStdOrder paramsOrder = new StockInStdOrder();
            paramsOrder.setId(id);
            paramsOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_ACTIVE);
            paramsOrder.setUpdateBy(SecurityUtils.getUsername());
            paramsOrder.setUpdateTime(DateUtils.getNowDate());
            paramsOrder.setTenantId(stockInStdOrderDto.getTenantId());
            int result = stockInStdOrderMapper.updateStockInStdOrder(paramsOrder);
            if (result <= 0) {
                throw new ServiceException("修改标准入库单状态失败！");
            }
            //添加入库任务集合
            for (StockInStdOrderDetail stockInStdOrderDetail : detailList) {
                TaskInfo taskInfo = new TaskInfo();
                BeanUtils.copyProperties(stockInStdOrderDetail, taskInfo);

                AjaxResult ajaxResult = null;
                if (CommonYesOrNo.YES.equals(fromType)) {
                    ajaxResult = iCodeRuleService.getSeqWithTenantId(TaskNoTypeConstant.STOCK_IN_STD_TASK, Convert.toStr(stockInStdOrder.getTenantId()));
                } else {
                    Long tenantId = SecurityUtils.getLoginUser().getSysUser().getTenantId();

                    ajaxResult = iCodeRuleService.getSeqWithTenantId(TaskNoTypeConstant.STOCK_IN_STD_TASK, Convert.toStr(tenantId));
                }
                if (ajaxResult.isError() || StringUtils.isEmpty(ajaxResult.get("data").toString())) {
                    throw new ServiceException("入库任务号生成失败！");
                }
                taskInfo.setTaskNo(ajaxResult.get("data").toString());
                taskInfo.setDetailId(stockInStdOrderDetail.getId());
                taskInfo.setTaskStatus(TaskStatusConstant.TASK_STATUS_NEW);
                taskInfo.setTaskType(stockInStdOrderDetail.getDetailType());
                taskInfo.setHandlerUserId(SecurityUtils.getUserId());
                taskInfo.setHandlerUserName(SecurityUtils.getUsername());
                taskInfo.setTenantId(stockInStdOrderDto.getTenantId());
                taskInfo.setStockInOrderNo(stockInStdOrder.getOrderNo());
                taskInfo.setCreateBy(SecurityUtils.getUsername());
                taskInfo.setCreateTime(DateUtils.getNowDate());
                taskInfo.setLocationCode(stockInStdOrderDetail.getLocationCode());
                taskInfoList.add(taskInfo);
                //更新标准入库单明细状态
                stockInStdOrderDetail.setStatus(OrderStatusConstant.ORDER_STATUS_ACTIVE);
                stockInStdOrderDetail.setTenantId(stockInStdOrderDto.getTenantId());
                stockInStdOrderDetail.setCreateBy(SecurityUtils.getUsername());
                stockInStdOrderDetail.setCreateTime(DateUtils.getNowDate());
                stockInStdOrderDetailMapper.updateStockInStdOrderDetail(stockInStdOrderDetail);
            }
        }
        //批量添加任务
        String tenantId = AuthUtil.getLoginUser(SecurityUtils.getToken()).getTenantId().toString();
        if (Objects.isNull(AuthUtil.getLoginUser(SecurityUtils.getToken()).getTenantId()) || SrmConstant.SOURCE_SRM.equals(tenantId)) {
            if (taskInfoMapper.insertTaskInfoListNoTenantId(taskInfoList) <= 0) {
                throw new ServiceException("任务生成失败！");
            }
        } else {
            if (taskInfoMapper.insertTaskInfoList(taskInfoList) <= 0) {
                throw new ServiceException("任务生成失败！");
            }
        }

        return 1;
    }

    /**
     * 同步标准入库单(ASN)
     * @param params
     * @return
     */
    @Override
    public String syncStockInStdOrder(Map<String, Object> params) {
        List<Map<String, Object>> headList = (List<Map<String, Object>>) params.get("HEAD");
        List<Map<String, Object>> itemList = (List<Map<String, Object>>) params.get("ITEM");
        List<Map<String, Object>> itemBomList = (List<Map<String, Object>>) params.get("ITEM_BOM");

        Long tenantId = MainDataConstant.TENANT_ID;
        String createBy = MainDataConstant.USER_SAP;

        Map<String, Object> orderMap = headList.get(0); // header只有一个
        // A：新增 B：修改
        String opFlag = Convert.toStr(orderMap.get("OP_FLAG"));
        // 单号
        String orderNo = Convert.toStr(orderMap.get("VBELN"));
        // ASN号
        String asnNo = Convert.toStr(orderMap.get("LIFEX"));
        // 交货日期
        String deliveryDate = Convert.toStr(orderMap.get("LFDAT"));
        // 供应商
        String supplierCode = Convert.toStr(orderMap.get("LIFNR"));
        // 供应商描述
        String supplierName = Convert.toStr(orderMap.get("NAME1"));
        // 总重量
        String totalWeight = Convert.toStr(orderMap.get("BTGEW"));
        // 净重量
        String netWeight = Convert.toStr(orderMap.get("NTGEW"));
        // 重量单位
        String weightUnit = Convert.toStr(orderMap.get("GEWEI"));
        // 包数
        Integer packageNum = Convert.toInt(orderMap.get("ANZPK"));
        // 交货单类型
        String deliveryType = Convert.toStr(orderMap.get("LFART"));

        StockInStdOrder stockInStdOrder;

        if (ObjectUtils.isEmpty(asnNo)) {
            throw new ServiceException("ASN号不能为空");
        }

        StockInStdOrderDto dto = new StockInStdOrderDto();
        dto.setOrderNo(orderNo);

        String factoryCode = Convert.toStr(itemList.get(0).get("WERKS"));
        List<FactoryCommonDto> factoryList = esbSendCommonMapper.getFactoryByCodeAndTenantId(factoryCode, tenantId);
        if (ObjectUtils.isEmpty(factoryCode)) {
            throw new ServiceException(String.format("根据工厂code:%s未查询到相关信息", factoryCode));
        }
        FactoryCommonDto factoryInfo = factoryList.get(0);
        dto.setFactoryId(factoryInfo.getId());
        dto.setFactoryCode(factoryInfo.getFactoryCode());
        dto.setFactoryName(factoryInfo.getFactoryName());

        // 总重量
        dto.setTotalWeight(totalWeight);
        // 净重量
        dto.setNetWeight(netWeight);
        // 重量单位
        dto.setWeightUnit(weightUnit);
        // 包数
        dto.setPackageNum(packageNum);
        // 交货单类型
        dto.setDeliveryType(deliveryType);

        // 获取供应商信息
        SupplierCommonDto supplierCommonDto = esbSendCommonMapper.getSupplierInfoByCode(supplierCode);

        if (ObjectUtils.isEmpty(supplierCommonDto)) {
            throw new ServiceException(String.format("根据供应商code:%s未查询到相关信息", supplierCode));
        }

        // ASN号信息
        dto.setAsnNo(asnNo);
        dto.setBusinessType(StockInOrderConstant.ORDER_TYPE_ASN);

        dto.setVendorId(supplierCommonDto.getId());
        dto.setVendorName(supplierName);
        dto.setVendorCode(supplierCommonDto.getSupplierCode());

        dto.setOrderType(StockInOrderConstant.ORDER_TYPE_ASN);
        dto.setOrderStatus(OrderStatusConstant.ORDER_STATUS_NEW);
        dto.setDeliveryDate(DateUtils.parseDate(deliveryDate));
        dto.setTenantId(tenantId);
        dto.setCreateBy(createBy);
        dto.setCreateTime(DateUtils.getNowDate());
        dto.setUpdateBy(createBy);
        dto.setUpdateTime(DateUtils.getNowDate());
        dto.setHasBom(CollectionUtil.isEmpty(itemBomList) ? CommonYesOrNo.NO : CommonYesOrNo.YES);

        stockInStdOrder = stockInStdOrderMapper.selectStockInStdOrderByAsnNo(asnNo);

        // A新增 ASN号存在 新增  B修改
        if (ObjectUtils.isEmpty(stockInStdOrder) || ObjectUtils.isEmpty(stockInStdOrder.getId())) {
            logger.info("新增ASN单据，ASN: {} ", asnNo);
            stockInStdOrderMapper.insertStockInStdOrder(dto);
        } else {
            Long orderId = stockInStdOrder.getId();
            List<String> checkStatus = new ArrayList<>(Arrays.asList(
                    OrderStatusConstant.ORDER_STATUS_NEW, OrderStatusConstant.ORDER_STATUS_ACTIVE));
            String orderStatus = stockInStdOrder.getOrderStatus();
            logger.info("ASN单据已存在，当前订单状态: {} ", orderStatus);

            TaskInfoVo taskInfoVo = new TaskInfoVo();
            taskInfoVo.setStockInOrderNo(stockInStdOrder.getOrderNo());
            List<TaskInfoVo> taskInfoVoList = taskInfoMapper.selectStdTaskInfoList(taskInfoVo);

            List<String> checkTaskStatus = new ArrayList<>(Arrays.asList(TaskStatusConstant.TASK_STATUS_PART_COMPLETE,
                    TaskStatusConstant.TASK_STATUS_COMPLETE));
            List<TaskInfoVo> completeTaskList = taskInfoVoList.stream().filter(
                    item -> checkTaskStatus.contains(item.getTaskStatus())).collect(Collectors.toList());
            logger.info("ASN单据完成任务数量: {} ", completeTaskList.size());

            if (checkStatus.contains(orderStatus) && completeTaskList.isEmpty()) {
                logger.info("ASN单据已存在，重新生成单据信息，ASN: {} ", asnNo);
                stockInStdOrderMapper.deleteStockInStdOrderById(orderId);

                StockInStdOrderDetailDto stockInStdOrderDetailDto = new StockInStdOrderDetailDto();
                stockInStdOrderDetailDto.setPurchaseOrderNo(stockInStdOrder.getOrderNo());
                stockInStdOrderDetailDto.setStdOrderId(orderId);
                List<StockInStdOrderDetail> detailList = stockInStdOrderDetailMapper.selectStockInStdOrderDetailList(stockInStdOrderDetailDto);

                if (CollectionUtils.isNotEmpty(detailList)) {
                    logger.info("删除单据关联的明细信息，订单编号：{} ", stockInStdOrder.getOrderNo());
                    Long[] detailIds = detailList.stream().map(StockInStdOrderDetail::getId).toArray(Long[]::new);
                    stockInStdOrderDetailMapper.deleteStockInStdOrderDetailByIds(detailIds);
                }

                if (CollectionUtils.isNotEmpty(taskInfoVoList)) {
                    logger.info("删除单据关联的任务信息，订单编号：{} ", stockInStdOrder.getOrderNo());
                    Long[] taskIds = taskInfoVoList.stream().map(TaskInfoVo::getId).toArray(Long[]::new);
                    taskInfoMapper.deleteTaskInfoByIds(taskIds);
                }

                stockInStdOrderMapper.insertStockInStdOrder(dto);
            } else {
                return "该单据已在执行中，不允许修改！";
            }
        }

        // 生成单据明细信息
        List<StockInStdOrderDetail> detailList = generateStockInStdOrderDetails(itemList, dto, packageNum, tenantId, createBy);

        // 记录asn单据bom详情信息
        addASNBomInfo(itemBomList, dto, tenantId, supplierCode);

        // asn单据自动生成入库任务
        addStockInTask(asnNo, detailList, tenantId, orderNo);

        return "任务同步成功";
    }

    private List<StockInStdOrderDetail> generateStockInStdOrderDetails(List<Map<String, Object>> itemList, StockInStdOrderDto dto,
                                                                       Integer packageNum, Long tenantId, String createBy) {
        List<StockInStdOrderDetail> detailList = new ArrayList<>();
        Map<String, Object> itemMap = itemList.get(0);
        // 校验入库单
        for (Map<String, Object> item : itemList) {
            // 交货单
            String deliveryNo = Convert.toStr(item.get("VBELN"));
            // 交货单行号
            String lineNo = Convert.toStr(item.get("POSNR"));
            // 物料编码
            String materialNo = Convert.toStr(item.get("MATNR"));
            // 物料描述
            String materialName = Convert.toStr(item.get("ARKTX"));
            // 交货数量
            String deliveryNum = Convert.toStr(item.get("LFIMG"));
            // 交货单位
            String deliveryUnit = Convert.toStr(item.get("VRKME"));
            // 基本计量单位
            String baseUnit = Convert.toStr(item.get("MEINS"));
            // 分母
            String denominator = Convert.toStr(item.get("UMVKN"));
            // 分子
            String numerator = Convert.toStr(item.get("UMVKZ"));
            // 数量（库存单位）
            String amount = Convert.toStr(item.get("LGMNG"));
            // 净重
            String itemNetWeight = Convert.toStr(item.get("NTGEW"));
            // 毛重
            String grossWeight = Convert.toStr(item.get("BRGEW"));
            // 重量单位
            String itemWeightUnit = Convert.toStr(item.get("GEWEI"));
            // 供应商批次
            String vendorBatchNo = Convert.toStr(item.get("LICHN"));
            // 工厂
            String itemFactory = Convert.toStr(item.get("WERKS"));
            // 库存地点
            String locationCode = Convert.toStr(item.get("LGORT"));
            // 批次
            String batchNo = Convert.toStr(item.get("CHARG"));
            // 参考单据的单据编号
            String vgbel = Convert.toStr(item.get("VGBEL"));
            // 参考项目的项目号
            String vgpos = Convert.toStr(item.get("VGPOS"));
            // 库存类型(是否质检)
            String stockType = Convert.toStr(item.get("INSMK"));
            // WBS 要素
            String wbsElement = Convert.toStr(item.get("PS_PSP_PNR"));
            // 获取移动类型
            String moveType = Convert.toStr(itemMap.get("BWART"));

            if (ObjectUtils.isEmpty(locationCode)) {
                throw new ServiceException("SAP接口返回的LGORT库存地点为空！");
            }

            AjaxResult locationCodeCountAjaxResult = mainDataService.getLocationCodeCount(locationCode);
            if (locationCodeCountAjaxResult.isError()) {
                throw new ServiceException(String.format("查询库存地点失败，库存地点:%s, 不存在!", locationCode));
            }
            AjaxResult materialBasicInfo = mainDataService.getMaterialBasicInfo(materialNo);
            if(materialBasicInfo.isError()) {
                throw new ServiceException(materialBasicInfo.get("msg").toString());
            }
            WmsMaterialBasicVo materialInfo = com.alibaba.fastjson2.JSONObject.parseObject(materialBasicInfo.get("data").toString(), WmsMaterialBasicVo.class);
            if(ObjectUtils.isEmpty(materialInfo)){
                throw new ServiceException("不存在该物料号"+materialNo);
            }
            StockInStdOrderDetail stockInStdOrderDetail = new StockInStdOrderDetail();
            stockInStdOrderDetail.setStdOrderId(dto.getId());
            stockInStdOrderDetail.setPurchaseOrderNo(deliveryNo);
            stockInStdOrderDetail.setPurchaseLineNo(lineNo);
            stockInStdOrderDetail.setMaterialNo(materialNo);
            stockInStdOrderDetail.setMaterialName(materialName);
            stockInStdOrderDetail.setVrkme(deliveryUnit);
            stockInStdOrderDetail.setMeins(baseUnit);
            stockInStdOrderDetail.setUmvkn(denominator);
            stockInStdOrderDetail.setUmvkz(numerator);
            stockInStdOrderDetail.setLgmng(amount);
            stockInStdOrderDetail.setNtgew(itemNetWeight);
            stockInStdOrderDetail.setBrgew(grossWeight);
            stockInStdOrderDetail.setGewei(itemWeightUnit);
            stockInStdOrderDetail.setLichn(vendorBatchNo);
            stockInStdOrderDetail.setFactoryCode(itemFactory);
            stockInStdOrderDetail.setVgbel(vgbel);
            stockInStdOrderDetail.setVgpos(vgpos);
            stockInStdOrderDetail.setInsmk(stockType);
            stockInStdOrderDetail.setWbsElement(wbsElement);
            stockInStdOrderDetail.setBwart(moveType);
            stockInStdOrderDetail.setOldMaterialNo(materialInfo.getOldMaterialNo());
            stockInStdOrderDetail.setDeliverQty(new BigDecimal(amount));
            stockInStdOrderDetail.setAvailableQty(new BigDecimal(amount));
            stockInStdOrderDetail.setReceivedQty(new BigDecimal(amount));
            stockInStdOrderDetail.setLfimg(Long.parseLong(String.valueOf(new BigDecimal(deliveryNum).intValue())));

            // 是否质检
            stockInStdOrderDetail.setIsQc("x".equalsIgnoreCase(stockType) || "2".equals(stockType) ? CommonYesOrNo.YES : CommonYesOrNo.NO);
            stockInStdOrderDetail.setIsConsign(CommonYesOrNo.NO);
            stockInStdOrderDetail.setDetailType(StockInOrderConstant.ORDER_TYPE_ASN);
            if (packageNum == null) {
                stockInStdOrderDetail.setMinPacking("0");
            } else {
                stockInStdOrderDetail.setMinPacking(Convert.toStr(packageNum));
            }
            stockInStdOrderDetail.setUnit(baseUnit);
            stockInStdOrderDetail.setOperationUnit(baseUnit);
            stockInStdOrderDetail.setIsPrinted("0");

            if (ObjectUtils.isEmpty(batchNo)) {
                AjaxResult ajaxResult = iCodeRuleService.getSeqWithTenantId(StockInTaskConstant.ENTER_LOT_NO, tenantId.toString());
                if (ajaxResult.isError() || StringUtils.isEmpty(ajaxResult.get("data").toString())) {
                    throw new ServiceException("入库批次号生成失败！");
                }
                stockInStdOrderDetail.setLotNo(Convert.toStr(ajaxResult.get("data")));
                logger.info("生成入库批次号：{} ", stockInStdOrderDetail.getLotNo());
            } else {
                stockInStdOrderDetail.setLotNo(batchNo);
                logger.info("SAP提供的入库批次号：{} ", stockInStdOrderDetail.getLotNo());
            }

            stockInStdOrderDetail.setLocationCode(locationCode);
            stockInStdOrderDetail.setThirdDeliveryOrderNo(deliveryNo);
            stockInStdOrderDetail.setThirdDeliveryLineNo(lineNo);
            stockInStdOrderDetail.setLineNo(lineNo);
            stockInStdOrderDetail.setStatus(ORDER_STATUS_NEW);
            stockInStdOrderDetail.setTenantId(tenantId);
            stockInStdOrderDetail.setCreateBy(createBy);
            stockInStdOrderDetail.setCreateTime(DateUtils.getNowDate());
            stockInStdOrderDetail.setUpdateBy(createBy);
            stockInStdOrderDetail.setUpdateTime(DateUtils.getNowDate());
            stockInStdOrderDetail.setMoveType(moveType);

            stockInStdOrderDetailMapper.insertStockInStdOrderDetail(stockInStdOrderDetail);
            logger.info("ASN标准入库单详情新增完成，add: {}", stockInStdOrderDetail);
            detailList.add(stockInStdOrderDetail);
        }
        return detailList;
    }

    private void addStockInTask(String asnNo, List<StockInStdOrderDetail> detailList, Long tenantId, String orderNo) {
        logger.info("ASN号: {}", asnNo);
        // ASN入库时生成任务
        if (!ObjectUtils.isEmpty(asnNo)) {
            // 按明细添加入库任务集合
            List<TaskInfo> taskInfoList = new ArrayList<>();
            for (StockInStdOrderDetail stockInStdOrderDetail : detailList) {
                logger.info("入库明细信息: {}", stockInStdOrderDetail);
                TaskInfo taskInfo = new TaskInfo();
                BeanUtils.copyProperties(stockInStdOrderDetail, taskInfo);
                AjaxResult ajaxResult = iCodeRuleService.getSeqWithTenantId(TaskNoTypeConstant.STOCK_IN_STD_ASN_TASK, Convert.toStr(tenantId));
                if (ObjectUtils.isEmpty(ajaxResult) || ajaxResult.isError() || StringUtils.isEmpty(ajaxResult.get("data").toString())) {
                    throw new ServiceException("入库任务号生成失败！");
                }
                taskInfo.setTaskNo(ajaxResult.get("data").toString());
                taskInfo.setDetailId(stockInStdOrderDetail.getId());
                taskInfo.setTaskStatus(TaskStatusConstant.TASK_STATUS_NEW);
                taskInfo.setTaskType(TaskTypeConstant.STOCK_IN_ASN_TASK);
                taskInfo.setHandlerUserId(SecurityUtils.getUserId());
                taskInfo.setHandlerUserName(SecurityUtils.getUsername());
                taskInfo.setTenantId(tenantId);
                taskInfo.setStockInOrderNo(orderNo);
                taskInfo.setQuantityLssued(stockInStdOrderDetail.getReceivedQty());
                taskInfo.setCreateBy(SecurityUtils.getUsername());
                taskInfo.setCreateTime(DateUtils.getNowDate());
                taskInfo.setLocationCode(stockInStdOrderDetail.getLocationCode());
                taskInfo.setSourceLocationCode(stockInStdOrderDetail.getLocationCode());
                taskInfo.setOldMaterialNo(stockInStdOrderDetail.getOldMaterialNo());
                taskInfoList.add(taskInfo);

                //更新标准入库单明细状态
                stockInStdOrderDetail.setStatus(OrderStatusConstant.ORDER_STATUS_ACTIVE);
                stockInStdOrderDetail.setTenantId(tenantId);
                stockInStdOrderDetail.setUpdateBy(SecurityUtils.getUsername());
                stockInStdOrderDetail.setUpdateTime(DateUtils.getNowDate());
                stockInStdOrderDetailMapper.updateStockInStdOrderDetail(stockInStdOrderDetail);
            }

            //批量添加任务
            if (CollectionUtils.isNotEmpty(taskInfoList)) {
                logger.info("批量添加任务: {}", taskInfoList);
                if (taskInfoMapper.insertTaskInfoListNoTenantId(taskInfoList) <= 0) {
                    throw new ServiceException("任务生成失败！");
                }
            }
        }
    }

    private void addASNBomInfo(List<Map<String, Object>> itemBomList, StockInStdOrderDto dto,
                               Long tenantId, String supplierCode) {
        String userName = SecurityUtils.getUsername();
        Date nowDate = DateUtils.getNowDate();

        if (CollectionUtil.isNotEmpty(itemBomList)) {
            stockInItemBomMapper.deleteStockInItemBomDetails(dto.getId());
        }
        for (Map<String, Object> itemBom : itemBomList) {
            // 物料号---BWART
            String matnr = Convert.toStr(itemBom.get("MATNR"));
            // 物料描述
            String maktx = Convert.toStr(itemBom.get("MAKTX"));
            // 交货单
            String vbeln = Convert.toStr(itemBom.get("VBELN"));
            // 交货单行号
            String posnr = Convert.toStr(itemBom.get("POSNR"));
            //行
            Integer linct = Convert.toInt(itemBom.get("LINCT"));
            // 基本单位数量
            BigDecimal bdmng = Convert.toBigDecimal(itemBom.get("BDMNG").toString().trim());
            // 基本单位
            String lagme = Convert.toStr(itemBom.get("LAGME"));
            // BOM单位数量
            BigDecimal erfmg = Convert.toBigDecimal(itemBom.get("ERFMG").toString().trim());
            // BOM单位
            String erfme = Convert.toStr(itemBom.get("ERFME"));
            // 库存地点
            String locationCode = Convert.toStr(itemBom.get("LGORT"));

            String materialNo = matnr.replaceFirst("^0+", "");
            StockInItemBom stockInItemBom = new StockInItemBom();
            stockInItemBom.setMatnr(materialNo);
            stockInItemBom.setMaktx(maktx);
            stockInItemBom.setVbeln(vbeln);
            stockInItemBom.setPosnr(posnr);
            stockInItemBom.setLinct(linct);
            if (ObjectUtils.isEmpty(bdmng)) {
                stockInItemBom.setBdmng(BigDecimal.ZERO);
            } else {
                stockInItemBom.setBdmng(new BigDecimal(Convert.toStr(bdmng).trim()));
            }
            stockInItemBom.setLagme(lagme);
            if (ObjectUtils.isEmpty(erfmg)) {
                stockInItemBom.setErfmg(BigDecimal.ZERO);
            } else {
                stockInItemBom.setErfmg(new BigDecimal(Convert.toStr(erfmg).trim()));
            }
            stockInItemBom.setErfme(erfme);
            stockInItemBom.setStdOrderId(dto.getId());
            stockInItemBom.setLocationCode(locationCode);
            stockInItemBom.setSupplierCode(supplierCode);
            stockInItemBom.setTenantId(tenantId);
            stockInItemBom.setUpdateBy(userName);
            stockInItemBom.setCreateBy(userName);
            stockInItemBom.setCreateTime(nowDate);
            stockInItemBom.setUpdateTime(nowDate);
            stockInItemBomMapper.insertStockInItemBomDetails(stockInItemBom);
        }
    }

    public int printInspectTask(PrintTOParamsDto printTOParamsDto) {
        AjaxResult printerByLocationAjaxResult = printService.findPrinterByLocation(printTOParamsDto.getPrintLocationCode());

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

        jsonObject.put("taskType", "成品/半成品入库任务");
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
        // 物料号
        jsonObject.put("oldMaterialNo", printTOParamsDto.getOldMaterialNo());
        jsonArray.add(jsonObject);
        dto.setDataArray(jsonArray);

        AjaxResult ajaxResult1 = printService.printByTemplate(dto);

        if (ajaxResult1.isError()) {
            throw new ServiceException(String.format("打印失败,原因：%s", ajaxResult1.get("msg").toString()));
        }
        return HttpStatus.SC_OK;
    }

    /**
     * 同步半成品/成品入库单
     *
     * @param itemList
     * @return
     */
    @Override
    @GlobalTransactional
    public int syncStockInSemOrFinOrder(List<Map<String, Object>> itemList) throws Exception {

        Long tenantId = 6L;
        PrintTOParamsDto printTOParamsDto = new PrintTOParamsDto();
        // 主键等于BUSNO
        // 不存在新增 存在修改
        // 131 带HU号成品入库, 不带HU半成品
        // wms_stockin_fin_order
        Boolean needPrint = false;
        for (Map<String, Object> itemMap : itemList) {
            // 业务ID
            String busno = Convert.toStr(itemMap.get("BUSNO"));
            // 获取物料凭证
            String mblnr = Convert.toStr(itemMap.get("MBLNR"));
            // 获取移动类型
            String bwart = Convert.toStr(itemMap.get("BWART"));
            // 获取工厂编码
            String werks = Convert.toStr(itemMap.get("WERKS"));
            // 获取过账日期
            String budat = Convert.toStr(itemMap.get("BUDAT"));
            // 获取凭证日期
            String bldat = Convert.toStr(itemMap.get("BLDAT"));
            // 获取抬头文本（HU号）
            String bktxt = Convert.toStr(itemMap.get("BKTXT"));
            // 获取MES容器号
            String exidv2 = Convert.toStr(itemMap.get("EXIDV2"));
            // 获取订单编号
            String aufnr = Convert.toStr(itemMap.get("AUFNR"));
            // 获取项目
            String zeile = Convert.toStr(itemMap.get("ZEILE"));
            // 获取物料编码
            String matnr = Convert.toStr(itemMap.get("MATNR"));
            // 获取物料描述
            String maktx = Convert.toStr(itemMap.get("MAKTX"));
            // 获取数量
            String menge = Convert.toStr(itemMap.get("MENGE")).trim();
            if (ObjectUtil.isEmpty(menge)) {
                throw new ServiceException("menge为空");
            }
            AjaxResult materialBasicInfo = mainDataService.getMaterialBasicInfo(matnr);
            if(materialBasicInfo.isError()) {
                throw new ServiceException(materialBasicInfo.get("msg").toString());
            }
            WmsMaterialBasicVo materialInfo = com.alibaba.fastjson2.JSONObject.parseObject(materialBasicInfo.get("data").toString(), WmsMaterialBasicVo.class);
            if(ObjectUtils.isEmpty(materialInfo)){
                throw new ServiceException("不存在该物料号"+matnr);
            }
            // 获取单位
            String meins = Convert.toStr(itemMap.get("MEINS"));
            if("MG".equals(meins)||"G".equals(meins)){
                if("MG".equals(materialInfo.getDefaultUnit())&&"G".equals(meins)){
                    meins = "MG";
                    menge = new BigDecimal(menge).multiply(new BigDecimal("1000")).toString();
                }
                if("G".equals(materialInfo.getDefaultUnit())&&"MG".equals(meins)){
                    meins = "G";
                    menge = new BigDecimal(menge).divide(new BigDecimal("1000")).toString();
                }
            }
            // 获取批次
            String charg = Convert.toStr(itemMap.get("CHARG"));
            // 获取库存地点
            String lgort = Convert.toStr(itemMap.get("LGORT"));
            // 获取原凭证
            String smbln = Convert.toStr(itemMap.get("SMBLN"));
            // 获取原凭证行号
            String smblp = Convert.toStr(itemMap.get("SMBLP"));
            //  工作中心
            String arbpl = Convert.toStr(itemMap.get("ARBPL"));
            // 库存地点
            String lgort1 = Convert.toStr(itemMap.get("LGORT1"));
            //特殊库存类型
            String sobkz = Convert.toStr(itemMap.get("SOBKZ"));
            //WBS元素
            String wbsElem = Convert.toStr(itemMap.get("PS_PSP_PNR"));
            //用户名
            String uname = Convert.toStr(itemMap.get("UNAME"));

            String positionNo = "";
            InventoryHistory ih = new InventoryHistory();
            ih.setMblnr(mblnr);
            ih.setSmblp(busno.substring(busno.length() - 4));
            AjaxResult ihResult = iInStoreService.selectInventoryHistoryList(ih);
            if (ihResult.isError()) {
                throw new ServiceException("查询库存历史台账变更失败！");
            }
            List<InventoryHistory> ihs = JSON.parseArray(JSON.toJSONString(ihResult.get("data")), InventoryHistory.class);
            if(CollectionUtils.isNotEmpty(ihs)){
                return 1;
            }
            StockInFinOrder stockInFinOrder = new StockInFinOrder();
            StockInFinOrder order = new StockInFinOrder();
            order.setOrderNo(busno);
            List<StockInFinOrderDto> stockInFinOrderList = stockInFinOrderMapper.selectStockInFinOrderList(order);
            String status = "A";
            if (CollectionUtils.isNotEmpty(stockInFinOrderList)) {
                status = "B";
                stockInFinOrder.setId(stockInFinOrderList.get(0).getId());
            }

            List<FactoryCommonDto> factoryInfo = esbSendCommonMapper.getFactoryByCodeAndTenantId(werks, tenantId);
            if (factoryInfo.size() <= 0) {
                throw new ServiceException(String.format("根据工厂code:%s未查询到相关信息", werks));
            }
            // 根据业务ID判断是否唯一
            // 如果有 E 已存在
            // 如果无 S 成功 MESSAGE 插入成功
            AjaxResult lotAjaxResult = iCodeRuleService.getSeqWithTenantId(StockInTaskConstant.ENTER_LOT_NO, tenantId.toString());
            if (lotAjaxResult.isError() || StringUtils.isEmpty(lotAjaxResult.get("data").toString())) {
                throw new ServiceException("入库批次号生成失败！");
            }
            stockInFinOrder.setMaterialName(maktx);
            stockInFinOrder.setMaterialNo(matnr);
            stockInFinOrder.setOldMaterialNo(materialInfo.getOldMaterialNo());
            stockInFinOrder.setOrderNo(busno);
            stockInFinOrder.setOrderType(OrderConstant.IN_FIN);
            stockInFinOrder.setBusno(busno);
            stockInFinOrder.setMblnr(mblnr);
            stockInFinOrder.setBwart(bwart);
            stockInFinOrder.setWerks(werks);
            stockInFinOrder.setBudat(budat);
            stockInFinOrder.setBldat(bldat);
            stockInFinOrder.setHuNo(bktxt.startsWith("HU") ? bktxt.split("HU")[1] : bktxt);
            stockInFinOrder.setExidv2(exidv2);
            stockInFinOrder.setAufnr(aufnr);
            stockInFinOrder.setZeile(zeile);
            stockInFinOrder.setMatnr(matnr);
            stockInFinOrder.setMaktx(maktx);
            stockInFinOrder.setMenge(menge);
            stockInFinOrder.setMeins(meins);
            stockInFinOrder.setCharg(charg);
            stockInFinOrder.setLgort(lgort);
            stockInFinOrder.setSmbln(smbln);
            stockInFinOrder.setSmblp(smblp);
            stockInFinOrder.setArbpl(arbpl);
            stockInFinOrder.setLgort1(lgort1);
            stockInFinOrder.setTenantId(tenantId);
            stockInFinOrder.setUpdateBy("SAP");
            stockInFinOrder.setUpdateTime(DateUtils.getNowDate());
            stockInFinOrder.setWbsElem(wbsElem);
            stockInFinOrder.setSobkz(sobkz);
            stockInFinOrder.setUname(uname);
            stockInFinOrder.setLot(lotAjaxResult.get("data").toString());
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setOldMaterialNo(materialInfo.getOldMaterialNo());
            StoragePositionVo storagePositionVo = new StoragePositionVo();
            if ("131".equals(bwart)) {
                // 生成任务号
                AjaxResult ajaxResult = iCodeRuleService.getSeqWithTenantId(TaskNoTypeConstant.STOCK_IN_FIN_ORDER, Convert.toStr(tenantId));
                if ("".equals(Objects.toString(ajaxResult.get("data"), "")) || ajaxResult.isError()) {
                    throw new ServiceException("任务号生成失败！");
                }
                //默认仓位字典
                List<SysDictData> defPositionNo = DictUtils.getDictCache("location_default_bin");
                if(ObjectUtils.isEmpty(defPositionNo) || defPositionNo.isEmpty()) {
                    throw new ServiceException("获取默认仓位字典类型失败");
                }
                for(SysDictData e:defPositionNo){
                    if(e.getDictLabel().equals(lgort)){
                        positionNo = e.getDictValue();
                    }
                }
                AjaxResult result = null;
                if(lgort.equals("2050")||lgort.equals("2060")){
                    result = mainDataService.getPositionCodeByLocationCodeAndMaterialNo("3000",matnr);
                    if(result.isError()){
                        throw new ServiceException(Objects.toString(result.get("msg"),"获取仓位失败"));
                    }
                    storagePositionVo = JSON.parseObject(JSON.toJSONString(result.get("data")), StoragePositionVo.class);
                    if("1".equals(storagePositionVo.getMixedFlag())) {
                        Long[] ids = {storagePositionVo.getId()};
                        mainDataService.updateStoragePositionStatus(ids, "1");
                    }
                    taskInfo.setLocationCode("3000");
                }else{
                    StoragePositionVo storagePosition = new StoragePositionVo();
                    storagePosition.setPositionNo(positionNo);
                    result = mainDataService.getStoragePositionList(storagePosition);
                    if(result.isError()){
                        throw new ServiceException(Objects.toString(result.get("msg"),"获取仓位失败"));
                    }
                    List<StoragePositionVo> list = JSON.parseArray(JSON.toJSONString(result.get("data")), StoragePositionVo.class);
                    if(CollectionUtils.isEmpty(list)){
                        throw new ServiceException("未找到"+positionNo+"仓位的信息");
                    }
                    storagePositionVo = list.get(0);
                    taskInfo.setLocationCode(lgort);
                    //result = mainDataService.getPositionCodeByLocationCodeAndMaterialNo(lgort,matnr);
                }

                String taskNo = Convert.toStr(ajaxResult.get("data"));
//                Long[] ids = {storagePositionVo.getId()};
//                mainDataService.updateStoragePositionStatus(ids,"1");
                taskInfo.setSourceLocationCode(lgort);
                taskInfo.setPositionId(storagePositionVo.getId());
                taskInfo.setPositionNo(storagePositionVo.getPositionNo());
                taskInfo.setTaskNo(taskNo);
                taskInfo.setStockInOrderNo(busno);
                taskInfo.setMaterialNo(matnr);
                taskInfo.setTaskStatus(TaskStatusConstant.TASK_STATUS_NEW);
                taskInfo.setCreateBy("SAP");
                taskInfo.setCreateTime(DateUtils.getNowDate());
                taskInfo.setTaskType(TaskTypeConstant.FINISHED_PRODUCT_LISTING_TASK);
                taskInfo.setTenantId(tenantId);
                taskInfo.setMaterialName(maktx);
                taskInfo.setPrintSum(1);
                taskInfo.setPrintStatus(1);
                taskInfo.setBatchNo(charg);
                printTOParamsDto.setTaskType("成品/半成品入库任务");
                printTOParamsDto.setFactoryCode(werks);
                printTOParamsDto.setLocationCode(lgort);
                printTOParamsDto.setMaterialName(maktx);
                printTOParamsDto.setMaterialNo(matnr);
                printTOParamsDto.setTaskNo(taskNo);
                printTOParamsDto.setPositionNo(storagePositionVo.getPositionNo());
                printTOParamsDto.setLot(charg);
                printTOParamsDto.setMoveQty(new BigDecimal(menge));
                printTOParamsDto.setPackQty(new BigDecimal(menge));
                printTOParamsDto.setUserName(uname);
            }
            if ("101".equals(bwart)) {
                // 生成任务号
                AjaxResult ajaxResult = iCodeRuleService.getSeqWithTenantId(TaskNoTypeConstant.STOCK_IN_FIN_ORDER, Convert.toStr(tenantId));
                if ("".equals(Objects.toString(ajaxResult.get("data"), "")) || ajaxResult.isError()) {
                    throw new ServiceException("任务号生成失败！");
                }
                //默认仓位字典
                List<SysDictData> defPositionNo = DictUtils.getDictCache("location_default_bin");
                if(ObjectUtils.isEmpty(defPositionNo) || defPositionNo.isEmpty()) {
                    throw new ServiceException("获取默认仓位字典类型失败");
                }
                for(SysDictData e:defPositionNo){
                    if(e.getDictLabel().equals(lgort)){
                        positionNo = e.getDictValue();
                    }
                }
                AjaxResult result = null;
                if(lgort.equals("2000")){
                    result = mainDataService.getPositionCodeByLocationCodeAndMaterialNo("3050",matnr);
                    if(result.isError()){
                        throw new ServiceException(Objects.toString(result.get("msg"),"获取仓位失败"));
                    }
                    storagePositionVo = JSON.parseObject(JSON.toJSONString(result.get("data")), StoragePositionVo.class);
                    if("1".equals(storagePositionVo.getMixedFlag())){
                        Long[] ids = {storagePositionVo.getId()};
                        mainDataService.updateStoragePositionStatus(ids,"1");
                    }
                    taskInfo.setLocationCode("3050");
                }else{
                    StoragePositionVo storagePosition = new StoragePositionVo();
                    storagePosition.setPositionNo(positionNo);
                    result = mainDataService.getStoragePositionList(storagePosition);
                    if(result.isError()){
                        throw new ServiceException(Objects.toString(result.get("msg"),"获取仓位失败"));
                    }
                    List<StoragePositionVo> list = JSON.parseArray(JSON.toJSONString(result.get("data")), StoragePositionVo.class);
                    if(CollectionUtils.isEmpty(list)){
                        throw new ServiceException("未找到"+positionNo+"仓位的信息");
                    }
                    storagePositionVo = list.get(0);
                    taskInfo.setLocationCode(lgort);
//                     result = mainDataService.getPositionCodeByLocationCodeAndMaterialNo(lgort,matnr);
                }

                String taskNo = Convert.toStr(ajaxResult.get("data"));
//                Long[] ids = {storagePositionVo.getId()};
//                mainDataService.updateStoragePositionStatus(ids,"1");
                taskInfo.setSourceLocationCode(lgort);
                taskInfo.setPositionId(storagePositionVo.getId());
                taskInfo.setPositionNo(storagePositionVo.getPositionNo());
                taskInfo.setTaskNo(taskNo);
                taskInfo.setStockInOrderNo(busno);
                taskInfo.setMaterialNo(matnr);
                taskInfo.setTaskStatus(TaskStatusConstant.TASK_STATUS_NEW);
                taskInfo.setCreateBy("SAP");
                taskInfo.setCreateTime(DateUtils.getNowDate());
                taskInfo.setTaskType(TaskTypeConstant.FINISHED_PRODUCT_LISTING_TASK);
                taskInfo.setTenantId(tenantId);
                taskInfo.setMaterialName(maktx);
                taskInfo.setBatchNo(charg);
                taskInfo.setPrintSum(1);
                taskInfo.setPrintStatus(1);
                printTOParamsDto.setTaskType("成品/半成品入库任务");
                printTOParamsDto.setFactoryCode(werks);
                printTOParamsDto.setLocationCode(lgort);
                printTOParamsDto.setMaterialName(maktx);
                printTOParamsDto.setMaterialNo(matnr);
                printTOParamsDto.setTaskNo(taskNo);
                printTOParamsDto.setPositionNo(storagePositionVo.getPositionNo());
                printTOParamsDto.setLot(charg);
                printTOParamsDto.setMoveQty(new BigDecimal(menge));
                printTOParamsDto.setPackQty(new BigDecimal(menge));
                printTOParamsDto.setUserName(uname);
            }

            if ("131".equals(bwart) || "101".equals(bwart)) {
                needPrint = true;
                if ("A".equals(status)) {
                    stockInFinOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_ACTIVE);
                    stockInFinOrder.setCreateTime(DateUtils.getNowDate());
                    stockInFinOrder.setCreateBy("SAP");
                    stockInFinOrderMapper.insertStockInFinOrder(stockInFinOrder);
                } else {
                    stockInFinOrder.setUpdateTime(DateUtils.getNowDate());
                    stockInFinOrder.setUpdateBy("SAP");
                    stockInFinOrderMapper.updateStockInFinOrder(stockInFinOrder);
                }
//                Long inventoryDetailsId = 0l;
//                if(stockInFinOrder.getSobkz().equals("Q")){
//                    inventoryDetailsId = updatespecialInventoryDetailsProduct(storagePositionVo,stockInFinOrder);
//                }else{
//                    inventoryDetailsId = updateInventoryDetailsProduct(storagePositionVo,stockInFinOrder);
//                }
                if(lgort.equals("2050")||lgort.equals("2060")||lgort.equals("2000")){
                    if(lgort.equals("2000")){
                        printTOParamsDto.setLocationCode("3050");
                    }else{
                        printTOParamsDto.setLocationCode("3000");
                    }
                    printTOParamsDto.setMoveType(bwart+",311");
//                    Long[] ids = {storagePositionVo.getId()};
//                    mainDataService.updateStoragePositionStatus(ids,"1");
                    taskInfo.setSourcePositionNo(positionNo);
                    printTOParamsDto.setSourcePostionNo(positionNo);
                }else{
                    taskInfo.setSourcePositionNo(storagePositionVo.getPositionNo());
                    printTOParamsDto.setSourcePostionNo(storagePositionVo.getPositionNo());
                    printTOParamsDto.setMoveType(bwart);
                }
                printTOParamsDto.setPrintLocationCode(lgort1);
                printTOParamsDto.setOldMaterialNo(materialInfo.getOldMaterialNo());
                printTOParamsDto.setSourceStorageType(storagePositionVo.getRemark());
                InventoryDetails inventoryDetails = updateInventoryDetailsProduct(storagePositionVo,stockInFinOrder,taskInfo);
                taskInfo.setDetailId(stockInFinOrder.getId());
                taskInfo.setQuantityLssued(new BigDecimal(menge));
                taskInfoMapper.insertTaskInfo(taskInfo);
                InventoryHistory inventoryHistory = new InventoryHistory();
                inventoryHistory.setOperationType("2");
                inventoryHistory.setQty(new BigDecimal(stockInFinOrder.getMenge()));
                inventoryHistory.setInventoryId(inventoryDetails.getId());
                inventoryHistory.setCreateTime(DateUtils.getNowDate());
                inventoryHistory.setCreateBy("SAP");
                inventoryHistory.setTaskNo(taskInfo.getTaskNo());
                inventoryHistory.setTenantId(6l);
                inventoryHistory.setBatchNo(charg);
                inventoryHistory.setPostionNo(storagePositionVo.getPositionNo());
                inventoryHistory.setInventoryLocal(lgort);
                inventoryHistory.setMblnr(mblnr);
                inventoryHistory.setSmblp(busno.substring(busno.length() - 4));
                inventoryHistory.setOldMaterialNo(inventoryDetails.getOldMaterialNo());
                inventoryHistory.setMaterialNo(inventoryDetails.getMaterialNo());
                inventoryHistory.setMaterialName(inventoryDetails.getMaterialName());
                iInStoreService.insertInventoryHistory(inventoryHistory);
                if("BC".equals(stockInFinOrder.getHuNo())){
                    taskInfo.setUpdateTime(DateUtils.getNowDate());
                    taskInfo.setUpdateBy("SAP");
                    taskInfo.setTaskStatus(TaskStatusConstant.TASK_STATUS_PART_COMPLETE);
                    TaskInfoDto taskInfoDto = new TaskInfoDto();
                    BeanUtils.copyProperties(taskInfo,taskInfoDto);
                    SpringUtils.getBean(VHTaskInfoServiceImpl.class).submitFinOrderTask(taskInfoDto);
                }else{
                    reserveStorage(inventoryDetails,taskInfo);
                }
            }
            if ("102".equals(bwart) || "132".equals(bwart)) {
//                if (sobkz.equals("Q")) {
//                    //特殊库存
//                    handleSpecialInventory(stockInFinOrder);
//                } else {
//                    //非特殊库存
//                    handleInventory(stockInFinOrder);
//                }
                handleInventory(stockInFinOrder);
                StockInFinOrder stock = new StockInFinOrder();
                stock.setMblnr(smbln);
                stock.setBusno(smblp);
                List<StockInFinOrderDto> stockInFinOrderDtos = stockInFinOrderMapper.selectStockInFinOrderList(stock);
                if(CollectionUtils.isEmpty(stockInFinOrderDtos)){
                    throw new ServiceException("原凭证号"+smbln+"未查到单据");
                }
                StockInFinOrderDto stockInFinOrderDto = stockInFinOrderDtos.get(0);
                StockInFinOrder stockInFinOrder1 = new StockInFinOrder();
                stockInFinOrder1.setSmbln(mblnr);
                stockInFinOrder1.setId(stockInFinOrderDto.getId());
                stockInFinOrder1.setUpdateTime(DateUtils.getNowDate());
                stockInFinOrder1.setUpdateBy("SAP");
                stockInFinOrder1.setOrderStatus(OrderStatusConstant.ORDER_STATUS_CLOSE);
                stockInFinOrderMapper.updateStockInFinOrder(stockInFinOrder1);
                TaskInfo taskInfo1 = new TaskInfo();
                taskInfo1.setTaskStatus(TaskStatusConstant.TASK_STATUS_CLOSE);
                taskInfo1.setUpdateBy("SAP");
                taskInfo1.setUpdateTime(DateUtils.getNowDate());
                taskInfo1.setStockInOrderNo(stockInFinOrderDto.getOrderNo());
                taskInfoMapper.closeTaskInfoByNo(taskInfo1);
                taskInfo1.setTaskType(TaskTypeConstant.FINISHED_PRODUCT_LISTING_TASK);
                taskInfo1.setDetailId(stockInFinOrderDto.getId());
                TaskInfo tf = taskInfoMapper.selectTaskInfoByDetailId(taskInfo1);
                if(tf==null){
                    throw new ServiceException("原凭证号"+smbln+"未查到任务");
                }
                iInStoreService.removeByTaskNos(Arrays.asList(tf.getTaskNo()));
            }
            //262根据库存地点批次号物料号查库存台账，不会有库存台账的记录
            if ("261".equals(bwart)) {
                //筛选批次之后，按批次扣库存数量
                InventoryDetailsVo inventoryDetails = new InventoryDetailsVo();
                inventoryDetails.setIsFreeze(CommonYesOrNo.NO);
                inventoryDetails.setIsConsign(CommonYesOrNo.NO);
                inventoryDetails.setIsQc(CommonYesOrNo.NO);
                inventoryDetails.setStockSpecFlag(CommonYesOrNo.NO);
                inventoryDetails.setMaterialNo(matnr);
                inventoryDetails.setLocationCode(lgort);
                if(StringUtils.isNotBlank(charg)){
                    inventoryDetails.setStockInLot(charg);
                }
                AjaxResult ajaxResult = iInStoreService.listAllVInventory(inventoryDetails);
                if (ajaxResult.isError()) {
                    throw new ServiceException("查询物料号"+matnr+"库存失败！");
                }
                List<InventoryDetails> inventoryDetails1 = JSON.parseArray(ajaxResult.get("data").toString(), InventoryDetails.class);
                //if(inventoryDetails1==null||inventoryDetails1.size()<=0){
//                    try{
//                        throw new ServiceException("查询物料号"+matnr+"库存台账失败！");
//                    }catch (Exception e){
              //          insertSapToWmsReport(stockInFinOrder,BigDecimal.ZERO);
//                        throw e;
//                    }
                //}
                BigDecimal mengeDecimal = new BigDecimal(stockInFinOrder.getMenge());
                BigDecimal sum = BigDecimal.ZERO;
                for(InventoryDetails details : inventoryDetails1){
                    sum = sum.add(details.getAvailableQty());
                }
                if(mengeDecimal.compareTo(sum)>0){
//                    try{
//                        throw new ServiceException("物料号"+stockInFinOrder.getMatnr()+"库存台账可用数量不足！");
//                    }catch (Exception e){
                        insertSapToWmsReport(stockInFinOrder,sum);
//                        throw e;
//                    }
                }
                inventoryDetails1 = inventoryDetails1.stream().sorted(Comparator.comparing(InventoryDetails::getStockInLot)
                                .thenComparing(InventoryDetails::getCreateTime)).collect(Collectors.toList());
                for (InventoryDetails details : inventoryDetails1) {
                    if (details.getAvailableQty().compareTo(BigDecimal.ZERO) > 0) {
                        InventoryHistory inventoryHistory = new InventoryHistory();
                        inventoryHistory.setInventoryId(details.getId());
                        inventoryHistory.setMblnr(mblnr);
                        inventoryHistory.setOperationType("1");
                        inventoryHistory.setCreateBy("SAP");
                        inventoryHistory.setCreateTime(DateUtils.getNowDate());
                        inventoryHistory.setTaskNo(taskInfo.getTaskNo());
                        inventoryHistory.setTenantId(6l);
                        inventoryHistory.setBatchNo(details.getStockInLot());
                        inventoryHistory.setInventoryLocal(details.getLocationCode());
                        inventoryHistory.setPostionNo(details.getPositionNo());
                        inventoryHistory.setSmblp(busno.substring(busno.length() - 4));
                        inventoryHistory.setOldMaterialNo(details.getOldMaterialNo());
                        inventoryHistory.setMaterialNo(details.getMaterialNo());
                        inventoryHistory.setMaterialName(details.getMaterialName());
                        //details.setIsFreeze("1");
                        BigDecimal availableQty = details.getAvailableQty() == null ? BigDecimal.ZERO : details.getAvailableQty();
                        mengeDecimal = mengeDecimal.subtract(availableQty);
                        if (mengeDecimal.compareTo(BigDecimal.ZERO) > 0) {
                            inventoryHistory.setQty(availableQty);
                            iInStoreService.insertInventoryHistory(inventoryHistory);
                            details.setInventoryQty(details.getInventoryQty().subtract(availableQty));
                            iInStoreService.subtractInventoryDetailsAvailable(details);
                        } else {
                            inventoryHistory.setQty(mengeDecimal.add(availableQty));
                            iInStoreService.insertInventoryHistory(inventoryHistory);
                            details.setInventoryQty(details.getInventoryQty().subtract(mengeDecimal.add(availableQty)));
                            details.setAvailableQty(mengeDecimal.add(availableQty));
                            iInStoreService.subtractInventoryDetailsAvailable(details);
                            break;
                        }

                    }
                }

            }
            if ("262".equals(bwart)) {
                //查询库存台账扣减增加历史表，加上对应的库存台账
                InventoryHistory inventoryHistory = new InventoryHistory();
                inventoryHistory.setMblnr(smbln);
                inventoryHistory.setOperationType("1");
                inventoryHistory.setSmblp(smblp);
                AjaxResult ajaxResult = iInStoreService.selectInventoryHistoryList(inventoryHistory);
                if (ajaxResult.isError()) {
//                    try{
//                        throw new ServiceException("查询物料号"+matnr+"库存历史台账变更失败！");
//                    }catch (Exception e){
                        insertSapToWmsReport(stockInFinOrder,BigDecimal.ZERO);
//                        throw e;
//                    }
                }
                List<InventoryHistory> inventoryHistories = JSON.parseArray(JSON.toJSONString(ajaxResult.get("data")), InventoryHistory.class);
                InventoryDetails inventoryDetails = new InventoryDetails();
                for(InventoryHistory inventory:inventoryHistories){
                    inventoryDetails.setId(inventory.getInventoryId());
                    ajaxResult = iInStoreService.selectWmsInventoryDetails2(inventoryDetails);
                    if (ajaxResult.isError()) {
//                        try {
//                            throw new ServiceException("查询物料号"+matnr+"库存失败！");
//                        }catch (Exception e){
                            insertSapToWmsReport(stockInFinOrder,BigDecimal.ZERO);
//                            throw e;
//                        }
                    }
                    List<InventoryDetails> inventoryDetails1 = JSON.parseArray(JSON.toJSONString(ajaxResult.get("data")), InventoryDetails.class);
                    if (inventoryDetails1 != null && inventoryDetails1.size() > 0) {
                        // 总数量
                        BigDecimal mengeDecimal = inventory.getQty();
                        InventoryDetails inventoryDetails2 = inventoryDetails1.get(0);
                        inventoryHistory = new InventoryHistory();
                        inventoryHistory.setInventoryId(inventoryDetails2.getId());
                        inventoryHistory.setMblnr(mblnr);
                        inventoryHistory.setSmblp(busno.substring(busno.length() - 4));
                        inventoryHistory.setOperationType("2");
                        inventoryHistory.setCreateBy("SAP");
                        inventoryHistory.setCreateTime(DateUtils.getNowDate());
                        inventoryHistory.setQty(inventory.getQty());
                        inventoryHistory.setTaskNo(taskInfo.getTaskNo());
                        inventoryHistory.setTenantId(6l);
                        inventoryHistory.setBatchNo(inventoryDetails2.getStockInLot());
                        inventoryHistory.setInventoryLocal(inventoryDetails2.getLocationCode());
                        inventoryHistory.setPostionNo(inventoryDetails2.getPositionNo());
                        inventoryHistory.setOldMaterialNo(inventoryDetails2.getOldMaterialNo());
                        inventoryHistory.setMaterialNo(inventoryDetails2.getMaterialNo());
                        inventoryHistory.setMaterialName(inventoryDetails2.getMaterialName());
                        iInStoreService.insertInventoryHistory(inventoryHistory);
                        inventoryDetails2.setAvailableQty(inventoryDetails2.getAvailableQty() == null ? BigDecimal.ZERO : inventoryDetails2.getAvailableQty().add(mengeDecimal));
                        inventoryDetails2.setInventoryQty(inventoryDetails2.getInventoryQty() == null ? BigDecimal.ZERO : inventoryDetails2.getInventoryQty().add(mengeDecimal));
                        iInStoreService.updateWmsInventoryDetails(inventoryDetails2);
                    }
                }
                SapMaterialMoveReport sapMaterialMoveReport = new SapMaterialMoveReport();
                sapMaterialMoveReport.setReportType("1");
                sapMaterialMoveReport.setMaterialCertificateNo(smbln);
                sapMaterialMoveReport.setLineNo(smblp);
                ajaxResult = reportService.selectReport(sapMaterialMoveReport);
                List<SapMaterialMoveReport> sapMaterialMoveReports = JSON.parseArray(ajaxResult.get("data").toString(), SapMaterialMoveReport.class);
                for(SapMaterialMoveReport report:sapMaterialMoveReports){
                    report.setUpdateBy("SAP");
                    report.setUpdateTime(DateUtils.getNowDate());
                    report.setTaskStatus("2");
                    reportService.update(report);
                }
            }
//            if ("261".equals(bwart) || "262".equals(bwart) || "102".equals(bwart) || "132".equals(bwart)) {
//                // 在SAP推送报表中体现扣账数据
//                // todo 走不通
//                //reportService.sapReportForWork(stockInFinOrder);
//            }
        }
        if(needPrint){
            printInspectTask(printTOParamsDto);
        }
        return 1;
    }

    private void reserveStorage(InventoryDetails inventoryDetail, TaskInfo taskInfo) {
        // 新增预定库存
        ReserveStorage reserveStorage = new ReserveStorage();
        reserveStorage.setTaskId(taskInfo.getId());
        reserveStorage.setBillType(TaskTypeConstant.FINISHED_PRODUCT_LISTING_TASK);
        reserveStorage.setTaskNo(taskInfo.getTaskNo());
        reserveStorage.setInventoryId(inventoryDetail.getId());
        reserveStorage.setReserveQty(inventoryDetail.getAvailableQty());
        reserveStorage.setUnit(inventoryDetail.getUnit());
        reserveStorage.setMaterialNo(inventoryDetail.getMaterialNo());
        reserveStorage.setMaterialName(inventoryDetail.getMaterialName());
        reserveStorage.setOldMaterialNo(inventoryDetail.getOldMaterialNo());
        reserveStorage.setLocationId(inventoryDetail.getLocationId());
        reserveStorage.setLocationCode(inventoryDetail.getLocationCode());
        reserveStorage.setAreaId(inventoryDetail.getAreaId());
        reserveStorage.setAreaCode(inventoryDetail.getAreaCode());
        reserveStorage.setWarehouseCode(inventoryDetail.getWarehouseCode());
        reserveStorage.setWarehouseId(inventoryDetail.getId());
        reserveStorage.setFactoryId(inventoryDetail.getFactoryId());
        reserveStorage.setFactoryCode(inventoryDetail.getFactoryCode());
        reserveStorage.setLot(inventoryDetail.getStockInLot());
        reserveStorage.setContainer(inventoryDetail.getContainerNo());
        reserveStorage.setCreateBy(SecurityUtils.getUsername());
        reserveStorage.setCreateTime(DateUtils.getNowDate());
        reserveStorage.setTenantId(6L);
        iInStoreService.createReserveStorage(reserveStorage);
    }

    /**
     * 更新台账 成品/半成品
     *
     * @param storagePositionVo
     * @param stockInFinOrder
     * @return inventoryId 台账id
     */
    public InventoryDetails updateInventoryDetailsProduct(StoragePositionVo storagePositionVo,StockInFinOrder stockInFinOrder,TaskInfo taskInfo) {
        //新增台账
        InventoryDetails inventoryDetailsDto = new InventoryDetails();
        inventoryDetailsDto.setMaterialNo(stockInFinOrder.getMatnr());
        inventoryDetailsDto.setMaterialName(stockInFinOrder.getMaktx());
        if(stockInFinOrder.getLgort().equals("2050")||stockInFinOrder.getLgort().equals("2060")||stockInFinOrder.getLgort().equals("2000")){
            inventoryDetailsDto.setPositionNo(taskInfo.getSourcePositionNo());
            inventoryDetailsDto.setLocationCode(stockInFinOrder.getLgort());
        }else{
            inventoryDetailsDto.setPositionNo(storagePositionVo.getPositionNo());
            inventoryDetailsDto.setLocationCode(storagePositionVo.getLocationCode());
        }
        inventoryDetailsDto.setOldMaterialNo(taskInfo.getOldMaterialNo());
        inventoryDetailsDto.setPositionId(storagePositionVo.getId());
        inventoryDetailsDto.setAreaId(storagePositionVo.getAreaId());
        inventoryDetailsDto.setAreaCode(storagePositionVo.getAreaCode());
        inventoryDetailsDto.setLocationId(storagePositionVo.getLocationId());
        inventoryDetailsDto.setFactoryId(storagePositionVo.getFactoryId());
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
        inventoryDetailsDto.setIsReserved(CommonYesOrNo.NO);
        inventoryDetailsDto.setWarehouseId(storagePositionVo.getWarehouseId());
        inventoryDetailsDto.setWarehouseCode(storagePositionVo.getWarehouseCode());
        inventoryDetailsDto.setHuNo(stockInFinOrder.getHuNo());
        inventoryDetailsDto.setWbsElement(stockInFinOrder.getWbsElem());
        inventoryDetailsDto.setOldMesContainerNo(stockInFinOrder.getExidv2());
        inventoryDetailsDto.setTenantId(6l);
        inventoryDetailsDto.setCreateBy("SAP");
        inventoryDetailsDto.setCreateTime(DateUtils.getNowDate());
        inventoryDetailsDto.setStockSpecType(stockInFinOrder.getSobkz());
        inventoryDetailsDto.setStockSpecFlag(StringUtils.isNotBlank(stockInFinOrder.getSobkz())?CommonYesOrNo.YES:CommonYesOrNo.NO);
        AjaxResult add = iInStoreService.insertWmsInventoryDetails(inventoryDetailsDto);
        if (add.isError()) {
            throw new ServiceException(add.get("msg").toString());
        }
        inventoryDetailsDto.setId(Long.parseLong(add.get("data").toString()));
        return inventoryDetailsDto;
    }

    /**
     * 更新特殊台账 成品/半成品
     *
     * @param storagePositionVo
     * @param stockInFinOrder
     * @return inventoryId 台账id
     */
    private Long updatespecialInventoryDetailsProduct(StoragePositionVo storagePositionVo,StockInFinOrder stockInFinOrder) {
        //新增台账
        SpecialInventoryDetails specialInventoryDetails = new SpecialInventoryDetails();
        specialInventoryDetails.setMaterialNo(stockInFinOrder.getMatnr());
        specialInventoryDetails.setMaterialName(stockInFinOrder.getMaktx());
        specialInventoryDetails.setPositionNo(storagePositionVo.getPositionNo());
        specialInventoryDetails.setPositionId(storagePositionVo.getId());
        specialInventoryDetails.setAreaId(storagePositionVo.getAreaId());
        specialInventoryDetails.setAreaCode(storagePositionVo.getAreaCode());
        specialInventoryDetails.setLocationId(storagePositionVo.getLocationId());
        specialInventoryDetails.setLocationCode(storagePositionVo.getLocationCode());
        specialInventoryDetails.setFactoryId(storagePositionVo.getFactoryId());
        specialInventoryDetails.setFactoryCode(storagePositionVo.getFactoryCode());
        specialInventoryDetails.setFactoryName(storagePositionVo.getFactoryName());
        specialInventoryDetails.setInventoryQty(new BigDecimal(stockInFinOrder.getMenge()));
        specialInventoryDetails.setAvailableQty(new BigDecimal(stockInFinOrder.getMenge()));
        specialInventoryDetails.setUnit(stockInFinOrder.getMeins());
        specialInventoryDetails.setPreparedQty(BigDecimal.ZERO);
        specialInventoryDetails.setOperationPreparedQty(BigDecimal.ZERO);
        specialInventoryDetails.setStockInDate(DateUtils.getNowDate());
        specialInventoryDetails.setStockInLot(stockInFinOrder.getCharg());
        specialInventoryDetails.setContainerNo(stockInFinOrder.getExidv2());
        specialInventoryDetails.setIsFreeze(CommonYesOrNo.NO);
        specialInventoryDetails.setIsConsign(CommonYesOrNo.NO);
        specialInventoryDetails.setIsQc(CommonYesOrNo.NO);
        specialInventoryDetails.setIsReserved(CommonYesOrNo.NO);
        specialInventoryDetails.setWarehouseId(storagePositionVo.getWarehouseId());
        specialInventoryDetails.setWarehouseCode(storagePositionVo.getWarehouseCode());
        specialInventoryDetails.setHuNo(stockInFinOrder.getHuNo());
        specialInventoryDetails.setWbsElement(stockInFinOrder.getWbsElem());
        specialInventoryDetails.setOldMesContainerNo(stockInFinOrder.getExidv2());
        specialInventoryDetails.setTenantId(6l);
        specialInventoryDetails.setCreateBy("SAP");
        specialInventoryDetails.setCreateTime(DateUtils.getNowDate());
        AjaxResult add = iInStoreService.insertSpecialInventoryHistory(specialInventoryDetails);
        if (add.isError()) {
            throw new ServiceException(add.get("msg").toString());
        }
        return Long.parseLong(add.get("data").toString());
    }

    //102和132减非特殊库存
    public void handleSpecialInventory(StockInFinOrder stockInFinOrder) {
        SpecialInventoryDetails specialInventoryDetails = new SpecialInventoryDetails();
        specialInventoryDetails.setIsFreeze("0");
        specialInventoryDetails.setIsConsign("0");
        specialInventoryDetails.setIsReserved("0");
        specialInventoryDetails.setMaterialNo(stockInFinOrder.getMatnr());
        specialInventoryDetails.setStockInLot(stockInFinOrder.getCharg());
        AjaxResult ajaxResult = iInStoreService.selectWmsSpecialInventory(specialInventoryDetails);
        if (ajaxResult.isError()) {
            throw new ServiceException("查询库存失败！");
        }
        List<SpecialInventoryDetails> specialInventoryDetails1 = JSON.parseArray(JSON.toJSONString(ajaxResult.get("data")), SpecialInventoryDetails.class);

        if (specialInventoryDetails1 != null && specialInventoryDetails1.size() > 0) {
            BigDecimal mengeDecimal = new BigDecimal(stockInFinOrder.getMenge());
            BigDecimal sum = BigDecimal.ZERO;
            for(SpecialInventoryDetails details : specialInventoryDetails1){
                sum = sum.add(details.getAvailableQty());
            }
            if(mengeDecimal.compareTo(sum)>0){
                throw new ServiceException("物料号"+stockInFinOrder.getMatnr()+"库存台账可用数量不足！");
            }
            // 台账减库存，可用数量
            for (SpecialInventoryDetails details : specialInventoryDetails1) {
                SpecialInventoryDetails specialInventoryDetails2 = new SpecialInventoryDetails();
                specialInventoryDetails2.setId(details.getId());
                BigDecimal availableQty = details.getAvailableQty() == null ? BigDecimal.ZERO : details.getAvailableQty();
                if(availableQty.compareTo(BigDecimal.ZERO)==0){
                    continue;
                }
                mengeDecimal = mengeDecimal.subtract(availableQty);
                if (mengeDecimal.compareTo(BigDecimal.ZERO) > 0) {
                    specialInventoryDetails2.setAvailableQty(new BigDecimal("0"));
                    iInStoreService.updateWmsSpecialInventory(specialInventoryDetails2);
                    InventoryHistory inventoryHistory = new InventoryHistory();
                    inventoryHistory.setSpecialInventoryId(details.getId());
                    inventoryHistory.setMblnr(stockInFinOrder.getMblnr());
                    inventoryHistory.setOperationType("1");
                    inventoryHistory.setCreateBy("SAP");
                    inventoryHistory.setCreateTime(DateUtils.getNowDate());
                    inventoryHistory.setQty(availableQty);
                    inventoryHistory.setTenantId(6l);
                    inventoryHistory.setInventoryLocal(details.getLocationCode());
                    inventoryHistory.setBatchNo(details.getStockInLot());
                    inventoryHistory.setPostionNo(details.getPositionNo());
                    inventoryHistory.setOldMaterialNo(details.getOldMaterialNo());
                    inventoryHistory.setMaterialNo(details.getMaterialNo());
                    inventoryHistory.setMaterialName(details.getMaterialName());
                    iInStoreService.insertInventoryHistory(inventoryHistory);
                } else {
                    specialInventoryDetails2.setAvailableQty(mengeDecimal.abs());
                    iInStoreService.updateWmsSpecialInventory(specialInventoryDetails2);
                    InventoryHistory inventoryHistory = new InventoryHistory();
                    inventoryHistory.setSpecialInventoryId(details.getId());
                    inventoryHistory.setMblnr(stockInFinOrder.getMblnr());
                    inventoryHistory.setOperationType("1");
                    inventoryHistory.setCreateBy("SAP");
                    inventoryHistory.setCreateTime(DateUtils.getNowDate());
                    inventoryHistory.setQty(availableQty.add(mengeDecimal));
                    inventoryHistory.setTenantId(6l);
                    inventoryHistory.setInventoryLocal(details.getLocationCode());
                    inventoryHistory.setBatchNo(details.getStockInLot());
                    inventoryHistory.setPostionNo(details.getPositionNo());
                    inventoryHistory.setOldMaterialNo(details.getOldMaterialNo());
                    inventoryHistory.setMaterialNo(details.getMaterialNo());
                    inventoryHistory.setMaterialName(details.getMaterialName());
                    iInStoreService.insertInventoryHistory(inventoryHistory);
                    break;
                }

            }
            mengeDecimal = new BigDecimal(stockInFinOrder.getMenge());
            // 台账减库存，库存数量
            for (SpecialInventoryDetails details : specialInventoryDetails1) {
                SpecialInventoryDetails specialInventoryDetails2 = new SpecialInventoryDetails();
                specialInventoryDetails2.setId(details.getId());
                BigDecimal inventoryQty = details.getInventoryQty() == null ? BigDecimal.ZERO : details.getInventoryQty();
                if(inventoryQty.compareTo(BigDecimal.ZERO)==0){
                    continue;
                }
                mengeDecimal = mengeDecimal.subtract(inventoryQty);
                if (mengeDecimal.compareTo(BigDecimal.ZERO) > 0) {
                    specialInventoryDetails2.setInventoryQty(new BigDecimal("0"));
                    iInStoreService.updateWmsSpecialInventory(specialInventoryDetails2);
                } else {
                    specialInventoryDetails2.setInventoryQty(mengeDecimal.abs());
                    iInStoreService.updateWmsSpecialInventory(specialInventoryDetails2);
                    break;
                }
            }
        }
    }

    //102和132减非特殊库存
    public void handleInventory(StockInFinOrder stockInFinOrder) {
        InventoryDetailsVo inventoryDetails = new InventoryDetailsVo();
        inventoryDetails.setIsFreeze(CommonYesOrNo.NO);
        inventoryDetails.setIsConsign(CommonYesOrNo.NO);
        inventoryDetails.setIsQc(CommonYesOrNo.NO);
        inventoryDetails.setStockSpecFlag(CommonYesOrNo.NO);
        inventoryDetails.setMaterialNo(stockInFinOrder.getMatnr());
        inventoryDetails.setStockInLot(stockInFinOrder.getCharg());
        AjaxResult ajaxResult = iInStoreService.listAllVInventory(inventoryDetails);
        if (ajaxResult.isError()) {
            throw new ServiceException("查询物料号"+stockInFinOrder.getMatnr()+"库存失败！");
        }
        List<InventoryDetails> inventoryDetails1 = JSON.parseArray(ajaxResult.get("data").toString(), InventoryDetails.class);
        if(CollectionUtils.isEmpty(inventoryDetails1)){
//            try{
//                throw new ServiceException("物料号"+stockInFinOrder.getMatnr()+"库存台账可用数量不足！");
//            }catch (Exception e){
                insertSapToWmsReport(stockInFinOrder,BigDecimal.ZERO);
//                throw e;
//            }
        }
        if (inventoryDetails1 != null && inventoryDetails1.size() > 0) {
            BigDecimal mengeDecimal = new BigDecimal(stockInFinOrder.getMenge());
            BigDecimal sum = BigDecimal.ZERO;
            for(InventoryDetails details : inventoryDetails1){
                sum = sum.add(details.getAvailableQty());
            }
            if(mengeDecimal.compareTo(sum)>0){
//                try{
//                    throw new ServiceException("物料号"+stockInFinOrder.getMatnr()+"库存台账可用数量不足！");
//                }catch (Exception e){
                    insertSapToWmsReport(stockInFinOrder,sum);
//                    throw e;
//                }

            }
            // 台账减库存，可用数量
            for (InventoryDetails details : inventoryDetails1) {
                InventoryDetails inventoryDetails2 = new InventoryDetails();
                inventoryDetails2.setId(details.getId());
                inventoryDetails2.setUpdateBy("SAP");
                inventoryDetails2.setUpdateTime(DateUtils.getNowDate());
                BigDecimal availableQty = details.getAvailableQty() == null ? BigDecimal.ZERO : details.getAvailableQty();
                if(availableQty.compareTo(BigDecimal.ZERO)==0){
                    continue;
                }
                mengeDecimal = mengeDecimal.subtract(availableQty);
                if (mengeDecimal.compareTo(BigDecimal.ZERO) > 0) {
                    InventoryHistory inventoryHistory = new InventoryHistory();
                    inventoryHistory.setInventoryId(details.getId());
                    inventoryHistory.setMblnr(stockInFinOrder.getMblnr());
                    inventoryHistory.setSmblp(stockInFinOrder.getBusno().substring(stockInFinOrder.getBusno().length() - 4));
                    inventoryHistory.setOperationType("1");
                    inventoryHistory.setCreateBy("SAP");
                    inventoryHistory.setCreateTime(DateUtils.getNowDate());
                    inventoryHistory.setQty(availableQty);
                    inventoryHistory.setTenantId(6l);
                    inventoryHistory.setInventoryLocal(details.getLocationCode());
                    inventoryHistory.setBatchNo(details.getStockInLot());
                    inventoryHistory.setPostionNo(details.getPositionNo());
                    inventoryHistory.setOldMaterialNo(details.getOldMaterialNo());
                    inventoryHistory.setMaterialNo(details.getMaterialNo());
                    inventoryHistory.setMaterialName(details.getMaterialName());
                    iInStoreService.insertInventoryHistory(inventoryHistory);
//                    inventoryDetails2.setInventoryQty(details.getInventoryQty().subtract(mengeDecimal.add(availableQty)));
                    inventoryDetails2.setInventoryQty(details.getInventoryQty().subtract(details.getAvailableQty()));
                    inventoryDetails2.setAvailableQty(details.getAvailableQty());
                    iInStoreService.subtractInventoryDetailsAvailable(inventoryDetails2);
                } else {
                    InventoryHistory inventoryHistory = new InventoryHistory();
                    inventoryHistory.setInventoryId(details.getId());
                    inventoryHistory.setMblnr(stockInFinOrder.getMblnr());
                    inventoryHistory.setSmblp(stockInFinOrder.getBusno().substring(stockInFinOrder.getBusno().length() - 4));
                    inventoryHistory.setOperationType("1");
                    inventoryHistory.setCreateBy("SAP");
                    inventoryHistory.setCreateTime(DateUtils.getNowDate());
                    inventoryHistory.setQty(availableQty.add(mengeDecimal));
                    inventoryHistory.setTenantId(6l);
                    inventoryHistory.setInventoryLocal(details.getLocationCode());
                    inventoryHistory.setBatchNo(details.getStockInLot());
                    inventoryHistory.setPostionNo(details.getPositionNo());
                    inventoryHistory.setOldMaterialNo(details.getOldMaterialNo());
                    inventoryHistory.setMaterialNo(details.getMaterialNo());
                    inventoryHistory.setMaterialName(details.getMaterialName());
                    iInStoreService.insertInventoryHistory(inventoryHistory);
                    inventoryDetails2.setInventoryQty(details.getInventoryQty().subtract(mengeDecimal.add(availableQty)));
                    inventoryDetails2.setAvailableQty(mengeDecimal.add(availableQty));
                    iInStoreService.subtractInventoryDetailsAvailable(inventoryDetails2);
                    break;
                }
            }
        }
    }

    private void insertSapToWmsReport(StockInFinOrder stockInFinOrder,BigDecimal completeQty){
        SapMaterialMoveReport sapMaterialMoveReport = new SapMaterialMoveReport();
        sapMaterialMoveReport.setReportType("1");
        sapMaterialMoveReport.setOrderType(OrderConstant.IN_FIN);
        sapMaterialMoveReport.setSapMenge(new BigDecimal(stockInFinOrder.getMenge()));
        sapMaterialMoveReport.setMaterialCertificateNo(stockInFinOrder.getMblnr());
        sapMaterialMoveReport.setLineNo(stockInFinOrder.getBusno().substring(stockInFinOrder.getBusno().length() - 4));
        sapMaterialMoveReport.setCompleteQty(completeQty);
        sapMaterialMoveReport.setDifference(new BigDecimal(stockInFinOrder.getMenge()).subtract(completeQty).abs());
        sapMaterialMoveReport.setMaterialNo(stockInFinOrder.getMaterialNo());
        sapMaterialMoveReport.setStorageLocationCode(stockInFinOrder.getLgort());
        sapMaterialMoveReport.setTenantId(6l);
        sapMaterialMoveReport.setCreateBy("SAP");
        sapMaterialMoveReport.setCreateTime(DateUtils.getNowDate());
        sapMaterialMoveReport.setMaterialName(stockInFinOrder.getMaterialName());
        sapMaterialMoveReport.setOldMaterialNo(stockInFinOrder.getOldMaterialNo());
        sapMaterialMoveReport.setLot(stockInFinOrder.getCharg());
        reportService.insertSapMaterialMoveReport(sapMaterialMoveReport);
    }

    /**
     * 入库任务关闭
     *
     * @param ids
     * @return
     */
    @Override
    @GlobalTransactional
    public int closeStockInTask(Long[] ids) {
        for (Long id : ids) {
            TaskInfo taskInfo = taskInfoMapper.selectTaskInfoById(id);
            String taskStatus = taskInfo.getTaskStatus();
            String taskType = taskInfo.getTaskType();
            if (TaskStatusConstant.TASK_STATUS_CLOSE.equals(taskInfo.getTaskStatus())) {
                throw new ServiceException("任务状态异常！");
            }
            taskInfo.setTaskStatus(TaskStatusConstant.TASK_STATUS_CLOSE);
            taskInfo.setUpdateBy(SecurityUtils.getUsername());
            taskInfo.setUpdateTime(DateUtils.getNowDate());
            // 修改任务状态
            taskInfoMapper.updateTaskInfo(taskInfo);
            // 查询未关闭的任务
            TaskInfo info = new TaskInfo();
            info.setStockInOrderNo(taskInfo.getStockInOrderNo());
            info.setTaskType(taskInfo.getTaskType());
            List<TaskInfo> taskInfoList = taskInfoMapper.openTask(info);
            if (StockInTaskConstant.FIN_ORDER_TASK_TYPE_FINISHED.equals(taskType)) {
                // 成品
                // 同步关闭成品收货明细
                StockInFinOrderDetail orderDetail = new StockInFinOrderDetail();
                orderDetail.setId(taskInfo.getDetailId());
                orderDetail.setDetailStatus(OrderStatusConstant.ORDER_STATUS_CLOSE);
                orderDetail.setUpdateBy(SecurityUtils.getUsername());
                orderDetail.setUpdateTime(DateUtils.getNowDate());
                stockInFinOrderDetailMapper.updateStockInFinOrderDetail(orderDetail);

                // 如果该任务的单据全部关闭 更改单据状态为关闭
//                if (taskInfoList == null || taskInfoList.size() == 0) {
//                    StockInFinOrder stockInFinOrder = new StockInFinOrder() {{
//                        setOrderNo(taskInfo.getStockInOrderNo());
//                        setOrderStatus(OrderStatusConstant.ORDER_STATUS_CLOSE);
//                        setUpdateBy(SecurityUtils.getUsername());
//                        setUpdateTime(DateUtils.getNowDate());
//                    }};
//                    // 更改单据状态为关闭
//                    stockInFinOrderMapper.closeStockInFinOrder(stockInFinOrder);
//                }
            } else if (StockInTaskConstant.FIN_ORDER_TASK_TYPE_SEMI_FINISHED.equals(taskType)) {
                //半成品
                StockInSemiFinOrderDetail finOrderDetail = new StockInSemiFinOrderDetail();
                finOrderDetail.setId(taskInfo.getDetailId());
                finOrderDetail.setStatus(OrderStatusConstant.ORDER_STATUS_CLOSE);
                finOrderDetail.setUpdateBy(SecurityUtils.getUsername());
                finOrderDetail.setUpdateTime(DateUtils.getNowDate());
                stockInSemiFinOrderDetailMapper.updateStockInSemiFinOrderDetail(finOrderDetail);

                // 如果该任务的单据全部关闭 更改单据状态为关闭
                if (taskInfoList == null || taskInfoList.size() == 0) {
                    StockInSemiFinOrder stockInSemiFinOrder = new StockInSemiFinOrder();
                    stockInSemiFinOrder.setOrderNo(taskInfo.getStockInOrderNo());
                    stockInSemiFinOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_CLOSE);
                    stockInSemiFinOrder.setUpdateBy(SecurityUtils.getUsername());
                    stockInSemiFinOrder.setUpdateTime(DateUtils.getNowDate());
                    // 更改单据状态为关闭
                    stockInSemiFinOrderMapper.updateStockInSemiFinOrder(stockInSemiFinOrder);
                }
            } else if (StockInTaskConstant.STD_ORDER_TASK_TYPE_ORDER.equals(taskType) ||
                    StockInTaskConstant.STD_ORDER_TASK_TYPE_AGREEMENT.equals(taskType) ||
                    StockInTaskConstant.STD_ORDER_TASK_TYPE_SUBCONTRACTING.equals(taskType) ||
                    StockInTaskConstant.STD_ORDER_TASK_TYPE_DUMP.equals(taskType)) {
                //原材料
                closeByTaskId(taskInfo, ObjectUtils.isEmpty(taskInfoList));
            }
        }
        return 1;
    }

    /**
     * 获取任务执行记录
     */
    public List<TaskLog> getTaskLogList(String taskNo) {
        TaskLog taskLog = new TaskLog();
        taskLog.setTaskNo(taskNo);
        AjaxResult result = taskService.getTaskLog(taskLog);
        if ("".equals(Objects.toString(result.get("data"), "")) || result.isError()) {
            throw new ServiceException("获取任务执行记录信息失败！");
        }
        String str = JSON.toJSONString(result.get("data"));
        List<TaskLog> list = JSON.parseArray(str, TaskLog.class);
        if (list == null || list.size() == 0) {
            throw new ServiceException("任务执行记录信息为空！");
        }
        return list;
    }

    /**
     * 根据送货单id 关闭标准入库单
     *
     * @param ids 送货单id集合
     * @return 结果
     */
    @Override
    public int closeStockInByDeliveryOrderIds(Long[] ids) {
        List<StockInStdOrder> stockInStdOrders = stockInStdOrderMapper.selectStockInStdOrderListByDeliveryOrderIds(ids);
        if (ObjectUtils.isEmpty(stockInStdOrders)) {

        }
        for (StockInStdOrder stockInStdOrder : stockInStdOrders) {
            closeStockInStdOrder(stockInStdOrder);
        }
        return 1;
    }

    /**
     * 查询入标准入口单明细列表(联查主表)
     *
     * @param stockInStdOrderDetail 标准入库单明细
     * @return 标准入库单明细vo集合
     */
    @Override
    public List<StockInStdOrderDetailVo> queryStdOrderAndDetailList(StockInStdOrderDetail stockInStdOrderDetail) {
        return stockInStdOrderDetailMapper.queryStdOrderAndDetailList(stockInStdOrderDetail);
    }

    /**
     * 修改标准入库单明细
     *
     * @param stockInStdOrderDetail 标准入库单明细
     * @return 结果
     */
    @Override
    public int updateStockInStdOrderDetail(StockInStdOrderDetail stockInStdOrderDetail) {
        return stockInStdOrderDetailMapper.updateStockInStdOrderDetail(stockInStdOrderDetail);
    }

    /**
     * 原材料退货新增同步更新标准入库单
     * * @param stockInStdOrderDto 标准入库单dto
     *
     * @return 结果
     */
    @Override
    public int editDetail(StockInStdOrderDto stockInStdOrderDto) {
        if (ObjectUtils.isEmpty(stockInStdOrderDto)) {
            throw new ServiceException("参数不存在!");
        }
        List<StockInStdOrderDetailDto> list = stockInStdOrderDto.getStockInStdOrderDetailDtoList();
        String type = stockInStdOrderDto.getType();
        if ("add".equals(type)) {
            for (StockInStdOrderDetail stockInStdOrderDetail : list) {
                stockInStdOrderDetail.setUpdateBy(SecurityUtils.getUsername());
                stockInStdOrderDetail.setUpdateTime(DateUtils.getNowDate());
                if (stockInStdOrderDetailMapper.updateStockInStdOrderDetail(stockInStdOrderDetail) <= 0) {
                    throw new ServiceException("更新标准入库单明细失败");
                }
            }
        } else if ("close".equals(type)) {
            //查询明细
            Long[] longs = list.stream().map(StockInStdOrderDetail::getId).toArray(Long[]::new);
            StockInStdOrderDetailDto detailDto = new StockInStdOrderDetailDto();
            detailDto.setIds(longs);
            List<StockInStdOrderDetail> details = stockInStdOrderDetailMapper.selectStockInStdOrderDetailList(detailDto);
            //需要回填明细收货数量 的数据
            Map<Long, StockInStdOrderDetail> map = stockInStdOrderDto.getStockInStdOrderDetailDtoList().stream()
                    .collect(Collectors.toMap(StockInStdOrderDetail::getId, s -> s));
            for (StockInStdOrderDetail detail : details) {
                if (!map.containsKey(detail.getId())) {
                    throw new ServiceException("标准入库单明细不存在!");
                }
                //此处receivedQty为原材料退货明细的 退货数量 - 完成数量
                BigDecimal receivedQty = map.get(detail.getId()).getReceivedQty();
                detail.setReceivedQty(detail.getReceivedQty().add(receivedQty));
                detail.setUpdateBy(SecurityUtils.getUsername());
                detail.setUpdateTime(DateUtils.getNowDate());
                if (stockInStdOrderDetailMapper.updateStockInStdOrderDetail(detail) <= 0) {
                    throw new ServiceException("更新标准入库单明细失败");
                }
            }
        } else {
            throw new ServiceException("更新标准入库明细失败!");
        }
        return 1;
    }

    @Override
    public int activeStockInStdByIds(Long[] ids) {


        return 0;
    }

    /**
     * 根据物料list、单据状态list查询入库单明细数据，将数据以物料代码分组后返回
     *
     * @param stock 库内Dto
     * @return 根据物料代码分组后的数据集合
     * @Date 2024/5/13
     * @Author fsc
     */

    @Override
    public List<StockInOrderCommonDto> queryOnWayNumGroupByMaterialNo(StockInOrderCommonDto stock) {
        return stockInStdOrderDetailMapper.queryOnWayNumGroupByMaterialNo(stock);
    }

    @Override
    public void timing() {

        // 获取当前的日期时间
        LocalDateTime now = LocalDateTime.now();

        // 从数据库获取库存标准订单列表
        List<StockInStdOrder> stockInStdOrderDtoList = stockInStdOrderMapper.timing();
        List<StockInStdOrder> stockInStdOrders = new ArrayList<>(); // 初始化列表
//        Map<String, StockInStdOrder> stringStockInStdOrderMap = new HashMap<>();

        List<StockInOrederVo> stockInOrederVos = new ArrayList<>();

        stockInStdOrderDtoList.stream().filter((item) -> {
            // 将 Date 转换为 LocalDateTime
            LocalDateTime expireDateTime = item.getExpireTime().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            // 计算当前时间与过期时间之间的天数差
            long daysUntilExpire = Duration.between(now, expireDateTime).toDays();

            // 判断过期时间是否在当前时间的五天内，且大于0
            if (daysUntilExpire <= 5 && daysUntilExpire >= 0) {
                // 输出符合条件的过期时间
                System.out.println(expireDateTime);

                return true; // 返回true以继续流处理
            } else if (daysUntilExpire < 0) {
                // 已过期数据
                System.out.println("过期数据:" + item);
                return true;
            }
            return false; // 否则不包括在结果中
        }).forEach(item -> {
            stockInStdOrders.add(item); // 添加到列表中
            if (!stockInStdOrders.isEmpty()) {

                List<StockInOrderDto> stockInOrederDtos = new ArrayList<>();

                stockInStdOrders.forEach(sitem -> {

                    StockInOrderDto stockInOrederDto = new StockInOrderDto();

//                    StockInOrederVo user = stockInStdOrderMapper.getUserLdap(sitem.getId());
                    StockInOrederVo user = stockInStdOrderMapper.getUserLdap(218L);
                    stockInOrederVos.add(user);
//                    BeanUtils.copyProperties(stockInOrederDto,user);
//                    stockInOrederDtos.add(stockInOrederDto);
//                    FeiShuMessage message = new FeiShuMessage();
//                    String uuid = UUID.randomUUID().toString();
//                    message.setMsgType("text")
//                            .setMsgId(uuid)
//                            .setSystem("wms")
//                            .setType(MessageType.LARK);
//                    message.setContent("{ \"text\": \"你好：你的交互单将在五天内到期！\" }") // Example alternative
////                        .setReceiveId(user.getLdap());
//                            .setReceiveId("7392603099466022916");

                });
            } else {
                mainDataService.getUserldapError();
            }
        });


        stockInOrederVos.forEach(item -> {

            // 调用远程
            // 将 Date 转换为 LocalDateTime
            LocalDateTime expireDateTime = item.getExpDay().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            // 计算当前时间与过期时间之间的天数差
            long daysUntilExpire = Duration.between(now, expireDateTime).toDays();

         MessageVo messageVo = new MessageVo();


            messageVo.setTemplateId("ctp_AAu5PIyDBIRQ");
            Map<String, Object> data = new HashMap<String, Object>();
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < stockInOrederVos.size(); i++) {
                Map<String, Object> items = new HashMap<String, Object>();
                items.put("material_no", item.getMaterialNo());
                items.put("material_name", item.getMaterialName());
                items.put("lot", item.getLot());
//                items.put("po", item.getPo());
                items.put("day", daysUntilExpire);
                items.put("exp_day", item.getExpDay());
//              items.put("day", item.get);
//              items.put("exp_day", );
//                        items.put("material_no", "100000"+i);
//                        items.put("material_name", UUID.randomUUID().toString());
//                        items.put("day", i+"");
//                        items.put("exp_day", DateUtils
//                                .getTime());
                list.add(items);
            }
            data.put("item", list);
            messageVo.setVariables(data);

//            message.setReceiveId(item.getLdap());
            messageVo.setReceiveId(item.getLdap());
            // 远程调用
            messageService.sendMessage(messageVo);
        });
    }

    /**
     * 标准入库单转收货单
     *
     * @param materialNo
     * @param orderNos
     * @return
     */
    @Override
    public List<StockInStdOrderDetailDto> stdOrderToDeliveryList(String materialNo, List<String> orderNos) {
        // 根据交货单号 查询所有单据明细
        return stockInStdOrderDetailMapper.stdOrderToDeliveryList(materialNo, orderNos);
    }

    /**
     * 标准入库单转收货单
     *
     * @param orderNos
     * @return
     */
    @Override
    public List<StockInStdOrderDetailDto> stdOrderToDeliveryList(List<String> orderNos) {
        // 根据交货单号 查询所有单据明细
        List<StockInStdOrderDetailDto> purchaseDtoList = stockInStdOrderDetailMapper.stdOrderToDeliveryList(orderNos);

        for (int i = 0; i < purchaseDtoList.size(); i++) {
            StockInStdOrderDetailDto detailDto = purchaseDtoList.get(i);

            WmsMaterialBasicDto dto = new WmsMaterialBasicDto();
//            dto.setMaterialNo(detailDto.getMaterialNo());
            List<String> materialNoList = new ArrayList<>();
            materialNoList.add(detailDto.getMaterialNo());
            dto.setMaterialNoList(materialNoList);
            AjaxResult materialInfoAjaxResult = mainDataService.getMaterialArrInfo(dto);

            if (materialInfoAjaxResult.isError()) {
                throw new ServiceException(materialInfoAjaxResult.get("msg").toString());
            }

            List<WmsMaterialAttrParamsDto> list = JSONObject.parseArray(materialInfoAjaxResult.get("data").toString(), WmsMaterialAttrParamsDto.class);

            if (!ObjectUtils.isEmpty(list) && list.size() > 0) {
                WmsMaterialAttrParamsDto wmsMaterialAttrParamsDto = list.get(0);
                String defaultExtValiditydays = wmsMaterialAttrParamsDto.getDefaultValidityPeriod();

                if (ObjectUtils.isEmpty(defaultExtValiditydays)) {
                    throw new ServiceException("物料属性默认有效期不存在!");
                }
                Date expireTime = DateUtils.addDays(new Date(), Integer.valueOf(defaultExtValiditydays));

                // 过期日期
                detailDto.setExpireTime(expireTime);
            }
            Date now = new Date();
            // 凭证日期
            detailDto.setVoucherTime(now);
            // 过账日期
            detailDto.setPostTime(now);
            // 批次

            String date = DateFormatUtils.format(now, "yyyyMMdd");
            String num = StrUtil.padPre(Convert.toStr(i + 1), 3, '0');
            String lotNo = String.format("V%s%s", date, num);
            detailDto.setLotNo(lotNo);
            detailDto.setIsQc(detailDto.getIsQc());
            // 本次收货数量
            // 生产日期
            detailDto.setProductTime(now);
            // 过期日期
            detailDto.setExpireTime(now);
        }
        return purchaseDtoList;
    }


    /**
     * 过账
     *
     * @param deliveryId
     * @throws Exception
     */
    @Override
    public void doPost(Long deliveryId) throws Exception {
        DeliveryOrderDetail deliveryOrderDetail = deliveryOrderDetailMapper.selectDeliveryOrderDetailById(deliveryId);

        if (CommonYesOrNo.POSTED.equals(deliveryOrderDetail.getIsPost())) {
            throw new ServiceException("已过账，请勿重复操作！");
        }
        Long orderDetailId = deliveryOrderDetail.getPurchaseOrderDetailId();
        StockInStdOrderDetailDto stdOrderDetail = new StockInStdOrderDetailDto();
        stdOrderDetail.setId(orderDetailId);
        List<StockInStdOrderDetail> stockInStdOrderDetails = stockInStdOrderDetailMapper.selectStockInStdOrderDetailList(stdOrderDetail);

        StockInStdOrderDetail stdOrderDetailInfo = stockInStdOrderDetails.get(0);

        WmsMaterialDeliverAttr deliverAttr = new WmsMaterialDeliverAttr();
        deliverAttr.setMaterialNo(stdOrderDetailInfo.getMaterialNo());
        AjaxResult deliverAttrAjaxResult = mainDataService.getDeliverAttr(deliverAttr);

        if (deliverAttrAjaxResult.isError()) {
            throw new ServiceException(deliverAttrAjaxResult.get("msg").toString());
        }

        List<WmsMaterialDeliverAttr> deliverAttrList = JSONObject.parseArray(deliverAttrAjaxResult.get("data").toString(), WmsMaterialDeliverAttr.class);

        if(ObjectUtils.isEmpty(deliverAttrList)) {
            throw new ServiceException("物料包装信息不存在");
        }
        // 获取库存地点策略
        AjaxResult storagePositionInfoAjaxResult = mainDataService.getPositionCodeByLocationCodeAndMaterialNo(stdOrderDetailInfo.getLocationCode(), stdOrderDetailInfo.getMaterialNo());

        if (storagePositionInfoAjaxResult.isError()) {
            throw new ServiceException(String.format("物料:%s,库存地点策略获取失败", stdOrderDetailInfo.getMaterialNo()));
        }
        StoragePositionVo storagePositionVo = JSON.parseObject(JSON.toJSONString(storagePositionInfoAjaxResult.get("data")), StoragePositionVo.class);

        // 免检直接上架
        if(!ObjectUtils.isEmpty(stdOrderDetailInfo) && "0".equals(stdOrderDetailInfo.getIsQc())) {
            // 生成上架任务
            BigDecimal totalQty = deliveryOrderDetail.getDeliverQty();

            // 获取物料属性
            WmsMaterialDeliverAttr dto = new WmsMaterialDeliverAttr();
            dto.setMaterialNo(stdOrderDetailInfo.getMaterialNo());
            AjaxResult ajaxResult = mainDataService.getDeliverAttr(dto);

            if (ajaxResult.isError()) {
                throw new ServiceException(ajaxResult.get("msg").toString());
            }
            List<WmsMaterialDeliverAttr> list = JSONObject.parseArray(ajaxResult.get("data").toString(), WmsMaterialDeliverAttr.class);
            if (ObjectUtils.isEmpty(list)) {
                throw new ServiceException(stdOrderDetailInfo.getMaterialNo() + "物料属性不存在,请维护相关数据");
            }
            BigDecimal palletQty = list.get(0).getPalletQty(); // 根据每托数量拆分

            BigDecimal passTaskQty = totalQty.divide(palletQty, 0, RoundingMode.CEILING); // 合格任务数

            // 根据货主获取租户id
            Long tenantId = commonMapper.getTenantIdByPlantCode(stdOrderDetailInfo.getFactoryCode());

            List<ShelfTask> shelfTaskList = new ArrayList<>();
            // 上架任务
            for (int i = 0; i < passTaskQty.intValue(); i++) {
                ShelfTask shelfTask = new ShelfTask();

                // 获取目的库存地点策略
                AjaxResult inventoryStoragePositionInfoAjaxResult = mainDataService.getPositionCode(stdOrderDetailInfo.getMaterialNo());

                if(inventoryStoragePositionInfoAjaxResult.isError()) {
                    throw new ServiceException("库存地点策略获取失败");
                }
                StoragePositionVo inventoryStoragePositionVo = JSON.parseObject(JSON.toJSONString(inventoryStoragePositionInfoAjaxResult.get("data")), StoragePositionVo.class);

                // 上架任务号
                AjaxResult result = iCodeRuleService.getSeqWithTenantId(TaskNoTypeConstant.SHELF_TASK, Convert.toStr(SecurityUtils.getLoginUser().getSysUser().getTenantId()));
                if (result.isError() || StringUtils.isEmpty(result.get("data").toString())) {
                    throw new ServiceException("上架任务号生成失败！");
                }
                String taskNo = result.get("data").toString();

                shelfTask.setTaskNo(taskNo);
                shelfTask.setTaskType(StockInTaskConstant.INSPECT_TYPE);
                shelfTask.setStatus(TaskStatusConstant.TASK_STATUS_NEW);
                shelfTask.setAllocateTime(DateUtils.getNowDate());
                shelfTask.setMaterialNo(stdOrderDetailInfo.getMaterialNo());
                shelfTask.setMaterialName(stdOrderDetailInfo.getMaterialName());
                shelfTask.setStockinOrderNo(stdOrderDetailInfo.getPurchaseOrderNo());
                shelfTask.setStockinLineNo(stdOrderDetailInfo.getLineNo());
                shelfTask.setLocationCode(inventoryStoragePositionVo.getLocationCode());
                shelfTask.setAreaCode(inventoryStoragePositionVo.getAreaCode());
                shelfTask.setPositionNo(inventoryStoragePositionVo.getPositionNo());
                shelfTask.setPositionId(inventoryStoragePositionVo.getId());
                shelfTask.setContainerNo(stdOrderDetailInfo.getWbsElement());
                shelfTask.setSourceLocationCode(storagePositionVo.getLocationCode());
                shelfTask.setSourceAreaCode(storagePositionVo.getAreaCode());
                shelfTask.setSourcePositionNo(storagePositionVo.getPositionNo());
                shelfTask.setLot(stdOrderDetailInfo.getLotNo());
                shelfTask.setUnit(deliveryOrderDetail.getUnit());
                shelfTask.setOperationUnit(deliveryOrderDetail.getUnit());
                shelfTask.setOperationCompleteQty(BigDecimal.ZERO);

                shelfTask.setQty(palletQty);
                shelfTask.setOperationQty(palletQty);

                // 最后一条数量取余数
                if(i == passTaskQty.intValue() - 1) {
                    BigDecimal qty = totalQty.remainder(deliverAttrList.get(0).getCartonQty());
                    if(!qty.equals(BigDecimal.ZERO)) {
                        shelfTask.setQty(qty);
                        shelfTask.setOperationQty(qty);
                    }
                }
                shelfTask.setCompleteQty(BigDecimal.ZERO);
                // 更新台账
                Long inventoryId = updateInventoryDetailsRaw(stdOrderDetailInfo, storagePositionVo);
                shelfTask.setInventoryId(inventoryId);
                shelfTask.setCreateBy(SecurityUtils.getUsername());
                shelfTask.setCreateTime(DateUtils.getNowDate());
                shelfTask.setTenantId(tenantId);
                shelfTaskList.add(shelfTask);
            }
            // 插入上架任务
            shelfTaskService.insertShelfTaskList(shelfTaskList);
            List<Long> ids = shelfTaskList.stream().map(ShelfTask::getId).collect(Collectors.toList());
            // 打印上架任务列表生成TO单
            shelfTaskService.printList(storagePositionVo.getLocationCode(), ids);
        }

        //查询bom信息，更新台账信息
        StockInItemBomVo stockInItemBom = new StockInItemBomVo();
        stockInItemBom.setStdOrderId(stdOrderDetailInfo.getStdOrderId());
        List<StockInItemBom> stockInItemBoms = stockInItemBomMapper.selectStockInItemBomList(stockInItemBom);
        InventoryDetails inventoryDetails = new InventoryDetails();
        inventoryDetails.setSpecialType(MainDataConstant.SPECIAL_TYPE_OUTSOURCE);
        if (stockInItemBoms != null && !stockInItemBoms.isEmpty()) {
            for (StockInItemBom bom : stockInItemBoms) {
                BomInventoryVo bomInventoryVo = new BomInventoryVo();
                BeanUtils.copyProperties(bom, bomInventoryVo);
                AjaxResult result = iInStoreService.updateBomInventoryDetails(bomInventoryVo);
                if (result.isError()) {
                    throw new ServiceException(Objects.toString(result.get("msg"), "更新库存台账失败"));
                }
            }
        }
        String voucherNo = sendToSap(stdOrderDetailInfo, deliveryOrderDetail);

        deliveryOrderDetail.setIsPost(CommonYesOrNo.POSTED);
        deliveryOrderDetail.setVoucherNo(voucherNo);
        deliveryOrderDetailMapper.updateDeliveryOrderDetail(deliveryOrderDetail);
    }

    /**
     * SAP调拨
     *
     * @param stockInStdOrderDetail
     * @param shelfTask
     * @param storagePositionVo
     */
    private String moveLocationSap(StockInStdOrderDetail stockInStdOrderDetail, ShelfTask shelfTask, StoragePositionVo storagePositionVo) {
        // sap调拨
        Map<String, String> map = new HashMap<>();
        map.put("factoryCode", stockInStdOrderDetail.getFactoryCode());
        map.put("materialNo", stockInStdOrderDetail.getMaterialNo());
        map.put("lotNo", stockInStdOrderDetail.getLotNo());
        map.put("sourceLocation", shelfTask.getSourceLocationCode());
        map.put("comCode", SecurityUtils.getComCode());
        map.put("targetLocation", shelfTask.getLocationCode());
        map.put("qty", Convert.toStr(shelfTask.getQty()));
        // 获取物料默认单位
        MaterialAttrDto materialAttrDto = new MaterialAttrDto();
        materialAttrDto.setMaterialNo(shelfTask.getMaterialNo());
        List<MaterialMainDto> materialList = esbSendCommonMapper.getMaterialMain(materialAttrDto);
        if (CollectionUtils.isEmpty(materialList)) {
            throw new ServiceException(String.format("物料号：%s，获取默认单位为空！", shelfTask.getMaterialNo()));
        }
        map.put("unit", materialList.get(0).getDefaultUnit());
        map.put("isConsign", CommonYesOrNo.NO);
        AjaxResult res = iInStoreService.moveLocationSap(map);
        if ("".equals(Objects.toString(res.get("data"), "")) || res.isError()) {
            throw new ServiceException(Objects.toString(res.get("msg").toString(), "调用库内作业服务失败"));
        }
        return JSON.parseObject(res.get("data").toString(), String.class);
    }

    /**
     * 添加移库记录
     *
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
        inventoryDetails.setIsFreeze("0");
        inventoryDetails.setIsConsign(stockInStdOrderDetail.getIsConsign());
        inventoryDetails.setIsReserved("0");
        // 台账列表
        AjaxResult inventoryDetailListAjaxResult = iInStoreService.selectWmsInventoryDetails2(inventoryDetails);
        if (inventoryDetailListAjaxResult.isError()) {
            throw new ServiceException("查询库存台账失败！");
        }
        List<InventoryDetailsDto> inventoryDetailsList = JSON.parseArray(JSON.toJSONString(inventoryDetailListAjaxResult.get("data")), InventoryDetailsDto.class);

        if (org.springframework.util.CollectionUtils.isEmpty(inventoryDetailsList)) {
            throw new ServiceException("查询库存台账失败！");
        }
        stockMoveDto.setInventoryList(inventoryDetailsList);
        stockMoveDto.setTenantId(stockInStdOrderDetail.getTenantId());
        stockMoveDto.setCreateBy(stockInStdOrderDetail.getCreateBy());
        stockMoveDto.setCreateTime(DateUtils.getNowDate());
        stockMoveDto.setUpdateTime(DateUtils.getNowDate());

        AjaxResult ajaxResult = iInStoreService.addStockMove(stockMoveDto);
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
        iInStoreService.addStockMoveLog(stockMoveLog);
        return JSON.parseObject(ajaxResult.get("data").toString(), String.class);
    }

    /**
     * 更新台账 原材料
     *
     * @param stdOrderDetail
     * @param storagePositionVo
     * @return inventoryId 台账id
     */
    public Long updateInventoryDetailsRaw(StockInStdOrderDetail stdOrderDetail, StoragePositionVo storagePositionVo) {

        // 获取单据信息
        StockInStdOrder stockInStdOrder = stockInStdOrderMapper.selectStockInStdOrderByNo(stdOrderDetail.getPurchaseOrderNo());

        //查询当前仓位是否已有台账存在
        InventoryDetailsVo inventoryDetailsVo = new InventoryDetailsVo();
        inventoryDetailsVo.setMaterialNo(stdOrderDetail.getMaterialNo());
//        inventoryDetailsVo.setFactoryId(stdOrderDetail.getFactoryId());
        inventoryDetailsVo.setFactoryCode(stdOrderDetail.getFactoryCode());
        inventoryDetailsVo.setWarehouseId(storagePositionVo.getWarehouseId());
        inventoryDetailsVo.setWarehouseCode(storagePositionVo.getWarehouseCode());
        inventoryDetailsVo.setAreaId(storagePositionVo.getAreaId());
        inventoryDetailsVo.setAreaCode(storagePositionVo.getAreaCode());
        inventoryDetailsVo.setLocationId(storagePositionVo.getLocationId());
        inventoryDetailsVo.setLocationCode(storagePositionVo.getLocationCode());
        inventoryDetailsVo.setPositionId(storagePositionVo.getId());
        inventoryDetailsVo.setPositionNo(storagePositionVo.getPositionNo());
        inventoryDetailsVo.setStockInLot(stdOrderDetail.getLotNo());
        inventoryDetailsVo.setIsConsign(stdOrderDetail.getIsConsign());
        inventoryDetailsVo.setIsFreeze(CommonYesOrNo.NO);
        inventoryDetailsVo.setIsQc(stdOrderDetail.getIsQc());
        // 排除特殊库存
        inventoryDetailsVo.setStockSpecFlag(CommonYesOrNo.NO);
        inventoryDetailsVo.setProductionLot(stdOrderDetail.getLotNo());
        AjaxResult listResult = iInStoreService.listAllVInventory(inventoryDetailsVo);
        if (listResult.isError()) {
            throw new ServiceException("查询库存台账失败！");
        }
        BigDecimal receivedQty = stdOrderDetail.getReceivedQty();
        List<InventoryDetails> inventoryDetailsList = JSONObject.parseArray(listResult.get("data").toString(), InventoryDetails.class);
        Long inventoryId;


        if (ObjectUtils.isEmpty(inventoryDetailsList)) {
            //新增台账
            InventoryDetails inventoryDetails = new InventoryDetails();
            inventoryDetails.setMaterialNo(stdOrderDetail.getMaterialNo());
            inventoryDetails.setMaterialName(stdOrderDetail.getMaterialName());
            inventoryDetails.setOldMaterialNo(stdOrderDetail.getOldMaterialNo());
            inventoryDetails.setPositionNo(storagePositionVo.getPositionNo());
            inventoryDetails.setPositionId(storagePositionVo.getId());
            inventoryDetails.setAreaId(storagePositionVo.getAreaId());
            inventoryDetails.setAreaCode(storagePositionVo.getAreaCode());
            inventoryDetails.setLocationId(storagePositionVo.getLocationId());
            inventoryDetails.setLocationCode(stdOrderDetail.getLocationCode());
            inventoryDetails.setFactoryId(stockInStdOrder.getFactoryId());
            inventoryDetails.setFactoryCode(stockInStdOrder.getFactoryCode());
            inventoryDetails.setFactoryName(stockInStdOrder.getFactoryName());
            inventoryDetails.setProductionLot(stdOrderDetail.getLotNo());
            inventoryDetails.setInventoryQty(receivedQty);
            inventoryDetails.setAvailableQty(receivedQty);
            inventoryDetails.setContainerNo(stockInStdOrder.getWbsElement());
//            inventoryDetails.setOperationInventoryQty(receivedQty.divide(materialUnitDefDto.getConversDefault(),4, RoundingMode.HALF_UP));
//            inventoryDetails.setOperationAvailableQty(receivedQty.divide(materialUnitDefDto.getConversDefault(),4, RoundingMode.HALF_UP));
//            inventoryDetails.setOperationUnit(materialUnitDefDto.getUnit());
            inventoryDetails.setUnit(stdOrderDetail.getUnit());
            inventoryDetails.setConversDefault(stdOrderDetail.getConversDefault());
            inventoryDetails.setPreparedQty(BigDecimal.ZERO);
            inventoryDetails.setOperationPreparedQty(BigDecimal.ZERO);
            inventoryDetails.setSupplierId(stockInStdOrder.getVendorId());
            inventoryDetails.setSupplierCode(stockInStdOrder.getVendorCode());
            inventoryDetails.setSupplierName(stockInStdOrder.getVendorName());
            inventoryDetails.setStockInDate(DateUtils.getNowDate());


//            BigDecimal extValidityDays = new BigDecimal();
//            inventoryDetails.setExpiryDate(DateUtils.addDays(DateUtils.getNowDate(), extValidityDays.intValue()));
//            inventoryDetails.setValidDays(extValidityDays);
            inventoryDetails.setStockInLot(stdOrderDetail.getLotNo());
            inventoryDetails.setIsQc(stdOrderDetail.getIsQc());
            inventoryDetails.setIsConsign(stdOrderDetail.getIsConsign());
            inventoryDetails.setIsFreeze(CommonYesOrNo.NO);
            inventoryDetails.setWarehouseId(storagePositionVo.getWarehouseId());
            inventoryDetails.setWarehouseCode(storagePositionVo.getWarehouseCode());
            inventoryDetails.setCreateBy(SecurityUtils.getUsername());
            inventoryDetails.setCreateTime(DateUtils.getNowDate());
            inventoryDetails.setUpdateTime(DateUtils.getNowDate());
            AjaxResult result = iInStoreService.add(inventoryDetails);
            if (result.isError()) {
                throw new ServiceException("新增库存台账失败！");
            }
            inventoryId = Long.parseLong(result.get("data").toString());
        } else if (inventoryDetailsList.size() == 1) {
            //修改台账
            InventoryDetails inventoryDetails = inventoryDetailsList.get(0);
            BigDecimal sumInventoryQty = inventoryDetails.getInventoryQty().add(stdOrderDetail.getReceivedQty());
            BigDecimal sumAvailableQty = inventoryDetails.getAvailableQty().add(stdOrderDetail.getReceivedQty());
            inventoryDetails.setInventoryQty(sumInventoryQty);
            inventoryDetails.setAvailableQty(sumAvailableQty);
//            inventoryDetails.setOperationInventoryQty(sumInventoryQty.divide(materialUnitDefDto.getConversDefault(),4, RoundingMode.HALF_UP));
//            inventoryDetails.setOperationAvailableQty(sumAvailableQty.divide(materialUnitDefDto.getConversDefault(),4, RoundingMode.HALF_UP));
            inventoryDetails.setUpdateBy(SecurityUtils.getUsername());
            inventoryDetails.setIsQc(stdOrderDetail.getIsQc());
            inventoryDetails.setUpdateTime(DateUtils.getNowDate());
            inventoryId = inventoryDetails.getId();
            if (iInStoreService.edit(inventoryDetails).isError()) {
                throw new ServiceException("修改库存台账失败！");
            }
        } else {
            throw new ServiceException("库存台账不唯一！");
        }
        return inventoryId;
    }

    /**
     * 添加移动记录
     *
     * @param stockInStdOrderDetail
     */
    private void addTaskLog(StockInStdOrderDetail stockInStdOrderDetail, ShelfTask shelfTask, StoragePositionVo storagePositionVo) {
        // 新增任务记录
        TaskLog taskLog = new TaskLog();
        taskLog.setTaskNo(shelfTask.getTaskNo());
        taskLog.setFactoryCode(stockInStdOrderDetail.getFactoryCode());
        taskLog.setMaterialNo(stockInStdOrderDetail.getMaterialNo());
        taskLog.setMaterialName(stockInStdOrderDetail.getMaterialName());
        taskLog.setOldMaterialNo(stockInStdOrderDetail.getOldMaterialNo());
        taskLog.setLot(stockInStdOrderDetail.getLotNo());
        taskLog.setQty(stockInStdOrderDetail.getDeliverQty());
        taskLog.setOperationQty(stockInStdOrderDetail.getDeliverQty());
        taskLog.setLocationCode(shelfTask.getSourceLocationCode());
        taskLog.setAreaCode(shelfTask.getSourceAreaCode());
        taskLog.setPositionCode(shelfTask.getSourcePositionNo());
        taskLog.setTargetLocationCode(shelfTask.getLocationCode());
        taskLog.setTargetPositionCode(shelfTask.getPositionNo());
        taskLog.setTargetAreaCode(shelfTask.getAreaCode());
        taskLog.setTaskType(TaskLogTypeConstant.CONS_CHANGE);
        taskLog.setOrderNo(stockInStdOrderDetail.getPurchaseOrderNo());
//        taskLog.setMaterialCertificate(String.join(",", voucherNoList));
        taskLog.setIsQc(stockInStdOrderDetail.getIsQc());
        taskLog.setIsConsign(stockInStdOrderDetail.getIsConsign());
        taskLog.setIsFreeze("0");
        taskLog.setOrderType(TaskLogTypeConstant.CONS_CHANGE);
        taskLog.setUnit(stockInStdOrderDetail.getUnit());
        taskLog.setOperationUnit(stockInStdOrderDetail.getOperationUnit());
//        taskLog.setSupplierCode(stockInStdOrderDetail.get);
//        taskLog.setSupplierName(stockInStdOrderDetail.getSupplierName());
        if (taskService.add(taskLog).isError()) {
            throw new ServiceException("新增任务记录失败！");
        }
    }

    /**
     * 新增标准入库单
     *
     * @param stockInStdOrder
     * @return
     */
    @Override
    public int addStockInStdOrder(StockInStdOrder stockInStdOrder) {
        return stockInStdOrderMapper.insertStockInStdOrder(stockInStdOrder);
    }

    @Override
    public List<StockInItemBom> getStockInItemBomById(Long id) {
        StockInItemBomVo stockInItemBomVo = new StockInItemBomVo();
        stockInItemBomVo.setStdOrderId(id);
        return stockInItemBomMapper.selectStockInItemBomList(stockInItemBomVo);
    }

    @Override
    public List<StockInFinOrder> getMaterialInfoByTaskNo(String tastNo) throws Exception {
        List<StockInFinOrder> orderList = new ArrayList<>();

        IMsgObject reqMo = new MsgObject(IMsgObject.MOType.initSR);
        List<GroupRecord> list = new ArrayList<>();
        GroupRecord groupRecord = new GroupRecord();
        // 入参抬头
        groupRecord.setName("T_SGTXT");
        // 任务号
        groupRecord.setFieldValue("SGTXT", tastNo);
        list.add(groupRecord);
        reqMo.setReqGroupRecord(list);
        byte[] result = remoteEsbSendService.sendToEsb(EsbSendSapConstant.Z_WMS_MM_GET_MSEG_SGTXT, reqMo.toString().getBytes());
        MsgObject resMo = new MsgObject(result, IMsgObject.MOType.initSR);

        List<GroupRecord> recordList = resMo.getResGroupRecord("RT_MSEG");

        // 解析数据
        for (GroupRecord record : recordList) {
            StockInFinOrder stockInFinOrder = new StockInFinOrder();
            for (int i = 0; i < record.getFieldSize(); i++) {
                // 汇总数量
                if ("MENGE".equals(groupRecord.getFieldKey(i))) {
                    stockInFinOrder.setMenge(groupRecord.getFieldValue(i));
                }
            }
            orderList.add(stockInFinOrder);
        }
        return orderList;
    }

    public String sendToSap(StockInStdOrderDetail stockInStdOrderDetail, DeliveryOrderDetail deliveryOrderDetail) throws Exception {
        String voucherNo = "";
        IMsgObject reqMo = new MsgObject(IMsgObject.MOType.initSR);
        List<GroupRecord> list = new ArrayList<>();
        GroupRecord groupRecord = new GroupRecord();
        // 入参抬头
        groupRecord.setName("IS_HEAD");
        // 凭证中的过账日期
        groupRecord.setFieldValue("BUDAT", DateUtils.dateTime());
        // 凭证中的凭证日期
        groupRecord.setFieldValue("BLDAT", DateUtils.dateTime());
        // 凭证抬头文本
        groupRecord.setFieldValue("BKTXT", "");
        // 用户名
        groupRecord.setFieldValue("UNAME", SecurityUtils.getUsername());
        GroupRecord itemRecord = new GroupRecord();
        // 入参行项目
        itemRecord.setName("IT_ITEM");
        // 物料编号
        itemRecord.setFieldValue("MATNR", stockInStdOrderDetail.getMaterialNo());
        // 工厂
        itemRecord.setFieldValue("PLANT", stockInStdOrderDetail.getFactoryCode());
        // 收货方
        itemRecord.setFieldValue("WEMPF", stockInStdOrderDetail.getFactoryCode());
        // 收货/发货库存地点
        itemRecord.setFieldValue("UMLGO", stockInStdOrderDetail.getLocationCode());
        // 存储地点
        itemRecord.setFieldValue("LGORT", stockInStdOrderDetail.getLocationCode());
        // 批次编号
        itemRecord.setFieldValue("BATCH", stockInStdOrderDetail.getLotNo());
        // 移动类型(库存管理)
        itemRecord.setFieldValue("BWART", stockInStdOrderDetail.getBwart());
        // 以录入项单位表示的数量
        itemRecord.setFieldValue("ERFMG", Convert.toStr(deliveryOrderDetail.getDeliverQty()));
        // 条目单位
        itemRecord.setFieldValue("ERFME", stockInStdOrderDetail.getUnit());
        // 特殊库存标识
        itemRecord.setFieldValue("SOBKZ", "");
        // 库存类型
        itemRecord.setFieldValue("INSMK", stockInStdOrderDetail.getInsmk());

        StockInItemBomVo stockInItemBom = new StockInItemBomVo();
        stockInItemBom.setStdOrderId(stockInStdOrderDetail.getStdOrderId());

        if (ObjectUtils.isEmpty(stockInStdOrderDetail.getBwart())) {
            itemRecord.setFieldValue("BWART", "101");
        }
        // 移动标识
        itemRecord.setFieldValue("KZBEW", "B");
        // 判断是否是交货单
        if (isDeliveryOrder(stockInStdOrderDetail)) {
            // 交货单号
            itemRecord.setFieldValue("VBELN_VL", stockInStdOrderDetail.getPurchaseOrderNo());
            // 交货项目
            itemRecord.setFieldValue("POSNR_VL", stockInStdOrderDetail.getPurchaseLineNo());
        } else {
            // 采购订单号
            itemRecord.setFieldValue("PO_NUMBER", stockInStdOrderDetail.getPurchaseOrderNo());
            // 采购凭证的项目编号 （行号）
            itemRecord.setFieldValue("PO_ITEM", stockInStdOrderDetail.getPurchaseLineNo());
        }

        // 标识: 未创建转移要求
        itemRecord.setFieldValue("NO_TRANSFER_REQ", "");
        // ORDERID
        itemRecord.setFieldValue("ORDERID", "");
        list.add(groupRecord);
        list.add(itemRecord);
        reqMo.setReqGroupRecord(list);
        byte[] result = remoteEsbSendService.sendToEsb(EsbSendSapConstant.Z_IWMS_VH00_PURCHASE, reqMo.toString().getBytes());
        MsgObject resMo = new MsgObject(result, IMsgObject.MOType.initSR);
        String errorMsg = resMo.getResValue("ERRORMSG");
        String flag = resMo.getResValue("FLAG");
        if (!SapReqOrResConstant.SAP_RETURN_TYPE.equals(flag)) {
            logger.error(errorMsg);
            throw new ServiceException(String.format("Sap标准采购入库（含内向交货单）!,报错信息:%s", errorMsg));
        }
        // 凭证号
        voucherNo = resMo.getResValue("MBLNR");
        return voucherNo;
    }

    /**
     * 判断是否是交货单
     *
     * @param stdOrderDetail
     * @return
     */
    private Boolean isDeliveryOrder(StockInStdOrderDetail stdOrderDetail) {
        String orderNo = stdOrderDetail.getPurchaseOrderNo();
        if (StringUtils.isNotEmpty(orderNo)) {
            // 区分交货单，根据交货单的首字段判断
            return orderNo.startsWith("1") || orderNo.startsWith("8");
        }

        return false;
    }

    /**
     * 确认bom提交
     *
     * @param stockInStdOrder
     * @return
     */
    @Override
    public int readBomSubmit(StockInStdOrder stockInStdOrder) {
        return stockInStdOrderMapper.updateStockInStdOrder(stockInStdOrder);
    }

    /**
     * 按bom详情扣减原材料委外库存
     * @param stdOrder
     * @return
     */
    @Override
    public void updateOutSourcedInventory(StockInStdOrder stdOrder) {
        //查询bom信息，更新台账信息
        StockInItemBomVo stockInItemBom = new StockInItemBomVo();
        stockInItemBom.setStdOrderId(stdOrder.getId());
        List<StockInItemBom> stockInItemBoms = stockInItemBomMapper.selectStockInItemBomList(stockInItemBom);

        logger.info("查询bom信息，更新台账信息");
        if (!ObjectUtils.isEmpty(stockInItemBoms)) {
            InventoryDetails inventoryDetails = new InventoryDetails();
            // 按bom详情扣减原材料委外库存
            inventoryDetails.setSpecialType(MainDataConstant.SPECIAL_TYPE_OUTSOURCE);
            for (StockInItemBom bom : stockInItemBoms) {
                BomInventoryVo bomInventoryVo = new BomInventoryVo();
                BeanUtils.copyProperties(bom, bomInventoryVo);
                AjaxResult result = iInStoreService.updateBomInventoryDetails(bomInventoryVo);
                if(result.isError()){
                    throw new ServiceException(Objects.toString(result.get("msg"),"更新库存台账失败"));
                }
            }
        }
    }
}
