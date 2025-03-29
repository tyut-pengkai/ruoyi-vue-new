package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreHomepage;

import java.util.List;

/**
 * 档口首页Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreHomepageMapper extends BaseMapper<StoreHomepage> {
    /**
     * 查询档口首页
     *
     * @param id 档口首页主键
     * @return 档口首页
     */
    public StoreHomepage selectStoreHomepageByStoreHomeId(Long id);

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
     * 删除档口首页
     *
     * @param id 档口首页主键
     * @return 结果
     */
    public int deleteStoreHomepageByStoreHomeId(Long id);

    /**
     * 批量删除档口首页
     *
     * @param storeHomeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoreHomepageByStoreHomeIds(Long[] storeHomeIds);
}
