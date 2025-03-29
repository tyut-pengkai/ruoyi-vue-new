package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreProductStock;
import com.ruoyi.xkt.mapper.StoreProductStockMapper;
import com.ruoyi.xkt.service.IStoreProductStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 档口商品库存Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class StoreProductStockServiceImpl implements IStoreProductStockService {
    @Autowired
    private StoreProductStockMapper storeProductStockMapper;

    /**
     * 查询档口商品库存
     *
     * @param storeProdStockId 档口商品库存主键
     * @return 档口商品库存
     */
    @Override
    @Transactional(readOnly = true)
    public StoreProductStock selectStoreProductStockByStoreProdStockId(Long storeProdStockId) {
        return storeProductStockMapper.selectStoreProductStockByStoreProdStockId(storeProdStockId);
    }

    /**
     * 查询档口商品库存列表
     *
     * @param storeProductStock 档口商品库存
     * @return 档口商品库存
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreProductStock> selectStoreProductStockList(StoreProductStock storeProductStock) {
        return storeProductStockMapper.selectStoreProductStockList(storeProductStock);
    }

    /**
     * 新增档口商品库存
     *
     * @param storeProductStock 档口商品库存
     * @return 结果
     */
    @Override
    @Transactional
    public int insertStoreProductStock(StoreProductStock storeProductStock) {
        storeProductStock.setCreateTime(DateUtils.getNowDate());
        return storeProductStockMapper.insertStoreProductStock(storeProductStock);
    }

    /**
     * 修改档口商品库存
     *
     * @param storeProductStock 档口商品库存
     * @return 结果
     */
    @Override
    @Transactional
    public int updateStoreProductStock(StoreProductStock storeProductStock) {
        storeProductStock.setUpdateTime(DateUtils.getNowDate());
        return storeProductStockMapper.updateStoreProductStock(storeProductStock);
    }

    /**
     * 批量删除档口商品库存
     *
     * @param storeProdStockIds 需要删除的档口商品库存主键
     * @return 结果
     */
    @Override
    public int deleteStoreProductStockByStoreProdStockIds(Long[] storeProdStockIds) {
        return storeProductStockMapper.deleteStoreProductStockByStoreProdStockIds(storeProdStockIds);
    }

    /**
     * 删除档口商品库存信息
     *
     * @param storeProdStockId 档口商品库存主键
     * @return 结果
     */
    @Override
    public int deleteStoreProductStockByStoreProdStockId(Long storeProdStockId) {
        return storeProductStockMapper.deleteStoreProductStockByStoreProdStockId(storeProdStockId);
    }
}
