package com.ruoyi.xkt.mapper;

import com.ruoyi.xkt.domain.StoreProductBarcodeMatch;

import java.util.List;

/**
 * 档口条形码和第三方系统条形码匹配结果Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreProductBarcodeMatchMapper {
    /**
     * 查询档口条形码和第三方系统条形码匹配结果
     *
     * @param storeProdBarcodeMatchId 档口条形码和第三方系统条形码匹配结果主键
     * @return 档口条形码和第三方系统条形码匹配结果
     */
    public StoreProductBarcodeMatch selectStoreProductBarcodeMatchByStoreProdBarcodeMatchId(Long storeProdBarcodeMatchId);

    /**
     * 查询档口条形码和第三方系统条形码匹配结果列表
     *
     * @param storeProductBarcodeMatch 档口条形码和第三方系统条形码匹配结果
     * @return 档口条形码和第三方系统条形码匹配结果集合
     */
    public List<StoreProductBarcodeMatch> selectStoreProductBarcodeMatchList(StoreProductBarcodeMatch storeProductBarcodeMatch);

    /**
     * 新增档口条形码和第三方系统条形码匹配结果
     *
     * @param storeProductBarcodeMatch 档口条形码和第三方系统条形码匹配结果
     * @return 结果
     */
    public int insertStoreProductBarcodeMatch(StoreProductBarcodeMatch storeProductBarcodeMatch);

    /**
     * 修改档口条形码和第三方系统条形码匹配结果
     *
     * @param storeProductBarcodeMatch 档口条形码和第三方系统条形码匹配结果
     * @return 结果
     */
    public int updateStoreProductBarcodeMatch(StoreProductBarcodeMatch storeProductBarcodeMatch);

    /**
     * 删除档口条形码和第三方系统条形码匹配结果
     *
     * @param storeProdBarcodeMatchId 档口条形码和第三方系统条形码匹配结果主键
     * @return 结果
     */
    public int deleteStoreProductBarcodeMatchByStoreProdBarcodeMatchId(Long storeProdBarcodeMatchId);

    /**
     * 批量删除档口条形码和第三方系统条形码匹配结果
     *
     * @param storeProdBarcodeMatchIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoreProductBarcodeMatchByStoreProdBarcodeMatchIds(Long[] storeProdBarcodeMatchIds);
}
