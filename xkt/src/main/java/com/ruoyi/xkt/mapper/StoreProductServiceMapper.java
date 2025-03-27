package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreProductService;

import java.util.List;

/**
 * 档口商品服务Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreProductServiceMapper extends BaseMapper<StoreProductService> {
    /**
     * 查询档口商品服务
     *
     * @param storeProdSvcId 档口商品服务主键
     * @return 档口商品服务
     */
    public StoreProductService selectStoreProductServiceByStoreProdSvcId(Long storeProdSvcId);

    /**
     * 查询档口商品服务列表
     *
     * @param storeProductService 档口商品服务
     * @return 档口商品服务集合
     */
    public List<StoreProductService> selectStoreProductServiceList(StoreProductService storeProductService);

    /**
     * 新增档口商品服务
     *
     * @param storeProductService 档口商品服务
     * @return 结果
     */
    public int insertStoreProductService(StoreProductService storeProductService);

    /**
     * 修改档口商品服务
     *
     * @param storeProductService 档口商品服务
     * @return 结果
     */
    public int updateStoreProductService(StoreProductService storeProductService);

    /**
     * 删除档口商品服务
     *
     * @param storeProdSvcId 档口商品服务主键
     * @return 结果
     */
    public int deleteStoreProductServiceByStoreProdSvcId(Long storeProdSvcId);

    /**
     * 批量删除档口商品服务
     *
     * @param storeProdSvcIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoreProductServiceByStoreProdSvcIds(Long[] storeProdSvcIds);
}
