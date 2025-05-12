package com.easycode.cloud.service;

import com.easycode.cloud.domain.WmsPurchaseOrderPlanqtyRecord;
import com.easycode.cloud.domain.dto.PlanQtyRecordDto;
import com.weifu.cloud.domain.PurchaseOrderDetail;

import java.util.List;

/**
 * 采购单据需求数量变更记录Service接口
 *
 * @author weifu
 * @date 2024-06-13
 */
public interface IWmsPurchaseOrderPlanqtyRecordService
{
    /**
     * 查询采购单据需求数量变更记录
     *
     * @param id 采购单据需求数量变更记录主键
     * @return 采购单据需求数量变更记录
     */
    public WmsPurchaseOrderPlanqtyRecord selectWmsPurchaseOrderPlanqtyRecordById(Long id);

    /**
     * 查询采购单据需求数量变更记录列表
     *
     * @param wmsPurchaseOrderPlanqtyRecord 采购单据需求数量变更记录
     * @return 采购单据需求数量变更记录集合
     */
    public List<WmsPurchaseOrderPlanqtyRecord> selectWmsPurchaseOrderPlanqtyRecordList(WmsPurchaseOrderPlanqtyRecord wmsPurchaseOrderPlanqtyRecord);

    /**
     * 查询采购单据需求数量变更记录列表
     *
     * @param list 采购单号list
     * @return 采购单据需求数量变更记录集合
     */
    public List<WmsPurchaseOrderPlanqtyRecord> queryDemandQtyList(List<String> list);

    /**
     * 新增采购单据需求数量变更记录
     *
     * @param wmsPurchaseOrderPlanqtyRecord 采购单据需求数量变更记录
     * @return 结果
     */
    public int insertWmsPurchaseOrderPlanqtyRecord(WmsPurchaseOrderPlanqtyRecord wmsPurchaseOrderPlanqtyRecord);

    /**
     * 修改采购单据需求数量变更记录
     *
     * @param wmsPurchaseOrderPlanqtyRecord 采购单据需求数量变更记录
     * @return 结果
     */
    public int updateWmsPurchaseOrderPlanqtyRecord(WmsPurchaseOrderPlanqtyRecord wmsPurchaseOrderPlanqtyRecord);

    /**
     * 批量删除采购单据需求数量变更记录
     *
     * @param ids 需要删除的采购单据需求数量变更记录主键集合
     * @return 结果
     */
    public int deleteWmsPurchaseOrderPlanqtyRecordByIds(Long[] ids);

    /**
     * 删除采购单据需求数量变更记录信息
     *
     * @param id 采购单据需求数量变更记录主键
     * @return 结果
     */
    public int deleteWmsPurchaseOrderPlanqtyRecordById(Long id);


    /**
     * 共通方法-新增数据
     *
     * @param wmsPurchaseOrderPlanqtyRecord 实例对象
     * @return 实例对象
     */
    WmsPurchaseOrderPlanqtyRecord commonSave(WmsPurchaseOrderPlanqtyRecord wmsPurchaseOrderPlanqtyRecord);

    /**
     * 计算收货数量并调用保存方法
     *
     * @param receiveInfoList 实例对象
     */
    void calcReceiveQty(List<PlanQtyRecordDto> receiveInfoList);

    void closeTaskUpdateReceiveQty(List<PurchaseOrderDetail> list);
}
