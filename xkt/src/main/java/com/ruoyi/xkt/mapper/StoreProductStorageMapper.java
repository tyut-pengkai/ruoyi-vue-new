package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreProductStorage;

import java.util.List;

/**
 * 档口商品入库Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreProductStorageMapper extends BaseMapper<StoreProductStorage> {
    /**
     * 查询档口商品入库
     *
     * @param id 档口商品入库主键
     * @return 档口商品入库
     */
    public StoreProductStorage selectStoreProductStorageByStoreProdStorId(Long id);

    /**
     * 查询档口商品入库列表
     *
     * @param storeProductStorage 档口商品入库
     * @return 档口商品入库集合
     */
    public List<StoreProductStorage> selectStoreProductStorageList(StoreProductStorage storeProductStorage);

    /**
     * 新增档口商品入库
     *
     * @param storeProductStorage 档口商品入库
     * @return 结果
     */
    public int insertStoreProductStorage(StoreProductStorage storeProductStorage);

    /**
     * 修改档口商品入库
     *
     * @param storeProductStorage 档口商品入库
     * @return 结果
     */
    public int updateStoreProductStorage(StoreProductStorage storeProductStorage);

    /**
     * 删除档口商品入库
     *
     * @param id 档口商品入库主键
     * @return 结果
     */
    public int deleteStoreProductStorageByStoreProdStorId(Long id);

    /**
     * 批量删除档口商品入库
     *
     * @param storeProdStorIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoreProductStorageByStoreProdStorIds(Long[] storeProdStorIds);
}
