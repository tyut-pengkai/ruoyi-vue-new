package com.easycode.cloud.service;

import com.easycode.cloud.domain.WmsPurchaseOrderRaw;

import java.util.List;

/**
 * 采购单临时-主Service接口
 *
 * @author weifu
 * @date 2023-02-20
 */
public interface IWmsPurchaseOrderRawService
{
    /**
     * 查询采购单临时-主
     *
     * @param id 采购单临时-主主键
     * @return 采购单临时-主
     */
    public WmsPurchaseOrderRaw selectWmsPurchaseOrderRawById(Long id);

    /**
     * 查询采购单临时-主列表
     *
     * @param wmsPurchaseOrderRaw 采购单临时-主
     * @return 采购单临时-主集合
     */
    public List<WmsPurchaseOrderRaw> selectWmsPurchaseOrderRawList(WmsPurchaseOrderRaw wmsPurchaseOrderRaw);

    /**
     * 新增采购单临时-主
     *
     * @param wmsPurchaseOrderRaw 采购单临时-主
     * @return 结果
     */
    public int insertWmsPurchaseOrderRaw(WmsPurchaseOrderRaw wmsPurchaseOrderRaw);

    /**
     * 修改采购单临时-主
     *
     * @param wmsPurchaseOrderRaw 采购单临时-主
     * @return 结果
     */
    public int updateWmsPurchaseOrderRaw(WmsPurchaseOrderRaw wmsPurchaseOrderRaw);

    /**
     * 批量删除采购单临时-主
     *
     * @param ids 需要删除的采购单临时-主主键集合
     * @return 结果
     */
    public int deleteWmsPurchaseOrderRawByIds(Long[] ids);

    /**
     * 删除采购单临时-主信息
     *
     * @param id 采购单临时-主主键
     * @return 结果
     */
    public int deleteWmsPurchaseOrderRawById(Long id);

    /**
     * 采购单同步订单-自动
     * @param burks
     */
    void syncPurchaseOrder(String burks);

    /**
     * 采购手动同步
     *
     * @param orderNo
     * @return
     * @throws Exception
     */
    void syncPurchaseOrderManual(String orderNo);

    /**
     * 采购订单同步至业务表
     */
    void purOrderBusinessSync();

    /**
     * SAP同步采购协议计划行
     */
    void purOrderScheduleSync(String companyCode);

    /**
     * 采购订单同步至业务表
     */
    void purOrderAsnQtySync(String companyCode);

    /**
     * 同步交货单
     *
     * @param orderNo
     * @return
     * @throws Exception
     */
    void exchangeOrderSync(String orderNo);

}
