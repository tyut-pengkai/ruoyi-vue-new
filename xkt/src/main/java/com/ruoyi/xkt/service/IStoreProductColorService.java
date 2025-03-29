package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.StoreProductColor;
import com.ruoyi.xkt.dto.storeProdColor.StoreProdColorResDTO;

import java.util.List;

/**
 * 档口当前商品颜色Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreProductColorService {
    /**
     * 查询档口当前商品颜色
     *
     * @param storeProdColorId 档口当前商品颜色主键
     * @return 档口当前商品颜色
     */
    public StoreProductColor selectStoreProductColorByStoreProdColorId(Long storeProdColorId);

    /**
     * 查询档口当前商品颜色列表
     *
     * @param storeProductColor 档口当前商品颜色
     * @return 档口当前商品颜色集合
     */
    public List<StoreProductColor> selectStoreProductColorList(StoreProductColor storeProductColor);

    /**
     * 新增档口当前商品颜色
     *
     * @param storeProductColor 档口当前商品颜色
     * @return 结果
     */
    public int insertStoreProductColor(StoreProductColor storeProductColor);

    /**
     * 修改档口当前商品颜色
     *
     * @param storeProductColor 档口当前商品颜色
     * @return 结果
     */
    public int updateStoreProductColor(StoreProductColor storeProductColor);

    /**
     * 批量删除档口当前商品颜色
     *
     * @param storeProdColorIds 需要删除的档口当前商品颜色主键集合
     * @return 结果
     */
    public int deleteStoreProductColorByStoreProdColorIds(Long[] storeProdColorIds);

    /**
     * 删除档口当前商品颜色信息
     *
     * @param storeProdColorId 档口当前商品颜色主键
     * @return 结果
     */
    public int deleteStoreProductColorByStoreProdColorId(Long storeProdColorId);

    /**
     * 根据商店ID和产品款式编号模糊查询颜色列表
     *
     * @param storeId 商店ID，用于限定查询范围
     * @param prodArtNum 产品款式编号，用于模糊匹配产品
     * @return 返回一个列表，包含匹配的产品颜色信息
     */
    List<StoreProdColorResDTO> fuzzyQueryColorList(Long storeId, String prodArtNum);
}
