package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreProductColor;
import com.ruoyi.xkt.mapper.StoreProductColorMapper;
import com.ruoyi.xkt.service.IStoreProductColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 档口当前商品颜色Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class StoreProductColorServiceImpl implements IStoreProductColorService {
    @Autowired
    private StoreProductColorMapper storeProductColorMapper;

    /**
     * 查询档口当前商品颜色
     *
     * @param storeProdColorId 档口当前商品颜色主键
     * @return 档口当前商品颜色
     */
    @Override
    public StoreProductColor selectStoreProductColorByStoreProdColorId(Long storeProdColorId) {
        return storeProductColorMapper.selectStoreProductColorByStoreProdColorId(storeProdColorId);
    }

    /**
     * 查询档口当前商品颜色列表
     *
     * @param storeProductColor 档口当前商品颜色
     * @return 档口当前商品颜色
     */
    @Override
    public List<StoreProductColor> selectStoreProductColorList(StoreProductColor storeProductColor) {
        return storeProductColorMapper.selectStoreProductColorList(storeProductColor);
    }

    /**
     * 新增档口当前商品颜色
     *
     * @param storeProductColor 档口当前商品颜色
     * @return 结果
     */
    @Override
    public int insertStoreProductColor(StoreProductColor storeProductColor) {
        storeProductColor.setCreateTime(DateUtils.getNowDate());
        return storeProductColorMapper.insertStoreProductColor(storeProductColor);
    }

    /**
     * 修改档口当前商品颜色
     *
     * @param storeProductColor 档口当前商品颜色
     * @return 结果
     */
    @Override
    public int updateStoreProductColor(StoreProductColor storeProductColor) {
        storeProductColor.setUpdateTime(DateUtils.getNowDate());
        return storeProductColorMapper.updateStoreProductColor(storeProductColor);
    }

    /**
     * 批量删除档口当前商品颜色
     *
     * @param storeProdColorIds 需要删除的档口当前商品颜色主键
     * @return 结果
     */
    @Override
    public int deleteStoreProductColorByStoreProdColorIds(Long[] storeProdColorIds) {
        return storeProductColorMapper.deleteStoreProductColorByStoreProdColorIds(storeProdColorIds);
    }

    /**
     * 删除档口当前商品颜色信息
     *
     * @param storeProdColorId 档口当前商品颜色主键
     * @return 结果
     */
    @Override
    public int deleteStoreProductColorByStoreProdColorId(Long storeProdColorId) {
        return storeProductColorMapper.deleteStoreProductColorByStoreProdColorId(storeProdColorId);
    }
}
