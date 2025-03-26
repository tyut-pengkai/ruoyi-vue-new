package com.ruoyi.xkt.mapper;

import com.ruoyi.xkt.domain.StoreProduct;

import java.util.List;

/**
 * 档口商品Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreProductMapper {
    /**
     * 查询档口商品
     *
     * @param storeProdId 档口商品主键
     * @return 档口商品
     */
    public StoreProduct selectStoreProductByStoreProdId(Long storeProdId);

    /**
     * 查询档口商品列表
     *
     * @param storeProduct 档口商品
     * @return 档口商品集合
     */
    public List<StoreProduct> selectStoreProductList(StoreProduct storeProduct);

    /**
     * 新增档口商品
     *
     * @param storeProduct 档口商品
     * @return 结果
     */
    public int insertStoreProduct(StoreProduct storeProduct);

    /**
     * 修改档口商品
     *
     * @param storeProduct 档口商品
     * @return 结果
     */
    public int updateStoreProduct(StoreProduct storeProduct);

    /**
     * 删除档口商品
     *
     * @param storeProdId 档口商品主键
     * @return 结果
     */
    public int deleteStoreProductByStoreProdId(Long storeProdId);

    /**
     * 批量删除档口商品
     *
     * @param storeProdIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoreProductByStoreProdIds(Long[] storeProdIds);
}
