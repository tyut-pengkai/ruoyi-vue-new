package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.StoreOrderReceive;

import java.util.List;

/**
 * 档口代发订单收件人Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreOrderReceiveService {
    /**
     * 查询档口代发订单收件人
     *
     * @param storeOrderRcvId 档口代发订单收件人主键
     * @return 档口代发订单收件人
     */
    public StoreOrderReceive selectStoreOrderReceiveByStoreOrderRcvId(Long storeOrderRcvId);

    /**
     * 查询档口代发订单收件人列表
     *
     * @param storeOrderReceive 档口代发订单收件人
     * @return 档口代发订单收件人集合
     */
    public List<StoreOrderReceive> selectStoreOrderReceiveList(StoreOrderReceive storeOrderReceive);

    /**
     * 新增档口代发订单收件人
     *
     * @param storeOrderReceive 档口代发订单收件人
     * @return 结果
     */
    public int insertStoreOrderReceive(StoreOrderReceive storeOrderReceive);

    /**
     * 修改档口代发订单收件人
     *
     * @param storeOrderReceive 档口代发订单收件人
     * @return 结果
     */
    public int updateStoreOrderReceive(StoreOrderReceive storeOrderReceive);

    /**
     * 批量删除档口代发订单收件人
     *
     * @param storeOrderRcvIds 需要删除的档口代发订单收件人主键集合
     * @return 结果
     */
    public int deleteStoreOrderReceiveByStoreOrderRcvIds(Long[] storeOrderRcvIds);

    /**
     * 删除档口代发订单收件人信息
     *
     * @param storeOrderRcvId 档口代发订单收件人主键
     * @return 结果
     */
    public int deleteStoreOrderReceiveByStoreOrderRcvId(Long storeOrderRcvId);
}
