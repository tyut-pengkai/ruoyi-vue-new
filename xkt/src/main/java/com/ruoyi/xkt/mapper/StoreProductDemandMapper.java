package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreProductDemand;

import java.util.List;

/**
 * 档口商品需求单Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreProductDemandMapper extends BaseMapper<StoreProductDemand> {
    /**
     * 查询档口商品需求单
     *
     * @param id 档口商品需求单主键
     * @return 档口商品需求单
     */
    public StoreProductDemand selectStoreProductDemandByStoreProdDemandId(Long id);

    /**
     * 查询档口商品需求单列表
     *
     * @param storeProductDemand 档口商品需求单
     * @return 档口商品需求单集合
     */
    public List<StoreProductDemand> selectStoreProductDemandList(StoreProductDemand storeProductDemand);

    /**
     * 新增档口商品需求单
     *
     * @param storeProductDemand 档口商品需求单
     * @return 结果
     */
    public int insertStoreProductDemand(StoreProductDemand storeProductDemand);

    /**
     * 修改档口商品需求单
     *
     * @param storeProductDemand 档口商品需求单
     * @return 结果
     */
    public int updateStoreProductDemand(StoreProductDemand storeProductDemand);

    /**
     * 删除档口商品需求单
     *
     * @param id 档口商品需求单主键
     * @return 结果
     */
    public int deleteStoreProductDemandByStoreProdDemandId(Long id);

    /**
     * 批量删除档口商品需求单
     *
     * @param storeProdDemandIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoreProductDemandByStoreProdDemandIds(Long[] storeProdDemandIds);
}
