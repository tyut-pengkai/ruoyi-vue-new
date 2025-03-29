package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.Store;
import com.ruoyi.xkt.mapper.StoreMapper;
import com.ruoyi.xkt.service.IStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 档口Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class StoreServiceImpl implements IStoreService {
    @Autowired
    private StoreMapper storeMapper;

    /**
     * 查询档口
     *
     * @param storeId 档口主键
     * @return 档口
     */
    @Override
    @Transactional(readOnly = true)
    public Store selectStoreByStoreId(Long storeId) {
        return storeMapper.selectStoreByStoreId(storeId);
    }

    /**
     * 查询档口列表
     *
     * @param store 档口
     * @return 档口
     */
    @Override
    @Transactional(readOnly = true)
    public List<Store> selectStoreList(Store store) {
        return storeMapper.selectStoreList(store);
    }

    /**
     * 新增档口
     *
     * @param store 档口
     * @return 结果
     */
    @Override
    public int insertStore(Store store) {
        store.setCreateTime(DateUtils.getNowDate());
        return storeMapper.insertStore(store);
    }

    /**
     * 修改档口
     *
     * @param store 档口
     * @return 结果
     */
    @Override
    public int updateStore(Store store) {
        store.setUpdateTime(DateUtils.getNowDate());
        return storeMapper.updateStore(store);
    }

    /**
     * 批量删除档口
     *
     * @param storeIds 需要删除的档口主键
     * @return 结果
     */
    @Override
    public int deleteStoreByStoreIds(Long[] storeIds) {
        return storeMapper.deleteStoreByStoreIds(storeIds);
    }

    /**
     * 删除档口信息
     *
     * @param storeId 档口主键
     * @return 结果
     */
    @Override
    public int deleteStoreByStoreId(Long storeId) {
        return storeMapper.deleteStoreByStoreId(storeId);
    }
}
