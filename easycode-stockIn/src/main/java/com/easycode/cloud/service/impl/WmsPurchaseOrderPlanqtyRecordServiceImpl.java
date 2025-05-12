package com.easycode.cloud.service.impl;

import com.easycode.cloud.domain.dto.PlanQtyRecordDto;
import com.weifu.cloud.common.core.exception.ServiceException;
import com.weifu.cloud.common.core.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import com.weifu.cloud.common.security.utils.SecurityUtils;
import com.easycode.cloud.constants.PlanqtyRecordEnum;
import com.weifu.cloud.domain.PurchaseOrderDetail;
import com.easycode.cloud.domain.WmsPurchaseOrderPlanqtyRecord;
import com.weifu.cloud.domain.vo.DeliveryOrderDetailVo;
import com.easycode.cloud.mapper.DeliveryOrderDetailMapper;
import com.easycode.cloud.mapper.WmsPurchaseOrderPlanqtyRecordMapper;
import com.easycode.cloud.service.IWmsPurchaseOrderPlanqtyRecordService;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 采购单据需求数量变更记录Service业务层处理
 *
 * @author weifu
 * @date 2024-06-13
 */
@Service
public class WmsPurchaseOrderPlanqtyRecordServiceImpl implements IWmsPurchaseOrderPlanqtyRecordService
{
    private static final Logger logger = LoggerFactory.getLogger(WmsPurchaseOrderPlanqtyRecordServiceImpl.class);

    @Autowired
    private WmsPurchaseOrderPlanqtyRecordMapper wmsPurchaseOrderPlanqtyRecordMapper;

    @Autowired
    private DeliveryOrderDetailMapper deliveryOrderDetailMapper;

    /**
     * 查询采购单据需求数量变更记录
     *
     * @param id 采购单据需求数量变更记录主键
     * @return 采购单据需求数量变更记录
     */
    @Override
    public WmsPurchaseOrderPlanqtyRecord selectWmsPurchaseOrderPlanqtyRecordById(Long id)
    {
        return wmsPurchaseOrderPlanqtyRecordMapper.selectWmsPurchaseOrderPlanqtyRecordById(id);
    }

    /**
     * 查询采购单据需求数量变更记录列表
     *
     * @param wmsPurchaseOrderPlanqtyRecord 采购单据需求数量变更记录
     * @return 采购单据需求数量变更记录
     */
    @Override
    public List<WmsPurchaseOrderPlanqtyRecord> selectWmsPurchaseOrderPlanqtyRecordList(WmsPurchaseOrderPlanqtyRecord wmsPurchaseOrderPlanqtyRecord)
    {
        return wmsPurchaseOrderPlanqtyRecordMapper.selectWmsPurchaseOrderPlanqtyRecordList(wmsPurchaseOrderPlanqtyRecord);
    }

    /**
     * 根据采购单号list查询采购单据需求数量变更记录列表
     *
     * @param list 采购单号list
     * @return 采购单据需求数量变更记录
     */
    @Override
    public List<WmsPurchaseOrderPlanqtyRecord> queryDemandQtyList(List<String> list)
    {
        return wmsPurchaseOrderPlanqtyRecordMapper.queryDemandQtyList(list);
    }

    /**
     * 新增采购单据需求数量变更记录
     *
     * @param wmsPurchaseOrderPlanqtyRecord 采购单据需求数量变更记录
     * @return 结果
     */
    @Override
    public int insertWmsPurchaseOrderPlanqtyRecord(WmsPurchaseOrderPlanqtyRecord wmsPurchaseOrderPlanqtyRecord)
    {
        return wmsPurchaseOrderPlanqtyRecordMapper.insertWmsPurchaseOrderPlanqtyRecord(wmsPurchaseOrderPlanqtyRecord);
    }

    /**
     * 修改采购单据需求数量变更记录
     *
     * @param wmsPurchaseOrderPlanqtyRecord 采购单据需求数量变更记录
     * @return 结果
     */
    @Override
    public int updateWmsPurchaseOrderPlanqtyRecord(WmsPurchaseOrderPlanqtyRecord wmsPurchaseOrderPlanqtyRecord)
    {
        return wmsPurchaseOrderPlanqtyRecordMapper.updateWmsPurchaseOrderPlanqtyRecord(wmsPurchaseOrderPlanqtyRecord);
    }

    /**
     * 批量删除采购单据需求数量变更记录
     *
     * @param ids 需要删除的采购单据需求数量变更记录主键
     * @return 结果
     */
    @Override
    public int deleteWmsPurchaseOrderPlanqtyRecordByIds(Long[] ids)
    {
        return wmsPurchaseOrderPlanqtyRecordMapper.deleteWmsPurchaseOrderPlanqtyRecordByIds(ids);
    }

    /**
     * 删除采购单据需求数量变更记录信息
     *
     * @param id 采购单据需求数量变更记录主键
     * @return 结果
     */
    @Override
    public int deleteWmsPurchaseOrderPlanqtyRecordById(Long id)
    {
        return wmsPurchaseOrderPlanqtyRecordMapper.deleteWmsPurchaseOrderPlanqtyRecordById(id);
    }

