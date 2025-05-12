package com.easycode.cloud.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.easycode.cloud.domain.vo.SaleReturnOrderDetailVo;
import com.easycode.cloud.domain.vo.SaleReturnOrderVo;
import com.easycode.cloud.domain.vo.WmsMaterialBasicVo;
import com.easycode.cloud.service.IRetTaskService;
import com.easycode.cloud.service.ISaleReturnOrderDetailService;
import com.easycode.cloud.service.ISaleReturnOrderService;
import com.weifu.cloud.common.core.exception.ServiceException;
import com.weifu.cloud.common.core.text.Convert;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.security.utils.SecurityUtils;
import com.weifu.cloud.constant.*;
import com.easycode.cloud.domain.SaleReturnOrder;
import com.easycode.cloud.domain.SaleReturnOrderDetail;
import com.easycode.cloud.domain.RetTask;
import com.weifu.cloud.domain.dto.*;
import com.weifu.cloud.domain.vo.PrintInfoVo;
import com.weifu.cloud.domian.WmsPrinterLocation;
import com.weifu.cloud.domian.dto.*;
import com.weifu.cloud.domian.vo.StoragePositionVo;
import com.weifu.cloud.enums.RemoteConfigEnum;
import com.easycode.cloud.mapper.SaleReturnOrderDetailMapper;
import com.easycode.cloud.mapper.SaleReturnOrderMapper;
import com.weifu.cloud.mapper.EsbSendCommonMapper;
import com.easycode.cloud.mapper.RetTaskMapper;
import com.weifu.cloud.service.*;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import static com.weifu.cloud.common.core.web.domain.AjaxResult.success;

/**
 * 销售发货退货单Service业务层处理
 *
 * @author fsc
 * @date 2023-03-11
 */
@Service
public class SaleReturnOrderServiceImpl implements ISaleReturnOrderService
{
    @Autowired
    private SaleReturnOrderMapper saleReturnOrderMapper;

    @Autowired
    private ICodeRuleService iCodeRuleService;

    @Autowired
    private SaleReturnOrderDetailMapper saleReturnOrderDetailMapper;

    @Autowired
    private IRetTaskService retTaskService;

    @Autowired
    private ISaleReturnOrderDetailService saleReturnOrderDetailService;

    @Autowired
    private RetTaskMapper retTaskMapper;


    @Autowired
    private IMainDataService iMainDataService;

    @Autowired
    private EsbSendCommonMapper esbSendCommonMapper;

    @Autowired
    private RemoteConfigHelper remoteConfigHelper;

    @Autowired
    private SapInteractionService sapInteractionService;

    @Autowired
    private IPrintService printService;

    /**
     * 查询销售发货退货单
     *
     * @param id 销售发货退货单主键
     * @return 销售发货退货单
     */
    @Override
    public SaleReturnOrder selectSaleReturnOrderById(Long id)
    {
        return saleReturnOrderMapper.selectSaleReturnOrderById(id);
    }

    /**
     * 查询销售发货退货单列表
     *
     * @param saleReturnOrderVo 销售发货退货单
     * @return 销售发货退货单
     */
    @Override
    public List<SaleReturnOrder> selectSaleReturnOrderList(SaleReturnOrderVo saleReturnOrderVo)
    {
        return saleReturnOrderMapper.selectSaleReturnOrderList(saleReturnOrderVo);
    }

