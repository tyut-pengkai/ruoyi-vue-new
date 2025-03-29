package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreFactory;
import com.ruoyi.xkt.mapper.StoreFactoryMapper;
import com.ruoyi.xkt.service.IStoreFactoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 档口合作工厂Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class StoreFactoryServiceImpl implements IStoreFactoryService {
    @Autowired
    private StoreFactoryMapper storeFactoryMapper;

    /**
     * 查询档口合作工厂
     *
     * @param storeFacId 档口合作工厂主键
     * @return 档口合作工厂
     */
    @Override
    @Transactional(readOnly = true)
    public StoreFactory selectStoreFactoryByStoreFacId(Long storeFacId) {
        return storeFactoryMapper.selectStoreFactoryByStoreFacId(storeFacId);
    }

    /**
     * 查询档口合作工厂列表
     *
     * @param storeFactory 档口合作工厂
     * @return 档口合作工厂
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreFactory> selectStoreFactoryList(StoreFactory storeFactory) {
        return storeFactoryMapper.selectStoreFactoryList(storeFactory);
    }

    /**
     * 新增档口合作工厂
     *
     * @param storeFactory 档口合作工厂
     * @return 结果
     */
    @Override
    @Transactional
    public int insertStoreFactory(StoreFactory storeFactory) {
        storeFactory.setCreateTime(DateUtils.getNowDate());
        return storeFactoryMapper.insertStoreFactory(storeFactory);
    }

    /**
     * 修改档口合作工厂
     *
     * @param storeFactory 档口合作工厂
     * @return 结果
     */
    @Override
    @Transactional
    public int updateStoreFactory(StoreFactory storeFactory) {
        storeFactory.setUpdateTime(DateUtils.getNowDate());
        return storeFactoryMapper.updateStoreFactory(storeFactory);
    }

    /**
     * 批量删除档口合作工厂
     *
     * @param storeFacIds 需要删除的档口合作工厂主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteStoreFactoryByStoreFacIds(Long[] storeFacIds) {
        return storeFactoryMapper.deleteStoreFactoryByStoreFacIds(storeFacIds);
    }

    /**
     * 删除档口合作工厂信息
     *
     * @param storeFacId 档口合作工厂主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteStoreFactoryByStoreFacId(Long storeFacId) {
        return storeFactoryMapper.deleteStoreFactoryByStoreFacId(storeFacId);
    }
}
