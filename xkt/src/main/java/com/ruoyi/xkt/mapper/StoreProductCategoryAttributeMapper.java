package com.ruoyi.xkt.mapper;

import com.ruoyi.xkt.domain.StoreProductCategoryAttribute;

import java.util.List;

/**
 * 档口商品类目信息Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreProductCategoryAttributeMapper {
    /**
     * 查询档口商品类目信息
     *
     * @param storeProdAttrId 档口商品类目信息主键
     * @return 档口商品类目信息
     */
    public StoreProductCategoryAttribute selectStoreProductCategoryAttributeByStoreProdAttrId(Long storeProdAttrId);

    /**
     * 查询档口商品类目信息列表
     *
     * @param storeProductCategoryAttribute 档口商品类目信息
     * @return 档口商品类目信息集合
     */
    public List<StoreProductCategoryAttribute> selectStoreProductCategoryAttributeList(StoreProductCategoryAttribute storeProductCategoryAttribute);

    /**
     * 新增档口商品类目信息
     *
     * @param storeProductCategoryAttribute 档口商品类目信息
     * @return 结果
     */
    public int insertStoreProductCategoryAttribute(StoreProductCategoryAttribute storeProductCategoryAttribute);

    /**
     * 修改档口商品类目信息
     *
     * @param storeProductCategoryAttribute 档口商品类目信息
     * @return 结果
     */
    public int updateStoreProductCategoryAttribute(StoreProductCategoryAttribute storeProductCategoryAttribute);

    /**
     * 删除档口商品类目信息
     *
     * @param storeProdAttrId 档口商品类目信息主键
     * @return 结果
     */
    public int deleteStoreProductCategoryAttributeByStoreProdAttrId(Long storeProdAttrId);

    /**
     * 批量删除档口商品类目信息
     *
     * @param storeProdAttrIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoreProductCategoryAttributeByStoreProdAttrIds(Long[] storeProdAttrIds);
}
