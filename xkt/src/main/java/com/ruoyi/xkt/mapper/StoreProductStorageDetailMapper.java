package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreProductStorageDetail;

import java.util.List;

/**
 * 档口商品入库明细Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreProductStorageDetailMapper extends BaseMapper<StoreProductStorageDetail> {
    /**
     * 查询档口商品入库明细
     *
     * @param id 档口商品入库明细主键
     * @return 档口商品入库明细
     */
    public StoreProductStorageDetail selectStoreProductStorageDetailByStoreProdStorDetailId(Long id);

    /**
     * 查询档口商品入库明细列表
     *
     * @param storeProductStorageDetail 档口商品入库明细
     * @return 档口商品入库明细集合
     */
    public List<StoreProductStorageDetail> selectStoreProductStorageDetailList(StoreProductStorageDetail storeProductStorageDetail);

    /**
     * 新增档口商品入库明细
     *
     * @param storeProductStorageDetail 档口商品入库明细
     * @return 结果
     */
    public int insertStoreProductStorageDetail(StoreProductStorageDetail storeProductStorageDetail);

    /**
     * 修改档口商品入库明细
     *
     * @param storeProductStorageDetail 档口商品入库明细
     * @return 结果
     */
    public int updateStoreProductStorageDetail(StoreProductStorageDetail storeProductStorageDetail);

    /**
     * 删除档口商品入库明细
     *
     * @param id 档口商品入库明细主键
     * @return 结果
     */
    public int deleteStoreProductStorageDetailByStoreProdStorDetailId(Long id);

    /**
     * 批量删除档口商品入库明细
     *
     * @param storeProdStorDetailIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoreProductStorageDetailByStoreProdStorDetailIds(Long[] storeProdStorDetailIds);
}
