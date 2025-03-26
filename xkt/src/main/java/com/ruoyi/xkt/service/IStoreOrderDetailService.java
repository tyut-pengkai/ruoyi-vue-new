package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.StoreOrderDetail;

import java.util.List;

/**
 * 档口代发订单明细Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreOrderDetailService {
    /**
     * 查询档口代发订单明细
     *
     * @param storeOrderDetailId 档口代发订单明细主键
     * @return 档口代发订单明细
     */
    public StoreOrderDetail selectStoreOrderDetailByStoreOrderDetailId(Long storeOrderDetailId);

    /**
     * 查询档口代发订单明细列表
     *
     * @param storeOrderDetail 档口代发订单明细
     * @return 档口代发订单明细集合
     */
    public List<StoreOrderDetail> selectStoreOrderDetailList(StoreOrderDetail storeOrderDetail);

    /**
     * 新增档口代发订单明细
     *
     * @param storeOrderDetail 档口代发订单明细
     * @return 结果
     */
    public int insertStoreOrderDetail(StoreOrderDetail storeOrderDetail);

    /**
     * 修改档口代发订单明细
     *
     * @param storeOrderDetail 档口代发订单明细
     * @return 结果
     */
    public int updateStoreOrderDetail(StoreOrderDetail storeOrderDetail);

    /**
     * 批量删除档口代发订单明细
     *
     * @param storeOrderDetailIds 需要删除的档口代发订单明细主键集合
     * @return 结果
     */
    public int deleteStoreOrderDetailByStoreOrderDetailIds(Long[] storeOrderDetailIds);

    /**
     * 删除档口代发订单明细信息
     *
     * @param storeOrderDetailId 档口代发订单明细主键
     * @return 结果
     */
    public int deleteStoreOrderDetailByStoreOrderDetailId(Long storeOrderDetailId);
}
