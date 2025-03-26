package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreOrder;
import com.ruoyi.xkt.mapper.StoreOrderMapper;
import com.ruoyi.xkt.service.IStoreOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 档口代发订单Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class StoreOrderServiceImpl implements IStoreOrderService {
    @Autowired
    private StoreOrderMapper storeOrderMapper;

    /**
     * 查询档口代发订单
     *
     * @param storeOrderId 档口代发订单主键
     * @return 档口代发订单
     */
    @Override
    public StoreOrder selectStoreOrderByStoreOrderId(Long storeOrderId) {
        return storeOrderMapper.selectStoreOrderByStoreOrderId(storeOrderId);
    }

    /**
     * 查询档口代发订单列表
     *
     * @param storeOrder 档口代发订单
     * @return 档口代发订单
     */
    @Override
    public List<StoreOrder> selectStoreOrderList(StoreOrder storeOrder) {
        return storeOrderMapper.selectStoreOrderList(storeOrder);
    }

    /**
     * 新增档口代发订单
     *
     * @param storeOrder 档口代发订单
     * @return 结果
     */
    @Override
    public int insertStoreOrder(StoreOrder storeOrder) {
        storeOrder.setCreateTime(DateUtils.getNowDate());
        return storeOrderMapper.insertStoreOrder(storeOrder);
    }

    /**
     * 修改档口代发订单
     *
     * @param storeOrder 档口代发订单
     * @return 结果
     */
    @Override
    public int updateStoreOrder(StoreOrder storeOrder) {
        storeOrder.setUpdateTime(DateUtils.getNowDate());
        return storeOrderMapper.updateStoreOrder(storeOrder);
    }

    /**
     * 批量删除档口代发订单
     *
     * @param storeOrderIds 需要删除的档口代发订单主键
     * @return 结果
     */
    @Override
    public int deleteStoreOrderByStoreOrderIds(Long[] storeOrderIds) {
        return storeOrderMapper.deleteStoreOrderByStoreOrderIds(storeOrderIds);
    }

    /**
     * 删除档口代发订单信息
     *
     * @param storeOrderId 档口代发订单主键
     * @return 结果
     */
    @Override
    public int deleteStoreOrderByStoreOrderId(Long storeOrderId) {
        return storeOrderMapper.deleteStoreOrderByStoreOrderId(storeOrderId);
    }
}
