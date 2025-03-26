package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreHomepage;
import com.ruoyi.xkt.mapper.StoreHomepageMapper;
import com.ruoyi.xkt.service.IStoreHomepageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 档口首页Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class StoreHomepageServiceImpl implements IStoreHomepageService {
    @Autowired
    private StoreHomepageMapper storeHomepageMapper;

    /**
     * 查询档口首页
     *
     * @param storeHomeId 档口首页主键
     * @return 档口首页
     */
    @Override
    public StoreHomepage selectStoreHomepageByStoreHomeId(Long storeHomeId) {
        return storeHomepageMapper.selectStoreHomepageByStoreHomeId(storeHomeId);
    }

    /**
     * 查询档口首页列表
     *
     * @param storeHomepage 档口首页
     * @return 档口首页
     */
    @Override
    public List<StoreHomepage> selectStoreHomepageList(StoreHomepage storeHomepage) {
        return storeHomepageMapper.selectStoreHomepageList(storeHomepage);
    }

    /**
     * 新增档口首页
     *
     * @param storeHomepage 档口首页
     * @return 结果
     */
    @Override
    public int insertStoreHomepage(StoreHomepage storeHomepage) {
        storeHomepage.setCreateTime(DateUtils.getNowDate());
        return storeHomepageMapper.insertStoreHomepage(storeHomepage);
    }

    /**
     * 修改档口首页
     *
     * @param storeHomepage 档口首页
     * @return 结果
     */
    @Override
    public int updateStoreHomepage(StoreHomepage storeHomepage) {
        storeHomepage.setUpdateTime(DateUtils.getNowDate());
        return storeHomepageMapper.updateStoreHomepage(storeHomepage);
    }

    /**
     * 批量删除档口首页
     *
     * @param storeHomeIds 需要删除的档口首页主键
     * @return 结果
     */
    @Override
    public int deleteStoreHomepageByStoreHomeIds(Long[] storeHomeIds) {
        return storeHomepageMapper.deleteStoreHomepageByStoreHomeIds(storeHomeIds);
    }

    /**
     * 删除档口首页信息
     *
     * @param storeHomeId 档口首页主键
     * @return 结果
     */
    @Override
    public int deleteStoreHomepageByStoreHomeId(Long storeHomeId) {
        return storeHomepageMapper.deleteStoreHomepageByStoreHomeId(storeHomeId);
    }
}
