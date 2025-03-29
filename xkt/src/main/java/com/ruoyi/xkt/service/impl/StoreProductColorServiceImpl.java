package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreProductColor;
import com.ruoyi.xkt.dto.storeProdColor.StoreProdColorResDTO;
import com.ruoyi.xkt.mapper.StoreProductColorMapper;
import com.ruoyi.xkt.service.IStoreProductColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 档口当前商品颜色Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class StoreProductColorServiceImpl implements IStoreProductColorService {

    final StoreProductColorMapper storeProdColorMapper;

    /**
     * 查询档口当前商品颜色
     *
     * @param storeProdColorId 档口当前商品颜色主键
     * @return 档口当前商品颜色
     */
    @Override
    @Transactional(readOnly = true)
    public StoreProductColor selectStoreProductColorByStoreProdColorId(Long storeProdColorId) {
        return storeProdColorMapper.selectStoreProductColorByStoreProdColorId(storeProdColorId);
    }

    /**
     * 查询档口当前商品颜色列表
     *
     * @param storeProductColor 档口当前商品颜色
     * @return 档口当前商品颜色
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreProductColor> selectStoreProductColorList(StoreProductColor storeProductColor) {
        return storeProdColorMapper.selectStoreProductColorList(storeProductColor);
    }

    /**
     * 新增档口当前商品颜色
     *
     * @param storeProductColor 档口当前商品颜色
     * @return 结果
     */
    @Override
    @Transactional
    public int insertStoreProductColor(StoreProductColor storeProductColor) {
        storeProductColor.setCreateTime(DateUtils.getNowDate());
        return storeProdColorMapper.insertStoreProductColor(storeProductColor);
    }

    /**
     * 修改档口当前商品颜色
     *
     * @param storeProductColor 档口当前商品颜色
     * @return 结果
     */
    @Override
    @Transactional
    public int updateStoreProductColor(StoreProductColor storeProductColor) {
        storeProductColor.setUpdateTime(DateUtils.getNowDate());
        return storeProdColorMapper.updateStoreProductColor(storeProductColor);
    }

    /**
     * 批量删除档口当前商品颜色
     *
     * @param storeProdColorIds 需要删除的档口当前商品颜色主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteStoreProductColorByStoreProdColorIds(Long[] storeProdColorIds) {
        return storeProdColorMapper.deleteStoreProductColorByStoreProdColorIds(storeProdColorIds);
    }

    /**
     * 删除档口当前商品颜色信息
     *
     * @param storeProdColorId 档口当前商品颜色主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteStoreProductColorByStoreProdColorId(Long storeProdColorId) {
        return storeProdColorMapper.deleteStoreProductColorByStoreProdColorId(storeProdColorId);
    }

    /**
     * 根据商店ID和产品款式编号模糊查询颜色列表
     *
     * @param storeId    商店ID，用于限定查询范围
     * @param prodArtNum 产品款式编号，用于模糊匹配产品
     * @return 返回一个列表，包含匹配的产品颜色信息
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreProdColorResDTO> fuzzyQueryColorList(Long storeId, String prodArtNum) {
        return storeProdColorMapper.fuzzyQueryColorList(storeId, prodArtNum);
    }
}
