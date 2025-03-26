package com.ruoyi.xkt.mapper;

import com.ruoyi.xkt.domain.StoreOrderReceive;

import java.util.List;

/**
 * 档口代发订单收件人Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreOrderReceiveMapper {
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
     * 删除档口代发订单收件人
     *
     * @param storeOrderRcvId 档口代发订单收件人主键
     * @return 结果
     */
    public int deleteStoreOrderReceiveByStoreOrderRcvId(Long storeOrderRcvId);

    /**
     * 批量删除档口代发订单收件人
     *
     * @param storeOrderRcvIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoreOrderReceiveByStoreOrderRcvIds(Long[] storeOrderRcvIds);
}