    /**
     * 查询销售发货退货单列表
     *
     * @param printInfo 销售发货退货单
     * @return 销售发货退货单
     */
    @Override
    public List<PrintInfoVo> getPrintInfoByIds(PrintInfoVo printInfo)
    {
        Long[] ids = printInfo.getIds();
        String type = com.weifu.cloud.common.core.utils.StringUtils.format("{}", printInfo.getParams().get("type"));
        List<PrintInfoVo> printInfoVos;
        if ("2".equals(type)) {
            printInfoVos = saleReturnOrderMapper.getPrintInfoByDetailIds(ids);
        } else {
            printInfoVos = saleReturnOrderMapper.getPrintInfoByIds(ids);
        }

        // 获取物料号
        List<String> materialNoList = printInfoVos.stream().map(PrintInfoVo::getMaterialNo).collect(Collectors.toList());
        // 根据物料查询对应物料类型
        WmsMaterialBasicDto materialBasicDto = new WmsMaterialBasicDto();
        materialBasicDto.setMaterialNoList(materialNoList);
        AjaxResult ajaxResult = iMainDataService.getMaterialArrInfo(materialBasicDto);
        if (ajaxResult.isError()) {
            throw new ServiceException(ajaxResult.get("msg").toString());
        }
        List<WmsMaterialAttrParamsDto> list = JSONObject.parseArray(ajaxResult.get("data").toString(), WmsMaterialAttrParamsDto.class);
        if (ObjectUtils.isEmpty(list)) {
            throw new ServiceException("物料属性表中未查询到物料相关信息！");
        }

        Map<String, String> materialInfoMap = list.stream().collect(Collectors.toMap(WmsMaterialAttrParamsDto::getMaterialNo, WmsMaterialAttrParamsDto::getType));

        for (PrintInfoVo printInfoVo : printInfoVos){
            String materialNo = printInfoVo.getMaterialNo();
            Integer deliverQty = printInfoVo.getDeliverQty().intValue();
            String materialType = materialInfoMap.get(materialNo);
            printInfoVo.setMaterialType(materialType);
            String qrCode = String.format("O%s%%D%s%%M%s%%Q%s%%B%s%%V%s%%L%s%%X1/1", "", "", materialNo,
                    deliverQty, "", "", "");
            printInfoVo.setQrCode(qrCode);
        }
        return printInfoVos;
    }

