package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreProductColorPrice;
import com.ruoyi.xkt.mapper.StoreProductColorPriceMapper;
import com.ruoyi.xkt.service.IStoreProductColorPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 档口商品颜色定价Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class StoreProductColorPriceServiceImpl implements IStoreProductColorPriceService {
    @Autowired
    private StoreProductColorPriceMapper storeProductColorPriceMapper;

    /**
     * 查询档口商品颜色定价
     *
     * @param storeProdColorPriceId 档口商品颜色定价主键
     * @return 档口商品颜色定价
     */
    @Override
    public StoreProductColorPrice selectStoreProductColorPriceByStoreProdColorPriceId(Long storeProdColorPriceId) {
        return storeProductColorPriceMapper.selectStoreProductColorPriceByStoreProdColorPriceId(storeProdColorPriceId);
    }

    /**
     * 查询档口商品颜色定价列表
     *
     * @param storeProductColorPrice 档口商品颜色定价
     * @return 档口商品颜色定价
     */
    @Override
    public List<StoreProductColorPrice> selectStoreProductColorPriceList(StoreProductColorPrice storeProductColorPrice) {
        return storeProductColorPriceMapper.selectStoreProductColorPriceList(storeProductColorPrice);
    }

    /**
     * 新增档口商品颜色定价
     *
     * @param storeProductColorPrice 档口商品颜色定价
     * @return 结果
     */
    @Override
    @Transactional
    public int insertStoreProductColorPrice(StoreProductColorPrice storeProductColorPrice) {
        storeProductColorPrice.setCreateTime(DateUtils.getNowDate());
        return storeProductColorPriceMapper.insertStoreProductColorPrice(storeProductColorPrice);
    }

    /**
     * 修改档口商品颜色定价
     *
     * @param storeProductColorPrice 档口商品颜色定价
     * @return 结果
     */
    @Override
    @Transactional
    public int updateStoreProductColorPrice(StoreProductColorPrice storeProductColorPrice) {
        storeProductColorPrice.setUpdateTime(DateUtils.getNowDate());
        return storeProductColorPriceMapper.updateStoreProductColorPrice(storeProductColorPrice);
    }

    /**
     * 批量删除档口商品颜色定价
     *
     * @param storeProdColorPriceIds 需要删除的档口商品颜色定价主键
     * @return 结果
     */
    @Override
    public int deleteStoreProductColorPriceByStoreProdColorPriceIds(Long[] storeProdColorPriceIds) {
        return storeProductColorPriceMapper.deleteStoreProductColorPriceByStoreProdColorPriceIds(storeProdColorPriceIds);
    }

    /**
     * 删除档口商品颜色定价信息
     *
     * @param storeProdColorPriceId 档口商品颜色定价主键
     * @return 结果
     */
    @Override
    public int deleteStoreProductColorPriceByStoreProdColorPriceId(Long storeProdColorPriceId) {
        return storeProductColorPriceMapper.deleteStoreProductColorPriceByStoreProdColorPriceId(storeProdColorPriceId);
    }
}
