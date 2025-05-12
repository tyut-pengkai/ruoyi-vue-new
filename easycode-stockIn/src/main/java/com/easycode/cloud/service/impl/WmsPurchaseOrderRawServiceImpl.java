package com.easycode.cloud.service.impl;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.easycode.cloud.mapper.PurchaseMapper;
import com.easycode.cloud.mapper.PurchaseOrderMapper;
import com.easycode.cloud.mapper.WmsPurchaseOrderDetailRawMapper;
import com.easycode.cloud.mapper.WmsPurchaseOrderRawMapper;
import com.soa.eis.adapter.framework.message.IMsgObject;
import com.soa.eis.adapter.framework.message.impl.GroupRecord;
import com.soa.eis.adapter.framework.message.impl.MsgObject;
import com.weifu.cloud.common.core.exception.ServiceException;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.security.utils.SecurityUtils;
import com.weifu.cloud.constant.*;
import com.easycode.cloud.domain.PurchaseOrder;
import com.weifu.cloud.domain.PurchaseOrderDetail;
import com.easycode.cloud.domain.WmsPurchaseOrderDetailRaw;
import com.easycode.cloud.domain.WmsPurchaseOrderRaw;
import com.weifu.cloud.domain.dto.MaterialMainDto;
import com.easycode.cloud.domain.dto.PurchaseOrderDetailRawDto;
import com.easycode.cloud.domain.vo.PurchaseVo;
import com.weifu.cloud.domian.NewWmsCompany;
import com.weifu.cloud.domian.WmsFactory;
import com.weifu.cloud.domian.dto.FactoryDto;
import com.weifu.cloud.domian.dto.WmsMaterialBasicDto;
import com.weifu.cloud.domian.dto.WmsSupplierDto;
import com.weifu.cloud.domian.vo.FactoryVo;
import com.weifu.cloud.mapper.*;
import com.weifu.cloud.service.IMainDataService;
import com.weifu.cloud.service.IStockOutService;
import com.easycode.cloud.service.IWmsPurchaseOrderRawService;
import com.weifu.cloud.service.RemoteEsbSendService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 采购单临时-主Service业务层处理
 *
 * @author weifu
 * @date 2023-02-20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WmsPurchaseOrderRawServiceImpl implements IWmsPurchaseOrderRawService
{
    private static final Logger logger = LoggerFactory.getLogger(WmsPurchaseOrderRawServiceImpl.class);

    @Autowired
    private WmsPurchaseOrderRawMapper wmsPurchaseOrderRawMapper;

    @Autowired
    private WmsPurchaseOrderDetailRawMapper wmsPurchaseOrderDetailRawMapper;

    @Autowired
    private RemoteEsbSendService remoteEsbSendService;

    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;

    @Autowired
    private PurchaseMapper purchaseOrderDetailMapper;

    @Autowired
    private IMainDataService iMainDataService;

    @Autowired
    private EsbSendCommonMapper esbSendCommonMapper;

    @Autowired
    private IStockOutService stockOutService;

    /**
     * 查询采购单临时-主
     *
     * @param id 采购单临时-主主键
     * @return 采购单临时-主
     */
    @Override
    public WmsPurchaseOrderRaw selectWmsPurchaseOrderRawById(Long id)
    {
        return wmsPurchaseOrderRawMapper.selectWmsPurchaseOrderRawById(id);
    }

    /**
     * 查询采购单临时-主列表
     *
     * @param wmsPurchaseOrderRaw 采购单临时-主
     * @return 采购单临时-主
     */
    @Override
    public List<WmsPurchaseOrderRaw> selectWmsPurchaseOrderRawList(WmsPurchaseOrderRaw wmsPurchaseOrderRaw)
    {
        return wmsPurchaseOrderRawMapper.selectWmsPurchaseOrderRawList(wmsPurchaseOrderRaw);
    }

    /**
     * 新增采购单临时-主
     *
     * @param wmsPurchaseOrderRaw 采购单临时-主
     * @return 结果
     */
    @Override
    public int insertWmsPurchaseOrderRaw(WmsPurchaseOrderRaw wmsPurchaseOrderRaw)
    {
        wmsPurchaseOrderRaw.setCreateTime(DateUtils.getNowDate());
        return wmsPurchaseOrderRawMapper.insertWmsPurchaseOrderRaw(wmsPurchaseOrderRaw);
    }

    /**
     * 修改采购单临时-主
     *
     * @param wmsPurchaseOrderRaw 采购单临时-主
     * @return 结果
     */
    @Override
    public int updateWmsPurchaseOrderRaw(WmsPurchaseOrderRaw wmsPurchaseOrderRaw)
    {
        wmsPurchaseOrderRaw.setUpdateTime(DateUtils.getNowDate());
        return wmsPurchaseOrderRawMapper.updateWmsPurchaseOrderRaw(wmsPurchaseOrderRaw);
    }

    /**
     * 批量删除采购单临时-主
     *
     * @param ids 需要删除的采购单临时-主主键
     * @return 结果
     */
    @Override
    public int deleteWmsPurchaseOrderRawByIds(Long[] ids)
    {
        return wmsPurchaseOrderRawMapper.deleteWmsPurchaseOrderRawByIds(ids);
    }

    /**
     * 删除采购单临时-主信息
     *
     * @param id 采购单临时-主主键
     * @return 结果
     */
    @Override
    public int deleteWmsPurchaseOrderRawById(Long id)
    {
        return wmsPurchaseOrderRawMapper.deleteWmsPurchaseOrderRawById(id);
    }

    /**
     * 采购订单同步
     *
     * @param burks
     */
    @Override
    public void syncPurchaseOrder(String burks){
        logger.info("同步采购单公司代码：{}", burks);
        try{
            Map<String, Map<String, Object>> companyMaps = wmsPurchaseOrderRawMapper.getCompanyMaps();
            Map<String, Map<String, Object>> supplyMaps = wmsPurchaseOrderRawMapper.getSupplyMaps();
            Map<String, Map<String, Object>> factoryMaps = wmsPurchaseOrderRawMapper.getFactoryMaps();
            Map<String, Map<String, Object>> materialMaps = wmsPurchaseOrderRawMapper.getMaterialMaps();
            MsgObject reqMo = new MsgObject(IMsgObject.MOType.initSR);
            reqMo.setReqValue("BUKRS",burks);
            //   reqMo.setReqValue("EBELN","5500060335");
            reqMo.setReqValue("SYSID","WMS");
            byte[] result =  remoteEsbSendService.sendToEsb(EsbSendSapConstant.Z_IWMS_PO, reqMo.toString().getBytes());
//       String res = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
//               "\n" +
//               "<Service>\n" +
//               "  <Route>\n" +
//               "    <ServiceID>02002000000228</ServiceID>\n" +
//               "    <SourceSysID>02054</SourceSysID>\n" +
//               "    <SerialNO>ad6e12b9df354106add15e1e0f44b2b4</SerialNO>\n" +
//               "    <ServiceTime>20230222092045</ServiceTime>\n" +
//               "    <FlowType>REQUESTER</FlowType>\n" +
//               "    <RequesterStartTime>1677028846405</RequesterStartTime>\n" +
//               "    <RequesterIN>ESB_IN01</RequesterIN>\n" +
//               "    <RequesterQM>ESB_QM02</RequesterQM>\n" +
//               "    <ServiceResponse>\n" +
//               "      <Status>COMPLETE</Status>\n" +
//               "    </ServiceResponse>\n" +
//               "    <AdapterIn>MQ</AdapterIn>\n" +
//               "    <QueueNumIn>054</QueueNumIn>\n" +
//               "    <ServiceTimeout>60000</ServiceTimeout>\n" +
//               "    <AdapterOut>SAP</AdapterOut>\n" +
//               "    <QueueNumOut>002</QueueNumOut>\n" +
//               "    <Priority>5</Priority>\n" +
//               "    <ProviderStartTime>1677028846466</ProviderStartTime>\n" +
//               "    <ProviderEndTime>1677028846479</ProviderEndTime>\n" +
//               "    <RequesterEndTime>1677028846485</RequesterEndTime>\n" +
//               "  </Route>\n" +
//               "  <Data>\n" +
//               "    <Control/>\n" +
//               "    <Request>\n" +
//               "      <BUKRS>LD01</BUKRS>\n" +
//               "      <EBELN>5500041821</EBELN>\n" +
//               "      <SYSID>WMS</SYSID>\n" +
//               "    </Request>\n" +
//               "    <Response>\n" +
//               "      <ERRORMSG>同步成功</ERRORMSG>\n" +
//               "      <FLAG>S</FLAG>\n" +
//               "      <RECORDS>00000</RECORDS>\n" +
//               "      <HEADER>\n" +
//               "        <EBELN>5500041821</EBELN>\n" +
//               "        <BUKRS>LD01</BUKRS>\n" +
//               "        <AEDAT>2023-02-22</AEDAT>\n" +
//               "        <LIFNR>106321</LIFNR>\n" +
//               "        <EKORG>LD01</EKORG>\n" +
//               "        <EKGRP>331</EKGRP>\n" +
//               "        <BSART>LPA</BSART>\n" +
//               "        <NAME1>重庆泽轩包装有限公司</NAME1>\n" +
//               "        <ESBTIMESTAMP>20230222092046</ESBTIMESTAMP>\n" +
//               "      </HEADER>\n" +
//               "      <ITEMS>\n" +
//               "        <EBELN>5500041821</EBELN>\n" +
//               "        <EBELP>00010</EBELP>\n" +
//               "        <WERKS>LD01</WERKS>\n" +
//               "        <LOEKZ/>\n" +
//               "        <MATNR>1705001078</MATNR>\n" +
//               "        <LMEIN>EA</LMEIN>\n" +
//               "        <MENGE>581.000</MENGE>\n" +
//               "        <MAKTXW>盖板 1200*800*8 中密度纤维板</MAKTXW>\n" +
//               "        <ELIKZ/>\n" +
//               "        <INSMK/>\n" +
//               "        <RETPO/>\n" +
//               "        <PSTYP>2</PSTYP>\n" +
//               "        <LGORT/>\n" +
//               "        <EINDT>2023-02-21</EINDT>\n" +
//               "        <MENGE2>10.000</MENGE2>\n" +
//               "        <ETENR>0162</ETENR>\n" +
//               "      </ITEMS>\n" +
//               "      <ITEMS>\n" +
//               "        <EBELN>5500041821</EBELN>\n" +
//               "        <EBELP>00010</EBELP>\n" +
//               "        <WERKS>LD01</WERKS>\n" +
//               "        <LOEKZ/>\n" +
//               "        <MATNR>1705001078</MATNR>\n" +
//               "        <LMEIN>EA</LMEIN>\n" +
//               "        <MENGE>581.000</MENGE>\n" +
//               "        <MAKTXW>盖板 1200*800*8 中密度纤维板</MAKTXW>\n" +
//               "        <ELIKZ/>\n" +
//               "        <INSMK/>\n" +
//               "        <RETPO/>\n" +
//               "        <PSTYP>2</PSTYP>\n" +
//               "        <LGORT/>\n" +
//               "        <EINDT>2023-03-15</EINDT>\n" +
//               "        <MENGE2>100.000</MENGE2>\n" +
//               "        <ETENR>0163</ETENR>\n" +
//               "      </ITEMS>\n" +
//               "    </Response>\n" +
//               "  </Data>\n" +
//               "</Service>";
            MsgObject resMo = new MsgObject(result, IMsgObject.MOType.initSR);
            logger.info("esb返回结果："+ resMo);
            // 头
            List<GroupRecord> headers = resMo.getResGroupRecord("HEADER");
            List<GroupRecord> items = resMo.getResGroupRecord("ITEMS");
            // 主表
            Map<String,WmsPurchaseOrderRaw> orderRawMaps = new HashMap<>(100);
            for (GroupRecord record: headers) {
                // 参考文档 信息入库
//            logger.info(record);
                logger.info(record.getFieldValue("EBELN"));
                // 主表
                WmsPurchaseOrderRaw orderRaw = new WmsPurchaseOrderRaw();
                // 采购凭证编号
                String orderNo = record.getFieldValue("EBELN");
                // 公司代码
                String  companyCode = record.getFieldValue("BUKRS");
                // 记录创建日期
                String createDate = record.getFieldValue("AEDAT");
                // 采购组织
                String supplyCode = record.getFieldValue("EKORG");
                // 采购凭证类型
                String billType = record.getFieldValue("BSART");
                // 供应商名称
                String supplyName = record.getFieldValue("NAME1");
                // 采购组
                String buyer = record.getFieldValue("EKGRP");
                // 采购组描述
                String buyerName = record.getFieldValue("NAME2");
                orderRaw.setOrderNo(orderNo);
                orderRaw.setCompanyCode(companyCode);
                // 公司id
                if(companyMaps.containsKey(companyCode)) {
                    Map<String, Object> companMap = companyMaps.get(companyCode);
                    orderRaw.setCompanyName(String.valueOf(companMap.get("companyName")));
                    orderRaw.setCompanyId(Long.valueOf( companMap.get("id").toString()));
                }
                orderRaw.setBillType(billType);
                orderRaw.setCreateTime(DateUtils.dateTime("yyyy-MM-dd",createDate));
                orderRaw.setSupplierName(supplyName);
                orderRaw.setSupplierCode(supplyCode);
                orderRaw.setProcessFlag(PurchaseOrderConstant.ORDER_RAW_FLAG_NO);
                // 供应商id
                if(supplyMaps.containsKey(supplyCode)) {
                    Map<String,Object> supplyMap = supplyMaps.get(supplyCode);
                    orderRaw.setSupplierId(Long.valueOf( supplyMap.get("id").toString()) );
                    orderRaw.setSupplierName((String) supplyMap.get("supplierName"));
                }
                //
                orderRaw.setOrderStatus(CommonYesOrNo.NO);
                orderRaw.setBuyer(buyer);
                orderRaw.setProcessTime(DateUtils.getNowDate());
                wmsPurchaseOrderRawMapper.insertWmsPurchaseOrderRaw(orderRaw);
                orderRawMaps.put(orderNo, orderRaw);
            }
            for (GroupRecord item: items) {
                // 参考文档 信息入库
                WmsPurchaseOrderDetailRaw detailRaw = new WmsPurchaseOrderDetailRaw();
                // 采购凭证编号
                String orderNo = item.getFieldValue("EBELN");
                // 交货日期
                String deliverDateStr = item.getFieldValue("EINDT");
                // WERKS 工厂代码
                String factoryCode = item.getFieldValue("WERKS");
                // EBELP 行号
                String lineNo = item.getFieldValue("EBELP");
                // MATNR 物料号
                String materialNo = item.getFieldValue("MATNR");
                // MENGE 数量
                String qtyStr = item.getFieldValue("MENGE");
                // MENGE2 计划数量
                String planQtyStr = item.getFieldValue("MENGE2");
                // PSTYP  是否寄售  K 为寄售
                String consignedFlag = item.getFieldValue("PSTYP");
                // INSMK  是否质检
                String isExempted = item.getFieldValue("INSMK");
                // EINDT 项目交货日期
                String deliveryDateStr = item.getFieldValue("EINDT");
                // LMEIN 计量单位
                String unit = item.getFieldValue("LMEIN");
                // ETENR 交货计划行计数器
                String etenr = item.getFieldValue("ETENR");
                // LOEKZ 资产类别删除标记
                String deleteFlag = item.getFieldValue("LOEKZ");
                // INSMK 库存类型
                String storageType = item.getFieldValue("INSMK");
                // RETPO 退货项目
                String refundItem = item.getFieldValue("RETPO");
                // LGORT 存储地点
                String stoLocation = item.getFieldValue("LGORT");
                // DELIV_QTY sap已收货数量
                String sapReceivedQty = item.getFieldValue("DELIV_QTY");
                // MATKL 物料组
                String materialGroup = item.getFieldValue("MATKL");
                // WGBEZ 物料组描述
                String materialGroupDesc = item.getFieldValue("WGBEZ");
                detailRaw.setProcessFlag(PurchaseOrderConstant.ORDER_RAW_FLAG_NO);
                detailRaw.setPurchaseOrderNo(orderNo);
                detailRaw.setMaterialGroup(materialGroup);
                detailRaw.setMaterialGroupDesc(materialGroupDesc);
                detailRaw.setStorageType(storageType);
                detailRaw.setStoLocation(stoLocation);
                detailRaw.setRefundItem(refundItem);
                detailRaw.setCompleteQty(BigDecimal.valueOf(Double.parseDouble(sapReceivedQty == null ? "0" : sapReceivedQty)));
                if(orderRawMaps.containsKey(orderNo)){
                    WmsPurchaseOrderRaw orderRaw = orderRawMaps.get(orderNo);
                    detailRaw.setPurchaseOrderId(orderRaw.getId());
                }
                detailRaw.setLoekz(deleteFlag);
                detailRaw.setProcessTime(DateUtils.getNowDate());
                detailRaw.setDeliveryDate(DateUtils.dateTime("yyyy-MM-dd",deliverDateStr));
                detailRaw.setFactoryCode(factoryCode);
                if ( factoryMaps.containsKey(factoryCode)) {
                    Map<String, Object> factoryMap = factoryMaps.get(factoryCode);
                    detailRaw.setFactoryId(Long.valueOf(factoryMap.get("id").toString()));
                    detailRaw.setFactoryName((String) factoryMap.get("factoryName"));
                }
                detailRaw.setPurchaseLineNo(lineNo);
                detailRaw.setMaterialNo(materialNo);
                if (materialMaps.containsKey(materialNo)) {
                    Map<String, Object> materialMap = materialMaps.get(materialNo);
                    detailRaw.setMaterialId(Long.valueOf(materialMap.get("id").toString()) );
                    detailRaw.setMaterialName((String) materialMap.get("materialName"));
                    // 旧物料号
                    if (Objects.nonNull(materialMap.get("oldMaterialNo"))) {
                        detailRaw.setOldMaterialNo((String) materialMap.get("oldMaterialNo"));
                    }
                }
                // 是否寄售
                detailRaw.setIsConsigned(consignedFlag);
                detailRaw.setUnit(unit);
                detailRaw.setDeliveryLineNo(etenr);
                // detailRaw.setTotalPlanQty(q);
                if(StringUtils.isNotEmpty(planQtyStr)){
                    detailRaw.setPlanQty(new BigDecimal(planQtyStr));
                }
                if (StringUtils.isNotEmpty(qtyStr)) {
                    detailRaw.setTotalPlanQty(new BigDecimal(qtyStr));
                }
                detailRaw.setIsExempted(isExempted);
                if(StringUtils.isNotEmpty(deliveryDateStr)){
                    detailRaw.setDeliveryDate(DateUtils.dateTime("yyyy-MM-dd", deliveryDateStr));
                }
                detailRaw.setCreateTime(DateUtils.getNowDate());
                wmsPurchaseOrderDetailRawMapper.insertWmsPurchaseOrderDetailRaw(detailRaw);
            }
        }catch (Exception e){
            throw new ServiceException("同步采购订单失败!");
        }
    }

    /**
     * 采购手动同步
     *
     * @param orderNo
     * @return
     * @throws Exception
     */
    @Override
    public void syncPurchaseOrderManual(String orderNo){
        logger.info("###手动同步采购单单号：{}", orderNo);
        try{
            MsgObject reqMo = new MsgObject(IMsgObject.MOType.initSR);
            // 单号
            if (orderNo.length() < 10) {
                orderNo = String.format("%010d", Integer.valueOf(orderNo));
            }
            reqMo.setReqValue("PURCHASEORDER", orderNo);
            // 不用修改
            reqMo.setReqValue("ITEMS", "X");
            reqMo.setReqValue("SCHEDULES", "X");
            reqMo.setReqValue("SYSID", "WMS");
            // feign 调用
            byte[] result = remoteEsbSendService.sendToEsb(EsbSendSapConstant.Z_IWMS_PO_01, reqMo.toString().getBytes());
            // 转化为标准结果
            MsgObject resMo = new MsgObject(result, IMsgObject.MOType.initSR);
            logger.info("esb返回结果：" + resMo.toString());
            // 抬头
            List<GroupRecord> isHeads = resMo.getResGroupRecord("IS_HEAD");
            if (isHeads == null || isHeads.size() == 0) {
                throw new ServiceException("手动同步采购订单失败!");
            }
            GroupRecord header = isHeads.get(0);
            // 单号
            String ebeln = header.getFieldValue("EBELN");
            if ("".equals(Objects.toString(ebeln, ""))) {
                throw new ServiceException("手动同步采购单据-获取失败!");
            }
            WmsPurchaseOrderRaw iOrder = new WmsPurchaseOrderRaw();
            // 生成批号
            iOrder.setLot(DateUtils.dateTimeNow("yyyyMMddHHmmssSSS"));
            iOrder.setSupplierCode(header.getFieldValue("LIFNR"));
            iOrder.setSupplierName(header.getFieldValue("NAME1"));
            iOrder.setProcessFlag(PurchaseOrderConstant.ORDER_RAW_FLAG_NO);
            iOrder.setCreateTime(DateUtils.getNowDate());
            iOrder.setCompanyCode(header.getFieldValue("EKORG"));
            iOrder.setOrderNo(ebeln);
            iOrder.setBillType(header.getFieldValue("BSART"));
            iOrder.setBuyer(header.getFieldValue("EKGRP"));
            iOrder.setProcessTime(DateUtils.getNowDate());
            iOrder.setAedat(DateUtils.parseDate(header.getFieldValue("AEDAT")));
            // 保存采购订单数据到中间表
            wmsPurchaseOrderRawMapper.insertWmsPurchaseOrderRaw(iOrder);
            // 返回多行数据
            List<Map<String,Object>> orderList = new ArrayList();
            for (int i = 0; i < isHeads.size(); i++) {
                GroupRecord item = isHeads.get(i);
                Map<String,Object> returnMap = new HashMap();
                for (int j = 0; j < item.getFieldSize(); j++) {
                    String key = item.getFieldKey(j);
                    String value = item.getFieldValue(j);
                    returnMap.put(key, value);
                }
                orderList.add(returnMap);
            }
            // 返回物料数据
            List<GroupRecord> itItems = resMo.getResGroupRecord("IT_ITEM");
            // 返回多行数据
            List<Map<String,Object>> lineList = new ArrayList();
            for (int i = 0; i < itItems.size(); i++) {
                GroupRecord item = itItems.get(i);
                Map<String,Object> returnMap = new HashMap();
                for (int j = 0; j < item.getFieldSize(); j++) {
                    String key = item.getFieldKey(j);
                    String value = item.getFieldValue(j);
                    returnMap.put(key, value);
                }
                lineList.add(returnMap);
            }
            // 返回收货数量数据
            List<GroupRecord> receiveQtyList = resMo.getResGroupRecord("IT_HISTORY");
            List<Map<String,Object>> qtyList = new ArrayList();//返回多行数据
            if(receiveQtyList != null && receiveQtyList.size() > 0){
                for (int i = 0; i < receiveQtyList.size(); i++) {
                    Map<String,Object> retDtDlvQtyMap = new HashMap();
                    GroupRecord item = receiveQtyList.get(i);
                    for (int j = 0; j < item.getFieldSize(); j++) {
                        String key = item.getFieldKey(j);
                        String value = item.getFieldValue(j);
                        retDtDlvQtyMap.put(key, value);
                    }
                    qtyList.add(retDtDlvQtyMap);
                }
            }
            // 返回协议计划数据
            List<GroupRecord> agreementList = resMo.getResGroupRecord("IT_SCHEDULES");
            List<Map<String,Object>> lpaList = new ArrayList<Map<String,Object>>();//返回多行数据
            if(agreementList != null && agreementList.size() > 0){
                for (int i = 0; i < agreementList.size(); i++) {
                    GroupRecord item = agreementList.get(i);
                    Map<String,Object> retDtAgMap = new HashMap();
                    for (int j = 0; j < item.getFieldSize(); j++) {
                        String key = item.getFieldKey(j);
                        String value = item.getFieldValue(j);
                        retDtAgMap.put(key, value);
                    }
                    lpaList.add(retDtAgMap);
                }
            }

            WmsPurchaseOrderDetailRaw detail = new WmsPurchaseOrderDetailRaw();
            if (PurchaseOrderConstant.SAP_LPA.equals(header.getFieldValue("BSART"))) {
                if(lpaList == null || lpaList.size() == 0){
                    throw new ServiceException("采购协议为空,同步失败！");
                }
                // 采购协议
                lpaList.stream().forEach(item->{
                    String lineNo = item.get("PO_ITEM").toString();
                    String planLineNo = item.get("SERIAL_NO").toString();
                    Map<String,Object> itemMap = lineList.stream()
                            .filter(ele -> lineNo.equals(ele.get("PO_ITEM")))
                            .findAny().orElse(null);
                    if(itemMap == null){
                        throw new ServiceException("手动同步采购订单失败!");
                    }
                    detail.setPurchaseOrderNo(ebeln);
                    detail.setPurchaseLineNo(Convert.toStr(lineNo));
                    detail.setDeliveryLineNo(planLineNo);
                    detail.setMaterialNo(itemMap.get("MATERIAL").toString());
                    detail.setLoekz(itemMap.get("DELETE_IND").toString());
                    detail.setTotalPlanQty(BigDecimal.valueOf(Double.valueOf(itemMap.get("QUANTITY").toString())));
                    detail.setPlanQty(BigDecimal.valueOf(Double.valueOf(item.get("QUANTITY").toString())));
                    Map<String,Object> qtyMap = null;
                    if(qtyList != null && qtyList.size() > 0){
                        qtyMap = qtyList.stream()
                                .filter(ele -> lineNo.equals(ele.get("EBELP")))
                                .filter(ele -> planLineNo.equals(ele.get("DZEKKN")))
                                .findAny().orElse(null);
                    }
                    if(qtyMap == null){
                        detail.setCompleteQty(BigDecimal.ZERO);
                    }else{
                        detail.setCompleteQty(BigDecimal.valueOf(Double.valueOf(qtyMap.get("LSMNG").toString())));
                    }
                    detail.setBatchNo(iOrder.getLot());
                    detail.setFactoryCode(itemMap.get("WERKS").toString());
                    detail.setUnit(itemMap.get("UNIT").toString());
                    detail.setMaterialName(itemMap.get("SHORT_TEXT").toString());
                    detail.setExcFinFlag(itemMap.get("DEL_COMPL").toString());
                    detail.setStorageType(itemMap.get("QUAL_INSP").toString());
                    detail.setRefundItem(itemMap.get("RET_ITEM").toString());
                    detail.setItemType(itemMap.get("ITEM_CAT").toString());
                    detail.setStoLocation(itemMap.get("STORE_LOC").toString());
                    detail.setDeliveryDate(DateUtils.dateTime("yyyy-MM-dd", item.get("DELIV_DATE").toString()));
                    detail.setProcessFlag(PurchaseOrderConstant.ORDER_RAW_FLAG_NO);
                    detail.setCreateTime(DateUtils.getNowDate());
                    detail.setMaterialGroup(itemMap.get("MATKL").toString());
                    detail.setMaterialGroupDesc(itemMap.get("WGBEZ").toString());
                    detail.setIsConsigned(SapReqOrResConstant.NOT_AUTO_IS_CONSIGN.equals(itemMap.get("ITEM_CAT").toString()) ?  SapReqOrResConstant.IS_CONSIGN : "");
                    detail.setIsExempted(itemMap.get("QUAL_INSP").toString());
                    wmsPurchaseOrderDetailRawMapper.insertWmsPurchaseOrderDetailRaw(detail);
                });
            }else{
                // 普通采购订单
                lineList.stream().forEach(item->{
                    String lineNo = item.get("PO_ITEM").toString();
                    detail.setPurchaseOrderNo(ebeln);
                    detail.setPurchaseLineNo(Convert.toStr(lineNo));
                    detail.setMaterialNo(item.get("MATERIAL").toString());
                    detail.setLoekz(item.get("DELETE_IND").toString());
                    detail.setTotalPlanQty(BigDecimal.valueOf(Double.valueOf(item.get("QUANTITY").toString())));
                    detail.setPlanQty(BigDecimal.valueOf(Double.valueOf(item.get("QUANTITY").toString())));
                    detail.setCreateTime(DateUtils.getNowDate());
                    detail.setFactoryCode(item.get("WERKS").toString());
                    detail.setUnit(item.get("UNIT").toString());
                    detail.setMaterialName(item.get("SHORT_TEXT").toString());
                    detail.setExcFinFlag(item.get("DEL_COMPL").toString());
                    detail.setStorageType(item.get("QUAL_INSP").toString());
                    detail.setRefundItem(item.get("RET_ITEM").toString());
                    detail.setItemType(item.get("ITEM_CAT").toString());
                    detail.setStoLocation(item.get("STORE_LOC").toString());
                    detail.setMaterialGroup(item.get("MATKL").toString());
                    detail.setMaterialGroupDesc(item.get("WGBEZ").toString());
                    detail.setBatchNo(iOrder.getLot());
                    detail.setIsConsigned(SapReqOrResConstant.NOT_AUTO_IS_CONSIGN.equals(item.get("ITEM_CAT").toString()) ?  SapReqOrResConstant.IS_CONSIGN : "");
                    detail.setIsExempted(item.get("QUAL_INSP").toString());
                    // 从计划中获取数量
                    Map<String,Object> lpaMap = lpaList.stream()
                            .filter(ele -> lineNo.equals(ele.get("PO_ITEM")))
                            .findAny().orElse(null);
                    if(lpaMap == null){
                        throw new ServiceException("手动同步采购订单失败!");
                    }
                    detail.setDeliveryDate(DateUtils.dateTime("yyyy-MM-dd", lpaMap.get("DELIV_DATE").toString()));
                    // 从历史数据中获取已收货数量
                    Map<String,Object> qtyMap = null;
                    if(qtyList != null && qtyList.size() > 0){
                        qtyMap = qtyList.stream()
                                .filter(ele -> lineNo.equals(ele.get("PO_ITEM")))
                                .findAny().orElse(null);
                    }
                    if(qtyMap != null){
                        detail.setCompleteQty(BigDecimal.valueOf(Double.valueOf(lpaMap.get("LSMNG").toString())));
                    }else{
                        detail.setCompleteQty(BigDecimal.ZERO);
                    }
                    detail.setProcessFlag(PurchaseOrderConstant.ORDER_RAW_FLAG_NO);
                    wmsPurchaseOrderDetailRawMapper.insertWmsPurchaseOrderDetailRaw(detail);
                });
            }
            String finalOrderNo = orderNo;
            WmsPurchaseOrderRaw purchaseOrderRaw = new WmsPurchaseOrderRaw();
            purchaseOrderRaw.setOrderNo(finalOrderNo);
            purchaseOrderRaw.setProcessFlag(PurchaseOrderConstant.ORDER_RAW_FLAG_NO);
            List<WmsPurchaseOrderRaw> iOrderList = wmsPurchaseOrderRawMapper.selectWmsPurchaseOrderRawList(purchaseOrderRaw);
            processPurOrder(iOrderList.get(0));
        }catch (Exception e){
            throw new ServiceException("手动同步采购单据失败!");
        }
    }


    /**
     * 采购订单同步至业务表
     */
    @Override
    public void purOrderBusinessSync(){
        // 查询未处理的中间表采购单据记录
        WmsPurchaseOrderRaw purchaseOrderRaw = new WmsPurchaseOrderRaw();
        purchaseOrderRaw.setProcessFlag(PurchaseOrderConstant.ORDER_RAW_FLAG_NO);
        List<WmsPurchaseOrderRaw> iOrderList = wmsPurchaseOrderRawMapper.selectWmsPurchaseOrderRawList(purchaseOrderRaw);
        if(iOrderList != null && iOrderList.size() > 0){
            // 处理中间表采购单据和明细数据
            for(WmsPurchaseOrderRaw order : iOrderList){
                try{
                    //一次处理一个采购单据及其明细
                    processPurOrder(order);
                }catch(Exception e){
                    logger.error("从中间表导入采购单据异常，数据："+order.toString()+"，异常："+e);
                    throw new ServiceException("同步采购协议失败!");
                }
            }
        }
    }

    public void processPurOrder(WmsPurchaseOrderRaw order) {
        // 采购凭证类型
        String billType = order.getBillType();
        // 采购单据类型
//        String orderType = order.getOrderType();
        // 获取中间表未处理明细数据
        WmsPurchaseOrderDetailRaw orderDetailRaw = new WmsPurchaseOrderDetailRaw();
        orderDetailRaw.setPurchaseOrderNo(order.getOrderNo());
        orderDetailRaw.setBatchNo(order.getLot());
        orderDetailRaw.setProcessFlag(PurchaseOrderConstant.ORDER_RAW_FLAG_NO);
        List<WmsPurchaseOrderDetailRaw> iOrderDtList = wmsPurchaseOrderDetailRawMapper.selectWmsPurchaseOrderDetailRawList(orderDetailRaw);
        // 若中间表单据的采购凭证类型为Z80,不做同步到业务表处理,直接修改处理状态
        if(PurchaseOrderConstant.ORDER_TYPE_OUT_SOURCE.equals(billType) || PurchaseOrderConstant.SAP_UB.equals(billType) || PurchaseOrderConstant.SAP_Z30.equals(billType)){
            // 修改中间表采购单据处理状态
            WmsPurchaseOrderRaw purchaseOrderRaw = new WmsPurchaseOrderRaw();
            purchaseOrderRaw.setId(order.getId());
            purchaseOrderRaw.setUpdateTime(DateUtils.getNowDate());
            purchaseOrderRaw.setProcessFlag(PurchaseOrderConstant.ORDER_RAW_FLAG_YES);
            wmsPurchaseOrderRawMapper.updateWmsPurchaseOrderRaw(purchaseOrderRaw);
            // 修改中间表采购单据明细处理状态
            for(WmsPurchaseOrderDetailRaw detail : iOrderDtList){
                WmsPurchaseOrderDetailRaw detailRaw = new WmsPurchaseOrderDetailRaw();
                detailRaw.setId(detail.getId());
                detailRaw.setUpdateTime(DateUtils.getNowDate());
                detailRaw.setProcessFlag(PurchaseOrderConstant.ORDER_RAW_FLAG_YES);
                wmsPurchaseOrderDetailRawMapper.updateWmsPurchaseOrderDetailRaw(detailRaw);
            }
            return;
        }
        // 检查采购单据是否已经在业务表中存在,存在则进入修改模式,不存在则进入新增模式
        PurchaseOrder purchaseOrder1 = new PurchaseOrder();
        purchaseOrder1.setOrderNo(order.getOrderNo());
        List<PurchaseOrder> orderList = purchaseOrderMapper.selectPurchaseOrderList(purchaseOrder1);
        // 判断修改还是新增模式
        if(orderList != null && orderList.size()>0){
            // 修改
            PurchaseOrder orderData = orderList.get(0);
            // 获取业务表中采购单据id
            Long orderId = orderData.getId();
            // 业务表采购单据状态
            String orderStatus = orderData.getOrderStatus();
            if (iOrderDtList != null && iOrderDtList.size() > 0) {
                if (!PurchaseOrderConstant.SAP_LPA.equals(billType)) {
                    // 非采购协议,如采购订单或者转储订单
                    // 业务表中采购单据明细数据
                    PurchaseOrderDetail orderDetail = new PurchaseOrderDetail();
                    orderDetail.setPurchaseOrderId(orderId);
                    List<PurchaseOrderDetail> orderDtList = purchaseOrderDetailMapper.selectOrderDetail(orderDetail);
                    // 删除列表
                    List<PurchaseOrderDetailRawDto> delOrderDtList = new ArrayList();
                    // 修改列表
                    List<PurchaseOrderDetailRawDto> updOrderDtList = new ArrayList();
                    // 新增列表
                    List<WmsPurchaseOrderDetailRaw> addOrderDtList = new ArrayList();
                    // 清除列表,物料代码为空的中间表明细数据保存到此列表,不参与现有明细与新明细的比对,直接修改新明细状态
                    List<WmsPurchaseOrderDetailRaw> clearOrderDtList = new ArrayList();
                    // 清除中间表物料号为空,删除标识为L,需求数量为0的数据
                    iOrderDtList.removeIf(ele -> {
                        boolean flag = false;
                        if (ele.getMaterialNo() == null
                                || ele.getMaterialNo().isEmpty()
                                || PurchaseOrderConstant.ORDER_RAW_DELETE.equals(ele.getLoekz())
                                || ele.getTotalPlanQty().compareTo(BigDecimal.ZERO) == 0) {
                            clearOrderDtList.add(ele);
                            flag = true;
                        }
                        return flag;
                    });
                    // 现有采购明细与新采购明细比对,得到删除、修改、新增的采购单据明细
                    orderDtList.stream().forEach(item -> {
                        // 在新采购明细中查找现有明细行号
                        String orderLine = item.getPurchaseLineNo();
                        // 根据行号确认明细是否存在
                        WmsPurchaseOrderDetailRaw lineDt = iOrderDtList.stream()
                                .filter(ele -> orderLine.equals(ele.getPurchaseLineNo()))
                                .findAny().orElse(null);
                        PurchaseOrderDetailRawDto iDetail = new PurchaseOrderDetailRawDto();
                        iDetail.setId(lineDt.getId());
                        iDetail.setDetailId(item.getId());
                        if (lineDt == null) {
                            // 业务表存在,中间表不存在的行号(删除业务表)
                            iDetail.setDetailId(item.getId());
                            delOrderDtList.add(iDetail);
                        } else {
                            // 业务表明细,按照中间表数据修改
                            iDetail.setMaterialNo(lineDt.getMaterialNo());
                            iDetail.setFactoryCode(lineDt.getFactoryCode());
                            iDetail.setDeliveryDate(lineDt.getDeliveryDate());
                            iDetail.setPurchaseOrderId(orderId);
                            iDetail.setPurchaseOrderNo(order.getOrderNo());
                            iDetail.setPurchaseLineNo(lineDt.getPurchaseLineNo());
                            iDetail.setPurchaseRefundOrderNo(lineDt.getPurchaseRefundOrderNo());
                            iDetail.setUnit(lineDt.getUnit());
                            iDetail.setTotalQty(lineDt.getPlanQty());
                            // 实际需求数量(扣除掉sap手动收货) = sap需求数量 - sap已收货数量 + wms已收货数量(计算出sap自己手动收货数量,由于定时任务不会实时同步,也存在计算不准确情况)
                            BigDecimal actNeedQty = lineDt.getPlanQty().subtract(lineDt.getCompleteQty()).add(item.getWmsReceivedQty());
                            iDetail.setTotalPlanQty(actNeedQty);
                            iDetail.setStoLocation(lineDt.getStoLocation());
                            iDetail.setNoDeliveryFlag(lineDt.getNoDeliveryFlag());
                            iDetail.setCompleteQty(lineDt.getCompleteQty());
                            iDetail.setWmsReceivedQty(item.getWmsReceivedQty());
//                            iDetail.setTenantId(item.getTenantId());
                            iDetail.setMadeQty(item.getMadeQty());
                            iDetail.setPlanQty(actNeedQty.subtract(iDetail.getMadeQty()));
                            iDetail.setIsExempted(lineDt.getIsExempted());
                            iDetail.setIsConsigned(lineDt.getIsConsigned());
                            updOrderDtList.add(iDetail);
                        }
                        // 已经处理的数据从list中删除
                        String findLine = lineDt.getPurchaseLineNo();
                        iOrderDtList.removeIf(ele -> findLine.equals(ele.getPurchaseLineNo()));
                    });
                    // 新采购明细中剩余的数据,作为新增的采购明细
                    if (iOrderDtList.size() > 0) {
                        addOrderDtList = iOrderDtList;
                    }

                    // 处理有变化的数据
                    try {
                        // 清除,物料代码为空的中间表新明细直接修改状态
                        clearOrderDtList.forEach(ele -> {
                            // 修改中间表采购单据明细处理状态
                            ele.setProcessFlag(PurchaseOrderConstant.ORDER_RAW_FLAG_YES);
                            ele.setUpdateTime(DateUtils.getNowDate());
                            ele.setProcessTime(DateUtils.getNowDate());
                            wmsPurchaseOrderDetailRawMapper.updateWmsPurchaseOrderDetailRaw(ele);
                        });
                        // 删除
                        delOrderDtList.forEach(ele -> {
                            // 删除采购单据明细
                            purchaseOrderDetailMapper.deleteWmsPurchaseOrderDetailById(ele.getDetailId());
                            // 修改中间表采购单据明细处理状态
                            WmsPurchaseOrderDetailRaw purchaseOrderDetailRaw = new WmsPurchaseOrderDetailRaw();
                            purchaseOrderDetailRaw.setId(ele.getDetailId());
                            purchaseOrderDetailRaw.setUpdateTime(DateUtils.getNowDate());
                            purchaseOrderDetailRaw.setProcessTime(DateUtils.getNowDate());
                            purchaseOrderDetailRaw.setProcessFlag(PurchaseOrderConstant.ORDER_RAW_FLAG_YES);
                            wmsPurchaseOrderDetailRawMapper.updateWmsPurchaseOrderDetailRaw(purchaseOrderDetailRaw);
                        });
                        // 修改
                        updOrderDtList.forEach(ele -> {
                            // 获取物料,工厂主数据信息
                            handleOrderDetail(ele);
                            // 是否质检
                            String isExempted = checkIsExempted(ele.getIsExempted());
                            // 是否寄售
                            String isConsigned = checkIsConsigned(ele.getIsConsigned());
                            PurchaseOrderDetail detail = new PurchaseOrderDetail();
                            BeanUtils.copyProperties(ele,detail);
                            if(detail.getDeliveryDate() == null){
                                detail.setDeliveryDate(DateUtils.parseDate("1900-01-01 00:00:00"));
                            }
                            detail.setId(ele.getDetailId());
                            detail.setTenantId(null);
                            detail.setIsExempted(isExempted);
                            detail.setIsConsigned(isConsigned);
                            detail.setDelFlag(CommonYesOrNo.NO);
                            detail.setTenantId(null);
                            detail.setUpdateTime(DateUtils.getNowDate());
                            detail.setSapReceivedQty(ele.getCompleteQty());
                            purchaseOrderDetailMapper.updateWmsPurchaseOrderDetail(detail);
                            // 修改中间表采购单据明细处理状态
                            WmsPurchaseOrderDetailRaw detailRaw = new WmsPurchaseOrderDetailRaw();
                            detailRaw.setId(ele.getId());
                            detailRaw.setProcessFlag(PurchaseOrderConstant.ORDER_RAW_FLAG_YES);
                            detailRaw.setProcessTime(DateUtils.getNowDate());
                            wmsPurchaseOrderDetailRawMapper.updateWmsPurchaseOrderDetailRaw(detailRaw);

                            // 修改采购订单明细之后,当采购订单现状态为关闭且新需求数量大于现已制单数量,则修改采购订单状态为处理中
                            BigDecimal newPlanQty = ele.getTotalQty();
                            // 已制单数量
                            BigDecimal madeQty = ele.getMadeQty();
                            if (OrderStatusConstant.ORDER_STATUS_COMPLETE.equals(orderStatus) && newPlanQty.compareTo(madeQty) > 0) {
                                PurchaseOrder purchaseOrder = new PurchaseOrder();
                                purchaseOrder.setId(orderId);
                                purchaseOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_PART_COMPLETE);
                                purchaseOrder.setUpdateTime(DateUtils.getNowDate());
                                purchaseOrderMapper.updatePurchaseOrder(purchaseOrder);
                            }
                        });
                        // 新增
                        addOrderDtList.forEach(ele -> {
                            // 获取物料,工厂主数据信息
                            PurchaseOrderDetailRawDto detailRaw = new PurchaseOrderDetailRawDto();
                            detailRaw.setMaterialNo(ele.getMaterialNo());
                            detailRaw.setFactoryCode(ele.getFactoryCode());
                            detailRaw.setCompanyId(order.getCompanyId());
                            handleOrderDetail(detailRaw);
                            // 判断是否质检,是否寄售
                            String isExempted = checkIsExempted(ele.getIsExempted());
                            String isConsigned = checkIsConsigned(ele.getIsConsigned());
                            PurchaseOrderDetail detail = new PurchaseOrderDetail();
                            BeanUtils.copyProperties(ele,detail);
                            if(detail.getDeliveryDate() == null){
                                detail.setDeliveryDate(DateUtils.parseDate("1900-01-01 00:00:00"));
                            }
                            detail.setMaterialId(detailRaw.getMaterialId());
                            detail.setFactoryId(detailRaw.getFactoryId());
                            detail.setFactoryName(detailRaw.getFactoryName());
                            detail.setOldMaterialNo(detailRaw.getOldMaterialNo());
                            detail.setMaterialName(detailRaw.getMaterialName());
                            detail.setIsExempted(isExempted);
                            detail.setTenantId(null);
                            detail.setIsConsigned(isConsigned);
                            detail.setDelFlag(CommonYesOrNo.NO);
                            detail.setCreateTime(DateUtils.getNowDate());
                            detail.setSapReceivedQty(ele.getCompleteQty() == null ? BigDecimal.ZERO : ele.getCompleteQty());
                            detail.setWmsReceivedQty(BigDecimal.ZERO);
                            detail.setMadeQty(ele.getMadeQty() == null ? BigDecimal.ZERO : ele.getMadeQty());
                            detail.setTotalQty(ele.getPlanQty());
                            detail.setPurchaseOrderId(orderId);
                            purchaseOrderDetailMapper.insertWmsPurchaseOrderDetail(detail);
                            // 修改中间表采购单据处理状态
                            WmsPurchaseOrderDetailRaw purchaseOrderDetailRaw = new WmsPurchaseOrderDetailRaw();
                            purchaseOrderDetailRaw.setId(ele.getId());
                            purchaseOrderDetailRaw.setProcessFlag(PurchaseOrderConstant.ORDER_RAW_FLAG_YES);
                            purchaseOrderDetailRaw.setProcessTime(DateUtils.getNowDate());
                            wmsPurchaseOrderDetailRawMapper.updateWmsPurchaseOrderDetailRaw(purchaseOrderDetailRaw);
                        });

                        // 更新采购单据业务表记录的单据类型字段
                        PurchaseOrder purchaseOrder = new PurchaseOrder();
                        purchaseOrder.setId(orderId);
                        purchaseOrder.setOrderType(checkOrderType(billType));
                        purchaseOrder.setBillType(billType);
                        purchaseOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_PART_COMPLETE);
                        purchaseOrder.setUpdateTime(DateUtils.getNowDate());
                        purchaseOrderMapper.updatePurchaseOrder(purchaseOrder);

                        // 修改中间表采购单据处理状态
                        WmsPurchaseOrderRaw purchaseOrderRaw = new WmsPurchaseOrderRaw();
                        purchaseOrderRaw.setId(order.getId());
                        purchaseOrderRaw.setProcessFlag(PurchaseOrderConstant.ORDER_RAW_FLAG_YES);
                        purchaseOrderRaw.setUpdateTime(DateUtils.getNowDate());
                        wmsPurchaseOrderRawMapper.updateWmsPurchaseOrderRaw(purchaseOrderRaw);
                    } catch (Exception e) {
                        throw new ServiceException("采购订单中间表同步业务表失败！");
                    }
                } else {
                    // 采购协议
                    // 根据新采购明细中的最小交货日期查询此交货日期之后的当前采购明细数据,用于删除操作判断
                    PurchaseOrderDetail purchaseOrderDetail = new PurchaseOrderDetail();
                    purchaseOrderDetail.setPurchaseOrderId(orderId);
                    purchaseOrderDetail.setDeliveryDate(iOrderDtList.get(0).getDeliveryDate());
                    List<PurchaseOrderDetail> agByTime = purchaseOrderDetailMapper.selectDetailByDeliveryDate(purchaseOrderDetail);

                    // 根据新采购明细的行计数器得到当前业务表中采购明细数据,用于新旧数据比对
                    WmsPurchaseOrderDetailRaw purchaseOrderDetailRaw = new WmsPurchaseOrderDetailRaw();
                    purchaseOrderDetailRaw.setPurchaseOrderId(orderId);
                    purchaseOrderDetailRaw.setBatchNo(order.getLot());
                    purchaseOrderDetailRaw.setPurchaseOrderNo(order.getOrderNo());
                    purchaseOrderDetailRaw.setProcessFlag(PurchaseOrderConstant.ORDER_RAW_FLAG_NO);
                    List<PurchaseOrderDetail> agByLineNum = purchaseOrderDetailMapper.selectDetailByLineNum(purchaseOrderDetailRaw);

                    // 删除列表
                    List<PurchaseOrderDetailRawDto> delAgList = new ArrayList();
                    // 修改列表
                    List<PurchaseOrderDetailRawDto> updAgList = new ArrayList();
                    // 新增列表
                    List<WmsPurchaseOrderDetailRaw> addAgList = new ArrayList();
                    // 其他列表,不做业务处理的中间表数据
                    List<WmsPurchaseOrderDetailRaw> otherAgList = new ArrayList();

                    // 特定日期之后的现有采购明细与新采购明细比对,得到删除采购单据明细
                    if (agByTime != null && agByTime.size() > 0) {
                        agByTime.stream().forEach(item -> {
                            // 在新采购明细中查找现有明细的计划行计数器
                            Long lineNum = Long.parseLong(item.getDeliveryLineNo());
                            WmsPurchaseOrderDetailRaw lineNumAg = iOrderDtList.stream()
                                    .filter(ele -> lineNum.equals(Long.parseLong(ele.getDeliveryLineNo())))
                                    .findAny().orElse(null);
                            if (lineNumAg == null) {
                                // 没有找到现有计划行计数器则删除当前采购明细
                                PurchaseOrderDetailRawDto iDetail = new PurchaseOrderDetailRawDto();
                                iDetail.setDetailId(item.getId());
                                delAgList.add(iDetail);
                            }
                        });
                    }
                    // 交货计划行计数器list
//                    List<PurchaseOrderDetailRawDto>  deliveryLineList = new ArrayList<>();
                    // 新采购明细与计划行计数器相同的现有采购明细比对,得到删除、修改、新增的采购单据明细
                    iOrderDtList.stream().forEach(item -> {
                        // 在现有明细中查找新采购明细的行号
                        Long lingNum = Long.parseLong(item.getDeliveryLineNo());
                        String lineNo = item.getPurchaseLineNo();
                        PurchaseOrderDetail lineNumAg = null;
                        if (agByLineNum != null && agByLineNum.size() > 0) {
                            lineNumAg = agByLineNum.stream()
                                    .filter(ele -> lingNum.equals(Long.parseLong(ele.getDeliveryLineNo())))
                                    .filter(ele -> lineNo.equals(ele.getPurchaseLineNo()))
                                    .findAny().orElse(null);
                        }
                        if (lineNumAg == null) {
                            // 没有找到计划行计数器,新采购明细的送货数量大于0并且删除标记不为L,则作为新增
                            BigDecimal planQty = item.getPlanQty();
                            // 删除标记,L表示删除
                            String delFlag = item.getLoekz();
                            if (planQty.compareTo(BigDecimal.ZERO) > 0 && !PurchaseOrderConstant.ORDER_RAW_DELETE.equals(delFlag)) {
                                // 送货数量大于0且删除标记不为L,则作为新增
                                addAgList.add(item);
                            } else {
                                // 送货数量等于0或者删除标记为L,则不做处理
                                otherAgList.add(item);
                            }
                        } else {
                            BigDecimal planQty = item.getPlanQty();
                            // 删除标记,L表示删除
                            String delFlag = item.getLoekz();
                            if (planQty.compareTo(BigDecimal.ZERO) == 0 || PurchaseOrderConstant.ORDER_RAW_DELETE.equals(delFlag)) {
                                // 找到但新数量为0或者删除标记为L,则删除当前采购明细
                                PurchaseOrderDetailRawDto iDetail = new PurchaseOrderDetailRawDto();
                                iDetail.setDetailId(lineNumAg.getId());
                                delAgList.add(iDetail);
                            } else {
                                // 找到且新数量不为0,则更新当前采购明细
                                PurchaseOrderDetailRawDto iDetail = new PurchaseOrderDetailRawDto();
                                iDetail.setId(item.getId());
                                iDetail.setMaterialNo(item.getMaterialNo());
                                iDetail.setFactoryCode(item.getFactoryCode());
                                iDetail.setDeliveryDate(item.getDeliveryDate());
                                iDetail.setPurchaseOrderId(orderId);
                                iDetail.setPurchaseOrderNo(order.getOrderNo());
                                iDetail.setPurchaseLineNo(item.getPurchaseLineNo());
                                iDetail.setPurchaseRefundOrderNo(item.getPurchaseRefundOrderNo());
                                iDetail.setUnit(item.getUnit());
                                iDetail.setTotalQty(item.getPlanQty());
                                // 实际总需求数量(扣除掉sap手动收货) = sap需求数量 - sap收货数量 + wms已收货数量(计算出sap自己手动收货数量,由于定时任务不会实时同步,也存在计算不准确情况)
                                BigDecimal actNeedQty = item.getPlanQty().subtract(item.getCompleteQty()).add(lineNumAg.getWmsReceivedQty());
                                iDetail.setTotalPlanQty(actNeedQty);
                                iDetail.setMadeQty(lineNumAg.getMadeQty());
                                iDetail.setIsExempted(item.getIsExempted());
                                iDetail.setIsConsigned(item.getIsConsigned());
                                iDetail.setStoLocation(item.getStoLocation());
                                iDetail.setNoDeliveryFlag(item.getNoDeliveryFlag());
                                iDetail.setWmsReceivedQty(lineNumAg.getWmsReceivedQty());
                                iDetail.setPlanQty(actNeedQty.subtract(lineNumAg.getMadeQty() == null ? BigDecimal.ZERO :lineNumAg.getMadeQty()));
                                iDetail.setDetailId(lineNumAg.getId());
                                updAgList.add(iDetail);
                            }
                        }
                    });

                    // 处理有变化的数据
                    try {
                        // 删除
                        delAgList.forEach(ele -> {
                            // 删除采购单据明细协议
                            purchaseOrderDetailMapper.deleteWmsPurchaseOrderDetailById(ele.getDetailId());
                            if (ele.getId() != null) {
                                // 修改中间表采购单据明细处理状态
                                WmsPurchaseOrderDetailRaw detailRaw = new WmsPurchaseOrderDetailRaw();
                                detailRaw.setId(ele.getDetailId());
                                detailRaw.setUpdateTime(DateUtils.getNowDate());
                                detailRaw.setProcessTime(DateUtils.getNowDate());
                                detailRaw.setProcessFlag(PurchaseOrderConstant.ORDER_RAW_FLAG_YES);
                                wmsPurchaseOrderDetailRawMapper.updateWmsPurchaseOrderDetailRaw(detailRaw);
                            }
                        });

                        // 修改
                        updAgList.forEach(ele -> {
                            handleOrderDetail(ele);
                            String isExempted = checkIsExempted(ele.getIsExempted());
                            String isConsigned = checkIsConsigned(ele.getIsConsigned());
                            PurchaseOrderDetail detail = new PurchaseOrderDetail();
                            BeanUtils.copyProperties(ele,detail);
                            if(detail.getDeliveryDate() == null){
                                detail.setDeliveryDate(DateUtils.parseDate("1900-01-01 00:00:00"));
                            }
                            detail.setIsExempted(isExempted);
                            detail.setIsConsigned(isConsigned);
                            detail.setDelFlag(CommonYesOrNo.NO);
                            detail.setTenantId(null);
                            detail.setUpdateTime(DateUtils.getNowDate());
                            detail.setSapReceivedQty(ele.getCompleteQty());
                            detail.setId(ele.getDetailId());
                            detail.setSapReceivedQty(ele.getCompleteQty());
                            purchaseOrderDetailMapper.updateWmsPurchaseOrderDetail(detail);

                            // 修改中间表采购单据明细处理状态
                            WmsPurchaseOrderDetailRaw detailRaw = new WmsPurchaseOrderDetailRaw();
                            detailRaw.setId(ele.getId());
                            detailRaw.setProcessFlag(PurchaseOrderConstant.ORDER_RAW_FLAG_YES);
                            detailRaw.setProcessTime(DateUtils.getNowDate());
                            wmsPurchaseOrderDetailRawMapper.updateWmsPurchaseOrderDetailRaw(detailRaw);

                            // 修改采购订单明细之后,当采购订单现状态为关闭且新需求数量大于现已制单数量,则修改采购订单状态为处理中
                            BigDecimal newPlanQty = ele.getPlanQty();
                            // 已制单数量
                            BigDecimal madeQty = ele.getMadeQty();
                            if (OrderStatusConstant.ORDER_STATUS_COMPLETE.equals(orderStatus) && newPlanQty.compareTo(madeQty) > 0) {
                                PurchaseOrder purchaseOrder = new PurchaseOrder();
                                purchaseOrder.setId(orderId);
                                purchaseOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_PART_COMPLETE);
                                purchaseOrder.setUpdateTime(DateUtils.getNowDate());
                                purchaseOrderMapper.updatePurchaseOrder(purchaseOrder);
                            }
                        });

                        // 新增
                        addAgList.forEach(ele -> {
                            // 当新采购明细的数量大于0并且删除标志不为L时,才会在业务表中新增明细
                            BigDecimal totalQty = ele.getPlanQty();
                            String delFlag = ele.getLoekz();//删除标记，L表示删除
                            if (totalQty.compareTo(BigDecimal.ZERO) > 0 && !PurchaseOrderConstant.ORDER_RAW_DELETE.equals(delFlag)) {
                                // 获取物料,工厂主数据信息
                                PurchaseOrderDetailRawDto detailRaw = new PurchaseOrderDetailRawDto();
                                detailRaw.setMaterialNo(ele.getMaterialNo());
                                detailRaw.setFactoryCode(ele.getFactoryCode());
                                detailRaw.setCompanyId(order.getCompanyId());
                                handleOrderDetail(detailRaw);
                                String isExempted = checkIsExempted(ele.getIsExempted());
                                String isConsigned = checkIsConsigned(ele.getIsConsigned());
                                PurchaseOrderDetail detail = new PurchaseOrderDetail();
                                BeanUtils.copyProperties(ele,detail);
                                if(detail.getDeliveryDate() == null){
                                    detail.setDeliveryDate(DateUtils.parseDate("1900-01-01 00:00:00"));
                                }
                                detail.setTotalQty(totalQty);
                                detail.setMadeQty(BigDecimal.ZERO);
                                detail.setPurchaseOrderId(orderId);
                                detail.setTenantId(null);
                                detail.setTotalPlanQty(totalQty);
                                detail.setMaterialId(detailRaw.getMaterialId());
                                detail.setMaterialName(detailRaw.getMaterialName());
                                detail.setOldMaterialNo(detailRaw.getOldMaterialNo());
                                detail.setFactoryName(detailRaw.getFactoryName());
                                detail.setFactoryId(detailRaw.getFactoryId());
                                detail.setIsExempted(isExempted);
                                detail.setIsConsigned(isConsigned);
                                detail.setDelFlag(CommonYesOrNo.NO);
                                detail.setUpdateTime(DateUtils.getNowDate());
                                detail.setSapReceivedQty(ele.getCompleteQty());
                                purchaseOrderDetailMapper.insertWmsPurchaseOrderDetail(detail);
                            }

                            // 修改中间表采购单据明细处理状态
                            WmsPurchaseOrderDetailRaw detailRaw = new WmsPurchaseOrderDetailRaw();
                            detailRaw.setId(ele.getId());
                            detailRaw.setProcessFlag(PurchaseOrderConstant.ORDER_RAW_FLAG_YES);
                            detailRaw.setProcessTime(DateUtils.getNowDate());
                            wmsPurchaseOrderDetailRawMapper.updateWmsPurchaseOrderDetailRaw(detailRaw);
                        });

                        // 更新采购单据业务表记录的单据类型字段
                        PurchaseOrder purchaseOrder = new PurchaseOrder();
                        purchaseOrder.setId(orderId);
                        purchaseOrder.setOrderType(checkOrderType(billType));
                        purchaseOrder.setBillType(billType);
                        purchaseOrder.setUpdateTime(DateUtils.getNowDate());
                        purchaseOrderMapper.updatePurchaseOrder(purchaseOrder);

                        // 修改中间表采购单据处理状态
                        WmsPurchaseOrderRaw purchaseOrderRaw = new WmsPurchaseOrderRaw();
                        purchaseOrderRaw.setId(order.getId());
                        purchaseOrderRaw.setProcessFlag(PurchaseOrderConstant.ORDER_RAW_FLAG_YES);
                        purchaseOrderRaw.setUpdateTime(DateUtils.getNowDate());
                        wmsPurchaseOrderRawMapper.updateWmsPurchaseOrderRaw(purchaseOrderRaw);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new ServiceException("采购订单中间表同步业务表失败！");
                    }
                }
            } else {
                // 修改中间表采购单据处理状态
                WmsPurchaseOrderRaw purchaseOrderRaw = new WmsPurchaseOrderRaw();
                purchaseOrderRaw.setId(order.getId());
                purchaseOrderRaw.setProcessFlag(PurchaseOrderConstant.ORDER_RAW_FLAG_YES);
                purchaseOrderRaw.setUpdateTime(DateUtils.getNowDate());
                wmsPurchaseOrderRawMapper.updateWmsPurchaseOrderRaw(purchaseOrderRaw);
            }
        }else{
            // 新增模式
            try{
                // 得到中间表的采购单据明细数据,未处理的
                if(iOrderDtList !=null && iOrderDtList.size()>0) {
                    // 单据明细是否全部没有物料代码
                    boolean allMaterialCodeEmpty = iOrderDtList.stream().allMatch(ele -> (ele.getMaterialNo() == null || ele.getMaterialNo().isEmpty()));

                    // 检查中间表采购单据下的明细信息,如果都没有物料号则不生成业务表数据直接修改中间表数据状态,否则正常处理单据和明细数据
                    if (allMaterialCodeEmpty) {
                        // 修改中间表采购单据处理状态
                        WmsPurchaseOrderRaw purchaseOrderRaw = new WmsPurchaseOrderRaw();
                        purchaseOrderRaw.setId(order.getId());
                        purchaseOrderRaw.setProcessFlag(PurchaseOrderConstant.ORDER_RAW_FLAG_YES);
                        purchaseOrderRaw.setUpdateTime(DateUtils.getNowDate());
                        wmsPurchaseOrderRawMapper.updateWmsPurchaseOrderRaw(purchaseOrderRaw);

                        // 修改中间表采购单据明细处理状态
                        for (WmsPurchaseOrderDetailRaw detail : iOrderDtList) {
                            WmsPurchaseOrderDetailRaw detailRaw = new WmsPurchaseOrderDetailRaw();
                            detailRaw.setId(detail.getId());
                            detailRaw.setProcessFlag(PurchaseOrderConstant.ORDER_RAW_FLAG_YES);
                            detailRaw.setProcessTime(DateUtils.getNowDate());
                            wmsPurchaseOrderDetailRawMapper.updateWmsPurchaseOrderDetailRaw(detailRaw);
                        }
                    } else {
                        // 保存单据
                        handleOrder(order);
                        PurchaseOrder purchaseOrder = new PurchaseOrder();
                        BeanUtils.copyProperties(order,purchaseOrder);
                        purchaseOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_NEW);
                        purchaseOrder.setCreateTime(DateUtils.getNowDate());
                        purchaseOrder.setOrderType(checkOrderType(purchaseOrder.getBillType()));
                        purchaseOrder.setTenantId(null);
                        purchaseOrderMapper.insertPurchaseOrder(purchaseOrder);
                        // 保存采购单据明细
                        for(WmsPurchaseOrderDetailRaw iDetail : iOrderDtList){
                            // 判断是否质检,是否寄售
                            String isExempted = checkIsExempted(iDetail.getIsExempted());
                            String isConsigned = checkIsConsigned(iDetail.getIsConsigned());
                            PurchaseOrderDetailRawDto detail = new PurchaseOrderDetailRawDto();
                            detail.setFactoryCode(iDetail.getFactoryCode());
                            detail.setMaterialNo(iDetail.getMaterialNo());
                            detail.setCompanyId(order.getCompanyId());
                            handleOrderDetail(detail);
                            PurchaseOrderDetail bDetail = new PurchaseOrderDetail();
                            BeanUtils.copyProperties(iDetail,bDetail);
                            if(bDetail.getDeliveryDate() == null){
                                detail.setDeliveryDate(DateUtils.parseDate("1900-01-01 00:00:00"));
                            }
                            bDetail.setPurchaseOrderId(purchaseOrder.getId());
                            bDetail.setMaterialId(detail.getMaterialId());
                            bDetail.setMaterialName(detail.getMaterialName());
                            bDetail.setOldMaterialNo(detail.getOldMaterialNo());
                            bDetail.setFactoryName(detail.getFactoryName());
                            bDetail.setFactoryId(detail.getFactoryId());
                            bDetail.setIsExempted(isExempted);
                            bDetail.setIsConsigned(isConsigned);
                            bDetail.setTotalQty(bDetail.getTotalPlanQty());
                            bDetail.setCreateTime(DateUtils.getNowDate());
                            bDetail.setDelFlag(CommonYesOrNo.NO);
                            bDetail.setWmsReceivedQty(BigDecimal.ZERO);
                            bDetail.setSapReceivedQty(BigDecimal.ZERO);
                            bDetail.setMadeQty(BigDecimal.ZERO);
                            bDetail.setTenantId(null);
                            bDetail.setMaterialGroup(detail.getMaterialGroup());
                            bDetail.setMaterialGroupDesc(detail.getMaterialGroupDesc());
                            bDetail.setTenantId(null);
                            purchaseOrderDetailMapper.insertWmsPurchaseOrderDetail(bDetail);
                            // 修改中间表采购单据明细处理状态
                            WmsPurchaseOrderDetailRaw detailRaw = new WmsPurchaseOrderDetailRaw();
                            detailRaw.setId(iDetail.getId());
                            detailRaw.setProcessFlag(PurchaseOrderConstant.ORDER_RAW_FLAG_YES);
                            detailRaw.setUpdateTime(DateUtils.getNowDate());
                            detailRaw.setProcessTime(DateUtils.getNowDate());
                            wmsPurchaseOrderDetailRawMapper.updateWmsPurchaseOrderDetailRaw(detailRaw);
                        }
                        // 修改中间表采购单据处理状态
                        WmsPurchaseOrderRaw purchaseOrderRaw = new WmsPurchaseOrderRaw();
                        purchaseOrderRaw.setId(order.getId());
                        purchaseOrderRaw.setProcessFlag(PurchaseOrderConstant.ORDER_RAW_FLAG_YES);
                        purchaseOrderRaw.setUpdateTime(DateUtils.getNowDate());
                        wmsPurchaseOrderRawMapper.updateWmsPurchaseOrderRaw(purchaseOrderRaw);
                    }
                }else{
                    // 修改中间表采购单据处理状态
                    WmsPurchaseOrderRaw purchaseOrderRaw = new WmsPurchaseOrderRaw();
                    purchaseOrderRaw.setId(order.getId());
                    purchaseOrderRaw.setProcessFlag(PurchaseOrderConstant.ORDER_RAW_FLAG_YES);
                    purchaseOrderRaw.setUpdateTime(DateUtils.getNowDate());
                    wmsPurchaseOrderRawMapper.updateWmsPurchaseOrderRaw(purchaseOrderRaw);
                }
            }catch(Exception e){
                e.printStackTrace();
                throw new ServiceException("采购订单中间表同步业务表失败！");
            }
        }
    }


    /**
     * SAP同步采购协议计划行
     */
    @Override
    public void purOrderScheduleSync(String companyCode) {
        logger.info("{}从sap同步采购协议的计划信息...begin",companyCode);
        try{
            // 查询采购协议单据
            PurchaseOrder purchaseOrder = new PurchaseOrder();
            purchaseOrder.setOrderType(StockInOrderConstant.ORDER_TYPE_AGREEMENT);
            purchaseOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_COMPLETE);
            purchaseOrder.setCompanyCode(companyCode);
            List<PurchaseOrder> orderList = purchaseOrderMapper.selectPurOrderByOrderType(purchaseOrder);
            if(orderList != null && orderList.size() > 0){
                // 每次调整一条采购单据
                for(PurchaseOrder order : orderList){
                    try{
                        // 一次处理一个采购单据
                        modifyPurOrderDtAg(order);
                    }catch(Exception e){
                        logger.error("调整采购协议的计划信息异常，数据："+ order.toString() + "，异常："+e);
                        throw new ServiceException("同步采购协议失败!");
                    }
                }
            }
        }catch (Exception e){
            throw new ServiceException("同步采购协议失败!");
        }
    }

    /**
     * 调整采购协议的协议明细
     */
    public void modifyPurOrderDtAg(PurchaseOrder order){
        Long orderId = order.getId();
        String orderNo = order.getOrderNo();
        logger.info("调整采购协议的计划信息["+orderNo+"]...begin");
        try{
            MsgObject reqMo = new MsgObject(IMsgObject.MOType.initSR);
            reqMo.setReqValue("PURCHASEORDER",orderNo);
            reqMo.setReqValue("ITEMS", "X");
            reqMo.setReqValue("SCHEDULES", "X");
            reqMo.setReqValue("HISTORY","X");
            // 远程feign 调用(BAPI_PO_GETDETAIL)
            byte[] result =  remoteEsbSendService.sendToEsb(EsbSendSapConstant.Z_IWMS_PO_01, reqMo.toString().getBytes());
            // 转化为标准结果
            MsgObject resMo = new MsgObject(result, IMsgObject.MOType.initSR);
            // 返回物料数据
            List<GroupRecord> itItems = resMo.getResGroupRecord("IT_ITEM");
            // 返回多行数据
            List<Map<String,Object>> returnList = new ArrayList();
            for (int i = 0; i < itItems.size(); i++) {
                GroupRecord item = itItems.get(i);
                Map<String,Object> returnMap = new HashMap();
                for (int j = 0; j < item.getFieldSize(); j++) {
                    String key = item.getFieldKey(j);
                    String value = item.getFieldValue(j);
                    if(key.equals("PO_NUMBER") || key.equals("PO_ITEM") || key.equals("MATERIAL") || key.equals("QUANTITY") || key.equals("DELETE_IND")){
                        returnMap.put(key, value);
                    }
                }
                returnList.add(returnMap);
            }
            // 返回收货数量数据
            List<GroupRecord> receiveQtyList = resMo.getResGroupRecord("IT_HISTORY");
            List<Map<String,Object>> retDtDlvQtyList = new ArrayList();//返回多行数据
            for (int i = 0; i < receiveQtyList.size(); i++) {
                Map<String,Object> retDtDlvQtyMap = new HashMap();
                GroupRecord item = receiveQtyList.get(i);
                for (int j = 0; j < item.getFieldSize(); j++) {
                    String key = item.getFieldKey(j);
                    String value = item.getFieldValue(j);
                    if(key.equals("EBELP") || key.equals("DZEKKN") || key.equals("LSMNG")){
                        retDtDlvQtyMap.put(key, value);
                    }
                }
                retDtDlvQtyList.add(retDtDlvQtyMap);
            }
            // 返回协议计划数据
            List<GroupRecord> agreementList = resMo.getResGroupRecord("IT_SCHEDULES");
            List<Map<String,Object>> retDtAgList = new ArrayList<Map<String,Object>>();//返回多行数据
            for (int i = 0; i < agreementList.size(); i++) {
                GroupRecord item = agreementList.get(i);
                Map<String,Object> retDtAgMap = new HashMap();
                for (int j = 0; j < item.getFieldSize(); j++) {
                    String key = item.getFieldKey(j);
                    String value = item.getFieldValue(j);
                    if(key.equals("PO_ITEM") || key.equals("SERIAL_NO") || key.equals("DELIV_DATE") || key.equals("QUANTITY")){
                        retDtAgMap.put(key, value);
                    }
                }
                retDtAgList.add(retDtAgMap);
            }
            // 得到SAP采购协议明细删除标志,L表示删除
            String orderDtDel = returnList.get(0).get("DELETE_IND")+"";
            if(PurchaseOrderConstant.ORDER_RAW_DELETE.equals(orderDtDel)){
                //如果SAP采购协议明细删除标志为L,既删除明细,则置协议计划行列表为空列表
                retDtAgList.clear();
            }
            PurchaseOrderDetail orderDetail1 = new PurchaseOrderDetail();
            orderDetail1.setPurchaseOrderId(orderId);
            orderDetail1.setDelFlag(CommonYesOrNo.NO);
            List<PurchaseOrderDetail> orderDtList = purchaseOrderDetailMapper.selectOrderDetail(orderDetail1);
            // 如果SAP采购协议明细需要删除并且当前系统中采购协议已经没有明细数据,则结束操作
            if(PurchaseOrderConstant.ORDER_RAW_DELETE.equals(orderDtDel) && (orderDtList == null || orderDtList.size() == 0)){
                logger.info("调整采购协议的计划信息{}...end",orderNo);
                return;
            }
            // 删除明细协议数据中的重复行计数器数据
//            List<PurchaseVo> agByLineNum = purchaseOrderDetailMapper.selectPurOrderDtAgByLineNum(new PurchaseOrderDetail(){{
//                setPurchaseOrderId(orderId);
//            }});

//            if(agByLineNum != null && agByLineNum.size() > 0){
//                agByLineNum.stream().forEach(item -> {
//                    // 删除重复的行计数器数据
//                    purchaseOrderDetailMapper.deleteOrderDetailById(new PurchaseVo(){{
//                        setPurchaseOrderId(orderId);
//                        setDelFlag(CommonYesOrNo.YES);
//                        setPurchaseOrderNo(item.getPurchaseOrderNo());
//                        setAgId(item.getAgId());
//                    }});
//                });
//            }
            // 调整采购协议下的计划信息
            // 删除列表
            List<PurchaseOrderDetail> delAgList = new ArrayList();
            // 修改列表
            List<PurchaseOrderDetail> updAgList = new ArrayList();
            // 新增列表
            List<PurchaseOrderDetail> addAgList = new ArrayList();
            // 其他列表,不做业务处理的中间表数据
            List<PurchaseOrderDetail> otherAgList = new ArrayList();

            // 现有采购计划与新采购计划比对,得到删除采购计划
            if(orderDtList != null && orderDtList.size() > 0){
                orderDtList.stream().forEach(item -> {
                    // 在新采购计划中查找现有采购计划的计划行计数器
                    Long lineNum = Long.valueOf(item.getDeliveryLineNo());
                    Map<String, Object> lineNumAg = retDtAgList.stream()
                            .filter(ele -> lineNum.equals(Long.parseLong(ele.get("SERIAL_NO")+"")))
                            .findAny().orElse(null);
                    if(lineNumAg == null){
                        // 没有找到现有计划行计数器则删除当前采购计划
                        delAgList.add(item);
                    }
                });
            }
            // 新采购计划与现有采购计划比较,得到删除、修改、新增的采购计划
            retDtAgList.stream().forEach(item -> {
                // 在现有采购计划中查找新采购计划的行号
                Long lineNum = Long.parseLong(Objects.toString(item.get("SERIAL_NO"),""));
                PurchaseOrderDetail lineNumAg = null;
                if(orderDtList != null && orderDtList.size() > 0){
                    lineNumAg = orderDtList.stream()
                            .filter(ele -> lineNum.equals(Long.valueOf(ele.getDeliveryLineNo())))
                            .findAny().orElse(null);
                }
                if(lineNumAg == null){
                    // 根据采购订单号,行号获取明细信息
                    if(orderDtList != null && orderDtList.size() > 0){
                        PurchaseOrderDetail orderDetail = orderDtList.get(0);
                        // 没有找到计划行计数器且新采购计划的送货数量大于0,则作为新增
                        BigDecimal planQty = BigDecimal.valueOf(Double.parseDouble(item.get("QUANTITY").toString()));
                        if(planQty.compareTo(BigDecimal.ZERO) > 0){
                            // 送货数量大于0,则作为新增
                            PurchaseOrderDetail detail = new PurchaseOrderDetail();
                            BeanUtils.copyProperties(orderDetail,detail);
                            BigDecimal qty = BigDecimal.valueOf(Double.valueOf(item.get("QUANTITY").toString()));
                            detail.setTotalPlanQty(qty);
                            detail.setTotalQty(qty);
                            detail.setPlanQty(qty);
                            detail.setTenantId(null);
                            detail.setDeliveryDate(DateUtils.parseDate(item.get("DELIV_DATE")));
                            detail.setDeliveryLineNo(item.get("SERIAL_NO").toString());
                            detail.setSapReceivedQty(BigDecimal.ZERO);
                            detail.setWmsReceivedQty(BigDecimal.ZERO);
                            detail.setCreateTime(DateUtils.getNowDate());
                            detail.setMadeQty(BigDecimal.ZERO);
                            addAgList.add(detail);
                        }else{
                            // 送货数量等于0,则不做处理
                            otherAgList.add(orderDetail);
                        }
                    }
                }else{
                    // 根据采购订单号,行号,计划行计数器获取明细信息
                    PurchaseOrderDetail detail = new PurchaseOrderDetail();
                    detail.setPurchaseOrderNo(orderNo);
                    detail.setDeliveryLineNo(item.get("SERIAL_NO").toString());
                    detail.setPurchaseLineNo(Convert.toStr(Objects.toString(item.get("PO_ITEM"), "0")));
                    List<PurchaseOrderDetail> detailList = purchaseOrderDetailMapper.selectOrderDetail(detail);
                    if(detailList != null && detailList.size() > 0) {
                        PurchaseOrderDetail orderDetail = detailList.get(0);
                        BigDecimal planQty = BigDecimal.valueOf(Double.valueOf(item.get("QUANTITY").toString()));
                        if(planQty.compareTo(BigDecimal.ZERO) == 0){
                            // 找到但新数量为0,则删除当前采购计划
                            delAgList.add(orderDetail);
                        }else{
                            // 找到且新数量不为0,则更新当前采购计划
                            orderDetail.setPlanQty(BigDecimal.valueOf(Double.valueOf(item.get("QUANTITY").toString())));
                            orderDetail.setDeliveryDate(DateUtils.parseDate(item.get("DELIV_DATE")));
                            orderDetail.setCreateTime(DateUtils.getNowDate());
                            updAgList.add(orderDetail);
                        }
                    }
                }
            });

            //处理有变化的数据
            try{
                // 删除
                delAgList.forEach(ele -> {
                    // 逻辑删除采购单据明细协议
                    PurchaseVo purchaseVo = new PurchaseVo();
                    purchaseVo.setDeliveryLineNo(ele.getDeliveryLineNo());
                    purchaseVo.setPurchaseOrderId(ele.getPurchaseOrderId());
                    purchaseVo.setDelFlag(CommonYesOrNo.YES);
                    purchaseOrderDetailMapper.deleteOrderDetailById(purchaseVo);
                });
                // 修改
                updAgList.forEach(ele -> {
                    purchaseOrderDetailMapper.updateWmsPurchaseOrderDetail(ele);
                });
                // 新增
                addAgList.forEach(ele -> {
                    purchaseOrderDetailMapper.insertWmsPurchaseOrderDetail(ele);
                });

                PurchaseOrderDetail orderDetail = new PurchaseOrderDetail();
                orderDetail.setPurchaseOrderId(orderId);
                orderDetail.setDelFlag(CommonYesOrNo.NO);
                List<PurchaseOrderDetail> detailList = purchaseOrderDetailMapper.selectOrderDetail(orderDetail);
                // 得到新总sap已收货数量
                BigDecimal dtAsnQty = BigDecimal.valueOf(Double.valueOf(retDtDlvQtyList.get(0).get("LSMNG").toString()));
                boolean flag = true;
                for (PurchaseOrderDetail detail : detailList) {
                    if(flag){
                        BigDecimal totalQty = detail.getTotalQty();
                        BigDecimal sapReceivedQty;
                        if(dtAsnQty.compareTo(totalQty) > 0){
                            sapReceivedQty = totalQty;
                            dtAsnQty = dtAsnQty.subtract(totalQty);
                        }else{
                            sapReceivedQty = dtAsnQty;
                            flag = false;
                        }
                        BigDecimal finalSapReceivedQty = sapReceivedQty;
                        // wms实际需求数量 = 总需求数量 - sap已收货数量 + wms已收货数量
                        BigDecimal actNeedQty = totalQty.subtract(sapReceivedQty).add(detail.getWmsReceivedQty());
                        PurchaseOrderDetail purchaseOrderDetail = new PurchaseOrderDetail();
                        purchaseOrderDetail.setId(detail.getId());
                        purchaseOrderDetail.setSapReceivedQty(finalSapReceivedQty);
                        purchaseOrderDetail.setUpdateTime(DateUtils.getNowDate());
                        purchaseOrderDetail.setTotalPlanQty(actNeedQty);
                        purchaseOrderDetailMapper.updateWmsPurchaseOrderDetail(purchaseOrderDetail);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
            throw new ServiceException("sap获取采购协议计划信息失败！");
        }
    }

    @Override
    public void purOrderAsnQtySync(String companyCode) {
        logger.info("从sap同步采购单据的已收货数量...begin");
        try {
            // 采购单据
            PurchaseOrder purchaseOrder = new PurchaseOrder();
            purchaseOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_COMPLETE);
            purchaseOrder.setCompanyCode(companyCode);
            List<PurchaseOrder> orderList = purchaseOrderMapper.selectPurOrderByOrderType(purchaseOrder);
            if (orderList != null && orderList.size() > 0) {
                // 每次调整一条采购单据的已制单数量
                for (PurchaseOrder order : orderList) {
                    try {
                        // 一次处理一个采购单据的已制单数
                        modifyPurOrderAsnQty(order);
                    } catch (Exception e) {
                        logger.error("调整采购单据已制单数量异常，数据：" + order.toString() + "，异常：" + e);
                        throw new ServiceException("同步已制单数量失败!");
                    }
                }
            }
        } catch (Exception e) {
            throw new ServiceException("同步已制单数量失败!");
        }
    }

    /**
     * 调整采购单据的已制单数量
     *
     * @return FormEngineResponse
     * @author xurui
     */
    public void modifyPurOrderAsnQty(PurchaseOrder order) {
        String orderNo = order.getOrderNo();
        logger.info("调整采购单据已制单数量{}...begin",orderNo);
        try{
            MsgObject reqMo = new MsgObject(IMsgObject.MOType.initSR);
            reqMo.setReqValue("PURCHASEORDER",orderNo);
            reqMo.setReqValue("ITEMS", "X");
            reqMo.setReqValue("SCHEDULES", "X");
            reqMo.setReqValue("HISTORY","X");
            // 远程feign 调用(BAPI_PO_GETDETAIL)
            byte[] result =  remoteEsbSendService.sendToEsb(EsbSendSapConstant.Z_IWMS_PO_01, reqMo.toString().getBytes());
            // 转化为标准结果
            MsgObject resMo = new MsgObject(result, IMsgObject.MOType.initSR);
            // 返回物料数据
            List<GroupRecord> itItems = resMo.getResGroupRecord("IT_ITEM");
            // 返回多行数据
            List<Map<String,Object>> returnList = new ArrayList();
            for (int i = 0; i < itItems.size(); i++) {
                GroupRecord item = itItems.get(i);
                Map<String,Object> returnMap = new HashMap();
                for (int j = 0; j < item.getFieldSize(); j++) {
                    String key = item.getFieldKey(j);
                    String value = item.getFieldValue(j);
                    if(key.equals("PO_NUMBER") || key.equals("PO_ITEM") || key.equals("MATERIAL") || key.equals("QUANTITY") || key.equals("DELETE_IND")){
                        returnMap.put(key, value);
                    }
                }
                returnList.add(returnMap);
            }
            // 返回收货数量数据
            List<GroupRecord> receiveQtyList = resMo.getResGroupRecord("IT_HISTORY");
            List<Map<String,Object>> retDtDlvQtyList = new ArrayList();//返回多行数据
            for (int i = 0; i < receiveQtyList.size(); i++) {
                Map<String,Object> retDtDlvQtyMap = new HashMap();
                GroupRecord item = receiveQtyList.get(i);
                for (int j = 0; j < item.getFieldSize(); j++) {
                    String key = item.getFieldKey(j);
                    String value = item.getFieldValue(j);
                    if(key.equals("EBELP") || key.equals("DZEKKN") || key.equals("LSMNG")){
                        retDtDlvQtyMap.put(key, value);
                    }
                }
                retDtDlvQtyList.add(retDtDlvQtyMap);
            }
            // 调整采购单据下每个明细的已收货数量
            if(!order.getBillType().equals(PurchaseOrderConstant.SAP_LPA) && !order.getBillType().equals(PurchaseOrderConstant.SAP_UB) && !order.getBillType().equals(PurchaseOrderConstant.SAP_Z30)){
                // 正常采购订单
                String lineNo = Convert.toStr(retDtDlvQtyList.get(0).get("EBELP"));
                PurchaseOrderDetail purchaseOrderDetail = new PurchaseOrderDetail();
                purchaseOrderDetail.setPurchaseOrderId(order.getId());
                purchaseOrderDetail.setDelFlag(CommonYesOrNo.NO);
                purchaseOrderDetail.setPurchaseLineNo(lineNo);
                List<PurchaseOrderDetail> detailList = purchaseOrderDetailMapper.selectOrderDetail(purchaseOrderDetail);
                if(detailList != null && detailList.size() > 0){
                    detailList.stream().forEach(d -> {
                        String line = d.getPurchaseLineNo();
                        Map<String, Object> sapMap = retDtDlvQtyList.stream()
                                .filter(ele -> line.equals(Long.parseLong(ele.get("EBELP")+"")))
                                .findAny().orElse(null);
                        if(sapMap == null){
                            throw new ServiceException("采购订单已收货数量同步失败,采购订单号:" + order.getOrderNo() + "行号:" + lineNo +"sap不存在!");
                        }
                        // sap已收货数量
                        BigDecimal sapReceivedQty = BigDecimal.valueOf(Double.valueOf(sapMap.get("LSMNG").toString()));
                        // wms实际需求数量 = 总需求数量 - sap已收货数量 + wms已收货数量
                        BigDecimal actNeedQty = d.getTotalQty().subtract(sapReceivedQty).add(d.getWmsReceivedQty());
                        // 需求数量 = wms总需求数量 - 已制单数量
                        BigDecimal needQty =  actNeedQty.subtract(d.getMadeQty());
                        PurchaseOrderDetail orderDetail = new PurchaseOrderDetail();
                        orderDetail.setId(d.getId());
                        orderDetail.setUpdateTime(DateUtils.getNowDate());
                        orderDetail.setSapReceivedQty(sapReceivedQty);
                        orderDetail.setTotalPlanQty(actNeedQty);
                        orderDetail.setPlanQty(needQty);
                        purchaseOrderDetailMapper.updateWmsPurchaseOrderDetail(orderDetail);
                    });
                }
            }else if(order.getBillType().equals(PurchaseOrderConstant.SAP_LPA)){
                // 计划协议
                PurchaseOrderDetail purchaseOrderDetail = new PurchaseOrderDetail();
                purchaseOrderDetail.setPurchaseOrderId(order.getId());
                purchaseOrderDetail.setDelFlag(CommonYesOrNo.NO);
                List<PurchaseOrderDetail> detailList = purchaseOrderDetailMapper.selectOrderDetail(purchaseOrderDetail);
                BigDecimal dtAsnQty = BigDecimal.valueOf(Double.valueOf(retDtDlvQtyList.get(0).get("LSMNG").toString()));
                boolean flag = true;
                for (PurchaseOrderDetail detail : detailList) {
                    if(flag){
                        // 总需求数量
                        BigDecimal totalQty = detail.getTotalQty();
                        BigDecimal sapReceivedQty;
                        if(dtAsnQty.compareTo(totalQty) > 0){
                            sapReceivedQty = totalQty;
                            dtAsnQty = dtAsnQty.subtract(totalQty);
                        }else{
                            sapReceivedQty = dtAsnQty;
                            flag = false;
                        }
                        // wms实际需求数量 = 总需求数量 - sap已收货数量 + wms已收货数量
                        BigDecimal actNeedQty = totalQty.subtract(sapReceivedQty).add(detail.getWmsReceivedQty());
                        // 需求数量 = wms总需求数量 - 已制单数量
                        BigDecimal needQty =  actNeedQty.subtract(detail.getMadeQty());
                        BigDecimal finalSapReceivedQty = sapReceivedQty;
                        PurchaseOrderDetail orderDetail = new PurchaseOrderDetail();
                        orderDetail.setId(detail.getId());
                        orderDetail.setSapReceivedQty(finalSapReceivedQty);
                        orderDetail.setUpdateTime(DateUtils.getNowDate());
                        orderDetail.setTotalPlanQty(actNeedQty);
                        orderDetail.setPlanQty(needQty);
                        purchaseOrderDetailMapper.updateWmsPurchaseOrderDetail(orderDetail);
                    }
                }
            }

//           if (retQtyList != null && retQtyList.size() > 0) {
//               for (Map retQty : retQtyList) {
//                   String purchaseLineNo = retQty.get("PO_ITEM").toString();
//                   String madeQty = retQty.get("DELIV_QTY").toString();
//                   purchaseOrderDetailMapper.updateMadeQty
//               }
//
//           }
//           // 调整采购单据下每个明细的计划数量
//           if (returnList != null && returnList.size() > 0) {
//               Map<String, Object> updAsnQtyData = new HashMap<String, Object>();
//               updAsnQtyData.put("orderId", orderId);
//               updAsnQtyData.put("orderNo", orderNo);
//               CustomFormModel updModel = new CustomFormModel();
//               for (Map retQty : returnList) {
//                   String orderLine = retQty.get("PO_ITEM") + "";
//                   updAsnQtyData.put("orderLine", Integer.parseInt(orderLine));
//                   String quantity = retQty.get("QUANTITY") + "";
//                   updAsnQtyData.put("quantity", Double.parseDouble(quantity));
//                   updModel.setData(updAsnQtyData);
//                   updModel.setSqlId("persistence/sap/UpdPurOrderDtAsnQuantity");
//                   formMapper.saveCustom(updModel);
//                   FormLogger.info("orderNo[" + orderNo + "],orderLine[" + orderLine + "],quantity[" + quantity + "]");
//               }
//           }
        }catch (Exception e){
            throw new ServiceException("同步采购单据已交货数量失败!");
        }
    }


    public String checkIsConsigned(String itemType){
        // 项目类别
        if(PurchaseOrderConstant.SAP_CONSIGNED.equals(itemType)){
            // 寄售
            return PurchaseOrderConstant.IS_CONSIGNED;
        }else{
            // 自有
            return PurchaseOrderConstant.NOT_CONSIGNED;
        }
    }

    public void handleOrderDetail (PurchaseOrderDetailRawDto detail){
        // 物料
        Map<String, Map<String, Object>> materialMaps = wmsPurchaseOrderRawMapper.getMaterialMaps();
        // 工厂
        Map<String, Map<String, Object>> factoryMaps = wmsPurchaseOrderRawMapper.getFactoryMaps();
        // 工厂信息
        if(factoryMaps.containsKey(detail.getFactoryCode())) {
            Map<String, Object> factoryMap = factoryMaps.get(detail.getFactoryCode());
            detail.setFactoryId(Long.valueOf(factoryMap.get("id").toString()));
            detail.setFactoryName((String) factoryMap.get("factoryName"));
        }else{
            // 主数据新增工厂信息
            FactoryDto factoryDto = new FactoryDto();
            factoryDto.setFactoryCode(detail.getFactoryCode());
            factoryDto.setSource(MainDataSourceConstant.INTERFACE);
            factoryDto.setFactoryName(detail.getFactoryName() == null ? "" : detail.getFactoryName());
            factoryDto.setStatus(CommonYesOrNo.NO);
            // todo companyId 必填,不然报错
            factoryDto.setCompanyId(detail.getCompanyId());
            AjaxResult result = iMainDataService.insertFactory(factoryDto);
            if("".equals(Objects.toString(result.get("data"),"")) || result.isError()){
                throw new ServiceException("主数据新增工厂信息失败！");
            }
            String str = JSON.toJSONString(result.get("data"));
            WmsFactory wmsFactory = JSON.parseObject(str, WmsFactory.class);
            detail.setFactoryId(wmsFactory.getId());
        }
        // 物料信息
        if (materialMaps.containsKey(detail.getMaterialNo())) {
            Map<String, Object> materialMap = materialMaps.get(detail.getMaterialNo());
            detail.setMaterialId(Long.valueOf(materialMap.get("id").toString()) );
            detail.setMaterialName((String) materialMap.get("materialName"));
            // 旧物料号
            if (Objects.nonNull(materialMap.get("oldMaterialNo"))) {
                detail.setOldMaterialNo((String) materialMap.get("oldMaterialNo"));
            }
        }else{
            // 主数据新增物料信息
            WmsMaterialBasicDto basicDto = new WmsMaterialBasicDto();
            basicDto.setMaterialNo(detail.getMaterialNo());
            basicDto.setDefaultUnit(detail.getUnit());
            basicDto.setSource(MainDataSourceConstant.INTERFACE);
            basicDto.setStatus(CommonYesOrNo.NO);
            basicDto.setFactoryId(detail.getFactoryId());
            AjaxResult result = iMainDataService.insertMaterial(basicDto);
            if(result.isError()){
                throw new ServiceException("主数据新增物料信息失败！");
            }
        }
    }

    public void handleOrder (WmsPurchaseOrderRaw order){
        // 公司
        Map<String, Map<String, Object>> companyMaps = wmsPurchaseOrderRawMapper.getCompanyMaps();
        // 供应商
        Map<String, Map<String, Object>> supplyMaps = wmsPurchaseOrderRawMapper.getSupplyMaps();
        // 公司信息
        if(companyMaps.containsKey(order.getCompanyCode())) {
            Map<String, Object> companyMap = companyMaps.get(order.getCompanyCode());
            order.setCompanyName(String.valueOf(companyMap.get("companyName")));
            order.setCompanyId(Long.valueOf( companyMap.get("id").toString()));
        }else{
            // 主数据新增公司信息
            NewWmsCompany company = new NewWmsCompany();
            company.setCompanyCode(order.getCompanyCode());
            company.setSource(MainDataSourceConstant.INTERFACE);
            company.setCompanyName(order.getCompanyName() == null ? "" : order.getCompanyName());
            company.setStatus(CommonYesOrNo.NO);
            AjaxResult result = iMainDataService.insertCompany(company);
            if("".equals(Objects.toString(result.get("data"),"")) || result.isError()){
                throw new ServiceException("主数据新增公司信息失败！");
            }
            String str = JSON.toJSONString(result.get("data"));
            NewWmsCompany wmsCompany = JSON.parseObject(str, NewWmsCompany.class);
            order.setCompanyId(wmsCompany.getId());
        }
        // 供应商信息
        if(supplyMaps.containsKey(order.getSupplierCode())) {
            Map<String,Object> supplyMap = supplyMaps.get(order.getSupplierCode());
            order.setSupplierId(Long.valueOf( supplyMap.get("id").toString()) );
            order.setSupplierName((String) supplyMap.get("supplierName"));
        }else{
            // 根据公司获取工厂信息(一个公司只有一个工厂)
            FactoryVo factoryVo = new FactoryVo();
            factoryVo.setCompanyId(order.getCompanyId());
            AjaxResult factoryRes = iMainDataService.getFactory(factoryVo);
            if("".equals(Objects.toString(factoryRes.get("data"),"")) || factoryRes.isError()){
                throw new ServiceException("获取工厂信息失败！");
            }
            String str = JSON.toJSONString(factoryRes.get("data"));
            List<FactoryDto> factoryList = JSON.parseArray(str, FactoryDto.class);
            if(factoryList == null || factoryList.size() == 0){
                throw new ServiceException("公司获取不到工厂信息！");
            }
            // 主数据新增供应商信息
            WmsSupplierDto supplierDto = new WmsSupplierDto();
            supplierDto.setSupplierCode(order.getSupplierCode());
            supplierDto.setSource(MainDataSourceConstant.INTERFACE);
            supplierDto.setSupplierName(order.getSupplierName() == null ? "": order.getSupplierName());
            supplierDto.setStatus(CommonYesOrNo.NO);
            supplierDto.setFactoryId(factoryList.get(0).getId());
            AjaxResult result = iMainDataService.insertSupplier(supplierDto);
            if(result.isError()){
                throw new ServiceException("主数据新增供应商信息失败！");
            }
        }
    }

    public String checkIsExempted(String storageType){
        // 库存类型
        if(PurchaseOrderConstant.SAP_EXEMPTED.equals(storageType)){
            // 检验
            return PurchaseOrderConstant.IS_EXEMPTED;
        }else{
            // 免检
            return PurchaseOrderConstant.NOT_EXEMPTED;
        }
    }

    public String checkOrderType(String billType){
        // 中间表采购凭证类型为LPA为采购协议
        if(PurchaseOrderConstant.SAP_LPA.equals(billType)){
            return StockInOrderConstant.ORDER_TYPE_AGREEMENT;
        }else{
            // 其他采购凭证类型为采购订单或者转储订单，如Z60、NB、Z30、UB等
            if(PurchaseOrderConstant.SAP_UB.equals(billType) || PurchaseOrderConstant.SAP_Z30.equals(billType)){
                // Z30、UB归类为转储订单
                return StockInOrderConstant.ORDER_TYPE_DUMP;
            }else{
                // 剩余部分归类为采购订单
                return StockInOrderConstant.ORDER_TYPE_ORDER;
            }
        }
    }

    /**
     * 同步交货单
     * @param orderNo
     */
    @Override
    public void exchangeOrderSync(String orderNo) {
        // 从sap拉取数据
        logger.info("###手动同步交货单号：{}", orderNo);
        try{
            MsgObject reqMo = new MsgObject(IMsgObject.MOType.initSR);
            reqMo.setReqValue("VBELN", orderNo);
            // feign 调用
            byte[] result = remoteEsbSendService.sendToEsb(EsbSendSapConstant.Z_IWMS_SEND_DELIVERY, reqMo.toString().getBytes());
            // 转化为标准结果
            MsgObject resMo = new MsgObject(result, IMsgObject.MOType.initSR);
            logger.info("esb返回结果：" + resMo.toString());
            String errorMsg = resMo.getResValue("ERRORMSG");
            String flag = resMo.getResValue("FLAG");
            if (!SapReqOrResConstant.SAP_RETURN_TYPE.equals(flag)) {
                logger.error(errorMsg);
                throw new ServiceException("交货单同步失败，原因：" + errorMsg);
            }
            // 凭证号
            // 头部信息
            List<GroupRecord> headerList = resMo.getResGroupRecord("IT_HEAD");
            // 返回多行数据
            List<Map<String,Object>> orderList = new ArrayList();
            for (int i = 0; i < headerList.size(); i++) {
                GroupRecord item = headerList.get(i);
                Map<String,Object> returnMap = new HashMap();
                for (int j = 0; j < item.getFieldSize(); j++) {
                    String key = item.getFieldKey(j);
                    String value = item.getFieldValue(j);
                    if(key.equals("VBELN") || key.equals("VKORG") || key.equals("ERNAM") || key.equals("ERDAT") || key.equals("LFART")
                            || key.equals("KUNNR") || key.equals("NAME1") || key.equals("VSART") || key.equals("BEZEI")
                            || key.equals("ESBTIMESTAMP") || key.equals("WADAT")){
                        returnMap.put(key, value);
                    }
                }
                orderList.add(returnMap);
            }
            if (orderList.size() == 0){
                throw new ServiceException(String.format("SAP返回错误：%s",resMo.getResValue("ERRORMSG")));
            }
            // 交货单明细数据
            List<GroupRecord> itItems = resMo.getResGroupRecord("IT_ITEM");
            // 返回多行数据
            List<Map<String,Object>> lineList = new ArrayList();
            for (int i = 0; i < itItems.size(); i++) {
                GroupRecord item = itItems.get(i);
                Map<String,Object> returnMap = new HashMap();
                for (int j = 0; j < item.getFieldSize(); j++) {
                    String key = item.getFieldKey(j);
                    String value = item.getFieldValue(j);
                    if(key.equals("VBELN") || key.equals("POSNR") || key.equals("PSTYV") || key.equals("MATNR") || key.equals("MAKTX")
                            || key.equals("") || key.equals("MEINS") || key.equals("MAKTXW") || key.equals("KOSTK")
                            || key.equals("CHARG") || key.equals("WERKS") || key.equals("LGORT") || key.equals("BWART")
                            || key.equals("VGBEL") || key.equals("VGPOS") || key.equals("LFIMG")){
                        returnMap.put(key, value);
                    }
                }
                lineList.add(returnMap);
            }
            List<String> materialList = lineList.stream().map(item -> item.get("MATNR").toString()).collect(Collectors.toList());
            // 根据物料代码列表去物料表中查询是否全部存在
            List<MaterialMainDto> list = esbSendCommonMapper.queryMaterialNum(materialList);
            List<String> newList = list.stream().map(item -> item.getMaterialNo()).collect(Collectors.toList());
            materialList.removeAll(newList);
            if (materialList.size() > 0){
                String join = StringUtils.join(materialList, ",");
                throw new ServiceException(String.format("交货单明细中物料%s不存在，请核对",join));
            }
            Map<String, Object> orderMap = orderList.get(0);
            WmsPurchaseOrderRaw orderRaw = new WmsPurchaseOrderRaw();
            orderRaw.setCompanyCode(orderMap.get("KUNNR").toString());
            orderRaw.setSupplierCode(orderMap.get("VKORG").toString());
            handleOrder(orderRaw);
            PurchaseOrder purchaseOrder = new PurchaseOrder();
            purchaseOrder.setOrderNo(orderNo);
            purchaseOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_NEW);
            purchaseOrder.setCompanyCode(orderMap.get("KUNNR").toString());
            purchaseOrder.setCompanyId(orderRaw.getCompanyId());
            purchaseOrder.setCompanyName(orderRaw.getCompanyName());
            purchaseOrder.setSupplierCode(orderMap.get("VKORG").toString());
            purchaseOrder.setSupplierName(orderRaw.getSupplierName());
            purchaseOrder.setSupplierId(orderRaw.getSupplierId());
            purchaseOrder.setDeliverDate(DateUtils.dateTime("yyyy-mm-dd",orderMap.get("WADAT").toString()));
            purchaseOrder.setOrderType(StockInOrderConstant.ORDER_TYPE_DUMP);
            // 采购订单新增
            PurchaseOrder order = new PurchaseOrder();
            order.setOrderNo(orderNo);
            List<PurchaseOrder> purOrderList = purchaseOrderMapper.selectPurchaseOrderList(order);
            if(purOrderList == null || purOrderList.size() == 0){
                // 新增
                purchaseOrder.setCreateTime(DateUtils.getNowDate());
                purchaseOrder.setCreateBy(SecurityUtils.getUsername());
                purchaseOrderMapper.insertPurchaseOrder(purchaseOrder);
            }else{
                // 编辑
                purchaseOrder.setId(purOrderList.get(0).getId());
                purchaseOrder.setUpdateTime(DateUtils.getNowDate());
                purchaseOrder.setUpdateBy(SecurityUtils.getUsername());
                purchaseOrderMapper.updatePurchaseOrder(purchaseOrder);
                // 获取明细信息
                PurchaseOrderDetail orderDetail = new PurchaseOrderDetail();
                orderDetail.setPurchaseOrderNo(orderNo);
                List<PurchaseOrderDetail> details = purchaseOrderDetailMapper.selectOrderDetail(orderDetail);
                if(details == null || details.size() == 0){
                    throw new ServiceException("获取交货单明细信息失败！");
                }
                // 删除原有明细
                for (PurchaseOrderDetail detail : details) {
                    PurchaseVo purchaseVo = new PurchaseVo();
                    purchaseVo.setDeliveryLineNo(detail.getDeliveryLineNo());
                    purchaseVo.setPurchaseOrderId(detail.getPurchaseOrderId());
                    purchaseOrderDetailMapper.deleteOrderDetailById(purchaseVo);
                }
            }
            // 采购单明细
            for (Map<String, Object> detailMap : lineList){
                PurchaseOrderDetailRawDto detail = new PurchaseOrderDetailRawDto();
                detail.setFactoryCode(orderMap.get("KUNNR").toString());
                detail.setFactoryName(orderRaw.getCompanyName());
                detail.setMaterialNo(detailMap.get("MATNR").toString());
                detail.setCompanyId(orderRaw.getCompanyId());
                handleOrderDetail(detail);

                PurchaseOrderDetail purchaseOrderDetail = new PurchaseOrderDetail();
                purchaseOrderDetail.setFactoryCode(detail.getFactoryCode());
                purchaseOrderDetail.setMaterialNo(detail.getMaterialNo());
                purchaseOrderDetail.setMaterialName(detailMap.get("MAKTX").toString());
                purchaseOrderDetail.setMaterialId(detail.getMaterialId());
                purchaseOrderDetail.setOldMaterialNo(detail.getOldMaterialNo());
                purchaseOrderDetail.setFactoryName(detail.getFactoryName());
                purchaseOrderDetail.setFactoryId(detail.getFactoryId());
                purchaseOrderDetail.setDeliveryDate(DateUtils.dateTime("yyyy-mm-dd",orderMap.get("WADAT").toString()));
                purchaseOrderDetail.setPurchaseOrderId(purchaseOrder.getId());
                purchaseOrderDetail.setPurchaseOrderNo(orderNo);
                purchaseOrderDetail.setPurchaseLineNo(Convert.toStr(detailMap.get("VGPOS")));
                purchaseOrderDetail.setUnit(detailMap.get("MEINS").toString());
                purchaseOrderDetail.setTotalPlanQty(BigDecimal.valueOf(Double.valueOf(detailMap.get("LFIMG").toString())));
                purchaseOrderDetail.setMadeQty(BigDecimal.ZERO);
                purchaseOrderDetail.setPlanQty(BigDecimal.valueOf(Double.valueOf(detailMap.get("LFIMG").toString())));
                purchaseOrderDetail.setStoLocation(detailMap.get("LGORT").toString());
                purchaseOrderDetail.setIsConsigned(PurchaseOrderConstant.NOT_CONSIGNED);
                purchaseOrderDetail.setIsExempted(PurchaseOrderConstant.NOT_EXEMPTED);
                purchaseOrderDetail.setBatchNo(DateUtils.dateTimeNow("yyyyMMddHHmmssSSS"));
                purchaseOrderDetail.setSapReceivedQty(BigDecimal.ZERO);
                purchaseOrderDetail.setWmsReceivedQty(BigDecimal.ZERO);
                purchaseOrderDetail.setTotalQty(BigDecimal.valueOf(Double.valueOf(detailMap.get("LFIMG").toString())));
                purchaseOrderDetail.setCreateTime(DateUtils.getNowDate());
                purchaseOrderDetail.setCreateBy(SecurityUtils.getUsername());
                purchaseOrderDetail.setDelFlag(CommonYesOrNo.NO);
                purchaseOrderDetailMapper.insertWmsPurchaseOrderDetail(purchaseOrderDetail);
            }
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }
}
