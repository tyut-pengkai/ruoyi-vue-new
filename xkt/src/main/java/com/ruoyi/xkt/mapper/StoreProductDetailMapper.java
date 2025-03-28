package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreProductDetail;

import java.util.List;

/**
 * 档口商品详情内容Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreProductDetailMapper extends BaseMapper<StoreProductDetail> {
    /**
     * 查询档口商品详情内容
     *
     * @param storeProdDetailId 档口商品详情内容主键
     * @return 档口商品详情内容
     */
    public StoreProductDetail selectStoreProductDetailByStoreProdDetailId(Long storeProdDetailId);

    /**
     * 查询档口商品详情内容列表
     *
     * @param storeProductDetail 档口商品详情内容
     * @return 档口商品详情内容集合
     */
    public List<StoreProductDetail> selectStoreProductDetailList(StoreProductDetail storeProductDetail);

    /**
     * 新增档口商品详情内容
     *
     * @param storeProductDetail 档口商品详情内容
     * @return 结果
     */
    public int insertStoreProductDetail(StoreProductDetail storeProductDetail);

    /**
     * 修改档口商品详情内容
     *
     * @param storeProductDetail 档口商品详情内容
     * @return 结果
     */
    public int updateStoreProductDetail(StoreProductDetail storeProductDetail);

    /**
     * 删除档口商品详情内容
     *
     * @param storeProdDetailId 档口商品详情内容主键
     * @return 结果
     */
    public int deleteStoreProductDetailByStoreProdDetailId(Long storeProdDetailId);

    /**
     * 批量删除档口商品详情内容
     *
     * @param storeProdDetailIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoreProductDetailByStoreProdDetailIds(Long[] storeProdDetailIds);

    void updateDelFlagByStoreProdId(Long storeProdId);

    StoreProductDetail selectByStoreProdId(Long storeProdId);

}