    /**
     * 共通方法-新增数据
     *
     * @param wmsPurchaseOrderPlanqtyRecord 实例对象
     * @return 实例对象
     */
    @Override
    public WmsPurchaseOrderPlanqtyRecord commonSave(WmsPurchaseOrderPlanqtyRecord wmsPurchaseOrderPlanqtyRecord) {
        //变更类型（1：sap需求量、11：sap变更时、2：sap收货数量、22：sap收货变更时、3：wms建单收货数量、4：wms修改收获数量变化值 5：wms收货完成同步sap数量
        String changeType = wmsPurchaseOrderPlanqtyRecord.getChangeType();
        if (StringUtils.isEmpty(changeType)){
            throw new ServiceException("类型不能为空!");
        }
        if (StringUtils.isEmpty(wmsPurchaseOrderPlanqtyRecord.getMaterialNo())){
            throw new ServiceException("物料编码不能为空!");
        }
        WmsPurchaseOrderPlanqtyRecord selectParams = new WmsPurchaseOrderPlanqtyRecord();
        //类型1和2为sapOrderId   其他类型必传purchaseOrderNo  查找所有的历史记录
        selectParams.setPurchaseOrderNo(wmsPurchaseOrderPlanqtyRecord.getPurchaseOrderNo());
        selectParams.setMaterialNo(wmsPurchaseOrderPlanqtyRecord.getMaterialNo());
        selectParams.setPurchaseLineNo(wmsPurchaseOrderPlanqtyRecord.getPurchaseLineNo());
        List<WmsPurchaseOrderPlanqtyRecord> wmsPurchaseOrderPlanqtyRecords = wmsPurchaseOrderPlanqtyRecordMapper.selectWmsPurchaseOrderPlanqtyRecordList(selectParams);
        wmsPurchaseOrderPlanqtyRecord.setCreatedBy(SecurityUtils.getUsername());
        wmsPurchaseOrderPlanqtyRecord.setCreatedTime(DateUtils.getNowDate());
//        if(ObjectUtils.isNotEmpty(wmsPurchaseOrderPlanqtyRecords)){
//            WmsPurchaseOrderPlanqtyRecord firstObj = wmsPurchaseOrderPlanqtyRecords.get(0);
//            if(ObjectUtils.isEmpty(wmsPurchaseOrderPlanqtyRecord.getTotalPlanQty()) && ObjectUtils.isNotEmpty(firstObj.getTotalPlanQty())){
//                wmsPurchaseOrderPlanqtyRecord.setTotalPlanQty(firstObj.getTotalPlanQty());
//            }
//        }

        //根据判断类型 进行不同业务逻辑的处理
        if(changeType.equals(PlanqtyRecordEnum.TYPE_SAP_CREATE_PLAN.getKey())){
            saveSapPlan(wmsPurchaseOrderPlanqtyRecord,wmsPurchaseOrderPlanqtyRecords);
        }else if(changeType.equals(PlanqtyRecordEnum.TYPE_SAP_CREATE_RECEIVING.getKey())){
            saveSapReceiving(wmsPurchaseOrderPlanqtyRecord,wmsPurchaseOrderPlanqtyRecords);
        }else if(changeType.equals(PlanqtyRecordEnum.TYPE_WMS_CREATE_RECEIVING.getKey())){
            saveWmsReceivingCreate(wmsPurchaseOrderPlanqtyRecord,wmsPurchaseOrderPlanqtyRecords);
        }else if(changeType.equals(PlanqtyRecordEnum.TYPE_WMS_UPDATE_RECEIVING.getKey())){
            saveWmsReceivingUpdate(wmsPurchaseOrderPlanqtyRecord,wmsPurchaseOrderPlanqtyRecords);
        }else if(changeType.equals(PlanqtyRecordEnum.TYPE_WMS_FINISH_RECEIVING.getKey())){
            saveWmsReceivingFinish(wmsPurchaseOrderPlanqtyRecord,wmsPurchaseOrderPlanqtyRecords);
        }
        if(StringUtils.isNotEmpty(wmsPurchaseOrderPlanqtyRecord.getUpdatedBy())){
            wmsPurchaseOrderPlanqtyRecordMapper.insertWmsPurchaseOrderPlanqtyRecord(wmsPurchaseOrderPlanqtyRecord);
        }
        return wmsPurchaseOrderPlanqtyRecord;
    }
    /**
     * 收货完成业务逻辑处理   类型：5
     */
    private void saveWmsReceivingFinish(WmsPurchaseOrderPlanqtyRecord wmsPurchaseOrderPlanqtyRecord, List<WmsPurchaseOrderPlanqtyRecord> wmsPurchaseOrderPlanqtyRecords) {
        //类型：5：wms收货完成同步sap数量   每完成一次收货数量变化则保存一条数据
        if (wmsPurchaseOrderPlanqtyRecord.getPurchaseOrderNo()==null){
            throw new ServiceException("wms完成收货单据号不能为空!");
        }
        if (wmsPurchaseOrderPlanqtyRecord.getDeliveryQty()==null){
            throw new ServiceException("wms完成收货数量不能为空!");
        }
        //eg:wms收货数量  10
        WmsPurchaseOrderPlanqtyRecord firstObj = null;
        if(ObjectUtils.isNotEmpty(wmsPurchaseOrderPlanqtyRecords)){
            firstObj = wmsPurchaseOrderPlanqtyRecords.get(0);
        }else{
            firstObj = new WmsPurchaseOrderPlanqtyRecord();
            firstObj.setPlanQty(0d);
        }
        //查找最近一个数据  如果有则获取当前需求量  没有则新增
        wmsPurchaseOrderPlanqtyRecord.setTotalPlanQty(firstObj.getTotalPlanQty());
        wmsPurchaseOrderPlanqtyRecord.setPlanQty(firstObj.getPlanQty());
        wmsPurchaseOrderPlanqtyRecord.setBeforePlanQty(firstObj.getPlanQty());
        wmsPurchaseOrderPlanqtyRecord.setChangePlanQty(0d);
        wmsPurchaseOrderPlanqtyRecord.setUpdatedBy("wms完成收货数量数据");
    }
    /**
     * wms修改收获数量业务逻辑处理  类型：4
     */
    private void saveWmsReceivingUpdate(WmsPurchaseOrderPlanqtyRecord wmsPurchaseOrderPlanqtyRecord, List<WmsPurchaseOrderPlanqtyRecord> wmsPurchaseOrderPlanqtyRecords) {
        //类型：4：wms修改收获数量变化值   每修改一次如果收货数量变化则保存一条数据
        if (wmsPurchaseOrderPlanqtyRecord.getPurchaseOrderNo()==null){
            throw new ServiceException("wms修改单据号不能为空!");
        }
        if (wmsPurchaseOrderPlanqtyRecord.getDeliveryQty()==null){
            throw new ServiceException("wms修改收货数量不能为空!");
        }
        //eg:wms收货数量  10->20
        WmsPurchaseOrderPlanqtyRecord firstObj = wmsPurchaseOrderPlanqtyRecords.get(0);
        //查找最近一条为4的数据 判断数量是否一致(不一致类型变更为4，一致则不保存数据)
        List<WmsPurchaseOrderPlanqtyRecord> collect4Sap = wmsPurchaseOrderPlanqtyRecords.stream().filter(planQtyObj ->
                        planQtyObj.getChangeType().equals(PlanqtyRecordEnum.TYPE_WMS_UPDATE_RECEIVING.getKey())).collect(Collectors.toList());
        //查找最近一条为3的数据 判断数量是否一致(不一致类型变更为4，一致则不保存数据)
        List<WmsPurchaseOrderPlanqtyRecord> collect3Sap = wmsPurchaseOrderPlanqtyRecords.stream().filter(planQtyObj ->
                planQtyObj.getChangeType().equals(PlanqtyRecordEnum.TYPE_WMS_CREATE_RECEIVING.getKey())).collect(Collectors.toList());
        if(ObjectUtils.isNotEmpty(collect4Sap) || ObjectUtils.isNotEmpty(collect3Sap)) {
            Double deliveryQty = 0d;
            Double supplierDeliveryQty = 0d;
            if(ObjectUtils.isNotEmpty(collect4Sap)) {
                WmsPurchaseOrderPlanqtyRecord firstByType2Obj = collect4Sap.get(0);
                deliveryQty = firstByType2Obj.getDeliveryQty();
                supplierDeliveryQty = firstByType2Obj.getSupplierDeliveryQty();
            }else if(ObjectUtils.isNotEmpty(collect3Sap)) {
                //BigDecimal求和    如果需求数量没有变更记录的话  用创建单据的总和判断
                deliveryQty = collect3Sap.stream().mapToDouble(u->u.getDeliveryQty()).sum();
                supplierDeliveryQty = collect3Sap.stream().mapToDouble(u->u.getSupplierDeliveryQty()).sum();
            }
            if (Double.compare(deliveryQty,wmsPurchaseOrderPlanqtyRecord.getDeliveryQty()) == 0 &&
                    Double.compare(supplierDeliveryQty,wmsPurchaseOrderPlanqtyRecord.getSupplierDeliveryQty()) == 0) {
                //如果相同则证明wms的收货数量没有变化，不用保存数据
                return;
            }else{
                //更改类型  数量变为累加的数量  eg：新增20 30 修改为70   则最近一条为changePlanQty为70-50
                wmsPurchaseOrderPlanqtyRecord.setChangeType(PlanqtyRecordEnum.TYPE_WMS_UPDATE_RECEIVING.getKey());
                //wms系统更改了收货数量，计算变更数量：最近一条1或11的需求数量-当前需求数量   eg:10->20
                BigDecimal changCount = BigDecimal.valueOf(wmsPurchaseOrderPlanqtyRecord.getDeliveryQty()).subtract(BigDecimal.valueOf(deliveryQty));
                BigDecimal supplierChangCount = BigDecimal.valueOf(wmsPurchaseOrderPlanqtyRecord.getSupplierDeliveryQty()).subtract(BigDecimal.valueOf(supplierDeliveryQty));
                BigDecimal planCount = BigDecimal.valueOf(firstObj.getPlanQty()).add(changCount.negate());
                BigDecimal totalPlanCount = BigDecimal.valueOf(firstObj.getTotalPlanQty()).add(supplierChangCount.negate());
                wmsPurchaseOrderPlanqtyRecord.setTotalPlanQty(totalPlanCount.doubleValue());
                wmsPurchaseOrderPlanqtyRecord.setPlanQty(planCount.doubleValue());
                wmsPurchaseOrderPlanqtyRecord.setBeforePlanQty(firstObj.getPlanQty());
                wmsPurchaseOrderPlanqtyRecord.setChangePlanQty(-changCount.doubleValue());
                wmsPurchaseOrderPlanqtyRecord.setUpdatedBy("wms系统修改收货数量数据");
            }
        }
    }
    /**
     * wms建单收货数量业务逻辑处理   类型：3
     */
    private void saveWmsReceivingCreate(WmsPurchaseOrderPlanqtyRecord wmsPurchaseOrderPlanqtyRecord, List<WmsPurchaseOrderPlanqtyRecord> wmsPurchaseOrderPlanqtyRecords) {
        //类型：3：wms建单收货数量
        if (wmsPurchaseOrderPlanqtyRecord.getPurchaseOrderNo()==null){
            throw new ServiceException("wms建单单据号不能为空!");
        }
        if (wmsPurchaseOrderPlanqtyRecord.getDeliveryQty()==null){
            throw new ServiceException("wms建单收货数量不能为空!");
        }
        WmsPurchaseOrderPlanqtyRecord firstObj = null;
        if(ObjectUtils.isNotEmpty(wmsPurchaseOrderPlanqtyRecords)){
            firstObj = wmsPurchaseOrderPlanqtyRecords.get(0);
        }else{
            firstObj = new WmsPurchaseOrderPlanqtyRecord();
            firstObj.setPlanQty(0d);
        }
        //查找最近一个数据  如果有则获取当前需求量  没有则新增
        BigDecimal planCount = BigDecimal.valueOf(firstObj.getPlanQty()).add(BigDecimal.valueOf(-wmsPurchaseOrderPlanqtyRecord.getDeliveryQty()));
        BigDecimal totalPlanCount = BigDecimal.valueOf(firstObj.getTotalPlanQty()).add(BigDecimal.valueOf(-wmsPurchaseOrderPlanqtyRecord.getDeliveryQty()));
        wmsPurchaseOrderPlanqtyRecord.setTotalPlanQty(totalPlanCount.doubleValue());
        wmsPurchaseOrderPlanqtyRecord.setPlanQty(planCount.doubleValue());
        wmsPurchaseOrderPlanqtyRecord.setBeforePlanQty(firstObj.getPlanQty());
        wmsPurchaseOrderPlanqtyRecord.setChangePlanQty(-wmsPurchaseOrderPlanqtyRecord.getDeliveryQty());
        wmsPurchaseOrderPlanqtyRecord.setUpdatedBy("wms新增收货数量数据");
        //查找是否有类型为4修改的数据   如果有则新生成一条4数据
        List<WmsPurchaseOrderPlanqtyRecord> collectSap = wmsPurchaseOrderPlanqtyRecords.stream().filter(planQtyObj ->
                planQtyObj.getChangeType().equals(PlanqtyRecordEnum.TYPE_WMS_UPDATE_RECEIVING.getKey())).collect(Collectors.toList());
        if(ObjectUtils.isNotEmpty(collectSap)){
            WmsPurchaseOrderPlanqtyRecord orderPlanqtyRecord = collectSap.get(0);
            Double toatalDeQty = orderPlanqtyRecord.getDeliveryQty() + wmsPurchaseOrderPlanqtyRecord.getDeliveryQty();
            Double totalSupplierQty = orderPlanqtyRecord.getSupplierDeliveryQty() + wmsPurchaseOrderPlanqtyRecord.getDeliveryQty();
            orderPlanqtyRecord.setDeliveryQty(toatalDeQty);
            orderPlanqtyRecord.setSupplierDeliveryQty(totalSupplierQty);
            orderPlanqtyRecord.setId(null);
            wmsPurchaseOrderPlanqtyRecordMapper.insertWmsPurchaseOrderPlanqtyRecord(orderPlanqtyRecord);
        }
    }

