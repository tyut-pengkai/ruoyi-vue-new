package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreProductColorSize;
import com.ruoyi.xkt.mapper.StoreProductColorSizeMapper;
import com.ruoyi.xkt.service.IStoreProductColorSizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 档口商品颜色的尺码Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class StoreProductColorSizeServiceImpl implements IStoreProductColorSizeService {
    @Autowired
    private StoreProductColorSizeMapper storeProductColorSizeMapper;

    /**
     * 查询档口商品颜色的尺码
     *
     * @param storeProdColorSizeId 档口商品颜色的尺码主键
     * @return 档口商品颜色的尺码
     */
    @Override
    @Transactional(readOnly = true)
    public StoreProductColorSize selectStoreProductColorSizeByStoreProdColorSizeId(Long storeProdColorSizeId) {
        return storeProductColorSizeMapper.selectStoreProductColorSizeByStoreProdColorSizeId(storeProdColorSizeId);
    }

    /**
     * 查询档口商品颜色的尺码列表
     *
     * @param storeProductColorSize 档口商品颜色的尺码
     * @return 档口商品颜色的尺码
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreProductColorSize> selectStoreProductColorSizeList(StoreProductColorSize storeProductColorSize) {
        return storeProductColorSizeMapper.selectStoreProductColorSizeList(storeProductColorSize);
    }

    /**
     * 新增档口商品颜色的尺码
     *
     * @param storeProductColorSize 档口商品颜色的尺码
     * @return 结果
     */
    @Override
    @Transactional
    public int insertStoreProductColorSize(StoreProductColorSize storeProductColorSize) {
        storeProductColorSize.setCreateTime(DateUtils.getNowDate());
        return storeProductColorSizeMapper.insertStoreProductColorSize(storeProductColorSize);
    }

    /**
     * 修改档口商品颜色的尺码
     *
     * @param storeProductColorSize 档口商品颜色的尺码
     * @return 结果
     */
    @Override
    public int updateStoreProductColorSize(StoreProductColorSize storeProductColorSize) {
        storeProductColorSize.setUpdateTime(DateUtils.getNowDate());
        return storeProductColorSizeMapper.updateStoreProductColorSize(storeProductColorSize);
    }

    /**
     * 批量删除档口商品颜色的尺码
     *
     * @param storeProdColorSizeIds 需要删除的档口商品颜色的尺码主键
     * @return 结果
     */
    @Override
    public int deleteStoreProductColorSizeByStoreProdColorSizeIds(Long[] storeProdColorSizeIds) {
        return storeProductColorSizeMapper.deleteStoreProductColorSizeByStoreProdColorSizeIds(storeProdColorSizeIds);
    }

    /**
     * 删除档口商品颜色的尺码信息
     *
     * @param storeProdColorSizeId 档口商品颜色的尺码主键
     * @return 结果
     */
    @Override
    public int deleteStoreProductColorSizeByStoreProdColorSizeId(Long storeProdColorSizeId) {
        return storeProductColorSizeMapper.deleteStoreProductColorSizeByStoreProdColorSizeId(storeProdColorSizeId);
    }
}
