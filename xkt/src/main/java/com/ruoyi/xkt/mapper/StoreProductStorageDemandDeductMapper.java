package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreProductStorageDemandDeduct;

import java.util.List;

/**
 * 档口商品入库抵扣需求Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreProductStorageDemandDeductMapper extends BaseMapper<StoreProductStorageDemandDeduct> {
    /**
     * 查询档口商品入库抵扣需求
     *
     * @param id 档口商品入库抵扣需求主键
     * @return 档口商品入库抵扣需求
     */
    public StoreProductStorageDemandDeduct selectStoreProductStorageDemandDeducteByStoreProdStorDemaDeducteId(Long id);

    /**
     * 查询档口商品入库抵扣需求列表
     *
     * @param storeProductStorageDemandDeducte 档口商品入库抵扣需求
     * @return 档口商品入库抵扣需求集合
     */
    public List<StoreProductStorageDemandDeduct> selectStoreProductStorageDemandDeducteList(StoreProductStorageDemandDeduct storeProductStorageDemandDeducte);

    /**
     * 新增档口商品入库抵扣需求
     *
     * @param storeProductStorageDemandDeducte 档口商品入库抵扣需求
     * @return 结果
     */
    public int insertStoreProductStorageDemandDeducte(StoreProductStorageDemandDeduct storeProductStorageDemandDeducte);

    /**
     * 修改档口商品入库抵扣需求
     *
     * @param storeProductStorageDemandDeducte 档口商品入库抵扣需求
     * @return 结果
     */
    public int updateStoreProductStorageDemandDeducte(StoreProductStorageDemandDeduct storeProductStorageDemandDeducte);

    /**
     * 删除档口商品入库抵扣需求
     *
     * @param id 档口商品入库抵扣需求主键
     * @return 结果
     */
    public int deleteStoreProductStorageDemandDeducteByStoreProdStorDemaDeducteId(Long id);

    /**
     * 批量删除档口商品入库抵扣需求
     *
     * @param storeProdStorDemaDeducteIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoreProductStorageDemandDeducteByStoreProdStorDemaDeducteIds(Long[] storeProdStorDemaDeducteIds);
}
