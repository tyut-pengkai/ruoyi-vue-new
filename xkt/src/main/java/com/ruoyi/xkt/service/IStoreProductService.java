package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.StoreProduct;
import com.ruoyi.xkt.dto.storeProduct.*;

import java.util.List;

/**
 * 档口商品Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreProductService {
    /**
     * 查询档口商品
     *
     * @param storeProdId 档口商品主键
     * @return 档口商品
     */
    public StoreProdResDTO selectStoreProductByStoreProdId(Long storeProdId);

    /**
     * 获取档口图片空间
     * @param storeId 档口ID
     * @return StoreProdPicSpaceResDTO
     */
    public StoreProdPicSpaceResDTO getStoreProductPicSpace(Long storeId);

    /**
     * 查询档口商品列表
     *
     * @param storeProduct 档口商品
     * @return 档口商品集合
     */
    public List<StoreProduct> selectStoreProductList(StoreProduct storeProduct);

    public List<StoreProdPageResDTO> selectPage(StoreProdPageDTO pageDTO);


    /**
     * 新增档口商品
     *
     * @param storeProdDTO 档口商品
     * @return 结果
     */
    public int insertStoreProduct(StoreProdDTO storeProdDTO);

    /**
     * 修改档口商品
     *
     * @param storeProdDTO 档口商品
     * @return 结果
     */
    public int updateStoreProduct(Long storeProdId, StoreProdDTO storeProdDTO);

    public void updateStoreProductStatus(StoreProdStatusDTO prodStatusDTO);

    /**
     * 批量删除档口商品
     *
     * @param storeProdIds 需要删除的档口商品主键集合
     * @return 结果
     */
    public int deleteStoreProductByStoreProdIds(Long[] storeProdIds);

    /**
     * 删除档口商品信息
     *
     * @param storeProdId 档口商品主键
     * @return 结果
     */
    public int deleteStoreProductByStoreProdId(Long storeProdId);

    /**
     * 根据档口ID和商品货号模糊查询货号列表
     * @param storeId 档口ID
     * @param prodArtNum 商品货号
     * @return List<StoreProdFuzzyResDTO>
     */
    List<StoreProdFuzzyResDTO> fuzzyQueryList(Long storeId, String prodArtNum);

}
