package com.easycode.cloud.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.common.utils.ConvertUtils;
import com.easycode.cloud.domain.InspectOrder;
import com.easycode.cloud.domain.InspectOrderDetails;
import com.easycode.cloud.domain.ShelfTask;
import com.easycode.cloud.domain.WmsAttachs;
import com.easycode.cloud.domain.dto.DeliveryRequirementCheckDto;
import com.easycode.cloud.domain.dto.PlanQtyRecordDto;
import com.easycode.cloud.domain.vo.PurchaseOrderDetailVo;
import com.easycode.cloud.domain.vo.WmsAttachsVo;
import com.easycode.cloud.mapper.*;
import com.easycode.cloud.service.*;
import com.soa.eis.adapter.framework.message.IMsgObject;
import com.soa.eis.adapter.framework.message.impl.GroupRecord;
import com.soa.eis.adapter.framework.message.impl.MsgObject;
import com.weifu.cloud.common.core.exception.ServiceException;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.weifu.cloud.common.core.utils.ServletUtils;
import com.weifu.cloud.common.core.utils.SpringUtils;
import com.weifu.cloud.common.core.utils.ip.IpUtils;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.log.annotation.Log;
import com.weifu.cloud.common.log.enums.BusinessStatus;
import com.weifu.cloud.common.log.enums.BusinessType;
import com.weifu.cloud.common.log.service.AsyncLogService;
import com.weifu.cloud.common.security.utils.DictUtils;
import com.weifu.cloud.common.security.utils.SecurityUtils;
import com.weifu.cloud.constant.*;
import com.easycode.cloud.constants.PlanqtyRecordEnum;
import com.weifu.cloud.domain.*;
import com.weifu.cloud.domain.dto.*;
import com.weifu.cloud.domain.vo.*;
import com.weifu.cloud.domian.*;
import com.weifu.cloud.domian.dto.DeliveryRequirementInfoDto;
import com.weifu.cloud.domian.dto.MaterialUnitDefDto;
import com.weifu.cloud.domian.dto.WmsMaterialAttrParamsDto;
import com.weifu.cloud.domian.dto.WmsMaterialBasicDto;
import com.weifu.cloud.domian.dto.*;
import com.weifu.cloud.domian.vo.InventoryDetailsVo;
import com.weifu.cloud.domian.vo.PopupBoxVo;
import com.weifu.cloud.domian.vo.StoragePositionVo;
import com.weifu.cloud.enums.RemoteConfigEnum;
import com.weifu.cloud.mapper.*;
import com.weifu.cloud.service.*;
import com.weifu.cloud.system.api.domain.SysDictData;
import com.weifu.cloud.system.api.domain.SysOperLog;
import com.weifu.cloud.system.api.model.LoginUser;
import com.weifu.cloud.system.service.ISysConfigService;
import com.weifu.cloud.tools.DateUtil;
import com.weifu.cloud.tools.StringUtil;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.weifu.cloud.util.PopupBoxUtils.processingFormat;

