package com.easycode.cloud.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.easycode.cloud.domain.dto.RetTaskDto;
import com.easycode.cloud.domain.vo.CostCenterReturnOrderDetailVo;
import com.easycode.cloud.domain.vo.CostCenterReturnOrderVo;
import com.easycode.cloud.domain.vo.RetTaskVo;
import com.easycode.cloud.service.ICostCenterReturnOrderDetailService;
import com.easycode.cloud.service.ICostCenterReturnOrderService;
import com.easycode.cloud.service.IRetTaskService;
import com.soa.eis.adapter.framework.message.IMsgObject;
import com.soa.eis.adapter.framework.message.impl.GroupRecord;
import com.soa.eis.adapter.framework.message.impl.MsgObject;
import com.weifu.cloud.common.core.exception.ServiceException;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.datascope.annotation.DataScope;
import com.weifu.cloud.common.security.utils.SecurityUtils;
import com.weifu.cloud.constant.*;
import com.easycode.cloud.domain.CostCenterReturnOrder;
import com.easycode.cloud.domain.CostCenterReturnOrderDetail;
import com.easycode.cloud.domain.RetTask;
import com.weifu.cloud.domain.dto.*;
import com.weifu.cloud.domain.vo.PrintInfoVo;
import com.weifu.cloud.domian.*;
import com.weifu.cloud.domian.dto.*;
import com.weifu.cloud.domian.vo.StoragePositionVo;
import com.weifu.cloud.enums.RemoteConfigEnum;
import com.easycode.cloud.mapper.CostCenterReturnOrderDetailMapper;
import com.easycode.cloud.mapper.CostCenterReturnOrderMapper;
import com.weifu.cloud.mapper.EsbSendCommonMapper;
import com.easycode.cloud.mapper.RetTaskMapper;
import com.weifu.cloud.service.*;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.weifu.cloud.common.core.web.domain.AjaxResult.success;

/**
 * 成本中心退货单Service业务层处理
 *
 * @author fsc
 * @date 2023-03-11
 */
@Service
public class CostCenterReturnOrderServiceImpl implements ICostCenterReturnOrderService
{
    @Autowired
    private CostCenterReturnOrderMapper costCenterReturnOrderMapper;

    @Autowired
    private CostCenterReturnOrderDetailMapper costCenterReturnOrderDetailMapper;

    @Autowired
    private IRetTaskService retTaskService;

    @Autowired
    private ICostCenterReturnOrderDetailService costCenterReturnOrderDetailService;

    @Autowired
    private RetTaskMapper retTaskMapper;


    @Autowired
    private IInStoreService inStoreService;

    @Autowired
    private IPrintService printService;

    /**
     * 查询成本中心退货单
     *
     * @param id 成本中心退货单主键
     * @return 成本中心退货单
     */
    @Override
    public CostCenterReturnOrder selectCostCenterReturnOrderById(Long id)
    {
        return costCenterReturnOrderMapper.selectCostCenterReturnOrderById(id);
    }

