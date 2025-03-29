package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreProductDemand;
import com.ruoyi.xkt.mapper.StoreProductDemandMapper;
import com.ruoyi.xkt.service.IStoreProductDemandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 档口商品需求单Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class StoreProductDemandServiceImpl implements IStoreProductDemandService {
    @Autowired
    private StoreProductDemandMapper storeProductDemandMapper;

    /**
     * 查询档口商品需求单
     *
     * @param storeProdDemandId 档口商品需求单主键
     * @return 档口商品需求单
     */
    @Override
    public StoreProductDemand selectStoreProductDemandByStoreProdDemandId(Long storeProdDemandId) {
        return storeProductDemandMapper.selectStoreProductDemandByStoreProdDemandId(storeProdDemandId);
    }

    /**
     * 查询档口商品需求单列表
     *
     * @param storeProductDemand 档口商品需求单
     * @return 档口商品需求单
     */
    @Override
    public List<StoreProductDemand> selectStoreProductDemandList(StoreProductDemand storeProductDemand) {
        return storeProductDemandMapper.selectStoreProductDemandList(storeProductDemand);
    }

    /**
     * 新增档口商品需求单
     *
     * @param storeProductDemand 档口商品需求单
     * @return 结果
     */
    @Override
    public int insertStoreProductDemand(StoreProductDemand storeProductDemand) {
        storeProductDemand.setCreateTime(DateUtils.getNowDate());
        return storeProductDemandMapper.insertStoreProductDemand(storeProductDemand);
    }

    /**
     * 修改档口商品需求单
     *
     * @param storeProductDemand 档口商品需求单
     * @return 结果
     */
    @Override
    @Transactional
    public int updateStoreProductDemand(StoreProductDemand storeProductDemand) {
        storeProductDemand.setUpdateTime(DateUtils.getNowDate());
        return storeProductDemandMapper.updateStoreProductDemand(storeProductDemand);
    }

    /**
     * 批量删除档口商品需求单
     *
     * @param storeProdDemandIds 需要删除的档口商品需求单主键
     * @return 结果
     */
    @Override
    public int deleteStoreProductDemandByStoreProdDemandIds(Long[] storeProdDemandIds) {
        return storeProductDemandMapper.deleteStoreProductDemandByStoreProdDemandIds(storeProdDemandIds);
    }

    /**
     * 删除档口商品需求单信息
     *
     * @param storeProdDemandId 档口商品需求单主键
     * @return 结果
     */
    @Override
    public int deleteStoreProductDemandByStoreProdDemandId(Long storeProdDemandId) {
        return storeProductDemandMapper.deleteStoreProductDemandByStoreProdDemandId(storeProdDemandId);
    }
}
