package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.StoreProductBarcodeMatch;

import java.util.List;

/**
 * 档口条形码和第三方系统条形码匹配结果Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreProductBarcodeMatchService {
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
     * 批量删除档口条形码和第三方系统条形码匹配结果
     *
     * @param storeProdBarcodeMatchIds 需要删除的档口条形码和第三方系统条形码匹配结果主键集合
     * @return 结果
     */
    public int deleteStoreProductBarcodeMatchByStoreProdBarcodeMatchIds(Long[] storeProdBarcodeMatchIds);

    /**
     * 删除档口条形码和第三方系统条形码匹配结果信息
     *
     * @param storeProdBarcodeMatchId 档口条形码和第三方系统条形码匹配结果主键
     * @return 结果
     */
    public int deleteStoreProductBarcodeMatchByStoreProdBarcodeMatchId(Long storeProdBarcodeMatchId);
}