    /**
     * 查询成本中心退货单列表
     *
     * @param costCenterReturnOrderVo 成本中心退货单
     * @return 成本中心退货单
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<CostCenterReturnOrder> selectCostCenterReturnOrderList(CostCenterReturnOrderVo costCenterReturnOrderVo)
    {
        List<CostCenterReturnOrder> centerReturnOrders = costCenterReturnOrderMapper.selectCostCenterReturnOrderList(costCenterReturnOrderVo);
        centerReturnOrders.forEach(e->{
            String moveType = e.getMoveType().substring(1);
            Integer result = Integer.valueOf(moveType)+1;
            moveType = String.format("%02d", result);
            e.setMoveType(e.getMoveType().substring(0, moveType.length() - 1) + moveType);
        });
        return centerReturnOrders;
    }

    /**
     * 查询成本中心退货单列表
     *
     * @param printInfo 成本中心退货单
     * @return 成本中心退货单
     */
    @Override
    public List<PrintInfoVo> getPrintInfoByIds(PrintInfoVo printInfo)
    {
        Long[] ids = printInfo.getIds();
        String type = com.weifu.cloud.common.core.utils.StringUtils.format("{}", printInfo.getParams().get("type"));
        List<PrintInfoVo> printInfoVos;
        if ("2".equals(type)) {
            printInfoVos = costCenterReturnOrderMapper.getPrintInfoByDetailIds(ids);
        } else {
            printInfoVos = costCenterReturnOrderMapper.getPrintInfoByIds(ids);
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
     * 新增成本中心退货单
     *
     * @param costCenterReturnOrder 成本中心退货单
     * @return 结果
     */
    @Override
    public int insertCostCenterReturnOrder(CostCenterReturnOrder costCenterReturnOrder)
    {
        costCenterReturnOrder.setCreateTime(DateUtils.getNowDate());
        SapGetHuNoParamsDto paramsDto = new SapGetHuNoParamsDto();
        try {
            String huNo = sapInteractionService.getHu(paramsDto);
            costCenterReturnOrder.setHuNo(huNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return costCenterReturnOrderMapper.insertCostCenterReturnOrder(costCenterReturnOrder);
    }

    /**
     * 关闭成本中心退货单
     *
     * @param costCenterReturnOrder 成本中心退货单
     * @return 结果
     */
    @Override
    public int closeCostCenterOrder(CostCenterReturnOrder costCenterReturnOrder)
    {
        RetTask retTask = new RetTask();
        retTask.setStockinOrderNo(costCenterReturnOrder.getOrderNo());
        List<RetTask> retTasks = retTaskMapper.selectRetTaskList(retTask);
        List<Long> detailIdList = retTasks.stream().map(RetTask::getDetailId).collect(Collectors.toList());
        retTasks = retTasks.stream().filter(e->TaskStatusConstant.TASK_STATUS_COMPLETE.equals(e.getTaskStatus())).collect(Collectors.toList());
        if(retTasks!=null&&retTasks.size()>0){
            throw new ServiceException("该单据下有已完成任务，不能关闭");
        }
        costCenterReturnOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_CLOSE);
        costCenterReturnOrder.setUpdateBy(SecurityUtils.getUsername());
        costCenterReturnOrder.setCreateTime(DateUtils.getNowDate());
        costCenterReturnOrderMapper.updateCostCenterReturnOrder(costCenterReturnOrder);
        // 关闭退货任务
        retTaskMapper.updateStatusByDetailList(detailIdList);
        return 1;
    }

    /**
     * 新增成本中心退货单及明细
     *
     * @param costCenterReturnOrderVo 成本中心退货单
     * @return 结果
     */
    @Override
    public CostCenterReturnOrder addCostCenterReturn(CostCenterReturnOrderVo costCenterReturnOrderVo)
    {
        // 明细数据
        List<CostCenterReturnOrderDetail> detailList = costCenterReturnOrderVo.getDetailList();

        List<String> stockInTaskNos = detailList.stream().map(m -> m.getStockInTaskNo()).collect(Collectors.toList());
        List<RetTask> retTasks = retTaskMapper.haveCenterReturnTask(stockInTaskNos);
        if(retTasks!=null&&retTasks.size()>0){
            throw new ServiceException("该领料记录已存在对应的未完成或已完成任务！");
        }
        CostCenterReturnOrder costCenterReturnOrder = new CostCenterReturnOrder();
        BeanUtils.copyProperties(costCenterReturnOrderVo, costCenterReturnOrder);
        Long tenantId = SecurityUtils.getLoginUser().getSysUser().getTenantId();

        // 生成退货单号
        AjaxResult ajaxResult = iCodeRuleService.getSeqWithTenantId(OrderNoTypeConstant.COST_CENTER_ORDER_RETURN, String.valueOf(tenantId));
        if (ajaxResult.isError() || StringUtils.isEmpty(ajaxResult.get("data").toString())){
            throw new ServiceException("成本中心退货单号生成失败！");
        }
        String orderNo = ajaxResult.get("data").toString();
        costCenterReturnOrder.setOrderNo(orderNo);
        costCenterReturnOrder.setCreateTime(DateUtils.getNowDate());
        costCenterReturnOrder.setCreateBy(SecurityUtils.getUsername());
        costCenterReturnOrder.setTenantId(SecurityUtils.getLoginUser().getSysUser().getTenantId());
        costCenterReturnOrder.setDeptId(SecurityUtils.getLoginUser().getSysUser().getDeptId());
        costCenterReturnOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_ACTIVE);
        // 存入主表数据
        if (costCenterReturnOrderMapper.insertCostCenterReturnOrder(costCenterReturnOrder) <= 0) {
            throw new ServiceException("新增成本中心退货单失败！");
        }
        // 明细表数据
        int lineNo = 1;
        List<Long> ids = new ArrayList<>();
        for (CostCenterReturnOrderDetail detail : detailList){
            ajaxResult = iMainDataService.getStoragePositionByNo(detail.getPositionNo());
            if(ajaxResult.isError()){
                throw new ServiceException("查询仓位信息失败！");
            }
            List<StoragePositionVo> storagePositionVoList =  JSONObject.parseArray(com.alibaba.fastjson.JSON.toJSONString(ajaxResult.get("data")), StoragePositionVo.class);
            if(CollectionUtils.isEmpty(storagePositionVoList)){
                throw new ServiceException("仓位信息为空！");
            }
            detail.setPositionId(storagePositionVoList.get(0).getId());
            detail.setLineNo(String.valueOf(lineNo++));
            detail.setReturnOrderNo(orderNo);
            detail.setFactoryCode(SecurityUtils.getComCode());
            detail.setCreateTime(DateUtils.getNowDate());
            detail.setCreateBy(SecurityUtils.getUsername());
            if (costCenterReturnOrderDetailMapper.insertCostCenterReturnOrderDetail(detail) <= 0) {
                throw new ServiceException("新增成本中心退货单失败！");
            }
            addRetTask(orderNo, detail,costCenterReturnOrder,storagePositionVoList.get(0));
            ids.add(storagePositionVoList.get(0).getId());
        }
        iMainDataService.updateStoragePositionStatus(ids.toArray(new Long[0]),"1");
        return costCenterReturnOrder;
    }

    /**
     * 新增退货入库任务
     * @param orderNo 单据号
     * @param detail 单据明细
     */
    public void addRetTask(String orderNo, CostCenterReturnOrderDetail detail,CostCenterReturnOrder costCenterReturnOrder,StoragePositionVo storagePositionVo) {
        Long tenantId = SecurityUtils.getLoginUser().getSysUser().getTenantId();

        // 生成退货任务表
        AjaxResult result = iCodeRuleService.getSeqWithTenantId(TaskNoTypeConstant.COST_CENTER_ORDER_RETURN, String.valueOf(tenantId));
        if("".equals(Objects.toString(result.get("data"),"")) || result.isError()){
            throw new ServiceException("成本中心退料任务号生成失败！");
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
        retTask.setPositionId(detail.getPositionId());
        retTask.setUnit(detail.getUnit());
        retTask.setQty(detail.getOperationQty());
        retTask.setCompleteQty(BigDecimal.ZERO);
        retTask.setOperationUnit(detail.getUnit());
        retTask.setOperationCompleteQty(BigDecimal.ZERO);
        retTask.setOperationQty(detail.getOperationQty());
        retTask.setStorageLocationCode(detail.getLocationCode());
        retTask.setPositionNo(detail.getPositionNo());
        retTask.setTaskStatus(TaskStatusConstant.TASK_STATUS_NEW);
        retTask.setTaskType(StockInTaskConstant.COST_CENTER_RETURN_TYPE);
        retTask.setCreateBy(SecurityUtils.getUsername());
        retTask.setCreateTime(DateUtils.getNowDate());
        retTask.setStockInTaskNo(detail.getStockInTaskNo());
        retTask.setPrintSum(1);
        retTask.setPrintStatus("1");
        retTask.setMoveType(costCenterReturnOrder.getMoveType());
        retTaskService.insertRetTask(retTask);
        printList(retTask,detail,storagePositionVo);
    }

    /**
     * 打印
     ** @param retTask
     */
    public void printList(RetTask retTask,CostCenterReturnOrderDetail costCenterReturnOrderDetail,StoragePositionVo storagePositionVo) {
        String moveType = retTask.getMoveType().substring(1);
        Integer result = Integer.valueOf(moveType)+1;
        moveType = String.format("%02d", result);
        moveType = retTask.getMoveType().substring(0, moveType.length() - 1) + moveType;
        PrintTOParamsDto printTOParamsDto = new PrintTOParamsDto();
        printTOParamsDto.setLot(retTask.getLot());
        printTOParamsDto.setFactoryCode(costCenterReturnOrderDetail.getFactoryCode());
        printTOParamsDto.setMoveQty(retTask.getQty());
        printTOParamsDto.setPositionNo(retTask.getPositionNo());
        printTOParamsDto.setPackQty(retTask.getQty());
        printTOParamsDto.setLocationCode(retTask.getStorageLocationCode());
        printTOParamsDto.setMaterialName(retTask.getMaterialName());
        printTOParamsDto.setMaterialNo(retTask.getMaterialNo());
        printTOParamsDto.setTaskType("成本中心退货任务");
        printTOParamsDto.setSourcePostionNo(retTask.getPositionNo());
        printTOParamsDto.setTaskNo(retTask.getTaskNo());
        printTOParamsDto.setMoveType(moveType);
        printTOParamsDto.setSourceStorageType(storagePositionVo.getRemark());
        printTOParamsDto.setOldMaterialNo(retTask.getOldMatrialName());
        printService.printTO(printTOParamsDto);
    }

    /**
     * 修改成本中心退货单
     *
     * @param costCenterReturnOrder 成本中心退货单
     * @return 结果
     */
    @Override
    public int updateCostCenterReturnOrder(CostCenterReturnOrder costCenterReturnOrder)
    {
        costCenterReturnOrder.setUpdateTime(DateUtils.getNowDate());
        return costCenterReturnOrderMapper.updateCostCenterReturnOrder(costCenterReturnOrder);
    }

    /**
     * 批量删除成本中心退货单
     *
     * @param ids 需要删除的成本中心退货单主键
     * @return 结果
     */
    @Override
    public int deleteCostCenterReturnOrderByIds(Long[] ids)
    {
        return costCenterReturnOrderMapper.deleteCostCenterReturnOrderByIds(ids);
    }

    /**
     * 删除成本中心退货单信息
     *
     * @param id 成本中心退货单主键
     * @return 结果
     */
    @Override
    public int deleteCostCenterReturnOrderById(Long id)
    {
        return costCenterReturnOrderMapper.deleteCostCenterReturnOrderById(id);
    }

    /**
     * 批量激活成本中心退货单
     *
     * @param ids 成本中心退货单主键集合
     * @return 结果
     */
    @Override
    public int activeCostCenterOrderReturnByIds(Long[] ids) {
        //校验参数
        if (ObjectUtils.isEmpty(ids)) {
            throw new ServiceException("参数不存在！");
        }
        for (Long id : ids) {
            //查询单据
            CostCenterReturnOrder costCenterReturnOrder = costCenterReturnOrderMapper.selectCostCenterReturnOrderById(id);
            String status = costCenterReturnOrder.getOrderStatus();
            String orderNo = costCenterReturnOrder.getOrderNo();
            if (!OrderStatusConstant.ORDER_STATUS_NEW.equals(status)) {
                throw new ServiceException("成本中心退货单:"+ orderNo+"必须为新建状态才能激活！");
            }
            //更新状态
            costCenterReturnOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_ACTIVE);
            costCenterReturnOrder.setUpdateBy(SecurityUtils.getUsername());
            costCenterReturnOrder.setUpdateTime(DateUtils.getNowDate());
            if (costCenterReturnOrderMapper.updateCostCenterReturnOrder(costCenterReturnOrder) <= 0) {
                throw new ServiceException("更新成本中心退货单失败！");
            }
            //查询明细
            CostCenterReturnOrderDetail centerReturnOrderDetail = new CostCenterReturnOrderDetail();
            centerReturnOrderDetail.setReturnOrderNo(orderNo);
            List<CostCenterReturnOrderDetail> detailList = costCenterReturnOrderDetailMapper.selectCostCenterReturnOrderDetailList(centerReturnOrderDetail);
            //新增退货入库任务
            for (CostCenterReturnOrderDetail detail : detailList) {
                // addRetTask(orderNo, detail,costCenterReturnOrder);
            }
        }
        return 1;
    }

    @Override
    public AjaxResult importData(List<CostCenterReturnOrderDetailVo> stockList) {
        StringBuffer errorMsg = new StringBuffer();
        List<CostCenterReturnOrderDetailVo> resultList = new ArrayList<>();
        List<String> materialList = stockList.stream().map(CostCenterReturnOrderDetailVo::getMaterialNo).distinct().collect(Collectors.toList());

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
        List<CostCenterReturnOrderDetailVo> list = stockList.stream()
            .collect(Collectors.collectingAndThen(
                    Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getMaterialNo() + "-" + o.getLot()))),
                    ArrayList::new
            ));
        if(list.size() != stockList.size()){
            throw new ServiceException("导入物料批次重复,导入失败!");
        }
        if(stockList != null && stockList.size() > 0){
            for (CostCenterReturnOrderDetailVo detail : stockList) {
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

    @Override
    public List<RetTaskVo> costCenterTaskList(RetTaskDto retTaskDto) {
        return retTaskMapper.costCenterTaskList(retTaskDto);
    }

    @Override
    public RetTaskVo getTaskInfo(Long id) {
        RetTaskVo retTaskVo = retTaskMapper.selectCostCenterTaskDetail(id);
        // 获取物料属性信息
        WmsMaterialBasicDto unitDefDto = new WmsMaterialBasicDto();

        List<String> materialNoList =  new ArrayList<>();
        materialNoList.add(retTaskVo.getMaterialNo());
        unitDefDto.setMaterialNoList(materialNoList);
        AjaxResult unitResult = iMainDataService.getMaterialArrInfo(unitDefDto);

        if(unitResult.isError()) {
            throw new ServiceException(unitResult.get("msg").toString());
        }
        List<WmsMaterialAttrParamsDto> materialAttrParamsList = JSONObject.parseArray(unitResult.get("data").toString(), WmsMaterialAttrParamsDto.class);

        if(materialAttrParamsList == null || materialAttrParamsList.isEmpty()) {
            throw new ServiceException(String.format("物料”%s“,过期时间获取失败！", retTaskVo.getMaterialNo()));
        }

        WmsMaterialAttrParamsDto materialUnitDefDto = materialAttrParamsList.get(0);
        if(ObjectUtils.isEmpty(materialUnitDefDto)) {
            throw new ServiceException("过期时间获取失败！");
        }
        // 获取生产日期 过期日期
        String defaultValidityPeriod = materialUnitDefDto.getDefaultValidityPeriod();

        if(ObjectUtils.isEmpty(defaultValidityPeriod)) {
            throw new ServiceException("默认有效期获取失败!");
        }
        retTaskVo.setExpireTime(DateUtils.parseDateToStr("yyyyMMdd",DateUtils.addDays(new Date(), Integer.valueOf(defaultValidityPeriod))));
        return retTaskVo;
    }

    @Override
    @GlobalTransactional
    public String submitCostCenterTask(RetTaskDto retTaskDto)  throws Exception {
        CostCenterReturnOrderDetail costCenterReturnOrderDetail = costCenterReturnOrderDetailMapper.selectCostCenterReturnOrderDetailById(retTaskDto.getDetailId());
        CostCenterReturnOrder costCenterReturnOrder = costCenterReturnOrderMapper.selectCostCenterReturnOrderByOrderNo(costCenterReturnOrderDetail.getReturnOrderNo());
        RetTask retTask = retTaskMapper.selectRetTaskById(retTaskDto.getId());
        RetTask queryRetTask = new RetTask();
        queryRetTask.setStockinOrderNo(retTask.getStockinOrderNo());
        List<RetTask> retTasks = retTaskMapper.selectRetTaskList(queryRetTask);
        costCenterReturnOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_COMPLETE);
        for(RetTask e:retTasks){
            if(e.getId().compareTo(retTask.getId())!=0&&TaskStatusConstant.TASK_STATUS_NEW.equals(e.getTaskStatus())){
                costCenterReturnOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_PART_COMPLETE);
                break;
            }
        }
        String userName = SecurityUtils.getUsername();
        Date nowDate = DateUtils.getNowDate();
        //更新单据状态
        costCenterReturnOrder.setUpdateBy(userName);
        costCenterReturnOrder.setUpdateTime(nowDate);
        costCenterReturnOrderMapper.updateCostCenterReturnOrder(costCenterReturnOrder);
        //更新任务状态
        retTask.setTaskStatus(TaskStatusConstant.TASK_STATUS_COMPLETE);
        retTask.setCreateBy(userName);
        retTask.setCreateTime(nowDate);
        retTaskMapper.updateRetTask(retTask);
        //查询台账变更历史记录，把数量加台账
        InventoryHistory inventoryHistory = new InventoryHistory();
        inventoryHistory.setTaskNo(retTask.getStockInTaskNo());
        AjaxResult ajaxResult = inStoreService.selectInventoryHistoryList(inventoryHistory);
        if(ajaxResult.isError()){
            throw new ServiceException("获取台账变更历史失败");
        }
        List<InventoryHistory> inventoryHistories = com.alibaba.fastjson2.JSON.parseArray(JSON.toJSONString(ajaxResult.get("data")), InventoryHistory.class);
        if(inventoryHistories==null||inventoryHistories.size()<=0){
            throw new ServiceException("获取台账变更历史失败");
        }
        InventoryDetails inventoryDetails2 = new InventoryDetails();
        for(InventoryHistory e:inventoryHistories){
            InventoryDetails inventoryDetails = new InventoryDetails();
            inventoryDetails.setId(e.getInventoryId());
            ajaxResult = inStoreService.selectWmsInventoryDetails2(inventoryDetails);
            if(ajaxResult.isError()){
                throw new ServiceException("获取台账信息失败");
            }
            List<InventoryDetails> inventoryDetails1 = JSON.parseArray(JSON.toJSONString(ajaxResult.get("data")), InventoryDetails.class);
            if(inventoryDetails1==null||inventoryDetails1.size()<=0){
                throw new ServiceException("获取台账信息失败");
            }
            inventoryDetails2 = inventoryDetails1.get(0);
            inventoryDetails2.setInventoryQty(inventoryDetails2.getInventoryQty().add(e.getQty()));
            inventoryDetails2.setAvailableQty(inventoryDetails2.getAvailableQty().add(e.getQty()));
            InventoryHistory inventoryHistory1 = new InventoryHistory();
            inventoryHistory1.setMblnr(e.getMblnr());
            inventoryHistory1.setInventoryLocal(e.getInventoryLocal());
            inventoryHistory1.setPostionNo(e.getPostionNo());
            inventoryHistory1.setQty(e.getQty());
            inventoryHistory1.setBatchNo(e.getBatchNo());
            inventoryHistory1.setInventoryId(e.getInventoryId());
            inventoryHistory1.setTaskNo(retTask.getTaskNo());
            inventoryHistory1.setOperationType("2");
            inventoryHistory1.setCreateBy(SecurityUtils.getUsername());
            inventoryHistory1.setCreateTime(DateUtils.getNowDate());
            inventoryHistory1.setMaterialNo(e.getMaterialNo());
            inventoryHistory1.setOldMaterialNo(e.getOldMaterialNo());
            inventoryHistory1.setMaterialName(e.getMaterialName());
            inStoreService.insertInventoryHistory(inventoryHistory1);
            if(inStoreService.updateInventory(inventoryDetails2).isError()){
                throw new ServiceException("更新台账信息失败");
            }
        }
        Long[] ids = {costCenterReturnOrderDetail.getPositionId()};
        iMainDataService.updateStoragePositionStatus(ids,"1");
        IMsgObject reqMo = new MsgObject(IMsgObject.MOType.initSR);
        List<GroupRecord> list = new ArrayList<>();
        GroupRecord groupRecord = new GroupRecord();
        groupRecord.setName("IS_HEAD");
        groupRecord.setFieldValue("BUDAT", DateFormatUtils.format(nowDate, "yyyyMMdd"));
        groupRecord.setFieldValue("BLDAT", DateFormatUtils.format(nowDate, "yyyyMMdd"));
        groupRecord.setFieldValue("BKTXT", SecurityUtils.getUsername());
        groupRecord.setFieldValue("UNAME", SecurityUtils.getUsername());
        GroupRecord itemRecord = new GroupRecord();
        itemRecord.setName("IT_ITEM");
        // 物料编号（18 个字符
        itemRecord.setFieldValue("MATNR", retTask.getMaterialNo());
        // 移动类型(库存管理)
        itemRecord.setFieldValue("BWART", costCenterReturnOrder.getMoveType());
        //工厂
        itemRecord.setFieldValue("PLANT", costCenterReturnOrderDetail.getFactoryCode());
        //移动标识
        itemRecord.setFieldValue("MVT_IND", "");
        //采购订单价格单位的数量
        itemRecord.setFieldValue("PO_PR_QNT", retTask.getQty().toString());
        //订单价格单位(采购)
        itemRecord.setFieldValue("ORDERPR_UN", retTask.getOperationUnit());
        //批次编号
        itemRecord.setFieldValue("BATCH", retTask.getLot());
        //存储地点
        itemRecord.setFieldValue("LGORT", retTask.getStorageLocationCode());
        // 成本中心
        itemRecord.setFieldValue("COSTCENTER", costCenterReturnOrder.getCostCenterCode());
        //订单编号
        itemRecord.setFieldValue("ORDERID", costCenterReturnOrder.getInnerOrderNo());
        //库存类型
        itemRecord.setFieldValue("STCK_TYPE", "");
        //特殊库存标识
        itemRecord.setFieldValue("SOBKZ", "");
        //仓位
        itemRecord.setFieldValue("STGE_BIN", retTask.getPositionNo());
        //仓储类型
        itemRecord.setFieldValue("STGE_TYPE", "");
        //使用冲销移动类型标识符
        itemRecord.setFieldValue("XSTOB", "X");
        //项目文本
        itemRecord.setFieldValue("ITEM_TEXT", retTask.getTaskNo());
        //工作分解结构元素 (WBS 元素)
        itemRecord.setFieldValue("WBS_ELEM", "");
        //科目分配的网络号
        itemRecord.setFieldValue("NETWORK", "");
        list.add(groupRecord);
        list.add(itemRecord);
        reqMo.setReqGroupRecord(list);
        byte[] result = remoteEsbSendService.sendToEsb(EsbSendSapConstant.Z_IWMS_COST, reqMo.toString().getBytes());
        // 转化为标准结果
        MsgObject resMo = new MsgObject(result, IMsgObject.MOType.initSR);
        String errorMsg = resMo.getResValue("ERRORMSG");
        String flag = resMo.getResValue("FLAG");
        if (!SapReqOrResConstant.SAP_RETURN_TYPE.equals(flag)) {
            throw new ServiceException("成品中心sap过账失败!" + errorMsg);
        }
        String voucherNo = resMo.getResValue("MBLNR");
        addTaskLog(retTask,voucherNo,inventoryDetails2);
        return "提交成功";
    }

