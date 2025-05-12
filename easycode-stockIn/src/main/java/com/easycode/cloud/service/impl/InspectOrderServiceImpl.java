package com.easycode.cloud.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import com.easycode.cloud.domain.DeliveryInspectionTask;
import com.easycode.cloud.domain.InspectOrder;
import com.easycode.cloud.domain.InspectOrderDetails;
import com.easycode.cloud.domain.ShelfTask;
import com.easycode.cloud.domain.dto.InspectOrderDto;
import com.easycode.cloud.domain.dto.TaskInfoDto;
import com.easycode.cloud.domain.vo.InspectOrderDetailsVo;
import com.easycode.cloud.domain.vo.InspectOrderVo;
import com.easycode.cloud.domain.vo.StockInDetailPrintVo;
import com.easycode.cloud.mapper.*;
import com.easycode.cloud.service.IInspectOrderService;
import com.easycode.cloud.service.IShelfTaskService;
import com.soa.eis.adapter.framework.message.IMsgObject;
import com.soa.eis.adapter.framework.message.impl.GroupRecord;
import com.soa.eis.adapter.framework.message.impl.MsgObject;
import com.weifu.cloud.common.core.exception.ServiceException;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.security.utils.DictUtils;
import com.weifu.cloud.common.security.utils.SecurityUtils;
import com.weifu.cloud.constant.*;
import com.weifu.cloud.domain.*;
import com.weifu.cloud.domain.dto.*;
import com.weifu.cloud.domain.vo.*;
import com.weifu.cloud.domian.*;
import com.weifu.cloud.domian.dto.*;
import com.weifu.cloud.domian.vo.InventoryDetailsVo;
import com.weifu.cloud.domian.vo.StoragePositionVo;
import com.weifu.cloud.enums.RemoteConfigEnum;
import com.weifu.cloud.enums.StockOutTaskEnum;
import com.weifu.cloud.mapper.*;
import com.weifu.cloud.service.*;
import com.weifu.cloud.system.api.domain.SysDictData;
import com.weifu.cloud.system.service.ISysConfigService;
import com.weifu.cloud.tools.StringUtil;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.HttpStatus;
import org.aspectj.weaver.loadtime.Aj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * 送检单Service业务层处理
 *
 * @author weifu
 * @date 2023-03-29
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class InspectOrderServiceImpl implements IInspectOrderService
{
    @Autowired
    private InspectOrderMapper inspectOrderMapper;

    @Autowired
    private ICodeRuleService iCodeRuleService;

    @Autowired
    private IMainDataService mainDataService;

    @Autowired
    private IStockOutService iStockOutService;

    @Autowired
    private InspectOrderDetailsMapper inspectOrderDetailsMapper;

    @Autowired
    private EsbSendCommonMapper esbSendCommonMapper;

    @Autowired
    private ITaskService taskService;

    @Autowired
    private IShelfTaskService shelfTaskService;

    @Autowired
    private IStockInService stockInService;

    @Autowired
    private RemoteEsbSendService remoteEsbSendService;


    @Autowired
    private RemoteConfigHelper remoteConfigHelper;

    @Autowired
    private IStockOutService stockOutService;

    @Autowired
    private IPrintService printService;

    @Autowired
    private StockInStdOrderMapper stockInStdOrderMapper;

    @Autowired
    private StockInStdOrderDetailMapper stockInStdOrderDetailMapper;

    @Autowired
    private DeliveryInspectionTaskMapper deliveryInspectionTaskMapper;

    @Autowired
    private IInStoreService inStoreService;

    @Autowired
    private SapInteractionService sapInteractionService;

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private RedisTemplate redisTemplate;


    private static final Logger log = LoggerFactory.getLogger(InspectOrderServiceImpl.class);
    /**
     * 查询送检单
     *
     * @param id 送检单主键
     * @return 送检单
     */
    @Override
    public InspectOrder selectInspectOrderById(Long id)
    {
        return inspectOrderMapper.selectInspectOrderById(id);
    }

    /**
     * 查询送检单列表
     *
     * @param inspectOrder 送检单
     * @return 送检单
     */
    @Override
    public List<InspectOrderVo> selectInspectOrderList(InspectOrderVo inspectOrder)
    {
        List<InspectOrderVo> inspectOrderVos = inspectOrderMapper.selectInspectOrderJoinDetailsList(inspectOrder);
        //未查询到数据时直接返回
        if (ObjectUtils.isEmpty(inspectOrderVos)){
            return inspectOrderVos;
        }
        List<String> materialNoList = inspectOrderVos.stream().map(InspectOrder::getMaterialNo).distinct().collect(Collectors.toList());
        // 根据生产发料、生产补料 判断是否高亮
        ProductionOrderInfoDto productionOrderInfoDto = new ProductionOrderInfoDto();
        productionOrderInfoDto.setMaterialNoList(materialNoList);
        AjaxResult highlightResult = iStockOutService.getInspectOrderHighlight(productionOrderInfoDto);
        if (highlightResult.isError()) {
            throw new ServiceException(highlightResult.get("msg").toString());
        }
        //需要高亮的物料号 key为 物料号 value为 高亮类型HighlightType
        Map<String, String> highlightMaterialNoMap = JSON.parseObject(JSONObject.toJSONString(highlightResult.get("data")), Map.class);
        //获取所有生产订单明细中包含有materialNoList对应的生产订单
        for (InspectOrderVo inspectOrderVo : inspectOrderVos) {
            String materialNo = inspectOrderVo.getMaterialNo();
            //设置高亮
            if (highlightMaterialNoMap.containsKey(materialNo)){
                inspectOrderVo.setHighlight(highlightMaterialNoMap.get(materialNo));
            }
        }
        return inspectOrderVos;
    }

    @Override
    public StockInDetailPrintVo selectInspectOrderByTaskNo(String taskNo) {
        StockInDetailPrintVo stockInDetailPrintVo = inspectOrderMapper.selectInspectOrderByTaskNo(taskNo);
        return stockInDetailPrintVo;
    }

    /**
     * 新增送检单
     *
     * @param inspectOrder 送检单
     * @return 结果
     */
    @Override
    public int insertInspectOrder(InspectOrder inspectOrder)
    {
        Long tenantId = SecurityUtils.getLoginUser().getSysUser().getTenantId();

        inspectOrder.setCreateTime(DateUtils.getNowDate());
        // 创建送检单单号
//        AjaxResult ajaxResult = iCodeRuleService.getSeqWithTenantId(OrderNoTypeConstant.INSPECT, String.valueOf(tenantId));
//        if (ajaxResult.isError() || StringUtils.isEmpty(ajaxResult.get("data").toString())){
//            throw new ServiceException("送检单单号生成失败！");
//        }
//        String orderNo = ajaxResult.get("data").toString();
//        inspectOrder.setOrderNo(orderNo);
        return inspectOrderMapper.insertInspectOrder(inspectOrder);
    }


    /**
     * 修改送检单
     *
     * @param inspectOrderDto 送检单
     * @return 结果
     */
    @Override
    public int updateInspectOrder(InspectOrderDto inspectOrderDto)
    {
        inspectOrderDto.setUpdateTime(DateUtils.getNowDate());
        inspectOrderDto.setUpdateBy(SecurityUtils.getUsername());
        inspectOrderDto.setQcInspector(SecurityUtils.getUsername());
        // 目前只有录入检验结果用到了此方法，所以在这里插入检验时间
        inspectOrderDto.setQcDate(DateUtils.getNowDate());
        inspectOrderDto.setEnterResultTime(DateUtils.getNowDate());
        // 处理单号目前存在于送检单明细备注字段中
        InspectOrderDto orderDto = new InspectOrderDto();
        orderDto.setOrderNos(inspectOrderDto.getOrderNos());
        orderDto.setDisposeOrderNo(inspectOrderDto.getDisposeOrderNo());
        orderDto.setRemark(inspectOrderDto.getRemark());
        orderDto.setUpdateTime(DateUtils.getNowDate());
        orderDto.setUpdateBy(SecurityUtils.getUsername());
        inspectOrderDetailsMapper.updateInspectOrderDetailsBatch(orderDto);
        return inspectOrderMapper.updateInspectOrderBatch(inspectOrderDto);
    }

    /**
     * 批量删除送检单
     *
     * @param ids 需要删除的送检单主键
     * @return 结果
     */
    @Override
    public int deleteInspectOrderByIds(Long[] ids)
    {
        return inspectOrderMapper.deleteInspectOrderByIds(ids);
    }

    /**
     * 上传附件
     *
     * @param file
     * @return 结果
     */
    @Override
    public int upload(MultipartFile file)
    {



        return HttpStatus.SC_OK;
    }

    /**
     * 删除送检单信息
     *
     * @param id 送检单主键
     * @return 结果
     */
    @Override
    public int deleteInspectOrderById(Long id)
    {
        return inspectOrderMapper.deleteInspectOrderById(id);
    }

    /**
     * 检验结果处理
     * @param detailDto
     * @return
     * @throws Exception
     */
    @Override
    public int sendInspectResult(SendInspectResultDto detailDto) throws Exception {
        log.info("sendInspectResult, params: {}", detailDto);
        if (StringUtils.isEmpty(detailDto.getTaskCode())) {
            throw new ServiceException("送检任务号为空，请确认送检结果！");
        }

        if (StringUtils.isEmpty(detailDto.getResult())) {
            throw new ServiceException("送检结果为空，请确认送检结果！");
        }
        InspectOrder inspectOrder = inspectOrderMapper.selectInspectOrderByNo(detailDto.getTaskCode());
        if(ObjectUtils.isEmpty(inspectOrder)) {
            throw new ServiceException(String.format("送检任务”%s“不存在！", detailDto.getTaskCode()));
        }
        if(!ObjectUtils.isEmpty(inspectOrder.getChkResult())) {
            throw new ServiceException(String.format("送检单“%s”已提供送检结果，不能重复更新！", detailDto.getTaskCode()));
        }
        String billStatus = inspectOrder.getBillStatus();
        if(!ObjectUtils.isEmpty(inspectOrder.getQcQty()) && inspectOrder.getQcQty().compareTo(BigDecimal.ZERO) > 0
                && InspectOrderStatusConstant.ORDER_STATUS_NEW.equals(billStatus)) {
            throw new ServiceException(String.format("送检任务”%s“不存在！ 检验单状态非合法状态！！！", detailDto.getTaskCode()));
        }
        StockInStdOrderDetailDto stdOrderDetailDto = new StockInStdOrderDetailDto();
        stdOrderDetailDto.setPurchaseOrderNo(inspectOrder.getPurchaseOrderNo());
        stdOrderDetailDto.setPurchaseLineNo(inspectOrder.getPurchaseLineNo());
        List<StockInStdOrderDetail> stdOrderDetailList = stockInStdOrderDetailMapper.selectStockInStdOrderDetailList(stdOrderDetailDto);

        if (CollectionUtils.isEmpty(stdOrderDetailList)) {
            throw new ServiceException("标准入库单明细不存在");
        }
        StockInStdOrderDetail stockInStdOrderDetail = stdOrderDetailList.get(0);

        InspectOrderDetails query = new InspectOrderDetailsVo();
        // 送检单据编号
        query.setOrderNo(inspectOrder.getOrderNo());
        List<InspectOrderDetailsVo> inspectOrderDetailsList = inspectOrderDetailsMapper.selectInspectOrderDetailsList(query);

        if (ObjectUtils.isEmpty(inspectOrderDetailsList)) {
            throw new ServiceException("检验单明细:" + inspectOrder.getOrderNo() + ", 不存在");
        }
        InspectOrderDetailsVo inspectOrderDetails = inspectOrderDetailsList.get(0);

        InspectOrderDto inspectOrderDto = new InspectOrderDto();
        BeanUtils.copyProperties(inspectOrder, inspectOrderDto);
        inspectOrderDto.setLot(inspectOrderDetails.getLot());

        if (inspectOrder.getQty().compareTo(BigDecimal.ZERO) > 0) {
            if (SQConstant.SQ_INSPECT_PASS.equals(detailDto.getResult())) {
                //更新台账增加移动记录 应该为5000 -> 5050
                syncSapAndUpdateInventory(inspectOrderDto, stockInStdOrderDetail,
                        inspectOrder.getLocationCode(), SQConstant.PASS_LOCATION_CODE, detailDto.getTaskCode());
                inspectOrder.setLocationCode(SQConstant.PASS_LOCATION_CODE);
            } else {
                //将物料移到 不合格区 并sap 过账
                // 应该为5000 -> 5060
                syncSapAndUpdateInventory(inspectOrderDto, stockInStdOrderDetail,
                        inspectOrder.getLocationCode(), SQConstant.FAIL_LOCATION_CODE, detailDto.getTaskCode());
                inspectOrder.setLocationCode(SQConstant.FAIL_LOCATION_CODE);
            }
        } else {
            releaseInventory(inspectOrderDetails, inspectOrder, "1000");
        }
        // 修改检验状态
        inspectOrder.setBillStatus(InspectOrderStatusConstant.ORDER_STATUS_INSPECTION_COMPLETE);
        inspectOrder.setChkResult(detailDto.getResult());
        inspectOrder.setUpdateBy("admin");
        inspectOrder.setUpdateTime(DateUtils.getNowDate());
        inspectOrder.setQcDate(DateUtils.getNowDate());
        return inspectOrderMapper.updateInspectOrder(inspectOrder);
    }

    /**
     * 质检后处理-
     * @param resultIQCDto
     * @return
     * @throws Exception
     */
    @Override
    @GlobalTransactional
    public int sendSealedResultIQC(SendSealedResultIQCDto resultIQCDto) throws Exception {
        log.info("sendSealedResultIQC, params: {}", resultIQCDto);
        try {
        if (StringUtils.isEmpty(resultIQCDto.getTaskCode())) {
            throw new ServiceException("送检单任务号不能为空！");
        }

        InspectOrder inspectOrder = inspectOrderMapper.selectInspectOrderByNo(resultIQCDto.getTaskCode());
        if (ObjectUtils.isEmpty(inspectOrder)) {
            throw new ServiceException(String.format("送检任务”%s“不存在！", resultIQCDto.getTaskCode()));
        }
        if(inspectOrder.getBillStatus().equals(InspectOrderStatusConstant.ORDER_STAT_COMPLETE)){
            throw  new ServiceException(String.format("送检单“%s”已完成，不能重复处理！", resultIQCDto.getTaskCode()));
        }
        // 尝试从缓存中读取缓存
        Object cacheResult =  redisTemplate.opsForValue().get(resultIQCDto.getTaskCode());

           if (!Objects.isNull(cacheResult)) {
               // 有缓存 表示任务 正在处理中
               throw new ServiceException("送检单任务号:" + resultIQCDto.getTaskCode() + ", 正在处理中，请稍后再试！");
           }else {
               // 缓存开始处理标识, 业务处理完成，删除缓存
               redisTemplate.opsForValue().set(resultIQCDto.getTaskCode(), resultIQCDto, 240, TimeUnit.SECONDS);
           }
        InspectOrderDetails query = new InspectOrderDetailsVo();
        // 送检单据编号
        query.setOrderNo(inspectOrder.getOrderNo());
        List<InspectOrderDetailsVo> inspectOrderDetailsList = inspectOrderDetailsMapper.selectInspectOrderDetailsList(query);

        if (ObjectUtils.isEmpty(inspectOrderDetailsList)) {
            throw new ServiceException("检验单明细:" + resultIQCDto.getTaskCode() + ", 不存在");
        }
        InspectOrderDetailsVo inspectOrderDetails = inspectOrderDetailsList.get(0);
        // 后处理结果IQC(数量)，让步, 挑选, 退货, 释放, 破坏
        BigDecimal concessionCount = new BigDecimal(StringUtils.isEmpty(resultIQCDto.getConcessionCount())? "0": resultIQCDto.getConcessionCount());
        BigDecimal selectCount = new BigDecimal(StringUtils.isEmpty(resultIQCDto.getSelectCount())? "0":resultIQCDto.getSelectCount());
        BigDecimal returnCount = new BigDecimal(StringUtils.isEmpty(resultIQCDto.getReturnedCount())? "0":resultIQCDto.getReturnedCount());
        BigDecimal releaseCount = new BigDecimal(StringUtils.isEmpty(resultIQCDto.getReleaseCount())? "0":resultIQCDto.getReleaseCount());
        BigDecimal destroyCount = new BigDecimal(StringUtils.isEmpty(resultIQCDto.getDestroyCount())? "0":resultIQCDto.getDestroyCount());

        log.info("质检后处理返回结果信息，让步: {}, 挑选: {}, 退货: {}, 释放: {}, 破坏: {}",
                concessionCount, selectCount, returnCount, releaseCount, destroyCount);

        Long tenantId = MainDataConstant.TENANT_ID;
        // 凭证号
        String voucherNo = "";

        // 抽检数量为零，数量不变，依据数量生成上架任务
        // 根据物料号查物料属性
        WmsMaterialDeliverAttr dto = new WmsMaterialDeliverAttr();
        dto.setMaterialNo(inspectOrder.getMaterialNo());
        AjaxResult ajaxResult = mainDataService.getDeliverAttr(dto);

        if (ajaxResult.isError()) {
            throw new ServiceException("物料包装信息未配置，物料号:" + inspectOrder.getMaterialNo() + ",请维护相关数据");
        }

        List<WmsMaterialDeliverAttr> deliverAttrList = com.alibaba.fastjson.JSONObject.parseArray(ajaxResult.get("data").toString(), WmsMaterialDeliverAttr.class);

        // 获取每箱数量
        WmsMaterialDeliverAttr materialDeliverAttr = deliverAttrList.get(0);

        BigDecimal cartonQty = materialDeliverAttr.getCartonQty();
        if (ObjectUtil.isEmpty(cartonQty)) {
            throw new ServiceException("该物料未维护每箱数量，物料号:" + inspectOrder.getMaterialNo() + ",请维护相关数据");
        }

        StockInStdOrderDetailDto detailDto = new StockInStdOrderDetailDto();
        detailDto.setPurchaseOrderNo(inspectOrder.getPurchaseOrderNo());
        List<StockInStdOrderDetail> stdOrderDetailList = stockInStdOrderDetailMapper.selectStockInStdOrderDetailList(detailDto);
        if (CollectionUtils.isEmpty(stdOrderDetailList)) {
            throw new ServiceException("标准入库单明细不存在");
        }
        if (StringUtils.isEmpty(inspectOrder.getChkResult())) {
           throw new ServiceException(String.format("送检单状态异常，送检单号:%s" , resultIQCDto.getTaskCode()));
        }
        // 检验合格
        if (SQConstant.SQ_INSPECT_PASS.equals(inspectOrder.getChkResult())) {
            log.info("开始质检合格处理...");
            if (!ObjectUtils.isEmpty(releaseCount) && releaseCount.intValue() > 0) {
                // 重新查询台账，根据台账去做解Q
                // 物料号，Q状态， 批次 = 》按库存地点分组，汇总数量，调用接口解Q
                // 台账更新和移动记录
                voucherNo = releaseInventory(inspectOrderDetails, inspectOrder, "1000");
            }
            // 破坏数量
            if (!ObjectUtils.isEmpty(destroyCount) && destroyCount.intValue() > 0) {
                // 从5050领取生成任务自动完成
                // 更新台账和移动记录, sap过账
                // 固定成本中心做成字典项
                addCostCenterOrder(inspectOrder, inspectOrderDetails, tenantId, destroyCount);
            }
        }

        // 检验不合格
        if (SQConstant.SQ_INSPECT_FAILED.equals(inspectOrder.getChkResult())) {
            // 让步数量
            if (concessionCount.intValue() > 0) {
                // 让步数量等于合格里的释放，减去破坏数量后释放，破坏算供应商，
                // 从5060用成本中心领料消耗扣掉数量，其他正常解Q
                // 查询台账，根据台账去做解Q
                // 物料号，Q状态， 批次 = 》按库存地点分组，汇总数量，调用接口解Q
                // 台账更新和移动记录
                voucherNo = releaseInventory(inspectOrderDetails, inspectOrder, "1001");
                // 破坏数量
                if (!ObjectUtils.isEmpty(destroyCount) && destroyCount.intValue() > 0) {
                    addCostCenterOrder(inspectOrder, inspectOrderDetails, tenantId, destroyCount);
                    // 从5060库存地点领取生成任务自动完成
                    // 更新台账和移动记录
                    // sap过账
                    // 固定成本中心做成字典项
                }
            }
            // 挑选数量不做处理，和退货流程一样（人为干预处理）
            // 原因：上架不拆包装，无法根据部分释放数量拆台账（多库存地点，多状态）
        }

        inspectOrderDetails.setConcessionQty(concessionCount);
        inspectOrderDetails.setDestructQty(destroyCount);
        inspectOrderDetails.setReturnQty(returnCount);
        inspectOrderDetails.setPickQty(selectCount);
        inspectOrderDetails.setReleaseQty(releaseCount);
        inspectOrderDetails.setUpdateTime(DateUtils.getNowDate());
        inspectOrderDetailsMapper.updateInspectOrderDetails(inspectOrderDetails);

        // 送检单完成
        inspectOrder.setVoucherNo(voucherNo);
        inspectOrder.setBillStatus(InspectOrderStatusConstant.ORDER_STAT_COMPLETE);
        inspectOrder.setUpdateBy("admin");
        inspectOrder.setUpdateTime(DateUtils.getNowDate());
        inspectOrder.setQcDate(DateUtils.getNowDate());
        inspectOrderMapper.updateInspectOrder(inspectOrder);
        return HttpStatus.SC_OK;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException("质检后结果处理异常，异常信息：" + e.getMessage());
        } finally {
            // 最后删除缓存
            if (StringUtils.isNotEmpty(resultIQCDto.getTaskCode())) {
               redisTemplate.delete(resultIQCDto.getTaskCode());
            }
        }
    }

    private String releaseInventory(InspectOrderDetailsVo inspectOrderDetails, InspectOrder inspectOrder, String number) throws Exception {
        InventoryDetailsVo inventoryDetailsVo = new InventoryDetailsVo();
        inventoryDetailsVo.setMaterialNo(inspectOrderDetails.getMaterialNo());
        inventoryDetailsVo.setIsQc(CommonYesOrNo.YES);
        inventoryDetailsVo.setStockInLot(inspectOrderDetails.getLot());
        //inventoryDetailsVo.setLocationCode(SQConstant.PASS_LOCATION_CODE);
        AjaxResult ajaxResult1 = inStoreService.list(inventoryDetailsVo);
        if (ajaxResult1.isError()) {
            throw new ServiceException("查询库存台账失败！");
        }
        List<InventoryDetails> inventoryDetailsList = com.alibaba.fastjson.JSONObject.parseArray(ajaxResult1.get("data").toString(), InventoryDetails.class);
        if (CollectionUtils.isEmpty(inventoryDetailsList)) {
            log.error("物料号" + inventoryDetailsVo.getMaterialNo() + "批次" + inventoryDetailsVo.getStockInLot() + "解Q未查询到数据");
        }
        Map<String, List<InventoryDetails>> inventoryDetailsMap = inventoryDetailsList.stream().collect(groupingBy(InventoryDetails::getLocationCode));
        List<Long> ids = new ArrayList<>();
        String voucherNo = "";
        for (Map.Entry<String, List<InventoryDetails>> entry : inventoryDetailsMap.entrySet()) {
            String locationCode = entry.getKey();  // 获取键
            List<InventoryDetails> inventoryDetails = entry.getValue();  // 获取值
            BigDecimal sum = BigDecimal.ZERO;
            for (InventoryDetails e : inventoryDetails) {
                sum = sum.add(e.getAvailableQty());
                ids.add(e.getId());
            }
            if (sum.compareTo(BigDecimal.ZERO) > 0) {
                voucherNo = inspectReleaseSendSap(inspectOrder.getMaterialNo(), inspectOrder.getFactoryCode(), inspectOrder.getSupplierCode(),
                        sum, inspectOrderDetails.getUnit(), locationCode, number,
                        inspectOrderDetails.getPrdLot(), inspectOrder.getIsConsign());
            }
        }
        if (!ObjectUtils.isEmpty(ids)) {
            inStoreService.updateInventoryIsQc(ids);
        }
        return voucherNo;
    }


    private void addRawReturnOrder(InspectOrder inspectOrder, Long tenantId, InspectOrderDetailsVo inspectOrderDetails, BigDecimal returnCount) {
        RwmReturnOrderDto rwmReturnOrderDto = new RwmReturnOrderDto();
        rwmReturnOrderDto.setOrderNo(inspectOrder.getOrderNo());
        rwmReturnOrderDto.setPurchaseOrderNo(inspectOrder.getPurchaseOrderNo());
        rwmReturnOrderDto.setFactoryCode(inspectOrder.getFactoryCode());
        List<FactoryCommonDto> factoryInfo = esbSendCommonMapper.getFactoryByCode(inspectOrder.getFactoryCode());

        rwmReturnOrderDto.setFactoryId(factoryInfo.get(0).getId());
        rwmReturnOrderDto.setIsShortage(CommonYesOrNo.NO);
        rwmReturnOrderDto.setBillStatus(OrderStatusConstant.ORDER_STATUS_PART_COMPLETE);
        rwmReturnOrderDto.setSupplierCode(inspectOrder.getSupplierCode());
        SupplierCommonDto supplierInfoByCode = esbSendCommonMapper.getSupplierInfoByCode(inspectOrder.getSupplierCode());
        if (!Optional.ofNullable(supplierInfoByCode).isPresent()) {
            throw new ServiceException(String.format("根据供应商code:%s未查询到相关信息", inspectOrder.getSupplierCode()));
        }
        rwmReturnOrderDto.setSupplierId(supplierInfoByCode.getId());
        rwmReturnOrderDto.setLocationCode(inspectOrder.getLocationCode());
        rwmReturnOrderDto.setTenantId(tenantId);
        iStockOutService.addRwmOrderTask(rwmReturnOrderDto);

        // 原材料退货明细
        RwmReturnOrderDetailsDto returnDetails = new RwmReturnOrderDetailsDto();
        returnDetails.setFactoryCode(rwmReturnOrderDto.getFactoryCode());
        returnDetails.setLineNo(inspectOrder.getPurchaseLineNo());
        returnDetails.setDeliveryOrderNo(inspectOrder.getReceiveOrderNo());
        returnDetails.setPurchaseOrderNo(inspectOrder.getPurchaseOrderNo());
        returnDetails.setDeliveryLineNo(inspectOrder.getPurchaseLineNo());
        returnDetails.setLot(inspectOrderDetails.getPrdLot());
        returnDetails.setMaterialCode(inspectOrder.getMaterialNo());

        // 根据物料、批次、任务类型、数量查询任务记录中的对应数据，获取物料凭证
        TaskLogDto taskLogDto = new TaskLogDto();
        taskLogDto.setMaterialNo(inspectOrder.getMaterialNo());
        taskLogDto.setLot(inspectOrderDetails.getPrdLot());
        taskLogDto.setQty(returnCount);
        AjaxResult result = taskService.queryTaskLogList(taskLogDto);

        if ("".equals(Objects.toString(result.get("data"), "")) || result.isError()) {
            throw new ServiceException("获取任务执行记录信息失败！");
        }
        List<TaskLogDto> list = JSON.parseArray(JSON.toJSONString(result.get("data")), TaskLogDto.class);
        if (!ObjectUtils.isEmpty(list)) {
            returnDetails.setWriteOffCert(list.get(0).getMaterialCertificate());
        }
        MaterialAttrDto materialAttrDto = new MaterialAttrDto();
        materialAttrDto.setFactoryId(factoryInfo.get(0).getId());
        materialAttrDto.setMaterialNo(inspectOrder.getMaterialNo());
        List<MaterialMainDto> materialMain = esbSendCommonMapper.getMaterialMain(materialAttrDto);
        if (null == materialMain || materialMain.isEmpty()) {
            throw new ServiceException(String.format("根据物料code:%s未查询到相关信息", inspectOrderDetails.getMaterialNo()));
        }
        MaterialMainDto material = materialMain.get(0);
        returnDetails.setMaterialName(material.getMaterialName());
        returnDetails.setOldMaterialNo(material.getOldMaterialNo());
        returnDetails.setUnit(material.getDefaultUnit());
        returnDetails.setOderNo(rwmReturnOrderDto.getOrderNo());
        returnDetails.setPurchaseOrderNo(rwmReturnOrderDto.getPurchaseOrderNo());
        returnDetails.setPurchaseLineNo(inspectOrder.getPurchaseLineNo());

        returnDetails.setActiveQty(returnCount);
        returnDetails.setCompleteQty(returnCount);
        returnDetails.setQty(returnCount);
        returnDetails.setOperationQty(returnCount);
        returnDetails.setOperationActiveQty(returnCount);
        returnDetails.setOperationCompleteQty(returnCount);
        returnDetails.setOderNo(inspectOrder.getOrderNo());
        returnDetails.setTenantId(tenantId);
        iStockOutService.addRwmOrderTaskDetail(returnDetails);
    }

    private void addCostCenterOrder(InspectOrder inspectOrder, InspectOrderDetailsVo inspectOrderDetails, Long tenantId, BigDecimal destroyCount) {
        // 生成成本中心领料
        List<FactoryCommonDto> factoryInfo = esbSendCommonMapper.getFactoryByCode(inspectOrder.getFactoryCode());

        String defCostCenterType = sysConfigService.selectConfigByKey("def_cost_center_type");
        AjaxResult costCenterResult = mainDataService.dropList();
        if (costCenterResult.isError() || ObjectUtils.isEmpty(costCenterResult.get("data"))) {
            throw new ServiceException("获取成本中心信息失败");
        }
        List<WmsCostCenterInfo> costCenterInfoList = JSON.parseArray(JSONObject.toJSONString(costCenterResult.get("data")), WmsCostCenterInfo.class);

        if(CollectionUtils.isEmpty(costCenterInfoList)) {
            throw new ServiceException("获取成本中心信息失败");
        }
        List<WmsCostCenterInfo> defWmsCostCenterInfo = costCenterInfoList.stream().filter(e -> e.getCostCenterCode().equals(defCostCenterType)).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(defWmsCostCenterInfo)) {
            throw new ServiceException("获取默认成本中心类型失败");
        }
        // 生成成本中心领料
        CostStockOutOrderDto costStockOutOrderDto = new CostStockOutOrderDto();
        costStockOutOrderDto.setCostCenterCode(defWmsCostCenterInfo.get(0).getCostCenterCode());
        costStockOutOrderDto.setCostCenterDesc(defWmsCostCenterInfo.get(0).getCostCenterDesc());
        costStockOutOrderDto.setCostCenterType(defWmsCostCenterInfo.get(0).getCostCenterType());
        costStockOutOrderDto.setFactroyId(factoryInfo.get(0).getId());
        costStockOutOrderDto.setFactoryCode(inspectOrder.getFactoryCode());
//        costStockOutOrderDto.setInnerOrderNo(inspectOrder.getOrderNo());
        costStockOutOrderDto.setMaterialRequisitionType("");
        costStockOutOrderDto.setMoveType("201");
        // 查询库存台账
        InventoryDetailsVo inventoryDetailsVo = new InventoryDetailsVo();
        inventoryDetailsVo.setIsConsign(CommonYesOrNo.NO);
        inventoryDetailsVo.setIsFreeze(CommonYesOrNo.NO);
        inventoryDetailsVo.setMaterialNo(inspectOrder.getMaterialNo());
        inventoryDetailsVo.setLocationCode(inspectOrder.getLocationCode());
        inventoryDetailsVo.setStockInLot(inspectOrderDetails.getLot());
        AjaxResult ajaxResult = inStoreService.list(inventoryDetailsVo);
        if (ajaxResult.isError()) {
            throw new ServiceException("查询库存库存台账失败！");
        }
        List<InventoryDetails> inventoryDetails = JSON.parseArray(ajaxResult.get("data").toString(), InventoryDetails.class);
        if (ObjectUtils.isEmpty(inventoryDetails)) {
            throw new ServiceException("没有查询到库存库存台账！");
        }
        ArrayList<CostStockOutOrderDetail> detailList = new ArrayList<>();
        for(InventoryDetails inventoryDetails1:inventoryDetails){
            CostStockOutOrderDetail detail = new CostStockOutOrderDetail();
            InventoryDetails inventoryDetails2 = new InventoryDetails();
            inventoryDetails2.setId(inventoryDetails1.getId());
            inventoryDetails2.setUpdateBy(SecurityUtils.getUsername());
            inventoryDetails2.setUpdateTime(DateUtils.getNowDate());
            BigDecimal availableQty = inventoryDetails1.getAvailableQty();
            detail.setMaterialNo(inspectOrder.getMaterialNo());
            detail.setOldMaterialNo(inventoryDetails1.getOldMaterialNo());
            detail.setMaterialName(inspectOrder.getMaterialName());
            detail.setShortageQty(new BigDecimal(0));
            detail.setOperationShortageQty(new BigDecimal(0));
            detail.setFactoryCode(inspectOrder.getFactoryCode());
            detail.setFactoryId(factoryInfo.get(0).getId());
            detail.setUnit(inventoryDetails1.getUnit());
            detail.setOperationUnit(inventoryDetails1.getUnit());
            detail.setConverterDefault(null);
            detail.setTaskBookNo("");
            detail.setTenantId(tenantId);
            detail.setLocationCode(inspectOrder.getLocationCode());
            detail.setInventoryId(inventoryDetails1.getId());
            if (availableQty.compareTo(destroyCount) >= 0) {
                detail.setRequestQty(destroyCount);
                detail.setActiveQty(destroyCount);
                detail.setFinishQty(destroyCount);
                detail.setOperationRequestQty(destroyCount);
                detail.setOperationActiveQty(destroyCount);
                detail.setOperationFinishQty(destroyCount);
                detailList.add(detail);
                inventoryDetails2.setAvailableQty(destroyCount);
                inventoryDetails2.setInventoryQty(inventoryDetails1.getInventoryQty().subtract(destroyCount));
                inStoreService.subtractInventoryDetailsAvailable(inventoryDetails2);
                break;
            } else {
                destroyCount = destroyCount.subtract(availableQty);
                detail.setRequestQty(availableQty);
                detail.setActiveQty(availableQty);
                detail.setFinishQty(availableQty);
                detail.setOperationRequestQty(availableQty);
                detail.setOperationActiveQty(availableQty);
                detail.setOperationFinishQty(availableQty);
                detailList.add(detail);
                inventoryDetails2.setAvailableQty(availableQty);
                inventoryDetails2.setInventoryQty(inventoryDetails1.getInventoryQty().subtract(availableQty));
                inStoreService.subtractInventoryDetailsAvailable(inventoryDetails2);
            }
        }
        costStockOutOrderDto.setDetailList(detailList);
        iStockOutService.addCostStockOutOrderByInspectOrder(costStockOutOrderDto);
    }

    /**
     * SAP调拨
     * @param shelfTask
     * @param storagePositionVo
     */
    private String moveLocationSap(StockInStdOrderDetail detailDto, ShelfTask shelfTask, StoragePositionVo storagePositionVo) {
        // sap调拨
        Map<String,String> map = new HashMap<>();
        map.put("factoryCode", detailDto.getFactoryCode());
        map.put("materialNo", shelfTask.getMaterialNo());
        map.put("lotNo", shelfTask.getLot());
        map.put("sourceLocation",shelfTask.getSourceLocationCode());
//        map.put("comCode", SecurityUtils.getComCode());
        map.put("targetLocation", shelfTask.getLocationCode());
        map.put("qty", com.weifu.cloud.common.core.text.Convert.toStr(shelfTask.getQty()));
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
        return com.alibaba.fastjson2.JSON.parseObject(res.get("data").toString(), String.class);
    }

    /**
     * 添加移库记录
     * @param shelfTask
     * @param storagePositionVo
     * @return
     */
    private String addStockMove(StockInStdOrderDetail detailDto, ShelfTask shelfTask, StoragePositionVo storagePositionVo) {
        StockMoveDto stockMoveDto = new StockMoveDto();
        stockMoveDto.setOrderNo(shelfTask.getStockinOrderNo());
        stockMoveDto.setFactoryCode(storagePositionVo.getFactoryCode());
        stockMoveDto.setFromLocationCode(shelfTask.getSourceLocationCode());
        stockMoveDto.setFromPositionCode(shelfTask.getSourcePositionNo());
        stockMoveDto.setTargetLocationCode(shelfTask.getLocationCode());
        stockMoveDto.setTargetPositionCode(shelfTask.getPositionNo());
        stockMoveDto.setOrderType("1");
        stockMoveDto.setMaterialName(shelfTask.getMaterialName());
        stockMoveDto.setMaterialNo(shelfTask.getMaterialNo());
        stockMoveDto.setQty(shelfTask.getQty());
        stockMoveDto.setOrderStatus(OrderStatusConstant.ORDER_STATUS_NEW);
        InventoryDetails inventoryDetails = new InventoryDetails();
        inventoryDetails.setTenantId(shelfTask.getTenantId());
        inventoryDetails.setMaterialNo(shelfTask.getMaterialNo());
        inventoryDetails.setLocationCode(shelfTask.getSourceLocationCode());
        inventoryDetails.setIsQc(detailDto.getIsQc());
        inventoryDetails.setStockInLot(detailDto.getLotNo());
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
        stockMoveDto.setCreateBy(shelfTask.getCreateBy());
        stockMoveDto.setCreateTime(DateUtils.getNowDate());
        stockMoveDto.setUpdateTime(DateUtils.getNowDate());

        AjaxResult ajaxResult = inStoreService.addStockMove(stockMoveDto);
        if ("".equals(Objects.toString(ajaxResult.get("data"), "")) || ajaxResult.isError()) {
            throw new ServiceException(Objects.toString(ajaxResult.get("msg").toString(), "调用库内作业服务失败"));
        }
        // 添加移库记录

        StockMoveLog stockMoveLog = new StockMoveLog();
        stockMoveLog.setMaterialName(shelfTask.getMaterialName());
        stockMoveLog.setMaterialNo(shelfTask.getMaterialNo());
        stockMoveLog.setLot(shelfTask.getLot());
        stockMoveLog.setOldLocationCode(shelfTask.getSourceLocationCode());
        stockMoveLog.setOldAreaCode(shelfTask.getSourceAreaCode());
        stockMoveLog.setOldPositionNo(shelfTask.getSourcePositionNo());
        stockMoveLog.setFactoryCode(storagePositionVo.getFactoryCode());
        stockMoveLog.setNewPositionCode(shelfTask.getPositionNo());
        stockMoveLog.setNewLocationCode(shelfTask.getLocationCode());
        stockMoveLog.setNewAreaCode(shelfTask.getAreaCode());
        stockMoveLog.setMoveQty(shelfTask.getQty());
        stockMoveLog.setContainerNo(shelfTask.getContainerNo());
        stockMoveLog.setTenantId(storagePositionVo.getTenantId());
        stockMoveLog.setCreateBy(storagePositionVo.getCreateBy());
        stockMoveLog.setCreateTime(DateUtils.getNowDate());
        stockMoveLog.setUpdateTime(DateUtils.getNowDate());
        inStoreService.addStockMoveLog(stockMoveLog);
        return com.alibaba.fastjson2.JSON.parseObject(JSON.toJSONString(ajaxResult.get("data")), String.class);
    }

    /**
     * 查询物料基础信息库存地点-区域-仓位
     * @param materialNo
     * @param locationCode
     * @param tenantId
     */
    public StoragePositionVo getLocationAndAreaAndPositionInfo(String materialNo, String locationCode, Long tenantId) {
        StoragePositionVo result = new StoragePositionVo();
        WmsMaterialBasicDto dto = new WmsMaterialBasicDto();
        List<String> materialNoList = new ArrayList<>();
        materialNoList.add(materialNo);
        dto.setMaterialNoList(materialNoList);
        AjaxResult materialInfo = mainDataService.getMaterialArrInfo(dto);

        if (materialInfo.isError()) {
            throw new ServiceException(materialInfo.get("msg").toString());
        }
        List<WmsMaterialAttrParamsDto> materialAttrInfo = JSON.parseArray(JSON.toJSONString(materialInfo.get("data")), WmsMaterialAttrParamsDto.class);
        if(ObjectUtils.isEmpty(materialAttrInfo)) {
            log.info("{},“物料基础信息-物料属性”不存在", materialNo);
            throw new ServiceException(materialNo + "“物料基础信息-物料属性”不存在");
        }

        // 仓位区域关联
        String section = materialAttrInfo.get(0).getSection();

        if(ObjectUtils.isEmpty(section)) {
            throw new ServiceException(materialNo + "默认区域不存在, 请维护相关数据");
        }

        AjaxResult ajaxResult1 = mainDataService.selectStorageAreaByDefaultReceiveLocationAndSection(locationCode, section, tenantId);

        if(ObjectUtils.isEmpty(ajaxResult1) || ObjectUtils.isEmpty(ajaxResult1.get("data"))) {
            throw new ServiceException(String.format("物料:%s,库存地点:%s,关联section:”%s“区域失败", materialNo,locationCode, section));
        }
        if(ajaxResult1.isError()) {
            throw new ServiceException(ajaxResult1.get("msg").toString());
        }
        // 区域
        StorageArea storageArea = JSON.parseObject(JSON.toJSONString(ajaxResult1.get("data")), StorageArea.class);
        if(ObjectUtils.isEmpty(storageArea)) {
            throw new ServiceException(String.format("物料:%s,库存地点:%s,关联section:”%s“区域失败", materialNo,locationCode, section));
        }

        // 仓位 (先取一条)
        AjaxResult storagePositionInfo = mainDataService.getPositionInfoByAreaCode(storageArea.getAreaCode());

        if(CollectionUtils.isEmpty(storagePositionInfo)) {
            throw new ServiceException(String.format("物料:%s,库存地点:%s,关联section:”%s“区域:”%s“获取仓位失败, 请维护相关数据", materialNo,locationCode, section, storageArea.getAreaCode()));
        }
        List<StoragePositionVo> storagePositionVoList = JSON.parseArray(JSON.toJSONString(storagePositionInfo.get("data")), StoragePositionVo.class);

        if(CollectionUtils.isEmpty(storagePositionVoList)) {
            throw new ServiceException(String.format("物料:%s,库存地点:%s,关联section:”%s“区域:”%s“获取仓位失败, 请维护相关数据", materialNo,locationCode, section, storageArea.getAreaCode()));
        }

        String positionNo = storagePositionVoList.get(0).getPositionNo();
        result.setLocationCode(locationCode);
        result.setAreaCode(storageArea.getAreaCode());
        result.setPositionNo(positionNo);
        return result;
    }


    /**
     * @Description: 质检释放(解Q)
     * @param materialNo  物料号
     * @param plantCode  工厂
     * @param supplierCode 供应商
     * @param qty 数量
     * @param unit 单位
     * @param locationCode 库存地点
     * @param moveReason 移动原因
     * @param proLot 批次
     * @param isConsign 是否委托
     * @return: void
     * @Author: fangshucheng
     * @Date: 2023/4/1
     */
    public String inspectReleaseSendSap(String materialNo, String plantCode, String supplierCode, BigDecimal qty, String unit,
                                        String locationCode, String moveReason, String proLot, String isConsign) throws Exception {
        String voucherNo = "";
        IMsgObject reqMo = new MsgObject(IMsgObject.MOType.initSR);
        List<GroupRecord> list = new ArrayList<>();
        GroupRecord groupRecord = new GroupRecord();
        groupRecord.setName("IS_HEAD");
        groupRecord.setFieldValue("BUDAT", DateUtils.dateTime());
        groupRecord.setFieldValue("BLDAT", DateUtils.dateTime());
        groupRecord.setFieldValue("BKTXT", SecurityUtils.getUsername());
        groupRecord.setFieldValue("UNAME", SecurityUtils.getUsername());

        GroupRecord itemRecord = new GroupRecord();
        itemRecord.setName("IT_ITEM");
        // 物料编号
        itemRecord.setFieldValue("MATNR", materialNo);
        // 工厂
        itemRecord.setFieldValue("PLANT", plantCode);
        // 批次
        itemRecord.setFieldValue("BATCH", proLot);
        // 移动类型(库存管理)
        itemRecord.setFieldValue("BWART", "321");
        // 存储地点
        itemRecord.setFieldValue("LGORT", locationCode);
        // 特殊库存标识
        itemRecord.setFieldValue("SOBKZ", SapReqOrResConstant.SPECIAL_MAP.get(isConsign));
        // 供应商帐户号
        itemRecord.setFieldValue("LIFNR", supplierCode);
        // 以录入项单位表示的数量
        itemRecord.setFieldValue("ERFMG",Convert.toStr(Convert.toInt(qty)));
        // ERFME条目单位
        itemRecord.setFieldValue("ERFME", unit);
        // MOVE_REAS 移动原因
        itemRecord.setFieldValue("MOVE_REAS", moveReason);
        // 标识: 未创建转移要求
        itemRecord.setFieldValue("NO_TRANSFER_REQ", "X");

        list.add(groupRecord);
        list.add(itemRecord);
        reqMo.setReqGroupRecord(list);

        byte[] result = remoteEsbSendService.sendToEsb(EsbSendSapConstant.Z_IWMS_QUALITY, reqMo.toString().getBytes());
        MsgObject resMo = new MsgObject(result, IMsgObject.MOType.initSR);
        String errorMsg = resMo.getResValue("ERRORMSG");
        String flag = resMo.getResValue("FLAG");
        if (!SapReqOrResConstant.SAP_RETURN_TYPE.equals(flag)) {
            log.error(errorMsg);
            throw new ServiceException("质检释放/让步接收失败!" + errorMsg);
        }
        // 凭证号
        voucherNo = resMo.getResValue("MBLNR");
        return voucherNo;
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
                               String voucherNo){
        //同步添加任务记录
        TaskLog taskLog = new TaskLog();
        taskLog.setTaskNo(taskInfoVo.getTaskNo());
        taskLog.setTaskType(taskInfoVo.getTaskType());
        taskLog.setMaterialNo(taskInfoVo.getMaterialNo());
        taskLog.setMaterialName(taskInfoVo.getMaterialName());
        taskLog.setOldMaterialNo(taskInfoVo.getOldMaterialNo());
        taskLog.setLot(taskInfoVo.getLotNo());
        taskLog.setQty(taskInfoVo.getPlanRecivevQty());
        taskLog.setUnit(taskInfoVo.getUnit());
        taskLog.setFactoryCode(storagePositionVo.getFactoryCode());
        if (!ObjectUtils.isEmpty(taskInfoDto.getSourceLocationCode())) {
            taskLog.setLocationCode(taskInfoDto.getSourceLocationCode());
        }
        taskLog.setTargetLocationCode(storagePositionVo.getLocationCode());
        taskLog.setTargetAreaCode(storagePositionVo.getAreaCode());
        taskLog.setTargetPositionCode(storagePositionVo.getPositionNo());
        taskLog.setOrderNo(taskInfoVo.getStockInOrderNo());
        taskLog.setOrderType(taskInfoVo.getTaskType());
        taskLog.setSupplierName(taskInfoDto.getVendorName());
        taskLog.setSupplierCode(taskInfoDto.getVendorCode());
        taskLog.setIsFreeze(CommonYesOrNo.NO);
        taskLog.setIsConsign(CommonYesOrNo.NO);
        taskLog.setIsQc(CommonYesOrNo.NO);
        taskLog.setMaterialCertificate(voucherNo);
        if (taskService.add(taskLog).isError()) {
            // 物料凭证冲销
            if(!StringUtils.isBlank(voucherNo)){
                materialReversal(voucherNo);
            }
            throw new ServiceException("新增任务记录失败！");
        }
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

    @Override
    @GlobalTransactional
    public int generateInspectTask(InspectOrderDto inspectOrderDto) throws Exception {
        StockInStdOrder stockInStdOrder = stockInStdOrderMapper.selectStockInStdOrderByNo(inspectOrderDto.getPurchaseOrderNo());
        if(ObjectUtils.isEmpty(stockInStdOrder)) {
            throw new ServiceException("标准入库单不存在！");
        }

        StockInStdOrderDetailDto detailDto = new StockInStdOrderDetailDto();
        detailDto.setPurchaseOrderNo(inspectOrderDto.getPurchaseOrderNo());
        detailDto.setPurchaseLineNo(inspectOrderDto.getPurchaseLineNo());
        List<StockInStdOrderDetail> stdOrderDetailList = stockInStdOrderDetailMapper.selectStockInStdOrderDetailList(detailDto);

        if (CollectionUtils.isEmpty(stdOrderDetailList)) {
            throw new ServiceException("标准入库单明细不存在");
        }
        StockInStdOrderDetail stockInStdOrderDetail = stdOrderDetailList.get(0);

        // 获取收货数量
        BigDecimal receiveQty = stockInStdOrderDetail.getReceivedQty();

        if (!ObjectUtils.isEmpty(receiveQty) && receiveQty.intValue() > 0) {
            // 获取物料属性
            WmsMaterialDeliverAttr dto = new WmsMaterialDeliverAttr();
            dto.setMaterialNo(inspectOrderDto.getMaterialNo());
            AjaxResult deliverAttrAjaxResult = mainDataService.getDeliverAttr(dto);

            if (deliverAttrAjaxResult.isError()) {
                throw new ServiceException(deliverAttrAjaxResult.get("msg").toString());
            }
            List<WmsMaterialDeliverAttr> deliverAttrList = JSON.parseArray(deliverAttrAjaxResult.get("data").toString(), WmsMaterialDeliverAttr.class);

            if(CollectionUtils.isEmpty(deliverAttrList)) {
                throw new ServiceException(String.format("物料%s的包装属性信息不存在,请维护相关数据", inspectOrderDto.getMaterialNo()));
            }
            // 送检部分移库 从1001送检至检验区5000
            // 生成质检任务
            addInspectTask(inspectOrderDto, stockInStdOrderDetail, deliverAttrList);

            // 生成上架任务
            addShelfTask(inspectOrderDto, stockInStdOrderDetail, deliverAttrList);
        }

        inspectOrderDto.setIsShelf(CommonYesOrNo.YES);
        inspectOrderDto.setLocationCode(SQConstant.SQ_LOCATION_CODE);
        inspectOrderDto.setBillStatus(InspectOrderStatusConstant.ORDER_STATUS_INSPECTION);

        // 如果数量修改, 更新送检单
        inspectOrderMapper.updateInspectOrder(inspectOrderDto);
        return HttpStatus.SC_OK;
    }

    private void addShelfTask(InspectOrderDto inspectOrderDto, StockInStdOrderDetail stockInStdOrderDetail,
                              List<WmsMaterialDeliverAttr> deliverAttrList) {
        Long tenantId = SecurityUtils.getLoginUser().getTenantId();
        // 手工填写送检数量，激活后-其余部分生成上架任务；
        BigDecimal moveQty = stockInStdOrderDetail.getReceivedQty().subtract(inspectOrderDto.getQty());
        // 上架任务根据每托数量拆分
        BigDecimal palletQty = deliverAttrList.get(0).getPalletQty();
        BigDecimal taskQty = moveQty.divide(palletQty, 0, RoundingMode.CEILING); // 总任务数
        log.info("完成任务总数量：{}", taskQty);

        List<ShelfTask> shelfTaskList = new ArrayList<>();
        // 上架任务
        for (int i = 0; i < taskQty.intValue(); i++) {
            // 上架任务号
            AjaxResult result = iCodeRuleService.getSeqWithTenantId(TaskNoTypeConstant.SHELF_TASK, String.valueOf(tenantId));
            if (result.isError() || StringUtils.isEmpty(result.get("data").toString())){
                throw new ServiceException("上架任务号生成失败！");
            }
            String taskNo = result.get("data").toString();

            // 获取库存地点策略
            AjaxResult targetStoragePositionResult = mainDataService.getPositionCode(stockInStdOrderDetail.getMaterialNo());
            if(targetStoragePositionResult.isError()) {
                throw new ServiceException(String.format("物料:%s,库存地点策略获取失败", stockInStdOrderDetail.getMaterialNo()));
            }
            StoragePositionVo targetStoragePositionVo = JSON.parseObject(JSON.toJSONString(targetStoragePositionResult.get("data")), StoragePositionVo.class);

            ShelfTask shelfTask = new ShelfTask();
            shelfTask.setTaskNo(taskNo);
            shelfTask.setTaskType(TaskLogTypeConstant.SHELF);
            shelfTask.setStatus(TaskStatusConstant.TASK_STATUS_NEW);
            shelfTask.setAllocateTime(DateUtils.getNowDate());
            shelfTask.setMaterialNo(inspectOrderDto.getMaterialNo());
            shelfTask.setMaterialName(inspectOrderDto.getMaterialName());
            shelfTask.setOldMaterialNo(deliverAttrList.get(0).getOldMaterialNo());
            shelfTask.setStockinOrderNo(stockInStdOrderDetail.getPurchaseOrderNo());
            shelfTask.setStockinLineNo(stockInStdOrderDetail.getPurchaseLineNo());
            shelfTask.setSourcePositionNo(inspectOrderDto.getPositionNo());
            shelfTask.setSourceLocationCode(stockInStdOrderDetail.getLocationCode());
            shelfTask.setLocationCode(targetStoragePositionVo.getLocationCode());
            shelfTask.setPositionNo(targetStoragePositionVo.getPositionNo());
            shelfTask.setPositionId(targetStoragePositionVo.getId());
            shelfTask.setContainerNo(stockInStdOrderDetail.getWbsElement());
            shelfTask.setQty(palletQty);
            shelfTask.setOperationQty(palletQty);
            shelfTask.setUnit(stockInStdOrderDetail.getUnit());
            shelfTask.setOperationUnit(stockInStdOrderDetail.getOperationUnit());
            shelfTask.setCompleteQty(BigDecimal.ZERO);
            shelfTask.setOperationCompleteQty(BigDecimal.ZERO);

            String LotNo = inspectOrderDto.getLot();
            if (StringUtil.isEmpty(LotNo)) {
                LotNo = stockInStdOrderDetail.getLotNo();
            }
            shelfTask.setLot(LotNo);
            // 最后一条数量取余数
            if(i == taskQty.intValue() - 1) {
                BigDecimal qty = moveQty.remainder(palletQty);
                if (qty.compareTo(BigDecimal.ZERO) > 0) {
                    shelfTask.setQty(qty);
                    shelfTask.setOperationQty(qty);
                }
            }

            shelfTask.setTenantId(tenantId);
            shelfTask.setCreateBy(SecurityUtils.getUsername());
            shelfTask.setCreateTime(DateUtils.getNowDate());
            shelfTask.setUpdateBy(SecurityUtils.getUsername());
            shelfTask.setUpdateTime(DateUtils.getNowDate());
            shelfTaskList.add(shelfTask);
        }
        // 批量插入任务列表
        shelfTaskService.insertShelfTaskList(shelfTaskList);
        log.info("完成上架任务数据保存");

        // 打印上架任务列表生成TO单
        List<Long> shelfIdList = shelfTaskList.stream().map(ShelfTask::getId).collect(Collectors.toList());
        shelfTaskService.printList(stockInStdOrderDetail.getLocationCode(), shelfIdList);
        log.info("完成上架任务标签打印");
    }

    private void addInspectTask(InspectOrderDto inspectOrderDto, StockInStdOrderDetail stockInStdOrderDetail,
                                List<WmsMaterialDeliverAttr> deliverAttrList) throws Exception {
        Long tenantId = SecurityUtils.getLoginUser().getTenantId();
        // 获取抽检数量
        BigDecimal inspectQty = inspectOrderDto.getQty();
        // 根据每箱数量拆分
        BigDecimal cartonQty = deliverAttrList.get(0).getCartonQty();
        // 送检任务数量
        BigDecimal inspectTaskQty = inspectQty.divide(cartonQty, 0, RoundingMode.CEILING); // 总任务数

        List<SysDictData> defPositionNo = DictUtils.getDictCache("location_default_bin");
        if (ObjectUtils.isEmpty(defPositionNo) || defPositionNo.isEmpty()) {
            throw new ServiceException("获取默认仓位字典类型失败");
        }

        SysDictData sysDictData = null ;
         Optional<SysDictData> first = defPositionNo.stream().filter(x -> SQConstant.SQ_LOCATION_CODE.equals(x.getDictLabel())).findFirst();
         if (!first.isPresent()){
             throw new ServiceException("未发现 质检区的仓位；请核实仓位字典！！！");
         }
        sysDictData = first.get();
        for (int i = 0; i < inspectTaskQty.intValue(); i++) {
            //获取检验任务号
            AjaxResult result = iCodeRuleService.getSeqWithTenantId(TaskNoTypeConstant.DELIVERY_INSPECTION_TASK, String.valueOf(tenantId));
            if (result.isError() || StringUtils.isEmpty(result.get("data").toString())){
                throw new ServiceException("检验任务号生成失败！");
            }
            String taskNo = result.get("data").toString();
            //生成质检任务
            DeliveryInspectionTask deliveryInspectionTask = new DeliveryInspectionTask();
            deliveryInspectionTask.setDeliveryTime(DateUtils.getNowDate());
            String LotNo = inspectOrderDto.getLot();
            if (StringUtil.isEmpty(LotNo)) {
                LotNo = stockInStdOrderDetail.getLotNo();
            }
            deliveryInspectionTask.setLot(LotNo);
            deliveryInspectionTask.setMaterialNo(inspectOrderDto.getMaterialNo());
            deliveryInspectionTask.setMaterialName(inspectOrderDto.getMaterialName());
            deliveryInspectionTask.setQty(inspectOrderDto.getQty());
            deliveryInspectionTask.setTaskNo(taskNo);
            deliveryInspectionTask.setStatus(TaskStatusConstant.TASK_STATUS_COMPLETE);
            deliveryInspectionTask.setTaskType(TaskTypeConstant.DELIVERY_INSPECTION_TASK);
            deliveryInspectionTask.setCreateBy(SecurityUtils.getUsername());
            deliveryInspectionTask.setTenantId(tenantId);
            deliveryInspectionTask.setCreateTime(DateUtils.getNowDate());
            deliveryInspectionTask.setInspectOrderNo(inspectOrderDto.getOrderNo());
            deliveryInspectionTask.setInspectOrderId(inspectOrderDto.getId());
            deliveryInspectionTask.setLocationCode(SQConstant.SQ_LOCATION_CODE);
            deliveryInspectionTask.setPositionNo(sysDictData.getDictValue());
            deliveryInspectionTaskMapper.insertDeliveryInspectionTask(deliveryInspectionTask);

            String sourceLocation = inspectOrderDto.getLocationCode();
            String targetLocation = SQConstant.SQ_LOCATION_CODE;
            syncSapAndUpdateInventory(inspectOrderDto, stockInStdOrderDetail, sourceLocation, targetLocation, taskNo);
        }
    }

    private void syncSapAndUpdateInventory(InspectOrderDto inspectOrderDto, StockInStdOrderDetail stockInStdOrderDetail,
                                           String sourceLocation, String targetLocation, String taskNo) {
        // 获取物料默认单位
//        MaterialAttrDto materialAttrDto = new MaterialAttrDto();
//        materialAttrDto.setMaterialNo(inspectOrderDto.getMaterialNo());
//        List<MaterialMainDto> materialList = esbSendCommonMapper.getMaterialMain(materialAttrDto);
//        if(CollectionUtils.isEmpty(materialList)){
//            throw new ServiceException(String.format("物料号：%s，获取默认单位为空！", inspectOrderDto.getMaterialNo()));
//        }
      //  String defaultUnit = materialList.get(0).getDefaultUnit();

        StoragePositionVo storagePositionVo = new StoragePositionVo();
        storagePositionVo.setPositionNo(inspectOrderDto.getPositionNo());
        storagePositionVo.setLocationCode(inspectOrderDto.getLocationCode());
        List<String> checkLocationList = Arrays.asList(SQConstant.SQ_LOCATION_CODE,
                SQConstant.PASS_LOCATION_CODE, SQConstant.FAIL_LOCATION_CODE);
        //默认仓位字典
        List<SysDictData> defPositionNo = DictUtils.getDictCache("location_default_bin");
        if (ObjectUtils.isEmpty(defPositionNo) || defPositionNo.isEmpty()) {
            throw new ServiceException("获取默认仓位字典类型失败");
        }

        if (checkLocationList.contains(sourceLocation)) {
            AjaxResult locationResult =  mainDataService.getStorageLocationByLocationCode(sourceLocation);

            if (locationResult.isError()) {
                throw new ServiceException(Objects.toString(locationResult.get("msg"),"获取原库存地点失败"));
            }
            StorageLocationDto sourceLocationDto = JSON.parseObject(String.valueOf(locationResult.get("data")), StorageLocationDto.class);

            defPositionNo.forEach(e->{
                if(e.getDictLabel().equals(sourceLocation)){
                    storagePositionVo.setPositionNo(e.getDictValue());
                    storagePositionVo.setLocationCode(sourceLocation);
                    storagePositionVo.setAreaCode(sourceLocationDto.getAreaCode());
                    storagePositionVo.setWarehouseCode(sourceLocationDto.getWarehouseCode());
                    storagePositionVo.setWarehouseId(sourceLocationDto.getWarehouseId());
                    storagePositionVo.setFactoryId(sourceLocationDto.getFactoryId());
                    storagePositionVo.setFactoryCode(sourceLocationDto.getFactoryCode());
                    storagePositionVo.setFactoryName(sourceLocationDto.getFactoryName());
                }
            });
        }

        StoragePositionVo targetStoragePositionVo = new StoragePositionVo();
        if (checkLocationList.contains(targetLocation)) {
            AjaxResult targetLocationResult =  mainDataService.getStorageLocationByLocationCode(targetLocation);

            if (targetLocationResult.isError()) {
                throw new ServiceException(Objects.toString(targetLocationResult.get("msg"),"获取目的库存地点失败"));
            }
            StorageLocationDto targetLocationDto = JSON.parseObject(String.valueOf(targetLocationResult.get("data")), StorageLocationDto.class);

            StoragePositionVo finalTargetStoragePositionVo = targetStoragePositionVo;
            defPositionNo.forEach(e->{
                if (e.getDictLabel().equals(targetLocation)) {
                    finalTargetStoragePositionVo.setPositionNo(e.getDictValue());
                    finalTargetStoragePositionVo.setLocationCode(targetLocation);
                    finalTargetStoragePositionVo.setAreaCode(targetLocationDto.getAreaCode());
                    finalTargetStoragePositionVo.setWarehouseCode(targetLocationDto.getWarehouseCode());
                    finalTargetStoragePositionVo.setWarehouseId(targetLocationDto.getWarehouseId());
                    finalTargetStoragePositionVo.setFactoryId(targetLocationDto.getFactoryId());
                    finalTargetStoragePositionVo.setFactoryCode(targetLocationDto.getFactoryCode());
                    finalTargetStoragePositionVo.setFactoryName(targetLocationDto.getFactoryName());
                }
            });
        } else {
            // 注意：如果目标仓位为特殊仓位 则不走寻址策略
            AjaxResult targetPositionResult = mainDataService.getPositionCodeByLocationCodeAndMaterialNo(
                    targetLocation, inspectOrderDto.getMaterialNo());
            if (targetPositionResult.isError()) {
                throw new ServiceException(Objects.toString(targetPositionResult.get("msg"),"获取仓位失败"));
            }
            targetStoragePositionVo = JSON.parseObject(JSON.toJSONString(targetPositionResult.get("data")), StoragePositionVo.class);
        }
        // 更新台账并插入移动记录
        updateInventoryDetails(inspectOrderDto, stockInStdOrderDetail, targetStoragePositionVo);
        log.info("更新台账成功");

        // 记录移动记录
        addTaskLog(inspectOrderDto, stockInStdOrderDetail, storagePositionVo, targetStoragePositionVo, taskNo);
        log.info("记录移动记录");

        // 打印标签=>在缓冲区记录检验单号
        printTaskInfo(inspectOrderDto, targetStoragePositionVo, storagePositionVo,stockInStdOrderDetail);
        log.info("打印标签成功");

        //SAP过账
        syncToSap(inspectOrderDto, stockInStdOrderDetail, sourceLocation, targetLocation, stockInStdOrderDetail.getUnit(),taskNo);
        log.info("SAP过账成功");
    }

    private void updateInventoryDetails(InspectOrderDto inspectOrderDto, StockInStdOrderDetail stockInStdOrderDetail,
                                        StoragePositionVo targetStoragePositionVo) {
        InventoryDetailsVo source = new InventoryDetailsVo();
        source.setIsQc(stockInStdOrderDetail.getIsQc());
        source.setIsFreeze(CommonYesOrNo.NO);
        source.setIsConsign(CommonYesOrNo.NO);
        source.setIsReserved(CommonYesOrNo.NO);
        // 过滤特殊库存
        source.setStockSpecFlag(CommonYesOrNo.NO);
        String LotNo = inspectOrderDto.getLot();
        if (StringUtil.isEmpty(LotNo)) {
            LotNo = stockInStdOrderDetail.getLotNo();
        }
        source.setStockInLot(LotNo);
        source.setMaterialNo(inspectOrderDto.getMaterialNo());
        source.setLocationCode(inspectOrderDto.getLocationCode());
        //查询并修改库存台账记录
        AjaxResult inventoryDetailsResult = inStoreService.list(source);
        if (inventoryDetailsResult.isError() || StringUtils.isEmpty(inventoryDetailsResult.get("data").toString())){
            throw new ServiceException("库存台账查询失败！");
        }
        List<InventoryDetails> inventoryDetails = JSON.parseArray(inventoryDetailsResult.get("data").toString(),InventoryDetails.class);
        if (ObjectUtils.isEmpty(inventoryDetails)){
            throw new ServiceException("查询源库存台账记录不存在！");
        }

        BigDecimal qcQty = inspectOrderDto.getQty();
        // 台账减库存，可用数量
        InventoryDetails inventoryDetail = inventoryDetails.get(0);
        Date productTime = inventoryDetail.getProductTime();
        Date expiryDate = inventoryDetail.getExpiryDate();
        BigDecimal inventoryQty = qcQty.subtract(inventoryDetail.getInventoryQty());
        if (inventoryQty.compareTo(BigDecimal.ZERO) > 0) {
            inventoryDetail.setInventoryQty(new BigDecimal("0"));
        } else {
            inventoryDetail.setInventoryQty(inventoryQty.abs());
        }
        BigDecimal availableQty = qcQty.subtract(inventoryDetail.getAvailableQty());
        if (availableQty.compareTo(BigDecimal.ZERO) > 0) {
            inventoryDetail.setAvailableQty(new BigDecimal("0"));
        } else {
            inventoryDetail.setAvailableQty(availableQty.abs());
        }
        // 扣为0后删除台账
        if (inventoryDetail.getInventoryQty().compareTo(BigDecimal.ZERO) == 0) {
            inStoreService.deleteInventory(Collections.singletonList(inventoryDetail.getId()));
        } else {
            inStoreService.updateWmsInventoryDetails(inventoryDetail);
        }

        // 5000，5050,5060，5080封存区，1001库存地点配置字典，不需要推荐
        InventoryDetailsVo target = new InventoryDetailsVo();
        target.setIsQc(stockInStdOrderDetail.getIsQc());
        target.setIsFreeze(CommonYesOrNo.NO);
        target.setIsConsign(CommonYesOrNo.NO);
        target.setIsReserved(CommonYesOrNo.NO);
        target.setStockInLot(stockInStdOrderDetail.getLotNo());
        target.setMaterialNo(inspectOrderDto.getMaterialNo());
        target.setLocationCode(targetStoragePositionVo.getLocationCode());
        target.setPositionNo(targetStoragePositionVo.getPositionNo());
        //查询并修改库存台账记录
        inventoryDetailsResult = inStoreService.list(target);
        if (inventoryDetailsResult.isError() || StringUtils.isEmpty(inventoryDetailsResult.get("data").toString())){
            throw new ServiceException("库存台账查询失败！");
        }
        inventoryDetails = JSON.parseArray(inventoryDetailsResult.get("data").toString(),InventoryDetails.class);

        if (ObjectUtils.isEmpty(inventoryDetails)){
            //新增台账
            InventoryDetails newInventoryDetails = new InventoryDetails();
            newInventoryDetails.setPositionNo(targetStoragePositionVo.getPositionNo());
            newInventoryDetails.setPositionId(targetStoragePositionVo.getId());
            newInventoryDetails.setAreaId(targetStoragePositionVo.getAreaId());
            newInventoryDetails.setAreaCode(targetStoragePositionVo.getAreaCode());
            newInventoryDetails.setLocationId(targetStoragePositionVo.getLocationId());
            newInventoryDetails.setLocationCode(targetStoragePositionVo.getLocationCode());
            newInventoryDetails.setFactoryId(targetStoragePositionVo.getFactoryId());
            newInventoryDetails.setFactoryCode(targetStoragePositionVo.getFactoryCode());
            newInventoryDetails.setFactoryName(targetStoragePositionVo.getFactoryName());
            newInventoryDetails.setWarehouseId(targetStoragePositionVo.getWarehouseId());
            newInventoryDetails.setWarehouseCode(targetStoragePositionVo.getWarehouseCode());
            newInventoryDetails.setMaterialNo(stockInStdOrderDetail.getMaterialNo());
            newInventoryDetails.setMaterialName(stockInStdOrderDetail.getMaterialName());
            newInventoryDetails.setOldMaterialNo(stockInStdOrderDetail.getOldMaterialNo());
            newInventoryDetails.setProductionLot(LotNo);
            newInventoryDetails.setStockInLot(LotNo);
            newInventoryDetails.setOperationUnit(stockInStdOrderDetail.getUnit());
            newInventoryDetails.setUnit(stockInStdOrderDetail.getUnit());
            newInventoryDetails.setConversDefault(stockInStdOrderDetail.getConversDefault());
            newInventoryDetails.setIsConsign(stockInStdOrderDetail.getIsConsign());
            BigDecimal receivedQty = inspectOrderDto.getQty();
            newInventoryDetails.setInventoryQty(receivedQty);
            newInventoryDetails.setAvailableQty(receivedQty);
            newInventoryDetails.setPreparedQty(BigDecimal.ZERO);
            newInventoryDetails.setOperationPreparedQty(BigDecimal.ZERO);

            StockInStdOrder stockInStdOrder = stockInStdOrderMapper.selectStockInStdOrderByNo(stockInStdOrderDetail.getPurchaseOrderNo());
            newInventoryDetails.setSupplierId(stockInStdOrder.getVendorId());
            newInventoryDetails.setSupplierCode(stockInStdOrder.getVendorCode());
            newInventoryDetails.setSupplierName(stockInStdOrder.getVendorName());
            newInventoryDetails.setStockInDate(DateUtils.getNowDate());
            newInventoryDetails.setIsQc(stockInStdOrderDetail.getIsQc());
            newInventoryDetails.setIsReserved(CommonYesOrNo.NO);
            newInventoryDetails.setIsConsign(CommonYesOrNo.NO);
            newInventoryDetails.setIsFreeze(CommonYesOrNo.NO);
            newInventoryDetails.setStockSpecFlag(CommonYesOrNo.NO);
            newInventoryDetails.setCreateBy(SecurityUtils.getUsername());
            newInventoryDetails.setCreateTime(DateUtils.getNowDate());
            newInventoryDetails.setUpdateBy(SecurityUtils.getUsername());
            newInventoryDetails.setUpdateTime(DateUtils.getNowDate());
            newInventoryDetails.setProductTime(productTime);
            newInventoryDetails.setExpiryDate(expiryDate);
            if (ObjectUtils.isEmpty(SecurityUtils.getLoginUser())
                    || ObjectUtils.isEmpty(SecurityUtils.getLoginUser().getSysUser())) {
                newInventoryDetails.setTenantId(MainDataConstant.TENANT_ID);
            } else {
                newInventoryDetails.setTenantId(SecurityUtils.getLoginUser().getSysUser().getTenantId());
            }

            AjaxResult result = inStoreService.add(newInventoryDetails);
            if (result.isError()) {
                throw new ServiceException("新增库存台账失败！");
            }
        } else {
            InventoryDetails targetInventoryDetail = inventoryDetails.get(0);
            targetInventoryDetail.setInventoryQty(targetInventoryDetail.getInventoryQty().add(inspectOrderDto.getQcQty()));
            targetInventoryDetail.setAvailableQty(targetInventoryDetail.getAvailableQty().add(inspectOrderDto.getQcQty()));
            inStoreService.updateWmsInventoryDetails(targetInventoryDetail);
        }
    }

    private void printTaskInfo(InspectOrderDto inspectOrderDto, StoragePositionVo targetStoragePositionVo, StoragePositionVo storagePositionVo,StockInStdOrderDetail stockInStdOrderDetail) {
        PrintTOParamsDto printTOParamsDto = new PrintTOParamsDto();
        printTOParamsDto.setLot(inspectOrderDto.getLot());
        printTOParamsDto.setFactoryCode(inspectOrderDto.getFactoryCode());
        printTOParamsDto.setLocationCode(targetStoragePositionVo.getLocationCode());
        printTOParamsDto.setPositionNo(targetStoragePositionVo.getPositionNo());
        printTOParamsDto.setMoveQty(inspectOrderDto.getQty());
        printTOParamsDto.setPackQty(inspectOrderDto.getQty());
        printTOParamsDto.setMaterialName(inspectOrderDto.getMaterialName());
        printTOParamsDto.setMaterialNo(inspectOrderDto.getMaterialNo());
        printTOParamsDto.setTaskType("质检任务单");
        printTOParamsDto.setSourcePostionNo(storagePositionVo.getPositionNo());
        printTOParamsDto.setSourceLocationCode(storagePositionVo.getLocationCode());
        printTOParamsDto.setSourceStorageType(storagePositionVo.getLocationCode());
        printTOParamsDto.setTaskNo(inspectOrderDto.getTaskNo());
        printTOParamsDto.setInspectNo(inspectOrderDto.getOrderNo());
        printTOParamsDto.setOldMaterialNo(stockInStdOrderDetail.getOldMaterialNo());
        printService.printTO(printTOParamsDto);
    }

    private void syncToSap(InspectOrderDto inspectOrderDto, StockInStdOrderDetail stockInStdOrderDetail, String sourceLocation, String targetLocation, String defaultUnit,String taskNo) {
        // sap调拨过账 =》 sap发送过来的库存地点 =》 5000
        SapMoveLocationParamsDto sp = new SapMoveLocationParamsDto();
        Date now = new Date();
        sp.setBudat(DateFormatUtils.format(now, "yyyyMMdd"));
        sp.setBldat(DateFormatUtils.format(now, "yyyyMMdd"));
        sp.setuName(SecurityUtils.getUsername());
        sp.setBktxt("");

        sp.setPlant(stockInStdOrderDetail.getFactoryCode());
        sp.setMaterial(stockInStdOrderDetail.getMaterialNo());
        sp.setStgeLoc(sourceLocation);
        sp.setMoveType(SapReqOrResConstant.MOVE_LOCATION_MOVE_TYPE_Q);
        String LotNo = inspectOrderDto.getLot();
        if (StringUtil.isEmpty(LotNo)) {
            LotNo = stockInStdOrderDetail.getLotNo();
        }
        sp.setBatch(LotNo);
        sp.setSpecStock("");
        sp.setMovePlant(stockInStdOrderDetail.getFactoryCode());
        sp.setMoveMat(stockInStdOrderDetail.getMaterialNo());
        sp.setMoveStloc(targetLocation);
        sp.setEntryQnt(Convert.toStr(inspectOrderDto.getQty()));
        sp.setEntryUom(defaultUnit);
        sp.setEntryUomIso("");
        sp.setMoveBatch(stockInStdOrderDetail.getLotNo());
        sp.setNoTransferReq("");
        sp.setItemText(taskNo);
        try {
            sapInteractionService.moveLocationSap(sp);
        } catch (Exception e) {
            log.error("SAP接口调用异常，信息: {}", e.getMessage());
            throw new ServiceException("SAP接口调用异常，信息: " + e.getMessage());
        }
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
        inventoryDetailsVo.setFactoryCode(storagePositionVo.getFactoryCode());
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
        inventoryDetailsVo.setProductionLot(stdOrderDetail.getLotNo());
        AjaxResult listResult = inStoreService.list(inventoryDetailsVo);
        if (listResult.isError()) {
            throw new ServiceException("查询库存台账失败！");
        }
        BigDecimal receivedQty = stdOrderDetail.getReceivedQty();
        List<InventoryDetails> inventoryDetailsList = JSON.parseArray(listResult.get("data").toString(), InventoryDetails.class);
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
            inventoryDetails.setContainerNo(stdOrderDetail.getWbsElement());

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
            AjaxResult result = inStoreService.add(inventoryDetails);
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
            if (inStoreService.edit(inventoryDetails).isError()) {
                throw new ServiceException("修改库存台账失败！");
            }
        } else {
            throw new ServiceException("库存台账不唯一！");
        }
        return inventoryId;
    }

    public String moveLocationSap(InspectOrderDto inspectOrderDto, StockInStdOrderDetail stockInStdOrderDetail,
                                  String taskNo, String targetLocation) throws Exception {
        SapMoveLocationParamsDto sp = new SapMoveLocationParamsDto();
        Date now = new Date();
        sp.setBudat(DateFormatUtils.format(now, "yyyyMMdd"));
        sp.setBldat(DateFormatUtils.format(now, "yyyyMMdd"));
        sp.setBktxt("");
        sp.setuName(SecurityUtils.getUsername());
        sp.setPlant(inspectOrderDto.getFactoryCode());
        sp.setMaterial(inspectOrderDto.getMaterialNo());
        sp.setStgeLoc(inspectOrderDto.getLocationCode());
        sp.setMoveType(SapReqOrResConstant.MOVE_LOCATION_MOVE_TYPE);
        sp.setBatch(stockInStdOrderDetail.getLotNo());
        sp.setSpecStock("");
        sp.setVendor("");
        sp.setMovePlant(inspectOrderDto.getFactoryCode());
        sp.setMoveMat(inspectOrderDto.getMaterialNo());
        sp.setMoveStloc(targetLocation);
        sp.setMoveBatch(stockInStdOrderDetail.getLotNo());
        sp.setEntryQnt(String.valueOf(inspectOrderDto.getQty()));
        sp.setEntryUom(stockInStdOrderDetail.getUnit());
        sp.setNoTransferReq("");
        sp.setWbsElem("");
        sp.setNetWork("");
        sp.setItemText(taskNo);
        sp.setSalesOrd("");
        sp.setsOrdItem("");
        sp.setValSalesOrd("");
        return sapInteractionService.moveLocationSap(sp);
    }

    private void addTaskLog(InspectOrderDto inspectOrderDto, StockInStdOrderDetail stockInStdOrderDetail,
                            StoragePositionVo storagePositionVo, StoragePositionVo targetStoragePositionVo,
                            String taskNo) {
        // 新增任务记录
        TaskLog taskLog = new TaskLog();
        taskLog.setTaskNo(taskNo);
        taskLog.setFactoryCode(stockInStdOrderDetail.getFactoryCode());
        taskLog.setMaterialNo(stockInStdOrderDetail.getMaterialNo());
        taskLog.setMaterialName(stockInStdOrderDetail.getMaterialName());
        taskLog.setOldMaterialNo(stockInStdOrderDetail.getOldMaterialNo());
        String LotNo = inspectOrderDto.getLot();
        if (StringUtil.isEmpty(LotNo)) {
            LotNo = stockInStdOrderDetail.getLotNo();
        }
        taskLog.setLot(LotNo);
        taskLog.setQty(inspectOrderDto.getQty());
        taskLog.setOperationQty(stockInStdOrderDetail.getReceivedQty());
        taskLog.setLocationCode(storagePositionVo.getLocationCode());
        taskLog.setPositionCode(storagePositionVo.getPositionNo());
        taskLog.setTargetFactoryCode(targetStoragePositionVo.getFactoryCode());
        taskLog.setTargetLocationCode(targetStoragePositionVo.getLocationCode());
        taskLog.setTargetPositionCode(targetStoragePositionVo.getPositionNo());
        taskLog.setTaskType(TaskLogTypeConstant.DELIVERY_INSPECTION_TASK);
        taskLog.setOrderNo(stockInStdOrderDetail.getPurchaseOrderNo());
        taskLog.setSupplierCode(inspectOrderDto.getSupplierCode());
//        taskLog.setMaterialCertificate(String.join(",", voucherNoList));
        taskLog.setIsQc(stockInStdOrderDetail.getIsQc());
        taskLog.setIsConsign(stockInStdOrderDetail.getIsConsign());
        taskLog.setIsFreeze(CommonYesOrNo.NO);
        taskLog.setOrderType(TaskLogTypeConstant.DELIVERY_INSPECTION_TASK);
        taskLog.setUnit(stockInStdOrderDetail.getUnit());
        taskLog.setOperationUnit(stockInStdOrderDetail.getOperationUnit());
        if (ObjectUtils.isEmpty(SecurityUtils.getLoginUser())
                || ObjectUtils.isEmpty(SecurityUtils.getLoginUser().getSysUser())) {
            taskLog.setTenantId(MainDataConstant.TENANT_ID);
        } else {
            taskLog.setTenantId(SecurityUtils.getLoginUser().getSysUser().getTenantId());
        }
        if (taskService.add(taskLog).isError()) {
            throw new ServiceException("新增任务记录失败！");
        }
    }
    /**
     * 取消质检任务
     * @param orderNo
     */
    @Override
    public int cancelInspectTask(String orderNo) {

        InspectOrder inspectOrder = inspectOrderMapper.selectInspectOrderByNo(orderNo);

        if(ObjectUtils.isEmpty(inspectOrder)) {
            throw new ServiceException("质检单任务:" + orderNo + ", 不存在");
        }
        // 送检单合格, 不能送检
        if("0".equals(inspectOrder.getChkResult())) {
            throw new ServiceException("质检单任务:" + orderNo + ", 合格，不能取消");
        }
        // 取消质检单
        if (inspectOrder.getBillStatus().equals("4")) {
            throw new ServiceException("质检单状态已完成, 不能取消");
        }
        if (inspectOrder.getBillStatus().equals("0")) {
            IMsgObject msgObject = null;
            try {
                msgObject = new MsgObject(IMsgObject.MOType.initSP);

                msgObject.setReqValue("TaskCode", orderNo);
                byte[] result = remoteEsbSendService.sendToEsb(EsbSendSapConstant.WS_SEND_CANCEL_IQC, msgObject.getBytes());

                MsgObject resMo = new MsgObject(result, IMsgObject.MOType.initSR);
                log.info("resMo: {}", resMo);

                // 修改已关闭状态
                inspectOrder.setBillStatus("3");
                inspectOrderMapper.updateInspectOrder(inspectOrder);

                if ("FAIL".equals(resMo.getServResStatus())) {
                    throw new ServiceException(resMo.getServResDesc());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return 1;
    }


    @Override
    public int materialReversal(com.alibaba.fastjson.JSONObject jsonObject) {

        if (ObjectUtils.isEmpty(jsonObject)){
            return 0;
        }
        String voucherNo = jsonObject.getString("voucherNo");
        if (!StringUtils.isEmpty(voucherNo)) {
            materialReversal(voucherNo);
        }
        return 1;
    }


    /**
     * 打印检验单
     *
     * @param inspectOrderDto
     * @return
     */
    @Override
    public int printInspectTask(InspectOrderDto inspectOrderDto) {

        AjaxResult printerByLocationAjaxResult = printService.findPrinterByLocation(inspectOrderDto.getLocationCode());

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

        jsonObject.put("taskType", "成品退货任务");
        // 目标存储类型
        jsonObject.put("postionNo", inspectOrderDto.getPositionNo());
        // 工厂或库存地点
        jsonObject.put("factoryCodeOrLocationCode", String.format("%s/%s", inspectOrderDto.getFactoryCode(), inspectOrderDto.getLocationCode()));
        // 操作类型
//        jsonObject.put("operateType", stockInStdOrderDetails.get(0).getMoveType());
        // 存储类型
        jsonObject.put("taskNo", inspectOrderDto.getTaskNo());
        // 移动类型
//        jsonObject.put("moveType", stockInStdOrderDetails.get(0).getMoveType());
        // 创建时间
        jsonObject.put("createDate", DateUtils.getNowDate());
        // 供应商名称
//        jsonObject.put("vendorName", stockInStdOrder.getVendorName());
        // 用户名
        jsonObject.put("userName", SecurityUtils.getUsername());
        // 货架寿命
        jsonObject.put("age", "");
        // 源发货库存地点
        jsonObject.put("oriiginLoction", inspectOrderDto.getLocationCode());
        // 重量
//        jsonObject.put("weight", stockInStdOrder.getBtgew());
        // qrCode
        jsonObject.put("qrCode", "O%T" + inspectOrderDto.getTaskNo() + "%M" + inspectOrderDto.getMaterialNo());
        // 供应商批次
        jsonObject.put("vendorBatchNo", "");
        // 物料号
        jsonObject.put("materialNo", inspectOrderDto.getMaterialNo());
        // 物料名称
        jsonObject.put("materialName", inspectOrderDto.getMaterialName());
        //检验流水号
        jsonObject.put("inspectNo", inspectOrderDto.getOrderNo());
        jsonArray.add(jsonObject);
        dto.setDataArray(jsonArray);

        AjaxResult ajaxResult1 = printService.printByTemplate(dto);

        if (ajaxResult1.isError()) {
            throw new ServiceException(String.format("打印失败,原因：%s", ajaxResult1.get("msg").toString()));
        }
        return HttpStatus.SC_OK;
    }

    /**
     * 手动批量释放--- 挑选后处理使用
     *
     * @param inspectOrderDto
     * @return
     */
    @Override
    public AjaxResult releaseManual(InspectOrderDto inspectOrderDto) {

        List<InventoryDetailsVo> inventoryDetails = inspectOrderDto.getInventoryDetails();
        // 对inventoryDetails进行按照库存地点进行分组并汇总数量
            Map<String, List<InventoryDetailsVo>> locationMap = inventoryDetails.stream().collect(Collectors.groupingBy(InventoryDetailsVo::getLocationCode));
            // 遍历 locationMap
            for (String location : locationMap.keySet()) {
                List<InventoryDetailsVo> details = locationMap.get(location);
                // 需要释放数量
                BigDecimal qty = details.stream().map(InventoryDetailsVo::getInventoryQty).reduce(BigDecimal.ZERO, BigDecimal::add);

                InventoryDetailsVo item = details.get(0);

                String   voucherNo = null;
                try {
                    voucherNo = inspectReleaseSendSap(item.getMaterialNo(), item.getFactoryCode(),item.getSupplierCode(),
                            qty, item.getUnit(), item.getLocationCode(), "1000",
                            item.getStockInLot(), CommonYesOrNo.NO);

                } catch (Exception e) {
                   throw  new ServiceException(e.getMessage());
                }
                log.info("释放凭证:" + voucherNo);
            }
            //
        List<Long> ids = inspectOrderDto.getInventoryDetails().stream().map(InventoryDetailsVo::getId).collect(Collectors.toList());
        // 更新台账状态为非质检
        if (!ObjectUtils.isEmpty(ids)) {
            inStoreService.updateInventoryIsQc(ids);
        }

        return AjaxResult.success("释放成功");
    }
}
