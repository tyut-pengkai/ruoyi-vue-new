package com.easycode.cloud.service;

import com.easycode.cloud.domain.dto.PurchaseOrderDetailPlanDto;
import com.weifu.cloud.domain.PurchaseOrderDetailPlan;

import java.util.List;

/**
 * 采购协议计划行Service接口
 * 
 * @author bcp
 * @date 2023-09-24
 */
public interface IPurchaseOrderDetailPlanService 
{
    /**
     * 查询采购协议计划行
     * 
     * @param id 采购协议计划行主键
     * @return 采购协议计划行
     */
    public PurchaseOrderDetailPlan selectPurchaseOrderDetailPlanById(Long id);

    /**
     * 查询采购协议计划行列表
     * 
     * @param purchaseOrderDetailPlan 采购协议计划行
     * @return 采购协议计划行集合
     */
    public List<PurchaseOrderDetailPlan> selectPurchaseOrderDetailPlanList(PurchaseOrderDetailPlan purchaseOrderDetailPlan);

    /**
     * 新增采购协议计划行
     * 
     * @param purchaseOrderDetailPlan 采购协议计划行
     * @return 结果
     */
    public int insertPurchaseOrderDetailPlan(PurchaseOrderDetailPlan purchaseOrderDetailPlan);

    /**
     * 修改采购协议计划行
     * 
     * @param purchaseOrderDetailPlan 采购协议计划行
     * @return 结果
     */
    public int updatePurchaseOrderDetailPlan(PurchaseOrderDetailPlan purchaseOrderDetailPlan);

    /**
     * 批量删除采购协议计划行
     * 
     * @param ids 需要删除的采购协议计划行主键集合
     * @return 结果
     */
    public int deletePurchaseOrderDetailPlanByIds(Long[] ids);

    /**
     * 删除采购协议计划行信息
     * 
     * @param id 采购协议计划行主键
     * @return 结果
     */
    public int deletePurchaseOrderDetailPlanById(Long id);

    /**
     * 采购单处理页面 查询采购单计划行
     * @param purchaseOrderDetailPlanDto 采购协议计划行
     * @return 结果
     */
    List<PurchaseOrderDetailPlan> queryPurchaseOrderDetailPlan(PurchaseOrderDetailPlanDto purchaseOrderDetailPlanDto);
}
