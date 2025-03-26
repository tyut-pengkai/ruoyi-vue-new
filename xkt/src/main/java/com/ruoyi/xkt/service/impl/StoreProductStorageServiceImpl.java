package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreProductStorage;
import com.ruoyi.xkt.mapper.StoreProductStorageMapper;
import com.ruoyi.xkt.service.IStoreProductStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 档口商品入库Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class StoreProductStorageServiceImpl implements IStoreProductStorageService {
    @Autowired
    private StoreProductStorageMapper storeProductStorageMapper;

    /**
     * 查询档口商品入库
     *
     * @param storeProdStorId 档口商品入库主键
     * @return 档口商品入库
     */
    @Override
    public StoreProductStorage selectStoreProductStorageByStoreProdStorId(Long storeProdStorId) {
        return storeProductStorageMapper.selectStoreProductStorageByStoreProdStorId(storeProdStorId);
    }

    /**
     * 查询档口商品入库列表
     *
     * @param storeProductStorage 档口商品入库
     * @return 档口商品入库
     */
    @Override
    public List<StoreProductStorage> selectStoreProductStorageList(StoreProductStorage storeProductStorage) {
        return storeProductStorageMapper.selectStoreProductStorageList(storeProductStorage);
    }

    /**
     * 新增档口商品入库
     *
     * @param storeProductStorage 档口商品入库
     * @return 结果
     */
    @Override
    public int insertStoreProductStorage(StoreProductStorage storeProductStorage) {
        storeProductStorage.setCreateTime(DateUtils.getNowDate());
        return storeProductStorageMapper.insertStoreProductStorage(storeProductStorage);
    }

    /**
     * 修改档口商品入库
     *
     * @param storeProductStorage 档口商品入库
     * @return 结果
     */
    @Override
    public int updateStoreProductStorage(StoreProductStorage storeProductStorage) {
        storeProductStorage.setUpdateTime(DateUtils.getNowDate());
        return storeProductStorageMapper.updateStoreProductStorage(storeProductStorage);
    }

    /**
     * 批量删除档口商品入库
     *
     * @param storeProdStorIds 需要删除的档口商品入库主键
     * @return 结果
     */
    @Override
    public int deleteStoreProductStorageByStoreProdStorIds(Long[] storeProdStorIds) {
        return storeProductStorageMapper.deleteStoreProductStorageByStoreProdStorIds(storeProdStorIds);
    }

    /**
     * 删除档口商品入库信息
     *
     * @param storeProdStorId 档口商品入库主键
     * @return 结果
     */
    @Override
    public int deleteStoreProductStorageByStoreProdStorId(Long storeProdStorId) {
        return storeProductStorageMapper.deleteStoreProductStorageByStoreProdStorId(storeProdStorId);
    }
}