/**
 * 送货单Service业务层处理
 *
 * @author ruoyi
 * @date 2022-11-25
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DeliveryOrderServiceImpl implements IDeliveryOrderService {
    private static final Logger logger = LoggerFactory.getLogger(DeliveryOrderServiceImpl.class);
    @Autowired
    private DeliveryOrderMapper deliveryOrderMapper;

    @Autowired
    private DeliveryOrderDetailMapper deliveryOrderDetailMapper;

    @Autowired
    private WmsPurchaseOrderPlanqtyRecordMapper wmsPurchaseOrderPlanqtyRecordMapper;

    @Autowired
    private WmsLkDeliveryDetailRequirementMapper wmsLkDeliveryDetailRequirementMapper;

    @Autowired
    private PurchaseMapper purchaseMapper;

    @Autowired
    private PurchaseOrderDetailMapper purchaseOrderDetailMapper;

    @Autowired
    private IStockInService stockInService;

    @Autowired
    private ICodeRuleService iCodeRuleService;

    @Autowired
    private StockInStdOrderMapper stockInStdOrderMapper;

    @Autowired
    private IStockInStdOrderService stockInStdOrderService;

    @Autowired
    private IMainDataService mainDataService;

    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    private IDeliveryDetailRequirementService wmsLkDeliveryDetailRequirementService;

    @Autowired
    private IWmsAttachsService wmsAttachsService;
//
//    @Autowired
//    private PurchaseOrderDetailPlanMapper purchaseOrderDetailPlanMapper;

    @Autowired
    private RemoteEsbSendService remoteEsbSendService;

    @Autowired
    private RemoteConfigHelper remoteConfigHelper;

    @Autowired
    private IShelfTaskService shelfTaskService;

    @Resource
    private IWmsPurchaseOrderPlanqtyRecordService wmsPurchaseRecordService;
    @Autowired
    private StockInStdOrderDetailMapper stockInStdOrderDetailMapper;

    @Autowired
    private EsbSendCommonMapper esbSendCommonMapper;

    @Autowired
    private IInspectOrderService inspectOrderService;

    @Autowired
    private IInspectOrderDetailsService inspectOrderDetailsService;

    @Autowired
    private InspectOrderMapper inspectOrderMapper;

    @Autowired
    private InspectOrderDetailsMapper inspectOrderDetailsMapper;

    @Autowired
    private IInStoreService inStoreService;

    @Autowired
    private ITaskService taskService;

    @Autowired
    AsyncLogService asyncLogService;

    @Autowired
    ISysConfigService sysConfigService;

    //送货单新增
    private final static String ADD = "add";
    //送货单修改
    private final static String EDIT = "edit";
    //送货单明细删除
    private final static String DELETE = "delete";

    /**
     * 查询送货单
     *
     * @param id 送货单主键
     * @return 送货单
     */
    @Override
    public DeliveryOrder selectDeliveryOrderById(Long id) {
        return deliveryOrderMapper.selectDeliveryOrderById(id);
    }

    /**
     * 查询送货单列表
     *
     * @param deliveryOrderDto 送货单
     * @return 送货单
     */
    @Override
    public List<DeliveryOrder> selectDeliveryOrderList(DeliveryOrderDto deliveryOrderDto) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //是否外部供应商
        String vendorFlag = loginUser.getSysUser().getVendorFlag();
        //用户公司代码
        String companyCode = SecurityUtils.getComCode();
        if (CommonYesOrNo.YES.equals(vendorFlag)){
            //外部供应商
            deliveryOrderDto.setSupplierCode(companyCode);
        }
        deliveryOrderDto.setStatus("0");
        return deliveryOrderMapper.selectDeliveryOrderList(deliveryOrderDto);
    }

    /**
     * 查询送货单列表
     *
     * @param deliveryOrder 送货单
     * @return 送货单
     */
    @Override
    public List<PrintInfoVo> getPrintInfoByOrderNo(DeliveryOrderVo deliveryOrder) {
        Long[] ids = deliveryOrder.getIds();
        List<PrintInfoVo> printInfoVos = stockInStdOrderMapper.selectInfoByOrderIds(ids);

        List<SysDictData> sysDictData = DictUtils.getDictCache("common_yes_no");
        Map<String, String> commonYesNo = sysDictData.stream()
                .collect(Collectors.toMap(SysDictData::getDictValue, SysDictData::getDictLabel));
        for (PrintInfoVo printInfoVo : printInfoVos) {
            printInfoVo.setSlowFlow(commonYesNo.get(printInfoVo.getSlowFlow()));
            printInfoVo.setPrintDate(DateUtils.getTime());
        }
        return getPrintInfoVos(printInfoVos, deliveryOrder);
    }

    /**
     * 根据送货单详情id查询打印信息
     */
    @Override
    public List<PrintInfoVo> getPrintInfoByIds(DeliveryOrderVo deliveryOrder) {
        Long[] ids = deliveryOrder.getIds();
        List<PrintInfoVo> printInfoVos = stockInStdOrderMapper.selectInfoByOrderDetailIds(ids);
        return getPrintInfoVos(printInfoVos, deliveryOrder);
    }

    /**
     * 处理物料标签所需的打印数据
     */
    public List<PrintInfoVo> getPrintInfoVos(List<PrintInfoVo> printInfoVos, DeliveryOrderVo deliveryOrder) {
        List<GoodsSourceDef> goodsSourceDefList = new ArrayList<>();
        for (PrintInfoVo printInfoVo : printInfoVos) {
            GoodsSourceDef goodsSourceDef = new GoodsSourceDef();
            goodsSourceDef.setSupplierCode(printInfoVo.getVendorCode());
            goodsSourceDef.setMaterialNo(printInfoVo.getMaterialNo());
            goodsSourceDef.setFactoryCode(printInfoVo.getFactoryCode());
            goodsSourceDef.setTenantId(deliveryOrder.getTenantId());
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
        Map<String, BigDecimal> minPackingMap = minPackingList.stream().collect(Collectors.toMap(GoodsSourceDef::getMaterialNo, GoodsSourceDef::getMinPacking, (k1, k2) -> k1));

        // 获取物料号
        List<String> materialNoList = printInfoVos.stream().map(PrintInfoVo::getMaterialNo).collect(Collectors.toList());
        // 根据物料查询对应物料类型

        WmsMaterialBasicDto wmsMaterialBasicDto = new WmsMaterialBasicDto();
        wmsMaterialBasicDto.setMaterialNoList(materialNoList);
        wmsMaterialBasicDto.setTenantId(deliveryOrder.getTenantId());
        ajaxResult = mainDataService.getMaterialArrInfo(wmsMaterialBasicDto);
        if (ajaxResult.isError()) {
            throw new ServiceException(ajaxResult.get("msg").toString());
        }
        List<WmsMaterialAttrParamsDto> list = JSON.parseArray(ajaxResult.get("data").toString(), WmsMaterialAttrParamsDto.class);
        if (ObjectUtils.isEmpty(list)) {
            throw new ServiceException("物料属性表中未查询到物料相关信息！");
        }
        Map<String, String> map = list.stream().collect(Collectors.toMap(WmsMaterialAttrParamsDto::getMaterialNo, s -> Optional.ofNullable(s.getType()).orElse("无"),(key1,key2)->key1));

        // 获取多级单位信息
        MaterialUnitDefDto unitDefDto = new MaterialUnitDefDto();
        unitDefDto.setMaterialNoList(materialNoList);
        unitDefDto.setIsDefault(CommonYesOrNo.NO);
        AjaxResult unitResult = mainDataService.batchMaterialUnitDef(unitDefDto);
        if (unitResult.isError()) {
            throw new ServiceException(unitResult.get("msg").toString());
        }
        List<MaterialUnitDefDto> unitList = JSON.parseArray(unitResult.get("data").toString(), MaterialUnitDefDto.class);
//        if (ObjectUtils.isEmpty(unitList)) {
//            throw new ServiceException("不存在入库使用单位，请维护相关数据");
//        }
        Map<String, BigDecimal> conversDefaultMap = unitList.stream()
                .collect(Collectors.toMap(MaterialUnitDefDto::getMaterialNo, MaterialUnitDefDto::getConversDefault, (k1, k2) -> k1));
        Map<String, String> unitMap = unitList.stream()
                .collect(Collectors.toMap(MaterialUnitDefDto::getMaterialNo, MaterialUnitDefDto::getUnit, (k1, k2) -> k1));

        for (PrintInfoVo printInfoVo : printInfoVos) {
            String purchaseOrderNo = printInfoVo.getPurchaseOrderNo();
            String deliveryOrderNo = printInfoVo.getDeliveryOrderNo();
            String materialNo = printInfoVo.getMaterialNo();
            String lotNo = printInfoVo.getLotNo();
            BigDecimal deliverQty = printInfoVo.getDeliverQty().setScale(0,BigDecimal.ROUND_UP);
            String vendorCode = printInfoVo.getVendorCode();
            String purchaseLineNo = printInfoVo.getPurchaseLineNo();
            BigDecimal minPacking = Optional.ofNullable(minPackingMap.get(String.format("%s-%s", materialNo, vendorCode))).orElse(deliverQty);
            printInfoVo.setUnit(unitMap.get(materialNo));
            int copies = minPacking.compareTo(BigDecimal.ZERO) == 0 ? 1 : deliverQty.divide(minPacking, 0, BigDecimal.ROUND_UP).intValue();
            printInfoVo.setCopies(copies);
            String qrCode = String.format("O%s%%D%s%%M%s%%Q%s%%B%s%%V%s%%L%s%%X1/1", purchaseOrderNo, deliveryOrderNo, materialNo,
                    deliverQty, lotNo, vendorCode, purchaseLineNo);
            printInfoVo.setQrCode(qrCode);
            printInfoVo.setOldMaterialNo(printInfoVo.getRequirementContent());
            printInfoVo.setMinPacking(minPacking.toString());
            printInfoVo.setType(map.get(printInfoVo.getMaterialNo()));
            printInfoVo.setCreateTime(DateUtils.getNowDate());
            //换算系数
            if (conversDefaultMap.containsKey(materialNo)){
                printInfoVo.setConversDefault(conversDefaultMap.getOrDefault(materialNo,BigDecimal.ONE));
            }
        }
        return printInfoVos;
    }

    /**
     * 新增送货单
     *
     * @param deliveryOrderVo 送货单
     * @return 结果
     */
    @Override
    @GlobalTransactional
    public DeliveryOrderVo insertDeliveryOrder(DeliveryOrderVo deliveryOrderVo) throws Exception {

        if (ObjectUtils.isEmpty(deliveryOrderVo)) {
            throw new ServiceException("参数不存在");
        }
        if(Objects.isNull(deliveryOrderVo.getTenantId())) {
            deliveryOrderVo.setTenantId(SecurityUtils.getLoginUser().getSysUser().getTenantId());
        }
        List<DeliveryOrderDetailVo> deliveryOrderDetailVoList = deliveryOrderVo.getDeliveryOrderDetailVoList();
        if (ObjectUtils.isEmpty(deliveryOrderDetailVoList)) {
            throw new ServiceException("送货单明细不能为空！");
        }

        Long tenantId = SecurityUtils.getLoginUser().getTenantId();
        // 获取收货明细
        DeliveryOrderDetailVo deliveryOrderDetail = deliveryOrderVo.getDeliveryOrderDetailVoList().get(0);

        String orderNo = deliveryOrderDetail.getPurchaseOrderNo();
        StockInStdOrder stdOrder = stockInStdOrderMapper.selectStockInStdOrderByNo(orderNo);

        StockInStdOrderDetailDto detailDto = new StockInStdOrderDetailDto();
        Long orderDetailId = deliveryOrderDetail.getId();
        detailDto.setId(orderDetailId);
        List<StockInStdOrderDetail> stdOrderDetailList = stockInStdOrderDetailMapper.selectStockInStdOrderDetailList(detailDto);

        if (ObjectUtils.isEmpty(stdOrderDetailList) || ObjectUtils.isEmpty(stdOrderDetailList.get(0))) {
            throw new ServiceException("采购单据明细信息为空！");
        }
        StockInStdOrderDetail stockInStdOrderDetail = stdOrderDetailList.get(0);

        Date now = DateUtils.getNowDate();
        DeliveryOrderVo deliveryOrder = new DeliveryOrderVo();
        BeanUtils.copyProperties(deliveryOrderVo, deliveryOrder);

        AjaxResult result = iCodeRuleService.getSeqWithTenantId(OrderNoTypeConstant.DELIVERY_ORDER, tenantId.toString());
        if ("".equals(Objects.toString(result.get("data"), "")) || result.isError()) {
            throw new ServiceException("送货单号生成失败！");
        }
        deliveryOrder.setDeliveryOrderNo(result.get("data").toString());
        deliveryOrder.setFactoryCode(stdOrder.getFactoryCode());
        deliveryOrder.setFactoryName(stdOrder.getFactoryName());
        deliveryOrder.setSupplierId(stdOrder.getVendorId());
        deliveryOrder.setSupplierCode(stdOrder.getVendorCode());
        deliveryOrder.setSupplierName(stdOrder.getVendorName());
        deliveryOrder.setCreateTime(now);
        deliveryOrder.setTenantId(tenantId);
        deliveryOrder.setCreateBy(SecurityUtils.getUsername());
        deliveryOrder.setPurchaseOrderNo(orderNo);
        deliveryOrder.setPurchaseOrderType(stdOrder.getBusinessType());
        if (deliveryOrderMapper.insertDeliveryOrder(deliveryOrder) < 0) {
            throw new ServiceException("生成送货单失败！");
        }

        // 生成送货单并返回送货单ID
        long lineNo = 1L;
        for (DeliveryOrderDetailVo deliveryOrderDetailVo : deliveryOrderDetailVoList) {
            String locationCode = deliveryOrderDetailVo.getLocationCode();
            if (StringUtil.isEmpty(locationCode)) {
                // 收货缓冲区
                locationCode = sysConfigService.selectConfigByKey("receive_buffer_location");;
            }
            AjaxResult positionResult = mainDataService.getPositionCodeByLocationCodeAndMaterialNo(
                    locationCode, deliveryOrderDetail.getMaterialNo());

            if(positionResult.isError()) {
                throw new ServiceException("查询入库单据的推荐仓位失败，库存地点代码" + deliveryOrderDetail.getLocationCode());
            }
            StoragePositionVo storagePositionVo = JSON.parseObject(JSON.toJSONString(positionResult.get("data")), StoragePositionVo.class);

            String mixedFlag = storagePositionVo.getMixedFlag();
            if (CommonYesOrNo.NOT_MIXED.equals(mixedFlag) && !ObjectUtils.isEmpty(storagePositionVo.getId())) {
                // 非混放仓位，锁定推荐仓位
                mainDataService.updateStoragePositionStatus(new Long[]{storagePositionVo.getId()}, CommonYesOrNo.LOCK_POSITION);
            }

            addDeliveryOrderDetailVo(deliveryOrderDetailVo, tenantId, deliveryOrder, stockInStdOrderDetail, lineNo, locationCode);
            lineNo = lineNo + 1;

            if (!ObjectUtils.isEmpty(deliveryOrderDetailVo.getRequirementList())) {
                wmsLkDeliveryDetailRequirementService.batchAdd(deliveryOrderDetailVo.getRequirementList(), deliveryOrderDetailVo.getId(), deliveryOrderVo.getTenantId());
            }
            Long stdOrderDetailId = deliveryOrderDetailVo.getPurchaseOrderDetailId();

            StockInStdOrderDetailDto stdOrderDetailDto = new StockInStdOrderDetailDto();
            stdOrderDetailDto.setId(stdOrderDetailId);
            stdOrderDetailDto.setAvailableQty(stockInStdOrderDetail.getAvailableQty().subtract(deliveryOrderDetailVo.getReceivedQty()));
            stdOrderDetailDto.setReceivedQty(deliveryOrderDetailVo.getReceivedQty());
            stockInStdOrderDetailMapper.updateStockInStdOrderDetail(stdOrderDetailDto);

            // 更新台账
            Long inventoryId = updateInventoryDetailsRaw(deliveryOrder, deliveryOrderDetailVo, storagePositionVo);

            if (CommonYesOrNo.NOT_MIXED.equals(mixedFlag) && !ObjectUtils.isEmpty(storagePositionVo.getId())) {
                // 非混放仓位，锁定推荐仓位
                mainDataService.updateStoragePositionStatus(new Long[]{storagePositionVo.getId()}, CommonYesOrNo.UNLOCK_POSITION);
            }

            // 扣除bom信息里的委外库存
            stockInStdOrderService.updateOutSourcedInventory(stdOrder);

            // 收货过账
            String voucherNo = sendToSap(stockInStdOrderDetail, deliveryOrderDetailVo);
            deliveryOrderDetailVo.setIsPost(CommonYesOrNo.POSTED);
            deliveryOrderDetailVo.setVoucherNo(voucherNo);
            deliveryOrderDetailMapper.updateDeliveryOrderDetail(deliveryOrderDetailVo);
            addTaskLog(stdOrder, stockInStdOrderDetail, deliveryOrderDetailVo, storagePositionVo, voucherNo);

            try {
                CompletableFuture<Void> future = CompletableFuture.runAsync(()->{
                    try {
                        addInspectOrderAndShelfTask(deliveryOrderDetailVo, stockInStdOrderDetail, stdOrder, storagePositionVo, inventoryId);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                future.exceptionally(e -> {
                    logger.error(e.getMessage());
                    SysOperLog operLog = new SysOperLog();
                    operLog.setTitle("新增收货单");
                    operLog.setBusinessType(BusinessType.INSERT.ordinal());
                    operLog.setOperatorType(BusinessType.INSERT.ordinal());
                    String username = SecurityUtils.getUsername();
                    if (!ObjectUtils.isEmpty(username)) {
                        operLog.setOperName(username);
                    }
                    operLog.setTenantId(MainDataConstant.TENANT_ID);
                    operLog.setStatus(BusinessStatus.FAIL.ordinal());
                    operLog.setErrorMsg(com.weifu.cloud.common.core.utils.StringUtils.substring(e.getMessage(), 0, 2000));
                    String className = this.getClass().getName();
                    String methodName = "addDeliveryOrder";
                    operLog.setMethod(className + "." + methodName + "()");
                    Map<String, Object> params = getParamsMap(deliveryOrder);
                    operLog.setOperParam(JSONObject.toJSONString(params));
                    asyncLogService.saveSysLog(operLog);
                    return null;
                });
            } catch (Exception e) {
                if (!StringUtils.isEmpty(voucherNo)){
                    com.alibaba.fastjson.JSONObject param = new com.alibaba.fastjson.JSONObject();
                    param.put("voucherNo", voucherNo);
                    inspectOrderService.materialReversal(param);
                }
                logger.error(e.getMessage());
                throw new ServiceException("生成送货单异常，请联系管理员，错误信息：" + e.getMessage());
            }
        }
        return deliveryOrder;
    }

    private void addDeliveryOrderDetailVo(DeliveryOrderDetailVo deliveryOrderDetailVo, Long tenantId, DeliveryOrderVo deliveryOrder,
                                          StockInStdOrderDetail stockInStdOrderDetail, Long lineNo, String locationCode) {
        AjaxResult ajaxResult;
        // 判断开始时间为null设置当前时间,
        if (null == deliveryOrderDetailVo.getProductTime()) {
            deliveryOrderDetailVo.setProductTime(DateUtils.getNowDate());
        } else {
            deliveryOrderDetailVo.setProductTime(deliveryOrderDetailVo.getProductTime());
        }
        // 如果结束时间为null设置为开始时间+有效期时间
        if (null == deliveryOrderDetailVo.getExpireTime()) {
            // 远程调用通过物料号查询物料的有效期时间
            WmsMaterialBasicDto dto = new WmsMaterialBasicDto();
            List<String> materialNoList = new ArrayList<>();
            materialNoList.add(deliveryOrderDetailVo.getMaterialNo());
            dto.setMaterialNoList(materialNoList);
            AjaxResult materialInfoAjaxResult = mainDataService.getMaterialArrInfo(dto);
            if (materialInfoAjaxResult.isError()) {
                throw new ServiceException(materialInfoAjaxResult.get("msg").toString());
            }
            List<WmsMaterialAttrParamsDto> list = com.alibaba.fastjson.JSONObject.parseArray(materialInfoAjaxResult.get("data").toString(), WmsMaterialAttrParamsDto.class);
            if (!ObjectUtils.isEmpty(list) && !list.isEmpty()) {
                WmsMaterialAttrParamsDto wmsMaterialAttrParamsDto = list.get(0);
                String defaultExtValiditydays = wmsMaterialAttrParamsDto.getDefaultValidityPeriod();

                if (ObjectUtils.isEmpty(defaultExtValiditydays)) {
                    throw new ServiceException("物料属性默认有效期不存在!");
                }
                if (null == deliveryOrderDetailVo.getCreateTime()) {
                    deliveryOrderDetailVo.setCreateTime(DateUtils.getNowDate());
                }
                Date expireTime = DateUtils.addDays(deliveryOrderDetailVo.getProductTime(), Integer.valueOf(defaultExtValiditydays));

                // 设置过期日期
                deliveryOrderDetailVo.setExpireTime(expireTime);
            }
        } else {
            deliveryOrderDetailVo.setExpireTime(deliveryOrderDetailVo.getExpireTime());
        }

        ajaxResult = iCodeRuleService.getSeqWithTenantId(StockInTaskConstant.ENTER_LOT_NO, tenantId.toString());
        if (ajaxResult.isError() || StringUtils.isEmpty(ajaxResult.get("data").toString())) {
            throw new ServiceException("入库批次号生成失败！");
        }

        deliveryOrderDetailVo.setPurchaseOrderDetailId(deliveryOrderDetailVo.getId());
        deliveryOrderDetailVo.setDeliveryOrderId(deliveryOrder.getId());
        deliveryOrderDetailVo.setDeliveryOrderNo(deliveryOrder.getDeliveryOrderNo());
        deliveryOrderDetailVo.setPurchaseOrderNo(deliveryOrder.getPurchaseOrderNo());
        deliveryOrderDetailVo.setPurchaseLineNo(stockInStdOrderDetail.getPurchaseLineNo());
        if (ObjectUtils.isEmpty(stockInStdOrderDetail.getLotNo())) {
            deliveryOrderDetailVo.setBatchNo(ajaxResult.get("data").toString());
            stockInStdOrderDetail.setLotNo(ajaxResult.get("data").toString());
        } else {
            deliveryOrderDetailVo.setBatchNo(stockInStdOrderDetail.getLotNo());
        }
        deliveryOrderDetailVo.setDeliveryLineNo(lineNo);
        deliveryOrderDetailVo.setIsQc(stockInStdOrderDetail.getIsQc());
        deliveryOrderDetailVo.setIsConsign(CommonYesOrNo.NO);
        deliveryOrderDetailVo.setLocationCode(locationCode);
        deliveryOrderDetailVo.setTenantId(tenantId);
        deliveryOrderDetailVo.setCreateBy(SecurityUtils.getUsername());
        deliveryOrderDetailVo.setCreateTime(new Date());
        deliveryOrderDetailVo.setUpdateBy(SecurityUtils.getUsername());
        deliveryOrderDetailVo.setFactoryCode(stockInStdOrderDetail.getFactoryCode());
        deliveryOrderDetailVo.setFactoryName(stockInStdOrderDetail.getFactoryCode());
        deliveryOrderDetailVo.setDeliverQty(stockInStdOrderDetail.getDeliverQty());
        deliveryOrderDetailVo.setReceivedQty(deliveryOrderDetailVo.getReceivedQty());
        deliveryOrderDetailVo.setOldMaterialNo(stockInStdOrderDetail.getOldMaterialNo());
        // 新增送货单明细
        deliveryOrderDetailMapper.insertDeliveryOrderDetail(deliveryOrderDetailVo);
    }

    private void addInspectOrderAndShelfTask(DeliveryOrderDetailVo deliveryOrderDetailVo, StockInStdOrderDetail stdOrderDetailInfo, StockInStdOrder stdOrder, StoragePositionVo storagePositionVo, Long inventoryId) throws Exception {
        AjaxResult ajaxResult;
        InspectOrder inspectOrder = null;
        try {
            // 获取物料属性
            WmsMaterialDeliverAttr dto = new WmsMaterialDeliverAttr();
            dto.setMaterialNo(stdOrderDetailInfo.getMaterialNo());
            ajaxResult = mainDataService.getDeliverAttr(dto);
            if (ajaxResult.isError()) {
                throw new ServiceException(ajaxResult.get("msg").toString());
            }
            List<WmsMaterialDeliverAttr> list = com.alibaba.fastjson.JSONObject.parseArray(ajaxResult.get("data").toString(), WmsMaterialDeliverAttr.class);
            if (ObjectUtils.isEmpty(list)) {
                throw new ServiceException(stdOrderDetailInfo.getMaterialNo() + "物料属性不存在,请维护相关数据");
            }
            BigDecimal palletQty = list.get(0).getPalletQty(); // 根据每托数量拆分上架

            if (CommonYesOrNo.YES.equals(stdOrderDetailInfo.getIsQc())) {
                logger.info("开始创建质检单: {}", DateUtil.getNowTime());
                // 生成送检单据
                inspectOrder = generateInspectOrder(stdOrder, stdOrderDetailInfo, deliveryOrderDetailVo, storagePositionVo);
                if (!ObjectUtils.isEmpty(inspectOrder) && !ObjectUtils.isEmpty(inspectOrder.getQcQty())
                        && inspectOrder.getQcQty().compareTo(BigDecimal.ZERO) == 0) {
                    SendInspectResultDto detail = new SendInspectResultDto();
                    detail.setTaskCode(inspectOrder.getOrderNo());
                    detail.setResult(SQConstant.SQ_INSPECT_PASS);
                    detail.setRemark("跳检");
                    inspectOrderService.sendInspectResult(detail);
                    // 抽检数量为0时，生成上架任务，其他场景不需要生成上架任务
                    generateShelfTask(deliveryOrderDetailVo, inspectOrder, storagePositionVo, inventoryId, palletQty);
                }
                logger.info("送检单创建流程完成");
            }
            if (CommonYesOrNo.NO.equals(stdOrderDetailInfo.getIsQc())) {
                // 生成上架任务
                generateShelfTaskNoInspect(deliveryOrderDetailVo, storagePositionVo, inventoryId, palletQty);
                logger.info("非质检场景生成上架任务: {}", DateUtil.getNowTime());
            }
        } catch (Exception e) {
            // 取消质检任务 回滚
            if (!ObjectUtils.isEmpty(inspectOrder)) {
                inspectOrderService.cancelInspectTask(inspectOrder.getOrderNo());
                inspectOrderMapper.deleteInspectOrderById(inspectOrder.getId());
            }
            throw new ServiceException("生成质检单异常，请联系管理员，错误信息：" + e.getMessage());
        }
    }

    private static Map<String, Object> getParamsMap(DeliveryOrderVo deliveryOrder) {
        Map<String, Object> params = new HashMap<>();
        params.put("deliveryOrderNo", deliveryOrder.getDeliveryOrderNo());
        params.put("factoryCode", deliveryOrder.getFactoryCode());
        params.put("supplierCode", deliveryOrder.getSupplierCode());
        params.put("supplierName", deliveryOrder.getSupplierName());
        params.put("purchaseOrderNo", deliveryOrder.getPurchaseOrderNo());
        params.put("purchaseOrderType", deliveryOrder.getPurchaseOrderType());
        return params;
    }

    private void addTaskLog(StockInStdOrder stdOrder, StockInStdOrderDetail stockInStdOrderDetail,
                            DeliveryOrderDetailVo deliveryOrderDetail, StoragePositionVo storagePositionVo, String voucherNo) {
        // 新增任务记录
        TaskLog taskLog = new TaskLog();
        taskLog.setFactoryCode(storagePositionVo.getFactoryCode());
        taskLog.setMaterialNo(stockInStdOrderDetail.getMaterialNo());
        taskLog.setMaterialName(stockInStdOrderDetail.getMaterialName());
        taskLog.setOldMaterialNo(stockInStdOrderDetail.getOldMaterialNo());
        taskLog.setLot(deliveryOrderDetail.getBatchNo());
        taskLog.setQty(deliveryOrderDetail.getReceivedQty());
        taskLog.setOperationQty(deliveryOrderDetail.getReceivedQty());
        taskLog.setLocationCode(deliveryOrderDetail.getLocationCode());
        taskLog.setTargetPositionCode(storagePositionVo.getPositionNo());
        taskLog.setTargetLocationCode(storagePositionVo.getLocationCode());
        taskLog.setTargetFactoryCode(storagePositionVo.getFactoryCode());
        taskLog.setAreaCode(storagePositionVo.getAreaCode());
        taskLog.setTaskType(TaskLogTypeConstant.STD_ORDER_ASN);
        taskLog.setOrderNo(stdOrder.getOrderNo());
        taskLog.setMaterialCertificate(voucherNo);
        taskLog.setIsQc(stockInStdOrderDetail.getIsQc());
        taskLog.setIsConsign(stockInStdOrderDetail.getIsConsign());
        taskLog.setOrderType(TaskLogTypeConstant.STD_ORDER_ASN);
        taskLog.setUnit(stockInStdOrderDetail.getUnit());
        taskLog.setSupplierCode(stdOrder.getVendorCode());
        taskLog.setSupplierName(stdOrder.getVendorName());
        taskLog.setOperationUnit(stockInStdOrderDetail.getOperationUnit());
        if (taskService.add(taskLog).isError()) {
            logger.error("新增任务记录失败！");
        }
    }

    private void generateShelfTaskNoInspect(DeliveryOrderDetailVo deliveryOrderDetail, StoragePositionVo storagePositionVo,
                                            Long inventoryId, BigDecimal palletQty) {
        BigDecimal shelfQty = deliveryOrderDetail.getReceivedQty();
        logger.info("非质检场景待上架物料数量: {}", shelfQty);

        List<ShelfTask> shelfTaskList = generateShelfTaskInfo(deliveryOrderDetail,
                storagePositionVo, shelfQty, inventoryId, palletQty);

        shelfTaskService.insertShelfTaskList(shelfTaskList);
        logger.info("生成上架任务成功");

        // 打印TO单
        List<Long> ids = shelfTaskList.stream().map(ShelfTask::getId).collect(Collectors.toList());
        shelfTaskService.printList(deliveryOrderDetail.getLocationCode(), ids);
        logger.info("上架任务打印完成");
    }

    private void generateShelfTask(DeliveryOrderDetailVo deliveryOrderDetail, InspectOrder inspectOrder,
                                   StoragePositionVo sourceStoragePositionVo, Long inventoryId, BigDecimal palletQty) {
        BigDecimal receivedQty = deliveryOrderDetail.getReceivedQty();
        if (null == receivedQty) {
            throw new ServiceException("交货单接收数量为空，请检查相关单据。");
        }

        // 抽检数量
        BigDecimal insertQty = inspectOrder.getQcQty() == null ? BigDecimal.ZERO : inspectOrder.getQcQty();
        // 上架数量
        BigDecimal shelfQty = receivedQty.subtract(insertQty);

        logger.info("单据抽检数量: {}, 生成上架数量: {}", insertQty, shelfQty);

        List<ShelfTask> shelfTaskList = generateShelfTaskInfo(deliveryOrderDetail,
                sourceStoragePositionVo, shelfQty, inventoryId, palletQty);

        shelfTaskService.insertShelfTaskList(shelfTaskList);
        logger.info("生成上架任务结束：{}", DateUtil.getNowTime());

        List<Long> taskIdList = shelfTaskList.stream().map(ShelfTask::getId).collect(Collectors.toList());
        // 打印上架任务
        shelfTaskService.printList(deliveryOrderDetail.getLocationCode(), taskIdList);
        logger.info("打印上架任务标签完成");
    }

    private List<ShelfTask> generateShelfTaskInfo(DeliveryOrderDetailVo deliveryOrderDetail, StoragePositionVo sourcePosition,
                                                  BigDecimal shelfQty, Long inventoryId, BigDecimal palletQty) {

        logger.info("生成上架任务开始：{}", DateUtil.getNowTime());
        Long tenantId = SecurityUtils.getLoginUser().getSysUser().getTenantId();
        List<ShelfTask> shelfTaskList = new ArrayList<>();

        // 获取每托数量，根据每托数量拆分总任务数
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

            AjaxResult positionResult = mainDataService.getPositionCode(deliveryOrderDetail.getMaterialNo());

            if(positionResult.isError()) {
                throw new ServiceException("查询入库单据的推荐仓位失败，库存地点代码" + deliveryOrderDetail.getLocationCode());
            }
            StoragePositionVo storagePositionVo = JSON.parseObject(JSON.toJSONString(positionResult.get("data")), StoragePositionVo.class);

            String mixedFlag = storagePositionVo.getMixedFlag();
            if (CommonYesOrNo.NOT_MIXED.equals(mixedFlag) && !ObjectUtils.isEmpty(storagePositionVo.getId())) {
                // 非混放仓位，锁定推荐仓位
                mainDataService.updateStoragePositionStatus(new Long[]{storagePositionVo.getId()}, CommonYesOrNo.LOCK_POSITION);
            }
            ShelfTask shelfTask = new ShelfTask();
            shelfTask.setTaskNo(taskNo);
            shelfTask.setTaskType(TaskLogTypeConstant.SHELF);
            shelfTask.setStatus(TaskStatusConstant.TASK_STATUS_NEW);
            shelfTask.setAllocateTime(DateUtils.getNowDate());
            shelfTask.setMaterialNo(deliveryOrderDetail.getMaterialNo());
            shelfTask.setMaterialName(deliveryOrderDetail.getMaterialName());
            shelfTask.setOldMaterialNo(deliveryOrderDetail.getOldMaterialNo());
            shelfTask.setStockinOrderNo(deliveryOrderDetail.getPurchaseOrderNo());
            shelfTask.setStockinLineNo(deliveryOrderDetail.getPurchaseLineNo());
            shelfTask.setLocationCode(storagePositionVo.getLocationCode());
            shelfTask.setPositionNo(storagePositionVo.getPositionNo());
            shelfTask.setPositionId(storagePositionVo.getId());
            shelfTask.setSourcePositionNo(sourcePosition.getPositionNo());
            shelfTask.setSourceLocationCode(sourcePosition.getLocationCode());
            shelfTask.setQty(palletQty);
            shelfTask.setOperationQty(palletQty);
            shelfTask.setUnit(deliveryOrderDetail.getUnit());
            shelfTask.setCompleteQty(BigDecimal.ZERO);
            shelfTask.setOperationCompleteQty(BigDecimal.ZERO);
            shelfTask.setLot(deliveryOrderDetail.getBatchNo());
            shelfTask.setCreateBy(SecurityUtils.getUsername());
            shelfTask.setCreateTime(DateUtils.getNowDate());
            shelfTask.setTenantId(tenantId);
            shelfTask.setDelivery0rderNo(deliveryOrderDetail.getDeliveryOrderNo());

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
        }
        return shelfTaskList;
    }

    private InspectOrder generateInspectOrder(StockInStdOrder stdOrder, StockInStdOrderDetail stdOrderDetailInfo,
                                              DeliveryOrderDetailVo deliveryOrderDetail, StoragePositionVo storagePositionVo) throws Exception {
        BigDecimal receivedQty = deliveryOrderDetail.getReceivedQty();
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
        inspectOrder.setQty(receivedQty);
        inspectOrder.setBillStatus(InspectOrderStatusConstant.ORDER_STATUS_NEW);
        inspectOrder.setFactoryCode(deliveryOrderDetail.getFactoryCode());
        inspectOrder.setIsConsign(deliveryOrderDetail.getIsConsign());
        inspectOrder.setPurchaseOrderNo(deliveryOrderDetail.getPurchaseOrderNo());
        inspectOrder.setPurchaseLineNo(deliveryOrderDetail.getPurchaseLineNo());
        inspectOrder.setLocationCode(deliveryOrderDetail.getLocationCode());
        inspectOrder.setMaterialNo(deliveryOrderDetail.getMaterialNo());
        inspectOrder.setMaterialName(deliveryOrderDetail.getMaterialName());

        inspectOrder.setReceiveDate(DateUtils.getNowDate());
        inspectOrder.setReceiveOrderNo(deliveryOrderDetail.getDeliveryOrderNo());
        inspectOrder.setSupplierCode(stdOrder.getVendorCode());
        inspectOrder.setSupplierName(stdOrder.getVendorName());
        inspectOrder.setCreateBy(SecurityUtils.getUsername());
        inspectOrder.setCreateTime(DateUtils.getNowDate());
        inspectOrderMapper.insertInspectOrder(inspectOrder);
        logger.info("新增送检单信息: {}", inspectTaskCode);

        // 送检单详情
        InspectOrderDetails inspectOrderDetails = new InspectOrderDetails();
        inspectOrderDetails.setOrderNo(inspectTaskCode);
        inspectOrderDetails.setMaterialNo(deliveryOrderDetail.getMaterialNo());
        inspectOrderDetails.setMaterialName(deliveryOrderDetail.getMaterialName());
        inspectOrderDetails.setLot(deliveryOrderDetail.getBatchNo());
        inspectOrderDetails.setPrdLot(deliveryOrderDetail.getBatchNo());
        inspectOrderDetails.setLineNo(deliveryOrderDetail.getPurchaseLineNo());
        inspectOrderDetails.setPositionNo(storagePositionVo.getPositionNo());
        inspectOrderDetails.setCreateBy(SecurityUtils.getUsername());
        inspectOrderDetails.setCreateTime(DateUtils.getNowDate());
        inspectOrderDetails.setUnit(deliveryOrderDetail.getUnit());
        inspectOrderDetailsMapper.insertInspectOrderDetails(inspectOrderDetails);
        logger.info("新增检验单明细信息: {}", inspectTaskCode);

        IMsgObject msgObject = new MsgObject(IMsgObject.MOType.initSR);
        msgObject.setReqValue("TaskCode", inspectTaskCode); // 收货单任务号
        msgObject.setReqValue("ReceiveCode", deliveryOrderDetail.getDeliveryOrderNo()); // 收货单号
        msgObject.setReqValue("Company", SecurityUtils.getComCode()); //公司
        msgObject.setReqValue("Plant", deliveryOrderDetail.getFactoryCode()); // 工厂（AC00，MA00）字典
        msgObject.setReqValue("PartNumber", deliveryOrderDetail.getMaterialNo()); // 物料编码

        msgObject.setReqValue("PartName", deliveryOrderDetail.getMaterialName()); // 物料名称
        msgObject.setReqValue("SupplyCode", stdOrder.getVendorCode()); // 供应商代码
        msgObject.setReqValue("SupplyName", stdOrder.getVendorName()); // 供应商名称
        msgObject.setReqValue("ProductBatch", deliveryOrderDetail.getBatchNo()); // 生产批次
        msgObject.setReqValue("InputBatch", deliveryOrderDetail.getBatchNo()); // 入库批次
        msgObject.setReqValue("InspectReason", "送检"); // 预留检验原因（进货）
        msgObject.setReqValue("TotalQuantity", com.weifu.cloud.common.core.text.Convert.toStr(deliveryOrderDetail.getReceivedQty())); // 来料数量
        msgObject.setReqValue("Warehouse", deliveryOrderDetail.getLocationCode()); // 库存地点（四位数字）
        msgObject.setReqValue("ReceiveUser", SecurityUtils.getUsername()); // 收货人
        msgObject.setReqValue("ReceiveTime",DateUtils.dateTime()); // 收货时间
        msgObject.setReqValue("Remark", deliveryOrderDetail.getRemark()); // 备注

        // 发送检验任务
        MsgObject inspectTaskMsg = sendInspectTask(msgObject);

        // 获取抽检数量
        String extractCountStr = inspectTaskMsg.getResValue("ExtractCount");
        logger.info("记录SQ平台返回抽检数量信息, extractCount:{}", extractCountStr);

        if (!ObjectUtils.isEmpty(extractCountStr) && !SQConstant.NO_SUPPORTED_INSPECT.equals(extractCountStr)) {
            BigDecimal extractCount = com.weifu.cloud.common.core.text.Convert.toBigDecimal(extractCountStr);

            if(ObjectUtils.isEmpty(extractCount)) {
                throw new ServiceException("获取抽检数量失败");
            }
            // 记录抽检数量
            inspectOrder.setQcQty(extractCount);
            inspectOrder.setQty(extractCount);
            inspectOrderDetails.setQcQty(extractCount);
            inspectOrderMapper.updateInspectOrder(inspectOrder);
            inspectOrderDetailsMapper.updateInspectOrderDetails(inspectOrderDetails);
        }
        return inspectOrder;
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
        itemRecord.setFieldValue("ERFMG", com.weifu.cloud.common.core.text.Convert.toStr(deliveryOrderDetail.getReceivedQty()));
        // 条目单位
        itemRecord.setFieldValue("ERFME", stockInStdOrderDetail.getUnit());
        // 特殊库存标识
        itemRecord.setFieldValue("SOBKZ", "");
        // 库存类型(是否质检)
        String stockType = "";
        if (CommonYesOrNo.YES.equals(stockInStdOrderDetail.getIsQc())) {
            stockType = SAPConstant.STOCK_TYPE_INSPECT;
        }
        itemRecord.setFieldValue("INSMK", stockType);

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
        List<WmsMaterialAttrParamsDto> materialAttrInfo = JSON.parseArray(materialInfo.get("data").toString(), WmsMaterialAttrParamsDto.class);
        if(ObjectUtils.isEmpty(materialAttrInfo)) {
            logger.info("{},“物料基础信息-物料属性”不存在", materialNo);
            throw new ServiceException(materialNo + "“物料基础信息-物料属性”不存在");
        }

        // 仓位区域关联
//        String section = materialAttrInfo.get(0).getSection();
//
//        if(ObjectUtils.isEmpty(section)) {
//            throw new ServiceException(materialNo + "默认区域不存在, 请维护相关数据");
//        }
//
//        AjaxResult ajaxResult1 = mainDataService.selectStorageAreaByDefaultReceiveLocationAndSection(locationCode, section, tenantId);
//
//        if(ObjectUtils.isEmpty(ajaxResult1) || ObjectUtils.isEmpty(ajaxResult1.get("data"))) {
//            throw new ServiceException(String.format("物料:%s,库存地点:%s,关联section:”%s“区域失败", materialNo,locationCode, section));
//        }
//        if(ajaxResult1.isError()) {
//            throw new ServiceException(ajaxResult1.get("msg").toString());
//        }
//        // 区域
//        StorageArea storageArea = JSON.parseObject(JSON.toJSONString(ajaxResult1.get("data")), StorageArea.class);
//        if(ObjectUtils.isEmpty(storageArea)) {
//            throw new ServiceException(String.format("物料:%s,库存地点:%s,关联section:”%s“区域失败", materialNo,locationCode, section));
//        }

        // 仓位 (先取一条)
//        AjaxResult storagePositionInfo = mainDataService.getPositionInfoByAreaCode(storageArea.getAreaCode());
//
//        if(CollectionUtils.isEmpty(storagePositionInfo)) {
//            throw new ServiceException(String.format("物料:%s,库存地点:%s,关联section:”%s“区域:”%s“获取仓位失败, 请维护相关数据", materialNo,locationCode, section, storageArea.getAreaCode()));
//        }
//        List<StoragePositionVo> storagePositionVoList = JSON.parseArray(storagePositionInfo.get("data").toString(), StoragePositionVo.class);
//
//        if(CollectionUtils.isEmpty(storagePositionVoList)) {
//            throw new ServiceException(String.format("物料:%s,库存地点:%s,关联section:”%s“区域:”%s“获取仓位失败, 请维护相关数据", materialNo,locationCode, section, storageArea.getAreaCode()));
//        }

        AjaxResult storagePositionInfoAjaxResult = mainDataService.getPositionCode(materialNo);

        if(storagePositionInfoAjaxResult.isError()) {
            throw new ServiceException(String.format("物料号:%s,库存地点策略获取失败", materialNo));
        }

        StoragePositionVo storagePositionVo = JSON.parseObject(JSON.toJSONString(storagePositionInfoAjaxResult.get("data")), StoragePositionVo.class);
        String positionNo = storagePositionVo.getPositionNo();
        result.setLocationCode(storagePositionVo.getLocationCode());
        result.setAreaCode(storagePositionVo.getAreaCode());
        result.setPositionNo(positionNo);
        return result;
    }

    /**
     * @param deliveryOrderDetailVo
     * @return void
     * @desc 校验送货单数量是否超出采购单数量 并更改送货单需求数量和已制单数量
     * @author yangSen
     * @date 2022/12/2
     */
    public void updatePurchaseOrderQuantity(DeliveryOrderDetailVo deliveryOrderDetailVo, String rule) {
        // 计算采购单明细数量 总需求数量、已制单数量、需求数量
        // 采购单据号
        String purchaseOrderNo = deliveryOrderDetailVo.getPurchaseOrderNo();
        // 采购单据行号
        String purchaseLineNo = Convert.toStr(deliveryOrderDetailVo.getPurchaseLineNo());
        // 计划行号
        String planLineNo = deliveryOrderDetailVo.getPlanLineNo();
        // 根据采购单号和采购单行号 可以确定唯一的采购明细
        PurchaseOrderDetail purchaseOrderDetail = new PurchaseOrderDetail();
        purchaseOrderDetail.setPurchaseOrderNo(purchaseOrderNo);
        purchaseOrderDetail.setPurchaseLineNo(purchaseLineNo);
        purchaseOrderDetail.setDelFlag(CommonYesOrNo.NO);
        purchaseOrderDetail.setTenantId(deliveryOrderDetailVo.getTenantId());
        List<PurchaseOrderDetail> purchaseOrderDetailList = purchaseMapper.selectPurchaseOrderDetail(purchaseOrderDetail);

        // 根据采购单号和采购单行号 获取采购单计划行
        PurchaseOrderDetailPlan plan = new PurchaseOrderDetailPlan();
        plan.setPurchaseOrderNo(purchaseOrderNo);
        plan.setPurchaseLineNo(purchaseLineNo);
        plan.setDelFlag(CommonYesOrNo.NO);
        plan.setTenantId(deliveryOrderDetailVo.getTenantId());
//        List<PurchaseOrderDetailPlan> planList = purchaseOrderDetailPlanMapper.selectPurchaseOrderDetailPlanList(plan);

        if (ObjectUtils.isEmpty(purchaseOrderDetailList)) {
            throw new ServiceException("采购单明细不存在！");
        }

        PurchaseOrderDetail detail = purchaseOrderDetailList.get(0);
        // 送货数量
        BigDecimal deliverQty = deliveryOrderDetailVo.getDeliverQty();
        // 原送货数量
        BigDecimal beforeDeliverQty = deliveryOrderDetailVo.getBeforeDeliverQty();
        // 送货数量无变动时 直接返回
        if (EDIT.equals(rule) && deliverQty.compareTo(beforeDeliverQty) == 0){
            return;
        }
//        //增加已制单数量使用集合 过滤需求数量-已制单数量为0的计划行 并根据计划交货日期 正序排序 计划交货日期相同时 根据计划行号 正序排序
//        List<PurchaseOrderDetailPlan> add = planList.stream()
//                .filter(p -> p.getPlanQty().subtract(p.getMadeQty()).compareTo(BigDecimal.ZERO) > 0)
//                .sorted(Comparator.comparing(PurchaseOrderDetailPlan::getDeliveryDate, Comparator.nullsLast(Comparator.naturalOrder()))
//                        .thenComparing(PurchaseOrderDetailPlan::getDeliveryLineNo,Comparator.nullsLast(Comparator.naturalOrder())))
//                .collect(Collectors.toList());
//        //减少已制单数量使用集合 过滤依制单数量为0的计划行 并根据计划交货日期 倒序排序 计划交货日期相同时 根据计划行号 倒序排序
//        List<PurchaseOrderDetailPlan> deduct = planList.stream()
//                .filter(p -> p.getMadeQty().compareTo(BigDecimal.ZERO) > 0)
//                .sorted(Comparator.comparing(PurchaseOrderDetailPlan::getDeliveryDate, Comparator.nullsLast(Comparator.reverseOrder()))
//                        .thenComparing(PurchaseOrderDetailPlan::getDeliveryLineNo,Comparator.nullsLast(Comparator.reverseOrder())))
//                .collect(Collectors.toList());
//        if (ADD.equals(rule)) {
//            //新增时
//            ADD(detail, add, deliverQty);
//        }
//        else if (EDIT.equals(rule)) {
//            // 编辑时
//            if (deliverQty.compareTo(beforeDeliverQty) > 0) {
//                //送货数量>原送货数量时
//                //送货数量增量
//                BigDecimal increment = deliverQty.subtract(beforeDeliverQty);
//                ADD(detail, add, increment);
//            } else if (deliverQty.compareTo(beforeDeliverQty) < 0) {
//                //送货数量<原送货数量时
//                //送货数量减量
//                BigDecimal reduction = beforeDeliverQty.subtract(deliverQty);
//                SUBTRACT(detail, deduct, reduction);
//            }
//        }
//        else if (DELETE.equals(rule)) {
//            //删除时
//            SUBTRACT(detail, deduct, deliverQty);
//        }
    }

    public void ADD(PurchaseOrderDetail detail, List<PurchaseOrderDetailPlan> add, BigDecimal deliverQty) {
        // 总需求数量
        BigDecimal totalPlanQty = detail.getTotalPlanQty();
        // 原需求数量
        BigDecimal planQty = detail.getPlanQty();
        // 已制单数量
        BigDecimal madeQty = detail.getMadeQty();
        // 租户id
        Long tenantId = detail.getTenantId();
        if (deliverQty.compareTo(planQty) >= 0) {
            //送货数量>=需求数量时
            madeQty = madeQty.add(planQty);
        } else {
            //送货数量<需求数量时
            madeQty = madeQty.add(deliverQty);
        }

        //更新采购单明细需求数量与已制单数量
        SpringUtils.getBean(DeliveryOrderServiceImpl.class)
                .updatePurchaseOrderDetailQty(detail.getId(), totalPlanQty, madeQty, tenantId);

        //更新采购单计划已制单数量
        for (PurchaseOrderDetailPlan plan : add) {
            //剩余可制单数量
            BigDecimal madeAbleQty = plan.getPlanQty().subtract(plan.getMadeQty());
            if (madeAbleQty.compareTo(BigDecimal.ZERO) <= 0){
                continue;
            }
            if (deliverQty.compareTo(madeAbleQty) >= 0) {
                //送货数量>=需求数量时
                plan.setMadeQty(plan.getMadeQty().add(madeAbleQty));
                deliverQty = deliverQty.subtract(madeAbleQty);
            } else {
                //送货数量<需求数量时
                plan.setMadeQty(plan.getMadeQty().add(deliverQty));
                deliverQty = BigDecimal.ZERO;
            }
            PurchaseOrderDetailPlan purchaseOrderDetailPlan = new PurchaseOrderDetailPlan();
            purchaseOrderDetailPlan.setId(plan.getId());
            purchaseOrderDetailPlan.setMadeQty(plan.getMadeQty());
            purchaseOrderDetailPlan.setUpdateBy(SecurityUtils.getUsername());
            purchaseOrderDetailPlan.setUpdateTime(DateUtils.getNowDate());
            purchaseOrderDetailPlan.setTenantId(tenantId);
//            purchaseOrderDetailPlanMapper.updatePurchaseOrderDetailPlan(purchaseOrderDetailPlan);
            if (deliverQty.compareTo(BigDecimal.ZERO) == 0){
                break;
            }
        }
    }

    public void SUBTRACT(PurchaseOrderDetail detail, List<PurchaseOrderDetailPlan> deduct, BigDecimal deliverQty) {
        // 总需求数量
        BigDecimal totalPlanQty = detail.getTotalPlanQty();
        // 已制单数量
        BigDecimal madeQty = detail.getMadeQty();
        Long tenantId = detail.getTenantId();
        if (deliverQty.compareTo(madeQty) >= 0) {
            // 送货数量>=已制单数量时
            madeQty = BigDecimal.ZERO;
        } else {
            // 送货数量<已制单数量时
            madeQty = madeQty.subtract(deliverQty);
        }
        //更新采购单明细需求数量与已制单数量
        SpringUtils.getBean(DeliveryOrderServiceImpl.class)
                .updatePurchaseOrderDetailQty(detail.getId(), totalPlanQty, madeQty, tenantId);

        //更新采购单计划已制单数量
        for (PurchaseOrderDetailPlan plan : deduct) {
            if (deliverQty.compareTo(plan.getMadeQty()) >= 0) {
                //送货数量>=已制单数量时
                deliverQty = deliverQty.subtract(plan.getMadeQty());
                plan.setMadeQty(BigDecimal.ZERO);
            } else {
                //送货数量<已制单数量时
                plan.setMadeQty(plan.getMadeQty().subtract(deliverQty));
                deliverQty = BigDecimal.ZERO;
            }
            PurchaseOrderDetailPlan purchaseOrderDetailPlan = new PurchaseOrderDetailPlan();
            purchaseOrderDetailPlan.setId(plan.getId());
            purchaseOrderDetailPlan.setMadeQty(plan.getMadeQty());
            purchaseOrderDetailPlan.setUpdateBy(SecurityUtils.getUsername());
            purchaseOrderDetailPlan.setUpdateTime(DateUtils.getNowDate());
            purchaseOrderDetailPlan.setTenantId(tenantId);
//            purchaseOrderDetailPlanMapper.updatePurchaseOrderDetailPlan(purchaseOrderDetailPlan);
            if (deliverQty.compareTo(BigDecimal.ZERO) == 0){
                break;
            }
        }
    }



    public void updatePurchaseOrderDetailQty(Long id, BigDecimal totalPlanQty, BigDecimal madeQty) {
        // 需求数量 = 总需求数量-已制单数量
        BigDecimal planQty = totalPlanQty.subtract(madeQty);
        // 更新送货单明细的需求数量和已制间数量
        PurchaseOrderDetail purchaseOrderDetail = new PurchaseOrderDetail();
        purchaseOrderDetail.setId(id);
        purchaseOrderDetail.setPlanQty(planQty);
        purchaseOrderDetail.setMadeQty(madeQty);
        purchaseOrderDetail.setUpdateBy(SecurityUtils.getUsername());
        purchaseOrderDetail.setUpdateTime(DateUtils.getNowDate());
//        purchaseMapper.updateWmsPurchaseOrderDetail(purchaseOrderDetail);
    }

    public void updatePurchaseOrderDetailQty(Long id, BigDecimal totalPlanQty, BigDecimal madeQty, Long tenantId) {
        // 需求数量 = 总需求数量-已制单数量
        BigDecimal planQty = totalPlanQty.subtract(madeQty);
        // 更新送货单明细的需求数量和已制间数量
        PurchaseOrderDetail purchaseOrderDetail = new PurchaseOrderDetail();
        purchaseOrderDetail.setId(id);
        purchaseOrderDetail.setPlanQty(planQty);
        purchaseOrderDetail.setMadeQty(madeQty);
        purchaseOrderDetail.setTenantId(tenantId);
        purchaseOrderDetail.setUpdateBy(SecurityUtils.getUsername());
        purchaseOrderDetail.setUpdateTime(DateUtils.getNowDate());
//        purchaseMapper.updateWmsPurchaseOrderDetail(purchaseOrderDetail);
    }



    /**
     * 删除送货单信息
     *
     * @param id 送货单主键
     * @return 结果
     */
    @Override
    public int deleteDeliveryOrderById(Long id) {
        //TODO  需要校验 deliveryOrder.setStatus("1"); 逻辑删除
        return deliveryOrderMapper.deleteDeliveryOrderById(id);
    }

    /**
     * 发布送货单
     *
     * @param deliveryOrderVo 送货单
     * @return 结果
     */
    @Override
    @GlobalTransactional
    public int updateDeliveryOrderStatus(DeliveryOrderVo deliveryOrderVo) {
        // 1 未发布 2 发布 3 关闭
        String statusType = deliveryOrderVo.getStatusType();
        Long[] ids = deliveryOrderVo.getIds();
        for (Long id : ids) {
            DeliveryOrder deliveryOrder = deliveryOrderMapper.selectDeliveryOrderById(id);
            List<PurchaseOrderDetailVo> purchaseOrderDetailVos = purchaseOrderDetailMapper.queryPurchaseDetailFinishList(ids);

            if (purchaseOrderDetailVos.size() > 0) {
                List<String> collect = purchaseOrderDetailVos.stream().map(PurchaseOrderDetail::getPurchaseOrderNo).collect(Collectors.toList());
                throw new ServiceException(String.format("采购订单%s有明细存在关闭状态，不允许发布", String.join(",", collect)));
            }
            if (Objects.isNull(deliveryOrder)) {
                throw new ServiceException("送货单不存在！");
            }
            String orderStatus = deliveryOrder.getOrderStatus();
            if (OrderStatusConstant.ORDER_STATUS_ACTIVE.equals(statusType) && !OrderStatusConstant.ORDER_STATUS_NEW.equals(orderStatus)) {
                throw new ServiceException("发布失败！送货单状态异常！");
            } else if (OrderStatusConstant.ORDER_STATUS_CLOSE.equals(statusType) && OrderStatusConstant.ORDER_STATUS_CLOSE.equals(orderStatus)) {
                throw new ServiceException("关闭失败！送货单状态异常！");
            }
        }
        // 校验通过
        if (OrderStatusConstant.ORDER_STATUS_ACTIVE.equals(statusType)) {
            // TODO 此条送货单发送到WMS入库单模块
            //封装数据
            List<StockInStdOrderDto> data = buildStockInOrder(ids);
            String stockInStdOrderDtoList = JSONObject.toJSONString(data);
            //调用入库模块 新增入库单、入库单明细
            AjaxResult ajaxResult = stockInService.addStockInStdOrder(stockInStdOrderDtoList);
            if (ajaxResult.isError()) {
                throw new ServiceException(Optional.ofNullable(ajaxResult.get("msg")).isPresent() ? ajaxResult.get("msg").toString() : "新增标准入库单失败！");
            }
        } else if (OrderStatusConstant.ORDER_STATUS_CLOSE.equals(statusType)) {
            // TODO 同步修改对应入库单状态为已关闭，
            AjaxResult ajaxResult = stockInService.closeStockInByDeliveryOrderIds(ids);
            if (ajaxResult.isError()) {
                throw new ServiceException("关闭标准入库单失败！");
            }
            // 修改需求数量记录里的收货数量
            updateReceiveQtyRecord(ids);
        }
        return deliveryOrderMapper.updateDeliveryOrderStatus(deliveryOrderVo);
    }

    /**
     * 送货单转为入库单
     *
     * @param ids
     */
    public List<StockInStdOrderDto> buildStockInOrder(Long[] ids) {
        //送货单list
        List<DeliveryOrder> deliveryOrdersList = deliveryOrderMapper.selectDeliveryOrderByIds(ids);
        List<StockInStdOrderDto> list = new ArrayList<>(deliveryOrdersList.size());
        for (DeliveryOrder deliveryOrder : deliveryOrdersList) {
            //送货单明细list
            List<DeliveryOrderDetailVo> deliveryOrderDetailVoList = deliveryOrderDetailMapper.selectDeliveryOrderDetailByDeliveryOrderId(deliveryOrder.getId());

            // 采购公司
            String companyCode = deliveryOrder.getFactoryCode();
            // 内部供应商，找到所属工厂，其实也是采购公司，如果是纯外部供应商的话，只能通过采购公司来决定数据权限
            Long tenantId = commonMapper.getTenantIdByPlantCode(companyCode);
            if (!Optional.ofNullable(tenantId).isPresent()) {
                throw new ServiceException(String.format("%s未获取到对应租户，请联系管理员", deliveryOrder.getFactoryCode()));
            }
            //标准入库单
            StockInStdOrderDto stockInStdOrderDto = new StockInStdOrderDto();
            stockInStdOrderDto.setOrderNo(deliveryOrder.getDeliveryOrderNo());
            stockInStdOrderDto.setFactoryId(deliveryOrder.getFactoryId());
            stockInStdOrderDto.setFactoryCode(deliveryOrder.getFactoryCode());
            stockInStdOrderDto.setFactoryName(deliveryOrder.getFactoryName());
            stockInStdOrderDto.setVendorId(deliveryOrder.getSupplierId());
            stockInStdOrderDto.setTenantId(tenantId);
            stockInStdOrderDto.setVendorCode(deliveryOrder.getSupplierCode());
            stockInStdOrderDto.setVendorName(deliveryOrder.getSupplierName());
            stockInStdOrderDto.setDeliveryDate(deliveryOrder.getDeliveryDate());
            stockInStdOrderDto.setContact(deliveryOrder.getContact());
            stockInStdOrderDto.setContactPhone(deliveryOrder.getContactPhone());
            stockInStdOrderDto.setDeliveryType(deliveryOrder.getDeliveryType());
            stockInStdOrderDto.setDeliveryAddr(deliveryOrder.getDeliveryAddr());
            stockInStdOrderDto.setDeliveryOrderId(deliveryOrder.getId());
            stockInStdOrderDto.setRemark(deliveryOrder.getRemark());
            //标准入库单明细
            List<StockInStdOrderDetailDto> detailList = new ArrayList<>(deliveryOrderDetailVoList.size());
            for (DeliveryOrderDetailVo deliveryOrderDetailVo : deliveryOrderDetailVoList) {
                StockInStdOrderDetailDto s = new StockInStdOrderDetailDto();
                s.setMaterialId(deliveryOrderDetailVo.getMaterialId());
                s.setMaterialNo(deliveryOrderDetailVo.getMaterialNo());
                s.setMaterialName(deliveryOrderDetailVo.getMaterialName() == null ? "" : deliveryOrderDetailVo.getMaterialName());
                s.setOldMaterialNo(deliveryOrderDetailVo.getOldMaterialNo());
                s.setTenantId(tenantId);
                s.setDeliverQty(deliveryOrderDetailVo.getDeliverQty());
                //TODO 包装数量 上游单据没有
                s.setReceivedQty(new BigDecimal(0));
                s.setIsQc(deliveryOrderDetailVo.getIsQc());
                s.setIsConsign(deliveryOrderDetailVo.getIsConsign());
                //TODO 有删除的采购协议计划行时 可能会导致采购单类型缺失 此处补上
                if (StringUtils.isEmpty(deliveryOrderDetailVo.getOrderType())){
                    DeliveryOrderDetailVo item = deliveryOrderDetailVoList.stream()
                            .filter(d -> d.getPurchaseOrderNo().equals(deliveryOrderDetailVo.getPurchaseOrderNo()) && !StringUtils.isEmpty(d.getOrderType()))
                            .findFirst().orElse(null);
                    if (null == item){
                        throw new ServiceException(String.format("送货单%s中,采购单号%s有变化请重建",deliveryOrder.getDeliveryOrderNo(),deliveryOrderDetailVo.getPurchaseOrderNo()));
                    }
                    deliveryOrderDetailVo.setOrderType(item.getOrderType());
                }
                s.setDetailType(deliveryOrderDetailVo.getOrderType());
                s.setMinPacking(deliveryOrderDetailVo.getMinPacking());
                s.setUnit(deliveryOrderDetailVo.getUnit());
                s.setFileId(deliveryOrderDetailVo.getFileId());
                s.setPurchaseOrderNo(deliveryOrderDetailVo.getPurchaseOrderNo());
                s.setPurchaseLineNo(deliveryOrderDetailVo.getPurchaseLineNo());
                s.setIsQequireFill(deliveryOrderDetailVo.getIsQequireFill());
                s.setIsPrinted(deliveryOrderDetailVo.getIsPrinted());
                s.setThirdDeliveryOrderNo(deliveryOrderDetailVo.getThirdDeliveryOrderNo());
                s.setThirdDeliveryLineNo(deliveryOrderDetailVo.getThirdDeliveryLineNo());
                s.setRemark(deliveryOrderDetailVo.getRemark());
                s.setLotNo(deliveryOrderDetailVo.getBatchNo());
                s.setLocationCode(deliveryOrderDetailVo.getLocationCode());
                //获取送货要求
                WmsLkDeliveryDetailRequirement requirement = new WmsLkDeliveryDetailRequirement();
                requirement.setDeliveryDetailId(deliveryOrderDetailVo.getId());
                List<WmsLkDeliveryDetailRequirement> requirementsList = wmsLkDeliveryDetailRequirementMapper.selectWmsLkDeliveryDetailRequirementList(requirement);
                if (!ObjectUtils.isEmpty(requirementsList)){
                    //根据关联的物料送货要求id升序排序
                    List<String> contentList = requirementsList.stream()
                            .sorted(Comparator.comparing(WmsLkDeliveryDetailRequirement::getDeliveryRequirementId))
                            .map(WmsLkDeliveryDetailRequirement::getRequirementContent).collect(Collectors.toList());
                    String contents = String.join(",", contentList);
                    if (!StringUtils.isEmpty(contents)){
                        s.setRequirementContent(contents);
                    }
                }
                detailList.add(s);
            }
            stockInStdOrderDto.setStockInStdOrderDetailDtoList(detailList);
            list.add(stockInStdOrderDto);
        }
        return list;
    }


    /**
     * 查询送货单明细列表
     *
     * @param deliveryOrderDetail 送货单明细
     * @return 送货单明细
     */
    @Override
    public List<DeliveryOrderDetailVo> selectDeliveryOrderDetailList(DeliveryOrderDetail deliveryOrderDetail) {

        DeliveryOrderDetailVo deliveryDetailVo = new DeliveryOrderDetailVo();
        BeanUtils.copyProperties(deliveryOrderDetail, deliveryDetailVo);

        //判断是否为外部供应商
        String vendorFlag = SecurityUtils.getLoginUser().getSysUser().getVendorFlag();
        if (StringUtils.isEmpty(vendorFlag)){
            throw new ServiceException("信息错误，请重新登录！");
        } else if (CommonYesOrNo.YES.equals(vendorFlag)){
            deliveryDetailVo.setSupplierCode(SecurityUtils.getLoginUser().getSysUser().getUserName());
        }


        deliveryOrderDetail.setStatus("0");
        List<DeliveryOrderDetailVo> list = deliveryOrderDetailMapper.selectDeliveryOrderDetailList(deliveryDetailVo);
        List<DeliveryOrderDetailVo> voList = new ArrayList<>();
        voList.addAll(list);
        voList.forEach(deliveryOrderDetailVo -> {

            // 根据送货单明细id查询送货要求
            WmsLkDeliveryDetailRequirement detailRequirement = new WmsLkDeliveryDetailRequirement();
            detailRequirement.setDeliveryDetailId(deliveryOrderDetailVo.getId());
            List<WmsLkDeliveryDetailRequirement> requirementsList = wmsLkDeliveryDetailRequirementService.selectWmsLkDeliveryDetailRequirementList(detailRequirement);
            deliveryOrderDetailVo.setRequirementList(requirementsList);
            // 附件上传
            WmsAttachsVo wmsAttachsVo = new WmsAttachsVo();
            wmsAttachsVo.setOrderId(deliveryOrderDetailVo.getId());
            List<WmsAttachs> wmsAttachs = wmsAttachsService.selectWmsAttachsList(wmsAttachsVo);
            List<FileVo> fileVos = new ArrayList<>();
            for (WmsAttachs attachs : wmsAttachs) {
                FileVo fileVo = new FileVo();
                fileVo.setName(attachs.getName());
                fileVo.setUrl(attachs.getUrl());
                fileVos.add(fileVo);
            }
            deliveryOrderDetailVo.setFileList(fileVos);
            deliveryOrderDetailVo.setBeforeDeliverQty(deliveryOrderDetailVo.getDeliverQty());
        });
        return voList;
    }

    /**
     * 根据物料、公司查询是否存在送货要求
     *
     * @param deliveryRequirementCheckDto 送货单明细
     * @return 送货单明细
     */
    @Override
    public List<DeliveryOrderDetailVo> checkDeliveryRequirement(DeliveryRequirementCheckDto deliveryRequirementCheckDto) {
        Long tenantId = deliveryRequirementCheckDto.getTenantId();
        List<DeliveryOrderDetailVo> deliveryOrderDetailVos = deliveryRequirementCheckDto.getDeliveryOrderDetailVos();
        List<DeliveryRequirementInfoDto> deliveryRequirementDtos = new ArrayList<>();
        List<GoodsSourceDef> materialInfos = new ArrayList<>();
        List<String> strList = new ArrayList<>();
        for (DeliveryOrderDetailVo deliveryOrderDetailVo : deliveryOrderDetailVos) {

            String str = String.format("%s-%s-%s", deliveryOrderDetailVo.getSupplierCode(), deliveryOrderDetailVo.getMaterialNo(), deliveryOrderDetailVo.getFactoryCode());
            if (strList.contains(str)) {
                continue;
            }
            DeliveryRequirementInfoDto deliveryRequirementDto = new DeliveryRequirementInfoDto();
            BeanUtils.copyProperties(deliveryOrderDetailVo, deliveryRequirementDto);
            deliveryRequirementDtos.add(deliveryRequirementDto);
            GoodsSourceDef goodsSourceDef = new GoodsSourceDef();
            goodsSourceDef.setSupplierCode(deliveryOrderDetailVo.getSupplierCode());
            goodsSourceDef.setMaterialNo(deliveryOrderDetailVo.getMaterialNo());
            goodsSourceDef.setTenantId(tenantId);
            goodsSourceDef.setFactoryCode(deliveryOrderDetailVo.getFactoryCode());
            materialInfos.add(goodsSourceDef);
            strList.add(str);
        }
        AjaxResult materialInfoResult = mainDataService.queryMaterialInfo(materialInfos);
        if (materialInfoResult.isError()) {
            throw new ServiceException(materialInfoResult.get("msg").toString());
        }
        List<GoodsSourceDef> attachList = JSON.parseArray(materialInfoResult.get("data").toString(), GoodsSourceDef.class);
        for (GoodsSourceDef goods : attachList) {
            goods.setMaterialNo(String.format("%s-%s", goods.getMaterialNo(), goods.getSupplierCode()));
        }
        attachList = attachList.stream().filter(goodsSourceDef -> Objects.nonNull(goodsSourceDef.getAttachFlag())).collect(Collectors.toList());
        if (attachList.isEmpty()) {
            throw new ServiceException("该物料未配置附件上传，请先配置！");
        }
        Map<String, String> attachMap = attachList.stream().collect(Collectors.toMap(GoodsSourceDef::getMaterialNo, GoodsSourceDef::getAttachFlag));

        // 主数据查询送货要求
        AjaxResult ajaxResult = mainDataService.checkDeliveryRequirement(deliveryRequirementDtos);
        if (ajaxResult.isError()) {
            throw new ServiceException(ajaxResult.get("msg").toString());
        }
        List<DeliveryRequirementInfoDto> requirementList = JSON.parseArray(ajaxResult.get("data").toString(), DeliveryRequirementInfoDto.class);
        for (DeliveryRequirementInfoDto dto : requirementList) {
            dto.setMaterialNo(String.format("%s-%s", dto.getMaterialNo(), dto.getFactoryCode()));
        }
        Map<String, Boolean> requirementMap = requirementList.stream().collect(Collectors.toMap(DeliveryRequirementInfoDto::getMaterialNo, DeliveryRequirementInfoDto::getExistRequire));

        for (DeliveryOrderDetailVo deliveryOrderDetailVo : deliveryOrderDetailVos) {
            Boolean aBoolean = Optional.ofNullable(requirementMap.get(String.format("%s-%s", deliveryOrderDetailVo.getMaterialNo(), deliveryOrderDetailVo.getCompanyCode()))).orElse(false);
            String attachFlag = Optional.ofNullable(attachMap.get(String.format("%s-%s", deliveryOrderDetailVo.getMaterialNo(), deliveryOrderDetailVo.getSupplierCode()))).orElse("0");
            if (aBoolean) {
                deliveryOrderDetailVo.setIsQequireFill(CommonYesOrNo.YES);
            } else {
                deliveryOrderDetailVo.setIsQequireFill(CommonYesOrNo.NO);
            }
            deliveryOrderDetailVo.setAttachFlag(attachFlag);
        }
        return deliveryOrderDetailVos;
    }

    /**
     * 根据物料、公司查询送货要求列表
     *
     * @param deliveryRequirementInfoDto 送货单明细
     * @return 送货单明细
     */
    @Override
    public List<DeliveryRequirementInfoDto> queryDeliveryRequirement(DeliveryRequirementInfoDto deliveryRequirementInfoDto) {

        // 主数据查询
        AjaxResult ajaxResult = mainDataService.queryDeliveryRequirement(deliveryRequirementInfoDto);
        if (ajaxResult.isError()) {
            throw new ServiceException(ajaxResult.get("msg").toString());
        }
        List<DeliveryRequirementInfoDto> requirementList = JSON.parseArray(ajaxResult.get("data").toString(), DeliveryRequirementInfoDto.class);
        return requirementList;
    }

    @Override
    public int updateStatusToClose(DeliveryOrderVo deliveryOrderVo) {
        if (ObjectUtils.isEmpty(deliveryOrderVo)) {
            throw new ServiceException("参数不存在");
        }
        DeliveryOrder deliveryOrder = new DeliveryOrder();
        deliveryOrder.setId(deliveryOrderVo.getId());
        deliveryOrder.setDeliveryOrderNo(deliveryOrderVo.getDeliveryOrderNo());
        deliveryOrder.setOrderStatus(deliveryOrderVo.getStatus());
        deliveryOrder.setUpdateBy(SecurityUtils.getUsername());
        deliveryOrder.setUpdateTime(DateUtils.getNowDate());
        if (deliveryOrderMapper.updateDeliveryOrder(deliveryOrder) <= 0) {
            throw new ServiceException("修改送货单状态失败");
        }

        // 如果为关闭，增修改送货单明细状态
        if (OrderStatusConstant.ORDER_STATUS_CLOSE.equals(deliveryOrderVo.getStatus())) {
            // 删除送货单明细
            DeliveryOrderDetail deliveryOrderDetail = new DeliveryOrderDetail();
            deliveryOrderDetail.setDeliveryOrderId(deliveryOrderVo.getId());
            deliveryOrderDetail.setDeliveryOrderNo(deliveryOrderVo.getDeliveryOrderNo());
            deliveryOrderDetail.setStatus(CommonYesOrNo.YES);
            deliveryOrderDetail.setUpdateTime(DateUtils.getNowDate());
            deliveryOrderDetailMapper.updateDeliveryOrderDetailByOrderId(deliveryOrderDetail);
        }
        return 1;
    }

    @Override
    public int checkDeliveryOrderDetail(Long[] ids) {
        List<DeliveryOrderDetailVo> detailVos = deliveryOrderDetailMapper.checkDeliveryOrderDetail(ids);
        if (!detailVos.isEmpty()) {
            throw new ServiceException("送货单:" + detailVos.get(0).getDeliveryOrderNo() + "中有采购协议且已发布，请先完成！");
        }
        return 1;
    }

    /**
     * 采购单处理页面送货单详情查询
     * @param deliveryOrderDetailVo 送货单明细vo
     * @return 送货单明细集合
     */
    @Override
    public List<DeliveryOrderDetailVo> getDeliveryOrderDetailByPurchaseOrderNo(DeliveryOrderDetailVo deliveryOrderDetailVo) {
        return deliveryOrderDetailMapper.getDeliveryOrderDetailByPurchaseOrderNo(deliveryOrderDetailVo);
    }

    // 修改需求数量记录里的收货数量
    private void updateReceiveQtyRecord(Long[] ids) {
        List<PlanQtyRecordDto> planQtyRecordDtoList = new ArrayList<>();
        PlanQtyRecordDto planQtyRecordDto = new PlanQtyRecordDto();

        DeliveryOrderDetailVo deliveryDetailVo;
        for (Long id : ids) {
            deliveryDetailVo = new DeliveryOrderDetailVo();
            deliveryDetailVo.setDeliveryOrderId(id);
            List<DeliveryOrderDetailVo> list = deliveryOrderDetailMapper.selectDeliveryOrderDetailList(deliveryDetailVo);

            for (DeliveryOrderDetailVo deliveryOrderDetailVo : list) {
                // 记录送货单明细计划数量
                planQtyRecordDto = new PlanQtyRecordDto();
                BeanUtils.copyProperties(deliveryOrderDetailVo, planQtyRecordDto);
                planQtyRecordDto.setDeliverQty(BigDecimal.ZERO);
                planQtyRecordDto.setChangeType(PlanqtyRecordEnum.TYPE_WMS_UPDATE_RECEIVING.getKey());
                planQtyRecordDtoList.add(planQtyRecordDto);
            }
        }
        wmsPurchaseRecordService.calcReceiveQty(planQtyRecordDtoList);
    }

    /**
     * 批量删除送货单
     *
     * @param ids 需要删除的送货单主键
     * @return 结果
     */
    @Override
    public int deleteDeliveryOrderByIds(Long[] ids) {
        if (ObjectUtils.isEmpty(ids)){
            throw new ServiceException("参数不存在！");
        }
        //校验送货单状态
        int results = deliveryOrderMapper.selectDeliveryOrderIsNew(ids);
        if (results > 0){
            throw new ServiceException("只能删除新建状态送货单！");
        }
        for (Long id : ids) {
            DeliveryOrderDetail orderDetail = new DeliveryOrderDetail();
            orderDetail.setDeliveryOrderId(id);
            List<DeliveryOrderDetailVo> list = deliveryOrderDetailMapper.selectDeliveryOrderDetailList(orderDetail);
            if (ObjectUtils.isEmpty(list)){
                throw new ServiceException("送货单明细不存在！");
            }
            Long[] longs = list.stream().map(DeliveryOrderDetailVo::getDeliveryOrderId).toArray(Long[]::new);
            for (DeliveryOrderDetailVo deliveryOrderDetailVo : list) {

//                this.updatePurchaseOrderQuantity(deliveryOrderDetailVo, "delete");
            }
            // 送货单明细逻辑删除
            this.deliveryOrderDetailMapper.updateDeliveryDetailStatus(longs);
        }
        return deliveryOrderMapper.deleteDeliveryOrderByIds(ids);
    }

    @Override
    public List<DeliveryOrderDetail> queryDeliveryDetailById(Long id) {
        return deliveryOrderDetailMapper.queryDeliveryDetailById(id);
    }

    /**
     * 修改送货单
     *
     * @param deliveryOrderVo 送货单
     * @return 结果
     */
    @Override
    @GlobalTransactional
    public int updateDeliveryOrder(DeliveryOrderVo deliveryOrderVo) {
        List<DeliveryOrderDetailVo> deliveryOrderDetailVoList = deliveryOrderVo.getDeliveryOrderDetailVoList();
        if (deliveryOrderDetailVoList == null || deliveryOrderDetailVoList.isEmpty()) {
            throw new ServiceException("送货单明细不能为空！");
        }
        Date now = DateUtils.getNowDate();
        String userName = SecurityUtils.getUsername();
        DeliveryOrder deliveryOrder = new DeliveryOrder();
        BeanUtils.copyProperties(deliveryOrderVo, deliveryOrder);
        deliveryOrder.setUpdateBy(userName);
        deliveryOrder.setUpdateTime(now);
        deliveryOrder.setTenantId(null);
        if (Objects.isNull(deliveryOrderMapper.selectDeliveryOrderById(deliveryOrder.getId()))) {
            throw new ServiceException("送货单不存在！");
        }
        //获取当前送货单最大行号
        List<DeliveryOrderDetailVo> collect = deliveryOrderDetailVoList.stream()
                .filter(d -> !ObjectUtils.isEmpty(d.getDeliveryLineNo()))
                .sorted(Comparator.comparing(DeliveryOrderDetailVo::getDeliveryLineNo).reversed())
                .collect(Collectors.toList());
        Long deliveryLineNo = collect.get(0).getDeliveryLineNo();

        for (DeliveryOrderDetailVo deliveryOrderDetailVo : deliveryOrderDetailVoList) {
            Long id = deliveryOrderDetailVo.getId();
            if (Objects.isNull(id)) {
                Long tenantId = SecurityUtils.getLoginUser().getSysUser().getTenantId();

                // 新增
                AjaxResult ajaxResult = iCodeRuleService.getSeqWithTenantId(StockInTaskConstant.ENTER_LOT_NO, String.valueOf(tenantId));
                if (ajaxResult.isError() || StringUtils.isEmpty(ajaxResult.get("data").toString())) {
                    throw new ServiceException("入库批次号生成失败！");
                }

                //当新增的送货单中送货的明细对应采购单为采购协议时 && 为同物料为同采购单号 则分配同一批次
                if (StockInOrderConstant.ORDER_TYPE_AGREEMENT.equals(deliveryOrderDetailVo.getOrderType())) {
                    DeliveryOrderDetailVo orderDetailVo = new DeliveryOrderDetailVo();
                    orderDetailVo.setDeliveryOrderNo(deliveryOrderVo.getDeliveryOrderNo());
                    orderDetailVo.setPurchaseOrderNo(deliveryOrderDetailVo.getPurchaseOrderNo());
                    orderDetailVo.setMaterialNo(deliveryOrderDetailVo.getMaterialNo());
                    orderDetailVo.setOrderType(StockInOrderConstant.ORDER_TYPE_AGREEMENT);
                    List<DeliveryOrderDetailVo> agreementList = deliveryOrderDetailMapper.selectDeliveryOrderDetailLkPurchaseOrder(orderDetailVo);
                    if (agreementList != null && agreementList.size() > 0) {
                        deliveryOrderDetailVo.setBatchNo(agreementList.get(0).getBatchNo());
                    }
                } else {
                    deliveryOrderDetailVo.setBatchNo(ajaxResult.get("data").toString());
                }
                deliveryLineNo += 1L;
                deliveryOrderDetailVo.setDeliveryOrderId(deliveryOrder.getId());
                deliveryOrderDetailVo.setDeliveryOrderNo(deliveryOrder.getDeliveryOrderNo());
                deliveryOrderDetailVo.setDeliveryLineNo(deliveryLineNo);
                deliveryOrderDetailVo.setCreateBy(userName);
                deliveryOrderDetailVo.setCreateTime(now);
//                deliveryOrderDetailVo.setVoucherNo();
//                deliveryOrderDetailVo.setDeliveryOrderStatus();
//                deliveryOrderDetailVo.setInspectOrderNo();
                this.deliveryOrderDetailMapper.insertDeliveryOrderDetail(deliveryOrderDetailVo);
                try {
//                    this.updatePurchaseOrderQuantity(deliveryOrderDetailVo, "add");
                } catch (ServiceException e) {
                    throw new RuntimeException(e);
                }
            } else {
                // 修改
                DeliveryOrderDetail orderDetail = new DeliveryOrderDetail();
                orderDetail.setId(deliveryOrderDetailVo.getId());
                orderDetail.setDeliverQty(deliveryOrderDetailVo.getDeliverQty());
                orderDetail.setRemark(deliveryOrderDetailVo.getRemark());
                orderDetail.setUpdateBy(userName);
                orderDetail.setUpdateTime(now);
                this.deliveryOrderDetailMapper.updateDeliveryOrderDetail(orderDetail);
                try {
//                    this.updatePurchaseOrderQuantity(deliveryOrderDetailVo, "edit");
                } catch (ServiceException e) {
                    throw new RuntimeException(e);
                }
            }

        }
        // 删除的ID集合
        Long[] ids = deliveryOrderVo.getIds();
        if (!Objects.isNull(ids)) {
            // 回退已制单数量和需求数量 查询出删除的所有删除的记录循环计算采购单数量
//            List<DeliveryOrderDetailVo> deliveryOrderDetailVos = this.deliveryOrderDetailMapper.selectDeliveryOrderDetailByIds(ids);
//            for (DeliveryOrderDetailVo deliveryOrderDetailVo : deliveryOrderDetailVos) {
//                this.updatePurchaseOrderQuantity(deliveryOrderDetailVo, "delete");
//            }
            // 送货单明细逻辑删除
            this.deliveryOrderDetailMapper.updateDeliveryDetailStatus(ids);
        }
        // 修改送货单
        return deliveryOrderMapper.updateDeliveryOrder(deliveryOrder);
    }


    /**
     * 发送送检单
     * @param id
     */
    @Override
    @GlobalTransactional
    public AjaxResult sendMessage(Long id) throws Exception {
        Long tenantId = SecurityUtils.getLoginUser().getSysUser().getTenantId();

        String taskCode = null;

        try {
            // 根据送货单id查询送货单明细
            List<DeliveryOrderDetailVo> deliveryOrderDetailVos = deliveryOrderDetailMapper.queryDeliveryDetailByDeliveryOrderId(id);
            for (DeliveryOrderDetailVo vo : deliveryOrderDetailVos) {
                StockInStdOrderDetailDto detailDto = new StockInStdOrderDetailDto();
                detailDto.setId(vo.getPurchaseOrderDetailId());
                List<StockInStdOrderDetail> stdOrderDetailList = stockInStdOrderDetailMapper.selectStockInStdOrderDetailList(detailDto);
                // 新增
                AjaxResult ajaxResult = iCodeRuleService.getSeqWithTenantId(OrderNoTypeConstant.INSPECT, String.valueOf(tenantId));
                if (ajaxResult.isError() || StringUtils.isEmpty(ajaxResult.get("data").toString())) {
                    throw new ServiceException("检验单号生成失败！");
                }
                taskCode = ajaxResult.get("data").toString();
                logger.info("新增检验单号: {}", taskCode);
                // 创建收货信息
                MsgObject msgObject = new MsgObject(IMsgObject.MOType.initSR);
                // 收货单任务号(送货单单号)
                msgObject.setReqValue("TaskCode", taskCode);
                // 收货单号(送货单行号)
                msgObject.setReqValue("ReceiveCode", vo.getDeliveryLineNo().toString());
                // 公司
                msgObject.setReqValue("Company", vo.getFactoryName());
                // 工厂（AC00，MA00）字典
                msgObject.setReqValue("Plant", vo.getFactoryCode());
                // 物料编码
                msgObject.setReqValue("PartNumber", vo.getMaterialNo());
                // 物料名称
                msgObject.setReqValue("PartName", vo.getMaterialName());
                // 供应商代码
                msgObject.setReqValue("SupplyCode", vo.getSupplierCode());
                // 供应商名称
                msgObject.setReqValue("SupplyName", vo.getSupplierName());
                // 生产批次
//              msgObject.setReqValue("ProductBatch", "4");
                // 入库批次（批次号）
                msgObject.setReqValue("InputBatch", vo.getBatchNo());
                // 预留检验原因
                msgObject.setReqValue("InspectReason", "送检");
                // 需求数量(送货数量)
                msgObject.setReqValue("TotalQuantity", vo.getReceivedQty().toBigInteger().toString());
                // 库存地点（四位数字）
                msgObject.setReqValue("Warehouse", vo.getLocationCode());
                // 收货人
                msgObject.setReqValue("ReceiveUser", "admin");
                // 收货时间
                msgObject.setReqValue("ReceiveTime", DateUtils.dateTime());
                // 备注
                msgObject.setReqValue("Remark", vo.getRemark());
                // 发送送检任务
                MsgObject sentMsg = sendInspectTask(msgObject);
                // 获取抽检数量
                String extractCountStr = sentMsg.getResValue("ExtractCount");

                BigDecimal extractCount = Convert.toBigDecimal(extractCountStr);
                if (Convert.toLong(extractCount) == -1){
                    throw new ServiceException("零件信息不存在，不需要检验");
                }

                // 抽检数量不为零，新建送检单
                InspectOrder inspectOrder = new InspectOrder();
                inspectOrder.setBillStatus("0");
                inspectOrder.setOrderNo(taskCode);
                inspectOrder.setQcQty(extractCount);
                inspectOrder.setMaterialNo(vo.getMaterialNo());
                inspectOrder.setMaterialName(vo.getMaterialName());
                inspectOrder.setFactoryCode(vo.getFactoryCode());
                inspectOrder.setSupplierCode(vo.getSupplierCode());
                inspectOrder.setSupplierName(vo.getSupplierName());
                inspectOrder.setPurchaseOrderNo(vo.getPurchaseOrderNo());
                inspectOrder.setReceiveOrderNo(vo.getDeliveryOrderNo());
                inspectOrder.setPurchaseLineNo(vo.getPurchaseLineNo());
                inspectOrder.setLocationCode(vo.getLocationCode());
                inspectOrder.setReceiveDate(DateUtils.getNowDate());
                inspectOrder.setCreateTime(DateUtils.getNowDate());
                inspectOrder.setUpdateTime(DateUtils.getNowDate());
                inspectOrderService.insertInspectOrder(inspectOrder);

                // 送检单详情
                InspectOrderDetails inspectOrderDetails = new InspectOrderDetails();
                inspectOrderDetails.setOrderNo(taskCode);
                inspectOrderDetails.setMaterialNo(vo.getMaterialNo());
                inspectOrderDetails.setQcQty(extractCount);
                inspectOrderDetails.setLot(vo.getBatchNo());
                inspectOrderDetails.setPrdLot(vo.getBatchNo());
                inspectOrderDetails.setLineNo(Convert.toStr(vo.getDeliveryLineNo()));
                inspectOrderDetails.setCreateBy(SecurityUtils.getUsername());
                inspectOrderDetails.setCreateTime(DateUtils.getNowDate());
                inspectOrderDetails.setOperationUnit(vo.getUnit());
                inspectOrderDetails.setUnit(vo.getUnit());
                inspectOrderDetailsService.insertInspectOrderDetails(inspectOrderDetails);
                return AjaxResult.success("正前往送检单填写送检数量");
            }
        } catch (Exception e) {
            // 取消质检任务 回滚
            if(!StringUtils.isEmpty(taskCode) && !StringUtils.startsWith(e.getMessage(), "wms-esb executing")) {
                inspectOrderService.cancelInspectTask(taskCode);
            }
            logger.error(e.getMessage());
        }
        return AjaxResult.error("未知错误，请联系管理员");
    }

    @Override
    public AjaxResult updateDeliveryOrderDetail(DeliveryOrderDetail deliveryOrderDetail){
        try {
            List<DeliveryOrderDetail> queryDeliveryDetailById = deliveryOrderDetailMapper.queryDeliveryDetailByIdAlt(deliveryOrderDetail.getId());
            if(queryDeliveryDetailById.size()>0){
                BigDecimal deliverQtyOrigin = queryDeliveryDetailById.get(0).getDeliverQty();
                BigDecimal deliverQty = deliveryOrderDetail.getDeliverQty();
                if (deliverQtyOrigin.compareTo(deliverQty) < 0) {
                    throw new ServiceException("保存数量不能大于原本的数量，保存失败！");
                } else {
                    deliveryOrderDetailMapper.updateDeliveryOrderDetail(deliveryOrderDetail);
                }
            }else{
                throw new ServiceException("收货单详情不存在，保存失败！");
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return AjaxResult.success("保存成功");
    }

    @Override
    public List<DeliveryOrder> openQuery(PopupBoxVo popupBoxVo) {
        Map<String, Object> paramsMap = processingFormat(popupBoxVo);
        return deliveryOrderMapper.openQuery(paramsMap);
    }

    public Long updateInventoryDetailsRaw(DeliveryOrderVo deliveryOrder, DeliveryOrderDetailVo deliveryOrderDetail,
                                          StoragePositionVo storagePositionVo) {
        //查询当前仓位是否已有台账存在
        InventoryDetailsVo inventoryDetailsVo = new InventoryDetailsVo();
        inventoryDetailsVo.setMaterialNo(deliveryOrderDetail.getMaterialNo());
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
        inventoryDetailsVo.setStockInLot(deliveryOrderDetail.getBatchNo());
        inventoryDetailsVo.setIsConsign(deliveryOrderDetail.getIsConsign());
        inventoryDetailsVo.setIsFreeze(CommonYesOrNo.NO);
        // 排除特殊库存
        inventoryDetailsVo.setStockSpecFlag(CommonYesOrNo.NO);
        inventoryDetailsVo.setIsQc(deliveryOrderDetail.getIsQc());
        AjaxResult listResult = inStoreService.list(inventoryDetailsVo);
        if (listResult.isError()) {
            throw new ServiceException("查询库存台账失败！");
        }
        BigDecimal receivedQty = deliveryOrderDetail.getReceivedQty();
        List<InventoryDetails> inventoryDetailsList = com.alibaba.fastjson.JSONObject.parseArray(listResult.get("data").toString(), InventoryDetails.class);
        Long inventoryId;
        if (ObjectUtils.isEmpty(inventoryDetailsList)) {
            //新增台账
            InventoryDetails inventoryDetails = new InventoryDetails();
            inventoryDetails.setMaterialNo(deliveryOrderDetail.getMaterialNo());
            inventoryDetails.setMaterialName(deliveryOrderDetail.getMaterialName());
            inventoryDetails.setOldMaterialNo(deliveryOrderDetail.getOldMaterialNo());
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
            inventoryDetails.setUnit(deliveryOrderDetail.getUnit());
            inventoryDetails.setSupplierId(deliveryOrderDetail.getSupplierId());
            inventoryDetails.setSupplierCode(deliveryOrderDetail.getSupplierCode());
            inventoryDetails.setSupplierName(deliveryOrderDetail.getSupplierName());
            inventoryDetails.setStockInDate(DateUtils.getNowDate());
            inventoryDetails.setStockInLot(deliveryOrderDetail.getBatchNo());
            inventoryDetails.setIsQc(deliveryOrderDetail.getIsQc());
            inventoryDetails.setIsConsign(deliveryOrderDetail.getIsConsign());
            inventoryDetails.setIsFreeze(CommonYesOrNo.NO);
            inventoryDetails.setIsReserved(CommonYesOrNo.NO);
            inventoryDetails.setStockSpecFlag(CommonYesOrNo.NO);
            inventoryDetails.setCreateBy(SecurityUtils.getUsername());
            inventoryDetails.setCreateTime(DateUtils.getNowDate());
            inventoryDetails.setUpdateBy(SecurityUtils.getUsername());
            inventoryDetails.setSupplierCode(deliveryOrder.getSupplierCode());
            inventoryDetails.setSupplierName(deliveryOrder.getSupplierName());
            inventoryDetails.setProductTime(deliveryOrderDetail.getProductTime());
            inventoryDetails.setExpiryDate(deliveryOrderDetail.getExpireTime());

            if (CommonYesOrNo.YES.equals(deliveryOrderDetail.getIsQc())) {
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
            BigDecimal sumInventoryQty = inventoryDetails.getInventoryQty().add(deliveryOrderDetail.getReceivedQty());
            BigDecimal sumAvailableQty = inventoryDetails.getAvailableQty().add(deliveryOrderDetail.getReceivedQty());
            inventoryDetails.setInventoryQty(sumInventoryQty);
            inventoryDetails.setAvailableQty(sumAvailableQty);
            inventoryDetails.setUpdateBy(SecurityUtils.getUsername());
            inventoryDetails.setIsQc(deliveryOrderDetail.getIsQc());
            inventoryDetails.setUpdateTime(DateUtils.getNowDate());
            if (CommonYesOrNo.YES.equals(deliveryOrderDetail.getIsQc())) {
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

    /**
     * 更新台账 原材料
     *
     * @param shelfTask
     * @param storagePositionVo
     * @return inventoryId 台账id
     */
    public Long updateInventoryDetailsRaw(DeliveryOrderDetailVo detailVo, ShelfTask shelfTask, StoragePositionVo storagePositionVo) {
        MaterialAttrDto materialAttrDto = new MaterialAttrDto();
        materialAttrDto.setMaterialNo(shelfTask.getMaterialNo());
        materialAttrDto.setFactoryId(detailVo.getFactoryId());
        materialAttrDto.setFactoryCode(detailVo.getFactoryCode());
        List<MaterialAttrDto> materialList = esbSendCommonMapper.getMaterialAttr(materialAttrDto);
        if (materialList == null || materialList.isEmpty()) {
            throw new ServiceException(String.format("“%s”:物料主数据“物料属性”未维护!", detailVo.getMaterialNo()));
        }
        //查询当前仓位是否已有台账存在
        InventoryDetailsVo inventoryDetailsVo = new InventoryDetailsVo();
        inventoryDetailsVo.setMaterialNo(detailVo.getMaterialNo());
        inventoryDetailsVo.setFactoryId(detailVo.getFactoryId());
        inventoryDetailsVo.setFactoryCode(detailVo.getFactoryCode());
        inventoryDetailsVo.setWarehouseId(storagePositionVo.getWarehouseId());
        inventoryDetailsVo.setWarehouseCode(storagePositionVo.getWarehouseCode());
        inventoryDetailsVo.setAreaId(storagePositionVo.getAreaId());
        inventoryDetailsVo.setAreaCode(storagePositionVo.getAreaCode());
        inventoryDetailsVo.setLocationId(storagePositionVo.getLocationId());
        inventoryDetailsVo.setLocationCode(storagePositionVo.getLocationCode());
        inventoryDetailsVo.setPositionId(storagePositionVo.getId());
        inventoryDetailsVo.setPositionNo(storagePositionVo.getPositionNo());
        inventoryDetailsVo.setStockInLot(shelfTask.getLot());
        inventoryDetailsVo.setIsConsign(detailVo.getIsConsign());
        inventoryDetailsVo.setIsFreeze(CommonYesOrNo.NO);
        // 排除特殊库存
        inventoryDetailsVo.setStockSpecFlag(CommonYesOrNo.NO);
        inventoryDetailsVo.setIsQc(detailVo.getIsQc());
        inventoryDetailsVo.setProductionLot(shelfTask.getLot());
        AjaxResult listResult = inStoreService.list(inventoryDetailsVo);
        if (listResult.isError()) {
            throw new ServiceException("查询库存台账失败！");
        }
        BigDecimal receivedQty = shelfTask.getQty();
        List<InventoryDetails> inventoryDetailsList = com.alibaba.fastjson.JSONObject.parseArray(listResult.get("data").toString(), InventoryDetails.class);
        Long inventoryId;

        if (ObjectUtils.isEmpty(inventoryDetailsList)) {
            //新增台账
            InventoryDetails inventoryDetails = new InventoryDetails();
            inventoryDetails.setMaterialNo(shelfTask.getMaterialNo());
            inventoryDetails.setMaterialName(shelfTask.getMaterialName());
            inventoryDetails.setOldMaterialNo(shelfTask.getOldMaterialNo());
            inventoryDetails.setPositionNo(shelfTask.getPositionNo());
            inventoryDetails.setPositionId(shelfTask.getId());
//            inventoryDetails.setAreaId(shelfTask.getAreaId());
            inventoryDetails.setAreaCode(shelfTask.getAreaCode());
//            inventoryDetails.setLocationId(shelfTask.getLocationId());
            inventoryDetails.setLocationCode(shelfTask.getLocationCode());
            inventoryDetails.setFactoryId(detailVo.getFactoryId());
            inventoryDetails.setFactoryCode(detailVo.getFactoryCode());
            inventoryDetails.setFactoryName(detailVo.getFactoryName());
            inventoryDetails.setProductionLot(shelfTask.getLot());
            inventoryDetails.setInventoryQty(receivedQty);
            inventoryDetails.setAvailableQty(receivedQty);
            inventoryDetails.setOperationUnit(detailVo.getUnit());
            inventoryDetails.setUnit(detailVo.getUnit());
//            inventoryDetails.setConversDefault(detailVo.getConversDefault());
            inventoryDetails.setPreparedQty(BigDecimal.ZERO);
            inventoryDetails.setOperationPreparedQty(BigDecimal.ZERO);
            inventoryDetails.setSupplierId(detailVo.getSupplierId());
            inventoryDetails.setSupplierCode(detailVo.getSupplierCode());
            inventoryDetails.setSupplierName(detailVo.getSupplierName());
            inventoryDetails.setStockInDate(DateUtils.getNowDate());
            inventoryDetails.setContainerNo(shelfTask.getContainerNo());
//            BigDecimal extValidityDays = new BigDecimal(wmsMaterialGroupList.get(0).getDefaultExtValiditydays());
//            inventoryDetails.setExpiryDate(DateUtils.addDays(DateUtils.getNowDate(), extValidityDays.intValue()));
//            inventoryDetails.setValidDays(extValidityDays);
            inventoryDetails.setStockInLot(shelfTask.getLot());
            inventoryDetails.setIsQc(detailVo.getIsQc());
            inventoryDetails.setIsConsign(detailVo.getIsConsign());
            inventoryDetails.setIsFreeze(CommonYesOrNo.NO);
            inventoryDetails.setWarehouseId(storagePositionVo.getWarehouseId());
            inventoryDetails.setWarehouseCode(storagePositionVo.getWarehouseCode());
            inventoryDetails.setCreateBy(SecurityUtils.getUsername());
            inventoryDetails.setCreateTime(DateUtils.getNowDate());
            inventoryDetails.setUpdateTime(DateUtils.getNowDate());
            inventoryDetails.setUpdateBy(SecurityUtils.getLoginUser().getUsername());

            AjaxResult result = inStoreService.add(inventoryDetails);
            if (result.isError()) {
                throw new ServiceException("新增库存台账失败！");
            }
            inventoryId = Long.parseLong(result.get("data").toString());
        } else if (inventoryDetailsList.size() == 1) {
            //修改台账
            InventoryDetails inventoryDetails = inventoryDetailsList.get(0);
            BigDecimal sumInventoryQty = inventoryDetails.getInventoryQty().add(shelfTask.getQty());
            BigDecimal sumAvailableQty = inventoryDetails.getAvailableQty().add(shelfTask.getQty());
            inventoryDetails.setInventoryQty(sumInventoryQty);
            inventoryDetails.setAvailableQty(sumAvailableQty);
            inventoryDetails.setUpdateBy(SecurityUtils.getUsername());
            inventoryDetails.setIsQc(detailVo.getIsQc());
            inventoryDetails.setUpdateTime(DateUtils.getNowDate());
            inventoryDetails.setUpdateBy(SecurityUtils.getLoginUser().getUsername());
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
     * SAP调拨
     * @param shelfTask
     * @param storagePositionVo
     */
    private String moveLocationSap(ShelfTask shelfTask, StoragePositionVo storagePositionVo) {
        // sap调拨
        Map<String,String> map = new HashMap<>();
        map.put("factoryCode", storagePositionVo.getFactoryCode());
        map.put("materialNo", shelfTask.getMaterialNo());
        map.put("lotNo", shelfTask.getLot());
        map.put("sourceLocation",shelfTask.getSourceLocationCode());
        map.put("comCode", SecurityUtils.getComCode());
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
    private String addStockMove(ShelfTask shelfTask, StoragePositionVo storagePositionVo) {
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
        inventoryDetails.setIsQc("1");
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
        return JSON.parseObject(JSON.toJSONString(ajaxResult.get("data")), String.class);
    }

    private MsgObject sendInspectTask(IMsgObject msgObject) throws Exception {
        byte[] result = remoteEsbSendService.sendToEsb(EsbSendSapConstant.WS_SEND_INSPECT_TASK, msgObject.getBytes());

        MsgObject resMo = new MsgObject(result, IMsgObject.MOType.initSR);
        logger.info("resMo: {}", resMo);

        if ("FAIL".equals(resMo.getServResStatus()) || "E".equals(resMo.getServResStatus())) {
            throw new ServiceException(resMo.getServResDesc());

        }
        if("false".equals(resMo.getResValue("Success"))) {
            throw new ServiceException(resMo.getServResDesc());
        }
        if("E".equals(resMo.getResValue("Flag"))) {
            throw new ServiceException(resMo.getResValue("ErrorMsg"));
        }

        return resMo;
    }
}