    /**
     * 新增销售发货退货单
     *
     * @param saleReturnOrder 销售发货退货单
     * @return 结果
     */
    @Override
    public int insertSaleReturnOrder(SaleReturnOrder saleReturnOrder)
    {
        saleReturnOrder.setCreateTime(DateUtils.getNowDate());
        SapGetHuNoParamsDto paramsDto = new SapGetHuNoParamsDto();
        try {
            String huNo = sapInteractionService.getHu(paramsDto);
            saleReturnOrder.setHuNo(huNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return saleReturnOrderMapper.insertSaleReturnOrder(saleReturnOrder);
    }

    /**
     * 关闭销售发货退货单
     *
     * @param saleReturnOrder 销售发货退货单
     * @return 结果
     */
    @Override
    public int closeSaleOrder(SaleReturnOrder saleReturnOrder)
    {
        saleReturnOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_CLOSE);
        saleReturnOrder.setUpdateBy(SecurityUtils.getUsername());
        saleReturnOrder.setUpdateTime(DateUtils.getNowDate());

        saleReturnOrderMapper.updateSaleReturnOrder(saleReturnOrder);

        SaleReturnOrderDetail centerReturnOrderDetail = new SaleReturnOrderDetail();
        centerReturnOrderDetail.setReturnOrderNo(saleReturnOrder.getOrderNo());
        List<SaleReturnOrderDetail> detailList = saleReturnOrderDetailService.selectSaleReturnOrderDetailList(centerReturnOrderDetail);

        List<Long> detailIdList = detailList.stream().map(SaleReturnOrderDetail::getId).collect(Collectors.toList());
        // 关闭退货任务
        retTaskMapper.updateStatusByDetailList(detailIdList);
        return 1;
    }

    /**
     * 新增销售发货退货单及明细
     *
     * @param saleReturnOrderVo 销售发货退货单
     * @return 结果
     */
    @Override
    @GlobalTransactional
    public SaleReturnOrder addSaleReturn(SaleReturnOrderVo saleReturnOrderVo)
    {
        // 明细数据
        List<SaleReturnOrderDetail> detailList = saleReturnOrderVo.getDetailList();

        SaleReturnOrder saleReturnOrder = new SaleReturnOrder();
        BeanUtils.copyProperties(saleReturnOrderVo, saleReturnOrder);
        Long tenantId = SecurityUtils.getLoginUser().getSysUser().getTenantId();

        // 生成退货单号
        AjaxResult ajaxResult = iCodeRuleService.getSeqWithTenantId(OrderNoTypeConstant.COST_CENTER_ORDER_RETURN, String.valueOf(tenantId));
        if (ajaxResult.isError() || StringUtils.isEmpty(ajaxResult.get("data").toString())){
            throw new ServiceException("销售发货退货单号生成失败！");
        }
        String orderNo = ajaxResult.get("data").toString();
        saleReturnOrder.setOrderNo(orderNo);
        // 工厂code
        saleReturnOrder.setFactoryCode("05C1");
        List<FactoryCommonDto> factoryInfo = esbSendCommonMapper.getFactoryByCode("05C1");
        if (factoryInfo.size() <= 0){
            throw new ServiceException(String.format("根据工厂code:%s未查询到相关信息", SecurityUtils.getComCode()));
        }
        saleReturnOrder.setFactoryName(factoryInfo.get(0).getFactoryName());
        saleReturnOrder.setCreateTime(DateUtils.getNowDate());
        saleReturnOrder.setCreateBy(SecurityUtils.getUsername());
        saleReturnOrder.setTenantId(null);
        saleReturnOrder.setDeptId(SecurityUtils.getLoginUser().getSysUser().getDeptId());
        //根据参数配置 决定是否走自动激活单据
        RemoteConfigEnum createStockInTask = RemoteConfigEnum.CREATE_STOCK_IN_TASK;
        String isCreateStockInTask = remoteConfigHelper.getConfig(createStockInTask.getKey());
        if("".equals(Objects.toString(isCreateStockInTask,""))){
            throw new ServiceException("获取参数配置失败！");
        }
        boolean flag = createStockInTask.getValue().equals(isCreateStockInTask);
        saleReturnOrder.setOrderStatus(flag ? OrderStatusConstant.ORDER_STATUS_ACTIVE : OrderStatusConstant.ORDER_STATUS_NEW);
        // 存入主表数据
        if (saleReturnOrderMapper.insertSaleReturnOrder(saleReturnOrder) <= 0) {
            throw new ServiceException("新增销售发货退货单失败！");
        }
        // 明细表数据
        int lineNo = 1;
        for (SaleReturnOrderDetail detail : detailList){
            //校验退货批次
            String lotNo;
            if ("".equals(Objects.toString(detail.getLot(), ""))){

                AjaxResult lotNoResult = iCodeRuleService.getSeqWithTenantId(StockInTaskConstant.RETURN_LOT_NO, String.valueOf(tenantId));
                if ("".equals(Objects.toString(lotNoResult.get("data"), "")) || lotNoResult.isError()) {
                    throw new ServiceException("退货批次号生成失败！");
                }
                lotNo = lotNoResult.get("data").toString();

                if (StockInTaskConstant.MOVE_TYPE_RESEARCH.equals(saleReturnOrderVo.getMoveType())){
                    lotNo = lotNo.substring(0, lotNo.length() - 1);
                }
            } else {
                if (detail.getLot().length() != 11){
                    throw new ServiceException("退货批次需输入11位！");
                }
                lotNo = detail.getLot();
                //TODO 退货批次 后续需要可配置
                // 移动类型为研发的话，不带T
                if (!StockInTaskConstant.MOVE_TYPE_RESEARCH.equals(saleReturnOrderVo.getMoveType())){
                    lotNo = lotNo + StockInTaskConstant.RETURN_LOT_NO_SUFFIX;
                }
            }
            detail.setLineNo(String.valueOf(lineNo++));
            detail.setLot(lotNo);
            detail.setReturnOrderNo(orderNo);
            detail.setFactoryCode(SecurityUtils.getComCode());
            detail.setCreateTime(DateUtils.getNowDate());
            detail.setCreateBy(SecurityUtils.getUsername());
            if (saleReturnOrderDetailMapper.insertSaleReturnOrderDetail(detail) <= 0) {
                throw new ServiceException("新增销售发货退货单失败！");
            }
            if (flag){
                addRetTask(detail,StockInTaskConstant.COST_CENTER_RETURN_TYPE);
            }
        }
        return saleReturnOrder;
    }

    @Override
    public String sapSalesReturn(List<Map<String, Object>> headList, List<Map<String, Object>> itemList) {
        String status = "I";
        Long tenantId = 6L;
        // head只有一个
        Map<String, Object> headInfo = headList.get(0);

        // 生成退货单号
        AjaxResult ajaxResult = iCodeRuleService.getSeqWithTenantId(OrderNoTypeConstant.FIN_ORDER_RETURN, String.valueOf(tenantId));
        if (ajaxResult.isError() || StringUtils.isEmpty(ajaxResult.get("data").toString())){
            throw new ServiceException("销售发货退货单号生成失败！");
        }
        String orderNo = ajaxResult.get("data").toString();

        // 交货编号
        String vbeln = Convert.toStr(headInfo.get("VBELN"));
        // 销售组织 工厂? 供应商?
        String vkorg = Convert.toStr(headInfo.get("VKORG"));
        // 创建对象的人员名称
        String ernam = Convert.toStr(headInfo.get("ERNAM"));
        // 记录创建日期
        String erdat = Convert.toStr(headInfo.get("ERDAT"));
        // 交货类型
        String lfart = Convert.toStr(headInfo.get("LFART"));
        //客户编号
        String kunnr = Convert.toStr(headInfo.get("KUNNR"));
        // 名称
        String name1 = Convert.toStr(headInfo.get("NAME1"));
        // 装运类型
        String vsart = Convert.toStr(headInfo.get("VSART"));
        //计划货物移动日期
        String wadat = Convert.toStr(headInfo.get("WADAT"));
        //交货日期
        String lfdat = Convert.toStr(headInfo.get("LFDAT"));
        SaleReturnOrder saleReturnOrder = new SaleReturnOrder();

        saleReturnOrder.setErdat(erdat);
        saleReturnOrder.setLfart(lfart);
        saleReturnOrder.setName1(name1);
        saleReturnOrder.setVsart(vsart);
        saleReturnOrder.setWadat(wadat);
        saleReturnOrder.setLfdat(lfdat);
        saleReturnOrder.setCustomerCode(kunnr);
        saleReturnOrder.setOrderNo(orderNo);
        saleReturnOrder.setSaleCode(vbeln);
        saleReturnOrder.setFactoryCode(vkorg);
        saleReturnOrder.setFactoryName(ernam);
        saleReturnOrder.setCreateTime(DateUtils.getNowDate());
        saleReturnOrder.setCreateBy("SAP");
        saleReturnOrder.setTenantId(tenantId);
        saleReturnOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_ACTIVE);

        SaleReturnOrderVo orderVo = new SaleReturnOrderVo();
        orderVo.setSaleCode(vbeln);
        List<SaleReturnOrder> saleReturnOrderList = saleReturnOrderMapper.selectSaleReturnOrderList(orderVo);
        if(CollectionUtils.isNotEmpty(saleReturnOrderList)){
            saleReturnOrderMapper.deleteSaleReturnOrderById(saleReturnOrderList.get(0).getId());
            saleReturnOrderDetailMapper.deleteByReturnOrderNo(saleReturnOrderList.get(0).getOrderNo());
            retTaskMapper.deleteByStockinOrderNo(saleReturnOrderList.get(0).getOrderNo());
        }


        // 存入主表数据
        if (saleReturnOrderMapper.insertSaleReturnOrder(saleReturnOrder) <= 0) {
            throw new ServiceException("新增销售发货退货单失败！");
        }
        StoragePositionVo storagePositionVo = new StoragePositionVo();
        for (Map<String, Object> itemMap : itemList) {
            // 交货项目
            String posnr = Convert.toStr(itemMap.get("POSNR"));
            // 销售凭证项目类别
            String pstyv = Convert.toStr(itemMap.get("PSTYV"));
            // 物料编号
            String matnr= Convert.toStr(itemMap.get("MATNR"));
            // 物料描述
            String maktx = Convert.toStr(itemMap.get("MAKTX"));
            // 交货数量
            String lfimg = Convert.toStr(itemMap.get("LFIMG"));
            // 基本计量单位
            String meins = Convert.toStr(itemMap.get("MEINS"));
            // 拣配状态
            String kostk = Convert.toStr(itemMap.get("KOSTK"));
            // 批次编号
            String charg = Convert.toStr(itemMap.get("CHARG"));
            // 工厂
            String werks = Convert.toStr(itemMap.get("WERKS"));
            // 存储地点
            String lgort = Convert.toStr(itemMap.get("LGORT"));
            // 移动类型
            String bwart = Convert.toStr(itemMap.get("BWART"));
            // 参考单据的单据编号
            String vgbel = Convert.toStr(itemMap.get("VGBEL"));
            // 参考项目的项目号
            String vgpos = Convert.toStr(itemMap.get("VGPOS"));
            // 库存类型
            String insmk = Convert.toStr(itemMap.get("INSMK"));

            ajaxResult = iMainDataService.getPositionCodeByLocationCodeAndMaterialNo(lgort,matnr);
            if(ajaxResult.isError()){
                throw new ServiceException(Objects.toString(ajaxResult.get("msg"),"获取仓位失败"));
            }
            storagePositionVo = JSON.parseObject(JSON.toJSONString(ajaxResult.get("data")), StoragePositionVo.class);
            if("1".equals(storagePositionVo.getMixedFlag())){
                Long[] ids = {storagePositionVo.getId()};
                iMainDataService.updateStoragePositionStatus(ids,"1");
            }
            AjaxResult materialBasicInfo = iMainDataService.getMaterialBasicInfo(matnr);
            if(materialBasicInfo.isError()) {
                throw new ServiceException(materialBasicInfo.get("msg").toString());
            }
            WmsMaterialBasicVo materialInfo = com.alibaba.fastjson2.JSONObject.parseObject(materialBasicInfo.get("data").toString(), WmsMaterialBasicVo.class);
            if(ObjectUtils.isEmpty(materialInfo)){
                throw new ServiceException("不存在该物料号"+matnr);
            }
            SaleReturnOrderDetail detail = new SaleReturnOrderDetail();
            detail.setPstyv(pstyv);
            detail.setKostk(kostk);
            detail.setVgbel(vgbel);
            detail.setVgpos(vgpos);
            detail.setInsmk(insmk);
            detail.setMaterialNo(matnr);
            detail.setMaterialName(maktx);
            detail.setReturnOrderNo(orderNo);
            detail.setReturnQty(new BigDecimal(BigInteger.ONE));
            detail.setOperationQty(new BigDecimal(BigInteger.ONE));
            if(StringUtils.isNotEmpty(lfimg)){
                detail.setOperationQty(new BigDecimal(lfimg.trim()));
                detail.setReturnQty(new BigDecimal(lfimg.trim()));
            }

            detail.setLocationCode(lgort);
            detail.setOperationUnit(meins);
            detail.setUnit(meins);
            detail.setLineNo(posnr);
            detail.setLot(charg);
            detail.setTenantId(tenantId);
            detail.setFactoryCode(werks);
            detail.setCreateTime(DateUtils.getNowDate());
            detail.setCreateBy("SAP");
            detail.setMoveType(bwart);
            detail.setOldMaterialNo(materialInfo.getOldMaterialNo());
            saleReturnOrderDetailMapper.insertSaleReturnOrderDetail(detail);
            // 生成退货任务表
            AjaxResult result = iCodeRuleService.getSeqWithTenantId(TaskNoTypeConstant.FIN_RETURN_TASK, String.valueOf(detail.getTenantId()));
            if("".equals(Objects.toString(result.get("data"),"")) || result.isError()){
                throw new ServiceException("销售发货退料任务号生成失败！");
            }
            String taskNo = result.get("data").toString();
            // 生成退货任务
            RetTask retTask = new RetTask();
            retTask.setDetailId(detail.getId());
            retTask.setTaskNo(taskNo);
            retTask.setMaterialNo(detail.getMaterialNo());
            retTask.setMaterialName(detail.getMaterialName());
            retTask.setOldMatrialName(detail.getOldMaterialNo());
            retTask.setStockinOrderNo(orderNo);
            retTask.setLot(detail.getLot());
            retTask.setUnit(detail.getUnit());
            retTask.setQty(detail.getReturnQty());
            retTask.setCompleteQty(BigDecimal.ZERO);
            retTask.setStorageLocationCode(detail.getLocationCode());
            retTask.setOperationUnit(detail.getOperationUnit());
            retTask.setOperationCompleteQty(BigDecimal.ZERO);
            retTask.setOperationQty(detail.getOperationQty());
            retTask.setTaskStatus(TaskStatusConstant.TASK_STATUS_NEW);
            retTask.setTaskType(StockInTaskConstant.SALE_RETURN_TYPE);
            retTask.setCreateBy("SAP");
            retTask.setCreateTime(DateUtils.getNowDate());
            retTask.setMoveType(detail.getMoveType());
            retTask.setTenantId(detail.getTenantId());
            retTask.setPositionNo(storagePositionVo.getPositionNo());
            retTask.setPositionId(storagePositionVo.getId());
            retTask.setPrintStatus("1");
            retTask.setPrintSum(1);
            retTaskService.insertRetTask(retTask);
            // 生成任务自动打印
            PrintTOParamsDto printTOParamsDto = new PrintTOParamsDto();
            printTOParamsDto.setFactoryCode(werks);
            printTOParamsDto.setLocationCode(lgort);
            printTOParamsDto.setMaterialName(maktx);
            printTOParamsDto.setMaterialNo(matnr);
            printTOParamsDto.setTaskNo(taskNo);
            printTOParamsDto.setPositionNo(storagePositionVo.getPositionNo());
            printTOParamsDto.setLot(charg);
            printTOParamsDto.setMoveQty(retTask.getQty());
            printTOParamsDto.setPackQty(retTask.getQty());
            printTOParamsDto.setUserName(ernam);
            printTOParamsDto.setSourcePostionNo(storagePositionVo.getPositionNo());
            printTOParamsDto.setSourceStorageType(storagePositionVo.getRemark());
            printTOParamsDto.setOldMaterialNo(retTask.getOldMatrialName());
            printTOParamsDto.setTaskType("成品退货");
            printService.printTO(printTOParamsDto);
        }

        return status;
    }

