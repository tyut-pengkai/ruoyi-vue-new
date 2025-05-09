package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.domain.StoreProduct;
import com.ruoyi.xkt.dto.storeProduct.*;

import java.io.IOException;
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
    StoreProdResDTO selectStoreProductByStoreProdId(Long storeProdId);

    /**
     * 获取档口图片空间
     *
     * @param storeId 档口ID
     * @return StoreProdPicSpaceResDTO
     */
    StoreProdPicSpaceResDTO getStoreProductPicSpace(Long storeId);

    /**
     * 查询档口商品列表
     *
     * @param storeProduct 档口商品
     * @return 档口商品集合
     */
    List<StoreProduct> selectStoreProductList(StoreProduct storeProduct);

    Page<StoreProdPageResDTO> page(StoreProdPageDTO pageDTO);


    /**
     * 新增档口商品
     *
     * @param storeProdDTO 档口商品
     * @return 结果
     */
    int insertStoreProduct(StoreProdDTO storeProdDTO) throws IOException;

    /**
     * 修改档口商品
     *
     * @param storeProdDTO 档口商品
     * @return 结果
     */
    public int updateStoreProduct(Long storeProdId, StoreProdDTO storeProdDTO) throws IOException;

    /**
     * 更新档口商品状态
     *
     * @param prodStatusDTO 更新状态入参
     */
    public void updateStoreProductStatus(StoreProdStatusDTO prodStatusDTO) throws IOException;

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
     *
     * @param storeId    档口ID
     * @param prodArtNum 商品货号
     * @return List<StoreProdFuzzyResDTO>
     */
    List<StoreProdFuzzyResDTO> fuzzyQueryList(Long storeId, String prodArtNum);

    /**
     * 根据档口ID和商品货号模糊查询货号列表，返回数据带上商品主图
     *
     * @param storeId    档口ID
     * @param prodArtNum 商品货号
     * @return List<StoreProdFuzzyResPicDTO>
     */
    List<StoreProdFuzzyResPicDTO> fuzzyQueryResPicList(Long storeId, String prodArtNum);

    /**
     * 获取商品所有的风格
     *
     * @return
     */
    List<String> getStyleList();

    /**
     * APP获取档口商品详情
     *
     * @param storeProdId 档口商品ID
     * @return StoreProdAppResDTO
     */
    StoreProdAppResDTO getAppInfo(Long storeProdId);

    /**
     * 获取档口商品颜色及sku等
     *
     * @param storeProdId 档口商品ID
     * @return StoreProdSkuResDTO
     */
    StoreProdSkuResDTO getSkuList(Long storeProdId);

    Integer update111(Long storeProdId);
}
