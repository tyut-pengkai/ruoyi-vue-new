package com.ruoyi.xkt.mapper;

import com.ruoyi.xkt.domain.StoreProductDemandDetail;

import java.util.List;

/**
 * 档口商品需求单明细Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreProductDemandDetailMapper {
    /**
     * 查询档口商品需求单明细
     *
     * @param storeProdDemaDetailId 档口商品需求单明细主键
     * @return 档口商品需求单明细
     */
    public StoreProductDemandDetail selectStoreProductDemandDetailByStoreProdDemaDetailId(Long storeProdDemaDetailId);

    /**
     * 查询档口商品需求单明细列表
     *
     * @param storeProductDemandDetail 档口商品需求单明细
     * @return 档口商品需求单明细集合
     */
    public List<StoreProductDemandDetail> selectStoreProductDemandDetailList(StoreProductDemandDetail storeProductDemandDetail);

    /**
     * 新增档口商品需求单明细
     *
     * @param storeProductDemandDetail 档口商品需求单明细
     * @return 结果
     */
    public int insertStoreProductDemandDetail(StoreProductDemandDetail storeProductDemandDetail);

    /**
     * 修改档口商品需求单明细
     *
     * @param storeProductDemandDetail 档口商品需求单明细
     * @return 结果
     */
    public int updateStoreProductDemandDetail(StoreProductDemandDetail storeProductDemandDetail);

    /**
     * 删除档口商品需求单明细
     *
     * @param storeProdDemaDetailId 档口商品需求单明细主键
     * @return 结果
     */
    public int deleteStoreProductDemandDetailByStoreProdDemaDetailId(Long storeProdDemaDetailId);

    /**
     * 批量删除档口商品需求单明细
     *
     * @param storeProdDemaDetailIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoreProductDemandDetailByStoreProdDemaDetailIds(Long[] storeProdDemaDetailIds);
}