    public int printInspectTask(PrintTOParamsDto printTOParamsDto) {
        AjaxResult printerByLocationAjaxResult = printService.findPrinterByLocation(printTOParamsDto.getLocationCode());

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
        jsonArray.add(jsonObject);
        dto.setDataArray(jsonArray);

        AjaxResult ajaxResult1 = printService.printByTemplate(dto);

        if (ajaxResult1.isError()) {
            throw new ServiceException(String.format("打印失败,原因：%s", ajaxResult1.get("msg").toString()));
        }
        return HttpStatus.SC_OK;
    }

    /**
     * 新增退货入库任务
     * @param detail 单据明细
     */
    @Override
    public int addRetTask(SaleReturnOrderDetail detail, String taskType) {
        String orderNo = detail.getReturnOrderNo();

        // 生成退货任务表
        AjaxResult result = iCodeRuleService.getSeqWithTenantId(TaskNoTypeConstant.COST_CENTER_ORDER_RETURN, String.valueOf(detail.getTenantId()));
        if("".equals(Objects.toString(result.get("data"),"")) || result.isError()){
            throw new ServiceException("销售发货退料任务号生成失败！");
        }
        String taskNo = result.get("data").toString();
        // 生成退货任务
        RetTask retTask = new RetTask();
        retTask.setDetailId(detail.getId());
        retTask.setTaskNo(taskNo);
        retTask.setMaterialNo(detail.getMaterialNo());
        retTask.setMaterialName(detail.getMaterialName());
        retTask.setOldMatrialName(detail.getOldMaterialNo());
        retTask.setStockinOrderNo(orderNo);
        retTask.setLot(detail.getLot());
        retTask.setUnit(detail.getUnit());
        retTask.setQty(detail.getReturnQty());
        retTask.setCompleteQty(BigDecimal.ZERO);
        retTask.setStorageLocationCode(detail.getLocationCode());
        retTask.setOperationUnit(detail.getOperationUnit());
        retTask.setOperationCompleteQty(BigDecimal.ZERO);
        retTask.setOperationQty(detail.getOperationQty());
        retTask.setTaskStatus(TaskStatusConstant.TASK_STATUS_NEW);
        retTask.setTaskType(taskType);
        retTask.setCreateBy("SAP");
        retTask.setCreateTime(DateUtils.getNowDate());
        retTask.setMoveType(detail.getMoveType());
        retTask.setTenantId(detail.getTenantId());
        retTaskService.insertRetTask(retTask);
        return 1;
    }

