package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreOrder;

import java.util.List;

/**
 * 档口代发订单Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreOrderMapper extends BaseMapper<StoreOrder> {
    /**
     * 查询档口代发订单
     *
     * @param storeOrderId 档口代发订单主键
     * @return 档口代发订单
     */
    public StoreOrder selectStoreOrderByStoreOrderId(Long storeOrderId);

    /**
     * 查询档口代发订单列表
     *
     * @param storeOrder 档口代发订单
     * @return 档口代发订单集合
     */
    public List<StoreOrder> selectStoreOrderList(StoreOrder storeOrder);

    /**
     * 新增档口代发订单
     *
     * @param storeOrder 档口代发订单
     * @return 结果
     */
    public int insertStoreOrder(StoreOrder storeOrder);

    /**
     * 修改档口代发订单
     *
     * @param storeOrder 档口代发订单
     * @return 结果
     */
    public int updateStoreOrder(StoreOrder storeOrder);

    /**
     * 删除档口代发订单
     *
     * @param storeOrderId 档口代发订单主键
     * @return 结果
     */
    public int deleteStoreOrderByStoreOrderId(Long storeOrderId);

    /**
     * 批量删除档口代发订单
     *
     * @param storeOrderIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoreOrderByStoreOrderIds(Long[] storeOrderIds);
}
