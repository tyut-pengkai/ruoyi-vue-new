package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.StoreProductColorPrice;

import java.util.List;

/**
 * 档口商品颜色定价Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreProductColorPriceService {
    /**
     * 查询档口商品颜色定价
     *
     * @param storeProdColorPriceId 档口商品颜色定价主键
     * @return 档口商品颜色定价
     */
    public StoreProductColorPrice selectStoreProductColorPriceByStoreProdColorPriceId(Long storeProdColorPriceId);

    /**
     * 查询档口商品颜色定价列表
     *
     * @param storeProductColorPrice 档口商品颜色定价
     * @return 档口商品颜色定价集合
     */
    public List<StoreProductColorPrice> selectStoreProductColorPriceList(StoreProductColorPrice storeProductColorPrice);

    /**
     * 新增档口商品颜色定价
     *
     * @param storeProductColorPrice 档口商品颜色定价
     * @return 结果
     */
    public int insertStoreProductColorPrice(StoreProductColorPrice storeProductColorPrice);

    /**
     * 修改档口商品颜色定价
     *
     * @param storeProductColorPrice 档口商品颜色定价
     * @return 结果
     */
    public int updateStoreProductColorPrice(StoreProductColorPrice storeProductColorPrice);

    /**
     * 批量删除档口商品颜色定价
     *
     * @param storeProdColorPriceIds 需要删除的档口商品颜色定价主键集合
     * @return 结果
     */
    public int deleteStoreProductColorPriceByStoreProdColorPriceIds(Long[] storeProdColorPriceIds);

    /**
     * 删除档口商品颜色定价信息
     *
     * @param storeProdColorPriceId 档口商品颜色定价主键
     * @return 结果
     */
    public int deleteStoreProductColorPriceByStoreProdColorPriceId(Long storeProdColorPriceId);
}