    /**
     * 修改销售发货退货单
     *
     * @param saleReturnOrder 销售发货退货单
     * @return 结果
     */
    @Override
    public int updateSaleReturnOrder(SaleReturnOrder saleReturnOrder)
    {
        saleReturnOrder.setUpdateTime(DateUtils.getNowDate());
        return saleReturnOrderMapper.updateSaleReturnOrder(saleReturnOrder);
    }

    /**
     * 批量删除销售发货退货单
     *
     * @param ids 需要删除的销售发货退货单主键
     * @return 结果
     */
    @Override
    public int deleteSaleReturnOrderByIds(Long[] ids)
    {
        return saleReturnOrderMapper.deleteSaleReturnOrderByIds(ids);
    }

    /**
     * 删除销售发货退货单信息
     *
     * @param id 销售发货退货单主键
     * @return 结果
     */
    @Override
    public int deleteSaleReturnOrderById(Long id)
    {
        return saleReturnOrderMapper.deleteSaleReturnOrderById(id);
    }

    /**
     * 批量激活销售发货退货单
     *
     * @param ids 销售发货退货单主键集合
     * @return 结果
     */
    @Override
    public int activeSaleOrderReturnByIds(Long[] ids) {
        //校验参数
        if (ObjectUtils.isEmpty(ids)) {
            throw new ServiceException("参数不存在！");
        }
        for (Long id : ids) {
            //查询单据
            SaleReturnOrder saleReturnOrder = saleReturnOrderMapper.selectSaleReturnOrderById(id);
            String status = saleReturnOrder.getOrderStatus();
            String orderNo = saleReturnOrder.getOrderNo();
            if (!OrderStatusConstant.ORDER_STATUS_NEW.equals(status)) {
                throw new ServiceException("销售发货退货单:"+ orderNo+"必须为新建状态才能激活！");
            }
            //更新状态
            saleReturnOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_ACTIVE);
            saleReturnOrder.setUpdateBy(SecurityUtils.getUsername());
            saleReturnOrder.setUpdateTime(DateUtils.getNowDate());
            if (saleReturnOrderMapper.updateSaleReturnOrder(saleReturnOrder) <= 0) {
                throw new ServiceException("更新销售发货退货单失败！");
            }
            //查询明细
            SaleReturnOrderDetail centerReturnOrderDetail = new SaleReturnOrderDetail();
            centerReturnOrderDetail.setReturnOrderNo(orderNo);
            List<SaleReturnOrderDetail> detailList = saleReturnOrderDetailMapper.selectSaleReturnOrderDetailList(centerReturnOrderDetail);
            //新增退货入库任务
            for (SaleReturnOrderDetail detail : detailList) {
                addRetTask(detail,StockInTaskConstant.COST_CENTER_RETURN_TYPE);
            }
        }
        return 1;
    }

    @Override
    public AjaxResult importData(List<SaleReturnOrderDetailVo> stockList) {
        StringBuffer errorMsg = new StringBuffer();
        List<SaleReturnOrderDetailVo> resultList = new ArrayList<>();
        List<String> materialList = stockList.stream().map(SaleReturnOrderDetailVo::getMaterialNo).distinct().collect(Collectors.toList());

        MaterialUnitDefDto materialUnitDefDto = new MaterialUnitDefDto();
        materialUnitDefDto.setMaterialNoList(materialList);
        materialUnitDefDto.setStockinEnable(CommonYesOrNo.YES);
        materialUnitDefDto.setFactoryCode(SecurityUtils.getComCode());
        AjaxResult unitResult = iMainDataService.batchMaterialUnitDef(materialUnitDefDto);
        if (unitResult.isError()) {
            throw new ServiceException(unitResult.get("msg").toString());
        }
        List<MaterialUnitDefDto> unitList = JSONObject.parseArray(unitResult.get("data").toString(), MaterialUnitDefDto.class);
        if (ObjectUtils.isEmpty(unitList)) {
            throw new ServiceException(String.format("当前导入全部物料多级单位未维护！"));
        }
        Map<String, BigDecimal> map = unitList.stream().collect(Collectors.toMap(MaterialUnitDefDto::getMaterialNo, MaterialUnitDefDto::getConversDefault));
        Map<String, String> unitMap = unitList.stream().collect(Collectors.toMap(MaterialUnitDefDto::getMaterialNo, MaterialUnitDefDto::getUnit));



//        if(materialList.size() != stockList.size()){
//            throw new ServiceException("导入物料重复,导入失败!");
//        }
        List<SaleReturnOrderDetailVo> list = stockList.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getMaterialNo() + "-" + o.getLot()))),
                        ArrayList::new
                ));
        if(list.size() != stockList.size()){
            throw new ServiceException("导入物料批次重复,导入失败!");
        }
        if(stockList != null && stockList.size() > 0){
            for (SaleReturnOrderDetailVo detail : stockList) {
                if (detail.getMaterialNo() == null || "".equals(detail.getMaterialNo()) || detail.getOperationQty() == null) {
                    throw new ServiceException("物料号，数量不能为空！");
                }
                // 调用主数据模块
                AjaxResult result = iMainDataService.getMatrialNoCount(detail.getMaterialNo());
                // 获取物料描述,旧物料号信息
                MaterialAttrDto materialAttrDto = new MaterialAttrDto();
                materialAttrDto.setMaterialNo(detail.getMaterialNo());
                List<MaterialMainDto> materialMainList = esbSendCommonMapper.getMaterialMain(materialAttrDto);
                if(materialMainList != null && materialMainList.size() > 0){
                    detail.setMaterialName(materialMainList.get(0).getMaterialName());
                    detail.setOldMaterialNo(materialMainList.get(0).getOldMaterialNo());
                    detail.setUnit(materialMainList.get(0).getDefaultUnit());
                    detail.setFactoryCode(materialMainList.get(0).getFactoryCode());
                    detail.setType(materialMainList.get(0).getType());
                }
                if (result.isError()) {
                    throw new ServiceException("调用主数据模块失败！");
                }

                BigDecimal conversDefault = map.get(detail.getMaterialNo());
                String operationUnit = unitMap.get(detail.getMaterialNo());

                detail.setOperationUnit(operationUnit);
//                detail.setConverterDefault(conversDefault);
                BigDecimal requestQty = detail.getOperationQty().multiply(conversDefault);
                detail.setReturnQty(requestQty);

                int num =  (int) result.get("data");
                if (num == 0) {
                    errorMsg.append(detail.getMaterialNo() + "，");
                }else {
                    resultList.add(detail);
                }
            }
        }else{
            throw new ServiceException("导入数据为空!");
        }
        if(!"".equals(errorMsg.toString())){
            errorMsg.append("物料主数据未维护！");
        }
        return success(errorMsg.toString(),resultList);
    }
}