    /**
     * sap收货数量业务逻辑处理  类型为2或者22
     */
    private void saveSapReceiving(WmsPurchaseOrderPlanqtyRecord wmsPurchaseOrderPlanqtyRecord,List<WmsPurchaseOrderPlanqtyRecord> wmsPurchaseOrderPlanqtyRecords) {
        //类型：2：sap收货数量、22：sap收货变更时
        if (wmsPurchaseOrderPlanqtyRecord.getPurchaseOrderNo()==null){
            throw new ServiceException("sap标识不能为空!");
        }
        if (wmsPurchaseOrderPlanqtyRecord.getDeliveryQty()==null){
            throw new ServiceException("sap收货数量不能为空!");
        }
        //查找最近一个数据  如果有则获取当前需求量  没有则新增
        if(ObjectUtils.isNotEmpty(wmsPurchaseOrderPlanqtyRecords)){
            WmsPurchaseOrderPlanqtyRecord firstObj = wmsPurchaseOrderPlanqtyRecords.get(0);
            //查找最近一条为2或者22的数据 判断数量是否一致(不一致类型变更为22，一致则不保存数据)
            List<WmsPurchaseOrderPlanqtyRecord> collectSap = wmsPurchaseOrderPlanqtyRecords.stream().filter(planQtyObj ->
                    planQtyObj.getChangeType().equals(PlanqtyRecordEnum.TYPE_SAP_CREATE_RECEIVING.getKey()) ||
                            planQtyObj.getChangeType().equals(PlanqtyRecordEnum.TYPE_SAP_UPDATE_RECEIVING.getKey())).collect(Collectors.toList());
            if(ObjectUtils.isNotEmpty(collectSap)){
                WmsPurchaseOrderPlanqtyRecord firstByType2Obj = collectSap.get(0);
                Double deliveryQty = firstByType2Obj.getDeliveryQty();
                Double supplierDeliveryQty = firstByType2Obj.getSupplierDeliveryQty();
                if (Double.compare(deliveryQty,wmsPurchaseOrderPlanqtyRecord.getDeliveryQty()) == 0 &&
                        Double.compare(supplierDeliveryQty,wmsPurchaseOrderPlanqtyRecord.getSupplierDeliveryQty()) == 0) {
                    //如果相同则证明sap的收货数量没有变化，不用保存数据
                    return;
                } else if(Double.compare(deliveryQty,wmsPurchaseOrderPlanqtyRecord.getDeliveryQty()) == 0 &&
                        Double.compare(supplierDeliveryQty,wmsPurchaseOrderPlanqtyRecord.getSupplierDeliveryQty()) != 0){
                    // 供应商收货数量增加（有两种情况会进入此方法 1、sap调整送货时间，eg: 原本协议行送货时间是1月6号，供应商看不见，sap调整为1月1号，供应商可以看见；
                    BigDecimal difference = BigDecimal.valueOf(wmsPurchaseOrderPlanqtyRecord.getSupplierDeliveryQty()).subtract(BigDecimal.valueOf(firstByType2Obj.getSupplierDeliveryQty()));
                    BigDecimal add = BigDecimal.valueOf(firstObj.getTotalPlanQty()).subtract(difference);
                    // 增加的这条记录，需要计算供应商需求数量，planQty继承最新的一条数据，
                    wmsPurchaseOrderPlanqtyRecord.setChangeType(PlanqtyRecordEnum.TYPE_SAP_UPDATE_RECEIVING.getKey());
                    wmsPurchaseOrderPlanqtyRecord.setDeliveryQty(firstByType2Obj.getDeliveryQty());
                    wmsPurchaseOrderPlanqtyRecord.setTotalPlanQty(add.doubleValue());
                    wmsPurchaseOrderPlanqtyRecord.setPlanQty(firstObj.getPlanQty());
                    wmsPurchaseOrderPlanqtyRecord.setBeforePlanQty(firstObj.getPlanQty());
                    wmsPurchaseOrderPlanqtyRecord.setChangePlanQty(firstObj.getChangePlanQty());
                    wmsPurchaseOrderPlanqtyRecord.setUpdatedBy("SAP同步或定时拉取时，sap系统修改供应商收货数量数据");

                }else{
                    //不相同则计算出数量差是否和数据库中收货完成的数量是否一致，不一致则sap系统里面的变更，一致则正常  eg：20-> 30  收货完成20
                    wmsPurchaseOrderPlanqtyRecord.setChangeType(PlanqtyRecordEnum.TYPE_SAP_UPDATE_RECEIVING.getKey());
                    //查找最近一条为2或者22的数据 判断数量是否一致(不一致类型变更为22，一致则不保存数据)
                    Double finishCount = wmsPurchaseOrderPlanqtyRecords.stream().filter(planQtyObj ->
                            planQtyObj.getChangeType().equals(PlanqtyRecordEnum.TYPE_WMS_FINISH_RECEIVING.getKey())
                            && planQtyObj.getId() > firstByType2Obj.getId())
                            .mapToDouble(WmsPurchaseOrderPlanqtyRecord::getDeliveryQty).sum();
                    Double finishSupplierCount = wmsPurchaseOrderPlanqtyRecords.stream().filter(planQtyObj ->
                            planQtyObj.getChangeType().equals(PlanqtyRecordEnum.TYPE_WMS_FINISH_RECEIVING.getKey())
                            && planQtyObj.getId() > firstByType2Obj.getId())
                            .mapToDouble(WmsPurchaseOrderPlanqtyRecord::getSupplierDeliveryQty).sum();
                    if(ObjectUtils.isNotEmpty(finishCount) && finishCount.intValue() != 0){
//                        wmsPurchaseOrderPlanqtyRecord.setChangeType(PlanqtyRecordEnum.TYPE_SAP_UPDATE_RECEIVING.getKey());
//                        BigDecimal changCount = BigDecimal.valueOf(finishCount).
//                                subtract(BigDecimal.valueOf(wmsPurchaseOrderPlanqtyRecord.getDeliveryQty()));
//                        BigDecimal supplierChangCount = BigDecimal.valueOf(finishSupplierCount).
//                                subtract(BigDecimal.valueOf(wmsPurchaseOrderPlanqtyRecord.getDeliveryQty()));
//                        BigDecimal planCount = BigDecimal.valueOf(firstObj.getPlanQty()).add(changCount);
//                        BigDecimal totalPlanCount = BigDecimal.valueOf(firstObj.getTotalPlanQty()).add(supplierChangCount);
//                        wmsPurchaseOrderPlanqtyRecord.setTotalPlanQty(totalPlanCount.doubleValue());
//                        wmsPurchaseOrderPlanqtyRecord.setPlanQty(planCount.doubleValue());
//                        wmsPurchaseOrderPlanqtyRecord.setBeforePlanQty(firstObj.getPlanQty());
//                        wmsPurchaseOrderPlanqtyRecord.setChangePlanQty(changCount.doubleValue());
//                        wmsPurchaseOrderPlanqtyRecord.setUpdatedBy("SAP同步或定时拉取时，sap系统修改收货数量数据，变更数量可能为0");
                        BigDecimal sapSubtractCount = BigDecimal.valueOf(wmsPurchaseOrderPlanqtyRecord.getDeliveryQty()).subtract(BigDecimal.valueOf(deliveryQty));
                        // 这里使用getDeliveryQty
                        BigDecimal sapSupplierSubtractCount = BigDecimal.valueOf(wmsPurchaseOrderPlanqtyRecord.getSupplierDeliveryQty())
                                .subtract(BigDecimal.valueOf(supplierDeliveryQty));
                        if(!sapSubtractCount.stripTrailingZeros().equals(BigDecimal.valueOf(finishCount).stripTrailingZeros()) ||
                                !sapSupplierSubtractCount.stripTrailingZeros().equals(BigDecimal.valueOf(finishSupplierCount).stripTrailingZeros())){
                            wmsPurchaseOrderPlanqtyRecord.setChangeType(PlanqtyRecordEnum.TYPE_SAP_UPDATE_RECEIVING.getKey());
                            BigDecimal changCount = BigDecimal.valueOf(finishCount).subtract(sapSubtractCount);
                            sapSupplierSubtractCount = BigDecimal.valueOf(wmsPurchaseOrderPlanqtyRecord.getSupplierDeliveryQty())
                                    .subtract(BigDecimal.valueOf(supplierDeliveryQty));
                            BigDecimal supplierChangCount = BigDecimal.valueOf(finishSupplierCount).subtract(sapSupplierSubtractCount);
                            BigDecimal planCount = BigDecimal.valueOf(firstObj.getPlanQty()).add(changCount);
                            BigDecimal totalPlanCount = BigDecimal.valueOf(firstObj.getTotalPlanQty()).add(supplierChangCount);
                            wmsPurchaseOrderPlanqtyRecord.setTotalPlanQty(totalPlanCount.doubleValue());
                            wmsPurchaseOrderPlanqtyRecord.setPlanQty(planCount.doubleValue());
                            wmsPurchaseOrderPlanqtyRecord.setBeforePlanQty(firstObj.getPlanQty());
                            wmsPurchaseOrderPlanqtyRecord.setChangePlanQty(changCount.doubleValue());
                            wmsPurchaseOrderPlanqtyRecord.setUpdatedBy("SAP同步或定时拉取时，sap系统修改收货数量数据");
                        }
//                        }
                    }else{
                        //sap系统更改了收货数量，计算变更数量：最近一条1或11的需求数量-当前需求数量   eg:10->20
                        BigDecimal changCount = BigDecimal.valueOf(wmsPurchaseOrderPlanqtyRecord.getDeliveryQty()).subtract(BigDecimal.valueOf(deliveryQty));
                        BigDecimal supplierChangCount = BigDecimal.valueOf(wmsPurchaseOrderPlanqtyRecord.getSupplierDeliveryQty()).subtract(BigDecimal.valueOf(supplierDeliveryQty));
                        BigDecimal planCount = BigDecimal.valueOf(firstObj.getPlanQty()).add(changCount.negate());
                        BigDecimal totalPlanCount = BigDecimal.valueOf(firstObj.getTotalPlanQty()).add(supplierChangCount.negate());
                        wmsPurchaseOrderPlanqtyRecord.setTotalPlanQty(totalPlanCount.doubleValue());
                        wmsPurchaseOrderPlanqtyRecord.setPlanQty(planCount.doubleValue());
                        wmsPurchaseOrderPlanqtyRecord.setBeforePlanQty(firstObj.getPlanQty());
                        wmsPurchaseOrderPlanqtyRecord.setChangePlanQty(-changCount.doubleValue());
                        wmsPurchaseOrderPlanqtyRecord.setUpdatedBy("SAP同步或定时拉取时，sap系统修改收货数量数据");
                    }
                }
            }else{
                BigDecimal planCount = BigDecimal.valueOf(firstObj.getPlanQty()).add(BigDecimal.valueOf(-wmsPurchaseOrderPlanqtyRecord.getDeliveryQty()));
                // totalPlanQty 是经过经过供应商时间处理后的需求数量，同样，supplierDeliveryQty也是，两者相减，得到实际的需求数量
                BigDecimal totalPlanCount = BigDecimal.valueOf(firstObj.getTotalPlanQty()).add(BigDecimal.valueOf(-wmsPurchaseOrderPlanqtyRecord.getSupplierDeliveryQty()));
                wmsPurchaseOrderPlanqtyRecord.setTotalPlanQty(totalPlanCount.doubleValue());
                wmsPurchaseOrderPlanqtyRecord.setPlanQty(planCount.doubleValue());
                wmsPurchaseOrderPlanqtyRecord.setBeforePlanQty(firstObj.getPlanQty());
                wmsPurchaseOrderPlanqtyRecord.setChangePlanQty(-wmsPurchaseOrderPlanqtyRecord.getDeliveryQty());
                wmsPurchaseOrderPlanqtyRecord.setUpdatedBy("SAP同步或定时拉取时，sap系统新增收货数量数据");
            }
        }else{
            throw new ServiceException("数据错乱，没有找到sap订单的数据!");
        }
    }

