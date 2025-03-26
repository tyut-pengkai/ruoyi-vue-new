package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.StoreOrderExpress;

import java.util.List;

/**
 * 档口代发订单快递Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreOrderExpressService {
    /**
     * 查询档口代发订单快递
     *
     * @param storeOrderExprId 档口代发订单快递主键
     * @return 档口代发订单快递
     */
    public StoreOrderExpress selectStoreOrderExpressByStoreOrderExprId(Long storeOrderExprId);

    /**
     * 查询档口代发订单快递列表
     *
     * @param storeOrderExpress 档口代发订单快递
     * @return 档口代发订单快递集合
     */
    public List<StoreOrderExpress> selectStoreOrderExpressList(StoreOrderExpress storeOrderExpress);

    /**
     * 新增档口代发订单快递
     *
     * @param storeOrderExpress 档口代发订单快递
     * @return 结果
     */
    public int insertStoreOrderExpress(StoreOrderExpress storeOrderExpress);

    /**
     * 修改档口代发订单快递
     *
     * @param storeOrderExpress 档口代发订单快递
     * @return 结果
     */
    public int updateStoreOrderExpress(StoreOrderExpress storeOrderExpress);

    /**
     * 批量删除档口代发订单快递
     *
     * @param storeOrderExprIds 需要删除的档口代发订单快递主键集合
     * @return 结果
     */
    public int deleteStoreOrderExpressByStoreOrderExprIds(Long[] storeOrderExprIds);

    /**
     * 删除档口代发订单快递信息
     *
     * @param storeOrderExprId 档口代发订单快递主键
     * @return 结果
     */
    public int deleteStoreOrderExpressByStoreOrderExprId(Long storeOrderExprId);
}
