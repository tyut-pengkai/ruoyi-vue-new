package com.easycode.cloud.mapper;

import com.easycode.cloud.domain.WmsPurchaseOrderPlanqtyRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 采购单据需求数量变更记录Mapper接口
 *
 * @author weifu
 * @date 2024-06-13
 */
@Repository
public interface WmsPurchaseOrderPlanqtyRecordMapper
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
     * 删除采购单据需求数量变更记录
     *
     * @param id 采购单据需求数量变更记录主键
     * @return 结果
     */
    public int deleteWmsPurchaseOrderPlanqtyRecordById(Long id);

    /**
     * 批量删除采购单据需求数量变更记录
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWmsPurchaseOrderPlanqtyRecordByIds(Long[] ids);

    public List<WmsPurchaseOrderPlanqtyRecord> queryDemandQtyList(List<String> list);
}