    /**
     * sap需求量业务逻辑处理  类型为1或者11
     */
    private void saveSapPlan(WmsPurchaseOrderPlanqtyRecord wmsPurchaseOrderPlanqtyRecord,List<WmsPurchaseOrderPlanqtyRecord> wmsPurchaseOrderPlanqtyRecords) {
        //类型：1：sap需求量、11：sap变更时
        if (wmsPurchaseOrderPlanqtyRecord.getPurchaseOrderNo()==null){
            throw new ServiceException("sap标识不能为空!");
        }
        if (wmsPurchaseOrderPlanqtyRecord.getDeliveryQty()==null){
            throw new ServiceException("sap需求数量不能为空!");
        }
        //查找最近一个数据  如果有则获取当前需求量  没有则新增
        if(ObjectUtils.isNotEmpty(wmsPurchaseOrderPlanqtyRecords)){
            WmsPurchaseOrderPlanqtyRecord firstObj = wmsPurchaseOrderPlanqtyRecords.get(0);
            //查找最近一条为1或者11的数据 判断数量是否一致(不一致类型变更为11，一致则不保存数据)
            List<WmsPurchaseOrderPlanqtyRecord> collectSap = wmsPurchaseOrderPlanqtyRecords.stream().filter(planQtyObj ->
                    planQtyObj.getChangeType().equals(PlanqtyRecordEnum.TYPE_SAP_CREATE_PLAN.getKey()) ||
                            planQtyObj.getChangeType().equals(PlanqtyRecordEnum.TYPE_SAP_UPDATE_PLAN.getKey())).collect(Collectors.toList());
            if(ObjectUtils.isNotEmpty(collectSap)){
                WmsPurchaseOrderPlanqtyRecord firstByType1Obj = collectSap.get(0);
                Double deliveryQty = firstByType1Obj.getDeliveryQty();
                Double supplierDeliveryQty = firstByType1Obj.getSupplierDeliveryQty();
                if (supplierDeliveryQty == null) {
                    logger.info(String.format("-----if----firstByType1Obj的id%s", firstByType1Obj.getId()));
                    return;
                }
                if (wmsPurchaseOrderPlanqtyRecord.getSupplierDeliveryQty() == null) {
                    logger.info(String.format("-----if----wmsPurchaseOrderPlanqtyRecord的内容%s", wmsPurchaseOrderPlanqtyRecord.toString()));
                    return;
                }
                if (Double.compare(deliveryQty,wmsPurchaseOrderPlanqtyRecord.getDeliveryQty()) == 0 &&
                        Double.compare(supplierDeliveryQty, wmsPurchaseOrderPlanqtyRecord.getSupplierDeliveryQty()) == 0) {
                    return;
                }else{
                    wmsPurchaseOrderPlanqtyRecord.setChangeType(PlanqtyRecordEnum.TYPE_SAP_UPDATE_PLAN.getKey());
                    //计算变更数量：最近一条1或11的需求数量-当前需求数量   eg:100->120
                    if (firstByType1Obj.getDeliveryQty() == null) {
                        logger.info(String.format("------else------firstByType1Obj的id%s", firstByType1Obj.getId()));
                    }
                    if (wmsPurchaseOrderPlanqtyRecord.getDeliveryQty() == null) {
                        logger.info(String.format("------else------wmsPurchaseOrderPlanqtyRecord的内容%s", wmsPurchaseOrderPlanqtyRecord.toString()));
                    }
                    BigDecimal changCount = BigDecimal.valueOf(wmsPurchaseOrderPlanqtyRecord.getDeliveryQty()).subtract(BigDecimal.valueOf(deliveryQty));
                    BigDecimal supplierChangCount = BigDecimal.valueOf(wmsPurchaseOrderPlanqtyRecord.getSupplierDeliveryQty()).subtract(BigDecimal.valueOf(supplierDeliveryQty));
                    BigDecimal planCount = BigDecimal.valueOf(firstObj.getPlanQty()).add(changCount);
                    BigDecimal totalPlanCount = BigDecimal.valueOf(firstObj.getTotalPlanQty()).add(supplierChangCount);
                    wmsPurchaseOrderPlanqtyRecord.setTotalPlanQty(totalPlanCount.doubleValue());
                    wmsPurchaseOrderPlanqtyRecord.setPlanQty(planCount.doubleValue());
                    wmsPurchaseOrderPlanqtyRecord.setBeforePlanQty(firstObj.getPlanQty());
                    wmsPurchaseOrderPlanqtyRecord.setChangePlanQty(changCount.doubleValue());

                    wmsPurchaseOrderPlanqtyRecord.setUpdatedBy("SAP同步或定时拉取时，sap系统修改需求数量数据");
                }
            }else{
                throw new ServiceException("数据错乱，没有找到1或11的数据!");
            }
        }else{
            wmsPurchaseOrderPlanqtyRecord.setTotalPlanQty(wmsPurchaseOrderPlanqtyRecord.getTotalPlanQty());
            wmsPurchaseOrderPlanqtyRecord.setPlanQty(wmsPurchaseOrderPlanqtyRecord.getDeliveryQty());
            wmsPurchaseOrderPlanqtyRecord.setBeforePlanQty(0d);
            wmsPurchaseOrderPlanqtyRecord.setChangePlanQty(wmsPurchaseOrderPlanqtyRecord.getDeliveryQty());
            wmsPurchaseOrderPlanqtyRecord.setUpdatedBy("SAP同步或定时拉取时，sap系统新增需求数量数据");
        }
    }