    public void addTaskLog(RetTask retTask,  String voucherNo,InventoryDetails inventoryDetails) throws Exception {
        //同步添加任务记录
        TaskLog taskLog = new TaskLog();
        taskLog.setTaskNo(retTask.getTaskNo());
        taskLog.setTaskType(retTask.getTaskType());
        taskLog.setMaterialNo(retTask.getMaterialNo());
        taskLog.setMaterialName(retTask.getMaterialName());
        taskLog.setOrderNo(retTask.getStockinOrderNo());
        taskLog.setLot(inventoryDetails.getStockInLot());
        taskLog.setQty(retTask.getQty());
        taskLog.setFactoryCode(inventoryDetails.getFactoryCode());
        taskLog.setPositionCode(inventoryDetails.getPositionNo());
        taskLog.setLocationCode(inventoryDetails.getLocationCode());
        taskLog.setMaterialCertificate(voucherNo);
        taskLog.setTargetFactoryCode(inventoryDetails.getFactoryCode());
        taskLog.setTargetLocationCode(inventoryDetails.getLocationCode());
        taskLog.setTargetAreaCode(inventoryDetails.getAreaCode());
        taskLog.setTargetPositionCode(inventoryDetails.getPositionNo());
        taskLog.setUnit(retTask.getUnit());
        taskLog.setOperationUnit(inventoryDetails.getOperationUnit());
        taskLog.setOrderNo(retTask.getStockinOrderNo());
        taskLog.setOrderType(TaskLogTypeConstant.COST);
        taskLog.setIsConsign(inventoryDetails.getIsConsign());
        taskLog.setIsFreeze(inventoryDetails.getIsFreeze());
        taskLog.setIsQc(inventoryDetails.getIsQc());
        taskLog.setSupplierCode(inventoryDetails.getSupplierCode());
        taskLog.setSupplierName(inventoryDetails.getSupplierName());
        taskLog.setTenantId(SecurityUtils.getLoginUser().getSysUser().getTenantId());
        taskLog.setCreateBy(SecurityUtils.getUsername());
        taskLog.setCreateTime(DateUtils.getNowDate());
        if (taskService.add(taskLog).isError()) {
            throw new ServiceException("新增任务记录失败！");
        }
    }

}
