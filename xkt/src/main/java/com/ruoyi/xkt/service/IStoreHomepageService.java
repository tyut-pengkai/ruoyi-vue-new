package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.StoreHomepage;

import java.util.List;

/**
 * 档口首页Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreHomepageService {
    /**
     * 查询档口首页
     *
     * @param storeHomeId 档口首页主键
     * @return 档口首页
     */
    public StoreHomepage selectStoreHomepageByStoreHomeId(Long storeHomeId);

    /**
     * 查询档口首页列表
     *
     * @param storeHomepage 档口首页
     * @return 档口首页集合
     */
    public List<StoreHomepage> selectStoreHomepageList(StoreHomepage storeHomepage);

    /**
     * 新增档口首页
     *
     * @param storeHomepage 档口首页
     * @return 结果
     */
    public int insertStoreHomepage(StoreHomepage storeHomepage);

    /**
     * 修改档口首页
     *
     * @param storeHomepage 档口首页
     * @return 结果
     */
    public int updateStoreHomepage(StoreHomepage storeHomepage);

    /**
     * 批量删除档口首页
     *
     * @param storeHomeIds 需要删除的档口首页主键集合
     * @return 结果
     */
    public int deleteStoreHomepageByStoreHomeIds(Long[] storeHomeIds);

    /**
     * 删除档口首页信息
     *
     * @param storeHomeId 档口首页主键
     * @return 结果
     */
    public int deleteStoreHomepageByStoreHomeId(Long storeHomeId);
}