    @Override
    public void calcReceiveQty(List<PlanQtyRecordDto> receiveInfoList){

        WmsPurchaseOrderPlanqtyRecord record = null;

        for (PlanQtyRecordDto planQtyRecordDto : receiveInfoList){
            record = new WmsPurchaseOrderPlanqtyRecord();
            record.setMaterialNo(planQtyRecordDto.getMaterialNo());
            record.setPurchaseOrderNo(planQtyRecordDto.getPurchaseOrderNo());
            record.setPurchaseLineNo(planQtyRecordDto.getPurchaseLineNo());
            record.setDeliveryQty(Double.valueOf(String.valueOf(planQtyRecordDto.getDeliverQty())));
            record.setSupplierDeliveryQty(Double.valueOf(String.valueOf(planQtyRecordDto.getDeliverQty())));
            record.setChangeType(planQtyRecordDto.getChangeType());
            this.commonSave(record);
        }
    }

    @Override
    public void closeTaskUpdateReceiveQty(List<PurchaseOrderDetail> list){

        // list中接到的madeqty是未收货数量
        WmsPurchaseOrderPlanqtyRecord record;
        for (PurchaseOrderDetail purchaseOrderDetail : list){

//            DeliveryOrderDetailVo deliveryOrderDetailVo = new DeliveryOrderDetailVo();
//            deliveryOrderDetailVo.setPurchaseOrderNo(purchaseOrderDetail.getPurchaseOrderNo());
//            deliveryOrderDetailVo.setPurchaseLineNo(purchaseOrderDetail.getPurchaseLineNo());
//            deliveryOrderDetailVo.setDeliveryOrderId(purchaseOrderDetail.getPurchaseOrderId());
//            deliveryOrderDetailVo.setStatus("0");
//            List<DeliveryOrderDetailVo> deliveryOrderDetailVos = deliveryOrderDetailMapper.selectDeliveryOrderDetailList(deliveryOrderDetailVo);

            WmsPurchaseOrderPlanqtyRecord selectParams = new WmsPurchaseOrderPlanqtyRecord();
            //类型1和2为sapOrderId   其他类型必传purchaseOrderNo  查找所有的历史记录
            selectParams.setPurchaseOrderNo(purchaseOrderDetail.getPurchaseOrderNo());
            selectParams.setMaterialNo(purchaseOrderDetail.getMaterialNo());
            selectParams.setPurchaseLineNo(purchaseOrderDetail.getPurchaseLineNo());
            List<WmsPurchaseOrderPlanqtyRecord> wmsPurchaseOrderPlanqtyRecords = wmsPurchaseOrderPlanqtyRecordMapper.selectWmsPurchaseOrderPlanqtyRecordList(selectParams);
            List<WmsPurchaseOrderPlanqtyRecord> collect4Sap = wmsPurchaseOrderPlanqtyRecords.stream().filter(planQtyObj ->
                    planQtyObj.getChangeType().equals(PlanqtyRecordEnum.TYPE_WMS_UPDATE_RECEIVING.getKey())).collect(Collectors.toList());

            BigDecimal reduce = null;

            // 如果不存在4的，则说明没有修改、关闭
            if(ObjectUtils.isEmpty(collect4Sap)){
                DeliveryOrderDetailVo deliveryOrderDetailVo = new DeliveryOrderDetailVo();
                deliveryOrderDetailVo.setPurchaseOrderNo(purchaseOrderDetail.getPurchaseOrderNo());
                deliveryOrderDetailVo.setPurchaseLineNo(purchaseOrderDetail.getPurchaseLineNo());
//                deliveryOrderDetailVo.setDeliveryOrderId(purchaseOrderDetail.getPurchaseOrderId());
                deliveryOrderDetailVo.setStatus("0");
                List<DeliveryOrderDetailVo> deliveryOrderDetailVos = deliveryOrderDetailMapper.selectDeliveryOrderDetailList(deliveryOrderDetailVo);
                reduce = deliveryOrderDetailVos.stream().map(DeliveryOrderDetailVo::getDeliverQty).reduce(BigDecimal.ZERO, BigDecimal::add);
            } else {
                reduce = new BigDecimal(String.valueOf(collect4Sap.get(0).getDeliveryQty()));
            }
            BigDecimal resultQty = reduce.subtract(purchaseOrderDetail.getMadeQty());

            record = new WmsPurchaseOrderPlanqtyRecord();
            record.setPurchaseOrderNo(purchaseOrderDetail.getPurchaseOrderNo());
            record.setPurchaseLineNo(purchaseOrderDetail.getPurchaseLineNo());
            record.setMaterialNo(purchaseOrderDetail.getMaterialNo());
            record.setDeliveryQty(Double.valueOf(String.valueOf(resultQty)));
            record.setSupplierDeliveryQty(Double.valueOf(String.valueOf(resultQty)));
            record.setChangeType(PlanqtyRecordEnum.TYPE_WMS_UPDATE_RECEIVING.getKey());
            this.commonSave(record);
        }
    }
}
